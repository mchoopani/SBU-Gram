package Controller;

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
        Properties.user = null;
        TimelineModel.disconnect();
        new PageLoader().load("login_page");
    }

    @FXML
    void newPost(ActionEvent event) {

    }

    @FXML
    void openPV(ActionEvent event) {

    }

    @FXML
    void openProfile(ActionEvent event) {

    }
}
