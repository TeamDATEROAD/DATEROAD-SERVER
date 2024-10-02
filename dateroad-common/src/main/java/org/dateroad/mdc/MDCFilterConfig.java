package org.dateroad.mdc;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!local")
@Configuration
public class MDCFilterConfig {
    @Bean
    public FilterRegistrationBean<RequestWrappingFilter> secondFilter() {
        FilterRegistrationBean<RequestWrappingFilter> filterRegistrationBean = new FilterRegistrationBean<>(new RequestWrappingFilter());
        filterRegistrationBean.setOrder(0);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<MDCFilter> thirdFilter() {
        FilterRegistrationBean<MDCFilter> filterRegistrationBean = new FilterRegistrationBean<>(new MDCFilter());
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }
}
