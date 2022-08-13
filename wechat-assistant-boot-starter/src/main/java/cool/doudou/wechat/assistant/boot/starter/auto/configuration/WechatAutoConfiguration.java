package cool.doudou.wechat.assistant.boot.starter.auto.configuration;

import cool.doudou.wechat.assistant.core.config.OkHttpConfig;
import cool.doudou.wechat.assistant.core.config.WechatConfig;
import cool.doudou.wechat.assistant.core.helper.RespMsgHelper;
import cool.doudou.wechat.assistant.core.properties.WechatProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * WechatAutoConfiguration
 *
 * @author jiangcs
 * @since 2022/2/19
 */
@EnableConfigurationProperties({WechatProperties.class})
@Import({WechatConfig.class, OkHttpConfig.class})
@AutoConfiguration
public class WechatAutoConfiguration {
    @ConditionalOnMissingBean(RespMsgHelper.class)
    @Bean
    public RespMsgHelper respMsgHelper() {
        return new RespMsgHelper();
    }
}
