package project555;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import project555.Schedule.MouseHandler;

public class Notice {
	
	public static JPanel noticeP;
	
	JComboBox comboBox;
	
	JLabel[] no_td = new JLabel[11];
	JLabel[] title_td = new JLabel[11];
	JLabel[] writer_td = new JLabel[11];
	JLabel[] date_td = new JLabel[11];
	String[] href = new String[11];
	
	Notice(){
		JFrame frame = Main.frame;
		
		noticeP = new JPanel();
		frame.getContentPane().add(noticeP);
		noticeP.setBounds(new Rectangle(0, 0, 1300, 800));
		noticeP.setBackground(new Color(255, 220, 220));
		noticeP.setLayout(null);
		
		new NavLabel(noticeP);
				
		String[] notice_text = {"°øÁö»çÇ×", "ÇÐ»ç°øÁö", "ÀåÇÐ°øÁö", "»çÈ¸ºÀ»ç", "¿ÜºÎ°øÁö", "Ãë¾÷°øÁö"};
		
		comboBox = new JComboBox(notice_text);
		comboBox.setFocusable(false);
		comboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		noticeP.add(comboBox);
		comboBox.setBounds(50, 70, 200, 50);
		comboBox.setBackground(new Color(255, 220, 220));
		comboBox.setFont(new Font("³ª´®°íµñ", Font.PLAIN, 40));
		
//		JLabel updateL = new JLabel("ÃÖ±Ù ¾÷µ¥ÀÌÆ®: 2020/12/12 10:30:20");
//		noticeP.add(updateL);
//		updateL.setFont(new Font("³ª´®°íµñ", Font.PLAIN, 15));
//		updateL.setForeground(Color.black);
//		updateL.setBounds(870, 90, 250, 30);
		
		JButton updateBtn = new JButton("¾÷µ¥ÀÌÆ®");
		noticeP.add(updateBtn);
		updateBtn.setFont(new Font("³ª´®°íµñ", Font.PLAIN, 15));
		updateBtn.setBackground(new Color(220, 220, 220));
		updateBtn.setBounds(1130, 90, 100, 30);
		
		
		JPanel noticeW = new JPanel();
		noticeW.setBorder(new LineBorder(new Color(192, 192, 192)));
		noticeP.add(noticeW);
		noticeW.setBounds(new Rectangle(50, 120, 1180, 600));
		noticeW.setBackground(new Color(255, 255, 255));
		noticeW.setLayout(null);
		
		JLabel no_th = new JLabel("¹øÈ£");
		noticeW.add(no_th);
		no_th.setBounds(0, 0, 100, 50);
		no_th.setFont(new Font("³ª´®°íµñ", Font.BOLD, 20));
		no_th.setHorizontalAlignment(SwingConstants.CENTER);
		no_th.setForeground(Color.GRAY);
		no_th.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		
		JLabel title_th = new JLabel("Á¦¸ñ");
		noticeW.add(title_th);
		title_th.setBounds(100, 0, 750, 50);
		title_th.setFont(new Font("³ª´®°íµñ", Font.BOLD, 20));
		title_th.setHorizontalAlignment(SwingConstants.CENTER);
		title_th.setForeground(Color.GRAY);
		title_th.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));;
		
		JLabel writer_th = new JLabel("ÀÛ¼ºÀÚ");
		noticeW.add(writer_th);
		writer_th.setBounds(850, 0, 165, 50);
		writer_th.setFont(new Font("³ª´®°íµñ", Font.BOLD, 20));
		writer_th.setHorizontalAlignment(SwingConstants.CENTER);
		writer_th.setForeground(Color.GRAY);
		writer_th.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		
		JLabel date_th = new JLabel("µî·ÏÀÏÀÚ");
		noticeW.add(date_th);
		date_th.setBounds(1015, 0, 165, 50);
		date_th.setFont(new Font("³ª´®°íµñ", Font.BOLD, 20));
		date_th.setHorizontalAlignment(SwingConstants.CENTER);
		date_th.setForeground(Color.GRAY);
		date_th.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		
		for(int i=0; i<11; i++) {
			no_td[i] = new JLabel();
			noticeW.add(no_td[i]);
			no_td[i].setBounds(0, (i+1)*50, 100, 50);
			no_td[i].setFont(new Font("³ª´®°íµñ", Font.PLAIN, 20));
			no_td[i].setHorizontalAlignment(SwingConstants.CENTER);
			no_td[i].setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		
			title_td[i] = new JLabel();
			noticeW.add(title_td[i]);
			title_td[i].setBounds(100, (i+1)*50, 750, 50);
			title_td[i].setFont(new Font("³ª´®°íµñ", Font.PLAIN, 20));
			title_td[i].setHorizontalAlignment(SwingConstants.CENTER);
			title_td[i].setBorder(new LineBorder(Color.LIGHT_GRAY, 1));;
			title_td[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			title_td[i].addMouseListener(new titleMouse());
			
			writer_td[i] = new JLabel();
			noticeW.add(writer_td[i]);
			writer_td[i].setBounds(850, (i+1)*50, 165, 50);
			writer_td[i].setFont(new Font("³ª´®°íµñ", Font.PLAIN, 20));
			writer_td[i].setHorizontalAlignment(SwingConstants.CENTER);
			writer_td[i].setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		
			date_td[i] = new JLabel();
			noticeW.add(date_td[i]);
			date_td[i].setBounds(1015, (i+1)*50, 165, 50);
			date_td[i].setFont(new Font("³ª´®°íµñ", Font.PLAIN, 20));
			date_td[i].setHorizontalAlignment(SwingConstants.CENTER);
			date_td[i].setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		}
		
		// µðÆúÆ® °ª
		comboBox.setSelectedIndex(0);
		select_category(0);
		
		// ¼±ÅÃ
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
					int c = comboBox.getSelectedIndex();
					select_category(c);
			}
		});
		
		// ¾÷µ¥ÀÌÆ® ¹öÆ°
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Notice_process n = new Notice_process();
				n.delete();
				n.insert();
				new Notice();
			}
		});
	}
	
	
	
	public void select_category(int c) {
		try {
			Connection conn = DriverManager.getConnection(Main.db_url, Main.db_user, Main.db_pw);
			Statement stm = conn.createStatement();
			
			String select_sql = "SELECT * FROM project_notice WHERE category="+c+" ORDER BY no DESC;";
			ResultSet rs = stm.executeQuery(select_sql);
			int i=0;
			while(rs.next()) {
				no_td[i].setText(rs.getString("no"));
				title_td[i].setText(rs.getString("title"));
				writer_td[i].setText(rs.getString("writer"));
				date_td[i].setText(rs.getString("date"));
				href[i] = rs.getString("href");
				i++;
			}
			rs.close();
			stm.close();
			conn.close();
		} catch(SQLException e) {
//			System.out.println(e);
		} catch(Exception e) {
//			System.out.println(e);
		}
	}
	
	public void notice_browse(int i) {
		Desktop desktop = Desktop.getDesktop();
		String base_url = "http://www.hoseo.ac.kr/Home//BBSView.mbz?action=MAPP_1708240139&schIdx=";
		try {
			URI uri = new URI(base_url+href[i]);
			desktop.browse(uri);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	class titleMouse implements MouseListener{
		public void mouseClicked(MouseEvent e) {
			for(int i=0; i<12; i++) {
				if(e.getSource() == title_td[i]) {
					notice_browse(i);
				}
			}
		}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}

}
