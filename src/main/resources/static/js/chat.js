$(function() {
  let selectedUserName = null;
  let selectedUserValue = null;
  let isNewMessage = false;

  let initApplication = function() {
    $('.setup-page').hide();
    $('.messages-and-users').css({ display: 'flex' });
    $('.controls').css({ display: 'flex' });

    updateUsers();

    $('.users-list').on('click', '.userButton', function() {
      let userValue = $(this).data('userValue');
      handleUserClick($(this).text(), userValue);
    });

    $('.send-message').on('click', function() {
      let message = $('.new-message').val();
      sendMessage(message, selectedUserName);
    });

    $('.new-message').on('keydown', function(event) {
      if (event.key === 'Enter') {
        let message = $(this).val();
        sendMessage(message, selectedUserName);
      }
    });

    setInterval(updateMessages, 250);
    setInterval(updateUsers, 600);
  };

  $.get('/init', {}, function(response) {
    if (!response.result) {
      $('.setup-page').show();
    } else {
      initApplication();
    }
  });

  let updateUsers = function() {
    $.get('/user', {}, function(response) {
      $('.users-list').empty();
      for (const userName in response) {
        const userButton = $('<button class="userButton"></button>').text(userName);
        const userValue = response[userName];
        userButton.data('userValue', userValue);
        const userContainer = $('<div></div>').append(userButton);
        if (userValue > 0) {
          const dotElement = $('<span class="dot"></span>').text('📌');
          const valueElement = $('<span class="userValue"></span>');

          if (userName === selectedUserName && isNewMessage) {
            const activeUserValue = $('.userButton.active').siblings('.userValue');
            const activeDotElement = $('.userButton.active').siblings('.dot');
            if (activeDotElement.is(':visible')) {
              dotElement.show();
            }
          }
          userContainer.append(dotElement, valueElement);
        }
        $('.users-list').append(userContainer);
        if (userName === selectedUserName) {
          userButton.addClass('active');
          selectedUserValue = userValue;
        }
      }
    }).fail(function(error) {
      console.log('Error:', error);
    });
  };

  let handleUserClick = function(userName, userValue) {
    $('.userButton').removeClass('active');
    $('.userButton').filter(function() {
      return $(this).text() === userName;
    }).addClass('active');
    selectedUserName = userName;

    selectedUserValue = (userValue > 0) ? userValue : 0;
    $('.dot').hide();

    isNewMessage = false;
    updateMessages();
  };

  let updateMessages = function() {
    if (!selectedUserName) {
      return;
    }

    $.get('/message', { userName: selectedUserName }, function(response) {
      $('.messages-list').html('');
      for (let i in response) {
        let element = getMessageElement(response[i]);
        $('.messages-list').append(element);
      }
    });
  };

  let getMessageElement = function(message) {
    let item = $('<div class="message-item"></div>');
    let header = $('<div class="message-header"></div>');

    let dateTimeElement = $('<div class="datetime"></div>');
    dateTimeElement.text(message.dateTime);

    let usernameElement = $('<div class="username"></div>');
    usernameElement.text(message.userName);

    let statusElement = $('<div class="message-status"></div>');
    statusElement.text(message.status);

    header.append(dateTimeElement, usernameElement);

    let textElement = $('<div class="message-text"></div>');
    textElement.text(message.text);

    item.append(header, textElement, statusElement);

    if (message.userName === selectedUserName) {
      item.addClass('sent-by-me');
      textElement.addClass('message-text-right');
      usernameElement.addClass('username-right');
      dateTimeElement.addClass('datetime-right');
      statusElement.addClass('message-status-right');
    } else {
      item.addClass('sent-by-other');
      textElement.addClass('message-text-left');
      usernameElement.addClass('username-left');
      dateTimeElement.addClass('datetime-left');
      statusElement.addClass('message-status-left');
    }

    return item;
  };

  let sendMessage = function(message, userName) {
    if (!userName) {
      alert('Please select a user.');
      return;
    }
    if (message.trim().length === 0) {
      return;
    }
    handleUserClick(userName, selectedUserValue);
    $.post('/message', { message: message, userName: userName }, function(response) {
      if (response.result) {
        $('.new-message').val('');
        isNewMessage = true;
        updateMessages();
      } else {
        alert('Something went wrong. Try again!');
      }
    });
  };
});