package server;

import Model.Chat;
import Model.Comment;
import Model.Post;
import bridges.Pack;
import Model.User;
import server.DB.Database;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    public static void main(String[] args) throws IOException {
        new Server().startUpServer();
    }
    public void startUpServer() throws IOException {

        Database.getInstance().loadData();

        new LoginHandling(new ServerSocket(8080)).start();
        new SignupHandling(new ServerSocket(8081)).start();
        new ForgetHandling(new ServerSocket(8082)).start();
        new Timeline(new ServerSocket(8083)).start();
        new AddPostHandler(new ServerSocket(8084)).start();
        new CommentHandler(new ServerSocket(8085)).start();
        new ProfileHandler(new ServerSocket(8086)).start();
        new SearchUsersHandler(new ServerSocket(8087)).start();
        new ChatHandler(new ServerSocket(8088)).start();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    Database.getInstance().updateData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask,0,30000L);

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
                                    System.err.printf("%s %s\ntime: %s\n", UP[0], "login", new Date().toString());
                                    System.err.flush();
                                    System.out.println("-----------------------------------------------");
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
                                    System.err.printf("%s %s %s\ntime: %s\n", user.getID(), "register", "D:/College/AP/SBU Gram/src/server/res/" + user.getID() + "/" + fileName, new Date().toString());
                                    System.err.flush();
                                    System.out.println("-----------------------------------------------");
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
                System.err.printf("%s %s\ntime: %s\n", username, "get posts list", new Date().toString());
                System.err.flush();
                System.out.println("-----------------------------------------------");
                new Thread(
                        () -> {
                            Loop:
                            while (true) {
                                String command = null;
                                try {
                                    command = dis.readUTF();
                                } catch (IOException e) {
//                                    e.printStackTrace();
                                    continue;
                                }
                                String[] splits = command.split("-");
                                switch (splits[0]) {
                                    case "like":
                                        Post post0 = Repository.getPostById(splits[2]);
                                        post0.like(splits[1]);
                                        System.err.printf("%s %s\nmessage: %s %s\ntime: %s\n", splits[1], "like", splits[1], post0.getTitle(), new Date().toString());
                                        System.err.flush();
                                        System.out.println("-----------------------------------------------");
                                        break;
                                    case "unlike":
                                        Post post1 = Repository.getPostById(splits[2]);
                                        post1.unlike(splits[1]);
                                        System.err.printf("%s %s\nmessage: %s %s\ntime: %s\n", splits[1], "unlike", splits[1], post1.getTitle(), new Date().toString());
                                        System.err.flush();
                                        System.out.println("-----------------------------------------------");
                                        break;
                                    case "logout":
                                        System.err.printf("%s %s\ntime: %s\n", splits[1], "logout", new Date().toString());
                                        System.err.flush();
                                        System.out.println("-----------------------------------------------");
                                        try {
                                            oos.close();
                                            dis.close();
                                            socket.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        break Loop;
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
                    System.err.printf("%s %s\nmessage: %s %s\ntime: %s\n", publisherName, "repost", publisherName, post.getTitle(), new Date().toString());
                    System.err.flush();
                    System.out.println("-----------------------------------------------");

                } else {
                    System.err.printf("%s %s\nmessage: %s %s %s\ntime: %s\n", publisherName, "publish", title, "null", publisherName, new Date().toString());
                    System.err.flush();
                    System.out.println("-----------------------------------------------");

                }
                post.setImage(image);
                Repository.addPost(post);
            }).start();
        }
    }
}

class CommentHandler extends Thread {
    private ServerSocket serverSocket;

