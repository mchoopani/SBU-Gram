package Model;

public class PhotoMessage extends Message{
    private byte[] image;
    public PhotoMessage(String text, User sender,byte[] image) {
        super(text, sender);
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }
}
