<%@ page language="java" pageEncoding="utf-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport"
			content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
			<title>浣衣坊洗衣管理系统—系统登录</title>
			<meta name="renderer" content="webkit">
				<script src="js/jquery/jquery-1.11.3.min.js"></script>
				<script src="js/jquery/jquery.cookie.js"></script>
				<link href="bootstrap-3.3.0/css/bootstrap.min.css" rel="stylesheet">
					<style type="text/css">
html, body {
	height: 100%;
	overflow: hidden;
}

body {
	font-family: "Verdana", "Tahoma", "Lucida Grande", "Microsoft YaHei",
		"Hiragino Sans GB", sans-serif;
	background: url(bootstrap-3.3.0/images/loginbg_01.jpg) no-repeat center
		center fixed;
	background-size: cover;
}

.form-control {
	height: 37px;
}

.main_box {
	position: absolute;
	top: 45%;
	left: 50%;
	margin: -200px 0 0 -180px;
	padding: 15px 20px;
	width: 360px;
	height: 400px;
	min-width: 320px;
	background: #FAFAFA;
	background: rgba(255, 255, 255, 0.5);
	box-shadow: 1px 5px 8px #888888;
	border-radius: 6px;
}

.login_msg {
	height: 30px;
}

.input-group>.input-group-addon.code {
	padding: 0;
}

#captcha_img {
	cursor: pointer;
}

.main_box .logo img {
	height: 35px;
}

@media ( min-width : 768px) {
	.main_box {
		margin-left: -240px;
		padding: 15px 55px;
		width: 480px;
	}
	.main_box .logo img {
		height: 40px;
	}
}
</style>
					<script type="text/javascript">
var COOKIE_NAME = 'sys_em_username';
$(function() {
    choose_bg();
    changeCode();
    if ($.cookie(COOKIE_NAME)){
        $("#j_username").val($.cookie(COOKIE_NAME));
        $("#j_password").focus();
        $("#j_remember").attr('checked', true);
    } else {
        $("#j_username").focus();
    }
    $("#captcha_img").click(function(){
    	changeCode();
    });
    $("#login_form").submit(function(){
        var issubmit = true;
        var i_index  = 0;
        $(this).find('.form-control').each(function(i){
            if ($.trim($(this).val()).length == 0) {
                $(this).css('border', '1px #ff0000 solid');
                issubmit = false;
                if (i_index == 0){
                    i_index  = i; 
                }
             
            }
        });
        if (!issubmit) {
            $(this).find('.form-control').eq(i_index).focus();
            return false;
        }
        var $remember = $("#j_remember");
        if ($remember.attr('checked')) {
            $.cookie(COOKIE_NAME, $("#j_username").val(), { path: '/', expires: 15 });
        } else {
            $.cookie(COOKIE_NAME, null, { path: '/' });  //删除cookie
        }
        
        $("#login_ok").attr("disabled", true).val('登陆中..');
        /*
        var key = CryptoJS.enc.Base64.parse($("#j_randomKey").val());
        var iv = CryptoJS.enc.Latin1.parse('0102030405060708');
        var password = CryptoJS.AES.encrypt($("#j_password").val(), key, {iv:iv, mode:CryptoJS.mode.CBC, padding:CryptoJS.pad.Pkcs7 });
        
        $("#j_password").val(password)
        */
        

        
    });
    
    setTimeout(function(){	
		$.ajax({
			url : "<%=basePath%>/getCode?ts="+new Date().getTime(),
			type : "GET",
			data : {},
			dataType : "text",
			success : function(data){
				$("#j_captcha").val(data);				
			}
		});
	},500);
});

// 触发JS刷新
function changeCode(){
    $("#captcha_img").attr("src", "<%=basePath%>/authImage?date="+ (new Date().getTime()));
}

function choose_bg() {
    var bg = Math.floor(Math.random() * 9 + 1);
    $('body').css('background-image', 'url(bootstrap-3.3.0/images/loginbg_0'+ bg +'.jpg)');
}

