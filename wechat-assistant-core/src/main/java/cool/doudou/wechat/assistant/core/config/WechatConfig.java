package cool.doudou.wechat.assistant.core.config;

import cool.doudou.wechat.assistant.core.api.WechatApi;
import cool.doudou.wechat.assistant.core.helper.HttpHelper;
import cool.doudou.wechat.assistant.core.processor.WechatNotifyBeanPostProcessor;
import cool.doudou.wechat.assistant.core.properties.WechatProperties;
import cool.doudou.wechat.assistant.core.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;

/**
 * WechatConfig
 *
 * @author jiangcs
 * @since 2022/2/20
 */
@Slf4j
public class WechatConfig {
    @Bean
    public HttpHelper httpHelper() {
        return new HttpHelper();
    }

    @Bean
    public WechatApi wechatApi() {
        return new WechatApi();
    }

    @Bean
    public WechatNotifyBeanPostProcessor wxNotifyBeanPostProcessor() {
        return new WechatNotifyBeanPostProcessor();
    }

    @Autowired
    public void init(WechatProperties wechatProperties, WechatApi wechatApi) {
        if (ObjectUtils.isEmpty(wechatProperties.getCredentials())) {
            throw new RuntimeException("微信凭证信息未配置");
        }

        if (ObjectUtils.isEmpty(wechatProperties.getCallbackServerAddress())) {
            throw new RuntimeException("微信回调服务信息缺失");
        }

        try {
            ThreadPoolUtil.getScheduledInstance()
                    .scheduleAtFixedRate(
                            () -> wechatProperties.getCredentials().forEach(
                                    credential -> {
                                        // AccessToken
                                        wechatApi.accessToken(credential.getAppId(), credential.getSecret());
                                        // jsApiToken
                                        wechatApi.jsApiTicket(credential.getAppId());
                                    }
                            ),
                            500, 7000, TimeUnit.SECONDS
                    );
        } catch (Exception e) {
            log.error("加载微信密钥异常: ", e);
        }
    }
}
