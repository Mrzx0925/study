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
 * �ͻ��˽��漰������
 */
public class ClientFrame extends JFrame implements ActionListener, Runnable {
	private static final long serialVersionUID = 1L; // ????????????????
	private static boolean flag = true; // �ж��Ƿ����������
	private static int file_number_flag = 0; // �������ļ�����

	private static String IP; // IP��ַ
	private static int port = 6666; // ���˿ڣ�������Ϣ�˿�
	private static int file_port = 9988; // �ļ����Ͷ˿�
	private static int download_port = 5555; // �ļ����ض˿�
	private static int file_list_port = 10018; // �õ��������ļ��б�˿�

	String filepath;
	private FileInputStream fis;
	private DataOutputStream dos;
	private File f; // �����ļ�
	private Socket socket; // ����socket
	private Socket datasocket; // �����ļ�Socket
	private Socket downloadsocket; // �����ļ�Socket
	private Socket getFile; // �õ��ļ�Socket
	BufferedReader br = null;
	OutputStream os = null; // �����
	BufferedWriter bw = null; // д

	JTextArea message; // ��ʾ��������ݣ�����������
	JScrollPane scroll; // ʹ�����ܹ���
	JTextField input; // ��������
	JButton submit; // ���Ͱ�ť
	JButton sendfile; // �����ļ���ť
	JButton download; // �����ļ���ť
	JButton IP_ok; // ����IPȷ����ť
	JPanel panel; // һ��������װinput �� submit
	JPanel getIP; // �õ�IP������
	JTextField IP_input; // ����IP��ַ
	JFrame IPJF; // ����IP����

	String uname = ""; // �õ�����

	// ����ҳ��
	JPanel chat; // ������� ����

	// ��¼ҳ��
	JPanel login; // ��¼��������
	JTextField username; // ���������
	JTextArea link = null; // ������ӳɹ��ĸ���
	JButton ok; // ���Ӱ�ť

