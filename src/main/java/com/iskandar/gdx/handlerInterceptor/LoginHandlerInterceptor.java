package com.iskandar.gdx.handlerInterceptor;


import com.iskandar.gdx.util.LoginUserUtil;
import com.iskandar.gdx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xlf on 2018/8/21.
 */
public class LoginHandlerInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /***
         * 1. 获取cookie信息中的id
         * 2. 通过id获取用户, 判断是否存在,以及id是否存在
         * 3. 通过抛异常, 到全局异常捕获! ,跳转到错误页面!
         * 4. 然后,自动回到登录页
         * */
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //AssertUtil.isNotLogin(null==userId || null==userService.queryUserById(userId),"用户不存在或者未登录");
        return true;//继续下边的操作
    }
}
