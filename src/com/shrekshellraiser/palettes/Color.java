package com.shrekshellraiser.palettes;

import static java.lang.Math.sqrt;

public class Color {
    int r;
    int g;
    int b;

    public Color(int R, int G, int B) {
        r = R;
        g = G;
        b = B;
    }

    public Color(int[] rgb) {
        this(rgb[0], rgb[1], rgb[2]);
    }

    public Color(int rgb) {
        this(conv(rgb));
    }

    public Color(Color color) {
        this(color.r, color.g, color.b);
    }

    private static int conv(int[] rgb) {
        return (((rgb[0] & 0xFF) << 16) +
                ((rgb[1] & 0xFF) << 8) +
                (rgb[2] & 0xFF));
    }

    private static int[] conv(int rgb) {
        return new int[]{
                (rgb & 0xFF0000) >> 16,
                (rgb & 0x00FF00) >> 8,
                (rgb & 0x0000FF)
        };
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Color tmp) {
            // compare colors
            return (r == tmp.r && g == tmp.g && b == tmp.b);
        }
        return false;
    }

    public int[] getColorArr() {
        return new int[]{r, g, b};
    }

    public int getColor() {
        return conv(getColorArr());
    }

    public Color multiply(float scale) {
        return new Color((int) (r * scale), (int) (g * scale), (int) (b * scale));
    }

    public Color add(Color c, int scale) {
        return new Color(
                r + c.r * scale,
                g + c.g * scale,
                b + c.b * scale);
    }

    public Color add(Color c) {
        return add(c, 1);
    }

    @Override
    public String toString() {
        return String.valueOf(conv(new int[]{r, g, b}));
    }

    public double diff(Color c) {
        int[] diff = new int[3];
        diff[0] = r - c.r;
        diff[1] = g - c.g;
        diff[2] = b - c.b;
        return sqrt((diff[0] * diff[0]) + (diff[1] * diff[1]) + (diff[2] * diff[2]));
    }
}
