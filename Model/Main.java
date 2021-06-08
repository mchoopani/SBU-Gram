package Model;

import Controller.Properties;
import javafx.application.Application;
import javafx.stage.Stage;
import server.Repository;
import server.Server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Main extends Application {
    public void call() throws IOException {
        User u1 = new User("1001", "Ali", "1234", "");
        u1.setProfileImage(new FileInputStream("D:\\New folder\\10.jpg").readAllBytes());
        User u2 = new User("1002", "Hasan", "1234", "");
        u2.setProfileImage(new FileInputStream("D:\\New folder\\11.jpg").readAllBytes());
        User u0 = new User("1003", "Shahrzad", "1234", "");
        u0.setProfileImage(new FileInputStream("D:\\New folder\\12.jpg").readAllBytes());
        User u3 = new User("1004", "Reza", "1234", "");
        u3.setProfileImage(new FileInputStream("D:\\New folder\\13.jpg").readAllBytes());
        User u5 = new User("1005", "Mahmood", "1234", "");
        u5.setProfileImage(new FileInputStream("D:\\New folder\\14.jpg").readAllBytes());
        u5.addFollowing(u1);
        User u4 = new User("1006", "Goli", "1234", "");
        u4.setProfileImage(new FileInputStream("D:\\New folder\\23.jpg").readAllBytes());
        u4.addFollowing(u4);
        User u7 = new User("1007", "1234", "1234", "");
        u7.setProfileImage(new FileInputStream("D:\\New folder\\24.jpg").readAllBytes());
        u7.addFollowing(u1);
        u7.addFollowing(u5);
        u1.follow(u5);
        Repository.addUser(u0);
        Repository.addUser(u1);
        Repository.addUser(u2);
        Repository.addUser(u3);
        Repository.addUser(u4);
        Repository.addUser(u5);
        Repository.addUser(u7);
        Post p1 = new Post("T1", "Ali1", Repository.getUserByUsername("1001"));
        p1.setImage(new FileInputStream("D:\\New folder\\1.jpeg").readAllBytes());
        p1.addComment(new Comment(u0,"Ridi ke"));
        p1.addComment(new Comment(u0,"Madar be khata"));
        Post p2 = new Post("T2", "Ali2", Repository.getUserByUsername("1001"));
        p2.setImage(new FileInputStream("D:\\New folder\\4.jpeg").readAllBytes());
        Post p3 = new Post("T3", "Mahmood", Repository.getUserByUsername("1005"));
        p3.setImage(new FileInputStream("D:\\New folder\\3.jpeg").readAllBytes());
        Repository.addPost(p1);
        Repository.addPost(p2);
        Repository.addPost(p3);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        new Server().startUpServer();
        call();
        PageLoader.initStage(primaryStage);
        boolean hasLogin = LoginModel.checkLoggedIn();
        if (hasLogin)
            new PageLoader().load("timeline_page");
        else
            new PageLoader().load("login_page");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
