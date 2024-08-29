package xyz.pary.webp;

public enum WebPFormat {
    UNDEFINED,
    LOSSY,
    LOSSLESS;

    public static WebPFormat getByValue(int value) {
        switch (value) {
            case 1:
                return LOSSY;
            case 2:
                return LOSSLESS;
            default:
                return UNDEFINED;
        }
    }
}
