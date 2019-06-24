package com.user.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import com.user.dao.UserDao;

/**
 * Servlet implementation class Register
 */
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String name  = request.getParameter("uname");
		String pwd = request.getParameter("upwd");
		UserDao user = new UserDao();
		boolean flag = user.insert(name, pwd);
		if(flag == true) {
			HttpSession se = request.getSession();
			se.setAttribute("message","注册成功");
			response.sendRedirect("login.jsp");
		}
		else{
			HttpSession se = request.getSession();
			se.setAttribute("message","注册失败");
			response.sendRedirect("register.jsp");
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
