package online.iizvv.filter;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import online.iizvv.core.pojo.Result;
import online.iizvv.utils.JwtHelper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ：iizvv
 * @date ：Created in 2019-08-25 16:03
 * @description：过滤器
 * @modified By：
 * @version: 1.0
 */
public class ApiFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request =(HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        response.setContentType("text/html;charset=UTF-8");
        String ua = request.getHeader("User-Agent");
        System.out.println("当前时间: " + DateUtil.now() +
                "\n当前用户User-Agent: " + ua +
                "\n当前请求接口: " + request.getRequestURL().toString());
        String token = request.getHeader("Authorization"); //获取请求传来的token
        Claims claims = JwtHelper.verifyJwt(token); //验证token
        if (claims == null) {
            Result result = new Result();
            result.setMsg("请重新登录");
            response.getWriter().write(JSON.toJSONString(result));
        }else {
            System.out.println("当前用户id: " + claims.get("userId"));
            filterChain.doFilter(request,response);
        }
    }

    @Override
    public void destroy() {

    }

}