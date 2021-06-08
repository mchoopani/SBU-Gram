package Model;

import Controller.Properties;
import Widgets.MyDialog;
import bridges.Pack;

import java.io.*;
import java.net.Socket;

public class ProfileModel {
    private static InputStream currentInputStream;
    private static OutputStream currentOutputStream;
    public static Pack connect() throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost",8086);
        currentOutputStream = socket.getOutputStream();
        currentInputStream = socket.getInputStream();
        DataOutputStream dos = new DataOutputStream(currentOutputStream);
        dos.writeUTF("" + Properties.profile.getID());
        dos.flush();
        return (Pack) new ObjectInputStream(currentInputStream).readObject();
    }
    public static void follow(String requester,String acceptor) throws IOException {
        DataOutputStream dos = new DataOutputStream(currentOutputStream);
        dos.writeUTF("follow-" + requester + "-" + acceptor);
        dos.flush();
    }
    public static void unFollow(String requester,String acceptor) throws IOException {
        DataOutputStream dos = new DataOutputStream(currentOutputStream);
        dos.writeUTF("unFollow-" + requester + "-" + acceptor);
        dos.flush();
    }
    public static void block(String requester,String acceptor) throws IOException {
        DataOutputStream dos = new DataOutputStream(currentOutputStream);
        dos.writeUTF("block-" + requester + "-" + acceptor);
        dos.flush();
    }
    public static void unBlock(String requester,String acceptor) throws IOException {
        DataOutputStream dos = new DataOutputStream(currentOutputStream);
        dos.writeUTF("unBlock-" + requester + "-" + acceptor);
        dos.flush();
    }
    public static void mute(String requester,String acceptor) throws IOException {
        DataOutputStream dos = new DataOutputStream(currentOutputStream);
        dos.writeUTF("mute-" + requester + "-" + acceptor);
        dos.flush();
    }
    public static void unMute(String requester,String acceptor) throws IOException {
        DataOutputStream dos = new DataOutputStream(currentOutputStream);
        dos.writeUTF("unMute-" + requester + "-" + acceptor);
        dos.flush();
    }

    public static void like(Post post) throws IOException {
        DataOutputStream dos = new DataOutputStream(currentOutputStream);
        dos.writeUTF("like-"+Properties.profile.getID()+"-"+post.getId());
        dos.flush();
    }
    public static void unlike(Post post) throws IOException {
        DataOutputStream dos = new DataOutputStream(currentOutputStream);
        dos.writeUTF("unlike-"+Properties.profile.getID()+"-"+post.getId());
        dos.flush();
    }
    public static void disconnect() throws IOException {
        DataOutputStream dos = new DataOutputStream(currentOutputStream);
        dos.writeUTF("exit");
        dos.flush();
    }
    public static void editProfile(User user,String fileName) throws IOException, ClassNotFoundException {
        DataOutputStream dos = new DataOutputStream(currentOutputStream);
        dos.writeUTF("edit");
        ObjectOutputStream oos = new ObjectOutputStream(currentOutputStream);
        oos.writeObject(new Pack(user,fileName));
        oos.flush();
        dos.flush();
    }

    public static void deleteAccount(String id) throws IOException {
        DataOutputStream dos = new DataOutputStream(currentOutputStream);
        dos.writeUTF("delete-"+id);
        dos.flush();
        new PageLoader().load("login_page");
    }
}
