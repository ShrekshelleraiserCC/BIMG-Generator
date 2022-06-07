import java.awt.image.BufferedImage;

public class ModeLowDensity implements IMode {
    private final Palette palette;
    private final int width;
    private final int height;
    private final PaletteImage image;
    ModeLowDensity(BufferedImage image, Palette palette, IDither dither) {
        this.palette = palette;
        this.image = new PaletteImage(image, this.palette, dither);
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
    }

    ModeLowDensity(BufferedImage image, IDither dither) {
        this(image, new Palette(KMeans.applyKMeans(image, 16)), dither);
    }

    private char[] getChar(int x, int y) {
        // Now that we established which 2 colors are most prevalent here
        return new char[]{' ',' ',Integer.toHexString(image.getPixelIndex(x,y)).charAt(0)};
    }

    private String[] getRow(int rowNum) {
        String[] im = new String[]{"","",""};
        for (int section = 0; section < this.width; section++) {
            char[] blit = getChar(section, rowNum);
            im[0] += blit[0];
            im[1] += blit[1];
            im[2] += blit[2];
        }
        return im;
    }

    public String[][] get() {
        String[][] im = new String[height][3];
        for (int row = 0; row < height; row++) {
            im[row] = getRow(row);
        }
        return im;
    }

    public Palette getPalette() {
        return palette;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public BufferedImage getImage() {
        return Mode.scaleImage(image.toImage(), 2, 3);
    }

}
