<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="item" required="true" type="java.lang.String" %>
<%@ attribute name="selected" type="java.lang.Boolean" required="false" %>
<%@ attribute name="id" type="java.lang.Integer" required="true" %>

<c:if test="${selected == null}">
    <c:set var="selected" value="false"/>
</c:if>
<c:choose>
    <c:when test="${selected == true}">
        <li data-itemid="${id}" class="active">${item}</li>
    </c:when>
    <c:otherwise>
        <li data-itemid="${id}">${item}</li>
    </c:otherwise>
</c:choose>
