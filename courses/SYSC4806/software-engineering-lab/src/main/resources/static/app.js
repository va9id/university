$(document).ready(function () {
    $("#add-buddy").on("click", function() {
        const id = $(".header").attr("addressBookId")
        const name = $("#name").val();
        const phone = $("#phone").val();

        const data = {
            name: name,
            phone: phone,
        }

        $.ajax({
            type: "POST",
            url: `http://localhost:8080/addressbook/${id}/spaAddBuddy`,
            data: JSON.stringify(data),
            contentType: "application/json"
        }).done(function(resp) {
            if (resp) {
                const buddy = `<p>${JSON.stringify(resp)}</p>`;
                $(".list").append(buddy);
            }
        })
    });
})