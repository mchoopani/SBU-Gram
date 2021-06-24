package Model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Music {
    private MediaPlayer mediaPlayer;
    private ImageView playButton;
    private boolean playing = false;
    public Music(MediaPlayer mediaPlayer){
        this.mediaPlayer = mediaPlayer;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setPlayButton(ImageView playButton) {
        this.playButton = playButton;
    }

    public boolean isPlaying() {
        return playing;
    }
    public void play(){
        playButton.setImage(new Image(new File("D:\\College\\AP\\SBU Gram\\src\\images\\pause.png").toURI().toString()));
        mediaPlayer.play();
        playing = true;
    }
    public void pause(){
        playButton.setImage(new Image(new File("D:\\College\\AP\\SBU Gram\\src\\images\\play.png").toURI().toString()));
        mediaPlayer.pause();
        playing = false;
    }
    public void stop(){
        playButton.setImage(new Image(new File("D:\\College\\AP\\SBU Gram\\src\\images\\play.png").toURI().toString()));
        mediaPlayer.stop();
        playing = false;
    }
}
