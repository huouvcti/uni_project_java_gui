package project555;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.border.MatteBorder;

public class Todo {
	
	JPanel todoP;
	String [] s = {"Month","Week","Day"};
	JCheckBox [][] ck = new JCheckBox [3][8];
	JComboBox combo;
	private JTextField insert;
	
	public Todo() {
		
		JFrame frame = Main.frame;
		
		todoP = new JPanel();
		todoP.setBounds(new Rectangle(0, 0, 1300, 800));
		todoP.setBackground(new Color(255, 220, 220));
		frame.getContentPane().add(todoP);
		todoP.setLayout(null);
		
		new NavLabel(todoP);
		
		JLabel title = new JLabel("ÇÒ ÀÏ");
		todoP.add(title);
		title.setFont(new Font("³ª´®°íµñ", Font.PLAIN, 40));
		title.setBounds(50, 50, 150, 70);
		
		
		JPanel todoW = new JPanel();
		todoP.add(todoW);
		todoW.setBounds(new Rectangle(50, 120, 1180, 500));
		todoW.setBackground(new Color(255, 220, 220));
		todoW.setLayout(null);
		
		
		JPanel[] part = new JPanel[3];
		for(int i=0; i<3; i++) {
			part[i] = new JPanel();
			todoW.add(part[i]);
			part[i].setBounds(new Rectangle(i*410, 0, 360, 500));
			part[i].setBackground(new Color(255, 255, 255));
			part[i].setLayout(null);
		}
		
		JLabel[] label = new JLabel[3]; 
		for(int i=0; i<3; i++) {
			label[i] = new JLabel(s[i]);
			part[i].add(label[i]);
			label[i].setBounds(new Rectangle(0, 0, 360, 100));
			label[i].setHorizontalAlignment(SwingConstants.CENTER);
			label[i].setFont(new Font("³ª´®°íµñ",Font.PLAIN, 30));
		}
		
		JPanel[] list = new JPanel[3];
		for(int i=0; i<3; i++) {
			list[i] = new JPanel();
			part[i].add(list[i]);
			list[i].setBounds(new Rectangle(0, 100, 360, 500));
			list[i].setBackground(new Color(255, 255, 255));
			list[i].setBorder(new MatteBorder(0, 30, 0, 0, new Color(255, 255, 255)));
			list[i].setLayout(new GridLayout(10,0));
		}
		
		for(int i=0; i<3; i++) {
			for(int j=0; j<8; j++) {
				ck[i][j] = new JCheckBox("");
				list[i].add(ck[i][j]);
				ck[i][j].setBackground(new Color(255, 255, 255));
				ck[i][j].setFont(new Font("³ª´®°íµñ",Font.PLAIN, 20));
				ck[i][j].addActionListener(new ckClick());
			}
		}
		
		combo = new JComboBox(s);
		todoP.add(combo);
		combo.setBackground(new Color(255, 255, 255));
		combo.setBounds(50, 650, 170, 70);
		combo.setFont(new Font("³ª´®°íµñ", Font.PLAIN, 25));
		
		insert = new JTextField();
		todoP.add(insert);
		insert.setBounds(250, 650, 980, 70);
		insert.setHorizontalAlignment(SwingConstants.CENTER);
		insert.setBorder(new LineBorder(Color.BLACK));
		insert.setFont(new Font("³ª´®°íµñ", Font.PLAIN, 25));
		
		insert.addKeyListener(new insertKey());
		
		
		try {
			Connection conn = DriverManager.getConnection(Main.db_url, Main.db_user, Main.db_pw);
			Statement stm = conn.createStatement();
			
			boolean check = false;
			String select_sql = "SELECT * from project_todo WHERE id='"+Main.ID+"';";
			ResultSet rs = stm.executeQuery(select_sql);
			if(rs.next()) {
				rs.beforeFirst();
				while(rs.next()) {
					int i = rs.getInt("i");
					int j = rs.getInt("j");
					String memo = rs.getString("memo");
					ck[i][j].setText(memo);
				}
			} else {
				for(int i=0; i<3; i++) {
					for(int j=0; j<8; j++) {
						String insert_sql = "INSERT INTO project_todo(id, i, j, memo) "
								+ "VALUES('"+Main.ID+"',"+i+","+j+",'');";
						stm.executeUpdate(insert_sql);
					}
				}
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
	
	public void db_update(int i, int j, String memo) {
		try {
			Connection conn = DriverManager.getConnection(Main.db_url, Main.db_user, Main.db_pw);
			Statement stm = conn.createStatement();
			
			String update_sql = "UPDATE project_todo SET memo='"+memo+
									"' WHERE id='"+Main.ID+"' AND i="+i+" AND j="+j+";";
			stm.executeUpdate(update_sql);
			
			stm.close();
			conn.close();
		} catch(SQLException e) {
			System.out.println(e);
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	
	

	public class insertKey implements KeyListener{
		public void keyTyped(KeyEvent e) {}
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				String temp = insert.getText();
				int i = combo.getSelectedIndex();
				boolean in = false;
				if(temp.length() == 0) {
					JOptionPane.showMessageDialog(null, "³»¿ëÀ» ÀÔ·ÂÇÏ¼¼¿ä.");
				} else {
					for(int j=0; j<8; j++) {
						if(ck[i][j].getText().length() == 0)  {
							ck[i][j].setText(temp);
							db_update(i, j, temp);
							insert.setText("");
							in = true;
							break;
						}
					}
					if(!in) {
						JOptionPane.showMessageDialog(null, "±âÁ¸ ÀÏÁ¤À» »èÁ¦ÇØÁÖ¼¼¿ä.");
					}
				}
				
			}
		}
		public void keyReleased(KeyEvent e) {}
	}
	
	public class ckClick implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			for(int i=0; i<3; i++) {
				for(int j=0; j<8; j++) {
					if(e.getSource() == ck[i][j]) {
						if(ck[i][j].isSelected()) {
							ck[i][j].setText("");
							ck[i][j].setSelected(false);
							db_update(i, j, "");
						}
					}
				}
			}
		}
	}
	
	
}

