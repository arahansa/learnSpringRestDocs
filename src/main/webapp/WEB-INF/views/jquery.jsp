<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title> Happy World </title>
<style>
 div.reply{ margin-bottom: 10px;}
</style>
</head>
<body>
<h1>jQuery</h1>
닉넴 : <input type="text" name="author" id="author"> ,  
내용 : <input type="text" name="comment" id="comment"> 
<button onclick="submit()">전송!</button>
<hr>
<h4>댓글 내용</h4>
<div id="replyContent">
	<div class="reply"> 
		<span class="author">글쓴이 :</span>
		<span class="comment">하하하</span>
		<button>수정</button>
		<button>삭제</button>
	</div>
</div>
<script src="/plugins/jquery/dist/jquery.js"></script>
<script>
$(document).ready(function(){
	// 목록 불러오기
	$.get( "/comment" , function( data ) {
        $(data).each( function(k,v){
        	addNewReply(v);
        });
    });	
});

// 일시적으로 넣은 댓글 변수
var tempContent;

// 새로운 댓글 넣기
function addNewReply(v){
	$("#replyContent").prepend(
		'<div class="reply" data-id='+v.id+'>' +
			'<span class="author">'+v.author+' : </span>'+
			'<span class="comment">'+v.comment+'</span>'+
			'<button onclick="modifyForm(this)">수정</button>'+
			'<button onclick="del(this)">삭제</button>'+
		'</div>'
	);
}


// 댓글쓰기  Create
function submit(){
	var reply = 
	{ author : $("#author").val(), comment : $("#comment").val() };
	
	$.ajax({
		type : 'POST',
		url : "/comment", 
		contentType: 'application/json',
        data : JSON.stringify( reply ),
        dataType: 'json',
        success : function(result) {
            addNewReply(result);
        }// 에러처리도 해주세요.
	});
}
// 수정 modifyForm
function modifyForm(obj){
	makeCommentView($("div.reply[status=modifying]"), tempContent);
	$(obj).closest("div.reply").attr("status", "modifying");
	tempContent = $(obj).prev().text();
	$(obj).prev().replaceWith(
		'<textarea id="updatedComment">'+tempContent +'</textarea>'+
        '<button onclick="update(this)">수정!</button>'+
        '<button onclick="cancelUpdate(this)">취소</button>'	
	);
	$(obj).next().remove();
    $(obj).remove();
}
// 수정 update
function update(obj){
	var replyDiv = $(obj).closest("div.reply");
    var replyId = $(replyDiv).attr("data-id");
    var reply = { comment : $("#updatedComment").val() };
    
    $.ajax({
		type : 'PUT',
		url : "/comment/"+replyId, 
		contentType: 'application/json',
        data : JSON.stringify( reply ),
        dataType: 'json',
        success : function(result) {
            // 원래의 양식 되돌릴 부분이죠
            // 뷰를 만지는 기능을 함수로빼겠습니다.
            makeCommentView(replyDiv, result.comment);
        }// 에러처리도 해주세요.
	});
}

// 원래의 뷰로 되돌리는 함수...
function makeCommentView(replyDiv, comment){
	$(replyDiv).removeAttr("status");
	$(replyDiv).find("button").remove();
	$("#updatedComment").replaceWith(
		' <span class="comment">'+comment +'</span>' +
		' <button onclick="modifyForm(this)">수정</button> '+
	    ' <button onclick="del(this)">삭제</button>'  
	);
}

// Cancel Update
function cancelUpdate(obj){
	var replyDiv = $(obj).closest("div.reply");
    makeCommentView(replyDiv, tempContent);
}

// 삭제 Delete
function del(obj){
	var replyId = $(obj).parent().attr("data-id");
	$.ajax({
		type : 'DELETE',
		url : "/comment/"+replyId, 
		success : function(result) {
            console.log("삭제 성공");
            $(obj).closest("div.reply").remove();
        }// 에러처리도 해주세요.
	});	
}


</script>

</body>
</html>