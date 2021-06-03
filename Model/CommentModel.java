package Model;

import bridges.Pack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CommentModel {
    public static Post post = null;
    public static void publishComment(Comment comment) throws IOException {
        Socket socket = new Socket("localhost",8085);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(new Pack(comment,post.getId()));
        oos.flush();
        socket.close();
        oos.close();
    }
}
