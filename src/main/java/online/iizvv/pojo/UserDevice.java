package online.iizvv.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(value = "用户、设备关联对象")
public class UserDevice {

  @ApiModelProperty(value = "id")
  private long id;
  @ApiModelProperty(value = "用户id")
  private long userId;
  @ApiModelProperty(value = "deviceId")
  private long deviceId;
  @ApiModelProperty(value = "isUse")
  private boolean isUse;
  @ApiModelProperty(value = "createTime")
  private Date createTime;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }


  public long getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(long deviceId) {
    this.deviceId = deviceId;
  }


  public boolean isUse() {
    return isUse;
  }

  public void setUse(boolean use) {
    isUse = use;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @Override
  public String toString() {
    return "UserDevice{" +
            "id=" + id +
            ", userId=" + userId +
            ", deviceId=" + deviceId +
            ", isUse=" + isUse +
            ", createTime=" + createTime +
            '}';
  }
}
