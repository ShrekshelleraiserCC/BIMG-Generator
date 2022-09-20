package com.masongulu.formats;

import com.masongulu.blit.BlitMap;
import com.masongulu.colors.Palette;
import com.masongulu.utils.LuaTable;
import com.masongulu.utils.Utils;

import java.io.File;
import java.io.IOException;

public class FormatBIMG extends FormatBase {
    @Override
    public void save(BlitMap[] blitMaps, File filename) {
        LuaTable table = new LuaTable();
        for (int frameIndex = 0; frameIndex < blitMaps.length; frameIndex++) {
            BlitMap frame = blitMaps[frameIndex];
            LuaTable frameTable = new LuaTable();
            for (int line = 0; line < frame.getHeight(); line++) {
                LuaTable lineTable = new LuaTable();
                lineTable.put(frame.getCharacter(line));
                lineTable.put(frame.getFG(line));
                lineTable.put(frame.getBG(line));
                frameTable.put(lineTable);
            }
            // insert palette stuff here
            if (frameIndex > 0 && !frame.getPalette().equals(blitMaps[frameIndex - 1].getPalette())) {
                // Not first frame and the palette differs from the last frame
                frameTable.put("palette", writePalette(frame.getPalette()));
            }
            table.put(frameTable);
        }
        if (!blitMaps[0].getPalette().equals(Palette.defaultPalette)) {
            // First frame uses a non-default palette
            table.put("palette", writePalette(blitMaps[0].getPalette()));
        }
        final String version = "1.0.0";
        table.put("version", version);
        table.put("creator", "BIMG Generator");
        if (blitMaps.length > 1)
            table.put("animated", true);
        try {
            Utils.writeToFile(filename, Utils.stringToInt(table.toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private LuaTable writePalette(Palette colors) {
        LuaTable paletteTable = new LuaTable();
        for (int colorIndex = 0; colorIndex < colors.getLength(); colorIndex++) {
            LuaTable paletteColorTable = new LuaTable();
            paletteColorTable.put(colors.getColor(colorIndex));
            paletteTable.put(colorIndex, paletteColorTable);
        }
        return paletteTable;
    }

    @Override
    public String toString() {
        return "bimg";
    }
}
