package online.iizvv.service;

import online.iizvv.dao.PKDao;
import online.iizvv.pojo.PackageKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：iizvv
 * @date ：Created in 2019-09-22 11:21
 * @description：密钥管理接口实现
 * @modified By：
 * @version: 1.0
 */
@Service
public class PKServiceImpl {

    @Autowired
    PKDao pkDao;

    /**
     * create by: iizvv
     * description: 添加密钥到指定IPA
     * create time: 2019-09-22 11:05
     *

     * @return boolean
     */
    public boolean insertKeyByPackageId(long packageId, String key) {
        int aPk = pkDao.insertKeyByPackageId(packageId, key);
        return aPk>0;
    }


    /**
     * create by: iizvv
     * description: 更新指定密钥状态
     * create time: 2019-09-22 11:13
     *

     * @return boolean
     */
    public boolean updateKeyStateById(long id) {
        int aPK = pkDao.updateKeyStateById(id);
        return aPK>0;
    }

    /**
     * create by: iizvv
     * description: 删除指定密钥
     * create time: 2019-09-22 11:19
     *

     * @return boolean
     */
    public boolean deleteKeyById(long id) {
        int aPK = pkDao.deleteKeyById(id);
        return aPK>0;
    }

    /**
     * create by: iizvv
     * description: 删除指定ipa的密钥
     * create time: 2019-09-24 19:37
     *

     * @return boolean
     */
    public boolean deleteKeyByPackageId(long packageId) {
        int aPK = pkDao.deleteKeyByPackageId(packageId);
        return aPK>0;
    }

    /**
     * create by: iizvv
     * description: 获取密钥信息
     * create time: 2019-09-22 12:22
     *

     * @return PackageKey
     */
    public PackageKey getPackageKeyInfoByKey(long packageId, String key) {
        PackageKey packageKey = pkDao.getPackageKeyInfoByKey(packageId, key);
        return packageKey;
    }

    /**
     * create by: iizvv
     * description: 获取指定ipa下的所有密钥
     * create time: 2019-09-22 10:58
     *

     * @return List
     */
    public List<PackageKey> getAllKeysByPackageId(long packageId) {
        List<PackageKey> keyList = pkDao.getAllKeysByPackageId(packageId);
        return keyList;
    }


    /**
     * create by: iizvv
     * description: 获取指定ipa下未使用的密钥
     * create time: 2019-09-22 11:44
     *

     * @return List
     */
    public List <PackageKey> getAllUnusedKeysByPackageId(long packageId) {
        List<PackageKey> keyList = pkDao.getAllUnusedKeysByPackageId(packageId);
        return keyList;
    }


    /**
     * create by: iizvv
     * description: 获取指定ipa下已使用的密钥
     * create time: 2019-09-22 11:44
     *

     * @return List
     */
    public List <PackageKey> getAllUsedKeysByPackageId(long packageId) {
        List<PackageKey> keyList = pkDao.getAllUsedKeysByPackageId(packageId);
        return keyList;
    }

}
