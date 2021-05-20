package Model;

import Widgets.MyDialog;
import javafx.scene.layout.StackPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class LoginModel {
    public static void login(String username, String password, StackPane stackPane) throws IOException {
        Socket socket = new Socket("localhost",8080);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(username+"*"+password);
        dos.flush();
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        new MyDialog(stackPane,"#1a237e")
                .setTitle("Server Said","#e5c07b")
                .setMessage(dis.readUTF(),"#ffffff")
                .setButton("Ok","#1a237e","#ffffff")
                .show();
    }
}
