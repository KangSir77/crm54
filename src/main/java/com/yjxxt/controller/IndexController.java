package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.bean.User;
import com.yjxxt.service.PermissionService;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private PermissionService permissionService;

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

    @RequestMapping("main")
    public String main(HttpServletRequest req){
        //获取用户信息,从何Cookie获取用户ID
        Integer userId=LoginUserUtil.releaseUserIdFromCookie(req);
        //根据ID查询用户信息
        User user=userService.selectByPrimaryKey(userId);
        //存储User
        req.setAttribute("user",user);
        //统计用户拥有的权限码
        List<String> permisssions=permissionService.queryUserHasRolesHasPermissions(userId);
        //存储
        req.getSession().setAttribute("permisssions",permisssions);
        //返回目标视图
        return "main";
    }
}
