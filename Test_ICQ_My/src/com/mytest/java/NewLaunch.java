	package com.mytest.java;

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
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JDialog;

public class NewLaunch extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField input_id;
	private JPasswordField input_password;
	private JTextField input_server;
	private JTextField input_port;

	JButton login;
	JButton new_user;
	JButton exit;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewLaunch frame = new NewLaunch();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public NewLaunch() {
		this.setTitle("NewLaunch ICQ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lable = new JLabel("输入你的信息");
		lable.setBounds(10, 0, 145, 23);
		contentPane.add(lable);

		JLabel lable_1 = new JLabel("你的icqID");
		lable_1.setBounds(10, 82, 86, 23);
		contentPane.add(lable_1);

		input_id = new JTextField();
		input_id.setBounds(83, 83, 100, 22);
		contentPane.add(input_id);
		input_id.setColumns(10);

		JLabel label_2 = new JLabel("你的密码");
		label_2.setBounds(212, 86, 54, 15);
		contentPane.add(label_2);

		input_password = new JPasswordField();
		input_password.setBounds(276, 83, 135, 18);
		contentPane.add(input_password);

		JLabel lable_3 = new JLabel("服务器");
		lable_3.setBounds(10, 162, 54, 15);
		contentPane.add(lable_3);

		input_server = new JTextField();
		input_server.setText("127.0.0.1");
		input_server.setBounds(83, 159, 100, 18);
		contentPane.add(input_server);
		input_server.setColumns(10);

		JLabel lable_4 = new JLabel("端口");
		lable_4.setBounds(212, 162, 54, 15);
		contentPane.add(lable_4);

		input_port = new JTextField();
		input_port.setText("8080");
		input_port.setBounds(276, 159, 135, 18);
		contentPane.add(input_port);
		input_port.setColumns(10);

		login = new JButton("登录");
		login.addActionListener(this);
		login.setBounds(45, 228, 93, 23);
		contentPane.add(login);

		new_user = new JButton("新建");
		new_user.addActionListener(this);
		new_user.setBounds(173, 228, 93, 23);
		contentPane.add(new_user);

		exit = new JButton("退出");
		exit.setBounds(306, 228, 93, 23);
		exit.addActionListener(this);
		contentPane.add(exit);
		this.setResizable(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == login) {
			try {
				Socket s = new Socket(input_server.getText(), Integer.parseInt(input_port.getText()));

				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())),
						true);
				out.println("login");// 告诉服务器我要登录
				out.println(input_id.getText());
				out.println(input_password.getPassword());
				String str = " ";
				str = in.readLine().trim();// 从服务器读取消息
				// 如果失败就告诉出错
				if (str.equals("false"))
					JOptionPane.showMessageDialog(this, "对不起，输入错误:-(", "ok", JOptionPane.INFORMATION_MESSAGE);
				else if(str.equals("occupy")) {
					JOptionPane.showMessageDialog(this, "此用户已经登录，如确定未登录，请及时联系客服:-(", "ok", JOptionPane.INFORMATION_MESSAGE);
				}
				else {// 如果成功就打开主程序
					JOptionPane.showMessageDialog(this, "登录成功", "ok", JOptionPane.INFORMATION_MESSAGE);
					this.dispose();
					new Main(Integer.parseInt(input_id.getText()), input_server.getText(),
							Integer.parseInt(input_port.getText()));
				}

			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, "连接服务器失败，可能服务器没开，或者没输入账号密码", "消息", 1);
			}

		} else if (e.getSource() == new_user) {
			new Register(input_server.getText(), Integer.parseInt(input_port.getText()));// 打开新建窗
		}
		else if(e.getSource() == exit) {
			System.exit(0);
		}

	}

}
