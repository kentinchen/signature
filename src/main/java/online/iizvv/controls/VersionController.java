package online.iizvv.controls;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.iizvv.core.config.Config;
import online.iizvv.core.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：iizvv
 * @date ：Created in 2019-09-21 15:42
 * @description：TODO
 * @version: 1.0
 */
@Api(tags = {"版本信息"})
@RequestMapping("/version")
@RestController
public class VersionController {

    @ApiOperation(value="/getVersionInfo", notes="获取当前版本信息", produces = "application/json")
    @GetMapping("/getVersionInfo")
    public Result<Map> getVersionInfo() {
        Map map = new HashMap();
        map.put("version", Config.version);
        map.put("info", "- 增加授权码功能; \n " +
                "- 增加管理员可单独查看某个用户上传的ipas; \n " +
                "- 增加上传ipa时显示当前ipa的基本信息; \n " +
                "- 修复无p12文件账号被使用的问题; \n " +
                "- 修改证书格式为IOS_APP_ADHOC; \n " +
                "- 添加账号时主动判断使用的文件是否正确; ");
        return new Result(1, map, "信息获取成功");
    }



}
