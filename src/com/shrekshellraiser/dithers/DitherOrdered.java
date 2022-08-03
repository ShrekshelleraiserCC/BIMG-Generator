package com.shrekshellraiser.dithers;

import com.shrekshellraiser.palettes.Color;
import com.shrekshellraiser.palettes.Palette;

public class DitherOrdered implements IDither {

    private final double colorSpread;
    private final double[][] thresholdMap;

    public DitherOrdered(int thresholdMapSize, double colorSpread) {
        thresholdMap = switch (thresholdMapSize) {
            case 2 -> new double[][]{{0, 2}, {3, 1}};
            case 4 -> new double[][]{{0, 8, 2, 10},
                    {12, 4, 14, 6},
                    {3, 11, 1, 9},
                    {15, 7, 13, 5}};
            case 8 -> new double[][]{
                    {0, 32, 8, 40, 2, 34, 10, 42},
                    {48, 16, 56, 24, 50, 18, 58, 26},
                    {12, 44, 4, 36, 14, 46, 6, 38},
                    {60, 28, 52, 20, 62, 30, 54, 22},
                    {3, 35, 11, 43, 1, 33, 9, 41},
                    {51, 19, 59, 27, 49, 17, 57, 25},
                    {15, 47, 7, 39, 13, 45, 5, 37},
                    {63, 31, 55, 23, 61, 29, 53, 21}
            };
            default ->
                    throw new IllegalStateException("Invalid threshold map size, must be 2 4 or 8: " + thresholdMapSize);
        };
        for (int row = 0; row < thresholdMap.length; row++) {
            for (int column = 0; column < thresholdMap[0].length; column++) {
                thresholdMap[row][column] *= 1 / Math.pow(2, thresholdMapSize);
            }
        }
        this.colorSpread = colorSpread;
    }

    @Override
    public int applyDither(int x, int y, Color[][] rgbPixelArray, Palette palette) {
        int colorOffset = (int) (colorSpread * (thresholdMap[x % thresholdMap.length][y % thresholdMap[0].length] - 1 / 2.0));
        return palette.getClosestPaletteIndex(new Color(colorOffset, colorOffset, colorOffset).add(rgbPixelArray[x][y]));
    }
}
