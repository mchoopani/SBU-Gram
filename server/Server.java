package server;

import bridges.Pack;
import Model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    static ArrayList<User> users = new ArrayList<>();
    ServerSocket serverSocket;

    public void startUpServer() throws IOException {
        serverSocket = new ServerSocket(8080);
        new LoginHandling(serverSocket).start();
        new SignupHandling(new ServerSocket(8081)).start();
    }
}

class LoginHandling extends Thread {
    ServerSocket serverSocket;

    public LoginHandling(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                new Thread(
                        () -> {
                            try {
                                while (true) {
                                    String pack = dis.readUTF();
                                    String[] UP = pack.split("[*]");
                                    if (Repository.userExists(UP[0], UP[1])) {
                                        dos.writeUTF("Login Successfully Done!");
                                        dos.flush();
                                        dos.close();
                                        dis.close();
                                        socket.close();
                                        break;
                                    } else {
                                        dos.writeUTF("Username or password is wrong!");
                                        dos.flush();
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                ).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
class SignupHandling extends Thread {
    ServerSocket serverSocket;

    public SignupHandling(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ObjectInputStream dis = new ObjectInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                new Thread(
                        () -> {
                            while (true){
                                try {
                                    Pack pack = (Pack)dis.readObject();
                                    User user = (User) pack.nodes.get(0);
                                    String fileName = (String) pack.nodes.get(1);
                                    byte[] image = (byte[]) pack.nodes.get(2);
                                    if (Repository.userExists(user)){
                                            try {
                                                dos.writeUTF("Username is already in use ...");
                                                dos.flush();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                    }else {
                                        if (image != null && image.length != 1 && fileName != null && fileName.length() != 0) {
                                            new File("D:/College/AP/SBU Gram/src/server/res/" + user.getID()).mkdir();
                                            new File("D:/College/AP/SBU Gram/src/server/res/"+user.getID()+"/"+fileName).createNewFile();
                                            File file = new File("D:/College/AP/SBU Gram/src/server/res/"+user.getID()+"/"+fileName);
                                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                                            fileOutputStream.write(image);
                                            fileOutputStream.flush();
                                            user.setImage(file.getAbsolutePath());
                                        }
                                        Repository.addUser(user);
                                        dos.writeUTF("Signup Completed.");
                                        dos.flush();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
class WaitForClient implements Runnable {
    ServerSocket serverSocket;

    public WaitForClient(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
