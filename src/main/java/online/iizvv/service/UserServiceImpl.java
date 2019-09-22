package online.iizvv.service;

import online.iizvv.dao.UserDao;
import online.iizvv.pojo.Package;
import online.iizvv.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：iizvv
 * @date ：Created in 2019-08-22 21:49
 * @description：用户管理
 * @modified By：
 * @version: 1.0
 */
@Service
public class UserServiceImpl {

    @Autowired
    private UserDao userDao;

    public int register(String username, String password, long level) {
        return userDao.register(username, password, level);
    }

    public User login(String username, String password) {
        return userDao.login(username, password);
    }

    public List<User> getAllReviewUser() {
        return userDao.getAllReviewUser();
    }

    public List<User> getAllUser() {
        return userDao.getAllUser();
    }

    public int getUserCount() {
        return userDao.getUserCount();
    }

    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public User getUserInfo(long id) {
        return userDao.getUserInfo(id);
    }

    public int checkUserById(long id, long level) {
        return userDao.checkUserById(id, level);
    }

}
