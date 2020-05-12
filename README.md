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
![](static/gateway.png)

### 网关使用

>使用规则：

- 路由规则：网关域名+服务名+资源路径
- 使用者调用资源服务只能统一通过gateway网关进行调用
- gateway网关会对每次请求校验jwt令牌(access_token)合法性和时效性
- 令牌使用方式：将access_token放入请求头信息加入Authorization字段
- access_token有效期内无法重复登录，只能使用refresh_token刷新有效期
- 刷新后获得新的access_token及refresh_token,原有access_token有效期内还能正常使用
- access_token可注销，注销后重新登录获取新的令牌

>校验失败
```json
{
    "code": 401,
    "message": "暂未登录或token已经过期"
}
```

>登录接口：/auth/login @post
```json
{
  "clientId": "net5ijy",
  "clientSecret": "123456",
  "password": "123456",
  "username": "admin001"
}
```

>刷新令牌接口：/auth/refresh @post
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

### 性能测试

>测试环境
- Mac mini (Late 2014)
- 处理器 3 GHz 双核Intel Core i7
- 内存 16 GB 1600 MHz DDR3

>对照组
1. 无jwt校验服务本体调用
2. 无jwt校验网关服务调用
3. jwt校验网关服务调用
4. Redis+jwt校验网关服务调用

>实验样本
1. /api 无任何操作，只返回响应数据格式
2. /io500 sleep 500ms 模拟io操作
3. /io1000 sleep 1000ms 模拟io操作

>实验方式
1. 100次请求，10次并发
2. 1000次请求，100次并发
3. 10000次请求，300次并发

#### 结果统计
##### 吞吐率
吞吐率reqs/s | 无jwt校验服务本体调用 | 无jwt校验网关服务调用 | jwt校验网关服务调用 | Redis+jwt校验网关服务调用
 :----: | :----: | :----: | :----: | :----: 
无io-100/10 | 4780.11 | 1422.96 | 934.89 | 773.11
无io-1000/100 | 4467.02 | 1336.04 | 757.67 | 703.97
无io-10000/100 | 4063.22 | 1228.22 | 702.78 | 644.33
io500-100/10 | 18 | 17.87 | 17.81 | 17.78
io500-1000/100 | 177.52 | 171.93 | 169.07 | 168.11
io500-10000/100 | 192.05 | 190.73 | 190.51 | 189.86
io1000-100/10 | 9.04 | 9.02 | 9 | 9
io1000-1000/100 | 90 | 88.61 | 87.9 | 87.64
io1000-10000/100 | 98.31 | 97.85 | 97.68 | 97.59
![](static/reqs_tred.png)

##### 平均耗时
平均耗时/ms | 无jwt校验服务本体调用 | 无jwt校验网关服务调用 | jwt校验网关服务调用 | Redis+jwt校验网关服务调用
 :----: | :----: | :----: | :----: | :----: 
无io-100/10 | 2.092 | 7.028 | 10.697 | 12.935
无io-1000/100 | 22.386 | 74.848 | 131.983 | 142.052
无io-10000/100 | 24.611 | 81.419 | 142.292 | 155.2
io500-100/10 | 555.57 | 559.531 | 561.448 | 562.531
io500-1000/100 | 563.312 | 581.638 | 591.456 | 594.853
io500-10000/100 | 520.707 | 524.299 | 524.909 | 526.716
io1000-100/10 | 1105.726 | 1108.679 | 1110.895 | 1111.178
io1000-1000/100 | 1111.081 | 1128.493 | 1137.595 | 1141.056
io1000-10000/100 | 1017.167 | 1022.004 | 1023.771 | 1024.682
![](static/time_tred.png)

#### 无jwt校验服务本体调用
- ab -n 100 -c 10 http://localhost:8001/api
- ab -n 1000 -c 100 http://localhost:8001/api
- ab -n 10000 -c 100 http://localhost:8001/api
- ab -n 100 -c 10 http://localhost:8001/io500
- ab -n 1000 -c 100 http://localhost:8001/io500
- ab -n 10000 -c 100 http://localhost:8001/io500
- ab -n 100 -c 10 http://localhost:8001/io1000
- ab -n 1000 -c 100 http://localhost:8001/io1000
- ab -n 10000 -c 100 http://localhost:8001/io1000

