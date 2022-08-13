package cool.doudou.wechat.assistant.core.processor;

import cool.doudou.wechat.assistant.annotation.WechatNotify;
import cool.doudou.wechat.assistant.core.factory.ConcurrentFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Arrays;

/**
 * WechatNotifyBeanPostProcessor
 *
 * @author jiangcs
 * @since 2022/2/20
 */
@Slf4j
@AllArgsConstructor
public class WechatNotifyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Arrays.stream(bean.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(WechatNotify.class))
                .forEach(method -> {
                    if (ConcurrentFactory.get() != null) {
                        log.error("there are multiple @WechatNotify annotations");
                        return;
                    }

                    ConcurrentFactory.set(receiveMsg -> {
                        try {
                            method.setAccessible(true);
                            return (Boolean) method.invoke(bean, receiveMsg);
                        } catch (Exception e) {
                            throw new RuntimeException("bean[" + bean + "].method[" + method + "]invoke exception: ", e);
                        }
                    });
                });
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
