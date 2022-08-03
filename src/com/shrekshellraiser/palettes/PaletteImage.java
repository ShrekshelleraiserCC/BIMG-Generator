package com.shrekshellraiser.palettes;

import com.shrekshellraiser.dithers.IDither;

import java.awt.image.BufferedImage;

public class PaletteImage {
    private final int[][] imageArray;
    private final int width;
    private final int height;
    private final Palette palette;

    public PaletteImage(BufferedImage image, Palette palette, IDither dither) {
        this.palette = palette;
        this.width = image.getWidth();
        this.height = image.getHeight();
        int[] pixelArray = new int[width * height];
        Color[][] colorPixelArr = new Color[width][height];
        this.imageArray = new int[width][height];
        image.getRGB(0, 0, width, height, pixelArray, 0, width);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                colorPixelArr[x][y] = new Color(pixelArray[x + y * width]);
            }
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                imageArray[x][y] = dither.applyDither(x, y, colorPixelArr, palette);
            }
        }
    }

    public BufferedImage toImage() {
        BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        int[] buffer = new int[this.width*this.height];
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                buffer[x + y * this.width] = palette.getColor(this.imageArray[x][y]).getColor();
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
        return palette.getColor(this.imageArray[x][y]).getColor();
    }
}
