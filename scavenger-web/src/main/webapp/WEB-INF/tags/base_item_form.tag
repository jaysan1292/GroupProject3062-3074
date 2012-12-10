<%@ tag isELIgnored="false" pageEncoding="UTF-8" %>
<%@ attribute name="name" type="java.lang.String" required="true" %>
<%@ attribute name="formjavascript" fragment="true" required="false" %>
<%@ attribute name="modalconfirmbody" fragment="true" required="true" %>

<%--initially start invisible for animation stuff--%>
<div id="home-item-detail" style="display:none;">
    <form class="form-horizontal" id="itemform">
        <legend>${name}</legend>
        <jsp:doBody/>
        <hr/>
        <div class="control-group">
            <div class="controls">
                <a id="save"
                   href="#confirm-modal" type="button"
                   class="btn btn-primary"
                   data-toggle="modal">Save
                </a>
                <button id="reset"
                        type="reset"
                        class="btn">Reset
                </button>
            </div>
        </div>
    </form>
    <jsp:invoke fragment="formjavascript"/>
    <div id="confirm-modal"
         class="modal hide fade"
         tabindex="-1"
         role="dialog"
         aria-hidden="true">
        <div class="modal-header">
            <button type="button"
                    class="close"
                    data-dismiss="modal"
                    aria-hidden="true">&times;</button>
            <script type="text/javascript">
                $('.modal-header h3').text("Really save '{name}'?".format({
                    name: '${name}'.substr(0, 6)
                }))
            </script>
            <h3></h3>
        </div>
        <div class="modal-body" id="confirm-body">
            <jsp:invoke fragment="modalconfirmbody"/>
        </div>
        <div class="modal-footer">
            <a href="javascript:void(0)"
               class="btn"
               aria-hidden="true"
               data-dismiss="modal">Cancel</a>
            <a id="save-form"
               href="javascript:void(0)"
               class="btn btn-primary">Save</a>
        </div>
    </div>
</div>
