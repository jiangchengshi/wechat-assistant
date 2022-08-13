package cool.doudou.wechat.assistant.core.api;

import com.alibaba.fastjson2.JSONObject;
import cool.doudou.wechat.assistant.core.Constant;
import cool.doudou.wechat.assistant.core.entity.memory.TokenMap;
import cool.doudou.wechat.assistant.core.helper.HttpHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * WechatApi
 *
 * @author jiangcs
 * @since 2022/06/23
 */
@Slf4j
public class WechatApi {
    private HttpHelper httpHelper;

    /**
     * 获取access token <br>
     * 成功：{"access_token":"ACCESS_TOKEN","expires_in":7200}<br>
     * 失败：{"errcode":40013,"errmsg":"invalid appid"}
     *
     * @param appId  微信AppId
     * @param secret 微信Secret
     */
    public void accessToken(String appId, String secret) {
        String url = "https://api.weixin.qq.com/cgi-bin/token";
        try {
            Map<String, Object> urlParam = new HashMap<>(3);
            urlParam.put("grant_type", "client_credential");
            urlParam.put("appid", appId);
            urlParam.put("secret", secret);
            // 请求
            String result = httpHelper.doGet(url, urlParam);
            if (!ObjectUtils.isEmpty(result)) {
                JSONObject accessTokenObj = JSONObject.parseObject(result);
                if (accessTokenObj != null && ObjectUtils.isEmpty(accessTokenObj.getString("errcode"))) {
                    String accessToken = accessTokenObj.getString("access_token");
                    if (!ObjectUtils.isEmpty(accessToken)) {
                        return;
                    }
                    throw new RuntimeException("token is empty");
                }
                throw new RuntimeException("accessToken is empty or err：" + JSONObject.toJSONString(accessTokenObj));
            }
            throw new RuntimeException("result is empty");
        } catch (Exception e) {
            log.error("调用微信接口[cgi-bin/token]异常: appId[{}] => ", appId, e);
        }
    }

    /**
     * 获取js rest ticket <br>
     * 成功：{"errcode":0,"errmsg":"ok","ticket":"bxLdikRXVbTPdHSM05e5u5sUoXNKd8-41ZO3MhKoyN5OfkWITDGgnr2fwJ0m9E8NYzWKVZvdVtaUgWvsdshFKA","expires_in":7200}
     *
     * @param appId 微信AppId
     */
    public void jsApiTicket(String appId) {
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
        try {
            Map<String, Object> urlParam = new HashMap<>(2);
            urlParam.put("type", "jsapi");
            urlParam.put("access_token", TokenMap.get(Constant.KEY_ACCESS_TOKEN + appId));
            // 请求
            String result = httpHelper.doGet(url, urlParam);
            if (!ObjectUtils.isEmpty(result)) {
                JSONObject jsonTicketObj = JSONObject.parseObject(result);
                if (!ObjectUtils.isEmpty(jsonTicketObj) && Constant.MSG_CODE_ZERO == jsonTicketObj.getIntValue("errcode")) {
                    String ticket = jsonTicketObj.getString("ticket");
                    if (!ObjectUtils.isEmpty(ticket)) {
                        TokenMap.set(Constant.KEY_JS_API_TICKET + appId, ticket);
                        return;
                    }
                    throw new RuntimeException("ticket is empty");
                }
                throw new RuntimeException("jsonTicket is empty or err." + JSONObject.toJSONString(jsonTicketObj));
            }
            throw new RuntimeException("result is empty.");
        } catch (Exception e) {
            log.error("调用微信接口[cgi-bin/ticket/getticket]异常: appId[{}] => ", appId, e);
        }
    }

    /**
     * oauth2授权<br>
     * 成功： { "access_token":"ACCESS_TOKEN", "expires_in":7200, "refresh_token":"REFRESH_TOKEN", "openid":"OPENID", "scope":"SCOPE" }<br>
     * 失败： {"errcode":40029,"errmsg":"invalid code"}
     *
     * @param appId  微信AppId
     * @param secret 微信Secret
     * @param code   授权Code
     * @return 结果
     */
    public JSONObject oauth2Token(String appId, String secret, String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
        try {
            Map<String, Object> urlParam = new HashMap<>(4);
            urlParam.put("appid", appId);
            urlParam.put("secret", secret);
            urlParam.put("code", code);
            urlParam.put("grant_type", "authorization_code");
            String result = httpHelper.doGet(url, urlParam);
            if (!ObjectUtils.isEmpty(result)) {
                return JSONObject.parseObject(result);
            }
            log.error("调用微信接口[sns/oauth2/access_token]失败：result is empty.");
        } catch (Exception e) {
            log.error("调用微信接口[sns/oauth2/access_token]异常.", e);
        }
        return null;
    }

