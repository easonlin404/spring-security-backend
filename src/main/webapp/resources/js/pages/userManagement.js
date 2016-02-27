$( function() {
	$('#app').grid({
		queryURL:'rest/user',
		gridFields:[ 'userName', 'password', 'enabled' ]
	});
});