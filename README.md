# myqingchengcode

ssm+dubbo 练习项目

# 项目架构

### qingcheng_common

公共模块，用于存放工具类等

### qingcheng_service_goods
商品服务模块 : web模块（独立发布），所以需要在web.xml中加载配置


### qingcheng_web_manager
后台控制器模块 : web模块（独立发布），所以需要在web.xml中加载配置



# dubbo
- 需要在服务侧注册服务，访问侧引入服务 （都需要配置dubbo） 


# 待做
- 后台创建商品
    - 插入一个spu
    - 插入多个sku
    - 实现品牌和分类的关联（多对多关系）
- 分布式id
- 定时任务
    - 开启任务调度的配置要 加上 （common——web下有独立的 配置文件）
- 统计需求里的 sql 语句
- 跨库查询，使用视图
- spring安全组件 在 web.xml中的配置要放在 springmvc 之前要不会失效
- springDataRadius 配置和使用
- es 的 安装和使用
- rabbitMQ 使用
- 单点登录 CAS 使用 （这个是用于没有前后端分离情况下的，可以先不看）
- 购物车（java 的 流式编程） 
- 分布式事务  （啥时候 所得库存？）
- echosite 内网映射工具的配置和使用（感觉可以先不用看）
- rabbitMp + webSocket 的使用
- rabbitMq 延时队列
- redis 可视化工具



  