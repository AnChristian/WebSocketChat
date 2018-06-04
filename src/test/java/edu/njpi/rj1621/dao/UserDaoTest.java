package edu.njpi.rj1621.dao;

import edu.njpi.rj1621.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-mybatis.xml"})
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void testSelectUser() throws Exception{
        String username = "flm";
        User user = userDao.selectUser(username);

        System.out.println(user.toString());

    }

    @Test
    public void testInsertUser() throws Exception{

        User user = new User();
        user.setUsername("flm");
        user.setPassword("111");
        user.setRoleCode("ff");

        userDao.insertUser(user);

    }

    @Test
    public void testUpdateUser() throws Exception{
        User user = new User();
        user.setUsername("flm");
        user.setPassword("111");
        user.setRoleCode("ff");

        userDao.updateUser(user);

    }

    @Test
    public void testDeleteUser() throws Exception{
        userDao.deleteUser("flm");
    }

    @Test
    public void testSelectUserList() throws Exception{
        List<User> userList = userDao.selectUserList();

        for (User user: userList) {
            System.out.println(user.toString());
        }

    }

}
