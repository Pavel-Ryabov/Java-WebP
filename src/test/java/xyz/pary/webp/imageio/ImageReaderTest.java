package xyz.pary.webp.imageio;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static xyz.pary.webp.TestImages.RAINBOW_8X8;
import static xyz.pary.webp.TestImages.WHITE_10X10;
import static xyz.pary.webp.TestImages.getRainbowRGBA;
import static xyz.pary.webp.TestImages.getWhiteRGB;
import xyz.pary.webp.utils.Utils;

public class ImageReaderTest {

    @Test
    public void testReadWhite() throws Exception {
        BufferedImage bi = ImageIO.read(Utils.getResourceAsStream(WHITE_10X10));
        assertEquals(10, bi.getWidth());
        assertEquals(10, bi.getHeight());
        int[] pixels = bi.getData().getPixels(0, 0, 10, 10, new int[10 * 10 * 3]);
        assertArrayEquals(convertByteArrayToIntArray(getWhiteRGB()), pixels);
    }

    @Test
    public void testReadRainbow() throws Exception {
        BufferedImage bi = ImageIO.read(Utils.getResourceAsStream(RAINBOW_8X8));
        assertEquals(8, bi.getWidth());
        assertEquals(8, bi.getHeight());
        int[] pixels = bi.getData().getPixels(0, 0, 8, 8, new int[8 * 8 * 4]);
        assertArrayEquals(convertByteArrayToIntArray(getRainbowRGBA()), pixels);
    }

    @Test
    public void testScaleWhite() throws Exception {
        BufferedImage bi = ImageIO.read(Utils.getResourceAsStream(WHITE_10X10));
        BufferedImage scaledImage = toBufferedImage(bi.getScaledInstance(5, 5, Image.SCALE_DEFAULT), bi.getType());
        assertEquals(5, scaledImage.getWidth());
        assertEquals(5, scaledImage.getHeight());
        int[] pixels = scaledImage.getData().getPixels(0, 0, 5, 5, new int[5 * 5 * 3]);
        assertArrayEquals(convertByteArrayToIntArray(getWhiteRGB(), 3, 10, 10, 5, 5), pixels);
    }

    @Test
    public void testScaleRainbow() throws Exception {
        BufferedImage bi = ImageIO.read(Utils.getResourceAsStream(RAINBOW_8X8));
        BufferedImage scaledImage = toBufferedImage(bi.getScaledInstance(4, 8, Image.SCALE_DEFAULT), bi.getType());
        assertEquals(4, scaledImage.getWidth());
        assertEquals(8, scaledImage.getHeight());
        int[] pixels = scaledImage.getData().getPixels(0, 0, 4, 8, new int[4 * 8 * 4]);
        assertArrayEquals(convertByteArrayToIntArray(getRainbowRGBA(), 4, 8, 8, 4, 8), pixels);
    }

    @Test
    public void testSubimage() throws Exception {
        BufferedImage bi = ImageIO.read(Utils.getResourceAsStream(RAINBOW_8X8));
        BufferedImage subimage = bi.getSubimage(0, 0, 4, 8);
        assertEquals(4, subimage.getWidth());
        assertEquals(8, subimage.getHeight());
        int[] pixels = subimage.getData().getPixels(0, 0, 4, 8, new int[4 * 8 * 4]);
        assertArrayEquals(convertByteArrayToIntArray(getRainbowRGBA(), 4, 8, 8, 4, 8), pixels);
    }

    private int[] convertByteArrayToIntArray(byte[] byteArray) {
        int[] intArray = new int[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            intArray[i] = byteArray[i] & 0xFF;
        }
        return intArray;
    }

    private int[] convertByteArrayToIntArray(byte[] byteArray, int bpp, int sw, int sh, int dw, int dh) {
        List<Integer> ints = new ArrayList<>(bpp * dw * dh);
        for (int l = 0; l < dh; l++) {
            for (int i = 0; i < dw * bpp; i++) {
                ints.add(Integer.valueOf(byteArray[l * sw * bpp + i]) & 0xFF);
            }
        }
        return ints.stream().mapToInt(Integer::intValue).toArray();
    }

    private static BufferedImage toBufferedImage(Image image, int type) {
        BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        bi.getGraphics().drawImage(image, 0, 0, null);
        return bi;
    }

}
