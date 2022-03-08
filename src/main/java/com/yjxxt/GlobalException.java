package com.yjxxt;

import com.alibaba.fastjson.JSON;
import com.yjxxt.base.ResultInfo;
import com.yjxxt.exceptions.NoLoginException;
import com.yjxxt.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Handler;

@Component
public class GlobalException implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception e) {
        //未登录异常
        if(e instanceof NoLoginException){
            ModelAndView mav=new ModelAndView("redirect:/index");
            return mav;
        }
        //实例化对象
        ModelAndView mav=new ModelAndView();
        //默认视图
        mav.setViewName("");
        mav.addObject("code",400);
        mav.addObject("msg","系统异常,稍后访问");
        //判断ResponseBody
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod=(HandlerMethod) handler;
            ResponseBody responseBody=handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            //判断是否为空
            if(responseBody==null){
                //返回视图
                if(e instanceof ParamsException){
                    ParamsException pe=(ParamsException)e;
                    mav.addObject("code",pe.getCode());
                    mav.addObject("msg",pe.getMsg());
                }
                return mav;
            }else{
                //返回的json数据
                ResultInfo resultInfo=new ResultInfo();
                resultInfo.setMsg("系统异常");
                resultInfo.setCode(400);
                if(e instanceof ParamsException){
                    ParamsException pe=(ParamsException)e;
                    resultInfo.setCode(pe.getCode());
                    resultInfo.setMsg(pe.getMsg());
                }else {
                    e.printStackTrace();
                }
                //resp
                resp.setContentType("application/json;charset=utf-8");
                //获取输入流
                PrintWriter out= null;
                try {
                    out = resp.getWriter();
                    //输出流
                    out.write(JSON.toJSONString(resultInfo));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }finally {
                    if(out!=null){
                        out.flush();
                        out.close();
                    }
                }
            }
        }
        return mav;
    }
}
