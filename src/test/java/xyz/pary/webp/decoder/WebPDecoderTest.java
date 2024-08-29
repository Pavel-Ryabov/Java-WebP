package xyz.pary.webp.decoder;

import xyz.pary.webp.WebPFormat;
import xyz.pary.webp.Dimensions;
import xyz.pary.webp.WebPInfo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static xyz.pary.webp.TestImages.BLACK_10X10;
import static xyz.pary.webp.TestImages.BLACK_10X10_LOSSY;
import static xyz.pary.webp.TestImages.BLACK_10X10_WITH_ALPHA50;
import static xyz.pary.webp.TestImages.RAINBOW_8X8;
import static xyz.pary.webp.TestImages.RAINBOW_COLORS;
import static xyz.pary.webp.TestImages.WHITE_10X10;
import static xyz.pary.webp.TestImages.getTestFile;

public class WebPDecoderTest {

    private static WebPDecoder decoder;

    @BeforeAll
    private static void setUp() {
        decoder = new WebPDecoderFactory().createDecoder();
    }

    @Test
    public void testGetDimensions() throws Exception {
        Dimensions dimensions = decoder.getDimensions(getTestFile(BLACK_10X10));
        assertEquals(10, dimensions.getWidth());
        assertEquals(10, dimensions.getHeight());
    }

    @Test
    public void testGetInfo() throws Exception {
        WebPInfo info = decoder.getInfo(getTestFile(BLACK_10X10));
        assertEquals(10, info.getWidth());
        assertEquals(10, info.getHeight());
        assertEquals(WebPFormat.LOSSLESS, info.getFormat());
        assertFalse(info.isHasAlpha());
    }

    @Test
    public void testGetInfoWithAlpha() throws Exception {
        WebPInfo info = decoder.getInfo(getTestFile(BLACK_10X10_WITH_ALPHA50));
        assertEquals(10, info.getWidth());
        assertEquals(10, info.getHeight());
        assertEquals(WebPFormat.LOSSLESS, info.getFormat());
        assertTrue(info.isHasAlpha());
    }

    @Test
    public void testGetInfoWithLossy() throws Exception {
        WebPInfo info = decoder.getInfo(getTestFile(BLACK_10X10_LOSSY));
        assertEquals(10, info.getWidth());
        assertEquals(10, info.getHeight());
        assertEquals(WebPFormat.LOSSY, info.getFormat());
        assertFalse(info.isHasAlpha());
    }

    @Test
    public void testBlackRGB() throws Exception {
        byte[] rgb = decoder.decodeRGB(getTestFile(BLACK_10X10));
        checkSingleColor(rgb, new byte[]{0, 0, 0});
    }

    @Test
    public void testBlackARGB() throws Exception {
        byte[] argb = decoder.decodeARGB(getTestFile(BLACK_10X10));
        checkSingleColor(argb, new byte[]{(byte) 255, 0, 0, 0});
    }

    @Test
    public void testBlackWithAlpha50() throws Exception {
        byte[] argb = decoder.decodeARGB(getTestFile(BLACK_10X10_WITH_ALPHA50));
        checkSingleColor(argb, new byte[]{(byte) 128, 0, 0, 0});
    }

    @Test
    public void testWhite() throws Exception {
        byte[] rgb = decoder.decodeRGB(getTestFile(WHITE_10X10));
        checkSingleColor(rgb, new byte[]{(byte) 255, (byte) 255, (byte) 255});
    }

    @Test
    public void testRainbow() throws Exception {
        byte[] argb = decoder.decodeARGB(getTestFile(RAINBOW_8X8));
        checkColorByLines(argb, RAINBOW_COLORS, 8);
    }

    private void checkSingleColor(byte[] data, byte[] color) {
        for (int i = 0; i < data.length; i += color.length) {
            for (int j = 0; j < color.length; j++) {
                assertEquals(color[j], data[i + j]);
            }
        }
    }

    private void checkColorByLines(byte[] data, byte[][] colorLines, int width) {
        int lineWidth = width * colorLines[0].length;
        for (int line = 0; line < colorLines.length; line++) {
            byte[] color = colorLines[line];
            for (int lineElement = 0; lineElement < lineWidth; lineElement += color.length) {
                for (int component = 0; component < color.length; component++) {
                    int dataIndex = line * lineWidth + lineElement + component;
                    assertEquals(color[component], data[dataIndex], "index: " + dataIndex);
                }
            }
        }
    }

}
