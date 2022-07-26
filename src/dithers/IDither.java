package dithers;

import palettes.Palette;

public interface IDither {
    int applyDither(int x, int y, int[][][] rgbPixelArray, Palette palette);

}
