package org.dimhat.spring.redis.service;

import java.util.Random;

import org.dimhat.spring.redis.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 注解的value指定cache的name，name对应一个teamplate，teamplate对应一个连接
 * 如果连接是相同，说明在一个库里，key要避开重复
 * 
 * @author dimhat
 * @date 2015年12月17日 下午9:19:56
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    private Random       random = new Random();

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /** 
     * 先从缓存中查找，如果命中则不执行方法
     */
    @Override
    @Cacheable(value = "user")
    public User getByName(String name) {
        User user = getFromDb(name);
        logger.info(String.format("execute getByName(%s)", name));
        return user;
    }

    @Override
    @Cacheable(value = "user2")
    public User getByNameFromDefault(String name) {
        User user = getFromDb(name);
        logger.info(String.format("execute getByNameFromDefault(%s)", name));
        return user;
    }

    /** 
     * 一定执行方法，并更新到缓存中
     */
    @Override
    @CachePut(value = "user")
    public User getByName2(String name) {
        User user = getFromDb(name);
        logger.info(String.format("execute getByName2(%s)", name));
        return user;
    }

    /** 
     * 条件限定，如果字符串长度小于5则去缓存中找
     */
    @Override
    @Cacheable(value = "user", condition = "#name.length()<5")
    public User getByName3(String name) {
        User user = getFromDb(name);
        logger.info(String.format("execute getByName3(%s)", name));
        return user;
    }

    /** 
     * 多参数方法自定义key，支持SpEL表达式
     */
    @Override
    @Cacheable(value = "user", key = "#name.concat(#id.toString())")
    public User getByNameAndId(String name, Long id, Boolean sendLog) {
        User user = getFromDb(name, id);
        logger.info(String.format("execute getByNameAndId(%s,%d,%b)", name, id, sendLog));
        return user;
    }

    /** 
     * 清除指定key的缓存，delete是否更恰当
     */
    @Override
    @CacheEvict(value = "user", key = "#user.getUsername()")
    public void update(User user) {
        updateDb(user);
        logger.info(String.format("execute update(%s)", user.toString()));
    }

    /** 
     * 更新指定key的缓存
     * 缓存值 = 返回模型？传入模型？
     */
    @Override
    @CachePut(value = "user", key = "#user.getUsername()")
    public User update2(User user) {
        updateDb(user);
        logger.info(String.format("execute update2(%s)", user.toString()));
        return user;
    }

    @Override
    @CacheEvict(value = "user", allEntries = true)
    public void reload() {
        logger.info("flushdb ok");
    }

    /** 
     * 更新指定key的缓存，由于cache实现类强转string，key一定要转换成string
     */
    @Override
    @Cacheable(value = "user", key = "#id.toString()")
    public User getById(Long id) {
        User user = getFromDb("haha", id);
        logger.info(String.format("execute getById(%d)", id));
        return user;
    }

    private void updateDb(User user) {
        logger.info("update db " + user.toString());
    }

    private User getFromDb(String name) {
        User user = new User();
        Long id = (long) (random.nextInt(1000));//模拟数据在更改
        user.setId(id);
        user.setUsername(name);
        user.setPassword(name + id);
        logger.info("get from db " + user.toString());
        return user;
    }

    private User getFromDb(String name, Long id) {
        User user = new User();
        user.setId(id);
        user.setUsername(name);
        user.setPassword(name + id);
        logger.info("get from db with id" + user.toString());
        return user;
    }

}
