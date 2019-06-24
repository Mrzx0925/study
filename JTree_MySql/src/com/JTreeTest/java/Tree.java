package com.JTreeTest.java;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class Tree extends JFrame implements ActionListener, MouseListener {

	private static int flag = 0;

	String[][] value_save; // 用来储存数据库原始内容，防止保存时删除了而重新插入错误后数据库数据无法恢复

	DefaultMutableTreeNode node;
	int col_number; // 表格列数
	int row_number; // 表格行数
	Vector rowData; // 数据
	Vector columnNames; // 表名
	DefaultTableModel tableModel; // public DefaultTableModel(Vector data，Vector columnNames)
	JTable table;
	JScrollPane s;
	JTree tree;
	JPopupMenu popMenu1; // 菜单
	JPopupMenu popMenu2;
	JPopupMenu popMenu3;
	JPopupMenu popMenu4;

	String sql_name;
	String table_name;
	JMenuItem item1 = new JMenuItem("打开数据库界面"); // 各个菜单项
	JMenuItem item2 = new JMenuItem("增加信息");
	JMenuItem item3 = new JMenuItem("查找");
	JMenuItem item4 = new JMenuItem("删除");
	JMenuItem item5 = new JMenuItem("保存");
	JMenuItem item6 = new JMenuItem("退出数据库表格界面");
	JMenuItem item7 = new JMenuItem("添加表格");
	JMenuItem item8 = new JMenuItem("删除表格");
	JMenuItem item9 = new JMenuItem("删除数据库");
	JMenuItem item10 = new JMenuItem("添加数据库");

	JFrame frame;

	public Tree() {
		// try {
		// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//Windows风格
		// } catch (ClassNotFoundException | InstantiationException |
		// IllegalAccessException
		// | UnsupportedLookAndFeelException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		this.setIconImage(Toolkit.getDefaultToolkit().createImage("月光.png")); // 设置窗口图片
		frame = this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 876, 589);
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("所有库");
		createNodes(node);
		tree = new JTree(node);
		tree.addMouseListener(this);

		tree.setCellEditor(new DefaultTreeCellEditor(tree, new DefaultTreeCellRenderer()));

		s = new JScrollPane(tree);
		this.add(s, BorderLayout.WEST);
		popMenu1 = new JPopupMenu();
		popMenu2 = new JPopupMenu();
		popMenu3 = new JPopupMenu();
		popMenu4 = new JPopupMenu();
		popMenu1.add(item1);
		popMenu2.add(item2);
		popMenu2.add(item3);
		popMenu2.add(item4);
		popMenu2.add(item5);
		popMenu2.add(item6);
		popMenu3.add(item7);
		popMenu1.add(item8);
		popMenu3.add(item9);
		popMenu4.add(item10);

		item1.addActionListener(this);
		item2.addActionListener(this);
		item3.addActionListener(this);
		item4.addActionListener(this);
		item5.addActionListener(this);
		item6.addActionListener(this);
		item7.addActionListener(this);
		item8.addActionListener(this);
		item9.addActionListener(this);
		item10.addActionListener(this);
		table = new JTable(tableModel);
		s = new JScrollPane(table);
	}

	public void mouseClicked(MouseEvent e) {
		TreePath path = tree.getPathForLocation(e.getX(), e.getY()); // 关键是这个方法的使用

		if (path == null) { // JTree上没有任何项被选中
			return;
		} else if (path.toString().equals("[所有库]") || path.getParentPath().toString().equals("[所有库]")) {
			return;
		} else if (path.getParentPath().getParentPath().toString().equals("[所有库]")) {
			System.out.println(flag);
			if (e.getClickCount() == 2) {
				open();
			}
		}
		tree.setSelectionPath(path);

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

		TreePath path = tree.getPathForLocation(e.getX(), e.getY()); // 关键是这个方法的使用

		if (path == null) { // JTree上没有任何项被选中

			return;
		} else if (path.toString().equals("[所有库]")) {
			if (e.getButton() == 3) {
				popMenu4.show(tree, e.getX(), e.getY());
			}
		} else if (path.getParentPath().toString().equals("[所有库]")) {
			if (e.getButton() == 3) {
				popMenu3.show(tree, e.getX(), e.getY());

			}

		} else {

			if (e.getButton() == 3) {

				popMenu1.show(tree, e.getX(), e.getY());

			}
		}
		tree.setSelectionPath(path);

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void actionPerformed(ActionEvent e) {

		node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent(); // 获得右键选中的节点
		System.out.println(node);

		if (node.toString().equals("所有库")) {

		} else {

			sql_name = node.getParent().toString();
			table_name = node.toString();
		}
		Sql_Create.setSql_name(node.toString());
		Sql_Create.setNode(node);
		Sql_Create.setTree(tree);
		if (e.getSource() == item1) {// 打开界面
			open();
		} else if (e.getSource() == item2) {// 增加信息
			tableModel.addRow(new Vector());
		} else if (e.getSource() == item3) {// 查找信息
			find();
		} else if (e.getSource() == item4) {// 删除
			int rowcount = table.getSelectedRow();
			if (rowcount >= 0) {
				tableModel.removeRow(rowcount);
			}
		} else if (e.getSource() == item5) {// 保存
			save();
		} else if (e.getSource() == item6) {// 退出表格界面
			s.setVisible(false);
			frame.remove(s);
			sql_name = null;
			table_name = null;
		} else if (e.getSource() == item7) {// 增加表格
			new Sql_Create();
			// addTable();

		} else if (e.getSource() == item8) {// 删除表格
			delete();

		} else if (e.getSource() == item9) {
			JOptionPane.showMessageDialog(null, "太过危险，已经注释", "消息", 1);
			// int result = JOptionPane.showConfirmDialog(null, "确定删除？", "删除确认",
			// JOptionPane.YES_NO_OPTION);
			// System.out.println(result);
			// if (table_name.equals("mysql") || table_name.equals("information_schema")
			// || table_name.equals("performance_schema") || table_name.equals("study")
			// || table_name.equals("sys")) {
			// JOptionPane.showMessageDialog(null, "抱歉，此数据库真的无法删除", "警告",
			// JOptionPane.WARNING_MESSAGE);
			// result = -1;
			// }
			//
			// if (result == 0) {
			// try {
			// String driver = "com.mysql.jdbc.Driver";
			// String url =
			// "jdbc:mysql://localhost:3306/mysql?useSSL=false&useUnicode=true&amp;characterEncoding=UTF-8";
			// String user = "root";
			// String password = "aa909090";
			// Class.forName(driver);
			// Connection con = DriverManager.getConnection(url, user, password);
			// PreparedStatement preparedStatement = null;
			//
			// preparedStatement = con.prepareStatement("drop database " + table_name);
			// preparedStatement.executeUpdate();
			//
			// if (node.isRoot()) {
			// return;
			// }
			//
			// ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
			// } catch (Exception e1) {
			// e1.printStackTrace();
			// }

			// }
		} else if (e.getSource() == item10) { // 添加数据库
			addSql();
		}
	}

	public void createNodes(DefaultMutableTreeNode parent) {
		DefaultMutableTreeNode temp = null;
		String strsql = "show databases";
		// 光标的相对位置
		ResultSet rs;
		java.sql.Statement statement = null;
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/mysql?useSSL=false&useUnicode=true&characterEncoding=UTF-8", "root",
					"aa909090");
			statement = connection.createStatement();
			rs = statement.executeQuery(strsql);
			while (rs.next()) {
				String name = rs.getString(1);
				temp = new DefaultMutableTreeNode(name);
				parent.add(temp);
				createNodes1(temp, name);
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	void createNodes1(DefaultMutableTreeNode parent, String tab) {
		DefaultMutableTreeNode temp = null;
		String strsql = null;
		// 光标的相对位置
		ResultSet rs;
		java.sql.Statement statement = null;
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/" + tab + "?useSSL=false&useUnicode=true&characterEncoding=UTF-8",
					"root", "aa909090");
			statement = connection.createStatement();
			strsql = "show tables";
			rs = statement.executeQuery(strsql);
			while (rs.next()) {
				temp = new DefaultMutableTreeNode(rs.getString(1));
				parent.add(temp);
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 匹配是否为数字 网上方法 异常捕获
	 */
	public static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void open() {
		try {
			s.setVisible(false);
			frame.remove(s);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent(); // 获得右键选中的节点
			System.out.println(node.toString());
			System.out.println(node.getParent().toString());
			sql_name = node.getParent().toString();
			table_name = node.toString();
			System.out.println("123456789");

			rowData = Conn.getRows(sql_name, table_name);
			columnNames = Conn.getHead(sql_name, table_name);
			tableModel = new DefaultTableModel(rowData, columnNames);
			table = new JTable(tableModel);
			s = new JScrollPane(table);
			frame.add(s, "Center");
			table.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {

					if (e.getButton() == MouseEvent.BUTTON3) {
						popMenu2.show(table, e.getX(), e.getY());

					}

				}

			});
			s.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {

					if (e.getButton() == MouseEvent.BUTTON3) {
						popMenu2.show(s, e.getX(), e.getY());

					}

				}

			});

			frame.setVisible(true);

			col_number = table.getColumnCount(); // 表格列数
			row_number = table.getRowCount(); // 表格行数
			// value数组存放表格中的所有数据

			value_save = new String[row_number][col_number];
			for (int i = 0; i < row_number; i++) {
				for (int j = 0; j < col_number; j++) {
					value_save[i][j] = table.getValueAt(i, j).toString();
				}
			}

		} catch (Exception e) {
			System.out.println(e);
			System.out.println(flag++);
		}
	}

	public void find() {
		try {
			Connection conn = Conn.getCon();
			Statement statement = null;
			String sql;
			ResultSet rs = null;
			String s = JOptionPane.showInputDialog(null, "请输入你要查询的条件（例如ID=1,不要有空格）：\n", "查询",
					JOptionPane.PLAIN_MESSAGE);
			String[] s1 = s.split("=");

			if (!conn.isClosed())
				System.out.println("成功连接数据库");

			boolean show_flag = false;
			statement = conn.createStatement();
			sql = "select *from " + table_name + " where " + s1[0] + "='" + s1[1] + "'";
			System.out.println(sql);
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				show_flag = true;
				String str = "";
				for (int i = 0; i < columnNames.size(); i++) {
					str += columnNames.get(i) + " ： " + rs.getString(i + 1) + "  ";
				}
				JOptionPane.showMessageDialog(null, str, "查询成功---", 1);

			}
			if (!show_flag) {
				JOptionPane.showMessageDialog(null, "没有数据,可能输入错误", "查询失败", 1);
			}
			rs.close();
			statement.close();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "查询失败，可能没有输入数据，也可能数据输入错误", "查询失败", 1);

		}
	}

	public void save() {
		try {
			int column = table.getColumnCount(); // 表格列数
			int row = table.getRowCount(); // 表格行数
			// value数组存放表格中的所有数据

			String[][] value = new String[row][column];
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < column; j++) {
					value[i][j] = table.getValueAt(i, j).toString();
				}
			}

			PreparedStatement preparedStatement = null;
			Connection conn = Conn.getCon();
			if (!conn.isClosed())
				System.out.println("成功连接数据库");
			preparedStatement = conn.prepareStatement("delete from " + table_name + " where true");
			preparedStatement.executeUpdate();

			String sql = null;
			for (int i = 0; i < row; i++) {
				sql = "insert into " + table_name + " values(";
				for (int j = 0; j < column; j++) {

					if (isNumeric(value[i][j])) {
						sql += Integer.parseInt(value[i][j]) + ",";
					} else {
						sql += "'" + value[i][j] + "',";
					}

				}

				sql = sql.substring(0, sql.length() - 1);
				sql += ")";
				System.out.println(sql);
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.executeUpdate();

			}
			JOptionPane.showMessageDialog(frame, "保存成功，可以继续操作", "消息", 1);

		} catch (Exception e1) {
			JOptionPane.showMessageDialog(frame, "保存失败，检查输入数据是否有问题", "警告", JOptionPane.WARNING_MESSAGE);

			PreparedStatement preparedStatement = null;
			Connection conn = Conn.getCon();
			String sql = null;
			for (int i = 0; i < row_number; i++) {
				sql = "insert into " + table_name + " values(";
				for (int j = 0; j < col_number; j++) {

					if (isNumeric(value_save[i][j])) {
						sql += Integer.parseInt(value_save[i][j]) + ",";
					} else {
						sql += "'" + value_save[i][j] + "',";
					}

				}

				sql = sql.substring(0, sql.length() - 1);
				sql += ")";
				System.out.println(sql);

				try {
					preparedStatement = conn.prepareStatement(sql);
					preparedStatement.executeUpdate();
				} catch (SQLException e2) {
					e2.printStackTrace();

				}
			}
		}
	}

	public void addTable() {
		String name = JOptionPane.showInputDialog(null, "请输入表格名：\n", "表格名", JOptionPane.PLAIN_MESSAGE);
		try {
			String sql = table_name;
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/" + sql
					+ "?useSSL=false&useUnicode=true&amp;characterEncoding=UTF-8";
			String user = "root";
			String password = "aa909090";
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = null;
			preparedStatement = con
					.prepareStatement("create table " + name + "(ID int(4),Name varchar(20),eid int(4))");
			preparedStatement.executeUpdate();
			System.out.println(name);
			((DefaultTreeModel) tree.getModel()).insertNodeInto(

					new DefaultMutableTreeNode(name), node, node.getChildCount());

			tree.expandPath(tree.getSelectionPath());

		} catch (Exception e2) {
			e2.printStackTrace();
			JOptionPane.showMessageDialog(null, "未输入表格名，或者表格名已经存在", "Sorry", 1);
		}
	}

	public void delete() {
		int result = JOptionPane.showConfirmDialog(null, "确定删除？", "删除确认", JOptionPane.YES_NO_OPTION);
		System.out.println(result);

		if (result == 0) {
			try {
				String driver = "com.mysql.jdbc.Driver";
				String url = "jdbc:mysql://localhost:3306/" + sql_name
						+ "?useSSL=false&useUnicode=true&amp;characterEncoding=UTF-8";
				String user = "root";
				String password = "aa909090";
				Class.forName(driver);
				Connection con = DriverManager.getConnection(url, user, password);
				PreparedStatement preparedStatement = null;
				preparedStatement = con.prepareStatement("drop table " + table_name);
				preparedStatement.executeUpdate();

				if (node.isRoot()) {
					return;
				}

				((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
	}

	public void addSql() {
		String name = JOptionPane.showInputDialog(null, "请输入数据库名：\n", "数据库名", JOptionPane.PLAIN_MESSAGE);
		try {
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/mysql?useSSL=false&useUnicode=true&amp;characterEncoding=UTF-8";
			String user = "root";
			String password = "aa909090";
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = null;
			preparedStatement = con.prepareStatement("create  database " + name);
			System.out.println(name);
			preparedStatement.executeUpdate();
			((DefaultTreeModel) tree.getModel()).insertNodeInto(

					new DefaultMutableTreeNode(name), node, node.getChildCount());

			tree.expandPath(tree.getSelectionPath());

		} catch (Exception e2) {
			JOptionPane.showMessageDialog(null, "未输入数据库名，或者数据库名已经存在", "Sorry", 1);
		}

	}
}
