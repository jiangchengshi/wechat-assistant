package cool.doudou.wechat.assistant.core.util;

import java.util.Arrays;

/**
 * SignUtil
 *
 * @author jiangcs
 * @since 2022/08/13
 */
public class SignUtil {
    /**
     * 微信 签名：服务端
     *
     * @param token     Token
     * @param timestamp 时间戳
     * @param nonce     随机字符串
     * @return 签名字符串
     */
    public static String server(String token, String timestamp, String nonce) {
        String[] paramArr = new String[]{token, timestamp, nonce};
        // 将token、timestamp、nonce三个参数进行字典序排序
        Arrays.sort(paramArr);

        //三个参数组合成一个字符串
        StringBuilder builder = new StringBuilder();
        for (String param : paramArr) {
            builder.append(param);
        }

        return Sha1Util.sha1(builder.toString(), false);
    }

    /**
     * 微信 签名：前端
     *
     * @param jsApTicket js_api_ticket
     * @param nonceStr   随机字符串
     * @param timestamp  时间戳
     * @param url        请求Url
     * @return 签名字符串
     */
    public static String js(String jsApTicket, String nonceStr, String timestamp, String url) {
        String builder = "jsapi_ticket=" + jsApTicket +
                "&noncestr=" + nonceStr +
                "&timestamp=" + timestamp +
                "&url=" + url;
        return Sha1Util.sha1(builder, false);
    }
}
