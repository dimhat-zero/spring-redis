##spring整合缓存示例

###spring的cache注解
主要使用了以下三个注解
* @Cacheable
* @CachePut
* @CacheEvict

####参数说明
value参数可指定多个库
key参数指定主键
返回值为保存的值。
condition是缓存条件限定

###关键配置
配置cacheManage，使用CacheFactoryBean的可用多个name会分开缓存
```xml
<!-- 启用缓存注解功能，这个是必须的，否则注解不会生效，另外，该注解一定要声明在spring主配置文件中才会生效 -->
<cache:annotation-driven cache-manager="cacheManager" />

<bean id="cacheManager" class="org.springframework.cache.support.SimpleCacheManager">
	<property name="caches">
		<set>
			<!-- 方式1：redis -->
			<bean class="org.dimhat.spring.redis.cache.RedisCache">
				<property name="redisTemplate" ref="redisTemplate" />
				<property name="name" value="user" />
			</bean>
			
			<!-- 方式2：基于 java.util.concurrent.ConcurrentHashMap 的一个内存缓存实现方案 -->
			<bean class="org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean">
				<property name="name" value="user" />
			</bean>
			<bean class="org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean">
				<property name="name" value="user2" />
			</bean>
		</set>
	</property>
</bean>
```

###方式1：整合redis

maven依赖
```
<!-- redis cache -->
<dependency>
	<groupId>redis.clients</groupId>
	<artifactId>jedis</artifactId>
	<version>2.5.2</version>
</dependency>

<dependency>
	<groupId>org.springframework.data</groupId>
	<artifactId>spring-data-redis</artifactId>
	<version>1.3.4.RELEASE</version>
</dependency>
```

spring配置
```xml
<!-- 新版本属性值 -->
<bean id="poolConfig" class="redis.clients.jedis.JedisPoolConfig">
	<!-- 最大保存idel状态对象数 -->
	<property name="maxIdle" value="${redis.maxIdle}" />
	<!-- 最大等待时间：单位ms -->
	<property name="maxWaitMillis" value="${redis.maxWait}" />
	<!-- 最大连接数：能够同时建立的最大链接个数 -->
	<property name="maxTotal" value="${redis.maxActive}" />
	<!-- 返回连接时，检测连接是否成功 -->
	<property name="testOnBorrow" value="${redis.testOnBorrow}" />
</bean>

<bean id="connectionFactory"
	class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory">
	<property name="hostName" value="${redis.host}" />
	<property name="port" value="${redis.port}" />
	<property name="password" value="${redis.pass}" />
	<property name="poolConfig" ref="poolConfig" />
</bean>

<bean id="redisTemplate" class="org.springframework.data.redis.core.StringRedisTemplate">
	<property name="connectionFactory" ref="connectionFactory" />
</bean>
```

###方式2：使用ConcurrentHashMap
基于 java.util.concurrent.ConcurrentHashMap 的一个内存缓存实现方案

###方式3：整合ehcache
maven依赖
```
<!-- ehcache -->
<dependency>
	<groupId>net.sf.ehcache</groupId>
	<artifactId>ehcache-core</artifactId>
	<version>2.6.6</version>
</dependency>
```
配置cacheManage
```xml
<!-- 方式3：使用ehcache -->
<bean id="cacheManagerFactory" class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean">  
	<property name="configLocation" value="classpath:ehcache.xml" />  
</bean>  
     
<bean id="cacheManager" class="org.springframework.cache.ehcache.EhCacheCacheManager">      
	<property name="cacheManager"  ref="cacheManagerFactory"/>      
</bean>  
```
ehcache.xml
```
<?xml version="1.0" encoding="UTF-8"?>  
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="http://ehcache.org/ehcache.xsd">  
     
    <diskStore path="java.io.tmpdir/ehcache"/>  
      
    <defaultCache  
           maxElementsInMemory="1000"  
           eternal="false"  
           timeToIdleSeconds="120"  
           timeToLiveSeconds="120"  
           overflowToDisk="false"/>  
             
    <cache name="user"   
           maxElementsInMemory="1000"   
           eternal="false"  
           timeToIdleSeconds="120"  
           timeToLiveSeconds="120"  
           overflowToDisk="false"   
           memoryStoreEvictionPolicy="LRU"/>  
    
    <cache name="user2"   
           maxElementsInMemory="1000"   
           eternal="false"  
           timeToIdleSeconds="120"  
           timeToLiveSeconds="120"  
           overflowToDisk="false"   
           memoryStoreEvictionPolicy="LRU" />  
      
</ehcache>  
```

###扩展
SpEL表达式

###环境依赖
- redis
- maven
