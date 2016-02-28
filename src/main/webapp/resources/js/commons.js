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
				$gridTable 		: 	$( '.table', $app ),
				$pagination		: 	$( '.pagination', $app ),
				$modal			:	$( '.modal', $app ),	
				$dataForm		:	$( '#dataForm', $app ),
				$addBtn		:   $( '#add', $app ),
				$updateBtn		:	$( '#update' , $app ),
				$deleteBtn		:	$( '#delete' , $app ),
				$PopUpAddBtn	: 	$('#popUpAddPage', $app ),
				queryURL 		: 	null,
				gridFields		: 	null,
				pageSize		: 	10
			}

		var newSettings = $.extend( defaultSettings, settings );

		if ( !check( newSettings ) )
			return;

		console.log( 'grid initial success' );

		initGridData( newSettings );
		bindDataForm( newSettings );
	});

	function check(settings) {
		if (settings.$gridTable.length == 0 ) {
			alert('尚未指定 $gridTable');
			return false;
		}
		if (settings.$pagination.length == 0 ) {
			alert('尚未指定 $pagination');
			return false;
		}
		if (settings.$modal.length == 0 ) {
			alert('尚未指定 $modal');
			return false;
		}
		if (settings.$dataForm.length == 0 ) {
			alert('尚未指定 $dataForm');
			return false;
		}
		if (settings.$addBtn.length == 0 ) {
        	alert('尚未指定 $addBtn');
        	return false;
        }
		if (settings.$updateBtn.length == 0 ) {
        	alert('尚未指定 $updateBtn');
        	return false;
        }
		if (settings.$deleteBtn.length == 0 ) {
        	alert('尚未指定 $deleteBtn');
        	return false;
        }
		if (settings.$PopUpAddBtn.length == 0 ) {
			alert('尚未指定 $PopUpAddBtn');
			return false;
		}

		if (settings.queryURL == null || $.trim(settings.queryURL).length == 0) {
			alert('尚未指定  queryURL');
			return false;
		}
		
		if ( settings.gridFields == null || settings.gridFields.size ==0 ) {
			alert('尚未指定  gridFields');
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
			var table="";
			var pageData = data.content;
			
			$( pageData ).each(function( index ) {
				table +="<tr class='gridRow'>";
				table += "<td>"+ index + "</td>";
				
				//forEach field
				settings.gridFields.forEach( function( entry ) {
				    table += "<td data-field='" + entry + "'>" + pageData[ index ] [entry ] + "</td>"
				});
				
				//TODO: #addPageModal 要從設定讀
				table += '<td>' +
							'<button type="button" class="btn btn-info" data-toggle="modal" data-target="#addPageModal" data-type="edit">'+
							'<span class="glyphicon glyphicon-edit"></span> Edit' +
							'</button> '+
							'<button type="button" class="btn btn-danger" data-toggle="modal" data-target="#addPageModal" data-type="delete">'+
							'<span class="glyphicon glyphicon-remove"></span> Delete' +
							'</button>'+
						 '</td>';
				table += "</tr>";
				
				
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
	
	function bindDataForm ( settings ) {
		
		settings.$modal.on('show.bs.modal', function (event) {
		  
		  cleanFormData( settings.$dataForm );//清除modal dataForm欄位資料
          var button = $( event.relatedTarget ); // Button that triggered the modal
          var type = button.data('type') // Extract info from data-* attributes

          var modal = $( this );
          console.log(button);
          console.log(type);
          
          if ( 'add' == type ) {
        	  modal.find( '.modal-title' ).text( '新增使用者' );
        	  settings.$addBtn.show();
        	  settings.$updateBtn.hide();
        	  settings.$deleteBtn.hide();
        	  
          } else if( 'edit' == type ) {
        	  modal.find( '.modal-title' ).text( '編輯使用者' );
        	  settings.$addBtn.hide();
        	  settings.$updateBtn.show();
        	  settings.$deleteBtn.hide();
        	  
        	  var $fields = button.parent().siblings('').filter("td[data-field]");
        	  settings.$updateBtn.data( '$fields', $fields ); //store grid ori fields data to button
        	  
        	  //each field mapping to dataForn's field
        	  $fields.each( function() {
        		  var $field = $( this );
        		  $( '[name="' + $field.data( 'field' ) +'"]', settings.$dataForm ).val( $field.text() );
        		  
        	  });
          } else if( 'delete'== type ) {
        	  modal.find( '.modal-title' ).text( '刪除使用者' );
        	  settings.$addBtn.hide();
        	  settings.$updateBtn.hide();
        	  settings.$deleteBtn.show();
        	  /TODO: modal內容為再度確認即可

          }
        });


		/**
		 * 新增
		 */
        settings.$addBtn.click(function(){
            //傳送新增資料到後端
        	var jsonData = settings.$dataForm.serializeObject()

        	_ajax.postJsonData( settings.queryURL, jsonData , function( data ){
        		//TODO: 如果新增失敗,顯示錯誤並中斷流程
        		if ( data.err ) {
        			alert( data.err.msg );
        			return;
        		}
        		
        		//清除新增資料
        		cleanFormData( settings.$dataForm );
        		
        		//重新查詢,回到第一頁
        		initGridData( settings );
        		
        	});
        });
        
        /**
		 * 更新
		 */
        settings.$updateBtn.click(function(){
        	 $fields = settings.$updateBtn.data( '$fields');
        	 console.log
        	//TODO:更新至後端
        	
        	//TODO:更新至該筆grid
        });
        
        
        
        function cleanFormData( $form ) {
        	//TODO: 其他html element
        	$( ':input', settings.$dataForm ).val( '' );
        }
	}

};

var _ajax = {
	post : function( url, successCallback ) {
		console.log( 'post:' );
		$.ajax({
			type : "POST",
			url : url,
			success : successCallback
		});
	},
	postJsonData : function( url, jsonData, successCallback ) {
		console.log( 'postJsonData:' );
		console.log( jsonData );
		$.ajax({
			 type: "POST",
		      contentType: "application/json",
		      url: url,
		      data:  JSON.stringify( jsonData ),
		      dataType: "json",
		      success : successCallback
		});
		
		
	},
	get : function(url, successCallback) {
		console.log( 'get:' );
		$.ajax({
			type : "GET",
			url : url,
			success : successCallback
		});
	}
}