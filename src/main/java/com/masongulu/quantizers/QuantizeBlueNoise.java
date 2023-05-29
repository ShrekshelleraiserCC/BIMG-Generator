package com.masongulu.quantizers;

import com.masongulu.colors.Color;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class QuantizeBlueNoise extends QuantizeNone {
    private final JSpinner colorSpread = new JSpinner(new SpinnerNumberModel(50, 5, 200, 5));
    private final BufferedImage noise;

    public QuantizeBlueNoise() {
        panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("Color spread"), c);
        c.gridx = 1;
        panel.add(colorSpread, c);
        try {
            noise = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/bluenoise.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected int[][] applyDither(Color[][] RGBImage) {
        int[][] paletteArr = new int[RGBImage.length][RGBImage[0].length];
        int width = RGBImage.length;
        int height = RGBImage[0].length;
        int colorSpread = (int) this.colorSpread.getValue();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int colorOffset = (int) (new Color(noise.getRGB(x % noise.getWidth(),
                        y % noise.getHeight())).diff(new Color(0)) / 445 * colorSpread);
                int colIndex = palette.getClosestPaletteIndex(
                        RGBImage[x][y].add(new Color(colorOffset, colorOffset, colorOffset)));
                paletteArr[x][y] = colIndex;
            }
        }
        return paletteArr;
    }

    @Override
    public String toString() {
        return "Blue Noise";
    }
}
