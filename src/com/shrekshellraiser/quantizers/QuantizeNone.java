package com.shrekshellraiser.quantizers;

import com.shrekshellraiser.colors.Color;
import com.shrekshellraiser.colors.Palette;
import com.shrekshellraiser.colors.PaletteImage;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class QuantizeNone {
    protected Palette palette;
    protected JPanel frame = new JPanel();

    public QuantizeNone(Palette palette) {
        this.palette = palette;
    }

    public JPanel getFrame() {
        return frame;
    }

    public PaletteImage quantize(BufferedImage image) {
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
        int[][] paletteArr = new int[RGBImage[0].length][RGBImage.length];
        int width = RGBImage[0].length;
        int height = RGBImage.length;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                paletteArr[x][y] = palette.getClosestPaletteIndex(RGBImage[x][y]);
            }
        }
        return paletteArr;
    }
}
