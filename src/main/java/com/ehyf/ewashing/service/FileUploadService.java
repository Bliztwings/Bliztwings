package com.ehyf.ewashing.service;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ehyf.ewashing.util.PropertiesUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

@Service
public class FileUploadService {

	private static PropertiesUtil propertiesUtil= new PropertiesUtil("config.properties");

	
	private static Logger logger =Logger.getLogger(FileUploadService.class);
	/**
	 * 上传文件
	 * @param localFilePath 文件路径
	 * @param fileName 文件名
	 * @return
	 */
	public String uploadFile(String localFilePath,String fileName){
		
		//构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone2());
		//...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		//...生成上传凭证，然后准备上传
		String accessKey = propertiesUtil.getProperty("accessKey");
		String secretKey = propertiesUtil.getProperty("secretKey");
		String bucket = propertiesUtil.getProperty("bucket");
		String domain = propertiesUtil.getProperty("domain");
		//如果是Windows情况下，格式是 D:\\qiniu\\test.png
		//默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = fileName;
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);
		try {
		    Response response = uploadManager.put(localFilePath, key, upToken);
		    //解析上传成功的结果
		    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
		    return "http://"+domain+"/"+putRet.key;
		} catch (QiniuException ex) {
		    Response r = ex.response;
		    logger.error(r.toString());
		    try {
		    	logger.error(r.bodyString());
		    } catch (QiniuException ex2) {
		    	logger.error(ex2.getMessage());
		    }
		}
		return null;
	}
	
	
	public String uploadFile(InputStream stream,String fileName){
		
		//构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone2());
		//...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		//...生成上传凭证，然后准备上传
		String accessKey = propertiesUtil.getProperty("accessKey");
		String secretKey = propertiesUtil.getProperty("secretKey");
		String bucket = propertiesUtil.getProperty("bucket");
		String domain = propertiesUtil.getProperty("domain");
		//如果是Windows情况下，格式是 D:\\qiniu\\test.png
		//默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = fileName;
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);
		try {
		    Response response = uploadManager.put(stream, key, upToken, null, null);
		    //解析上传成功的结果
		    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
		    return "http://"+domain+"/"+putRet.key;
		} catch (QiniuException ex) {
		    Response r = ex.response;
		    logger.error(r.toString());
		    try {
		    	logger.error(r.bodyString());
		    } catch (QiniuException ex2) {
		    	logger.error(ex2.getMessage());
		    }
		}
		return null;
	}
}
