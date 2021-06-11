package Model;

import Controller.Properties;
import bridges.Pack;
import java.io.*;
import java.net.Socket;

public class LoginModel {
    public static boolean checkLoggedIn() {
        try {
            File file = new File("D:\\College\\AP\\SBU Gram\\src\\Temporary\\login_data.bin");
            ObjectInputStream loadLoginData = new ObjectInputStream(new FileInputStream(file));
            Properties.user = (User) loadLoginData.readObject();
            String loginResult = login(Properties.user.getID(), Properties.user.getPassword());
            if (loginResult.equals("SUCCESSFULL"))
                return true;
        }catch (Exception e){
            return false;
        }
        return false;
    }
    public static String login(String username, String password) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 8080);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(username + "*" + password);
        dos.flush();
        ObjectInputStream dis = new ObjectInputStream(socket.getInputStream());
        Pack pack = (Pack) dis.readObject();
        if (pack.nodes.get(0).equals("SUCCESSFULL")) {
            Properties.user = (User) pack.nodes.get(1);
            File file = new File("D:\\College\\AP\\SBU Gram\\src\\Temporary\\login_data.bin");
            ObjectOutputStream saveLoginData = new ObjectOutputStream(new FileOutputStream(file));
            saveLoginData.writeObject(Properties.user);
            saveLoginData.flush();
        }
        return (String) pack.nodes.get(0);
    }
}
