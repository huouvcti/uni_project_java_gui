package project555;

import java.awt.Color;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import javax.swing.JTextArea;

public class Schedule {
	public static JPanel scheduleP;
	public static JPanel scheduleTableW;
	
	String[] week = {"", "월", "화", "수", "목", "금"};
	String[] period = {"", "0교시", "1교시", "2교시", "3교시", 
			"4교시", "5교시","6교시", "7교시", "8교시", "9교시", "10교시"};
	String[] color = {};
	JLabel[] scheduleTableWeek = new JLabel[6];
	
	JLabel[] scheduleSubL = new JLabel[20];
	
	JLabel subSelect_sub;
	JLabel subSelect_time;
	JLabel subSelect_room;
	JTextArea subSelect_memo;
	
	int selected_c=0;
	
	int c=-1;	//과목 개수
	
	Schedule(){
		JFrame frame = Main.frame;
		
		scheduleP = new JPanel();
		frame.getContentPane().add(scheduleP);
		scheduleP.setBounds(new Rectangle(0, 0, 1300, 800));
		scheduleP.setBackground(new Color(255, 220, 220));
		scheduleP.setLayout(null);
		
		new NavLabel(scheduleP);
		
		JLabel title = new JLabel("시간표");
		scheduleP.add(title);
		title.setFont(new Font("나눔고딕", Font.PLAIN, 40));
		title.setBounds(50, 50, 150, 70);
		
		/*
		 * 시간표
		 */
		scheduleTableW = new JPanel();
		scheduleTableW.setBounds(new Rectangle(50, 120, 600, 600));
		scheduleTableW.setBackground(new Color(255, 255, 255));
		scheduleP.add(scheduleTableW);
		scheduleTableW.setLayout(null);
		
		JButton addBtn = new JButton("+");
		scheduleP.add(addBtn);
		addBtn.setFont(new Font("나눔고딕", Font.PLAIN, 15));
		addBtn.setBounds(550, 70, 50, 50);
		addBtn.setBackground(Color.LIGHT_GRAY);
		
		JButton deleteBtn = new JButton("x");
		scheduleP.add(deleteBtn);
		deleteBtn.setFont(new Font("나눔고딕", Font.PLAIN, 15));
		deleteBtn.setBounds(600, 70, 50, 50);
		deleteBtn.setBackground(Color.LIGHT_GRAY);
		
		
		for(int i=0; i<6; i++) {
			JLabel l = new JLabel(week[i]);
			scheduleTableW.add(l);
			l.setBounds(new Rectangle(100*i, 0, 100, 70)); //크기 100*70
			l.setHorizontalAlignment(JLabel.CENTER);
			l.setFont(new Font("나눔고딕", Font.BOLD, 20));
			l.setOpaque(true);
			l.setBackground(new Color(255, 255, 255));
			
		}
		for(int i=0; i<12; i++) {
			JLabel l = new JLabel(period[i]);
			scheduleTableW.add(l);
			l.setBounds(new Rectangle(0, i*70, 100, 70)); //크기 100*70
			l.setHorizontalAlignment(JLabel.CENTER);
			l.setFont(new Font("나눔고딕", Font.BOLD, 15));
			l.setOpaque(true);
			l.setBackground(new Color(255, 255, 255));
		}
		
		
		//DB 불러오기
		try {
			Connection conn = DriverManager.getConnection(Main.db_url, Main.db_user, Main.db_pw);
			Statement stm = conn.createStatement();
			
			String select_sql = "SELECT * FROM project_subject WHERE id='"+Main.ID+"';";
			
			ResultSet rs = stm.executeQuery(select_sql);
			
			while(rs.next()) {
				// 시간표 O
				c = rs.getInt("c");
				String sub = rs.getString("subject");
				int row = rs.getInt("row");
				int col = rs.getInt("col");
				int h = rs.getInt("h");
				int[] color = new int[3];
				color[0] = Integer.valueOf(rs.getString("color").split(",")[0]);
				color[1] = Integer.valueOf(rs.getString("color").split(",")[1]);
				color[2] = Integer.valueOf(rs.getString("color").split(",")[2]);
				schedule_draw(c, sub, row, col, h, color);
			}
			rs.close();
			stm.close();
			conn.close();
		} catch(SQLException e) {
			System.out.println(e);
		} catch(Exception e) {
			System.out.println(e);
		}
		
		// RE (새로고침) 버튼 동작
//		reBtn.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent event) {
//				new Schedule();
//			}
//		});
		
		
		// + (시간표 추가) 버튼 동작
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// if문 -> 시간표가 없을 때만 실행
				// X 버튼 추가 예정 -> delete문 실행
				// (다른 시간표를 불러올 경우 테이블 값이 꼬일 수 있기 때문)
				try {
					Connection conn = DriverManager.getConnection(Main.db_url, Main.db_user, Main.db_pw);
					Statement stm = conn.createStatement();
					
					String select_sql = "SELECT * FROM project_subject WHERE id='"+Main.ID+"';";
					
					ResultSet rs = stm.executeQuery(select_sql);
					if(rs.next()) {
						// 시간표 O
						JOptionPane.showMessageDialog(null, "기존 시간표를 삭제해주세요.\n(X 버튼 클릭!)");
					} else {
						// 시간표 X
						SimpleService selTest = new SimpleService();
						selTest.oper(Main.ID, Main.PW);
						
						schedule_add(selTest.subject);
						new Schedule();
					}
					rs.close();
					stm.close();
					conn.close();
				} catch(SQLException e) {
					System.out.println(e);
				} catch(Exception e) {
					System.out.println(e);
				}
			}
		});
		
		// x (시간표 삭제) 버튼 동작
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int delete_A = JOptionPane.showConfirmDialog(null, "시간표를 삭제하시겠습니까?",
						"경고창", JOptionPane.YES_NO_OPTION);
