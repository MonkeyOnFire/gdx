package com.iskandar.gdx.dto;

import com.iskandar.gdx.po.User;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xlf on 2018/8/23.
 */
public class UserDto extends User {
    private String roleName;

    private List<Integer> roleIds = new ArrayList<>();

    private String roleIdsStr;// roleid字符串

    public String getRoleIdsStr() {
        return roleIdsStr;
    }

    public void setRoleIdsStr(String roleIdsStr) {
        this.roleIdsStr = roleIdsStr;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }
}
