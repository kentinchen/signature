package online.iizvv.controls;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.dd.plist.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import online.iizvv.core.config.Config;
import online.iizvv.core.pojo.Result;
import online.iizvv.pojo.Apple;
import online.iizvv.pojo.Authorize;
import online.iizvv.pojo.Device;
import online.iizvv.pojo.Package;
import online.iizvv.server.WebSocketServer;
import online.iizvv.service.AppleServiceImpl;
import online.iizvv.service.DPServiceImpl;
import online.iizvv.service.DeviceServiceImpl;
import online.iizvv.service.PackageServiceImpl;
import online.iizvv.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.InvalidKeyException;

/**
 * @author ：iizvv
 * @date ：Created in 2019-06-26 20:39
 * @description：获取UDID
 * @modified By：
 * @version: 1.0
 */
@RestController
@Api(tags = {"获取UDID下载APP"})
@RequestMapping("/udid")
public class UDIDController {

    @Autowired
    private DeviceServiceImpl deviceService;

    @Autowired
    private AppleServiceImpl appleService;

    @Autowired
    private PackageServiceImpl packageService;

    @Autowired
    private DPServiceImpl dpService;

    @Autowired
    private FileManager fileManager;


    @ApiOperation(value="/getUDID", notes="获取设备udid", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "IPA id", required = true),
    })
    @PostMapping("/getUDID")
    public void getUDID(HttpServletResponse response, HttpServletRequest request, String encryptHex) throws UnsupportedEncodingException {
        response.setContentType("text/html;charset=UTF-8");
        String ua = request.getHeader(Config.ua);
        System.out.println("当前时间: " + DateUtil.now() + "\n当前用户User-Agent: " + ua);
        String udid = null;
        try {
            request.setCharacterEncoding("UTF-8");
            // 获取HTTP请求的输入流
            InputStream is = request.getInputStream();
            // 已HTTP请求输入流建立一个BufferedReader对象
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            StringBuilder sb = new StringBuilder();
            // 读取HTTP请求内容
            String buffer = null;
            while ((buffer = br.readLine()) != null) {
                sb.append(buffer);
            }
            String xml = sb.toString().substring(sb.toString().indexOf("<?xml"), sb.toString().indexOf("</plist>")+8);
            NSDictionary parse = (NSDictionary) PropertyListParser.parse(xml.getBytes());
            udid = (String) parse.get("UDID").toJavaObject();
            System.out.println("当前设备udid: " + udid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String redirect = Config.h5Host + "/app/" +
                encryptHex + "?encryptHex=" +
                AESUtils.encryptHex(udid + "/"+ AESUtils.decryptStr(encryptHex));
        System.out.println("开始重定向至h5: " + redirect);
        response.setHeader("Location", redirect);
        response.setStatus(301);
    }

    @ApiOperation(value="/getSignatureStatus", notes="获取当前IPA签名状态", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "encryptHex", value = "encryptHex", required = true),
    })
    @GetMapping("/getSignatureStatus")
    public Result getSignatureStatus(String encryptHex) {
        String hexStr = AESUtils.decryptHexStr(encryptHex);
        String[] split = hexStr.split("/");
        return calculate(split[0], Integer.valueOf(split[1]));
    }

    /**
     * create by: iizvv
     * description: 重定向后的耗时操作
     * create time: 2019-09-03 19:02
     *

     * @return void
     */
    Result calculate(String udid, long id) {
        Result result = new Result();
        long begin = System.currentTimeMillis();
        System.out.println("开始签名操作");
        String itemService = analyzeUDID(udid, id);
        System.out.println("itemService文件名为: " + itemService);
        if (itemService != null) {
            result.setCode(0);
            if (itemService.equalsIgnoreCase("1")) {
                System.out.println("没有找到合适的账号");
                result.setMsg("当前已无可使用帐号");
            }else if (itemService.equalsIgnoreCase("2")) {
                System.out.println("当前ipa已无下载次数");
                result.setMsg("当前ipa已无下载次数, 请联系管理员修改");
            }else if (itemService.equalsIgnoreCase("3")) {
                System.out.println("未找到ipa文件");
                result.setMsg("未找到ipa文件");
            }else {
                result.setCode(1);
                result.setMsg("签名成功");
                result.setData("itms-services://?action=download-manifest&url=" + Config.aliTempHost + "/" + itemService);
                packageService.updatePackageDownloadCountById(id);
            }
        }else {
            System.out.println("签名失败");
            result.setMsg("签名失败");
        }
        long end = System.currentTimeMillis();
        long time = (end - begin)/1000;
        System.out.println("自动签名执行耗时: " + time + "秒");
        System.out.println("所有操作已完成");
        return result;
    }


    /**
     * create by: iizvv
     * description: 分析UDID信息
     * create time: 2019-06-26 21:40
     *

     * @return void
     */
    String analyzeUDID(String udid, long id) {
        String itemService = null;
        System.out.println("获取ipa信息");
        Package pck = packageService.getPackageById(id);
        if (pck!=null) {
            // 判断当前ipa是否还有可继续下载
            if (pck.getUseDevice()<pck.getTotalDevice()) {
                System.out.println("开始寻找可用帐号");
                Device device = deviceService.getDeviceByUDID(udid);
                // 设备不存在于任何帐号下，或设备所处帐号已无法使用
                if (device==null) {
                    itemService = getItemService(id, udid, pck);
                }else {
                    System.out.println("设备存在， 开始获取帐号信息");
                    // 设备存在
                    Apple apple = appleService.getAppleAccountById(device.getAppleId());
                    System.out.println("帐号信息获取成功: " + apple.toString());
                    String resignature = resignature(apple, device, pck.getLink());
                    if (resignature != null && !resignature.equalsIgnoreCase(Config.errors)) {
                        itemService = software(resignature, pck.getBundleIdentifier(), pck.getVersion(), pck.getName());
                        if (dpService.getDPByIds(device.getId(), id) != null) {
                            System.out.println("此设备已下载过此应用, 当前次不消耗设备量");
                        }else {
                            System.out.println("此设备未下载过此应用, 当前次消耗设备量");
                            System.out.println("device_id: " + device.toString() + ", package_id: " + id);
                            dpService.insertDP(device.getId(), id);
                            packageService.updatePackageDeviceCountById(id);
                        }
                    }else {
                        itemService = getItemService(id, udid, pck);
                    }
                }
            }else {
                itemService = "2";
            }
        }else {
            itemService = "3";
        }
        return itemService;
    }

    /**
      * create by: iizvv
      * description: 获取最终执行结果
      * create time: 2019-09-25 13:22

      * @return itemService
      */
    String getItemService(long id, String udid, Package pck) {
        String itemService;
        System.out.println("设备不存在, 或设备所处帐号已无法使用, 寻找新的可用账号");
        Apple apple = appleService.getBeUsableAppleAccount();
        // 没有找到合适的帐号
        if (apple==null) {
            System.out.println("没有找到合适的帐号");
            itemService = "1";
        }else {
            System.out.println("找到合适帐号， 开始添加设备");
            // 找到合适的帐号
            String resignature = insertDevice(id, udid, apple, pck.getLink());
            if (resignature == null) {
                itemService = "1";
            }else {
                packageService.updatePackageDeviceCountById(id);
                itemService = software(resignature, pck.getBundleIdentifier(), pck.getVersion(), pck.getName());
            }
        }
        return itemService;
    }

    /**
     * create by: iizvv
     * description: 添加设备
     * create time: 2019-06-26 23:11
     *

     * @return String
     */
    String insertDevice(long id, String udid, Apple apple, String link) {
        // 发现可用账号
        String key = null;
        String devId = null;
        System.out.println("开始添加设备");
        try {
            devId = ITSUtils.insertDevice(udid, new Authorize(apple.getP8(), apple.getIss(), apple.getKid()));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        if (devId!=null && !devId.equalsIgnoreCase(Config.errors)) {
            boolean b = deviceService.insertDevice(udid, apple.getId(), devId);
            appleService.updateDevicesCount(apple.getId());
            if (b) {
                Device device = deviceService.getDeviceByUDID(udid);
                dpService.insertDP(device.getId(), id);
                key = resignature(apple, device, link);
            }
        }
        if (devId == null ||
                devId.equalsIgnoreCase(Config.errors) ||
                key == null ||
                key.equalsIgnoreCase(Config.errors)) {
            System.out.println("帐号不可用, 继续寻找可用帐号");
            boolean b = appleService.updateAppleIsUse(apple.getId(), false);
            deviceService.updateDeviceIsUseByAppleId(apple.getId(), false);
            if (b) {
                System.out.println("已将账号: " + apple.getAccount() + ", 以及账号下设备标记为不可用");
            }else {
                System.out.println("账号: " + apple.getAccount() + "标记失败");
            }
            System.out.println("开始寻找新的账号");
            apple = appleService.getBeUsableAppleAccount();
            if (apple != null) {
                key = insertDevice(id, udid, apple, link);
            }
        }
        if (apple == null) {
            return null;
        }
        return key;
    }

    /**
      * create by: iizvv
      * description: 重签名
      * create time: 2019-07-06 08:42

      * @return String
      */
    String resignature(Apple apple, Device device, String appLink) {
        String key = Config.errors;
        // ResourceUtils.getURL("classpath:").getPath()
//        String classPath = "/root/";
        long begin = System.currentTimeMillis();
        File mobileprovision = null;
        try {
            System.out.println("开始创建签名证书");
            String profile = ITSUtils.insertProfile(apple, device.getDeviceId());
            System.out.println("证书内容为: " + profile);
            if (profile != null && !profile.equalsIgnoreCase(Config.errors)) {
                System.out.println("证书创建成功");
                mobileprovision = fileManager.base64ToFile(profile,
                        Config.rootPath + IdUtil.simpleUUID() + ".mobileprovision");
            }
        } catch (InvalidKeyException e) {
            System.out.println("签名证书创建失败");
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        long time = (end - begin)/1000;
        System.out.println("创建证书耗时: " + time + "秒");
        if (mobileprovision!=null && mobileprovision.exists()) {
            String command = null;
            System.out.println("文件创建成功");
            String appUrl = Config.vpcAliMainHost + "/" + appLink;
            HttpUtil.downloadFile(appUrl, Config.rootPath);
            System.out.println("ipa下载完成: " + appUrl);
            File app = new File(Config.rootPath+appLink);
            String p12Url = Config.vpcAliMainHost + "/" + apple.getP12();
            HttpUtil.downloadFile(p12Url, Config.rootPath);
            System.out.println("p12下载完成: " + p12Url);
            File p12 = new File(Config.rootPath+apple.getP12());
            // 调用本地shell脚本并传递必须参数
//                command = "/root/ausign.sh " + app.getAbsolutePath() + " " +
//                        p12.getAbsolutePath() + " " +
//                        mobileprovision.getAbsolutePath();
            // 最终下载的ipa文件
            File file = new File(Config.rootPath + IdUtil.simpleUUID() + ".ipa");
            command = Config.rootPath + "zsign.sh " +
                    p12.getAbsolutePath() + " " +
                    mobileprovision.getAbsolutePath() + " " +
                    file.getAbsolutePath() + " " +
                    app.getAbsolutePath();
            System.out.println("调用shell进行签名: " + command);
            try {
                begin = System.currentTimeMillis();
                boolean result = Shell.run(command);
                end = System.currentTimeMillis();
                time = (end - begin)/1000;
                System.out.println("签名脚本执行耗时: " + time + "秒");
                if (result) {
                    key = uploadIPA(file);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                mobileprovision.delete();
                app.delete();
                p12.delete();
                file.delete();
            }
        }
        return key;
    }

    /**
     * create by: iizvv
     * description: 上传IPA
     * create time: 2019-07-05 13:11

     * @return void
     */
    String uploadIPA(File file) {
        String objName = UUID.randomUUID().toString().replace("-", "")+".ipa";
        System.out.println("开始上传最终ipa文件, 文件名: " + objName);
        fileManager.uploadFile(file, objName);
        System.out.println("文件上传完成");
        file.delete();
        System.out.println("ipa文件删除： " + objName);
        return objName;
    }

    /**
      * create by: iizvv
      * description: 下载IPA的item-service
      * create time: 2019-07-06 10:34

      * @return
      */
    String software(String ipaUrl, String id, String version, String title) {
        ipaUrl = Config.aliTempHost + "/" + ipaUrl;
        System.out.println("ipaUrl: " + ipaUrl);
        String plist = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>  \n" +
                "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">  \n" +
                "<plist version=\"1.0\">  \n" +
                "<dict>  \n" +
                "    <key>items</key>  \n" +
                "    <array>  \n" +
                "        <dict>  \n" +
                "            <key>assets</key>  \n" +
                "            <array>  \n" +
                "                <dict>  \n" +
                "                    <key>kind</key>  \n" +
                "                    <string>software-package</string>  \n" +
                "                    <key>url</key>  \n" +
                "                    <string>"+ ipaUrl +"</string>  \n" +
                "                </dict>  \n" +
                "            </array>  \n" +
                "            <key>metadata</key>  \n" +
                "            <dict>  \n" +
                "                <key>bundle-identifier</key>  \n" +
                "                <string>"+ id +"</string>  \n" +
                "                <key>bundle-version</key>  \n" +
                "                <string>" + version + "</string>  \n" +
                "                <key>kind</key>  \n" +
                "                <string>software</string>  \n" +
                "                <key>title</key>  \n" +
                "                <string>" + title + "</string>  \n" +
                "            </dict>  \n" +
                "        </dict>  \n" +
                "    </array>  \n" +
                "</dict>  \n" +
                "</plist> ";
        String filePath = "itemService_" + IdUtil.simpleUUID() +".plist";
        FileWriter writer = new FileWriter(filePath);
        writer.write(plist);
        String itemService = uploadItemService(writer.getFile());
        writer.getFile().delete();
        return itemService;
    }

    /**
     * create by: iizvv
     * description: 上传itemService
     * create time: 2019-07-04 11:18

     * @return plist名称
     */
    String uploadItemService(File file) {
        String objName = IdUtil.simpleUUID()+".plist";
        fileManager.uploadFile(file, objName);
        return objName;
    }

    /**
      * create by: iizvv
      * description: 签名完成
      * create time: 2019-09-03 17:46

      * @return void
      */
    void pushToWebSocket(@PathVariable String socketId, Result result) {
        try {
            WebSocketServer.sendInfo(JSON.toJSONString(result),socketId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
