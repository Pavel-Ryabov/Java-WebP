package xyz.pary.webp.imageio;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import xyz.pary.webp.TestImages;
import static xyz.pary.webp.TestImages.RAINBOW_8X8;
import static xyz.pary.webp.TestImages.WHITE_10X10;
import static xyz.pary.webp.TestImages.getRainbowRGBA;
import static xyz.pary.webp.TestImages.getWhiteRGB;
import xyz.pary.webp.WebPFormat;
import xyz.pary.webp.encoder.WebPEncoder;
import xyz.pary.webp.encoder.WebPEncoderFactory;
import xyz.pary.webp.utils.Utils;

public class ImageWriterTest {

    private static WebPEncoder encoder;

    @BeforeAll
    private static void setUp() {
        encoder = new WebPEncoderFactory().createEncoder();
    }

    @TempDir
    private Path tempDir;

    @Test
    public void testWriteRGBA() throws Exception {
        BufferedImage bi = ImageIO.read(Utils.getResourceAsStream(RAINBOW_8X8));
        File targetFile = Paths.get(tempDir.toString(), RAINBOW_8X8).toFile();
        ImageIO.write(bi, "webp", targetFile);
        assertImageEquals(RAINBOW_8X8, targetFile.getAbsolutePath());
    }

    @Test
    public void testWriteRGB() throws Exception {
        BufferedImage bi = ImageIO.read(Utils.getResourceAsStream(WHITE_10X10));
        File targetFile = Paths.get(tempDir.toString(), WHITE_10X10).toFile();
        ImageIO.write(bi, "webp", targetFile);
        assertImageEquals(encoder.encodeLosslessRGB(getWhiteRGB(), 10, 10), targetFile.getAbsolutePath());
    }

    @Test
    public void testWriteLossy() throws Exception {
        BufferedImage bi = ImageIO.read(Utils.getResourceAsStream(RAINBOW_8X8));
        File targetFile = Paths.get(tempDir.toString(), RAINBOW_8X8).toFile();
        try (FileImageOutputStream out = new FileImageOutputStream(targetFile)) {
            ImageWriter writer = ImageIO.getImageWritersByFormatName("webp").next();
            writer.setOutput(out);
            WebPImageWriteParam writeParam = new WebPImageWriteParam();
            writeParam.setCompressionType(WebPFormat.LOSSY);
            writeParam.setCompressionQuality(0.9F);
            IIOImage image = new IIOImage(bi, null, null);
            writer.write(null, image, writeParam);
        }
        assertImageEquals(encoder.encodeRGBA(getRainbowRGBA(), 8, 8, 90F), targetFile.getAbsolutePath());
    }

    @Test
    public void testConvertFromJpeg() throws Exception {
        BufferedImage bi = ImageIO.read(Utils.getResourceAsStream(RAINBOW_8X8.replace(".webp", ".jpg")));
        File targetFile = Paths.get(tempDir.toString(), RAINBOW_8X8).toFile();
        ImageIO.write(bi, "webp", targetFile);
        assertImageEquals(encoder.encodeLosslessRGB(getPixels(bi), 8, 8), targetFile.getAbsolutePath());
    }

    @Test
    public void testConvertFromPng() throws Exception {
        BufferedImage bi = ImageIO.read(Utils.getResourceAsStream(RAINBOW_8X8.replace(".webp", ".png")));
        File targetFile = Paths.get(tempDir.toString(), RAINBOW_8X8).toFile();
        ImageIO.write(bi, "webp", targetFile);
        assertImageEquals(RAINBOW_8X8, targetFile.getAbsolutePath());
    }

    @Test
    public void testConvertFromGrayPng() throws Exception {
        BufferedImage bi = ImageIO.read(Utils.getResourceAsStream(RAINBOW_8X8.replace(".webp", "_gray.png")));
        File targetFile = Paths.get(tempDir.toString(), RAINBOW_8X8).toFile();
        ImageIO.write(bi, "webp", targetFile);
        assertImageEquals(encoder.encodeLosslessRGBA(getPixels(bi), 8, 8), targetFile.getAbsolutePath());
    }

    @Test
    public void testConvertFromBmp() throws Exception {
        BufferedImage bi = ImageIO.read(Utils.getResourceAsStream(RAINBOW_8X8.replace(".webp", ".bmp")));
        File targetFile = Paths.get(tempDir.toString(), RAINBOW_8X8).toFile();
        ImageIO.write(bi, "webp", targetFile);
        assertImageEquals(RAINBOW_8X8, targetFile.getAbsolutePath());
    }

    @Test
    public void testWriteScaled() throws Exception {
        BufferedImage bi = ImageIO.read(Utils.getResourceAsStream(RAINBOW_8X8));
        BufferedImage scaledImage = toBufferedImage(bi.getScaledInstance(4, 8, Image.SCALE_DEFAULT), bi.getType());
        File targetFile = Paths.get(tempDir.toString(), RAINBOW_8X8).toFile();
        ImageIO.write(scaledImage, "webp", targetFile);
        assertImageEquals(encoder.encodeLosslessRGBA(getPixels(scaledImage), 4, 8), targetFile.getAbsolutePath());
    }

    @Test
    public void testWriteSubimage() throws Exception {
        BufferedImage bi = ImageIO.read(Utils.getResourceAsStream(RAINBOW_8X8));
        BufferedImage subimage = bi.getSubimage(0, 0, 4, 8);
        File targetFile = Paths.get(tempDir.toString(), RAINBOW_8X8).toFile();
        ImageIO.write(subimage, "webp", targetFile);
        assertImageEquals(encoder.encodeLosslessRGBA(getPixels(subimage), 4, 8), targetFile.getAbsolutePath());
    }

    private static BufferedImage toBufferedImage(Image image, int type) {
        BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        bi.getGraphics().drawImage(image, 0, 0, null);
        return bi;
    }

    private static void assertImageEquals(String expected, String actual) throws Exception {
        byte[] expectedBytes = TestImages.getTestFile(expected);
        byte[] actualBytes = getTempFile(actual);
        assertImageEquals(expectedBytes, actualBytes);
    }

    private static void assertImageEquals(byte[] expected, String actual) throws Exception {
        byte[] actualBytes = getTempFile(actual);
        assertImageEquals(expected, actualBytes);
    }

    private static void assertImageEquals(byte[] expected, byte[] actual) throws Exception {
        assertArrayEquals(expected, actual);
    }

    private static byte[] getTempFile(String path) throws IOException {
        try (InputStream is = new FileInputStream(path); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Utils.transfer(is, baos);
            return baos.toByteArray();
        }
    }

    private static byte[] getPixels(BufferedImage bi) {
        return PixelUtils.getPixels(bi.getRaster(), bi.getColorModel());
    }
}
