package com.dk.web.session.controller;

import com.dk.web.session.entity.Account;
import com.dk.web.session.entity.ResultCode;
import com.dk.web.session.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author dkay
 * @version 1.0
 */
@Controller
public class SessionController {

    @ResponseBody
    @RequestMapping("/login")
    public ResultCode<String> login(HttpServletRequest request, String accountName, String pwd) {
        ResultCode<String> resultCode = ResultCode.defaultFailResultCode();
        if (StringUtils.hasText(accountName) && StringUtils.hasText(pwd)) {
            User defaultUser = User.defaultUser();
            Account account = defaultUser.getAccount();
            if (accountName.equals(account.getAccount()) && pwd.equals(account.getPwd())) {
                request.getSession().setAttribute("user", defaultUser);
                resultCode.setMsg("login success");
                resultCode.setCode(10000);
                return resultCode;
            }
        }

        return resultCode;
    }

    @ResponseBody
    @RequestMapping("/logout")
    public ResultCode<String> logout(HttpServletRequest request) {
        ResultCode<String> resultCode = ResultCode.defaultFailResultCode();
        request.getSession().removeAttribute("user");
        resultCode.setMsg("logout success");
        resultCode.setCode(10000);
        return resultCode;
    }

    @ResponseBody
    @RequestMapping("/index")
    public ResultCode<String> index(HttpServletRequest request) {
        ResultCode<String> resultCode = new ResultCode<>();

        Object attribute = request.getSession().getAttribute("user");
        if (Objects.isNull(attribute)) {
            resultCode.setCode(1000102);
            resultCode.setMsg("please login");
            return resultCode;
        }

        resultCode.setMsg("index message");
        resultCode.setCode(10000);
        return resultCode;
    }
}
