package Model;

import java.io.Serializable;
import java.net.Socket;
import java.util.*;

public class User implements Serializable {
    private String ID;
    private String password;
    private String name;
    private String profilePath;
    private Socket socket;
    private Set<User> followings = new HashSet<>();
    private ArrayList<User> followers = new ArrayList<>();
    public void addFollowing(User following){
        followings.add(following);
    }
    public User(String ID, String name,String password) {
        this.ID = ID;
        this.name = name;
        this.password = password;
    }
    public void setImage(String path){
        profilePath = path;
    }
    public String getImage(){
        if (profilePath == null){
            profilePath = "D:\\College\\AP\\SBU Gram\\src\\nullprof.jpg";
        }
        return profilePath;
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

}
