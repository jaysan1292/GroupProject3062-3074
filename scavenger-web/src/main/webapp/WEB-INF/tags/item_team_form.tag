<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="item" type="com.jaysan1292.groupproject.data.Team" required="true" %>

<t:base_item_form name="${item.description}">
    <jsp:attribute name="formjavascript">
        <script type="text/javascript">
            $(document).ready(initTeam);

            function initTeam() {
                var teamMembers = new Array();
                for (var i = 0; i < $('.btn-team-form').length; i++) {
                    teamMembers[i] = $($('.btn-team-form')[i]).data('playerjson');
                }

                // Remove players that are already on the team from the player list
                $($('#all-players').children()).each(function () {
                    var player = $.parseJSON($(this).attr('value'));
                    for (var i = 0; i < teamMembers.length; i++) {
                        if (JSON.stringify(player) === JSON.stringify(teamMembers[i])) {
                            $(this).remove();
                        }
                    }
                });

                // set reset button handler
                $('#team').data('originalstate', $('#team').html().replace(/\s\s/g, ''));
                $('#reset').click(function () {
                    $('#team').html($('#team').data('originalstate'));
                    initTeam();
                });

                // set submit button handler
                $('#save').click(function () {onSave()});

                setRemoveClickHandlers();

                // set add button click handler
                $('#add-btn').click(function () {
                    if ($('#all-players').children().length == 0) return;
                    var player = $('#all-players').val();
                    $($('#all-players').children()).filter(function () {return $(this).val() == player}).remove();
                    player = $.parseJSON(player);
                    onAddPlayer(player);
                });

                // set confirm dialog save button click handler
                $('#save-form').click(function () {saveTeam()});
            }

            function onAddPlayer(player) {
                var newrow = ('<tr data-playerid="{pid}">' +
                        '<td style="width:90%;">{desc}</td>' +
                        '<td class="remove-button-td">' +
                        '<a href="javascript:void(0)" class="btn btn-danger btn-team-form remove-button" ' +
                        'data-playerjson=\'{json}\'>Remove</a></td></tr>').format({
                            pid:  player.id,
                            desc: player.description,
                            json: JSON.stringify(player)
                        });
                console.log(newrow);
                $('#all-players-row').before(newrow);
                setRemoveClickHandlers();

                if ($('#all-players').children().length == 0) {
                    $('#add-btn').attr('disabled', true);
                    $('#all-players').attr('disabled', true);
                } else {
                    $('#add-btn').removeAttr('disabled');
                    $('#all-players').removeAttr('disabled');
                }
            }

            function onRemovePlayer(player) {
                var pdiv = '<option value=\'{json}\'>{desc}</option>'.format({
                    json: JSON.stringify(player),
                    desc: player.description
                });
                console.log(pdiv);
                var all = $('#all-players');
                all.append(pdiv);

                var allPlayers = all.find('option').sort(function (a, b) {return $(a).text() > $(b).text()});

                all.find('option').remove();
                all.append(allPlayers);

                if ($('#all-players').children().length == 0) {
                    $('#add-btn').attr('disabled', true);
                    $('#all-players').attr('disabled', true);
                } else {
                    $('#add-btn').removeAttr('disabled');
                    $('#all-players').removeAttr('disabled');
                }
            }

            function onSave() {
                $('#teamconfirm').html($('#team').html());
                $('#teamconfirm #all-players-row').remove();

                $('#teamconfirm .remove-button-td a').each(function () {
                    var members = $('#teamconfirm tr');
                    for (var i = 0; i < members.length; i++) {
                        var member = members[i];
                        var newrowid = $(member).data('playerid');
                        var oldrowid = $(this).data('playerjson').id;
                        console.log(oldrowid);
                        console.log(newrowid);
                        if (oldrowid == newrowid) {
                            console.log('match');
                            $(member).data('playerjson', $(this).data('playerjson'));
                        }
                    }
                });

                $('#teamconfirm .remove-button-td').remove();
            }

            function setRemoveClickHandlers() {
                $('.remove-button').unbind('click'); // unbind just in case
                $('.remove-button').click(function () {
                    console.log('remove player ' + $(this).data('playerjson').id);
                    var player = $(this).data('playerjson');
                    $('tr').filter(function () {return $(this).data('playerid') == player.id}).remove();
                    onRemovePlayer(player);
                });
            }

            function saveTeam() {
                // create the JSON object representing the updated team
                var teamJson = '{"id":{id},"teamMembers":{'.format({id: $('#teamconfirm').data('teamid')});

                var teamRows = $('#teamconfirm tr');
                var teamMembers;
                for (var i = 0; i < teamRows.length; i++) {
                    var row = teamRows[i];
                    var player = $(row).data('playerjson');
                    teamJson += '"{id}":{"firstName":"{fn}","lastName":"{ln}","studentNumber":"{sn}","id":{id}},'.format({
                        id: player.id,
                        fn: player.firstName,
                        ln: player.lastName,
                        sn: player.studentNumber
                    });
                }
                // remove the last ',' from the final element to ensure a valid JSON string
                teamJson = teamJson.replace(/,$/, '');
                teamJson += '}}';
                console.log(teamJson);

                //TODO: Send to service
            }
        </script>
    </jsp:attribute>
    <jsp:attribute name="modalconfirmbody">
        <table id="teamconfirm" class="table table-striped table-hover table-bordered" data-teamid="${item.id}"></table>
    </jsp:attribute>
    <jsp:body>
        <div class="control-group">
            <label class="control-label"
                   style="width:100px;"
                   for="team">Team</label>

            <div class="controls" style="margin-left:120px; margin-right:10px;">
                <table id="team" class="table table-striped table-hover table-bordered">
                    <c:forEach items="${item.teamMembers}" var="player">
                        <tr data-playerid="${player.value.id}">
                            <td style="width:90%;">${player.value.description}</td>
                            <td class="remove-button-td">
                                <a href="javascript:void(0)"
                                   class="btn btn-danger btn-team-form remove-button"
                                   data-playerjson='${player.value}'>Remove</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr id="all-players-row">
                        <td>
                            <select id="all-players" style="width:100%;">
                                <c:forEach items="${applicationScope.playerAccessor.playersWithoutTeam}" var="player">
                                    <option value='${player}'>${player.description}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td>
                            <a id="add-btn"
                               class="btn"
                               style="width:52px;"
                               href="javascript:void(0)">Add</a>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </jsp:body>
</t:base_item_form>
