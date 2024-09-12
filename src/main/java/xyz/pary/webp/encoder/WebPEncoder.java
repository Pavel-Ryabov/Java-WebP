package xyz.pary.webp.encoder;

import xyz.pary.webp.Dimensions;

public interface WebPEncoder {

    /**
     *
     * @param rgb RGB samples
     * @param dimensions image dimensions
     * @param quality quality factor ranges from 0 (smaller output, lower quality) to 100 (best quality, larger output)
     * @return encoded data
     * @throws WebPEncoderException if a encoding error occurred
     */
    byte[] encodeRGB(byte[] rgb, Dimensions dimensions, float quality) throws WebPEncoderException;

    /**
     *
     * @param rgb RGB samples
     * @param width image width
     * @param height image height
     * @param quality quality factor ranges from 0 (smaller output, lower quality) to 100 (best quality, larger output)
     * @return encoded data
     * @throws WebPEncoderException if a encoding error occurred
     */
    byte[] encodeRGB(byte[] rgb, int width, int height, float quality) throws WebPEncoderException;

    /**
     *
     * @param rgba RGBA samples
     * @param dimensions image dimensions
     * @param quality quality factor ranges from 0 (smaller output, lower quality) to 100 (best quality, larger output)
     * @return encoded data
     * @throws WebPEncoderException if a encoding error occurred
     */
    byte[] encodeRGBA(byte[] rgba, Dimensions dimensions, float quality) throws WebPEncoderException;

    /**
     *
     * @param rgba RGBA samples
     * @param width image width
     * @param height image height
     * @param quality quality factor ranges from 0 (smaller output, lower quality) to 100 (best quality, larger output)
     * @return encoded data
     * @throws WebPEncoderException if a encoding error occurred
     */
    byte[] encodeRGBA(byte[] rgba, int width, int height, float quality) throws WebPEncoderException;

    /**
     *
     * @param rgb RGB samples
     * @param dimensions image dimensions
     * @return encoded data
     * @throws WebPEncoderException if a encoding error occurred
     */
    byte[] encodeLosslessRGB(byte[] rgb, Dimensions dimensions) throws WebPEncoderException;

    /**
     *
     * @param rgb RGB samples
     * @param width image width
     * @param height image height
     * @return encoded data
     * @throws WebPEncoderException if a encoding error occurred
     */
    byte[] encodeLosslessRGB(byte[] rgb, int width, int height) throws WebPEncoderException;

    /**
     *
     * @param rgba RGBA samples
     * @param dimensions image dimensions
     * @return encoded data
     * @throws WebPEncoderException if a encoding error occurred
     */
    byte[] encodeLosslessRGBA(byte[] rgba, Dimensions dimensions) throws WebPEncoderException;

    /**
     *
     * @param rgba RGBA samples
     * @param width image width
     * @param height image height
     * @return encoded data
     * @throws WebPEncoderException if a encoding error occurred
     */
    byte[] encodeLosslessRGBA(byte[] rgba, int width, int height) throws WebPEncoderException;

}