	public ClientFrame(String name) {
		super(name);

		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); // windows���
			 //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel") ; // Mac���
			// UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel") ; // JavaĬ�Ϸ��
		} catch (Exception e) {
			System.out.println("windows�������ʧ��");
			System.exit(0);
		}
		// �ڴ����ͻ��˴����Ǿ�Ҫ�ѿͻ�������������
		IPJF = new JFrame("����IP");
		IPJF.setBounds(400, 200, 400, 100);
		getIP = new JPanel();
		IP_input = new JTextField(15);
		IP_input.setFont(new Font(Font.DIALOG, Font.BOLD, 20)); // new Font(String ���壬int ���int �ֺ�);
		IP_input.setForeground(Color.DARK_GRAY);
		IP_input.setText("129.204.52.33"); // ���ó�ʼIP��ַΪ����

		IP_ok = new JButton("ȷ��"); // ������IPȷ����ť
		IP_ok.setForeground(Color.RED);
		getIP.add(IP_input);
		getIP.add(IP_ok);
		IPJF.add(getIP);
		IPJF.setVisible(true);
		IP_ok.addActionListener(this);// IPȷ����ť����

		message = new JTextArea();
		message.setEditable(false); // ʹ���治��ѡ��
		scroll = new JScrollPane(message); // ʹ�����ܹ���
		input = new JTextField(25);
		submit = new JButton("����");
		sendfile = new JButton("�����ļ�");
		download = new JButton("�����ļ�");
		download.setForeground(Color.red); // ��ɫ

		panel = new JPanel();
		panel.add(input); // �����������Ϣ
		panel.add(submit); // ���Ͱ�ť
		panel.add(sendfile); // �����ļ���ť
		chat = new JPanel(new BorderLayout()); // �߽粼��
		chat.add(scroll, "Center");
		chat.add(panel, "South");
		login = new JPanel(); // ��¼����
		username = new JTextField(10);
		ok = new JButton("ȷ��");
		// 4�������水ť����

		submit.addActionListener(this);
		sendfile.addActionListener(this);

		download.addActionListener(this);

		ok.addActionListener(this);

		JLabel label = new JLabel("�������û���"); // ��ǩ
		label.setFont(new Font(Font.DIALOG, Font.BOLD, 28)); // new Font(String ���壬int ���int �ֺ�);
		label.setForeground(Color.red);
		login.add(label);
		login.add(username);
		login.add(ok);
		login.add(download); // �����ļ�
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
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���˳�
	}

	// @SuppressWarnings("unused")
	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * IP_ok��ť���¼�������ϢSocket�����IP���Ի���ɷ������޷����ӵ����������ʾ
		 */
		if (e.getSource() == IP_ok) {
			try {
				IP = IP_input.getText();
				socket = new Socket(IP, port);
				JOptionPane.showMessageDialog(this, "���ӳɹ�"); // ��ʾ���ӳɹ�
				new Thread(this).start(); // �߳̿�ʼ
				os = socket.getOutputStream(); // ��������
				bw = new BufferedWriter(new OutputStreamWriter(os)); // д
				IPJF.setVisible(false); // �ر�IP���봰��
				this.setVisible(true); // ��������

			} catch (Exception e1) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(this, "����������ʧ��");
				System.exit(0); // ���ǵ��ͻ��˺ͷ���������һ̨������
			}

			/*
			 * ���Ͱ�ť ��ȡ������Ϣ��������
			 */
		} else if (e.getSource() == submit) {
			if ("".equals(uname)) {
				JOptionPane.showMessageDialog(this, "û�������û���!"); // ��������
			} else {
				// �������ύ��ť�����ʾ��Ҫ����Ϣ���ͳ�ȥ��
				String str = null;
				str = uname + " : " + input.getText(); // ������������
				try {
					bw.write(str); // ��strд��������
					bw.newLine();
					// System.out.println("�ͻ��˷����ˣ�"+str);
					bw.flush();

					// System.out.println(str);
				} catch (IOException e1) {
					System.out.println("�޷����ͣ������������쳣��");
				}
				input.setText("");
			}

			/*
			 * ���ӽ������������,���ڷ������Ͻ�����ʾ
			 */

		} else if (e.getSource() == ok) {// �û���¼
			uname = username.getText();// ��ȡ�ı���������ı���Ϣ

			if ("".equals(uname)) {
				JOptionPane.showMessageDialog(this, "�û�������Ϊ�գ�"); // ��������
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
				link.setText("���ӳɹ�");
				this.setTitle(uname + " �Ŀͻ���");

			}

			/*
			 * �����ļ���ť���õ���JFileChooser���ȶ��ļ��ٽ��ļ����ֽ������з���
			 */
		} else if (e.getSource() == sendfile) {

			if ("".equals(uname)) {
				JOptionPane.showMessageDialog(this, "û�������û���!"); // ��������
			} else {
				int b = 0;
				JFileChooser jfc = new JFileChooser("c://11"); // Ĭ�ϴ�C://

				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int value = jfc.showDialog(new JLabel(), "�����ļ�");
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
						datasocket.close(); // ������Ϲر�����

						// message.append(uname + "�ļ��ɹ����͵��ͻ���\r\n");

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.out.println(e);
					}
				}
			}
			/*
			 * �ļ����ذ�ť���漰�����������ݷ������ļ����ݣ�����ѡ��ȷ���ļ����ݣ��н��棬�������˿�
			 */
		} else if (e.getSource() == download) {

			// String filepath = null;

			JFrame download = new JFrame("�����ļ���������ţ�");
			JButton download_file = new JButton("����");
			JTextField download_name = new JTextField(20);
			JPanel panel = new JPanel();

			panel.add(download_name);
			panel.add(download_file);
			download.add(panel, "Center");
			download.setBounds(800, 400, 400, 100);

			if ("".equals(uname)) {
				JOptionPane.showMessageDialog(this, "û�������û���!"); // ��������
			} else {

				JFrame file_path = new JFrame("�����ļ������ַ");
				JPanel path_name = new JPanel();
				JButton choose = new JButton("ѡ��");
				choose.setForeground(Color.RED);
				JTextField input_path = new JTextField(20);
				input_path.setText("E://JavaDownload");
				JButton input_ok = new JButton("ȷ��");
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
						selectfile.setDialogTitle("ѡ���㴢���ļ����ļ���");
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
							JFrame show_File = new JFrame("�������ļ�");
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
												JOptionPane.showMessageDialog(download, "�������");
											} else if ((Integer.valueOf(download_name.getText()).intValue()) <= 0
													|| (Integer.valueOf(download_name.getText())
															.intValue()) > file_number_flag) {
												JOptionPane.showMessageDialog(download, "�������");
											} else {
												bw.write(download_name.getText());
												bw.newLine();
												bw.flush();
												dis = new DataInputStream(downloadsocket.getInputStream());
												String fileName = dis.readUTF();
												long fileLength = dis.readLong(); // fileLength��Ϊ�˲鿴���ȣ�����δʵ�֣������дreadLong��ʹ�����ļ���ͷ����
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
												JOptionPane.showMessageDialog(download, "���سɹ�"); // ��������
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
							JOptionPane.showMessageDialog(file_path, "��ַ����Υ��");
						}
					}

				});
			}
		}
	}

	// run����������ǽ��ܷ���������Ϣ�ͽ���Ϣ��ʾ�Ĵ���
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
				message.append(str); // ׷��
			}

		} catch (IOException e) {
			// System.out.println("�޷����ͣ������������쳣��");
			JOptionPane.showMessageDialog(this, "�������ѶϿ�����");
			System.exit(0);
		}
	}

	/**
	 * ƥ���Ƿ�Ϊ���� ���Ϸ��� �쳣����
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
