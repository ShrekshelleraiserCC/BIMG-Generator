package modes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Mode {
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

    public static void mergeAtIndex(int[] main, int[] secondary, int index) {
        System.arraycopy(secondary, 0, main, index, secondary.length);
    }

    public enum IM_MODE {
        HD,
        LD,
        HD_AUTO,
        LD_AUTO
    }

    public static int[] mergeAtIndexC(int[] main, int[] secondary, int index) {
        int[] ret = Arrays.copyOf(main, main.length);
        mergeAtIndex(ret, secondary, index);
        return ret;
    }

    public static BufferedImage resizeImage(BufferedImage image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return resizedImage;
    }
}
