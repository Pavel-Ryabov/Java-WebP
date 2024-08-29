package xyz.pary.webp.decoder;

import xyz.pary.webp.Dimensions;
import xyz.pary.webp.WebPInfo;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import xyz.pary.webp.decoder.WebPDecoderLibrary.WebPBitstreamFeatures;

class JnaWebPDecoder implements WebPDecoder {

    private final WebPDecoderLibrary decoder = WebPDecoderLibrary.INSTANCE;

    @Override
    public Dimensions getDimensions(byte[] data) throws WebPDecoderException {
        IntByReference width = new IntByReference();
        IntByReference height = new IntByReference();
        boolean result = decoder.WebPGetInfo(data, data.length, width, height);
        if (!result) {
            throw new WebPDecoderException("Error getting image dimensions");
        }
        return new Dimensions(width.getValue(), height.getValue());
    }

    @Override
    public WebPInfo getInfo(byte[] data) throws WebPDecoderException {
        WebPBitstreamFeatures features = new WebPBitstreamFeatures();
        VP8StatusCode status = decoder.WebPGetFeaturesInternal(data, data.length, features, WebPDecoderLibrary.WEBP_DECODER_ABI_VERSION);
        if (status != VP8StatusCode.VP8_STATUS_OK) {
            throw new WebPDecoderException("Error getting webp information. VP8StatusCode: " + status);
        }
        return new WebPInfo(features.width, features.height, features.has_alpha, features.has_animation, features.format);
    }

    @Override
    public byte[] decodeARGB(byte[] data) throws WebPDecoderException {
        Dimensions dimensions = getDimensions(data);
        Pointer argbPointer = decoder.WebPDecodeARGB(data, data.length,
                new IntByReference(dimensions.getWidth()), new IntByReference(dimensions.getHeight()));
        if (argbPointer == null) {
            throw new WebPDecoderException("Image decoding error");
        }
        byte[] argb = toByteArray(argbPointer, dimensions, WebPCspMode.MODE_ARGB);;
        decoder.WebPFree(argbPointer);
        return argb;
    }

    @Override
    public byte[] decodeRGB(byte[] data) throws WebPDecoderException {
        Dimensions dimensions = getDimensions(data);
        Pointer rgbPointer = decoder.WebPDecodeRGB(data, data.length,
                new IntByReference(dimensions.getWidth()), new IntByReference(dimensions.getHeight()));
        if (rgbPointer == null) {
            throw new WebPDecoderException("Image decoding error");
        }
        byte[] rgb = toByteArray(rgbPointer, dimensions, WebPCspMode.MODE_RGB);;
        decoder.WebPFree(rgbPointer);
        return rgb;
    }

    private byte[] toByteArray(Pointer pointer, Dimensions dimensions, WebPCspMode csp) {
        int size = dimensions.getWidth() * dimensions.getHeight() * csp.bytesPerPixel();
        byte[] bytes = new byte[size];
        pointer.read(0, bytes, 0, size);
        return bytes;
    }

}
