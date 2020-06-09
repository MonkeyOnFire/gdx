package com.iskandar.gdx.dao;

import com.iskandar.gdx.base.BaseDao;
import com.iskandar.gdx.po.UserRole;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleMapper extends BaseDao<UserRole> {
    public Integer deleteUserRoleBatch(Integer userId);
    public Integer queryUserRolesByUserId(Integer userId);

    public Integer deleteUserRoleBatch2(Integer roleId);
    public Integer queryUserRolesByRoleId(Integer roleId);
}