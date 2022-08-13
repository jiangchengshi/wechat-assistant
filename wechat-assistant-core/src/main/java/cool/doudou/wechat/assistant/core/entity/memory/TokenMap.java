package cool.doudou.wechat.assistant.core.entity.memory;

import java.util.HashMap;
import java.util.Map;

/**
 * TokenMap
 *
 * @author jiangcs
 * @since 2022/08/13
 */
public class TokenMap {
    private static final Map<String, String> map = new HashMap<>(2);

    public static String get(String key) {
        return map.get(key);
    }

    public static void set(String key, String value) {
        map.put(key, value);
    }
}
