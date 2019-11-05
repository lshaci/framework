# framework-swagger
该模块基于swagger UI和spring boot web构建的restful风格的api文档
> latest-version: 1.0.4

# Start
## 1.引入依赖
```xml
<dependency>
    <groupId>top.lshaci</groupId>
    <artifactId>framework-swagger</artifactId>
    <version>${latest-version}</version>
</dependency>
```

### 1.1 启用(默认开启)
在yml配置文件中添加如下配置:
```yaml
framework:
  swagger:
    enabled: true # 生产环境中可设为false, 以关闭swagger ui
```

### 1.2 无分组配置
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

### 1.3 分组配置
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
更多配置信息请查看: 
> top.lshaci.framework.swagger.properties.FrameworkSwaggerProperties$DocketInfo
