package com.masongulu.quantizers;

import com.masongulu.colors.Color;

public class QuantizeFloydSteinberg extends QuantizeNone {
    @Override
    protected int[][] applyDither(Color[][] RGBImage) {
        int[][] paletteArr = new int[RGBImage.length][RGBImage[0].length];
        int width = RGBImage.length;
        int height = RGBImage[0].length;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int newColor = palette.getClosestPaletteIndex(RGBImage[x][y]);
                Color quantizationError = RGBImage[x][y].add(palette.getColor(newColor), -1);
                if (x + 1 < RGBImage.length)
                    RGBImage[x + 1][y] = RGBImage[x + 1][y].add(quantizationError.multiply(7.0f / 16));
                if (y + 1 < RGBImage[0].length) {
                    RGBImage[x][y + 1] = RGBImage[x][y + 1].add(quantizationError.multiply(5.0f / 16));
                    if (x > 1)
                        RGBImage[x - 1][y + 1] = RGBImage[x - 1][y + 1].add(quantizationError.multiply(3.0f / 16));
                    if (x + 1 < RGBImage.length)
                        RGBImage[x + 1][y + 1] = RGBImage[x + 1][y + 1].add(quantizationError.multiply(1.0f / 16));
                }
                paletteArr[x][y] = newColor;
            }
        }
        return paletteArr;
    }

    @Override
    public String toString() {
        return "Floyd Steinberg";
    }
}
