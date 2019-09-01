package online.iizvv.dao;

import online.iizvv.pojo.Apple;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author ：iizvv
 * @date ：Created in 2019-06-26 21:45
 * @description：开发者帐号
 * @modified By：
 * @version: 1.0
 */
@Mapper
public interface AppleDao {

    /**
     * create by: iizvv
     * description: 添加开发者帐号
     * create time: 2019-06-29 17:57
     *

     * @return 是否添加成功
     */
    @Insert("INSERT INTO apple (account, count, p8, iss, kid, cerId, bundleIds) " +
            "VALUES (#{account}, #{count}, #{p8}, #{iss}, #{kid}, #{cerId}, #{bundleIds})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int insertAppleAccount(Apple apple);

    /**
      * create by: iizvv
      * description: 更新账号P12
      * create time: 2019-07-06 14:59

      * @return 是否成功
      */
    @Update("UPDATE apple SET p12 = #{p12} WHERE id = #{id}")
    int updateP12(String p12, long id);

    /**
     * create by: iizvv
     * description: 删除帐号
     * create time: 2019-08-20 06:14
     *

     * @return 删除帐号与当前帐号下的所有设备
     */
    @Delete("DELETE FROM apple WHERE id = #{id}")
    int deleteById(long id);

    /**
      * create by: iizvv
      * description: 更新账号下设备数量
      * create time: 2019-07-06 16:24
      
      * @return 是否成功
      */
    @Update("UPDATE apple SET count = count-1 WHERE id = #{id}")
    int updateDevicesCount(long id);

    /**
     * create by: iizvv
     * description: 获取开发者帐号信息
     * create time: 2019-06-26 21:52
     *
     
     * @return Apple
     */
    @Select("SELECT * FROM apple WHERE id=#{id}")
    Apple getAppleAccountById(long id);

    @Select("SELECT * FROM apple WHERE account=#{account}")
    Apple getAppleAccountByAccount(String account);

    /**
      * create by: iizvv
      * description: 获取全部账号信息
      * create time: 2019-07-23 09:29

      * @return List<Apple>
      */
    @Select("SELECT * FROM apple")
    List<Apple> getAllAppleAccounts();

    /**
     * create by: iizvv
     * description: 获取一个可使用的开发者帐号
     * create time: 2019-06-26 21:59
     *

     * @return Apple
     */
    @Select("SELECT * FROM apple where is_use = true ORDER BY count DESC LIMIT 1")
    Apple getBeUsableAppleAccount();

}
