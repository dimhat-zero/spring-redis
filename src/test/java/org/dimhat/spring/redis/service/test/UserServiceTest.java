package org.dimhat.spring.redis.service.test;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.dimhat.spring.redis.model.User;
import org.dimhat.spring.redis.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Log4jConfigurer;

/**
 * @author dimhat
 * @date 2015年12月17日 下午9:47:40
 * @version 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring.xml" })
public class UserServiceTest {

    static {
        try {
            Log4jConfigurer.initLogging("classpath:log4j.properties");
        } catch (FileNotFoundException ex) {
            System.err.println("Cannot Initialize log4j");
        }
    }

    private Logger      logger = Logger.getLogger(UserServiceTest.class);

    @Autowired
    private UserService userService;

    @Test
    public void testGetByName() {
        User user1 = userService.getByName("admin");
        logger.info("user1:" + user1);
        User user2 = userService.getByName("admin");// in cache
        logger.info("user2:" + user2);
    }

    @Test
    public void testGetByNameDiff() {
        User user1 = userService.getByName("admin");//not in cache
        logger.info("user1:" + user1);
        User user2 = userService.getByName("admin");// in cache
        logger.info("user2:" + user2);
        User user3 = userService.getByNameFromDefault("admin");//not in cache , it's different with cache named user
        logger.info("user3:" + user3);
        User user4 = userService.getByNameFromDefault("admin");// in cache
        logger.info("user4:" + user4);
    }

    @Test
    public void testGetByName2() {
        User user1 = userService.getByName2("admin");
        logger.info("user1:" + user1);
        User user2 = userService.getByName2("admin");// not in cache
        logger.info("user2:" + user2);
    }

    @Test
    public void testGetByName3() {
        User user1 = userService.getByName3("admin");
        logger.info("user1:" + user1);
        User user2 = userService.getByName3("admin");//not in cache
        logger.info("user2:" + user2);
        User user3 = userService.getByName3("adm");
        logger.info("user3:" + user3);
        User user4 = userService.getByName3("adm");//in cache
        logger.info("user4:" + user4);
    }

    @Test
    public void testGetByNameAndId() {
        User user1 = userService.getByNameAndId("admin", 666L, Boolean.FALSE);
        logger.info("user1:" + user1);
        User user2 = userService.getByNameAndId("admin", 665L, Boolean.FALSE);//not in cache
        logger.info("user2:" + user2);
        User user3 = userService.getByNameAndId("admin", 666L, Boolean.TRUE);//in cache
        logger.info("user3:" + user3);
    }

    @Test
    public void testUpdate() {
        User user1 = userService.getByName("admin");
        logger.info("user1:" + user1);

        User user = new User();
        user.setUsername("admin");
        userService.update(user);//清除了缓存

        User user2 = userService.getByName("admin");// not in cache
        logger.info("user2:" + user2);
    }

    @Test
    public void testUpdate2() {
        User user1 = userService.getByName("admin");
        logger.info("user1:" + user1);

        User user = new User();
        user.setUsername("admin");
        user.setId(0L);
        user.setPassword("test set");
        userService.update2(user);//更新了缓存

        User user2 = userService.getByName("admin");//in cache
        logger.info("user2:" + user2);
    }

    @Test
    public void testGetById() {
        Long id = 444L;
        User user1 = userService.getById(id);
        logger.info("user1:" + user1);

        User user2 = userService.getById(id);// in cache
        logger.info("user2:" + user2);
    }

    @Test
    public void testReload() {
        userService.getByName("admin");
        userService.reload();
        //keys * is empty
    }

    public void test() {
        System.out.println("hello world");
    }
}
