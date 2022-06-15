package project555;

import java.awt.*;

import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class Join{
	private JTextField idF;
	private JPasswordField pwF;
	
	
	public static JPanel joinP;
	
	Join(){
		JFrame frame = Main.frame;
		
		joinP = new JPanel();
		frame.getContentPane().add(joinP);
		joinP.setBackground(new Color(255, 220, 220));
		joinP.setBounds(new Rectangle(0, 0, 1300, 800));
		joinP.setLayout(null);
		
		
		JPanel JoinW = new JPanel();
		JoinW.setLocation(450, 100);
		JoinW.setSize(400, 600);
		joinP.add(JoinW);
		JoinW.setBackground(new Color(255, 255, 255));
		JoinW.setLayout(null);
		
		JLabel JoinTitle = new JLabel("회원가입");
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
		
		JLabel loginA = new JLabel("로그인 페이지로 이동");
		JoinW.add(loginA);
		loginA.setForeground(Color.GRAY);
		loginA.setLocation(50, 430);
		loginA.setSize(150, 30);
		loginA.setHorizontalAlignment(SwingConstants.LEFT);
		loginA.setFont(new Font("나눔고딕", Font.BOLD, 10));
		loginA.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		JButton joinBtn = new JButton("회원가입");
		JoinW.add(joinBtn);
		joinBtn.setBackground(Color.LIGHT_GRAY);
		joinBtn.setFont(new Font("나눔고딕", Font.PLAIN, 20));
		joinBtn.setBounds(50, 470, 300, 80);
		joinBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		loginA.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				joinP.setVisible(false);
				new LogIn();
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		
		
		joinBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JoinCheck joinCheck = new JoinCheck();
				
				String userId = idF.getText();
				String userPw = pwF.getText();
				
				if(userId.length() == 0) {
					JOptionPane.showMessageDialog(null, "학번을 입력하시오.");
					return;
				} else if(userPw.length() == 0) {
					JOptionPane.showMessageDialog(null, "비밀번호을 입력하시오.");
					return;
				} else {
					if(joinCheck.oper(userId, userPw)) {
						//JOptionPane.showMessageDialog(null, "회원가입 성공");
						try {
							Connection conn = DriverManager.getConnection(Main.db_url, Main.db_user, Main.db_pw);
							System.out.println(conn);
							Statement stm = conn.createStatement();
							String sql = "insert into project_auth(id, pw) values('"+userId+"', '"+userPw+"');";
							stm.executeUpdate(sql);
							stm.close();
							conn.close();
							JOptionPane.showMessageDialog(null, "회원가입 성공!");
							
							joinP.setVisible(false);
							new LogIn();
						} catch(SQLException e){
							System.out.println(e);
						} catch (Exception e) {
							System.out.println(e);
							JOptionPane.showMessageDialog(null, "DB 오류");
						} 
						
					}else {
						JOptionPane.showMessageDialog(null, "학번 또는 비밀번호를 확인하시오.");
					}
				}
			}
		});
		
		
	}
}
