package Controller;

import Model.Chat;
import Model.Comment;
import Model.Post;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class ChatItem extends ListCell<Chat> {

    @Override
    protected void updateItem(Chat chat, boolean b) {
        super.updateItem(chat, b);
        if (chat != null) {
            try {
                setGraphic(new ChatItemController(chat).init());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
