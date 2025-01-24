public class User {
    private String userId; // Unique identifier for the user
    private CustomHashSet<User> followers;  // Set containing users who follow this user
    private CustomHashSet<User> following;  // Set containing users this user is following
    private CustomHashSet<Post> posts;      // Set containing posts made by this user
    private CustomHashSet<Post> seenPosts;  // Set containing posts this user has seen
    private CustomHashSet<Post> likedPosts;  // Set containing posts this user has liked

    // Constructor initializes the user ID and the sets for managing relationships and content
    public User(String userId) {
        this.userId = userId;
        this.followers = new CustomHashSet<>();
        this.following = new CustomHashSet<>();
        this.posts = new CustomHashSet<>();
        this.seenPosts = new CustomHashSet<>();
        this.likedPosts = new CustomHashSet<>();
    }

    // Adds a follower to this user, ensuring the follower is not null and not the user itself
    public void addFollower(User follower) {
        if (follower != null && !this.equals(follower)) {
            followers.add(follower);
        }
    }

    // Follows another user, ensuring not to follow oneself
    public void follow(User user) {
        if (user != null && !this.equals(user)) {
            following.add(user);
            user.addFollower(this);
        }
    }

    // Unfollows another user, if they are currently being followed
    public void unfollow(User user) {
        if (user != null && following.contains(user)) {
            following.remove(user);
            user.followers.remove(this);
        }
    }

    // Adds a post to this user's set of posts
    public void addPost(Post post) {
        if (post != null) {
            posts.add(post);
        }
    }

    // Marks a post as seen by this user
    public void seePost(Post post) {
        if (post != null) {
            seenPosts.add(post);
        }
    }

    // Returns the user ID
    public String getUserId() {
        return userId;
    }

    // Returns the set of followers
    public CustomHashSet<User> getFollowers() {
        return followers;
    }

    // Returns the set of users this user is following
    public CustomHashSet<User> getFollowing() {
        return following;
    }

    // Returns the set of posts created by this user
    public CustomHashSet<Post> getPosts() {
        return posts;
    }

    // Returns the set of posts that have been seen by this user
    public CustomHashSet<Post> getSeenPosts() {
        return seenPosts;
    }

    // Returns the set of posts that have been liked by this user
    public CustomHashSet<Post> getLikedPosts() {
        return likedPosts;
    }
}
