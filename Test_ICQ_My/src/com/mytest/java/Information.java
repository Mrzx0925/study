package com.mytest.java;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Information extends JDialog {
	String snickname;
	String sID;
	String sip;
	String semail;
	String sinfo;
	private final JPanel contentPanel = new JPanel();
	private JButton ok;
	
	
	public Information(String snickname,String sID,String sip,String semail,String sinfo) {
		this.snickname = snickname;
		this.sID = sID;
		this.sip = sip;
		this.semail = semail;
		this.sinfo = sinfo;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lable_1 = new JLabel("昵称:");
		lable_1.setBounds(10, 10, 54, 15);
		contentPanel.add(lable_1);
		
		JTextArea nickname = new JTextArea();
		nickname.setEnabled(false);
		nickname.setText(snickname);
		nickname.setBounds(81, 6, 115, 19);
		contentPanel.add(nickname);
		
		JLabel lable_2 = new JLabel("ID:");
		lable_2.setBounds(210, 10, 54, 15);
		contentPanel.add(lable_2);
		
		JLabel label_3 = new JLabel("来自:");
		label_3.setBounds(10, 71, 54, 15);
		contentPanel.add(label_3);
		
		JTextArea ID = new JTextArea();
		ID.setEnabled(false);
		ID.setText(sID);
		ID.setBounds(274, 6, 150, 19);
		contentPanel.add(ID);
		
		JLabel lable_4 = new JLabel("email:");
		lable_4.setBounds(210, 71, 54, 15);
		contentPanel.add(lable_4);
		
		JTextArea email = new JTextArea();
		email.setEnabled(false);
		email.setText(semail);
		email.setBounds(274, 67, 150, 19);
		contentPanel.add(email);
		
		JLabel lable_5 = new JLabel("个人说明");
		lable_5.setBounds(10, 121, 54, 15);
		contentPanel.add(lable_5);
		
		JTextArea info = new JTextArea();
		info.setEnabled(false);
		info.setText(sinfo);
		info.setBounds(81, 127, 343, 82);
		contentPanel.add(info);
		
		ok = new JButton("确定");
		ok.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}
		});
		ok.setBounds(331, 228, 93, 23);
		contentPanel.add(ok);
		
		JTextArea place = new JTextArea();
		place.setEnabled(false);
		place.setText(sip);
		place.setBounds(81, 67, 115, 19);
		contentPanel.add(place);
		this.setResizable(false);
	}
}
