package online.iizvv.core.config;

import online.iizvv.filter.ApiFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        registration.addUrlPatterns("/file/*");
        registration.addUrlPatterns(
                "/user/getAllUser",
                "/user/getAllReviewUser",
                "/user/getUserInfo");
        registration.addUrlPatterns("/apple/*");
        registration.addUrlPatterns("/device/*");
        registration.addUrlPatterns("/package/deletePackageById",
                "/package/getAllPackage",
                "/package/insertPackage",
                "/package/updatePackageById",
                "/package/updatePackageImgsById",
                "/package/updatePackageSummaryById",
                "/package/updatePackageTotalDeviceById");
        return registration;
    }
}