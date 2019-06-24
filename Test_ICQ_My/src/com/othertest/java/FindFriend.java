package com.othertest.java;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

public class FindFriend extends JFrame implements ActionListener {

	private JPanel contentPane;
	private int id;
	private String ip;
	private int port;

	JList list;

	JPopupMenu popMenu;
	JMenuItem item1 = new JMenuItem("查看资料");
	JMenuItem item2 = new JMenuItem("加为好友");
	Vector nickname = new Vector();
	Vector sex = new Vector();
	Vector place = new Vector();
	Vector f_id = new Vector();
	Vector f_ip = new Vector();
	Vector pic = new Vector();
	Vector status = new Vector();
	Vector emails = new Vector();
	Vector selfinfos = new Vector();
	// 以下临时保存好友的呢称，性别等信息
	static Vector tmpid = new Vector();// jicqid
	static Vector tmpname = new Vector();// jicqname
	static Vector tmpip = new Vector();// ip
	static Vector tmppic = new Vector();// pic info
	static Vector tmpstatus = new Vector();// status
	static Vector tmpemail = new Vector();
	static Vector tmpselfinfo = new Vector();
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	int sendPort = 5001;
	DatagramPacket sendPacket;
	DatagramSocket sendSocket;

	JButton find;
	JButton next;
	JButton up;

	JButton cancel;

	Vector IDs;

	public FindFriend(int id, String ip, int port, Vector IDs) {
		this.id = id;
		this.ip = ip;
		this.port = port;
		this.IDs = IDs;

		try {
			sendSocket = new DatagramSocket();
			socket = new Socket(ip, port);

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (Exception e) {
		}

		setBounds(100, 100, 401, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lable = new JLabel("下面是在注册的朋友");
		lable.setBounds(141, 10, 128, 29);
		contentPane.add(lable);
		popMenu = new JPopupMenu();
		popMenu.add(item1);
		item1.addActionListener(this);
		popMenu.add(item2);
		item2.addActionListener(this);
		ListModel model = new FindListModel(nickname, sex, place, f_ip);// 列表模型
		list = new JList(model);
		ListCellRenderer renderer = new FindListCellRenderer();
		list.setCellRenderer(renderer);
		JScrollPane s = new JScrollPane(list);
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
		s.setBounds(22, 40, 338, 139);
		contentPane.add(s);

		find = new JButton("查找");
		find.addActionListener(this);
		find.setBounds(10, 208, 93, 23);
		contentPane.add(find);

		next = new JButton("next");
		next.setBounds(102, 208, 93, 23);
		contentPane.add(next);

		up = new JButton("up");
		up.setBounds(195, 208, 93, 23);
		contentPane.add(up);

		cancel = new JButton("cancel");
		cancel.setBounds(287, 208, 93, 23);
		contentPane.add(cancel);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == find) { // 查找好友
			out.println("find");
			DefaultListModel mm = (DefaultListModel) list.getModel();
			///////////////// find friend info
			try {
				String s = " ";
				s = in.readLine();
				// 从服务器读取好友信息
				while (!s.equals("over")) {

					nickname.add(s);
					sex.add(in.readLine());
					place.add(in.readLine());
					f_ip.add(in.readLine());
					emails.add(in.readLine());
					selfinfos.add(in.readLine());
					s = in.readLine();
				}
				int theirjicq, picinfo, sta;
				for (int x = 0; x < nickname.size(); x++) {
					theirjicq = Integer.parseInt(in.readLine());
					// System.out.println(theirjicq);
					f_id.add(new Integer(theirjicq));
					picinfo = Integer.parseInt(in.readLine());
					pic.add(new Integer(picinfo));
					sta = Integer.parseInt(in.readLine());
					// System.out.println(sta);
					status.add(new Integer(sta));
					// System.out.println(jicq.get(x));
				}
				// 在列表中显示
				for (int i = 0; i < nickname.size(); i++) {
					mm.addElement(new Object[] { nickname.get(i), sex.get(i), place.get(i), f_ip.get(i) });
				} // for

			} catch (IOException e4) {
				System.out.println("false");
			}
		} else if (e.getSource() == item1) {
			int index = list.getSelectedIndex();
			String nick = nickname.get(index).toString().trim();
			String id = f_id.get(index).toString().trim();
			String ip = f_ip.get(index).toString().trim();
			String email = emails.get(index).toString().trim();
			String info = selfinfos.get(index).toString().trim();
			Information information = new Information(nick, id, ip, email, info);
			information.setVisible(true);

		} else if (e.getSource() == item2) { // 加为好友
			int dd;
			boolean flag = true;
			dd = list.getSelectedIndex();
			for (int i = 0; i < IDs.size(); i++) {
				if (f_id.get(dd).equals(IDs.get(i))) {
					flag = false;
				}
			}
			if (f_id.get(dd).equals(id)) {
				flag = false;
			}
			if (flag) {
				tmpid.add(f_id.get(dd));
				tmpname.add(nickname.get(dd));
				tmpip.add(f_ip.get(dd));
				tmppic.add(pic.get(dd));

				tmpstatus.add(status.get(dd));

				tmpemail.add(emails.get(dd));
				tmpselfinfo.add(selfinfos.get(dd));
				out.println("addfriend");
				out.println(f_id.get(dd));
				out.println(id);
				try { // 以下告诉客户将其加为好友
					String whoips;
					String s = "oneaddyou" + id;
					s.trim();
					System.out.println(s);
					byte[] data = s.getBytes();
					whoips = f_ip.get(dd).toString().trim();
					sendPacket = new DatagramPacket(data, s.length(), InetAddress.getByName(whoips), sendPort);
					sendSocket.send(sendPacket);

					if (in.readLine().equals("true")) {
						JOptionPane.showMessageDialog(null, "添加成功，可以到页面进行更新，就可以进行操作了", "消息", 1);
					} else
						JOptionPane.showMessageDialog(null, "添加失败，未知原因，请重新开启客户端试试", "抱歉", 1);

				} catch (IOException e2) {
					e2.printStackTrace();
				}
			} else
				JOptionPane.showMessageDialog(null, "已经添加过好友，或者你加了自身", "消息", 1);

		}

	}

}

// 以下扩展DefaultListModel类建立列表
class FindListModel extends DefaultListModel {
	public FindListModel(Vector nickname, Vector sex, Vector place, Vector f_ip) {
		for (int i = 0; i < nickname.size(); ++i) {
			addElement(new Object[] { nickname.get(i), sex.get(i), place.get(i), f_ip.get(i) });
		}
	}

	public String getName(Object object) {
		Object[] array = (Object[]) object;
		System.out.println("长度：.." + array.length);
		return (String) array[0];
	}

	public String getSex(Object object) {
		Object[] array = (Object[]) object;
		return (String) array[1];
	}

	public String getPlace(Object object) {
		Object[] array = (Object[]) object;
		return (String) array[2];

	}

	public String getIP(Object object) {
		Object[] array = (Object[]) object;
		return (String) array[3];

	}
}

class FindListCellRenderer extends JLabel implements ListCellRenderer// 以下是处理列表渲染
{
	private Border lineBorder = BorderFactory.createLineBorder(Color.red, 2),
			emptyBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);

	public FindListCellRenderer() {
		setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		FindListModel model = (FindListModel) list.getModel();
		setText("昵称： " + model.getName(value) + "  性别: " + model.getSex(value) + "  来自：" + model.getPlace(value)
				+ "  IP：" + model.getIP(value));
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
