<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="item" type="com.jaysan1292.groupproject.data.Path" required="true" %>

<t:base_item_form name="Path ${item.id}">
    <jsp:body>
        <div class="control-group">
            <label class="control-label" for="checkpoints">Checkpoints</label>

            <div class="controls">
                <select id="checkpoints"
                        name="checkpoints">
                    <c:forEach items="${item.checkpoints}" var="checkpoint">
                        <option value="${checkpoint.id}">${checkpoint.description}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
    </jsp:body>
</t:base_item_form>
