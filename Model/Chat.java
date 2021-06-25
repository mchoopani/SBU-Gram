package Model;

import server.Repository;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

public class Chat implements Serializable,Comparable<Chat> {
    private String id;
    private static int counter = 1;
    private User[] pair = new User[2];
    private Vector<Message> messages = new Vector<>();
    private transient Vector<ObjectOutputStream> outputs = new Vector<>();

    public Chat(User user1, User user2) {
        pair[0] = user1;
        pair[1] = user2;
        id = "" + counter++;
    }

    public boolean hasPV(User user) {
        return pair[0].equals(user) || pair[1].equals(user);
    }

    public String getId() {
        return id;
    }

    public User getCompanion(User user) {
        if (pair[0].equals(user))
            return pair[1];
        return pair[0];
    }

    public boolean existBlocking() {
        return pair[0].isBlock(pair[1]) || pair[1].isBlock(pair[0]);
    }

    public void injectChat(Socket socket,String usernames) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        if (outputs == null)
            outputs = new Vector<>();
        outputs.add(oos);
        oos.writeObject(new ArrayList<>(messages));
        oos.flush();
        for (Message message : messages){
            if (!message.isFromMe(usernames.split("-")[0]))
                message.seen();
        }
        while (true) {
            Message message = null;
            try {
                message = (Message) ois.readObject();
            } catch (SocketException ignored) {
                continue;
            }
            if (message.getText() == null)
                break;
            if (existBlocking()) {
                message.setText("(This Message Won't Send, Because One Of Pair blocked companion)");
                oos.writeObject(message);
                oos.flush();
                continue;
            }
            if (!messages.contains(message))
                message.stickID();
            sendMessage(message);
            addMessage(message);
            System.err.printf("%s send\nmessage: from %s to %s\ntime: %s\n",message.getSender().getID(),message.getSender().getName(),getCompanion(message.getSender()).getName(), new Date().toString());
            System.err.flush();
            System.out.println("-----------------------------------------------");
        }
        outputs.remove(oos);
    }

    public void addMessage(Message message) {
        int index = messages.indexOf(message);
        if (index != -1) {
            messages.remove(index);
            if(!message.isGonnaDelete()){
                messages.add(index, message);
            }
        } else {
            if (message.isGonnaDelete())
                messages.remove(index);
            else
                messages.add(message);
        }
    }

    private void sendMessage(Message message) {
        for (int i = outputs.size() - 1; i >= 0; i--) {
            try {
                outputs.get(i).writeObject(message);
                outputs.get(i).flush();
            } catch (SocketException e) {
                outputs.remove(i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.err.printf("%s receive\nmessage: from %s\ntime: %s\n",message.getSender().getID(),getCompanion(message.getSender()).getName(), new Date().toString());
        System.err.flush();
        System.out.println("-----------------------------------------------");
    }
    public int getNotSeenMessageCount(String username){
        int sum = 0;
        for(Message message : messages){
            if (!message.isFromMe(username) && !message.isSeen())
                sum++;
        }
        return sum;
    }
    public ArrayList<Message> getMessages() {
        return new ArrayList<>(messages);
    }
    @Override
    public int compareTo(Chat o) {
        return this.messages.get(messages.size()-1).getDate().compareTo(o.messages.get(o.messages.size()-1).getDate());
    }
}