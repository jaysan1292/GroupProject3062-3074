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
    <jsp:attribute name="optional_footer">
        <script type="text/javascript">
            // Counts the elements in the item list and puts the result at the very bottom
            function countListItems() {
                var items = $('#home-item-list').children().not('#home-item-list-footer');
                $('#home-item-list-footer').html(items.length +
                                                 items.length != 1 ? ' items' : ' item');
            }
            function determineItemType() {
                var itemtype = $('#home-sidebar ul').children().filter('.active').attr('data-type');

                switch (itemtype) {
                    case 'scavengerhunt':
                        console.log('THIS IS A SCAVENGER HUNT');
                        break;
                    case 'player':
                        console.log('THIS IS A PLAYER');
                        break;
                    case 'team':
                        console.log('THIS IS A TEAM');
                        break;
                    case 'checkpoint':
                        console.log('THIS IS A CHECKPOINT');
                        break;
                    case 'challenge':
                        console.log('THIS IS A CHALLENGE');
                        break;
                    case 'path':
                        console.log('THIS IS A PATH');
                        break;
                }
            }
            $(document).ready(function () {
                countListItems();
                determineItemType();
            });
        </script>
        <script type="text/javascript">
            // Set click listeners for left sidebar
            $(document).ready(function () {
                var menuItems = $('#home-sidebar ul').children().not('.nav-header');
                menuItems.click(function () {
                    // Don't do anything if the clicked element is already selected
                    if ($(this).hasClass('active')) return;

                    $(menuItems).removeClass('active');
                    $(this).addClass('active');

                    $('#home-item-list').children()[0].click();
                    countListItems();
                });
            });
        </script>
        <script type="text/javascript">
            // Set click listeners for item list
            $(document).ready(function () {
                var items = $('#home-item-list').children().not('#home-item-list-footer');
                items.click(function () {
                    // Don't do anything if the clicked element is already selected
                    if ($(this).hasClass('active')) return;

                    $(items).removeClass('active');
                    $(this).addClass('active');
                    countListItems();

                    getItem($('#home-sidebar ul').children().filter('.active').attr('data-type'),
                            $(this).attr('data-itemid'));
                });
            });
        </script>
        <script>
            // The meat of the web app. Retrieve an item of the given type from the server
            // and display it in the detail view.
            function getItem(itemType, itemId) {
                //TODO
                console.log('Retrieving ' + itemType + ' ' + itemId);
            }
        </script>
    </jsp:attribute>
    <jsp:body>
        <div class="container" id="home-main-container">
            <div class="row">
                <div class="span2" id="home-sidebar">
                    <ul class="nav nav-list">
                        <li class="nav-header" id="home-sidebar-header">Items</li>
                        <li data-type="scavengerhunts" class="active">
                            <a href="javascript:void(0)">Scavenger Hunts</a>
                        </li>
                        <li data-type="players">
                            <a href="javascript:void(0)">Players</a>
                        </li>
                        <li data-type="teams">
                            <a href="javascript:void(0)">Teams</a>
                        </li>
                        <li data-type="checkpoints">
                            <a href="javascript:void(0)">Checkpoints</a>
                        </li>
                        <li data-type="challenges">
                            <a href="javascript:void(0)">Challenges</a>
                        </li>
                        <li data-type="paths">
                            <a href="javascript:void(0)">Paths</a>
                        </li>
                    </ul>
                </div>
                <div class="span4" id="home-item-list-container">
                    <div class="container-fluid" id="home-item-list-header">
                        <h4>Header</h4>
                    </div>
                    <ul id="home-item-list">
                        <c:forEach begin="1" end="25"
                                   varStatus="status">
                            <c:choose>
                                <c:when test="${status.index == 1}">
                                    <t:home_list_item
                                            id="${status.index}"
                                            item="Some item ${status.index}"
                                            selected="true"/>
                                </c:when>
                                <c:otherwise>
                                    <t:home_list_item
                                            id="${status.index}"
                                            item="Some item ${status.index}"/>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <li id="home-item-list-footer"></li>
                    </ul>
                </div>
                <div class="span6" id="home-item-detail-container">
                    <t:dev_item_detail item="Some item 5 details"/>
                </div>
            </div>
        </div>
    </jsp:body>
</t:base>
