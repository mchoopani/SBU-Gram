package Controller;

import Model.Message;
import Model.PageLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class PVItemController {
    public HBox root;
    public Label lbl_message;
    public Label lbl_date;
    public Circle img_profile;
    public Message message;
    public PVItemController(Message m) throws IOException {
        message = m;
        if (!m.isFromMe(Properties.user.getID()))
            new PageLoader().load("pvitem_flipped",this);
        else
            new PageLoader().load("pvitem",this);
    }

    public HBox init() {
        lbl_message.setText(message.getText());
        lbl_date.setText(message.getDate().toString());
        if (message.getSender().getProfileImage() != null )
            img_profile.setFill(new ImagePattern(new Image(new ByteArrayInputStream(message.getSender().getProfileImage()))));
        return root;
    }
}
