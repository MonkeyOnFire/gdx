package com.iskandar.gdx.controller;

//import com.iskandar.gdx.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PageController {

    @RequestMapping("*")
    public String noFound() {
        return "404";
    }


    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @RequestMapping("404")
    public String pageError() {
        return "404";
    }
    @RequestMapping("blank")
    public String blank() {
        return "blank";
    }
    @RequestMapping("cards")
    public String cards() {
        return "cards";
    }
    @RequestMapping("buttons")
    public String buttons() {
        return "buttons";
    }
    @RequestMapping("charts")
    public String charts() {
        return "charts";
    }
    @RequestMapping("forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    @RequestMapping("register")
    public String register() {
        return "register";
    }
    @RequestMapping("tables")
    public String tables() {
        return "tables";
    }

    @RequestMapping("utilities-animation")
    public String utilitiesAnimation() {
        return "utilities-animation";
    }

    @RequestMapping("utilities-border")
    public String utilitiesBorder() {
        return "utilities-border";
    }

    @RequestMapping("utilities-other")
    public String utilitiesOther() {
        return "utilities-other";
    }


}