#### 无jwt校验网关服务调用
- ab -n 100 -c 10 http://localhost:8201/api/api
- ab -n 1000 -c 100 http://localhost:8201/api/api
- ab -n 10000 -c 100 http://localhost:8201/api/api
- ab -n 100 -c 10 http://localhost:8201/api/io500
- ab -n 1000 -c 100 http://localhost:8201/api/io500
- ab -n 10000 -c 100 http://localhost:8201/api/io500
- ab -n 100 -c 10 http://localhost:8201/api/io1000
- ab -n 1000 -c 100 http://localhost:8201/api/io1000
- ab -n 10000 -c 100 http://localhost:8201/api/io1000

#### jwt校验网关服务调用
- ab -n 100 -c 10 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODkxOTQyNTksInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6Ijk1ZDcwMWQ1LWUwYjItNGJiNi04MWRmLTAzY2U4ODIxOTMxMSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.QRV-7d7hpysuVfqdwPMbsggJ9q3Qzy8s1ri_rl66jGfL2RVlRsDEpaksd-4QOesFbxv_uzW2--ONfV9h-COjKL99_attIypMlTo3JqWkUGSOQ_h16WpK75ii6ryImoUAEibG4CIdH8C0O2yWxAYkkAUugzaMPdeUbPh74lqK1aJ4KSwcu2zXknOrx188KrRWEiQamK0eZ1NAG6gFt2qEPy6TXKqsECyRSmrdAcsbp1MfEoPpGVpsEUhrmUfqXIertpUJBOclzsKD2gBN6e_vjLrMgrA8wMmk6eYhWa5pSs1HHtJiAVKFFi6bB22DOnCr8vTx2GgDSKCGSj0oGM9p3w' http://localhost:8201/api/api
- ab -n 1000 -c 100 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODkxOTQyNTksInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6Ijk1ZDcwMWQ1LWUwYjItNGJiNi04MWRmLTAzY2U4ODIxOTMxMSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.QRV-7d7hpysuVfqdwPMbsggJ9q3Qzy8s1ri_rl66jGfL2RVlRsDEpaksd-4QOesFbxv_uzW2--ONfV9h-COjKL99_attIypMlTo3JqWkUGSOQ_h16WpK75ii6ryImoUAEibG4CIdH8C0O2yWxAYkkAUugzaMPdeUbPh74lqK1aJ4KSwcu2zXknOrx188KrRWEiQamK0eZ1NAG6gFt2qEPy6TXKqsECyRSmrdAcsbp1MfEoPpGVpsEUhrmUfqXIertpUJBOclzsKD2gBN6e_vjLrMgrA8wMmk6eYhWa5pSs1HHtJiAVKFFi6bB22DOnCr8vTx2GgDSKCGSj0oGM9p3w' http://localhost:8201/api/api
- ab -n 10000 -c 100 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODkxOTQyNTksInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6Ijk1ZDcwMWQ1LWUwYjItNGJiNi04MWRmLTAzY2U4ODIxOTMxMSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.QRV-7d7hpysuVfqdwPMbsggJ9q3Qzy8s1ri_rl66jGfL2RVlRsDEpaksd-4QOesFbxv_uzW2--ONfV9h-COjKL99_attIypMlTo3JqWkUGSOQ_h16WpK75ii6ryImoUAEibG4CIdH8C0O2yWxAYkkAUugzaMPdeUbPh74lqK1aJ4KSwcu2zXknOrx188KrRWEiQamK0eZ1NAG6gFt2qEPy6TXKqsECyRSmrdAcsbp1MfEoPpGVpsEUhrmUfqXIertpUJBOclzsKD2gBN6e_vjLrMgrA8wMmk6eYhWa5pSs1HHtJiAVKFFi6bB22DOnCr8vTx2GgDSKCGSj0oGM9p3w' http://localhost:8201/api/api
- ab -n 100 -c 10 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODkxOTQyNTksInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6Ijk1ZDcwMWQ1LWUwYjItNGJiNi04MWRmLTAzY2U4ODIxOTMxMSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.QRV-7d7hpysuVfqdwPMbsggJ9q3Qzy8s1ri_rl66jGfL2RVlRsDEpaksd-4QOesFbxv_uzW2--ONfV9h-COjKL99_attIypMlTo3JqWkUGSOQ_h16WpK75ii6ryImoUAEibG4CIdH8C0O2yWxAYkkAUugzaMPdeUbPh74lqK1aJ4KSwcu2zXknOrx188KrRWEiQamK0eZ1NAG6gFt2qEPy6TXKqsECyRSmrdAcsbp1MfEoPpGVpsEUhrmUfqXIertpUJBOclzsKD2gBN6e_vjLrMgrA8wMmk6eYhWa5pSs1HHtJiAVKFFi6bB22DOnCr8vTx2GgDSKCGSj0oGM9p3w' http://localhost:8201/api/io500
- ab -n 1000 -c 100 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODkxOTQyNTksInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6Ijk1ZDcwMWQ1LWUwYjItNGJiNi04MWRmLTAzY2U4ODIxOTMxMSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.QRV-7d7hpysuVfqdwPMbsggJ9q3Qzy8s1ri_rl66jGfL2RVlRsDEpaksd-4QOesFbxv_uzW2--ONfV9h-COjKL99_attIypMlTo3JqWkUGSOQ_h16WpK75ii6ryImoUAEibG4CIdH8C0O2yWxAYkkAUugzaMPdeUbPh74lqK1aJ4KSwcu2zXknOrx188KrRWEiQamK0eZ1NAG6gFt2qEPy6TXKqsECyRSmrdAcsbp1MfEoPpGVpsEUhrmUfqXIertpUJBOclzsKD2gBN6e_vjLrMgrA8wMmk6eYhWa5pSs1HHtJiAVKFFi6bB22DOnCr8vTx2GgDSKCGSj0oGM9p3w' http://localhost:8201/api/io500
- ab -n 10000 -c 100 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODkxOTQyNTksInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6Ijk1ZDcwMWQ1LWUwYjItNGJiNi04MWRmLTAzY2U4ODIxOTMxMSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.QRV-7d7hpysuVfqdwPMbsggJ9q3Qzy8s1ri_rl66jGfL2RVlRsDEpaksd-4QOesFbxv_uzW2--ONfV9h-COjKL99_attIypMlTo3JqWkUGSOQ_h16WpK75ii6ryImoUAEibG4CIdH8C0O2yWxAYkkAUugzaMPdeUbPh74lqK1aJ4KSwcu2zXknOrx188KrRWEiQamK0eZ1NAG6gFt2qEPy6TXKqsECyRSmrdAcsbp1MfEoPpGVpsEUhrmUfqXIertpUJBOclzsKD2gBN6e_vjLrMgrA8wMmk6eYhWa5pSs1HHtJiAVKFFi6bB22DOnCr8vTx2GgDSKCGSj0oGM9p3w' http://localhost:8201/api/io500
- ab -n 100 -c 10 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODkxOTQyNTksInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6Ijk1ZDcwMWQ1LWUwYjItNGJiNi04MWRmLTAzY2U4ODIxOTMxMSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.QRV-7d7hpysuVfqdwPMbsggJ9q3Qzy8s1ri_rl66jGfL2RVlRsDEpaksd-4QOesFbxv_uzW2--ONfV9h-COjKL99_attIypMlTo3JqWkUGSOQ_h16WpK75ii6ryImoUAEibG4CIdH8C0O2yWxAYkkAUugzaMPdeUbPh74lqK1aJ4KSwcu2zXknOrx188KrRWEiQamK0eZ1NAG6gFt2qEPy6TXKqsECyRSmrdAcsbp1MfEoPpGVpsEUhrmUfqXIertpUJBOclzsKD2gBN6e_vjLrMgrA8wMmk6eYhWa5pSs1HHtJiAVKFFi6bB22DOnCr8vTx2GgDSKCGSj0oGM9p3w' http://localhost:8201/api/io1000
- ab -n 1000 -c 100 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODkxOTQyNTksInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6Ijk1ZDcwMWQ1LWUwYjItNGJiNi04MWRmLTAzY2U4ODIxOTMxMSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.QRV-7d7hpysuVfqdwPMbsggJ9q3Qzy8s1ri_rl66jGfL2RVlRsDEpaksd-4QOesFbxv_uzW2--ONfV9h-COjKL99_attIypMlTo3JqWkUGSOQ_h16WpK75ii6ryImoUAEibG4CIdH8C0O2yWxAYkkAUugzaMPdeUbPh74lqK1aJ4KSwcu2zXknOrx188KrRWEiQamK0eZ1NAG6gFt2qEPy6TXKqsECyRSmrdAcsbp1MfEoPpGVpsEUhrmUfqXIertpUJBOclzsKD2gBN6e_vjLrMgrA8wMmk6eYhWa5pSs1HHtJiAVKFFi6bB22DOnCr8vTx2GgDSKCGSj0oGM9p3w' http://localhost:8201/api/io1000
- ab -n 10000 -c 100 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODkxOTQyNTksInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6Ijk1ZDcwMWQ1LWUwYjItNGJiNi04MWRmLTAzY2U4ODIxOTMxMSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.QRV-7d7hpysuVfqdwPMbsggJ9q3Qzy8s1ri_rl66jGfL2RVlRsDEpaksd-4QOesFbxv_uzW2--ONfV9h-COjKL99_attIypMlTo3JqWkUGSOQ_h16WpK75ii6ryImoUAEibG4CIdH8C0O2yWxAYkkAUugzaMPdeUbPh74lqK1aJ4KSwcu2zXknOrx188KrRWEiQamK0eZ1NAG6gFt2qEPy6TXKqsECyRSmrdAcsbp1MfEoPpGVpsEUhrmUfqXIertpUJBOclzsKD2gBN6e_vjLrMgrA8wMmk6eYhWa5pSs1HHtJiAVKFFi6bB22DOnCr8vTx2GgDSKCGSj0oGM9p3w' http://localhost:8201/api/io1000

