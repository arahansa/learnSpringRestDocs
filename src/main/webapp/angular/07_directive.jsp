<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html ng-app="testApp">
<head>
<title> Happy WOrld </title>
</head>
<body>
<h1>디렉티브를 알아보자. </h1>
<my-view></my-view>

<script src="/plugins/jquery/dist/jquery.js"></script>
<script src="/plugins/angular/angular.min.js"></script>
<script>
var app = angular.module('testApp', []);
app.directive('myView', function(){
	return {
		restrict: 'E',
		template : '<span>안녕하세요</span>'
	};
});
</script>
</body>
</html>