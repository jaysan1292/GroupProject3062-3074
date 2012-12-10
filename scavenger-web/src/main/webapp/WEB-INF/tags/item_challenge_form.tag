<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="item" type="com.jaysan1292.groupproject.data.Challenge" required="true" %>

<t:base_item_form name="${item.description}">
    <jsp:attribute name="formjavascript">
        <script type="text/javascript">
            $(document).ready(function () {
                $('#itemform input[type="reset"]').click(function () {
                    $('#challengeText').value($('#challengeText').data('defaultvalue'));
                });

                $('#save').click(function () {onSave()});
                $('#save-form').click(function () {saveChallenge()});
            });

            function onSave() {
                $('#confirm-body p').text($('#challengeText').val());
            }

            function saveChallenge() {
                var challenge = {
                    id:            $('#challenge-id').text(),
                    challengeText: $('#challengeText').val()
                };

                challenge = JSON.stringify(challenge);

                console.log(challenge);

                //TODO: Send to service
            }
        </script>
    </jsp:attribute>
    <jsp:attribute name="modalconfirmbody">
        <h4>Challenge text:</h4><p class="well"></p>
    </jsp:attribute>
    <jsp:body>
        <span class="hide" id="challenge-id">${item.id}</span>

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
