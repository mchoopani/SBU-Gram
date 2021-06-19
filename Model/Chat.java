package Model;

import server.Repository;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class Chat implements Serializable {
    private String id;
    private static int counter = 1;
    private User[] pair = new User[2];
    public Vector<Message> messages = new Vector<>();
    private transient Vector<ObjectOutputStream> outputs = new Vector<>();
    public Chat(User user1, User user2) {
        pair[0] = user1;
        pair[1] = user2;
        id = "" + counter++;
    }
    public boolean hasPV(User user){
        return pair[0].equals(user) || pair[1].equals(user);
    }

    public String getId() {
        return id;
    }
    public User getCompanion(User user){
        if (pair[0].equals(user))
            return pair[1];
        return pair[0];
    }
    public void injectChat(Socket socket) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        outputs.add(oos);
        oos.writeObject(new ArrayList<>(messages));
        oos.flush();
        while (true){
            Message message = null;
            try {
                message = (Message) ois.readObject();
            }catch (SocketException ignored){
                continue;
            }

            if (message.getText() == null)
                break;
            System.out.println("---- " + message.toString() + " ----");
            sendMessage(message);
            messages.add(message);
        }
        outputs.remove(oos);
    }
    private void sendMessage(Message message) {
        for (int i = outputs.size()-1; i >= 0; i--) {
            try {
                outputs.get(i).writeObject(message);
                outputs.get(i).flush();
            }catch (SocketException e){
                outputs.remove(i);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public ArrayList<Message> getMessages(){
        return new ArrayList<>(messages);
    }
}