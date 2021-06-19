package Model;

import Controller.PVController;
import Controller.Properties;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class PVModel {
    static ObjectOutputStream oos;
    static ObjectInputStream ois ;
    public static List<Message> init(PVController pvController) throws IOException, ClassNotFoundException {
        Socket socket = ChatModel.getSocket();
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        List<Message> output = (List<Message>) ois.readObject();
        Thread thread = new Thread(()->{
            Platform.setImplicitExit(false);
            while (true){
                Message received = null;
                try {
//                    received = (String) ois.readObject();
                    received = (Message) ois.readObject();
                    Message finalReceived = received;
                    Platform.runLater(()->{
                        pvController.addMessageToList(finalReceived);
                    });
                }
                catch (SocketException e){
                    break;
                }
                catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.setDaemon(true);
        thread.start();
        return output;
    }
    public static void sendMessage(String message) throws IOException {
        oos.writeObject(new Message(message, Properties.user) );
        oos.flush();
    }
}
