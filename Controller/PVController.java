package Controller;

import Model.*;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Popup;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PVController {
    public ListView<Message> messageList;
    public JFXTextArea txt_msg;
    public Label lbl_companion;
    public List<Message> messages = new ArrayList<>();
    public ImageView img_attach;
    public byte[] image;
    public byte[] audio;
    public String audioName;

    public void initialize() throws IOException, ClassNotFoundException {
        lbl_companion.setText(Properties.toShowChat.getName());
        messages = PVModel.init(this);
        messageList.setItems(FXCollections.observableArrayList(messages));
        messageList.setCellFactory(postList -> new PVItem());
        scrollTo(messages.size() - 1);
        if (Properties.toShowChat.isBlock(Properties.user) || Properties.user.isBlock(Properties.toShowChat)) {
            txt_msg.setText("There is a Block :(");
            txt_msg.setDisable(true);
        }
    }

    public void addMessageToList(Message message) {
        int index = messages.indexOf(message);
        if (index != -1) {
            if (message.isGonnaDelete()) {
                messages.remove(index);
                messages.add(index, new DeleteMessage("This message was deleted", message.getSender()));
            } else {
                messages.remove(index);
                messages.add(index, message);
            }
        } else
            messages.add(message);
        messageList.setItems(FXCollections.observableArrayList(messages));
        messageList.setCellFactory(postList -> new PVItem());
        int scrollIndex = messages.indexOf(message);
        scrollTo(scrollIndex);
        //customize each cell of postList with new graphic object PostItem
    }

    public void scrollTo(int index) {
        messageList.scrollTo(index);
    }

    public void send(ActionEvent event) throws IOException {
        String toSend = txt_msg.getText();
        if (toSend.length() == 0)
            return;
        if (image != null)
            PVModel.sendMessage(toSend,image);
        else if (audio != null)
            PVModel.sendMessage(toSend,audio,audioName);
        else
            PVModel.sendMessage(toSend);
        txt_msg.setText("");
        img_attach.setImage(new Image(new File("D:\\College\\AP\\SBU Gram\\src\\images\\attach.png").toURI().toString()));
        image = null;
        audio = null;
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        PVModel.sendMessage(null);
        ChatModel.disconnect();
        for(Music music : PVItemController.mediaPlayers.values())
            music.stop();
        PVItemController.mediaPlayers.clear();
        new PageLoader().load(Properties.lastPage);
    }

    public void attach(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extension = new FileChooser.ExtensionFilter("Image,Music", "*.jpg", "*.png","*.mp3","*.ogg");
        fileChooser.getExtensionFilters().add(extension);
        fileChooser.setTitle("Choose your media...");
        File file = fileChooser.showOpenDialog(new Popup());
        if (file != null) {
            image = null;
            audio = null;
            FileInputStream input = new FileInputStream(file);
            if (file.getName().contains("mp3") || file.getName().contains("ogg")) {
                audio = input.readAllBytes();
                audioName = file.getName();
            }
            else
                image = input.readAllBytes();
            img_attach.setImage(new Image(new File("D:\\College\\AP\\SBU Gram\\src\\images\\ok_24px.png").toURI().toString()));

        }
    }
}
