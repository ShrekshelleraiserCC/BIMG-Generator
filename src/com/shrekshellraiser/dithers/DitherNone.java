package com.shrekshellraiser.dithers;

import com.shrekshellraiser.palettes.Palette;

public class DitherNone implements IDither {
    @Override
    public int applyDither(int x, int y, int[][][] rgbPixelArray, Palette palette) {
        return palette.getClosestPaletteIndex(rgbPixelArray[x][y]);
    }
}
