package online.iizvv.core.config;

public class Config {

    /** 账号最大可容纳设备量 */
    public final static int total = 100;

    /** 安装证书时显示的标题 */
    public final static String payloadDisplayName = "此文件仅用作获取设备UDID";

    /** 安装证书时显示的描述 */
    public final static String payloadDescription = "该配置文件帮助用户进行APP授权安装！";

    /** 后台请求地址域名 例：https://api.test.com */
    public final static String apiHost = "https://api.iizvv.online";

    /** 组织名称 */
    public final static String payloadOrganization = "Apple Inc.";

    /** access key */
    public final static String accessKeyID = "LTAIsQ3I75GkpTYd";

    /** secret key */
    public final static String accessKeySecret = "Y8a8q3FCnA0lQqeQh22OuciDCNw88C";


    /** 主内容 Bucket 域名 例：https://ipa1.oss-cn-zhangjiakou.aliyuncs.com */
    public final static String aliMainHost = "https://file.iizvv.online";

    /** 主内容空间名 */
    public final static String aliMainBucket = "auto-main-file";


    /** 临时内容 Bucket 域名 例：https://ipa2.oss-cn-zhangjiakou.aliyuncs.com*/
    public final static String aliTempHost = "https://auto-ipa2.oss-cn-zhangjiakou.aliyuncs.com";

    /** 临时内容空间名 */
    public final static String aliTempBucket = "auto-ipa2";


    /** 内网地域节点 例：https://oss-cn-zhangjiakou-internal.aliyuncs.com */
    public final static String vpcEndpoint = "https://oss-cn-zhangjiakou-internal.aliyuncs.com";

    /** 内网主内容 Bucket 域名 例：https://ipa1.oss-cn-zhangjiakou-internal.aliyuncs.com*/
    public final static String vpcAliMainHost = "https://auto-main-file.oss-cn-zhangjiakou-internal.aliyuncs.com";

    /** 前端h5域名 例：https://www.test.com */
    public final static String h5Host = "https://auto.iizvv.online";


    /** 请求头键 */
    public final static String Authorization = "Authorization";

    /** 加密key */
    public final static String aesKey = "$%6AA^123*^%@!@$";

}