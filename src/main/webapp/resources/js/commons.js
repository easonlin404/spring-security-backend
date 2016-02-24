var _pathname = window.location.pathname;
$(function() {
	
	/**
	 * include the CSRF token within all Ajax requests */
	
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	}); 
	
	//登出
	$('#layout_page_logout_a').click(function() {
		$('#layout_page_logout_form').submit();
	});

	$(".nav li").removeClass("active");//this will remove the active class from  
	//previously active menu item 

	var $sideBarMenu = $('#sideBarMenu');

	if (_pathname == '/backend/userManagement')
		$('#userManagement,#userManagementLi', $sideBarMenu).addClass('active');
	else if (_pathname == '/backend/')
		$('#dashboard', $sideBarMenu).addClass('active');

	console.log("path:" + _pathname);

});

$.fn.grid = function(settings) {
	var defaultSettings = {
		$gridTable : $('#gridTable'),
		queryURL: null
	}

	return this.each(function() {
		var newSettings = $.extend(defaultSettings, settings);

		if ( !check(newSettings) ) return;
		
		console.log('grid initial success');
		
		queryGridData(newSettings);
	});

	function check( settings ) {
		if ( settings.$gridTable.length == 0 ) {
			alert('尚未指定$gridTable');
			false;
		}
		
		if( settings.queryURL == null || $.trim( settings.queryURL ).length == 0 ){
			alert( '尚未指定  queryURL' );
			return false;
		}
		
		return true;
	}
	
	function queryGridData( settings ) {
		_ajax.get(settings.queryURL,function( data ){
			console.log(data);
		});
	}
	

};

var _ajax = {
		post:function(url, successCallback ) {
			$.ajax({
				  type: "POST",
				  url: url,
				  success: successCallback
			});
		},
		get:function(url, successCallback){
			$.ajax({
				  type: "GET",
				  url: url,
				  success: successCallback
			});
		}
}
