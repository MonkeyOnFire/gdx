package com.iskandar.gdx.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iskandar.gdx.base.BaseService;
import com.iskandar.gdx.dao.UserMapper;
import com.iskandar.gdx.dao.UserRoleMapper;
import com.iskandar.gdx.dto.UserDto;
import com.iskandar.gdx.model.UserInfo;
import com.iskandar.gdx.po.User;
import com.iskandar.gdx.po.UserRole;
import com.iskandar.gdx.query.UserQuery;
import com.iskandar.gdx.util.AssertUtil;
import com.iskandar.gdx.util.Md5Util;
import com.iskandar.gdx.util.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by xlf on 2018/8/20.
 */
@Service
public class UserService extends BaseService<User> {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;


    public UserInfo login(String userName, String userPwd){
//        ResultInfo info = new ResultInfo();
        /***
         * 1. 接收参数,并校验
         * 2. 通过用户名查询用户
         * 3. 判断密码是否一致
         *          前台传明文密码,后台查询出的时加密密码
         * */
//        if(StringUtils.isBlank(userName)){
//            info.setCode(300);
//            info.setMsg("用户名为空");
//        }
//        if(StringUtils.isBlank(userPwd)){
//            info.setCode(300);
//            info.setMsg("密码为空");
//        }
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码为空");

        User user = userMapper.queryUserByName(userName);
        AssertUtil.isTrue(null == user, "用户不存或已注销");

        AssertUtil.isTrue(!Md5Util.encode(userPwd).equals(user.getUserPwd()), "用户名密码不正确");

        /***
         * 返回用户信息
         * */
        UserInfo userInfo = new UserInfo();
        userInfo.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userInfo.setUserName(user.getUserName());
        userInfo.setTrueName(user.getTrueName());
        return userInfo;
//        return info;
    }

    public User queryUserById(Integer id) {
        return userMapper.queryUserById(id);
    }

    /**
     * 修改密码
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @param userId
     */
    public void updateUserPwd(String oldPassword, String newPassword,
                              String confirmPassword, Integer userId){
        /***
         * 1. 参数校验
         * 2. 通过id查询user,比对密码是否正确
         * 3. 修改用户密码
         * */
        checkUpdateUserPwdParams(oldPassword, newPassword, confirmPassword, userId);

        User user = userMapper.queryUserById(userId);
        AssertUtil.isTrue(null==user, "用户不存在或已注销");

        AssertUtil.isTrue(!Md5Util.encode(oldPassword).equals(user.getUserPwd()), "原始密码不正确");

        /***
         * 1. 存加密密码
         * 2. 保证事务回滚
         * */
        String encodePassword = Md5Util.encode(newPassword);
        user.setUserPwd(encodePassword);// 存加密密码
        AssertUtil.isTrue(userMapper.updateUserPwd(user)<1,"用户密码修改失败");
    }

