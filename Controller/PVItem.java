package Controller;

import Model.Chat;
import Model.Message;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class PVItem extends ListCell<Message> {
    @Override
    protected void updateItem(Message chat, boolean b) {
        super.updateItem(chat, b);
        if (chat != null) {
            try {
                setGraphic(new PVItemController(chat).init());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
