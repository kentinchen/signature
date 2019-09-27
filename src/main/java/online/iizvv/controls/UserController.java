package online.iizvv.controls;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import online.iizvv.core.config.Config;
import online.iizvv.core.pojo.Result;
import online.iizvv.pojo.Package;
import online.iizvv.pojo.User;
import online.iizvv.service.DPServiceImpl;
import online.iizvv.service.PKServiceImpl;
import online.iizvv.service.PackageServiceImpl;
import online.iizvv.service.UserServiceImpl;
import online.iizvv.utils.JwtHelper;
import online.iizvv.utils.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：iizvv
 * @date ：Created in 2019-08-21 21:52
 * @description：用户
 * @modified By：
 * @version: 1.0
 */
@Api(tags = {"用户"})
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PackageServiceImpl packageService;

    @Autowired
    private PKServiceImpl pkService;

    @Autowired
    DPServiceImpl dpService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ApiOperation(value="/register", notes="注册", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true)
    })
    @PostMapping("/register")
    public Result register(String username, String password) {
        Result result = new Result();
        if (username.trim().length() > 3 && password.trim().length() > 3) {
            if (userService.getUserCount() > 0) {
                if (userService.getUserByUsername(username) != null) {
                    result.setMsg("此用户名已存在");
                }else {
                    Boolean bool = userService.register(username, MD5Utils.encrypt(password), 0);
                    if (bool) {
                        result.setCode(1);
                        result.setMsg("注册成功, 等待管理员审核");
                    }else {
                        result.setMsg("注册失败, 稍后再试");
                    }
                }
            }else {
                log.info("用户表为空表， 开始注册管理员");
                Boolean bool = userService.register(username, MD5Utils.encrypt(password), 1);
                if (bool) {
                    result.setCode(1);
                    result.setMsg("注册成功, 此用户为管理员");
                }else {
                    result.setMsg("注册失败, 稍后再试");
                }
            }
        }else {
            result.setMsg("用户名与密码不能小于三位字符");
        }
        return result;
    }

    @ApiOperation(value="/login", notes="登录", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true)
    })
    @PostMapping("/login")
    public Result login(String username, String password) {
        Result result = new Result();
        if (username.trim().length() > 3 && password.trim().length() > 3) {
            User user = userService.login(username, MD5Utils.encrypt(password));
            if (user!=null) {
                if (user.getLevel() == 0) {
                    result.setMsg("帐号未审核");
                }else {
                    result.setCode(1);
                    result.setMsg("登录成功");
                    Map map = new HashMap();
                    map.put(Config.level, user.getLevel());
                    map.put(Config.username, username);
                    map.put(Config.userId, user.getId());
                    String token = JwtHelper.generateToken(map);
                    result.setData(token);
                }
            }else {
                result.setMsg("用户名或密码错误");
            }
        }else {
            result.setMsg("用户名与密码不能小于三位字符");
        }
        return result;
    }

    @ApiOperation(value="/deleteUserById", notes="删除用户", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "用户id", required = true),
    })
    @PostMapping("/deleteUserById")
    public Result deleteUserById(HttpServletRequest request, long id){
        Result result = new Result();
        String authorization = request.getHeader(Config.Authorization);
        Claims claims = JwtHelper.verifyJwt(authorization);
        long level = (Integer)claims.get(Config.level);
        if (level == 1) {
            List<Package> packages = packageService.getAllPackageByUserId(id);
            for (Package aPackage : packages) {
                pkService.deleteKeyByPackageId(aPackage.getId());
                dpService.deleteDPByPackageId(aPackage.getId());
            }
            packageService.deletePackageByUserId(id);
            boolean b = userService.deleteUserById(id);
            if (b) {
                result.setCode(1);
                result.setMsg("用户删除成功");
            }else {
                result.setMsg("用户删除失败");
            }
        }else {
            result.setMsg("用户权限不足");
        }
        return result;
    }

    @ApiOperation(value="/updateUserPassword", notes="修改密码", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "oldPassword", value = "原始密码", required = true),
            @ApiImplicitParam(name = "password", value = "新密码", required = true),
    })
    @PostMapping("/updateUserPassword")
    public Result updateUserPassword(HttpServletRequest request, String oldPassword, String password) {
        Result result = new Result();
        String authorization = request.getHeader(Config.Authorization);
        Claims claims = JwtHelper.verifyJwt(authorization);
        long userId = (Integer)claims.get(Config.userId);
        boolean b = userService.updateUserPasswordByIdAndOldPassword(userId,
                MD5Utils.encrypt(oldPassword),
                MD5Utils.encrypt(password));
        if (b) {
            result.setMsg("密码修改成功");
            result.setCode(1);
        }else {
            result.setMsg("密码修改失败");
        }
        return result;
    }

    @ApiOperation(value="/getAllReviewUser", notes="待审核用户列表", produces = "application/json")
    @GetMapping("/getAllReviewUser")
    public Result <List<User>>getAllReviewUser(HttpServletRequest request) {
        Result result = new Result();
        String authorization = request.getHeader(Config.Authorization);
        Claims claims = JwtHelper.verifyJwt(authorization);
        long level = (Integer)claims.get(Config.level);
        if (level == 1) {
            List<User> userList = userService.getAllReviewUser();
            result.setData(userList);
            result.setCode(1);
            result.setMsg("待审核用户列表获取成功");
        }else {
            result.setMsg("用户权限不足");
        }
        return result;
    }

    @ApiOperation(value="/getAllUser", notes="待审核用户列表", produces = "application/json")
    @GetMapping("/getAllUser")
    public Result <List<User>>getAllUser(HttpServletRequest request) {
        Result result = new Result();
        String authorization = request.getHeader(Config.Authorization);
        Claims claims = JwtHelper.verifyJwt(authorization);
        long level = (Integer)claims.get(Config.level);
        if (level == 1) {
            List<User> userList = userService.getAllUser();
            result.setData(userList);
            result.setCode(1);
            result.setMsg("普通用户列表获取成功");
        }else {
            result.setMsg("用户权限不足");
        }
        return result;
    }

    @ApiOperation(value="/checkUserById", notes="审核帐号", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "审核的帐号id", required = true),
            @ApiImplicitParam(name = "status", value = "修改帐号状态", required = true)
    })
    @PostMapping("/checkUserById")
    public Result checkUserById(HttpServletRequest request, long id, boolean status) {
        Result result = new Result();
        String authorization = request.getHeader(Config.Authorization);
        Claims claims = JwtHelper.verifyJwt(authorization);
        long level = (Integer)claims.get(Config.level);
        if (level == 1) {
            boolean b = userService.updateUserLevelById(id, status?2:0);
            if (b) {
                result.setMsg("帐号状态修改通过");
                result.setCode(1);
            }else {
                result.setMsg("帐号状态修改失败");
            }
        }else {
            result.setMsg("当前用户权限不足");
        }
        return result;
    }

    @ApiOperation(value="/getUserInfo", notes="获取用户信息", produces = "application/json")
    @GetMapping("/getUserInfo")
    public Result <User>getUserInfo(HttpServletRequest request) {
        Result result = new Result();
        String authorization = request.getHeader(Config.Authorization);
        Claims claims = JwtHelper.verifyJwt(authorization);
        long userId = (Integer)claims.get(Config.userId);
        User user = userService.getUserInfo(userId);
        if (user!=null) {
            result.setData(user);
            result.setCode(1);
            result.setMsg("信息获取成功");
        }else {
            result.setMsg("信息获取失败");
        }
        return result;
    }

}
