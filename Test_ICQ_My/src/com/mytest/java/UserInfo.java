package com.mytest.java;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

public class UserInfo extends JDialog implements ActionListener {

	BufferedReader in;
	PrintWriter out;
	String id;
	private JPanel contentPane;
	private JTextField ID;
	private JTextField input_nickname;
	private JTextField input_name;
	private JTextField input_age;
	private JTextField input_school;
	private JTextField input_web;
	private JTextField input_email;
	private JTextField input_place;
	private JTextField input_mobilephone;
	private JTextField input_phone;
	private JTextArea input_info;
	private JComboBox pic;
	private JTextArea input_Sig;
	private JComboBox sex;
	private JComboBox zodic;
	private JComboBox job;
	private JComboBox constellation;
	private JComboBox blood;

	JButton ok;
	JButton cancel;
	private String[] pics = new String[] { // 头像
			"pic//1.jpg", "pic//3.jpg", "pic//5.jpg", "pic//7.jpg" };

	public UserInfo(String id, BufferedReader in, PrintWriter out) {
		this.in = in;
		this.out = out;
		this.id = id;

		out.println(id);
		ComboBoxModel model = new HeadPicCombobox(pics);

		ListCellRenderer renderer = new HeadpicCellRenderer();
		setBounds(100, 100, 475, 673);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label_1 = new JLabel("用户号码:");
		label_1.setBounds(10, 10, 60, 15);
		contentPane.add(label_1);

		ID = new JTextField();
		ID.setEnabled(false);
		ID.setText(id);
		ID.setBounds(87, 7, 189, 21);
		contentPane.add(ID);
		ID.setColumns(10);

		JLabel lable_2 = new JLabel("用户昵称:");
		lable_2.setBounds(10, 45, 60, 15);
		contentPane.add(lable_2);

		input_nickname = new JTextField();
		input_nickname.setBounds(87, 42, 189, 21);
		contentPane.add(input_nickname);
		input_nickname.setColumns(10);

		pic = new JComboBox();
		pic.setBounds(335, 10, 79, 52);
		pic.setRenderer(renderer);
		pic.setModel(model);
		contentPane.add(pic);

		JLabel lable_3 = new JLabel("个性签名:");
		lable_3.setBounds(10, 80, 60, 15);
		contentPane.add(lable_3);

		input_Sig = new JTextArea();
		input_Sig.setBounds(87, 76, 362, 67);
		contentPane.add(input_Sig);

		JLabel lable_4 = new JLabel("性    别:");
		lable_4.setBounds(10, 166, 60, 15);
		contentPane.add(lable_4);

		sex = new JComboBox();
		sex.addItem(null);
		sex.addItem("男");
		sex.addItem("女");
		sex.setBounds(87, 163, 51, 21);
		contentPane.add(sex);

		JLabel lable_5 = new JLabel("姓名:");
		lable_5.setBounds(162, 166, 43, 15);
		contentPane.add(lable_5);

		input_name = new JTextField();
		input_name.setBounds(197, 163, 79, 21);
		contentPane.add(input_name);
		input_name.setColumns(10);

		JLabel lable_6 = new JLabel("年龄:");
		lable_6.setBounds(299, 166, 54, 15);
		contentPane.add(lable_6);

		input_age = new JTextField();
		input_age.setBounds(335, 163, 99, 21);
		contentPane.add(input_age);
		input_age.setColumns(10);

		JLabel lable_7 = new JLabel("毕业院校:");
		lable_7.setBounds(10, 203, 60, 15);
		contentPane.add(lable_7);

		input_school = new JTextField();
		input_school.setBounds(87, 200, 189, 21);
		contentPane.add(input_school);
		input_school.setColumns(10);

		JLabel label_8 = new JLabel("生肖:");
		label_8.setBounds(299, 203, 54, 15);
		contentPane.add(label_8);

		zodic = new JComboBox();
		zodic.addItem(null);
		zodic.addItem("子鼠");
		zodic.addItem("丑牛");
		zodic.addItem("寅虎");
		zodic.addItem("卯兔");
		zodic.addItem("辰龙");
		zodic.addItem("巳蛇");
		zodic.addItem("午马");
		zodic.addItem("未羊");
		zodic.addItem("申猴");
		zodic.addItem("酉鸡");
		zodic.addItem("戌狗");
		zodic.addItem("亥猪");
		zodic.setBounds(335, 200, 99, 21);
		contentPane.add(zodic);

		JLabel lable_9 = new JLabel("职    业:");
		lable_9.setBounds(10, 240, 60, 15);
		contentPane.add(lable_9);

		job = new JComboBox();
		job.addItem(null);
		job.addItem("学生");
		job.addItem("工人");
		job.addItem("老师");
		job.setBounds(87, 237, 189, 21);
		contentPane.add(job);

		JLabel lable_10 = new JLabel("星座:");
		lable_10.setBounds(299, 240, 54, 15);
		contentPane.add(lable_10);

		constellation = new JComboBox();
		constellation.addItem(null);
		constellation.addItem("白羊座");
		constellation.addItem("金牛座");
		constellation.addItem("双子座");
		constellation.addItem("巨蟹座");
		constellation.addItem("狮子座");
		constellation.addItem("处女座");
		constellation.addItem("天秤座");
		constellation.addItem("天蝎座");
		constellation.addItem("射手座");
		constellation.addItem("摩羯座");
		constellation.addItem("水瓶座");
		constellation.addItem("双鱼座");
		constellation.setBounds(335, 237, 99, 21);
		contentPane.add(constellation);

		JLabel lable_11 = new JLabel("个人主页:");
		lable_11.setBounds(10, 281, 60, 15);
		contentPane.add(lable_11);

		input_web = new JTextField();
		input_web.setBounds(87, 278, 189, 21);
		contentPane.add(input_web);
		input_web.setColumns(10);

		JLabel label_12 = new JLabel("血型:");
		label_12.setBounds(299, 281, 54, 15);
		contentPane.add(label_12);

		blood = new JComboBox();
		blood.addItem(null);
		blood.addItem("A");
		blood.addItem("B");
		blood.addItem("AB");
		blood.addItem("O");
		blood.addItem("其他");
		blood.setBounds(335, 278, 99, 21);
		contentPane.add(blood);

		JLabel label_13 = new JLabel("个人说明:");
		label_13.setBounds(10, 321, 60, 15);
		contentPane.add(label_13);

		input_info = new JTextArea();
		input_info.setBounds(87, 321, 362, 83);
		contentPane.add(input_info);

		JLabel lable_14 = new JLabel("电子邮件:");
		lable_14.setBounds(10, 431, 60, 15);
		contentPane.add(lable_14);

		input_email = new JTextField();
		input_email.setBounds(72, 428, 204, 21);
		contentPane.add(input_email);
		input_email.setColumns(10);

		JLabel lable_15 = new JLabel("联系地址:");
		lable_15.setBounds(10, 473, 60, 15);
		contentPane.add(lable_15);

		input_place = new JTextField();
		input_place.setBounds(72, 470, 204, 21);
		contentPane.add(input_place);
		input_place.setColumns(10);

		JLabel lable_16 = new JLabel("手机号码:");
		lable_16.setBounds(10, 513, 60, 15);
		contentPane.add(lable_16);

		input_mobilephone = new JTextField();
		input_mobilephone.setColumns(10);
		input_mobilephone.setBounds(72, 510, 204, 21);
		contentPane.add(input_mobilephone);

		JLabel lable_17 = new JLabel("电话号码:");
		lable_17.setBounds(10, 552, 60, 15);
		contentPane.add(lable_17);

		input_phone = new JTextField();
		input_phone.setColumns(10);
		input_phone.setBounds(72, 549, 204, 21);
		contentPane.add(input_phone);

		ok = new JButton("确定");
		ok.setBounds(162, 591, 93, 23);
		contentPane.add(ok);

		cancel = new JButton("取消");
		cancel.setBounds(299, 591, 93, 23);
		contentPane.add(cancel);
		ok.addActionListener(this);
		cancel.addActionListener(this);

		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // 关键 点击关闭不会在这关闭 关键代码
		// DO_NOTHING_ON_CLOSE（在 WindowConstants 中定义）：不执行任何操作；要求程序在已注册的 WindowListener
		// 对象的 windowClosing 方法中处理该操作。
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				out.println("cancel");
				dispose();

			}

		});

		this.setVisible(true);
		this.setResizable(false);
		try {
			input_nickname.setText(in.readLine());
			pic.setSelectedIndex(Integer.parseInt(in.readLine()));
			input_email.setText(in.readLine());
			input_info.setText(in.readLine());

			input_Sig.setText(in.readLine());
			sex.setSelectedItem(in.readLine());

			input_name.setText(in.readLine());
			input_age.setText(in.readLine());
			input_school.setText(in.readLine());
			zodic.setSelectedItem(in.readLine());
			job.setSelectedItem(in.readLine());
			constellation.setSelectedItem(in.readLine());
			input_web.setText(in.readLine());
			blood.setSelectedItem(in.readLine());

			input_place.setText(in.readLine());
			input_mobilephone.setText(in.readLine());
			input_phone.setText(in.readLine());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			out.println(input_nickname.getText());
			out.println(pic.getSelectedIndex());
			out.println(input_email.getText());
			out.println(input_info.getText());
			out.println(input_Sig.getText());
			out.println(sex.getSelectedItem());
			out.println(input_name.getText());
			out.println(input_age.getText());
			out.println(input_school.getText());
			out.println(zodic.getSelectedItem());
			out.println(job.getSelectedItem());
			out.println(constellation.getSelectedItem());
			out.println(input_web.getText());
			out.println(blood.getSelectedItem());
			out.println(input_place.getText());
			out.println(input_mobilephone.getText());
			out.println(input_phone.getText());
			try {
				if (in.readLine().equals("ok")) {
					JOptionPane.showMessageDialog(null, "更新成功", "消息", 1);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			dispose();
		} else if (e.getSource() == cancel) {
			out.println("cancel");
			dispose();
		}

	}
}
