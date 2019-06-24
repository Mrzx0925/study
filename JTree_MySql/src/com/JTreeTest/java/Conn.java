package com.JTreeTest.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JOptionPane;

public class Conn {
	private static Connection con;
	
	
	public static Connection getCon() {
		return con;
	}

	public static void setCon(Connection con) {
		Conn.con = con;
	}
	public Conn() {
		super();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector getRows(String sql_name, String table_name) throws SQLException, ClassNotFoundException {
		con = null;
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/" + sql_name
				+ "?useSSL=false&useUnicode=true&amp;characterEncoding=UTF-8";
		String user = "root";
		String password = "aa909090";
		Vector rows = null;
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = null;

			// if(!conn.isClosed())
			// System.out.println("成功连接数据库");
			preparedStatement = con.prepareStatement("select * from " + table_name); // 查找所有数据
			ResultSet result1 = preparedStatement.executeQuery();

			if (result1.wasNull()) // 空 不影响
				JOptionPane.showMessageDialog(null, "结果集中无记录");

			rows = new Vector(); // 当Vector或ArrayList中的元素超过它的初始大小时,Vector会将它的容量翻倍,而ArrayList只增加50%的大小，这样,ArrayList就有利于节约内存空间
									// 如果你需要高效的随即存取，而不在乎插入和删除的效率，使用vector

			ResultSetMetaData rsmd = result1.getMetaData(); // getMetaData包括了数据的字段名称、类型以及数目等表格所必须具备的信息。

			while (result1.next()) {
				rows.addElement(getNextRow(result1, rsmd));
			}
			
			System.out.println("测试数据.......................");
			for(int i = 0; i < rows.size();i++) {
				System.out.println(rows.size());
				System.out.println(rows.toString());
			}
		

		return rows;
	}

	@SuppressWarnings("unchecked")
	public static Vector getHead(String sql_name, String table_name) throws SQLException, ClassNotFoundException {
		con = null;
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/" + sql_name
				+ "?useSSL=false&useUnicode=true&amp;characterEncoding=UTF-8";
		String user = "root";
		String password = "aa909090";
		PreparedStatement preparedStatement = null;

		Vector columnHeads = null;
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			preparedStatement = con.prepareStatement("select * from " + table_name);
			ResultSet result1 = preparedStatement.executeQuery();

			boolean moreRecords = result1.next();
			if (!moreRecords)
				JOptionPane.showMessageDialog(null, "结果集中无记录");

			columnHeads = new Vector();
			ResultSetMetaData rsmd = result1.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) // 从1开始
				columnHeads.addElement(rsmd.getColumnName(i)); // 方法说明：根据字段的索引值取得字段的名称。参数字段的索引值，从1开始。返回值：字段的名称（字符串）。
		
		return columnHeads;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Vector getNextRow(ResultSet rs, ResultSetMetaData rsmd) throws SQLException {
		Vector currentRow = new Vector();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) { // 一行个数 方法说明：返回所有字段的数目 返回值：所有字段的数目（整数）。
			currentRow.addElement(rs.getString(i)); // addElement(Object obj 把组件加到向量尾部，同时大小加1，向量容量比以前大1
		}
		return currentRow;
	}

}
