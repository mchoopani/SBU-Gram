package server;

import Model.Post;
import Model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class Repository {
//    private static ArrayList<Post> posts = new ArrayList<>();
    private static Vector<Post> posts = new Vector<>();
    private static Vector<User> users = new Vector<>();

    public static void addPost(Post post) {
        posts.add(post);
    }
    public static void addUser(User user) {
        users.add(user);
    }

    public static List<Post> getFollowingPosts(User responder) {
        return posts.stream()
                .filter(post -> responder.getFollowings().contains(post.getPublisher()))
                .sorted(Comparator.comparing(Post::getPublishDate))
                .collect(Collectors.toList());
    }

    public static boolean userExists(String username, String password) {
        for (User user : users) {
            if (user.getID().equals(username))
                if (user.getPassword().equals(password))
                    return true;
        }
        return false;
    }
    public static <T> boolean exists(Vector<T> arr, T t){
        for (T u : arr)
            if (t.equals(u))
                return true;
        return false;
    }
    public static boolean userExists(User thisUser) {
        return exists(users,thisUser);
    }
    public static boolean postExists(Post thisPost) {
        return exists(posts,thisPost);
    }
    public static ArrayList<User> getUsers() {
        return new ArrayList<>(users);
    }
}
