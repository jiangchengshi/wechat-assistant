package cool.doudou.wechat.assistant.core.entity.msg;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * ReceiveMsg
 *
 * @author jiangcs
 * @since 2022/08/13
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ReceiveMsg {
    /**
     * 开发者微信号
     */
    @XmlElement(name = "ToUserName")
    private String toUserName;
    /**
     * 发送方帐号（一个OpenID）
     */
    @XmlElement(name = "FromUserName")
    private String fromUserName;
    /**
     * 消息创建时间 （整型）
     */
    @XmlElement(name = "FromUserName")
    private Long createTime;
    /**
     * 消息类型
     */
    @XmlElement(name = "MsgType")
    private String msgType;

    // 普通消息
    /**
     * 文本消息内容
     */
    @XmlElement(name = "Content")
    private String content;
    /**
     * 消息id，64位整型
     */
    @XmlElement(name = "MsgId")
    private Long msgId;

    // 事件推送
    /**
     * 事件类型
     */
    @XmlElement(name = "Event")
    private String event;
    /**
     * 扫描带参数二维码事件：
     * 1.用户未关注：事件类型，subscribe；事件KEY值，qrscene_为前缀，后面为二维码的参数值
     * 2.用户已关注：事件类型，SCAN；事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id
     * <p>
     * 自定义菜单事件：
     * 1.点击菜单拉去消息：事件类型，CLICK；事件KEY值，与自定义菜单接口中KEY值对应
     * 2.点击菜单跳转链接：事件类型，VIEW；事件KEY值，设置的跳转URL
     */
    @XmlElement(name = "EventKey")
    private String eventKey;
    /**
     * 二维码的ticket，可用来换取二维码图片
     */
    @XmlElement(name = "Ticket")
    private String ticket;
}
