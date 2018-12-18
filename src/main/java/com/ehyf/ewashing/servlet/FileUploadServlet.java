package com.ehyf.ewashing.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ehyf.ewashing.util.FileUploadUtil;
import com.ehyf.ewashing.util.FileUploadUtil.UploadFileItem;


/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet("/fileUploadServlet")
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileUploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final UploadFileItem[] uploadFiles = FileUploadUtil.saveUploadFile(request);
		
		if (uploadFiles.length >= 1) {
			for (int t = 0; t < uploadFiles.length; t++) {
				File file = uploadFiles[t].getInternalFile();

				FileInputStream fis = new FileInputStream(file);
			}
		}
		
	}

}
