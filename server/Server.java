package server;

import Model.Comment;
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
        new AddPostHandler(new ServerSocket(8084)).start();
        new CommentHandler(new ServerSocket(8085)).start();
        new ProfileHandler(new ServerSocket(8086)).start();
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
//                                    e.printStackTrace();
                                    continue ;
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

class AddPostHandler extends Thread {
    private ServerSocket serverSocket;

    public AddPostHandler(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            Socket socket = null;
            ObjectInputStream ois = null;
            try {
                socket = serverSocket.accept();
                ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            ObjectInputStream finalOis = ois;
            new Thread(() -> {
                Pack pack = null;
                try {
                    pack = (Pack) finalOis.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                String title = (String) pack.nodes.get(0);
                String text = (String) pack.nodes.get(1);
                String publisherName = (String) pack.nodes.get(2);
                byte[] image = (byte[]) pack.nodes.get(3);
                boolean isRepost = (boolean) pack.nodes.get(4);
                Post post = new Post(title, text, Repository.getUserByUsername(publisherName));
                if (isRepost) {
                    Post reference = Repository.getPostById((String) pack.nodes.get(5));
                    reference.addReposts();
                    post.setReferencePost(reference);

                }
                post.setImage(image);
                Repository.addPost(post);
                System.out.println("New Post Added");
            }).start();
        }
    }
}
class CommentHandler extends Thread{
    private ServerSocket serverSocket;

    public CommentHandler(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            while (true){
                Socket socket = serverSocket.accept();
                socket.getOutputStream();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Pack pack = (Pack) ois.readObject();
                Comment comment = (Comment) pack.nodes.get(0);
                long id = (long) pack.nodes.get(1);
                Repository.getPostById(id + "").addComment(comment);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
class ProfileHandler extends Thread {
    ServerSocket serverSocket;

    public ProfileHandler(ServerSocket serverSocket) {
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
                ArrayList<Post> posts = Repository.getUserPosts(username);
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
//                                    e.printStackTrace();
                                    continue ;
                                }
                                String[] splits = command.split("-");
                                switch (splits[0]) {
                                    case "like":
                                        Repository.getPostById(splits[2]).like(splits[1]);
                                        break;
                                    case "unlike":
                                        Repository.getPostById(splits[2]).unlike(splits[1]);
                                        break;
                                    case "follow":
                                        Repository.getUserByUsername(splits[1]).follow(Repository.getUserByUsername(splits[2]));
                                        System.out.println("Follow");
                                        break;
                                    case "unFollow":
                                        Repository.getUserByUsername(splits[1]).unFollow(Repository.getUserByUsername(splits[2]));
                                        break;
                                    case "block":
                                        Repository.getUserByUsername(splits[1]).block(Repository.getUserByUsername(splits[2]));
                                        break;
                                    case "unBlock":
                                        Repository.getUserByUsername(splits[1]).unBlock(Repository.getUserByUsername(splits[2]));
                                        break;
                                    case "mute":
                                        Repository.getUserByUsername(splits[1]).mute(Repository.getUserByUsername(splits[2]));
                                        break;
                                    case "unMute":
                                        Repository.getUserByUsername(splits[1]).unMute(Repository.getUserByUsername(splits[2]));
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