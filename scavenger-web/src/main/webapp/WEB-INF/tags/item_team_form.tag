<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="item" type="com.jaysan1292.groupproject.data.Team" required="true" %>

<t:base_item_form name="${item.description}">

    <jsp:body>
        <div class="control-group">
            <label class="control-label" for="team">Team</label>

            <div class="controls">
                <select id="team" multiple="multiple">
                    <c:forEach items="${item.teamMembers}" var="player">
                        <option value="${player.key}">${player.value.description}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
    </jsp:body>
</t:base_item_form>
