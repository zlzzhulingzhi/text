
详细配置,在config目录 配置文件可参考teach-question 

redisson:
 singleServerConfig:
  address: 127.0.0.1:6379
  database: 3


1.RedissonObject 这个是比较通用的模板,任何对象都可以存在这里面,在spring 容器中注入对象即可 

    @Resource
    private RedissonObject redissonObject;
2.RedissonBinary 这个是存储二进制的模板.可以存放图片之内的二进制文件,在spring 容器中注入对象即可 

    @Resource
    private RedissonBinary redissonBinary;
3.RedissonCollection 这个是集合模板,可以存放Map,List,Set集合元素,在spring 容器中注入对象即可 

    @Resource
    private RedissonCollection redissonCollection;
