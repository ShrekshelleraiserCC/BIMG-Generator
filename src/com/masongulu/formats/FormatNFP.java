package com.masongulu.formats;

import com.masongulu.blit.BlitMap;
import com.masongulu.utils.Utils;

import java.io.File;
import java.io.IOException;

public class FormatNFP extends FormatBase {
    @Override
    public void save(BlitMap[] blitMaps, File file) {
        StringBuilder fileDataStringBuilder = new StringBuilder();
        BlitMap frame = blitMaps[0];
        for (int line = 0; line < frame.getHeight(); line++) {
            fileDataStringBuilder.append(frame.getBG(line)).append("\n");
        }
        try {
            Utils.writeToFile(file, Utils.stringToInt(fileDataStringBuilder.toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return "nfp";
    }

    @Override
    public boolean supportCharacters() {
        return false;
    }

    @Override
    public boolean supportCustomPalette() {
        return false;
    }
}
