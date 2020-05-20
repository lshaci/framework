# framework-swagger
该模块基于swagger UI和spring boot web构建的restful风格的api文档
> latest-version: 1.0.4

# swagger api使用

## 1.引入依赖
```xml
<dependency>
    <groupId>top.lshaci</groupId>
    <artifactId>framework-swagger</artifactId>
    <version>${latest-version}</version>
</dependency>
```

## 2.启用(默认开启)
在yml配置文件中添加如下配置:
```yaml
framework:
  swagger:
    enabled: true # 生产环境中可设为false, 以关闭swagger ui
```

## 3.无分组配置
在yml配置文件中添加swagger必须的配置：
```yaml
framework:
  swagger:
    base-package: ${controller-package} # 根据项目实际情况填写
    title: XXX项目API文档
    description: XXX项目API文档描述
    version: 1.0
    contact: # 联系信息
      name: ${your-name} # 根据实际情况修改
      email: ${your-email} # 根据实际情况修改
```
更多配置信息请查看: 
> top.lshaci.framework.swagger.properties.FrameworkSwaggerProperties

## 4.分组配置
在yml配置文件中添加swagger必须的配置：
```yaml
framework:
  swagger:  # 可配置分组中的公共信息
    description: XXX项目API文档描述
    contact:
      name: ${your-name} # 根据实际情况修改
      email: ${your-email} # 根据实际情况修改
    docket: # 分组配置开始
      common: # 分组名称
        base-package: ${controller-common-package} # 根据项目实际情况填写
        title: 公共接口
        description: XXX项目API文档描述
        version: 1.0
      manage: # 分组名称
        base-package: ${controller-manage-package} # 根据项目实际情况填写
        title: 管理中心
        description: XXX项目API文档描述
        version: 1.0
```

## 5.全局参数配置
在yml配置文件中添加swagger必须的配置：
```yaml
# framework
framework:
  # swagger
  swagger:
    global-parameters:
    - name: Auth
      type: header
      required: true
      modelRef: string
      description: 登录凭证
```
更多配置信息请查看: 
> top.lshaci.framework.swagger.properties.FrameworkSwaggerProperties$DocketInfo

# Swagger2Doc使用

## 1.引入依赖
```xml
<dependency>
    <groupId>top.lshaci</groupId>
    <artifactId>framework-utils-spring</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-core</artifactId>
    <version>${hutool.version}</version>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
</dependency>
```

## 2.配置
```yaml
# spring
spring:
  # freemarker
  freemarker:
    prefer-file-system-access: false
    settings:
      classic_compatible: true
      number_format: 0
```

## 3.使用
```java
public class Swagger2DocTest {
    public void test() {
        String json = ""; // 获取到的swagger api json字符串
        FileOutputStream os = new FileOutputStream("E:/swagger.doc");
        Swagger2Doc.generate(json, os);
    }
}
```
