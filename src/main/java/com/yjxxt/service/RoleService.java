package com.yjxxt.service;

import com.yjxxt.base.BaseService;
import com.yjxxt.bean.Permission;
import com.yjxxt.bean.Role;
import com.yjxxt.bean.User;
import com.yjxxt.bean.UserRole;
import com.yjxxt.mapper.*;
import com.yjxxt.query.RoleQuery;
import com.yjxxt.utils.AssertUtil;
import com.yjxxt.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleService extends BaseService<Role,Integer> {


    @Autowired(required = false)
    private RoleMapper roleMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private ModuleMapper moduleMapper;


    /**
     * 查询所有的角色
     * @return
     */
    public List<Map<String,Object>> findRoles(){
        //返回目标集合对象
        return roleMapper.selectRoles();
    }


    /**
     * 角色的条件查询
     * @param query
     * @return
     */
    public Map<String,Object> findAllRole(RoleQuery query){
        //查询
        List<Role> rlist = roleMapper.selectByParams(query);
        //实例化Map
        Map<String,Object> map=new HashMap<>();
        //准备列表展示的数据
        map.put("code",0);
        map.put("msg","success");
        map.put("count",rlist.size());
        map.put("data",rlist);
        //返回目标数据
        return  map;
    }

    public List<Map<String,Object>> queryAllRoles(Integer id){
        return roleMapper.queryAllRoles(id);
    }



    /**
     * 角色的添加
     * @param role
     */
    public void addRole(Role role) {
        //角色非空
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "请输入角色名称");
        //角色名称已经存在
        Role temp=roleMapper.queryRoleByName(role.getRoleName());
        //AssertUtil.isTrue(temp != null&&temp.getId()!=role.getId(), "角色已经存在");
        AssertUtil.isTrue(temp != null, "角色已经存在");
        //设定默认值
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        role.setIsValid(1);
        //是否添加成功
        AssertUtil.isTrue(roleMapper.insertSelective(role) < 1, "角色添加失败");
    }






    /**
     * 角色的修改
     * @param role
     */
    public void changeRole(Role role) {
        //当前对象roleId
        AssertUtil.isTrue(roleMapper.selectByPrimaryKey(role.getId()) == null || role.getId() == null, "待修改数据不存在");
        //角色名称要唯一
        Role temp = roleMapper.queryRoleByName(role.getRoleName());
        AssertUtil.isTrue(temp != null && !(temp.getId().equals(role.getId())), "角色已经存在");
        //默认值修改时间
        role.setUpdateDate(new Date());
        //修改是否成功
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1, "角色修改失败了");
    }


    /**
     * @param roleId
     */
    public void removRoleById(Integer roleId) {
        //验证roleId
        AssertUtil.isTrue(roleId == null || roleMapper.selectByPrimaryKey(roleId) == null, "请求选择删除数据");
        //判断是否成功
        AssertUtil.isTrue(roleMapper.deleteByPrimaryKey(roleId) < 1, "删除失败了");
    }


    /**
     * 给角色授权
     * @param roleId
     * @param mids
     */
    public void addGrant(Integer roleId,String [] mids){
        //验证roleId存在
        Role temp=roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(temp==null,"请选择授权的角色");
        //资源存在
        if(mids!=null && mids.length>0){
            //角色原来是否有资源
            //若有资源,新增一部分
            //删除原来的资源,重新分配
            //统计角色拥有多少个资源,删除,重新分配
            int count=permissionMapper.countModulsByRoleId(roleId);
            if(count>0){
                //删除原来的资源信息
                AssertUtil.isTrue(permissionMapper.deleteModuleByRoleId(roleId)!=count,"操作失败");
            }
            //重新分配资源
            //t_permission t_role;
            //遍历集合对象
            List<Permission> plist=new ArrayList<Permission>();
            for (String mid:mids) {
                //实例化目标对象
                Permission permission=new Permission();
                //初始化角色ID
                permission.setRoleId(roleId);
                //初始化资源ID
                permission.setModuleId(Integer.parseInt(mid));

                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                //获取当前资源的权限码
                String optValue = moduleMapper.selectByPrimaryKey(Integer.parseInt(mid)).getOptValue();
                permission.setAclValue(optValue);
                //添加道容器plist
                plist.add(permission);
            }
            //批量添加
            permissionMapper.insertBatch(plist);
        }

    }

}
