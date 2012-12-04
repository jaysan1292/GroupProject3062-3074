<%@ tag isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row well">
    <form action="<c:url value="/login"/>" method="post">
        <legend>Sign in</legend>
        <fieldset>
            <label for="username">Admin ID</label>
            <input type="text" id="username" name="username" class="span4">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" class="span4">

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Sign in</button>
            </div>
        </fieldset>
    </form>
</div>
