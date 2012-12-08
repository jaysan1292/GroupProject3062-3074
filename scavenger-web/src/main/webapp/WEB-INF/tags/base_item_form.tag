<%@ tag isELIgnored="false" pageEncoding="UTF-8" %>
<%@ attribute name="name" type="java.lang.String" required="true" %>
<%@ attribute name="formJavaScript" fragment="true" required="false" %>

<%--initially start invisible for animation stuff--%>
<div id="home-item-detail" style="display:none;">
    <form class="form-horizontal" id="itemform">
        <legend>${name}</legend>
        <jsp:doBody/>
        <hr/>
        <div class="control-group">
            <div class="controls">
                <button type="button" class="btn btn-primary">Submit</button>
                <button type="reset" class="btn">Reset</button>
            </div>
        </div>
    </form>
    <jsp:invoke fragment="formJavaScript"/>
</div>
