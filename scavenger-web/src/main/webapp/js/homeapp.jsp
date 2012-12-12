<%@ page contentType="text/javascript" pageEncoding="UTF-8" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

        var loadingImage = '<c:url value="/img/fb-loader.gif"/>';

        var menuItems;
        var isLoading = false;

    // Initialize
        $(document).ready(function () {initAll()});

        function initAll() {
            getListItems().remove();
            getAll();

            // Set click listeners for left sidebar
            menuItems = $('#home-sidebar ul').children().not('.nav-header');
            menuItems.click(function () {onSidebarItemClick(this)});
            $('#create-new').click(function () {createNew()});
        }

    // The meat of the web app. Retrieve an item of the given type from the server
    // and display it in the detail view.
        function loadItem(itemType, itemId) {
            if (itemId == undefined) itemId = 0;
            console.log('Retrieving ' + itemType + ' ' + itemId);
            var ajaxUrl = '<c:url value="/ajax"/>?mode=one&type=' + itemType + '&id=' + itemId;
            console.log(ajaxUrl);

            showProgress('#home-item-detail-container');
            $('#itemform').fadeOut(500, function () {
                $(this).remove();
            });
            $.ajax({
                url:      ajaxUrl,
                dataType: 'html',
                type:     'GET',
                success:  function (data, textStatus, jqXHR) {
                    isLoading = false;
                    $('#home-item-detail-container').html(data);
                    $('#home-item-detail').fadeIn(500);
                },
                error:    function (jqXHR, textStatus, errorThrown) {
                    console.log('error loading data: {jq} {text} {error}'.f({
                        jq:    JSON.stringify(jqXHR),
                        text:  textStatus,
                        error: errorThrown
                    }));
                    $('#home-item-detail-container').html('<div id="err-modal" class="modal hide fade" role="dialog">' +
                            '<div class="modal-header">' +
                            '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' +
                            '<h3>Error</h3>' +
                            '</div>' +
                            '<div class="modal-body">' +
                            '<p>Sorry, it looks like the server gremlins broke something. ' +
                            'We\'ll get right around to fixing it as soon as possible!</p>' +
                            '<p><b>Error code</b>: {status} ({text})</p>'.f({status: jqXHR.status, text: jqXHR.statusText}) +
                            '</div>' +
                            '<div class="modal-footer">' +
                            '<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">OK</button>' +
                            '</div></div>');
                    $('#err-modal').modal();
                },
                complete: function (jqXHR, textStatus) {
                    console.log('ajax complete');
                    hideProgress('#home-item-detail-container');
                }
            });
        }

    // Counts the elements in the item list and puts the result at the very bottom
        function countListItems() {
            var count = getListItemCount();
            var name = count != 1 ? 'items' : 'item';
            $('#home-item-list-footer').html('{c} {n}'.f({c: count, n: name}));
        }
        function setSelected(element) {
            var items = getListItems();
            $(items).removeClass('active');
            $(element).addClass('active');
        }
        function getAll() {
            var type = $('#home-sidebar ul').children().filter('.active').data('type');
            var url = '<c:url value="/ajax"/>?mode=all&type=' + type;

            showProgress('#home-item-list-container');
            $.getJSON(url, function (data) {
                // Remove all items currently in the list
                $(getListItems()).remove();
                $.each(data, function (index, value) {
                    console.log(value);
                    var htmlstr = '<li data-itemid="{id}" style="display: none;">{desc}</li>'.f({
                        id:   value.id,
                        desc: value.description
                    });
                    $('#home-item-list-footer').before(htmlstr);
                });
                hideProgress('#home-item-list-container');
                $('#home-item-list li').fadeIn(500);
                initItemList();
                $('#home-item-list').children().removeClass('active');
                $('#home-item-list').children()[0].click();
                <%--setSelected(getListItems()[0]);--%>
                onListItemClick(getListItems()[0], getListItems());
            });
        }
        function initItemList() {
            // Set click listeners for item list
            var items = getListItems();
            items.click(function () {onListItemClick(this, items)});
            countListItems();
        }

        function onSidebarItemClick(element) {
            // Don't do anything if the clicked element is already selected
            if ($(element).hasClass('active') || isLoading) return;

            $(menuItems).removeClass('active');
            $(element).addClass('active');

            $('#home-item-detail').fadeOut(500, function () {$(this).remove()});
            $('#home-item-list li').fadeOut(500, function () {
                $(this).not('#home-item-list-footer').remove();
            });

            getAll();

            countListItems();

            var itemId = $(element).data('itemid');

            var type = $('#home-sidebar ul').children().filter('.active').data('type');

            var header;
            switch (type) {
                case 'challenge':
                    header = 'Challenges';
                    break;
                case 'checkpoint':
                    header = 'Checkpoints';
                    break;
                case 'path':
                    header = 'Paths';
                    break;
                case 'player':
                    header = 'Players';
                    break;
                case 'scavengerhunt':
                    header = 'Scavenger Hunts';
                    break;
                case 'team':
                    header = 'Teams';
                    break;
            }
            $('#home-item-list-header h4').text(header);

            loadItem(type, itemId);
        }

        function onListItemClick(element, itemList) {
            // Don't do anything if the clicked element is already selected
            if ($(element).hasClass('active') || isLoading) return;

            $(itemList).removeClass('active');
            $(element).addClass('active');

            loadItem($('#home-sidebar ul').children().filter('.active').data('type'),
                    $(element).data('itemid'));
        }

        const updateTypes = 'challenge,checkpoint,path,player,scavengerhunt,team'.split(',');
    // Update the given item
        function update(type, json) {
            if (typeof type !== 'string' || typeof json !== 'string') {
                throw new TypeError('Both arguments must be strings');
            }
            if ($.inArray(type, updateTypes) == -1) {
                throw new Error('Incorrect type: ' + type);
            }
            var obj = $.parseJSON(json);
            var host = '<c:url value="/ajax"/>';

            console.log(host);

            var sendobj = {
                type:  type,
                obj:   json,
                isnew: $('#home-item-detail').data('isnew')
            };

            $.ajax({
                url:      host,
                type:     'POST',
                data:     sendobj,
                success:  function (data, textStatus, jqXHR) {
                    var alert = '<div class="alert alert-success"><strong>Success!</strong> Item successfully updated.</div>';
                    $('body').append(alert);
                    $('.alert').width(300)
                            .center()
                            .css('top', -10)
                            .animate({top: 40}, 500)
                            .animate({top: '+=0'}, 2500)
                            .animate({top: -10}, 500, function () {$('.alert').alert('close')});
                    initAll();
                },
                error:    function (jqXHR, textStatus, errorThrown) {
                    var alert = '<div class="alert alert-error"><strong>Error!</strong> Item did not update successfully.</div>';
                    $('body').append(alert);
                    $('.alert').width(300).center().css('top', -10)
                            .animate({top: 40}, 500)
                            .animate({top: '+=0'}, 2500)
                            .animate({top: -10}, 500, function () {$('.alert').alert('close')});
                },
                complete: function (jqXHR, textStatus) {
                    $('#confirm-modal').modal('hide');
                }
            });
        }

        function createNew() {
            $.ajax({
                url:      '<c:url value="/ajax"/>?mode=create&type={type}'.f({
                    type: $('#home-sidebar ul li.active').data('type')
                }),
                dataType: 'html',
                type:     'GET',
                success:  function (data, textStatus, jqXHR) {
                    $('#home-item-detail-container').html(data);
                    $('#home-item-detail').fadeIn(500);
                }
            })
        }

        function showProgress(div) {
            div = typeof div !== 'undefined' ? div : 'body';
            console.log('show spinner in ' + div);

            // Normally when you want to show progress, you're loading new content, so...
            isLoading = true;

            var spinner = new Image();
            spinner.onload = function () {
                var spindiv = ('<div id="spinner" data-parent="{div}" style="' +
                        'display: none; ' +
                        'background-image: url({src}); ' +
                        'width: {width}px; ' +
                        'height: {height}px; ' +
                        'z-index: 900"></div>').f({
                            div:    div,
                            src:    spinner.src,
                            width:  this.width,
                            height: this.height
                        });

                $('body').append(spindiv);
                var spin = $('body').find('#spinner[data-parent="{div}"]'.f({div: div}));
                spin.center(div);
                spin.fadeIn(500);
            };

            spinner.src = loadingImage;
        }
        function hideProgress(div) {
            div = typeof div !== 'undefined' ? div : 'body';
            console.log('hide spinner in ' + div);

            $('body').find('#spinner[data-parent="{div}"]'.f({div: div})).each(function () {$(this).remove()});
            isLoading = false;
        }

    // Simple one-liner methods
        function getListItems() {return $('#home-item-list').children().not('#home-item-list-footer')}
        function getListItemCount() {return getListItems().length}
