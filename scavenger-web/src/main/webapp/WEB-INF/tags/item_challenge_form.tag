<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="item" type="com.jaysan1292.groupproject.data.Challenge" required="true" %>

<t:base_item_form name="Challenge ${item.id}">
    <jsp:attribute name="formJavaScript">
        <script type="text/javascript">
            $('#itemform input[type="reset"]').click(function () {
                $('#challengeText').value($('#challengeText').attr('data-defaultvalue'));
            });
        </script>
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
