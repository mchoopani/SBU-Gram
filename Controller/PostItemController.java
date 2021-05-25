package Controller;

import Model.PageLoader;
import Model.Post;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class PostItemController {
    public AnchorPane root;
    public ImageView profileImage;
    public ImageView img_post;
    public Label txt_name;
    public Label txt_title;
    public Label txt_text;
    Post post;

    //each list item will have its exclusive controller in runtime so we set the controller as we load the fxml
    public PostItemController(Post post) throws IOException {
        new PageLoader().load("postitem", this);
        this.post = post;
    }

    //this anchor pane is returned to be set as the list view item
    public AnchorPane init() {

        profileImage.setImage(post.getPublisher().getProfileImage());
        txt_title.setText(post.getTitle());
        txt_name.setText(post.getPublisher().getName());
        txt_text.setText(post.getText());
        img_post.setImage(post.getImage());

        return root;
    }

    //you can show post's detail in new page with this method
    public void detail(ActionEvent actionEvent) {

    }

    public void comment(MouseEvent mouseEvent) {
    }
    public void like(MouseEvent mouseEvent){
        System.out.println(post.getTitle() + " liked.");
    }
    /*
    you can also add on mouse click for like and repost image
     */
}
