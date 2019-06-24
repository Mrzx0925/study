package com.MyNotepad.java;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.VetoableChangeListener;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import org.omg.CORBA.Object;

public class TxtFrame extends JFrame implements ActionListener {
	private static boolean flag = true; // 防止点击按钮重复提交
	private static int overwrite; // 判断是否覆盖
	private static boolean fileflag = false; // 判断是否打开了文件
	private static boolean sql_flag = false;

	static String path = "无标题";

	String[][] value_save; //用来储存数据库原始内容，防止保存时删除了而重新插入错误后数据库数据无法恢复

	int col_number; // 表格列数  
	int row_number; // 表格行数
	JScrollPane s;
	DefaultTableModel tableModel;  //public DefaultTableModel(Vector data，Vector columnNames)

	JTable table;

	Vector rowData;   //数据
	Vector columnNames; //表名

	String txt = ""; // 记录内容
	JMenuItem item1 = new JMenuItem("新建");
	JMenuItem item2 = new JMenuItem("打开");
	JMenuItem item3 = new JMenuItem("保存");
	JMenuItem item4 = new JMenuItem("另存为");
	JMenuItem item5 = new JMenuItem("退出");
	JMenuItem item6 = new JMenuItem("打开数据库表格界面");
	JMenuItem item7 = new JMenuItem("增加信息");
	JMenuItem item8 = new JMenuItem("删除");
	JMenuItem item9 = new JMenuItem("保存");
	JMenuItem item10 = new JMenuItem("退出数据库表格界面");
	JMenuItem item11 = new JMenuItem("查找");
	JMenuItem item = new JMenuItem("查看此记事本信息");
	JMenuItem item12 = new JMenuItem("登录数据库");
	JMenuItem item13 = new JMenuItem("查看数据库连接状态");
	JMenuItem item14 = new JMenuItem("退出数据库连接");

	JMenuBar bar;

	JScrollPane j1;
	private static JDialog denfine;
	private static JButton ok;
	private static JButton no_ok;
	private static JButton cancel;
	private static JTextArea information;
	private JPanel button;
	private JPanel right_button;
	private static JFrame frame;
	static JTextArea jt;

	static File selectfile = null;
	static File f = null;
	static String title = "无标题";
	static JFileChooser select;

