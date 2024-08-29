package xyz.pary.webp.encoder;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static xyz.pary.webp.TestImages.BLACK_10X10;
import static xyz.pary.webp.TestImages.BLACK_10X10_LOSSY;
import static xyz.pary.webp.TestImages.BLACK_10X10_WITH_ALPHA50;
import static xyz.pary.webp.TestImages.RAINBOW_8X8;
import static xyz.pary.webp.TestImages.WHITE_10X10;
import static xyz.pary.webp.TestImages.getBlackRGB;
import static xyz.pary.webp.TestImages.getBlackRGBA;
import static xyz.pary.webp.TestImages.getBlackWithAlpha50RGBA;
import static xyz.pary.webp.TestImages.getRainbowRGBA;
import static xyz.pary.webp.TestImages.getTestFile;
import static xyz.pary.webp.TestImages.getWhiteRGB;

public class WebPEncoderTest {

    private static WebPEncoder encoder;

    @BeforeAll
    private static void setUp() {
        encoder = new WebPEncoderFactory().createEncoder();
    }

    @Test
    public void testBlackRGB() throws Exception {
        byte[] data = encoder.encodeLosslessRGB(getBlackRGB(), 10, 10);
        assertArrayEquals(getTestFile(BLACK_10X10), data);
    }

    @Test
    public void testBlackRGBA() throws Exception {
        byte[] data = encoder.encodeLosslessRGBA(getBlackRGBA(), 10, 10);
        assertArrayEquals(getTestFile(BLACK_10X10), data);
    }

    @Test
    public void testBlackWithAlpha50() throws Exception {
        byte[] data = encoder.encodeLosslessRGBA(getBlackWithAlpha50RGBA(), 10, 10);
        assertArrayEquals(getTestFile(BLACK_10X10_WITH_ALPHA50), data);
    }

    @Test
    public void testBlackWithLossy() throws Exception {
        byte[] data = encoder.encodeRGB(getBlackRGB(), 10, 10, 50);
        assertArrayEquals(getTestFile(BLACK_10X10_LOSSY), data);
    }

    @Test
    public void testWhite() throws Exception {
        byte[] data = encoder.encodeLosslessRGB(getWhiteRGB(), 10, 10);
        assertArrayEquals(getTestFile(WHITE_10X10), data);
    }

    @Test
    public void testRainbow() throws Exception {
        byte[] data = encoder.encodeLosslessRGBA(getRainbowRGBA(), 8, 8);
        assertArrayEquals(getTestFile(RAINBOW_8X8), data);
    }

}
