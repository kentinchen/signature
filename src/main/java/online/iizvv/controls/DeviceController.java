package online.iizvv.controls;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.*;
import online.iizvv.pojo.Device;
import online.iizvv.core.pojo.Result;
import online.iizvv.service.DeviceServiceImpl;
import online.iizvv.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Api(tags = {"设备管理"})
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private DeviceServiceImpl deviceService;

    @ApiOperation(value = "/getAllByAppleId", notes = "获取帐号下的所有设备", produces = "application/json")
    @GetMapping("/getAllByAppleId")
    public Result getAll(HttpServletRequest request, long id) {
        Result result = new Result();
        String authorization = request.getHeader("Authorization");
        Claims claims = JwtHelper.verifyJwt(authorization);
        long level = (Integer)claims.get("level");
        if (level == 1) {
            List<Device> list = deviceService.getAllByAppleId(id);
            result.setData(list);
            result.setCode(1);
            result.setMsg("数据获取成功");
        }else {
            result.setMsg("权限不足");
        }
        return result;
    }


}
