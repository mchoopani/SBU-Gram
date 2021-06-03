package Model;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable,Comparable {
    private final User sender;
    private final Date date;
    private final String comment;

    public Comment(User sender, String comment) {
        this.sender = sender;
        this.comment = comment;
        date = new Date();
    }
    public String getSenderName(){
        return sender.getName();
    }
    public String getSenderUsername(){
        return sender.getID();
    }
    public String getComment(){
        return comment;
    }
    public Date getDate(){return date;}
    public byte[] getSenderImage(){
        return sender.getProfileImage();
    }

    @Override
    public int compareTo(Object o) {
        return -date.compareTo(((Comment) o).getDate());
    }
}