//				System.out.println(delete_A);
//				// X: -1, Yes: 0, No: 1
				if(delete_A == 0) {
					try {
						Connection conn = DriverManager.getConnection(Main.db_url, Main.db_user, Main.db_pw);
						Statement stm = conn.createStatement();
	
						String delete_sql = "DELETE FROM project_subject WHERE id='"+Main.ID+"';";
						stm.executeUpdate(delete_sql);
						
						stm.close();
						conn.close();
					} catch(SQLException e) {
						System.out.println(e);
					} catch(Exception e) {
						System.out.println(e);
					}
					new Schedule();
				}	
			}
		});
		
		
		/////////////////////////////////////////////////////////////////////////////////////
		/*
		 * 과목 클릭시 부가 설명(?)
		 */
		/////////////////////////////////////////////////////////////////////////////////////
		
		if(c > 0) {
			for(int i=1; i<=c; i++) {
				scheduleSubL[i].addMouseListener(new MouseHandler());
			}
		}
		
		JPanel subSelectP = new JPanel();
		subSelectP.setBorder(null);
		subSelectP.setBackground(Color.WHITE);
		scheduleP.add(subSelectP);
		subSelectP.setBounds(new Rectangle(700, 120, 530, 600));
		subSelectP.setLayout(null);
		subSelectP.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		subSelectP.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent event) {
				subSelect_memo.setFocusable(false);
				if(selected_c >0) {
					try {
						Connection conn = DriverManager.getConnection(Main.db_url, Main.db_user, Main.db_pw);
						Statement stm = conn.createStatement();
						
						String update_sql = "UPDATE project_subject SET memo='"+subSelect_memo.getText()
											+"' WHERE id='"+Main.ID+"' AND c="+selected_c+";";
						stm.executeUpdate(update_sql);
						
						stm.close();
						conn.close();
					} catch(SQLException e) {
						System.out.println(e);
					} catch(Exception e) {
						System.out.println(e);
					}
				}
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}

			public void mouseEntered(MouseEvent e) {}

			public void mouseExited(MouseEvent e) {}
			
		});
		
		subSelect_sub = new JLabel(""); //공백 5
		subSelect_sub.setFont(new Font("나눔고딕", Font.PLAIN, 30));
		subSelect_sub.setHorizontalAlignment(SwingConstants.LEFT);
		subSelect_sub.setOpaque(true);
		subSelect_sub.setBackground(Color.LIGHT_GRAY);
		subSelect_sub.setBounds(0, 0, 530, 100);
		subSelectP.add(subSelect_sub);
		
		subSelect_time = new JLabel("");		//공백 6
		subSelect_time.setFont(new Font("나눔고딕", Font.PLAIN, 20));
		subSelect_time.setBounds(0, 120, 530, 50);
		subSelectP.add(subSelect_time);
		
		subSelect_room = new JLabel("");						//공백 6
		subSelect_room.setFont(new Font("나눔고딕", Font.PLAIN, 20));
		subSelect_room.setBounds(0, 170, 530, 50);
		subSelectP.add(subSelect_room);

		
		subSelect_memo = new JTextArea(5, 5);
		subSelect_memo.setBorder(null);
		subSelect_memo.setFont(new Font("나눔고딕", Font.PLAIN, 20));
		subSelect_memo.setBounds(40, 250, 450, 300);
		subSelectP.add(subSelect_memo);
		
		
		
		
		
		subSelect_memo.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				subSelect_memo.setFocusable(true);
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			
		});
		subSelect_memo.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				subSelect_memo.setBorder(new LineBorder(Color.LIGHT_GRAY));
			}
			public void focusLost(FocusEvent e) {
				subSelect_memo.setBorder(null);
			}
		});
		
		
		
		
		
	}
	
	
