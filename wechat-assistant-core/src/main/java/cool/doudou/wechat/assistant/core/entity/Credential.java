package cool.doudou.wechat.assistant.core.entity;

import lombok.Data;

@Data
public class Credential {
    private String appId;
    private String secret;
    private String token;
}