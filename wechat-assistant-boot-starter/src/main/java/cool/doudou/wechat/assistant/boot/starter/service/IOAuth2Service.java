package cool.doudou.wechat.assistant.boot.starter.service;

/**
 * IOAuth2Service
 *
 * @author jiangcs
 * @since 2022/08/13
 */
public interface IOAuth2Service {
    /**
     * @param state 重定附带参数
     * @return 前端 路由
     */
    String route(String state);
}
