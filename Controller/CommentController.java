package Controller;

import Model.*;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommentController {
    public Circle prof;
    public ImageView img_post;
    public Label txt_name;
    public Label txt_title;
    public Label txt_text;
    public Label txt_date;
    public JFXTextArea txt_comment;
    ArrayList<Comment> comments;
    public ListView<Comment> commentList;
    @FXML
    public void initialize() throws IOException, ClassNotFoundException {

        comments = CommentModel.post.getComments();
        comments.sort(Comment::compareTo);
        //initialize posts array list to be shown in list view
        if (CommentModel.post.getPublisher().getProfileImage() != null)
            prof.setFill(new ImagePattern(new Image(new ByteArrayInputStream(CommentModel.post.getPublisher().getProfileImage()))));
        txt_title.setText(CommentModel.post.getTitle());
        txt_name.setText(CommentModel.post.getReferencePost().getPublisher().getName());
        txt_text.setText(CommentModel.post.getText());
        txt_date.setText(CommentModel.post.getPublishDate().toString());
        if (CommentModel.post.getImage() != null)
        img_post.setImage(new Image(new ByteArrayInputStream(CommentModel.post.getImage())));
        //show the post array in list view
        commentList.setItems(FXCollections.observableArrayList(comments));

        //customize each cell of postList with new graphic object PostItem
        commentList.setCellFactory(commentListView -> new CommentItem());
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        new PageLoader().load("timeline_page");
    }

    public void publish(MouseEvent mouseEvent) throws IOException{
        Comment comment = new Comment(Properties.user,txt_comment.getText());
        CommentModel.publishComment(comment);
        comments.add(0,comment);
        commentList.setItems(FXCollections.observableArrayList(comments));

        //customize each cell of postList with new graphic object PostItem
        commentList.setCellFactory(commentListView -> new CommentItem());

    }
}
