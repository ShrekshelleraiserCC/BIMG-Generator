package com.shrekshellraiser.dithers;

import com.shrekshellraiser.palettes.Color;
import com.shrekshellraiser.palettes.Palette;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class DitherBlueNoise implements IDither {

    private BufferedImage noise;
    private double colorSpread;

    public DitherBlueNoise(double colorSpread) {
        this.colorSpread = colorSpread;
        try {
            noise = ImageIO.read(getClass().getResource("/resources/images/bluenoise.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int applyDither(int x, int y, Color[][] rgbPixelArray, Palette palette) {
        int colorOffset = (int) (new Color(noise.getRGB(x % noise.getWidth(), y % noise.getHeight()))
                .diff(new Color(0)) / 445 * colorSpread);
        return palette.getClosestPaletteIndex(rgbPixelArray[x][y].add(new Color(colorOffset, colorOffset, colorOffset)));
    }
}
