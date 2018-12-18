$(function() {
	if(typeof($.cookie('menusf')) == "undefined"){
		$("#sidebar").attr("class","menu-min");
	}else{
		$("#sidebar").attr("class","");
		$("#main-content").css('marginLeft',190);
	}
});

//保存缩放菜单状态
function menusf(){
	if(document.getElementsByName('menusf')[0].checked){
		$.cookie('menusf', '', { expires: -1 });
		$("#sidebar").attr("class","menu-min");
		$("#main-content").css('marginLeft',43); 
	}else{
		$.cookie('menusf', 'ok');
		$("#sidebar").attr("class","");
		$("#main-content").css('marginLeft',190); 
	}
}
