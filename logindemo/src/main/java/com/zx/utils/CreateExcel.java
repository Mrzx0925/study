package com.zx.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.zx.entity.User;
import com.zx.hibernate.CRUD;

public class CreateExcel {
	public static SXSSFWorkbook getworkbook() {
		List<User> list = new ArrayList<User>();
		list = CRUD.QueryAll();
		// 创建一个Excel文件
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		// 创建一个工作表
		Sheet sheet = workbook.createSheet("信息表");
		// 添加表头行
		Row hssfRow = sheet.createRow(0);
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中 

		// 红色标志
		CellStyle redStyle = workbook.createCellStyle();
		redStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中 
		Font redFont = workbook.createFont();
		// 高度
		redFont.setFontHeightInPoints((short) 13);
		redStyle.setFont(redFont);
		
		// 颜色
		redFont.setColor(Font.COLOR_RED);
		// cellStyle.setAlignment(HSSFCellStyle.);

		//黄色
		CellStyle yellowStyle = workbook.createCellStyle();
		yellowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中 
		yellowStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
		yellowStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); //黄色背景
		Font yellowfont =  workbook.createFont();
		yellowfont.setFontHeightInPoints((short)  10);
		yellowStyle.setFont(yellowfont);
		// 添加表头内容
		Cell headCell = hssfRow.createCell(0);
		headCell.setCellValue("ID");
		headCell.setCellStyle(cellStyle);

		headCell = hssfRow.createCell(1);
		headCell.setCellValue("姓名");
		headCell.setCellStyle(cellStyle);

		headCell = hssfRow.createCell(2);
		headCell.setCellValue("年龄");
		headCell.setCellStyle(cellStyle);

		for (int i = 0; i < list.size(); i++) {
			hssfRow = sheet.createRow((int) i + 1);
			User user = list.get(i);
			// 创建单元格，并设置值
			Cell cellid = hssfRow.createCell(0);
		
			Cell cell = hssfRow.createCell(1);
			cell.setCellValue(user.getName());
			cell.setCellStyle(cellStyle);

			cell = hssfRow.createCell(2);
			cell.setCellValue(user.getAge());
			if(CRUD.isexist(user.getName(), user.getAge()) > 1){
				cellid.setCellValue(user.getId());
				cellid.setCellStyle(yellowStyle);
			}else {
				cellid.setCellValue(user.getId());
				cellid.setCellStyle(cellStyle);
			}
			if (user.getAge() > 50) {
				// 50岁以上的红色标注
				cell.setCellStyle(redStyle);
			} else {
				cell.setCellStyle(cellStyle);
			}
		}
		return workbook;
	}

	public static SXSSFWorkbook geterrorbook() {
		List<String> list = new ArrayList<String>();
		list = FileUpload.list;
		// 创建一个Excel文件
		SXSSFWorkbook errorbook = new SXSSFWorkbook();
		// 创建一个工作表
		Sheet sheet = errorbook.createSheet("信息表");
		// 添加表头行
		int width = 98;
		sheet.setColumnWidth(1, 256*width+184); //公式
		sheet.setColumnWidth(0, 256*40+184); //公式
		Row hssfRow = sheet.createRow(0);
		
		CellStyle cellStyle = errorbook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中 
		
		//黄色
		CellStyle yellowStyle = errorbook.createCellStyle();
		yellowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中 
		yellowStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
		yellowStyle.setFillPattern(CellStyle.SOLID_FOREGROUND); //黄色背景
		Font yellowfont =  errorbook.createFont();
		yellowfont.setFontHeightInPoints((short)  10);
		yellowStyle.setFont(yellowfont);
		// 红色标志
		CellStyle redStyle = errorbook.createCellStyle();
		redStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中 
		Font redFont = errorbook.createFont();
		// 高度
		redFont.setFontHeightInPoints((short) 12);
		redStyle.setFont(redFont);

		// 颜色
		redFont.setColor(Font.COLOR_RED);
		// cellStyle.setAlignment(HSSFCellStyle.);

		// 添加表头内容
		Cell headCell = hssfRow.createCell(0);
		headCell.setCellValue("行数");
		headCell.setCellStyle(cellStyle);
		headCell = hssfRow.createCell(1);
		headCell.setCellValue("错误信息");
		headCell.setCellStyle(cellStyle);

		for (int i = 0; i < list.size(); i++) {
			hssfRow = sheet.createRow((int) i + 1);
			String[] msg = list.get(i).split("：");
			// 创建单元格，并设置值
			String existmsg = list.get(i).substring(0, 2);
			
			Cell cell = hssfRow.createCell(0);
			cell.setCellValue(msg[0]);
			cell.setCellStyle(cellStyle);
			cell = hssfRow.createCell(1);
			cell.setCellValue(msg[1]);
			if(msg[1].equals("数据格式错误")) {
				cell.setCellStyle(redStyle);
			}else if(existmsg.equals("提醒")) {
				cell.setCellStyle(yellowStyle);
			}
			else {
				cell.setCellStyle(cellStyle);
			}
		}
		return errorbook;
	}
}
