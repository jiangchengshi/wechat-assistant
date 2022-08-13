package cool.doudou.wechat.assistant.core.entity.msg;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * ImgTextArticle
 *
 * @author jiangcs
 * @since 2022/08/13
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ImgTextArticle {
    /**
     * 图文消息标题
     */
    @XmlElement(name = "Title")
    private String title;
    /**
     * 图文消息描述
     */
    @XmlElement(name = "Description")
    private String description;
    /**
     * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
     */
    @XmlElement(name = "PicUrl")
    private String picUrl;
    /**
     * 点击图文消息跳转链接
     */
    @XmlElement(name = "Url")
    private String url;
}
