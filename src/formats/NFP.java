package formats;

import modes.IMode;
import utils.Utils;

import java.io.File;
import java.io.IOException;

public class NFP implements IFormat {
    private final IMode[] frames;

    public NFP(IMode[] frames) {
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
        Utils.writeToFile(filename, Utils.stringToInt(fileDataStringBuilder.toString()));
    }

    public void save(File filename) throws IOException {
        StringBuilder fileDataStringBuilder = new StringBuilder();
        IMode frame = frames[0];
        String[][] frameString = frame.get();
        for (int lineIndex = 0; lineIndex < frameString.length; lineIndex++) {
            String[] line = frameString[lineIndex];
            fileDataStringBuilder.append(line[2]);
            if (lineIndex + 1 < frameString.length)
                fileDataStringBuilder.append('\n'); // don't add this to the last line
        }
        Utils.writeToFile(filename, Utils.stringToInt(fileDataStringBuilder.toString()));
    }
}
