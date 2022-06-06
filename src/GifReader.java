import org.w3c.dom.*;
import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.*;
import java.io.*;
import java.util.*;

public class GifReader {
    public static BufferedImage[] openGif(File file) {
        BufferedImage[] frames = new BufferedImage[0];
        int length = 0;
        int framesProcessed = 0;
        int noi = 0;
        try {
            String[] imageatt = new String[]{
                    "imageLeftPosition",
                    "imageTopPosition",
                    "imageWidth",
                    "imageHeight"
            };

            ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
            ImageInputStream ciis = ImageIO.createImageInputStream(file);
            reader.setInput(ciis, false);

            noi = reader.getNumImages(true);
            BufferedImage master = null;
            frames = new BufferedImage[noi];
            for (framesProcessed = 0; framesProcessed < noi; framesProcessed++) {
                BufferedImage image = reader.read(framesProcessed);
                IIOMetadata metadata = reader.getImageMetadata(framesProcessed);

                Node tree = metadata.getAsTree("javax_imageio_gif_image_1.0");
                NodeList children = tree.getChildNodes();

                for (int j = 0; j < children.getLength(); j++) {
                    Node nodeItem = children.item(j);

                    if (nodeItem.getNodeName().equals("ImageDescriptor")) {
                        Map<String, Integer> imageAttr = new HashMap<>();

                        for (String s : imageatt) {
                            NamedNodeMap attr = nodeItem.getAttributes();
                            Node attnode = attr.getNamedItem(s);
                            imageAttr.put(s, Integer.valueOf(attnode.getNodeValue()));
                        }
                        if (framesProcessed == 0) {
                            master = new BufferedImage(imageAttr.get("imageWidth"), imageAttr.get("imageHeight"),
                                    BufferedImage.TYPE_INT_ARGB);
                        }
                        master.getGraphics().drawImage(image, imageAttr.get("imageLeftPosition"),
                                imageAttr.get("imageTopPosition"), null);

                    }
                }
                frames[framesProcessed] = copyImage(master);
                length = framesProcessed;
            }
        } catch (IOException e) {
            System.out.println("<eof> reached after reading " + framesProcessed + " frames (of suppposed "
                    + noi + " frames)");
        }
        BufferedImage[] rFrames = new BufferedImage[length];
        System.arraycopy(frames, 0, rFrames, 0, length);
        return rFrames;
    }

    public static BufferedImage copyImage(BufferedImage bi){
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}