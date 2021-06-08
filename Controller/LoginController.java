package Controller;

import Model.LoginModel;
import Model.PageLoader;
import Model.User;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import Widgets.MyDialog;
import server.Server;

import java.io.*;
import java.net.Socket;


public class LoginController {

    public StackPane stackpane;
    @FXML
    public JFXButton btnLogin;
    @FXML
    public JFXTextField usernameTF;
    @FXML
    public JFXTextField visiblePass;
    @FXML
    public JFXPasswordField passwordTF;


    public void loginBtnClicked(ActionEvent event) throws IOException, ClassNotFoundException {
        String username = usernameTF.getText();
        String password = passwordTF.getText();
        if (username.length() == 0 || password.length() == 0) {
            new MyDialog(stackpane, "#f50057")
                    .setTitle("Error!", "#e5c07b")
                    .setMessage("Username or password is null"
                            + "\nPlease complete both fields at first, then click Login.", "#ffffff")
                    .setButton("Ok", "#f50057", "#ffffff")
                    .show();
        } else {
            String message = LoginModel.login(username, password);
            switch (message) {
                case "SUCCESSFULL":
                    new MyDialog(stackpane, "#1a237e")
                            .setTitle("Server Said", "#e5c07b")
                            .setMessage("Login Successfully done...", "#ffffff")
                            .setButton("Ok", "#1a237e", "#ffffff")
                            .setAction(a -> {
                                try {
                                    new PageLoader().load("timeline_page");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            })
                            .show();
                    break;
                case "WRONG":
                    new MyDialog(stackpane, "#dd2c00")
                            .setTitle("Server Said", "#e5c07b")
                            .setMessage("Doesn't find a matched user!", "#ffffff")
                            .setButton("Ok", "#dd2c00", "#ffffff")
                            .show();
                    break;
            }
        }
    }

    public void goToSignup(ActionEvent event) throws IOException {
        new PageLoader().load("signup_page");
    }

    public void forgetPassword(ActionEvent event) throws IOException {
        new PageLoader().load("forget_page");

    }

    public void visiblePass(MouseEvent mouseEvent) {
        passwordTF.setVisible(false);
        visiblePass.setVisible(true);
        visiblePass.setText(passwordTF.getText());
    }

    public void unVisiblePass(MouseEvent mouseEvent) {
        passwordTF.setVisible(true);
        visiblePass.setVisible(false);
        passwordTF.setText(visiblePass.getText());

    }
}
