package xyz.pary.webp.decoder;

import xyz.pary.webp.jna.JnaEnum;

public enum WebPCspMode implements JnaEnum<WebPCspMode> {
    MODE_RGB(0),
    MODE_RGBA(1),
    MODE_BGR(2),
    MODE_BGRA(3),
    MODE_ARGB(4),
    MODE_RGBA_4444(5),
    MODE_RGB_565(6),
    // RGB-premultiplied transparent modes (alpha value is preserved)
    MODE_rgbA(7),
    MODE_bgrA(8),
    MODE_Argb(9),
    MODE_rgbA_4444(10),
    // YUV modes must come after RGB ones.
    MODE_YUV(11),
    MODE_YUVA(12), // yuv 4:2:0
    MODE_LAST(13);

    private final int value;

    private WebPCspMode(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    public boolean isRgbMode() {
        return this != MODE_YUV && this != MODE_YUVA && this != MODE_LAST;
    }

    public int bytesPerPixel() {
        switch (this) {
            case MODE_RGBA:
            case MODE_BGRA:
            case MODE_ARGB:
            case MODE_rgbA:
            case MODE_bgrA:
            case MODE_Argb:
                return 4;
            case MODE_RGB:
            case MODE_BGR:
                return 3;
            case MODE_RGBA_4444:
            case MODE_RGB_565:
            case MODE_rgbA_4444:
                return 2;
            case MODE_YUV:
            case MODE_YUVA:
                return 1;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
