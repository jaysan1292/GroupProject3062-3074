<%--
  Created by IntelliJ IDEA.
  User: Jason Recillo
  Date: 03/12/12
  Time: 10:49 PM
--%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<t:base>
    <jsp:attribute name="page_title">Scavenger Hunt Home</jsp:attribute>
    <jsp:body>
        <div class="container dev-border">
            <div class="row">
                <div class="span2 dev-border" id="home-sidebar">
                    <ul class="nav nav-list">
                        <li class="nav-header">Items</li>
                        <li class="active"><a href="#">Overview</a></li>
                        <li><a href="#">Scavenger Hunts</a></li>
                        <li><a href="#">Players</a></li>
                        <li><a href="#">Teams</a></li>
                        <li><a href="#">Checkpoints</a></li>
                        <li><a href="#">Challenges</a></li>
                        <li><a href="#">Paths</a></li>
                    </ul>
                </div>
                <div class="span6 dev-border">
                    <div class="container-fluid well well-small">
                        <h4>Header</h4>
                    </div>
                    <ul id="home-itemlist">
                        <c:forEach begin="0" end="10">
                            <t:home_list_item>
                                <jsp:attribute name="item_description">Some item</jsp:attribute>
                            </t:home_list_item>
                        </c:forEach>
                    </ul>
                </div>
                <div class="span4 dev-border">
                    item detail
                </div>
            </div>
        </div>
    </jsp:body>
</t:base>
