app.factory('AddUsersFactory', function ($resource, $location) {
    return $resource(($location.protocol() + "://" + $location.host() + ":" + $location.port()) + '/users', {}, {
    	save: { method: 'POST' }
    });
});

app.factory('EditUserFactory', function ($resource, $location) {
    return $resource(($location.protocol() + "://" + $location.host() + ":" + $location.port()) + '/users/:id',{}, {
    	update: { method: 'PUT' }
    });
});

app.factory('DeleteUserFactory', function ($resource, $location) {
    return $resource(($location.protocol() + "://" + $location.host() + ":" + $location.port()) + '/users/:id',{}, {
    	delete_task: { method: 'DELETE' }
    });
});

app.factory('ListUsersFactory', function ($resource, $location) {
    return $resource(($location.protocol() + "://" + $location.host() + ":" + $location.port()) + '/users', {}, {
    	query: { method: 'GET', isArray: true }
    })
});

app.factory('ListUsersByIdFactory', function ($resource, $location) {
    return $resource(($location.protocol() + "://" + $location.host() + ":" + $location.port()) + '/users/:id',{}, {
    	query: { method: 'GET' }
    });
});

/*app.factory('TasksPrioritiesListFactory', function ($resource, $location) {
    return $resource(($location.protocol() + "://" + $location.host() + ":" + $location.port()) + '/populateTasksPriorities', {}, {
    	query: { method: 'GET', isArray: true }
    })
});
*/

app.factory('DataUsersFactory', function(){
	  var usersList = [];

	  var init = function(usersList){
		  UsersList = angular.copy(usersList);
	  };
	  
	  var addUser = function(newuser) {
		  UsersList.push(newuser);
	  };

	  var getUsers = function(){
	      return UsersList;
	  };

	  return {
		  init : init,
		  addUser: addUser,
		  getUsers: getUsers
	  };
  });