function update(id) {
    var formEdit = $("#form-edit-" + id);

    formEdit.submit(function(event) {
        event.preventDefault();
        $.ajax({
            url: formEdit.attr('action'),
            type: 'PUT',
            data: formEdit.serialize(),
            error: function () {
                alert("Update status fail");
            },
            success: function (result) {
            }
        })
    })

    formEdit.submit()
}
