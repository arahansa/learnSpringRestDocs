<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html ng-app="testApp">
<head>
<title> Happy World </title>
<style>
	div.reply { margin-bottom: 10px; }
</style>
</head>

<body ng-controller="replyController" ng-init="init(1)">
<h3>현재 댓글 {{ replies.length }} </h3>

<h1>헬로 앵귤러</h1>
<h4>댓글 입력</h4>
	닉넴 : <input type="text" ng-model="reply.author"> ,  
	내용 : <input type="text" ng-model="reply.comment"> 
	<button ng-click="submit( reply )">전송!</button> 
<hr>

<div id="replyContent">
	<my-reply ng-repeat="reply in replies|orderBy:'-id'"></my-reply>
</div>

<script src="/plugins/jquery/dist/jquery.js"></script>
<script src="/plugins/angular/angular.min.js"></script>
<script>
var app = angular.module('testApp', []);
var tempContent="";

app.controller('replyController', function($scope, $http){
	$scope.replies=[];
	$scope.submit = submit;
	$scope.del = del;
	$scope.init = init;
	
	function init(pagenumber){
		var req = {method: "GET", url:"/comment"};
		$http(req)
		.success(function(data) { $scope.replies=data; })
		.error(function(){console.log("에러");});
	}
	
	function submit( reply ){
		var req = { method : "POST", url : "/comment", data: reply};
		$http(req)
		.success(function(data) {
			$scope.replies.push(data);
			reply.author=""; reply.comment="";
		}).error(function(){console.log("에러");});
	}
	
	function del( reply ){
		var req = { method : "DELETE", url : "/comment/"+reply.id };
		$http(req)
		.success(function(data) {
			var index = $scope.replies.indexOf(reply);
			$scope.replies.splice(index, 1); 
		})
		.error(function(){console.log("에러");});
	}
});


app.directive('myReply', function($compile, $http){
	return {
		restrict : 'E',
		replace: true,
		template: 
			'<div class="reply" data-id="{{reply.replyId}}" >' +
			'<span class="author"> {{reply.author}} : </span> '+
			'<span class="comment"> {{reply.comment}} </span>'+
			'<button ng-click="modifyForm( $event )">수정</button>'+  
			'<button ng-click="del( reply )">삭제</button>',
		link : function ( scope ){
			scope.modifyForm = function modifyForm(){
				makeOriginalView($("div.reply[status=modifying]"), tempContent);
				var replyDiv = $(event.currentTarget).closest("div.reply");
				$(replyDiv).attr("status", "modifying");
				tempContent = $(event.currentTarget).prev().text();
				$(replyDiv).find("button").remove();
				$(replyDiv).find("span.author").next().replaceWith(
						$compile(
						'<textarea id="updatedComment">'+tempContent +'</textarea>'+
						'<button ng-click="update( reply, $event )">수정!</button>'+
						'<button ng-click="cancelUpdate( reply , $event )">취소</button>')(scope));
			};
			
			scope.update= function(reply, event){
				reply.comment = $("#updatedComment").val();
				console.log("댓글?", reply);
				var req = {method:"PUT", url:"/comment/"+reply.id, data:reply};
				$http(req)
				.success(function(data) {
					var replyDiv = $(event.currentTarget).closest("div.reply");
					makeOriginalView( replyDiv , reply.comment );
				})
				.error(function(){console.log("에러");});
			}
			scope.cancelUpdate = function( reply , event ){
				var replyDiv = $(event.currentTarget).closest("div.reply");
				makeOriginalView( replyDiv , tempContent );
			}
			
			function makeOriginalView( replyDiv , comment){
				$(replyDiv).removeAttr("status");
				$(replyDiv).find("button").remove();
				$(replyDiv).find("span.author").next().replaceWith(
					$compile(	
					'<span class="comment">'+comment+'</span>' +
					'<button ng-click="modifyForm( $event )">수정</button>'+  
					'<button ng-click="del( reply )">삭제</button>')(scope) 
				);
			}
		}	
	}
});





</script>
</body>
</html>