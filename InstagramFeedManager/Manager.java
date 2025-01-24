public class Manager {
    private CustomHashMap<String, User> users; // Hash map to store users by their IDs
    private CustomHashMap<String, Post> posts; // Hash map to store posts by their IDs

    // Constructor initializes the hash maps for users and posts
    public Manager() {
        users = new CustomHashMap<>();
        posts = new CustomHashMap<>();
    }

    // Creates and adds a new user to the hash map if not already present
    public String createUser(String userId) {
        if (users.containsKey(userId)) {
            return "Some error occurred in create_user."; // Return error if user already exists
        } else {
            users.put(userId, new User(userId)); // Add new user to the map
            return "Created user with Id " + userId + "."; // Success message
        }
    }

    // Allows one user to follow another if both exist and are not the same
    public String followUser(String userId1, String userId2) {
        if (!users.containsKey(userId1) || !users.containsKey(userId2) || userId1.equals(userId2)) {
            return "Some error occurred in follow_user."; // Check existence and prevent self-follow
        } else {
            User user1 = users.get(userId1);
            User user2 = users.get(userId2);
            if (!user1.getFollowing().contains(user2)) {
                user1.follow(user2); // Perform follow operation
                return userId1 + " followed " + userId2 + ".";
            } else {
                return "Some error occurred in follow_user."; // Error if already following
            }
        }
    }

    // Allows a user to unfollow another user, given both exist and are not the same
    public String unfollowUser(String userId1, String userId2) {
        if (!users.containsKey(userId1) || !users.containsKey(userId2) || userId1.equals(userId2)) {
            return "Some error occurred in unfollow_user."; // Validate existence and prevent self-unfollow
        } else {
            User user1 = users.get(userId1);
            User user2 = users.get(userId2);
            if (user1.getFollowing().contains(user2)) {
                user1.unfollow(user2); // Perform unfollow operation
                return userId1 + " unfollowed " + userId2 + ".";
            } else {
                return "Some error occurred in unfollow_user."; // Error if not currently following
            }
        }
    }

    // Creates a post for a user if the user exists and the post ID is unique
    public String createPost(String userId, String postId, String content) {
        if (!users.containsKey(userId)) {
            return "Some error occurred in create_post."; // Check user existence
        }
        if (posts.containsKey(postId)) {
            return "Some error occurred in create_post."; // Ensure post ID uniqueness
        }
        User user = users.get(userId);
        Post newPost = new Post(postId, content, user);
        posts.put(postId, newPost); // Add post to the map
        user.addPost(newPost); // Add post to user's list of posts
        return userId + " created a post with Id " + postId + ".";
    }

    // Marks a post as seen by a user, assuming both the user and the post exist
    public String seePost(String userId, String postId) {
        if (!users.containsKey(userId) || !posts.containsKey(postId)) {
            return "Some error occurred in see_post."; // Validate user and post existence
        }
        User user = users.get(userId);
        Post post = posts.get(postId);
        user.seePost(post); // Mark the post as seen
        return userId + " saw " + postId + ".";
    }

    // Shows all posts from one user as seen by another user, given both users exist
    public String seeAllPostsFromUser(String viewerId, String viewedId) {
        if (!users.containsKey(viewerId) || !users.containsKey(viewedId)) {
            return "Some error occurred in see_all_posts_from_user."; // Check both users exist
        }
        User viewer = users.get(viewerId);
        User viewed = users.get(viewedId);
        CustomHashSet<Post> posts = viewed.getPosts();

        for (Post post : posts) {
            viewer.seePost(post); // Mark each post as seen by the viewer
        }

        return viewerId + " saw all posts of " + viewedId + ".";
    }

    // Toggles a like on a post by a user, assuming both the user and the post exist
    public String toggleLike(String userId, String postId) {
        if (!users.containsKey(userId) || !posts.containsKey(postId)) {
            return "Some error occurred in toggle_like."; // Check user and post existence
        }

        User user = users.get(userId);
        Post post = posts.get(postId);

        if (post.getLikedBy().contains(user)) {
            post.unlikePost(user); // Unlike the post if already liked
            return userId + " unliked " + postId + ".";
        } else {
            post.likePost(user); // Like the post if not already liked
            return userId + " liked " + postId + ".";
        }
    }

    // Generates a user-specific feed, filtering out seen and user's own posts
    public String generateFeed(String userId, int num) {
        if (!users.containsKey(userId)) {
            return "Some error occurred in generate_feed."; // Check user existence
        }

        User user = users.get(userId);
        CustomHashSet<Post> seenPosts = user.getSeenPosts();
        CustomHashSet<Post> userPosts = user.getPosts();

        CustomPriorityQueue feedQueue = new CustomPriorityQueue();

        for (User followedUser : user.getFollowing()) {
            for (Post post : followedUser.getPosts()) {
                if (!seenPosts.contains(post) && !userPosts.contains(post)) {
                    feedQueue.add(post); // Add eligible posts to the feed
                }
            }
        }

        StringBuilder log = new StringBuilder("Feed for " + userId + ":\n");
        int count = 0;
        while (!feedQueue.isEmpty() && count < num) {
            Post post = feedQueue.poll(); // Poll posts based on priority
            log.append("Post ID: ").append(post.getPostId())
                    .append(", Author: ").append(post.getAuthorUsername())
                    .append(", Likes: ").append(post.getLikeCount()).append("\n");
            count++;
        }

        if (count < num) {
            log.append("No more posts available for ").append(userId).append(".");
        }

        // Removing any unnecessary trailing newline
        if (log.length() > 0 && log.charAt(log.length() - 1) == '\n') {
            log.deleteCharAt(log.length() - 1);
        }

        return log.toString();
    }

    // Scrolls through a user's feed and handles likes specified by the user
    public String scrollThroughFeed(String userId, int num, int[] likes) {
        if (!users.containsKey(userId)) {
            return "Some error occurred in scroll_through_feed."; // Error if the user does not exist
        }

        User user = users.get(userId);
        CustomHashSet<User> following = user.getFollowing(); // Get the list of users this user is following
        CustomHashSet<Post> seenPosts = user.getSeenPosts(); // Get the list of posts this user has seen
        CustomHashSet<Post> userPosts = user.getPosts(); // Get the user's own posts

        CustomPriorityQueue postQueue = new CustomPriorityQueue(); // Priority queue for managing the feed

        // Populate the priority queue with eligible posts from followed users
        for (User followedUser : following) {
            for (Post post : followedUser.getPosts()) {
                if (!seenPosts.contains(post) && !userPosts.contains(post)) { // Ensure the post is neither seen nor owned by the user
                    postQueue.add(post); // Add the post to the queue
                }
            }
        }

        StringBuilder log = new StringBuilder(userId + " is scrolling through feed:\n");
        int count = 0;

        // Process each post in the feed up to the specified number
        while (!postQueue.isEmpty() && count < num) {
            Post post = postQueue.poll(); // Get the next highest priority post
            user.seePost(post); // Mark the post as seen
            log.append(userId).append(" saw ").append(post.getPostId()).append(" while scrolling");

            if (likes[count] == 1) { // Check if the user liked this particular post
                post.likePost(user); // Like the post
                log.append(" and clicked the like button");
            }

            count++;
            log.append(".\n");
        }

        if (count < num) {
            log.append("No more posts in feed."); // Inform the user if there are not enough posts to satisfy the requested number
        } else if (log.toString().endsWith("\n")) {
            log.setLength(log.length() - 1); // Remove any unnecessary trailing newline
        }

        return log.toString();
    }

    // Sorts the posts of a user by like count and post ID and returns them in a formatted string
    public String sortPosts(String userId) {
        if (!users.containsKey(userId)) {
            return "Some error occurred in sort_posts."; // Error if the user does not exist
        }

        User user = users.get(userId);
        if (user.getPosts().isEmpty()) {
            return "No posts from " + userId + "."; // Check if the user has no posts
        }

        CustomPriorityQueue postQueue = new CustomPriorityQueue(); // Priority queue to sort the posts
        for (Post post : user.getPosts()) {
            postQueue.add(post); // Add each post to the queue
        }

        StringBuilder log = new StringBuilder("Sorting " + userId + "'s posts:\n");
        boolean isFirst = true; // Control for formatting output without extra newline at the end
        while (!postQueue.isEmpty()) {
            if (!isFirst) { // Add newline before each post except the first
                log.append("\n");
            }
            Post post = postQueue.poll(); // Poll the highest priority post based on like count and ID
            log.append(post.getPostId()).append(", Likes: ").append(post.getLikeCount());
            isFirst = false; // After the first post, all others should be prefixed with a newline
        }

        return log.toString();
    }
}


