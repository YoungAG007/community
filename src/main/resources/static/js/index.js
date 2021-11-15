/*绑定单击事件，调用publish函数*/
$(function(){
	$("#publishBtn").click(publish);
});
function publish() {
	$("#publishModal").modal("hide");

	// 获取标题和内容
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	// 发送异步请求（POST）
	$.post(
		/*url*/
		CONTEXT_PATH+"/discuss/add",
		/*data*/
		{"title":title,"content":content},
		/*成功后的回调函数*/
		function (data) {
			data = $.parseJSON(data);
			// 在提示框显示返回消息
			$("#hintBody").text(data.msg);
			// 显示提示框
			$("#hintBody").modal("show");
			// 2s后，自动隐藏提示框
			setTimeout(function(){
				$("#hintBody").modal("hide");
				// 如果添加成功，刷新页面显示新帖子
				if (data.code == 0) {
					window.location.reload();
				}
			}, 2000);
		}
	);

	$("#hintModal").modal("show");
	setTimeout(function(){
		$("#hintModal").modal("hide");
	}, 2000);
}