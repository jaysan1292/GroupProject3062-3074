<%@ tag isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="well" id="signin-form" style="width:400px;">
    <form action="<c:url value="/login"/>" method="post">
        <legend>Sign in</legend>
        <fieldset>
            <label for="username">Admin ID</label>
            <input type="text" id="username" name="username">
            <label for="password">Password</label>
            <input type="password" id="password" name="password">

            <c:if test="${not empty requestScope.errorMessage}">
                <div class="control-group error">
                    <label class="control-label">${requestScope.errorMessage}</label>
                </div>
            </c:if>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Sign in</button>
            </div>
        </fieldset>
    </form>
</div>
