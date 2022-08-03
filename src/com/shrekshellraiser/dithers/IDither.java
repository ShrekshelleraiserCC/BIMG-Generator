package com.shrekshellraiser.dithers;

import com.shrekshellraiser.palettes.Palette;

public interface IDither {
    int applyDither(int x, int y, int[][][] rgbPixelArray, Palette palette);

}
