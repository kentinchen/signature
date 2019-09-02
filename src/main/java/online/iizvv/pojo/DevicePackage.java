package online.iizvv.pojo;


import io.swagger.annotations.ApiModelProperty;

public class DevicePackage {

  @ApiModelProperty(value = "id")
  private long id;

  @ApiModelProperty(value = "设备id")
  private long deviceId;

  @ApiModelProperty(value = "ipaId")
  private long packageId;

  @ApiModelProperty(value = "创建时间")
  private long createTime;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(long deviceId) {
    this.deviceId = deviceId;
  }

  public long getPackageId() {
    return packageId;
  }

  public void setPackageId(long packageId) {
    this.packageId = packageId;
  }

  public long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }

  @Override
  public String toString() {
    return "DevicePackage{" +
            "id=" + id +
            ", deviceId=" + deviceId +
            ", packageId=" + packageId +
            ", createTime=" + createTime +
            '}';
  }
}
