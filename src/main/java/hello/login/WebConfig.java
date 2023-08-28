package hello.login;

import hello.login.web.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
//import javax.servlet.FilterRegistration;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean logFilter() { //이렇게 해두면 WAS를 띄울 때 filter를 같이 띄워줌
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1); //순서를 정해주는 거임
        filterRegistrationBean.addUrlPatterns("/*"); //어떤 url 패턴에 적용할지 설정 /* 이렇게 하면 모든 url에 다 적용

        return filterRegistrationBean;
    }
}
