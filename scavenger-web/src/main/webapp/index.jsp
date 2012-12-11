<%@ page contentType="text/html" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:base page_title="Home">
    <jsp:attribute name="optional_footer">
        <script type="text/javascript">
            $(document).ready(function () {
                $('#signin-form')
                        .center()
                        .css('top', 100)
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <t:login/>
    </jsp:body>
</t:base>
