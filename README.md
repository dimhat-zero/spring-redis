###spring整合redis 注解方式示例
主要使用了以下三个注解
* @Cacheable
* @CachePut
* @CacheEvict

value参数可指定多个库，key参数指定主键，返回值为保存的值。
condition是缓存条件限定

###扩展
SpEL表达式

###环境依赖
- redis
- maven
