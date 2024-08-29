package xyz.pary.webp;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import xyz.pary.webp.utils.Utils;

public class TestImages {

    public static final String BLACK_10X10 = "black_10x10.webp";
    public static final String WHITE_10X10 = "white_10x10.webp";
    public static final String BLACK_10X10_WITH_ALPHA50 = "black_10x10_alpha50.webp";
    public static final String BLACK_10X10_LOSSY = "black_10x10_lossy.webp";
    public static final String RAINBOW_8X8 = "rainbow_8x8.webp";

    public static final byte[][] RAINBOW_COLORS = new byte[][]{
        {(byte) 255, (byte) 255, (byte) 0, (byte) 0},
        {(byte) 255, (byte) 255, (byte) 165, (byte) 0},
        {(byte) 255, (byte) 255, (byte) 255, (byte) 0},
        {(byte) 255, (byte) 0, (byte) 255, (byte) 0},
        {(byte) 255, (byte) 0, (byte) 191, (byte) 255},
        {(byte) 255, (byte) 0, (byte) 0, (byte) 255},
        {(byte) 255, (byte) 105, (byte) 0, (byte) 198},
        {(byte) 0, (byte) 0, (byte) 0, (byte) 0}
    };

    public static byte[] getTestFile(String name) throws Exception {
        try (InputStream is = Utils.getResourceAsStream(name); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Utils.transfer(is, baos);
            return baos.toByteArray();
        }
    }

    public static byte[] getBlackRGB() {
        return createSingleColorData(10, 10, new byte[]{0, 0, 0});
    }

    public static byte[] getBlackRGBA() {
        return createSingleColorData(10, 10, new byte[]{0, 0, 0, (byte) 255});
    }

    public static byte[] getBlackWithAlpha50RGBA() {
        return createSingleColorData(10, 10, new byte[]{0, 0, 0, (byte) 128});
    }

    public static byte[] getWhiteRGB() {
        return createSingleColorData(10, 10, new byte[]{(byte) 255, (byte) 255, (byte) 255});
    }

    public static byte[] getRainbowRGBA() {
        return createDataByColorLines(8, RAINBOW_COLORS, true);
    }

    private static byte[] createSingleColorData(int width, int height, byte[] color) {
        int bpp = color.length;
        byte[] data = new byte[width * height * bpp];
        for (int h = 0; h < height; h++) {
            int hdx = h * width * bpp;
            for (int w = 0; w < width; w++) {
                for (int c = 0; c < bpp; c++) {
                    data[hdx + w * bpp + c] = color[c];
                }
            }
        }
        return data;
    }

    private static byte[] createDataByColorLines(int width, byte[][] colorLines, boolean moveAlpha) {
        int height = colorLines.length;
        int bpp = colorLines[0].length;
        byte[] data = new byte[width * height * bpp];
        for (int l = 0; l < height; l++) {
            int ldx = l * width * bpp;
            for (int w = 0; w < width; w++) {
                for (int c = 0; c < bpp; c++) {
                    int index = ldx + w * bpp + c;
                    if (moveAlpha) {
                        int o = c + 1;
                        if (o == bpp) {
                            o = 0;
                        }
                        data[index] = colorLines[l][o];
                    } else {
                        data[index] = colorLines[l][c];
                    }
                }
            }
        }
        return data;
    }
}
