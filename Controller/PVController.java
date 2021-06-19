package Controller;

import Model.ChatModel;
import Model.Message;
import Model.PVModel;
import Model.PageLoader;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PVController {
    public ListView<Message> messageList;
    public JFXTextArea txt_msg;
    public List<Message> messages = new ArrayList<>();
    public void initialize() throws IOException, ClassNotFoundException {
        messages = PVModel.init(this);
        messageList.setItems(FXCollections.observableArrayList(messages));
        messageList.setCellFactory(postList -> new PVItem());
        scrollToEnd();
    }
    public void addMessageToList(Message message){
        messages.add(message);

        messageList.setItems(FXCollections.observableArrayList(messages));
        messageList.setCellFactory(postList -> new PVItem());
        scrollToEnd();
        //customize each cell of postList with new graphic object PostItem
        System.out.println("Received : " + message);

    }
    public void scrollToEnd(){
        messageList.scrollTo(messages.size()-1);
    }
    public void send(ActionEvent event) throws IOException {
        String toSend = txt_msg.getText();
        if (toSend.length() == 0)
            return;
        PVModel.sendMessage(toSend);
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        PVModel.sendMessage(null);
        ChatModel.disconnect();
        new PageLoader().load("timeline_page");
    }
}