    private void checkUpdateUserPwdParams(String oldPassword, String newPassword, String confirmPassword, Integer userId) {
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword), "旧密码为空");
        AssertUtil.isTrue(StringUtils.isBlank(newPassword), "新密码为空");
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword), "确认密码为空");
        AssertUtil.isTrue(!newPassword.equals(confirmPassword), "两次密码不一致");
        AssertUtil.isTrue(null == userId, "用户未登录");
}

    public List<Map> queryCustomerManagers() {
        return userMapper.queryCustomerManagers();
    }

    public Map<String,Object> queryUserForPag(UserQuery baseQuery) throws DataAccessException {
        PageHelper.startPage(baseQuery.getPageNum(),baseQuery.getPageSize());
        List<UserDto> entities=userMapper.queryUserByParams(baseQuery);
        PageInfo<UserDto> pageInfo=new PageInfo<UserDto>(entities);
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("total",pageInfo.getTotal());

        /***
         * 通过roleIdsStr 把数据放入 list 的 roleIds中
         * 1,2,3  ->  list集合
         * */
        List<UserDto> userDtos = pageInfo.getList();
        if(!CollectionUtils.isEmpty(userDtos)&&userDtos.size()>0){
            for (UserDto userDto : userDtos){
                String roleIdsStr = userDto.getRoleIdsStr();
                if(null!=roleIdsStr&&!"".equals(roleIdsStr)){
                    List<Integer> roleIds = userDto.getRoleIds();// roleId的集合
                    String[] roldIdStrs = roleIdsStr.split(",");
                    for (int i = 0; i < roldIdStrs.length; i++) {
                        roleIds.add(Integer.valueOf(roldIdStrs[i]));
                    }
                }
            }
        }
        map.put("rows",pageInfo.getList());
        return map;
    }

    /***
     * 添加和更新
     * */
    public void saveOrUpdate(UserDto user) {
        /***
         * 1. 校验
         * 2. 参数补全
         *      1) 密码 (注意: 是加密的密码 123456)
         *      2) isValid
         *      3) createDate
         *      4) updateDate
         *  3. id区分添加和跟新
         *  4. user操作 和 t_user_role中间表操作
         * */

        checkUserParams(user.getUserName(),user.getTrueName());
        Integer id  = user.getId();
        Date now = new Date();
        user.setUpdateDate(now);
        //添加操作
        if(null == id) {
            /***
             * 用户名唯一校验
             * */
            AssertUtil.isTrue(null != userMapper.queryUserByName(user.getUserName()),"用户名已被注册");
            //添加用户操作
            user.setUserPwd(Md5Util.encode("123456"));
            user.setIsValid(1);
            user.setCreateDate(now);
            AssertUtil.isTrue(userMapper.save(user) < 1, "用户添加失败");

            //添加用户角色信息
            List<Integer> roleIds = user.getRoleIds();
            if(null != roleIds && 0 < roleIds.size()) {
                Integer newUserId = userMapper.queryUserByName(user.getUserName()).getId();
                List<UserRole> userRoles = new ArrayList<>();
                for (Integer roleId : roleIds) {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(newUserId);
                    userRole.setRoleId(roleId);
                    userRole.setCreateDate(now);
                    userRole.setUpdateDate(now);
                    userRoles.add(userRole);
                }

                AssertUtil.isTrue(userRoleMapper.saveBatch(userRoles) < userRoles.size(),"用户角色添加失败");
            }
            //更新操作
        }else {
            /***
             * 用户名不能修改校验
             * */
            String oldUserName = userMapper.queryUserById(id).getUserName();
            AssertUtil.isTrue(!oldUserName.equals(user.getUserName()),"用户名不允许修改");

            //更新
            AssertUtil.isTrue(userMapper.update(user) < 1,"用户信息更新失败");
            /***
             * 更新角色
             * 1. 删除该用户所有角色
             * 2. 批量添加该用户所有角色
             * */
            /***
             * 首先查询用户是否有角色. 有删除再添加; 没有直接添加
             * */
            Integer nums = userRoleMapper.queryUserRolesByUserId(id);
            if (null != nums && nums >0 ) {
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleBatch(id) < nums,"删除用户角色失败");
            }
            //添加角色
            List<Integer> roleIds = user.getRoleIds();
            if(null != roleIds && roleIds.size() > 0) {
                List<UserRole> userRoles = new ArrayList<>();
                for (Integer roleId : roleIds) {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(id);
                    userRole.setRoleId(roleId);
                    userRole.setCreateDate(now);
                    userRole.setUpdateDate(now);
                    userRoles.add(userRole);
                }
                AssertUtil.isTrue(userRoleMapper.saveBatch(userRoles) < userRoles.size(),"用户角色更新失败");
            }
        }
    }

    private void checkUserParams(String userName, String trueName) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名为空");
        AssertUtil.isTrue(StringUtils.isBlank(trueName), "真实姓名为空");
    }

    public void deleteUser(Integer[] ids) {
        for (int i=0;i<ids.length;i++) {
            Integer id = ids[i];
            AssertUtil.isTrue(null == id || null==queryUserById(id),"用户不存在");
            AssertUtil.isTrue(userMapper.delete(id)<1 ,"用户删除失败");
            /***
             * 首先查询用户是否有角色. 有删除再删除
             * */
            Integer nums = userRoleMapper.queryUserRolesByUserId(id);
            if (null !=nums && nums > 0) {
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleBatch(id) < nums , "用户角色删除失败");
            }

        }
    }
}
