package com.othertest.java;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JList;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class Register extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField input_nickname;
	private JTextField input_email;
	private JPasswordField input_password;

	JLabel lable;
	JLabel lable_nickname;
	JLabel lable_password;
	JLabel lable_email;
	JLabel lable_pic;
	JComboBox pic_box;
	JLabel lable_sex;
	JRadioButton boy;
	JRadioButton girl;
	JComboBox place;
	JLabel lable_1;
	JButton ok;
	JButton cancel;
	JTextArea input_info;
	JLabel label;
	private String[] pics = new String[] { // 头像
		"pic//1.jpg","pic//3.jpg","pic//5.jpg","pic//7.jpg"};

	/**
	 * Create the frame.
	 */

	String IP;// 服务器IP
	int serverport;// 服务器端口

	public Register(String s, int port) {
		this.IP = s;
		this.serverport = port;
		this.setTitle("Register");

		ComboBoxModel model = new HeadPicCombobox(pics);

		ListCellRenderer renderer = new HeadpicCellRenderer();
		setBounds(100, 100, 450, 364);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lable = new JLabel("请填写以下内容");
		lable.setBackground(Color.BLACK);
		lable.setBounds(10, 0, 131, 15);
		contentPane.add(lable);

		lable_nickname = new JLabel("昵称");
		lable_nickname.setBounds(10, 25, 54, 15);
		contentPane.add(lable_nickname);

		input_nickname = new JTextField();
		input_nickname.setBounds(52, 22, 148, 21);
		contentPane.add(input_nickname);
		input_nickname.setColumns(10);

		lable_password = new JLabel("密码");
		lable_password.setBounds(220, 25, 54, 15);
		contentPane.add(lable_password);

		lable_email = new JLabel("电子邮件");
		lable_email.setBounds(10, 72, 65, 15);
		contentPane.add(lable_email);

		input_email = new JTextField();
		input_email.setBounds(75, 69, 125, 21);
		contentPane.add(input_email);
		input_email.setColumns(10);

		lable_pic = new JLabel("头像");
		lable_pic.setBounds(220, 72, 54, 15);
		contentPane.add(lable_pic);

		pic_box = new JComboBox();
		pic_box.setModel(model);
		pic_box.setRenderer(renderer);

		pic_box.setBounds(261, 59, 65, 43);

		contentPane.add(pic_box);

		input_password = new JPasswordField();
		input_password.setBounds(261, 22, 148, 18);
		contentPane.add(input_password);

		lable_sex = new JLabel("性别");
		lable_sex.setBounds(10, 126, 54, 15);
		contentPane.add(lable_sex);

		boy = new JRadioButton("男");
		boy.setBounds(52, 122, 48, 23);
		contentPane.add(boy);

		girl = new JRadioButton("女");
		girl.setBounds(102, 122, 65, 23);
		contentPane.add(girl);

		label = new JLabel("来自");
		label.setBounds(220, 126, 54, 15);
		contentPane.add(label);

		place = new JComboBox();
		place.addItem("四川");
		place.addItem("北京");
		place.addItem("上海");
		place.addItem("重庆");
		place.setBounds(261, 123, 70, 22);
		contentPane.add(place);

		lable_1 = new JLabel("个人资料");
		lable_1.setBounds(10, 175, 131, 15);
		contentPane.add(lable_1);

		ok = new JButton("确定");
		ok.addActionListener(this);
		ok.setBounds(181, 292, 93, 23);
		contentPane.add(ok);

		cancel = new JButton("取消");
		cancel.setBounds(297, 292, 93, 23);
		contentPane.add(cancel);

		input_info = new JTextArea();
		input_info.setBounds(10, 200, 414, 87);
		contentPane.add(input_info);
		this.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == ok) {
			try {
				Socket socket = new Socket(IP, serverport);// 连接服务器
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),
						true);
				out.println("new");// 发送新建用户请求
				out.println(input_nickname.getText().trim());// 发送呢称等信息
				out.println(input_password.getPassword());
				out.println(input_email.getText().trim());
				out.println(input_info.getText().trim());
				out.println(place.getSelectedItem());
				out.println(pic_box.getSelectedIndex());// head picindex
				int no;
				System.out.print("我的");
				no = Integer.parseInt(in.readLine());
				System.out.print(no);

				String str = " ";
				// do{
				str = in.readLine().trim();// 从服务器读取信息
				// 如果出错
				if (str.equals("false"))
					JOptionPane.showMessageDialog(this, "对不起，出错了:-(", "ok", JOptionPane.INFORMATION_MESSAGE);
				else {// 如果成功就告诉用户其号码
					JOptionPane.showMessageDialog(this, "your javaicq#is" + no, "ok", JOptionPane.INFORMATION_MESSAGE);
				}
				// System.out.println("\n");
				// }while(!str.equals("ok"));
				// socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
	}

}

class HeadPicCombobox extends DefaultComboBoxModel {// 头象列表类
	public HeadPicCombobox(String[] pics) {
		for (int i = 0; i < pics.length; ++i) {

			addElement(new Object[] { new ImageIcon(pics[i]) });
		}
	}

	public Icon getIcon(Object object) {
		Object[] array = (Object[]) object;
		return (Icon) array[0];
	}
}

class HeadpicCellRenderer extends JLabel implements ListCellRenderer {
	private Border lineBorder = BorderFactory.createLineBorder(Color.red, 2),
			emptyBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);

	public HeadpicCellRenderer() {
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		HeadPicCombobox model = (HeadPicCombobox) list.getModel();

		setIcon(model.getIcon(value));

		if (isSelected) {
			setForeground(list.getSelectionForeground());
			setBackground(list.getSelectionBackground());
		} else {
			setForeground(list.getForeground());
			setBackground(list.getBackground());
		}

		if (cellHasFocus)
			setBorder(lineBorder);
		else
			setBorder(emptyBorder);

		return this;
	}
}