    public CommentHandler(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                socket.getOutputStream();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Pack pack = (Pack) ois.readObject();
                Comment comment = (Comment) pack.nodes.get(0);
                long id = (long) pack.nodes.get(1);
                Post post = Repository.getPostById(id + "");
                post.addComment(comment);
                System.err.printf("%s %s\nmessage: %s\ntime: %s\n", comment.getSenderUsername(), "comment", post.getTitle(), new Date().toString());
                System.err.flush();
                System.out.println("-----------------------------------------------");
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
                new Thread(
                        () -> {
                            try {
                                String income = dis.readUTF();
                                String username = income.split("-")[0];
                                String getter = income.split("-")[1];
                                ArrayList<Post> posts = Repository.getUserPosts(username);
                                User user = Repository.getUserByUsername(username);
                                Pack pack = new Pack(posts,user);
                                oos.writeObject(pack);
                                oos.flush();
                                System.err.printf("%s %s %s\nmessage: %s %s\ntime: %s\n", getter, "get info", username, username, "profile", new Date().toString());
                                System.err.flush();
                                System.out.println("-----------------------------------------------");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Loop:
                            while (true) {
                                String command = null;
                                try {
                                    command = dis.readUTF();
                                } catch (IOException e) {
//                                    e.printStackTrace();
                                    continue;
                                }
                                String[] splits = command.split("-");
                                switch (splits[0]) {
                                    case "like":
                                        Post post0 = Repository.getPostById(splits[2]);
                                        post0.like(splits[1]);
                                        System.err.printf("%s %s\nmessage: %s %s\ntime: %s\n", splits[1], "like", splits[1], post0.getTitle(), new Date().toString());
                                        System.err.flush();
                                        System.out.println("-----------------------------------------------");
                                        break;
                                    case "unlike":
                                        Post post1 = Repository.getPostById(splits[2]);
                                        post1.unlike(splits[1]);
                                        System.err.printf("%s %s\nmessage: %s %s\ntime: %s\n", splits[1], "unlike", splits[1], post1.getTitle(), new Date().toString());
                                        System.err.flush();
                                        System.out.println("-----------------------------------------------");
                                        break;
                                    case "follow":
                                        Repository.getUserByUsername(splits[1]).follow(Repository.getUserByUsername(splits[2]));
                                        System.err.printf("%s %s\nmessage: %s\ntime: %s\n", splits[1], "follow", splits[2], new Date().toString());
                                        System.err.flush();
                                        System.out.println("-----------------------------------------------");
                                        break;
                                    case "unFollow":
                                        Repository.getUserByUsername(splits[1]).unFollow(Repository.getUserByUsername(splits[2]));
                                        System.err.printf("%s %s\nmessage: %s\ntime: %s\n", splits[1], "unfollow", splits[2], new Date().toString());
                                        System.err.flush();
                                        System.out.println("-----------------------------------------------");
                                        break;
                                    case "block":
                                        Repository.getUserByUsername(splits[1]).block(Repository.getUserByUsername(splits[2]));
                                        System.err.printf("%s %s\nmessage: %s\ntime: %s\n", splits[1], "block", splits[2], new Date().toString());
                                        System.err.flush();
                                        System.out.println("-----------------------------------------------");
                                        break;
                                    case "unBlock":
                                        Repository.getUserByUsername(splits[1]).unBlock(Repository.getUserByUsername(splits[2]));
                                        System.err.printf("%s %s\nmessage: %s\ntime: %s\n", splits[1], "unblock", splits[2], new Date().toString());
                                        System.err.flush();
                                        System.out.println("-----------------------------------------------");
                                        break;
                                    case "mute":
                                        Repository.getUserByUsername(splits[1]).mute(Repository.getUserByUsername(splits[2]));
                                        System.err.printf("%s %s\nmessage: %s\ntime: %s\n", splits[1], "mute", splits[2], new Date().toString());
                                        System.err.flush();
                                        System.out.println("-----------------------------------------------");
                                        break;
                                    case "unMute":
                                        Repository.getUserByUsername(splits[1]).unMute(Repository.getUserByUsername(splits[2]));
                                        System.err.printf("%s %s\nmessage: %s\ntime: %s\n", splits[1], "unMute", splits[2], new Date().toString());
                                        System.err.flush();
                                        System.out.println("-----------------------------------------------");
                                        break;
                                    case "delete":
                                        Repository.delete(splits[1]);
                                        System.err.printf("%s %s\ntime: %s\n", splits[1], "delete account", new Date().toString());
                                        System.err.flush();
                                        System.out.println("-----------------------------------------------");
                                        break;
                                    case "edit":
                                        try {
                                            ObjectInputStream objectInputStream = new ObjectInputStream(dis);
                                            Pack pack = (Pack) objectInputStream.readObject();
                                            User user = (User) pack.nodes.get(0);
                                            String fileName = (String) pack.nodes.get(1);
                                            if (fileName != null) {
                                                byte[] image = user.getProfileImage();
                                                new File("D:/College/AP/SBU Gram/src/server/res/" + user.getID()).mkdir();
                                                new File("D:/College/AP/SBU Gram/src/server/res/" + user.getID() + "/" + fileName).createNewFile();
                                                File file = new File("D:/College/AP/SBU Gram/src/server/res/" + user.getID() + "/" + fileName);
                                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                                fileOutputStream.write(image);
                                                fileOutputStream.flush();
                                                System.err.printf("%s %s\nmessage: %s\ntime: %s\n", user.getID(), "update info", "D:/College/AP/SBU Gram/src/server/res/" + user.getID() + "/" + fileName, new Date().toString());
                                                System.err.flush();
                                                System.out.println("-----------------------------------------------");
                                            }
                                            Repository.replace(user);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case "exit": {
                                        try {
                                            oos.close();
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

class SearchUsersHandler extends Thread {
    ServerSocket serverSocket;

    public SearchUsersHandler(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(Repository.getUsers());
                        socket.close();
                        oos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class ChatHandler extends Thread {
    ServerSocket serverSocket;

    public ChatHandler(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                new Thread(
                        () -> {
                            try {
                                String command = ois.readUTF();
                                switch (command) {
                                    case "get": {
                                        String username = ois.readUTF();
                                        ArrayList<Chat> chats = Repository.collectChats(Repository.getUserByUsername(username));
                                        oos.writeObject((ArrayList<Chat>) chats);
                                        oos.flush();
                                        String anotherCommand = ois.readUTF();
                                        switch (anotherCommand) {
                                            case "start": {
                                                String usernames = ois.readUTF();
                                                Chat chat = Repository.createChatIfNotExist(usernames.split("-")[0], usernames.split("-")[1]);
                                                new Thread(
                                                        () -> {
                                                            try {
                                                                chat.injectChat(socket,username);
                                                            } catch (IOException | ClassNotFoundException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                ).start();
                                            }
                                            break;
                                        }
                                    }
                                    break;
                                    case "chat": {
                                        String chatID = ois.readUTF();
                                        Chat chat = Repository.getChatById(chatID);
                                        oos.writeObject(chat);
                                        new Thread(
                                                () -> {
                                                    try {
                                                        chat.injectChat(socket,chatID);
                                                    } catch (IOException | ClassNotFoundException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                        ).start();
                                    }
                                    break;
                                    case "start": {
                                        String usernames = ois.readUTF();
                                        Chat chat = Repository.createChatIfNotExist(usernames.split("-")[0], usernames.split("-")[1]);
                                        new Thread(
                                                () -> {
                                                    try {
                                                        chat.injectChat(socket,usernames);
                                                    } catch (IOException | ClassNotFoundException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                        ).start();
                                    }
                                    break;
                                }

                            } catch (EOFException ignored) {
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                ).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}