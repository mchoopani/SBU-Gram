package Controller;

import Model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ChatItemController {
    @FXML
    private AnchorPane root;

    @FXML
    private Circle img_profile;

    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_date;

    @FXML
    private Label lbl_text;
    Chat chat;

    public ChatItemController(Chat chat) throws IOException {
        new PageLoader().load("chatitem", this);
        this.chat = chat;
    }

    //this anchor pane is returned to be set as the list view item
    public AnchorPane init() {
        User companion = chat.getCompanion(Properties.user);
        lbl_name.setText(companion.getName());
        if (chat.getMessages().size() > 0)
            lbl_text.setText(chat.getMessages().get(chat.getMessages().size()-1).getText());
        else
            lbl_text.setText("Nothing...");
        img_profile.setFill(new ImagePattern(new Image(new ByteArrayInputStream(companion.getProfileImage()))));
        return root;
    }

    public void onChatItemClick(MouseEvent e) throws IOException {
        ChatModel.goToChat(chat.getCompanion(Properties.user).getID());
        new PageLoader().load("pv_page");
    }
}
