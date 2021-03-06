# SuperBuy限时抢购秒杀系统
__________________________

## 一、项目简介
该系统主要实现了基本的登录、查看商品列表、秒杀下单等功能，项目中还针对高并发情况做了相关处理。

## 二、团队分工
- zhumengstar：队长，负责项目结构的搭建，商品列表页、详情页的后台开发，以及安全方面优化点的实现，添加spring security。
- hxllhhy：队员，负责登录、秒杀功能的后台开发，以及其他优化点的实现，使用JMeter对项目性能进行压测。

## 三、开发技术
SpringBoot + MyBatis + MySQL + Redis + Druid + RabbitMQ

## 四、压测工具
JMeter

## 五、实现的技术点
### 1. 两次MD5加密
将用户输入的密码和固定Salt进行拼接，通过MD5加密生成第一次加密后的密码；再将该密码和随机生成的Salt进行拼接，通过MD5进行第二次加密；最后将第二次加密后的密码和随机Salt存储到数据库。

好处：

第一次作用：防止用户明文密码在网络进行传输。

第二次作用：防止数据库被盗，避免通过MD5反推出密码，双重保险。

### 2. session共享
在用户的账号密码验证都正确的情况下，通过UUID生成唯一id作为token，再将token作为key、用户信息作为value模拟session存储到redis，同时将token存储到cookie，保存登录状态。

好处：在分布式集群情况下，服务器间需要同步，使用redis把session数据集中存储起来，解决session不一致的问题。

### 3. JSR303自定义参数验证
使用JSR303自定义校验器，实现对用户账号、密码的验证。

好处：使得验证逻辑从业务代码中脱离出来。

### 4. 全局异常统一处理
通过拦截所有异常，对各种异常进行相应的处理，当遇到异常就逐层上抛，一直抛到最终由一个统一的、专门负责异常处理的地方进行处理。

好处：有利于对异常的维护。

### 5. 页面缓存 + 对象缓存
页面缓存：将手动渲染得到的html页面缓存到redis，利用缓存使得访问次数较多的页面不用每次都动态加载。

对象缓存：包括对用户信息、商品信息、订单信息和token等数据进行缓存，利用缓存来减少对数据库的访问，大大加快查询速度。

### 6. 页面静态化
对商品详情和订单详情进行页面静态化处理，动态数据是通过接口从服务端获取。

好处：静态页面无需连接数据库，打开速度较动态页面会有明显提高。

### 7. 对卖超现象的处理
1. 对商品库存进行-1更新时，先对库存进行判断，只有当库存大于0才能做-1操作；防止库存变成负数。
2. 对数据库的秒杀订单表user_id和goods_id建立一个唯一索引，通过这种约束来避免用户重复购买。

### 8. 秒杀接口优化：减少数据库访问
1. 系统初始化，把商品库存数量加载到Redis
2. 收到请求，Redis预减库存，库存不足，直接返回，否则进入3
3. 请求入队，立即返回排队中
4. 请求出队，生成订单，减少库存
5. 客户端轮询，是否秒杀成功


### 10. 数学公式验证码防刷
在点击秒杀之前，先输入验证码，分散用户的请求
1. 添加生成验证码的接口
2. 在获取秒杀路径的时候，验证验证码
3. ScriptEngine使用

## 六、压测结果对比
开启1000个线程循环10次同时访问

![](https://github.com/hxllhhy/SuperBuy/blob/master/TestImg/base.png)
- 秒杀优化前 QPS=64.7

![](https://github.com/hxllhhy/SuperBuy/blob/master/TestImg/before.png)
- 秒杀优化后 QPS=374.1

![](https://github.com/hxllhhy/SuperBuy/blob/master/TestImg/after.png)
- 解决了超卖

![](https://github.com/hxllhhy/SuperBuy/blob/master/TestImg/data.png)
