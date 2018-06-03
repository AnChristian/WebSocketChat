package edu.njpi.rj1621.service;

import edu.njpi.rj1621.domain.User;

import java.util.List;

/**
 * @author Fleming
 */
public interface UserSvc {

    /**
     * addUser
     * @param user
     */
    public void addUser(User user);

    /**
     * removeUser
     * @param username
     */
    public void removeUser(String username);

    /**
     * modifyUser
     * @param user
     */
    public void modifyUser(User user);

    /**
     * queryUser
     * @param username
     * @return User
     */
    public User queryUser(String username);

    /**
     * queryUserList
     * @return List<User>
     */
    public List<User> queryUserList();

}
