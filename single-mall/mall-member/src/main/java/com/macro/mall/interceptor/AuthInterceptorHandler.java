package com.macro.mall.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.macro.mall.JwtKit;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.common.exception.BusinessException;
import com.macro.mall.config.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author superUser
 * @Date 2021/6/13
 */
@Slf4j
public class AuthInterceptorHandler implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtKit jwtKit;

    /**
     * url前置拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("进入前置拦截器");

        //spring session
//        if(request.getSession().getAttribute("member") != null){
//            return true;
//        }

        //jwt
        String authorization = request.getHeader(jwtProperties.getTokenHeader());
        log.info("authorization:" + authorization);

        //校验token
        String message = null;
        if(StringUtils.isNotEmpty(authorization) && authorization.startsWith(jwtProperties.getTokenHead())){
            String authtoken = authorization.substring(jwtProperties.getTokenHead().length());
            Claims claims = null;
            try {
                claims = jwtKit.parseJwtToken(authtoken);
                if(claims != null){
//                    request.setAttribute(GLOBAL_JWT_USER_INFO,claims);
                    return true;
                }
            } catch (BusinessException e) {
                log.error(message = (e.getMessage() + ":" + authtoken));
            }

        }

        print(response,"您没有权限访问，请重新登录");
        return false;
    }

    private void print(HttpServletResponse response, String message) throws Exception {
        /**
         * 设置响应式头
         */
        response.setHeader("Content-Type","applicaiton/json");
        response.setCharacterEncoding("utf-8");
        String result = new ObjectMapper().writeValueAsString(CommonResult.forbidden(message));
        response.getWriter().print(result);
    }
}
