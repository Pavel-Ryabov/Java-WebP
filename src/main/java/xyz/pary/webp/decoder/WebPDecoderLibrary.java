package xyz.pary.webp.decoder;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import com.sun.jna.ptr.IntByReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import xyz.pary.webp.WebPLibrary;
import xyz.pary.webp.jna.EnumTypeMapper;

interface WebPDecoderLibrary extends Library {

    static final int WEBP_DECODER_ABI_VERSION = 0x0209;

    Map<String, Object> DECODER_OPTIONS = Collections.unmodifiableMap(new HashMap<String, Object>() {
        {
            put(Library.OPTION_TYPE_MAPPER, new EnumTypeMapper());
        }
    });

    WebPDecoderLibrary INSTANCE = WebPLibrary.loadLibrary(WebPDecoderLibrary.class, DECODER_OPTIONS);

    boolean WebPGetInfo(byte[] data, int data_size, IntByReference width, IntByReference height);

    VP8StatusCode WebPGetFeaturesInternal(byte[] data, int data_size, WebPBitstreamFeatures features, int decoderAbiVerion);

    Pointer WebPDecodeARGB(byte[] data, int data_size, IntByReference width, IntByReference height);

    Pointer WebPDecodeRGB(byte[] data, int data_size, IntByReference width, IntByReference height);

    void WebPFree(Pointer ptr);

    @FieldOrder({"width", "height", "has_alpha", "has_animation", "format", "pad"})
    static class WebPBitstreamFeatures extends Structure {

        public int width;          // Width in pixels.
        public int height;         // Height in pixels.
        public boolean has_alpha;      // True if the bitstream contains an alpha channel.
        public boolean has_animation;  // True if the bitstream is an animation.
        public int format;         // 0 = undefined (/mixed), 1 = lossy, 2 = lossless

        public int[] pad = new int[5];    // padding for later use
    }

}
