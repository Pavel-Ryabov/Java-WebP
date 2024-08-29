package xyz.pary.webp.encoder;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import java.util.Collections;
import xyz.pary.webp.WebPLibrary;

interface WebPEncoderLibrary extends Library {

    WebPEncoderLibrary INSTANCE = WebPLibrary.loadLibrary(WebPEncoderLibrary.class, Collections.emptyMap());

    int WebPEncodeRGB(byte[] rgb, int width, int height, int stride, float quality_factor, PointerByReference output);

    int WebPEncodeRGBA(byte[] rgba, int width, int height, int stride, float quality_factor, PointerByReference output);

    int WebPEncodeLosslessRGB(byte[] rgb, int width, int height, int stride, PointerByReference output);

    int WebPEncodeLosslessRGBA(byte[] rgba, int width, int height, int stride, PointerByReference output);

    void WebPFree(Pointer ptr);
}
