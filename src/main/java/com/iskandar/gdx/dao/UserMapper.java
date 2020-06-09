package com.iskandar.gdx.dao;

import com.iskandar.gdx.base.BaseDao;
import com.iskandar.gdx.dto.UserDto;
import com.iskandar.gdx.po.User;
import com.iskandar.gdx.query.UserQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by xlf on 2018/8/20.
 */
@Repository
public interface UserMapper extends BaseDao<User>{

    public User queryUserByName(String userName);

    public User queryUserById(Integer id);

    public Integer updateUserPwd(User user);

    public List<Map> queryCustomerManagers();

    List<UserDto> queryUserByParams(UserQuery baseQuery);
}
