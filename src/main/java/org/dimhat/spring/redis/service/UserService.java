package org.dimhat.spring.redis.service;

import org.dimhat.spring.redis.model.User;

/**
 * TODO
 * @author dimhat
 * @date 2015年12月17日 下午9:19:09
 * @version 1.0
 */
public interface UserService {

    User getByName(String name);

    User getByName2(String name);

    User getByName3(String name);

    User getByNameAndId(String name, Long id, Boolean sendLog);

    User getById(Long id);

    void update(User user);

    User update2(User user);

    void reload();

    User getByNameFromDefault(String name);

}
