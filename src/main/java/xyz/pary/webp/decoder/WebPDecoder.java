package xyz.pary.webp.decoder;

public interface WebPDecoder {

    Dimensions getDimensions(byte[] data) throws WebPDecoderException;

    WebPInfo getInfo(byte[] data) throws WebPDecoderException;

    byte[] decodeARGB(byte[] data) throws WebPDecoderException;

    byte[] decodeRGB(byte[] data) throws WebPDecoderException;
}