#### Redis+jwt校验网关服务调用
- ab -n 100 -c 10 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODkxOTQyNTksInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6Ijk1ZDcwMWQ1LWUwYjItNGJiNi04MWRmLTAzY2U4ODIxOTMxMSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.QRV-7d7hpysuVfqdwPMbsggJ9q3Qzy8s1ri_rl66jGfL2RVlRsDEpaksd-4QOesFbxv_uzW2--ONfV9h-COjKL99_attIypMlTo3JqWkUGSOQ_h16WpK75ii6ryImoUAEibG4CIdH8C0O2yWxAYkkAUugzaMPdeUbPh74lqK1aJ4KSwcu2zXknOrx188KrRWEiQamK0eZ1NAG6gFt2qEPy6TXKqsECyRSmrdAcsbp1MfEoPpGVpsEUhrmUfqXIertpUJBOclzsKD2gBN6e_vjLrMgrA8wMmk6eYhWa5pSs1HHtJiAVKFFi6bB22DOnCr8vTx2GgDSKCGSj0oGM9p3w' http://localhost:8201/api/api
- ab -n 1000 -c 100 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODkxOTQyNTksInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6Ijk1ZDcwMWQ1LWUwYjItNGJiNi04MWRmLTAzY2U4ODIxOTMxMSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.QRV-7d7hpysuVfqdwPMbsggJ9q3Qzy8s1ri_rl66jGfL2RVlRsDEpaksd-4QOesFbxv_uzW2--ONfV9h-COjKL99_attIypMlTo3JqWkUGSOQ_h16WpK75ii6ryImoUAEibG4CIdH8C0O2yWxAYkkAUugzaMPdeUbPh74lqK1aJ4KSwcu2zXknOrx188KrRWEiQamK0eZ1NAG6gFt2qEPy6TXKqsECyRSmrdAcsbp1MfEoPpGVpsEUhrmUfqXIertpUJBOclzsKD2gBN6e_vjLrMgrA8wMmk6eYhWa5pSs1HHtJiAVKFFi6bB22DOnCr8vTx2GgDSKCGSj0oGM9p3w' http://localhost:8201/api/api
- ab -n 10000 -c 100 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODkxOTQyNTksInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6Ijk1ZDcwMWQ1LWUwYjItNGJiNi04MWRmLTAzY2U4ODIxOTMxMSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.QRV-7d7hpysuVfqdwPMbsggJ9q3Qzy8s1ri_rl66jGfL2RVlRsDEpaksd-4QOesFbxv_uzW2--ONfV9h-COjKL99_attIypMlTo3JqWkUGSOQ_h16WpK75ii6ryImoUAEibG4CIdH8C0O2yWxAYkkAUugzaMPdeUbPh74lqK1aJ4KSwcu2zXknOrx188KrRWEiQamK0eZ1NAG6gFt2qEPy6TXKqsECyRSmrdAcsbp1MfEoPpGVpsEUhrmUfqXIertpUJBOclzsKD2gBN6e_vjLrMgrA8wMmk6eYhWa5pSs1HHtJiAVKFFi6bB22DOnCr8vTx2GgDSKCGSj0oGM9p3w' http://localhost:8201/api/api
- ab -n 100 -c 10 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODkxOTQyNTksInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6Ijk1ZDcwMWQ1LWUwYjItNGJiNi04MWRmLTAzY2U4ODIxOTMxMSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.QRV-7d7hpysuVfqdwPMbsggJ9q3Qzy8s1ri_rl66jGfL2RVlRsDEpaksd-4QOesFbxv_uzW2--ONfV9h-COjKL99_attIypMlTo3JqWkUGSOQ_h16WpK75ii6ryImoUAEibG4CIdH8C0O2yWxAYkkAUugzaMPdeUbPh74lqK1aJ4KSwcu2zXknOrx188KrRWEiQamK0eZ1NAG6gFt2qEPy6TXKqsECyRSmrdAcsbp1MfEoPpGVpsEUhrmUfqXIertpUJBOclzsKD2gBN6e_vjLrMgrA8wMmk6eYhWa5pSs1HHtJiAVKFFi6bB22DOnCr8vTx2GgDSKCGSj0oGM9p3w' http://localhost:8201/api/io500
- ab -n 1000 -c 100 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODkxOTQyNTksInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6Ijk1ZDcwMWQ1LWUwYjItNGJiNi04MWRmLTAzY2U4ODIxOTMxMSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.QRV-7d7hpysuVfqdwPMbsggJ9q3Qzy8s1ri_rl66jGfL2RVlRsDEpaksd-4QOesFbxv_uzW2--ONfV9h-COjKL99_attIypMlTo3JqWkUGSOQ_h16WpK75ii6ryImoUAEibG4CIdH8C0O2yWxAYkkAUugzaMPdeUbPh74lqK1aJ4KSwcu2zXknOrx188KrRWEiQamK0eZ1NAG6gFt2qEPy6TXKqsECyRSmrdAcsbp1MfEoPpGVpsEUhrmUfqXIertpUJBOclzsKD2gBN6e_vjLrMgrA8wMmk6eYhWa5pSs1HHtJiAVKFFi6bB22DOnCr8vTx2GgDSKCGSj0oGM9p3w' http://localhost:8201/api/io500
- ab -n 10000 -c 100 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODkxOTQyNTksInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6Ijk1ZDcwMWQ1LWUwYjItNGJiNi04MWRmLTAzY2U4ODIxOTMxMSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.QRV-7d7hpysuVfqdwPMbsggJ9q3Qzy8s1ri_rl66jGfL2RVlRsDEpaksd-4QOesFbxv_uzW2--ONfV9h-COjKL99_attIypMlTo3JqWkUGSOQ_h16WpK75ii6ryImoUAEibG4CIdH8C0O2yWxAYkkAUugzaMPdeUbPh74lqK1aJ4KSwcu2zXknOrx188KrRWEiQamK0eZ1NAG6gFt2qEPy6TXKqsECyRSmrdAcsbp1MfEoPpGVpsEUhrmUfqXIertpUJBOclzsKD2gBN6e_vjLrMgrA8wMmk6eYhWa5pSs1HHtJiAVKFFi6bB22DOnCr8vTx2GgDSKCGSj0oGM9p3w' http://localhost:8201/api/io500
- ab -n 100 -c 10 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODkxOTQyNTksInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6Ijk1ZDcwMWQ1LWUwYjItNGJiNi04MWRmLTAzY2U4ODIxOTMxMSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.QRV-7d7hpysuVfqdwPMbsggJ9q3Qzy8s1ri_rl66jGfL2RVlRsDEpaksd-4QOesFbxv_uzW2--ONfV9h-COjKL99_attIypMlTo3JqWkUGSOQ_h16WpK75ii6ryImoUAEibG4CIdH8C0O2yWxAYkkAUugzaMPdeUbPh74lqK1aJ4KSwcu2zXknOrx188KrRWEiQamK0eZ1NAG6gFt2qEPy6TXKqsECyRSmrdAcsbp1MfEoPpGVpsEUhrmUfqXIertpUJBOclzsKD2gBN6e_vjLrMgrA8wMmk6eYhWa5pSs1HHtJiAVKFFi6bB22DOnCr8vTx2GgDSKCGSj0oGM9p3w' http://localhost:8201/api/io1000
- ab -n 1000 -c 100 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODkxOTQyNTksInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6Ijk1ZDcwMWQ1LWUwYjItNGJiNi04MWRmLTAzY2U4ODIxOTMxMSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.QRV-7d7hpysuVfqdwPMbsggJ9q3Qzy8s1ri_rl66jGfL2RVlRsDEpaksd-4QOesFbxv_uzW2--ONfV9h-COjKL99_attIypMlTo3JqWkUGSOQ_h16WpK75ii6ryImoUAEibG4CIdH8C0O2yWxAYkkAUugzaMPdeUbPh74lqK1aJ4KSwcu2zXknOrx188KrRWEiQamK0eZ1NAG6gFt2qEPy6TXKqsECyRSmrdAcsbp1MfEoPpGVpsEUhrmUfqXIertpUJBOclzsKD2gBN6e_vjLrMgrA8wMmk6eYhWa5pSs1HHtJiAVKFFi6bB22DOnCr8vTx2GgDSKCGSj0oGM9p3w' http://localhost:8201/api/io1000
- ab -n 10000 -c 100 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODkxOTQyNTksInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6Ijk1ZDcwMWQ1LWUwYjItNGJiNi04MWRmLTAzY2U4ODIxOTMxMSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.QRV-7d7hpysuVfqdwPMbsggJ9q3Qzy8s1ri_rl66jGfL2RVlRsDEpaksd-4QOesFbxv_uzW2--ONfV9h-COjKL99_attIypMlTo3JqWkUGSOQ_h16WpK75ii6ryImoUAEibG4CIdH8C0O2yWxAYkkAUugzaMPdeUbPh74lqK1aJ4KSwcu2zXknOrx188KrRWEiQamK0eZ1NAG6gFt2qEPy6TXKqsECyRSmrdAcsbp1MfEoPpGVpsEUhrmUfqXIertpUJBOclzsKD2gBN6e_vjLrMgrA8wMmk6eYhWa5pSs1HHtJiAVKFFi6bB22DOnCr8vTx2GgDSKCGSj0oGM9p3w' http://localhost:8201/api/io1000
