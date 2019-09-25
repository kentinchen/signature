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
        registration.addUrlPatterns("/version/*");
        registration.addUrlPatterns("/file/*");
        registration.addUrlPatterns(
                "/user/getAllUser",
                "/user/getAllReviewUser",
                "/user/getUserInfo",
                "/user/checkUserById",
                "/user/deleteUserById",
                "/user/updateUserPassword");
        registration.addUrlPatterns("/apple/*");
        registration.addUrlPatterns("/device/*");
        registration.addUrlPatterns(
                "/package/deletePackageById",
                "/package/deletePackageKeysById",
                "/package/getAllPackage",
                "/package/getAllPackageByUserId",
                "/package/getAllUnusedKeysById",
                "/package/getAllUsedKeysById",
                "/package/getPackageById",
                "/package/insertPackage",
                "/package/insertPackageKeysById",
                "/package/updatePackageById",
                "/package/updatePackageImgsById",
                "/package/updatePackageSummaryById",
                "/package/updatePackageTotalDeviceById",
                "/package/updatePackageStateById");
        return registration;
    }
}