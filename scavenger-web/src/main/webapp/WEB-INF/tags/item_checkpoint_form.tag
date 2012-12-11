<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="item" type="com.jaysan1292.groupproject.data.Checkpoint" required="true" %>

<t:base_item_form name="${item.description}">
    <jsp:attribute name="formjavascript">
        <script type="text/javascript">
            $(document).ready(function () {initCheckpoint()});

            function initCheckpoint() {
                $('#reset').click(function (e) {
                    e.preventDefault();
                    $('#latitude').val(${item.latitude});
                    $('#longitude').val(${item.longitude});
                    $('#challenge').val(${item.challenge.id});
                });
                $('#challenge').val(${item.challenge.id});

                $('#save').click(function () {onSave()});
                $('#save-form').click(function () {saveCheckpoint()});
            }

            function onSave() {
                console.log('onsave');
                $('#conf-latitude').text('Latitude: {lat}'.f({lat: $('#latitude').val()}));
                $('#conf-longitude').text('Latitude: {lon}'.f({lon: $('#longitude').val()}));
                var challenge = $.parseJSON($('#challenge').find('option:selected').val());
                var challengestr = '{id}: {text}'.format({
                    id:   challenge.description.substr(0, 6),
                    text: challenge.challengeText
                });

                $('#conf-challengetext').text(challengestr).css('text-align', 'center');
                ;
            }

            function saveCheckpoint() {
                var challenge = $.parseJSON($('#challenge').find('option:selected').val());
                var checkpoint = {
                    id:        $('#cpconfirm').data('checkpointid'),
                    latitude:  $('#latitude').val(),
                    longitude: $('#longitude').val(),
                    challenge: challenge
                };

                console.log(checkpoint);

                cleanOutput(checkpoint);

                checkpoint = JSON.stringify(checkpoint);

                console.log(checkpoint);

                update('checkpoint', checkpoint);
            }
        </script>
    </jsp:attribute>
    <jsp:attribute name="modalconfirmbody">
        <table id="cpconfirm" class="table table-striped table-hover table-bordered" data-checkpointid="${item.id}">
            <tr>
                <td id="conf-latitude"></td>
                <td id="conf-longitude"></td>
            </tr>
            <tr>
                <td colspan="2" id="conf-challengetext"></td>
            </tr>
        </table>
    </jsp:attribute>
    <jsp:body>
        <div class="control-group">
            <label class="control-label" for="latitude">Latitude</label>

            <div class="controls">
                <input type="text"
                       id="latitude"
                       name="latitude"
                       pattern="[0-9.-]*"
                       value="${item.latitude}">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="longitude">Longitude</label>

            <div class="controls">
                <input type="text"
                       id="longitude"
                       name="longitude"
                       pattern="[0-9.-]*"
                       value="${item.longitude}">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="challenge">Challenge</label>

            <div class="controls">
                <select id="challenge"
                        name="challenge"
                        data-defaultvalue="${item.challenge.id}">
                    <c:forEach items="${sessionScope.client.allChallenges}" var="challenge">
                        <option value='${challenge}'>${challenge.description}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
    </jsp:body>
</t:base_item_form>
