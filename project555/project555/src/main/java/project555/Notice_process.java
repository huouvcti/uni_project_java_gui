package project555;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Notice_process {
	
	private WebElement element;
	private static List<WebElement> elements;
	private static WebDriver driver;
	
	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "chromedriver.exe";
	
    static String url_base = "http://www.hoseo.ac.kr/Home//BBSList.mbz?action=MAPP_1708240139&schCategorycode=CTG_";
	static String[] page_code = {"17082400011", "17082400012", "17082400013", "17082400014", "20012200070", "20120400086"};
	
	public Notice_process() {}
	
	public static void insert() {
		System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        driver = new ChromeDriver();
        
		try {
			for(int i=0; i<6; i++) {
				driver.get(url_base+page_code[i]);
				Thread.sleep(3000);
				
				int[] no = new int[20];
				String[] title = new String[20];
				String[] writer = new String[20];
				String[] date = new String[20];
				String[] href = new String[20];
				int j;
				
				elements = driver.findElements(By.xpath("//*[@data-header='번호']"));
				j=0;
				for(WebElement ele : elements) {
					no[j] = Integer.parseInt(ele.getText());
					j++;
				}
				elements = driver.findElements(By.xpath("//*[@data-header='제목']"));
				j=0;
				for(WebElement ele : elements) {
					title[j] = ele.getText().replace("'", "\\'").replace("\"", "");
					j++;
				}
				elements = driver.findElements(By.xpath("//*[@data-header='작성자']"));
				j=0;
				for(WebElement ele : elements) {
					writer[j] = ele.getText();
					j++;
				}
				elements = driver.findElements(By.xpath("//*[@data-header='등록일자']"));
				j=0;
				
				for(WebElement ele : elements) {
					date[j] = ele.getText();
					j++;
				}
				elements = driver.findElements(By.xpath("//*[@data-header='제목']/a"));
				j=0;
				for(WebElement ele : elements) {
					href[j] = ele.getAttribute("href").split("'")[1];
					j++;
				}
				
//				for(j=0; j<20; j++) {
//					System.out.println(no[j]+title[j]+writer[j]+date[j]+href[j]);
//				}
				
				
				try {
					Connection conn = DriverManager.getConnection(Main.db_url, Main.db_user, Main.db_pw);
					Statement stm = conn.createStatement();
					
					for(j=0; j<20; j++) {
						String insert_sql = "INSERT INTO project_notice(category,no,title,writer,date,href) "
								+"VALUES("+i+","+no[j]+",'"+title[j]+"','"+writer[j]+"','"+date[j]+"','"+href[j]+"')";
						stm.executeUpdate(insert_sql);
					}
					stm.close();
					conn.close();
				} catch(SQLException e) {
					System.out.println(e);
				} catch(Exception e) {
					System.out.println(e);
				}	
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.close();
		}
	}
	
	public static void delete() {
		try {
			Connection conn = DriverManager.getConnection(Main.db_url, Main.db_user, Main.db_pw);
			Statement stm = conn.createStatement();
			
			String delete_sql = "DELETE FROM project_notice";
			stm.executeUpdate(delete_sql);
			
			stm.close();
			conn.close();
		} catch(SQLException e) {
			System.out.println(e);
		} catch(Exception e) {
			System.out.println(e);
		}	
	}

}
