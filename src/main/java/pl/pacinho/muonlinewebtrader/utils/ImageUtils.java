package pl.pacinho.muonlinewebtrader.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import pl.pacinho.muonlinewebtrader.model.enums.CharacterClass;
import pl.pacinho.muonlinewebtrader.model.enums.PaymentItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ImageUtils {

    public static final String UNKNOWN_IMAGE = FileUtils.IMG_LOCATION + "/null.png";
    public static final String ACCEPT_TRADE_IMAGE =  encodeFileToBase64Binary(FileUtils.IMG_LOCATION + "/acceptTrade.png");
    public static final String DECLINE_TRADE_IMAGE = encodeFileToBase64Binary(FileUtils.IMG_LOCATION + "/declineTrade.png");
    public static final String BACKGROUND_TRADE_IMAGE = encodeFileToBase64Binary(FileUtils.IMG_LOCATION + "/backgroundTrade.png");

    public static String encodeFileToBase64Binary(String path) {
        if (path == null || path.isEmpty()) return null;
        FileInputStream fileInputStreamReader = null;
        try {
            File file = new File(path);
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

    public static String getItemIcon(PaymentItem paymentItem) {
        switch (paymentItem) {
            case BLESS -> {
                return encodeFileToBase64Binary(FileUtils.IMG_LOCATION + "/Jewel of Bless.jpg");
            }
            case SOUL -> {
                return encodeFileToBase64Binary(FileUtils.IMG_LOCATION + "/Jewel of Soul.jpg");
            }
            case ZEN -> {
                return encodeFileToBase64Binary(FileUtils.IMG_LOCATION + "/Zen.png");
            }
        }
        return null;
    }

    public static String getCharacterClassImages(String name) {
        return encodeFileToBase64Binary(FileUtils.IMG_LOCATION + "/" + name + ".png");
    }

    public static String getBackgroundImage(){
        return encodeFileToBase64Binary(FileUtils.IMG_LOCATION + "/loginBackground.jpg");
    }

}