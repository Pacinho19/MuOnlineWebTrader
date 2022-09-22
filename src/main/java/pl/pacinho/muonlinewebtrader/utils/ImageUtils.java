package pl.pacinho.muonlinewebtrader.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ImageUtils {

    public static String encodeFileToBase64Binary(String path) {
        if (path == null || path.isEmpty()) return null;
        FileInputStream fileInputStreamReader = null;
        try {
            File file = new File( path);
            fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            return new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStreamReader != null)
                    fileInputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
