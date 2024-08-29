package xyz.pary.webp.encoder;

public class WebPEncoderFactory {

    public WebPEncoder createEncoder() {
        return new JnaWebPEncoder();
    }
}
