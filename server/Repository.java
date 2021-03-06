package server;

import Model.Chat;
import Model.Post;
import Model.User;

import java.util.*;
import java.util.stream.Collectors;

public class Repository {
    private static Vector<Post> posts = new Vector<>();
    private static Vector<User> users = new Vector<>();
    private static Vector<Chat> chats = new Vector<>();

    public static void addPost(Post post) {
        posts.add(post);
    }
    public static void addUser(User user) {
        users.add(user);
    }

    public static List<Post> getFollowingPosts(User responder) {
        return posts.stream()
                .filter(post -> (post.getPublisher().equals(responder)) || (responder.getFollowings().contains(post.getPublisher()) &&
                        !responder.isMute(post.getPublisher()) &&
                        !post.getPublisher().isBlock(responder) &&
                        !responder.isBlock(post.getPublisher())))
                .sorted((post1,post2)->-post1.getPublishDate().compareTo(post2.getPublishDate()))
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
    public static User getUserByUsername(String username){
        for(User user : users)
            if (user.getID().equals(username))
                return user;
        return null;
    }
    public static boolean userExists(User thisUser) {
        return exists(users,thisUser);
    }
    public static boolean postExists(Post thisPost) {
        return exists(posts,thisPost);
    }
    public static void setUsers(ArrayList<User> usersToAdd){
        users.addAll(usersToAdd);
    }
    public static void setPosts(ArrayList<Post> postsToAdd){
        posts.addAll(postsToAdd);
    }
    public static void setChats(ArrayList<Chat> chatsToAdd){
        chats.addAll(chatsToAdd);
    }
    public static ArrayList<User> getUsers() {
        return new ArrayList<>(users);
    }
    public static ArrayList<Post> getPosts(){
        return new ArrayList<>(posts);
    }
    public static ArrayList<Chat> getChats(){
        return new ArrayList<>(chats);
    }
    public static ArrayList<Post> getFollowingPosts(String username){
        User user = getUserByUsername(username);
        ArrayList<Post> output = new ArrayList<>();
        if (user == null) return output;
        return (ArrayList<Post>) getFollowingPosts(user);
    }

    public static Post getPostById(String id) {
        for (Post post : posts){
            if (id.equals(post.getId()+""))
                return post;
        }
        return null;
    }
    public static void replace(User user){
        for (User tempUser: users){
            if (user.getID().equals(tempUser.getID())){
                tempUser.setName(user.getName());
                tempUser.setPassword(user.getPassword());
                tempUser.setProfileImage(user.getProfileImage());
                tempUser.setJob(user.getJob());
                tempUser.setAddress(user.getAddress());
                tempUser.setBirthday(user.getBirthday());
            }
        }
    }
    public static ArrayList<Post> getUserPosts(String responder) {
        return (ArrayList<Post>) posts.stream()
                .filter(post -> post.getPublisher().getID().contains(responder))
                .sorted((post1,post2)->-post1.getPublishDate().compareTo(post2.getPublishDate()))
                .collect(Collectors.toList());
    }

    public static void delete(String username) {
        User toDeleteUser = getUserByUsername(username);
        for (User user : users){
            user.getFollowings().remove(toDeleteUser);
            user.getFollowers().remove(toDeleteUser);
        }
        users.remove(toDeleteUser);
    }

    public static ArrayList<Chat> collectChats (User user){
        return (ArrayList<Chat>) chats.stream().filter(chat -> chat.hasPV(user)).collect(Collectors.toList());
    }
    public static Chat createChatIfNotExist(String username1, String username2){
        User user1 = getUserByUsername(username1);
        User user2 = getUserByUsername(username2);
        for (Chat chat : chats)
            if (chat.hasPV(user1) && chat.hasPV(user2))
                return chat;
        Chat newChat = new Chat(user1,user2);
        chats.add(newChat);
        return newChat;
    }
    public static Chat getChatById(String id){
        for (Chat chat : chats){
            if (chat.getId().equals(id))
                return chat;
        }
        return null;
    }
}
