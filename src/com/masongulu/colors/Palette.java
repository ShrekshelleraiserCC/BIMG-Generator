package com.masongulu.colors;

public class Palette {
    private Color[] colors;
    public static final Palette defaultPalette = new Palette(new Color[]{new Color(0xf0f0f0),
            new Color(0xf2b233), new Color(0xe57fd8), new Color(0x99b2f2), new Color(0xdede6c),
            new Color(0x7fcc19), new Color(0xf2b2cc), new Color(0x4c4c4c), new Color(0x999999),
            new Color(0x4c99b2), new Color(0xb266e5), new Color(0x3366cc), new Color(0x7f664c),
            new Color(0x57a64e), new Color(0xcc4c4c), new Color(0x111111)});

    public Palette(Color[] colors) {
        this.colors = colors;
    }

    public Color getColor(int index) {
        return colors[index];
    }

    public int getLength() {
        return colors.length;
    }

    public int getClosestPaletteIndex(Color color) {
        int closestIndex = 0;
        double closestDiff = 0xFFFFFF;
        for (int index = 0; index < colors.length; index++) {
            double colorDiff = color.diff(colors[index]);
            if (colorDiff < closestDiff) {
                closestDiff = colorDiff;
                closestIndex = index;
            }
        }
        return closestIndex;
    }

    public boolean equals(Palette palette) {
        if (this.getLength() == palette.getLength()) {
            for (int colorIndex = 0; colorIndex < this.getLength(); colorIndex++) {
                if (getColor(colorIndex) != palette.getColor(colorIndex))
                    return false;
            }
            return true;
        }
        return false;
    }
}
