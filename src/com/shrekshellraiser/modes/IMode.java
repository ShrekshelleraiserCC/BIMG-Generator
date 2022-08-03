package com.shrekshellraiser.modes;

import com.shrekshellraiser.palettes.Palette;

import java.awt.image.BufferedImage;

public interface IMode {
    String[][] get();

    BufferedImage getImage();

    Palette getPalette();

    int getWidth();

    int getHeight();
}
