import java.io.IOException;

public class BBF {
    private final IMode[] frames;

    BBF(IMode[] frames) {
        this.frames = frames;
    }

    public void save(String filename) throws IOException {
        StringBuilder fileDataStringBuilder = new StringBuilder();
        fileDataStringBuilder.append("BLBFOR1\n").append(frames[0].getWidth()).append("\n")
                .append(frames[0].getHeight()).append("\n")
                .append(frames.length).append("\n")
                .append(System.currentTimeMillis()).append("\n"); // This is supposed to be os.epoch("utc") in lua
        if (frames[0].getPalette().equals(ImageMaker.defaultPalette))
            fileDataStringBuilder.append("{}").append("\n"); // this is supposed to be a "meta" parameter
        else {
            fileDataStringBuilder.append("{\"palette\":["); // Open meta
            for (IMode frame : frames) {
                fileDataStringBuilder.append("{");
                Palette palette = frames[0].getPalette();
                for (int i = 0; i < 16; i++) {
                    fileDataStringBuilder.append("\"").append((int) Math.pow(2, i)).append("\":")
                            .append(palette.getColor(i));
                    if (i != 15)
                        fileDataStringBuilder.append(",");
                }
                fileDataStringBuilder.append("}");
                if (frame != frames[frames.length - 1])
                    fileDataStringBuilder.append(",");
            }
            fileDataStringBuilder.append("]}\n");
        }
        for (IMode frame : frames) {
            String[][] frameString = frame.get();
            for (String[] line : frameString) {
                for (int charIndex = 0; charIndex < line[0].length(); charIndex++) {
                    fileDataStringBuilder.append(line[0].charAt(charIndex));
                    int FG = Character.digit(line[1].charAt(charIndex), 16);
                    int BG = Character.digit(line[2].charAt(charIndex), 16);
                    fileDataStringBuilder.append((char) ((FG << 4) + BG));
                }
            }
        }
        ImageMaker.writeToFile(filename, ImageMaker.stringToInt(fileDataStringBuilder.toString()));
    }
}
