package hello.login;

import hello.login.web.argumentResolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;
//import javax.servlet.FilterRegistration;

@Configuration
public class WebConfig implements WebMvcConfigurer {
 //필터랑 인터셉터가 같이 있으면 필터가 먼저 실행되고 인터셉터가 나중에 실행 됨


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        resolvers.add(new LoginMemberArgumentResolver());

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1) //순서
                .addPathPatterns("/**") //모든 url에 적용
                .excludePathPatterns("/css/**", "/*.ico", "/error"); //지정된 url 빼고 실행
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members/add", "/login", "/logout", "/css/**", "/*.ico", "/error");
    }

    //@Bean
    public FilterRegistrationBean logFilter() { //이렇게 해두면 WAS를 띄울 때 filter를 같이 띄워줌
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1); //순서를 정해주는 거임
        filterRegistrationBean.addUrlPatterns("/*"); //어떤 url 패턴에 적용할지 설정 /* 이렇게 하면 모든 url에 다 적용

        return filterRegistrationBean;
    }

    //@Bean
    public FilterRegistrationBean loginCheckFilter() { //이렇게 해두면 WAS를 띄울 때 filter를 같이 띄워줌
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2); //순서를 정해주는 거임
        filterRegistrationBean.addUrlPatterns("/*"); //어떤 url 패턴에 적용할지 설정 /* 이렇게 하면 모든 url에 다 적용

        return filterRegistrationBean;
    }
}
