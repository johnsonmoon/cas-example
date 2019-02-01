package com.github.johnsonmoon.casclient;

import com.github.johnsonmoon.casclient.filter.CASAuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletContextInitializer;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.EnumSet;

/**
 * Create by johnsonmoon at 2019/1/30 09:24.
 */
@ServletComponentScan
@SpringBootApplication
public class Main implements ServletContextInitializer {
    //TODO modified
    public static final String serverName = "http://127.0.0.1:8500";
    public static final String casServerLoginUrl = "https://example.com/cas/login";
    public static final String casServerUrlPrefix = "https://example.com/cas";
    public static final String casLogoutUrl = "https://example.com/cas/logout";

    public static void main(String... args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        // 用于单点退出
        servletContext.addListener(org.jasig.cas.client.session.SingleSignOutHttpSessionListener.class.getName());

        FilterRegistration.Dynamic CASSignOutFilter = servletContext.addFilter("CASSignOutFilter", org.jasig.cas.client.session.SingleSignOutFilter.class.getName());
        CASSignOutFilter.setInitParameter("casServerUrlPrefix", casServerUrlPrefix);
        CASSignOutFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");

        // 该过滤器负责用户的认证工作，必须启用它，因业务需求，重新实现CasAuthFilter
        FilterRegistration.Dynamic CASFilter = servletContext.addFilter("CASFilter", CASAuthFilter.class.getName());
        CASFilter.setInitParameter("casServerLoginUrl", casServerLoginUrl);
        CASFilter.setInitParameter("serverName", serverName);
        CASFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");

        // 该过滤器负责对Ticket的校验工作，必须启用它
        FilterRegistration.Dynamic CASValiFilter = servletContext.addFilter("CASValiFilter", org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter.class.getName());
        CASValiFilter.setInitParameter("casServerUrlPrefix", casServerUrlPrefix);
        CASValiFilter.setInitParameter("serverName", serverName);
        CASValiFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");

        /*
         *   该过滤器负责实现HttpServletRequest请求的包裹，比如允许开发者通过HttpServletRequest的getRemoteUser()方法获得SSO登录用户的登录名，可选配置。
         */
        FilterRegistration.Dynamic CASWrapperFilter = servletContext.addFilter("CASWrapperFilter", org.jasig.cas.client.util.HttpServletRequestWrapperFilter.class.getName());
        CASWrapperFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
    }
}
