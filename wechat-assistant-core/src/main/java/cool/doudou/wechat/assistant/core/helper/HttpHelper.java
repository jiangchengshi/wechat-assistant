package cool.doudou.wechat.assistant.core.helper;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.Map;

/**
 * HttpHelper
 *
 * @author jiangcs
 * @since 2022/06/30
 */
@Slf4j
public class HttpHelper {
    private OkHttpClient okHttpClient;

    /**
     * get 请求
     *
     * @param url      请求Url
     * @param urlParam Url参数
     * @return 结果
     */
    public String doGet(String url, Map<String, Object> urlParam) {
        Request.Builder builder = new Request.Builder();
        // Query
        StringBuilder sbParam = new StringBuilder();
        if (urlParam != null && urlParam.keySet().size() > 0) {
            for (String key : urlParam.keySet()) {
                if (sbParam.length() <= 0) {
                    sbParam.append("?").append(key).append("=").append(urlParam.get(key));
                } else {
                    sbParam.append("&").append(key).append("=").append(urlParam.get(key));
                }
            }
        }
        url += sbParam;
        Request request = builder.url(url).build();

        log.info("url => GET {}", url);
        log.info("urlParam => {}", urlParam);

        return execute(request);
    }

    /**
     * Post 请求
     *
     * @param url      请求Url
     * @param urlParam Url参数
     * @param jsonBody Body参数
     * @return 结果
     */
    public String doPost(String url, Map<String, Object> urlParam, String jsonBody) {
        Request.Builder builder = new Request.Builder();
        // Query
        StringBuilder sbParam = new StringBuilder();
        if (urlParam != null && urlParam.keySet().size() > 0) {
            for (String key : urlParam.keySet()) {
                if (sbParam.length() <= 0) {
                    sbParam.append("?").append(key).append("=").append(urlParam.get(key));
                } else {
                    sbParam.append("&").append(key).append("=").append(urlParam.get(key));
                }
            }
        }
        // Body
        if (jsonBody == null) {
            jsonBody = "";
        }
        RequestBody requestBody = RequestBody.create(jsonBody, MediaType.parse("application/json; charset=utf-8"));
        Request request = builder.url(url).post(requestBody).build();

        log.info("url => POST {}", url);
        log.info("urlParam => {}", urlParam);
        log.info("body => {}", jsonBody);

        return execute(request);
    }

    private String execute(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                if (!ObjectUtils.isEmpty(responseBody)) {
                    String result = responseBody.string();
                    log.info("execute => success: {}", result);
                    return result;
                }
            }
            log.error("execute => fail: {}", response);
        } catch (Exception e) {
            log.error("execute => exception: ", e);
        }
        return null;
    }

    @Autowired
    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }
}
