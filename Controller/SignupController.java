package Controller;

import Model.PageLoader;
import Model.SignupModel;
import Widgets.MyDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import Model.User;

import java.io.*;

public class SignupController {
    public StackPane sign_sp;
    public JFXTextField txt_name;
    public JFXTextField txt_username;
    public JFXPasswordField txt_pass1;
    public JFXPasswordField txt_pass2;
    public ImageView img_profile;

    byte[] image;
    String fileName;

    public void attach(MouseEvent mouseEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extension = new FileChooser.ExtensionFilter("Image", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(extension);
        fileChooser.setTitle("Choose your profile photo");
        File file = fileChooser.showOpenDialog(new Popup());
        if (file != null) {
            FileInputStream input = new FileInputStream(file);
            image = input.readAllBytes();
            img_profile.setImage(new Image(file.toURI().toString()));
            fileName = file.getName();
        }

    }

    public void signup(ActionEvent actionEvent) throws IOException {
        if (txt_name.getText().length() == 0
                ||
                txt_pass1.getText().length() == 0
                ||
                txt_pass2.getText().length() == 0
                ||
                txt_username.getText().length() == 0) {
            new MyDialog(sign_sp,"#f50057")
                    .setTitle("Error!","#e5c07b")
                    .setMessage("one or more field is null"
                            +"\nPlease complete both fields at first, then click Signup.","#ffffff")
                    .setButton("Ok","#f50057","#ffffff")
                    .show();

        }
        else if (!txt_pass1.getText().equals(txt_pass2.getText())){
            new MyDialog(sign_sp,"#f50057")
                    .setTitle("Error!","#e5c07b")
                    .setMessage("Password and repeat should match"
                            +"\nPlease enter those exactly","#ffffff")
                    .setButton("Ok","#f50057","#ffffff")
                    .show();
        }
        else {
            SignupModel.signup(new User(txt_username.getText(),txt_name.getText(),txt_pass1.getText()),sign_sp,fileName,image);
        }
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        new PageLoader().load("login_page");
    }
}
