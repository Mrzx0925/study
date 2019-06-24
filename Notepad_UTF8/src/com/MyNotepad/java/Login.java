package com.MyNotepad.java;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

public class Login extends JFrame {
	private JPanel contentPane;
	private JTextField txtIp;
	private JTextField txtPort;
	private JTextField txtUsername;
	private JTextField txtAa;
	private static Connection con = null;
	private JLabel lblNewLabel_1;
	private JLabel label_2;
	private JTextField textField;
	private JFrame frame;
	private static String name_sql;
	private static boolean flag = false;
	private JTextField txtTstudents;
	private static String name_table;
	private static ResultSet rs;

	

	public static String getName_table() {
		return name_table;
	}

	public static void setName_table(String name_table) {
		Login.name_table = name_table;
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		frame = this;
		this.setTitle("--登录数据库--");
		this.setIconImage(Toolkit.getDefaultToolkit().createImage("月光.png")); // 设置窗口图片
		setBounds(400, 100, 461, 326);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("主机或者IP地址");
		lblNewLabel.setBounds(23, 10, 119, 25);
		contentPane.add(lblNewLabel);

		txtIp = new JTextField();
		txtIp.setText("127.0.0.1");
		txtIp.setBounds(152, 12, 216, 25);
		contentPane.add(txtIp);
		txtIp.setColumns(10);

		JLabel label = new JLabel("端口");
		label.setBounds(23, 45, 135, 25);
		contentPane.add(label);

		txtPort = new JTextField();
		txtPort.setText("3306");
		txtPort.setBounds(152, 46, 125, 23);
		contentPane.add(txtPort);
		txtPort.setColumns(10);

		JLabel label_1 = new JLabel("用户名");
		label_1.setBounds(23, 83, 54, 25);
		contentPane.add(label_1);

		txtUsername = new JTextField();
		txtUsername.setText("root");
		txtUsername.setBounds(152, 85, 183, 23);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);

		JLabel lblNewbel = new JLabel("密码");
		lblNewbel.setBounds(23, 119, 76, 31);
		contentPane.add(lblNewbel);

		txtAa = new JTextField();
		txtAa.setText("aa909090");
		txtAa.setBounds(152, 119, 194, 25);
		contentPane.add(txtAa);
		txtAa.setColumns(10);

		JButton btnNewButton = new JButton("连接");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String driver = "com.mysql.jdbc.Driver";
				String url = "jdbc:mysql://" + txtIp.getText() + ":" + Integer.parseInt(txtPort.getText()) + "/"
						+ textField.getText() + "?useSSL=false&useUnicode=true&amp;characterEncoding=UTF-8";
				String user = txtUsername.getText();
				String password = txtAa.getText();
				try {
					name_table = txtTstudents.getText();
				
					name_sql = textField.getText();
					Class.forName(driver);
					con = DriverManager.getConnection(url, user, password);
					PreparedStatement preparedStatement = con.prepareStatement("select * from "+name_table);
					rs = preparedStatement.executeQuery();  //测试代码
					
					JOptionPane.showMessageDialog(null, "连接成功", "消息", 1);
					frame.setVisible(false);
					flag = true;
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "连接失败,不能使用数据库相关功能", "消息", 1);
					con = null;
				}

			}
		});
		btnNewButton.setBounds(286, 235, 138, 42);
		contentPane.add(btnNewButton);

		lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setBounds(10, 160, 21, -7);
		contentPane.add(lblNewLabel_1);

		label_2 = new JLabel("数据库名");
		label_2.setBounds(23, 160, 54, 15);
		contentPane.add(label_2);

		textField = new JTextField();
		textField.setText("study");
		textField.setBounds(152, 154, 183, 25);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("表格名");
		lblNewLabel_2.setBounds(23, 183, 92, 42);
		contentPane.add(lblNewLabel_2);
		
		txtTstudents = new JTextField();
		txtTstudents.setText("T_Students");
		txtTstudents.setBounds(152, 189, 183, 25);
		contentPane.add(txtTstudents);
		txtTstudents.setColumns(10);
		this.setVisible(true);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector getRows() {
		PreparedStatement preparedStatement = null;

		Vector rows = null;

		try {
			// if(!conn.isClosed())
			// System.out.println("成功连接数据库");
			preparedStatement = con.prepareStatement("select * from "+name_table); // 查找所有数据
			ResultSet result1 = preparedStatement.executeQuery();

			if (result1.wasNull()) // 空 不影响
				JOptionPane.showMessageDialog(null, "结果集中无记录");

			rows = new Vector(); // 当Vector或ArrayList中的元素超过它的初始大小时,Vector会将它的容量翻倍,而ArrayList只增加50%的大小，这样,ArrayList就有利于节约内存空间
									// 如果你需要高效的随即存取，而不在乎插入和删除的效率，使用vector

			ResultSetMetaData rsmd = result1.getMetaData(); // getMetaData包括了数据的字段名称、类型以及数目等表格所必须具备的信息。

			while (result1.next()) {
				rows.addElement(getNextRow(result1, rsmd));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("未成功打开数据库,检查数据库的表格是否存在");
			
		}
		return rows;
	}

	@SuppressWarnings("unchecked")
	public static Vector getHead() {
		PreparedStatement preparedStatement = null;

		Vector columnHeads = null;

		try {

			preparedStatement = con.prepareStatement("select * from "+name_table);
			ResultSet result1 = preparedStatement.executeQuery();

			boolean moreRecords = result1.next();
			if (!moreRecords)
				JOptionPane.showMessageDialog(null, "结果集中无记录");

			columnHeads = new Vector();
			ResultSetMetaData rsmd = result1.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) //从1开始
				columnHeads.addElement(rsmd.getColumnName(i)); // 方法说明：根据字段的索引值取得字段的名称。参数字段的索引值，从1开始。返回值：字段的名称（字符串）。
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("未成功打开数据库。");
			e.printStackTrace();
		}
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

	public static String getName_sql() {
		return name_sql;
	}

	public static void setName_sql(String name_sql) {
		Login.name_sql = name_sql;
	}

	public static boolean isFlag() {
		return flag;
	}

	public static void setFlag(boolean flag) {
		Login.flag = flag;
	}

	public static Connection getCon() {
		return con;
	}

	public static void setCon(Connection con) {
		Login.con = con;
	}
	
}
