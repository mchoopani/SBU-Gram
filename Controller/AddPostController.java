package Controller;

import Model.AddPostModel;
import Model.PageLoader;
import Model.User;
import Widgets.MyDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import server.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AddPostController {
    public ImageView postPhoto;
    public JFXTextField txt_title;
    public JFXTextArea txt_text;
    public StackPane stack;
    byte[] image;
    public void attach(MouseEvent mouseEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extension = new FileChooser.ExtensionFilter("Image", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(extension);
        fileChooser.setTitle("Choose your profile photo");
        File file = fileChooser.showOpenDialog(new Popup());
        if (file != null) {
            FileInputStream input = new FileInputStream(file);
            image = input.readAllBytes();
            postPhoto.setImage(new Image(file.toURI().toString()));
        }
    }

    public void send(MouseEvent mouseEvent) throws IOException {
        AddPostModel.sendPost(txt_title.getText(),txt_text.getText(),image, Properties.user.getID(),false,"");
        txt_text.setText("");
        txt_title.setText("");
        postPhoto.setImage(new Image(new File("D:/College/AP/SBU Gram/noimage.png").toURI().toString()));
        image = null;
        new MyDialog(stack, "#1a237e")
                .setTitle("Horaaaaaaa", "#e5c07b")
                .setMessage("Your post added to repository...", "#ffffff")
                .setButton("Ok", "#1a237e", "#ffffff")
                .show();
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        new PageLoader().load("timeline_page");
    }
}