/////////////////////////////////////////////////////////////////////////
	/*
	 * 시간표 첫 세팅 (크롤링 후 불러오기)
	 */
	public void schedule_add(String[][] subject){
		for(int i=0; i<6; i++) {
			JLabel l = new JLabel(week[i]);
			scheduleTableW.add(l);
			l.setBounds(new Rectangle(100*i, 0, 100, 70)); //크기 100*70
			l.setHorizontalAlignment(JLabel.CENTER);
			l.setFont(new Font("나눔고딕", Font.BOLD, 20));
			l.setOpaque(true);
			l.setBackground(new Color(255, 255, 255));
			
		}
		for(int i=0; i<12; i++) {
			JLabel l = new JLabel(period[i]);
			scheduleTableW.add(l);
			l.setBounds(new Rectangle(0, i*70, 100, 70)); //크기 100*70
			l.setHorizontalAlignment(JLabel.CENTER);
			l.setFont(new Font("나눔고딕", Font.BOLD, 15));
			l.setOpaque(true);
			l.setBackground(new Color(255, 255, 255));
		}
		
		
		
		
		try {
			Connection conn = DriverManager.getConnection(Main.db_url, Main.db_user, Main.db_pw);
			Statement stm = conn.createStatement();
			
			int c=1; //count - 1부터 저장
		
			for(int i=0; i<10; i++) {
				for(int j=0; j<5; j++) {
					Random rand = new Random();
					String sub = subject[i][j].split("-")[0]; //과목명만
					int[] randColor = {rand.nextInt(100)+155, rand.nextInt(100)+155, rand.nextInt(100)+155};
					int k=1;
					try {	//연강 합치기
						if(subject[i][j].equals(subject[i+1][j])) {
							k=2;
							if(subject[i][j].equals(subject[i+2][j]))
								k=3;
						}
						if(subject[i][j].equals(subject[i-1][j]))
							continue;
					}catch(Exception e) {}
					
					
					if(!subject[i][j].contains("공강")) {
						String name = subject[i][j].split("-")[0];
						String part = subject[i][j].split("-")[1].substring(0,2);
						String room = subject[i][j].split("-")[1];
						room = room.substring(3, room.length()-2);
						String color = randColor[0]+","+randColor[1]+","+randColor[2];
						
						String insert_sql = "INSERT INTO project_subject(id,row,col,h,c,subject,part,room,color) "
								+"VALUES('"+Main.ID+"',"+i+","+j+","+k+","+c+",'"+name+"','"+part+"','"+room+"','"+color+"')";
						stm.executeUpdate(insert_sql);
						
						c++;
					}
				}	
			}
			stm.close();
			conn.close();
		} catch(SQLException e) {
			System.out.println(e);
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	/*
	 * 시간표 그리기
	 */
	public void schedule_draw(int c, String sub, int row, int col, int h, int[] color) {
		scheduleSubL[c] = new JLabel(sub);
		scheduleTableW.add(scheduleSubL[c]);
		scheduleSubL[c].setBounds(new Rectangle(100+col*100, 70+row*70, 100, 70*h)); //크기 100*70
		scheduleSubL[c].setHorizontalAlignment(JLabel.CENTER);
		scheduleSubL[c].setFont(new Font("나눔고딕", Font.BOLD, 15));
		scheduleSubL[c].setOpaque(true);
		scheduleSubL[c].setBackground(new Color(color[0], color[1], color[2]));
		scheduleSubL[c].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	
	
///////////////////////////////////////////////////////////////////////////
	/*
	 * 시간표 라벨 MouseListener
	 */
	class MouseHandler implements MouseListener{

		public void mouseClicked(MouseEvent e) {
			for(int i=1; i<20; i++) {
				if(e.getSource() == scheduleSubL[i]) {
					subSelectP_draw(i);
					selected_c = i;
				}
			}
		}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}
	
/////////////////////////////////////////////////////////////////////////////////
	/*
	 * subSelectP 채우기
	 */
	public void subSelectP_draw(int c) {
		String subject;
		String time;
		String room;
		
		try {
			Connection conn = DriverManager.getConnection(Main.db_url, Main.db_user, Main.db_pw);
			Statement stm = conn.createStatement();
			
			String select_sql = "SELECT * FROM project_subject WHERE id='"+Main.ID+"' AND c="+c+";";
			ResultSet rs = stm.executeQuery(select_sql);
			
			while(rs.next()) {
				String sub = rs.getString("subject");
				String part = rs.getString("part");
				int row = rs.getInt("row");
				int col = rs.getInt("col");
				int h = rs.getInt("h");
				int[] color = new int[3];
				color[0] = Integer.valueOf(rs.getString("color").split(",")[0]);
				color[1] = Integer.valueOf(rs.getString("color").split(",")[1]);
				color[2] = Integer.valueOf(rs.getString("color").split(",")[2]);
				String memo = rs.getString("memo");
				
				subject = sub+" ["+part+"]";
				room = rs.getString("room");
				
				// 시간 계산
				int t1 = row;
				int t2 = t1 + h -1;
				int[] t3 = new int[2];
				int[] t4 = new int[2];
				
				String w = "";
				switch(col) {
					case 0: w = "월요일"; break;
					case 1: w = "화요일"; break;
					case 2: w = "수요일"; break;
					case 3: w = "목요일"; break;
					case 4: w = "금요일"; break;
				}
				t3[0] = row+8;
				t3[1] = 30;
				
				t4[1] = 30 + h*50 + (h-1)*10;
				if(t4[1] > 30) {
					t4[0] = t3[0] + t4[1]/60;
					t4[1] = t4[1]%60;
				}
				
				if(h > 1) {
					time = w+" "+t1+"~"+t2+"교시 ("
						+t3[0]+":"+t3[1]+"~"+t4[0]+":"+t4[1]+")";
				} else {
					time = w+" "+t1+"교시 ("
							+t3[0]+":"+t3[1]+"~"+t4[0]+":"+t4[1]+")";
				}
				/* <출력 예시>
				   subject = 기초중국어(1) [02]
				   room = 강석규교육관(아산) 319
				   time = 월요일 1~2교시 (9:30~11:20)
				 */
				subject = "     "+subject;
				time = "      "+time;
				room = "      "+room;
				
				subSelect_sub.setText(subject);
				subSelect_sub.setBackground(new Color(color[0], color[1], color[2]));
				subSelect_time.setText(time);
				subSelect_room.setText(room);
				subSelect_memo.setText(memo);
			}
			rs.close();
			stm.close();
			conn.close();
		} catch(SQLException e) {
			System.out.println(e);
		} catch(Exception e) {
			System.out.println(e);
		}
		
		
		
	}
}
