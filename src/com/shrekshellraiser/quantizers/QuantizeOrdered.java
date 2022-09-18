package com.shrekshellraiser.quantizers;

import com.shrekshellraiser.colors.Color;
import com.shrekshellraiser.colors.Palette;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class QuantizeOrdered extends QuantizeNone {
    private static final Map<Integer, double[][]> thresholdMap = new HashMap<>();

    static {
        thresholdMap.put(2, new double[][]{{0, 2}, {3, 1}});
        thresholdMap.put(4, new double[][]{{0, 8, 2, 10},
                {12, 4, 14, 6},
                {3, 11, 1, 9},
                {15, 7, 13, 5}
        });
        thresholdMap.put(8, new double[][]{
                {0, 32, 8, 40, 2, 34, 10, 42},
                {48, 16, 56, 24, 50, 18, 58, 26},
                {12, 44, 4, 36, 14, 46, 6, 38},
                {60, 28, 52, 20, 62, 30, 54, 22},
                {3, 35, 11, 43, 1, 33, 9, 41},
                {51, 19, 59, 27, 49, 17, 57, 25},
                {15, 47, 7, 39, 13, 45, 5, 37},
                {63, 31, 55, 23, 61, 29, 53, 21}
        });
    }

    private final JSpinner colorSpread = new JSpinner(new SpinnerNumberModel(50, 5, 200, 5));
    private final JComboBox<Integer> thresholdMapSelect = new JComboBox<>(new Integer[]{2, 4, 8});

    public QuantizeOrdered(Palette palette) {
        super(palette);
        frame = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        frame.add(new JLabel("Color spread"), c);
        c.gridx = 1;
        frame.add(colorSpread, c);

        c.gridx = 0;
        c.gridy = 1;
        frame.add(new JLabel("Threshold map"), c);
        c.gridx = 1;
        frame.add(thresholdMapSelect, c);
    }

    @Override
    protected int[][] applyDither(Color[][] RGBImage) {
        int[][] paletteArr = new int[RGBImage[0].length][RGBImage.length];
        int width = RGBImage[0].length;
        int height = RGBImage.length;
        double[][] thresholdMap = QuantizeOrdered.thresholdMap.get((Integer) thresholdMapSelect.getSelectedItem());
        int colorSpread = (int) this.colorSpread.getValue();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int colorOffset = (int) (colorSpread *
                        (thresholdMap[x % thresholdMap.length][y % thresholdMap[0].length] - 1 / 2.0));
                int paletteIndex = palette.getClosestPaletteIndex(
                        new Color(colorOffset, colorOffset, colorOffset).add(RGBImage[y][x]));

                paletteArr[x][y] = paletteIndex;
            }
        }
        return paletteArr;
    }
}
