package project555;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NavLabel {
	
	NavLabel(final JPanel p){
		JLabel useridL = new JLabel(Main.ID);
		p.add(useridL);
		useridL.setFont(new Font("³ª´®°íµñ", Font.PLAIN, 20));
		useridL.setBounds(980, 25, 100, 50);
		
		JLabel logoutL = new JLabel("·Î±×¾Æ¿ô");
		p.add(logoutL);
		logoutL.setFont(new Font("³ª´®°íµñ", Font.PLAIN, 20));
		logoutL.setBounds(1100, 25, 100, 50);
		logoutL.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		JLabel homeBtn = new JLabel("È¨");
		p.add(homeBtn);
		homeBtn.setFont(new Font("³ª´®°íµñ", Font.PLAIN, 20));
		homeBtn.setBounds(1200, 25, 50, 50);
		homeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		logoutL.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				p.setVisible(false);
				Main.ID = null;
				Main.PW = null;
				new Main();
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		
		homeBtn.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				p.setVisible(false);
				new Home();
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
	}
}
