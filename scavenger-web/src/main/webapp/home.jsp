<%@ page import="com.google.common.collect.Lists" %>
<%@ page import="com.jaysan1292.groupproject.client.ScavengerClient" %>
<%@ page import="com.jaysan1292.groupproject.client.accessors.Accessors" %>
<%@ page import="com.jaysan1292.groupproject.data.*" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="java.util.HashMap" %>
<%--
  Created by IntelliJ IDEA.
  User: Jason Recillo
  Date: 03/12/12
  Time: 10:49 PM
--%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>

<%!
    public void jspInit() {
        new ScavengerClient("999999999", "admin");
        ServletContext context = getServletConfig().getServletContext();
        context.setAttribute("challengeAccessor", Accessors.getChallengeAccessor());
        context.setAttribute("checkpointAccessor", Accessors.getCheckpointAccessor());
        context.setAttribute("pathAccessor", Accessors.getPathAccessor());
        context.setAttribute("playerAccessor", Accessors.getPlayerAccessor());
        context.setAttribute("scavengerHuntAccessor", Accessors.getScavengerHuntAccessor());
        context.setAttribute("teamAccessor", Accessors.getTeamAccessor());
    }
%>

<%
    // Test data
    application.setAttribute("challenge",
                             new ChallengeBuilder()
                                     .setChallengeId(0)
                                     .setChallengeText("First go there and do this thing.")
                                     .build());
    application.setAttribute("checkpoint",
                             new CheckpointBuilder()
                                     .setCheckpointId(0)
                                     .setLatitude(43.675854f)
                                     .setLongitude(-79.71069f)
                                     .setChallenge((Challenge) application.getAttribute("challenge"))
                                     .build());
    application.setAttribute("path",
                             new PathBuilder()
                                     .setPathId(0)
                                     .setCheckpoints(Lists.newArrayList(
                                             new CheckpointBuilder()
                                                     .setCheckpointId(0)
                                                     .setLatitude(43.675854f)
                                                     .setLongitude(-79.71069f)
                                                     .setChallenge(new ChallengeBuilder()
                                                                           .setChallengeId(0)
                                                                           .setChallengeText("First go there and do this thing.")
                                                                           .build())
                                                     .build(),
                                             new CheckpointBuilder()
                                                     .setCheckpointId(1)
                                                     .setLatitude(43.675854f)
                                                     .setLongitude(-79.71069f)
                                                     .setChallenge(new ChallengeBuilder()
                                                                           .setChallengeId(1)
                                                                           .setChallengeText("Go here and there.")
                                                                           .build())
                                                     .build(),
                                             new CheckpointBuilder()
                                                     .setLatitude(43.676130f)
                                                     .setLongitude(-79.410492f)
                                                     .setChallenge(new ChallengeBuilder()
                                                                           .setChallengeId(2)
                                                                           .setChallengeText("Do this thing at this place.")
                                                                           .build())
                                                     .build()))
                                     .build());
    application.setAttribute("player",
                             new PlayerBuilder()
                                     .setPlayerId(6)
                                     .setFirstName("Soonkyu")
                                     .setLastName("Lee")
                                     .setStudentId("100123498")
                                     .build());
    application.setAttribute("team",
                             new TeamBuilder()
                                     .setTeamId(2)
                                     .setTeamMembers(new HashMap<Long, Player>() {{
                                         put(1L, new PlayerBuilder()
                                                 .setPlayerId(1)
                                                 .setFirstName("Peter")
                                                 .setLastName("Le")
                                                 .setStudentId("100145965")
                                                 .build());
                                         put(2L, new PlayerBuilder()
                                                 .setPlayerId(2)
                                                 .setFirstName("Mellicent")
                                                 .setLastName("Dres")
                                                 .setStudentId("100793317")
                                                 .build());
                                     }})
                                     .build());
    application.setAttribute("scavengerhunt",
                             new ScavengerHuntBuilder()
                                     .setScavengerHuntId(0)
                                     .setPath((Path) application.getAttribute("path"))
                                     .setTeam((Team) application.getAttribute("team"))
                                     .setStartTime(new DateTime(2012, 11, 27, 10, 0))
                                     .setFinishTime(new DateTime(2012, 11, 27, 16, 0))
                                     .build());
%>

<t:base page_title="Scavenger Hunt Home">
    <jsp:attribute name="optional_header">
        <link href="<c:url value="/jqui/jquery-ui-1.8.16.custom.css"/>" rel="stylesheet"/>
    </jsp:attribute>
    <jsp:attribute name="optional_footer">
        <script src="<c:url value="/js/homeapp.jsp"/>" type="text/javascript"></script>
        <script src="<c:url value="/js/jquery.validate.js"/>" type="text/javascript"></script>
        <script src="<c:url value="/js/jquery-ui-1.9.2.js"/>" type="text/javascript"></script>
    </jsp:attribute>
    <jsp:body>
        <div class="container" id="home-main-container">
            <div class="row">
                <div class="span2" id="home-sidebar">
                    <ul class="nav nav-list">
                        <li class="nav-header" id="home-sidebar-header">Items</li>
                        <li data-type="scavengerhunt" class="active">
                            <a href="javascript:void(0)">Scavenger Hunts</a>
                        </li>
                        <li data-type="player">
                            <a href="javascript:void(0)">Players</a>
                        </li>
                        <li data-type="team">
                            <a href="javascript:void(0)">Teams</a>
                        </li>
                        <li data-type="checkpoint">
                            <a href="javascript:void(0)">Checkpoints</a>
                        </li>
                        <li data-type="challenge">
                            <a href="javascript:void(0)">Challenges</a>
                        </li>
                        <li data-type="path">
                            <a href="javascript:void(0)">Paths</a>
                        </li>
                    </ul>
                </div>
                <div class="span4" id="home-item-list-container">
                    <div class="container-fluid" id="home-item-list-header">
                        <h4>Header</h4>
                    </div>
                    <ul id="home-item-list">
                        <li id="home-item-list-footer"></li>
                    </ul>
                </div>
                <div class="span6" id="home-item-detail-container">
                        <%--<script type="text/javascript">$(document).ready(function () {$('#home-item-detail').fadeIn(500)})</script>--%>
                        <%--<t:item_team_form item="${applicationScope.team}"/>--%>
                </div>
            </div>
        </div>
    </jsp:body>
</t:base>
