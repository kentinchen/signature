package online.iizvv.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(value = "安装包对象")
public class Package {

  @ApiModelProperty(value = "id")
  private long id;

  @ApiModelProperty(value = "包名")
  private String name;

  @ApiModelProperty(value = "图标")
  private String icon;

  @ApiModelProperty(value = "预览图")
  private String imgs;

  @ApiModelProperty(value = "用户id")
  private long userId;

  @ApiModelProperty(value = "版本")
  private String version;

  @ApiModelProperty(value = "安装包id")
  private String bundleIdentifier;

  @ApiModelProperty(value = "下载地址")
  private String link;

  @ApiModelProperty(value = "获取UDID证书地址")
  private String mobileconfig;

  @ApiModelProperty(value = "下载次数")
  private long downloadCount;

  @ApiModelProperty(value = "已使用设备量")
  private long useDevice;

  @ApiModelProperty(value = "总可用设备量")
  private long totalDevice;

  @ApiModelProperty(value = "编译版本号")
  private String buildVersion;

  @ApiModelProperty(value = "最小支持版本")
  private String miniVersion;

  @ApiModelProperty(value = "副标题")
  private String subTitle;

  @ApiModelProperty(value = "星级")
  private float level;

  @ApiModelProperty(value = "评分数量")
  private String commentCount;

  @ApiModelProperty(value = "排行")
  private int ranking;

  @ApiModelProperty(value = "分类名称")
  private String className;

  @ApiModelProperty(value = "适用年龄")
  private int age;

  @ApiModelProperty(value = "文件大小")
  private String size;

  @ApiModelProperty(value = "简介")
  private String summary;

  @ApiModelProperty(value = "是否限制使用")
  private boolean isStint;

  @ApiModelProperty(value = "配置文件组织名")
  private String organization;

  @ApiModelProperty(value = "配置文件标题")
  private String display;

  @ApiModelProperty(value = "配置文件描述")
  private String description;

  @ApiModelProperty(value = "创建时间")
  private Date createTime;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public String getImgs() {
    return imgs;
  }

  public void setImgs(String imgs) {
    this.imgs = imgs;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getBundleIdentifier() {
    return bundleIdentifier;
  }

  public void setBundleIdentifier(String bundleIdentifier) {
    this.bundleIdentifier = bundleIdentifier;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getMobileconfig() {
    return mobileconfig;
  }

  public void setMobileconfig(String mobileconfig) {
    this.mobileconfig = mobileconfig;
  }

  public long getDownloadCount() {
    return downloadCount;
  }

  public void setDownloadCount(long downloadCount) {
    this.downloadCount = downloadCount;
  }

  public long getUseDevice() {
    return useDevice;
  }

  public void setUseDevice(long useDevice) {
    this.useDevice = useDevice;
  }

  public long getTotalDevice() {
    return totalDevice;
  }

  public void setTotalDevice(long totalDevice) {
    this.totalDevice = totalDevice;
  }

  public String getBuildVersion() {
    return buildVersion;
  }

  public void setBuildVersion(String buildVersion) {
    this.buildVersion = buildVersion;
  }

  public String getMiniVersion() {
    return miniVersion;
  }

  public void setMiniVersion(String miniVersion) {
    this.miniVersion = miniVersion;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public boolean isStint() {
    return isStint;
  }

  public void setStint(boolean stint) {
    isStint = stint;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getSubTitle() {
    return subTitle;
  }

  public void setSubTitle(String subTitle) {
    this.subTitle = subTitle;
  }

  public float getLevel() {
    return level;
  }

  public void setLevel(float level) {
    this.level = level;
  }

  public String getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(String commentCount) {
    this.commentCount = commentCount;
  }

  public int getRanking() {
    return ranking;
  }

  public void setRanking(int ranking) {
    this.ranking = ranking;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public String getDisplay() {
    return display;
  }

  public void setDisplay(String display) {
    this.display = display;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  @Override
  public String toString() {
    return "Package{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", icon='" + icon + '\'' +
            ", imgs='" + imgs + '\'' +
            ", userId=" + userId +
            ", version='" + version + '\'' +
            ", bundleIdentifier='" + bundleIdentifier + '\'' +
            ", link='" + link + '\'' +
            ", mobileconfig='" + mobileconfig + '\'' +
            ", downloadCount=" + downloadCount +
            ", useDevice=" + useDevice +
            ", totalDevice=" + totalDevice +
            ", buildVersion='" + buildVersion + '\'' +
            ", miniVersion='" + miniVersion + '\'' +
            ", subTitle='" + subTitle + '\'' +
            ", level=" + level +
            ", commentCount='" + commentCount + '\'' +
            ", ranking=" + ranking +
            ", className='" + className + '\'' +
            ", age=" + age +
            ", size='" + size + '\'' +
            ", summary='" + summary + '\'' +
            ", isStint=" + isStint +
            ", organization='" + organization + '\'' +
            ", display='" + display + '\'' +
            ", description='" + description + '\'' +
            ", createTime=" + createTime +
            '}';
  }
}
