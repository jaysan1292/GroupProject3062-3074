<%--
  Created by IntelliJ IDEA.
  User: Jason Recillo
  Date: 03/12/12
  Time: 10:49 PM
--%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>

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
                        <h4>Scavenger Hunts</h4>
                    </div>
                    <ul id="home-item-list">
                        <li id="home-item-list-footer"></li>
                    </ul>
                </div>
                <div class="span6" id="home-item-detail-container"></div>
            </div>
        </div>
    </jsp:body>
</t:base>
