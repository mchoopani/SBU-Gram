package Controller;

import Model.LoginModel;
import Model.PageLoader;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

import Widgets.MyDialog;
import server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class LoginController {

    public StackPane stackpane;
    @FXML
    public JFXButton btnLogin;
    @FXML
    public JFXTextField usernameTF;
    @FXML
    public JFXPasswordField passwordTF;


    public void loginBtnClicked(ActionEvent event) throws IOException {
        String username = usernameTF.getText();
        String password = passwordTF.getText();
        if (username.length()==0 || password.length()==0)
        {
            new MyDialog(stackpane,"#f50057")
                    .setTitle("Error!","#e5c07b")
                    .setMessage("Username or password is null"
                            +"\nPlease complete both fields at first, then click Login.","#ffffff")
                    .setButton("Ok","#f50057","#ffffff")
                    .show();
        }else {
            LoginModel.login(username,password,stackpane);
        }
    }
    public void goToSignup(ActionEvent event) throws IOException {
        new PageLoader().load("signup_page");
    }

}
