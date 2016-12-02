$(document).ready(function(){
    var formEdit = $("#form-edit-issue")

    formEdit.submit(function(event) {
        event.preventDefault();

        $.ajax({
            url: formEdit.attr('action'),
            type: 'PUT',
            data: formEdit.serialize(),
            error: function () {
                alert("Edit fail");
            },
            success: function (result) {
                window.location.href = '/';
            }
        })
    })
})