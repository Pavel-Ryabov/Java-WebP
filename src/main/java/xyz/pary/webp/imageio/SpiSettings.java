package xyz.pary.webp.imageio;

import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

class SpiSettings {

    public static final String VENDOR = "xyz.pary.webp";
    public static final String VERSION = "0.1";
    public static final String[] NAMES = {"WebP", "webp"};
    public static final String[] SUFFIXES = {"webp"};
    public static final String[] MIME_TYPES = {"image/webp"};
    public static final String READER_CLASS_NAME = WebPImageReader.class.getName();
    public static final String WRITER_CLASS_NAME = WebPImageWriter.class.getName();
    public static final Class[] INPUT_TYPES = {ImageInputStream.class};
    public static final Class[] OUTPUT_TYPES = {ImageOutputStream.class};
    public static final String[] READER_SPI_NAMES = {WebPImageReaderSpi.class.getName()};
    public static final String[] WRITER_SPI_NAMES = {WebPImageWriterSpi.class.getName()};
    public static final boolean SUPPORTS_STANDARD_STREAM_METADATA_FORMAT = false;
    public static final String NATIVE_STREAM_METADATA_FORMAT_NAME = null;
    public static final String NATIVE_STREAM_METADATA_FORMAT_CLASS_NAME = null;
    public static final String[] EXTRA_STREAM_METADATA_FORMAT_NAMES = null;
    public static final String[] EXTRA_STREAM_METADATA_FORMAT_CLASS_NAMES = null;
    public static final boolean SUPPORTS_STANDARD_IMAGE_METADATA_FORMAT = false;
    public static final String NATIVE_IMAGE_METADATA_FORMAT_NAME = null;
    public static final String NATIVE_IMAGE_METADATA_FORMAT_CLASS_NAME = null;
    public static final String[] EXTRA_IMAGE_METADATA_FORMAT_NAMES = null;
    public static final String[] EXTRA_IMAGE_METADATA_FORMAT_CLASS_NAMES = null;
}
