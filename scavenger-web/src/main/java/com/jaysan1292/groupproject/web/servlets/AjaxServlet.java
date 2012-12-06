package com.jaysan1292.groupproject.web.servlets;

import com.jaysan1292.groupproject.WebAppCommon;
import com.jaysan1292.groupproject.data.BaseEntity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Kazedayz
 * Date: 06/12/12
 * Time: 10:52 AM
 */
public class AjaxServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> queryParams = WebAppCommon.queryStringToMap(request);
        String type = queryParams.get("type");
        String id = queryParams.get("id");

        if (StringUtils.isBlank(type) || StringUtils.isBlank(id)) {
            throw new ServletException("Error its null");
        }

        File root = new File(getServletContext().getRealPath("/"));
        String filename = UUID.randomUUID() + "_item.jsp";

        String jsp = "<%@ page pageEncoding=\"UTF-8\" %>" +
                     "<%@ taglib prefix=\"t\" tagdir=\"/WEB-INF/tags\" %>";

        String beanTemplate = "<jsp:useBean id=\"item\" scope=\"request\" type=\"%s\"/>";
        String tagTemplate = "<t:%s item=${item}/>";

        //TODO: Finish web service client
        BaseEntity item = null;
        if (type.equals("scavengerhunts")) {
            jsp += String.format(beanTemplate, "com.jaysan1292.groupproject.data.ScavengerHunt");
            jsp += String.format(tagTemplate, "item_scavengerhunt_form");
            // item =  GET FROM WEB SERVICE
        } else if (type.equals("challenges")) {
            jsp += String.format(beanTemplate, "com.jaysan1292.groupproject.data.Challenge");
            // item =  GET FROM WEB SERVICE
            jsp += String.format(tagTemplate, "item_challenge_form");
        } else if (type.equals("checkpoints")) {
            jsp += String.format(beanTemplate, "com.jaysan1292.groupproject.data.Checkpoint");
            jsp += String.format(tagTemplate, "item_checkpoint_form");
            // item =  GET FROM WEB SERVICE
        } else if (type.equals("paths")) {
            jsp += String.format(beanTemplate, "com.jaysan1292.groupproject.data.Path");
            jsp += String.format(tagTemplate, "item_path_form");
            // item =  GET FROM WEB SERVICE
        } else if (type.equals("players")) {
            jsp += String.format(beanTemplate, "com.jaysan1292.groupproject.data.Player");
            jsp += String.format(tagTemplate, "item_player_form");
            // item =  GET FROM WEB SERVICE
        } else if (type.equals("teams")) {
            jsp += String.format(beanTemplate, "com.jaysan1292.groupproject.data.Team");
            jsp += String.format(tagTemplate, "item_team_form");
            // item =  GET FROM WEB SERVICE
        } else {
            throw new ServletException("Invalid type: " + type);
        }

        File itemJsp = new File(root, filename);
        write(jsp, itemJsp);

        request.setAttribute("item", item);
        request.getRequestDispatcher('/' + filename).forward(request, response);
        itemJsp.delete();
    }

    @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
    private static void write(String content, File file) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            writer.write(content);
        } catch (IOException e) {
            WebAppCommon.log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }
}
