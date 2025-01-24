public class Post {
    private String postId;  // Unique identifier for the post
    private String content;  // Text content of the post
    private int likeCount;  // Number of likes this post has received
    private User author;  // The user who authored this post
    private CustomHashSet<User> likedBy; // Set of users who have liked this post

    // Constructor to initialize the Post object with its ID, content, and author
    public Post(String postId, String content, User author) {
        this.postId = postId;
        this.content = content;
        this.likeCount = 0; // Initialize like count to zero
        this.author = author;
        this.likedBy = new CustomHashSet<>();
    }

    // Getter for post ID
    public String getPostId() {
        return postId;
    }

    // Getter for post content
    public String getContent() {
        return content;
    }

    // Getter for the count of likes
    public int getLikeCount() {
        return likeCount;
    }

    // Getter for the author of the post
    public User getAuthor() {
        return author;
    }

    // Method to handle liking a post
    public void likePost(User user) {
        // Ensure the post is marked as seen by the user when it is liked
        user.seePost(this);

        // If this user has not previously liked this post, increase the like count
        if (likedBy.add(user)) {
            likeCount++;
        }
    }

    // Method to handle unliking a post
    public void unlikePost(User user) {
        // If this user has liked this post, decrease the like count upon unliking
        if (likedBy.remove(user)) {
            likeCount--;
        }
    }

    // Returns the set of users who have liked this post
    public CustomHashSet<User> getLikedBy() {
        return likedBy;
    }

    // Returns the username of the author of the post
    public String getAuthorUsername() {
        return author.getUserId(); // This method assumes User class has a getUserId() method
    }
}
