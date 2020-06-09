package com.iskandar.gdx.controller;

import com.iskandar.gdx.base.BaseController;
import com.iskandar.gdx.dto.UserDto;
import com.iskandar.gdx.exception.ParamException;
import com.iskandar.gdx.model.ResultInfo;
import com.iskandar.gdx.model.UserInfo;
import com.iskandar.gdx.query.UserQuery;
import com.iskandar.gdx.service.UserService;
import com.iskandar.gdx.util.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by xlf on 2018/8/20.
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;


    @RequestMapping("login")
    @ResponseBody
    public ResultInfo login(String userName, String userPwd){
        ResultInfo info = new ResultInfo();
        try {
            UserInfo userInfo = userService.login(userName, userPwd);
            info.setCode(200);
            info.setMsg("登陆成功");
            info.setResult(userInfo);
        } catch (ParamException e){
            info.setCode(300);
            info.setMsg(e.getMsg());
            e.printStackTrace();
        } catch (Exception e){
            info.setCode(300);
            info.setMsg("参数异常");
            e.printStackTrace();
        }
        return info;
    }

    @RequestMapping("updateUserPwd")
    @ResponseBody
    public ResultInfo updateUserPwd(String oldPassword, String newPassword,
                                    String confirmPassword, HttpServletRequest request){
        ResultInfo info = new ResultInfo();
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        try {
            userService.updateUserPwd(oldPassword, newPassword, confirmPassword, userId);
            info.setCode(200);
            info.setMsg("密码修改成功");
        } catch (ParamException e){
            info.setCode(300);
            info.setMsg(e.getMsg());
        } catch (Exception e){
            info.setCode(300);
            info.setMsg("参数异常");
        }
        return info;
    }

    @ResponseBody
    @RequestMapping("queryCustomerManagers")
    public List<Map> queryCustomerManagers() {
        return userService.queryCustomerManagers();
    }

    @RequestMapping("index")
    public String index(){
        return "user";
    }

    @RequestMapping("queryUserByParams")
    @ResponseBody
    public Map<String, Object> queryUserByParams(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer rows,
            UserQuery query){
        query.setPageNum(page);
        query.setPageSize(rows);

        return userService.queryUserForPag(query);
    }
    @RequestMapping("saveOrUpdateUser")
    @ResponseBody
    public ResultInfo saveOrUpdateUser(UserDto user) {
        userService.saveOrUpdate(user);
        return success("操作成功");
    }

    @RequestMapping("deleteUser")
    @ResponseBody
    public ResultInfo  deleteUser(Integer[] ids){
        userService.deleteUser(ids);
        return success("操作成功");
    }

}
