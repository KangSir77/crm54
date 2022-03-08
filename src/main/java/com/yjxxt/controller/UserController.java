package com.yjxxt.controller;

import com.yjxxt.annotation.RequiredPermission;
import com.yjxxt.base.BaseController;
import com.yjxxt.base.ResultInfo;
import com.yjxxt.bean.User;
import com.yjxxt.exceptions.ParamsException;
import com.yjxxt.model.UserModel;
import com.yjxxt.query.UserQuery;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping("login")
    @ResponseBody
    public ResultInfo login(String userName, String userPwd){
        //实例化对象
        ResultInfo resultInfo=new ResultInfo();
        //登录操作
        UserModel userModel=userService.userLogin(userName,userPwd);
        resultInfo.setResult(userModel);
        //返回目标对象
        return resultInfo;
    }


    @RequestMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest req,String oldPassword,String newPassword,String confirmPawd){
        //实例化对象
        ResultInfo resultInfo=new ResultInfo();
        //获取当前用户ID
        Integer userId=LoginUserUtil.releaseUserIdFromCookie(req);
        //修改密码
        userService.updateUserPassword(userId,oldPassword,newPassword,confirmPawd);
        //返回目标对象
        return resultInfo;
    }

    @RequestMapping("updateUser")
    @ResponseBody
    public ResultInfo updateUser(User user){
        //修改
        Integer x=userService.updateByPrimaryKeySelective(user);
        //返回目标对象
        return success("修改成功了");
    }

    @RequestMapping("toPasswordPage")
    public String update(){
        return "user/password";
    }

    @RequestMapping("toSettingPage")
    public String setting(HttpServletRequest req){
        //从Cookie获取用户ID
        Integer userId=LoginUserUtil.releaseUserIdFromCookie(req);
        //调用方法查询用户信息
        User user=userService.selectByPrimaryKey(userId);
        //存储数据
        req.setAttribute("user",user);
        //转发重定向
        return "user/setting";
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        List<Map<String, Object>> maps = userService.queryAllSales();
        return maps;
    }

    /**
     * 多条件查询用户数据
     * @param userQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    @RequiredPermission(code = "6010")
    public Map<String, Object> queryUserByParams(UserQuery userQuery) {
        return userService.queryUserByParams(userQuery);
    }

    /**
     * 进入用户页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "user/user";
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveUser(User user,String roleIds) {
        userService.saveUser(user);
        return success("用户添加成功！");
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser1(User user) {
        userService.updateUser(user);
        return success("用户更新成功！");
    }

    /**
     * 进入用户添加或更新页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdateUserPage")
    public String addUserPage(Integer id, Model model){
        if(null != id){
            model.addAttribute("user",userService.selectByPrimaryKey(id));
        }
        return "user/add_update";
    }

    /**
     * 删除用户
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.removeIds(ids);
        return success("用户记录删除成功");
    }
}
