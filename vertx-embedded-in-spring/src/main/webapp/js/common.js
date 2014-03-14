"use strict";

$(document).ready(function(){
    var textArea = document.getElementById('chatTextArea');
    textArea.scrollTop = 99999;

    $('body').on("click", "#sendChat", function(event) {
        event.preventDefault();
        chat.sendChatMessage();
    });

    chat.createSockJSSocket();
});

var chat = {
    sock: null,

    receiveChatMessage: function (data) {
        document.getElementById('chatTextArea').value += data.data;
    },

    sendChatMessage: function() {
        var message = document.getElementById("message");
        $.ajax({
            url : "/vertx-embedded-in-spring/chat",
            type : "POST",
            contentType : "application/json",
            dataType : "json",
            processData: false,
            data : message.value
        }).done(function() {
            var formObject = document.getElementById("chatForm");
            formObject.clearForm();
        }).fail(function() {
            alert("In Fail");
        });
    },

    createSockJSSocket: function() {
        if (chat.sock === null) {
            try {
                chat.setUpSockJS();
            } catch (err) {
                alert("Unable to create SockJS Socket to server");
            }
        }
    },

    setUpSockJS: function() {
        chat.sock = new SockJS("http://localhost:8083/chat");
        chat.sock.onopen = function() {};

        this.sock.onmessage = this.receiveChatMessage.bind(this);

        chat.sock.onclose = function() {
            chat.sock = null;
        };
    }
};

$.fn.clearForm = function() {
    return this.each(function() {
        var type = this.type, tag = this.tagName.toLowerCase();
        if (tag === 'form') {
            return $(':input',this).clearForm();
        }
        if (type === 'text' || type === 'password' || tag === 'textarea' || type === "email") {
            this.value = '';
        } else if (type === 'checkbox' || type === 'radio') {
            this.checked = false;
        } else if (tag === 'select') {
            this.selectedIndex = -1;
        }
    });
};