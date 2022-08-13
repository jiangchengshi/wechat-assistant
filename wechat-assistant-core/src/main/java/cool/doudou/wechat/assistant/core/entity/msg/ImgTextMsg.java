package cool.doudou.wechat.assistant.core.entity.msg;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * ImgTextMsg
 *
 * @author jiangcs
 * @since 2022/08/13
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ImgTextMsg {
    /**
     * 接收方帐号（收到的OpenID）
     */
    @XmlElement(name = "ToUserName")
    private String toUserName;
    /**
     * 开发者微信号
     */
    @XmlElement(name = "FromUserName")
    private String fromUserName;
    /**
     * 消息创建时间 （整型）
     */
    @XmlElement(name = "CreateTime")
    private Long createTime;
    /**
     * 消息类型
     */
    @XmlElement(name = "MsgType")
    private String msgType;
    @XmlElement(name = "ArticleCount")
    private Integer articleCount;
    @XmlElement(name = "Articles")
    private ImgTextItem imgTextItem;
}
