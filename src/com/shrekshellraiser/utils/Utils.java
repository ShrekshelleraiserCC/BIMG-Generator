package com.shrekshellraiser.utils;

import org.apache.commons.io.FilenameUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class Utils {
    public static void writeToFile(String filename, int[] data) throws IOException {
        FileOutputStream file = new FileOutputStream(filename);
        for (int datum : data) {
            file.write((byte) datum);
        }
        file.close();
    }

    public static void writeToFile(File file, int[] data) throws IOException {
        FileOutputStream fileStream = new FileOutputStream(file);
        for (int datum : data) {
            fileStream.write((byte) datum);
        }
        fileStream.close();
    }

    public static String insertBeforeFileEx(String filename, String insert) {
        String modFilename = FilenameUtils.removeExtension(filename) + insert;
        if (!Objects.equals(FilenameUtils.getExtension(filename), ""))
            modFilename += "." + FilenameUtils.getExtension(filename);
        return modFilename;
    }

    public static int[] stringToInt(String str) {
        int[] data = new int[str.length()];
        for (int index = 0; index < str.length(); index++) {
            data[index] = str.charAt(index);
        }
        return data;
    }

    // https://stackoverflow.com/questions/13783295/getting-all-names-in-an-enum-as-a-string
    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }

    // https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
    public static BufferedImage copyImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics2D g = b.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }
}
