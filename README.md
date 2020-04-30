# 微服务网关Zeus-开发文档
## 关键组件版本

| 组件 | 版本 | 备注 |
| ---- | :---- | :---- |
Spring Cloud|Hoxton.SR1|Spring Boot >= 2.2.0.M4 and < 2.2.5.BUILD-SNAPSHOT
Spring Boot|2.2.2.RELEASE|null
Cloud Alibaba|2.1.0.RELEASE|null

### 子模块参数

模块|默认端口|包名|注册服务名
:--|:--|:--|:--
注册中心|8761|eureka|eureka-server
网关|8200|gateway|gateway-server
用户鉴权服务|8001|auth|auth


| 参数 | 内容 | 类型 | 
| :---: | :---: | :----: | 
| name | 事件名 | string |
| show_name | 显示名 | string |
| status | 显示状态 多个以逗号分隔(0,1) | string | 
| label | 标签 多个以逗号分隔 ("login","track") | string |
| page | 页码 （默认 1） | int| 
| limit | 每页数量 （默认 50）| int |