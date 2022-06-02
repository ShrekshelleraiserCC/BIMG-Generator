import static java.lang.Math.sqrt;

public class Colors {
    public static int[] splitColor(int color) {
        color = color & 0xFFFFFF;
        return new int[]{(color & 0xFF0000) >> 16, (color & 0x00FF00) >> 8, color & 0x0000FF};
    }

    public static int combineColors(int red, int green, int blue) {
        red = red & 0xFF;
        green = green & 0xFF;
        blue = blue & 0xFF;
        return ((red << 16) + (green << 8) + blue);
    }

    public static int combineColors(int[] color) {
        return combineColors(color[0], color[1], color[2]);
    }

    public static int[] scaleArray(int[] array, float scale) {
        int[] newArr = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            newArr[i] = Math.round(array[i] * scale);
        }
        return newArr;
    }

    public static int[] addArrays(int[] array1, int[] array2) {
        return addArrays(array1, array2, 1);
    }
    public static int[] addArrays(int[] array1, int[] array2, int mul2) {
        if (array1.length != array2.length) {
            throw new RuntimeException("Arrays are of different lengths!");
        }
        int[] newArr = new int[array1.length];
        for (int i = 0; i < array1.length; i++) {
            newArr[i] = array1[i] + (mul2 * array2[i]);
        }
        return newArr;
    }

    public static double getDifferenceBetweenColors(int[] colorA, int[] colorB) {
        int[] diff = new int[3];
        for (int i = 0; i < 3; i++) {
            diff[i] = colorA[i] - colorB[i];
        }
        return sqrt(diff[0]*diff[0] + diff[1]*diff[1] + diff[2]*diff[2]);
    }
}
