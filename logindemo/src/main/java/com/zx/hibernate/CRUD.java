package com.zx.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.zx.entity.Button;
import com.zx.entity.TUser;
import com.zx.entity.User;
import com.zx.utils.HibernateUtils;
import com.zx.utils.Tools;

public class CRUD {

	public static List<User> QueryAll() {

		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		SQLQuery sqlQuery = session.createSQLQuery("select * from user");
		sqlQuery.addEntity(User.class);
		// 调用sqlQuery里面的方法
		List<User> list = sqlQuery.list();
		// System.out.println(list.get(0).toString());
//					 提交事务
		tx.commit();
//					 关闭资源
		session.close();
		return list;
	}

	public static int Add(String name, int age) {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		User user = new User();
		user.setName(name);
		user.setAge(age);
		session.save(user);

		tx.commit();
//		 关闭资源
		session.close();
		return user.getId();
	}

	public static void AddUser(User user) {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		session.save(user);

		tx.commit();
//		 关闭资源
		session.close();
	}

	public static User QuerySingle(int id) {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		User user = (User) session.get(User.class, id);
		tx.commit();
//		 关闭资源
		session.close();
		return user;
	}

	public static void Upate(int id, String name, int age) {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		User user = (User) session.get(User.class, id);
		user.setName(name);
		user.setAge(age);
		session.update(user);
		// System.out.println(user.toString());
		tx.commit();
//		 关闭资源
		session.close();
	}

	public static void Delete(int id) {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		User user = (User) session.get(User.class, id);
		session.delete(user);
		// System.out.println(user.toString());
		tx.commit();
//		 关闭资源
		session.close();
	}

	public static int isexist(String name, int age) {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		String hql = "select u.id from User u where u.name ='" + name + "' and u.age='" + age + "'";

		Query Query = session.createSQLQuery(hql);
		List<User> list = Query.list();
		tx.commit();
		// System.out.println(user.toString());
//		 关闭资源
		session.close();

		return list.size();
	}

	// 获取用户名的密码
	public static String getpassword(String account) {
		String re = "";
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		String hql = "select u.password from TUser u where u.phone ='" + account + "' or u.email = '" + account + "'";
		Query Query = session.createSQLQuery(hql);
		List<String> list = Query.list();
		if (list.size() == 1) {
			re = list.get(0);
		}
		tx.commit();
		// System.out.println(user.toString());
//		 关闭资源
		session.close();
		return re;
	}

	// 判断用户是否存在，不存在就增加
	public static boolean isexistTUser(String account) {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		String hql = "select u.password from TUser u where u.phone ='" + account + "' or u.email = '" + account + "'";
		Query Query = session.createSQLQuery(hql);
		List<String> list = Query.list();
		if (list.size() == 0) {
			// addTUser(account,"1123");//初始密码
			return false;
		}
		tx.commit();
		// System.out.println(user.toString());
//		 关闭资源
		session.close();
		return true;
	}

	public static int addTUser(String account, String password) {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		TUser user = new TUser();
		if (Tools.isEmail(account)) {
			user.setEmail(account);
		} else {
			user.setPhone(account);
		}
		user.setPassword(password);
		session.save(user);
		tx.commit();
//		 关闭资源
		session.close();
		
		return user.getId();
	}

	public static void addButton(int userid) {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		Button bt = new Button();
		bt.setUserid(userid);
		bt.setQuery(1);
		session.save(bt);
		tx.commit();
//		 关闭资源
		session.close();
	}

	public static List<String> QueryButton(String account) {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		String hql = "select btn.tadd,btn.tdelete,btn.tupdate,btn.tquery,btn.tin,btn.tout,btn.userid from Button as btn,TUser as tu where btn.userid = tu.id and (tu.phone='"
				+ account + "'or tu.email = '" + account + "')";
		Query Query = session.createSQLQuery(hql);
		List<Object> list = Query.list();
		System.out.println(list.size());
		Object[] ob = (Object[]) list.get(0);
		List<String> btlist = new ArrayList<String>();
		for (int i = 0; i < ob.length; i++) {
			btlist.add(ob[i].toString());
			System.out.println(ob[i].toString());
		}
		tx.commit();
//		 关闭资源
		session.close();

		return btlist;
	}

	public static void UpatePassword(int id, String password) {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		TUser user = (TUser) session.get(TUser.class, id);
		user.setPassword(password);
		session.update(user);
		// System.out.println(user.toString());
		tx.commit();
//		 关闭资源
		session.close();
	}

	public static Button QueryButtonCond(String cond) {

		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		SQLQuery sqlQuery = session.createSQLQuery(
				"select btn.id,btn.userid,btn.tadd,btn.tdelete,btn.tupdate,btn.tquery,btn.tin,btn.tout from Button as btn,TUser as tu where btn.userid = tu.id and (tu.phone='"
						+ cond + "' or tu.email = '" + cond + "' or tu.id = '" + cond + "')");
		List<Object> query = sqlQuery.list();
//					 提交事务
		if (query.isEmpty()) {
			return null;
		} else {
			Object[] ob = (Object[]) query.get(0);
			System.out.println(ob[0]);
			tx.commit();
//					 关闭资源
			session.close();
			Button button = new Button();
			button.setId(Integer.parseInt(ob[0].toString()));
			button.setUserid(Integer.parseInt(ob[1].toString()));
			button.setAdd(Integer.parseInt(ob[2].toString()));
			button.setDelete(Integer.parseInt(ob[3].toString()));
			button.setUpdate(Integer.parseInt(ob[4].toString()));
			button.setQuery(Integer.parseInt(ob[5].toString()));
			button.setIn(Integer.parseInt(ob[6].toString()));
			button.setOut(Integer.parseInt(ob[7].toString()));

			return button;
		}
	}

	public static void UpdatePer(int btid, int add, int delete, int update, int query, int in, int out) {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		Button button = (Button) session.get(Button.class, btid);
		System.out.println(button.toString());
		button.setAdd(add);
		button.setDelete(delete);
		button.setUpdate(update);
		button.setQuery(query);
		button.setIn(in);
		button.setOut(out);
		session.update(button);
		// System.out.println(user.toString());
		tx.commit();
//		 关闭资源
		session.close();
	}

}
