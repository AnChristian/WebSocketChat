package edu.njpi.rj1621.dao;

import edu.njpi.rj1621.domain.User;

import java.util.List;

/**
 * @author Fleming
 */
public interface UserDao {

    /**
     * insertUser
     * @param user
     */
    void insertUser(User user);

    /**
     * deleteUser
     * @param username
     */
    void deleteUser(String username);

    /**
     * updateUser
     * @param user
     */
    void updateUser(User user);

    /**
     * selectUser
     * @param username
     * @return User
     */
    User selectUser(String username);

    /**
     * selectUserList
     * @return List<User>
     */
    List<User> selectUserList();

}
