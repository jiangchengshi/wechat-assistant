package cool.doudou.wechat.assistant.boot.starter.controller;

import cool.doudou.wechat.assistant.boot.starter.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * WechatController
 *
 * @author jiangcs
 * @since 2022/06/30
 */
@RequestMapping("wechat")
@RestController
public class WechatController {
    private WechatService wechatService;

    @GetMapping("notify")
    public String check(String signature, String timestamp, String nonce, String echostr) {
        return wechatService.check(signature, timestamp, nonce, echostr);
    }

    @PostMapping("notify")
    public void notify(@RequestBody String xml) {
        wechatService.notify(xml);
    }

    @GetMapping("oauth2")
    public ModelAndView oauth2(String code, String state) {
        return wechatService.oauth2(code, state);
    }

    @PostMapping("access-token")
    public String accessToken() {
        return wechatService.accessToken();
    }

    @PostMapping("js-api-ticket")
    public String jsApiTicket() {
        return wechatService.jsApiTicket();
    }

    @GetMapping("menu/get/{appId}")
    public String get(@PathVariable("appId") String appId) {
        return wechatService.get(appId);
    }

    @PostMapping("menu/add/{appId}")
    public boolean add(@PathVariable("appId") String appId, @RequestBody String json) {
        return wechatService.add(appId, json);
    }

    @PostMapping("menu/delete/{appId}")
    public boolean delete(@PathVariable("appId") String appId) {
        return wechatService.delete(appId);
    }

    @Autowired
    public void setPayNotifyService(WechatService wechatService) {
        this.wechatService = wechatService;
    }
}
