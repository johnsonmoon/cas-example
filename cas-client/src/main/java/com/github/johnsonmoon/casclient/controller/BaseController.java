package com.github.johnsonmoon.casclient.controller;

import com.github.johnsonmoon.casclient.Main;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by johnsonmoon at 2019/1/30 10:19.
 */
@RestController
@RequestMapping("/client/api/v1/base")
public class BaseController {
    private static Logger logger = LoggerFactory.getLogger(BaseController.class);

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) {
        String casLogoutUrl = Main.casLogoutUrl;
        try {
            if (response != null) {
                response.sendRedirect(casLogoutUrl);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    @GetMapping("/gene_data")
    public Map<String, Object> generateData(HttpServletRequest request) {
        /**
         * TODO get loggedIn user information
         */
        String userName = null;
        AttributePrincipal attributePrincipal = (AttributePrincipal) request.getUserPrincipal();
        if (attributePrincipal != null) {
            userName = attributePrincipal.getName();
            logger.info(String.format("User name: [%s]", userName));
            //Map<String, Object> map =  attributePrincipal.getAttributes();
        }


        Map<String, Object> result = new HashMap<>();
        long time = System.currentTimeMillis();
        result.put("time", time);
        result.put("name", (userName == null ? "johnson_" : userName));
        result.put("desc", "Hello!__" + time);
        return result;
    }
}
