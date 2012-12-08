<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="item" type="com.jaysan1292.groupproject.data.Checkpoint" required="true" %>

<t:base_item_form name="Checkpoint ${item.id}">
    <jsp:body>
        <div class="control-group">
            <label class="control-label" for="latitude">Latitude</label>

            <div class="controls">
                <input type="text"
                       id="latitude"
                       name="latitude"
                       value="${item.latitude}">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="longitude">Longitude</label>

            <div class="controls">
                <input type="text"
                       id="longitude"
                       name="longitude"
                       value="${item.longitude}">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="challenge">Challenge</label>

            <div class="controls">
                <select id="challenge"
                        name="challenge"
                        data-defaultvalue="${item.challenge.id}">
                    <option>TODO: DO THIS WHEN WEB SERVICE CLIENT IS DONE</option>
                </select>
            </div>
        </div>
    </jsp:body>
</t:base_item_form>
