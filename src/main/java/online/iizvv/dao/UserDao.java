package online.iizvv.dao;

import online.iizvv.pojo.Package;
import online.iizvv.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author ：iizvv
 * @date ：Created in 2019-08-21 22:03
 * @description：用户
 * @modified By：
 * @version: 1.0
 */

@Mapper
public interface UserDao {

    /**
     * create by: iizvv
     * description: 用户注册
     * create time: 2019-08-25 09:36
     *

     * @return 是否添加成功
     */
    @Insert("INSERT INTO user (username, password, level) VALUES (#{username}, #{password}, #{level})")
    int register(String username, String password, long level);

    /**
     * create by: iizvv
     * description: 登录
     * create time: 2019-08-25 09:37
     *

     * @return 用户对象
     */
    @Select("SELECT * FROM user WHERE username=#{username} AND password=#{password}")
    User login(String username, String password);

    /**
     * create by: iizvv
     * description: 获取用户数量
     * create time: 2019-08-25 09:37
     *

     * @return int
     */
    @Select("SELECT COUNT(id) FROM user")
    int getUserCount();

    /**
     * create by: iizvv
     * description: 获取所有待审核用户
     * create time: 2019-08-25 15:54
     * 
     
     * @return 
     */
    @Select("SELECT * FROM user WHERE level=0")
    List <User> getAllReviewUser();
    
    /**
     * create by: iizvv
     * description: 获取所有已审核用户
     * create time: 2019-08-25 15:56
     * 
     
     * @return 
     */
    @Select("SELECT * FROM user WHERE level=2")
    List <User> getAllUser();
    
    /**
     * create by: iizvv
     * description: 通过用户名获取用户id
     * create time: 2019-08-25 11:17
     *

     * @return id
     */
    @Select("SELECT * FROM user WHERE username=#{username}")
    User getUserByUsername(String username);

    /**
     * create by: iizvv
     * description: 获取用户信息
     * create time: 2019-08-25 19:31
     * 
     
     * @return 
     */
    @Select("SELECT * FROM user WHERE id=#{id} AND level<>0")
    User getUserInfo(long id);

    /**
     * create by: iizvv
     * description: 更新帐号状态
     * create time: 2019-08-25 21:01
     *

     * @return
     */
    @Update("UPDATE user SET level = #{level} WHERE id=#{id}")
    int checkUserById(long id, long level);

}
