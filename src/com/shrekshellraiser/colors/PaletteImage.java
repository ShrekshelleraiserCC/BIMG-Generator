package com.shrekshellraiser.colors;

import java.awt.image.BufferedImage;

public class PaletteImage {
    private final int[][] imageArray;
    private final int width;
    private final int height;
    private final Palette palette;

    /**
     * @param image   A [x][y] indexed array of palette indexes
     * @param palette Palette
     */
    public PaletteImage(int[][] image, Palette palette) {
        this.palette = palette;
        this.imageArray = image;
        this.width = image.length;
        this.height = image[0].length;
    }

    public BufferedImage toImage() {
        BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        int[] buffer = new int[this.width * this.height];
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

    public int getPixelIndex(int x, int y) {
        return this.imageArray[x][y];
    }

    public int getPixelColor(int x, int y) {
        return palette.getColor(this.imageArray[x][y]).getColor();
    }

    public Palette getPalette() {
        return palette;
    }
}
