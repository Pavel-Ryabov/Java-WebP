package xyz.pary.webp.encoder;

public class WebPEncoderFactory {

    /**
     *
     * @return webp encoder
     */
    public WebPEncoder createEncoder() {
        return new JnaWebPEncoder();
    }
}
