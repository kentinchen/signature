package online.iizvv.dao;

import online.iizvv.pojo.Package;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author ：iizvv
 * @date ：Created in 2019-06-28 19:25
 * @description：TODO
 * @modified By：
 * @version: 1.0
 */
public interface PackageDao {

    /**
      * create by: iizvv
      * description: 添加IPA
      * create time: 2019-06-28 20:09
      *
      * @return 是否添加成功
      */
    @Insert("INSERT INTO package (name, icon, version, build_version, mini_version, bundle_identifier, link, user_id) " +
            "VALUES (#{name}, #{icon}, #{version}, #{buildVersion}, #{miniVersion}, #{bundleIdentifier}, #{link}, #{userId})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int insertPackage(Package pck);

    /**
      * create by: iizvv
      * description: 更新可用下载次数
      * create time: 2019-08-21 12:48

      * @return int
      */
    @Update("UPDATE package SET total_count = total_count+#{totalCount} WHERE id = #{id}")
    int updateTotalCountById(long id, long totalCount);

    /**
      * create by: iizvv
      * description: 重置可用下载次数
      * create time: 2019-08-21 19:41
      
      * @return int
      */
    @Update("UPDATE package SET total_count = #{totalCount} WHERE id = #{id}")
    int resetTotalCountById(long id, long totalCount);

    /**
     * create by: iizvv
     * description: 更新证书信息
     * create time: 2019-07-04 11:36

     * @return void
     */
    @Update("UPDATE package SET mobileconfig = #{mobileconfig} WHERE id=#{id}")
    void updateMobileconfigById(String mobileconfig, long id);

    /**
      * create by: iizvv
      * description: 删除ipa文件
      * create time: 2019-08-21 12:37

      * @return int
      */
    @Delete("DELETE FROM package WHERE id=#{id}")
    int deleteById(long id);

    /**
      * create by: iizvv
      * description: 更新IPA
      * create time: 2019-07-04 14:38

      * @return int
      */
    @Update("UPDATE package SET name = #{name}, icon = #{icon}, version = #{version}, " +
            "build_version = #{buildVersion}, mini_version = #{miniVersion}, " +
            "link = #{link} WHERE id = #{id}")
    int updatePackage(Package pck);

    /**
      * create by: iizvv
      * description: 更新ipa下载量
      * create time: 2019-07-23 09:50

      * @return int
      */
    @Update("UPDATE package SET count = count+1 WHERE id = #{id}")
    int updatePackageCountById(long id);

    /**
      * create by: iizvv
      * description: 获取指定IPA
      * create time: 2019-07-03 16:47

      * @return Package
      */
    @Select("SELECT * FROM package WHERE id=#{id}")
    Package getPackageById(long id);

    @Select("SELECT * FROM package WHERE bundle_identifier=#{bundleIdentifier} AND user_id=#{userId}")
    Package getPackageByBundleIdentifier(String bundleIdentifier, long userId);

    /**
      * create by: iizvv
      * description: 获取指定IPA下载名称
      * create time: 2019-07-06 09:12

      * @return String
      */
    @Select("SELECT link FROM package WHERE id=#{id}")
    String getPackageLinkById(String id);

    /**
      * create by: iizvv
      * description: 获取全部IPA
      * create time: 2019-07-03 16:27

      * @return List
      */
    @Select("SELECT * FROM package")
    List<Package> getAllPackage();

    /**
     * create by: iizvv
     * description: 获取当前用户IPA
     * create time: 2019-08-25 16:57
     *

     * @return
     */
    @Select("SELECT * FROM package WHERE user_id=#{userId}")
    List <Package> getAllPackageByUserId(long userId);

}
