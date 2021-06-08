package Controller;

import Model.PageLoader;
import Model.ProfileModel;
import Model.SignupModel;
import Model.User;
import Widgets.MyDialog;
import bridges.Pack;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Popup;

import java.io.*;

public class EditProfileController {
    public StackPane sign_sp;
    public JFXTextField txt_name;
    public JFXTextField txt_oldpass;
    public JFXPasswordField txt_pass1;
    public JFXPasswordField txt_pass2;
    public ImageView img_profile;
    public JFXTextField txt_year;
    public JFXTextField txt_month;
    public JFXTextField txt_day;
    public JFXTextField txt_recovery;
    public JFXTextField txt_country;
    public JFXTextField txt_city;
    public JFXTextField txt_job;
    public Label lbl_username;
    public byte[] image = null;
    public String fileName = null;

    @FXML
    public void initialize() {
        txt_name.setText(Properties.profile.getName());
        lbl_username.setText("@" + Properties.profile.getID());
        String[] splits = Properties.profile.getAddress().split(" - ");
        if (splits.length > 0)
            txt_country.setText(splits[0]);
        if (splits.length > 1)
            txt_city.setText(splits[1]);
        txt_day.setText(Properties.profile.getBirthday().split("/")[2]);
        txt_month.setText(Properties.profile.getBirthday().split("/")[1]);
        txt_year.setText(Properties.profile.getBirthday().split("/")[0]);
        txt_job.setText(Properties.profile.getJob());
        if (Properties.profile.getProfileImage() != null)
            img_profile.setImage(new Image(new ByteArrayInputStream(Properties.profile.getProfileImage())));
    }

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

    public void back(MouseEvent mouseEvent) throws IOException {
        new PageLoader().load("profile_page");
    }

    public void edit(ActionEvent event) throws IOException, ClassNotFoundException {
        if (txt_name.getText().length() == 0
                ||
                txt_pass1.getText().length() == 0
                ||
                txt_pass2.getText().length() == 0
                ||
                txt_recovery.getText().length() == 0
        ) {
            new MyDialog(sign_sp, "#f50057")
                    .setTitle("Error!", "#e5c07b")
                    .setMessage("one or more field is null"
                            + "\nPlease complete fields at first, then click Edit.", "#ffffff")
                    .setButton("Ok", "#f50057", "#ffffff")
                    .show();

        } else if (!txt_pass1.getText().equals(txt_pass2.getText())) {
            new MyDialog(sign_sp, "#f50057")
                    .setTitle("Error!", "#e5c07b")
                    .setMessage("Password and repeat should match"
                            + "\nPlease enter those exactly", "#ffffff")
                    .setButton("Ok", "#f50057", "#ffffff")
                    .show();
        } else if (!txt_pass1.getText().matches("[a-zA-Z0-9]{8,}")) {
            new MyDialog(sign_sp, "#f50057")
                    .setTitle("Error!", "#e5c07b")
                    .setMessage("Password length should >= 8 and just can have latin characters and numbers!" +
                            "\nCorrect Example: Mahmood123456", "#ffffff")
                    .setButton("Ok", "#f50057", "#ffffff")
                    .show();
        } else if (!txt_oldpass.getText().equals(Properties.profile.getPassword())) {
            new MyDialog(sign_sp, "#f50057")
                    .setTitle("Error!", "#e5c07b")
                    .setMessage("Old Password is incorrect" +
                            "\nPlease be more careful!", "#ffffff")
                    .setButton("Ok", "#f50057", "#ffffff")
                    .show();
        } else {
            Properties.profile.setName(txt_name.getText());
            Properties.profile.setAddress(txt_country.getText(),txt_city.getText());
            Properties.profile.setBirthday(txt_year.getText(),txt_month.getText(),txt_day.getText());
            Properties.profile.setJob(txt_job.getText());
            if (image!=null)
                Properties.profile.setProfileImage(image);
            Properties.profile.setPassword(txt_pass1.getText());
            ProfileModel.editProfile(Properties.profile,fileName);
            new MyDialog(sign_sp, "#1a237e")
                    .setTitle("Server Said", "#e5c07b")
                    .setMessage("Edited Successfully!", "#ffffff")
                    .setButton("Ok", "#1a237e", "#ffffff")
                    .show();
        }

    }
}
