package com.masongulu.blit;

import com.masongulu.colors.Color;
import com.masongulu.colors.Palette;
import com.masongulu.colors.PaletteImage;

import java.util.Arrays;

public class BlitMap {
    private final BlitChar[][] map; // Y, X map
    private final Palette palette;

    /**
     * @param image PaletteImage
     * @param HD    boolean
     */
    public BlitMap(PaletteImage image, boolean HD) {
        palette = image.getPalette();
        if (HD) {
            int width = 2 * (image.getWidth() / 2);
            int height = 3 * (image.getHeight() / 3);
            map = new BlitChar[height / 3][width / 2];

            for (int y = 0; y < height - 2; y += 3) {
                for (int x = 0; x < width - 1; x += 2) {
                    map[y / 3][x / 2] = getHDChar(image, x, y);
                }
            }

        } else {
            int width = image.getWidth();
            int height = image.getHeight();
            map = new BlitChar[height][width];
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    int colIndex = image.getPixelIndex(x, y);
                    map[y][x] = new BlitChar(' ', colIndex, colIndex);
                }
            }
        }
    }

    private BlitChar getHDChar(PaletteImage image, int x, int y) {
        // will return a single character blit array ex {'\146', 'a', '0'}
        //                                                ^ except this is a character
        int[] indexFrequency = new int[16];
        Arrays.fill(indexFrequency, 0);
        int BGIndex = 0;
        int FGIndex = 0;
        for (int dx = 0; dx < 2; dx++) {
            for (int dy = 0; dy < 3; dy++) {
                int index = image.getPixelIndex(x + dx, y + dy);
                indexFrequency[index]++;
                if (indexFrequency[index] > indexFrequency[BGIndex]) {
                    FGIndex = BGIndex;
                    BGIndex = index;
                } else if (index != BGIndex && indexFrequency[index] > indexFrequency[FGIndex]) {
                    FGIndex = index;
                }
            }
        }
        int[] crossPaletteLookup = {FGIndex, BGIndex};
        Palette reducedPalette = new Palette(new Color[]{palette.getColor(FGIndex), palette.getColor(BGIndex)});

        int charCode = 0;
        for (int dy = 2; dy >= 0; dy--) {
            for (int dx = 1; dx >= 0; dx--) {
                int index = image.getPixelIndex(x + dx, y + dy);
                int reducedIndex = reducedPalette.getClosestPaletteIndex(new Color(image.getPixelColor(x + dx, y + dy)));
                image.setPixelIndex(x + dx, y + dy, crossPaletteLookup[reducedIndex]);
                charCode = charCode << 1;
                charCode += (reducedIndex == 0) ? 0 : 1;
            }
        }
        // For future reference to convert a 2x3 section of pixels to a 2x3 "braille" character
        // 0x80 + binary count of top left -> bottom right with MS -> LS
        // If LS bit is set invert upper bits
        if ((charCode & 0b100000) > 0) {
            // invert colors
            int tmp = BGIndex;
            BGIndex = FGIndex;
            FGIndex = tmp;
            charCode ^= 0b111111;
        }

//        if t[6] == 1 then for i = 1, 5 do t[i] = 1-t[i] end end
        // t ^ 0b011111
//        local n = 128
//        for i = 0, 4 do n = n + t[i+1]*2^i end
        // n += t & 0b011111
//        return n, t[6] == 1
        charCode += 0x80;
        return new BlitChar((char) charCode, BGIndex, FGIndex);
    }

    public String getCharacter(int line) {
        StringBuilder str = new StringBuilder();
        for (BlitChar bc : map[line]) {
            str.append(bc.getCharacter());
        }
        return str.toString();
    }

    public String getFG(int line) {
        StringBuilder str = new StringBuilder();
        for (BlitChar bc : map[line]) {
            str.append(bc.getFGChar());
        }
        return str.toString();
    }

    public String getBG(int line) {
        StringBuilder str = new StringBuilder();
        for (BlitChar bc : map[line]) {
            str.append(bc.getBGChar());
        }
        return str.toString();
    }

    public int getHeight() {
        return map.length;
    }

    public int getWidth() {
        return map[0].length;
    }

    public Palette getPalette() {
        return palette;
    }
}

class BlitChar {
    private char character;
    private int FG;
    private int BG;

    public BlitChar(char character, int FG, int BG) {
        this.character = character;
        this.FG = FG;
        this.BG = BG;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public int getFG() {
        return FG;
    }

    public void setFG(int FG) {
        this.FG = FG;
    }

    public char getFGChar() {
        return Integer.toHexString(FG).charAt(0);
    }

    public int getBG() {
        return BG;
    }

    public void setBG(int BG) {
        this.BG = BG;
    }

    public char getBGChar() {
        return Integer.toHexString(BG).charAt(0);
    }
}
