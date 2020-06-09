package com.iskandar.gdx.dao;

import com.iskandar.gdx.base.BaseDao;
import com.iskandar.gdx.dto.ModuleDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RoleMapper extends BaseDao{
    public List<Map> queryAllRoles();

    public List<ModuleDto> queryAllModulesByRoleId(Integer roleId);
}