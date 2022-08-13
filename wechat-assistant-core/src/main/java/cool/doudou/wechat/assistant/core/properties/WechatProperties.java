package cool.doudou.wechat.assistant.core.properties;

import cool.doudou.wechat.assistant.core.entity.Credential;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * WechatProperties
 *
 * @author jiangcs
 * @since 2022/06/23
 */
@Data
@ConfigurationProperties(prefix = "wechat")
public class WechatProperties {
    private List<Credential> credentials;
    private String callbackServerAddress;
}
