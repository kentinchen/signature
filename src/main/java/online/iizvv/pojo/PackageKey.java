package online.iizvv.pojo;


import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class PackageKey {

  @ApiModelProperty(value = "id")
  private long id;
  @ApiModelProperty(value = "密钥")
  private String key;
  @ApiModelProperty(value = "是否可使用")
  private boolean isUse;
  @ApiModelProperty(value = "packageId")
  private long packageId;
  @ApiModelProperty(value = "创建时间")
  private Date createTime;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public boolean isUse() {
    return isUse;
  }

  public void setUse(boolean use) {
    isUse = use;
  }

  public long getPackageId() {
    return packageId;
  }

  public void setPackageId(long packageId) {
    this.packageId = packageId;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @Override
  public String toString() {
    return "PackageKey{" +
            "id=" + id +
            ", key='" + key + '\'' +
            ", isUse=" + isUse +
            ", packageId=" + packageId +
            ", createTime=" + createTime +
            '}';
  }
}
