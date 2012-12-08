<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="item" type="com.jaysan1292.groupproject.data.ScavengerHunt" required="true" %>

<t:base_item_form name="${item.description}">

    <jsp:body>
        <div class="control-group">
                <%--TODO: Dropdown--%>
            <label class="control-label" for="team">Team</label>

            <div class="controls">
                <input type="text"
                       id="team"
                       name="team"
                       value="${item.team.description}">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="path">Path</label>

            <div class="controls">
                <input type="text"
                       id="path"
                       name="path"
                       value="${item.path.description}">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="startTime">Start Time</label>

            <div class="controls">
                <input type="datetime"
                       id="startTime"
                       name="startTime"
                       value="${item.startTimeMillis}">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="finishTime">Finish Time</label>

            <div class="controls">
                <input type="datetime"
                       id="finishTime"
                       name="finishTime"
                       value="${item.finishTimeMillis}">
            </div>
        </div>
    </jsp:body>
</t:base_item_form>
