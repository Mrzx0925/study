package com.mytest.java;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

class ServerThread extends Thread {// 继承线程
	private Socket socket;// 定义套接口
	private BufferedReader in;// 定义输入流
	private PrintWriter out;// 定义输出流
	int no;// 定义申请的ID号码

	public ServerThread(Socket s) throws IOException {// 线程构造函数
		socket = s;// 取得传递参数
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 创建输入流
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);// 创建输出流
		start();// 启动线程
	}

	public void run() {// 线程监听函数
		try {
			while (true) {
				String str = in.readLine();// 取得输入字符串
				if (str.equals("end"))
					break;// 如果是结束就关闭连接
				else if (str.equals("login")) {// 如果是登录
					try {
						Connection c = Conn();
						String sql = "select Nickname,Password,Status from t_icq where ID=?";
						// 准备从数据库选择呢称和密码
						PreparedStatement prepare = c.prepareCall(sql);// 设定数据库查寻条件
						String ID = in.readLine();
						int g = Integer.parseInt(ID);// 取得输入的ID号码
						System.out.println(ID);
						String passwd = in.readLine().trim();// 取得输入的密码
						System.out.println(passwd);
						prepare.clearParameters();
						prepare.setInt(1, g);// 设定参数
						ResultSet r = prepare.executeQuery();// 执行数据库查寻
						if (r.next()) {// 以下比较输入的号码于密码是否相同
							String pass = r.getString("Password").trim();
							String status = r.getString("Status");
							if (status.equals("1")) {
								out.println("occupy");// 占用
							} else {
								System.out.println(pass);
								if (passwd.equals(pass)) {
									out.println("ok");
									// 如果相同就告诉客户ok
									// 并且更新数据库用户为在线
									// 以及注册用户的ip 地址
									// *************register ipaddress
									String setip = "update t_icq set ip=? where ID=?";
									PreparedStatement prest = c.prepareCall(setip);
									prest.clearParameters();
									prest.setString(1, socket.getInetAddress().getHostAddress());
									System.out.println("111111111111111111111111        "
											+ socket.getInetAddress().getHostAddress());
									prest.setInt(2, g);
									int set = prest.executeUpdate();
									System.out.println(set);
									// *************ipaddress
									// set status online
									String updatestatus = "update t_icq set status= 1 where ID=?";
									PreparedStatement prest2 = c.prepareCall(updatestatus);
									prest2.clearParameters();
									prest2.setInt(1, g);
									int set2 = prest2.executeUpdate();
									System.out.println(set2);
									// set online
								}

								// 否者告诉客户失败
								else
									out.println("false");
								r.close();
								c.close();
							}
						} else {
							out.println("false");
							System.out.println("false");
							r.close();
							c.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					socket.close();
				} // end login
					// 登录结束
					// 以下为处理客户的新建请求
				else if (str.equals("new")) {
					try {
						Connection c2 = Conn();
						String newsql = "insert into t_icq(Nickname,Password,status,IP,Email,SelfInfo,place,pic) values(?,?,?,?,?,?,?,?)";
						// 准备接受用户的呢称，密码，email，个人资料，籍贯，头像等信息
						PreparedStatement prepare2 = c2.prepareCall(newsql);
						String nickname = in.readLine().trim();
						String password = in.readLine().trim();
						String email = in.readLine().trim();
						String info = in.readLine().trim();
						String place = in.readLine().trim();
						int picindex = Integer.parseInt(in.readLine());
						prepare2.clearParameters();
						prepare2.setString(1, nickname);
						prepare2.setString(2, password);
						prepare2.setInt(3, 0);
						prepare2.setString(4, socket.getInetAddress().getHostAddress());
						prepare2.setString(5, email);
						prepare2.setString(6, info);
						prepare2.setString(7, place);
						prepare2.setInt(8, picindex);
						int r3 = prepare2.executeUpdate();// 执行数据库添加
						String sql2 = "select ID from t_icq where nickname=?";
						// 以下告诉客户其注册的号码
						PreparedStatement prepare3 = c2.prepareCall(sql2);
						prepare3.clearParameters();
						prepare3.setString(1, nickname);
						ResultSet r2 = prepare3.executeQuery();
						while (r2.next()) {
							// out.println(r2.getInt(1));
							no = r2.getInt(1);
							System.out.println(no);
						}
						out.println(no);
						out.println("ok");
						String myself = "Insert into t_friend(IcqID,FriendID) values(?,?)";
						PreparedStatement pr = c2.prepareStatement(myself);
						pr.setInt(1, no);
						pr.setInt(2, no);
						pr.executeUpdate();

						c2.close();
						// 完毕
					} catch (Exception e) {
						e.printStackTrace();
						out.println("false");
					}
					socket.close();
				} // end new
					// 新建用户结束
					// 以下处理用户查找好友
				else if (str.equals("find")) {
					try {
						Connection c3 = Conn();
						// 以下连接数据库，并且返回其他用户的呢称，性别，籍贯，个人资料等信息
						String find = "select Nickname,sex,place,ip,email,SelfInfo from t_icq";
						Statement st = c3.createStatement();
						ResultSet result = st.executeQuery(find);
						while (result.next()) {
							if (!result.getString("ip").equals("")) {
								out.println(result.getString("nickname"));
								out.println(result.getString("sex"));
								out.println(result.getString("place"));
								out.println(result.getString("ip"));
								out.println(result.getString("email"));
								out.println(result.getString("selfinfo"));
							}
						} // while end
						out.println("over");
						//////// GET ICQNO
						int d, x;
						boolean y;
						// 以下返回用户的jicq号码，头像号，及是否在线
						ResultSet iset = st.executeQuery("select ID,pic,status from t_icq");
						while (iset.next()) {
							d = iset.getInt("ID");
							out.println(d);
							x = iset.getInt("pic");// pic info
							out.println(x);
							y = iset.getBoolean("status");
							if (y) {
								out.println("1");
							} else {
								out.println("0");
							}
							// System.out.println(d);
						}
						// end send jicqno
						iset.close();
						///////// icqno end
						c3.close();
						result.close();
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("false");
					}
					// socket.close();
				} // end find
					// 查找好友结束
					// 以下处理用户登录时读取其好友资料
				else if (str.equals("friend")) {
					try {
						Connection c4 = Conn();
						// 以下连接好友表，返回用户的好友名单
						String friend = "select friendID from t_friend where IcqID=?";
						PreparedStatement prepare4 = c4.prepareCall(friend);
						prepare4.clearParameters();
						int icqno = Integer.parseInt(in.readLine());
						System.out.println(icqno);
						prepare4.setInt(1, icqno);
						ResultSet r4 = prepare4.executeQuery();
						Vector friendno = new Vector();// 该矢量保存好友号码
						while (r4.next()) {
							friendno.add(new Integer(r4.getInt(1)));
						}
						// read friend info
						// 以下告诉客户其好友的呢称，号码，ip地址，状态，头像，个人资料等信息
						out.println(friendno.size());
						for (int i = 0; i < friendno.size(); i++) {
							String friendinfo = "select nickname,id,ip,status,pic,email,selfinfo from t_icq where ID=?";
							PreparedStatement prepare5 = c4.prepareCall(friendinfo);
							prepare5.clearParameters();
							prepare5.setObject(1, friendno.get(i));
							ResultSet r5 = prepare5.executeQuery();
							boolean status;
							while (r5.next()) {
								out.println(r5.getString("nickname"));
								out.println(r5.getInt("ID"));
								out.println(r5.getString("ip"));
								status = r5.getBoolean("status");
								if (status)
									out.println("1");
								else {
									out.println("0");
								}
								out.println(r5.getInt("pic"));
								out.println(r5.getString("email"));
								out.println(r5.getString("selfinfo"));
							} // while
							r5.close();
						} // for
							// 发送完毕
						out.println("over");
						System.out.println("over");
						c4.close();
						r4.close();
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("false");
					}
					// socket.close();
				} // end friend

				else if (str.equals("information")) {
					try {
						Connection c5 = Conn();
						String sql = "select *from t_icq where ID = ?";
						PreparedStatement ps = c5.prepareStatement(sql);
						int id = Integer.parseInt(in.readLine());
						ps.setInt(1, id);
						ResultSet rs = ps.executeQuery();
						while (rs.next()) {
							out.println(rs.getString("Nickname"));

							out.println(rs.getString("Pic"));

							// out.println(rs.getString("IP"));
							out.println(rs.getString("Email"));
							out.println(rs.getString("SelfInfo"));
							out.println(rs.getString("Sig"));
							out.println(rs.getString("Sex"));
							out.println(rs.getString("Name"));
							out.println(rs.getString("Age"));
							out.println(rs.getString("School"));
							out.println(rs.getString("Zodiac"));
							out.println(rs.getString("job"));
							out.println(rs.getString("Constellation"));
							out.println(rs.getString("SelfWeb"));
							out.println(rs.getString("blood"));
							out.println(rs.getString("place"));
							out.println(rs.getString("mobilephone"));
							out.println(rs.getString("phone"));
						}
						String flag = in.readLine();
						if (!flag.equals("cancel")) {
							sql = "update t_icq set nickname = ?,pic = ?,Email = ?,selfinfo = ?,sig = ?,sex = ?,Name =?,"
									+ "Age = ?,school = ?,zodiac = ?,job = ?, constellation = ?,selfweb =?,blood = ?,place = ?,mobilephone = ?,phone = ? "
									+ "where ID = ?";

							PreparedStatement ps1 = c5.prepareStatement(sql);
							ps1 = c5.prepareStatement(sql);
							ps1.clearParameters();
							ps1.setString(1, flag);
							ps1.setInt(2, Integer.parseInt(in.readLine()));
							ps1.setString(3, in.readLine());
							ps1.setString(4, in.readLine());
							ps1.setString(5, in.readLine());
							ps1.setString(6, in.readLine());
							ps1.setString(7, in.readLine());
							ps1.setString(8, in.readLine());
							ps1.setString(9, in.readLine());
							ps1.setString(10, in.readLine());
							ps1.setString(11, in.readLine());
							ps1.setString(12, in.readLine());
							ps1.setString(13, in.readLine());

							ps1.setString(14, in.readLine());
							ps1.setString(15, in.readLine());
							ps1.setString(16, in.readLine());
							ps1.setString(17, in.readLine());
							ps1.setInt(18, id);
							int r = ps1.executeUpdate();// 执行数据库添加
							out.println("ok");
						}

						c5.close();
						rs.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// 读取好友信息完毕

				// 以下处理用户添加好友
				else if (str.equals("addfriend")) {
					System.out.println("add");
					try {
						Connection c6 = Conn();
						// 连接数据库，根据接受的用户号码及好友号码向好友表添加记录
						int friendid = Integer.parseInt(in.readLine());
						System.out.println("fri"+friendid);
						int myid = Integer.parseInt(in.readLine());
						System.out.println("my"+myid);
						String addfriend = "insert into t_friend values(?,?)";
						PreparedStatement prepare6 = c6.prepareCall(addfriend);
						prepare6.clearParameters();
						prepare6.setInt(1, myid);
						prepare6.setInt(2, friendid);
						int r6 = 0;
						r6 = prepare6.executeUpdate();
						if (r6 == 1) {
							System.out.println("ok  addfriend");
						//	System.out.println("这步执行了。。。。。。。。。。");
							//out.println("146465156165156");
							//System.out.println("这步执行了。。。。。。。。。。");
						//	out.println("true");
						}
						 else {
							//out.println("false");
							System.out.println("false addfriend");
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("false");
					}

					// socket.close();
					System.out.println("over addfriend");
				} // end addfriend
					// 用户添加好友结束
					// add new friend who add me
					// 以下处理其他用户如果加我，我就加他
				else if (str.equals("addnewfriend")) {
					System.out.println("add");
					try {
						boolean flag = false;
						Connection c6 = Conn();
						Vector<String> IDs = new Vector<>();
						// 连接数据库，根据接受的用户号码及好友号码向好友表添加记录
						int friendid = Integer.parseInt(in.readLine());
						System.out.println(friendid);

						String sql = "select *from t_icq where true";
						Statement st = c6.createStatement();
						ResultSet rs = st.executeQuery(sql);
						while (rs.next()) {
							IDs.add(rs.getString("ID"));
						}
						for (int i = 0; i < IDs.size(); i++) {
							if (Integer.parseInt(IDs.get(i)) == friendid) {
								flag = true;
							}
						}

						int myid = Integer.parseInt(in.readLine());
						if (flag) {
							System.out.println(myid);
							String addfriend = "insert into t_friend values(?,?)";
							PreparedStatement prepare6 = c6.prepareCall(addfriend);
							prepare6.clearParameters();
							prepare6.setInt(1, myid);
							prepare6.setInt(2, friendid);
							int r6 = 0;
							r6 = prepare6.executeUpdate();
							if (r6 == 1)
								System.out.println("ok  addfrien");
							else
								System.out.println("false addfriend");

							String friendinfo = "select nickname,ID,ip,status,pic,email,selfinfo from t_icq where ID=?";
							// 如果成功，就向用户传递好友的基本信息，比如呢称等
							PreparedStatement prepare5 = c6.prepareCall(friendinfo);
							prepare5.clearParameters();
							prepare5.setInt(1, friendid);
							ResultSet r5 = prepare5.executeQuery();
							boolean status;
							while (r5.next()) {
								System.out.println("dsf");
								out.println(r5.getString("nickname"));
								out.println(r5.getInt("ID"));
								out.println(r5.getString("ip"));
								status = r5.getBoolean("status");
								if (status)
									out.println("1");
								else {
									out.println("0");
								}
								out.println(r5.getInt("pic"));
								out.println(r5.getString("email"));
								out.println(r5.getString("selfinfo"));
							} // while
							out.println("over");
							r5.close();
							c6.close();
						} else {
							out.println("false");
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("false");
					}
					System.out.println("over addnewfriend");
				} // end addfriend
					// 结束处理其他用户如果加我，我就加他
					// delete friend
					// 以下执行用户删除好友
				else if (str.equals("delfriend")) {
					System.out.println("del");
					try {

						Connection c7 = Conn();
						// 连接数据库，根据接受的用户号码及好友号码向好友表删除记录
						int friendid = Integer.parseInt(in.readLine());
						System.out.println(friendid);
						int myid = Integer.parseInt(in.readLine());
						System.out.println(myid);
						String addfriend = "delete from t_friend where icqid=? and friendid=?";
						PreparedStatement prepare7 = c7.prepareCall(addfriend);
						prepare7.clearParameters();
						prepare7.setInt(1, myid);
						prepare7.setInt(2, friendid);
						int r7 = 0;
						r7 = prepare7.executeUpdate();
						if (r7 == 1)
							System.out.println("ok  delfrien");// 成功
						else
							System.out.println("false delfriend");// 失败
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("del false");
					}
				} // end delete friend
					// 执行用户删除好友结束
					// 以下处理用户退出程序
				else if (str.equals("logout")) {
					try {

						Connection c8 = Conn();
						// 连接数据库，根据接受的用户号码，将其状态字段设为0，及ip地址设为空
						int myicqno = Integer.parseInt(in.readLine());
						System.out.println(myicqno);
						String status = "update t_icq set status=0 , ip=' ' where ID=?";
						PreparedStatement prest8 = c8.prepareCall(status);
						prest8.clearParameters();
						prest8.setInt(1, myicqno);
						int r8 = prest8.executeUpdate();
						if (r8 == 1)
							System.out.println("ok  logout");
						else
							System.out.println("false logout");

					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("logout false");
					}
				} // logout end
					// 处理用户退出程序结束
					// get who add me as friend
					// 以下处理那些人加了我为好友，以便上线通知他们
				else if (str.equals("getwhoaddme")) {
					System.out.println("getwhoaddme");
					try {
						Connection c9 = Conn();
						// 连接数据库，根据我的号码，从好友表中选择谁加了我
						int myicqno = Integer.parseInt(in.readLine());
						System.out.println(myicqno);
						String getwhoaddme = "select icqid from t_friend where friendid=?";
						PreparedStatement prepare6 = c9.prepareCall(getwhoaddme);
						prepare6.clearParameters();
						prepare6.setInt(1, myicqno);
						ResultSet r6 = prepare6.executeQuery();
						Vector who = new Vector();
						while (r6.next()) {
							who.add(new Integer(r6.getInt(1)));
						} // end while
							// 然后告诉这些好友的ip地址，然后发给用户以便告诉其他客户我上线了
						for (int i = 0; i < who.size(); i++) {
							// String whoinfo = "select ip from t_icq where id=? and status=1";
							String whoinfo = "select ip from t_icq where id=?";
							PreparedStatement prepare = c9.prepareCall(whoinfo);
							prepare.clearParameters();
							prepare.setObject(1, who.get(i));
							ResultSet r = prepare.executeQuery();
							while (r.next()) {
								out.println(r.getString("ip"));
							} // while
							r.close();
						} // for

						out.println("over");
						System.out.println("over");
						c9.close();
						r6.close();
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("false");
					}
				} // end get who add me as friend
					// 处理上线结束

				else if (str.equals("save")) {
					String id = in.readLine();
					File f = new File("E://text");
					if (!f.exists()) {
						f.mkdirs();
					}
					BufferedWriter bw = new BufferedWriter(new FileWriter("E://text//" + id + ".txt", true));
					String s = in.readLine().trim();
					while (!s.equals("over")) {
						bw.write(s);
						bw.newLine();
						bw.flush();
						s = in.readLine().trim();
					}
					bw.close();
				} else if (str.equals("txt")) {
					String id = in.readLine().trim();
					File f = new File("E://text//" + id + ".txt");
					if (!f.exists()) {
						out.println("false");
					} else {
						BufferedReader br = new BufferedReader(new FileReader(f));
						String line;
						while ((line = br.readLine()) != null) {
							out.println(line.trim());
							out.flush();
						}
						out.println("over");
						br.close();
					}
				}

				System.out.println("Echo ing :" + str);
			}
			System.out.println("Close...");
		} catch (IOException e) {
		} // 捕或异常
		finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}

	public Connection Conn() throws ClassNotFoundException, SQLException {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/test_icq?useSSL=false&useUnicode=true&amp;characterEncoding=UTF-8";
		String user = "root";
		String password = "aa909090";
		Class.forName(driver);
		Connection con = DriverManager.getConnection(url, user, password);
		return con;
	}
}	

public class Server {// 主服务器类
	// public static List<Socket> list = new ArrayList<Socket>(); // 保存Socket集合，即通道
	public static void main(String args[]) throws IOException {
		try {
			ServerSocket s = new ServerSocket(8080);// 在8080端口创建套接口
			System.out.println("Server start.." + s);
			JOptionPane.showMessageDialog(null, "successful!", "提醒", 1);
			try {
				while (true) {
					Socket socket = s.accept();// 无限监听客户的请求
					// list.add(socket);
					System.out.println("Connectino accept:" + socket);
					try {
						new ServerThread(socket);// 创建新线程
					} catch (IOException e) {
						socket.close();
					}
				}
			} finally {
				s.close();
			} // 捕或异常
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "错误，服务器已经启动", "提醒", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
}// 服务器程序结束
