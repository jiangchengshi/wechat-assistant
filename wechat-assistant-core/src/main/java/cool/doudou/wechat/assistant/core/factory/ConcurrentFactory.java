package cool.doudou.wechat.assistant.core.factory;

import cool.doudou.wechat.assistant.core.entity.msg.ReceiveMsg;

import java.util.function.Function;

/**
 * ConcurrentFactory
 *
 * @author jiangcs
 * @since 2022/08/13
 */
public class ConcurrentFactory {
    private static Function<ReceiveMsg, Boolean> function;

    public static Function<ReceiveMsg, Boolean> get() {
        return function;
    }

    public static void set(Function<ReceiveMsg, Boolean> function) {
        ConcurrentFactory.function = function;
    }
}
