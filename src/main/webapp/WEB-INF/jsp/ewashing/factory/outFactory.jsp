<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/inc/tag_css_js.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加门店</title>

<link type="text/css" href="<%=basePath%>/zui-1.5.0/css/zui.min.css"
    rel="stylesheet" />
<script type="text/javascript"
    src="<%=basePath%>/zui-1.5.0/lib/jquery/jquery.js"></script>
<script type="text/javascript"
    src="<%=basePath%>/zui-1.5.0/js/zui.min.js"></script>

<!-- form验证 -->
<link rel="stylesheet"
    href="<%=basePath%>/js/validationEngine/css/validationEngine.jquery.css"
    type="text/css" />
<!-- <script src="<%=basePath%>/js/jquery/jquery-1.7.2.min.js" type="text/javascript"></script> -->
<script
    src="<%=basePath%>/js/validationEngine/js/languages/jquery.validationEngine-zh_CN.js"
    type="text/javascript" charset="utf-8"></script>
<script
    src="<%=basePath%>/js/validationEngine/js/jquery.validationEngine-2.6.4.js"
    type="text/javascript" charset="utf-8"></script>

<!--  art.dialog  -->
<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css"
    rel="stylesheet" type="text/css" />
<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js"
    type="text/javascript"></script>
<script
    src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js"
    type="text/javascript"></script>

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
      $("#multi_tab").show();
      $("#single_tab").hide();
      $("#o2o_tab").hide();
  });
  
  
  // 查询历史批次
  function queryHistoryBatch(){
	  
  }
  
  // 出厂打印
  function outFactory(){
	  
	  var count =0;
		var arrays ="";
		var batchNo =$("#batchNo").val();
		var storeId =$("#storeId").val();
		var pkids = document.getElementsByName("checkbox");
		for ( var j = 0; j < pkids.length; j++) {
			if (pkids.item(j).checked == true) {
				count+=1;
				arrays+=pkids.item(j).value+","
			}
		}
		if(count==0){
			art.dialog.alert('请选择出厂衣服!');
			return;
		}
		
		
		jQuery.ajax({
			url : "<%=basePath%>/factory/doOutFactory",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'ids':arrays,'batchNo':batchNo,'storeId':storeId},
			success : function(data, textStatus) {
				if (data.resultCode == "0") {
					art.dialog.alert(data.resultMsg);
				} else {
					art.dialog.alert(data.resultMsg,function(){
						// 弹出订单查页面
						art.dialog.open( '<%=basePath%>/o2oOrder/printOrder', {
			                  title : 'O2O订单查询',
			                  width : '100%',
			                  height : 690,
			                  lock : true,
			                  close:function (){
			                	  queryClothes(3);
			                	  queryClothes(1);
			                	  queryClothes(2);
			      			  }
			            });
						
					});
				}
			}
		});
  }
  
  // 全选
  var flag=1;
	function checkAll(){
		var pkids = document.getElementsByName("checkbox");
		if(flag==1){
			flag=2;
			for ( var j = 0; j < pkids.length; j++) {
				if (pkids.item(j).checked == false) {
					pkids.item(j).checked = true;
				}
			}
		}else{
			if(flag==2){
				flag=1;
				for ( var j = 0; j < pkids.length; j++) {
					if (pkids.item(j).checked == true) {
						pkids.item(j).checked = false;
					}
				}
			}
		}
	}
  
  function trimNull(text){
	  if(text ==null || text =='' || text == 'null'){
		  return ""
	  }
	  else{
		  return text;
	  }
  }
	
  // 根据门店ID 获取待出厂数据(已上挂)
  function queryClothes(type){
	  
	  var storeId =$("#storeId").val();
	  var barCode =$("#barCode").val();
	  jQuery.ajax({
		  url : "<%=basePath%>/factory/querywatingHandonClothes",
		  type : "post",
          cache : false,
          dataType : "json",
          data : {
              'storeId' : storeId,
              'type':type,
              'barCode':barCode
          },
          success : function(data, textStatus) {
        	  
        	  $("#batchNo").val(data.batchNo);
              var tableStr = '';
              tableStr += '<table class="table datatable table-bordered  table-hover"  id=\datatable">';
              tableStr += '<thead>';
              tableStr += '<tr>';
              tableStr += '<td  align="center"><input type="checkbox" onclick="checkAll()"></td>';
              tableStr += '<td  align="center">订单号</td>';
              tableStr += '<td  align="center">会员名称</td>';
              tableStr += '<td  align="center">手机号</td>';
              tableStr += '<td  align="center">衣物条码</td>';
              tableStr += '<td  align="center">衣物状态</td>';
              tableStr += '<td  align="center">衣物名称</td>';
              tableStr += '<td  align="center">隔架区</td>';
              tableStr += '<td  align="center">隔架号</td>';
              tableStr += '<td  align="center">附件</td>';
              tableStr += '<td  align="center">颜色</td>';
              tableStr += '<td  align="center">品牌</td>';
              tableStr += '</tr>';
              // 遍历内容
              if (data.clothesList != null) {
                  for (var i = 0; i < data.clothesList.length; i++) {
                      tableStr += '<tr>';
                      tableStr += '<td align="center"><input type="checkbox" name="checkbox" value="'+data.clothesList[i].id+'"></td>';
                      
                      tableStr += '<td align="center">' + trimNull(data.clothesList[i].orderCode)
                      + '</td>';
              		  tableStr += '<td align="center">' + trimNull(data.clothesList[i].memberName)
                      + '</td>';
                      tableStr += '<td align="center">' + trimNull(data.clothesList[i].mobilePhone)
                      + '</td>'
              
                      tableStr += '<td align="center">' + trimNull(data.clothesList[i].barCode)
                              + '</td>';
                      tableStr += '<td align="center">' + trimNull(data.clothesList[i].status)
                              + '</td>';
                      tableStr += '<td align="center">' + trimNull(data.clothesList[i].clothesName)
                      + '</td>'
                      
                      
                      tableStr += '<td align="center">' + trimNull(data.clothesList[i].factoryHandOnArea)
                      + '</td>'
                      tableStr += '<td align="center">' + trimNull(data.clothesList[i].factoryHandOnNo)
                      + '</td>'
                      
                      
                      tableStr += '<td align="center">' + trimNull(data.clothesList[i].attachment)
                      + '</td>'
                      tableStr += '<td align="center">' + trimNull(data.clothesList[i].color)
                      + '</td>'
                      tableStr += '<td align="center">' + trimNull(data.clothesList[i].brand)
                      + '</td>'
                      tableStr += '</tr>';
                  }
              }
              tableStr += '</thead>';
              tableStr += '</table>';
              //门店信息,同时生成批次号
              
              var storeIno="";
              if(data.store!=null){
            	  $("#storeId").val(data.store.id);
            	  storeIno += '<table class="table datatable table-bordered  table-hover"  id=\datatable">';
                  storeIno += '<thead>';
                  storeIno += '<tr>';
                  storeIno += '<td  align="center">出厂批次号</td>';
                  storeIno += '<td  align="center">'+data.batchNo+'</td>';
                  storeIno += '<td  align="center">门店名称</td>';
                  storeIno += '<td  align="center">'+data.store.storeName+'</td>';
                  storeIno += '<td  align="center">门店号</td>';
                  storeIno += '<td  align="center">'+data.store.storeCode+'</td>';
                  storeIno += '</tr>';
                  storeIno += '<tr>';
                  storeIno += '<td  align="center">衣物数量</td>';
                  storeIno += '<td  align="center">'+data.clothesList.length+'</td>';
                  storeIno += '<td  align="center">联系人地址</td>';
                  storeIno += '<td  align="center">'+data.store.storeAddress+'</td>';
                  storeIno += '<td  align="center">联系电话</td>';
                  storeIno += '<td  align="center">'+data.store.telephone+'</td>';
                  storeIno += '</tr>';
              }
              
              if(type==1){
            	  $("#showData").html(tableStr);
                  $("#storeInfo").html(storeIno); 
              }
              if(type ==2){
            	  $("#showData1").html(tableStr);
                  $("#storeInfo1").html(storeIno);
              }
              if(type ==3){
            	  $("#showData2").html(tableStr);
                  $("#storeInfo2").html(storeIno);
              }
              
          }
      });
	  
  }
  
  // 单件出厂
  function singleLeaveFactory(){
	    var count =0;
		var arrays ="";
		var batchNo =$("#batchNo").val();
		var storeId =$("#storeId").val();
		var pkids = document.getElementsByName("checkbox");
		for ( var j = 0; j < pkids.length; j++) {
			if (pkids.item(j).checked == true) {
				count+=1;
				arrays+=pkids.item(j).value+","
			}
		}
		if(count==0){
			art.dialog.alert('请选择出厂衣服!');
			return;
		}
		
		jQuery.ajax({
			url : "<%=basePath%>/factory/doOutFactory",
			type : "post",
			cache : false,
			dataType : "json",
			data : {'ids':arrays,'batchNo':batchNo,'storeId':storeId},
			success : function(data, textStatus) {
				if (data.resultCode == "0") {
					art.dialog.alert(data.resultMsg);
				} else {
					art.dialog.alert(data.resultMsg,function(){
						queryClothes(1);
					});
				}
			}
		});
  }
  
  // 监听回车键
  document.onkeydown = function (event) {
        var e = event || window.event;
        if (e && e.keyCode == 13) { //回车键的键值为13
        	queryClothes(2);
        }
  }; 
  
  function choose(type){
      if (type=='1') {
          $("#multi_tab").show();
          $("#single_tab").hide();
          $("#o2o_tab").hide();
          $("#storeId option:first").prop("selected", 'selected'); 
          $("#showData1").html("");
          $("#storeInfo1").html("");
          $("#showData1").html("");
          $("#storeInfo1").html(""); 
          $("#showData2").html("");
          $("#storeInfo2").html(""); 
      } else if (type == '2') {
          $("#multi_tab").hide();
          $("#o2o_tab").hide();
          $("#storeId option:first").prop("selected", 'selected'); 
          $("#barCode").val("");
          $("#showData").html("");
          $("#storeInfo").html(""); 
          $("#showData1").html("");
          $("#storeInfo1").html("");
          $("#showData2").html("");
          $("#storeInfo2").html(""); 
          $("#single_tab").show();
      }
      else if (type == '3') {
          $("#multi_tab").hide();
          $("#single_tab").hide();
          $("#storeId option:first").prop("selected", 'selected'); 
          $("#barCode").val("");
          $("#showData").html("");
          $("#storeInfo").html(""); 
          $("#showData1").html("");
          $("#storeInfo1").html("");
          $("#showData2").html("");
          $("#storeInfo2").html(""); 
          $("#o2o_tab").show();
          queryClothes(3);
      }
      flag=1;
   }
