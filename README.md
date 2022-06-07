# BIMG Generator

This is a java program to convert just about any format of image
to the [BIMG](https://github.com/SkyTheCodeMaster/bimg) blit image
format for ComputerCraft. Also has support for
[bbf](https://github.com/9551-Dev/BLBFOR), a binary blit image format.

GIF animations are supported, though I've noticed sometimes
frames go missing. Images are automatically scaled to fit within
102x57 in HD and 51x19 in LD, the default resolution of a monitor.
If the image already fits within that range, it is not scaled.

Compiled on Java 17 using
[Apache Commons CLI](https://commons.apache.org/proper/commons-cli/) 1.5
and [Apache Commons IO](https://commons.apache.org/proper/commons-io/) 2.11.

```
usage: BIMG <input> <output> [-auto] [-bbf] [-d] [-hd] [-ordered <arg>]
       [-p <arg>] [-post <arg>] [-spf <arg>] [--uncapresolution]
Convert an image into an bimg file.

 -auto                  Automatically generate palette
 -bbf                   Save output in bbf format
 -d,--dither            Do Floyd-Steinberg dithering
 -hd                    High density
 -ordered <arg>         Do ordered dithering
 -p,--palette <arg>     Comma separated list of palette colors
 -post <arg>            Output processed image to path
 -spf <arg>             Seconds per frame
    --uncapresolution   Uncap the resolution
```