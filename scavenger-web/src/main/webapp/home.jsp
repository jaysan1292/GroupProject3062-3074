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
    <jsp:body>
        <div class="container" id="home-main-container">
            <div class="row">
                <div class="span2" id="home-sidebar">
                    <ul class="nav nav-list">
                        <li class="nav-header">Items</li>
                        <li class="active"><a href="#">Scavenger Hunts</a></li>
                        <li><a href="#">Players</a></li>
                        <li><a href="#">Teams</a></li>
                        <li><a href="#">Checkpoints</a></li>
                        <li><a href="#">Challenges</a></li>
                        <li><a href="#">Paths</a></li>
                    </ul>
                </div>
                <div class="span4" id="home-itemlist-container">
                    <div class="container-fluid" id="home-itemlist-header">
                        <h4>Header</h4>
                    </div>
                    <ul id="home-itemlist">
                        <c:forEach begin="1" end="25" varStatus="item">
                            <c:choose>
                                <c:when test="${item.index == 5}">
                                    <t:home_list_item selected="true" item_description="Some item"/>
                                </c:when>
                                <c:otherwise>
                                    <t:home_list_item item_description="Some item"/>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </ul>
                </div>
                <div class="span6">
                    <t:home_item_detail item="Item details"/>
                </div>
            </div>
        </div>
    </jsp:body>
</t:base>
