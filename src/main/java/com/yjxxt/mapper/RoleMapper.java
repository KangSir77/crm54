package com.yjxxt.mapper;

import com.yjxxt.base.BaseMapper;
import com.yjxxt.bean.Role;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role, Integer> {
    // 查询角色列表
    @MapKey("")
    List<Map<String,Object>> selectRoles();
    // 查询角色列表
    @MapKey("")
     List<Map<String,Object>> queryAllRoles(Integer id);


//    根据名字查询角色
    Role queryRoleByName(String roleName);

}
