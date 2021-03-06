package Widgets;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.function.Consumer;


public final class MyDialog {
    public JFXDialogLayout jfxDialogLayout;
    private StackPane stackpane;
    private JFXDialog dialog;
    private JFXButton jfxButton;
    public MyDialog(StackPane stackpane , String backgroundHexColor){
        jfxDialogLayout = new JFXDialogLayout();
        jfxDialogLayout.setStyle("-fx-background-color: " + backgroundHexColor);
        this.stackpane = stackpane;

    }
    public MyDialog setColor(String hex){
        jfxDialogLayout.setStyle("-fx-background-color: " + hex);
        return this;
    }
    public MyDialog setTitle(String title,String hexColor){
        Label label = new Label(title);
        label.setTextFill(Color.valueOf(hexColor));
        jfxDialogLayout.setHeading(label);
        return this;
    }
    public MyDialog setTitle(String title){
        Label label = new Label(title);
        jfxDialogLayout.setHeading(label);
        return this;
    }
    public MyDialog setMessage(String message,String hexColor){
        Label label = new Label(message);
        label.setTextFill(Color.valueOf(hexColor));
        jfxDialogLayout.setBody(label);
        return this;
    }
    public MyDialog setButton(String title, String backHexColor,String textHexColor){
        jfxButton = new JFXButton(title);
        jfxButton.setStyle("-fx-background-color: " + backHexColor);
        jfxButton.setTextFill(Color.valueOf(textHexColor));
        jfxDialogLayout.setActions(jfxButton);
        return this;
    }
    public MyDialog setAction(EventHandler<ActionEvent> eventHandler){
        if (eventHandler == null)
            jfxButton.setOnAction(a->close());
        else
            jfxButton.setOnAction(eventHandler);
        return this;
    }
    public void show(){
        dialog = new JFXDialog(stackpane,jfxDialogLayout, JFXDialog.DialogTransition.BOTTOM);
        if (jfxButton.getOnAction() == null) {
            jfxButton.setOnAction(x->close());
        }
        dialog.setOverlayClose(false);
        dialog.show();
    }
    public void close(){
        dialog.close();
    }
}
