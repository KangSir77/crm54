package com.yjxxt.mapper;

import com.yjxxt.base.BaseMapper;
import com.yjxxt.bean.User;
import com.yjxxt.bean.UserRole;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {
    //根据用户名查询对象
     User selectUserByName(String userName);

    // 查询所有的销售人员
    @MapKey("")
     List<Map<String,Object>> queryAllSales();

    UserRole queryUserByUserName(String userName);
}