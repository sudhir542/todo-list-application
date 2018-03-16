var app = angular.module('ToDoListApp', ['ngRoute','ngResource','ui.bootstrap', 'ui.bootstrap.datetimepicker']);

		app.config(function($routeProvider){
				$routeProvider
				.when('/',
				{
					controller: 'ListTasksController',
					templateUrl: '/views/tasks.html'
				})
				.when('/addTask',
				{
					controller: 'AddTasksController',
					templateUrl: '/views/addTasks.html'
				})
				.when('/editTask:id',
				{
					controller: 'EditTasksController',
					templateUrl: '/views/editTasks.html'
				})
				.when('/completedTasks',
				{
					controller: 'ListTasksController',
					templateUrl: '/views/completedTasks.html'
				})
				.when('/users',
				{
					controller: 'ListUsersController',
					templateUrl: '/views/users.html'
				})
				.when('/addUser',
				{
					controller: 'AddUsersController',
					templateUrl: '/views/addUsers.html'
				})
				.when('/editUser:id',
				{
					controller: 'EditUsersController',
					templateUrl: '/views/editUsers.html'
				})
				.otherwise({
					redirectTo: '/#'
				})

		});
		