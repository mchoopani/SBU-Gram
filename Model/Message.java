package Model;

import Controller.Properties;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private String text;
    private Date date;
    private User sender;
    private byte[] senderImage;

    public Message(String text,User sender) {
        this.text = text;
        this.sender = sender;
        date = new Date();
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }

    public boolean isFromMe(String username){
        return sender.getID().equals(username);
    }

    public void setSenderImage(byte[] senderImage) {
        this.senderImage = senderImage;
    }

    public byte[] getSenderImage() {
        return senderImage;
    }

    public User getSender() {
        return sender;
    }

    public String getSenderUsername() {
        return getSender().getID();
    }
}
