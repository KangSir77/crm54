package com.yjxxt.mapper;

import com.yjxxt.base.BaseMapper;
import com.yjxxt.bean.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    //统计角色拥有的资源id数量
    int countModulsByRoleId(Integer roleId);
    //删除当前角色拥有的资源信息
    Integer deleteModuleByRoleId(Integer roleId);


    List<Integer> selectRoleHasAllModuleIds(Integer roleId);

    //
    List<String> selectUserHasRolePermission(Integer userId);
}