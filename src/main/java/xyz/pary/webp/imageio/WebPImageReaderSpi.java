package xyz.pary.webp.imageio;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Locale;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

public class WebPImageReaderSpi extends ImageReaderSpi {

    private static final byte[] RIFF = new byte[]{'R', 'I', 'F', 'F'};
    private static final byte[] WEBP = new byte[]{'W', 'E', 'B', 'P'};
    private static final byte[] VP8 = new byte[]{'V', 'P', '8'};

    public WebPImageReaderSpi() {
        super(
                SpiSettings.VENDOR,
                SpiSettings.VERSION,
                SpiSettings.NAMES,
                SpiSettings.SUFFIXES,
                SpiSettings.MIME_TYPES,
                SpiSettings.READER_CLASS_NAME,
                SpiSettings.INPUT_TYPES,
                SpiSettings.WRITER_SPI_NAMES,
                SpiSettings.SUPPORTS_STANDARD_STREAM_METADATA_FORMAT,
                SpiSettings.NATIVE_STREAM_METADATA_FORMAT_NAME,
                SpiSettings.NATIVE_STREAM_METADATA_FORMAT_CLASS_NAME,
                SpiSettings.EXTRA_STREAM_METADATA_FORMAT_NAMES,
                SpiSettings.EXTRA_STREAM_METADATA_FORMAT_CLASS_NAMES,
                SpiSettings.SUPPORTS_STANDARD_IMAGE_METADATA_FORMAT,
                SpiSettings.NATIVE_IMAGE_METADATA_FORMAT_NAME,
                SpiSettings.NATIVE_IMAGE_METADATA_FORMAT_CLASS_NAME,
                SpiSettings.EXTRA_IMAGE_METADATA_FORMAT_NAMES,
                SpiSettings.EXTRA_IMAGE_METADATA_FORMAT_CLASS_NAMES
        );
    }

    @Override
    //WebP Container Specification https://developers.google.com/speed/webp/docs/riff_container
    public boolean canDecodeInput(Object source) throws IOException {
        if (!(source instanceof ImageInputStream)) {
            return false;
        }
        ImageInputStream imageStream = (ImageInputStream) source;
        ByteOrder originalByteOrder = imageStream.getByteOrder();
        imageStream.mark();
        try {
            imageStream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
            byte[] bytes = new byte[4];
            imageStream.readFully(bytes);
            if (!Arrays.equals(RIFF, bytes)) {
                return false;
            }
            imageStream.readFully(bytes); //skip file size
            imageStream.readFully(bytes);
            if (!Arrays.equals(WEBP, bytes)) {
                return false;
            }
            imageStream.readFully(bytes, 0, 3);
            if (VP8[0] != bytes[0] || VP8[1] != bytes[1] || VP8[2] != bytes[2]) {
                return false;
            }
        } finally {
            imageStream.setByteOrder(originalByteOrder);
            imageStream.reset();
        }
        return true;
    }

    @Override
    public ImageReader createReaderInstance(Object extension) throws IOException {
        return new WebPImageReader(this);
    }

    @Override
    public String getDescription(Locale locale) {
        return "WebP Image Reader";
    }

}
