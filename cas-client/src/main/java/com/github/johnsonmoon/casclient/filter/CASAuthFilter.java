package com.github.johnsonmoon.casclient.filter;

import com.github.johnsonmoon.casclient.Main;
import org.jasig.cas.client.Protocol;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Create by johnsonmoon at 2019/1/30 09:48.
 */
public class CASAuthFilter extends AbstractCasFilter {
    private static Logger logger = LoggerFactory.getLogger(CASAuthFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        final HttpSession session = request.getSession(false);
        final Assertion assertion = session != null ? (Assertion) session.getAttribute(CONST_CAS_ASSERTION) : null;


        /**
         * TODO
         * Check whether there has already been a user session of this request.
         * If there has been, then pass the request to other filters in {@code FilterChain}
         */
        if (assertion != null) {
            filterChain.doFilter(request, response);
            return;
        }

        /**
         * TODO
         * If there is no user session, while the request contains a {@code ticket} parameter,
         * it means that a user has already loggedIn in the CAS-Server, redirect back to our application,
         * then pass the request to other filters in {@code FilterChain}, in order to verify
         * the ticket or so.
         */
        final String ticket = retrieveTicketFromRequest(request);
        if (CommonUtils.isNotBlank(ticket)) {
            filterChain.doFilter(request, response);
            return;
        }

        /**
         * TODO
         * If there is no user session, then get the redirectUrl ready,
         * and redirect the request to CAS-Server login page.
         */
        final String serviceUrl = constructServiceUrl(request, response);
        logger.info("Service url: {}", serviceUrl);
        final String redirectUrl = CommonUtils.constructRedirectUrl(
                Main.casServerLoginUrl,
                getProtocol().getServiceParameterName(),
                serviceUrl, false, false);

        logger.info("Redirecting to: \"{}\"", redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    public CASAuthFilter(Protocol protocol) {
        super(protocol);
    }

    public CASAuthFilter() {
        this(Protocol.CAS2);
    }
}
