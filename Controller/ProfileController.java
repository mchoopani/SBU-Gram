package Controller;

import Model.PageLoader;
import Model.Post;
import Model.ProfileModel;
import Model.TimelineModel;
import Widgets.MyDialog;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileController {
    public StackPane stackPane;
    public ListView<Post> postList;
    public Label txt_name;
    public Label txt_username;
    public Label txt_address;
    public Label txt_birthday;
    public Label txt_follower;
    public Label txt_following;
    public HBox hisProfile;
    public JFXButton btn_follow;
    public JFXButton btn_block;
    public JFXButton btn_mute;
    public HBox myProfile;
    public Circle img_prof;

    List<Post> posts;

    @FXML
    public void initialize() throws IOException, ClassNotFoundException {
        if (Properties.profile.isBlock(Properties.user)){
            new MyDialog(stackPane, "#f50057")
                    .setTitle("Error!", "#e5c07b")
                    .setMessage(Properties.profile.getName() + " Blocked You :("
                            + "\nSo you can't see his or her profile...", "#ffffff")
                    .setButton("Ok", "#f50057", "#ffffff")
                    .setAction(event -> {
                        try {
                            ProfileModel.disconnect();
                            new PageLoader().load("timeline_page");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    })
                    .show();
        }
        Properties.isInProfilePage = true;
        posts = (List<Post>) ProfileModel.connect().nodes.get(0);

        //initialize posts array list to be shown in list view
        //show the post array in list view
        postList.setItems(FXCollections.observableArrayList(posts));

        //customize each cell of postList with new graphic object PostItem
        postList.setCellFactory(postList -> new PostItem());

        txt_name.setText(Properties.profile.getName());
        txt_username.setText("@" + Properties.profile.getID());
        txt_address.setText(Properties.profile.getAddress());
        txt_birthday.setText(Properties.profile.getBirthday());
        txt_follower.setText("" + Properties.profile.getFollowers().size());
        txt_following.setText("" + Properties.profile.getFollowings().size());
        img_prof.setFill(new ImagePattern(new Image(new ByteArrayInputStream(Properties.profile.getProfileImage()))));
        updateButtonsTexts();


        if (Properties.user.equals(Properties.profile)) {
            hisProfile.toBack();
            hisProfile.setVisible(false);
            myProfile.toFront();
            myProfile.setVisible(true);
        } else {
            hisProfile.toFront();
            hisProfile.setVisible(true);
            myProfile.toBack();
            myProfile.setVisible(false);

        }

    }
    public void updateButtonsTexts(){
        if (Properties.user.isMute(Properties.profile))
            btn_mute.setText("UnMute");
        else
            btn_mute.setText("Mute");
        if (Properties.user.isBlock(Properties.profile))
            btn_block.setText("UnBlock");
        else
            btn_block.setText("Block");
        if (Properties.user.isFollow(Properties.profile))
            btn_follow.setText("UnFollow");
        else
            btn_follow.setText("Follow");
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        new PageLoader().load("timeline_page");
    }

    public void follow(ActionEvent event) throws IOException {
        if (!Properties.user.isFollow(Properties.profile)) {
            ProfileModel.follow(Properties.user.getID()+"",Properties.profile.getID()+"");
            Properties.user.follow(Properties.profile);
        }else {
            ProfileModel.unFollow(Properties.user.getID()+"",Properties.profile.getID()+"");
            Properties.user.unFollow(Properties.profile);
        }
        updateButtonsTexts();
    }

    public void block(ActionEvent event) throws IOException {
        if (!Properties.user.isBlock(Properties.profile)) {
            ProfileModel.block(Properties.user.getID()+"",Properties.profile.getID()+"");
            Properties.user.block(Properties.profile);
        }else {
            ProfileModel.unBlock(Properties.user.getID()+"",Properties.profile.getID()+"");
            Properties.user.unBlock(Properties.profile);
        }
        updateButtonsTexts();
    }

    public void mute(ActionEvent event) throws IOException {
        if (!Properties.user.isMute(Properties.profile)) {
            ProfileModel.mute(Properties.user.getID()+"",Properties.profile.getID()+"");
            Properties.user.mute(Properties.profile);
        }else {
            ProfileModel.unMute(Properties.user.getID()+"",Properties.profile.getID()+"");
            Properties.user.unMute(Properties.profile);
        }
        updateButtonsTexts();
    }

    public void editProfile(ActionEvent event) {
    }

    public void deleteAccount(ActionEvent event) {
    }
}
