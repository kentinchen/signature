package online.iizvv.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ：iizvv
 * @date ：Created in 2019-08-22 12:57
 * @description：TODO
 * @version: 1.0
 */
@ApiModel(value = "用户对象")
public class User {

    @ApiModelProperty(value = "id")
    private long id;
    @ApiModelProperty(value = "0:待管理员审核 1:超级管理员 2:普通用户")
    private long level;
    @ApiModelProperty(value = "用户名")
    private String username;
//    @ApiModelProperty(value = "密码")
//    private String password;
    @ApiModelProperty(value = "邮箱")
    private String email;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}