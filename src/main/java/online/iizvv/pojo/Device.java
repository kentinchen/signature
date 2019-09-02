package online.iizvv.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "设备对象")
public class Device {

  @ApiModelProperty(value = "id")
  private long id;

  @ApiModelProperty(value = "设备udid")
  private String udid;

  @ApiModelProperty(value = "绑定的开发者账号")
  private long appleId;

  @ApiModelProperty(value = "设备在开发者后台id")
  private String deviceId;

  @ApiModelProperty(value = "当前设备是否可用")
  private boolean isUse;

  @ApiModelProperty(value = "创建时间")
  private long createTime;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUdid() {
    return udid;
  }

  public void setUdid(String udid) {
    this.udid = udid;
  }

  public long getAppleId() {
    return appleId;
  }

  public void setAppleId(long appleId) {
    this.appleId = appleId;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public boolean isUse() {
    return isUse;
  }

  public void setUse(boolean use) {
    isUse = use;
  }

  public long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }

  @Override
  public String toString() {
    return "Device{" +
            "id=" + id +
            ", udid='" + udid + '\'' +
            ", appleId=" + appleId +
            ", deviceId='" + deviceId + '\'' +
            ", isUse=" + isUse +
            ", createTime=" + createTime +
            '}';
  }
}
