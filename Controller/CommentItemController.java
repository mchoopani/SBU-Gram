package Controller;

import Model.Comment;
import Model.PageLoader;
import Model.Post;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class CommentItemController {
    public VBox root;
    public Comment comment;
    public Label txt_username;
    public Label txt_name;
    public Label txt_comment;
    public Circle img_prof;

    public CommentItemController(Comment comment) throws IOException {
        new PageLoader().load("commentitem", this);
        this.comment = comment;
    }

    //this anchor pane is returned to be set as the list view item
    public VBox init() {
        txt_username.setText(comment.getSenderUsername());
        txt_name.setText(comment.getSenderName());
        txt_comment.setText(comment.getComment());
        if (comment.getSenderImage() != null)
            img_prof.setFill(new ImagePattern(new Image(new ByteArrayInputStream(comment.getSenderImage()))));
        return root;
    }
}
