package com.shrekshellraiser.utils;

import org.apache.commons.io.FilenameUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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

    public static BufferedImage resizeImageLinear(BufferedImage image, int width, int height) {
        BufferedImage inputImage = Utils.copyImage(image);
        if (inputImage.getWidth() > width) {
            double scale = width / (double) inputImage.getWidth();
            inputImage = scaleImage(inputImage, scale, scale);
        }
        if (inputImage.getHeight() > height) {
            double scale = height / (double) inputImage.getHeight();
            inputImage = scaleImage(inputImage, scale, scale);
        }
        return inputImage;
    }

    public static BufferedImage scaleImage(BufferedImage before, double scaleX, double scaleY) {
        int w = before.getWidth();
        int h = before.getHeight();
        BufferedImage after = new BufferedImage((int) (w * scaleX), (int) (h * scaleY), BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scaleX, scaleY);
        AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        after = scaleOp.filter(before, after);
        // https://stackoverflow.com/questions/4216123/how-to-scale-a-bufferedimage
        return after;
    }

    public static BufferedImage resizeImage(BufferedImage image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return resizedImage;
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
