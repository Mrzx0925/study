package com.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
	static String driver = "com.mysql.jdbc.Driver";
	static String url = "jdbc:mysql://localhost:3306/study?useSSL=false&useUnicode=true&amp;characterEncoding=UTF-8";
	static String user = "root";
	static String password = "aa909090";

	public static Connection getCon() {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	public String login(String name, String pwd) {
		try {
			// 要执行的SQL语句return -1;
			String sql = "select *from Web where Name = ?";
			Connection conn = getCon();
			PreparedStatement pr = conn.prepareStatement(sql);
			pr.setString(1, name);
			ResultSet rs = pr.executeQuery();
			while (rs.next()) {
				String spwd = rs.getString("Password");
				String score = rs.getString("score");
				if (spwd.equals(pwd)) {
					return score;
				}
			}
			rs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return "0";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
		return "0";
	}

	public boolean insert(String name, String pwd) {
		try {
			String sql = "select *from Web where Name = ?";
			Connection conn = getCon();
			PreparedStatement pr = conn.prepareStatement(sql);
			pr.setString(1, name);
			ResultSet rs = pr.executeQuery();
			if (rs.next()) {
				return false;
			}
			if (!name.equals("") && !pwd.equals("")) {
				Statement statement = conn.createStatement();
				// 要执行的SQL语句return -1;
				String addfriend = "insert into Web values(?,?,?)";
				PreparedStatement prepare = conn.prepareStatement(addfriend);
				prepare.setString(1, name);
				prepare.setString(2, pwd);
				prepare.setString(3, "4");
				prepare.executeUpdate();
				statement.close();
				conn.close();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void update(String name, String score) {
		int sc = Integer.parseInt(score);
		try {
			Connection conn = getCon();
			// 要执行的SQL语句return -1;
			String sql = "select * from Web where Name = ?";

			PreparedStatement pr = conn.prepareStatement(sql);
			pr.setString(1, name);

			ResultSet rs = pr.executeQuery();
			String ss = "";
			while (rs.next()) {
				ss = rs.getString("score");
			}
			System.out.println((Integer.parseInt(ss)));
			System.out.println(sc);
			if (Integer.parseInt(ss) < sc) {
				String upd = "update Web set score = ? where Name = ?";
				PreparedStatement pr1 = conn.prepareStatement(upd);
				pr1.setString(1, score);
				pr1.setString(2, name);
				pr1.executeUpdate();
				System.out.println(pr);
				rs.close();
				conn.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public String DJ(String name) {
		String rank = "五名之后";
		try {
			// 要执行的SQL语句return -1;
			String sql = "select *from Web order by score desc";
			Connection conn = getCon();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			int flag = 1;
			while (rs.next()) {
				if (rs.getString("name").equals(name)) {
					return "第" + Integer.toString(flag) + "名";
				}
				flag++;
				if (flag > 5) {
					break;
				}
			}
			rs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return "0";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}

		return rank;

	}

	public static List<Info> getSelect() {

		List list = new ArrayList<Info>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "select *from Web order by score desc";
			con = getCon();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Info info = new Info();
				info.setName(rs.getString("name"));
				info.setScore(rs.getString("score"));
				list.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
