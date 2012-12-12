<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="item" type="com.jaysan1292.groupproject.data.ScavengerHunt" required="true" %>
<%@ attribute name="isnew" type="java.lang.Boolean" required="true" %>
<%@ attribute name="type" type="java.lang.String" required="true" %>

<t:base_item_form name="${item.description}" isnew="${isnew}" type="${type}">
<jsp:attribute name="modalconfirmbody">
    <h4>Team</h4>
    <table id="teamconfirm"
           class="table table-bordered table-striped table-hover">
        <tr class="thead">
            <th>Player ID</th>
            <th>Player Name</th>
            <th>Student Number</th>
        </tr>
    </table>
    <hr/>
    <h4>Path</h4>
    <table id="pathconfirm"
           class="table table-bordered table-striped table-hover">
        <tr class="thead">
            <th>#</th>
            <th>Ch. ID</th>
            <th>Latitude</th>
            <th>Longitude</th>
            <th>Challenge</th>
        </tr>
    </table>
    <hr/>
    <h4>Start Time:</h4><span id="stimeconfirm"></span><br/>
    <h4>Finish Time:</h4><span id="ftimeconfirm"></span>
</jsp:attribute>
<jsp:attribute name="formjavascript">
    <script type="text/javascript">
        $(document).ready(function () {initScavenger()});

        function initScavenger() {
            var start = new Date(${item.startTimeMillis});
            var finish = new Date(${item.finishTimeMillis});

            $('#startTime').datepicker({
                onSelect: function (datestr) {
                    console.log('date select');
                    var d1 = $(this).datepicker('getDate');
                    d1.setDate(d1.getDate() + 0);

                    $('#finishTime').datepicker('setDate', null);
                    $('#finishTime').datepicker('option', 'minDate', d1);
                }
            }).datepicker('setDate', start);
            $('#finishTime').datepicker().datepicker('setDate', finish);
            $('#startTime-hour').val(start.getHours());
            $('#startTime-minute').val(start.getMinutes());
            $('#finishTime-hour').val(finish.getHours());
            $('#finishTime-minute').val(finish.getMinutes());

            formatTimeSpinners();

            $('#startTime-hour,' +
                    '#finishTime-hour,' +
                    '#startTime-minute,' +
                    '#finishTime-minute').change(function (evt) {formatTimeSpinners()});

            $('#team').val(${item.team.id});
            $('#path').val(${item.path.id});

            $('#save').click(function () {onSave()});
            $('#save-form').click(function () {saveScavengerHunt()});

            $('#reset').click(function (e) {
                e.preventDefault();
                initScavenger();
            });
        }

        function formatTimeSpinners() {
            var s = $('#startTime-hour');
            var f = $('#finishTime-hour');

            s.val(pad(s.val(), 2));
            f.val(pad(f.val(), 2));

            s = $('#startTime-minute');
            f = $('#finishTime-minute');

            s.val(pad(s.val(), 2));
            f.val(pad(f.val(), 2));
        }
        function pad(str, max) {
            return str.length < max ? pad('0' + str, max) : str;
        }
        function onSave() {
            var team = $('#team').find('option:selected').data('teamjson');
            var path = $('#path').find('option:selected').data('pathjson');

            var headings = $('#confirm-modal h4');
            $(headings[0]).text('Team: ' + team.description.substr(0, 6));
            $(headings[1]).text('Path: ' + path.description.substr(0, 6));

            console.log(team);
            $('#confirm-modal tr').not('.thead').remove();

            $('#teamconfirm').attr('data-teamjson', JSON.stringify(team));
            for (var x in team.teamMembers) {
                var player = team.teamMembers[x];
                console.log(JSON.stringify(player));
                var p = '<tr><td>{id}</td><td>{fn} {ln}</td><td>{snum}</td></tr>'.f({
                    id:   player.id,
                    fn:   player.firstName,
                    ln:   player.lastName,
                    snum: player.studentNumber
                });
                console.log(p);
                $('#teamconfirm').append(p);
            }

            $('#pathconfirm').attr('data-pathjson', JSON.stringify(path));
            for (var i = 0; i < path.checkpoints.length; i++) {
                var checkpoint = path.checkpoints[i];
                console.log(JSON.stringify(checkpoint));

                var c = '<tr><td>{i}</td><td>{id}</td><td>{lat}</td><td>{lon}</td><td>{ctext}</td></tr>'.f({
                    i:     i + 1,
                    id:    checkpoint.id,
                    lat:   checkpoint.latitude,
                    lon:   checkpoint.longitude,
                    ctext: checkpoint.challenge.description.substr(8)
                });
                console.log(c);
                $('#pathconfirm').append(c);

                var startd = $('#startTime').datepicker('getDate');
                startd.setHours(parseInt($('#startTime-hour').val()));
                startd.setMinutes(parseInt($('#startTime-minute').val()));

                var finishd = $('#finishTime').datepicker('getDate');
                finishd.setHours(parseInt($('#finishTime-hour').val()));
                finishd.setMinutes(parseInt($('#finishTime-minute').val()));

                var weekdays = 'Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday'.split(',');
                var months = 'January,February,March,April,May,June,July,August,September,October,November,December'.split(',');

                $('#stimeconfirm').text('{dow}, {dom} {mon} {year}: {hour}:{minute}'.f({
                    dow:    weekdays[startd.getDay()],
                    dom:    startd.getDate(),
                    mon:    months[startd.getMonth()],
                    year:   startd.getYear() + 1900,
                    hour:   pad(String(startd.getHours()), 2),
                    minute: pad(String(startd.getMinutes()), 2)
                }));
                $('#stimeconfirm').attr('data-date', startd.getTime());

                $('#ftimeconfirm').text('{dow}, {dom} {mon} {year}: {hour}:{minute}'.f({
                    dow:    weekdays[finishd.getDay()],
                    dom:    finishd.getDate(),
                    mon:    months[finishd.getMonth()],
                    year:   finishd.getYear() + 1900,
                    hour:   pad(String(finishd.getHours()), 2),
                    minute: pad(String(finishd.getMinutes()), 2)
                }));
                $('#ftimeconfirm').attr('data-date', finishd.getTime());
            }
        }

        function saveScavengerHunt() {
            var team = $('#teamconfirm').data('teamjson');
            var path = $('#pathconfirm').data('pathjson');
            var startTimeMillis = $('#stimeconfirm').data('date');
            var finishTimeMillis = $('#ftimeconfirm').data('date');

            var shjson = '{"id":{id},"team":{team},"path":{path},"startTimeMillis":{start},"finishTimeMillis":{finish}}'.f({
                id:     $('#scavenger-hunt').data('schid'),
                team:   JSON.stringify(team),
                path:   JSON.stringify(path),
                start:  startTimeMillis,
                finish: finishTimeMillis
            });

            shjson = $.parseJSON(shjson);
            cleanOutput(shjson);
            shjson = JSON.stringify(shjson);

            console.log(shjson);

            update('scavengerhunt', shjson);
        }
    </script>
