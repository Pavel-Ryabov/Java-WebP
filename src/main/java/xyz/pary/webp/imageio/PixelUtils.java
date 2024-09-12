package xyz.pary.webp.imageio;

import java.awt.image.ColorModel;
import java.awt.image.Raster;

class PixelUtils {

    public static int[] toIntPixels(byte[] pixels, boolean withAlpha) {
        int bpp = withAlpha ? 4 : 3;
        int[] intPixels = new int[pixels.length / bpp];
        for (int i = 0; i < pixels.length; i += bpp) {
            int index = i / bpp;
            if (withAlpha) {
                intPixels[index] = argbToInt(pixels[i], pixels[i + 1], pixels[i + 2], pixels[i + 3]);
            } else {
                intPixels[index] = rgbToInt(pixels[i], pixels[i + 1], pixels[i + 2]);
            }
        }
        return intPixels;
    }

    public static int rgbToInt(byte r, byte g, byte b) {
        return argbToInt((byte) 0, r, g, b);
    }

    public static int argbToInt(byte a, byte r, byte g, byte b) {
        return (a & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
    }

    public static byte[] getPixels(Raster raster, ColorModel colorModel) {
        int bpp = colorModel.hasAlpha() ? 4 : 3;
        byte[] out = new byte[raster.getWidth() * raster.getHeight() * bpp];
        for (int y = raster.getMinY(); y < raster.getMinY() + raster.getHeight(); y++) {
            for (int x = raster.getMinX(); x < raster.getMinX() + raster.getWidth(); x++) {
                Object elements = raster.getDataElements(x, y, null);
                int index = y * raster.getWidth() * bpp + x * bpp;
                out[index] = (byte) colorModel.getRed(elements);
                out[index + 1] = (byte) colorModel.getGreen(elements);
                out[index + 2] = (byte) colorModel.getBlue(elements);
                if (colorModel.hasAlpha()) {
                    out[index + 3] = (byte) colorModel.getAlpha(elements);
                }
            }
        }
        return out;
    }

}
