<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <a class="brand" href="<c:url value="/"/>">GBC ScavengerHunt</a>

            <c:if test="<%=session.getAttribute(\"loggedIn\") != null%>">
                <div class="navbar-form pull-right">
                    <a class="btn" href="<c:url value="/login?logout"/>">Logout</a>
                </div>
            </c:if>
        </div>
    </div>
</div>
