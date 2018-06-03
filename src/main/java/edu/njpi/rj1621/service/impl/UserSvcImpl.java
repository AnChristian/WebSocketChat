package edu.njpi.rj1621.service.impl;

import edu.njpi.rj1621.dao.UserDao;
import edu.njpi.rj1621.domain.User;
import edu.njpi.rj1621.service.UserSvc;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Fleming
 */

@Service("userSvc")
public class UserSvcImpl implements UserSvc {

    @Resource
    private UserDao userDao;

    @Override
    public void addUser(User user) {
        userDao.insertUser(user);
    }

    @Override
    public void removeUser(String username) {
        userDao.deleteUser(username);
    }

    @Override
    public void modifyUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    public User queryUser(String username) {
        return this.userDao.selectUser(username);
    }

    @Override
    public List<User> queryUserList() {
        return userDao.selectUserList();
    }
}
