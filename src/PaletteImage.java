import java.awt.image.BufferedImage;

public class PaletteImage {
    private int[][] imageArray;
    private int width;
    private int height;
    private Palette palette;
    PaletteImage(BufferedImage image, Palette palette, boolean dither) {
        this.palette = palette;
        this.width = image.getWidth();
        this.height = image.getHeight();
        int[] pixelArray = new int[width*height];
        int[][][] rgbPixelArray = new int[width][height][3];
        this.imageArray = new int[width][height];
        image.getRGB(0, 0, width, height, pixelArray, 0, width);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rgbPixelArray[x][y] = Colors.splitColor(pixelArray[x + y * width]);
            }
        }
        pixelArray = null; // dereference to save some memory
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                imageArray[x][y] = palette.getClosestPaletteIndex(rgbPixelArray[x][y]);
                if (dither) {
                    int[] quantErr = Colors.addArrays(rgbPixelArray[x][y],
                            Colors.splitColor(palette.getColor(imageArray[x][y])), -1);
                    if (x < width - 1)
                        rgbPixelArray[x+1][y] = Colors.addArrays(rgbPixelArray[x+1][y],
                                Colors.scaleArray(quantErr, 7.0f/16));
                    if (y < height - 1) {
                        rgbPixelArray[x][y+1] = Colors.addArrays(rgbPixelArray[x][y+1],
                                Colors.scaleArray(quantErr, 5.0f/16));
                        if (x > 1)
                            rgbPixelArray[x-1][y+1] = Colors.addArrays(rgbPixelArray[x-1][y+1],
                                    Colors.scaleArray(quantErr, 3.0f/16));
                        if (x < width - 1)
                            rgbPixelArray[x+1][y+1] = Colors.addArrays(rgbPixelArray[x+1][y+1],
                                    Colors.scaleArray(quantErr, 1.0f/16));
                    }
                }
            }
        }
    }

    public BufferedImage toImage() {
        BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        int[] buffer = new int[this.width*this.height];
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                buffer[x + y*this.width] = palette.getColor(this.imageArray[x][y]);
            }
        }
        image.setRGB(0, 0, this.width, this.height, buffer, 0, this.width);
        return image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPixelIndex(int x, int y, int index) {
        if (index >= 0 && index < this.imageArray.length)
            this.imageArray[x][y] = index;
    }

    public int getPixelIndex(int x,int y) {
        return this.imageArray[x][y];
    }

    public int getPixelColor(int x, int y) {
        return palette.getColor(this.imageArray[x][y]);
    }
}
