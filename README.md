Java [JNA](https://github.com/java-native-access/jna) wrapper for libwebp.

### Supported Platforms

- Windows x86_64
- Linux x86_64

### Usage

```xml
<dependency>
  <groupId>xyz.pary</groupId>
  <artifactId>webp</artifactId>
  <version>0.1</version>
</dependency>
```

#### ImageIO

Read image:

```java
BufferedImage bufferedImage = ImageIO.read(new File("image.webp"));
```

Write lossless image:

```java
ImageIO.write(bufferedImage, "webp", new File("image.webp"));
```

Write lossy image:

```java
WebPImageWriteParam writeParam = new WebPImageWriteParam();
writeParam.setCompressionType(WebPFormat.LOSSY);
writeParam.setCompressionQuality(0.5F);

ImageWriter writer = ImageIO.getImageWritersByFormatName("webp").next();
try (FileImageOutputStream out = new FileImageOutputStream(new File("image.webp"))) {
    writer.setOutput(out);
    writer.write(null, new IIOImage(bufferedImage, null, null), writeParam);
}
```

#### Decoder / Encoder

Decoding:

```java
byte[] fileData;
try (FileInputStream in = new FileInputStream("image.webp")) {
    fileData = readInputStreamToByteArray(in);
}
WebPDecoder decoder = new WebPDecoderFactory().createDecoder();
byte[] rgb = decoder.decodeRGB(fileData);
//or
byte[] argb = decoder.decodeARGB(fileData);
```

Encoding:

```java
WebPEncoder encoder = new WebPEncoderFactory().createEncoder();
//lossless
byte[] encodedData = encoder.encodeLosslessRGB(rgb, width, height);
//or
byte[] encodedData = encoder.encodeLosslessRGBA(rgba, width, height);
//lossy
float quality = 50f; // 0 - 100
byte[] encodedData = encoder.encodeRGB(rgb, width, height, quality);
//or
byte[] encodedData = encoder.encodeRGBA(rgba, width, height, quality);

try (FileOutputStream out = new FileOutputStream("image.webp")) {
    out.write(encodedData, 0, encodedData.length);
}
```
