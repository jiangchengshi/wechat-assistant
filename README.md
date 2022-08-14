# 欢迎使用 Wechat-Assistant

[![Maven](https://img.shields.io/badge/Maven-v2.0.0-blue)](https://search.maven.org/search?q=g:cool.doudou%20a:wechat-assistant-*)
[![License](https://img.shields.io/badge/License-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0)
![SpringBoot](https://img.shields.io/badge/SpringBoot-v2.7.2-blue)

## 简介

微信公众号助手 - 简化配置，注解带飞！

## 特点

> 基于微信公众号官方API封装

- 简单注解配置，即可接入微信公众号
- 省掉碍眼繁琐的签名与验签，简化交互过程
- 支持多公众号接入

## 使用指引

### 引入依赖

```kotlin
implementation("cool.doudou:wechat-assistant-boot-starter:latest")
```

### 微信支付配置

> 配置属性如下：

```yaml
wechat:
  credentials:
    - appId: wx0000000000001
      secret: xxxxxxxxxxxxxx
      token: test1234567890
  callback-server-address: http://127.0.0.1:8000
```

### 使用方式

> 消息：API

```java
public class RespMsgHelper {
    // 文本消息
    public void text(ReceiveMsg receiveMsg, String content);

    // 图文消息
    public void imgText(ReceiveMsg receiveMsg, List<Map<String, String>> articleList);

    // 模版消息
    public void template(String appId, String openId, String templateId, Map<String, Object> templateParam);
}
```

> 消息通知：注解方法

```java

@Component
public class WxNotifyComponent {
    @WechatNotify
    public void wechatNotify(ReceiveMsg receiveMsg) {
        System.out.println(receiveMsg);
    }
}
```

> 网页授权：重定向后会带上 state 参数，多个以"_"分隔，第一个参数必须为有效的appId

```java

@Component
public class OAuth2ServiceImpl implements IOAuth2Service {
    @Override
    public String route(String callbackServerAddress, String state) {
        String[] stateArr = state.split("_");
        return new ModelAndView("redirect:" + callbackServerAddress + "/#/home?" + stateArr[0] + "&" + stateArr[1]);
    }
}
```

### 其他说明

> 验证消息的确来自微信服务器：开发者提交信息后，微信服务器将发送 GET 请求到填写的服务器地址 URL 上

- GET /wechat/notify

> 接收普通消息：当普通微信用户向公众账号发消息时，微信服务器将 POST 消息的 XML 数据包到开发者填写的 URL 上

- POST /wechat/notify

> 接收事件推送：微信用户和公众号产生交互的过程中，用户的某些操作会使得微信服务器通过事件推送的形式通知到开发者在开发者中心处设置的服务器地址

- POST /wechat/notify

> 菜单管理

- GET /menu/get/{appId}
- POST /menu/create/{appId}
- POST /menu/delete/{appId}

> Token管理

- POST /access-token
- POST /js-api-ticket

## 版权

[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)

## 鼓励一下，喝杯咖啡

> 欢迎提出宝贵意见，不断完善 MQ-Assistant

![鼓励一下，喝杯咖啡](https://user-images.githubusercontent.com/21210629/172556529-544b2581-ea34-4530-932b-148198b1b265.jpg)
