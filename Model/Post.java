package Model;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.*;

public class Post implements Serializable {
    private static long id = 1L;
    private long postId;
    private String writer;
    private String title;
    private String text;
    private Set<String> likes;
    private ArrayList<String> reposts;
    private User publisher;
    private Date publishDate;
    private byte[] image;

    public Post(String title, String text,User publisher,String writer) {
        postId = id++;
        this.title = title;
        this.text = text;
        this.publisher = publisher;
        publishDate = new Date();
        this.writer = writer;
        likes = new HashSet<>();
        reposts = new ArrayList<>();
    }

    public User getPublisher() {
        return publisher;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", likes=" + likes +
                ", publisher=" + publisher +
                '}';
    }

    public long getId() {
        return postId;
    }

    public boolean likedBefore(String username){
        return likes.contains(username);
    }
    public void like(String username){
        likes.add(username);
    }
    public void unlike(String username){
        likes.remove(username);
    }

    public int getLikes() {
        return likes.size();
    }

    public Post repost(User reposter){
        reposts.add(reposter.getID());
        return new Post(title,text,reposter,writer);
    }
    public int getReposts(){
        return reposts.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return postId == post.postId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId);
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }
}
