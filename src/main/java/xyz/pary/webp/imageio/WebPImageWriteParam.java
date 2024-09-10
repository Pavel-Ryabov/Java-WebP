package xyz.pary.webp.imageio;

import java.util.Locale;
import javax.imageio.ImageWriteParam;
import xyz.pary.webp.WebPFormat;

public class WebPImageWriteParam extends ImageWriteParam {

    public WebPImageWriteParam() {
        this(null);
    }

    public WebPImageWriteParam(Locale locale) {
        super(locale);
        canWriteCompressed = true;
        compressionMode = MODE_EXPLICIT;
        compressionTypes = new String[]{WebPFormat.LOSSLESS.toString(), WebPFormat.LOSSY.toString()};
        compressionType = WebPFormat.LOSSLESS.toString();
    }

    @Override
    public boolean isCompressionLossless() {
        super.isCompressionLossless();
        return WebPFormat.valueOf(getCompressionType()) == WebPFormat.LOSSLESS;
    }

    @Override
    public void setCompressionType(String compressionType) {
        setCompressionType(WebPFormat.valueOf(compressionType));
    }

    public void setCompressionType(WebPFormat format) {
        if (format == WebPFormat.UNDEFINED) {
            throw new IllegalArgumentException("Undefined format not supported");
        }
        super.setCompressionType(format.toString());
    }

}
