# BIMG Generator

This is a java program to convert just about any format of image
to the [BIMG](https://github.com/SkyTheCodeMaster/bimg) blit image
format for ComputerCraft. Also has support for
[bbf](https://github.com/9551-Dev/BLBFOR), a binary blit image format.

GIF animations are supported, though I've noticed sometimes
frames go missing. You can optionally scale images to fit on a default terminal,
or any larger terminal by putting in the screen resolution in characters.

Compiled on Java 17 using
[Apache Commons CLI](https://commons.apache.org/proper/commons-cli/) 1.5

and [Apache Commons IO](https://commons.apache.org/proper/commons-io/) 2.11.

## Usage

To use this program simply run it. If you want to use the CLI version, simply pass in any arguments.

After you make changes in your setting push the refresh button in the top right to refresh
the preview.

The save preview button will save the current preview as an image,
in the case of gifs this only saves the first frame.

The save button will save the image in
the specified format, matching the settings that are currently in the preview.