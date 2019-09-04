package online.iizvv.controls;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ZipUtil;
import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListFormatException;
import com.dd.plist.PropertyListParser;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import online.iizvv.pojo.Package;
import online.iizvv.core.pojo.Result;
import online.iizvv.service.PackageServiceImpl;
import online.iizvv.core.config.Config;
import online.iizvv.utils.AESUtils;
import online.iizvv.utils.FileManager;
import online.iizvv.utils.JwtHelper;
import online.iizvv.utils.Shell;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ：iizvv
 * @date ：Created in 2019-06-28 15:08
 * @description：IPA包管理
 * @version: 1.0
 */
@RestController
@RequestMapping("/package")
@Api(tags = {"IPA管理"})
public class PackageController {

    @Autowired
    private PackageServiceImpl packageService;

    @Autowired
    private FileManager fileManager;

    @ApiOperation(value = "/insertPackage", notes = "上传ipa", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "file", value = "ipa文件", required = true),
            @ApiImplicitParam(name = "totalCount", value = "可下载次数"),
    })
    @PostMapping("/insertPackage")
    public Result insertPackage(HttpServletRequest request, MultipartFile file) {
        Result result = databaseWithIPA(request, file, 0);
        return result;
    }


    @ApiOperation(value = "/updatePackageById", notes = "更新ipa", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "file", value = "ipa文件", required = true),
            @ApiImplicitParam(name = "id", value = "ipaId"),
    })
    @PostMapping("/updatePackageById")
    public Result updatePackageById(HttpServletRequest request, MultipartFile file, long id) {
        Result result = databaseWithIPA(request, file, id);
        return result;
    }

    @ApiOperation(value = "/updatePackageSummaryById", notes = "更新简介")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "ipaId", required = true),
            @ApiImplicitParam(name = "summary", value = "简介内容"),
    })
    @PostMapping("/updatePackageSummaryById")
    public Result updatePackageSummaryById(long id, String summary) {
        Result result = new Result();
        boolean b = packageService.updatePackageSummaryById(id, summary);
        if (b) {
            result.setCode(1);
            result.setMsg("简介更新成功");
        }else {
            result.setMsg("简介更新失败");
        }
        return result;
    }

