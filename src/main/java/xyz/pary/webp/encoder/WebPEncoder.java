package xyz.pary.webp.encoder;

import xyz.pary.webp.Dimensions;

public interface WebPEncoder {

    byte[] encodeRGB(byte[] rgb, Dimensions dimensions, float quality) throws WebPEncoderException;

    byte[] encodeRGB(byte[] rgb, int width, int height, float quality) throws WebPEncoderException;

    byte[] encodeRGBA(byte[] rgba, Dimensions dimensions, float quality) throws WebPEncoderException;

    byte[] encodeRGBA(byte[] rgba, int width, int height, float quality) throws WebPEncoderException;

    byte[] encodeLosslessRGB(byte[] rgb, Dimensions dimensions) throws WebPEncoderException;

    byte[] encodeLosslessRGB(byte[] rgb, int width, int height) throws WebPEncoderException;

    byte[] encodeLosslessRGBA(byte[] rgba, Dimensions dimensions) throws WebPEncoderException;

    byte[] encodeLosslessRGBA(byte[] rgba, int width, int height) throws WebPEncoderException;

}
