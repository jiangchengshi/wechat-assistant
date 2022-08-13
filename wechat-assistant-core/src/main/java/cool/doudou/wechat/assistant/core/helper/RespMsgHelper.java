package cool.doudou.wechat.assistant.core.helper;

import com.alibaba.fastjson2.JSONObject;
import cool.doudou.wechat.assistant.core.Constant;
import cool.doudou.wechat.assistant.core.api.WechatApi;
import cool.doudou.wechat.assistant.core.entity.msg.*;
import cool.doudou.wechat.assistant.core.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * RespMsgHelper
 *
 * @author jiangcs
 * @since 2022/08/13
 */
@Slf4j
public class RespMsgHelper {
    private WechatApi wechatApi;

    /**
     * 通用回复
     *
     * @param content 内容
     */
    public void ok(String content) {
        write(content);
    }

    /**
     * 回复 文本消息
     *
     * @param receiveMsg 微信回复消息对象
     * @param content    文本内容
     */
    public void text(ReceiveMsg receiveMsg, String content) {
        try {
            TextMsg textMsg = new TextMsg();
            textMsg.setFromUserName(receiveMsg.getToUserName());
            textMsg.setToUserName(receiveMsg.getFromUserName());
            textMsg.setCreateTime(System.currentTimeMillis());
            textMsg.setMsgType("text");
            textMsg.setContent(content);
            write(XmlUtil.marshal(textMsg, TextMsg.class));
            log.info("回复文本消息成功：{}", receiveMsg.getFromUserName());
        } catch (Exception e) {
            log.error("回复文本消息异常", e);
        }
    }

    /**
     * 回复 图文消息
     *
     * @param receiveMsg  微信回复消息对象
     * @param articleList 图文列表
     */
    public void imgText(ReceiveMsg receiveMsg, List<Map<String, String>> articleList) {
        try {
            ImgTextMsg imgTextMsg = new ImgTextMsg();
            imgTextMsg.setFromUserName(receiveMsg.getToUserName());
            imgTextMsg.setToUserName(receiveMsg.getFromUserName());
            imgTextMsg.setCreateTime(System.currentTimeMillis());
            imgTextMsg.setMsgType("news");
            imgTextMsg.setArticleCount(articleList.size());

            ImgTextItem imgTextItem = new ImgTextItem();
            ImgTextArticle imgTextArticle;
            for (Map<String, String> article : articleList) {
                imgTextArticle = new ImgTextArticle();
                imgTextArticle.setTitle(article.get("title"));
                imgTextArticle.setDescription(article.get("description"));
                imgTextArticle.setPicUrl(article.get("picUrl"));
                imgTextArticle.setUrl(article.get("url"));
                imgTextItem.add(imgTextArticle);
            }
            imgTextMsg.setImgTextItem(imgTextItem);
            write(XmlUtil.marshal(imgTextMsg, ImgTextMsg.class));
            log.info("回复图文消息成功：{}", receiveMsg.getFromUserName());
        } catch (Exception e) {
            log.error("回复图文消息异常", e);
        }
    }

    /**
     * 发送 模板消息
     *
     * @param appId         微信AppId
     * @param openId        用户OpenId
     * @param templateId    模版Id
     * @param templateParam 模版参数
     */
    public void template(String appId, String openId, String templateId, Map<String, Object> templateParam) {
        // 消息体
        JSONObject json = new JSONObject();
        json.put("touser", openId);
        json.put("template_id", templateId);
        json.put("data", getTemplateData(templateParam));
        JSONObject resultObj = wechatApi.sendTemplateMsg(appId, json.toString());
        if (!ObjectUtils.isEmpty(resultObj)) {
            if (Constant.MSG_CODE_ZERO == resultObj.getIntValue("errcode")) {
                log.info("模板消息发送成功：{}", json);
            } else {
                log.error("模板消息发送失败：" + resultObj.getString("errmsg"));
            }
        } else {
            log.error("模板消息发送失败：返回结果为空");
        }
    }

    /**
     * 获取模板组合数据
     *
     * @param templateParam 模版参数
     * @return 模版数据对象
     */
    private JSONObject getTemplateData(Map<String, Object> templateParam) {
        JSONObject data = new JSONObject();

        // first
        JSONObject first = new JSONObject();
        first.put("value", templateParam.remove("first"));
        first.put("color", "#173177");
        data.put("first", first);

        // remark
        JSONObject remark = new JSONObject();
        remark.put("value", templateParam.remove("remark"));
        remark.put("color", "#173177");
        data.put("remark", remark);

        // keyword
        JSONObject keyword;
        for (String key : templateParam.keySet()) {
            keyword = new JSONObject();
            keyword.put("value", templateParam.get(key));
            keyword.put("color", "#173177");
            data.put(key, keyword);
        }
        return data;
    }

    /**
     * 写入 请求客户端数据
     *
     * @param content 文本内容
     */
    private void write(String content) {
        Writer writer = null;
        try {
            HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
            assert response != null;
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            writer = response.getWriter();
            writer.write(content);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Autowired
    public void setWechatApi(WechatApi wechatApi) {
        this.wechatApi = wechatApi;
    }
}
