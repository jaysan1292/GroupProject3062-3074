<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="item" type="com.jaysan1292.groupproject.data.Player" required="true" %>
<%@ attribute name="isnew" type="java.lang.Boolean" required="true" %>
<%@ attribute name="type" type="java.lang.String" required="true" %>

<t:base_item_form name="${item.description}" isnew="${isnew}" type="${type}">
    <jsp:attribute name="formjavascript">
        <script type="text/javascript">
            $(document).ready(function () {
                $('#itemform').validate({
                    rules:      {
                        firstName:     'required',
                        lastName:      'required',
                        password:      {
                            required:  ${isnew},
                            minlength: 6
                        },
                        passwordcheck: {
                            required:  ${isnew},
                            minlength: 6,
                            equalTo:   '#password'
                        },
                        studentNumber: {
                            required:  true,
                            minlength: 9,
                            maxlength: 9,
                            digits:    true
                        }
                    },
                    messages:   {
                        firstName:     'First name is required.',
                        lastName:      'Last name is required.',
                        password:      {
                            equalTo:  'Both passwords must match.',
                            required: 'Password is required.'
                        },
                        passwordcheck: {
                            equalTo:  'Both passwords must match.',
                            required: 'Password is required.'
                        },
                        studentNumber: {
                            required:  'Student ID is required.',
                            minlength: 'Student ID must be 9 characters.',
                            maxlength: 'Student ID must be 9 characters.',
                            digits:    'Student ID must only contain digits.'
                        }
                    },
                    showErrors: function (errorMap, errorList) {
                        if ($('#itemform').validate().numberOfInvalids() != 0) {
                            $('#save').removeAttr('data-toggle');
                            $('#save').removeAttr('href');
                        } else {
                            $('#save').attr('data-toggle', 'modal');
                            $('#save').attr('href', '#confirm-modal');
                        }
                        this.defaultShowErrors();
                    }
                });

                $('#save').click(function () {
                    if ($('#itemform').validate().form()) {
                        onSave();
                    } else {
                        $('#confirm-modal').modal('hide');
                    }
                });
                $('#save-form').click(function () {savePlayer()});
            });

            function onSave() {
                var player = {
                    id:            $('#player-id').data('playerid'),
                    firstName:     $('#firstName').val(),
                    lastName:      $('#lastName').val(),
                    studentNumber: $('#studentNumber').val()
                    <c:if test="${isnew}">,
                    plainPassword: $('#password').val()
                    </c:if>
                };

                $('#player-data').data('pid', player.id);
                $('#player-first-name').text(player.firstName);
                $('#player-last-name').text(player.lastName);
                $('#player-student-number').text(player.studentNumber);
            <c:if test="${isnew}">
                $('#player-password').text(player.plainPassword);
            </c:if>

            }

            function savePlayer() {
                var player = {
                    id:            $('#player-data').data('pid'),
                    firstName:     $('#player-first-name').text(),
                    lastName:      $('#player-last-name').text(),
                    studentNumber: $('#player-student-number').text()
                    <c:if test="${isnew}">,
                    plainPassword: $('#player-password').text()
                    </c:if>
                };
                player = JSON.stringify(player);

                console.log(player);

                update('player', player);
            }
        </script>
    </jsp:attribute>
    <jsp:attribute name="modalconfirmbody">
        <table class="table table-striped table-hover table-bordered" id="player-data">
            <tr>
                <td style="width:150px;">First Name</td>
                <td id="player-first-name"></td>
            </tr>
            <tr>
                <td style="width:150px;">Last Name</td>
                <td id="player-last-name"></td>
            </tr>
            <tr>
                <td style="width:150px;">Student ID</td>
                <td id="player-student-number"></td>
            </tr>
            <c:if test="${isnew}">
                <tr>
                    <td style="width:150px;">Password</td>
                    <td id="player-password"></td>
                </tr>
            </c:if>
        </table>
    </jsp:attribute>
    <jsp:body>
        <span class="hide" id="player-id" data-playerid="${item.id}"></span>

        <div class="control-group">
            <label class="control-label" for="firstName">First Name</label>

            <div class="controls">
                <input type="text"
                       id="firstName"
                       name="firstName"
                       value="${item.firstName}">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="lastName">Last Name</label>

            <div class="controls">
                <input type="text"
                       id="lastName"
                       name="lastName"
                       value="${item.lastName}">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="studentNumber">Student ID</label>

            <div class="controls">
                <input type="text"
                       id="studentNumber"
                       name="studentNumber"
                       pattern="[0-9]*"
                       value="${item.studentNumber}">
            </div>
        </div>
        <c:if test="${isnew}">
            <div class="control-group">
                <label id="password-label" class="control-label" for="password"></label>
                <label for="passwordcheck" style="display: none;"></label>

                <div class="controls">
                    <input type="password"
                           id="password"
                           name="password"
                           placeholder="Password">
                    <input type="password"
                           id="passwordcheck"
                           name="passwordcheck"
                           placeholder="Confirm Password">
                </div>
            </div>
        </c:if>
    </jsp:body>
</t:base_item_form>
