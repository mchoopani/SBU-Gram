package Controller;

import Model.PageLoader;
import Model.Post;
import Model.TimelineModel;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.List;

public class TimelinePage {


    public ListView<Post> postList;

    List<Post> posts;

    @FXML
    public void initialize() throws IOException, ClassNotFoundException {
        Properties.isInProfilePage = false;
        posts = (List<Post>) TimelineModel.connect().nodes.get(0);

        //initialize posts array list to be shown in list view

        //show the post array in list view
        postList.setItems(FXCollections.observableArrayList(posts));

        //customize each cell of postList with new graphic object PostItem
        postList.setCellFactory(postList -> new PostItem());
    }


    public void onClick(MouseEvent mouseEvent) {
    }

    public void menu(MouseEvent mouseEvent) throws IOException {
        new PageLoader().load("menu_page");
    }

    public void refresh(MouseEvent mouseEvent) throws IOException {
        TimelineModel.disconnect();
        new PageLoader().load("timeline_page");
    }
}
