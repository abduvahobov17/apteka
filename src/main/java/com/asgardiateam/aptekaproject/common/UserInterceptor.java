package com.asgardiateam.aptekaproject.common;

import com.asgardiateam.aptekaproject.enums.Lang;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.asgardiateam.aptekaproject.constants.HeaderConstants.LANG;

@Component
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        Lang lang = Lang.tryFindLang(request.getHeader(LANG));

        ThreadLocalSingleton.setLang(lang);

        return true;
    }
}