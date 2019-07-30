package com.zx.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.mysql.fabric.xmlrpc.base.Data;
import com.zx.utils.CreateExcel;
import com.zx.utils.FileUpload;
import com.zx.utils.LogWrite;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Path("upload")
public class Upload {
	@POST
	public void printMessage(@Context HttpServletRequest request, @Context HttpServletResponse response)
			throws IOException, InvalidFormatException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		PrintWriter out = response.getWriter();
		String filepath = FileUpload.getfilepath(request);
		System.out.println(filepath);
		List<String> msg = new ArrayList<String>();
		msg = FileUpload.ReadExcel(filepath);
		out.print(msg);
		out.close();
		File file = new File(filepath);
		file.delete(); // 用完删除，不留痕迹
		
		
		//写入日志文件
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String data = df.format(new Date());// new Date()为获取当前系统时间
		String  content = data+":  上传Excel文件";
		System.out.println(content);
		LogWrite.write(content);
	}
}
