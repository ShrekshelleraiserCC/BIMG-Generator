import java.io.IOException;

public class BIMG {
    private final IMode[] frames;
    private boolean metadataWritten = false;
    private boolean finalized = false;
    StringBuilder file = new StringBuilder();

    BIMG(IMode[] frames) {
        this.frames = frames;
        file.append("{"); // Open root BIMG table
        for (int frameIndex = 0; frameIndex < this.frames.length; frameIndex++) {
            IMode frame = this.frames[frameIndex];
            file.append("{"); // Open frame
            for (String[] line : frame.get()) {
                file.append("{\"").append(line[0])
                        .append("\",\"").append(line[1])
                        .append("\",\"").append(line[2]).append("\"},");
            }
            // insert palette stuff here
            if (frameIndex > 0 && !frame.getPalette().equals(frames[frameIndex-1].getPalette())) {
                // Not first frame and the palette differs from the last frame
                writePalette(frame.getPalette());
            }
            file.append("},"); // End frame
        }
        if (!frames[0].getPalette().equals(ImageMaker.defaultPalette)) {
            // First frame uses a non-default palette
            writePalette(frames[0].getPalette());
        }
    }

    public void save(String filename) throws IOException {
        writeMetadata();
        if (!finalized) {
            file.append("}"); // Close root BIMG table
            finalized = true;
        }
        ImageMaker.writeToFile(filename, ImageMaker.stringToInt(file.toString()));
    }

    private void appendString(String str) {
        if (!finalized)
            file.append("\"").append(str).append("\"");
    }

    public void writeKeyValuePair(String key, String value) {
        if (!finalized) {
            file.append(key).append("=");
            appendString(value);
            file.append(",");
        }
    }

    public void writeKeyValuePair(String key, boolean value) {
        if (!finalized) {
            file.append(key).append("=").append(value);
            file.append(",");
        }
    }

    public void writeKeyValuePair(String key, double value) {
        if (!finalized) {
            file.append(key).append("=").append(value);
            file.append(",");
        }
    }

    private void writePalette(Palette colors) {
        if (!finalized) {
            file.append("palette={");
            for (int colorIndex = 0; colorIndex < colors.getLength(); colorIndex++) {
                file.append("[").append((int)Math.pow(2, colorIndex)).append("]={");
                file.append(colors.getColor(colorIndex)).append("},");
            }
            file.append("},");
        }
    }

    public void writeMetadata() {
        if (!metadataWritten && !finalized) {
            final String version = "1.0.0";
            writeKeyValuePair("version", version);
            writeKeyValuePair("creator", "Java BIMG Generator");
            if (frames.length > 1)
                writeKeyValuePair("animation", true);
            metadataWritten = true;
        }
    }
}
