package Controller;

import Model.AddPostModel;
import Model.PageLoader;
import Model.Post;
import Model.TimelineModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.*;
import java.nio.file.Paths;

public class PostItemController {
    public AnchorPane root;
    public Circle prof;
    public ImageView img_post;
    public ImageView img_like;
    public ImageView img_repost;
    public Label txt_name;
    public Label txt_title;
    public Label txt_text;
    public Label txt_date;
    public Label repostCount;
    public Label likeCount;
    private Post post;
    private boolean liked;

    //each list item will have its exclusive controller in runtime so we set the controller as we load the fxml
    public PostItemController(Post post) throws IOException {
        new PageLoader().load("postitem", this);
        this.post = post;
    }

    //this anchor pane is returned to be set as the list view item
    public AnchorPane init() {
        liked = post.likedBefore(Properties.user.getID());
        prof.setFill(new ImagePattern(new Image(new ByteArrayInputStream(post.getPublisher().getProfileImage()))));
        txt_title.setText(post.getTitle());
        txt_name.setText(post.getReferencePost().getPublisher().getName());
        txt_text.setText(post.getText());
        txt_date.setText(post.getPublishDate().toString());
        if (post.getImage() != null)
            img_post.setImage(new Image(new ByteArrayInputStream(post.getImage())));
        likeCount.setText(post.getLikes()+"");
        repostCount.setText(post.getReposts()+"");
        if (liked){
            img_like.setImage(new Image(new File("D:\\College\\AP\\SBU Gram\\src\\images\\liked.png").toURI().toString()));
        }
        return root;
    }

    //you can show post's detail in new page with this method
    public void detail(ActionEvent actionEvent) {

    }

    public void comment(MouseEvent mouseEvent) {
    }
    public void like(MouseEvent mouseEvent) throws IOException {
        if (liked){
            liked = false;
            likeCount.setText(Integer.parseInt(likeCount.getText())-1+"");
            img_like.setImage(new Image(new File("D:\\College\\AP\\SBU Gram\\src\\images\\like.png").toURI().toString()));
            TimelineModel.unlike(post);
        }else {
            liked = true;
            likeCount.setText(Integer.parseInt(likeCount.getText())+1+"");
            img_like.setImage(new Image(new File("D:\\College\\AP\\SBU Gram\\src\\images\\liked.png").toURI().toString()));
            TimelineModel.like(post);
        }
    }
    public void repost(MouseEvent event) throws IOException {
        AddPostModel.sendPost(post.getTitle(),post.getText(),post.getImage(),Properties.user.getID(),true,post.getReferencePost().getId()+"");
        repostCount.setText(Integer.parseInt(repostCount.getText())+1+"");
    }
}
