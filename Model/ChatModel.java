package Model;

import Controller.Properties;
import bridges.Pack;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatModel {
    private static Socket socket;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;

    public static void connect() throws IOException {
        if (socket == null){
            socket = new Socket("localhost", 8088);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        }

    }

    public static List<Chat> getChats() throws IOException, ClassNotFoundException {
        connect();
        oos.writeUTF("get");
        oos.flush();
        oos.writeUTF(Properties.user.getID());
        oos.flush();
        return (List<Chat>) ois.readObject();
    }

    public static void goToChat(String id) throws IOException {
        connect();
        oos.writeUTF("start");
        oos.flush();
        oos.writeUTF(Properties.user.getID() + "-" + id);
        oos.flush();
    }

    public static void goToChat() throws IOException {
        connect();
        oos.writeUTF("start");
        oos.flush();
        oos.writeUTF(Properties.user.getID() + "-" + Properties.profile.getID());
        oos.flush();
    }
    public static void disconnect() throws IOException {
        socket.close();
        oos.close();
        ois.close();
        socket = null;
    }
    public static Socket getSocket() {
        return socket;
    }
}
