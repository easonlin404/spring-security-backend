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
				$modal			:	$( '#addPageModal', $app ),	
				$delModal		:	$( '#delPageModal', $app ),	
				$dataForm		:	$( '#dataForm', $app ),
				$addBtn		:   $( '#add', $app ),
				$updateBtn		:	$( '#update' , $app ),
				$deleteBtn		:	$( '#delete' , $app ),
				$PopUpAddBtn	: 	$('#popUpAddPage', $app ),
				$searchBarForm	:	$( '#searchBar' ,$app ),
				queryURL 		: 	null,
				gridFields		: 	null,
				gridKeys		:	null,
				pageSize		: 	10
			}

		var newSettings = $.extend( defaultSettings, settings );

		if ( !check( newSettings ) )
			return;

		console.log( 'grid initial success' );

		initGridData( newSettings );
		bindDataForm( newSettings );
		initSearchBarForm( newSettings );
		configGlobalAjaxError();
		
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
		if (settings.$delModal.length == 0 ) {
			alert('尚未指定 $delModal');
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
		
		if (settings.$searchBarForm.length == 0 ) {
			alert('尚未指定 $searchBarForm');
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
		if ( settings.gridKeys == null || settings.gridKeys.size ==0 ) {
			alert('尚未指定  gridKeys');
			return false;
		}
		if (settings.pageSize == null) {
			alert('尚未指定  pageSize');
			return false;
		}

		return true;
	}

	function initGridData( settings, page, url ) {
		if ( page == undefined )
			page =1;
		
		if (url == undefined )
			url = settings.queryURL;
		
		_ajax.get( url + "/"+ page +"/?size=" + settings.pageSize, function( data ) {
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
				//TODO: #delPageModal 要從設定讀
				table += '<td>' +
							'<button type="button" class="btn btn-info" data-toggle="modal" data-target="#addPageModal" data-type="edit">'+
							'<span class="glyphicon glyphicon-edit"></span> Edit' +
							'</button> '+
							'<button type="button" class="btn btn-danger" data-toggle="modal" data-target="#delPageModal" data-type="delete">'+
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
		/**
		 * 處理新增、編輯popup按鈕
		 */
		settings.$modal.on('show.bs.modal', function (event) {
		  
		  cleanFormData( settings.$dataForm );//清除modal dataForm欄位資料
          var button = $( event.relatedTarget ); // Button that triggered the modal
          var type = button.data('type') // Extract info from data-* attributes

          var modal = $( this );
          
          if ( 'add' == type ) {
        	  modal.find( '.modal-title' ).text( '新增使用者' );
        	  settings.$addBtn.show();
        	  settings.$updateBtn.hide();
        	  
          } else if( 'edit' == type ) {
        	  modal.find( '.modal-title' ).text( '編輯使用者' );
        	  settings.$addBtn.hide();
        	  settings.$updateBtn.show();
        	  
        	  var $fields = button.parent().siblings('').filter("td[data-field]");
        	  settings.$updateBtn.data( '$oriGridRowData', $fields ); //store grid ori fields data to button
        	  
        	  //each field mapping to dataForn's field
        	  $fields.each( function() {
        		  var $field = $( this );
        		  $( '[name="' + $field.data( 'field' ) +'"]', settings.$dataForm ).val( $field.text() );
        		  
        	  });
          }
        });
		
		settings.$delModal.on('show.bs.modal', function (event) {
			console.log('popup del page');
			var button = $( event.relatedTarget ); // Button that triggered the modal
			var $fields = button.parent().siblings('').filter("td[data-field]");
       	  	settings.$deleteBtn.data( '$oriGridRowData', $fields ); //store grid ori fields data to button
			console.log($fields);
		});


		/**
		 * 新增
		 */
        settings.$addBtn.click(function(){
            //傳送新增資料到後端
        	var jsonData = settings.$dataForm.serializeObject();

        	_ajax.postJsonData( settings.queryURL, jsonData , function( data ){
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
        	var $oriGridRowData =  $( this ).data( '$oriGridRowData');
        	 console.log($oriGridRowData);
        	 var query="";
        	 var keyEqual = true;
        	 
        	 settings.gridKeys.forEach(function( entry ) {
        		 var text=$oriGridRowData.filter( 'td[data-field="'+entry+'"]' ).text();
        		 
        		 var dataFromKey= settings.$dataForm.find('input[name="'+entry +'"]').val();
        		 console.log(dataFromKey);
        		 
        		 if ( text!=  dataFromKey) 
        			 keyEqual = false;
        		 
        		 query+="/"+text;
            	 console.log(query);
        	 });
        	 
        	 //如果key不相等，終止
        	 if ( !keyEqual ){
        		 showMsg('key值不能修改');
        		 return;
        	 }
        	
        	 //gridKeys不能更新，因為是key
        	 //傳送新增資料到後端
         	var jsonData = settings.$dataForm.serializeObject();
         	
         	_ajax.putJsonData( settings.queryURL + query, jsonData , function( data ){
         		//清除新增資料
         		cleanFormData( settings.$dataForm );
         		
         		//重新查詢,回到第一頁
         		initGridData( settings );
         		
         	});
        });
        
        /**
         * 刪除，點選確定刪除
         */
        settings.$deleteBtn.click(function(){
        	var $oriGridRowData = $( this ).data( '$oriGridRowData' );
        	var query="";
        	
        	settings.gridKeys.forEach(function( entry ) {
        		 var text = $oriGridRowData.filter( 'td[data-field="'+entry+'"]' ).text();
        		 query +="/"+ text;
        	 });
        	 
        	
        	_ajax.del( settings.queryURL + query, function( data ){
         		//清除新增資料
         		cleanFormData( settings.$dataForm );
         		
         		//重新查詢,回到第一頁
         		initGridData( settings );
         		
         	});
        });
        
	}
	
	/**
	 * 初始化搜尋Bar
	 */
	function initSearchBarForm( settings ) {
		settings.$searchBarForm.submit( function( e ){
			 e.preventDefault();
			 
			 serarchBarClick( settings );
		});
		
	}
	
	/**
	 * 按下搜尋Bar
	 */
	function serarchBarClick( settings ) {
		 var  searchText = $( 'input', settings.$searchBarForm ).val();
		 
		 if (searchText =='' ||  searchText.trim() == '') {
			 //清除Search Bar
			 cleanFormData( settings.$searchBarForm );
			 //重新查詢
			 initGridData( settings , 1 ); 
			 return;
		 }
			 
		 var page = 1;
		 var url  = settings.queryURL+"/like/" + searchText;
		 //依照搜尋文字做查詢
		 initGridData( settings, page,  url ); 
	}
	
	function cleanFormData( $form ) {
		//TODO: 其他html element
		$( ':input', $form ).val( '' );
	}
      
	  /**
	   * 顯示訊息於alert視窗
	   */
	function showMsg( msg ) {
	  	$( '.alert' ).find( '.msg' ).text( msg );
		$( '.alert' ).show();
	
		setTimeout(function(){
			$( '.alert' ).hide();
		},3*1000);
    }
      
	function configGlobalAjaxError(){
    	$(document).ajaxError(function (event, jqxhr, settings) {
			var msg = "系統發生錯誤,請洽管理人員";
			//CONFLICT
			if( jqxhr.status == 409 ) //新增有問題
				msg = '使用者已存在';
			else if( jqxhr.status == 404 ) //更新有問題
				msg = '更新錯誤';
			
			showMsg( msg );
		})
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
	putJsonData : function( url, jsonData, successCallback ) {
		console.log( 'putJsonData:' );
		console.log( jsonData );
		$.ajax({
			 type: "PUT",
		      contentType: "application/json",
		      url: url,
		      data:  JSON.stringify( jsonData ),
		      dataType: "json",
		      success : successCallback
		});
	},
	del	:function( url,successCallback ) {
		console.log( 'del:' );
		$.ajax({
			 type: "DELETE",
		      contentType: "application/json",
		      url: url,
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