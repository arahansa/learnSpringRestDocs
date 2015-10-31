<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>

<html ng-app="testApp">
<head>
<title> Happy WOrld </title>
</head>
<body ng-controller="testController">


<input type="text" ng-model="testMsg" />

<br>

메시지 : <span> {{ testMsg }} </span> 

<br><br>
<script src="/plugins/jquery/dist/jquery.js"></script>
<script src="/plugins/angular/angular.min.js"></script>

<script>
var app = angular.module('testApp', []);
app.controller('testController', function( $scope ){
	$scope.testMsg="hi";
});

</script>
</body>
</html>