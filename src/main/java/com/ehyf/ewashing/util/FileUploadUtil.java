/*
 * Copyright 2000-2020 YGSoft.Inc All Rights Reserved.
 */
package com.ehyf.ewashing.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 文件上传工具类.
 * 
 * @author suntao
 * 
 */
public class FileUploadUtil {

	private static final Log log = LogFactory.getLog(FileUploadUtil.class);
	
	/** 私有化. */
	private FileUploadUtil() {
	}

	/**
	 * 文件条目.
	 * 
	 * @author suntao
	 * 
	 */
	public static class UploadFileItem implements Serializable {

		/** serialVersionUID. */
		private static final long serialVersionUID = 1L;
		/**
		 * 文件内部标志值.
		 */
		private String fileId;
		/**
		 * 上传时的文件名称，即客户端文件名称.
		 */
		private String fileName;
		/**
		 * 文件大小.
		 */
		private long fileSize;
		/**
		 * 内部文件名称，即服务器磁盘目录的真实文件名称.
		 */
		private String internalFileName;
		/**
		 * 文件类型
		 */
		private String fileType;

		/**
		 * 文件内部标志值.
		 * 
		 * @return 文件内部标志值.
		 */
		public String getFileId() {
			return fileId;
		}

		/**
		 * 上传时的文件名称，即客户端文件名称.
		 * 
		 * @return 上传时的文件名称，即客户端文件名称
		 */
		public String getFileName() {
			return fileName;
		}
		/**
		 * 文件类型
		 * @return 文件类型
		 */
		public String getFileType() {
			return fileType;
		}

		/**
		 * 文件大小.
		 * 
		 * @return 文件大小.
		 */
		public long getFileSize() {
			return fileSize;
		}

		/**
		 * 内部文件名称，即服务器磁盘目录的真实文件名称.
		 * 
		 * @return 内部文件名称，即服务器磁盘目录的真实文件名称.
		 */
		public String getInternalFileName() {
			return internalFileName;
		}

		/**
		 * 内部文件，即服务器磁盘目录的真实文件.
		 * 
		 * @return 内部文件，即服务器磁盘目录的真实文件.
		 */
		public File getInternalFile() {
			return new File(internalFileName);
		}

	}

	/**
	 * 缓存的临时目录.
	 */
	private static String tempPath = null;
	/**
	 * 文件描述名称.
	 */
	private static final String DESC_FILE_NAME = "uploadfiles.bin";

