package com.JTreeTest.java;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class Sql_Create extends JFrame implements ActionListener {
	private JTable table;
	private JPopupMenu popMenu;
	private JMenuItem item1 = new JMenuItem("增加一行");
	private JMenuItem item2 = new JMenuItem("删除一行");
	private JMenuItem item3 = new JMenuItem("生成表");
	Vector<String> columnNames;
	Vector rowData;
	DefaultTableModel tableModel;
	JScrollPane s;

	JFrame frame = new JFrame();
	String[][] value_save; // 用来储存数据库原始内容，防止保存时删除了而重新插入错误后数据库数据无法恢复

	static DefaultMutableTreeNode node;
	static String sql_name;
	
	static JTree tree;

	

	public static DefaultMutableTreeNode getNode() {
		return node;
	}

	public static void setNode(DefaultMutableTreeNode node) {
		Sql_Create.node = node;
	}

	public static JTree getTree() {
		return tree;
	}

	public static void setTree(JTree tree) {
		Sql_Create.tree = tree;
	}

	public static String getSql_name() {
		return sql_name;
	}

	public static void setSql_name(String sql_name) {
		Sql_Create.sql_name = sql_name;
	}

	public Sql_Create() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//Windows风格
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.setIconImage(Toolkit.getDefaultToolkit().createImage("月光.png")); // 设置窗口图片
		this.setTitle("创建表");
		frame = this;
		columnNames = new Vector<>();
		columnNames.add("名");
		columnNames.add("数据类型");
		columnNames.add("数据大小");
		setBounds(250, 100, 450, 400);

		tableModel = new DefaultTableModel(rowData, columnNames);
		tableModel.addRow(new Vector());
		table = new JTable(tableModel);
		s = new JScrollPane(table);

		popMenu = new JPopupMenu();
		popMenu.add(item1);
		popMenu.add(item2);
		popMenu.add(item3);
		item1.addActionListener(this);
		item2.addActionListener(this);
		item3.addActionListener(this);
		s.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {

				if (e.getButton() == 3) {
					popMenu.show(s, e.getX(), e.getY());

				}
			}

		});
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {

				if (e.getButton() == 3) {
					popMenu.show(s, e.getX(), e.getY());

				}
			}
		});
		this.add(s);
		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == item1) {
			tableModel.addRow(new Vector());
		} else if (e.getSource() == item2) {
			int rowcount = table.getSelectedRow();
			if (rowcount >= 0) {
				tableModel.removeRow(rowcount);
			}
		} else if (e.getSource() == item3) {
			String name = JOptionPane.showInputDialog(null, "请输入表格名：\n", "表格名", JOptionPane.PLAIN_MESSAGE);
			try {
				int column = table.getColumnCount(); // 表格列数
				int row = table.getRowCount(); // 表格行数

				String[][] value = new String[row][column];
				for (int i = 0; i < row; i++) {
					for (int j = 0; j < column; j++) {
						value[i][j] = table.getValueAt(i, j).toString();
					}
				}

				PreparedStatement preparedStatement = null;
				String driver = "com.mysql.jdbc.Driver";
				String url = "jdbc:mysql://localhost:3306/" + sql_name
						+ "?useSSL=false&useUnicode=true&amp;characterEncoding=UTF-8";
				String user = "root";
				String password = "aa909090";
				Class.forName(driver);
				Connection con = DriverManager.getConnection(url, user, password);
				String sql;
				sql = "create table " + name + "(";
				for (int i = 0; i < row; i++) {
					sql += value[i][0]+" " + value[i][1] + "(" + value[i][2] + "),";
				}

				sql = sql.substring(0, sql.length() - 1);
				sql += ")";
				System.out.println(sql);
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.executeUpdate();
				JOptionPane.showMessageDialog(frame, "表创建成功", "消息", 1);
				frame.setVisible(false);
				
				((DefaultTreeModel) tree.getModel()).insertNodeInto(

						new DefaultMutableTreeNode(name), node, node.getChildCount());

				tree.expandPath(tree.getSelectionPath());

			} catch (Exception e1) {
				//e1.printStackTrace();
				JOptionPane.showMessageDialog(frame, "创建失败，检查输入数据是否有问题", "警告", JOptionPane.WARNING_MESSAGE);
				frame.setVisible(true);
			}
		}

	}

}
