package online.iizvv.service;

import online.iizvv.dao.PackageDao;
import online.iizvv.pojo.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：iizvv
 * @date ：Created in 2019-06-28 20:10
 * @description：IPA管理
 * @modified By：
 * @version: 1.0
 */
@Service
public class PackageServiceImpl {

    @Autowired
    private PackageDao packageDao;

    /**
     * create by: iizvv
     * description: 添加ipa
     * create time: 2019-09-02 18:59
     * 
     
     * @return boolean
     */
    public boolean insertPackage(Package pck) {
        int aPackage = packageDao.insertPackage(pck);
        return aPackage>0;
    }
    
    /**
     * create by: iizvv
     * description: 更新ipa
     * create time: 2019-09-02 19:00
     * 
     
     * @return boolean
     */
    public boolean updatePackage(Package pck) {
        int aPackage = packageDao.updatePackage(pck);
        return aPackage>0;
    }

    /**
     * create by: iizvv
     * description: 更新ipa是否限制下载
     * create time: 2019-09-21 17:16

     * @return boolean
     */
    public boolean updatePackageStateById(long id, boolean state) {
        int aPackage = packageDao.updatePackageStateById(id, state);
        return aPackage>0;
    }


    /**
     * create by: iizvv
     * description: 更新简介
     * create time: 2019-09-02 19:10
     *

     * @return boolean
     */
    public boolean updatePackageSummaryById(long id, String summary) {
        int aPackage = packageDao.updatePackageSummaryById(id, summary);
        return aPackage>0;
    }

    /**
     * create by: iizvv
     * description: 更新基本信息
     * create time: 2019-09-02 19:05
     *

     * @return boolean
     */
    public boolean updatePackageInfoById(long id, String subTitle, float level, String commentCount, int ranking, String className, int age, String summary) {
        int aPackage = packageDao.updatePackageInfoById(id, subTitle, level, commentCount, ranking, className, age, summary);
        return aPackage>0;
    }

    /**

    /**
     * create by: iizvv
     * description: 更新预览图
     * create time: 2019-09-02 19:13
     *

     * @return boolean
     */
    public boolean updatePackageImgsById(long id, String imgs) {
        int aPackage = packageDao.updatePackageImgsById(id, imgs);
        return aPackage>0;
    }

    /**
     * create by: iizvv
     * description: 更新可使用设备量
     * create time: 2019-09-02 19:13
     *

     * @return boolean
     */
    public boolean updatePackageTotalDeviceById(long id, long count) {
        int aPackage = packageDao.updatePackageTotalDeviceById(id, count);
        return aPackage>0;
    }

    /**
     * create by: iizvv
     * description: 更新ipa下载量
     * create time: 2019-07-23 09:50

     * @return boolean
     */
    public boolean updatePackageDownloadCountById(long id) {
        int aPackage = packageDao.updatePackageDownloadCountById(id);
        return aPackage>0;
    }

    /**
     * create by: iizvv
     * description: 更新ipa可用设备量
     * create time: 2019-09-02 17:45

     * @return boolean
     */
    public boolean updatePackageDeviceCountById(long id) {
        int aPackage = packageDao.updatePackageDeviceCountById(id);
        return aPackage>0;
    }

    /**
      * create by: iizvv
      * description: 设备添加失败后， 将已使用设备量-1
      * create time: 2019-09-03 12:23

      * @return boolean
      */
    public boolean rowBackPackageDeviceCountById(long id) {
        int aPackage = packageDao.rowBackPackageDeviceCountById(id);
        return aPackage>0;
    }

    /**
     * create by: iizvv
     * description: 更新证书信息
     * create time: 2019-07-04 11:36

     * @return boolean
     */
    public boolean updatePackageMobileconfigById(long id, String mobileconfig) {
        int aPackage = packageDao.updatePackageMobileconfigById(id, mobileconfig);
        return aPackage>0;
    }

    /**
     * create by: iizvv
     * description: 删除ipa文件
     * create time: 2019-08-21 12:37

     * @return boolean
     */
    public boolean deletePackageById(long id) {
        int aPackage = packageDao.deletePackageById(id);
        return aPackage>0;
    }

    /**
     * create by: iizvv
     * description: 删除指定用户上传的ipa文件
     * create time: 2019-08-21 12:37

     * @return int
     */
    public boolean deletePackageByUserId(long userId) {
        return packageDao.deletePackageByUserId(userId)>0;
    }

    /**
     * create by: iizvv
     * description: 获取指定IPA
     * create time: 2019-07-03 16:47

     * @return Package
     */
    public Package getPackageById(long id) {
        Package aPackage = packageDao.getPackageById(id);
        return aPackage;
    }

    /**
     * create by: iizvv
     * description: 获取指定IPA的证书地址
     * create time: 2019-09-23 06:41
     *

     * @return String
     */
    public String getMobileconfigById(long id) {
        String mobileconfig = packageDao.getMobileconfigById(id);
        return mobileconfig;
    }

    /**
      * create by: iizvv
      * description: 通过用户获取指定ipa
      * create time: 2019-09-05 14:34

      * @return Package
      */
    public Package getPackageByIdAndUserId(long userId, long id) {
        Package aPackage = packageDao.getPackageByIdAndUserId(userId, id);
        return aPackage;
    }

    /**
     * create by: iizvv
     * description: 获取当前用户上传的ipa列表
     * create time: 2019-09-22 12:50
     *

     * @return List
     */
    public List <Package> getAllPackageById(long userId) {
        return packageDao.getAllPackageById(userId);
    }

    /**
     * create by: iizvv
     * description: 获取指定IPA下载名称
     * create time: 2019-07-06 09:12

     * @return String
     */
    public String getPackageLinkById(long id) {
        String link = packageDao.getPackageLinkById(id);
        return link;
    }

    /**
     * create by: iizvv
     * description: 获取全部IPA
     * create time: 2019-07-03 16:27

     * @return List
     */
    public List<Package> getAllPackage() {
        List<Package> list = packageDao.getAllPackage();
        return list;
    }

    /**
     * create by: iizvv
     * description: 获取当前用户IPA
     * create time: 2019-08-25 16:57
     *

     * @return
     */
    public List <Package> getAllPackageByUserId(long userId) {
        List<Package> list = packageDao.getAllPackageByUserId(userId);
        return list;
    }
    
}
