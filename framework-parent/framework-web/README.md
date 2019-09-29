# framework-web

## 1.全局异常处理
该功能基于spring boot中的@ControllerAdvice构建

### 1.1 启用(默认开启)
+ 在yml配置文件中添加如下配置:
```yaml
framework:
  web:
    enabled:
      global-exception-handler: true
```
+ 框架中已定义的异常, 参考: **top.lshaci.framework.web.enums.ErrorCode**
### 1.2 添加其它异常
在项目启动后调用top.lshaci.framework.web.utils.GlobalExceptionUtils中的put方法, 如下:
```java
public class Application {
    static {
        GlobalExceptionUtils.put(new ExceptionMessage(50025, "异常消息", RuntimeException.class));
    }
}
```
## 2.防重复提交
该功能基于访问url和session进行构建, 集群项目请谨慎使用(使用session共享的项目可以使用)

### 2.1 启用(默认开启)
在yml配置文件中添加如下配置:
```yaml
framework:
  web:
    enabled:
      prevent-repeat-submit: true
```
## 3.控制层请求日志
该功能用于打印标记有注解@RequestMapping, @GetMapping, @PostMapping, @PutMapping, @DeleteMapping, @PatchMapping的接口的参数和返回数据

### 3.1 启用(默认关闭)
在yml配置文件中添加如下配置:
```yaml
framework:
  web:
    enabled:
      web-log: true
logging:
  level:
    top.lshaci.framework.web.aspect.WebLogAspect: debug
```
