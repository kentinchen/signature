package online.iizvv.controls;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.iizvv.core.config.Config;
import online.iizvv.core.pojo.Result;
import online.iizvv.utils.JwtHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
    public Result<Map> getVersionInfo(HttpServletRequest request) throws InterruptedException {
        Result <Map>result = new Result<Map>();
        String authorization = request.getHeader(Config.Authorization);
        Claims claims = JwtHelper.verifyJwt(authorization);
        long level = (Integer)claims.get(Config.level);
        Map <String, Object>map = new HashMap<String, Object>();
        List <String>list = new LinkedList<String>();
        list.add("- 增加修改密码;");
        list.add("- 增加授权码;");
        list.add("- 增加上传ipa时显示当前ipa的基本信息;");
        list.add("- 修改证书格式为IOS_APP_ADHOC;");
        list.add("- 优化签名操作, 减少整体流程耗时;");
        list.add("- 修复部分ipa图标无法显示问题;");
        if (level == 1) {
            list.add("- 增加删除用户;");
            list.add("- 增加管理员可单独查看某个用户上传的ipas;");
            list.add("- 添加账号时主动判断使用的文件是否正确;");
            list.add("- 优化对账号不可用的判断;");
            list.add("- 移除对第三方签名工具的依赖;");
            list.add("- 修复无p12文件账号被使用的问题;");
        }
        map.put("version", Config.version);
        map.put("info", list);
        result.setCode(1);
        result.setData(map);
        result.setMsg("数据获取成功");
        return result;
    }



}
