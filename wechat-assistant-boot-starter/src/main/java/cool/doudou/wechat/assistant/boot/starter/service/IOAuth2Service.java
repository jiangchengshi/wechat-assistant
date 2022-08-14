package cool.doudou.wechat.assistant.boot.starter.service;

/**
 * IOAuth2Service
 *
 * @author jiangcs
 * @since 2022/08/13
 */
public interface IOAuth2Service {
    /**
     * @param callbackServerAddress 回调服务地址
     * @param extraParam            额外参数
     * @return 前端 路由
     */
    String route(String callbackServerAddress, String extraParam);
}
