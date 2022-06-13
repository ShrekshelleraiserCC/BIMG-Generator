import java.io.IOException;

public class NFP {
    private final IMode[] frames;

    NFP(IMode[] frames) {
        this.frames = frames;
    }

    public void save(String filename) throws IOException {
        StringBuilder fileDataStringBuilder = new StringBuilder();
        IMode frame = frames[0];
        String[][] frameString = frame.get();
        for (int lineIndex = 0; lineIndex < frameString.length; lineIndex++) {
            String[] line = frameString[lineIndex];
            fileDataStringBuilder.append(line[2]);
            if (lineIndex + 1 < frameString.length)
                fileDataStringBuilder.append('\n'); // don't add this to the last line
        }
        ImageMaker.writeToFile(filename, ImageMaker.stringToInt(fileDataStringBuilder.toString()));
    }
}
