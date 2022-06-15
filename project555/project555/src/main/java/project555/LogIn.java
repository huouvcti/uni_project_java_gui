package project555;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class LogIn{
	private JTextField idF;
	private JPasswordField pwF;
	
	public static JPanel loginP;
	
	LogIn(){
		JFrame frame = Main.frame;
		
		loginP = new JPanel();
		frame.getContentPane().add(loginP);
		loginP.setBackground(new Color(255, 220, 220));
		loginP.setBounds(new Rectangle(0, 0, 1300, 800));
		loginP.setLayout(null);
		LogIn.loginP.setVisible(true);
		
		
		JPanel JoinW = new JPanel();
		JoinW.setLocation(450, 100);
		JoinW.setSize(400, 600);
		loginP.add(JoinW);
		JoinW.setBackground(new Color(255, 255, 255));
		JoinW.setLayout(null);
		
		JLabel JoinTitle = new JLabel("로그인");
		JoinTitle.setHorizontalAlignment(SwingConstants.CENTER);
		JoinTitle.setLocation(0, 0);
		JoinTitle.setFont(new Font("나눔고딕", Font.BOLD, 40));
		JoinTitle.setSize(400, 150);
		JoinW.add(JoinTitle);
		
		JLabel idL = new JLabel("학번");
		JoinW.add(idL);
		idL.setFont(new Font("나눔고딕", Font.PLAIN, 20));
		idL.setBounds(50, 150, 100, 50);
		
		
		idF = new JTextField(8);
		idF.setFont(new Font("나눔고딕", Font.PLAIN, 20));
		JoinW.add(idF);
		idF.setBorder(new LineBorder(Color.LIGHT_GRAY));
		idF.setBounds(50, 200, 300, 50);
		
		idF.setColumns(10);
		
		JLabel pwL = new JLabel("비밀번호");
		JoinW.add(pwL);
		pwL.setFont(new Font("나눔고딕", Font.PLAIN, 20));
		pwL.setBounds(50, 300, 100, 50);
		
		
		pwF = new JPasswordField(20);
		pwF.setFont(new Font("나눔고딕", Font.PLAIN, 20));
		JoinW.add(pwF);
		pwF.setBorder(new LineBorder(Color.LIGHT_GRAY));
		pwF.setBounds(50, 350, 300, 50);
		pwF.setColumns(10);
		
		JLabel joinA = new JLabel("회원가입 페이지로 이동");
		JoinW.add(joinA);
		joinA.setForeground(Color.GRAY);
		joinA.setLocation(50, 430);
		joinA.setSize(150, 30);
		joinA.setHorizontalAlignment(SwingConstants.LEFT);
		joinA.setFont(new Font("나눔고딕", Font.BOLD, 10));
		joinA.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		JButton loginBtn = new JButton("로그인");
		JoinW.add(loginBtn);
		loginBtn.setBackground(Color.LIGHT_GRAY);
		loginBtn.setFont(new Font("나눔고딕", Font.PLAIN, 20));
		loginBtn.setBounds(50, 470, 300, 80);
		loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		joinA.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				loginP.setVisible(false);
				new Join();
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String userId = idF.getText();
				String userPw = pwF.getText();
				
				if(userId.length() == 0) {
					JOptionPane.showMessageDialog(null, "학번을 입력하시오.");
					return;
				} else if(userPw.length() == 0) {
					JOptionPane.showMessageDialog(null, "비밀번호을 입력하시오.");
					return;
				} else {
					try {
						Connection conn = DriverManager.getConnection(Main.db_url, Main.db_user, Main.db_pw);
						Statement stm = conn.createStatement();
						String sql = "SELECT * FROM project_auth WHERE id='"+userId+"';";
						ResultSet rs = stm.executeQuery(sql);
						
						String rs_pw = null;
						while(rs.next()) {
							rs_pw = rs.getString("pw");
						}
						
						if(rs_pw.equals(userPw)) {
							JOptionPane.showMessageDialog(null, "로그인 성공");
							
							Main.ID = userId;
							Main.PW = userPw;
							loginP.setVisible(false);
							new Home();
							
						}else {
							JOptionPane.showMessageDialog(null, "학번 또는 비밀번호를 확인하시오.");
						}
						
						stm.close();
						conn.close();
					
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "DB 오류");
					}
					
				}
				
				
				
					
			}
		});
		
		
	}
}
