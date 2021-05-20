package Model;

import Widgets.MyDialog;
import bridges.Pack;
import javafx.scene.layout.StackPane;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SignupModel {
    public static void signup(User user,StackPane stackPane,String fileName,byte[] image) throws IOException {
        Socket socket = new Socket("localhost",8081);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(new Pack(user,fileName,image));
        oos.flush();
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        new MyDialog(stackPane,"#1a237e")
                .setTitle("Server Said","#e5c07b")
                .setMessage(dis.readUTF(),"#ffffff")
                .setButton("Ok","#1a237e","#ffffff")
                .show();
    }
}
