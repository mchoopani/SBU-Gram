package Controller;

import Model.ForgetModel;
import Model.LoginModel;
import Model.PageLoader;
import Widgets.MyDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class ForgetController {
    public StackPane stackpane;
    public JFXTextField txt_username;
    public JFXTextField txt_recovery;

    public void loginBtnClicked(ActionEvent event) throws IOException {
        if (txt_username.getText().length() == 0 || txt_recovery.getText().length() == 0) {
            new MyDialog(stackpane, "#f50057")
                    .setTitle("Error!", "#e5c07b")
                    .setMessage("Username or recovery word is null"
                            + "\nPlease complete both fields at first, then click Check.", "#ffffff")
                    .setButton("Ok", "#f50057", "#ffffff")
                    .show();
        } else {
            ForgetModel.forget(txt_username.getText(), txt_recovery.getText(), stackpane);
        }
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        new PageLoader().load("login_page");
    }
}
