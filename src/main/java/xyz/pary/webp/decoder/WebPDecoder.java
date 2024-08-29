package xyz.pary.webp.decoder;

import xyz.pary.webp.Dimensions;
import xyz.pary.webp.WebPInfo;

public interface WebPDecoder {

    Dimensions getDimensions(byte[] data) throws WebPDecoderException;

    WebPInfo getInfo(byte[] data) throws WebPDecoderException;

    byte[] decodeARGB(byte[] data) throws WebPDecoderException;

    byte[] decodeRGB(byte[] data) throws WebPDecoderException;
}
