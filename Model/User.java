package Model;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.*;

public class User implements Serializable {
    private String recoveryWord;
    private String ID;
    private String password;
    private String name;
    private String address = "World";
    private String job = "Human";
    private String birthday = "Y/M/D";
    private Socket socket;
    private Set<User> followings = new HashSet<>();
    private Set<User> followers = new HashSet<>();
    private byte[] profileImage;

    public void addFollowing(User following) {
        followings.add(following);
    }

    public User(String ID, String name, String password, String recoveryWord) {
        this.ID = ID;
        this.name = name;
        this.password = password;
        this.recoveryWord = recoveryWord;
    }

    public void setProfileImage(byte[] img) {
        this.profileImage = img;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(ID, user.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    public Set<User> getFollowings() {
        return followings;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setAddress(String country, String city) {
        if (country == null)
            country = "World";
        if (city == null)
            city = "Earth";
        this.address = country + " - " + city;
    }

    public void setJob(String job) {
        if (job.equals(""))
            return;
        this.job = job;
    }

    public void setBirthday(String y, String m, String d) {
        if (y.equals("") || m.equals("") || d.equals(""))
            return;
        this.birthday = y + "/" + m + "/" + d;
    }

    public String getAddress() {
        return address;
    }

    public String getJob() {
        return job;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getRecoveryWord() {
        return recoveryWord;
    }
}
