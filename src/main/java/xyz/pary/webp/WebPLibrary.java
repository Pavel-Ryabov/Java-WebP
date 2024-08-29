package xyz.pary.webp;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import xyz.pary.webp.utils.Utils;

public class WebPLibrary {

    private static final Logger LOG = Logger.getLogger(WebPLibrary.class.getName());

    public static final String WEBP_VERSION = "1_4_0";
    public static final String LIBRARY_NAME = "libwebp";

    private static final String TEMP_DIR = Paths.get(
            System.getProperty("java.io.tmpdir"),
            "xyz_pary_webp",
            WEBP_VERSION)
            .toAbsolutePath().toString();

    private static final String WINDOWS_PREFIX = "win32-x86-64";
    private static final String LINUX_PREFIX = "linux-x86-64";

    private static final String SHARPYUV_NAME = "libsharpyuv";

    public static synchronized <T extends Library> T loadLibrary(Class<T> cls, Map<String, Object> options) {
        if (!Platform.is64Bit()) {
            throw new IllegalStateException("Unsupported ARCH");
        }
        if (!Platform.isWindows() && !Platform.isLinux()) {
            throw new IllegalStateException("Unsupported OS");
        }
        new File(TEMP_DIR).mkdirs();
        return Platform.isWindows() ? loadLibraryInWindows(cls, options) : loadLibraryInLinux(cls, options);
    }

    private static <T extends Library> T loadLibraryInWindows(Class<T> cls, Map<String, Object> options) {
        String sharpyuvPath = unpackLib(SHARPYUV_NAME + ".dll", WINDOWS_PREFIX);
        NativeLibrary.getInstance(sharpyuvPath);
        String webpPath = unpackLib(LIBRARY_NAME + ".dll", WINDOWS_PREFIX);
        return (T) Native.load(webpPath, cls, options);
    }

    private static <T extends Library> T loadLibraryInLinux(Class<T> cls, Map<String, Object> options) {
        String prefix = LINUX_PREFIX;
        String targetPath = TEMP_DIR;
        if (isMusl()) {
            prefix += "/musl";
            targetPath = "/usr/lib";
        }
        String sharpyuvPath = unpackLib(SHARPYUV_NAME + ".so.0", prefix, targetPath);
        NativeLibrary.getInstance(sharpyuvPath);
        String webpPath = unpackLib(LIBRARY_NAME + ".so", prefix, targetPath);
        return (T) Native.load(webpPath, cls, options);
    }

    private static String unpackLib(String libName, String from) {
        return unpackLib(libName, from, TEMP_DIR);
    }

    private static String unpackLib(String libName, String from, String to) {
        File tmpFile = Paths.get(to, libName).toFile();
        if (!tmpFile.exists()) {
            try {
                LOG.log(Level.INFO, "Unpacking library {0} from {1} to {2}", new Object[]{libName, from, tmpFile.getAbsoluteFile()});
                Utils.copyResourceToFile(from + "/" + libName, tmpFile);
            } catch (IOException e) {
                throw new RuntimeException("Failed to unpack library " + libName);
            }
        }
        return tmpFile.getAbsolutePath();
    }

    private static boolean isMusl() {
        boolean isMusl = false;
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("ldd", "/bin/ls");
        try {
            Process lsProc = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(lsProc.getInputStream()))) {
                String line = reader.readLine();
                if (line.contains("libc.musl") || line.contains("ld-musl")) {
                    isMusl = true;
                }
            }
        } catch (Exception ignore) {
        }
        return isMusl;
    }
}
