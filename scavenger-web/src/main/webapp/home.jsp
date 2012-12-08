<%@ page import="com.google.common.collect.Lists" %>
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

<%
    // Test data
    application.setAttribute("challenge",
                             new ChallengeBuilder()
                                     .setChallengeId(1)
                                     .setChallengeText("Blahblahblah")
                                     .build());
    application.setAttribute("checkpoint",
                             new CheckpointBuilder()
                                     .setCheckpointId(2)
                                     .setLatitude(-43.123456f)
                                     .setLongitude(75.456123f)
                                     .setChallenge((Challenge) application.getAttribute("challenge"))
                                     .build());
    application.setAttribute("path",
                             new PathBuilder()
                                     .setPathId(3)
                                     .setCheckpoints(Lists.newArrayList(
                                             new CheckpointBuilder()
                                                     .setLatitude(-43.158465f)
                                                     .setLongitude(75.165463f)
                                                     .setChallenge((Challenge) application.getAttribute("challenge"))
                                                     .build(),
                                             new CheckpointBuilder()
                                                     .setLatitude(-43.168465f)
                                                     .setLongitude(75.124986f)
                                                     .setChallenge((Challenge) application.getAttribute("challenge"))
                                                     .build(),
                                             new CheckpointBuilder()
                                                     .setLatitude(-43.134986f)
                                                     .setLongitude(75.565465f)
                                                     .setChallenge((Challenge) application.getAttribute("challenge"))
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
                                     .setTeamId(4)
                                     .setTeamMembers(new HashMap<Long, Player>() {{
                                         put(1L, new PlayerBuilder()
                                                 .setFirstName("Jason")
                                                 .setLastName("Recillo")
                                                 .setStudentId("100123123")
                                                 .setPlayerId(1)
                                                 .build());
                                         put(2L, new PlayerBuilder()
                                                 .setFirstName("Peter")
                                                 .setLastName("Le")
                                                 .setStudentId("100465246")
                                                 .setPlayerId(2)
                                                 .build());
                                     }})
                                     .build());
    application.setAttribute("scavengerhunt",
                             new ScavengerHuntBuilder()
                                     .setScavengerHuntId(5)
                                     .setPath((Path) application.getAttribute("path"))
                                     .setTeam((Team) application.getAttribute("team"))
                                     .setStartTime(new DateTime(2012, 12, 3, 10, 0))
                                     .setFinishTime(new DateTime(2012, 12, 3, 16, 0))
                                     .build());
%>

<t:base page_title="Scavenger Hunt Home">
    <jsp:attribute name="optional_header">
        <script src="<c:url value="/js/homeapp.jsp"/>" type="text/javascript"></script>
        <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.10.0/jquery.validate.js"
                type="text/javascript"></script>
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
                        <%--<script type="text/javascript">$(document).ready(function(){$('#home-item-detail').fadeIn(500)})</script>--%>
                        <%--<t:item_scavengerhunt_form item="${applicationScope.scavengerhunt}"/>--%>
                </div>
            </div>
        </div>
    </jsp:body>
</t:base>
