package Controller;

import Model.Post;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import server.Repository;

import java.util.ArrayList;

public class TimelinePage {


    public ListView<Post> postList;

    ArrayList<Post> posts = new ArrayList<>(Repository.posts);
    Post currentPost = new Post("", "", null, "");

    @FXML
    public void initialize() {
        postList.setFocusTraversable(false);
        //initialize posts array list to be shown in list view

        //show the post array in list view
        postList.setItems(FXCollections.observableArrayList(posts));

        //customize each cell of postList with new graphic object PostItem
        postList.setCellFactory(postList -> new PostItem());
    }


    public void onClick(MouseEvent mouseEvent) {
    }
}
