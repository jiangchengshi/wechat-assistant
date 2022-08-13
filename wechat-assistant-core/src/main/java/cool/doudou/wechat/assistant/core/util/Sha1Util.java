package cool.doudou.wechat.assistant.core.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Sha1Util
 *
 * @author jiangcs
 * @since 2022/07/04
 */
@Slf4j
public class Sha1Util {
    /**
     * SHA-1加密
     *
     * @param data  待加密字符串
     * @param upper 是否转为大写字符串
     * @return 字符串
     */
    public static String sha1(String data, boolean upper) {
        try {
            // 获得SHA-1摘要对象
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(data.getBytes(StandardCharsets.UTF_8));
            String result = byteToStr(messageDigest.digest());
            return upper ? result.toUpperCase() : result;
        } catch (Exception e) {
            log.error("sha1加密异常", e);
            return "";
        }
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray 字节数组
     * @return 字符串
     */
    private static String byteToStr(byte[] byteArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : byteArray) {
            stringBuilder.append(byteToHexStr(b));
        }
        return stringBuilder.toString();
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param b 字节
     * @return 字符串
     */
    private static String byteToHexStr(byte b) {
        String shaHex = Integer.toHexString(b & 0xFF);
        if (shaHex.length() < 2) {
            shaHex = 0 + shaHex;
        }
        return shaHex;
    }
}
