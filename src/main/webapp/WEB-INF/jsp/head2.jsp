<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div class="navbar navbar-inverse">
	<div class="navbar-inner">
		<div class="container-fluid">
			<a class="brand">
			   <img alt="" src="<%=basePath%>/static/images/logo.png" style="width: 54px;height: 30px">
			   <span style="width: 10px;"/><small>Ewashing-浣衣坊工厂管理系统</small> 
			</a>

			<ul class="nav ace-nav pull-right">
				<li class="light-blue user-profile">
					<a class="user-menu dropdown-toggle" href="javascript:;" data-toggle="dropdown"> 
					   <img alt="FH" src="<%=basePath%>/static/avatars/user.jpg" class="nav-user-photo" />
						<span id="user_info"> 
						   <span>${user.username}</span>
						</span> 
						<i class="icon-caret-down"></i>
					</a>
					<ul id="user_menu"
						class="pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-closer">
						<li><a href="javascript:void(0)" onclick="updatePassword()">个人信息维护</a></li>
						<c:if test="${user.userType=='1'}" >
							<li><a href="javascript:void(0)" onclick="updateStore()">维护门店信息</a></li>
						</c:if>
						<li><a href="<%=basePath%>/logout" target="_parent">退出</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
</div>

<!--引入属于此页面的js -->
<script type="text/javascript" src="<%=basePath%>/static/js/myjs/head.js"></script>
<script type="text/javascript">
	function updatePassword(){
		   
		   art.dialog.open( '<%=basePath%>/user/toPwdChange', {
				title : '个人信息维护',
				width : 600,
				height : 300,
				lock : true,
				close:function (){
					var type = art.dialog.data('type'); // 读取子窗口返回的数据  
					var resultCode = art.dialog.data('resultCode'); // 读取子窗口返回的数据  
					if(type=='pwd' && resultCode =='1'){
						location.href = "<%=basePath%>/logout";
					}
				}
			});
	}
</script>
