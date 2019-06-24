package com.mytest.java;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.TreePath;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollBar;

public class Main extends JFrame implements ActionListener, Runnable {

	private String IP;
	private int port;
	private int ID;

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date = new Date();
	String time = df.format(date);
	JPopupMenu popMenu;
	JMenuItem item1 = new JMenuItem("发送消息");
	JMenuItem item2 = new JMenuItem("接收消息");
	JMenuItem item3 = new JMenuItem("查看资料");
	JMenuItem item4 = new JMenuItem("聊天记录"); // 这里记录1个ID收到的所有的记录
	JMenuItem item5 = new JMenuItem("删除好友");

	String message = null;
	String save = "";
	JButton find;
	JButton update;
	JButton findfriend;
	JButton stranger;
	JButton login;
	private JPanel contentPane;
	JList list;
	FindFriend findf;
	int tempgetid;

	Vector friendnames = new Vector();
	int friendnum;// friend number
	private String[] picsonline = new String[] { "pic//1.jpg", "pic//3.jpg", "pic//5.jpg", "pic//7.jpg" };
	private String[] picsoffline = new String[] { "pic//2.jpg", "pic//4.jpg", "pic//6.jpg", "pic//8.jpg" };
	Vector friendIDs = new Vector();
	Vector fridendnicks = new Vector();
	Vector friendips = new Vector();
	Vector friendemail = new Vector();
	Vector friendselfinfo = new Vector();
	Vector picno = new Vector();
	Vector status = new Vector();

	Vector tempname = new Vector();
	Vector tempid = new Vector();
	Vector tempip = new Vector();
	Vector temppic = new Vector();
	Vector tempstatus = new Vector();
	Vector whoaddmesip = new Vector();// get whoaddme as friend
	Vector tempemail = new Vector();
	Vector tempselfinfo = new Vector();

	Vector txt = new Vector();

	int index;// get list index
	int index3;// get firiend onlineinfo
	int index4;// get message from info
	boolean fromunknow = false;

	Socket socket;
	BufferedReader in;
	PrintWriter out;
	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendSocket, receiveSocket;
	int udpPORT = 5000;//
	int sendPort = 5000;// 单机调试请改动这里，谢谢！！！！！
	byte array[] = new byte[1024];
	Thread thread;
	int myID;
	String received;
	private JButton main;

	//// 以下是程序界面的变量

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void ConnectServer(int myid) {
		try {
			socket = new Socket(IP, port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

			// this is call my friend info
			// 以下读取好友信息
			out.println("friend");
			out.println(myid);
			friendnum = Integer.parseInt(in.readLine());
			String friendname = " ";

			String friendjicqno, friendip, friendstatus, picinfo, email, infos;
			friendname = in.readLine();
			while (!friendname.equals("over")) {
				System.out.println(friendname);
				friendnames.add(friendname);
				System.out.println(friendnames.get(0).toString().trim());

				friendjicqno = in.readLine();
				friendIDs.add(new Integer(friendjicqno));
				System.out.println("Friend ID ----------------" + friendjicqno);
				friendip = in.readLine();
				friendips.add(friendip);
				friendstatus = in.readLine();
				status.add(friendstatus);
				picinfo = in.readLine();
				picno.add(new Integer(picinfo));
				email = in.readLine();
				friendemail.add(email);
				infos = in.readLine();
				friendselfinfo.add(infos);
				friendname = in.readLine();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("false");
		}
		// 以下在列表中显示好友
		DefaultListModel mm = (DefaultListModel) list.getModel();
		int picid;
		for (int p = 0; p < friendnames.size(); p++) {
			picid = Integer.parseInt(picno.get(p).toString());
			if (status.get(p).equals("1")) {
				mm.addElement(new Object[] { friendnames.get(p), new ImageIcon(picsonline[picid]) });
			} else {
				mm.addElement(new Object[] { friendnames.get(p), new ImageIcon(picsoffline[picid]) });
			}
		}
	}

	public Main(int ID, String ip, int port) {

		this.ID = ID;
		this.IP = ip;
		this.port = port;

		this.setTitle("ID=" + ID + ".............客户端");
		popMenu = new JPopupMenu();
		popMenu.add(item1);
		item1.setEnabled(false);
		item1.addActionListener(this);
		popMenu.add(item2);
		item2.setEnabled(false);
		item2.addActionListener(this);
		popMenu.add(item3);
		item3.setEnabled(false);
		item3.addActionListener(this);
		popMenu.add(item4);
		item4.setEnabled(false);
		item4.addActionListener(this);
		popMenu.add(item5);
		item5.setEnabled(false);
		item5.addActionListener(this);
		setBounds(100, 100, 450, 486);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		ListModel model = new NameAndPicListModel(friendnames, picsonline);
		ListCellRenderer renderer = new NameAndPicListCellRenderer();
		list = new JList(model);
		list.setCellRenderer(renderer);
		JScrollPane s = new JScrollPane(list);
		s.setBounds(220, 10, 174, 308);
		list.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (list.getSelectedIndex() == -1) {
					return;
				}

				else if (e.getButton() == 3) {
					popMenu.show(list, e.getX(), e.getY());

				}
			}

		});
		contentPane.add(s);

