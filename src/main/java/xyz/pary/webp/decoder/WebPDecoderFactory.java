package xyz.pary.webp.decoder;

public class WebPDecoderFactory {

    /**
     *
     * @return webp decoder
     */
    public WebPDecoder createDecoder() {
        return new JnaWebPDecoder();
    }
}
