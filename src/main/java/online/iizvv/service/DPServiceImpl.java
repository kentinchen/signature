package online.iizvv.service;

import online.iizvv.dao.DPDao;
import online.iizvv.pojo.DevicePackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ：iizvv
 * @date ：Created in 2019-09-04 21:05
 * @description：关联表操作
 * @modified By：
 * @version: 1.0
 */
@Service
public class DPServiceImpl {

    @Autowired
    DPDao dpDao;

    /**
     * create by: iizvv
     * description: 设置设备是否可用
     * create time: 2019-09-04 21:35
     *

     * @return boolean
     */
    public boolean updateDPIsUse(long deviceId, boolean isUse) {
        return dpDao.updateDPIsUse(deviceId, isUse)>0;
    }

    /**
     * create by: iizvv
     * description: 写入关联表
     * create time: 2019-09-04 20:57
     *

     * @return boolean
     */
    public boolean insertDP(long deviceId, long packageId) {
        return dpDao.insertDP(deviceId, packageId)>0;
    }

    /**
     * create by: iizvv
     * description: 查询绑定信息
     * create time: 2019-09-04 21:00
     *

     * @return DevicePackage
     */
    public DevicePackage getDPByIds(long deviceId, long packageId) {
        return dpDao.getDPByIds(deviceId, packageId);
    }

    /**
     * create by: iizvv
     * description: 获取绑定数量
     * create time: 2019-09-04 21:00
     *

     * @return DevicePackage
     */
    public long getDPCountByIds(long deviceId, long packageId) {
        return dpDao.getDPCountByIds(deviceId, packageId);
    }


    /**
     * create by: iizvv
     * description: 删除关联信息
     * create time: 2019-09-04 21:03
     *

     * @return int
     */
    public int deleteDPByIds(long deviceId, long packageId) {
        return dpDao.deleteDPByIds(deviceId, packageId);
    }

}
