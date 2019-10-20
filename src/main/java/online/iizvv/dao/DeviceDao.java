package online.iizvv.dao;

import online.iizvv.pojo.Device;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author ：iizvv
 * @date ：Created in 2019-06-26 21:23
 * @description：Device
 * @modified By：
 * @version: 1.0
 */
@Mapper
public interface DeviceDao {

    /**
     * create by: iizvv
     * description: 设置设备是否可用
     * create time: 2019-09-04 21:35
     *

     * @return int
     */
    @Update("UPDATE device SET is_use = #{isUse} WHERE apple_id = #{appleId}")
    int updateDeviceIsUseByAppleId(long appleId, boolean isUse);

    /**
     * create by: iizvv
     * description: 设置所有设备为可用
     * create time: 2019/10/20 4:49 下午
     * 
     
     * @return 
     */
    @Update("UPDATE device SET is_use = true WHERE is_use = false")
    int updateAllDeviceUse();
    
    /**
     * create by: iizvv
     * description: 获取设备
     * create time: 2019-06-26 21:32
     *

     * @return Device
     */
    @Select("SELECT * FROM device WHERE udid = #{udid} AND is_use = true LIMIT 1")
    Device getDeviceByUDID(String udid);

    /**
     * create by: iizvv
     * description: 获取帐号下的所有设备
     * create time: 2019-08-19 21:49
     *

     * @return List
     */
    @Select("SELECT * FROM device WHERE apple_id=#{id}")
    List<Device> getAllByAppleId(long id);

    /**
     * create by: iizvv
     * description: 删除帐号下的所有设备
     * create time: 2019-08-20 06:22
     *

     * @return
     */
    @Delete("DELETE FROM device WHERE apple_id = #{id}")
    int deleteByAppleId(long id);

    /**
     * create by: iizvv
     * description: 添加设备到帐号
     * create time: 2019-06-29 13:50
     *

     * @return 是否添加成功
     */
    @Insert("INSERT INTO device (udid, apple_id, device_id) VALUES (#{udid}, #{appleId}, #{deviceId})")
    int insertDevice(String udid, Long appleId, String  deviceId);

}
