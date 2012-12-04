<%@ page contentType="text/html" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:base>
    <jsp:attribute name="page_title">Home</jsp:attribute>
    <jsp:body>
        <div class="container">
            <div class="row">
                <div class="span12">
                    <div class="hero-unit">
                        <h1>Hello, world!</h1>

                        <p>Seen for the very first time!</p>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="span6 offset3">
                    <t:login/>
                </div>
            </div>
        </div>
    </jsp:body>
</t:base>
