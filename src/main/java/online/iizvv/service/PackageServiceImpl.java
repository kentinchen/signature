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

    public int insertPackage(Package pck) {
        return packageDao.insertPackage(pck);
    }

    public int updateTotalCountById(long id, long totalCount) {
        return packageDao.updateTotalCountById(id, totalCount);
    }

    public int resetTotalCountById(long id, long totalCount) {
        return packageDao.resetTotalCountById(id, totalCount);
    }

    public int deleteById(long id) {
        return packageDao.deleteById(id);
    }

    public void updateMobileconfig(String mobileconfig, long id) {
        packageDao.updateMobileconfigById(mobileconfig, id);
    }

    public int updatePackage(Package pck) {
        return packageDao.updatePackage(pck);
    }

    public Package getPackageById(long id) {
        return packageDao.getPackageById(id);
    }

    public Package getPackageByBundleIdentifier(String bundleIdentifier, long userId) {
        return packageDao.getPackageByBundleIdentifier(bundleIdentifier, userId);
    }

    public String getPackageLinkById(String id) {
        return packageDao.getPackageLinkById(id);
    }

    public int updatePackageCountById(long id) {
        return packageDao.updatePackageCountById(id);
    }

    public List<Package>getAllPackage() {
        return packageDao.getAllPackage();
    }

    public List <Package> getAllPackageByUserId(long userId) {
        return packageDao.getAllPackageByUserId(userId);
    }


}
