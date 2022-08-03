package com.shrekshellraiser.dithers;

import com.shrekshellraiser.palettes.Color;
import com.shrekshellraiser.palettes.Palette;

public class DitherFloydSteinberg implements IDither {

    @Override
    public int applyDither(int x, int y, Color[][] rgbPixelArray, Palette palette) {
        int newColor = palette.getClosestPaletteIndex(rgbPixelArray[x][y]);
        Color quantizationError = rgbPixelArray[x][y].add(palette.getColor(newColor), -1);
        if (x + 1 < rgbPixelArray.length)
            rgbPixelArray[x + 1][y] = rgbPixelArray[x + 1][y].add(quantizationError.multiply(7.0f / 16));
        if (y + 1 < rgbPixelArray[0].length) {
            rgbPixelArray[x][y + 1] = rgbPixelArray[x][y + 1].add(quantizationError.multiply(5.0f / 16));
            if (x > 1)
                rgbPixelArray[x - 1][y + 1] = rgbPixelArray[x - 1][y + 1].add(quantizationError.multiply(3.0f / 16));
            if (x + 1 < rgbPixelArray.length)
                rgbPixelArray[x + 1][y + 1] = rgbPixelArray[x + 1][y + 1].add(quantizationError.multiply(1.0f / 16));
        }
        return newColor;
    }
}
