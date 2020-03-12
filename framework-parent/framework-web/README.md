# framework-web

## 1.全局异常处理
该功能基于spring boot中的@ControllerAdvice构建

### 1.1 启用(默认开启)
+ 在yml配置文件中添加如下配置:
```yaml
framework:
  web:
    global-exception-handler: 
      enabled: true
```
+ 框架中已定义的异常, 参考: **top.lshaci.framework.web.enums.ErrorCode**

### 1.2 代码中添加其它异常
在项目启动后调用top.lshaci.framework.web.utils.GlobalExceptionUtils中的put方法, 如下:
```java
public class Application {
    static {
        GlobalExceptionUtils.put(new ExceptionMessage(50025, "异常消息", RuntimeException.class));
    }
}
```

### 1.3 配置中添加其它异常
在项目的配置文件application.yml中添加如下配置:
```yaml
framework:
  web:
    global-exception-handle: 
      messages:
      - code: 50025
        message: 算术计算异常
        exception-class: java.lang.ArithmeticException
      - code: 50026
        message: 空指针异常
        exception-class: java.lang.NullPointerException
```

## 2.防重复提交

+ 该功能基于请求地址url实现。默认使用TimedCachePreventRepeat进行防重复提交控制；当项目中存在StringRedisTemplate.class时，使用Redis进行防重复提交控制。

+ 默认使用[sessionId + ":" + requestMethod + ":" + requestUrl]()拼接成为防重复提交的key。

### 1.1 启用

```yaml
framework:
  web:
    prevent-repeat-submit:
      enabled: true # 开启防重复提交功能
      timeout: 500 # 防重复提交key的超时时间
```
