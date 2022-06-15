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
			//get방식으로 url 요청
			driver.get(auth_url);
			
			Thread.sleep(3000);
		
			//아이디 입력
			element = driver.findElement(By.id("user_id"));Thread.sleep(500);
			element.sendKeys(id); //아이디
			
			//패스워드 입력
			element = driver.findElement(By.id("user_password"));
			element.sendKeys(pw); //패스워드
			Thread.sleep(1000);
			
			//전송
			element = driver.findElement(By.className("l_ck_Login"));
			element.submit();
			
			Thread.sleep(3000);
			
			//경고창 제거
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_ENTER);
			
			Thread.sleep(1000);
			
			//현재 페이지
			String current_url = driver.getCurrentUrl();
			
			
			if(current_url.contains("0000000027")) {
				//System.out.println("성공");
				check = true;
			}else {
				//System.out.println("실패");
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
