package com.zx.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.HtmlEmail;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

public class SendCode {
	public static void phonesend(String account, int code) {
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAITquo365IoZhB", "yLiVEleFRAG29RgSem3NxKpHiyyRi1");
		IAcsClient client = new DefaultAcsClient(profile);

		CommonRequest request = new CommonRequest();
		request.setMethod(MethodType.POST);
		request.setDomain("dysmsapi.aliyuncs.com");
		request.setVersion("2017-05-25");
		request.setAction("SendSms");
		request.putQueryParameter("RegionId", "cn-hangzhou");
		request.putQueryParameter("PhoneNumbers", account);
		request.putQueryParameter("SignName", "RestStudy");
		request.putQueryParameter("TemplateCode", "SMS_171187497");
		String json = "{\"code\":\""+code+"\"}";
		request.putQueryParameter("TemplateParam", json);
		try {
			CommonResponse response = client.getCommonResponse(request);
			System.out.println(response.getData());
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean sendEmail(String emailaddress,int code){
		try {
			HtmlEmail email = new HtmlEmail();//不用更改
			email.setHostName("smtp.163.com");//需要修改，126邮箱为smtp.126.com,163邮箱为smtp.163.com，QQ为smtp.qq.com
			email.setCharset("UTF-8");
			email.addTo(emailaddress);// 收件地址
 
			email.setFrom("a1035395948@163.com", "zx");//此处填邮箱地址和用户名,用户名可以任意填写

			email.setAuthentication("a1035395948@163.com", "aa123123");//此处填写邮箱地址和客户端授权码
			email.setSubject("大学生就该有大学生的亚子");//此处填写邮件名，邮件名可任意填写
			email.setMsg("尊敬的用户您好,您本次的验证码是:" + code);//此处填写邮件内容
			//email.setMsg("");//此处填写邮件内容
			email.send();
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean sendregister(String emailaddress,String url){
		try {
			HtmlEmail email = new HtmlEmail();//不用更改
			email.setHostName("smtp.163.com");//需要修改，126邮箱为smtp.126.com,163邮箱为smtp.163.com，QQ为smtp.qq.com
			email.setCharset("UTF-8");
			email.addTo(emailaddress);// 收件地址
 
			email.setFrom("a1035395948@163.com", "zx");//此处填邮箱地址和用户名,用户名可以任意填写

			email.setAuthentication("a1035395948@163.com", "aa123123");//此处填写邮箱地址和客户端授权码
			email.setSubject("大学生就该有大学生的亚子");//此处填写邮件名，邮件名可任意填写
			email.setMsg("尊敬的用户您好，复制链接到浏览器打开注册::" + url);//此处填写邮件内容
			//email.setMsg("");//此处填写邮件内容
			email.send();
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
