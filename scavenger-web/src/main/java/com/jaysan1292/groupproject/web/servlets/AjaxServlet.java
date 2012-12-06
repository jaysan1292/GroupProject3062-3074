package com.jaysan1292.groupproject.web.servlets;

import com.jaysan1292.groupproject.WebAppCommon;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Kazedayz
 * Date: 06/12/12
 * Time: 10:52 AM
 */
public class AjaxServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> queryParams = WebAppCommon.queryStringToMap(request);

    }
}
