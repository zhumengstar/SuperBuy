package com.duckdream.superbuy.security;


import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当需要身份认证时跳转到这
 */
@RestController
public class SecurityController {

    private RequestCache requestCache=new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy;

    @RequestMapping("/authentication/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResponse requireAuthenttication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest=requestCache.getRequest(request,response);
        if(savedRequest!=null){

            String target=savedRequest.getRedirectUrl();

            if(StringUtils.endsWithIgnoreCase(target,".html")){
                redirectStrategy.sendRedirect(request,response,"/login");
            }

        }
        return new SimpleResponse("访问的服务需要身份认证，请引导到登录页");
    }
//
//    @RequestMapping("/authentication/form")
//    public void form(){
//
//    }
}
