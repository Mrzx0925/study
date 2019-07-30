package com.zx.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.jboss.logging.Param;
import org.jboss.resteasy.plugins.server.servlet.HttpServletResponseHeaders;

import com.zx.entity.Button;
import com.zx.entity.Data;
import com.zx.entity.TUser;
import com.zx.entity.User;
import com.zx.hibernate.CRUD;
import com.zx.utils.ChartUtils;
import com.zx.utils.ImageUtils;
import com.zx.utils.SendCode;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

@Path("/resteasy")
public class Service {

	// 访问地址：http://localhost:8080/logindemo/zx/resteasy/??

	@GET
	@Path("/zx")
	public void printMessage() throws Exception {

		// System.out.println(code);

	}

	// 得到图片
	@GET
	@Path("/image")
	public void getimage(@Context HttpServletResponse response, @Context HttpServletRequest request) throws Exception {
		ImageUtils.getimgYZM(response, request);
	}

	// 得到图片验证码
	@POST
	@Path("/getimage/{mycode}")
	public void getimagedata(@Context HttpServletResponse response, @Context HttpServletRequest request,
			@PathParam("mycode") String mycode) throws Exception {
		int flag = 0;
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		PrintWriter out = response.getWriter();
		String code = (String) request.getSession().getAttribute("code");
		System.out.println(code);
		System.out.println(mycode);
		if (code.equals(mycode)) {
			flag = 1;
		}
		out.print(flag);
		out.close();
	}

