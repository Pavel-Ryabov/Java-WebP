package xyz.pary.webp.imageio;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import xyz.pary.webp.WebPInfo;
import xyz.pary.webp.decoder.WebPDecoder;
import xyz.pary.webp.decoder.WebPDecoderException;
import xyz.pary.webp.decoder.WebPDecoderFactory;

public class WebPImageReader extends ImageReader {

    private final WebPDecoder decoder;

    private byte[] data;
    private WebPInfo info;
    private int[] pixels;

    public WebPImageReader(ImageReaderSpi irs) {
        super(irs);
        decoder = new WebPDecoderFactory().createDecoder();
    }

    @Override
    public void setInput(Object input, boolean seekForwardOnly, boolean ignoreMetadata) {
        Object prevInput = getInput();
        super.setInput(input, seekForwardOnly, ignoreMetadata);
        if (input == null || prevInput != input) {
            data = null;
            info = null;
            pixels = null;
        }
    }

    @Override
    public int getNumImages(boolean allowSearch) throws IOException {
        return 1;
    }

    @Override
    public int getWidth(int imageIndex) throws IOException {
        checkIndex(imageIndex);
        readInfo();
        return info.getWidth();
    }

    @Override
    public int getHeight(int imageIndex) throws IOException {
        checkIndex(imageIndex);
        readInfo();
        return info.getHeight();
    }

    @Override
    public Iterator<ImageTypeSpecifier> getImageTypes(int imageIndex) throws IOException {
        checkIndex(imageIndex);
        readInfo();
        List<ImageTypeSpecifier> imageTypes = new ArrayList<>(1);
        imageTypes.add(ImageTypeSpecifier.createFromBufferedImageType(getBufferedImageType()));
        return imageTypes.iterator();
    }

    @Override
    public IIOMetadata getStreamMetadata() throws IOException {
        return null;
    }

    @Override
    public IIOMetadata getImageMetadata(int imageIndex) throws IOException {
        checkIndex(imageIndex);
        return null;
    }

    @Override
    public BufferedImage read(int imageIndex, ImageReadParam param) throws IOException {
        checkIndex(imageIndex);
        clearAbortRequest();
        processImageStarted(imageIndex);
        if (param == null) {
            param = getDefaultReadParam();
        }

        decode();

        Rectangle sourceRegion = new Rectangle(0, 0, 0, 0);
        Rectangle destinationRegion = new Rectangle(0, 0, 0, 0);

        computeRegions(param, info.getWidth(), info.getHeight(),
                param.getDestination(),
                sourceRegion,
                destinationRegion);

        BufferedImage bi = param.getDestination();
        if (bi == null) {
            bi = new BufferedImage(destinationRegion.x + destinationRegion.width,
                    destinationRegion.y + destinationRegion.height, getBufferedImageType());
        }

        boolean noTransform = destinationRegion.equals(new Rectangle(0, 0, info.getWidth(), info.getHeight()))
                && destinationRegion.equals(new Rectangle(0, 0, bi.getWidth(), bi.getHeight()));

        WritableRaster tile = bi.getWritableTile(0, 0);

        if (noTransform) {
            if (abortRequested()) {
                processReadAborted();
                return bi;
            }

            DataBufferInt dataBuffer = (DataBufferInt) tile.getDataBuffer();
            System.arraycopy(pixels, 0, dataBuffer.getData(), 0, pixels.length);

            processImageUpdate(bi, 0, 0, info.getWidth(), info.getHeight(), 1, 1, new int[]{0});
            processImageProgress(100.0F);
        } else {
            throw new IOException("Transformation not supported");
        }

        if (abortRequested()) {
            processReadAborted();
        } else {
            processImageComplete();
        }
        return bi;
    }

    private void checkIndex(int imageIndex) {
        if (imageIndex != 0) {
            throw new IndexOutOfBoundsException("Bad image index");
        }
    }

    private void decode() throws IOException {
        if (pixels != null) {
            return;
        }
        readInfo();
        try {
            if (info.isHasAlpha()) {
                pixels = PixelUtils.toIntPixels(decoder.decodeARGB(data), true);
            } else {
                pixels = PixelUtils.toIntPixels(decoder.decodeRGB(data), false);
            }
        } catch (WebPDecoderException e) {
            throw new IOException(e);
        }
        data = null;
    }

    private void readInfo() throws IOException {
        if (info != null) {
            return;
        }
        try {
            readData();
        } catch (IOException e) {
            throw new IOException("Image reading error");
        }
        try {
            info = decoder.getInfo(data);
        } catch (WebPDecoderException e) {
            throw new IOException(e);
        }
    }

    private void readData() throws IOException {
        if (data != null) {
            return;
        }
        ImageInputStream iis = (ImageInputStream) getInput();
        if (iis == null) {
            throw new IllegalStateException("No input stream");
        }
        setStreamPositionToStart(iis);
        data = readToByteArray(iis);
    }

    private void setStreamPositionToStart(ImageInputStream iis) throws IOException {
        if (iis.getStreamPosition() != 0L) {
            if (isSeekForwardOnly()) {
                throw new IllegalStateException("Unable to read data completely");
            } else {
                iis.seek(0);
            }
        }
    }

    private byte[] readToByteArray(ImageInputStream iis) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024 * 4];
            int length;
            while ((length = iis.read(buffer)) > 0) {
                baos.write(buffer, 0, length);
            }
            return baos.toByteArray();
        }
    }

    private int getBufferedImageType() {
        return info.isHasAlpha() ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
    }

}
