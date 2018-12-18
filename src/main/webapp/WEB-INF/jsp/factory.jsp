<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ path;
%>
<!DOCTYPE html>
<html lang="en">
<head>
<!-- jsp文件头和头部 -->
<meta name="description" content="overview & stats" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>浣衣坊洗衣管理系统</title>
<link href="static/css/bootstrap.min.css" rel="stylesheet" />
<link href="static/css/bootstrap-responsive.min.css" rel="stylesheet" />
<link rel="stylesheet" href="static/css/font-awesome.min.css" />
<link rel="stylesheet" href="static/css/ace.min.css" />
<link rel="stylesheet" href="static/css/ace-responsive.min.css" />
<link rel="stylesheet" href="static/css/ace-skins.min.css" />
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>

<!--  art.dialog  -->
<link href="<%=basePath%>/js/artDialog4.1.7/skins/blue.css" rel="stylesheet" type="text/css" />
<script src="<%=basePath%>/js/artDialog4.1.7/artDialog.js" type="text/javascript"></script>
<script src="<%=basePath%>/js/artDialog4.1.7/plugins/iframeTools.source.js" type="text/javascript"></script>

<!-- 引入factory.css -->
<link rel="stylesheet" href="<%=basePath%>/static/css/factory.css" type="text/css" />

<script type="text/javascript"> 
	  	function openMenu(id){
	  		var display =$('#'+id).css('display');
	  		if(display == 'none'){
	  			$('.submenu').css('display','none');
	  			$('#'+id).parent().parent('.submenu').css('display','block');
	  			$('#'+id).css('display','block');
	  		}else{
	  			$('#'+id).css('display','none');
	  		}
	  	}
	  	
	  	var title="";
	  	/* 服务类型0：质检 1:干洗 2:水洗 3:皮衣 4:织补 5:单烫 6：精洗 7：奢侈品护理 8:银行类 9：校服类 10：工服类 11：染色12：商品出售 */
	  	function infactory() {
	  	  art.dialog.open( '<%=basePath%>/factory/inFactory', {
	            title : '入厂',
	            width : '100%',
	            height : 700,
	            lock : true
	        });
        }
	  	// 洗涤操作
	  	function washingOperate(washingType) {
            title =getNameByWashingType(washingType);
	  	    art.dialog.open( '<%=basePath%>/factory/doWashing?washingType='+washingType, {
	                title : title,
	                width : 900,
	                height : 690,
	                lock : true
            });
        }
	  	
	  	// 出厂
	  	function outfactory() {
            art.dialog.open( '<%=basePath%>/factory/outFactory', {
                  title : '出厂',
                  width : '100%',
                  height : 690,
                  lock : true
              });
          }
	  	
	  	// 个人计件
	  	function clothesStatistics(){
	  		art.dialog.open( '<%=basePath%>/statistics/clothesStatistics', {
                title : '个人计件',
                width : '100%',
                height : 690,
                lock : true
            });
	  	}
	  	
	  	//  入厂统计
	  	function inFactoryStatistics(){
	  		art.dialog.open( '<%=basePath%>/statistics/inFactoryStatistics', {
                title : '门店入厂统计',
                width : '100%',
                height : 690,
                lock : true
            });
	  	}
	  	
	  	// 订单查询
	  	function orderQuery(){
	  		art.dialog.open( '<%=basePath%>/orderQuery/order', {
                title : '订单查询',
                width : '100%',
                height : 690,
                lock : true
            });
	  	}
	  	
	 	// 物流单打印
	  	function printOrder(){
	  		art.dialog.open( '<%=basePath%>/o2oOrder/printOrder', {
                title : 'O2O 物流单打印',
                width : '100%',
                height : 690,
                lock : true
          });
	  	}
	 
	  	// 统计分析
	  	function statisticalAnalysis(){
	  		art.dialog.open( '<%=basePath%>/statistics/statisticalAnalysis', {
                title : '统计分析',
                width : '100%',
                height : 690,
                lock : true
            });
	  	}
	  	
	  	// 拍照
	  	function takephoto() {
	        art.dialog.open( '<%=basePath%>/factory/takePhoto', {
	            title : '拍照',
	            width : '80%',
	            height : 500,
	            lock : true
        	});
    	}
	  	
	  	// 修改密码
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

    // 根据类型获取服务名称
    function getNameByWashingType(washingType) {
        var title = "";
        if (washingType == '30') {
            title = '质检';
        } else if (washingType == '31') {
            title = '水洗';
        } else if (washingType == '32') {
            title = '干洗';
        } else if (washingType == '33') {
            title = '皮衣';
        } else if (washingType == '34') {
            title = '精洗';
        } else if (washingType == '35') {
            title = '熨烫';
        } else if (washingType == '36') {
            title = '去渍';
        } else if (washingType == '37') {
            title = '烘干';
        } else if (washingType == '38') {
            title = '去毛';
        } else if (washingType == '39') {
            title = '织补';
        } else if (washingType == '40') {
            title = '鞋类';
        } else if (washingType == '41') {
            title = '工服洗护';
        }
        return title;
    }
    
    // 设置隔架区
    function setHandArea(){
    	 art.dialog.open( '<%=basePath%>/handon/area', {
	            title : '设置隔架区',
	            width : '100%',
	            height : 690,
	            lock : true
     	});
    }
    
	// 设置隔架区
    function setHandNo(){
    	art.dialog.open( '<%=basePath%>/handon/no', {
            title : '设置隔架号',
            width : '100%',
            height : 690,
            lock : true
 		});
    }
	 
	// 上挂
    function handon(){
    	art.dialog.open( '<%=basePath%>/factory/factoryHandOnNew', {
            title : '工厂上挂',
            width : '100%',
            height : 690,
            lock : true
 		});
    }
	
	//查询衣物详情
	function queryClothesByBarcode() {
	    var barCode =$("#barCode").val();
	    var type = 2;
	    if (barCode == null || barCode =='') {
	        art.dialog.alert('条码不能为空!');
            return;
	    }
	    jQuery.ajax({url : "<%=basePath%>/factory/queryClothes",
	          type : "post",
	          cache : false,
	          dataType : "json",
	          data : {
	              'type':type,
	              'barCode':barCode
	          },
	          success : function(data, textStatus) {
	              var listStr = JSON.stringify(data.clothesList[0]);
	              var order = JSON.stringify(data.orderInfo);
	              if (data.clothesList==null || data.clothesList.length==0) {
	                  art.dialog.alert('查不到该条码的衣服!');
	              } else {
	                  if (data.resultCode == "0") {
	                      art.dialog.alert(data.resultMsg);
	                  } else {
    	                  art.dialog.open(encodeURI(encodeURI('<%=basePath%>/factory/queryColthesResult?orderInfo='+order+'&result='+listStr)), {
    	                      title : '衣服查询结果',
    	                      width : '100%',
    	                      height : 690,
    	                      lock : true
    	                  });
	                  }
	              }
	          }
	      });
    }
	 
    
