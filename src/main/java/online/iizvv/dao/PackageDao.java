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
@Mapper
public interface PackageDao {

    /**
      * create by: iizvv
      * description: 添加IPA
      * create time: 2019-06-28 20:09
      *
      * @return int
      */
    @Insert("INSERT INTO package (name, icon, version, build_version, mini_version, bundle_identifier, link, user_id, size) " +
            "VALUES (#{name}, #{icon}, #{version}, #{buildVersion}, #{miniVersion}, #{bundleIdentifier}, #{link}, #{userId}, #{size})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int insertPackage(Package pck);

    /**
     * create by: iizvv
     * description: 更新IPA
     * create time: 2019-07-04 14:38

     * @return int
     */
    @Update("UPDATE package SET name = #{name}, icon = #{icon}, version = #{version}, " +
            "build_version = #{buildVersion}, mini_version = #{miniVersion}, " +
            "link = #{link}, size = #{size} WHERE id = #{id}")
    int updatePackage(Package pck);

    /**
      * create by: iizvv
      * description: 更新ipa是否限制下载
      * create time: 2019-09-21 17:16

      * @return int
      */
    @Update("UPDATE package SET is_stint = #{state} WHERE id = #{id}")
    int updatePackageStateById(long id, boolean state);

    /**
     * create by: iizvv
     * description: 更新简介
     * create time: 2019-09-02 19:05
     * 
     
     * @return int
     */
    @Update("UPDATE package SET summary=#{summary} WHERE id=#{id}")
    int updatePackageSummaryById(long id, String summary);

    /**
     * create by: iizvv
     * description: 更新基本信息
     * create time: 2019-09-02 19:05
     *

     * @return int
     */
    @Update("UPDATE package SET sub_title=#{subTitle}, level=#{level}, comment_count=#{commentCount}, " +
            "ranking=#{ranking}, class_name=#{className}, age=#{age}, summary=#{summary} WHERE id=#{id}")
    int updatePackageInfoById(long id, String subTitle, float level, String commentCount, int ranking, String className, int age, String summary);

    /**
     * create by: iizvv
     * description: 更新预览图
     * create time: 2019-09-02 19:07
     * 
     
     * @return int
     */
    @Update("UPDATE package SET imgs=#{imgs} WHERE id=#{id}")
    int updatePackageImgsById(long id, String imgs);
    
    /**
      * create by: iizvv
      * description: 设置可用设备量
      * create time: 2019-08-21 19:41
      
      * @return int
      */
    @Update("UPDATE package SET total_device = #{count} WHERE id = #{id}")
    int updatePackageTotalDeviceById(long id, long count);

    /**
     * create by: iizvv
     * description: 更新ipa下载量
     * create time: 2019-07-23 09:50

     * @return int
     */
    @Update("UPDATE package SET download_count = download_count+1 WHERE id = #{id}")
    int updatePackageDownloadCountById(long id);

    /**
     * create by: iizvv
     * description: 更新ipa可用设备量
     * create time: 2019-09-02 17:45

     * @return int
     */
    @Update("UPDATE package SET use_device = use_device+1 WHERE id = #{id} AND use_device<=total_device")
    int updatePackageDeviceCountById(long id);

    /**
      * create by: iizvv
      * description: 设备添加失败后， 将已使用设备量-1
      * create time: 2019-09-03 12:14

      * @return
      */
    @Update("UPDATE package SET use_device = use_device-1 WHERE id = #{id}")
    int rowBackPackageDeviceCountById(long id);

    /**
     * create by: iizvv
     * description: 更新证书信息
     * create time: 2019-07-04 11:36

     * @return int
     */
    @Update("UPDATE package SET mobileconfig = #{mobileconfig} WHERE id=#{id}")
    int updatePackageMobileconfigById(long id, String mobileconfig);

    /**
      * create by: iizvv
      * description: 删除ipa文件
      * create time: 2019-08-21 12:37

      * @return int
      */
    @Delete("DELETE FROM package WHERE id=#{id}")
    int deletePackageById(long id);

    /**
     * create by: iizvv
     * description: 删除指定用户上传的ipa文件
     * create time: 2019-08-21 12:37

     * @return int
     */
    @Delete("DELETE FROM package WHERE user_id=#{userId}")
    int deletePackageByUserId(long userId);

    /**
      * create by: iizvv
      * description: 获取指定IPA
      * create time: 2019-07-03 16:47

      * @return Package
      */
    @Select("SELECT * FROM package WHERE id=#{id}")
    Package getPackageById(long id);

    /**
      * create by: iizvv
      * description: 获取指定用户的指定ipa
      * create time: 2019-09-05 14:36

      * @return Package
      */
    @Select("SELECT * FROM package WHERE user_id=#{userId} AND id=#{id}")
    Package getPackageByIdAndUserId(long userId, long id);

    /**
     * create by: iizvv
     * description: 获取指定IPA的证书名称
     * create time: 2019-09-23 06:41
     *

     * @return String
     */
    @Select("SELECT mobileconfig FROM package WHERE id=#{id}")
    String getMobileconfigById(long id);

    /**
      * create by: iizvv
      * description: 获取指定IPA下载名称
      * create time: 2019-07-06 09:12

      * @return String
      */
    @Select("SELECT link FROM package WHERE id=#{id}")
    String getPackageLinkById(long id);

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
     * description: 获取指定用户上传的ipa列表
     * create time: 2019-09-22 12:50
     *

     * @return List
     */
    @Select("SELECT * FROM package WHERE user_id = #{userId}")
    List <Package> getAllPackageById(long userId);

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
