package com.ehyf.ewashing.wechat.model;

import java.io.InputStream;

/**
 * @author shenxiaohong
 *
 */
public class WeixinFileModel {

	private String fileName;

	private String fileType;

	private InputStream fileInputStream;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

}
