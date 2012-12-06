<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="item" type="java.lang.String" required="true" %>

<div id="home-item-detail">
    <form class="form-horizontal">
        <legend>${item}</legend>
        <div class="control-group">
            <label class="control-label" for="testInput1">Some property</label>

            <div class="controls">
                <input type="text" id="testInput1">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="testInput2">Some other property</label>

            <div class="controls">
                <input type="text" id="testInput2">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="testInputDropdown">Some property (dropdown)</label>

            <div class="controls">
                <select id="testInputDropdown">
                    <c:forEach begin="1" end="10" varStatus="status">
                        <option value="${status.count}">${status.count}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="testInputMultiple">Property (multiple values)</label>

            <div class="controls">
                <select id="testInputMultiple" multiple="multiple">
                    <c:forEach begin="1" end="10" varStatus="status">
                        <option value="${status.count}">${status.count}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <label class="radio">
                    <input name="options" type="radio" value="opt1" checked>
                    Option one
                </label>
                <label class="radio">
                    <input name="options" type="radio" value="opt2">
                    Option two
                </label>
                <label class="radio">
                    <input name="options" type="radio" value="opt3">
                    Option three
                </label>
                <label class="radio">
                    <input name="options" type="radio" value="opt4">
                    Option four
                </label>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <label class="checkbox">
                    <input name="optionsCheckbox" type="checkbox" value="opt1" checked>
                    Option one
                </label>
                <label class="checkbox">
                    <input name="optionsCheckbox" type="checkbox" value="opt2">
                    Option two
                </label>
                <label class="checkbox">
                    <input name="optionsCheckbox" type="checkbox" value="opt3">
                    Option three
                </label>
                <label class="checkbox">
                    <input name="optionsCheckbox" type="checkbox" value="opt4">
                    Option four
                </label>
            </div>
        </div>
        <hr/>
        <div class="control-group">
            <div class="controls">
                <button type="button" class="btn btn-primary">Submit</button>
                <button type="reset" class="btn">Reset</button>
            </div>
        </div>
    </form>
</div>
