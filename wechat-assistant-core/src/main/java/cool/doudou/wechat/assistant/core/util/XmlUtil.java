package cool.doudou.wechat.assistant.core.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * XmlUtil
 *
 * @author jiangcs
 * @since 2022/08/13
 */
public class XmlUtil {
    private static JAXBContext jaxbContext;

    /**
     * 解析 xml格式字符串 到 对象
     *
     * @param xml   XML字符串
     * @param clazz 类
     * @return 对象
     * @throws Exception 解析异常
     */
    public static Object unMarshal(String xml, Class<?> clazz) throws Exception {
        jaxbContext = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(xml);
        return unmarshaller.unmarshal(reader);
    }

    /**
     * 解析 对象 到 xml格式字符串
     *
     * @param obj   对象
     * @param clazz 类
     * @return 字符串
     * @throws Exception 解析异常
     */
    public static String marshal(Object obj, Class<?> clazz) throws Exception {
        jaxbContext = JAXBContext.newInstance(clazz);
        Marshaller marshaller = jaxbContext.createMarshaller();
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        return writer.toString().replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
    }
}
