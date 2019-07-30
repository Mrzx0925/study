package com.zx.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImageUtils {

	public static void getimgYZM(HttpServletResponse response,HttpServletRequest request) throws IOException {
		BufferedImage bi = new BufferedImage(68, 22, BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();// 画笔对象
		Color c = new Color(200, 150, 255);// 颜色对象
		g.setColor(c);// 给画笔赋上颜色
		g.fillRect(0, 0, 430, 30);// 画一个外框
		char[] ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		Random r = new Random();
		int len = ch.length, index;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			index = r.nextInt(len);
			g.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt(255)));
			g.drawString(ch[index] + "", (i * 15) + 3, 18);// 画随机字符
			sb.append(ch[index]);
		}
		String checkcode = sb.toString();
		request.getSession().setAttribute("code", checkcode);
		ImageIO.write(bi, "JPG", response.getOutputStream());
	}



}