		JLabel lable = new JLabel("我的好友");
		lable.setBounds(92, 136, 216, 51);
		contentPane.add(lable);
		find = new JButton("查找");
		find.setEnabled(false);
		find.addActionListener(this);
		find.setBounds(10, 338, 93, 23);
		contentPane.add(find);

		update = new JButton("更新");
		update.setEnabled(false);
		update.addActionListener(this);
		update.setBounds(331, 338, 93, 23);
		contentPane.add(update);

		findfriend = new JButton("直接加友");
		findfriend.setEnabled(false);

		findfriend.addActionListener(this);
		findfriend.setBounds(75, 381, 93, 23);
		contentPane.add(findfriend);

		stranger = new JButton("陌生人(消息)");
		stranger.setEnabled(false);
		stranger.addActionListener(this);
		stranger.setBounds(220, 381, 118, 23);
		contentPane.add(stranger);

		login = new JButton("上线");
		login.addActionListener(this);
		login.setBounds(147, 414, 93, 23);
		contentPane.add(login);

		main = new JButton("用户信息维护");
		main.setEnabled(false);
		main.setBounds(127, 338, 174, 23);
		contentPane.add(main);
		main.addActionListener(this);

		CreatUDP();
		ConnectServer(ID);

		thread = new Thread(this);
		thread.start();
		this.setVisible(true);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // 关键 点击关闭不会在这关闭 关键代码
		// DO_NOTHING_ON_CLOSE（在 WindowConstants 中定义）：不执行任何操作；要求程序在已注册的 WindowListener
		// 对象的 windowClosing 方法中处理该操作。
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					String whoips;
					String s = "offline" + ID;
					s.trim();
					System.out.println(s);
					byte[] data = s.getBytes();
					for (int i = 0; i < whoaddmesip.size(); i++) {
						whoips = whoaddmesip.get(i).toString().trim();
						sendPacket = new DatagramPacket(data, s.length(), InetAddress.getByName(whoips), sendPort);
						sendSocket.send(sendPacket);// 通知好友我下线了
					} // for
				} catch (IOException e2) {
					// sendtext.append(sendtext.getText());
					e2.printStackTrace();
				}
				// end offline

				// 告诉服务器我下线了
				out.println("logout");
				out.println(ID);
				out.println("save");
				out.println(ID);
				for (int i = 0; i < txt.size(); i++) {
					out.println(txt.get(i).toString());
					System.out.println(txt.get(i).toString());
				}
				out.println("over");
				System.exit(0);

			}

		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void run() {
		while (true) {
			// System.out.println("系统当前时间 ："+df.format(date));
			try {
				for (int x = 0; x < 512; x++)
					array[x] = ' ';
				// 创建数据报
				receivePacket = new DatagramPacket(array, array.length);
				receiveSocket.receive(receivePacket);
				byte[] data = receivePacket.getData();
				String infofromip = receivePacket.getAddress().getHostAddress().toString().trim();
				index3 = 0;
				received = new String(data, 0, data.length);
				received.trim();
				String tempstr;
				int tx;
				if (received.substring(0, 6).equals("online")) {// 如果有好友上线就变彩色
					tempstr = received.substring(6).trim();
					tempgetid = Integer.parseInt(tempstr);
					System.out.println("id" + tempgetid);
					while (index3 < friendnames.size()) {
						tx = Integer.parseInt(friendIDs.get(index3).toString());
						System.out.println("tx------------" + tx + "11111" + tempgetid);
						if (tempgetid == tx)
							break;
						index3++;
					}
					System.out.println("index--------" + index3);
					friendips.setElementAt(infofromip, index3);
					// status.setElementAt(,index3);
					// System.out.println(index3);
					DefaultListModel mm3 = (DefaultListModel) list.getModel();
					int picid = Integer.parseInt(picno.get(index3).toString());
					mm3.setElementAt(new Object[] { friendnames.get(index3), new ImageIcon(picsonline[picid]) },
							index3);
				} // end online
					// friend offline
				else if (received.substring(0, 7).equals("offline")) {// 如果有好友下线就变灰色
					tempstr = received.substring(7).trim();
					System.out.println("str" + tempstr);
					tempgetid = Integer.parseInt(tempstr);
					System.out.println("id" + tempgetid);
					while (index3 < friendnames.size()) {
						tx = Integer.parseInt(friendIDs.get(index3).toString());
						System.out.println("tx" + tx);
						if (tempgetid == tx)
							break;
						index3++;
					}
					System.out.println("index3---------" + index3);
					infofromip = "null";
					friendips.setElementAt(infofromip, index3);
					// status.setElementAt(,index3);
					System.out.println(index3 + "11111111111111111111111测试函数");
					DefaultListModel mm3 = (DefaultListModel) list.getModel();
					int picid = Integer.parseInt(picno.get(index3).toString());
					mm3.setElementAt(new Object[] { friendnames.get(index3), new ImageIcon(picsoffline[picid]) },
							index3);

				} else if (received.substring(0, 9).equals("oneaddyou")) {
					tempstr = received.substring(9).trim();
					System.out.println("str" + tempstr);
					tempgetid = Integer.parseInt(tempstr);
					System.out.println("id" + tempgetid);
					JOptionPane.showMessageDialog(this, "收到" + tempgetid + "addyou", "ok",
							JOptionPane.INFORMATION_MESSAGE);

				} else {// 否则就显示收到消息
					index4 = 0;
					// String
					// infofromip=receivePacket.getAddress().getHostAddress().toString().trim();
					do {
						String friendip = friendips.get(index4).toString().trim();
						if (infofromip.equals(friendip)) {
							String nameinfo = friendnames.get(index4).toString().trim();
							String id = friendIDs.get(index4).toString().trim();
							JOptionPane.showMessageDialog(this, "收到" + nameinfo + "的消息", "ok",
									JOptionPane.INFORMATION_MESSAGE);

							String log = time + "        收到    昵称：" + nameinfo + "    ID:    " + id + ":" + received
									+ "\n";
							save += log.trim() + "\n";
							new GetData();
							fromunknow = false;

							txt.addElement(log.trim());

							break;

						} // if
						index4++;
						if (index4 >= friendnames.size()) {
							fromunknow = true;// 收到陌生人的消息
							JOptionPane.showMessageDialog(this, "收到陌生人" + infofromip + "的消息，及时查看（陌生人消息不会保存）", "ok",
									JOptionPane.INFORMATION_MESSAGE);
							String log = time + "        收到   陌生人" + ":" + received + "\n";
							save += log.trim() + "\n";
							txt.addElement(log.trim());
						}

					} while (index4 < friendnames.size());// while
					System.out.println(index4);

				}

			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "发送未知错误，如果客户端不能正常运行，请重启客户端", "提示", JOptionPane.ERROR_MESSAGE);
				System.out.println(ex);
			}
		}

	}// run end
		// **********************
		// 以下创建数据报

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == find) {
			new FindFriend(ID, IP, port, friendIDs);

		} else if (e.getSource() == main) {
			out.println("information");
			String id = Integer.toString(ID);
			new UserInfo(id, in, out);
		} else if (e.getSource() == login) {
			item1.setEnabled(true);
			item2.setEnabled(true);
			item3.setEnabled(true);
			item4.setEnabled(true);
			item5.setEnabled(true);
			find.setEnabled(true);
			update.setEnabled(true);
			findfriend.setEnabled(true);
			stranger.setEnabled(true);
			main.setEnabled(true);
			login.setEnabled(false);
			out.println("getwhoaddme");
			out.println(ID);

			String whoip = " ";
			do {
				try {
					whoip = in.readLine().trim();
					if (whoip.equals("over"))
						break;
					whoaddmesip.add(whoip);
				} catch (IOException s) {
					System.out.println("false getwhoaddme");
				}
			} while (!whoip.equals("over"));
			for (int i = 0; i < whoaddmesip.size(); i++) {
				System.out.println("谁加了我" + whoaddmesip.get(i));
			}
			try {
				String whoips;
				String s = "online" + ID;
				s.trim();
				System.out.println(s);
				byte[] data = s.getBytes();
				for (int i = 0; i < whoaddmesip.size(); i++) {
					whoips = whoaddmesip.get(i).toString().trim();
					sendPacket = new DatagramPacket(data, s.length(), InetAddress.getByName(whoips), sendPort);
					sendSocket.send(sendPacket);
				} // for
			} catch (IOException e2) {
				// sendtext.append(sendtext.getText());
				e2.printStackTrace();
				System.exit(1);
			}

		} ///// end tellfrienonline
		else if (e.getSource() == stranger) {
			if (fromunknow)
				new GetData();
			else
				JOptionPane.showMessageDialog(null, "没有消息", "消息", 1);
		} else if (e.getSource() == update) {

			tempname = FindFriend.tmpname;
			System.out.println("大小、、、、、、、、、、、、、、、、、、、、" + tempname.size());
			tempid = FindFriend.tmpid;
			tempip = FindFriend.tmpip;
			temppic = FindFriend.tmppic;
			tempstatus = FindFriend.tmpstatus;
			tempemail = FindFriend.tmpemail;
			tempselfinfo = FindFriend.tmpselfinfo;
			DefaultListModel mm2 = (DefaultListModel) list.getModel();
			int picid = 0;
			for (int p = 0; p < tempname.size(); p++) {
				picid = Integer.parseInt(temppic.get(p).toString());
				if (tempstatus.get(p).toString().equals("1")) {
					mm2.addElement(new Object[] { tempname.get(p), new ImageIcon(picsonline[picid]) });
				} else {
					mm2.addElement(new Object[] { tempname.get(p), new ImageIcon(picsoffline[picid]) });
				}
				// picid=Integer.parseInt(temppic.get(p).toString());
				// mm2.addElement(new Object[]{tempname.get(p),new
				// ImageIcon(picsonline[picid])});

			} // for
				// add to friendlist
			for (int k = 0; k < tempname.size(); k++) {
				friendnames.add(tempname.get(k));
				friendIDs.add(tempid.get(k));
				friendips.add(tempip.get(k));
				picno.add(temppic.get(k));
				status.add(tempstatus.get(k));
				friendemail.add(tempemail.get(k));
				friendselfinfo.add(tempselfinfo.get(k));
			} // for
				// clean tmp
			for (int p = 0; p < tempname.size(); p++) {
				FindFriend.tmpip.removeAllElements();
				FindFriend.tmpid.removeAllElements();
				FindFriend.tmpname.removeAllElements();
				FindFriend.tmppic.removeAllElements();
				FindFriend.tmpstatus.removeAllElements();
				FindFriend.tmpemail.removeAllElements();
				FindFriend.tmpid.removeAllElements();
			}

			JOptionPane.showMessageDialog(null, "更新成功", "消息", 1);

		} else if (e.getSource() == findfriend) {
			String fid = JOptionPane.showInputDialog(null, "请输入你要查找的ID", "加好友", 1);
			boolean flag = true;
			if (fid != null) {
				if (!isNumeric(fid)) {
					flag = false;
				}
				if (isNumeric(fid)) {
					// if (Integer.parseInt(fid) == ID) {
					// flag = false;
					// }
					for (int i = 0; i < friendIDs.size(); i++) {
						if (fid.equals(friendIDs.get(i).toString())) {
							flag = false;
						}
					}
				}

				if (flag) {
					out.println("addnewfriend");
					out.println(fid.trim());
					out.println(ID);
					String thename = "";
					try {
						thename = in.readLine();
						if (!thename.equals("false")) {

							String thejicqno, theip, thestatus, picinfo, email, infos;
							while (!thename.equals("over")) {
								friendnames.add(thename);
								thejicqno = in.readLine();
								friendIDs.add(new Integer(thejicqno));
								theip = in.readLine();
								friendips.add(theip);
								thestatus = in.readLine();
								status.add(thestatus);
								picinfo = in.readLine();
								picno.add(new Integer(picinfo));
								email = in.readLine();
								friendemail.add(email);
								infos = in.readLine();
								friendselfinfo.add(infos);
								thename = in.readLine();
							}

							int dddd = friendnames.size() - 1;
							DefaultListModel mm2 = (DefaultListModel) list.getModel();
							int picid;
							picid = Integer.parseInt(picno.get(dddd).toString());
							if (status.get(dddd).equals("1")) {
								mm2.addElement(
										new Object[] { friendnames.get(dddd), new ImageIcon(picsonline[picid]) });
							} else {
								mm2.addElement(
										new Object[] { friendnames.get(dddd), new ImageIcon(picsoffline[picid]) });
							}
							JOptionPane.showMessageDialog(null, "添加成功", "消息", 1);
						} else {
							JOptionPane.showMessageDialog(null, "ID不存在", "消息", 1);
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "发送错误，可能原因\n1：已经添加过好友(消除).\n2：加自身为好友.\n3：输入错误", "消息", 1);
				}
			}
		} else if (e.getSource() == item1) {
			try {
				new SendData();
			} catch (SocketException e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == item2) {

			new GetData();

		} else if (e.getSource() == item3) {
			int index = list.getSelectedIndex();
			String nick = friendnames.get(index).toString().trim();
			String id = friendIDs.get(index).toString().trim();
			String ip = friendips.get(index).toString().trim();
			String email = friendemail.get(index).toString().trim();
			String info = friendselfinfo.get(index).toString().trim();
			Information information = new Information(nick, id, ip, email, info);
			information.setVisible(true);
		} else if (e.getSource() == item4) {
			try {
				String txt = "";

				out.println("txt");
				out.println(ID);
				String s = in.readLine();
				if (s.equals("false")) {
					JOptionPane.showMessageDialog(null, "没有消息记录", "消息", 1);
				} else {
					System.out.print(s);
					while (!s.equals("over")) {

						// String separator = System.getProperty("line.separator");
						txt += s + "\n";

						s = in.readLine();

					}
					txt += "截止到: " + time;
					new MessageText(txt);
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} else if (e.getSource() == item5) {
			if (Integer.toString(ID).equals(friendIDs.get(list.getSelectedIndex()).toString())) {
				JOptionPane.showMessageDialog(this, "不能删除自身", "提醒", JOptionPane.INFORMATION_MESSAGE);
			} else {
				out.println("delfriend");
				int index2;
				index2 = list.getSelectedIndex();

				out.println(friendIDs.get(index2));// the friendjicq to del
				out.println(ID);// my jicqno
				DefaultListModel mm = (DefaultListModel) list.getModel();
				mm.removeElementAt(index2);
				friendnames.removeElementAt(index2);
				friendips.removeElementAt(index2);
				friendIDs.removeElementAt(index2);
				picno.removeElementAt(index2);
				status.removeElementAt(index2);
				friendemail.removeElementAt(index2);
				friendselfinfo.removeElementAt(index2);
			}
		}
	}

	public void CreatUDP() {
		try {
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(udpPORT);

		} catch (SocketException se) {
			// se.printStackTrace();
			JOptionPane.showMessageDialog(null, "涉及到端口占用，暂时允许一个客户端登录，功能待完善", "已经退出", 1);
			System.exit(0);
			System.out.println("false udp");
		}
	}

	class GetData extends JDialog implements ActionListener {

		private JPanel contentPane;
		JButton ok;
		JTextArea recive;

		public GetData() {

			if (save != null) {

				setBounds(100, 100, 450, 300);
				contentPane = new JPanel();
				contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
				setContentPane(contentPane);
				contentPane.setLayout(null);

				recive = new JTextArea();
				JScrollPane s = new JScrollPane(recive);
				recive.setText(save);
				s.setBounds(10, 30, 402, 174);
				contentPane.add(s);

				ok = new JButton("确定");
				ok.setBounds(153, 214, 93, 23);
				ok.addActionListener(this);
				contentPane.add(ok);

				JLabel lblNewLabel = new JLabel("收到的消息");
				lblNewLabel.setBounds(153, 10, 281, 15);
				contentPane.add(lblNewLabel);
				this.setVisible(true);
			} else
				JOptionPane.showMessageDialog(null, "没有消息", "消息", 1);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == ok) {
				recive.setText("");
				dispose();
			}

		}
	}

	class SendData extends JDialog implements ActionListener {
		private final JPanel contentPanel = new JPanel();

		JButton ok;
		JButton cancel;
		JTextArea input;

		int index = list.getSelectedIndex();
		String name = friendnames.get(index).toString();
		String id = friendIDs.get(index).toString();
		String ip = friendips.get(index).toString();
		Socket socket;
		BufferedReader in;
		PrintWriter out;

		public SendData() throws SocketException {

			setBounds(100, 100, 450, 300);
			getContentPane().setLayout(new BorderLayout());
			contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(contentPanel, BorderLayout.CENTER);
			contentPanel.setLayout(null);

			JLabel label_1 = new JLabel("对方昵称:");
			label_1.setBounds(10, 10, 73, 15);
			contentPanel.add(label_1);

			JTextArea nickname_from = new JTextArea();
			nickname_from.setEnabled(false);
			nickname_from.setText(name);
			nickname_from.setBounds(67, 6, 123, 24);
			contentPanel.add(nickname_from);

			JLabel label_2 = new JLabel("对方ID:");
			label_2.setBounds(200, 5, 91, 24);
			contentPanel.add(label_2);

			JTextArea nickname_to = new JTextArea();
			nickname_to.setText(id);
			nickname_to.setEnabled(false);
			nickname_to.setBounds(266, 6, 140, 24);
			contentPanel.add(nickname_to);

			JLabel label_3 = new JLabel("发送内容:");
			label_3.setBounds(10, 50, 100, 24);
			contentPanel.add(label_3);

			input = new JTextArea();
			input.setBounds(20, 84, 404, 125);
			contentPanel.add(input);

			ok = new JButton("发送");
			ok.addActionListener(this);

			ok.setBounds(226, 219, 93, 23);
			contentPanel.add(ok);

			cancel = new JButton("取消");
			cancel.setBounds(329, 219, 93, 23);
			contentPanel.add(cancel);
			this.setVisible(true);
			this.setResizable(false);

		}

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				String s = input.getText();

				// System.out.println(s+"5454154154545555555554854847887");
				byte[] data = s.getBytes();
				System.out.println("朋友的IP。。。。。。。" + ip);
				ip.trim();
				if (ip.equals("null") || ip.equals("") || ip.equals("0")) {
					JOptionPane.showMessageDialog(this, "：-（对不起,不在线", "ok", JOptionPane.INFORMATION_MESSAGE);
				} else {
					sendPacket = new DatagramPacket(data, data.length, InetAddress.getByName(ip), sendPort);
					sendSocket.send(sendPacket);
				}
				dispose();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}

	}

	class NameAndPicListModel extends DefaultListModel {
		public NameAndPicListModel(Vector friendnames, String[] pics) {
			for (int i = 0; i < friendnames.size(); ++i) {
				addElement(new Object[] { friendnames.get(i), new ImageIcon(pics[i]) });
			}
		}

		public String getName(Object object) {
			Object[] array = (Object[]) object;
			return (String) array[0];
		}

		public Icon getIcon(Object object) {
			Object[] array = (Object[]) object;
			return (Icon) array[1];
		}
	}

	class NameAndPicListCellRenderer extends JLabel implements ListCellRenderer {
		private Border lineBorder = BorderFactory.createLineBorder(Color.red, 2),
				emptyBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);

		public NameAndPicListCellRenderer() {
			setOpaque(true);
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			NameAndPicListModel model = (NameAndPicListModel) list.getModel();
			setText(model.getName(value));
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
