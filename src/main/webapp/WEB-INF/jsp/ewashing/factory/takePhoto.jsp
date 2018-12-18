<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/inc/tag_css_js.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>拍照</title>

<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>

<!-- form验证 -->
<link rel="stylesheet" href="<%=basePath%>/js/validationEngine/css/validationEngine.jquery.css"  type="text/css" />
<!-- <script src="<%=basePath%>/js/jquery/jquery-1.7.2.min.js" type="text/javascript"></script> -->
<script src="<%=basePath%>/js/validationEngine/js/languages/jquery.validationEngine-zh_CN.js"  type="text/javascript" charset="utf-8"></script>
<script src="<%=basePath%>/js/validationEngine/js/jquery.validationEngine-2.6.4.js"  type="text/javascript" charset="utf-8"></script>

<!--  art.dialog  -->
<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css" rel="stylesheet" type="text/css" />
<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js" type="text/javascript"></script>
<script  src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js" type="text/javascript"></script>

<!-- 引入factory.css -->
<link rel="stylesheet" href="<%=basePath%>/static/css/factory.css" type="text/css" />

<script type="text/javascript">
    jQuery(document).ready(function() {
        $("#takephoto_clothes_barcode").focus();
    });
    
    // 拍照历史页面
    function showHistoryPhoto(){
    	var clothesId = $("#clothesId").val();
    	if (trimNull(clothesId)=='') {
            art.dialog.alert('找不到指定的衣服！');
        } else {
	  	  art.dialog.open( '<%=basePath%>/storeBusiness/showPhoto?clothesId='+clothesId, {
	            title : '拍照历史',
	            width : '100%',
	            height : 700,
	            lock : true
	        });
        }
    }
    
    // 跳转到拍照页面
    function goTakePhoto() {
    	var clothesId = $("#clothesId").val();
        if (clothesId=="") {
            art.dialog.alert("请先输指定拍照衣服!");
            return;
        }
        art.dialog.open( '<%=basePath%>/factory/goTakePhoto?barCode='+clothesId, {
            title : '拍照',
            width : 1350,
            height : 690,
            lock : true,
            close:function (){
				var imagePath = art.dialog.data('imagePath'); // 读取子窗口返回的数据  
				$("#imagePath").val(imagePath);
				
				if(imagePath ==null || imagePath ==''){
					return;
				}
				// 保存图片
				jQuery.ajax({url : "<%=basePath%>/storeBusiness/savePhoto",
			          type : "post",
			          cache : false,
			          dataType : "json",
			          data : {
			              'imagePath':imagePath,
			              'clothesId':clothesId
			          },
			          success : function(data, textStatus) {
			        	  if (data.resultCode == "0") {
		                      art.dialog.alert(data.resultMsg);
		                  }
			        	  else {
			        		  art.dialog.alert("上传图片成功");
			        	  }
			          }
			      });
			}
    	});
    }
    
    // 根据条码查询衣服信息
    function queryClothes(){
    	var clothesCode = $("#takephoto_clothes_barcode").val();
        if (clothesCode=="") {
            art.dialog.alert("请先输制定拍照衣服!");
            return;
        }
        // 条码搜索
        var type =2;
        jQuery.ajax({url : "<%=basePath%>/factory/queryClothes",
	          type : "post",
	          cache : false,
	          dataType : "json",
	          data : {
	              'type':type,
	              'barCode':clothesCode
	          },
	          success : function(data, textStatus) {
	              if (data.clothesList==null || data.clothesList.length==0) {
	                  art.dialog.alert('查不到该条码的衣服!');
	              } else {
	                  if (data.resultCode == "0") {
	                      art.dialog.alert(data.resultMsg);
	                  } else {
	                	  $("#r_clothesname").text(trimNull(data.clothesList[0].clothesName));
	                	  $("#r_color").text(trimNull(data.clothesList[0].color));
	                	  $("#r_brand").text(trimNull(data.clothesList[0].brand));
	                	  $("#r_price").text(trimNull(data.clothesList[0].price));
	                	  $("#r_urgency").text(trimNull(data.clothesList[0].urgency));
	                	  $("#r_servicetype").text(trimNull(data.clothesList[0].serviceType));
	                	  $("#r_attachment").text(trimNull(data.clothesList[0].attachment));
	                      $("#r_flaw").text(trimNull(data.clothesList[0].flaw));
	                      $("#clothesId").val(trimNull(data.clothesList[0].id));
	                      
	                      // 清空条码区域
	                      $("#takephoto_clothes_barcode").val("");
	                      $("#takephoto_clothes_barcode").focus();
	                  }
	              }
	          }
	      });
    }
    
    // 过滤字符串的 null
    function trimNull(text){
    	if(text ==null || text =='' || text =='null'){
    		return ""
    	}
    	return text;
    }
    
    // 清除记录
    function clearData(){
        $("#r_clothesname").text("");
        $("#r_color").text("");
        $("#r_brand").text("");
        $("#r_price").text("");
        $("#r_urgency").text("");
        $("#r_servicetype").text("");
        $("#r_flaw").text("");
    }
</script>

</head>
<body>
	<input type="hidden" name ="clothesId" id ="clothesId">
	<input type="hidden" h name ="imagePath" id ="imagePath">
    <div style="float: left; margin-left: 80px;">
        <div class="input-group" style="margin-top: 40px;margin-left: 10px">
            <span class="input-group-addon" style="width: 20px; text-align: left;">衣物条码：</span> 
            <input id="takephoto_clothes_barcode" name="barCode" value="${barCode}" onchange="queryClothes()" type="text" class="form-control" style="width: 200px;">
        </div>

        <div style="padding-left: 10px; margin-top: 20px;">
            <table cellpadding="0" cellspacing="1px" border="1px"
                width="520px">

                <tr>
                    <td colspan="4" style="text-align: center;">
                        <div class="infactory-td-div-title">信息提示区域</div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">衣服名称:</div>
                    </td>
                    <td>
                        <div id="r_clothesname" class="infactory-td-div-content"></div>
                    </td>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">颜色:</div>
                    </td>
                    <td>
                        <div id="r_color" class="infactory-td-div-content"></div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">品牌:</div>
                    </td>
                    <td>
                        <div id="r_brand" class="infactory-td-div-content"></div>
                    </td>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">收衣价格:</div>
                    </td>
                    <td>
                        <div id="r_price" class="infactory-td-div-content"></div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">紧急程度:</div>
                    </td>
                    <td>
                        <div id="r_urgency" class="infactory-td-div-content"></div>
                    </td>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">加急费用:</div>
                    </td>
                    <td>
                        <div class="infactory-td-div-content">无</div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">服务:</div>
                    </td>
                    <td colspan="3">
                        <div id="r_servicetype" class="infactory-td-div-content"></div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">附件:</div>
                    </td>
                    <td colspan="3">
                        <div class="infactory-td-div-content"></div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">瑕疵:</div>
                    </td>
                    <td colspan="3">
                        <div id="r_flaw" class="infactory-td-div-content"></div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">附加服务:</div>
                    </td>
                    <td colspan="3">
                        <div class="infactory-td-div-content"></div>
                    </td>
                </tr>
            </table>
        </div>
        <button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="showHistoryPhoto()" style="background-color: #2183c5; margin-right: 10px; margin-top: 10px;margin-left: 10px">显示衣服历史拍照列表</button>
		<button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="goTakePhoto()" style="background-color: #2183c5;margin-top: 10px;">打开摄像头拍照</button>
    </div>
</body>
</html>