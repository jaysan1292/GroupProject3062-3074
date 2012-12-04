package com.jaysan1292.groupproject.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.jaysan1292.groupproject.WebAppCommon.ATTR_LOGIN;
import static com.jaysan1292.groupproject.WebAppCommon.SRV_LOGIN;

/**
 * Created with IntelliJ IDEA.
 * User: Kazedayz
 * Date: 03/12/12
 * Time: 11:13 AM
 */
public class PlayerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute(ATTR_LOGIN) == null) {
            response.sendRedirect(getServletContext().getContextPath() + SRV_LOGIN);
        }
    }
}
