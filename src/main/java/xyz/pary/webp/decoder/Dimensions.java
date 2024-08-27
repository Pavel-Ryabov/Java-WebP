package xyz.pary.webp.decoder;

public class Dimensions {

    private final int width;
    private final int height;

    public Dimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Dimensions{" + "width=" + width + ", height=" + height + '}';
    }
}
