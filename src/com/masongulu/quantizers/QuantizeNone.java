package com.masongulu.quantizers;

import com.masongulu.colors.Color;
import com.masongulu.colors.Palette;
import com.masongulu.colors.PaletteImage;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class QuantizeNone {
    protected Palette palette;
    protected JPanel panel;

    public JPanel getPanel() {
        return panel;
    }

    public PaletteImage quantize(BufferedImage image, Palette palette) {
        this.palette = palette;
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixelArray = new int[width * height];
        Color[][] colorPixelArr = new Color[width][height];
        int[][] imageArray;
        image.getRGB(0, 0, width, height, pixelArray, 0, width);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                colorPixelArr[x][y] = new Color(pixelArray[x + y * width]);
            }
        }
        imageArray = applyDither(colorPixelArr);
        return new PaletteImage(imageArray, palette);
    }

    protected int[][] applyDither(Color[][] RGBImage) {
        int[][] paletteArr = new int[RGBImage.length][RGBImage[0].length];
        int width = RGBImage.length;
        int height = RGBImage[0].length;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                paletteArr[x][y] = palette.getClosestPaletteIndex(RGBImage[x][y]);
            }
        }
        return paletteArr;
    }

    @Override
    public String toString() {
        return "None";
    }
}
