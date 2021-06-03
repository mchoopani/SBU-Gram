package Model;

import Controller.Properties;
import Controller.TimelinePage;
import Widgets.MyDialog;
import bridges.Pack;
import javafx.scene.layout.StackPane;

import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;

public class LoginModel {
    public static void checkLoggedIn() throws IOException {
        try {
            File file = new File("D:\\College\\AP\\SBU Gram\\src\\Temporary\\login_data.bin");
            ObjectInputStream loadLoginData = new ObjectInputStream(new FileInputStream(file));
            Properties.user = (User) loadLoginData.readObject();
        }catch (Exception ignored){
        }
    }
    public static void login(String username, String password, StackPane stackPane) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 8080);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(username + "*" + password);
        dos.flush();
        ObjectInputStream dis = new ObjectInputStream(socket.getInputStream());
        Pack pack = (Pack) dis.readObject();
        String message = (String) pack.nodes.get(0);
        switch (message) {
            case "SUCCESSFULL":
                Properties.user = (User) pack.nodes.get(1);
                new MyDialog(stackPane, "#1a237e")
                        .setTitle("Server Said", "#e5c07b")
                        .setMessage("Login Successfully done...", "#ffffff")
                        .setButton("Ok", "#1a237e", "#ffffff")
                        .setAction(a -> {
                            try {
                                File file = new File("D:\\College\\AP\\SBU Gram\\src\\Temporary\\login_data.bin");
                                ObjectOutputStream saveLoginData = new ObjectOutputStream(new FileOutputStream(file));
                                saveLoginData.writeObject(Properties.user);
                                saveLoginData.flush();
                                new PageLoader().load("timeline_page");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        })
                        .show();
                break;
            case "WRONG":
                new MyDialog(stackPane, "#dd2c00")
                        .setTitle("Server Said", "#e5c07b")
                        .setMessage("Doesn't find a matched user!", "#ffffff")
                        .setButton("Ok", "#dd2c00", "#ffffff")
                        .show();
                break;
        }

    }
}
