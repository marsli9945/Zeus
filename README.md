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
![](/static/gateway.png)

### 网关使用

>使用规则：

- 路由规则：网关域名+服务名+资源路径
- 使用者调用资源服务只能统一通过gateway网关进行调用
- gateway网关会对每次请求校验jwt令牌(access_token)合法性和时效性
- 令牌使用方式：将access_token放入请求头信息加入Authorization字段
- access_token有效期内无法重复登录，只能使用refresh_token刷新有效期
- 刷新后获得新的access_token及refresh_token,原有access_token有效期内还能正常使用
- access_token可注销，注销后重新登录获取新的令牌

>调用失败
```json
{
    "code": 401,
    "message": "暂未登录或token已经过期"
}
```

>登录参数
```json
{
  "clientId": "net5ijy",
  "clientSecret": "123456",
  "password": "123456",
  "username": "admin001"
}
```

>刷新令牌参数
```json
{
	"clientId": "net5ijy",
	"clientSecret": "123456",
	"refreshToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODgxNjQ0NTcsInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6IjVjMzYyNjdlLThjOTgtNDc3Ny04Yzk4LTY4OGE1NjZmZDYyZiIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.Yf0xoZCFCuWo4cZRheV5neDTbcyqUhAiTyjxDxTrsgnC6R9CWAmY83rnu_WEbHUGvOY7DsIkjo-G4x25TxPKFbQF3B4DNYR9VByPAvme5GcZfRYfLIqPRe8K9kEqJgN6zD3y8Beql317PdH4P1hScb9XVrpoGIvPWCHW07W2WQZMYe0-hnn-cp_mJkfkR80lxbHLXLC4v5PKMzAjIFo0sYEa0WzA2ORn08eGWld4DaW8_mw0lcpjbUVrpWm878jRV-y36PvQiQatJd5xrNkhwyzFWqqCig2d84qXxGhPYH3o0r0K2PmLvLiHEacIRHyjTjCCdNhfLgRbTMJ9Wfinsw"
}
```
>获取的令牌结果
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODgyNTE0NzcsInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6ImYzODVhNTNmLWIwZTYtNDg0Yy1hYzRhLTA5NjI1NGQwOTZkOCIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.ICifT9_4_ww3yCJWYqlbxRtdxep4DyzaMgJsPiYt9akp_bZye82WReRFHfJTzei9zmDdqOIoTiDPaOmtoiX7P0wOCcQfSvbScVoZKdG6i7TRsn2ssuqEo6pI25fw5wJB77mVUQ3OpJ3s0GhjkT743QEHSDMzV7RNomRsSoqdNhOCwqrNjUrMJxN-kEODF9WLyvE4xRrsBMg5iU891dIjSIFWRXv8XHIIBcTp5fxEsQS3EEoG-XrG7KF2-fVVB0xFOYOdAky_bJKcjJ13ojvu7z8JdksStT85T45XOcTz9tOe8iGs-0BtyKhk14eoSMeYoVE_qstprX21ux7CHAbopQ",
        "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbjAwMSIsInNjb3BlIjpbImFsbCIsInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiJmMzg1YTUzZi1iMGU2LTQ4NGMtYWM0YS0wOTYyNTRkMDk2ZDgiLCJleHAiOjE1ODgyNTE0NzcsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iLCJST0xFX0RCQSJdLCJqdGkiOiJkNGE1YTQ4OS03Zjk2LTQ0MmQtODY0Ny00NDQyNmJlMzBkNDYiLCJjbGllbnRfaWQiOiJuZXQ1aWp5In0.GlbgVF-_a_m49ZcHErvNmuvwnlSFG_eenkSbQ5sw2k9CCT-J1ZUTseBkgEx_NXWW-dFyC845JtjhVdVoPbRByCemAXfMBHBumwAskSIdRvavvN3RH4NP1GBi0xej3BjMYw1Vgiua4ltriN1BBqtGk5nzTr_2UdpVC9Ptq1wj5nnnvBlVumJq6U31oIhUL7eWuIIFC49PYDFjoS1OnPfYUOT-n5fa55lz7dsaLlaEFQ0RDvuxVh5THep6-GbcccHZ3voZcUQAdZAVSileTVaGPtrZkr9TwEMJGHZ3yaJ9GdtrawwCsBbwxmiHtvyX9x9rLLLVOtv2WXoYlxIVE9rIaw",
        "scope": "all read write",
        "token_type": "bearer",
        "expires_in": 7199,
        "jti": "f385a53f-b0e6-484c-ac4a-096254d096d8"
    }
}
```
