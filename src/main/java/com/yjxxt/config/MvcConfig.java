package com.yjxxt.config;

import com.yjxxt.exceptions.NoLoginException;
import com.yjxxt.interceptors.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    /**
     * 配置拦截器对象
     * @return
     */
    @Bean
    public NoLoginInterceptor noLoginInterceptor(){
        return new NoLoginInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //指定拦截器对象
        registry.addInterceptor(noLoginInterceptor())
                //拦截路径
                .addPathPatterns("/**")
                //放行路径
                .excludePathPatterns("/index","/user/login","/js/**","/css/**","/images/**","/lib/**");
    }
}
