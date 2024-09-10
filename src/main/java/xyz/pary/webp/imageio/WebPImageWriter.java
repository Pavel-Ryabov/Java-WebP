package xyz.pary.webp.imageio;

import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;
import xyz.pary.webp.encoder.WebPEncoder;
import xyz.pary.webp.encoder.WebPEncoderFactory;

public class WebPImageWriter extends ImageWriter {

    private final WebPEncoder encoder;

    public WebPImageWriter(ImageWriterSpi iws) {
        super(iws);
        encoder = new WebPEncoderFactory().createEncoder();
    }

    @Override
    public IIOMetadata getDefaultStreamMetadata(ImageWriteParam param) {
        return null;
    }

    @Override
    public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier imageType, ImageWriteParam param) {
        return null;
    }

    @Override
    public IIOMetadata convertStreamMetadata(IIOMetadata inData, ImageWriteParam param) {
        return null;
    }

    @Override
    public IIOMetadata convertImageMetadata(IIOMetadata inData, ImageTypeSpecifier imageType, ImageWriteParam param) {
        return null;
    }

    @Override
    public ImageWriteParam getDefaultWriteParam() {
        return new WebPImageWriteParam(getLocale());
    }

    @Override
    public void write(IIOMetadata streamMetadata, IIOImage image, ImageWriteParam param) throws IOException {
        ImageOutputStream ios = (ImageOutputStream) getOutput();
        if (ios == null) {
            throw new IllegalStateException("No output stream");
        }

        clearAbortRequest();
        processImageStarted(0);

        if (param == null) {
            param = getDefaultWriteParam();
        }

        RenderedImage ri = image.getRenderedImage();
        Rectangle sourceRegion = param.getSourceRegion();
        ColorModel cm = ri.getColorModel();

        Rectangle rect = new Rectangle(ri.getMinX(), ri.getMinY(), ri.getWidth(), ri.getHeight());
        if (sourceRegion == null) {
            sourceRegion = rect;
        } else {
            sourceRegion = sourceRegion.intersection(rect);
        }

        int scaleX = param.getSourceXSubsampling();
        int scaleY = param.getSourceYSubsampling();
        int xOffset = param.getSubsamplingXOffset();
        int yOffset = param.getSubsamplingYOffset();

        sourceRegion.translate(xOffset, yOffset);
        sourceRegion.width -= xOffset;
        sourceRegion.height -= yOffset;

        int minX = sourceRegion.x / scaleX;
        int minY = sourceRegion.y / scaleY;
        int w = (sourceRegion.width + scaleX - 1) / scaleX;
        int h = (sourceRegion.height + scaleY - 1) / scaleY;

        Rectangle destinationRegion = new Rectangle(minX, minY, w, h);

        boolean noTransform = destinationRegion.equals(sourceRegion);
        boolean noSubband = param.getSourceBands() == null;

        if (!(noTransform && noSubband)) {
            throw new IOException("Transformation not supported");
        }

        if (abortRequested()) {
            processWriteAborted();
            return;
        }

        byte[] pixels = PixelUtils.getPixels(ri.getData(sourceRegion), cm);

        if (abortRequested()) {
            processWriteAborted();
            return;
        }

        byte[] data;
        if (cm.hasAlpha()) {
            if (param.isCompressionLossless()) {
                data = encoder.encodeLosslessRGBA(pixels, w, h);
            } else {
                data = encoder.encodeRGBA(pixels, w, h, param.getCompressionQuality() * 100);
            }
        } else {
            if (param.isCompressionLossless()) {
                data = encoder.encodeLosslessRGB(pixels, w, h);
            } else {
                data = encoder.encodeRGB(pixels, w, h, param.getCompressionQuality() * 100);
            }
        }

        if (abortRequested()) {
            processWriteAborted();
            return;
        }

        ios.write(data, 0, data.length);
        processImageProgress(100.0F);

        if (abortRequested()) {
            processWriteAborted();
        } else {
            processImageComplete();
        }
    }

}
