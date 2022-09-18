package com.shrekshellraiser.quantizers;

import com.shrekshellraiser.colors.Color;
import com.shrekshellraiser.colors.Palette;

public class QuantizeFloydSteinberg extends QuantizeNone {

    public QuantizeFloydSteinberg(Palette palette) {
        super(palette);
    }

    @Override
    protected int[][] applyDither(Color[][] RGBImage) {
        int[][] paletteArr = new int[RGBImage[0].length][RGBImage.length];
        int width = RGBImage[0].length;
        int height = RGBImage.length;
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
}
