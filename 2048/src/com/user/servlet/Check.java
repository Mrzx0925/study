package com.user.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.user.dao.UserDao;

/**
 * Servlet implementation class Check
 */
public class Check extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String name  = request.getParameter("uname");
		String pwd = request.getParameter("upwd");
		UserDao user = new UserDao();
		String flag  = user.login(name, pwd);
		String rank = user.DJ(name);
		if(!flag.equals("0")) {
			request.setAttribute("name", name);
			request.setAttribute("score", flag);
			request.setAttribute("rank", rank);
			System.out.println(rank);
			request.getRequestDispatcher("2048.jsp").forward(request, response);
		}
		else {
			HttpSession se = request.getSession();
			se.setAttribute("message","登录失败");
			response.sendRedirect("login.jsp");
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
