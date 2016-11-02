<html>
<body>
<script src="BaseJs/jquery-1.8.3.min.js"></script>
<h2>Hello World!</h2>
</body>
<script type="text/javascript">
 	$.ajax({
		url:"test.do",
		type:'post',
		data:{},
		dataType: 'text',
		success:function(data){
			console.log(data);
		},
		error:function(){
		}
	})

</script>
</html> 
