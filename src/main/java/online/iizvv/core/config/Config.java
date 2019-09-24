package online.iizvv.core.config;

public class Config {

    /***********************************选填******************************/

    /** 组织名称 */
    public final static String payloadOrganization = "Apple Inc.";

    /** 安装证书时显示的标题 */
    public final static String payloadDisplayName = "此文件仅用作获取设备UDID";

    /** 安装证书时显示的描述 */
    public final static String payloadDescription = "该配置文件帮助用户进行APP授权安装！";

    /*********************************************************************/


    /***********************************必填******************************/

    /** 后台请求地址域名 例：https://api.test.com */
    public final static String apiHost = "https://";

    /** 前端h5域名 例：https://www.test.com */
    public final static String h5Host = "https://";


    /** access key */
    public final static String accessKeyID = "";

    /** secret key */
    public final static String accessKeySecret = "";


    /** 主内容 Bucket 域名 例：https://ipa1.oss-cn-zhangjiakou.aliyuncs.com */
    public final static String aliMainHost = "https://";

    /** 主内容空间名 */
    public final static String aliMainBucket = "";


    /** 临时内容 Bucket 域名 例：https://ipa2.oss-cn-zhangjiakou.aliyuncs.com */
    public final static String aliTempHost = "https://";

    /** 临时内容空间名 */
    public final static String aliTempBucket = "";


    /** 内网地域节点 例：https://oss-cn-zhangjiakou-internal.aliyuncs.com */
    public final static String vpcEndpoint = "https://";

    /** 内网主内容 Bucket 域名 例：https://ipa1.oss-cn-zhangjiakou-internal.aliyuncs.com */
    public final static String vpcAliMainHost = "https://";

    /*********************************************************************/




    /**  以下信息无需修改 */
    /** 当前版本 */
    public final static String version = "3.0";

    /** 账号最大可容纳设备量 */
    public final static int total = 100;

    /** 请求头键 */
    public final static String Authorization = "Authorization";

    /** 加解密ipaId时使用的key */
    public final static String aesKey = "$%6AA^123*^%@!@$";

}
