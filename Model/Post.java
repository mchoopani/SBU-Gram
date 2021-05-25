package Model;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.Objects;

public class Post {
    private static long id = 1L;
    private long postId;
    private String writer;
    private String title;
    private String text;
    private int likes;
    private User publisher;
    private Date publishDate;
    private Image image;

    public Post(String title, String text,User publisher,String writer) {
        postId = id++;
        this.title = title;
        this.text = text;
        this.publisher = publisher;
        publishDate = new Date();
        this.writer = writer;
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
    public void like(){
        likes++;
    }
    public void unlike(){
        likes--;
    }

    public int getLikes() {
        return likes;
    }

    public Post repost(User reposter){
        return new Post(title,text,reposter,writer);
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
        this.image = new Image(new ByteArrayInputStream(image));
    }

    public Image getImage() {
        return image;
    }
}
