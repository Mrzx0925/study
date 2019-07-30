package com.zx.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {

   // 1.创建工厂对象;
	private static SessionFactory sessionFactory;

	// 2.初始化工厂对象;
	static {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}

	// 3.获得Session;
	public static Session getSession() {

		return sessionFactory.openSession();

	}
}