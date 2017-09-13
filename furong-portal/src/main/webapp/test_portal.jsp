<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<script type="text/javascript" src="js/jquery-1.6.4.js"></script>
<script type="text/javascript">
	function fun123(msg){
		alert(msg.a);
	}
	$(function() {
		 $.ajax({
			type : "GET",
			url : "http://sso.taotao.com/sso_json.json",
			dataType : "script",
			success : function(msg) {
				//alert(msg.a);
			}
		});
		/* $.ajax({
			type : "GET",
			url : "http://sso.taotao.com/sso_js.js",
			dataType : "script",
			success : function(msg) {
				//alert(msg);
			}
		}); */ 
	});
</script>
<body>

</body>
</html>