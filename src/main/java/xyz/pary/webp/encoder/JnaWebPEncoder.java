package xyz.pary.webp.encoder;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import xyz.pary.webp.Dimensions;

public class JnaWebPEncoder implements WebPEncoder {

    private final WebPEncoderLibrary encoder = WebPEncoderLibrary.INSTANCE;

    @Override
    public byte[] encodeRGB(byte[] rgb, Dimensions dimensions, float quality) {
        return encodeRGB(rgb, dimensions.getWidth(), dimensions.getHeight(), quality);
    }

    @Override
    public byte[] encodeRGB(byte[] rgb, int width, int height, float quality) throws WebPEncoderException {
        PointerByReference ptrRef = new PointerByReference();
        int size = encoder.WebPEncodeRGB(rgb, width, height, width * 3, quality, ptrRef);
        return readDataAndFreeMemory(ptrRef, size);
    }

    @Override
    public byte[] encodeRGBA(byte[] rgba, Dimensions dimensions, float quality) throws WebPEncoderException {
        return encodeRGBA(rgba, dimensions.getWidth(), dimensions.getHeight(), quality);
    }

    @Override
    public byte[] encodeRGBA(byte[] rgba, int width, int height, float quality) throws WebPEncoderException {
        PointerByReference ptrRef = new PointerByReference();
        int size = encoder.WebPEncodeRGBA(rgba, width, height, width * 4, quality, ptrRef);
        return readDataAndFreeMemory(ptrRef, size);
    }

    @Override
    public byte[] encodeLosslessRGB(byte[] rgb, Dimensions dimensions) throws WebPEncoderException {
        return encodeLosslessRGB(rgb, dimensions.getWidth(), dimensions.getHeight());
    }

    @Override
    public byte[] encodeLosslessRGB(byte[] rgb, int width, int height) throws WebPEncoderException {
        PointerByReference ptrRef = new PointerByReference();
        int size = encoder.WebPEncodeLosslessRGB(rgb, width, height, width * 3, ptrRef);
        return readDataAndFreeMemory(ptrRef, size);
    }

    @Override
    public byte[] encodeLosslessRGBA(byte[] rgba, Dimensions dimensions) throws WebPEncoderException {
        return encodeLosslessRGBA(rgba, dimensions.getWidth(), dimensions.getHeight());
    }

    @Override
    public byte[] encodeLosslessRGBA(byte[] rgba, int width, int height) throws WebPEncoderException {
        PointerByReference ptrRef = new PointerByReference();
        int size = encoder.WebPEncodeLosslessRGBA(rgba, width, height, width * 4, ptrRef);
        return readDataAndFreeMemory(ptrRef, size);
    }

    private byte[] readDataAndFreeMemory(PointerByReference ref, int size) throws WebPEncoderException {
        if (size == 0) {
            throw new WebPEncoderException("Image encoding error");
        }
        Pointer ptr = ref.getValue();
        byte[] data = toByteArray(ptr, size);
        encoder.WebPFree(ptr);
        return data;
    }

    private byte[] toByteArray(Pointer pointer, int size) {
        byte[] bytes = new byte[size];
        pointer.read(0, bytes, 0, size);
        return bytes;
    }
}
