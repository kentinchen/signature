package online.iizvv.core.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author ：iizvv
 * @date ：Created in 2019-08-25 16:03
 * @description：过滤器加载
 * @modified By：
 * @version: 1.0
 */
@Configuration
public class BeanRegisterConfig {

    @Bean
    public FilterRegistrationBean createFilterBean() {
        // 过滤器注册类
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ApiFilter());
        //需要过滤的接口
        registration.addUrlPatterns(
                "/user/getAllUser",
                "/user/getAllReviewUser",
                "/user/getUserInfo");
        registration.addUrlPatterns("/apple/*");
        registration.addUrlPatterns("/device/*");
        registration.addUrlPatterns("/package/getAllPackage",
                "/package/uploadPackage",
                "/package/deleteById",
                "/package/resetTotalCountById",
                "/package/updateTotalCountById");
        return registration;
    }
}