/**
 * 
 */

var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope) {
    $scope.firstName= "John???";
    $scope.lastName= "Doe";
    $scope.personals = [ {name: "Kasia", surname: "Prop"}, {name: "Zosia", surname: "Pop"}];
});
 
/*
 * Directive example
 */
app.directive('w3Test', function() {
	return {
		restrict: "EACM",
		template: "jakies gowno"
	};
});

app.directive('w3TestHu', function() {
	return {
		restrict: "EACM",
		template: "<h1>lolipoli</h1>"
	};
});

var moduleApp = angular.module('myApp');
moduleApp.directive('test', function() {
	return {
		template: "test"
	};
});

