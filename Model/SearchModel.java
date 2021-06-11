package Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class SearchModel {
    public static ArrayList<User> getAllUsers() throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost",8087);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ArrayList<User> output = (ArrayList<User>) ois.readObject();
        socket.close();
        ois.close();
        return output;
    }
}
