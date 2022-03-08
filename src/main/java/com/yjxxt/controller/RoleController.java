package com.yjxxt.controller;

import com.yjxxt.base.BaseController;
import com.yjxxt.base.ResultInfo;
import com.yjxxt.bean.Role;
import com.yjxxt.query.RoleQuery;
import com.yjxxt.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController  extends BaseController {


    @Autowired(required = false)
    private RoleService roleService;


    @RequestMapping("roles")
    @ResponseBody
    public List<Map<String,Object>> sayRoles(){
        return roleService.findRoles();
    }


    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> findRoleByParam(RoleQuery query){
        //根据条件查询角色
        Map<String, Object> map = roleService.findAllRole(query);
        //map--json
        return map;
    }


    @RequestMapping("index")
    public String index(){
        return "role/role";
    }

    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer id){
        return roleService.queryAllRoles(id);
    }


//    @RequestMapping("list")
//    @ResponseBody
//    public Map<String,Object> userList(RoleQuery roleQuery){
//        return roleService.queryByParamsForTable(roleQuery);
//    }


    @RequestMapping("addOrUpdateRolePage")
    public String addUserPage(Integer id, Model model){
        if(null !=id){
            model.addAttribute("role",roleService.selectByPrimaryKey(id));
        }
        return "role/add_update";
    }


    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveRole(Role role){
        roleService.addRole(role);
        return success("角色记录添加成功");
    }


    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.changeRole(role);
        return success("角色记录更新成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer roleId){
        roleService.removRoleById(roleId);
        return success("角色记录删除成功");
    }

    @RequestMapping("toRoleGrantPage")
    public String sayGrand(Integer roleId,Model model){
        model.addAttribute("roleId",roleId);
        return "role/grant";
    }


    @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo grant(Integer roleId,String [] mids){
        //调用
        roleService.addGrant(roleId,mids);
        //判断是否成功
        return success("角色授权成功");
    }
}
