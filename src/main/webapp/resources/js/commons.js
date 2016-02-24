var _pathname = window.location.pathname;
$( function() {
	//登出
	$('#layout_page_logout_a').click(function(){
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