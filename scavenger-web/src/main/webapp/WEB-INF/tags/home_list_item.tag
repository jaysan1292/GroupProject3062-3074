<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="item_description" required="true" type="java.lang.String" %>
<%@ attribute name="selected" type="java.lang.Boolean" required="false" %>
<c:if test="${selected == null}">
    <c:set var="selected" value="false"/>
</c:if>
<c:choose>
    <c:when test="${selected == true}">
        <li class="active">${item_description}</li>
    </c:when>
    <c:otherwise>
        <li>${item_description}</li>
    </c:otherwise>
</c:choose>
