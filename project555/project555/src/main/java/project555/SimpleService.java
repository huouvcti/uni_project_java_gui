/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package project555;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


public class SimpleService {
	
	private WebElement element;
	private WebDriver driver;
	public static String [] week = new String [8]; 
	public static String[] period = new String [17];  
	public static String [][] subject = new String[17][8];
	public static int row = 0;
	
	
	//Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "chromedriver.exe";
	
	
	private String base_url;
	
	public SimpleService() {
	        super();
	 
	        //System Property SetUp
	        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
	        //Driver SetUp
	        driver = new ChromeDriver();
	        base_url = "https://portal.hoseo.edu/saml/Auth.do";
	}
	
	
	public void oper(String id, String pw) {
		try {
			//get방식으로 url 요청
			driver.get(base_url);
			
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
			
			Thread.sleep(5000);
		
			// 강의 시간표로 이동
			driver.get("https://portal.hoseo.edu/p/0000000027/__wsSch006Portlet!2u2%7C4_maximized");
			
			Thread.sleep(1000);
			
			 String ab =  driver.getPageSource();
			 
			 ab = ab.split("table_15")[1];
			 ab = ab.split("</table>")[0];
			 
			 
			 String[] check_yoil = ab.split("<th scope=\"col\">");
			 
			 for(int i =1; i<check_yoil.length; i++) {
				//System.out.print(check_yoil[i].split("</th>")[0]  + "\t");
			 	week[i-1] = check_yoil[i].split("</th>")[0]  + "\t";
			 }
			 
			 System.out.println();
			 // 요일 가져오기 완료 
			 
			 String[] ck_subject = ab.split("<tr onclick=\"doSelecteXPortal_Sch006Portle");
			 
			 for(int i = 1; i<ck_subject.length;i++) {
				 
				 String temp = ck_subject[i].split("</tr>")[0];
				 
				 String data_one = temp.split("<td class=\"t_center\" title=\"")[1];
				 data_one = data_one.split("\"")[0];
				 // 교시 가져오기 
				 
				 //System.out.print(data_one + "\t");
				 period[i-1] = data_one;
				 
				 String[] data_other = temp.split("<td class=\"t_left\" title=\"");
				
				 for(int j=1; j<data_other.length; j++) {
					 	data_other[j] = data_other[j].split("\"")[0];
					 	
					 	if(data_other[j].trim().length() > 3) {
						 //System.out.print(data_other[j] + "\t");
					 		
					 		subject [row][j-1] = data_other[j] + " ";
					 	}else {
					 		subject [row][j-1] = "공강 ";
					 	 //System.out.print("공 강" + "\t");
					 	}
					 
					 	
					 	
				 }
				 System.out.println();
				 row++;
				 
				 /////////////////////////////////////////////////////
				 
//				 driver.findElement(By.className("wrapDiv")).click();
//				 driver.findElement(By.id("image07")).click();
//				 Thread.sleep(3000);
//				 driver.findElement(By.xpath("/html/body/div[2]/div[2]/ul/li/ul[5]/li/ul/li[5]/a")).click();
		}
	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.close();
		}
 
	}
}


