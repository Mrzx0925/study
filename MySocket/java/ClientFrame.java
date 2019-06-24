package com.MySocket.java;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.synth.SynthScrollBarUI;

/*
 * 客户端界面及功能类
 */
public class ClientFrame extends JFrame implements ActionListener, Runnable {
	private static final long serialVersionUID = 1L; // ????????????????
	private static boolean flag = true; // 判断是否输入的姓名
	private static int file_number_flag = 0; // 服务器文件数量

	private static String IP; // IP地址
	private static int port = 6666; // 主端口，发送消息端口
	private static int file_port = 9988; // 文件发送端口
	private static int download_port = 5555; // 文件下载端口
	private static int file_list_port = 10018; // 得到服务器文件列表端口

	String filepath;
	private FileInputStream fis;
	private DataOutputStream dos;
	private File f; // 发送文件
	private Socket socket; // 连接socket
	private Socket datasocket; // 发送文件Socket
	private Socket downloadsocket; // 下载文件Socket
	private Socket getFile; // 得到文件Socket
	BufferedReader br = null;
	OutputStream os = null; // 输出流
	BufferedWriter bw = null; // 写

	JTextArea message; // 显示输入的内容，即聊天内容
	JScrollPane scroll; // 使内容能滚动
	JTextField input; // 输入内容
	JButton submit; // 发送按钮
	JButton sendfile; // 发送文件按钮
	JButton download; // 下载文件按钮
	JButton IP_ok; // 输入IP确定按钮
	JPanel panel; // 一个容器，装input 和 submit
	JPanel getIP; // 得到IP的容器
	JTextField IP_input; // 输入IP地址
	JFrame IPJF; // 输入IP界面

	String uname = ""; // 得到名字

	// 聊天页面
	JPanel chat; // 聊天界面 容器

	// 登录页面
	JPanel login; // 登录界面容器
	JTextField username; // 输入的名字
	JTextArea link = null; // 输出连接成功四个字
	JButton ok; // 连接按钮

