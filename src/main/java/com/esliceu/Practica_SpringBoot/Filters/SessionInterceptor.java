package com.esliceu.Practica_SpringBoot.Filters;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception{
        HttpSession session = req.getSession();
        String user = (String) session.getAttribute("userName");
        req.setAttribute("user", user);
        if (user == null)
            return false;
        return true;
    }
}

