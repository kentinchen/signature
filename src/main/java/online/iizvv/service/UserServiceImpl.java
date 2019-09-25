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

    public boolean register(String username, String password, long level) {
        int register = userDao.register(username, password, level);
        return register > 0;
    }

    /**
     * create by: iizvv
     * description: 删除指定用户
     * create time: 2019-09-25 08:48

     * @return boolean
     */
    public boolean deleteUserById(long id) {
        int i = userDao.deleteUserById(id);
        return i>0;
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

    public boolean updateUserLevelById(long id, long level) {
        return userDao.updateUserLevelById(id, level)>0;
    }

    /**
     * create by: iizvv
     * description: 超管修改用户密码
     * create time: 2019-09-25 10:03

     * @return boolean
     */
    public boolean updateUserPasswordById(long id, String password) {
        int i = userDao.updateUserPasswordById(id, password);
        return i>0;
    }

    /**
     * create by: iizvv
     * description: 使用原始密码修改密码
     * create time: 2019-09-25 10:06

     * @return int
     */
    public boolean updateUserPasswordByIdAndOldPassword(long id, String oldPassword, String password) {
        return userDao.updateUserPasswordByIdAndOldPassword(id, oldPassword, password)>0;
    }

}
