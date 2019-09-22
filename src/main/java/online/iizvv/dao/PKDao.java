package online.iizvv.dao;

import online.iizvv.pojo.PackageKey;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author ：iizvv
 * @date ：Created in 2019-09-22 10:48
 * @description：下载密钥
 * @modified By：
 * @version: 1.0
 */
@Mapper
public interface PKDao {

    /**
     * create by: iizvv
     * description: 添加密钥到指定IPA
     * create time: 2019-09-22 11:05
     * 

     * @return int
     */
    @Insert("INSERT INTO package_key (package_id, `key`) VALUES (#{packageId}, #{key})")
    int insertKeyByPackageId(long packageId, String key);


    /**
     * create by: iizvv
     * description: 更新指定密钥状态
     * create time: 2019-09-22 11:13
     * 
     
     * @return 
     */
    @Update("UPDATE package_key SET is_use = false, use_time = CURRENT_TIMESTAMP WHERE id = #{id}")
    int updateKeyStateById(long id);

    /**
     * create by: iizvv
     * description: 删除指定密钥
     * create time: 2019-09-22 11:19
     *

     * @return int
     */
    @Delete("DELETE FROM package_key WHERE id = #{id}")
    int deleteKeyById(long id);


    /**
     * create by: iizvv
     * description: 获取密钥信息
     * create time: 2019-09-22 12:22
     *

     * @return PackageKey
     */
    @Select("SELECT * FROM package_key WHERE package_id = #{packageId} AND `key` = #{key} AND is_use = true")
    PackageKey getPackageKeyInfoByKey(long packageId, String key);

    /**
     * create by: iizvv
     * description: 获取指定ipa下的所有密钥
     * create time: 2019-09-22 10:58
     *

     * @return List
     */
    @Select("SELECT * FROM package_key WHERE package_id = #{packageId}")
    List<PackageKey> getAllKeysByPackageId(long packageId);


    /**
     * create by: iizvv
     * description: 获取指定ipa下未使用的密钥
     * create time: 2019-09-22 11:44
     * 

     * @return List
     */
    @Select("SELECT * FROM package_key WHERE package_id = #{packageId} AND is_use = true")
    List <PackageKey> getAllUnusedKeysByPackageId(long packageId);


    /**
     * create by: iizvv
     * description: 获取指定ipa下已使用的密钥
     * create time: 2019-09-22 11:44
     *

     * @return List
     */
    @Select("SELECT * FROM package_key WHERE package_id = #{packageId} AND is_use = false")
    List <PackageKey> getAllUsedKeysByPackageId(long packageId);


}
