
jQuery(document).ready(function() {
	
    /*
        Fullscreen background
    */
    $.backstretch("assets/img/backgrounds/1.jpg");
    
    /*
        Forms show / hide
    */
    $('.show-register-form').on('click', function(){
    	if( ! $(this).hasClass('active') ) {
    		$('.show-login-form').removeClass('active');
    		$(this).addClass('active');
    		$('.login-form').fadeOut('fast', function(){
    			$('.register-form').fadeIn('fast');
    		});
    	}
    });
    // ---
    $('.show-login-form').on('click', function(){
    	if( ! $(this).hasClass('active') ) {
    		$('.show-register-form').removeClass('active');
    		$(this).addClass('active');
    		$('.register-form').fadeOut('fast', function(){
    			$('.login-form').fadeIn('fast');
    		});
    	}
    });
    
    /*
        Login form validation
    */
    $('.l-form input[type="text"], .l-form input[type="password"], .l-form textarea').on('focus', function() {
    	$(this).removeClass('input-error');
    });
    
    $('.l-form').on('submit', function(e) {
    	
    	$(this).find('input[type="text"], input[type="password"], textarea').each(function(){
    		if( $(this).val() == "" ) {
    			e.preventDefault();
    			$(this).addClass('input-error');
    		}
    		else {
    			$(this).removeClass('input-error');
    		}
    	});
    	
    });
    
    /*
        Registration form validation
    */
    $('.r-form input[type="text"], .r-form textarea').on('focus', function() {
    	$(this).removeClass('input-error');
    });
    
    $('.r-form').on('submit', function(e) {
    	
    	$(this).find('input[type="text"], textarea').each(function(){
    		if( $(this).val() == "" ) {
    			e.preventDefault();
    			$(this).addClass('input-error');
    		}
    		else {
    			$(this).removeClass('input-error');
    		}
    	});

    	signUp();
        $('.show-login-form').click();
        e.preventDefault();
    });

    function signUp() {
        var password = CryptoJS.MD5($('#r-form-password').val()).toString();
        var data = {"email": $('#r-form-email').val(), "user": $('#r-form-first-name').val(), "password": password};

        var settings = {
            "async": false,
            "crossDomain": true,
            "url": "http://localhost:8080/registerUser",
            "method": "POST",
            "headers": {
                "content-type": "application/x-www-form-urlencoded",
                "cache-control": "no-cache"
            },
            "data": data
        }

        $.ajax(settings).success(function() {
            alert('Ok');
        }).error(function(jqXHR, textStatus, ajaxSettings, thrownError) {
            console.log(jqXHR.responseText);
            alert("Wrong credentials: " + jqXHR.responseJSON.message);
        });
    }

    function signIn() {
        var pass = CryptoJS.MD5($('#l-form-password').val()).toString();
        var data = {"user": $('#l-form-username'), "password": pass};

        var settings = {
            "async": false,
            "crossDomain": true,
            "url": "http://localhost:8080/login",
            "method": "POST",
            "headers": {
                "content-type": "application/x-www-form-urlencoded",
                "cache-control": "no-cache"
            },
            "data": data
        }

        $.ajax(settings).success(function(token) {
            alert('Ok');
        }).error(function(jqXHR, textStatus, ajaxSettings, thrownError) {
            console.log(jqXHR.responseText);
            alert("Wrong credentials");
        });
    }
});