	public ClientFrame(String name) {
		super(name);

		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); // windows风格
			 //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel") ; // Mac风格
			// UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel") ; // Java默认风格
		} catch (Exception e) {
			System.out.println("windows风格设置失败");
			System.exit(0);
		}
		// 在创建客户端窗体是就要把客户端与服务端连接
		IPJF = new JFrame("输入IP");
		IPJF.setBounds(400, 200, 400, 100);
		getIP = new JPanel();
		IP_input = new JTextField(15);
		IP_input.setFont(new Font(Font.DIALOG, Font.BOLD, 20)); // new Font(String 字体，int 风格，int 字号);
		IP_input.setForeground(Color.DARK_GRAY);
		IP_input.setText("129.204.52.33"); // 设置初始IP地址为本机

		IP_ok = new JButton("确定"); // 输入完IP确定按钮
		IP_ok.setForeground(Color.RED);
		getIP.add(IP_input);
		getIP.add(IP_ok);
		IPJF.add(getIP);
		IPJF.setVisible(true);
		IP_ok.addActionListener(this);// IP确定按钮监听

		message = new JTextArea();
		message.setEditable(false); // 使界面不能选中
		scroll = new JScrollPane(message); // 使界面能滚动
		input = new JTextField(25);
		submit = new JButton("发送");
		sendfile = new JButton("发送文件");
		download = new JButton("下载文件");
		download.setForeground(Color.red); // 红色

		panel = new JPanel();
		panel.add(input); // 输入的文字消息
		panel.add(submit); // 发送按钮
		panel.add(sendfile); // 发送文件按钮
		chat = new JPanel(new BorderLayout()); // 边界布局
		chat.add(scroll, "Center");
		chat.add(panel, "South");
		login = new JPanel(); // 登录界面
		username = new JTextField(10);
		ok = new JButton("确定");
		// 4个主界面按钮监听

		submit.addActionListener(this);
		sendfile.addActionListener(this);

		download.addActionListener(this);

		ok.addActionListener(this);

		JLabel label = new JLabel("请输入用户名"); // 标签
		label.setFont(new Font(Font.DIALOG, Font.BOLD, 28)); // new Font(String 字体，int 风格，int 字号);
		label.setForeground(Color.red);
		login.add(label);
		login.add(username);
		login.add(ok);
		login.add(download); // 下载文件
		link = new JTextArea();
		link.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		link.setForeground(Color.blue);
		link.setEditable(false);
		login.add(link);

		chat.add(login, "North");
		this.add(chat, "Center");
		this.setResizable(false);
		this.setBounds(200, 200, 600, 400); // x,y,width,high
		this.setVisible(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 能退出
	}

	// @SuppressWarnings("unused")
	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * IP_ok按钮按下即开启信息Socket，如何IP不对会造成服务器无法连接的情况，有显示
		 */
		if (e.getSource() == IP_ok) {
			try {
				IP = IP_input.getText();
				socket = new Socket(IP, port);
				JOptionPane.showMessageDialog(this, "连接成功"); // 显示连接成功
				new Thread(this).start(); // 线程开始
				os = socket.getOutputStream(); // 获得输出流
				bw = new BufferedWriter(new OutputStreamWriter(os)); // 写
				IPJF.setVisible(false); // 关闭IP输入窗口
				this.setVisible(true); // 打开主界面

			} catch (Exception e1) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(this, "服务器连接失败");
				System.exit(0); // 考虑到客户端和服务器不在一台电脑上
			}

			/*
			 * 发送按钮 读取输入信息到服务器
			 */
		} else if (e.getSource() == submit) {
			if ("".equals(uname)) {
				JOptionPane.showMessageDialog(this, "没有输入用户名!"); // 弹出窗口
			} else {
				// 如果点击提交按钮，则表示需要将信息发送出去。
				String str = null;
				str = uname + " : " + input.getText(); // 输出输入的内容
				try {
					bw.write(str); // 将str写入流里面
					bw.newLine();
					// System.out.println("客户端发送了："+str);
					bw.flush();

					// System.out.println(str);
				} catch (IOException e1) {
					System.out.println("无法发送，服务器连接异常！");
				}
				input.setText("");
			}

			/*
			 * 连接进入后输入名字,会在服务器上进行显示
			 */

		} else if (e.getSource() == ok) {// 用户登录
			uname = username.getText();// 获取文本框输入的文本信息

			if ("".equals(uname)) {
				JOptionPane.showMessageDialog(this, "用户名不能为空！"); // 弹出窗口
			} else {
				if (flag) {
					try {
						bw.write(uname);
						bw.newLine();
						bw.flush();
						flag = false;
					} catch (Exception e1) {
						// TODO: handle exception
					}
				}
				link.setText("连接成功");
				this.setTitle(uname + " 的客户端");

			}

			/*
			 * 发送文件按钮，用到了JFileChooser，先读文件再将文件以字节流进行发送
			 */
		} else if (e.getSource() == sendfile) {

			if ("".equals(uname)) {
				JOptionPane.showMessageDialog(this, "没有输入用户名!"); // 弹出窗口
			} else {
				int b = 0;
				JFileChooser jfc = new JFileChooser("c://11"); // 默认打开C://

				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int value = jfc.showDialog(new JLabel(), "发送文件");
				if (value == JFileChooser.APPROVE_OPTION) {
					b = 1;
				}
				if (b == 1) {
					f = jfc.getSelectedFile();
					try {
						datasocket = new Socket(IP, file_port);
						fis = new FileInputStream(f);
						dos = new DataOutputStream(datasocket.getOutputStream());
						dos.writeUTF(uname);
						dos.writeUTF(f.getName());
						dos.flush();
						dos.writeLong(f.length());
						dos.flush();

						byte[] bytes = new byte[1024];
						int length = 0;
						// long progress = 0;
						while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
							dos.write(bytes, 0, length);
							dos.flush();
							// progress += length;
						}
						if (fis != null)
							fis.close();
						if (dos != null)
							dos.close();
						datasocket.close(); // 发送完毕关闭连接

						// message.append(uname + "文件成功发送到客户端\r\n");

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.out.println(e);
					}
				}
			}
			/*
			 * 文件下载按钮，涉及到服务器传递服务器文件内容，根据选择确定文件内容，有界面，有两个端口
			 */
		} else if (e.getSource() == download) {

			// String filepath = null;

			JFrame download = new JFrame("下载文件（输入序号）");
			JButton download_file = new JButton("下载");
			JTextField download_name = new JTextField(20);
			JPanel panel = new JPanel();

			panel.add(download_name);
			panel.add(download_file);
			download.add(panel, "Center");
			download.setBounds(800, 400, 400, 100);

			if ("".equals(uname)) {
				JOptionPane.showMessageDialog(this, "没有输入用户名!"); // 弹出窗口
			} else {

				JFrame file_path = new JFrame("输入文件储存地址");
				JPanel path_name = new JPanel();
				JButton choose = new JButton("选择");
				choose.setForeground(Color.RED);
				JTextField input_path = new JTextField(20);
				input_path.setText("E://JavaDownload");
				JButton input_ok = new JButton("确定");
				path_name.add(input_path);
				path_name.add(input_ok);
				path_name.add(choose);
				file_path.add(path_name);
				file_path.setBounds(800, 400, 400, 100);
				file_path.setVisible(true);

				choose.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						JFileChooser selectfile = new JFileChooser();

						selectfile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						selectfile.setDialogTitle("选择你储存文件的文件夹");
						int result = selectfile.showSaveDialog(input_path);
						if (result == JFileChooser.APPROVE_OPTION) {
							filepath = selectfile.getSelectedFile().getAbsolutePath();
							input_path.setText(filepath);

						}
					}

				});
				input_ok.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						filepath = input_path.getText();
						File directory = new File(filepath);
						if (directory.mkdir() || directory.exists()) {
							file_path.setVisible(false);
							JFrame show_File = new JFrame("服务器文件");
							JTextArea fileInformation = new JTextArea();
							fileInformation.setLineWrap(true);
							JScrollPane file_path_list = new JScrollPane(fileInformation);
							fileInformation.setEditable(false);
							fileInformation.setFont(new Font(Font.SERIF, Font.BOLD, 15));
							fileInformation.setForeground(Color.BLUE);
							show_File.add(file_path_list);
							show_File.setBounds(850, 100, 300, 300);
							show_File.setResizable(false);
							try {

								downloadsocket = new Socket(IP, download_port);
								getFile = new Socket(IP, file_list_port);
								// System.out.println("1111111111111");
								BufferedReader fbr = new BufferedReader(
										new InputStreamReader(getFile.getInputStream()));
								String str = fbr.readLine();

								while (str != null) {
									fileInformation.append(str + "\n");
									str = fbr.readLine();
									file_number_flag++;
								}
								getFile.close();
							} catch (Exception e1) {
								// TODO: handle exception
							}

							show_File.setVisible(true);

							download.setVisible(true);
							try {

								BufferedWriter bw = new BufferedWriter(
										new OutputStreamWriter(downloadsocket.getOutputStream()));
								download_file.addActionListener(new ActionListener() {
									DataInputStream dis;
									FileOutputStream fos;

									@Override
									public void actionPerformed(ActionEvent e) {
										boolean flag = isNumeric(download_name.getText());
										try {
											if (flag == false) {
												JOptionPane.showMessageDialog(download, "输入错误");
											} else if ((Integer.valueOf(download_name.getText()).intValue()) <= 0
													|| (Integer.valueOf(download_name.getText())
															.intValue()) > file_number_flag) {
												JOptionPane.showMessageDialog(download, "输入错误");
											} else {
												bw.write(download_name.getText());
												bw.newLine();
												bw.flush();
												dis = new DataInputStream(downloadsocket.getInputStream());
												String fileName = dis.readUTF();
												long fileLength = dis.readLong(); // fileLength是为了查看进度，这里未实现，如果不写readLong会使发送文件开头出错
												// long fileLength = dis.readLong();

												File file = new File(
														directory.getAbsolutePath() + File.separatorChar + fileName);
												fos = new FileOutputStream(file);

												byte[] bytes = new byte[1024];
												int length = 0;
												while ((length = dis.read(bytes, 0, bytes.length)) != -1) {
													fos.write(bytes, 0, length);
													fos.flush();
												}
												JOptionPane.showMessageDialog(download, "下载成功"); // 弹出窗口
												download.setVisible(false);

												show_File.setVisible(false);
												downloadsocket.close();

												// getFile = new Socket(IP, 10018);
											}
										} catch (Exception e2) {
											// TODO: handle exception
										}
									}
								});

							} catch (Exception e1) {
								// TODO: handle exception
							}

						} else {
							JOptionPane.showMessageDialog(file_path, "地址输入违法");
						}
					}

				});
			}
		}
	}

	// run方法里面的是接受服务器端信息和将信息显示的代码
	@Override
	public void run() {
		BufferedReader br = null;
		InputStream is = null;
		String str = null;
		try {
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			while (true) {
				str = br.readLine() + "\r\n";
				message.append(str); // 追加
			}

		} catch (IOException e) {
			// System.out.println("无法发送，服务器连接异常！");
			JOptionPane.showMessageDialog(this, "服务器已断开连接");
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
