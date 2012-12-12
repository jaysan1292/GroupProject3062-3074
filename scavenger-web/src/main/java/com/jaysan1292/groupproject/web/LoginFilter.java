package com.jaysan1292.groupproject.web;

import com.jaysan1292.groupproject.WebAppCommon;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Date: 10/12/12
 * Time: 8:17 PM
 *
 * @author Jason Recillo
 */
@WebFilter
public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        Object client = request.getSession().getAttribute(WebAppCommon.ATTR_CLIENT);

        if (request.getRequestURI().equals(request.getContextPath() + "/home.jsp")) {
            if (client == null) {
                response.sendRedirect(request.getServletContext().getContextPath());
            }
        } else if (request.getRequestURI().equals(request.getContextPath() + '/')) {
            if (client != null) {
                response.sendRedirect(request.getServletContext().getContextPath() + "/home.jsp");
            }
        }

        chain.doFilter(request, response);
    }

    public void init(FilterConfig config) throws ServletException {}
}