//    @ApiOperation(value = "/updatePackageImgsById", notes = "更新预览图")
//    @ApiImplicitParams(value = {
//            @ApiImplicitParam(name = "id", value = "ipaId", required = true),
//            @ApiImplicitParam(name = "file", value = "图片文件"),
//    })
//    @PostMapping("/updatePackageImgsById")
//    public Result updatePackageImgsById(long id, @RequestParam("file")MultipartFile[] file) {
//        Result result = new Result();
//        if (file.length > 10) {
//            result.setMsg("图片最多不能超过10张");
//        }else {
//            List <String>list = new LinkedList();
//            for (MultipartFile multipartFile : file) {
//                if (multipartFile.getContentType().equalsIgnoreCase("image/png") ||
//                multipartFile.getContentType().equalsIgnoreCase("image/jpeg")) {
//                    System.out.println("开始上传文件: " + multipartFile.getOriginalFilename() +
//                            ", 文件类型: " + multipartFile.getContentType());
//                    try {
//                        String imgName = IdUtil.simpleUUID();
//                        BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
//                        if (bufferedImage!=null){
//                            Integer width = bufferedImage.getWidth();
//                            Integer height = bufferedImage.getHeight();
//                            imgName+="("+width+"A"+height+")";
//                        }
//                        imgName+=".jpeg";
//                        fileManager.uploadFile(multipartFile.getBytes(), imgName, false);
//                        list.add(imgName);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            boolean b = packageService.updatePackageImgsById(id, StringUtils.join(list, ","));
//            if (b) {
//                result.setCode(1);
//                result.setMsg("预览图上传成功");
//            }else {
//                result.setMsg("预览图上传失败");
//            }
//        }
//        return result;
//    }

    @ApiOperation(value = "/updatePackageImgsById", notes = "更新预览图")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "ipaId", required = true),
            @ApiImplicitParam(name = "file", value = "图片文件"),
    })
    @PostMapping("/updatePackageImgsById")
    public Result updatePackageImgsById(long id, String imgs) {
        Result result = new Result();
        if (imgs==null || imgs.isEmpty()) {
            imgs = "";
        }
        imgs = imgs.trim();
        boolean b = packageService.updatePackageImgsById(id, imgs.replace(Config.aliMainHost + "/", ""));
        if (b) {
            result.setCode(1);
            result.setMsg("预览图上传成功");
        }else {
            result.setMsg("预览图上传失败");
        }
        return result;
    }

    @ApiOperation(value = "/getAllPackage", notes = "获取全部IPA")
    @GetMapping("/getAllPackage")
    public Result<List<Package>> getAllPackage(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        System.out.println("当前用户User-Agent: " + ua );
        String authorization = request.getHeader(Config.Authorization);
        Claims claims = JwtHelper.verifyJwt(authorization);
        long level = (Integer)claims.get("level");
        List<Package> allPackage = null;
        if (level == 1) {
            allPackage = packageService.getAllPackage();
        }else {
            allPackage = packageService.getAllPackageByUserId((Integer)claims.get("userId"));
        }
        Result result = new Result();
        for (Package aPackage : allPackage) {
            aPackage.setIcon(Config.aliMainHost + "/" + aPackage.getIcon());
        }
        result.setMsg("数据获取成功");
        result.setData(allPackage);
        result.setCode(1);
        return result;
    }

    @ApiOperation(value = "/getPackageById", notes = "获取指定ipa")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "ipaId", required = true)
    })
    @GetMapping("/getPackageById")
    public Result<Package> getPackageById(long id) {
        Result result = new Result();
        Package pck = packageService.getPackageById(id);
        if (pck==null) {
            result.setMsg("内容不存在");
        }else {
            pck.setIcon(Config.aliMainHost + "/" + pck.getIcon());
            if (pck.getImgs()!=null) {
                List imgs = new LinkedList();
                for (String s : pck.getImgs().split(",")) {
                    imgs.add(Config.aliMainHost + "/" + s);
                }
                pck.setImgs(StringUtils.join(imgs, ","));
            }
            pck.setLink(Config.redirect +"/app/" + AESUtils.encryptHex(String.valueOf(id)));
            pck.setMobileconfig(Config.aliMainHost + "/" + pck.getMobileconfig());
            result.setCode(1);
            result.setMsg("获取成功");
            result.setData(pck);
        }
        return result;
    }

    @ApiOperation(value = "/getPackageH5ById", notes = "H5获取指定ipa")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "ipaId", required = true)
    })
    @GetMapping("/getPackageH5ById")
    public Result<Package> getPackageH5ById(String id) {
        Result result = new Result();
        long i = AESUtils.decryptStr(id);
        Package pck = packageService.getPackageById(i);
        if (pck==null) {
            result.setMsg("内容不存在");
        }else {
            pck.setId(0);
            pck.setIcon(Config.aliMainHost + "/" + pck.getIcon());
            if (pck.getImgs()!=null) {
                List imgs = new LinkedList();
                for (String s : pck.getImgs().split(",")) {
                    imgs.add(Config.aliMainHost + "/" + s);
                }
                pck.setImgs(StringUtils.join(imgs, ","));
            }
            pck.setMobileconfig(Config.aliMainHost + "/" + pck.getMobileconfig());
            result.setCode(1);
            result.setMsg("获取成功");
            result.setData(pck);
        }
        return result;
    }

    @ApiOperation(value = "/updatePackageTotalDeviceById", notes = "更新ipa可使用设备量")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "ipaId", required = true),
            @ApiImplicitParam(name = "count", value = "总可用设备量", required = true)
    })
    @PostMapping("/updatePackageTotalDeviceById")
    public Result updatePackageTotalDeviceById(HttpServletRequest request, long id, long count) {
        Result result = new Result();
        String authorization = request.getHeader(Config.Authorization);
        Claims claims = JwtHelper.verifyJwt(authorization);
        long level = (Integer)claims.get("level");
        if (level == 1) {
            boolean b = packageService.updatePackageTotalDeviceById(id, count);
            if (b) {
                result.setCode(1);
                result.setMsg("更新成功");
            }else {
                result.setMsg("更新失败");
            }
        }else {
            result.setMsg("权限不足， 请联系管理员修改");
        }
        return result;
    }


    @ApiOperation(value = "/deletePackageById", notes = "删除指定ipa")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "ipaId", required = true)
    })
    @PostMapping("/deletePackageById")
    public Result deleteById(long id) {
        Result result = new Result();
        boolean b = packageService.deletePackageById(id);
        if (b) {
            result.setCode(1);
            result.setMsg("ipa文件删除成功");
        }else {
            result.setMsg("ipa文件删除失败");
        }
        return result;
    }

    /**
     * create by: iizvv
     * description: 解析ipa
     * create time: 2019-06-28 15:59

     * @return void
     */
    Package analyzeIPA(MultipartFile file) throws ParserConfigurationException, ParseException, SAXException, PropertyListFormatException, IOException {
        File excelFile = File.createTempFile(UUID.randomUUID().toString(), ".ipa");
        file.transferTo(excelFile);
        File ipa = ZipUtil.unzip(excelFile);
        File app = getAppFile(ipa);
        File info = new File(app.getAbsolutePath()+"/Info.plist");
        NSDictionary parse = (NSDictionary) PropertyListParser.parse(new FileReader(info).readBytes());
        String name = "";
        if (parse.containsKey("CFBundleName")) {
            name = parse.get("CFBundleName").toString();
        }
        if (parse.containsKey("CFBundleDisplayName")) {
            name = parse.get("CFBundleDisplayName").toString();
        }
        String version = "";
        if (parse.containsKey("CFBundleShortVersionString")) {
            version = parse.get("CFBundleShortVersionString").toString();
        }
        String buildVersion = "";
        if (parse.containsKey("CFBundleVersion")) {
            buildVersion = parse.get("CFBundleVersion").toString();
        }
        String miniVersion = "";
        if (parse.containsKey("MinimumOSVersion")) {
            miniVersion = parse.get("MinimumOSVersion").toString();
        }
        String bundleIdentifier = "";
        if (parse.containsKey("CFBundleIdentifier")) {
            bundleIdentifier = parse.get("CFBundleIdentifier").toString();
        }
        NSDictionary icons = null;
        if (parse.containsKey("CFBundleIcons")) {
            icons = (NSDictionary) parse.get("CFBundleIcons");
        }else if (parse.containsKey("CFBundleIcons~ipad")) {
            icons = (NSDictionary) parse.get("CFBundleIcons~ipad");
        }
        String iconLink = "";
        if (icons.toJavaObject()!=null) {
            List list = ((NSDictionary) icons.get("CFBundlePrimaryIcon")).get("CFBundleIconFiles").toJavaObject(List.class);
            iconLink = (String) list.get(list.size()-1);
            String iconPath = app.getAbsolutePath() + "/" + iconLink;
            File icon = new File( iconPath + "@3x.png");
            if (icon==null) {
                icon = new File(iconPath+"@2x.png");
            }
            iconLink = uploadIcon(icon);
            icon.delete();
        }
        String appLink = uploadAppFile(excelFile);
        Package pck = null;
        if (appLink!=null) {
            System.out.println("ipa文件上传完成");
            pck = new Package();
            pck.setName(name);
            pck.setVersion(version);
            pck.setBuildVersion(buildVersion);
            pck.setMiniVersion(miniVersion);
            pck.setBundleIdentifier(bundleIdentifier);
            pck.setIcon(iconLink);
            pck.setLink(appLink);
        }else {
            System.out.println("ipa文件上传失败");
        }
        FileUtil.del(excelFile);
        FileUtil.del(ipa);
        FileUtil.del(app);
        FileUtil.del(info);
        return pck;
    }

    /**
     * create by: iizvv
     * description: 上传app
     * create time: 2019-06-28 17:21

     * @return app
     */
    String uploadAppFile(File file) {
        System.out.println("开始上传原始ipa文件");
        String objName = IdUtil.simpleUUID() + ".ipa";
        fileManager.uploadFile(file, objName, false);
        return objName;
    }

    /**
     * create by: iizvv
     * description: 上传icon
     * create time: 2019-06-29 12:19

     * @return icon名称
     */
    String uploadIcon(File file) {
        String objName = IdUtil.simpleUUID()+".png";
        fileManager.uploadFile(file, objName, false);
        return objName;
    }

    /**
     * create by: iizvv
     * description: 创建获取UDID所用证书
     * create time: 2019-07-04 11:01

     * @return 证书名称
     */
    String creatUDIDMobileconfig(long id) {
        System.out.println("创建获取UDID所用证书");
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n" +
                "<plist version=\"1.0\">\n" +
                "    <dict>\n" +
                "        <key>PayloadContent</key>\n" +
                "        <dict>\n" +
                "            <key>URL</key>\n" +
                "            <string>"+ Config.udidURL +"/udid/getUDID?encryptHex="+ AESUtils.encryptHex(String.valueOf(id)) +"</string> <!--接收数据的接口地址-->\n" +
                "            <key>DeviceAttributes</key>\n" +
                "            <array>\n" +
                "                <string>SERIAL</string>\n" +
                "                <string>MAC_ADDRESS_EN0</string>\n" +
                "                <string>UDID</string>\n" +
                "                <string>IMEI</string>\n" +
                "                <string>ICCID</string>\n" +
                "                <string>VERSION</string>\n" +
                "                <string>PRODUCT</string>\n" +
                "            </array>\n" +
                "        </dict>\n" +
                "        <key>PayloadOrganization</key>\n" +
                "        <string>" + Config.payloadOrganization +"</string>  <!--组织名称-->\n" +
                "        <key>PayloadDisplayName</key>\n" +
                "        <string>" + Config.payloadDisplayName + "</string>  <!--安装时显示的标题-->\n" +
                "        <key>PayloadVersion</key>\n" +
                "        <integer>1</integer>\n" +
                "        <key>PayloadUUID</key>\n" +
                "        <string>"+ IdUtil.simpleUUID() +"</string>  <!--自己随机填写的唯一字符串-->\n" +
                "        <key>PayloadIdentifier</key>\n" +
                "        <string>online.iizvv.profile-service</string>\n" +
                "        <key>PayloadDescription</key>\n" +
                "        <string>"+Config.payloadDescription+"</string>   <!--描述-->\n" +
                "        <key>PayloadType</key>\n" +
                "        <string>Profile Service</string>\n" +
                "    </dict>\n" +
                "</plist>";
        String tempName = "udid_"+ id + "_" + IdUtil.simpleUUID();
        String tempMobileconfig = tempName + ".mobileconfig";
        FileWriter writer = new FileWriter(tempMobileconfig);
        writer.write(xml);
        System.out.println("开始执行shell");
        String mobileconfig = tempName + "_.mobileconfig";
        String com = "/root/mobileconfig.sh " + writer.getFile().getAbsolutePath() + " " + mobileconfig;
        try {
            Shell.run(com);
            System.out.println("shell执行成功, 文件位置为: " + mobileconfig);
            File file = new File("/root/" + mobileconfig);
            mobileconfig = uploadMobileconfig(file);
            file.delete();
        } catch (Exception e) {
            System.out.println("shell执行失败");
            mobileconfig = uploadMobileconfig(writer.getFile());
            e.printStackTrace();
        }finally {
            writer.getFile().delete();
        }
        System.out.println("mobileconfig文件上传结束");
        return mobileconfig;
    }


    /**
     * create by: iizvv
     * description: 上传mobileconfig
     * create time: 2019-07-04 11:18

     * @return mobileconfig名称
     */
    String uploadMobileconfig(File file) {
        String objName = IdUtil.simpleUUID()+".mobileconfig";
        fileManager.uploadFile(file, objName, false);
        return objName;
    }


    /**
     * create by: iizvv
     * description: 获取ipa中的app文件
     * create time: 2019-07-24 10:57

     * @return app文件
     */
    File getAppFile(File ipaFile) {
        File payload = new File(ipaFile.getAbsolutePath() + "/Payload/");
        if (payload != null) {
            for (File file : payload.listFiles()) {
                System.out.println(fileManager.getSuffixName(file));
                if (fileManager.getSuffixName(file).equalsIgnoreCase("app")) {
                    System.out.println(file.getName());
                    return file;
                }
            }
        }
        return null;
    }

    /**
     * create by: iizvv
     * description: ipa文件处理分析
     * create time: 2019-09-02 20:16
     *
     * @return Result
     */
    Result databaseWithIPA(HttpServletRequest request, MultipartFile file, long id) {
        Result result = new Result();
        if (file!=null) {
            String authorization = request.getHeader("Authorization");
            Claims claims = JwtHelper.verifyJwt(authorization);
            long userId = (Integer)claims.get("userId");
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (suffix.equalsIgnoreCase("ipa")) {
                // 上传的文件为ipa文件
                Package aPackage = null;
                try {
                    aPackage = analyzeIPA(file);
                } catch (Exception e) {
                    e.printStackTrace();
                    result.setMsg("ipa解析失败");
                }
                if (aPackage != null) {
                    aPackage.setUserId(userId);
                    if (id>0) {
                        aPackage.setId(id);
                        boolean b = packageService.updatePackage(aPackage);
                        if (b) {
                            result.setCode(1);
                            result.setData(aPackage.getId());
                            result.setMsg("更新成功");
                        }else {
                            result.setMsg("更新失败");
                        }
                    }else {
                        boolean b = packageService.insertPackage(aPackage);
                        if (b) {
                            System.out.println("ipa写入数据库成功");
                            String mobileconfig = creatUDIDMobileconfig(aPackage.getId());
                            if (mobileconfig != null) {
                                b = packageService.updatePackageMobileconfigById(aPackage.getId(), mobileconfig);
                                if (b) {
                                    result.setCode(1);
                                    result.setData(aPackage.getId());
                                    result.setMsg("ipa文件上传完成");
                                    System.out.println("udid配置文件上传成功");
                                }else {
                                    System.out.println("配置文件生成或上传失败");
                                    b = packageService.deletePackageById(aPackage.getId());
                                    result.setMsg("ipa文件上传失败, 请重新上传");
                                    if (b) {
                                        System.out.println("ipa文件删除成功");
                                    }else {
                                        System.out.println("ipa文件删除失败");
                                    }
                                }
                            }else {
                                System.out.println("udid配置文件上传失败");
                            }
                        }else {
                            result.setMsg("数据库写入失败");
                        }
                    }
                }else {
                    result.setMsg("ipa文件解析失败");
                }
            }else {
                // 上传的文件非ipa文件
                result.setMsg("请检查文件类型");
            }
        }else {
            result.setMsg("文件不存在， 检查路径是否正确");
        }
        return result;
    }

}
