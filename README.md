# 微服务网关Zeus-开发文档
## 关键组件版本

 组件 | 版本 | 备注 
 :----: | :----: | ---- 
Spring Cloud|Hoxton.SR1|Spring Boot >= 2.2.0.M4 and < 2.2.5.BUILD-SNAPSHOT
Spring Boot|2.2.2.RELEASE|null
Cloud Alibaba|2.1.0.RELEASE|null

### 子模块参数

模块|默认端口|包名|注册服务名
 :----: | :----: | :----: | :----:
注册中心|8761|eureka|eureka
网关|8200|gateway|gateway
用户鉴权服务|8100|auth|auth
测试接口|8001|api|api

### 整体架构
![] (./static/网关架构图.png "网关架构图")

![blockchain](https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/
u=702257389,1274025419&fm=27&gp=0.jpg "区块链")


