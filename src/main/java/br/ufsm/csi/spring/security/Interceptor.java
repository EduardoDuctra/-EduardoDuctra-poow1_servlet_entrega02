package br.ufsm.csi.spring.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component //o spring intancia o objeto
public class Interceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse responde, Object handler) throws IOException {
        String url = request.getRequestURI();

        if(url.equals("/") || url.equals("/login")) {
            return true;
        }

        if (url.startsWith("/css/") || url.startsWith("/js/") || url.startsWith("/assets/")) {
            return true;
        }

        if(request.getSession().getAttribute("user") == null) {
            responde.sendRedirect("/");
            return false;
        }
        return true;
    }
}
