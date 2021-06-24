package Model;

public class AudioMessage extends Message{
    private byte[] audio;
    private String filename;
    public AudioMessage(String text, User sender,byte[] audio,String filename) {
        super(text, sender);
        this.audio = audio;
        this.filename = filename;
    }

    public byte[] getAudio() {
        return audio;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
