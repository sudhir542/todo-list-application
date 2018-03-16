app.controller('AddUsersController', function($scope , $location, DataUsersFactory, AddUsersFactory, $route ){
	this.addUser = function(addNewUserCtrl){
		AddUsersFactory.save(addNewUserCtrl);
		DataUserFactory.addUser(addNewUserCtrl);
		
		window.setTimeout(function() {
			$location.path('/');
        }, 10); 
		
	};
});

app.controller('EditUsersController', function($scope, $location, ListUsersByIdFactory, EditUsersFactory, $route, $routeParams ){
			//Retrieve selected task
	var selectedUser = ListUsersByIdFactory.query({id:$routeParams.id});
	selectedUser.$promise.then(function(result){
			//Populate scope variables
		$scope.editUserCtrl = $scope.editUserCtrl || {};
		$scope.editUserCtrl.username = result.username;
		$scope.editUserCtrl.isdone = result.isdone;
	});
	
	this.editUser = function(editUserCtrl){
		EditUsersFactory.update({id:$routeParams.id},editUserCtrl);
		
		window.setTimeout(function() {
			$location.path('/');
        }, 10); 
		
	};
});
	
app.controller('ListUsersController', function($scope, $location,  DataUsersFactory, ListUsersFactory, $route){
	var user = ListUsersFactory.query();
	user.$promise.then(function (result) {
		DataUsersFactory.init(result);
	    $scope.items = DataUsersFactory.getUsers().getUserName();
	});
});

app.controller('UsersListController', function($scope, ListUsersFactory) {
	$scope.items = ListUsersFactory.query();
});

app.controller('UsersActionsController', function($scope, $location, ListUsersByIdFactory, EditUsersFactory, DeleteUsersFactory) {
    
	$scope.hoverIn = function(){
        this.Icon = true;
    };

    $scope.hoverOut = function(){
        this.Icon = false;
    };
    
    $scope.checkUser = function(userid) {
    	var selectedUser = ListUsersByIdFactory.query({id:userid});
    	selectedUser.$promise.then(function(result){
    			//Checking task makes isdone = "true"
    		result.isdone = "true";
    		EditUsersFactory.update({id:userid},result);
    		
    		window.setTimeout(function() {
    			location.reload(true);
            }, 10);
    		
    	});
     };
     
    // <<-- EditUser has its separate controller: EditUsersController.
     
    $scope.deleteUser = function(userid) {
        DeleteUsersFactory.delete_user({id:userid},null);
        
		window.setTimeout(function() {
			location.reload(true);
        }, 10);
		
     };
});
    