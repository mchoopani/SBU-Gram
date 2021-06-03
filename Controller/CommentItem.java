package Controller;

import Model.Comment;
import Model.Post;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class CommentItem extends ListCell<Comment> {

    @Override
    protected void updateItem(Comment comment, boolean b) {
        super.updateItem(comment, b);
        if (comment != null) {
            try {
                setGraphic(new CommentItemController(comment).init());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
