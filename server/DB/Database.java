package server.DB;

import Model.Chat;
import Model.Post;
import Model.User;
import server.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class Database {
    private final static Database database = new Database();
    private final String POSTS_DB = "C:\\Users\\Macho\\Desktop\\DB\\Posts.bin";
    private final String USERS_DB = "C:\\Users\\Macho\\Desktop\\DB\\Users.bin";
    private final String CHATS_DB = "C:\\Users\\Macho\\Desktop\\DB\\Chats.bin";
    private final String LOG = "C:\\Users\\Macho\\Desktop\\DB\\Log.txt";

    private Database() {
    }

    public static Database getInstance() {
        return database;
    }

    public void updateData() throws IOException {
        try {
            FileWriter fileWriter = new FileWriter(POSTS_DB);
            FileOutputStream fileOutputStream = new FileOutputStream(POSTS_DB);
            ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);
            ArrayList<Post> postsToSave = Repository.getPosts();
            oos.writeObject(postsToSave);
            oos.flush();
            fileOutputStream.close();
            oos.close();
            fileWriter.close();

            fileWriter = new FileWriter(USERS_DB);
            fileOutputStream = new FileOutputStream(USERS_DB);
            oos = new ObjectOutputStream(fileOutputStream);
            ArrayList<User> usersToSave = Repository.getUsers();
            oos.writeObject(usersToSave);
            oos.flush();
            fileOutputStream.close();
            oos.close();
            fileWriter.close();

            fileWriter = new FileWriter(CHATS_DB);
            fileOutputStream = new FileOutputStream(CHATS_DB);
            oos = new ObjectOutputStream(fileOutputStream);
            ArrayList<Chat> chatsToSave = Repository.getChats();
            oos.writeObject(chatsToSave);
            oos.flush();
            fileOutputStream.close();
            oos.close();
            fileWriter.close();

            FileWriter log = new FileWriter(LOG, true);
            log.write("\n" + "Updated Data at : " + new Date().toString());
            log.close();
            System.out.println("Database Updated Data Successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("There Is A Problem In Updating Database.");
        }

    }

    public void loadData() {
        try {
            FileInputStream fileInputStream = new FileInputStream(POSTS_DB);
            ObjectInputStream ois = new ObjectInputStream(fileInputStream);
            ArrayList<Post> postsToLoad = (ArrayList<Post>) ois.readObject();
            Repository.setPosts(postsToLoad);
            fileInputStream.close();
            ois.close();
        } catch (EOFException e) {
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("There Is A Problem In Loading Database.");
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(USERS_DB);
            ObjectInputStream ois = new ObjectInputStream(fileInputStream);
            ArrayList<User> usersToLoad = (ArrayList<User>) ois.readObject();
            Repository.setUsers(usersToLoad);
            fileInputStream.close();
            ois.close();
        } catch (EOFException e) {
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("There Is A Problem In Loading Database.");
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(CHATS_DB);
            ObjectInputStream ois = new ObjectInputStream(fileInputStream);
            ArrayList<Chat> chatsToLoad = (ArrayList<Chat>) ois.readObject();
            Repository.setChats(chatsToLoad);
            fileInputStream.close();
            ois.close();

        }catch (EOFException e){}
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("There Is A Problem In Loading Database.");
        }

        try {
            FileWriter log = new FileWriter(LOG, true);
            log.write("\n" + "Loaded Data at : " + new Date().toString());
            log.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("There Is A Problem In Logging.");
        }

    }
}
