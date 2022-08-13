package cool.doudou.wechat.assistant.core.entity.msg;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * ImgTextItem
 *
 * @author jiangcs
 * @since 2022/08/13
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ImgTextItem {
    @XmlElement(name = "item")
    private List<ImgTextArticle> imgTextArticleList = new ArrayList<>();

    public void add(ImgTextArticle imgTextArticle) {
        this.imgTextArticleList.add(imgTextArticle);
    }
}
