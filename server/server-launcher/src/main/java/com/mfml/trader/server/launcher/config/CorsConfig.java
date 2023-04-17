package com.mfml.trader.server.launcher.config;

import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: caozhou
 * @create: 2022-07-18 10:02
 * @description:
 */
@Configuration
public class CorsConfig implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,set-cookie,x-requested-with");
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            // streamGPT add Content-Type, try to fix 406 error
            if (httpServletRequest.getPathInfo().contains("streamGPT")) {
                response.setHeader("Content-Type", "text/event-stream");
            }

            if ("OPTIONS".equals(httpServletRequest.getMethod())) {
                try {
                    response.getWriter().print("OK");
                    response.getWriter().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return ;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
