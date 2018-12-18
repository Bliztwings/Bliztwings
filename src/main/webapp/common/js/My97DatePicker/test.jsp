<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Test My97DatePicker</title>
    <script src="WdatePicker.js"></script>
  </head>
  
  <body>
    <font color="blue">测试 My97DatePicker：</font> <hr>
   <table>
   	<tr>
   		<td  bgcolor="#cccfff">默认皮肤：</td>
   		<td>
   			<input id="selectDate1" name="endDate" type="text" size="18" />
				<img id="img2"
						onclick="WdatePicker({el:'selectDate1',dateFmt:'yyyy-MM-dd'})"
						src="./skin/datePicker.gif" width="16"
						height="22" align="absmiddle">
   		</td>
   		<td  bgcolor="#cccfff">换肤结果：</td>
   		<td>
   			<input id="selectDate2" name="endDate" type="text" size="18" />
   			<img id="img2"
						onclick="WdatePicker({el:'selectDate2',dateFmt:'yyyy-MM-dd',skin:'blue'})"
						src="./skin/datePicker.gif" width="16"
						height="22" align="absmiddle">
   		</td>
   	</tr>
   	<tr>
   		<td  bgcolor="#cccfff"> yyyy-MM-dd：</td>
   		<td>
   			<input id="selectDate3" name="endDate" type="text" size="18" />
   			<img id="img2"
						onclick="WdatePicker({el:'selectDate3',dateFmt:'yyyy-MM-dd',skin:'blue'})"
						src="./skin/datePicker.gif" width="16"
						height="22" align="absmiddle">
   		</td>
   		<td   bgcolor="#cccfff">yyyy-MM-dd  HH:mm:ss：</td>
   		<td>
   			<input id="selectDate4" name="endDate" type="text" size="18" />
   			<img id="img2"
						onclick="WdatePicker({el:'selectDate4',dateFmt:'yyyy-MM-dd  HH:mm:ss',skin:'blue'})"
						src="./skin/datePicker.gif" width="16"
						height="22" align="absmiddle">
   		</td>
   	</tr>
   		<tr>
   		<td  bgcolor="#cccfff"> yyyy-MM：</td>
   		<td>
   			<input id="selectDate5" name="endDate" type="text" size="18" />
   			<img id="img2"
						onclick="WdatePicker({el:'selectDate5',dateFmt:'yyyy-MM',skin:'blue'})"
						src="./skin/datePicker.gif" width="16"
						height="22" align="absmiddle">
   		</td>
   		<td   bgcolor="#cccfff">yyyy-MM  HH:mm：</td>
   		<td>
   			<input id="selectDate6" name="endDate" type="text" size="18" />
   			<img id="img2"
						onclick="WdatePicker({el:'selectDate6',dateFmt:'yyyy-MM  HH:mm',skin:'blue'})"
						src="./skin/datePicker.gif" width="16"
						height="22" align="absmiddle">
   		</td>
   	</tr>
   </table>
  </body>
</html>
