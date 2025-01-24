import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) { // Check if the required arguments are provided
            System.err.println("Usage: java Main <input file path> <output file path>");
            return; // Exit if not enough arguments are provided
        }
        String inputFile = args[0];  // Use the first command line argument as the input file path
        String outputFile = args[1];  // Use the second command line argument as the output file path
        Manager manager = new Manager(); // Initialize the manager

        try (Scanner scanner = new Scanner(new File(inputFile));
             PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {

            while (scanner.hasNextLine()) {
                String commandLine = scanner.nextLine().trim();  // Read and trim the next line from the input file
                String[] parts = commandLine.split(" ", 4);  // Split the command line into parts
                String result = "";  // Variable to store the result of command execution

                if (parts.length > 0) {
                    String command = parts[0];  // Get the command part
                    switch (command) {  // Process the command
                        case "create_user":
                            result = parts.length == 2 ? manager.createUser(parts[1]) : "Invalid command format for create_user.";
                            break;
                        case "follow_user":
                            result = parts.length == 3 ? manager.followUser(parts[1], parts[2]) : "Invalid command format for follow_user.";
                            break;
                        case "unfollow_user":
                            result = parts.length == 3 ? manager.unfollowUser(parts[1], parts[2]) : "Invalid command format for unfollow_user.";
                            break;
                        case "create_post":
                            if (parts.length == 4) {
                                result = manager.createPost(parts[1], parts[2], parts[3]);
                            } else {
                                result = "Invalid command format for create_post.";
                            }
                            break;
                        case "see_post":
                            result = parts.length == 3 ? manager.seePost(parts[1], parts[2]) : "Invalid command format for see_post.";
                            break;
                        case "see_all_posts_from_user":
                            result = parts.length == 3 ? manager.seeAllPostsFromUser(parts[1], parts[2]) : "Invalid command format for see_all_posts_from_user.";
                            break;
                        case "toggle_like":
                            result = parts.length == 3 ? manager.toggleLike(parts[1], parts[2]) : "Invalid command format for toggle_like.";
                            break;
                        case "generate_feed":
                            if (parts.length == 3) {
                                String userId = parts[1];
                                int numPosts;
                                try {
                                    numPosts = Integer.parseInt(parts[2]);  // Try to parse the number of posts
                                    result = manager.generateFeed(userId, numPosts);
                                } catch (NumberFormatException e) {
                                    result = "Invalid number format for numPosts.";
                                }
                            } else {
                                result = "Invalid command format for generate_feed.";
                            }
                            break;
                        case "scroll_through_feed":
                            try (Scanner commandScanner = new Scanner(commandLine)) {  // Create a new scanner for command processing
                                commandScanner.next();  // Skip the command itself
                                String userId = commandScanner.next();
                                int num = commandScanner.nextInt();
                                int[] likes = new int[num];
                                int count = 0;
                                while (commandScanner.hasNext() && count < num) {
                                    likes[count] = commandScanner.nextInt();  // Read each like value
                                    count++;
                                }
                                result = manager.scrollThroughFeed(userId, num, likes);
                            } catch (Exception e) {
                                result = "Invalid command format for scroll_through_feed.";
                            }
                            break;
                        case "sort_posts":
                            if (parts.length == 2) {
                                result = manager.sortPosts(parts[1]);
                            } else {
                                result ="Invalid command format for sort_posts.";
                            }
                            break;
                        default:
                            result = "Unknown command: " + command;  // Handle unknown commands
                            break;
                    }
                } else {
                    result = "Empty command line.";  // Handle empty command lines
                }

                writer.println(result);  // Write the result to the output file
            }

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());  // Log any exceptions that occur
        }
    }
}
