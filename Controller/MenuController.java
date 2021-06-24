package Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Model.PageLoader;
import Model.TimelineModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class MenuController {


    @FXML
    void closeMenu(MouseEvent event) throws IOException {
        new PageLoader().load("timeline_page");
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        TimelineModel.logout();
        new FileOutputStream("D:\\College\\AP\\SBU Gram\\src\\Temporary\\login_data.bin").close();
        new PageLoader().load("login_page");
        Properties.user = null;
    }

    @FXML
    void newPost(ActionEvent event) throws IOException {
        TimelineModel.disconnect();
        new PageLoader().load("addpost_page");
    }

    @FXML
    void openPV(ActionEvent event) throws IOException {
        new PageLoader().load("chat_page");
        TimelineModel.disconnect();
    }

    @FXML
    void openProfile(ActionEvent event) throws IOException {
        TimelineModel.disconnect();
        Properties.profile = Properties.user;
        new PageLoader().load("profile_page");
        Properties.lastPage = "menu_page";
    }

    public void findPage(ActionEvent event) throws IOException {
        TimelineModel.disconnect();
        new PageLoader().load("search_page");
    }
}
