package com.masongulu.formats;

import com.masongulu.blit.BlitMap;

import java.io.File;

public abstract class FormatBase {
    public abstract void save(BlitMap[] blitMaps, File file);

    public abstract String toString();

    public boolean supportCharacters() {
        return true;
    }

    public boolean supportCustomPalette() {
        return true;
    }
}