    /**
     * 获取用户基本信息（包括UnionID机制）
     *
     * @param appId  微信AppId
     * @param openId 用户OpenId
     * @return 结果
     */
    public JSONObject getUserInfo(String appId, String openId) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info";
        try {
            Map<String, Object> urlParam = new HashMap<>(3);
            urlParam.put("access_token", TokenMap.get(Constant.KEY_ACCESS_TOKEN + appId));
            urlParam.put("openid", openId);
            urlParam.put("lang", "zh_CN");
            String result = httpHelper.doGet(url, urlParam);
            if (!ObjectUtils.isEmpty(result)) {
                return JSONObject.parseObject(result);
            }
            log.error("调用微信接口[cgi-bin/user/info]失败：{} => result is empty", openId);
        } catch (Exception e) {
            log.error("调用微信接口[cgi-bin/user/info]异常：{}", openId, e);
        }
        return null;
    }

    /**
     * 发送 模板消息
     *
     * @param appId   微信AppId
     * @param jsonStr 模版消息字符串
     * @return 结果
     */
    public JSONObject sendTemplateMsg(String appId, String jsonStr) {
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send";
        try {
            Map<String, Object> urlParam = new HashMap<>(1);
            urlParam.put("access_token", TokenMap.get(Constant.KEY_ACCESS_TOKEN + appId));
            String result = httpHelper.doPost(url, urlParam, jsonStr);
            if (!ObjectUtils.isEmpty(result)) {
                return JSONObject.parseObject(result);
            }
            log.error("调用微信接口[cgi-bin/message/template/send]异常：{} => result is empty", jsonStr);
        } catch (Exception e) {
            log.error("调用微信接口[cgi-bin/message/template/send]异常：{}", jsonStr, e);
        }
        return null;
    }

    /**
     * 查询 自定义菜单<br>
     *
     * @param appId 微信AppId
     * @return 结果
     */
    public JSONObject getMenu(String appId) {
        String url = "https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info";
        try {
            Map<String, Object> urlParam = new HashMap<>(1);
            urlParam.put("access_token", TokenMap.get(Constant.KEY_ACCESS_TOKEN + appId));
            String result = httpHelper.doGet(url, urlParam);
            if (!ObjectUtils.isEmpty(result)) {
                return JSONObject.parseObject(result);
            }
            log.error("调用微信接口[cgi-bin/get_current_selfmenu_info]失败：result is empty");
        } catch (Exception e) {
            log.error("调用微信接口[cgi-bin/get_current_selfmenu_info]异常", e);
        }
        return null;
    }

    /**
     * 创建 自定义菜单<br>
     * 成功： {"errcode":0,"errmsg":"ok"}<br>
     * 失败： {"errcode":40018,"errmsg":"invalid button name size"}
     *
     * @param appId   微信AppId
     * @param jsonStr 菜单字符串
     * @return 结果
     */
    public JSONObject createMenu(String appId, String jsonStr) {
        String url = "https://api.weixin.qq.com/cgi-bin/menu/create";
        Map<String, Object> urlParam = new HashMap<>(1);
        urlParam.put("access_token", TokenMap.get(Constant.KEY_ACCESS_TOKEN + appId));
        try {
            String result = httpHelper.doPost(url, urlParam, jsonStr);
            if (!ObjectUtils.isEmpty(result)) {
                return JSONObject.parseObject(result);
            }
            log.error("调用微信接口[menu/create]失败: {} => result is empty", jsonStr);
        } catch (Exception e) {
            log.error("调用微信接口[menu/create]失败: {}", jsonStr, e);
        }
        return null;
    }

    /**
     * 删除 自定义菜单<br>
     * 成功： {"errcode":0,"errmsg":"ok"}
     *
     * @param appId 微信AppId
     * @return 结果
     */
    public JSONObject deleteMenu(String appId) {
        String url = "https://api.weixin.qq.com/cgi-bin/menu/delete";
        Map<String, Object> urlParam = new HashMap<>(1);
        urlParam.put("access_token", TokenMap.get(Constant.KEY_ACCESS_TOKEN + appId));
        try {
            String result = httpHelper.doGet(url, urlParam);
            if (!ObjectUtils.isEmpty(result)) {
                return JSONObject.parseObject(result);
            }
            log.error("调用微信接口[menu/delete]失败：result is empty");
        } catch (Exception e) {
            log.error("调用微信接口[menu/delete]异常.", e);
        }
        return null;
    }

    @Autowired
    public void setHttpHelper(HttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }
}
