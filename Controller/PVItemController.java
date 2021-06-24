package Controller;

import Model.*;
import com.jfoenix.controls.JFXTextArea;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PVItemController {
    public static Map<Long, Music> mediaPlayers = new HashMap<>();
    public HBox root;
    public Label lbl_message;
    public Label lbl_date;
    public Circle img_profile;
    public JFXTextArea editBox;
    public ImageView edit_tick;
    public ImageView img_message;
    public boolean editing = false;
    public Message message;
    public ImageView play;

    public PVItemController(Message m) throws IOException {
        message = m;
        if (message instanceof PhotoMessage) {
            if (!m.isFromMe(Properties.user.getID()))
                new PageLoader().load("pvImageitem_flipped", this);
            else
                new PageLoader().load("pvImageitem", this);
        } else if (message instanceof AudioMessage) {
            if (!m.isFromMe(Properties.user.getID()))
                new PageLoader().load("pvAudioitem_flipped", this);
            else
                new PageLoader().load("pvAudioitem", this);
        } else {
            if (!m.isFromMe(Properties.user.getID()))
                new PageLoader().load("pvitem_flipped", this);
            else
                new PageLoader().load("pvitem", this);
        }
    }

    public HBox init() throws IOException {
        lbl_message.setText(message.getText());
        lbl_date.setText(message.getDate().toString());
        if (message instanceof PhotoMessage)
            img_message.setImage(new Image(new ByteArrayInputStream(((PhotoMessage) message).getImage())));
        else if (message instanceof AudioMessage) {
            AudioMessage audioMessage = (AudioMessage) message;
            String path = "D:\\College\\AP\\SBU Gram\\src\\Temporary\\";
            path += audioMessage.getFilename();
            File file = new File(path);
            if (!file.exists()) {
                new FileWriter(path);
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(audioMessage.getAudio());
            }
            Media media = new Media(new File(path).toURI().toString());
            Music music = mediaPlayers.get(message.getId());
            if (music == null) {
                music = new Music(new MediaPlayer(media));
                mediaPlayers.put(message.getId(), music);
            } else {
                if (music.isPlaying())
                    play.setImage(new Image(new File("D:\\College\\AP\\SBU Gram\\src\\images\\pause.png").toURI().toString()));
            }
            music.setPlayButton(play);
            Music finalMusic = music;
            play.setOnMouseClicked(mouseEvent -> {
                if (finalMusic.isPlaying()) {
                    finalMusic.pause();
                } else {
                    stopAllAudios();
                    finalMusic.play();
                }
            });
        }
        if (message.getSender().getProfileImage() != null)
            img_profile.setFill(new ImagePattern(new Image(new ByteArrayInputStream(message.getSender().getProfileImage()))));
        return root;
    }

    public void edit_click() throws IOException {
        if (message instanceof DeleteMessage)
            return;
        if (!editing) {
            lbl_message.setVisible(false);
            editBox.setText(lbl_message.getText());
            editBox.setVisible(true);
            edit_tick.setImage(new Image(new File("D:\\College\\AP\\SBU Gram\\src\\images\\ok_24px.png").toURI().toString()));
            editing = true;
        } else {
            editBox.setVisible(false);
            lbl_message.setText(editBox.getText());
            lbl_message.setVisible(true);
            edit_tick.setImage(new Image(new File("D:\\College\\AP\\SBU Gram\\src\\images\\pencil_24px.png").toURI().toString()));
            editing = false;
            message.setText(editBox.getText());
            PVModel.sendMessage(message, "EDIT");
        }
    }

    public void delete_click() throws IOException {
        if (message instanceof DeleteMessage)
            return;
        mediaPlayers.get(message.getId()).stop();
        message.setGonnaDelete();
        PVModel.sendMessage(message, "DELETE");
    }

    public void stopAllAudios() {
        for (Long id : mediaPlayers.keySet()) {
            if (id != message.getId())
            mediaPlayers.get(id).stop();
        }
    }
}
