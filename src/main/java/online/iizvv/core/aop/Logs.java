package online.iizvv.core.aop;

import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.jsonwebtoken.Claims;
import online.iizvv.core.config.Config;
import online.iizvv.utils.JwtHelper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @Author FXS
 * @Date 2018/12/6
 **/
@Component
@Aspect
public class Logs {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private HttpServletRequest request;

    public Logs() {
    }

    @Before("execution(* online.iizvv.controls..*(..))")
    public void before(JoinPoint join) {
        Logs.RequestInfo requestInfo = this.getRequestInfo(this.request);
        String className = join.getTarget().getClass().getName();
        String function = join.getSignature().getName();
        String args = Arrays.asList(join.getArgs()).toString();
        StringBuffer msg = new StringBuffer();
        msg.append("当前用户【");
        msg.append(requestInfo.getUsername());
        msg.append("】 IP【");
//        msg.append(getIpAddr(this.request));
        msg.append("】 正在访问【类：<");
        msg.append(className);
        msg.append(">，方法：<");
        msg.append(function);
        msg.append(">，参数：<");
        msg.append(args);
        msg.append(">】");
        msg.append("【" + requestInfo.getUserAgent() + "】");
        this.log.info(msg.toString());
    }

    @AfterThrowing(
            pointcut = "execution(* online.iizvv..*(..))",
            throwing = "e"
    )
    public void exception(JoinPoint join, Exception e) {
        Logs.RequestInfo requestInfo = this.getRequestInfo(this.request);
        String className = join.getTarget().getClass().getName();
        String function = join.getSignature().getName();
        String args = Arrays.asList(join.getArgs()).toString();
        StringBuffer msg = new StringBuffer();
        StackTraceElement[] elems = e.getStackTrace();
        String text = "\n";
        StackTraceElement[] var10 = elems;
        int var11 = elems.length;

        for(int var12 = 0; var12 < var11; ++var12) {
            StackTraceElement elem = var10[var12];
            text = text + "\t" + elem.toString() + "\n";
        }

        msg.append("当前用户【");
        msg.append(requestInfo.getUsername());
        msg.append("】 IP【");
        msg.append(SystemUtil.getHostInfo().getAddress());
        msg.append("】 正在访问【类：<");
        msg.append(className);
        msg.append(">，方法：<");
        msg.append(function);
        msg.append(">，参数：<");
        msg.append(args);
        msg.append("出现异常:【");
        msg.append(e.toString());
        msg.append("】 \n------------------ 异常详情 -----------------\n");
        msg.append(e.toString());
        msg.append(text);
        msg.append("【" + requestInfo.getUserAgent() + "】");
        this.log.error(msg.toString());
    }

    @AfterReturning(
            returning = "result",
            pointcut = "execution(* online.iizvv.controls..*(..))"
    )
    public void After(JoinPoint join, Object result) {
        Logs.RequestInfo requestInfo = this.getRequestInfo(this.request);
        String className = join.getTarget().getClass().getName();
        String function = join.getSignature().getName();
        String args = JSON.toJSONString(result, new SerializerFeature[]{SerializerFeature.WriteMapNullValue});
        StringBuffer msg = new StringBuffer();
        msg.append("当前用户【");
        msg.append(requestInfo.getUsername());
        msg.append("】 返回结果【类：<");
        msg.append(className);
        msg.append(">，方法：<");
        msg.append(function);
        msg.append(">，结果：<");
        msg.append(args);
        msg.append(">】");
        this.log.info(msg.toString());
    }

    private Logs.RequestInfo getRequestInfo(HttpServletRequest request) {
        Logs.RequestInfo requestInfo = new Logs.RequestInfo();
        try {
//            requestInfo.setIp(getIpAddr(request));
            requestInfo.setUserAgent(request.getHeader(Config.ua));
            String token = request.getHeader(Config.Authorization);
            if (!StringUtils.isEmpty(token)) {
                Claims claims = JwtHelper.verifyJwt(token); //验证token
                requestInfo.setUsername((String) claims.get(Config.username));
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return requestInfo;
    }

    private class RequestInfo {
        private String ip;
        private String userAgent;
        private String username;

        private RequestInfo() {
            this.ip = "";
            this.userAgent = "";
            this.username = "游客";
        }

        public String getIp() {
            return this.ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getUserAgent() {
            return this.userAgent;
        }

        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        public String getUsername() {
            return this.username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

}
