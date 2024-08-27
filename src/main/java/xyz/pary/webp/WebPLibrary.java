package xyz.pary.webp;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;
import java.util.Map;

public class WebPLibrary {

    public static final String WEBP_VERSION = "1_4_0";
    public static final String LIBRARY_NAME = "libwebp";

    public static synchronized <T extends Library> T loadLibrary(Class<T> cls, Map<String, Object> options) {
        return Platform.isWindows() ? loadLibraryInWindows(cls, options) : loadLibraryInLinux(cls, options);
    }

    private static <T extends Library> T loadLibraryInWindows(Class<T> cls, Map<String, Object> options) {
        NativeLibrary.getInstance("libsharpyuv");
        return (T) Native.load(LIBRARY_NAME, cls, options);
    }

    private static <T extends Library> T loadLibraryInLinux(Class<T> cls, Map<String, Object> options) {
        NativeLibrary.getInstance("libsharpyuv.so.0");
        return (T) Native.load(LIBRARY_NAME + ".so", cls, options);
    }
}
