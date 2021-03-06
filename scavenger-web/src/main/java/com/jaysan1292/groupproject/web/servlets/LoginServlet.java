package com.jaysan1292.groupproject.web.servlets;

import com.jaysan1292.groupproject.WebAppCommon;
import com.jaysan1292.groupproject.client.ScavengerClient;
import com.jaysan1292.groupproject.service.security.AuthorizationException;
import com.sun.jersey.api.client.ClientHandlerException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;

import static com.jaysan1292.groupproject.WebAppCommon.ATTR_CLIENT;
import static com.jaysan1292.groupproject.WebAppCommon.ATTR_LOGIN;

/**
 * Created with IntelliJ IDEA.
 * User: Kazedayz
 * Date: 03/12/12
 * Time: 10:59 AM
 */
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("logout") != null) {
            request.getSession().invalidate();
        }
        response.sendRedirect(request.getServletContext().getContextPath() + '/');
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // process login credentials
        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        String host = request.getParameter("host");

        ScavengerClient client;
        try {
            URI hostUri = null;
            if (host != null) hostUri = URI.create(host);

            // if hostUri turns out to be null, a default host will be selected
            // from localhost or my server URL (http://jaysan1292.com)
            client = new ScavengerClient(hostUri, user, pass);
        } catch (AuthorizationException e) {
            WebAppCommon.log.error(e.getMessage(), e);
            request.setAttribute("errorMessage", "Username or password was incorrect.");
            request.getRequestDispatcher("/").forward(request, response);
            return;
        } catch (ClientHandlerException e) {
            String errorMessage;
            if (e.getCause().getClass() == ConnectException.class) {
                errorMessage = "Hostname was incorrect.";
            } else {
                errorMessage = e.getMessage();
            }
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/").forward(request, response);
            return;
        }

        request.getSession().setAttribute(ATTR_CLIENT, client);
        request.getSession().setAttribute(ATTR_LOGIN, true);
        //send to welcome page
        response.sendRedirect(request.getServletContext().getContextPath() + "/home.jsp");
    }
}