function onEnterDown() {
	if (event.keyCode == 13) {
		if($("#j_password").val()==null || $("#j_password").val() ==''){
			event.returnValue = false;
		}else{
			event.returnValue = false;
			document.getElementById("login_form").submit();
		}
	}
}
</script>
</head>
<body onkeydown="onEnterDown();">
	<!--[if lte IE 7]>
<style type="text/css">
#errorie {position: fixed; top: 0; z-index: 100000; height: 30px; background: #FCF8E3;}
#errorie div {width: 900px; margin: 0 auto; line-height: 30px; color: orange; font-size: 14px; text-align: center;}
#errorie div a {color: #459f79;font-size: 14px;}
#errorie div a:hover {text-decoration: underline;}
</style>
<div id="errorie"><div>您还在使用老掉牙的IE，请升级您的浏览器到 IE8以上版本 <a target="_blank" href="http://windows.microsoft.com/zh-cn/internet-explorer/ie-8-worldwide-languages">点击升级</a>&nbsp;&nbsp;强烈建议您更改换浏览器：<a href="http://down.tech.sina.com.cn/content/40975.html" target="_blank">谷歌 Chrome</a></div></div>
<![endif]-->
	<div class="container">
		<div class="main_box">
			<form action="" id="login_form" method="post">
				<input type="hidden" value="" id="j_randomKey" /> <input
					type="hidden" name="jfinal_token" value="" />
				<p class="text-center logo">
					<!--  
            <img src="images/logo.png" height="45">
            -->
					<h3 align="center">Ewashing-浣衣坊洗衣管理系统</h3>
				</p>
				<p
					style="position: absolute; top: 68px; left: 310px; color: red; font-size: 15px;"
					id="notice">${resultMsg}</p>
				<div class="login_msg text-center">
					<font color="red"></font>
				</div>
				<div class="form-group">
					<div class="input-group">
						<span class="input-group-addon" id="sizing-addon-user"><span
							class="glyphicon glyphicon-user"></span></span> <input type="text"
							class="form-control" id="j_username" name="username"
							 placeholder="登录账号"
							aria-describedby="sizing-addon-user">
					</div>
				</div>
				<div class="form-group">
					<div class="input-group">
						<span class="input-group-addon" id="sizing-addon-password"><span
							class="glyphicon glyphicon-lock"></span></span> <input type="password"
							class="form-control" id="j_password" name="password"
							placeholder="登录密码"
							aria-describedby="sizing-addon-password">
					</div>
				</div>
				<div class="form-group">
					<div class="input-group">
						<span class="input-group-addon" id="sizing-addon-password"><span
							class="glyphicon glyphicon-exclamation-sign"></span></span> <input
							type="text" class="form-control" id="j_captcha" name="captcha"
							placeholder="验证码" aria-describedby="sizing-addon-password">
							<span class="input-group-addon code" id="basic-addon-code"><img
								id="captcha_img" src="" alt="点击更换" title="点击更换" class="m"></span>
					</div>
				</div>
				<div class="form-group">
					<div class="checkbox">
						<label for="j_remember" class="m"> <!-- 
                    <input id="j_remember" name="rememberMe" type="checkbox" value="">&nbsp;记住登陆账号!</label>
                     -->
					</div>
				</div>
				<div class="text-center">
					<button type="submit" id="login_ok" class="btn btn-primary btn-lg">&nbsp;登&nbsp;录&nbsp;</button>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<button type="reset" class="btn btn-default btn-lg">&nbsp;重&nbsp;置&nbsp;</button>
				</div>
				<hr style="margin-top: 15px; margin-bottom: 15px">
					<div style="text-align: center; padding-top: 0px; margin-top: 0px">
						<div>©2017-2018 浣衣坊洗衣股份有限公司 版权所有</div>
					</div>
			</form>
		</div>
	</div>
</body>
</html>