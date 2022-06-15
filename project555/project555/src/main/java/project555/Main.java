package project555;

import java.awt.Color;

import javax.servlet.http.*;
import javax.swing.JFrame;


public class Main{
	
	public static JFrame frame;
	
	public static String ID = null;
	public static String PW = null;
	
//	public static String ID = "20200996";
//	public static String PW = "nagyeong01@@@";
	
	public static String db_url = "jdbc:mysql://huouvcti.cafe24.com/huouvcti";
	public static String db_user = "huouvcti";
	public static String db_pw = "nagyeong01@";
	
	Main(){
		if(ID == null || PW == null) {
			new LogIn();
		} else {
			new Home();
		}
	}
	
	public static void main(String[] args) {
		frame = new JFrame();
		frame.setUndecorated(false);
		frame.setSize(1300, 800);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.getContentPane().setLayout(null);
		
		new Main();
	}
	
}
