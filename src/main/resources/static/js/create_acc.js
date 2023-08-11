$(document).ready(function() {
    $('.create-account-message a').on('click', function(event) {
        event.preventDefault();
        openNewAccountPage();
    });

    function openNewAccountPage() {
        window.open('/new_account', '_self');
    }

    $('.submit-name').on('click', function() {
        let email = $('.email-input').val();
        let password = $('.password-input').val();
        let username = $('.username-input').val();
        if (email.trim().length === 0 || password.trim().length === 0 || username.trim().length === 0) {
            alert('Please enter your email, password, and username.');
            return;
        }
        authUser(email, password, username);
    });

    $('.email-input, .password-input, .username-input').on('keydown', function(event) {
        if (event.key === 'Enter') {
            let email = $('.email-input').val();
            let password = $('.password-input').val();
            let username = $('.username-input').val();
            if (email.trim().length === 0 || password.trim().length === 0 || username.trim().length === 0) {
                alert('Please enter your email, password, and username.');
                return;
            }
            authUser(email, password, username);
        }
    });

    let authUser = function(email, password, username) {
        var encryptedEmail = encryptNameWithPassword(email, password);
        $.post('/new_account', { encryptedEmail: encryptedEmail, username: username }, function(response) {
            if (response.result) {
            window.location.href = "/chat.html";

            } else {
                $('.create-account-message')
                    .html('Create failed. Your user email and password are exist.')
                    .show();
            }
        });
    };

    function encryptNameWithPassword(email, password) {
        var key = CryptoJS.enc.Utf8.parse(password);
        var emailBytes = CryptoJS.enc.Utf8.parse(email);
        var encrypted = CryptoJS.AES.encrypt(emailBytes, key, {
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