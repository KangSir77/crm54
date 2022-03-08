package com.yjxxt.interceptors;

import com.yjxxt.exceptions.NoLoginException;
import com.yjxxt.mapper.UserMapper;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class NoLoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired(required = false)
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //用户未登录,抛异常
        Integer userId=LoginUserUtil.releaseUserIdFromCookie(request);
        System.out.println(userId+"<<");
        //判断
        if(userId==null || userService.selectByPrimaryKey(userId)==null){
            throw new NoLoginException("用户未登录");
        }
        //放行
        return true;
    }
}
