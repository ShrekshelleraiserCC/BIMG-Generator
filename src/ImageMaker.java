import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;

public class ImageMaker {
    static final int VERSION = -3;
    public static final Palette defaultPalette = new Palette(new int[]{0xf0f0f0, 0xf2b233, 0xe57fd8, 0x99b2f2, 0xdede6c,
            0x7fcc19, 0xf2b2cc, 0x4c4c4c, 0x999999, 0x4c99b2, 0xb266e5, 0x3366cc, 0x7f664c, 0x57a64e, 0xcc4c4c, 0x111111});
    static Palette palette = defaultPalette;

    enum IM_MODE {
        HD,
        LD,
        HD_AUTO,
        LD_AUTO
    }

    static IM_MODE mode = IM_MODE.LD;
    // default CC palette
    public static void main(String[] args) {
        boolean showHelp = false;
        boolean doDither = false;
        boolean savePostImage = false;
        double secondsPerFrame = 0.2;
        String postImagePath = "";
        CommandLine commandLine;
        Option option_postImageFile = Option.builder("post")
                .required(false)
                .desc("Output processed image to path")
                .hasArg(true)
                .build();
        Option option_doDither = Option.builder("d")
                .required(false)
                .desc("Do dithering")
                .longOpt("dither")
                .hasArg(false)
                .build();
        Option option_highDensity = Option.builder("hd")
                .required(false)
                .desc("High density")
                .hasArg(false)
                .build();
        Option option_customPalette = Option.builder("p")
                .required(false)
                .desc("Comma separated list of palette colors")
                .longOpt("palette")
                .hasArgs()
                .valueSeparator(',')
                .build();
        Option option_autoPalette = Option.builder("auto")
                .required(false)
                .desc("Automatically generate palette")
                .hasArg(false)
                .build();
        Option option_secondsPerFrame = Option.builder("spf")
                .required(false)
                .desc("Seconds per frame")
                .hasArg(true)
                .build();
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        options.addOption(option_postImageFile);
        options.addOption(option_doDither);
        options.addOption(option_highDensity);
        options.addOption(option_customPalette);
        options.addOption(option_autoPalette);
        options.addOption(option_secondsPerFrame);

        System.out.println("BIMG Image Generator version " + VERSION);

        try {
            commandLine = parser.parse(options, args);

            if (commandLine.hasOption("post")) {
                savePostImage = true;
                postImagePath = commandLine.getOptionValue("post");
            }

            if (commandLine.hasOption("d")) {
                doDither = true;
            }

            if (commandLine.hasOption("hd")) {
                mode = IM_MODE.HD;
            }

            if (commandLine.hasOption("auto")) {
                mode = switch (mode) {
                    case HD -> IM_MODE.HD_AUTO;
                    case LD -> IM_MODE.LD_AUTO;
                    case HD_AUTO, LD_AUTO -> null; // should be impossible to reach
                };
            } else if (commandLine.hasOption("p")) {
                try {
                    String[] paletteColors = commandLine.getOptionValues("p");
                    int[] paletteColorsInt = new int[paletteColors.length];
                    for (int index = 0; index < paletteColors.length; index++) {
                        paletteColorsInt[index] = Integer.decode(paletteColors[index]);
                    }
                    palette = new Palette(paletteColorsInt);
                } catch (NumberFormatException e) {
                    System.out.println("Incorrectly formatted palette. Example usage: ");
                    System.out.println("-p=1,2,0xFF0000");
                    return;
                }
            }

            if (commandLine.hasOption("spf"))
                secondsPerFrame = Double.parseDouble(commandLine.getOptionValue("spf"));
            args = commandLine.getArgs(); // reset args to contain JUST the needed information

        } catch (org.apache.commons.cli.ParseException exception) {
            showHelp = true;
        }
        if (args.length < 2) {
            showHelp = true;
        } else {
            try {
                BufferedImage[] imageArr;
                if (Objects.equals(FilenameUtils.getExtension(args[0]), "gif")) {
                    // terrible check for a gif file
                    imageArr = GifReader.openGif(new File(args[0]));
                } else {
                    imageArr = new BufferedImage[]{ImageIO.read(new File(args[0]))};
                }
                IMode[] im = new IMode[imageArr.length];
                for (int i = 0; i < imageArr.length; i++) {
                    BufferedImage inputImage = imageArr[i];
                    long startTime = System.nanoTime();
                    im[i] = switch (mode) {
                        case HD -> new ModeHighDensity(inputImage, palette, doDither);
                        case LD -> new ModeLowDensity(inputImage, palette, doDither);
                        case HD_AUTO -> new ModeHighDensity(inputImage, doDither);
                        case LD_AUTO -> new ModeLowDensity(inputImage, doDither);
                    };
                    long endTime = System.nanoTime();
                    System.out.println("Quantized image in " + (endTime - startTime)/1000000.0f + "ms.");
                    if (savePostImage) {
                        startTime = System.nanoTime();
                        if (imageArr.length > 1) {
                            ImageIO.write(im[i].getImage(), FilenameUtils.getExtension(postImagePath),
                                    new File(insertBeforeFileEx(postImagePath, String.valueOf(i))));
                        } else {
                            ImageIO.write(im[i].getImage(), FilenameUtils.getExtension(postImagePath),
                                    new File(postImagePath));
                        }
                        endTime = System.nanoTime();
                        System.out.println("Wrote post image in " + (endTime - startTime)/1000000.0f + "ms.");
                    }
                }
                long startTime = System.nanoTime();
                BIMG bimg = new BIMG(im);
                if (imageArr.length > 1)
                    bimg.writeKeyValuePair("secondsPerFrame", secondsPerFrame);
                bimg.save(args[1]);
                long endTime = System.nanoTime();
                System.out.println("Wrote bimg in " + (endTime - startTime)/1000000.0f + "ms.");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (showHelp) {
            // An issue has occurred with the args, print help and exit.
            // args should contain:
            // mode inputFile outputFile
            // Valid modes are:
            // 2b0L, 2b1L, 2b5L - 2bit per pixel palette/mode intensity
            // 2b0H, 2b1H, 2b5H
            // 1b - 1bit per pixel
            // 2Bo0, 2Bo1, 2bo - 2byte per pixel old/new subpalette
            // 2Bn0, 2Bn1, 2Bn
            String header = "Convert an image into an bimg file.\n\n";
            String footer = """
                    """;

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("BIMG input output",
                    header, options, footer, true);
        }
    }

    static void writeToFile(String filename, int[] data) throws IOException {
        FileOutputStream file = new FileOutputStream(filename);
        for (int datum : data) {
            file.write((byte) datum);
        }
        file.close();
    }

    static String insertBeforeFileEx(String filename, String insert) {
        String modFilename = FilenameUtils.removeExtension(filename) + insert;
        if (!Objects.equals(FilenameUtils.getExtension(filename), ""))
            modFilename += "." + FilenameUtils.getExtension(filename);
        return modFilename;
    }

    static int[] stringToInt(String str) {
        int[] data = new int[str.length()];
        for (int index = 0; index < str.length(); index++) {
            data[index] = str.charAt(index);
        }
        return data;
    }


}
