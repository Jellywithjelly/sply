package com.ly.vrps.config.security;

import com.ly.vrps.common.dao.TBusFilmDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定拦截器
 * @author sunkl
 * @date 2018/12/23
 */
@Service
public class FilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {
 
    @Autowired
    private FilterInvocationSecurityMetadataSource securityMetadataSource;

    @Autowired
    private TBusFilmDao dao;
 
    @Autowired
    public void setMyAccessDecisionManager(SpLyAccessDecisionManager spLyAccessDecisionManager) {
        super.setAccessDecisionManager(spLyAccessDecisionManager);
    }
 
 
    @Override
    public void init(FilterConfig filterConfig) {
 
    }
 
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
      /*  获取请求参数
        String param = request.getParameter("key");
        String key = dao.getKey();
        if(param!=null && !param.equals("")){
            String lock = param.substring(param.lastIndexOf("=") + 1);
            if(lock.equals(key)){
                FilterInvocation fi = new FilterInvocation(request, response, chain);
                invoke(fi);
            }
        }*/
        //转换类型
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        //获取请求界面的路径
        String a = req.getRequestURI();
        if (a.contains(".css") || a.contains(".js") || a.contains(".png") || a.contains(".jpg")
                || a.contains("xl")||a.contains("login") || a.contains("user") ||
                a.contains("film") || a.contains("catalog") || a.contains("video") || a.contains("pay")) {
            //如果发现是css或者js文件，直接放行
            FilterInvocation fi = new FilterInvocation(request, response, chain);
            invoke(fi);
        }
        //在else中放对网页过滤的代码
        else {
            String param = request.getParameter("key");
            String key = dao.getKey();
            if (param != null && !param.equals("")) {
                String lock = param.substring(param.lastIndexOf("=") + 1);
                if (lock.equals(key)) {
                    FilterInvocation fi = new FilterInvocation(request, response, chain);
                    invoke(fi);
                }
            }

        }
    }
 
 
    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
        //执行下一个拦截器
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }
 
    @Override
    public void destroy() {
 
    }
 
    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }
 
    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }
}
