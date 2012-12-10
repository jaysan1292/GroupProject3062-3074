<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="item" type="com.jaysan1292.groupproject.data.Checkpoint" required="true" %>

<t:base_item_form name="${item.description}">
    <jsp:attribute name="formjavascript">
        <script type="text/javascript">
            $(document).ready(function () {initCheckpoint()});

            function initCheckpoint() {
                $('#reset').click(function (e) {
                    e.preventDefault();
                    $('#latitude').val(${item.latitude});
                    $('#longitude').val(${item.longitude});
                    $('#challenge').val(${item.challenge.id});
                });
                $('#challenge').val(${item.challenge.id});
            }
        </script>
    </jsp:attribute>
    <jsp:attribute name="modalconfirmbody">

    </jsp:attribute>
    <jsp:body>
        <div class="control-group">
            <label class="control-label" for="latitude">Latitude</label>

            <div class="controls">
                <input type="text"
                       id="latitude"
                       name="latitude"
                       pattern="[0-9.-]*"
                       value="${item.latitude}">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="longitude">Longitude</label>

            <div class="controls">
                <input type="text"
                       id="longitude"
                       name="longitude"
                       pattern="[0-9.-]*"
                       value="${item.longitude}">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="challenge">Challenge</label>

            <div class="controls">
                <select id="challenge"
                        name="challenge"
                        data-defaultvalue="${item.challenge.id}">
                    <c:forEach items="${applicationScope.challengeAccessor.all}" var="challenge">
                        <option value="${challenge.id}">${challenge.description}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
    </jsp:body>
</t:base_item_form>
