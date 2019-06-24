package com.MySocket.java;

import java.awt.Color;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.management.ListenerNotFoundException;
import javax.management.loading.PrivateClassLoader;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyServer {
	public static List<Socket> list = new ArrayList<Socket>(); // 保存Socket集合，即通道
	public static List<String> nameList = new ArrayList<>(); // 保存名字

	public static void main(String[] args) {
		new ServerFrame("服务器端");
	}

}

/*
 * 服务器,进来一个客户端开启几个对应进程并进行相应功能的等待连接，当客户端启动功能时，对应连接会被开启，进行传输.退出客户端直接退出进程
 */
class ServerFrame extends JFrame implements ActionListener, Runnable {
	private static final long serialVersionUID = 1L; // ？？？？？？？？？？？？？？？？？？不懂
	private static boolean flag = true; // 用来标志是否输入的姓名
	private static String Server_Save_File = "C:\\333"; // 服务器接收文件的储存地址
	private static int name_flag = 0;

	private static boolean Server_flag = true;
	String address = null;
	private static int port = 6666;
	private static int file_port = 9988;
	private static int send_port = 5555;
	private static int file_list_port = 10018;
	String name = null;
	private ServerSocket ss = null;
	private Socket s;
	private ServerSocket datass;
	private Socket datas;
	private ServerSocket sendss;
	private ServerSocket getfiless;
	private Socket getfiles;
	private Socket sends;
	private JButton start;
	private JButton end;
	private JPanel server;
	private JTextArea information;
	private ScrollPane scroll;
	private JTextArea message;

	String filepath = Server_Save_File;// D盘下的file文件夹的目录
	File file = new File(filepath);// File类型可以是文件也可以是文件夹
	File[] fileList = file.listFiles();// 将该目录下的所有文件放置在一个File类型的数组中

