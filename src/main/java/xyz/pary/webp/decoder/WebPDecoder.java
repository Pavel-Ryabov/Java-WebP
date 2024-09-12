package xyz.pary.webp.decoder;

import xyz.pary.webp.Dimensions;
import xyz.pary.webp.WebPInfo;

public interface WebPDecoder {

    /**
     *
     * @param data image data
     * @return image dimensions
     * @throws WebPDecoderException if a decoding error occurred
     */
    Dimensions getDimensions(byte[] data) throws WebPDecoderException;

    /**
     *
     * @param data image data
     * @return image information
     * @throws WebPDecoderException if a decoding error occurred
     */
    WebPInfo getInfo(byte[] data) throws WebPDecoderException;

    /**
     *
     * @param data image data
     * @return ARGB samples
     * @throws WebPDecoderException if a decoding error occurred
     */
    byte[] decodeARGB(byte[] data) throws WebPDecoderException;

    /**
     *
     * @param data image data
     * @return RGB samples
     * @throws WebPDecoderException if a decoding error occurred
     */
    byte[] decodeRGB(byte[] data) throws WebPDecoderException;
}
