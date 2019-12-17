# framework-fastdfs

在项目的配置文件application.yml中添加配置，**示例中的配置均为默认值**。

## 1.1 启用

```yaml
# framework
framework:
  fastdfs:
    enabled: true
```
## 1.2 trackerServerPool配置

```yaml
# framework
framework:
  fastdfs:
    max-file-size: 1MB # 上传文件最大限制
    min-idle: 2 # 最小空闲连接数
    max-idle: 8 # 最大空闲连接数
    max-total: 8 # 最大活动连接数
    reverse-proxy-address: # 配合nginx使用的反向代理地址
```

## 1.3 FastDFSClient配置

```yaml
# framework
framework:
  fastdfs:
    properties:
      charset: UTF-8 # 编码
      connect_timeout_in_seconds: 5 # 连接超时(秒)
      network_timeout_in_seconds: 30 # 网络超时(秒)
      http_anti_steal_token: fasle # http反窃取令牌
      http_secret_key: FastDFS1234567890 # http密钥
      http_tracker_http_port: 80 # http跟踪器http端口
      tracker_servers:  # tracker servers地址(server的IP和端口用冒号':'分隔;server之间用逗号','分隔)
```
