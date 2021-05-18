$('#password, #confirm_password').on('keyup', function () {
    if ($('#password').val() === $('#confirm_password').val()) {
        $('#message').html('').css('color', 'green');
    } else
        $('#message').html('<p class="alert-danger" >Password does not match</p>');
});


function check_pass() {
    if (document.getElementById('password').value === document.getElementById('confirm_password').value) {
        document.getElementById('register').disabled = false;
    } else {
        document.getElementById('register').disabled = true;
    }
}


function show() {
    var p = document.getElementById('pwd');
    p.setAttribute('type', 'text');
}

function hide() {
    var p = document.getElementById('pwd');
    p.setAttribute('type', 'password');
}

var pwShown = 0;

document.getElementById("eye").addEventListener("click", function () {
    if (pwShown === 0) {
        pwShown = 1;
        show();
    } else {
        pwShown = 0;
        hide();
    }
}, false);