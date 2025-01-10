$(document).ready(function() {
    const baseUrl = window.location.origin;
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    function makeAjaxCall(method, endpointUrl) {
        $.ajax({
            type: method,
            url: baseUrl + endpointUrl
        })
            .done(function () {
                window.location.reload();
            });
    }

    function getStudentID(elem) {
        return $(elem).attr("student-id")
    }

    $('.notify').on("click", function() {
        const notifyEndpoint = "/notifyStudent/" + getStudentID(this);
        makeAjaxCall("PATCH", notifyEndpoint);
    });

});