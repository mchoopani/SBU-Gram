package Model;

import Controller.Properties;
import bridges.Pack;

import java.io.*;
import java.net.Socket;

public class TimelineModel {
    private static InputStream currentInputStream;
    private static OutputStream currentOutputStream;
    public static Pack connect() throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost",8083);
        currentOutputStream = socket.getOutputStream();
        currentInputStream = socket.getInputStream();
        DataOutputStream dos = new DataOutputStream(currentOutputStream);
        dos.writeUTF(Properties.user.getID());
        dos.flush();
        return (Pack) new ObjectInputStream(currentInputStream).readObject();
    }
    public static void like(Post post) throws IOException {
        DataOutputStream dos = new DataOutputStream(currentOutputStream);
        dos.writeUTF("like-"+Properties.user.getID()+"-"+post.getId());
        dos.flush();
    }
    public static void unlike(Post post) throws IOException {
        DataOutputStream dos = new DataOutputStream(currentOutputStream);
        dos.writeUTF("unlike-"+Properties.user.getID()+"-"+post.getId());
        dos.flush();
    }
    public static void disconnect() throws IOException {
        DataOutputStream dos = new DataOutputStream(currentOutputStream);
        dos.writeUTF("exit");
        dos.flush();
    }
    public static void logout() throws IOException {
        DataOutputStream dos = new DataOutputStream(currentOutputStream);
        dos.writeUTF("logout-"+Properties.user.getID());
        dos.flush();
    }
}
