'use strict'

jQuery(document).ready(function() {
    $.backstretch("assets/img/backgrounds/1.jpg");
    $('.show-register-form').on('click', function(){
    	if( ! $(this).hasClass('active') ) {
    		$('.show-login-form').removeClass('active');
    		$(this).addClass('active');
    		$('.login-form').fadeOut('fast', function(){
    			$('.register-form').fadeIn('fast');
    		});
    	}
    });
    $('.show-login-form').on('click', function(){
    	if( ! $(this).hasClass('active') ) {
    		$('.show-register-form').removeClass('active');
    		$(this).addClass('active');
    		$('.register-form').fadeOut('fast', function(){
    			$('.login-form').fadeIn('fast');
    		});
    	}
    });

    $('#signupBtn').on('click', function() {
        let success = function(token) {
            $('.show-login-form').click();
            setTimeout(function() {
                $('.show-info-message').addClass('information-message')
                $('.show-info-message').show();
            }, 200);
        };

        $.ajax({
            "crossDomain": true,
            "url": "http://localhost:8080/registerUser",
            "method": "POST",
            "headers": {
                "content-type": "application/json",
                "cache-control": "no-cache"
            },
            "data": JSON.stringify({
                email : $('#r-form-email').val(),
                user : $('#r-form-first-name').val(),
                password : CryptoJS.MD5($('#r-form-password').val()).toString(),
                jobTitle : $('#r-form-job-title').val()
            })
        }).success(success)
        }).error(function(jqXHR, textStatus, ajaxSettings, thrownError) {
            console.log(jqXHR.responseText);
            alert("Wrong credentials: " + jqXHR.responseJSON.message);
        });
    });

    function showUserMessage(message, type) {
        let infoMessageArea = $('.show-info-message');
        infoMessageArea.removeClass('error-message warning-message information-message');
        infoMessageArea.addClass(type);
        infoMessageArea.html(message);
        infoMessageArea.css('margin-top', '0px');
        infoMessageArea.show();

        infoMessageArea.animate({
            "margin-top":"7px"
        }, 'fast');
    }

    function clearUserMessage() {
        $('.show-info-message').html('');
    }

    $('#signInBtn').on('click', function() {
        clearUserMessage();

        let success = function(token) {
            let data = "_ijt=r2rhvmevleqqjrjkpacb4k1ivd";
            data = data.concat("&info=" + token).concat(";").concat($('#l-form-email').val());
            $(window).attr('location','/wakandaservice/web/dashboard.html?' + data);
        };

        let error = function(jqXHR, textStatus, ajaxSettings, thrownError) {
            if(jqXHR.status === 401) {
                showUserMessage('Email or Password incorrect, please try again', 'error-message');
            } else {
                showUserMessage('Sorry the servers are in maintenance, please try again later', 'warning-message');
            }

            $('#loadingLoginSpinner').hide();
            $('#textLoginBtn').show();
        };

        let ajaxData = {
            "crossDomain": true,
            "url": "http://localhost:8080/login",
            "method": "POST",
            "headers": {
                "content-type": "application/x-www-form-urlencoded",
                "cache-control": "no-cache"
            },
            "data": {
                "email": $('#l-form-email').val(),
                "password": CryptoJS.MD5($('#l-form-password').val()).toString()
            },
        };

        $('#textLoginBtn').hide();
        $('#loadingLoginSpinner').show();
        setTimeout(function(){
            $.ajax(ajaxData).done(function() {
            }).success(success).error(error);
        }, 100);


    });
