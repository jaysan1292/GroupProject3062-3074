<%@ page contentType="text/javascript" pageEncoding="UTF-8" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

        var loadingImage = 'img/spinner.gif';

        var menuItems;
        var isLoading = false;

    // Initialize
        $(document).ready(function () {
            getAll();

            // Set click listeners for left sidebar
            menuItems = $('#home-sidebar ul').children().not('.nav-header');
            menuItems.click(function () {onSidebarItemClick(this)});

            $.fn.center = function (div) {
                return this.each(function () {
                    div = typeof div != 'undefined' ? $(div) : window;
                    var xoff = 0, yoff = 0;
                    if (div !== window) {
                        xoff = div.position().left;
                        yoff = div.position().top;
                    }

                    var el = $(this);
                    var h = el.height();
                    var w = el.width();
                    var w_box = $(div).width();
                    var h_box = $(div).height();
                    var w_total = ((w_box - w) / 2) + xoff;
                    var h_total = ((h_box - h) / 2) + yoff;
                    var css = {
                        position: 'absolute',
                        left:     w_total + "px",
                        top:      h_total + "px"
                    };
                    el.css(css)
                });
            };
        });

    // The meat of the web app. Retrieve an item of the given type from the server
    // and display it in the detail view.
        function loadItem(itemType, itemId) {
            if (itemId == undefined) itemId = 0;
            console.log('Retrieving ' + itemType + ' ' + itemId);
            var ajaxUrl = '<c:url value="/ajax"/>?mode=one&type=' + itemType + '&id=' + itemId;
            console.log(ajaxUrl);
            //TODO: Show progress indicator

            $('#itemform').fadeOut(500, function () {
                $(this).remove();
                showProgress('#home-item-detail-container');
            });
            $.ajax({
                url:      ajaxUrl,
                dataType: 'html',
                type:     'GET',
                success:  function (data, textStatus, jqXHR) {
                    isLoading = false;
                    hideProgress('#home-item-detail-container', function () {
                        console.log('progress hidden')
                    });
                    $('#home-item-detail-container').html(data);
                    $('#home-item-detail').fadeIn(500);
                },
                error:    function (jqXHR, textStatus, errorThrown) {
                    console.log('error loading data');
                    hideProgress('#home-item-detail-container');
                },
                complete: function (jqXHR, textStatus) {
                    console.log('ajax complete');
                }
            });
        }

    // Counts the elements in the item list and puts the result at the very bottom
        function countListItems() {
            var count = getListItemCount();
            var str = count != 1 ? ' items' : ' item';
            $('#home-item-list-footer').html(count + str);
        }
        function setSelected(element) {
            var items = getListItems();
            $(items).removeClass('active');
            $(element).addClass('active');
        }
        function getAll() {
            var type = $('#home-sidebar ul').children().filter('.active').attr('data-type');
            var url = '<c:url value="/ajax"/>?mode=all&type=' + type;

            showProgress('#home-item-list-container');
            $.getJSON(url, function (data) {
                // Remove all items currently in the list
                $(getListItems()).remove();
                $.each(data, function (index, value) {
                    console.log(value);
                    var htmlstr = '<li data-itemid=' + value.id + ' style="display: none;">' +
                            value.description + '</li>';
                    $('#home-item-list-footer').before(htmlstr);
                });
                $('#home-item-list').children().removeClass('active');
                $('#home-item-list').children()[0].click();
                setSelected($('#home-item-list').children()[0]);
                initItemList();
                hideProgress('#home-item-list-container');
                $('#home-item-list li').fadeIn(500);
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

            var itemId = $(element).attr('data-itemid');

            var type = $('#home-sidebar ul').children().filter('.active').attr('data-type');

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

            loadItem($('#home-sidebar ul').children().filter('.active').attr('data-type'),
                    $(element).attr('data-itemid'));
        }

        function showProgress(div) {
            div = typeof div !== 'undefined' ? div : 'body';
            console.log('show spinner in ' + div);

            // Normally when you want to show progress, you're loading new content, so...
            isLoading = true;

            var spinner = new Image();
            spinner.onload = function () {
                var spindiv =
                        '<div id="spinner" data-parent="' + div + '" style="' +
                                'display: none; ' +
                                'background-image: url(' + spinner.src + '); ' +
                                'width: ' + this.width + 'px; ' +
                                'height: ' + this.height + 'px; ' +
                                'z-index: 9001;"></div>';

                $('body').append(spindiv);
                $('#spinner').center(div);
                $('#spinner').fadeIn(500);
            };

            spinner.src = loadingImage;
        }
        function hideProgress(div) {
            div = typeof div !== 'undefined' ? div : 'body';
            console.log('hide spinner in ' + div);
            isLoading = false;
            $('body').find('#spinner').filter(function () {
                return $(this).attr('data-parent') === div
            }).fadeOut(500, function () {$(this).remove()});
        }

    // Simple one-liner methods
        function getListItems() {return $('#home-item-list').children().not('#home-item-list-footer')}
        function getListItemCount() {return getListItems().length}
