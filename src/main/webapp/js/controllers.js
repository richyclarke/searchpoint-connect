/**
 * INSPINIA - Responsive Admin Theme
 *
 */

/**
 * MainCtrl - controller
 */
function MainCtrl(user, auth, jwtHelper) {

	this.userName = 'Example user';
	this.helloText = 'Welcome in SeedProject';
	this.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';

	var self = this;
	
	var token;
	var username;
	var roles;

	function handleRequest(res) {
		if (res) {
			if (res.headers()) {
				if (res.headers()["authorization"]) {
					token = res.headers()["authorization"].split(" ")[1];
					self.message = token;
				}
				if (token) {
					var tokenPayload = jwtHelper.decodeToken(token);
					roles = tokenPayload["roles"];
					username = tokenPayload["sub"];
					console.log(username + ' has these roles : ' + roles);
				}
			}
		}
	}

	self.login = function() {
		user.login(self.username, self.password).then(handleRequest, handleRequest)
	}
	self.register = function() {
		user.register(self.username, self.password).then(handleRequest, handleRequest)
	}
	self.getQuote = function() {
		user.getQuote().then(handleRequest, handleRequest)
	}
	self.logout = function() {
		auth.logout && auth.logout()
	}
	self.isAuthed = function() {
		return auth.isAuthed ? auth.isAuthed() : false
	}

};

function authInterceptor(API, auth) {
	var token;
	return {
		// automatically attach Authorization header
		request : function(config) {
			//console.log('Adding Token : ' + token);
			config.headers['Authorization'] = 'Bearer ' + token;
			return config;
		},

		// If a token was sent back, save it
		response : function(res) {
			if (res) {
				if (res.headers()["authorization"]) {
					token = res.headers()["authorization"].split(" ")[1];
					//console.log('Saving Token : ' + token);
					
				}
			}
			return res;
		},
	}
}

function authService($window) {
	var self = this;

	// Add JWT methods here
}

function userService($http, API, auth, jwtHelper) {
	var self = this;
	self.getQuote = function() {
		return $http.get(API + '/users/1')
	}

	self.register = function(username, password) {
		return $http({
			method : "POST",
			url : API + '/users/register',
			data : $.param({
				'username' : username,
				'password' : password
			}),
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			}
		})
	}

	self.login = function(username, password) {
		return $http({
			method : "POST",
			url : API + '/users/login',
			data : $.param({
				'username' : username,
				'password' : password
			}),
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			}
		})
	};
}

angular.module('inspinia').factory('authInterceptor', authInterceptor).service('user', userService).service('auth', authService).constant('API', 'http://localhost:8080/searchpoint-connect/api').config(function($httpProvider) {
	$httpProvider.interceptors.push('authInterceptor');
}).controller('MainCtrl', MainCtrl)
