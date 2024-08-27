package xyz.pary.webp.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

    public static InputStream getResourceAsStream(String resource) {
        return Utils.class.getClassLoader().getResourceAsStream(resource);
    }

    public static void copyResourceToFile(String resource, File file) throws IOException {
        try (InputStream is = getResourceAsStream(resource); OutputStream os = new FileOutputStream(file)) {
            transfer(is, os);
        }
    }

    public static void transfer(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[1024 * 4];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
    }
}
