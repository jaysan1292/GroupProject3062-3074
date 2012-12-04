package com.jaysan1292.groupproject.web.servlets;

import com.jaysan1292.groupproject.client.ScavengerClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.jaysan1292.groupproject.WebAppCommon.ATTR_LOGIN;
import static com.jaysan1292.groupproject.WebAppCommon.JSP_LOGIN;

/**
 * Created with IntelliJ IDEA.
 * User: Kazedayz
 * Date: 03/12/12
 * Time: 10:59 AM
 */
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // return login page
        request.getRequestDispatcher(JSP_LOGIN).include(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // process login credentials
        String user = request.getParameter("username");
        String pass = request.getParameter("password");

        ScavengerClient client = new ScavengerClient(user, pass);

        request.getSession().setAttribute(ATTR_LOGIN, client);

        //send to welcome page
    }
}
