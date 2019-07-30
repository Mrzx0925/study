package com.zx.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.mysql.fabric.xmlrpc.base.Data;
import com.zx.utils.CreateExcel;
import com.zx.utils.LogWrite;

@Path("/download")
public class Download {

	@GET
	@Path("/{param}")
	public void printMessage(@PathParam("param") String msg,@Context HttpServletResponse response) throws IOException {
		String result = "Restful example : " + msg;
		SXSSFWorkbook wb = new SXSSFWorkbook();
		wb = CreateExcel.getworkbook();
		OutputStream output = response.getOutputStream();
		response.addHeader("Content-Disposition", "inline;filename="+msg+".xlsx");
		response.setContentType("application/msexcel");
		wb.write(output);	
		output.flush();
		output.close();
		//return Response.status(200).entity(result).build();
		
		//写入日志文件
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String data = df.format(new Date());// new Date()为获取当前系统时间
		String  content = data+":  导出用户信息";
		System.out.println(content);
		LogWrite.write(content);
	}
	
	@GET
	@Path("/msg")
	public void printmsg(@Context HttpServletResponse response) throws IOException {
		OutputStream output = response.getOutputStream();
		response.addHeader("Content-Disposition", "inline;filename=error.xlsx");
		response.setContentType("application/msexcel");
		SXSSFWorkbook erwb = new SXSSFWorkbook();
		erwb = CreateExcel.geterrorbook();
		erwb.write(output);	
		output.flush();
		output.close();
		
		//return Response.status(200).entity(result).build();
		
		//写入日志文件
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
				String data = df.format(new Date());// new Date()为获取当前系统时间
				String  content = data+":  导出错误信息";
				System.out.println(content);
				LogWrite.write(content);
		
	}

	
}
