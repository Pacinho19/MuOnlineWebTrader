$(function () {
    $('[data-toggle="popover"]').popover(
     {
        html: true,
        sanitize: false
     })
})

$(function() {
    $('#popover')
    .attr('data-content', $('#source').html());
});