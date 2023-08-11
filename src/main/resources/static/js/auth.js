$(document).ready(function() {
    $('.create-account-message a').on('click', function(event) {
        event.preventDefault();
        openNewAccountPage();
    });

    function openNewAccountPage() {
        window.open('/create_acc.html', '_self');
    }

    $('.submit-name').on('click', function() {
        let name = $('.email-input').val();
        let password = $('.password-input').val();
        if (name.trim().length === 0 || password.trim().length === 0) {
            alert('Please enter your email and password.');
            return;
        }
        authUser(name, password);
    });

    $('.email-input, .password-input').on('keydown', function(event) {
        if (event.key === 'Enter') {
            let name = $('.email-input').val();
            let password = $('.password-input').val();
            if (name.trim().length === 0 || password.trim().length === 0) {
                alert('Please enter your email and password.');
                return;
            }
            authUser(name, password);
        }
    });

    let authUser = function(name, password) {
        var encryptedEmail = encryptNameWithPassword(name, password);

        $.post('/auth', { encryptedEmail: encryptedEmail }, function(response) {
            if (response.result) {
                  window.location.href = "/chat.html";
            } else {
                $('.create-account-message').show();
            }
        });
    };

    function encryptNameWithPassword(name, password) {
        var key = CryptoJS.enc.Utf8.parse(password);
        var nameBytes = CryptoJS.enc.Utf8.parse(name);
        var encrypted = CryptoJS.AES.encrypt(nameBytes, key, {
            mode: CryptoJS.mode.ECB,
            padding: CryptoJS.pad.Pkcs7
        });
        return encrypted.toString();
    }

    $('.show-password').on('click', function() {
        var passwordInput = $('.password-input');
        var passwordFieldType = passwordInput.attr('type');

        if (passwordFieldType === 'password') {
            passwordInput.attr('type', 'text');
            $(this).text('Hide Password');
        } else {
            passwordInput.attr('type', 'password');
            $(this).text('Show Password');
        }
    });
});
