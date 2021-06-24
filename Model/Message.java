package Model;

import Controller.Properties;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Message implements Serializable {
    private static long counter = 1;
    private long id = 0;
    private String text;
    private Date date;
    private User sender;
    private boolean gonnaEdit = false;
    private boolean gonnaDelete = false;
    private boolean seen = false;
    private byte[] senderImage;

    public Message(String text,User sender) {
        this.text = text;
        this.sender = sender;
        date = new Date();
    }

    public void stickID(){
        id = counter++;
    }

    public void edit(String text){
        this.text = text;
        gonnaEdit = true;
    }
    public void refreshEdit(){
        gonnaEdit = false;
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

    public void setText(String text) {
        this.text = text;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id;
    }

    public long getId() {
        return id;
    }

    public void setGonnaDelete(){
        gonnaDelete = true;
    }
    public boolean isGonnaDelete(){
        return gonnaDelete;
    }

    public boolean isSeen() {
        return seen;
    }
    public void seen(){
        seen = true;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
