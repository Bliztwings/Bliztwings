<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

 
<%@ page import="javax.servlet.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>	
<%@ page import="java.io.File" %>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %>	 
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory" %>	
<%@ page import="org.apache.commons.fileupload.FileItem" %>	
<%@ page import="org.apache.commons.fileupload.FileItemIterator" %>
<%@ page import="org.apache.commons.fileupload.FileItemStream" %>
<%@ page import="org.apache.commons.fileupload.util.Streams" %>

<%

	try{  
	    if(ServletFileUpload.isMultipartContent(request))
	    {  
		      DiskFileItemFactory dff = new DiskFileItemFactory();  
		      dff.setRepository(new File("d://Image1"));
		      dff.setSizeThreshold(1024000); 
		      ServletFileUpload sfu = new ServletFileUpload(dff);
		      sfu.setFileSizeMax(5000000); //单个上传文件最大值  
		      sfu.setSizeMax(10000000);  //整个请求的大小最大值  
		      FileItemIterator fii = sfu.getItemIterator(request);
		      while(fii.hasNext()){  
		        FileItemStream fis = fii.next(); 
		        if(!fis.isFormField()){	        	
		           
		            String fileName = fis.getName();  //获取上传的文件名
		            System.out.println(fileName);
		            
		            Calendar calendar = Calendar.getInstance(); 
		            String savename = String.valueOf(calendar.getTimeInMillis());  
		            String imgPath_s_suffix = fileName.substring(fileName.lastIndexOf(".") , fileName.length());  //获取后缀
		            savename=savename  + imgPath_s_suffix;
	                        
		            BufferedInputStream in = new BufferedInputStream(fis.openStream());
		            BufferedOutputStream outs = new BufferedOutputStream(new FileOutputStream(new File("d:\\Image1\\"+ savename)));  
		            Streams.copy(in, outs, true); 
		        }  
		      }  
		      response.getWriter().println("File upload successfully!!!"); 
	    }
	}catch(Exception e){  
	    e.printStackTrace();  
	}  

%>
<!-- https://zhidao.baidu.com/question/41386120.html
 -->

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>接收拍照 图片</title>
</head>
<body>
	This is a demo!
</body>
</html>