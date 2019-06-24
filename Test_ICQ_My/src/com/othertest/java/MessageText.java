package com.othertest.java;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MessageText extends JDialog {

	private JPanel contentPane;
	String message;

	/**
	 * Create the frame.
	 */
	public MessageText(String message) {
		this.message = message;
		setBounds(100, 100, 450, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lable = new JLabel("消息记录");
		lable.setBounds(180, 10, 54, 15);
		contentPane.add(lable);

		JTextPane textPane = new JTextPane();
		JScrollPane s = new JScrollPane(textPane);
		textPane.setEditable(false);
		textPane.setText(message);
		s.setBounds(10, 38, 414, 179);
		contentPane.add(s);

		JButton ok = new JButton("确定");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();

			}

		});
		ok.setBounds(319, 228, 93, 23);
		contentPane.add(ok);
		this.setVisible(true);
	}
}