	public ServerFrame(String name) {
		
		super(name);
		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); // windows风格
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel") ;
			// // Mac风格
			// UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel") ; //
			// Java默认风格
		} catch (Exception e) {
			System.out.println("windows风格设置失败");
			System.exit(0);
		}

		start = new JButton("启动服务器");
		end = new JButton("退出服务器");
		server = new JPanel();
		server.add(start);
		server.add(end);
		end.setForeground(Color.red);
		information = new JTextArea();
		start.addActionListener(this);
		end.addActionListener(this);
		message = new JTextArea();
		message.setEditable(false);
		information.setEditable(false);
		information.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
		information.setForeground(Color.GREEN);
		information.append("客户端发送的文件保存在" + Server_Save_File);
		scroll = new ScrollPane();
		scroll.add(message);
		this.add(server, "North");
		this.add(scroll, "Center");
		this.add(information, "South");
		message.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
		message.setForeground(Color.blue);

		this.setResizable(false);
		this.setBounds(0, 0, 400, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	@Override
	public void run() {
		InputStream is = null;
		BufferedReader br = null;
		while (true) {
			try {
				// if (!Server_flag) {
				// break;
				// } else {
				address = null;
				s = ss.accept();
				MyServer.list.add(s);
				flag = true;

				is = s.getInputStream();
				br = new BufferedReader(new InputStreamReader(is));
				// System.out.println(str);
				address = s.getInetAddress().getHostAddress(); // ip地址
				message.append("\r\nip" + address + ":" + port + "连接");

				name = br.readLine();
				MyServer.nameList.add(name);
				// port = s.getPort();
				if (flag) {
					message.append("\r\n用户=》" + name + " ip" + address + "连接进入聊天室");
					flag = false;
				}

				new sendThread(s, name_flag).start(); // 聊天线程
				name_flag++;
				new FileSaveThread(datas).start(); // 文件储存线程

				new FileSendThread(sends).start(); // 文件发送线程，对应客户端下载

				new FileInThread(getfiles).start(); // 得到服务器文件的内容（只有文件名）
				// }
			} catch (Exception e) {
				try {
					// TODO: handle exception
					if (!address.equals(null))
						message.append("\r\nip" + address + "未输入姓名退出");
				} catch (Exception e1) {
					// TODO: handle exception
					// System.out.println(e);
				}
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == start) {
			try {
				ss = new ServerSocket(port);
				datass = new ServerSocket(file_port);
				sendss = new ServerSocket(send_port);
				getfiless = new ServerSocket(file_list_port);
				Server_flag = true;
				message.setText("服务器启动");

				// 进程
				new Thread(this).start();

			} catch (Exception e1) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(this, "服务器已经开启"); // 弹出窗口
			}
		} else if (e.getSource() == end) {
			try {
				ss.close();
				datass.close();
				sendss.close();
				getfiless.close();
				JOptionPane.showMessageDialog(this, "服务器关闭");
				System.exit(0);
				// Server_flag = false;
			} catch (Exception e1) {
				// TODO: handle exception
				System.out.println(e1);
			}
		}
	}

	class sendThread extends Thread {
		private Socket s;
		private int flag;

		public sendThread(Socket s, int flag) {
			super();
			this.s = s;
			this.flag = flag;

		}

		public void run() {
			if (Server_flag) {
				InputStream is = null;
				BufferedReader br = null;
				String str = null;
				OutputStream os = null;
				BufferedWriter bw = null;
				try {
					while (true) {
						is = s.getInputStream();
						br = new BufferedReader(new InputStreamReader(is));
						str = br.readLine();
						// System.out.println(str);
						for (Socket sr : MyServer.list) {
							os = sr.getOutputStream();
							bw = new BufferedWriter(new OutputStreamWriter(os));
							bw.write(str);
							bw.newLine();
							bw.flush();
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
					MyServer.list.remove(s);
					message.append("\r\n用户=》" + MyServer.nameList.get(flag) + "：退出");
				}
			}
		}

	}

	class FileSaveThread extends Thread {
		private Socket s;
		private DataInputStream dis;
		private FileOutputStream fos;

		public FileSaveThread(Socket socket) {
			this.s = socket;

		}

		@Override
		public void run() {
			// long fileLength = dis.readLong();
			File directory = new File(Server_Save_File);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			while (true) {
				try {
					s = datass.accept();
					dis = new DataInputStream(s.getInputStream());
					// System.out.println("进入循环");
					name=dis.readUTF();
					String fileName = dis.readUTF();
					long fileLength = dis.readLong();   //fileLength是为了查看进度，这里未实现，如果不写readLong会使发送文件开头出错
					File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
					fos = new FileOutputStream(file);
					byte[] bytes = new byte[1024];
					// int length = 0;
					while (true) {
						int read = 0;
						read = dis.read(bytes);
						if (read == -1)
							break;
						fos.write(bytes, 0, read);
						fos.flush();

					}
					if (dis != null)
						dis.close();
					if (fos != null)
						fos.close();
					s.close();
					for (Socket sr : MyServer.list) {
						OutputStream os = sr.getOutputStream();
						BufferedWriter fbw = new BufferedWriter(new OutputStreamWriter(os));
						fbw.write(name + "：发送文件 " + fileName + "到服务器");
						fbw.newLine();
						fbw.flush();
					}
					s.close();
					// System.out.println("发送成功");

				} catch (Exception e) {
					// System.out.println("退出，避免无限循环");
					break;
				}
			}

		}
	}

	class FileSendThread extends Thread {
		private Socket s;

		public FileSendThread(Socket s) {
			this.s = s;

		}

		FileInputStream fis;
		DataOutputStream dos;

		@Override
		public void run() {
			while (true) {

				try {
					s = sendss.accept();
					BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
					String str = br.readLine();

					File file = new File(
							Server_Save_File + "\\" + fileList[Integer.valueOf(str).intValue() - 1].getName());
					FileInputStream fis = new FileInputStream(file);
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());

					dos.writeUTF(file.getName());
					dos.flush();
					dos.writeLong(file.length());
					dos.flush();

					// System.out.println("开始传输文件");
					byte[] bytes = new byte[1024];
					int length = 0;
					long progress = 0;
					while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
						dos.write(bytes, 0, length);
						dos.flush();
						progress += length;
					}
					// System.out.println("文件传输成功");

				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					try {
						if (fis != null)
							fis.close();
						if (dos != null)
							dos.close();
						s.close();

					} catch (Exception e) {
						break;
					}
				}
			}

		}
	}

	class FileInThread extends Thread {
		private Socket s;

		public FileInThread(Socket s) {
			this.s = s;
		}

		@Override
		public void run() {
			while (true) {
				try {
					s = getfiless.accept();
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
					for (int i = 0; i < fileList.length; i++) {
						bw.write((i + 1) + "：..........................." + fileList[i].getName());
						bw.newLine();
						bw.flush();
					}
					bw.close();
					s.close();

				} catch (Exception e) {

					break;
				}
			}
		}
	}

}