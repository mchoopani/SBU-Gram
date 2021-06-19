package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.List;

public class ChatController {

    @FXML
    private ListView<Chat> chatList;
    List<Chat> chats;

    @FXML
    public void initialize() throws IOException, ClassNotFoundException {
        Properties.isInProfilePage = false;
        //TODO
        chats = ChatModel.getChats();
        //initialize posts array list to be shown in list view

        //show the post array in list view
        chatList.setItems(FXCollections.observableArrayList(chats));

        //customize each cell of postList with new graphic object PostItem
        chatList.setCellFactory(postList -> new ChatItem());
    }

    @FXML
    void back(MouseEvent event) throws IOException {
        ChatModel.disconnect();
        new PageLoader().load("timeline_page");
    }

    @FXML
    void onClick(MouseEvent event) {

    }

    @FXML
    void refresh(MouseEvent event) throws IOException {
        ChatModel.disconnect();
        new PageLoader().load("chat_page");
    }

}
