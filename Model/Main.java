package Model;

import Model.PageLoader;
import javafx.application.Application;
import javafx.stage.Stage;
import Model.User;
import server.Repository;
import server.Server;


public class Main extends Application {
    public void call(){
        Repository.getUsers().add(new User("1001","Ali","1234"));
        Repository.getUsers().add(new User("1002","Hasan","1234"));
        Repository.getUsers().add(new User("1003","Shahrzad","1234"));
        Repository.getUsers().add(new User("1004","Reza","1234"));
        Repository.getUsers().add(new User("1005","Mahmood","1234"));
        Repository.getUsers().add(new User("1006","Goli","1234"));
        Repository.getUsers().add(new User("1007","1234","1234"));
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        new Server().startUpServer();
        call();
        PageLoader.initStage(primaryStage);
        new PageLoader().load("login_page");
    }


    public static void main(String[] args)
    {
        launch(args);




    }
}
