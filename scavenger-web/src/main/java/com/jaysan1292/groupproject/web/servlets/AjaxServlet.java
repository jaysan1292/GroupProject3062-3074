package com.jaysan1292.groupproject.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaysan1292.groupproject.WebAppCommon;
import com.jaysan1292.groupproject.client.ScavengerClient;
import com.jaysan1292.groupproject.client.accessors.Accessors;
import com.jaysan1292.groupproject.data.*;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.util.SerializationUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status;

/**
 * Created with IntelliJ IDEA.
 * User: Kazedayz
 * Date: 06/12/12
 * Time: 10:52 AM
 */
public class AjaxServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // imaginary network lag, just because ;p
        try {Thread.sleep(500);} catch (InterruptedException ignored) {}

        Map<String, String> queryParams = WebAppCommon.queryStringToMap(request);
        String type = queryParams.get("type");
        String mode = queryParams.get("mode");

        if (StringUtils.isBlank(type)) {
            sendBadRequestResponse(response, "Type not specified.");
            return;
        }
        if (StringUtils.isBlank(mode)) {
            sendBadRequestResponse(response, "Mode not specified.");
            return;
        }

        // Shouldn't be null because you need to be logged in to have this session attribute
        ScavengerClient client = (ScavengerClient) request.getSession().getAttribute(WebAppCommon.ATTR_CLIENT);

        // ..but let's check for sanity anyway
        if (client == null) {
            // in the case that this does happen, redirect to /
            response.sendRedirect(request.getServletContext().getContextPath());
            return;
        }

        if (mode.equals("one")) {
            //region Get single item
            if (queryParams.get("id") == null) {
                sendBadRequestResponse(response, "ID not specified.");
                return;
            }
            long id = NumberUtils.toLong(queryParams.get("id"));

            File root = new File(getServletContext().getRealPath("/"));
            String filename = UUID.randomUUID() + "_item.jsp";

            String jsp;
            BaseEntity item;
            try {
                if (type.equals("scavengerhunt")) {
                    jsp = createItemFormJsp(ScavengerHunt.class, false);
                    item = client.getScavengerHunt(id);
                } else if (type.equals("challenge")) {
                    jsp = createItemFormJsp(Challenge.class, false);
                    item = client.getChallenge(id);
                } else if (type.equals("checkpoint")) {
                    jsp = createItemFormJsp(Checkpoint.class, false);
                    item = client.getCheckpoint(id);
                } else if (type.equals("path")) {
                    jsp = createItemFormJsp(Path.class, false);
                    item = client.getPath(id);
                } else if (type.equals("player")) {
                    jsp = createItemFormJsp(Player.class, false);
                    item = client.getPlayer(id);
                } else if (type.equals("team")) {
                    jsp = createItemFormJsp(Team.class, false);
                    item = client.getTeam(id);
                } else {
                    sendBadRequestResponse(response, "Invalid type: " + type);
                    return;
                }
            } catch (GeneralServiceException e) {
                throw new ServletException(e);
            }

            File itemJsp = new File(root, filename);
            write(jsp, itemJsp);

            request.setAttribute("item", item);
            request.getRequestDispatcher('/' + filename).forward(request, response);
            itemJsp.delete();
            //endregion
        } else if (mode.equals("all")) {
            //region Get all items
            List<? extends BaseEntity> items;
            try {
                if (type.equals("scavengerhunt")) {
                    items = client.getAllScavengerHunts();
                } else if (type.equals("challenge")) {
                    items = client.getAllChallenges();
                } else if (type.equals("checkpoint")) {
                    items = client.getAllCheckpoints();
                } else if (type.equals("path")) {
                    items = client.getAllPaths();
                } else if (type.equals("player")) {
                    items = client.getAllPlayers();
                } else if (type.equals("team")) {
                    items = client.getAllTeams();
                } else {
                    sendBadRequestResponse(response, "Invalid type: " + type);
                    return;
                }
                response.setContentType(MediaType.APPLICATION_JSON);
                response.getWriter().print(SerializationUtils.serialize(items));
            } catch (GeneralServiceException e) {
                throw new ServletException(e);
            }
            //endregion
        } else if (mode.equals("create")) {
            //region Create single item
            File root = new File(getServletContext().getRealPath("/"));
            String filename = UUID.randomUUID() + "_item.jsp";

            String jsp;
            BaseEntity item;
            if (type.equals("scavengerhunt")) {
                jsp = createItemFormJsp(ScavengerHunt.class, true);
                item = new ScavengerHuntBuilder()
                        .setStartTime(DateTime.now())
                        .setFinishTime(DateTime.now())
                        .build();
            } else if (type.equals("challenge")) {
                jsp = createItemFormJsp(Challenge.class, true);
                item = new ChallengeBuilder()
                        .setChallengeText("")
                        .build();
            } else if (type.equals("checkpoint")) {
                jsp = createItemFormJsp(Checkpoint.class, true);
                item = new CheckpointBuilder()
                        .setLatitude(0f)
                        .setLongitude(0f)
                        .build();
            } else if (type.equals("path")) {
                jsp = createItemFormJsp(Path.class, true);
                item = new Path();
            } else if (type.equals("player")) {
                jsp = createItemFormJsp(Player.class, true);
                item = new PlayerBuilder()
                        .setFirstName("")
                        .setLastName("")
                        .setStudentId("")
                        .build();
            } else if (type.equals("team")) {
                jsp = createItemFormJsp(Team.class, true);
                item = new Team();
            } else {
                sendBadRequestResponse(response, "Invalid type: " + type);
                return;
            }

            File itemJsp = new File(root, filename);
            write(jsp, itemJsp);

            request.setAttribute("item", item);
            request.getRequestDispatcher('/' + filename).forward(request, response);
            itemJsp.delete();
            //endregion
        } else {
            sendBadRequestResponse(response, "Invalid mode: " + mode);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        String obj = request.getParameter("obj");
        boolean isnew = BooleanUtils.toBooleanDefaultIfNull(BooleanUtils.toBoolean(request.getParameter("isnew")), false);
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (type.equals("challenge")) {
                Challenge item = mapper.readValue(obj, Challenge.class);
                if (isnew) {
                    Accessors.getChallengeAccessor().create(item);
                } else {
                    Accessors.getChallengeAccessor().update(item);
                }
            } else if (type.equals("checkpoint")) {
                Checkpoint item = mapper.readValue(obj, Checkpoint.class);
                if (isnew) {
                    Accessors.getCheckpointAccessor().create(item);
                } else {
                    Accessors.getCheckpointAccessor().update(item);
                }
            } else if (type.equals("path")) {
                Path item = mapper.readValue(obj, Path.class);
                if (isnew) {
                    Accessors.getPathAccessor().create(item);
                } else {
                    Accessors.getPathAccessor().update(item);
                }
            } else if (type.equals("player")) {
                Player item = mapper.readValue(obj, Player.class);
                if (isnew) {
                    Accessors.getPlayerAccessor().create(item);
                } else {
                    Accessors.getPlayerAccessor().update(item);
                }
            } else if (type.equals("scavengerhunt")) {
                ScavengerHunt item = mapper.readValue(obj, ScavengerHunt.class);
                if (isnew) {
                    Accessors.getScavengerHuntAccessor().create(item);
                } else {
                    Accessors.getScavengerHuntAccessor().update(item);
                }
            } else if (type.equals("team")) {
                Team item = mapper.readValue(obj, Team.class);
                if (isnew) {
                    Accessors.getTeamAccessor().create(item);
                } else {
                    Accessors.getTeamAccessor().update(item);
                }
            } else {
                throw new ServletException("Invalid type: " + type);
            }
        } catch (GeneralServiceException e) {
            WebAppCommon.log.error(e.getMessage(), e);
            throw new ServletException(e);
        }
        response.setStatus(Status.NO_CONTENT.getStatusCode());
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

    private static <T extends BaseEntity> String createItemFormJsp(Class<T> cls, boolean isNew) {
        return String.format("<%%@ page pageEncoding=\"UTF-8\" isELIgnored=\"false\" %%>" +
                             "<%%@ taglib prefix=\"t\" tagdir=\"/WEB-INF/tags\" %%>" +
                             "<jsp:useBean id=\"item\" scope=\"request\" type=\"%s\"/>" +
                             "<t:item_%s_form item=\"${item}\" type=\"%s\" isnew=\"%s\"/>",
                             cls.getName(),
                             cls.getSimpleName().toLowerCase(),
                             cls.getSimpleName(),
                             isNew);
    }

    private static void sendBadRequestResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(Status.BAD_REQUEST.getStatusCode());
        response.getWriter().printf("HTTP %d %s: %s",
                                    Status.BAD_REQUEST.getStatusCode(),
                                    Status.BAD_REQUEST.toString(),
                                    errorMessage);
    }
}
