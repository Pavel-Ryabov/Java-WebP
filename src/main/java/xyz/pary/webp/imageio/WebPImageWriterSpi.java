package xyz.pary.webp.imageio;

import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.util.Locale;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;

public class WebPImageWriterSpi extends ImageWriterSpi {

    public WebPImageWriterSpi() {
        super(
                SpiSettings.VENDOR,
                SpiSettings.VERSION,
                SpiSettings.NAMES,
                SpiSettings.SUFFIXES,
                SpiSettings.MIME_TYPES,
                SpiSettings.WRITER_CLASS_NAME,
                SpiSettings.OUTPUT_TYPES,
                SpiSettings.READER_SPI_NAMES,
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
    public boolean canEncodeImage(ImageTypeSpecifier type) {
        SampleModel sampleModel = type.getSampleModel();
        ColorModel colorModel = type.getColorModel();

        int numBands = sampleModel.getNumBands();
        if (numBands < 1 || numBands > 4) {
            return false;
        }

        boolean hasAlpha = colorModel.hasAlpha();
        if (colorModel instanceof IndexColorModel) {
            return true;
        }
        if ((numBands == 1 || numBands == 3) && hasAlpha) {
            return false;
        }
        if ((numBands == 2 || numBands == 4) && !hasAlpha) {
            return false;
        }

        int[] sampleSize = sampleModel.getSampleSize();
        for (int i = 1; i < sampleSize.length; i++) {
            if (sampleSize[i] > 8) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ImageWriter createWriterInstance(Object extension) throws IOException {
        return new WebPImageWriter(this);
    }

    @Override
    public String getDescription(Locale locale) {
        return "WebP Image Writer";
    }

}
