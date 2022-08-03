package com.shrekshellraiser.dithers;

import com.shrekshellraiser.palettes.Color;
import com.shrekshellraiser.palettes.Palette;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class DitherBlueNoise implements IDither {

    private BufferedImage noise;
    private int colorSpread;

    public DitherBlueNoise(int colorSpread) {
        this.colorSpread = colorSpread;
        try {
            noise = ImageIO.read(getClass().getResource("/resources/bluenoise.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int applyDither(int x, int y, Color[][] rgbPixelArray, Palette palette) {
        return 0; // TODO
    }
}