	/**
	 * 获取java临时目录.
	 * 
	 * @return 临时目录.
	 */
	public static String getJavaTempPath() {
		if (tempPath != null) {
			return tempPath;
		}

		String ret = System.getProperty("java.io.tmpdir");
		if (ret == null || ret.length() == 0) {
			try {
				ret = File.createTempFile("test", "test").getAbsoluteFile().getParent();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		if (ret == null || ret.length() == 0) {
			ret = new File(".").getAbsoluteFile().getParent();
		}
		if (!ret.endsWith(File.separator)) {
			ret = ret + File.separator;
		}
		tempPath = ret + "yg.smartfast.upload" + File.separator;
		return tempPath;
	}

	/**
	 * 获取一个token, 后续根据这个token 来定位文件(创建临时文件夹时使用).
	 * 
	 * @param request
	 *            HTTP请求
	 * @return 用于创建临时文件夹的token
	 */
	public static String getToken(final HttpServletRequest request) {
		String token;
		if (request.getSession(false) != null) {
			token = request.getSession(false).getId();
		} else {
			token = request.getContextPath();
		}
		if (token == null || token.trim().length() == 0) {
			token = "nosession";
		}
		token = token.trim().replaceAll("/", "_").replaceAll("\\\\", "_").replaceAll(",", "_")
				.replaceAll(";", "_");
		return token;
	}

	/**
	 * 获取工作区目录.
	 * 
	 * @param token
	 *            用于创建临时文件夹的token
	 * @return 工作区目录.
	 */
	private static String getWorkspacePath(final String token) {
		return getJavaTempPath() + token + File.separator;

	}

	/**
	 * 保存文件至临时文件空间中.
	 * 
	 * @param request
	 *            HTTP请求
	 * 
	 * @return 上传信息.
	 */
	public static UploadFileItem[] saveUploadFile(final HttpServletRequest request) {
		final List<FileItem> fileItems = parseRequest(request);
		 
		final String path = ""; // request.getParameter("path");
		final String isAppend = "true"; //request.getParameter("isAppend");

		final String token = getToken(request);

		final File folder = getFolder(token, path, Boolean.parseBoolean(isAppend));

		final List<UploadFileItem> retList = saveFiles(folder, fileItems);

		final UploadFileItem[] retArray = retList.toArray(new UploadFileItem[retList.size()]);
		// 保存上传列表至文件
//		saveUploadFileDesc(token, path, retList);

		return retArray;
	}

	/**
	 * 获取上传文件保存的文件夹, 如果不存在, 则进行创建.
	 * 
	 * @param token
	 *            用于创建临时文件夹的token
	 * @param path
	 *            路径信息
	 * @param isAppend
	 *            是否添加模式, 如果为否, 则会删除原来已经上传的文件
	 * @return 上传文件保存的文件夹
	 */
	private static File getFolder(final String token, final String path, final boolean isAppend) {
		final File workspaceFile = new File(getWorkspacePath(token));
		if (!workspaceFile.exists()) {
			workspaceFile.mkdirs();
		}
		if (!workspaceFile.exists()) {
			throw new RuntimeException("未正确创建临时会话目录");
		}
		final File folder = new File(workspaceFile, path);
		if (!isAppend && folder.exists()) {
			deleteAllFiles(folder);
		}
		if (!folder.exists()) {
			folder.mkdirs();
		}
		return folder;
	}

	/**
	 * Processes multipart/form-data stream.
	 * 
	 * @param request
	 *            The servlet request to be parsed
	 * @return A list of FileItem instances parsed from the request, in the order that they were transmitted.
	 * @see ServletFileUpload#parseRequest(HttpServletRequest)
	 */
	@SuppressWarnings("unchecked")
	private static List<FileItem> parseRequest(final HttpServletRequest request) {
		final DiskFileItemFactory fac = new DiskFileItemFactory();
		final ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");

		try {
			return upload.parseRequest(request);
		} catch (FileUploadException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 保存上传的文件列表, 并生成对应的文件信息列表.
	 * 
	 * @param folder
	 *            文件保存的文件夹
	 * @param fileItems
	 *            上传的文件列表
	 * 
	 * @return 文件信息列表
	 */
	private static List<UploadFileItem> saveFiles(final File folder, final List<FileItem> fileItems) {
		final List<UploadFileItem> retList = new ArrayList<UploadFileItem>();
	
		final Iterator<FileItem> it = fileItems.iterator();
		while (it.hasNext()) {
			final FileItem item = it.next();
			if (item.isFormField()) {
				continue;
			}
			final UploadFileItem fileInfo = saveItem(folder, item);
			if (fileInfo != null) {
				retList.add(fileInfo);
			}
		}
		return retList;
	}

	/**
	 * 保存上传的文件, 并生成对象记录文件信息.
	 * 
	 * @param folder
	 *            文件保存的文件夹
	 * @param item
	 *            上传的文件
	 * 
	 * @return 文件信息, 如果不能保存, 则返回null
	 */
	private static UploadFileItem saveItem(final File folder, final FileItem item) {
		final String name = item.getName();
		// final String type = item.getContentType();
		if (name == null || name.trim().length() == 0) {
			return null;
		}

		final String ext = getExt(name);
		File tmpFile;
		String fileId;
		do {
			fileId = UUID.randomUUID().toString() + ext;
			tmpFile = new File(folder, fileId);
		} while (tmpFile.exists());

		try {
 			item.write(tmpFile);
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new RuntimeException(e);
			}
		}

		final UploadFileItem uploadFile = new UploadFileItem();
		uploadFile.fileId = fileId;
		uploadFile.fileName = name;
		uploadFile.fileSize = item.getSize(); 
		String [] contenType = name.split("[.]");
		uploadFile.fileType = contenType[contenType.length -1];
		uploadFile.internalFileName = tmpFile.getAbsolutePath();
		return uploadFile;
	} 
	/**
	 * 获取文件的扩展名, 取最后一个"." 及之后的字符
	 * 
	 * @param name
	 *            文件名
	 * @return 文件扩展名
	 */
	private static String getExt(String name) {
		final int index = name.lastIndexOf(".");
		if (index > 0) {
			return name.substring(index);
		}
		return "";
	}

	/**
	 * 保存上传列表至文件.
	 * 
	 * @param token
	 *            用于创建临时文件夹的token
	 * @param path
	 *            路径信息
	 * @param uploadFiles
	 *            上传的文件列表
	 */
	private static void saveUploadFileDesc(final String token, final String path,
			final List<UploadFileItem> uploadFiles) {
		if (uploadFiles.size() == 0) {
			return;
		}
		// 读取以前上传的文件.
		final UploadFileItem[] ufiList = getUploadFile(token, path);
		if (ufiList.length > 0) {
			uploadFiles.addAll(0, Arrays.asList(ufiList));
		}
		resetUploadFileDesc(token, path, uploadFiles.toArray(new UploadFileItem[uploadFiles.size()]));
	}

	/**
	 * 获取上传文件列表.
	 * 
	 * @param token
	 *            用于创建临时文件夹的token
	 * @param path
	 *            目录.
	 * 
	 * @return 条目列表.
	 */
	public static UploadFileItem[] getUploadFile(final String token, final String path) {
		final File descFile = new File(getWorkspacePath(token) + File.separator + path + File.separator
				+ DESC_FILE_NAME);
		if (!descFile.exists()) {
			return new UploadFileItem[0];
		}
		try {
			final FileInputStream fis = new FileInputStream(descFile);
			if (fis.available() < 1) {
				fis.close();
				return new UploadFileItem[0];
			}
			final ObjectInputStream ois = new ObjectInputStream(fis);
			try {
				return (UploadFileItem[]) ois.readObject();
			} finally {
				ois.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取上传文件.
	 * 
	 * @param token
	 *            用于创建临时文件夹的token
	 * @param path
	 *            目录.
	 * @param fileId
	 *            文件ID.
	 * 
	 * @return 条目列表.
	 */
	public static UploadFileItem getUploadFile(final String token, final String path, final String fileId) {
		final UploadFileItem[] files = getUploadFile(token, path);
		for (UploadFileItem file : files) {
			if (file.fileId.equals(fileId)) {
				return file;
			}
		}
		return null;
	}

	/**
	 * 获取上传文件.
	 * 
	 * @param token
	 *            用于创建临时文件夹的token
	 * @param path
	 *            目录.
	 * @param fileIndex
	 *            文件索引，从0计数.
	 * 
	 * @return 条目列表.
	 */
	public static UploadFileItem getUploadFile(final String token, final String path, final int fileIndex) {
		final UploadFileItem[] files = getUploadFile(token, path);
		if (fileIndex > -1 && fileIndex < files.length) {
			return files[fileIndex];
		}
		return null;
	}

	/**
	 * 删除指定的文件.
	 * 
	 * @param token
	 *            用于创建临时文件夹的token
	 * @param path
	 *            目录
	 * @param fileId
	 *            文件ID
	 */
	public static void deleteUploadFile(final String token, final String path, final String fileId) {
		final UploadFileItem[] files = getUploadFile(token, path);
		final List<UploadFileItem> list = new ArrayList<UploadFileItem>(files.length);
		for (UploadFileItem file : files) {
			if (file.fileId.equals(fileId)) {
				file.getInternalFile().delete();
			} else {
				list.add(file);
			}
		}
		if (list.size() == 0) {
			clearUploadFiles(token, path);
		} else if (list.size() < files.length) {
			resetUploadFileDesc(token, path, list.toArray(new UploadFileItem[list.size()]));
		}
	}

	/**
	 * 删除指定的文件.
	 * 
	 * @param token
	 *            用于创建临时文件夹的token
	 * @param path
	 *            目录
	 * @param fileIndex
	 *            文件索引，从0计数.
	 */
	public static void deleteUploadFile(final String token, final String path, final int fileIndex) {
		final UploadFileItem[] files = getUploadFile(token, path);
		final List<UploadFileItem> list = new ArrayList<UploadFileItem>(files.length);
		for (int i = 0; i < files.length; i++) {
			if (i == fileIndex) {
				files[i].getInternalFile().delete();
			} else {
				list.add(files[i]);
			}
		}
		if (list.size() == 0) {
			clearUploadFiles(token, path);
		} else if (list.size() < files.length) {
			resetUploadFileDesc(token, path, list.toArray(new UploadFileItem[list.size()]));
		}
	}

	/**
	 * 清除临时文件空间中的文件.
	 * 
	 * @param token
	 *            用于创建临时文件夹的token
	 * @param path
	 *            目录.
	 */
	public static void clearUploadFiles(final String token, final String path) {
		final File workspaceFile = new File(getWorkspacePath(token));
		if (!workspaceFile.exists()) {
			return;
		}
		final File folder = new File(workspaceFile, path);
		deleteAllFiles(folder);
	}

	/**
	 * 删除指定目录.
	 * 
	 * @param folder
	 *            文件目录
	 */
	private static void deleteAllFiles(final File folder) {
		if (!folder.exists()) {
			return;
		}

		for (File tmpFile : folder.listFiles()) {
			tmpFile.delete();
		}
		folder.delete();
	}

	/**
	 * 重置上传文件描述文件.
	 * 
	 * @param token
	 *            用于创建临时文件夹的token
	 * @param path
	 *            目录
	 * @param uploadFiles
	 *            上传列表
	 */
	private static void resetUploadFileDesc(final String token, final String path,
			final UploadFileItem[] uploadFiles) {
		try {
			final File descFile = new File(getWorkspacePath(token) + File.separator + path + File.separator
					+ DESC_FILE_NAME);
			if (descFile.exists()) {
				descFile.delete();
			}
			descFile.createNewFile();
			final FileOutputStream fos = new FileOutputStream(descFile, false);
			final ObjectOutputStream oos = new ObjectOutputStream(fos);
			try {
				oos.writeObject(uploadFiles);
			} finally {
				oos.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 保存文件到临时目录
	 * 
	 * @param fileName
	 * @param in
	 */
	public static String saveTmpFile(String fileName, InputStream in) {
		String fullTmpFilePath = null;
		FileOutputStream out = null;
		try {
			fullTmpFilePath = FileUploadUtil.getJavaTempPath() + fileName;
			log.debug("save file to : "  + fullTmpFilePath);
	        // 读取附件字节并存储到文件中  
	        out = new FileOutputStream(fullTmpFilePath);  
	        int data;
	        while((data = in.read()) != -1) {  
	            out.write(data);  
	        }  
		} catch (Exception e) {
			log.warn(e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				log.error(e);
			}
		}
		return fullTmpFilePath;
	}
}
