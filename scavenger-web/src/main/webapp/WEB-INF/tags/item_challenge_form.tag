<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="item" type="com.jaysan1292.groupproject.data.Challenge" required="true" %>

<t:base_item_form name="${item.description}">
    <jsp:attribute name="formjavascript">
        <script type="text/javascript">
            $('#itemform input[type="reset"]').click(function () {
                $('#challengeText').value($('#challengeText').data('defaultvalue'));
            });
        </script>
    </jsp:attribute>
    <jsp:attribute name="modalconfirmbody">
        <%--TODO: Challenge confirm modal body--%>
    </jsp:attribute>
    <jsp:body>
        <div class="control-group">
            <label class="control-label" for="challengeText">Challenge Text</label>

            <div class="controls">
                <textarea id="challengeText"
                          name="challengeText"
                          data-defaultvalue="${item.challengeText}">${item.challengeText}</textarea>
            </div>
        </div>
    </jsp:body>
</t:base_item_form>