</jsp:attribute>
<jsp:body>
    <span id="scavenger-hunt" style="display:none;" data-schid="${item.id}"></span>

    <div class="control-group">
        <label class="control-label" for="team">Team</label>

        <div class="controls">
            <select id="team"
                    name="team"
                    data-defaultvalue="${item.team.id}">
                <c:forEach items="${sessionScope.client.allTeams}" var="team">
                    <option data-teamjson='${team}' value="${team.id}">${team.description}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" for="path">Path</label>

        <div class="controls">
            <select id="path"
                    name="path">
                <c:forEach items="${sessionScope.client.allPaths}" var="path">
                    <option data-pathjson='${path}' value="${path.id}">${path.description}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" for="startTime">Start Time</label>

        <div class="controls">
            <span>
                <input type="datetime"
                       id="startTime"
                       name="startTime"
                       style="width:78px;">
                <input id="startTime-hour"
                       type="number"
                       min="00"
                       max="23"
                       value="${item.startTime.hourOfDay}"
                       style="width:42px">
                :
                <input id="startTime-minute"
                       type="number"
                       min="00"
                       max="59"
                       value="${item.startTime.minuteOfHour}"
                       style="width:42px">
            </span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" for="finishTime">Finish Time</label>

        <div class="controls">
            <span>
                <input type="datetime"
                       id="finishTime"
                       name="finishTime"
                       style="width:78px;">
                <input id="finishTime-hour"
                       type="number"
                       min="00"
                       max="23"
                       value="${item.finishTime.hourOfDay}"
                       style="width:42px">
                :
                <input id="finishTime-minute"
                       type="number"
                       min="00"
                       max="59"
                       value="${item.finishTime.minuteOfHour}"
                       style="width:42px">
            </span>
        </div>
    </div>
</jsp:body>
</t:base_item_form>
