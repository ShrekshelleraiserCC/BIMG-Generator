import java.awt.image.BufferedImage;

public interface IMode {
    public String[][] get();

    public BufferedImage getImage();

    Palette getPalette();
}