	@POST
	@Path("/cclg")
	public void checkaccountlogin(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@FormParam("account_1") String account, @FormParam("password") String password) throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		System.out.println(account);
		System.out.println(password);
		String pass = CRUD.getpassword(account);
		PrintWriter out = response.getWriter();
		if (pass.equals(password)) {
			// 通过
			out.print("1");
			request.getSession().setAttribute("loginaccount", account);
		} else if (pass.equals("")) {
			// 无此用户
			out.print("0");
		} else {
			// 错误
			out.print("2");
		}
		out.close();
	}

	@POST
	@Path("/codelg")
	public void checkcodelog(@Context HttpServletResponse response, @Context HttpServletRequest request,
			@FormParam("account_2") String account, @FormParam("code") String code) throws Exception {
		System.out.println(account);
		Object scode = "";
		if (CRUD.isexistTUser(account) == true) {
			 scode = request.getSession().getAttribute(account + "log");
		}else {
			scode = request.getSession().getAttribute(account + "re");
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		PrintWriter out = response.getWriter();
		System.out.println(scode + "scode" + code);
		if (scode == null || !scode.toString().equals(code)) {
			out.print("0");
		} else {
			
			request.getSession().setAttribute("loginaccount", account);
			if (CRUD.isexistTUser(account) == false) {
				int userid = CRUD.addTUser(account, "1123");
				CRUD.addButton(userid);
				out.print("1123");
			}else {
				out.print("1");
			}

		}
		out.close();
	}

	@POST
	@Path("/phonesend")
	public void phonesend(@Context HttpServletResponse response, @Context HttpServletRequest request,
			@FormParam("account_2") String account) throws Exception {
		int code = (int) (Math.random() * 9000) + 1000;
		System.out.println(code);
		System.out.println(account);
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		PrintWriter out = response.getWriter();
		if (CRUD.isexistTUser(account) == false) {
			out.print("1");
			SendCode.phonesend(account, code);
			request.getSession().setAttribute(account + "re", code);
		} else {
			//SendCode.phonesend(account, code);
			request.getSession().setAttribute(account + "log", code);
			out.print("0");
		}
		out.close();
	}

	@POST
	@Path("/emailsend")
	public void emailsend(@Context HttpServletResponse response, @Context HttpServletRequest request,
			@FormParam("account_2") String account) throws Exception {
		int code = (int) (Math.random() * 9000) + 1000;
		System.out.println(code);
		System.out.println(account);

		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		PrintWriter out = response.getWriter();
		if (CRUD.isexistTUser(account) == false) {
			out.print("1");
			SendCode.sendEmail(account, code);
			request.getSession().setAttribute(account + "re", code);
		} else {
			SendCode.sendEmail(account, code);
			request.getSession().setAttribute(account + "log", code);
			out.print("0");
		}

		out.close();
	}

	@POST
	@Path("/emailregister")
	public void emailregister(@Context HttpServletResponse response, @Context HttpServletRequest request,
			@FormParam("account_2") String account, @FormParam("password_1_1") String password) throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		PrintWriter out = response.getWriter();
		if (CRUD.isexistTUser(account) == false) {
			out.print("1");
			SendCode.sendregister(account,
					"http://localhost:8080/logindemo/zx/resteasy/email/" + account + "/" + password);
			// request.getSession().setAttribute("emailaccount", account);
			// request.getSession().setAttribute("emailpassword", password);
		} else {
			out.print("0");
		}
		out.close();
	}

	@POST
	@Path("/phoneregister")
	public void phoneregister(@Context HttpServletResponse response, @Context HttpServletRequest request,
			@FormParam("account_2") String account, @FormParam("password_2_1") String password,
			@FormParam("code") String code) throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		PrintWriter out = response.getWriter();
		if (request.getSession().getAttribute(account + "re").toString().equals(code)) {
			out.print("1");
			int userid = CRUD.addTUser(account, password);
			CRUD.addButton(userid);
		} else {
			out.print("0");
		}
		out.close();
	}

	@GET
	@Produces("text/plain; charset=utf-8")
	@Path("/email/{account}/{password}")
	public String email(@PathParam("account") String account, @PathParam("password") String password) throws Exception {
		if(CRUD.isexistTUser(account) == false) {
			int userid = CRUD.addTUser(account, password);
			CRUD.addButton(userid);
			return "注册成功";
		}else{
			return "不要重复注册";
		}
	}

	@POST
	@Path("/getaccount")
	public void getaccount(@Context HttpServletResponse response, @Context HttpServletRequest request)
			throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		PrintWriter out = response.getWriter();
		Object account = request.getSession().getAttribute("loginaccount");
		if (account == null) {
			out.print(0);
		}else {
			List<String> list = new ArrayList<String>();
			list = CRUD.QueryButton(account.toString());
			String userid = list.get(6);
			list.remove(6);
			request.getSession().setAttribute("userid", userid);
			list.add(account.toString());
			out.print(list.toString());
		}
		out.close();
	}
	@POST
	@Path("/getper")
	public void getper(@Context HttpServletResponse response, @Context HttpServletRequest request)
			throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		PrintWriter out = response.getWriter();
		Object account = request.getSession().getAttribute("loginaccount");
		if (account == null) {
			out.print(0);
		}else if(account.equals("admin")) {
			out.print(1);
		}else {
			out.print(666);
		}
		out.close();
	}
	
	@POST
	@Path("/updatepass/{password}")
	public void updatepassword(@Context HttpServletResponse response, @Context HttpServletRequest request,@PathParam("password") String password)
			throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		PrintWriter out = response.getWriter();
		Object id = request.getSession().getAttribute("userid");
		System.out.println(id);
		CRUD.UpatePassword(Integer.parseInt(id.toString()), password);
		out.print("1");
		out.close();
	}
	
	@POST
	@Path("/updateinfo/{info}")
	public void updateinfo(@Context HttpServletResponse response, @Context HttpServletRequest request,@PathParam("info") String info)
			throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		PrintWriter out = response.getWriter();
		int id = (Integer) request.getSession().getAttribute("userid");
		CRUD.UpatePassword(id, info);
		out.print("1");
		out.close();
	}
	
	
	@GET
	@Path("/loginout")
	public void loginout(@Context HttpServletRequest request)
			throws Exception {
		System.out.println("zx");
		request.getSession().invalidate();//清除所有信息
	}
	
	
	@POST
	
	@Path("/permissions/{cond}")
	public void permissions(@Context HttpServletResponse response,@PathParam("cond") String cond)
			throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		PrintWriter out = response.getWriter();
		Button button = CRUD.QueryButtonCond(cond);
		if(button == null) {
			out.print(0);
		}else {
			JSONObject result = JSONObject.fromObject(button); 
			System.out.println(result);
			out.print(result);
		}
		out.close();
	}
	
	
	@POST
	@Path("/updateper/{btid}")
	public void updateper(@Context HttpServletResponse response,@PathParam("btid") String btid,@FormParam("add") int add
			,@FormParam("delete") int delete,@FormParam("update") int update,@FormParam("query") int query,@FormParam("in") int in,@FormParam("out") int age,@FormParam("out") int out) throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("utf-8");
		PrintWriter pout = response.getWriter();
		System.out.println(btid+"  "+out);
		CRUD.UpdatePer(Integer.parseInt(btid), add, delete, update, query, in, out);
		pout.print(1);
		pout.close();
	}
	
	
	// http://localhost:8080/mytest/zx/restesy/??
		@GET
		@Path("/{param}")
		public Response printMessage(@PathParam("param") String msg) {
			String result = "Restful example : " + msg;
			return Response.status(200).entity(result).build();
		}

		@POST
		@Path("getName")
		public String getName(@FormParam("fname") String fname, @FormParam("lname") String lname) {
			String result = "RESTEasy Hello World : " + fname + lname;
			System.out.println("fname" + fname);
			return result;
		}

		@GET
		@Produces("application/json")
		@Path("zx/{id}")
		public String get(@PathParam("id") String id) {
			String result = "Hello " + id;
			return result;
		} 
		
		@POST
		@Path("queryall")
		public void queryall(@Context HttpServletResponse response) throws IOException{
			response.setCharacterEncoding("utf-8");
			response.setContentType("utf-8");
			PrintWriter out = response.getWriter();
			List<User> user = CRUD.QueryAll();
			JSONArray result = JSONArray.fromObject(user); 
			out.print(result);
			System.out.println("zxxz");	
			out.close();
		}
		
		@POST
		@Produces("text/plain; charset=utf-8")
		@Path("add")
		public void add(@QueryParam("name") String name,@FormParam("age") int age,@Context HttpServletResponse response) throws IOException{
			int id = CRUD.Add(name,age);
			int exist = CRUD.isexist(name, age) - 1;
			PrintWriter out = response.getWriter();
			if(exist == 0) {
				out.print("true:"+id);
			}else {
				out.print(id+":"+exist);
			}
			out.close();
		}
		
		@POST
		@Path("querysingle")
		public void querysingle(String sid,@Context HttpServletResponse response) throws IOException {
			int id = Integer.parseInt(sid);
			response.setCharacterEncoding("utf-8");
			response.setContentType("utf-8");
			PrintWriter out = response.getWriter();
			User user = CRUD.QuerySingle(id);
			JSONObject jsonObject = JSONObject.fromObject(user);
			System.out.println(jsonObject.toString());
			out.print(jsonObject);
			out.close();
		} 
		@POST
		@Path("update")
		public void update(@FormParam("id") int id, @QueryParam("name") String name,@FormParam("age") int age) {
			System.out.println("进入");
			System.out.println(id);
			System.out.println(name);
			CRUD.Upate(id,name,age);
		}
		
		@POST
		@Path("delete")
		public void delete(@FormParam("id") int id) {
			CRUD.Delete(id);
		}
		
		@POST
		@Path("get")
		public void get(@Context HttpServletResponse response) throws IOException {
			response.setCharacterEncoding("utf-8");
			response.setContentType("utf-8");
			PrintWriter out = response.getWriter();
			List<Data> data = new ArrayList<Data>();
			System.out.println("你来没");
			data = ChartUtils.getDataList();
			
			JSONArray result = JSONArray.fromObject(data); 
			out.print(result);
			System.out.println(result.toString());	
			out.close();
		}

	
}