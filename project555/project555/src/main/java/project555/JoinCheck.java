package project555;

import java.awt.Robot;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class JoinCheck{
	
	private WebElement element;
	private WebDriver driver;
	
	public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "chromedriver.exe";
	
	private String auth_url;
	
	public JoinCheck() {
		super();

        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        
        driver = new ChromeDriver();
        auth_url = "https://portal.hoseo.edu/saml/Auth.do";
	}

	public boolean oper(String id, String pw) {
		boolean check = false;
		
		try {
			//get������� url ��û
			driver.get(auth_url);
			
			Thread.sleep(3000);
		
			//���̵� �Է�
			element = driver.findElement(By.id("user_id"));Thread.sleep(500);
			element.sendKeys(id); //���̵�
			
			//�н����� �Է�
			element = driver.findElement(By.id("user_password"));
			element.sendKeys(pw); //�н�����
			Thread.sleep(1000);
			
			//����
			element = driver.findElement(By.className("l_ck_Login"));
			element.submit();
			
			Thread.sleep(3000);
			
			//���â ����
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_ENTER);
			
			Thread.sleep(1000);
			
			//���� ������
			String current_url = driver.getCurrentUrl();
			
			
			if(current_url.contains("0000000027")) {
				//System.out.println("����");
				check = true;
			}else {
				//System.out.println("����");
				check = false;
			}
			
	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.close();
		}
		
		return check;
 
	}
	
}
