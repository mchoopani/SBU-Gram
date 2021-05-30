package Model;

import Controller.Properties;
import bridges.Pack;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AddPostModel {
    public static void sendPost(String title,String text,byte[] image) throws IOException {
        Socket socket = new Socket("localhost",8084);
        ObjectOutputStream dos = new ObjectOutputStream(socket.getOutputStream());
        dos.writeObject(new Pack(title,text, Properties.user.getID(),Properties.user.getName(),image));
        dos.flush();
    }
}