</script>
</head>
<body>
	<div style="background-color: #d9d6d6;height: 50px;line-height: 50px;color: #54b04f;">
		&nbsp;&nbsp;当前工序：出厂 &nbsp;当前工人:${currentUser}
	</div>
    <div
        style="position: relative; margin-left: 15px; margin-top: 10px; width: 58%;">
        <ul class="nav nav-tabs" id="myTab">
            <li id="choose1" class="active"><a href="#identifier"
                data-toggle="tab" onclick="choose(1)">单店出厂</a></li>
            <li id="choose2"><a href="#identifier"
                data-toggle="tab" onclick="choose(2)">单件出厂</a></li>
            <li id="choose3"><a href="#identifier"
                data-toggle="tab" onclick="choose(3)">O2O出厂</a></li>
        </ul>
    </div>

    <!-- <div style="float: left;">挂衣区:</div> -->



    <div id="multi_tab" style="display: inline;">
		<input type="hidden" name="batchNo" id="batchNo" value="${data.batchNo}">
        <div style="float: left; margin-top: 20px; margin-left: 12px;">门店:</div>

        <div style="float: left;">
            <select id="storeId" name="storeId" onchange="queryClothes(1)"
                class="form-control validate[required] required"
                style="width: 140px; height: 40px; margin-top: 10px; margin-left: 3px;">
				<option value="-1">所有门店</option>
                <c:forEach items="${storeList}" var="dto" varStatus="status">
                    <option value="${dto.id}">${dto.storeName}</option>
                </c:forEach>
            </select>
        </div>
        <button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="outFactory()" style="background-color: #2183c5; margin-left: 10px; margin-top: 14px;">出厂打印</button>
        <button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="queryHistoryBatch()" style="background-color: #2183c5; margin-left: 10px; margin-top: 14px;">查看历史批次</button>
    	<br/>
    	<div id="showData">
    		<table class="table datatable table-bordered" cellpadding="0" cellspacing="0" border="1" width="100%" >
				<tr>
					<td width="10%" align="center">
						<input type="checkbox">
					</td>
					<td width="10%" align="center">
						订单号
					</td>
					<td width="10%" align="center">
						会员名
					</td>
					
					<td width="10%" align="center">
						手机号
					</td>
					<td width="10%" align="center">
						衣物条码
					</td>
					
					<td width="10%" align="center">
						衣物状态
					</td>
					<td width="10%" align="center">
						衣物名称
					</td>
					<td width="10%" align="center">
						附件
					</td>
					
					<td width="10%" align="center">
						颜色
					</td>
					
					<td width="10%" align="center">
						品牌
					</td>
				</tr>
			</table>
    	</div>
    	<div id="storeInfo"></div>
    </div>

    <div id="single_tab" style="display: inline;">
        <div class="input-group"
            style="float: left;margin-left: 12px; margin-top: 14px;">
            <span class="input-group-addon"
                style="width: 20px; text-align: left;">衣物条码：</span> 
                <input  id="barCode" name="barCode" value="${barCode}" placeholder="输入条码" onchange="queryClothes(2)"
                 type="text" class="form-control" style="width: 200px;">
        </div>

		<div style="float: left;" onclick="singleLeaveFactory()">
			<button class="btn btn-primary" type="button" id="transLogQueryBtn"
				style="background-color: #2183c5; margin-left: 10px; margin-top: 14px;">单件出厂</button>
		</div>

		<div style="float: left;margin-bottom: 20px;">
			 <button class="btn btn-primary" type="button"
            id="transLogQueryBtn" onclick="query()"
            style="background-color: #2183c5; margin-left: 10px; margin-top: 14px;">查看历史批次</button>
		 </div>
		 
		 
		  <div  style="padding-left: 10px; margin-top: 30px;position: relative;" id="showData1">
			<table class="table datatable table-bordered" cellpadding="0" cellspacing="0" border="1" width="100%" >
				<tr>
					<td width="10%" align="center">
						订单号
					</td>
					<td width="10%" align="center">
						会员名
					</td>
					
					<td width="10%" align="center">
						手机号
					</td>
					<td width="10%" align="center">
						衣物条码
					</td>
					<td width="10%" align="center">
						衣物状态
					</td>
					<td width="10%" align="center">
						衣物名称
					</td>
					<td width="10%" align="center">
						附件
					</td>
					
					<td width="10%" align="center">
						颜色
					</td>
					
					<td width="10%" align="center">
						品牌
					</td>
				</tr>
			</table>
	</div>
	
	<div  style="padding-left: 10px; margin-top: 20px;" id="storeInfo1">
		<table class="table datatable table-bordered" cellpadding="0" cellspacing="0" border="1" width="100%" >
			<tr>
				<td width="10%" align="right">
					出厂批次号:
				</td>
				<td width="10%" align="right">
					&nbsp;
				</td>
				
				<td width="10%" align="right">
					门店名称:
				</td>
				
				<td width="10%" align="right">
					&nbsp;
				</td>
				
				<td width="10%" align="right">
					门店号:
				</td>
				
				<td width="10%" align="right">
					&nbsp;
				</td>
			</tr>
			
			<tr>
				<td width="10%" align="right">
					衣物数量:
				</td>
				<td width="10%" align="right">
					&nbsp;
				</td>
				
				<td width="10%" align="right">
					联系人地址:
				</td>
				
				<td width="10%" align="right">
					&nbsp;
				</td>
				
				<td width="10%" align="right">
					联系电话:
				</td>
				
				<td width="10%" align="right">
					&nbsp;
				</td>
			</tr>
		</table>
		</div>
    </div>
    
    <div id="o2o_tab" style="display: inline;">
		<input type="hidden" name="batchNo" id="batchNo" value="${data.batchNo}">
        <%-- <div style="float: left; margin-top: 20px; margin-left: 12px;">门店:</div>

        <div style="float: left;">
            <select id="storeId" name="storeId" onchange="queryClothes(1)"
                class="form-control validate[required] required"
                style="width: 140px; height: 40px; margin-top: 10px; margin-left: 3px;">
				<option value="-1">所有门店</option>
                <c:forEach items="${storeList}" var="dto" varStatus="status">
                    <option value="${dto.id}">${dto.storeName}</option>
                </c:forEach>
            </select>
        </div> --%>
        <button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="outFactory()" style="background-color: #2183c5; margin-left: 10px; margin-top: 14px;">出厂打印</button>
        <button class="btn btn-primary" type="button" id="transLogQueryBtn" onclick="queryHistoryBatch()" style="background-color: #2183c5; margin-left: 10px; margin-top: 14px;">查看历史批次</button>
    	<br/>
    	<div id="showData2">
    		<table class="table datatable table-bordered" cellpadding="0" cellspacing="0" border="1" width="100%" >
				<tr>
					<td width="10%" align="center">
						<input type="checkbox">
					</td>
					<td width="10%" align="center">
						订单号
					</td>
					<td width="10%" align="center">
						会员名
					</td>
					
					<td width="10%" align="center">
						手机号
					</td>
					<td width="10%" align="center">
						衣物条码
					</td>
					
					<td width="10%" align="center">
						衣物状态
					</td>
					<td width="10%" align="center">
						衣物名称
					</td>
					<td width="10%" align="center">
						附件
					</td>
					
					<td width="10%" align="center">
						颜色
					</td>
					
					<td width="10%" align="center">
						品牌
					</td>
				</tr>
			</table>
    	</div>
    	<div id="storeInfo2"></div>
    </div>

</body>
</html>