package com.iskandar.gdx.query;

import com.iskandar.gdx.base.BaseQuery;

/**
 * Created by Iskandar on 2018/8/31.
 */
public class RoleQuery extends BaseQuery{
    private String roleName;
    private String createDate;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
