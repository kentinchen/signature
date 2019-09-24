package online.iizvv.controls;

import cn.hutool.core.util.IdUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import online.iizvv.core.config.Config;
import online.iizvv.pojo.Apple;
import online.iizvv.pojo.Authorize;
import online.iizvv.core.pojo.Result;
import online.iizvv.pojo.Device;
import online.iizvv.service.AppleServiceImpl;
import online.iizvv.service.DPServiceImpl;
import online.iizvv.service.DeviceServiceImpl;
import online.iizvv.utils.FileManager;
import online.iizvv.utils.ITSUtils;
import online.iizvv.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ：iizvv
 * @date ：Created in 2019-06-26 21:24
 * @description：帐号操作
 * @version: 1.0
 */
@RestController
@Api(tags = {"帐号管理"})
@RequestMapping("/apple")
public class AppleController {

    @Autowired
    private AppleServiceImpl appleService;

    @Autowired
    private DeviceServiceImpl deviceService;

    @Autowired
    private DPServiceImpl dpService;

    @Autowired
    private FileManager fileManager;


    @ApiOperation(value = "/getAllAppleAccounts", notes = "获取全部账号")
    @GetMapping("/getAllAppleAccounts")
    public Result<List<Apple>> getAllAppleAccounts(HttpServletRequest request) {
        Result result = new Result();
        String authorization = request.getHeader(Config.Authorization);
        Claims claims = JwtHelper.verifyJwt(authorization);
        long level = (Integer)claims.get("level");
        if (level==1) {
            List<Apple> apples = appleService.getAllAppleAccounts();
            result.setCode(1);
            result.setMsg("数据获取成功");
            result.setData(apples);
        }else {
            result.setMsg("当前帐号权限不足");
        }
        return result;
    }

    @ApiOperation(value = "/insertAppleAccount", notes = "添加苹果开发者账号", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "account", value = "开发者账号", required = true),
            @ApiImplicitParam(name = "iss", value = "在Store Connect上可以点击复制 iss ID", required = true),
            @ApiImplicitParam(name = "kid", value = "与p8证书保持一致的密钥id", required = true),
            @ApiImplicitParam(name = "csr", value = "本机导出的csr文件内容", required = true),
            @ApiImplicitParam(name = "p8", value = "p8文件内容", required = true)
    })
    @PostMapping("/insertAppleAccount")
    public Result insertAppleAccount(String account, String iss, String kid, String csr, String p8) {
        Result result = new Result();
        if (appleService.getAppleAccountByAccount(account)==null) {
            // 可以写入数据库
            Map map = null;
            try {
                map = ITSUtils.getNumberOfAvailableDevices(new Authorize(p8, iss, kid, csr));
            }catch (Exception e) {
                e.printStackTrace();
                result.setMsg("帐号信息有误， 请检查后重新提交");
            }
            String msg = (String)map.get("msg");
            if (map!=null && msg==null) {
                List<Map> devices = (List)map.get("devices");
                String cerId = (String)map.get("cerId");
                String bundleIds = (String) map.get("bundleIds");
                int number = (int)map.get("number");
                Apple apple = new Apple();
                apple.setAccount(account);
                apple.setCount(number);
                apple.setP8(p8);
                apple.setIss(iss);
                apple.setKid(kid);
                apple.setCerId(cerId);
                apple.setBundleIds(bundleIds);
                int r = appleService.insertAppleAccount(apple);
                if (r==1) {
                    for (Map<String, String> item : devices) {
                        deviceService.insertDevice(item.get("udid"), apple.getId(), item.get("deviceId"));
                    }
                    result.setData(apple.getId());
                    result.setCode(1);
                    result.setMsg("开发者账号添加成功");
                }else {
                    result.setMsg("数据添加失败，请检查证书文件是否正确");
                }
            }else {
                result.setMsg(msg);
            }
        }else {
            // 账号已存在
            result.setMsg("账号已存在， 请勿重复添加");
        }
        return result;
    }

    @ApiOperation(value = "/uploadP12", notes = "上传p12文件", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "开发者账号id", required = true),
            @ApiImplicitParam(name = "file", value = "p12文件", required = true)
    })
    @PostMapping("/uploadP12")
    public Result uploadP12(long id, MultipartFile file) throws IOException {
        System.out.println("开始上传配p12文件：" + file.getOriginalFilename());
        Result result = new Result();
        String p12 = IdUtil.simpleUUID() + ".p12";
        if (file.getContentType().equalsIgnoreCase("application/x-pkcs12")) {
            // p12文件
            uploadP12File(file.getBytes(), p12);
            int i = appleService.updateP12(p12, id);
            if (i==1) {
                result.setCode(1);
                result.setMsg("信息更新成功");
            }else {
                result.setMsg("信息更新失败");
            }
        }else {
            result.setMsg("文件类型错误，请上传p12文件");
        }
        return result;
    }

    @ApiOperation(value = "/deleteById", notes = "删除帐号", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "开发者账号id", required = true),
    })
    @Transactional
    @PostMapping("/deleteById")
    public Result deleteById(HttpServletRequest request, long id) {
        Result result = new Result();
        String authorization = request.getHeader(Config.Authorization);
        Claims claims = JwtHelper.verifyJwt(authorization);
        long level = (Integer)claims.get("level");
        if (level==1) {
            try {
                for (Device device : deviceService.getAllByAppleId(id)) {
                    System.out.println("正在删除设备: " + device.toString());
                    dpService.deleteDPByDeviceId(device.getId());
                }
                deviceService.deleteByAppleId(id);
                appleService.deleteById(id);
                result.setCode(1);
                result.setMsg("删除成功");
            }catch (Exception e) {
                e.printStackTrace();
                result.setMsg("删除失败");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }else {
            result.setMsg("当前帐号权限不足");
        }
        return result;
    }

    /**
     * create by: iizvv
     * description: 将p12文件上传至阿里
     * create time: 2019-06-28 15:06
     *
     * @return 文件名
     */
    void uploadP12File(byte[] bytes, String fileName) {
        fileManager.uploadFile(bytes, fileName, false);
    }

}
