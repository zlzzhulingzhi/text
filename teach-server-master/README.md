# train-server
### init 2022.09.10

* 后端采用Spring Boot、Spring Cloud & Alibaba。
* 注册中心、配置中心选型Nacos，权限认证使用jwt+Redis。
* 流量控制框架选型Sentinel，分布式事务选型Seata。
* 代码生成器用easyCode生成.


# 编码约定
### controller 层
为方便不同角色接口权限管控，约定如下：  
该目录下分别建立单独目录，不同平台访问的数据接口不建议共用，原则上不同controller接口对应的service也不相同  
**platform**：平台管理后台接口  
**manage**：  机构管理后台接口  
**web**：     用户前台网页接口  
**h5**：      用户前台H5接口  
**lite**：    用户前台小程序接口  
**inner**：   内部调用接口  

### service 层
1.业务逻辑接口访问数据层接口，必须使用 **Mapper** 接口，避免出现循环依赖的情况。  
2.若遇到需要共用的业务逻辑，则自行商量将公共的业务逻辑接口抽取。  
3.定义服务上下游关系，下游服务可以直接依赖上游服务，反之则不可上游服务的变更对下游服务产生影响
  需要通过领域事件（异步）的方式来实现服务之间要通过数据Id（或类Id，能够唯一代表数据且不变的属性）来进行关联，
  尽量不做过多的数据冗余一旦需要上游服务调用下游服务才能完成业务时，要考虑是否上游服务缺少业务概念  

注意:  
 demo项目可以作为参考,业务项目都需要接入teach-common-core,teach-common-security,security主要获取额外的用户信息,因此配置文件redis配置
 database要设置成15来接受用户的参数,如果自身业务需要缓存,可再接入teach-common-redisson,自行配置

异步服务feign消息头解决方法:  
(1) 执行前把当线程的头重新设置进新线程区  
RequestAttributes attributes = RequestContextHolder.getRequestAttributes();  
RequestContextHolder.setRequestAttributes(attributes);  
(2)在主线程阻塞等待结果，由于请求仍有效没执行完毕，此时Servlet不会销毁，请求属性还保存在请求链路中，能被传递下去
RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(),true);//请求属性可继承

上下文工具: SecurityContextHolder  

项目mybatis-plus用了多租户模式,sql会自动注入orgId,createBy,updateBy,CreateTime,updateTime 不需要自己填充.


端口整理:
/doc 目录

entity目录:
 实体类，与数据库结构一一映射，是数据持久化过程中的数据载体。 

pojo目录:  
 DTO，用于微服务之间的数据组装和传输，是应用之间数据传输的载体。  
 VO，用于复杂的sql mapper返回  
 以Request结尾的类,用于请求参数封装  
 以Response结尾的类,用于请求结果响应  

单机swagger地址:  
日志服务为例  
localhost:9204/log/swagger-ui/index.html  
          port/path/swagger-ui/index.html

代码生成脚本在:/other/EasyCodeConfig.json
把里边的内容复制,在easyCode里选入剪贴板导入即可







