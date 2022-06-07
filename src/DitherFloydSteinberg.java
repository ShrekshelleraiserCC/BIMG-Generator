public class DitherFloydSteinberg implements IDither {

    @Override
    public int applyDither(int x, int y, int[][][] rgbPixelArray, Palette palette) {
        int newColor = palette.getClosestPaletteIndex(rgbPixelArray[x][y]);
        int[] quantizationError = Colors.addArrays(rgbPixelArray[x][y],
                Colors.splitColor(palette.getColor(newColor)), -1);
        if (x + 1 < rgbPixelArray.length)
            rgbPixelArray[x + 1][y] = Colors.addArrays(rgbPixelArray[x + 1][y],
                    Colors.scaleArray(quantizationError, 7.0f / 16));
        if (y + 1 < rgbPixelArray[0].length) {
            rgbPixelArray[x][y + 1] = Colors.addArrays(rgbPixelArray[x][y + 1],
                    Colors.scaleArray(quantizationError, 5.0f / 16));
            if (x > 1)
                rgbPixelArray[x - 1][y + 1] = Colors.addArrays(rgbPixelArray[x - 1][y + 1],
                        Colors.scaleArray(quantizationError, 3.0f / 16));
            if (x + 1 < rgbPixelArray.length)
                rgbPixelArray[x + 1][y + 1] = Colors.addArrays(rgbPixelArray[x + 1][y + 1],
                        Colors.scaleArray(quantizationError, 1.0f / 16));
        }
        return newColor;
    }
}
