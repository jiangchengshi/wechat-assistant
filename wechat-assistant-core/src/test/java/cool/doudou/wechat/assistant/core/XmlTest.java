package cool.doudou.wechat.assistant.core;

import cool.doudou.wechat.assistant.core.entity.msg.TextMsg;
import cool.doudou.wechat.assistant.core.util.XmlUtil;
import org.junit.jupiter.api.Test;

/**
 * XmlTest
 *
 * @author jiangcs
 * @since 2022/08/13
 */
public class XmlTest {
    @Test
    public void marshal() {
        try {
            TextMsg textMsg = new TextMsg();
            textMsg.setFromUserName("fromTest");
            textMsg.setToUserName("toTest");
            textMsg.setCreateTime(System.currentTimeMillis());
            textMsg.setMsgType("text");
            textMsg.setContent("test");
            System.out.println(XmlUtil.marshal(textMsg, TextMsg.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
