# framework-web-helper

在项目的配置文件application.yml中添加配置，**示例中的配置均为默认值**。

## 1.防重复提交

+ 该功能基于请求地址url实现。默认使用TimedCachePreventRepeat进行防重复提交控制；当项目中存在StringRedisTemplate.class时，使用Redis进行防重复提交控制。

+ 请求地址url使用[sessionId + ":" + requestMethod + ":" + requestUrl]()拼接成为防重复提交的key。

### 1.1 启用

```yaml
framework:
  web:
    helper:
      prevent-repeat-submit:
        enabled: true # 开启防重复提交功能
        timeout: 500 # 防重复提交key的超时时间
```
