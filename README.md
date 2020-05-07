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
1. 无jwt校验服务本身调用
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

#### 无jwt校验服务本身调用
> ab -n 100 -c 10 http://localhost:8001/api
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8001

Document Path:          /api
Document Length:        58 bytes

Concurrency Level:      10
Time taken for tests:   0.090 seconds
Complete requests:      100
Failed requests:        0
Total transferred:      16300 bytes
HTML transferred:       5800 bytes
Requests per second:    1111.49 [#/sec] (mean)
Time per request:       8.997 [ms] (mean)
Time per request:       0.900 [ms] (mean, across all concurrent requests)
Transfer rate:          176.93 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1   1.3      1       9
Processing:     2    7   3.2      7      16
Waiting:        1    6   3.0      5      16
Total:          2    8   3.5      8      17

Percentage of the requests served within a certain time (ms)
  50%      8
  66%      9
  75%     10
  80%     11
  90%     13
  95%     16
  98%     17
  99%     17
 100%     17 (longest request)
```

> ab -n 1000 -c 100 http://localhost:8001/api
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8001

Document Path:          /api
Document Length:        58 bytes

Concurrency Level:      100
Time taken for tests:   0.251 seconds
Complete requests:      1000
Failed requests:        0
Total transferred:      163000 bytes
HTML transferred:       58000 bytes
Requests per second:    3989.47 [#/sec] (mean)
Time per request:       25.066 [ms] (mean)
Time per request:       0.251 [ms] (mean, across all concurrent requests)
Transfer rate:          635.04 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    6   3.4      5      17
Processing:     2   18   7.0     19      38
Waiting:        1   14   5.5     14      30
Total:          6   24   7.0     25      41

Percentage of the requests served within a certain time (ms)
  50%     25
  66%     28
  75%     29
  80%     30
  90%     33
  95%     34
  98%     36
  99%     38
 100%     41 (longest request)
```

> ab -n 10000 -c 100 http://localhost:8001/api
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8001

Document Path:          /api
Document Length:        58 bytes

Concurrency Level:      100
Time taken for tests:   3.501 seconds
Complete requests:      10000
Failed requests:        0
Total transferred:      1630000 bytes
HTML transferred:       580000 bytes
Requests per second:    2856.29 [#/sec] (mean)
Time per request:       35.011 [ms] (mean)
Time per request:       0.350 [ms] (mean, across all concurrent requests)
Transfer rate:          454.66 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0   13  85.2      7    1567
Processing:     2   22  82.9     16    1199
Waiting:        1   14  57.5     11    1183
Total:          5   35 118.5     23    1581

Percentage of the requests served within a certain time (ms)
  50%     23
  66%     25
  75%     26
  80%     27
  90%     30
  95%     33
  98%     37
  99%     45
 100%   1581 (longest request)
```

> ab -n 100 -c 10 http://localhost:8001/io500
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8001

Document Path:          /io500
Document Length:        57 bytes

Concurrency Level:      10
Time taken for tests:   5.555 seconds
Complete requests:      100
Failed requests:        0
Total transferred:      16200 bytes
HTML transferred:       5700 bytes
Requests per second:    18.00 [#/sec] (mean)
Time per request:       555.539 [ms] (mean)
Time per request:       55.554 [ms] (mean, across all concurrent requests)
Transfer rate:          2.85 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.3      0       2
Processing:   502  505   1.1    505     507
Waiting:      501  504   1.1    504     506
Total:        502  505   1.2    505     507

Percentage of the requests served within a certain time (ms)
  50%    505
  66%    505
  75%    506
  80%    506
  90%    506
  95%    507
  98%    507
  99%    507
 100%    507 (longest request)
```

> ab -n 1000 -c 100 http://localhost:8001/io500
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8001

Document Path:          /io500
Document Length:        57 bytes

Concurrency Level:      100
Time taken for tests:   5.634 seconds
Complete requests:      1000
Failed requests:        0
Total transferred:      162000 bytes
HTML transferred:       57000 bytes
Requests per second:    177.49 [#/sec] (mean)
Time per request:       563.401 [ms] (mean)
Time per request:       5.634 [ms] (mean, across all concurrent requests)
Transfer rate:          28.08 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1   1.1      1       6
Processing:   502  509   3.4    510     519
Waiting:      502  508   2.6    508     514
Total:        503  511   3.9    511     522

Percentage of the requests served within a certain time (ms)
  50%    511
  66%    512
  75%    513
  80%    514
  90%    516
  95%    517
  98%    519
  99%    519
 100%    522 (longest request)
```

> ab -n 10000 -c 100 http://localhost:8001/io500
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8001

Document Path:          /io500
Document Length:        57 bytes

Concurrency Level:      100
Time taken for tests:   51.904 seconds
Complete requests:      10000
Failed requests:        0
Total transferred:      1620000 bytes
HTML transferred:       570000 bytes
Requests per second:    192.66 [#/sec] (mean)
Time per request:       519.038 [ms] (mean)
Time per request:       5.190 [ms] (mean, across all concurrent requests)
Transfer rate:          30.48 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1   1.3      1      12
Processing:   501  513  12.2    507     552
Waiting:      501  512  12.0    507     552
Total:        501  514  12.9    508     555

Percentage of the requests served within a certain time (ms)
  50%    508
  66%    512
  75%    517
  80%    518
  90%    538
  95%    545
  98%    549
  99%    551
 100%    555 (longest request)
```

> ab -n 100 -c 10 http://localhost:8001/io1000
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8001

Document Path:          /io1000
Document Length:        57 bytes

Concurrency Level:      10
Time taken for tests:   11.070 seconds
Complete requests:      100
Failed requests:        0
Total transferred:      16200 bytes
HTML transferred:       5700 bytes
Requests per second:    9.03 [#/sec] (mean)
Time per request:       1106.952 [ms] (mean)
Time per request:       110.695 [ms] (mean, across all concurrent requests)
Transfer rate:          1.43 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.2      0       2
Processing:  1001 1005   1.8   1006    1009
Waiting:     1001 1005   1.6   1005    1009
Total:       1002 1006   1.8   1006    1009

Percentage of the requests served within a certain time (ms)
  50%   1006
  66%   1006
  75%   1007
  80%   1007
  90%   1008
  95%   1009
  98%   1009
  99%   1009
 100%   1009 (longest request)
```

> ab -n 1000 -c 100 http://localhost:8001/io1000
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8001

Document Path:          /io1000
Document Length:        57 bytes

Concurrency Level:      100
Time taken for tests:   11.114 seconds
Complete requests:      1000
Failed requests:        0
Total transferred:      162000 bytes
HTML transferred:       57000 bytes
Requests per second:    89.97 [#/sec] (mean)
Time per request:       1111.439 [ms] (mean)
Time per request:       11.114 [ms] (mean, across all concurrent requests)
Transfer rate:          14.23 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1   1.1      1       5
Processing:  1001 1008   3.4   1007    1020
Waiting:     1001 1007   3.0   1007    1017
Total:       1002 1009   3.9   1008    1023

Percentage of the requests served within a certain time (ms)
  50%   1008
  66%   1009
  75%   1011
  80%   1012
  90%   1016
  95%   1016
  98%   1020
  99%   1022
 100%   1023 (longest request)
```

> ab -n 10000 -c 100 http://localhost:8001/io1000
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8001

Document Path:          /io1000
Document Length:        57 bytes

Concurrency Level:      100
Time taken for tests:   102.314 seconds
Complete requests:      10000
Failed requests:        0
Total transferred:      1620000 bytes
HTML transferred:       570000 bytes
Requests per second:    97.74 [#/sec] (mean)
Time per request:       1023.141 [ms] (mean)
Time per request:       10.231 [ms] (mean, across all concurrent requests)
Transfer rate:          15.46 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    2   1.4      1      11
Processing:  1001 1011   5.3   1012    1051
Waiting:     1001 1010   4.9   1010    1050
Total:       1001 1013   6.1   1014    1057

Percentage of the requests served within a certain time (ms)
  50%   1014
  66%   1016
  75%   1017
  80%   1018
  90%   1019
  95%   1021
  98%   1024
  99%   1025
 100%   1057 (longest request)
```

#### 无jwt校验网关服务调用
> ab -n 100 -c 10 http://localhost:8201/api/api
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8201

Document Path:          /api/api
Document Length:        69 bytes

Concurrency Level:      10
Time taken for tests:   0.082 seconds
Complete requests:      100
Failed requests:        0
Total transferred:      50600 bytes
HTML transferred:       6900 bytes
Requests per second:    1222.54 [#/sec] (mean)
Time per request:       8.180 [ms] (mean)
Time per request:       0.818 [ms] (mean, across all concurrent requests)
Transfer rate:          604.11 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1   0.4      1       3
Processing:     2    7   2.1      7      12
Waiting:        2    7   2.0      6      12
Total:          3    8   2.0      7      13

Percentage of the requests served within a certain time (ms)
  50%      7
  66%      8
  75%      9
  80%      9
  90%     11
  95%     11
  98%     12
  99%     13
 100%     13 (longest request)
```

> ab -n 1000 -c 100 http://localhost:8201/api/api
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8201

Document Path:          /api/api
Document Length:        69 bytes

Concurrency Level:      100
Time taken for tests:   1.176 seconds
Complete requests:      1000
Failed requests:        0
Total transferred:      506000 bytes
HTML transferred:       69000 bytes
Requests per second:    850.62 [#/sec] (mean)
Time per request:       117.562 [ms] (mean)
Time per request:       1.176 [ms] (mean, across all concurrent requests)
Transfer rate:          420.32 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    2   3.8      0      34
Processing:    10  108  45.6    105     201
Waiting:        2  105  47.6    104     200
Total:         10  110  43.9    105     202

Percentage of the requests served within a certain time (ms)
  50%    105
  66%    121
  75%    146
  80%    154
  90%    177
  95%    186
  98%    190
  99%    193
 100%    202 (longest request)
```

> ab -n 10000 -c 100 http://localhost:8201/api/api
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8201

Document Path:          /api/api
Document Length:        69 bytes

Concurrency Level:      100
Time taken for tests:   14.517 seconds
Complete requests:      10000
Failed requests:        94
   (Connect: 0, Receive: 0, Length: 94, Exceptions: 0)
Total transferred:      5060472 bytes
HTML transferred:       690472 bytes
Requests per second:    688.86 [#/sec] (mean)
Time per request:       145.167 [ms] (mean)
Time per request:       1.452 [ms] (mean, across all concurrent requests)
Transfer rate:          340.43 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1  24.7      0    1910
Processing:     5  143 324.2     97    2855
Waiting:        2  140 318.8     96    2837
Total:          7  145 324.8     97    2855

Percentage of the requests served within a certain time (ms)
  50%     97
  66%    109
  75%    116
  80%    122
  90%    141
  95%    161
  98%   1957
  99%   2679
 100%   2855 (longest request)
```

> ab -n 100 -c 10 http://localhost:8201/api/io500
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8201

Document Path:          /api/io500
Document Length:        68 bytes

Concurrency Level:      10
Time taken for tests:   5.598 seconds
Complete requests:      100
Failed requests:        2
   (Connect: 0, Receive: 0, Length: 2, Exceptions: 0)
Total transferred:      50510 bytes
HTML transferred:       6810 bytes
Requests per second:    17.86 [#/sec] (mean)
Time per request:       559.776 [ms] (mean)
Time per request:       55.978 [ms] (mean, across all concurrent requests)
Transfer rate:          8.81 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.1      0       1
Processing:   504  508   2.6    508     514
Waiting:      504  508   2.5    508     514
Total:        504  509   2.6    508     515

Percentage of the requests served within a certain time (ms)
  50%    508
  66%    509
  75%    510
  80%    510
  90%    514
  95%    515
  98%    515
  99%    515
 100%    515 (longest request)
```

> ab -n 1000 -c 100 http://localhost:8201/api/io500
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8201

Document Path:          /api/io500
Document Length:        68 bytes

Concurrency Level:      100
Time taken for tests:   5.940 seconds
Complete requests:      1000
Failed requests:        1
   (Connect: 0, Receive: 0, Length: 1, Exceptions: 0)
Total transferred:      505005 bytes
HTML transferred:       68005 bytes
Requests per second:    168.36 [#/sec] (mean)
Time per request:       593.979 [ms] (mean)
Time per request:       5.940 [ms] (mean, across all concurrent requests)
Transfer rate:          83.03 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    2   2.0      1       9
Processing:   503  528  18.2    532     566
Waiting:      502  526  17.2    529     565
Total:        503  530  19.3    536     568

Percentage of the requests served within a certain time (ms)
  50%    536
  66%    541
  75%    543
  80%    544
  90%    556
  95%    560
  98%    563
  99%    567
 100%    568 (longest request)
```

> ab -n 10000 -c 100 http://localhost:8201/api/io500
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8201

Document Path:          /api/io500
Document Length:        68 bytes

Concurrency Level:      100
Time taken for tests:   53.264 seconds
Complete requests:      10000
Failed requests:        85
   (Connect: 0, Receive: 0, Length: 85, Exceptions: 0)
Total transferred:      5050429 bytes
HTML transferred:       680429 bytes
Requests per second:    187.74 [#/sec] (mean)
Time per request:       532.644 [ms] (mean)
Time per request:       5.326 [ms] (mean, across all concurrent requests)
Transfer rate:          92.60 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1   2.2      0      33
Processing:   502  525  25.2    514     723
Waiting:      502  523  23.4    513     694
Total:        503  526  26.0    515     723

Percentage of the requests served within a certain time (ms)
  50%    515
  66%    529
  75%    539
  80%    544
  90%    555
  95%    566
  98%    618
  99%    645
 100%    723 (longest request)
```

> ab -n 100 -c 10 http://localhost:8201/api/io1000
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8201

Document Path:          /api/io1000
Document Length:        68 bytes

Concurrency Level:      10
Time taken for tests:   11.103 seconds
Complete requests:      100
Failed requests:        0
Total transferred:      50500 bytes
HTML transferred:       6800 bytes
Requests per second:    9.01 [#/sec] (mean)
Time per request:       1110.345 [ms] (mean)
Time per request:       111.034 [ms] (mean, across all concurrent requests)
Transfer rate:          4.44 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.2      0       1
Processing:  1007 1009   1.3   1009    1012
Waiting:     1007 1009   1.3   1009    1012
Total:       1007 1010   1.3   1010    1012

Percentage of the requests served within a certain time (ms)
  50%   1010
  66%   1010
  75%   1011
  80%   1011
  90%   1011
  95%   1012
  98%   1012
  99%   1012
 100%   1012 (longest request)
```

> ab -n 1000 -c 100 http://localhost:8201/api/io1000
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8201

Document Path:          /api/io1000
Document Length:        68 bytes

Concurrency Level:      100
Time taken for tests:   11.334 seconds
Complete requests:      1000
Failed requests:        0
Total transferred:      505000 bytes
HTML transferred:       68000 bytes
Requests per second:    88.23 [#/sec] (mean)
Time per request:       1133.367 [ms] (mean)
Time per request:       11.334 [ms] (mean, across all concurrent requests)
Transfer rate:          43.51 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1   1.2      0       6
Processing:  1003 1019  13.9   1012    1062
Waiting:     1003 1018  13.1   1012    1062
Total:       1003 1020  14.7   1013    1065

Percentage of the requests served within a certain time (ms)
  50%   1013
  66%   1027
  75%   1033
  80%   1035
  90%   1038
  95%   1048
  98%   1059
  99%   1060
 100%   1065 (longest request)
```

> ab -n 10000 -c 100 http://localhost:8201/api/io1000
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8201

Document Path:          /api/io1000
Document Length:        68 bytes

Concurrency Level:      100
Time taken for tests:   102.382 seconds
Complete requests:      10000
Failed requests:        83
   (Connect: 0, Receive: 0, Length: 83, Exceptions: 0)
Total transferred:      5050419 bytes
HTML transferred:       680419 bytes
Requests per second:    97.67 [#/sec] (mean)
Time per request:       1023.818 [ms] (mean)
Time per request:       10.238 [ms] (mean, across all concurrent requests)
Transfer rate:          48.17 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.6      0       7
Processing:  1002 1011   7.0   1008    1052
Waiting:     1002 1010   6.5   1008    1047
Total:       1002 1011   7.4   1009    1055

Percentage of the requests served within a certain time (ms)
  50%   1009
  66%   1010
  75%   1013
  80%   1014
  90%   1018
  95%   1023
  98%   1041
  99%   1046
 100%   1055 (longest request)
```

#### jwt校验网关服务调用
> ab -n 100 -c 10 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODg4NDM5MzMsInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6ImRmYTBmNWQ5LTIyYTQtNGEzYi05OTlmLTk2MWM3YjNmNWQ0MSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.AV7uCVNSpHcPzD5nnOu834TD6qeeFMH3hXB6AQiwSVLKQB7Tp72gAvrWoBGEHf88PfMp3za3d0VLlqWHB0E_z5z5lWpreVQJyKF2JNTtGwM3iQ_MIMZYK5OlAZA8yLqfb52_0j0jlQ-RUrseMbgmQzYkwXthcCdROYiLQkboCZ3OCjwhP_rvCkCVEW-s94yV_GOL8bnz-d4POVfgH9y1mCDWh6R7S1woBkpAjW4nlq1TH9_b0sae3wSowx6mhNvpHllUpdQThhgVEuMGNhQUrsCQEpq0P0wsYQGUzMtcWbGuoqVo4rqq4aPsHbOv9aQejlS8cE2UbhtU9UkyuyG2hw' http://localhost:8201/api/api
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8201

Document Path:          /api/api
Document Length:        68 bytes

Concurrency Level:      10
Time taken for tests:   0.139 seconds
Complete requests:      100
Failed requests:        0
Total transferred:      38700 bytes
HTML transferred:       6800 bytes
Requests per second:    718.97 [#/sec] (mean)
Time per request:       13.909 [ms] (mean)
Time per request:       1.391 [ms] (mean, across all concurrent requests)
Transfer rate:          271.72 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.9      0       7
Processing:     4   13   5.8     11      39
Waiting:        3   12   5.6     11      36
Total:          4   13   6.2     11      39

Percentage of the requests served within a certain time (ms)
  50%     11
  66%     13
  75%     15
  80%     16
  90%     23
  95%     28
  98%     33
  99%     39
 100%     39 (longest request)
```

> ab -n 1000 -c 100 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODg4NDM5MzMsInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6ImRmYTBmNWQ5LTIyYTQtNGEzYi05OTlmLTk2MWM3YjNmNWQ0MSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.AV7uCVNSpHcPzD5nnOu834TD6qeeFMH3hXB6AQiwSVLKQB7Tp72gAvrWoBGEHf88PfMp3za3d0VLlqWHB0E_z5z5lWpreVQJyKF2JNTtGwM3iQ_MIMZYK5OlAZA8yLqfb52_0j0jlQ-RUrseMbgmQzYkwXthcCdROYiLQkboCZ3OCjwhP_rvCkCVEW-s94yV_GOL8bnz-d4POVfgH9y1mCDWh6R7S1woBkpAjW4nlq1TH9_b0sae3wSowx6mhNvpHllUpdQThhgVEuMGNhQUrsCQEpq0P0wsYQGUzMtcWbGuoqVo4rqq4aPsHbOv9aQejlS8cE2UbhtU9UkyuyG2hw' http://localhost:8201/api/api
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8201

Document Path:          /api/api
Document Length:        68 bytes

Concurrency Level:      100
Time taken for tests:   0.939 seconds
Complete requests:      1000
Failed requests:        0
Total transferred:      387000 bytes
HTML transferred:       68000 bytes
Requests per second:    1064.94 [#/sec] (mean)
Time per request:       93.902 [ms] (mean)
Time per request:       0.939 [ms] (mean, across all concurrent requests)
Transfer rate:          402.47 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1   1.4      0      17
Processing:     8   88  23.4     97     126
Waiting:        2   87  24.5     97     125
Total:          8   89  22.6     98     126

Percentage of the requests served within a certain time (ms)
  50%     98
  66%    102
  75%    106
  80%    108
  90%    113
  95%    116
  98%    120
  99%    122
 100%    126 (longest request)
```

> ab -n 10000 -c 100 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODg4NDM5MzMsInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6ImRmYTBmNWQ5LTIyYTQtNGEzYi05OTlmLTk2MWM3YjNmNWQ0MSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.AV7uCVNSpHcPzD5nnOu834TD6qeeFMH3hXB6AQiwSVLKQB7Tp72gAvrWoBGEHf88PfMp3za3d0VLlqWHB0E_z5z5lWpreVQJyKF2JNTtGwM3iQ_MIMZYK5OlAZA8yLqfb52_0j0jlQ-RUrseMbgmQzYkwXthcCdROYiLQkboCZ3OCjwhP_rvCkCVEW-s94yV_GOL8bnz-d4POVfgH9y1mCDWh6R7S1woBkpAjW4nlq1TH9_b0sae3wSowx6mhNvpHllUpdQThhgVEuMGNhQUrsCQEpq0P0wsYQGUzMtcWbGuoqVo4rqq4aPsHbOv9aQejlS8cE2UbhtU9UkyuyG2hw' http://localhost:8201/api/api
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8201

Document Path:          /api/api
Document Length:        68 bytes

Concurrency Level:      100
Time taken for tests:   10.583 seconds
Complete requests:      10000
Failed requests:        0
Total transferred:      3870000 bytes
HTML transferred:       680000 bytes
Requests per second:    944.89 [#/sec] (mean)
Time per request:       105.832 [ms] (mean)
Time per request:       1.058 [ms] (mean, across all concurrent requests)
Transfer rate:          357.10 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1   8.8      0     657
Processing:     7  105 138.5     76    1078
Waiting:        2  104 138.4     76    1078
Total:          7  105 138.5     76    1082

Percentage of the requests served within a certain time (ms)
  50%     76
  66%     81
  75%     85
  80%     88
  90%     98
  95%    347
  98%    658
  99%   1029
 100%   1082 (longest request)
```

> ab -n 100 -c 10 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODg4NDM5MzMsInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6ImRmYTBmNWQ5LTIyYTQtNGEzYi05OTlmLTk2MWM3YjNmNWQ0MSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.AV7uCVNSpHcPzD5nnOu834TD6qeeFMH3hXB6AQiwSVLKQB7Tp72gAvrWoBGEHf88PfMp3za3d0VLlqWHB0E_z5z5lWpreVQJyKF2JNTtGwM3iQ_MIMZYK5OlAZA8yLqfb52_0j0jlQ-RUrseMbgmQzYkwXthcCdROYiLQkboCZ3OCjwhP_rvCkCVEW-s94yV_GOL8bnz-d4POVfgH9y1mCDWh6R7S1woBkpAjW4nlq1TH9_b0sae3wSowx6mhNvpHllUpdQThhgVEuMGNhQUrsCQEpq0P0wsYQGUzMtcWbGuoqVo4rqq4aPsHbOv9aQejlS8cE2UbhtU9UkyuyG2hw' http://localhost:8201/api/io500
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8201

Document Path:          /api/io500
Document Length:        68 bytes

Concurrency Level:      10
Time taken for tests:   0.069 seconds
Complete requests:      100
Failed requests:        0
Total transferred:      38700 bytes
HTML transferred:       6800 bytes
Requests per second:    1441.79 [#/sec] (mean)
Time per request:       6.936 [ms] (mean)
Time per request:       0.694 [ms] (mean, across all concurrent requests)
Transfer rate:          544.90 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1   0.7      0       4
Processing:     2    6   2.1      6      13
Waiting:        2    6   2.0      6      11
Total:          2    7   2.3      6      13
WARNING: The median and mean for the initial connection time are not within a normal deviation
        These results are probably not that reliable.

Percentage of the requests served within a certain time (ms)
  50%      6
  66%      7
  75%      7
  80%      8
  90%     10
  95%     13
  98%     13
  99%     13
 100%     13 (longest request)
```

> ab -n 1000 -c 100 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODg4NDM5MzMsInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6ImRmYTBmNWQ5LTIyYTQtNGEzYi05OTlmLTk2MWM3YjNmNWQ0MSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.AV7uCVNSpHcPzD5nnOu834TD6qeeFMH3hXB6AQiwSVLKQB7Tp72gAvrWoBGEHf88PfMp3za3d0VLlqWHB0E_z5z5lWpreVQJyKF2JNTtGwM3iQ_MIMZYK5OlAZA8yLqfb52_0j0jlQ-RUrseMbgmQzYkwXthcCdROYiLQkboCZ3OCjwhP_rvCkCVEW-s94yV_GOL8bnz-d4POVfgH9y1mCDWh6R7S1woBkpAjW4nlq1TH9_b0sae3wSowx6mhNvpHllUpdQThhgVEuMGNhQUrsCQEpq0P0wsYQGUzMtcWbGuoqVo4rqq4aPsHbOv9aQejlS8cE2UbhtU9UkyuyG2hw' http://localhost:8201/api/io500
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8201

Document Path:          /api/io500
Document Length:        68 bytes

Concurrency Level:      100
Time taken for tests:   0.796 seconds
Complete requests:      1000
Failed requests:        0
Total transferred:      387000 bytes
HTML transferred:       68000 bytes
Requests per second:    1256.26 [#/sec] (mean)
Time per request:       79.601 [ms] (mean)
Time per request:       0.796 [ms] (mean, across all concurrent requests)
Transfer rate:          474.78 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1   1.2      0       5
Processing:     9   75  15.7     80      99
Waiting:        2   74  16.1     80      98
Total:          9   75  15.0     80      99

Percentage of the requests served within a certain time (ms)
  50%     80
  66%     84
  75%     87
  80%     87
  90%     90
  95%     91
  98%     93
  99%     94
 100%     99 (longest request)
```

> ab -n 10000 -c 100 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODg4NDM5MzMsInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6ImRmYTBmNWQ5LTIyYTQtNGEzYi05OTlmLTk2MWM3YjNmNWQ0MSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.AV7uCVNSpHcPzD5nnOu834TD6qeeFMH3hXB6AQiwSVLKQB7Tp72gAvrWoBGEHf88PfMp3za3d0VLlqWHB0E_z5z5lWpreVQJyKF2JNTtGwM3iQ_MIMZYK5OlAZA8yLqfb52_0j0jlQ-RUrseMbgmQzYkwXthcCdROYiLQkboCZ3OCjwhP_rvCkCVEW-s94yV_GOL8bnz-d4POVfgH9y1mCDWh6R7S1woBkpAjW4nlq1TH9_b0sae3wSowx6mhNvpHllUpdQThhgVEuMGNhQUrsCQEpq0P0wsYQGUzMtcWbGuoqVo4rqq4aPsHbOv9aQejlS8cE2UbhtU9UkyuyG2hw' http://localhost:8201/api/io500
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8201

Document Path:          /api/io500
Document Length:        68 bytes

Concurrency Level:      100
Time taken for tests:   15.775 seconds
Complete requests:      10000
Failed requests:        0
Total transferred:      3870000 bytes
HTML transferred:       680000 bytes
Requests per second:    633.93 [#/sec] (mean)
Time per request:       157.747 [ms] (mean)
Time per request:       1.577 [ms] (mean, across all concurrent requests)
Transfer rate:          239.58 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0   67 651.3      0    6626
Processing:     2   88 132.2     75    1445
Waiting:        2   88 131.5     74    1442
Total:          2  155 659.6     75    6642

Percentage of the requests served within a certain time (ms)
  50%     75
  66%     78
  75%     81
  80%     83
  90%     92
  95%    105
  98%   1361
  99%   5591
 100%   6642 (longest request)
```

> ab -n 100 -c 10 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODg4NDM5MzMsInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6ImRmYTBmNWQ5LTIyYTQtNGEzYi05OTlmLTk2MWM3YjNmNWQ0MSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.AV7uCVNSpHcPzD5nnOu834TD6qeeFMH3hXB6AQiwSVLKQB7Tp72gAvrWoBGEHf88PfMp3za3d0VLlqWHB0E_z5z5lWpreVQJyKF2JNTtGwM3iQ_MIMZYK5OlAZA8yLqfb52_0j0jlQ-RUrseMbgmQzYkwXthcCdROYiLQkboCZ3OCjwhP_rvCkCVEW-s94yV_GOL8bnz-d4POVfgH9y1mCDWh6R7S1woBkpAjW4nlq1TH9_b0sae3wSowx6mhNvpHllUpdQThhgVEuMGNhQUrsCQEpq0P0wsYQGUzMtcWbGuoqVo4rqq4aPsHbOv9aQejlS8cE2UbhtU9UkyuyG2hw' http://localhost:8201/api/io1000
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8201

Document Path:          /api/io1000
Document Length:        68 bytes

Concurrency Level:      10
Time taken for tests:   0.068 seconds
Complete requests:      100
Failed requests:        0
Total transferred:      38700 bytes
HTML transferred:       6800 bytes
Requests per second:    1468.80 [#/sec] (mean)
Time per request:       6.808 [ms] (mean)
Time per request:       0.681 [ms] (mean, across all concurrent requests)
Transfer rate:          555.10 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1   0.6      0       2
Processing:     3    6   1.9      5      11
Waiting:        2    5   1.9      5      10
Total:          3    6   1.9      6      12
WARNING: The median and mean for the initial connection time are not within a normal deviation
        These results are probably not that reliable.

Percentage of the requests served within a certain time (ms)
  50%      6
  66%      7
  75%      8
  80%      8
  90%      9
  95%     10
  98%     11
  99%     12
 100%     12 (longest request)
```

> ab -n 1000 -c 100 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODg4NDM5MzMsInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6ImRmYTBmNWQ5LTIyYTQtNGEzYi05OTlmLTk2MWM3YjNmNWQ0MSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.AV7uCVNSpHcPzD5nnOu834TD6qeeFMH3hXB6AQiwSVLKQB7Tp72gAvrWoBGEHf88PfMp3za3d0VLlqWHB0E_z5z5lWpreVQJyKF2JNTtGwM3iQ_MIMZYK5OlAZA8yLqfb52_0j0jlQ-RUrseMbgmQzYkwXthcCdROYiLQkboCZ3OCjwhP_rvCkCVEW-s94yV_GOL8bnz-d4POVfgH9y1mCDWh6R7S1woBkpAjW4nlq1TH9_b0sae3wSowx6mhNvpHllUpdQThhgVEuMGNhQUrsCQEpq0P0wsYQGUzMtcWbGuoqVo4rqq4aPsHbOv9aQejlS8cE2UbhtU9UkyuyG2hw' http://localhost:8201/api/io1000
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8201

Document Path:          /api/io1000
Document Length:        68 bytes

Concurrency Level:      100
Time taken for tests:   0.876 seconds
Complete requests:      1000
Failed requests:        0
Total transferred:      387000 bytes
HTML transferred:       68000 bytes
Requests per second:    1141.00 [#/sec] (mean)
Time per request:       87.643 [ms] (mean)
Time per request:       0.876 [ms] (mean, across all concurrent requests)
Transfer rate:          431.22 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    1   1.1      0      12
Processing:     7   83  13.8     85     106
Waiting:        2   82  14.9     84     106
Total:          8   84  13.3     85     106

Percentage of the requests served within a certain time (ms)
  50%     85
  66%     90
  75%     93
  80%     95
  90%     99
  95%    101
  98%    103
  99%    104
 100%    106 (longest request)
```

> ab -n 10000 -c 100 -H 'Authorization: eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODg4NDM5MzMsInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfREJBIl0sImp0aSI6ImRmYTBmNWQ5LTIyYTQtNGEzYi05OTlmLTk2MWM3YjNmNWQ0MSIsImNsaWVudF9pZCI6Im5ldDVpankiLCJzY29wZSI6WyJhbGwiLCJyZWFkIiwid3JpdGUiXX0.AV7uCVNSpHcPzD5nnOu834TD6qeeFMH3hXB6AQiwSVLKQB7Tp72gAvrWoBGEHf88PfMp3za3d0VLlqWHB0E_z5z5lWpreVQJyKF2JNTtGwM3iQ_MIMZYK5OlAZA8yLqfb52_0j0jlQ-RUrseMbgmQzYkwXthcCdROYiLQkboCZ3OCjwhP_rvCkCVEW-s94yV_GOL8bnz-d4POVfgH9y1mCDWh6R7S1woBkpAjW4nlq1TH9_b0sae3wSowx6mhNvpHllUpdQThhgVEuMGNhQUrsCQEpq0P0wsYQGUzMtcWbGuoqVo4rqq4aPsHbOv9aQejlS8cE2UbhtU9UkyuyG2hw' http://localhost:8201/api/io1000
```shell script
Server Software:
Server Hostname:        localhost
Server Port:            8201

Document Path:          /api/io1000
Document Length:        68 bytes

Concurrency Level:      100
Time taken for tests:   9.778 seconds
Complete requests:      10000
Failed requests:        0
Total transferred:      3870000 bytes
HTML transferred:       680000 bytes
Requests per second:    1022.70 [#/sec] (mean)
Time per request:       97.780 [ms] (mean)
Time per request:       0.978 [ms] (mean, across all concurrent requests)
Transfer rate:          386.51 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.5      0      29
Processing:    10   97  26.0     91     226
Waiting:        3   97  26.0     91     226
Total:         10   97  26.0     91     226

Percentage of the requests served within a certain time (ms)
  50%     91
  66%    106
  75%    114
  80%    119
  90%    134
  95%    144
  98%    157
  99%    172
 100%    226 (longest request)
```

#### Redis+jwt校验网关服务调用

