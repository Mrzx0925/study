package com.zx.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.zx.entity.User;
import com.zx.hibernate.CRUD;

public class FileUpload {
	static List<String> list = new ArrayList<String>();//存放返回信息
	static int errorflag = 0; //判断连续出错
	public static String getfilepath(HttpServletRequest request) throws UnsupportedEncodingException {
		System.out.println("zx111");
		String filepath = "";
		request.setCharacterEncoding("UTF-8");
		// String type=request.getParameter("type");
		try {
			// 配置上传参数
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 解析请求的内容提取文件数据
			@SuppressWarnings("unchecked")
			List<FileItem> formItems = upload.parseRequest(request);

			// 迭代表单数据
			for (FileItem item : formItems) {
				// 处理不在表单中的字段
				if (!item.isFormField()) {
					String fileName = item.getName(); // 获取上传的文件名
					String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
					// 定义上传文件的存放路径
					String path = "C:\\TempExcel";
					File pfile = new File(path);
					if (!pfile.exists()) {
						pfile.mkdir();
					}

					// 定义上传文件的完整路径
					File tmp = new File(fileName);
					// fileName = tmp.getName();
					// fileName = fileName.substring(0, fileName.lastIndexOf("."));
					fileName = "";
					fileName += new SimpleDateFormat("yyMMddhhmmss").format(new Date()).toString();
					fileName += "." + fileType; // 将文件名字改为时间，避免重名文件
					filepath = String.format("%s\\%s", path, fileName);
					File storeFile = new File(filepath);
					// 在控制台输出文件的上传路径
					System.out.println(path);
					System.out.println(filepath);
					// 保存文件到硬盘
					item.write(storeFile);

				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filepath;
	}

	public static List<String> ReadExcel(String filepath) throws InvalidFormatException, IOException {
		list.removeAll(list);
		InputStream inputStream = new FileInputStream(filepath);
		// InputStream inp = new FileInputStream("C:/Users/H__D/Desktop/workbook.xls");

		Workbook workbook = WorkbookFactory.create(inputStream);
		Sheet sheet = workbook.getSheetAt(0);
		DataFormatter formatter = new DataFormatter();
		// int flag = 0;
		// System.out.println(length);
		int head[] = new int[2];
		head[0] = -1;
		head[1] = -1;
		for (Row row : sheet) {
			int length = row.getLastCellNum();
			int rownum = row.getRowNum()+1;
			System.out.println("进入判断  " + length);
			if (length < 2) {
				System.out.println("格式都不对，少于两列");
				list.add("第 "+rownum+" 行：少于两列");
				errorflag +=1;
				//return "少于两列";
			} else if (length >= 2 && length < 100) {// 两列
				if (row.getRowNum() == 0) {
					head = SaveMySql(formatter, row, head);
				} else {
					if (head[0] != -1 && head[1] != -1) {
						SaveMySql(formatter, row, head);
					} else {
						head[0] = 0;
						head[1] = 1;
						SaveMySql(formatter, row, head);
					}
				}

			} else {
				if (head[0] != -1 && head[1] != -1) {
					SaveMySql(formatter, row, head);
				}
				else {
					list.add("第 "+rownum+" 行：列太长");
					errorflag +=1;
				}
			}
			if(errorflag == 5) {
				list.add("第 "+rownum+" 行：连续出错五行，直接退出");
				errorflag = 0;
				break;
			}
		}
		return list;
	}

	public static int[] SaveMySql(DataFormatter formatter, Row row, int[] headnum) {
		// headnum[0] = -1;
		// headnum[1] = -1;
		//String returnflag = "";
		int flag = 0;
		String name = "";
		String age = "首先不合格";//对空的限制
		List<String> head = new ArrayList<String>();
		User user = new User();
		int rownum = row.getRowNum()+1;
		System.out.println("第一次进来" + rownum);
		if (rownum == 1) {
			int nameconfirm = 0; // 引入目的为 多行标题时 只读取前面两个
			int ageconfirm = 0; // 引入目的为 多行标题时 只读取前面两个
			for (Cell cell : row) {
				String text = formatter.formatCellValue(cell);
				head.add(text);
				if ("name".equals(text.toLowerCase()) || "姓名".equals(text)) {
					if (nameconfirm == 0) {
						headnum[0] = cell.getColumnIndex();
						nameconfirm = 100;
					}
				} else if ("age".equals(text.toLowerCase()) || "年龄".equals(text)) {
					if (ageconfirm == 0) {
						headnum[1] = cell.getColumnIndex();
						ageconfirm = 100;
					}
				}
			}
			if (headnum[0] != -1 && headnum[1] != -1) {
				flag = 1;
			} else {
				name = head.get(0);
				age = head.get(1);
			}
		} else {
			for (Cell cell : row) {
				String text = formatter.formatCellValue(cell);
				if (cell.getColumnIndex() == headnum[0]) {
					name = text;
				}
				if (cell.getColumnIndex() == headnum[1]) {
					age = text;
				}
			}
		}
		if (judgeName(name) && judgeAge(age)) {
			user.setName(name);
			user.setAge(Integer.parseInt(age));
			flag = 0;
		} else {
			flag = 1;
			if(rownum == 1 &&  headnum[0] != -1 && headnum[1] != -1) {
				
			}else {
				list.add("第 "+rownum+" 行：数据格式错误");
				errorflag +=1;
			}
		}
		if (flag != 1) {
			int exist = CRUD.isexist(user.getName(), user.getAge());
			if( exist > 0) {
				list.add("提醒	第"+rownum+" 行：已经有 "+exist+"条数据");
			}
			CRUD.AddUser(user);
			errorflag  = 0;
		}
		return headnum;

	}

	// 多行按照两行处理
	public static String OtherColumn(Sheet sheet, DataFormatter formatter) {

		return "";
	}

	public void save() {
		// 获取值并自己格式化
		/*
		 * switch (cell.getCellType()) { case Cell.CELL_TYPE_STRING:// 字符串型
		 * System.out.println(cell.getRichStringCellValue().getString()); break; case
		 * Cell.CELL_TYPE_NUMERIC:// 数值型 if (DateUtil.isCellDateFormatted(cell)) { //
		 * 如果是date类型则 ，获取该cell的date值 System.out.println(cell.getDateCellValue()); } else
		 * {// 纯数字 System.out.println(cell.getNumericCellValue()); } break; case
		 * Cell.CELL_TYPE_BOOLEAN:// 布尔 System.out.println(cell.getBooleanCellValue());
		 * break; case Cell.CELL_TYPE_FORMULA:// 公式型
		 * System.out.println(cell.getCellFormula()); break; case
		 * Cell.CELL_TYPE_BLANK:// 空值 System.out.println(); break; case
		 * Cell.CELL_TYPE_ERROR: // 故障 System.out.println(); break; default:
		 * System.out.println(); }
		 */
	}

	public static boolean judgeAge(String str) {
		boolean flag = true;
		try {
			int age = Integer.parseInt(str);
			if(age < 0 || age >100) {
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	public static boolean judgeName(String str) {
		boolean flag = true;
		if(isSpecialChar(str)) {
			flag = false;
		}else if(str.length() > 8 || str.length() == 0) {
			flag = false;
		}
		
		return flag;
	}
	
	  /**
     * 判断是否含有特殊字符
     *
     * @param str
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialChar(String str) {
    	boolean flag = false;
       // String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        String regEx = "[ _.`~!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t"; //字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if(m.find()) {
        	flag = true;
        	return flag;
        }
       
        p = Pattern.compile(".*\\d+.*"); //是否包含数字
        m = p.matcher(str);
        if (m.matches()) {
            flag = true;
            return flag;
        }
        return flag;
    }

}
