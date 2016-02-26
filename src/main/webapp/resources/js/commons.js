var _pathname = window.location.pathname;
$(function() {

	/**
	 * include the CSRF token within all Ajax requests */

	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");

	$(document).ajaxSend(function(e, xhr, options) {
		//waitingDialog.show();
		xhr.setRequestHeader(header, token);
	});
	
//	$(document).ajaxStop(function(){
//		setTimeout(function(){
//			waitingDialog.hide();
//			}, 500);
//		
//     });
	
	

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
	return this.each(function() {
		var $app = $( this );
		
		var defaultSettings = {
				$gridTable : $( '.table', $app ),
				$pagination: $( '.pagination', $app ),
				$dataFormDialog:	 $( '#dataForm', $app ),
				$PopUpAddBtn: $('#popUpAddPage', $app ),
				queryURL : null,
				pageSize: 10
			}

		var newSettings = $.extend( defaultSettings, settings );

		if ( !check( newSettings ) )
			return;

		console.log('grid initial success');

		initGridData(newSettings);
		bindDataForm(newSettings);
	});

	function check(settings) {
		if (settings.$gridTable.length == 0 ) {
			alert('尚未指定 $gridTable');
			false;
		}
		
		if (settings.$pagination.length == 0 ) {
			alert('尚未指定 $pagination');
			false;
		}
		
		if (settings.$dataFormDialog.length == 0 ) {
			alert('尚未指定 $dataFormDialog');
			false;
		}
		if (settings.$PopUpAddBtn.length == 0 ) {
			alert('尚未指定 $PopUpAddBtn');
			false;
		}

		if (settings.queryURL == null || $.trim(settings.queryURL).length == 0) {
			alert('尚未指定  queryURL');
			return false;
		}
		if (settings.pageSize == null) {
			alert('尚未指定  pageSize');
			return false;
		}

		return true;
	}

	function initGridData(settings, page) {
		if ( page == undefined )
			page =1;
		
		_ajax.get(settings.queryURL+"/"+ page +"/?size="+settings.pageSize, function(data) {
			console.log(data);
			
			var table="";
			var pageData = data.content;
			
			$(pageData).each(function( index ){
				//TODO:屬性要從設定取得
				var userName = pageData[index].userName;
				var password = pageData[index].password;
				var enabled = pageData[index].enabled;
				table +="<tr class='gridRow' >";
				table += "<td>"+index +"</td>";
				table += "<td>"+userName +"</td>"
				table += "<td>"+password +"</td>";
				table += "<td>"+enabled +"</td>";
				table += '<td>' +
							'<button type="button" class="btn btn-info">'+
							'<span class="glyphicon glyphicon-edit"></span> Edit' +
							'</button> '+
							'<button type="button" class="btn btn-danger">'+
							'<span class="glyphicon glyphicon-remove"></span> Delete' +
							'</button>'+
						'</td>';
				table +="</tr>";
				
				
			});
			
			
			
			//組成pager
			var pager = "";
			
			var totalPages =  data.totalPages;
			var isFirst =  data.first;
			var current = data.number + 1;
			
			//如果是第一頁，Previous 要disable
			if (data.first == true) {
				pager +='<li class="disabled"><a href="javascript:void(0);" aria-label="Previous">'+
				' <span aria-hidden="true">&laquo;</span>'+
				'</a></li>';
			}else {
				pager +='<li><a href="javascript:void(0);" aria-label="Previous">'+
				' <span aria-hidden="true">&laquo;</span>'+
				'</a></li>';
			}
			
			for(var i =1; i<= totalPages; i++ ) {
				if ( i == current )
					pager +='<li class="active"><a href="javascript:void(0);">'+ i +'</a></li>';
				else
					pager +='<li><a href=" javascript:void(0);">'+ i +'</a></li>';
				
			}
			
			//如果是最後一頁，Next要disable
			if (data.last  == true) {
				pager += '<li class="disabled"><a href="javascript:void(0);" aria-label="Next">'+
				' <span aria-hidden="true">&raquo;</span>'+
				'</a></li>';
			}else{
				pager += '<li><a href="#" aria-label="Next">'+
				' <span aria-hidden="true">&raquo;</span>'+
				'</a></li>';
			}
			
			//清空舊有的 , event是否需要unbind?
			$( '.gridRow', settings.$gridTable ).remove();
			settings.$pagination.empty();
			
			//render頁面
			settings.$gridTable.append(table);
			
			settings.$pagination.append(pager);
			
			
				
			
			//bind on click
			$( 'a', settings.$pagination ).click( onPagerClik( settings,data.totalPages ) );
			
			
			
		});
	
	} 
	
	function onPagerClik( settings, totalPages ) {
		return function() {
			$ele =  $( this );
			$currentLiEle = $ele.parent( 'li' );
			var label = $ele.attr( "aria-label" );
			
			if ( label == undefined ) { //有頁數的，跳往指定的頁數
				if( $currentLiEle.hasClass( "active" )){ //案同一頁，無動作
					// noting to do
				}else{ // 跳到指定頁數
					$currentLiEle.siblings( 'li' ).removeClass( 'active' );
					$currentLiEle.addClass( 'active' );
					
					var page = $ele.text();
					console.log(page);
					console.log(totalPages);
					
					initGridData(settings, page);
					
				}
			}else{
				
				if( $currentLiEle.hasClass('disabled') )
					return;
				
				//取得目前active的頁數
				var currentPage=  $currentLiEle.siblings().filter('.active').find('a').text();
				console.log(currentPage);
				if ( label == 'Next' ) { //跳下一頁
					initGridData(settings, ++currentPage);
				}else { //Previous//回上一頁
					initGridData(settings, --currentPage);
					
				}
			}
				
		};
	}
	
	function bindDataForm (settings) {
//		settings.$PopUpAddBtn.click(function(){
//			settings.$dataFormDialog.modal();
//		});
	}

};

var _ajax = {
	post : function(url, successCallback) {
		$.ajax({
			type : "POST",
			url : url,
			success : successCallback
		});
	},
	get : function(url, successCallback) {
		$.ajax({
			type : "GET",
			url : url,
			success : successCallback
		});
	}
}
