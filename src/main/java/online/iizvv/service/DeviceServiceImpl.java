package online.iizvv.service;

import online.iizvv.dao.DeviceDao;
import online.iizvv.pojo.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：iizvv
 * @date ：Created in 2019-06-26 21:22
 * @description：设备管理
 * @modified By：
 * @version: 1.0
 */
@Service
public class DeviceServiceImpl {

    @Autowired
    private DeviceDao deviceDao;

    /**
     * create by: iizvv
     * description: 设置设备是否可用
     * create time: 2019-09-04 21:35
     *

     * @return boolean
     */
    public boolean updateDeviceIsUseByAppleId(long appleId, boolean isUse){
        return deviceDao.updateDeviceIsUseByAppleId(appleId, isUse)>0;
    }

    public Device getDeviceByUDID(String udid) {
        return deviceDao.getDeviceByUDID(udid);
    }

    public List<Device> getAllByAppleId(long id) {
        return deviceDao.getAllByAppleId(id);
    }

    public boolean deleteByAppleId(long id) {
        return deviceDao.deleteByAppleId(id)>0;
    }

    public boolean insertDevice(String udid, Long appleId, String  deviceId) {
        return deviceDao.insertDevice(udid, appleId, deviceId)>0;
    }

}
