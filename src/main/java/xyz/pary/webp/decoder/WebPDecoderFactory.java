package xyz.pary.webp.decoder;

public class WebPDecoderFactory {

    public WebPDecoder createDecoder() {
        return new JnaWebPDecoder();
    }
}
