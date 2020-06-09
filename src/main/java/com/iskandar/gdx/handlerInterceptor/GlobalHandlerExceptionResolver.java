package com.iskandar.gdx.handlerInterceptor;

import com.alibaba.fastjson.JSON;
import com.iskandar.gdx.exception.LoginException;
import com.iskandar.gdx.exception.ParamException;
import com.iskandar.gdx.model.ResultInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * Created by xlf on 2018/8/21.
 */
@Component
public class GlobalHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception e) {
        // 创建默认返回视图
        ModelAndView mv = createDefaultModelAndView(request);

        /***
         * 1. 通过handler(目标方法),获取Method相关信息
         * 2. 通过Method获取是否在当前方法中含有@ResponseBody
         * 3. 如果有代表时返回json
         * */
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        ResponseBody responseBody = method.getAnnotation(ResponseBody.class);
        if(null==responseBody){
            // 不是json接口
            // 对自定义异常进行单独判断
            if(e instanceof ParamException){
                ParamException ex = (ParamException) e;
                mv.setViewName("error");
                mv.addObject("errorMsg", ex.getMsg());
            }

            if(e instanceof LoginException){
                LoginException ex = (LoginException) e;
                mv.setViewName("login_error");
                mv.addObject("errorMsg", ex.getMsg());
            }
            return mv;
        }else{
            // 是json接口
            ResultInfo info = new ResultInfo();
            info.setCode(300);
            info.setMsg("参数异常");

            if(e instanceof ParamException){
                ParamException ex = (ParamException) e;
                info.setCode(ex.getCode());
                info.setMsg(ex.getMsg());
            }


            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            PrintWriter pw =null;
            try {
                pw=response.getWriter();
                pw.write(JSON.toJSONString(info));
                pw.flush();
            } catch (IOException e2) {
                e2.printStackTrace();
            }finally {
                if(null!=pw){
                    pw.close();
                }
            }
            return null;
        }

    }

    private ModelAndView createDefaultModelAndView(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error");
        mv.addObject("errorMsg", "系统繁忙");
        mv.addObject("ctx", request.getContextPath());
        mv.addObject("uri", request.getRequestURI());
        System.out.println(request.getRequestURI());
        return mv;
    }


}
