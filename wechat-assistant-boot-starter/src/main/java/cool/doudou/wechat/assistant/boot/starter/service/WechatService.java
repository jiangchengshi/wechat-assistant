package cool.doudou.wechat.assistant.boot.starter.service;

import com.alibaba.fastjson2.JSONObject;
import cool.doudou.wechat.assistant.core.Constant;
import cool.doudou.wechat.assistant.core.api.WechatApi;
import cool.doudou.wechat.assistant.core.entity.Credential;
import cool.doudou.wechat.assistant.core.entity.msg.ReceiveMsg;
import cool.doudou.wechat.assistant.core.factory.ConcurrentFactory;
import cool.doudou.wechat.assistant.core.helper.RespMsgHelper;
import cool.doudou.wechat.assistant.core.properties.WechatProperties;
import cool.doudou.wechat.assistant.core.util.SignUtil;
import cool.doudou.wechat.assistant.core.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * WechatService
 *
 * @author jiangcs
 * @since 2022/07/04
 */
@Slf4j
@Service
public class WechatService {
    private WechatProperties wechatProperties;
    private WechatApi wechatApi;
    private IOAuth2Service oauth2Service;
    private RespMsgHelper respMsgHelper;

    public String check(String signature, String timestamp, String nonce, String echostr) {
        // 判断参数是否正常
        if (ObjectUtils.isEmpty(signature) || ObjectUtils.isEmpty(timestamp)
                || ObjectUtils.isEmpty(nonce) || ObjectUtils.isEmpty(echostr)) {
            log.error("微信服务器验证失败：参数异常，拒绝访问.");
            return "";
        }

        try {
            if (wechatProperties.getCredentials().isEmpty()) {
                log.error("微信服务器验证失败：参数配置错误.");
                return "";
            }

            // 参数签名验证：成功返回echostr，则微信开发者接入成功
            for (Credential credential : wechatProperties.getCredentials()) {
                String signatureStr = SignUtil.server(credential.getToken(), timestamp, nonce);
                if (signature.equals(signatureStr)) {
                    log.info("微信服务器验证通过：appId[{}]", credential.getAppId());
                    return echostr;
                }
            }

            log.error("微信服务器验证失败：拒绝访问.");
            return "";
        } catch (Exception e) {
            log.error("微信服务器验证异常：拒绝访问.：", e);
            return "";
        }
    }

    public ModelAndView oauth2(String code, String state) {
        ModelAndView modelAndView;

        try {
            // 重定向后会带上 state 参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
            // 多个参数以"_"分隔，第1个参数必须是appId
            String[] stateArray = state.split("_");
            if (stateArray.length == 0) {
                throw new RuntimeException("微信授权回掉参数state验证失败");
            }

            String appId = stateArray[0], secret = null;
            for (Credential credential : wechatProperties.getCredentials()) {
                if (appId.equals(credential.getAppId())) {
                    secret = credential.getSecret();
                    break;
                }
            }
            if (secret == null) {
                throw new RuntimeException("微信授权密钥验证失败");
            }

            // 通过code换取网页授权access_token
            JSONObject resultObj = wechatApi.oauth2Token(appId, secret, code);
            if (resultObj == null) {
                throw new RuntimeException("微信授权验证失败");
            }

            // OPENID
            String openId = resultObj.getString("openid");
            if (ObjectUtils.isEmpty(openId)) {
                throw new RuntimeException("微信通讯标识获取失败");
            }

            modelAndView = new ModelAndView("redirect:" + wechatProperties.getCallbackServerAddress() + "/#/"
                    + oauth2Service.route(wechatProperties.getCallbackServerAddress(), state));
        } catch (Exception e) {
            log.error("微信授权oauth2异常：", e);

            // 返回错误页面
            modelAndView = new ModelAndView("redirect:" + wechatProperties.getCallbackServerAddress() + "/#/error");
        }
        return modelAndView;
    }

    public void notify(String xml) {
        log.info("notice：{}", xml);

        try {
            ReceiveMsg receiveMsg = (ReceiveMsg) XmlUtil.unMarshal(xml, ReceiveMsg.class);
            // respFlag为false，需要回复默认ok
            boolean respFlag = ConcurrentFactory.get().apply(receiveMsg);
            if (!respFlag) {
                respMsgHelper.ok("success");
            }
        } catch (Exception e) {
            log.error("消息通知异常", e);
        }
    }

    public String accessToken() {
        wechatProperties.getCredentials()
                .forEach(credential -> wechatApi.accessToken(credential.getAppId(), credential.getSecret()));
        return "ok";
    }

    public String jsApiTicket() {
        wechatProperties.getCredentials()
                .forEach(credential -> wechatApi.jsApiTicket(credential.getAppId()));
        return "ok";
    }

    public String get(String appId) {
        JSONObject resultObj = wechatApi.getMenu(appId);
        if (resultObj != null) {
            return resultObj.toString();
        }
        return null;
    }

    public boolean add(String appId, String json) {
        JSONObject resultObj = wechatApi.createMenu(appId, json);
        if (resultObj != null) {
            int code = resultObj.getInteger("errcode");
            return Constant.MSG_CODE_ZERO == code;
        }
        return false;
    }

    public boolean delete(String appId) {
        JSONObject resultObj = wechatApi.deleteMenu(appId);
        if (resultObj != null) {
            int code = resultObj.getInteger("errcode");
            return Constant.MSG_CODE_ZERO == code;
        }
        return false;
    }

    @Autowired
    public void setWechatProperties(WechatProperties wechatProperties) {
        this.wechatProperties = wechatProperties;
    }

    @Autowired
    public void setWechatApi(WechatApi wechatApi) {
        this.wechatApi = wechatApi;
    }

    @Autowired
    public void setOauth2Service(IOAuth2Service oauth2Service) {
        this.oauth2Service = oauth2Service;
    }

    @Autowired
    public void setRespMsgHelper(RespMsgHelper respMsgHelper) {
        this.respMsgHelper = respMsgHelper;
    }
}
