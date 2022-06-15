package project555;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.LineBorder;



public class Home {
	
	public static JPanel homeP;
	JLabel[] menu = new JLabel[5];
	
	Home(){
		JFrame frame = Main.frame;
		
		homeP = new JPanel();
		homeP.setBounds(new Rectangle(0, 0, 1300, 800));
		homeP.setBackground(new Color(255, 220, 220));
		frame.getContentPane().add(homeP);
		homeP.setLayout(null);
		
		new NavLabel(homeP);
		

		ImageIcon[] menu_img = new ImageIcon[3];
		menu_img[0] = new ImageIcon("schedule.png");
		menu_img[1] = new ImageIcon("todo.png");
		menu_img[2] = new ImageIcon("notice.png");
		
		
		for(int i=0; i<3; i++)
			menu_img[i] = imgSetSize(menu_img[i], 150, 150);
		
		String menuTitle[] = {"시간표 ", "할일   ", "공지사항"};
		menu = new JLabel[5];
		for(int i=0; i<3; i++) {
			menu[i] = new JLabel(menuTitle[i], JLabel.CENTER);
			homeP.add(menu[i]);
			menu[i].setIcon(menu_img[i]);
			menu[i].setFont(new Font("맑은고딕", Font.BOLD, 30));
			menu[i].setForeground(Color.WHITE);
			menu[i].setBorder(new LineBorder(Color.white, 10, true));
			menu[i].addMouseListener(new menuClick());
			menu[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		
		menu[0].setBounds(new Rectangle(80, 100, 330, 180));
		menu[1].setBounds(new Rectangle(80, 310, 330, 180));
		menu[2].setBounds(new Rectangle(80, 520, 330, 180));
		
		
		/*
		 * 
		 */
		
		JPanel contentP = new JPanel(); 
		homeP.add(contentP);
		contentP.setBounds(new Rectangle(500, 100, 720, 600));
		contentP.setBackground(new Color(94, 121, 110));
		contentP.setBorder(new LineBorder(new Color(94, 121, 110), 5, true));
		contentP.setLayout(null);
		
		
		JPanel todayP = new JPanel();
		contentP.add(todayP);
		todayP.setBounds(new Rectangle(0, 0, 720, 380));
		todayP.setBackground(new Color(94, 121, 110));
		todayP.setLayout(null);
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy / MM / dd");
		String today = sdf.format(cal.getTime());
		
		
//		System.out.println(cal.get(Calendar.DAY_OF_WEEK));
//		일 월 화 수 목 금 토 == 1 2 3 4 5 6 7
		int col = 100;
		switch(cal.get(Calendar.DAY_OF_WEEK)) {
			case 2: col = 0; today +="   (월)"; break;
			case 3: col = 1; today +="   (화)";break;
			case 4: col = 2; today +="   (수)";break;
			case 5: col = 3; today +="   (목)";break;
			case 6: col = 4; today +="   (금)";break;
			case 7: today +="   (토)";break;
			case 1: today +="   (일)";break;
		}
		
		JLabel todayL = new JLabel(today, JLabel.CENTER);
		todayP.add(todayL);
		todayL.setBounds(0, 0, 720, 80);
		todayL.setForeground(Color.white);
		todayL.setFont(new Font("휴먼편지체", Font.PLAIN, 30));	

		try {
			Connection conn = DriverManager.getConnection(Main.db_url, Main.db_user, Main.db_pw);
			Statement stm = conn.createStatement();
			
			String select_sql = "SELECT * FROM project_subject WHERE id='"+Main.ID+"' AND col="+col+" ORDER BY row ASC;";
			ResultSet rs = stm.executeQuery(select_sql);
			
			rs.last();
			int rows = rs.getRow();
			
			if(rows > 0) {
				JLabel[][] table = new JLabel[5][2];
				for(int i=0; i<rows; i++) {
					for(int j=0; j<2; j++) {
						table[i][j] = new JLabel("", JLabel.CENTER);
						todayP.add(table[i][j]);
						table[i][j].setFont(new Font("맑은고딕", Font.PLAIN, 20));
						table[i][j].setOpaque(true);
						table[i][j].setBorder(new LineBorder(new Color(94, 121, 110), 5));
						table[i][j].setBounds(new Rectangle(j*240, i*60+80, 240*(j+1), 60));	
					}
				}
				int i=0;
				rs.beforeFirst();
				while(rs.next()) {
					String subject = rs.getString("subject");
					int row = rs.getInt("row");
					int h = rs.getInt("h");
					int[] color = new int[3];
					color[0] = Integer.valueOf(rs.getString("color").split(",")[0]);
					color[1] = Integer.valueOf(rs.getString("color").split(",")[1]);
					color[2] = Integer.valueOf(rs.getString("color").split(",")[2]);
					
					// 시간 계산
					String time;
					int t1 = row;
					int t2 = t1 + h -1;
					int[] t3 = new int[2];
					int[] t4 = new int[2];
					
					t3[0] = row+8;
					t3[1] = 30;
					
					t4[1] = 30 + h*50 + (h-1)*10;
					if(t4[1] > 30) {
						t4[0] = t3[0] + t4[1]/60;
						t4[1] = t4[1]%60;
					}
					
					if(h > 1) {
						time = t1+"~"+t2+"교시 ("
							+t3[0]+":"+t3[1]+"~"+t4[0]+":"+t4[1]+")";
					} else {
						time = t1+"교시 ("
								+t3[0]+":"+t3[1]+"~"+t4[0]+":"+t4[1]+")";
					}
					/* <출력 예시>
					   subject = 기초중국어(1)
					   time = 월요일 1~2교시 (9:30~11:20)
					 */

					table[i][0].setText(time);
					table[i][1].setText(subject);
					table[i][0].setBackground(new Color(color[0], color[1], color[2]));
					table[i][1].setBackground(new Color(color[0], color[1], color[2]));
					
					i++;
				}
				for(; i<5; i++) {
					for(int j=0; j<2; j++) {
						table[i][j].setBackground(new Color(94, 121, 110));
					}
				}
			} else {
				JLabel no_class = new JLabel("오늘은 수업이 없습니다.", JLabel.CENTER);
				todayP.add(no_class);
				no_class.setFont(new Font("휴먼편지체", Font.PLAIN, 30));
				no_class.setBounds(new Rectangle(0, 100, 720, 100));
				no_class.setForeground(Color.white);
			}
			
			rs.close();
			stm.close();
			conn.close();
		} catch(SQLException e) {
			System.out.println(e);
		} catch(Exception e) {
			System.out.println(e);
		}

		
		
		
		
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		
		/*
		 * 오늘의 명언
		 */
		String [] sentence = {"행동의 가치는 그 행동을 끝까지 이루는 데 있다.","당신이 어떤 일을 해낼 수 있는지 누군가가 물어보면 대답해라. 물론이죠! 그 다음 어떻게 그 일을 해낼 수 있을지 부지런히 고민하라."
				,"대학 졸업장은 한 인간이 완성품이라는 증명이 아니라, 인생의 준비가 되었다는 표시이다."
				,"인생에서 실패한 사람 중 다수는, 성공을 목전에 두고도 모른 채 포기한 이들이다.","미래를 예측하는 최선의 방법은, 미래를 창조하는 것이다."
				,"만족은 결과가 아니라 과정에서 온다.","절대로 고개를 떨구지 말라, 고개를 치켜들고 세상을 똑바로 바라보라.","낭비한 시간에 대한 후회는, 더 큰 시간 낭비이다."
				,"나는 내가 더 노력할수록 운이 더 좋아진다는 걸 발견했다.","강은 알고 있어 서두르지 않아도, 언젠가는 도착하게 되리라는 것을",
				"고통은 지나가지만, 아름다움은 남는다.","끝난 일에 대해서는 언급할 필요가 없으며, 지난 일에 대해서는 허물을 물을 필요가 없다."
				,"네가 가고 있는 길이 아니다 싶을때엔, 다시 처음부터 시작할 수 있는 강인함을 갖길","할 수 있다고 믿는 사람은, 그렇게 되고, 할 수 없다고 믿는 사람 역시, 그렇게 된다.",
				"매일 행복하진 않지만, 행복한 '일'은 매일 있어.","침묵보다 나은 할말이 있을 때에만, 입을 여는 게 좋다.","벚꽃은 제가 절정인 줄 모르고,절정은 또한 제 시절을 모르고"
				,"별은 바라보는 자에게 빛을 준다","편안하면서 존경받는 삶은 없다.","기회를 기다려라, 그러나 절대로 때를 기다려서는 안된다.","후회하기 싫으면 그렇게 살지 말고, 그렇게 살거면 후회하지 마라."
				,"당신이 포기할 때, 나는 시작한다.","사랑하는 이여, 상처받지 않은 사랑이 어디 있으랴, 추운 겨울 다 지내고, 꽃필차례가 바로 그대 앞에 있다."};
		
		String [] person = {"Genghis Khan","Theodore Roosevelt","Reverend Edward A. Malloy","-Thomas A. Edison","Alan Kay"
				,"James Dean","Helen Keller","Mason Cooley","Thomas Jefferson","곰돌이 푸","Pierre-Auguste Renoir","공자"
				,"<벤자민 버튼의 시간은 거꾸로 간다中>","Charles de Gaulle","곰돌이 푸","<침묵의 기술中>","이규리<벚꽃이 달아난다中>","이영도<드래곤 라자中>"
				,"유시민","F.Miller","이문열<젊은 날의 초상中>","Elon Musk","김종해<그대 앞에 보이 있다中>"};
		
		Random num = new Random();
		int randnum = num.nextInt(sentence.length-1);
		
		String [] writer = sentence[randnum].split(",");
		
		String temp = "<html><body style='text-align:center;'>";
		String temp2 = "<html><body style='text-align:center;'><br />"+"-" +person[randnum]+  "<br /></body><html>";
		
		
		for(int i=0; i<writer.length; i++) {
			temp += writer[i]+"<br />";
		}
		temp+="<br /></body></html>";
		
		JPanel pvP = new JPanel();
		contentP.add(pvP);
		pvP.setBounds(new Rectangle(0, 400, 360, 200));
		pvP.setBackground(Color.white);
		pvP.setBorder(new LineBorder(new Color(94, 121, 110), 10));
		pvP.setLayout(null);
		
		JLabel pvL = new JLabel("- 오늘의 명언 -", JLabel.CENTER);
		pvL.setBounds(0, 20, 350, 40);
		pvP.add(pvL);
		pvL.setFont(new Font("휴먼편지체", Font.PLAIN, 30));
		
		JLabel writing  = new JLabel(temp, JLabel.CENTER);
		writing.setBounds(10, 70, 340, 100);
		JLabel writing2 = new JLabel(temp2,JLabel.RIGHT);
		writing2.setBounds(10, 130, 310, 50);
		
		pvP.add(writing);
		pvP.add(writing2);
		
		writing.setFont(new Font("휴먼편지체", Font.PLAIN, 18));
		writing2.setFont(new Font("휴먼편지체", Font.PLAIN, 18));
		
		/////////////////////////////////////////////////////////////////////////////
		
		/*
		 * 오늘의 영단어
		 */
		String [][] word = {
				{"account", "계산"},
				{"account for", "를 설명하다"},
				{"benefit from", "~로부터 혜택을 받다"},
				{"condense", "압축하다"},
				{"condense into", "~로 압축하다"},
				{"better", "~를 개선하다"},
				{"conduct", "실시, 시학하다"},
				{"accure", "생기다, 얻어지다"},
				{"conductor", "지회자, 차장"},
				{"confirmation", "확정서"}
		};
		
		Random num2 = new Random();
		int randnum2 = num.nextInt(word.length);
		
		JPanel wordP = new JPanel();
		contentP.add(wordP);
		wordP.setBounds(new Rectangle(360, 400, 360, 200));
		wordP.setBackground(Color.white);
		wordP.setBorder(new LineBorder(new Color(94, 121, 110), 10));
		wordP.setLayout(null);
		
		JLabel wordL = new JLabel("- 오늘의 영단어 -", JLabel.CENTER);
		wordL.setBounds(0, 20, 350, 40);
		wordP.add(wordL);
		wordL.setFont(new Font("휴먼편지체", Font.PLAIN, 30));
		
		JLabel word1  = new JLabel(word[randnum2][0], JLabel.CENTER);
		word1.setBounds(10, 70, 340, 50);
		JLabel word2 = new JLabel(word[randnum2][1],JLabel.CENTER);
		word2.setBounds(10, 120, 340, 50);
		
		wordP.add(word1);
		wordP.add(word2);
		
		word1.setFont(new Font("휴먼편지체", Font.BOLD, 25));
		word2.setFont(new Font("휴먼편지체", Font.PLAIN, 25));
	}
	
	ImageIcon imgSetSize(ImageIcon icon, int x, int y) {
		Image ximg = icon.getImage();
		Image yimg = ximg.getScaledInstance(x, y, Image.SCALE_SMOOTH);
		ImageIcon xyimg = new ImageIcon(yimg);
		return xyimg;
		
	}
	
	class menuClick implements MouseListener{
		public void mouseClicked(MouseEvent e) {
			homeP.setVisible(false);
			if(e.getSource() == menu[0]) {
				new Schedule();
			} else if(e.getSource() == menu[1]) {
				new Todo();
			} else if(e.getSource() == menu[2]) {
				new Notice();
			} 
		}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}	
	}
}
