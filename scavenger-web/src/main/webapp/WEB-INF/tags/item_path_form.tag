<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="item" type="com.jaysan1292.groupproject.data.Path" required="true" %>

<t:base_item_form name="${item.description}">
<jsp:attribute name="formjavascript">
        <script type="text/javascript">
            $(document).ready(initPath);

            function initPath() {
                var pathMembers = new Array();
                for (var i = 0; i < $('.btn-path-form').length; i++) {
                    pathMembers[i] = $($('.btn-path-form')[i]).data('checkpointjson');
                }

                // Remove checkpoints that are already on the path from the checkpoint list
                $($('#all-checkpoints').children()).each(function () {
                    var checkpoint = $.parseJSON($(this).attr('value'));
                    for (var i = 0; i < pathMembers.length; i++) {
                        if (JSON.stringify(checkpoint) === JSON.stringify(pathMembers[i])) {
                            $(this).remove();
                        }
                    }
                });

                // set reset button handler
                $('#path').data('originalstate', $('#path').html().replace(/\s\s/g, ''));
                $('#reset').click(function () {
                    $('#path').html($('#path').data('originalstate'));
                    initPath();
                });

                // set submit button handler
                $('#save').click(function () {onSave()});

                setButtonClickHandlers();

                // set add button click handler
                $('#add-btn').click(function () {
                    if ($('#all-checkpoints').children().length == 0) return;
                    var checkpoint = $('#all-checkpoints').val();
                    $($('#all-checkpoints').children()).filter(function () {return $(this).val() == checkpoint}).remove();
                    checkpoint = $.parseJSON(checkpoint);
                    onAddCheckpoint(checkpoint);
                });

                // set confirm dialog save button click handler
                $('#save-form').click(function () {savePath()});
            }

            function onAddCheckpoint(checkpoint) {
                var newrow = ('<tr data-checkpointid="{pid}">' +
                        '<td style="width:247px;">{desc}</td>' +
                        '<td class="button-td">' +
                        '<a href="javascript:void(0)" class="btn btn-move-up">&uarr;</a> ' +
                        '<a href="javascript:void(0)" class="btn btn-move-down">&darr;</a> ' +
                        '<a href="javascript:void(0)" class="btn btn-danger btn-path-form remove-button" ' +
                        'data-checkpointjson=\'{json}\'>&times;</a></td></tr>').format({
                            pid:  checkpoint.id,
                            desc: checkpoint.description,
                            json: JSON.stringify(checkpoint)
                        });
                console.log(newrow);
                $('#all-checkpoints-row').before(newrow);
                setButtonClickHandlers();

                if ($('#all-checkpoints').children().length == 0) {
                    $('#add-btn').attr('disabled', true);
                    $('#all-checkpoints').attr('disabled', true);
                } else {
                    $('#add-btn').removeAttr('disabled');
                    $('#all-checkpoints').removeAttr('disabled');
                }
            }

            function onRemoveCheckpoint(checkpoint) {
                var pdiv = '<option value=\'{json}\'>{desc}</option>'.format({
                    json: JSON.stringify(checkpoint),
                    desc: checkpoint.description
                });
                console.log(pdiv);
                var all = $('#all-checkpoints');
                all.append(pdiv);

                var allCheckpoints = all.find('option').sort(function (a, b) {return $(a).text() > $(b).text()});

                all.find('option').remove();
                all.append(allCheckpoints);

                if ($('#all-checkpoints').children().length == 0) {
                    $('#add-btn').attr('disabled', true);
                    $('#all-checkpoints').attr('disabled', true);
                } else {
                    $('#add-btn').removeAttr('disabled');
                    $('#all-checkpoints').removeAttr('disabled');
                }
            }

            function onSave() {
                $('#pathconfirm').html($('#path').html());
                $('#pathconfirm #all-checkpoints-row').remove();

                $('#pathconfirm .button-td a.remove-button').each(function () {
                    var members = $('#pathconfirm tr');
                    for (var i = 0; i < members.length; i++) {
                        var member = members[i];
                        var newrowid = $(member).data('checkpointid');
                        var oldrowid = $(this).data('checkpointjson').id;
                        console.log(oldrowid);
                        console.log(newrowid);
                        if (oldrowid == newrowid) {
                            console.log('match');
                            $(member).data('checkpointjson', $(this).data('checkpointjson'));
                        }
                    }
                });

                $('#pathconfirm .button-td').remove();
            }

            function setButtonClickHandlers() {
                $('.remove-button, .btn-move-up, .btn-move-down').unbind('click'); // unbind just in case
                $('.remove-button').click(function () {
                    console.log('remove checkpoint ' + $(this).data('checkpointjson').id);
                    var checkpoint = $(this).data('checkpointjson');
                    $('tr').filter(function () {return $(this).data('checkpointid') == checkpoint.id}).remove();
                    onRemoveCheckpoint(checkpoint);
                });
                $('.btn-move-up').click(function () {moveCheckpoint('up', this)});
                $('.btn-move-down').click(function () {moveCheckpoint('down', this)});

                $('.btn-move-up, .btn-move-down').removeAttr('disabled');

                var checkpoints = $('#path tr').not('#all-checkpoints-row');
                $(checkpoints[0]).find('.btn-move-up').unbind('click').attr('disabled', true);
                $(checkpoints[checkpoints.length - 1]).find('.btn-move-down').unbind('click').attr('disabled', true);
            }

            function moveCheckpoint(direction, context) {
                var checkpointId = $(context).parent().parent().data('checkpointid');
                console.log('{cid}, {dir}'.f({cid: checkpointId, dir: direction}));

                var checkpoint = $('#path').find('tr[data-checkpointid="{id}"]'.f({id: checkpointId}));
                console.log(checkpoint);
                switch (direction) {
                    case 'up':
                        $(checkpoint).prev().before(checkpoint);
                        break;
                    case 'down':
                        $(checkpoint).next().after(checkpoint);
                        break;
                }

                setButtonClickHandlers();
            }

            function savePath() {
                // create the JSON object representing the updated path
                var path = {};
                var checkelements = $('.remove-button'); // The remove button contains the checkpoint JSON data

                var checkpoints = new Array();
                for (var i = 0; i < checkelements.length; i++) {
                    checkpoints[i] = $(checkelements[i]).data('checkpointjson');
                    console.log(checkpoints[i]);
                }

                path.id = $('#path-id').text();
                path.checkpoints = checkpoints;

                console.log(path);

                console.log('before clean');
                cleanOutput(path);
                console.log('after clean');

                path = JSON.stringify(path);

                console.log(path);
                //TODO: Send to service
            }
        </script>
    </jsp:attribute>
    <jsp:attribute name="modalconfirmbody">
        <table id="pathconfirm" class="table table-striped table-hover table-bordered" data-pathid="${item.id}"></table>
    </jsp:attribute>
