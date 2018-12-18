<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/inc/tag_css_js.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>入厂</title>

<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css" rel="stylesheet" />
<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>

<!-- form验证 -->
<link rel="stylesheet" href="<%=basePath%>/js/validationEngine/css/validationEngine.jquery.css" type="text/css" />
<!-- <script src="<%=basePath%>/js/jquery/jquery-1.7.2.min.js" type="text/javascript"></script> -->
<script src="<%=basePath%>/js/validationEngine/js/languages/jquery.validationEngine-zh_CN.js" type="text/javascript" charset="utf-8"></script>
<script src="<%=basePath%>/js/validationEngine/js/jquery.validationEngine-2.6.4.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="<%=basePath%>/js/ieplugin.js"></script>

<!--  art.dialog  -->
<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css" rel="stylesheet" type="text/css" />
<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js" type="text/javascript"></script>
<script src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js" type="text/javascript"></script>

<!-- 引入factory.css -->
<link rel="stylesheet" href="<%=basePath%>/static/css/factory.css" type="text/css" />

<style>
ul.ztree {
 margin-top: 10px;
 border: 1px solid #617775;
 background: #f0f6e4;
 width: 220px;
 height: 200px;
 overflow-y: scroll;
 overflow-x: auto;
}
</style>

<script type="text/javascript">
    jQuery(document).ready(function() {
		$("#infactory_clothes_barcode").focus();
    });
    function queryClothes() {
    	
    	// 再次扫码，需要清空之前的记录
    	clearData();
    	
        var clothesCode = $("#infactory_clothes_barcode").val();
        if (clothesCode=="")
            return;
        jQuery.ajax({
            url : "<%=basePath%>/factory/queryClothesUpdateType",
            type : "post",
            cache : false,
            dataType : "json",
            data : {'barCode':clothesCode},
            success : function(data, textStatus) {
                if (data.resultCode == "0") {
                    art.dialog.alert(data.resultMsg);
                } else {
                    $("#l_storename").text(trimNull(data.storeclothes.storeName));
                    $("#l_ordernum").text(trimNull(data.storeclothes.orderCode));
                    $("#l_sendnum").text(trimNull(data.storeclothes.sendNumber));
                    $("#l_createdate").text(trimNull(data.storeclothes.sendDateStr));
                    $("#r_clothesname").text(trimNull(data.storeclothes.clothesName));
                    $("#r_clothescolor").text(trimNull(data.storeclothes.color));
                    $("#r_brand").text(trimNull(data.storeclothes.brand));
                    $("#r_money").text(data.storeclothes.price);
                    $("#r_urgency").text(trimNull(data.storeclothes.urgency));
                    $("#r_urgencymoney").text(data.storeclothes.serviceFee);
                    $("#r_washtype").text(trimNull(data.storeclothes.serviceType));
                    $("#r_attachment").text(trimNull(data.storeclothes.attachment));
                    $("#r_flaw").text(trimNull(data.storeclothes.flaw));
                    if(data.storeInfo!=null && data.storeInfo!=''){
                    	$("#l_storetel").text(trimNull(data.storeInfo.telephone));
                    }
                    $("#l_count").text(trimNull(data.storeclothes.clothesCount));
                    $("#l_payStatus").text(trimNull(data.storeclothes.payStatus));
                    $("#clothesId").val(trimNull(data.storeclothes.id));
                    
                    if(trimNull(data.orderInfo.orderType)=="1"){
                    	$("#orderType").text("门店订单");
                    }
                    if(trimNull(data.orderInfo.orderType)=="2"){
                    	
                    	if(trimNull(data.orderInfo.appId) =='wx614b63f73b55d7ef'){
                    		$("#orderType").text("萨维亚订单");
                    	}
                    	if(trimNull(data.orderInfo.appId) =='wxc6097b5bf9ba801d'){
                    		$("#orderType").text("保利.浣衣坊订单");
                    	}
                    }
                    if(data.handon_count !=null && data.handon_count != ''){
                        $("#l_in_count").text(trimNull(data.handon_count));
                    }
                    
                    // 清空条码，并且focus
                    $("#infactory_clothes_barcode").val("");
                    $("#infactory_clothes_barcode").focus();
                }
            }
        });
    }
    
    function clearData(){
    	$("#l_storename").text("");
        $("#l_ordernum").text("");
        $("#l_sendnum").text("");
        $("#l_createdate").text("");
        $("#r_clothesname").text("");
        $("#r_clothescolor").text("");
        $("#r_brand").text("");
        $("#r_money").text("");
        $("#r_urgency").text("");
        $("#r_urgencymoney").text("");
        $("#r_washtype").text("");
        $("#r_attachment").text("");
        $("#r_flaw").text("");
        $("#l_in_count").text("");
        
    }
    
    
    /** 拍照功能 **/
	function takePhoto(){
    	
		var clothesId = $("#clothesId").val();
    	if (trimNull(clothesId)=='') {
            art.dialog.alert('找不到指定的衣服！');
            return;
        }
    	
		// 判断浏览器类型
		if(isIe()){
			art.dialog.open( '<%=basePath%>/storeBusiness/takePhoto', {
				title : '衣服拍照',
				width : '90%',
				height : 500,
				lock : false,
				close:function (){
					var imagePath = art.dialog.data('imagePath'); // 读取子窗口返回的数据  
					if(imagePath==null || imagePath ==''){
						return;
					}
					var clothesId = $("#clothesId").val();
					$("#imagePath").val(imagePath);
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
		}else{
			art.dialog.alert("目前只支持IE浏览器");
		}
	}
	
    //  是否为IE
	function isIe(){
		var nvshell =$.NV('shell');
		// 判断浏览器类型
		if(nvshell=='搜狗浏览器' || nvshell =='360se' || nvshell =='ie'){
			return true;
		}
		else {
			return false;
		}
		/* if (!!window.ActiveXObject || "ActiveXObject" in window){   
	        //是  
	         return true;
	        }else{  
	        //不是   
	        return false;
	    }   */
	}
	
	// 拍照历史页面
    function photoHistory() {
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
    
	//  格式化数据
    function trimNull(text){
    	if(text ==null || text =='' || text =='null'){
    		return ""
    	}
    	return text;
    }
</script>
</head>
<body>
	<input type="hidden" name="clothesId" id ="clothesId">
	<input type="hidden" name ="imagePath" id ="imagePath">
    <div style="float: left">

        <div style="margin-top: 5px;margin-left: 10px;">
            <button class="btn btn-primary" type="button"
                id="transLogQueryBtn" onclick="query()"
                style="background-color: #2183c5; margin-right: 10px;">查看未入厂明细</button>
            <!-- <button class="btn btn-primary" type="button"
                id="transLogQueryBtn" onclick="query()"
                style="background-color: #6eb3e0; margin-right: 10px;">补价</button> -->
            <button class="btn btn-primary" type="button"
                id="transLogQueryBtn" onclick="query()"
                style="background-color: #feb752; margin-right: 10px;">订单衣物详情</button>
            <button class="btn btn-primary" type="button"
                id="transLogQueryBtn" onclick="query()"
                style="background-color: #d05b47; margin-right: 10px;">退洗</button>
            <!-- <button class="btn btn-primary" type="button"
                id="transLogQueryBtn" onclick="query()"
                style="background-color: #2183c5; margin-right: 10px;">查看衣服明细</button> -->
        </div>

        <div style="padding-left: 10px; margin-top: 5px;">
            <table cellpadding="0" cellspacing="1px" border="1px"
                width="520px">
                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">送洗门店:</div>
                    </td>
                    <td>
                        <div id="l_storename" class="infactory-td-div-content"></div>
                    </td>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">门店电话:</div>
                    </td>
                    <td>
                        <div id="l_storetel" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">送洗件数:</div>
                    </td>
                    <td>
                        <div id="l_count" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">已入厂件数:</div>
                    </td>
                    <td>
                        <div id="l_in_count"  class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

				<tr>
                    <td style="width: 80px;">
                        <div class="infactory-td-div">订单类型:</div>
                    </td>
                    <td colspan="3">
                        <div class="infactory-td-div-content" id ="orderType">&nbsp;</div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 80px;">
                        <div class="infactory-td-div">未入厂件数:</div>
                    </td>
                    <td colspan="3">
                        <div class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">订单号:</div>
                    </td>
                    <td colspan="3">
                        <div id="l_ordernum" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>
                
                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">付款状态:</div>
                    </td>
                    <td colspan="3">
                        <div id="l_payStatus" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">送洗单号:</div>
                    </td>
                    <td colspan="3">
                        <div id="l_sendnum" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">送洗时间:</div>
                    </td>
                    <td>
                        <div id="l_createdate" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">是否返洗:</div>
                    </td>
                    <td>
                        <div class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

            </table>

        </div>

        <button class="btn btn-primary" type="button"
            id="transLogQueryBtn" onclick="takePhoto()"
            style="background-color: #2183c5; margin-right: 10px; margin-top: 10px;margin-left: 10px;">打开摄像头拍照</button>
    </div>

    <div style="float: left; margin-left: 80px;">

        <div class="input-group"
            style="margin-left: 150px; margin-top: 40px;">
            <span class="input-group-addon"
                style="width: 20px; text-align: left;">衣物条码：</span> <input
                id="infactory_clothes_barcode" name="barCode" value=""
                type="text" class="form-control" style="width: 200px;" onchange="queryClothes()">
            <!-- <span class="glyphicon glyphicon-star"></span> -->
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
                        <div id="r_clothesname" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">颜色:</div>
                    </td>
                    <td>
                        <div id="r_clothescolor" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">品牌:</div>
                    </td>
                    <td>
                        <div id="r_brand" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">收衣价格:</div>
                    </td>
                    <td>
                        <div id="r_money" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">紧急程度:</div>
                    </td>
                    <td>
                        <div id="r_urgency" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">加急费用:</div>
                    </td>
                    <td>
                        <div id="r_urgencymoney" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">服务:</div>
                    </td>
                    <td colspan="3">
                        <div id="r_washtype" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">附件:</div>
                    </td>
                    <td colspan="3">
                        <div id="r_attachment" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">瑕疵:</div>
                    </td>
                    <td colspan="3">
                        <div id="r_flaw" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

                <tr>
                    <td style="width: 75px;">
                        <div class="infactory-td-div">附加服务:</div>
                    </td>
                    <td colspan="3">
                        <div id="r_additionalservice" class="infactory-td-div-content">&nbsp;</div>
                    </td>
                </tr>

            </table>

        </div>

        <button class="btn btn-primary" type="button"
            id="transLogQueryBtn" onclick="photoHistory()"
            style="background-color: #2183c5; margin-right: 10px; margin-top: 10px;">显示衣服历史拍照列表</button>

    </div>
    <!-- 
$("if").val("2222") -->

</body>
</html>