	public TxtFrame() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); // windows风格
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel") ;
			// // Mac风格
			// UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel") ; //
			// Java默认风格
		} catch (Exception e) {
			System.out.println("窗体风格设置失败");
			System.exit(0);
		}
		frame = this;

		this.setTitle("无标题--记事本");
		this.setIconImage(Toolkit.getDefaultToolkit().createImage("logo.png")); // 设置窗口图片

		bar = new JMenuBar();
		JMenu file = new JMenu("文件");
		JMenu mysql = new JMenu("数据库");
		JMenu help = new JMenu("帮助");
		bar.add(file);
		bar.add(mysql);
		bar.add(help);
		file.add(item1);
		file.add(item2);
		file.add(item3);
		file.add(item4);
		file.add(item5);
		mysql.add(item12);
		mysql.add(item6);
		mysql.add(item7);
		mysql.add(item8);
		mysql.add(item11);
		mysql.add(item9);
		mysql.add(item10);
		mysql.add(item14);

		help.add(item);
		help.add(item13);

		// this.setIconImage(Toolkit.getDefaultToolkit().createImage("logo.PNG"));
		this.setBounds(420, 100, 780, 600);

		jt = new JTextArea();
		j1 = new JScrollPane(jt);

		denfine = new JDialog(frame);
		denfine.setIconImage(Toolkit.getDefaultToolkit().createImage("logo.png")); // 要IMAGE格式
		denfine.setResizable(false);
		denfine.setBounds(550, 250, 320, 120);
		button = new JPanel();
		ok = new JButton("保存");
		no_ok = new JButton("不保存");
		cancel = new JButton("取消");
		information = new JTextArea();
		information.setLineWrap(true); // 能到达边界换行
		jt.setFont(new Font("宋体", Font.BOLD, 16));
		jt.setLineWrap(true);
		information.setFont(new Font("宋体", Font.BOLD, 16)); // Font.BOLD+ Font.ITALIC 粗斜体
		information.setEditable(false);

		right_button = new JPanel(new BorderLayout());
		button.add(ok);
		button.add(no_ok);
		button.add(cancel);
		right_button.add(button, "East");
		denfine.add(right_button, "South");
		denfine.add(information);

		item7.setEnabled(false);
		item8.setEnabled(false);
		item9.setEnabled(false);
		item10.setEnabled(false);
		item11.setEnabled(false);
		item12.setEnabled(true);
		item14.setEnabled(false);
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
		item11.addActionListener(this);
		item.addActionListener(this);
		item12.addActionListener(this);
		item13.addActionListener(this);
		item14.addActionListener(this);

		this.setJMenuBar(bar);
		this.add(j1);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // 关键 点击关闭不会在这关闭 关键代码
		// DO_NOTHING_ON_CLOSE（在 WindowConstants 中定义）：不执行任何操作；要求程序在已注册的 WindowListener
		// 对象的 windowClosing 方法中处理该操作。
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Exit();
			}

		});

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * 新建操作，判断界面内容是否输入或者打开文件了更改，如果更改要提醒是否保存，无就清空界面内容，并且标题改为无标题
		 */
		if (e.getSource() == item1) {// 新建
			flag = true;
			// frame.setAlwaysOnTop(false);
			if (txt.equals(jt.getText())) {// 说明打开未进行更改，或者根本就没有打开过文件
				path = "无标题";
				title = "无标题";
				jt.setText("");
				selectfile = null;
				frame.setTitle(title + "--记事本");
				txt = "";
				flag = false;

			}

			else if (!jt.getText().equals(txt)) { // 说明先直接输入了文字或者更改了，这要提示是否保存
				// frame.setEnabled(false);

				if (selectfile != null) {
					path = selectfile.getAbsolutePath();
				}
				information.setText("是否要将更改保存到  " + path);
				denfine.setVisible(true);
				// denfine.setAlwaysOnTop(true);
				ok.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println(flag);
						if (flag) {
							Save();
							if (overwrite != 1) {
								jt.setText("");
								path = "无标题";
								title = "无标题";
								frame.setTitle(title + "--记事本");
								selectfile = null;
								txt = "";

							}
						}
					}
				});
				no_ok.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						path = "无标题";
						title = "无标题";
						jt.setText("");
						denfine.dispose();
						// frame.setEnabled(true);
						frame.setTitle(title + "--记事本");
						selectfile = null;
						txt = "";
						// frame.setAlwaysOnTop(true);
						flag = false;

					}
				});
				cancel.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						denfine.dispose();
						// frame.setEnabled(true);
						// frame.setAlwaysOnTop(true);
						flag = false;

					}
				});
			}
			/*
			 * 打开操作，一样也要先像新建那样判断，然后打开文件，flag的使用只弹出一次
			 */
		} else if (e.getSource() == item2) { // 打开
			flag = true;
			// frame.setAlwaysOnTop(false);
			JFrame frame = this;
			if (!jt.getText().equals(txt)) {
				// frame.setEnabled(false);
				if (selectfile != null) {
					path = selectfile.getAbsolutePath();
				}
				information.setText("是否要将更改保存到  " + path);
				denfine.setVisible(true);
				// denfine.setAlwaysOnTop(true);
				ok.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (flag) {
							Save();
							String str1 = jt.getText();
							File f = selectfile;
							jt.setText("");
							// frame.setEnabled(true);
							// frame.setTitle(title + "--记事本");
							selectfile = null;
							String str2 = txt;
							txt = "";
							Select();
							denfine.dispose();
							if (!fileflag) {
								jt.setText(str1);
								txt = str2;
							}

						}

					}

				});

				no_ok.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (flag) {
							String str1 = jt.getText();
							File f = selectfile;
							jt.setText("");
							// frame.setEnabled(true);
							// frame.setTitle(title + "--记事本");
							selectfile = null;
							String str2 = txt;
							txt = "";
							Select();
							denfine.dispose();
							if (!fileflag) {
								jt.setText(str1);
								txt = str2;
							}

						}
					}
				});
				cancel.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						denfine.dispose();

						// frame.setEnabled(true);
						// frame.setAlwaysOnTop(true);

					}
				});
			} else {
				if (flag)
					jt.setText("");
				selectfile = null;
				txt = "";
				Select();
			}

			/*
			 * 保存操作，如果是直接打开的文件，就直接存入，如果是无标题则类似另存为
			 */
		} else if (e.getSource() == item3) { // 保存
			// frame.setAlwaysOnTop(false);
			flag = true;
			if (flag)
				Save();
			if (f != null) {
				selectfile = f;
			}

			/*
			 * 另存为，先把title变为无标题执行Save函数，如果原先就是无标题，就不执行下面的换回title值操作
			 */
		} else if (e.getSource() == item4) { // 另存为
			flag = true;
			// frame.setAlwaysOnTop(false);
			title = "无标题";
			Save();
			if (f != null)
				title = f.getName();
			selectfile = f;

		} else if (e.getSource() == item5) { // 退出
			Exit();
		} else if (e.getSource() == item) {
			JOptionPane.showMessageDialog(frame, "自制记事本----可以实现简单的数据库增删查改功能-------搜狗输入法会卡死---by张喜", "消息",
					JOptionPane.PLAIN_MESSAGE);
		} else if (e.getSource() == item6) {// 打开数据库

			if (Login.isFlag()) {
				item12.setEnabled(false);
				frame.setTitle("我的数据库 ---" + Login.getName_sql() + "---" + Login.getName_table());
				rowData = Login.getRows();
				columnNames = Login.getHead();
				tableModel = new DefaultTableModel(rowData, columnNames);
				table = new JTable(tableModel);
				s = new JScrollPane(table);

				item1.setEnabled(false);
				item2.setEnabled(false);
				item3.setEnabled(false);
				item4.setEnabled(false);
				item5.setEnabled(false);
				item6.setEnabled(false);
				item7.setEnabled(true);
				item8.setEnabled(true);
				item9.setEnabled(true);
				item10.setEnabled(true);
				item11.setEnabled(true);
				item12.setEnabled(false);
				item14.setEnabled(false);

				this.setIconImage(Toolkit.getDefaultToolkit().createImage("logo1.png"));
				jt.setText("");
				frame.remove(j1);
				frame.add(s);
				s.setVisible(true);
				s.setEnabled(true);

				col_number = table.getColumnCount(); // 表格列数
				row_number = table.getRowCount(); // 表格行数
				// value数组存放表格中的所有数据

				value_save = new String[row_number][col_number];
				for (int i = 0; i < row_number; i++) {
					for (int j = 0; j < col_number; j++) {
						value_save[i][j] = table.getValueAt(i, j).toString();
					}
				}
				frame.setVisible(false);
				frame.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(frame, "数据库未连接，请登录后再试", "消息", 1);
			}

		} else if (e.getSource() == item7) {
			tableModel.addRow(new Vector());

		} else if (e.getSource() == item8) {
			int rowcount = table.getSelectedRow();
			if (rowcount >= 0) {
				tableModel.removeRow(rowcount);
			}
		} else if (e.getSource() == item9) {
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
				Connection conn = Login.getCon();
				if (!conn.isClosed())
					System.out.println("成功连接数据库");
				preparedStatement = conn.prepareStatement("delete from " + Login.getName_table() + " where true");
				preparedStatement.executeUpdate();

				String sql = null;
				for (int i = 0; i < row; i++) {
					sql = "insert into " + Login.getName_table() + " values(";
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
				Connection conn = Login.getCon();
				String sql = null;
				for (int i = 0; i < row_number; i++) {
					sql = "insert into " + Login.getName_table() + " values(";
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
						// TODO Auto-generated catch block
						e2.printStackTrace();

					}
				}
			}

		} else if (e.getSource() == item10) {
			item1.setEnabled(true);
			item2.setEnabled(true);
			item3.setEnabled(true);
			item4.setEnabled(true);
			item5.setEnabled(true);
			item6.setEnabled(true);
			item7.setEnabled(false);
			item8.setEnabled(false);
			item9.setEnabled(false);
			item10.setEnabled(false);
			item11.setEnabled(false);
			item12.setEnabled(true);
			item14.setEnabled(true);
			frame.remove(s);
			frame.add(j1);
			frame.setVisible(false);
			frame.setVisible(true);
			frame.setTitle(title + "--记事本");
			this.setIconImage(Toolkit.getDefaultToolkit().createImage("logo.png"));

		} else if (e.getSource() == item11) {
			try {
				Connection conn = Login.getCon();
				Statement statement = null;
				String sql;
				ResultSet rs = null;
				String s = JOptionPane.showInputDialog(null, "请输入你要查询的条件（例如ID=1）：\n", "查询", JOptionPane.PLAIN_MESSAGE);
				String[] s1 = s.split("=");

				if (!conn.isClosed())
					System.out.println("成功连接数据库");

				boolean show_flag = false;
				statement = conn.createStatement();
				sql = "select *from T_Students where " + s1[0] + "='" + s1[1] + "'";
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
					JOptionPane.showMessageDialog(null, "没有数据", "查询失败", 1);
				}
				rs.close();
				statement.close();
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, "查询失败，可能没有输入数据，也可能数据输入错误", "查询失败", 1);
			}

		} else if (e.getSource() == item12) {
			new Login();
		} else if (e.getSource() == item13) {
			if (Login.isFlag()) {
				JOptionPane.showMessageDialog(frame, "已连接数据库：" + Login.getName_sql() + "  表格： " + Login.getName_table(),
						"消息", 1);
			} else {
				JOptionPane.showMessageDialog(frame, "未连接", "消息", 1);
			}
		} else if (e.getSource() == item14) {
			try {
				Login.getCon().close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			Login.setFlag(false);
			JOptionPane.showMessageDialog(frame, "已经退出数据库连接，再次打开数据库需要重新登录", "消息", 1);
			item14.setEnabled(false);
		}
	}

	public void Select() {

		JFileChooser choose = new JFileChooser();
		int result = choose.showOpenDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) { // 没有点取消或者× APPROVE_OPTION 选择确认（yes、ok）后返回该值。
			selectfile = choose.getSelectedFile();
			try {
				FileReader fr = new FileReader(selectfile);
				BufferedReader br = new BufferedReader(fr);
				String line = null;
				while ((line = br.readLine()) != null) {
					txt += line + "\n";
				}
				txt = txt.substring(0, txt.length() - 1);
				br.close();
				System.out.println(selectfile.getAbsolutePath());
				title = selectfile.getName();
				frame.setTitle(title + "--记事本");

				jt.setText(txt);
				// frame.setAlwaysOnTop(true);
				fileflag = true;

			} catch (Exception e2) {
				JOptionPane.showMessageDialog(choose, "找不到指定文件。\n请检查文件名是否正确，然后重试。", "打开", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			fileflag = false;
			// frame.setEnabled(true);
			// frame.setAlwaysOnTop(true);
		}
		flag = false;

	}

	public void Save() {
		f = selectfile;
		System.out.println("111");
		overwrite = 0;
		denfine.dispose();
		if (title.equals("无标题")) {
			System.out.println("11");
			int value = 0;
			select = new JFileChooser();
			select.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			value = select.showSaveDialog(denfine);

			if (value == JFileChooser.APPROVE_OPTION) {
				f = select.getSelectedFile();
				if (f.exists()) {
					overwrite = JOptionPane.showConfirmDialog(select, f.getName() + "已经存在.\n要替换它嘛?", "确认另存为",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); // 返回 0 1
					if (overwrite == 1) {
						Save();
					}
				}
			} else
				overwrite = 1;
		}
		if (overwrite == 0) {

			try {
				FileWriter fw = new FileWriter(f);
				fw.write(jt.getText());
				fw.close();
				title = f.getName();
				frame.setTitle(title + "--记事本");

				JOptionPane.showMessageDialog(denfine, "保存成功");

			} catch (IOException e1) {
				e1.printStackTrace();
			}

			txt = jt.getText();
		}
		denfine.dispose();

		flag = false;

	}

	public void Exit() {
		flag = true;
		if (!jt.getText().equals(txt)) { // 说明先直接输入了文字或者更改了，这要提示是否保存
			// frame.setEnabled(false);

			if (selectfile != null) {
				path = selectfile.getAbsolutePath();
			}
			information.setText("是否要将更改保存到  " + path);
			denfine.setVisible(true);
			// denfine.setAlwaysOnTop(true);

			ok.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(flag);
					if (flag) {
						Save();
						if (overwrite == 0)
							System.exit(0);
					}

				}
			});
			no_ok.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					denfine.dispose();
					// frame.setEnabled(true);
					// frame.setAlwaysOnTop(true);

				}
			});
		} else {
			System.exit(0);
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
			// TODO: handle exception
			return false;
		}
	}
}
