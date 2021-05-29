package server;

import Model.Post;
import bridges.Pack;
import Model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public void startUpServer() throws IOException {
        new LoginHandling(new ServerSocket(8080)).start();
        new SignupHandling(new ServerSocket(8081)).start();
        new ForgetHandling(new ServerSocket(8082)).start();
        new Timeline(new ServerSocket(8083)).start();
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
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                new Thread(
                        () -> {
                            try {
                                String pack = dis.readUTF();
                                String[] UP = pack.split("[*]");
                                Pack output;
                                if (Repository.userExists(UP[0], UP[1])) {
                                    output = new Pack("SUCCESSFULL", Repository.getUserByUsername(UP[0]));
                                } else {
                                    output = new Pack("WRONG");
                                }
                                oos.writeObject(output);
                                oos.flush();
                                dis.close();
                                socket.close();
//
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
                            try {
                                Pack pack = null;
                                try {
                                    pack = (Pack) dis.readObject();
                                } catch (EOFException e) {
                                    System.out.println(e.getMessage());
                                }
                                User user = (User) pack.nodes.get(0);
                                String fileName = (String) pack.nodes.get(1);
                                byte[] image = (byte[]) pack.nodes.get(2);
                                if (Repository.userExists(user)) {
                                    try {
                                        dos.writeUTF("Username is already in use ...");
                                        dos.flush();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    if (image != null && image.length != 1 && fileName != null && fileName.length() != 0) {
                                        new File("D:/College/AP/SBU Gram/src/server/res/" + user.getID()).mkdir();
                                        new File("D:/College/AP/SBU Gram/src/server/res/" + user.getID() + "/" + fileName).createNewFile();
                                        File file = new File("D:/College/AP/SBU Gram/src/server/res/" + user.getID() + "/" + fileName);
                                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                                        fileOutputStream.write(image);
                                        fileOutputStream.flush();
                                        user.setProfileImage(image);
                                    }
                                    Repository.addUser(user);
                                    dos.writeUTF("Signup Completed.");
                                    dos.flush();
                                }
                            } catch (IOException | ClassNotFoundException e) {
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

class ForgetHandling extends Thread {
    ServerSocket serverSocket;

    public ForgetHandling(ServerSocket serverSocket) {
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
                                String pack = dis.readUTF();
                                String[] UR = pack.split("[*]");
                                User user = Repository.getUserByUsername(UR[0]);
                                if (user != null) {
                                    if (user.getRecoveryWord().equals(UR[1]))
                                        dos.writeUTF("Your Password is : " + user.getPassword());
                                    else
                                        dos.writeUTF("Sorry! Recovery Word is wrong...");
                                } else {
                                    dos.writeUTF("User doesn't exist!");
                                }
                                dos.flush();
                                dos.close();
                                dis.close();
                                socket.close();
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

class Timeline extends Thread {
    ServerSocket serverSocket;

    public Timeline(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                String username = dis.readUTF();
                ArrayList<Post> posts = Repository.getFollowingPosts(username);
                Pack pack = new Pack(posts);
                oos.writeObject(pack);
                oos.flush();
                new Thread(
                        () -> {
                            Loop:
                            while (true) {
                                String command = null;
                                try {
                                    command = dis.readUTF();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String[] splits = command.split("-");
                                switch (splits[0]) {
                                    case "like":
                                        Repository.getPostById(splits[2]).like(splits[1]);
                                        break;
                                    case "unlike":
                                        Repository.getPostById(splits[2]).unlike(splits[1]);
                                        break;
                                    case "exit": {
                                        try {
                                            dis.close();
                                            socket.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        break Loop;
                                    }
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
