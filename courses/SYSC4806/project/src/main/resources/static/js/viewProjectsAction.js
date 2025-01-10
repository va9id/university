$(document).ready(function() {
    const baseUrl = window.location.origin;
    const patchMethod = "PATCH";
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

    function getProjectID(elem) {
        return $(elem).parent().attr("project-id")
    }

   $('.delete').on("click", function() {
       const deleteEndpoint = "/deleteProject/" + getProjectID(this);
       makeAjaxCall("DELETE", deleteEndpoint);
   });

    $('.join').on("click", function() {
        const joinEndpoint = "/project/" + getProjectID(this) + "/addStudent/" + $(this).attr("user-id");
        makeAjaxCall(patchMethod, joinEndpoint);
    });

    $('.leave').on("click", function() {
        const leaveEndpoint = "/project/" + getProjectID(this) + "/removeStudent/" + $(this).attr("user-id");
        makeAjaxCall(patchMethod, leaveEndpoint);
    });
});