package xyz.pary.webp.imageio;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class PixelUtilsTest {

    @Test
    public void testRGBToIntPixels() {
        byte[] rgb = new byte[]{0, 0, 0, (byte) 128, (byte) 128, (byte) 128, (byte) 255, (byte) 255, (byte) 255};
        int[] intRgb = new int[]{0x0, 0x00808080, 0x00ffffff};
        assertArrayEquals(intRgb, PixelUtils.toIntPixels(rgb, false));
    }

    @Test
    public void testARGBToIntPixels() {
        byte[] argb = new byte[]{0, 0, 0, 0, (byte) 128, (byte) 128, (byte) 128, (byte) 128, (byte) 255, (byte) 255, (byte) 255, (byte) 255};
        int[] intArgb = new int[]{0x0, 0x80808080, 0xffffffff};
        assertArrayEquals(intArgb, PixelUtils.toIntPixels(argb, true));
    }

    @Test
    public void testRgbToInt() {
        assertEquals("00000000", toHex(PixelUtils.rgbToInt((byte) 0, (byte) 0, (byte) 0)));
        assertEquals("00ffffff", toHex(PixelUtils.rgbToInt((byte) 255, (byte) 255, (byte) 255)));
    }

    @Test
    public void testArgbToInt() {
        assertEquals("ffffffff", toHex(PixelUtils.argbToInt((byte) 255, (byte) 255, (byte) 255, (byte) 255)));
        assertEquals("80808080", toHex(PixelUtils.argbToInt((byte) 128, (byte) 128, (byte) 128, (byte) 128)));
    }

    private String toHex(int v) {
        return String.format("%08x", v);
    }

}
