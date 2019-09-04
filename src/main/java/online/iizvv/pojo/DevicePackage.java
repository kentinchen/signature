package online.iizvv.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(value = "设备与IPA关联表")
public class DevicePackage {

  @ApiModelProperty(value = "id")
  private long id;
  @ApiModelProperty(value = "id")
  private long packageId;
  @ApiModelProperty(value = "id")
  private long deviceId;
  @ApiModelProperty(value = "id")
  private Date createTime;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getPackageId() {
    return packageId;
  }

  public void setPackageId(long packageId) {
    this.packageId = packageId;
  }


  public long getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(long deviceId) {
    this.deviceId = deviceId;
  }


  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @Override
  public String toString() {
    return "DevicePackage{" +
            "id=" + id +
            ", packageId=" + packageId +
            ", deviceId=" + deviceId +
            ", createTime=" + createTime +
            '}';
  }
}