</script>
</head>
<body>

    <!-- 页面顶部¨ -->
    <%@ include file="head2.jsp"%>
    <div id="websocket_button"></div>
    <div class="container-fluid" id="main-container"
        style="background-color: #199ac1">
        <div id="main-content" class="clearfix" style="display: inline;">
            <div id="mainFrame"
                style="position: relative; margin-top: 10px; margin-left: 50px; float: left;">
                <table
                    style="float: left; border-collapse: separate; border-spacing: 10px 10px;">
                    <tr style="background-color: #0c645c;">
                        <td
                            style="text-align: center; vertical-align: middle; height: 60px;"
                            colspan="5"><span
                            class="input-group-addon"
                            style="width: 20px; text-align: left; color: white; font-size: 24px;">洗衣工序：</span>
                            <input id="barCode" name="barCode"
                            value="${barCode}" placeholder="输入条码"
                            type="text" class="form-control"
                            style="width: 200px;">
                            <button class="btn btn-primary"
                                type="button" id="transLogQueryBtn"
                                onclick="queryClothesByBarcode()" 
                                style="background-color: #2183c5; margin-left: 10px;">搜索衣物</button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #d44923;"
                                onclick="infactory()"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#d44923'">入厂</div>
                        </td>
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #d44923; position: relative;"
                                onclick="washingOperate('30')"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#d44923'">质检</div>
                        </td>
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #145b70; position: relative;"
                                onclick="washingOperate('31')"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#145b70'">水洗</div>
                        </td>
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #d44923; position: relative;"
                                onclick="washingOperate('32')"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#d44923'">干洗</div>
                        </td>
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #d44923; position: relative;"
                                onclick="washingOperate('33')"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#d44923'">皮衣</div>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #145b70; position: relative;"
                                onclick="washingOperate('34')"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#145b70'">精洗</div>
                        </td>
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #ab367e; position: relative;"
                                onclick="washingOperate('35')"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#ab367e'">熨烫</div>
                        </td>
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #f82a10; position: relative;"
                                onclick="washingOperate('36')"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#f82a10'">去渍</div>
                        </td>
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #ab367e; position: relative;"
                                onclick="washingOperate('37')"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#ab367e'">烘干</div>
                        </td>
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #ab367e; position: relative;"
                                onclick="washingOperate('38')"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#ab367e'">去毛</div>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #f82a10; position: relative;"
                                onclick="washingOperate('39')"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#f82a10'">织补</div>
                        </td>
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #145b70; position: relative;"
                                onclick="washingOperate('40')"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#145b70'">鞋类</div>
                        </td>
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #d44923; position: relative;"
                                onclick="takephoto()"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#d44923'">拍照</div>
                        </td>
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #ab367e; position: relative;"
                                onclick="washingOperate('41')"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#ab367e'">工服洗护</div>
                        </td>
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #f82a10; position: relative;"
                                onclick="handon()"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#f82a10'">上挂</div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #5236ac; position: relative;"
                                onclick="outfactory()"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#5236ac'">出厂</div>
                        </td>
                    </tr>
                </table>

                <table
                    style="float: left; border-collapse: separate; border-spacing: 10px 10px; margin-left: 120px;">

                    <tr style="background-color: #0c645c">
                        <td
                            style="text-align: center; vertical-align: middle; height: 60px;"
                            colspan="4">
                            <div style="font-size: 24px; color: white;">当前用户:${user.username}</div>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #5236ac;"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#5236ac'">退款审核</div>
                        </td>
                        <td>
                            <div class="factory-left-td-div" 
                                style="background-color: #ab367e; position: relative;"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#ab367e'">计件统计</div>
                        </td>
                        <td>
                            <div class="factory-left-td-div" onclick="clothesStatistics()"
                                style="background-color: #d44923; position: relative;"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#d44923'">个人计件</div>
                        </td>
                        <td>
                            <div class="factory-left-td-div" onclick="inFactoryStatistics()"
                                style="background-color: #145b70; position: relative;"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#145b70'">入厂统计</div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #ab367e;"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#ab367e'">电子看板</div>
                        </td>
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #5236ac; position: relative;"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#5236ac'">预警分析</div>
                        </td>
                        <td>
                            <div class="factory-left-td-div" onclick="updatePassword()"
                                style="background-color: #145b70; position: relative;"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#145b70'">修改密码</div>
                        </td>
                        <td>
                            <div class="factory-left-td-div" onclick="statisticalAnalysis()"
                                style="background-color: #5236ac; position: relative;"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#5236ac'">统计分析</div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="factory-left-td-div" onclick="setHandArea()"
                                style="height:80px;padding-top:30px;background-color: #ab367e; position: relative;line-height:30px;"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#ab367e'">隔架区<br>设置</div>
                        </td>
                        <td>
                            <div class="factory-left-td-div" onclick="setHandNo()"
                                style="height:80px;padding-top:30px;background-color: #5236ac; position: relative;line-height:30px;"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#5236ac'">隔架号<br>设置</div>
                        </td>
                        
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #f82a10; position: relative;"
                                onclick="orderQuery()"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#f82a10'">订单查询</div>
                        </td>
                        
                        <td>
                            <div class="factory-left-td-div"
                                style="background-color: #f82a10; position: relative;"
                                onclick="printOrder()"
                                onmouseover="this.style.backgroundColor='#aca8d4'"
                                onmouseout="this.style.backgroundColor='#f82a10'">物流单打印</div>
                        </td>
                    </tr>
                </table>
            </div>

            <!-- 换肤 -->
            <div id="ace-settings-container">
                <div class="btn btn-app btn-mini btn-warning"
                    id="ace-settings-btn">
                    <i class="icon-cog"></i>
                </div>
                <div id="ace-settings-box">
                    <div>
                        <div class="pull-left">
                            <select id="skin-colorpicker" class="hidden">
                                <option data-class="default"
                                    value="#438EB9" selected>#438EB9</option>
                                <option data-class="skin-1"
                                    value="#222A2D">#222A2D</option>
                                <option data-class="skin-2"
                                    value="#C6487E">#C6487E</option>
                                <option data-class="skin-3"
                                    value="#D0D0D0">#D0D0D0</option>
                            </select>
                        </div>
                        <span>&nbsp; 选择皮肤</span>
                    </div>
                    <div>
                        <label><input type='checkbox'
                            name='menusf' id="menusf"
                            onclick="menusf();" /><span class="lbl"
                            style="padding-top: 5px;">菜单缩放</span></label>
                    </div>
                </div>
            </div>
        </div>

        <!-- #main-content -->
        <a href="#" id="btn-scroll-up"
            class="btn-scroll-up btn btn-sm btn-inverse"> <i
            class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
        </a>
    </div>
    <!-- 引入 -->
    <script type="text/javascript">window.jQuery || document.write("<script src='<%=basePath%>/static/js/jquery-1.9.1.min.js'>\x3C/script>");
                </script>
    <script src="<%=basePath%>/static/js/bootstrap.min.js"></script>
    <script src="<%=basePath%>/static/js/ace-elements.min.js"></script>
    <script src="<%=basePath%>/static/js/ace.min.js"></script>
    <!-- 引入 -->
    <script type="text/javascript"
        src="<%=basePath%>/static/js/jquery.cookie.js"></script>
    <script type="text/javascript"
        src="<%=basePath%>/static/js/myjs/menusf.js"></script>
    <!--引入属于此页面的js -->
    <script type="text/javascript"
        src="<%=basePath%>/static/js/myjs/index.js"></script>
</body>
</html>
