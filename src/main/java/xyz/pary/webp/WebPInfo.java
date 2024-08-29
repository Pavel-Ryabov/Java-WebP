package xyz.pary.webp;

public class WebPInfo {

    private final Dimensions dimensions;
    private final boolean hasAlpha;
    private final boolean hasAnimation;
    private final WebPFormat format;

    public WebPInfo(int width, int height, boolean hasAlpha, boolean hasAnimation, int format) {
        this.dimensions = new Dimensions(width, height);
        this.hasAlpha = hasAlpha;
        this.hasAnimation = hasAnimation;
        this.format = WebPFormat.getByValue(format);
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public boolean isHasAlpha() {
        return hasAlpha;
    }

    public boolean isHasAnimation() {
        return hasAnimation;
    }

    public WebPFormat getFormat() {
        return format;
    }

    public int getWidth() {
        return dimensions.getWidth();
    }

    public int getHeight() {
        return dimensions.getHeight();
    }

}
