package com.MyNotepad.java;

/*
 * 未用  备用
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;

public class MySql {

	// 得到数据库表数据
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector getRows() {
		String sql_url = "jdbc:mysql://localhost:3306/study?useSSL=false&useUnicode=true&amp;characterEncoding=utf-8"; // 数据库路径（一般都是这样写），study是数据库名称
		String name = "root"; // 用户名
		String password = "aa909090"; // 密码
		Connection conn;
		PreparedStatement preparedStatement = null;

		Vector rows = null;

		try {
			Class.forName("com.mysql.jdbc.Driver"); // 连接驱动
			conn = DriverManager.getConnection(sql_url, name, password); // 连接数据库
			// if(!conn.isClosed())
			// System.out.println("成功连接数据库");
			preparedStatement = conn.prepareStatement("select * from T_Students"); // 查找所有数据
			ResultSet result1 = preparedStatement.executeQuery();

			if (result1.wasNull()) // 空 不影响
				JOptionPane.showMessageDialog(null, "结果集中无记录");

			rows = new Vector(); // 当Vector或ArrayList中的元素超过它的初始大小时,Vector会将它的容量翻倍,而ArrayList只增加50%的大小，这样,ArrayList就有利于节约内存空间
									// 如果你需要高效的随即存取，而不在乎插入和删除的效率，使用vector

			ResultSetMetaData rsmd = result1.getMetaData(); // getMetaData包括了数据的字段名称、类型以及数目等表格所必须具备的信息。

			while (result1.next()) {
				rows.addElement(getNextRow(result1, rsmd));
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("未成功加载驱动。");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("未成功打开数据库。");
			e.printStackTrace();
		}
		return rows;
	}

	// 得到数据库表头
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector getHead() {
		String sql_url = "jdbc:mysql://localhost:3306/study?useSSL=false&useUnicode=true&amp;characterEncoding=utf-8"; // 数据库路径（一般都是这样写），test是数据库名称
		String name = "root"; // 用户名
		String password = "aa909090"; // 密码

		Connection conn;
		PreparedStatement preparedStatement = null;

		Vector columnHeads = null;

		try {
			Class.forName("com.mysql.jdbc.Driver"); // 连接驱动
			conn = DriverManager.getConnection(sql_url, name, password); // 连接数据库
			
			
			preparedStatement = conn.prepareStatement("select * from T_Students");
			ResultSet result1 = preparedStatement.executeQuery();

			boolean moreRecords = result1.next();
			if (!moreRecords)
				JOptionPane.showMessageDialog(null, "结果集中无记录");

			columnHeads = new Vector();
			ResultSetMetaData rsmd = result1.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++)
				columnHeads.addElement(rsmd.getColumnName(i)); // 方法说明：根据字段的索引值取得字段的名称。参数字段的索引值，从1开始。返回值：字段的名称（字符串）。

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("未成功加载驱动。");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("未成功打开数据库。");
			e.printStackTrace();
		}
		return columnHeads;
	}

	// 得到数据库中下一行数据
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Vector getNextRow(ResultSet rs, ResultSetMetaData rsmd) throws SQLException {
		Vector currentRow = new Vector();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) { // 一行个数 方法说明：返回所有字段的数目 返回值：所有字段的数目（整数）。
			currentRow.addElement(rs.getString(i)); // addElement(Object obj 把组件加到向量尾部，同时大小加1，向量容量比以前大1
		}
		return currentRow;
	}
}
