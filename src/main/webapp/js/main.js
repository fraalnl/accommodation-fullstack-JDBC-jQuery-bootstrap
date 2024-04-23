var rootURL = "http://localhost:8081/Accommodation/rest/rooms";
var currentRoom;

var findAll = function(){
	console.log('findAll');
	$.ajax({
		type: 'GET',
		url: rootURL,
		dataType: 'json',
		success: renderTable
	});
};

var renderTable = function(roomList){
	$('#roomTableBody').empty();
	
	$.each(roomList, function(index, room){

		var tr = $('<tr>');	
		
		var td1 = $('<td>');
		td1.text(room.name);
		
		var td2 = $('<td>');
		td2.text(room.address);
		
		var td3 = $('<td>');
		td3.text(room.distance);
		
		var td4 = $('<td>');
		td4.text(room.rent);
		
		var td5 = $('<td>');
		var btn = $('<button>')
		btn.attr('type', 'button');
// btn.attr('id', 'moreBtn'); // Since there's only one button here, id unnecessary for selector
//the only opportunity to get room.id, so store it in button, for button can listen to events, while a regular <td> can't
		btn.attr('data-id', room.id); 
		btn.attr('class', 'btn btn-primary'); // .btn .btn-primary perhpas for CSS
		btn.attr('data-toggle', 'modal');
		btn.attr('data-target', '#roomDetailsModal');
		btn.text('More'); 
		td5.append(btn);
		
		tr.append(td1,td2, td3, td4, td5);
		
		$('#roomTableBody').append(tr);
	});
};

var findById = function(id) {
	console.log('findById: ' + id);
	$.ajax({
		type: 'GET',
		url: rootURL + '/' + id,
		dataType: 'json',
		success: function(room) {
			currentRoom = room;
			renderMore(currentRoom);
		}
	});	
};

function renderMore(room) {
	$('#roomId').val(room.id);
	$('#nameInput').val(room.name);
	$('#emailInput').val(room.email);
	$('#phoneInput').val(room.phone);
	$('#addressInput').val(room.address);
	$('#eircodeInput').val(room.eircode);	
	$('#distanceInput').val(room.distance);
	$('#typeInput').val(room.roomType);
	$('#durationInput').val(room.durationStay);
	$('#rentInput').val(room.rent);
	$('#billsInput').val(room.bills);
	$('#genderInput').val(room.genderPreference);
	$('#addMsgInput').val(room.addMessage);
}

var search = function(minRent, maxRent, minDistance, maxDistance) {
	console.log('search: ' + minRent + ', ' + maxRent + ', ' + minDistance + ', ' + maxDistance)
	$.ajax({
		type: 'GET',
		url: rootURL + '/search',
		dataType: 'json',
/*The data property sends the search criteria to the server. It creates an object that acts like a key-value store.
The keys are used by the server-side code to identify the search parameters. 
The values are the actual values passed to the search function.*/
		data: { 
			'minRent': minRent,
			'maxRent': maxRent,
			'minDistance': minDistance,
			'maxDistance': maxDistance,
		},
		success: function(response){
			if(response.length === 0) {
				alert('No rooms found matching your search criteria.');
			} else {
				renderTable(response);
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert('search failed: ' + textStatus);
		}
	});
};

var addRoom = function(){
	console.log('addRoom');
	$.ajax({
		type: 'POST',
		contentType: 'application/json',
		url: rootURL,
		dataType: 'json',
		data: modalToJSON(), //user's input
		success: function(response, textStatus, jqXHR){ // response is the added room object
			alert('Room created successfully');
			$('#deleteRoomBtn').show();
// newly added room with id undefined, so update it
			$('#roomId').val(response.id);
			findAll();
		},
		error: function(jqXHR, textStatus, errorThrown){
			console.log(jqXHR);
  			console.log(textStatus);
  			console.log(errorThrown);
			alert('addRoom error: ' + textStatus);
		}
	});
};

var modalToJSON = function(){ 
	var roomId = $('#roomId').val();
	console.log("roomId: " + roomId);
	return JSON.stringify({
		'id': roomId == ''? null : roomId, //Add or update. When add, you don't provide id, so null
		'name': $('#nameInput').val(),
		'email': $('#emailInput').val(),
		'phone': $('#phoneInput').val(),
		'address': $('#addressInput').val(),
		'eircode': $('#eircodeInput').val(),
		'distance': $('#distanceInput').val(),
		'roomType': $('#typeInput').val(),
		'durationStay': $('#durationInput').val(),
		'rent': $('#rentInput').val(),
		'bills': $('#billsInput').val(),
		'genderPreference': $('#genderInput').val(),
		'addMessage': $('#addMsgInput').val()
	});
};

var updateRoom = function(){	
	console.log('updateRoom');
	$.ajax({
		type: 'PUT',
		contentType: 'application/json',
		url: rootURL + '/' + $('#roomId').val(),
		dataType: 'json',
		data: modalToJSON(),
		success: function(data, textStatus, jqXHR) {
			alert('Room updated successfully');
			findAll();
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert('updateRoom error: ' + textStatus);
		}
	});
};

var deleteRoom = function() {
	console.log('deleteRoom');
	$.ajax({
		type: 'DELETE',
		url: rootURL + '/' + $('#roomId').val(),
		success: function(data, textStatus, jqXHR) {
			alert('room deleted successfully');
			//renderMore({}); // unnecessary because the modal will be hidden
			findAll();
		},
		error: function (jqXHR, textStatus, errorThrown) {
			alert('deleteRoom error');
		}
	});
};

$(document).ready(function(){ 
	
	findAll();

// '#roomTableBody button' didn't work at first because I built the element var btn = $('<btn>'). JQuery automatically corrects it
	$(document).on('click', '#roomTableBody button', function(){ 	
		var roomId = $(this).data('id'); //button's data-id, which is room.id
		console.log(roomId);
		$('#roomDetailsModal').modal('show');
		$('#imgBtn').show();
		findById(roomId);
	});
	
	$(document).on('click', '#imgBtn', function(){ 	
// manually control whehter a modal/button shows or hides, so they don't block each other
		$('#roomDetailsModal').modal('hide');
		$('#roomImageModal').modal('show');
		$('#image').attr('src', 'images/' + currentRoom.image);	// wonderful currentRoom saves an ajax request	
	});

	$('#searchRoomBtn').click(function(){
		search($('#minRent').val(), $('#maxRent').val(), $('#minDistance').val(), $('#maxDistance').val());
		return false;
	});
	
	$('#addRoomBtn').click(function(){
		$('#roomDetailsModal').modal('show');
		$('#imgBtn').hide();
		$('#deleteRoomBtn').hide();
// To clear a form, pass {}. Don't assign currentRoom={}, because a non-empty object is useful
		renderMore({}); 
		return false;
	});
	
	$(document).on('click', '#saveRoomBtn', function(){
		console.log('save button clicked!')
		if($('#roomId').val() == '') { // user can't input id; id is only returned from server
			addRoom();
		}
		else
			updateRoom(); 
		$('#roomDetailsModal').modal('hide');
		return false;
	});
	
	$(document).on('click', '#deleteRoomBtn', function(){
		deleteRoom();
		// Once done in the modal, close the modal. These two lines can be put in either order
		$('#roomDetailsModal').modal('hide'); 		
		return false;
	});
});