<jsp:body>
    <span class="hide" id="path-id">${item.id}</span>

    <div class="control-group">
        <label class="control-label"
               style="width:50px;"
               for="path">Path</label>

        <div class="controls" style="margin-left:70px; margin-right:10px;">
            <table id="path" class="table table-striped table-hover table-bordered">
                <c:forEach items="${item.checkpoints}" var="checkpoint">
                    <tr data-checkpointid="${checkpoint.id}">
                        <td style="width:247px;">${checkpoint.description}</td>
                        <td class="button-td">
                            <a href="javascript:void(0)"
                               class="btn btn-move-up">&uarr;</a>
                            <a href="javascript:void(0)"
                               class="btn btn-move-down">&darr;</a>
                            <a href="javascript:void(0)"
                               class="btn btn-danger btn-path-form remove-button"
                               data-checkpointjson='${checkpoint}'>&times;</a>
                        </td>
                    </tr>
                </c:forEach>
                <tr id="all-checkpoints-row">
                    <td>
                        <select id="all-checkpoints" style="width:100%;">
                            <c:forEach items="${applicationScope.checkpointAccessor.all}" var="checkpoint">
                                <option value='${checkpoint}'>${checkpoint.description}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td>
                        <a id="add-btn"
                           class="btn"
                           style="width:82px;"
                           href="javascript:void(0)">Add</a>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</jsp:body>
</t:base_item_form>
