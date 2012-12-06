<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="page_title" required="true" %>
<%@ attribute name="optional_header" fragment="true" required="false" %>
<%@ attribute name="optional_footer" fragment="true" required="false" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title><c:out value="${page_title}"/></title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link href='http://fonts.googleapis.com/css?family=Droid+Sans:400,700|Droid+Sans+Mono'
          rel='stylesheet'
          type='text/css'>
    <link href="<c:url value="/bootstrap/css/bootstrap.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/css/application.css"/>" rel="stylesheet"/>
    <script src="http://code.jquery.com/jquery-1.8.2.js" type="text/javascript"></script>
    <jsp:invoke fragment="optional_header"/>
</head>
<body>
<t:navbar/>
<jsp:doBody/>
<div class="navbar navbar-fixed-bottom" id="footer">
    <div class="navbar-inner">
        <div class="container">
            <ul class="nav">
                <li>
                    <a href="javascript:void(0)">
                        COMP 3062 Group Project by Jason Recillo, Peter Le, and Mellicent Dres
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
<script src="<c:url value="/bootstrap/js/bootstrap.min.js"/>" type="text/javascript"></script>
<jsp:invoke fragment="optional_footer"/>
</body>
</html>
