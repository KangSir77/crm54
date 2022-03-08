package com.yjxxt.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.base.BaseService;
import com.yjxxt.bean.Role;
import com.yjxxt.bean.User;
import com.yjxxt.bean.UserRole;
import com.yjxxt.mapper.RoleMapper;
import com.yjxxt.mapper.UserMapper;
import com.yjxxt.mapper.UserRoleMapper;
import com.yjxxt.model.UserModel;
import com.yjxxt.query.UserQuery;
import com.yjxxt.utils.AssertUtil;
import com.yjxxt.utils.Md5Util;
import com.yjxxt.utils.PhoneUtil;
import com.yjxxt.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserService extends BaseService<User,Integer> {

    @Resource
    private UserMapper userMapper;

    @Autowired(required = false)
    private RoleMapper roleMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    //根据用户名和密码进行登录
    public UserModel userLogin(String userName,String userPwd){
        //校验用户和密码
        checkLoginParams(userName,userPwd);
        //查看用户是否存在
        User user = userMapper.selectUserByName(userName);
        AssertUtil.isTrue(user==null,"用户名已注销或者不存在");
        //密码校验,加密+比对==
        checkLoginPwd(userPwd, user.getUserPwd());
        //构建返回对象
        return builderUserInfo(user);

    }


    /**
     * 构建返回对象的
     * @param user
     * @return
     */
    private UserModel builderUserInfo(User user) {
        //实例化对象
        UserModel userModel=new UserModel();
        //加密
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        //返回
        return userModel;
    }


    /**
     * 校验用户密码和数据库中的密码是否匹配
     * @param userPwd
     * @param userPwd1
     */
    private void checkLoginPwd(String userPwd, String userPwd1) {
        //将输入的密码加密
        userPwd=Md5Util.encode(userPwd);
        //比对密码是否正确
        AssertUtil.isTrue(!userPwd.equals(userPwd1),"用户密码不正确");
    }


    /**
     * 验证用户名和用户密码(登录参数)
     * @param userName
     * @param userPwd
     */
    private void checkLoginParams(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码不能为空");
    }


    /**
     * 修改用户密码
     * @param userId 用户ID
     * @param oldPassword 原始密码
     * @param newPassword 新密码
     * @param confirmPwd 确认密码
     */
    public void updateUserPassword(Integer userId,String oldPassword,String newPassword,String confirmPwd){
        //根据ID查询用户信息
        User user=userMapper.selectByPrimaryKey(userId);
        //验证
        checkUserPasswordParams(user,oldPassword,newPassword,confirmPwd);
        //加密,修改用户密码
        user.setUserPwd(Md5Util.encode(newPassword));
        //判断是否修改成功
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改失败");
    
    }


    /**
     * 验证用户密码参数
     * @param user 当前用户对象
     * @param oldPassword
     * @param newPassword
     * @param confirmPwd
     */
    private void checkUserPasswordParams(User user, String oldPassword, String newPassword, String confirmPwd) {
        //当前用户是否存在
        AssertUtil.isTrue(user==null,"用户未登录或者已经注销");
        //原始密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"请输入原始密码");
        //原始密码和数据中的密码(加密)一致
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPassword)),"原始密码不正确");
        //新密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"请输入新密码");
        //新密码和原始密码不能一致
        AssertUtil.isTrue(oldPassword.equals(newPassword),"新密码和原始密码不能一样");
        //确认密码,不能为空
        AssertUtil.isTrue(StringUtils.isBlank(confirmPwd),"确认密码不能为空");
        //确认密码和新密码要一致
        AssertUtil.isTrue(!confirmPwd.equals(newPassword),"确认密码和新密码必须一致");
    }


    /**
     * 查询所有的销售人员
     * @return
     */
    public List<Map<String,Object>> queryAllSales(){
        List<Map<String, Object>> maps = userMapper.queryAllSales();
        return maps;
    }


    /**
     * 多条件分页查询用户数据
     * @param query
     * @return
     */
    public Map<String, Object> queryUserByParams (UserQuery query) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(query.getPage(), query.getLimit());
        PageInfo<User> pageInfo = new PageInfo<>(userMapper.selectByParams(query));
        map.put("code",0);
        map.put("msg", "");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }


    /**
     * 添加用户
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(User user) {
        // 1. 参数校验
        checkParams(user.getUserName(), user.getEmail(), user.getPhone());
        // 2. 设置默认参数
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        // 3. 执行添加，判断结果
        AssertUtil.isTrue(userMapper.insertSelective(user)<1, "用户添加失败！");
        //添加中间表t_user_role user_id role_id
        System.out.println(user.getId()+"--->"+user.getRoleIds());
        //
        // insert into tb_user (name,age) values(),(),()
        relaionUserRole(user.getId(),user.getRoleIds());
    }
    private void relaionUserRole(Integer userId, String roleIds) {
        /**
         * 用户角色分配
         * 原始角色不存在 添加新的角色记录
         * 原始角色存在 添加新的角色记录
         * 原始角色存在 清空所有角色
         * 原始角色存在 移除部分角色
         * 如何进行角色分配???
         * 如果用户原始角色存在 首先清空原始所有角色 添加新的角色记录到用户角色表
         */
        int count = userRoleMapper.countUserRoleByUserId(userId);
        if (count > 0) {
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count, "用户角色分配失败!");
        }
        if (StringUtils.isNotBlank(roleIds)) {
            //重新添加新的角色
            List<UserRole> userRoles = new ArrayList<UserRole>();
            for (String s : roleIds.split(",")) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(Integer.parseInt(s));
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoles.add(userRole);
            }
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoles) != userRoles.size(), "用户角色分配失败!");
        }
    }



    /**
     * 参数校验
     * @param userName
     * @param email
     * @param phone
     */
    private void checkParams(String userName, String email, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空！");
        // 验证用户名是否存在
        User temp = userMapper.selectUserByName(userName);
        AssertUtil.isTrue(null != temp, "该用户已存在！");
        AssertUtil.isTrue(StringUtils.isBlank(email), "请输入邮箱地址！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone), "手机号码格式不正确！");
    }


    /**
     * 更新用户
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        // 1. 参数校验
        // 通过id查询用户对象
        User temp = userMapper.selectByPrimaryKey(user.getId());
        // 判断对象是否存在
        AssertUtil.isTrue(temp == null, "待更新记录不存在！");
        // 验证参数
        checkUser(user.getUserName(),user.getEmail(),user.getPhone());
        // 2. 设置默认参数
        temp.setUpdateDate(new Date());
        // 3. 执行更新，判断结果
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "用户更新 失败！");

        relaionUserRole(user.getId(),user.getRoleIds());
    }

    private void checkUser(String userName, String email, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(email), "请输入邮箱地址！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone), "手机号码格式不正确！");
    }


    /**
     * 删除用户
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserByIds(Integer[] ids) {
        AssertUtil.isTrue(null==ids || ids.length == 0,"请选择待删除的用户记录!");
        AssertUtil.isTrue(deleteBatch(ids) != ids.length,"用户记录删除失败!");
    }


    /**
     * 修改用户信息
     * @param user
     */
    public void changeUser(User user){
        //修改用户的id存在
        User temp=userMapper.selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(temp==null,"待修改的记录不存在");
        //验证
        checkUser(user.getUserName(),user.getEmail(),user.getPhone());
        //默认值
        user.setUpdateDate(new Date());
        //修改是否成功
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改失败了");
        //修改角色和用户的关系
        relaionUserRole(user.getId(),user.getRoleIds());
    }


    /**
     * 批量删除
     */
    public void removeIds(Integer[] ids){
        //验证
        AssertUtil.isTrue(ids==null || ids.length==0,"选择数据");
        //删除是否成功
        AssertUtil.isTrue(userMapper.deleteBatch(ids)!=ids.length,"删除异常");
        //遍历
        for (Integer userId: ids) {
            int count=userRoleMapper.countUserRoleByUserId(userId);
            if(count>0){
                //删除原来的角色
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"角色删除失败");
            }
        }
    }
}

