package advance;
import java.awt.List;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
//import org.json.simple.JSONObject;
import java.util.Scanner;

import org.apache.jasper.tagplugins.jstl.core.Set;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;


// PART 1
public class advance {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException, IOException, JSONException, MalformedURLException{
		
		    WebDriver driver =new FirefoxDriver();
		
		    driver.get("http://10.0.1.86/tatoc/advanced/hover/menu");
		    WebElement menu=driver.findElement(By.xpath("html/body/div/div[2]/div[2]/span[1]"));
		   
		    Actions actions = new Actions(driver);
		    
		    actions.moveToElement(menu);

		    WebElement subMenu = driver.findElement(By.xpath("html/body/div/div[2]/div[2]/span[5]"));	
		    actions.moveToElement(subMenu);
		    actions.click().build().perform();		
		
	




// PART 2

          
		     driver.get("http://10.0.1.86/tatoc/advanced/query/gate");		
			  String dburl="jdbc:mysql://10.0.1.86/tatoc";
		      String username="tatocuser";
		      String password="tatoc01";
		      Class.forName("com.mysql.jdbc.Driver");
		      Connection con=(Connection) DriverManager.getConnection(dburl,username,password);
		      System.out.println("connected");
		     
		      String text=driver.findElement(By.cssSelector("#symboldisplay")).getText();
		      Statement stm=con.createStatement();
		      ResultSet rs=stm.executeQuery("select id from identity where symbol= '"+text+"'");
		      int id=0;
		      
		      while(rs.next())
		      {
		      	id=rs.getInt(1);
		      	System.out.print(id);
		      }
		      
		      rs.close();
		      ResultSet rs1= stm.executeQuery("select name,passkey from credentials where id='"+id+"'");
		      while(rs1.next())
		      {
		      	String name=rs1.getString(1);
		      	driver.findElement(By.cssSelector("#name")).sendKeys(name);
		      	System.out.println(name);
		      	String pwd=rs1.getString(2);
		      	driver.findElement(By.cssSelector("#passkey")).sendKeys(pwd);
		      	System.out.print(pwd);
		      	
		      }
		      rs1.close();
		      con.close();
		      driver.findElement(By.cssSelector("#submit")).click();
		      
		      
  // PART 3
		      
		      	 Thread.sleep(5000);
		         boolean played = false;
		         double totalTime ;
		         if (driver instanceof JavascriptExecutor) {

		            JavascriptExecutor js = (JavascriptExecutor) driver;
		            totalTime = (double) js.executeScript("return player.getTotalTime()");
		            
		            System.out.println(totalTime);
		            js.executeScript("player.play();");
		            
		            
		            Thread.sleep(((long)totalTime+1)*2000);
		            System.out.println("heelo");
		            driver.findElement(By.partialLinkText("Proceed")).click();
		        } else {
		            throw new IllegalStateException("This driver does not support JavaScript!");
		        }
		        
	
// PART 4
		        driver.get("http://10.0.1.86/tatoc/advanced/rest#");
		        String string= driver.findElement(By.id("session_id")).getText();
		        string =string .substring(12, string .length());
		        System.out.println(string);
		        URL url = new URL("http://10.0.1.86/tatoc/advanced/rest/service/token/"+string);

				HttpURLConnection con2 = (HttpURLConnection) url.openConnection();
				

				con2.setRequestMethod("GET");
				con2.setRequestProperty("Accept", "application/json");
				
				int  responseCode = con2.getResponseCode();
				System.out.println("\nSending 'GET' request to URL : " + url);
				System.out.println("Response Code : " + responseCode);
				if (con2.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
					+ con2.getResponseCode());
					                                }

				BufferedReader in = new BufferedReader
						(
						new InputStreamReader(con2.getInputStream()));
						String inputLine;
						StringBuffer response = new StringBuffer();
						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
							System.out.println("hi");
						}
						in.close();

						
						System.out.println(response.toString()
						);
						
						String ssss=new String(response);

						JSONObject obj=new JSONObject(ssss);
						ssss=(String) obj.get("token");

						System.out.println(ssss);

					

	                   URL url1 = new URL("http://10.0.1.86/tatoc/advanced/rest/service/register");
	                   HttpURLConnection con3 = (HttpURLConnection) url1.openConnection();
	                   con3.setRequestMethod("POST");

	                   con3.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

	                   String urlParameters = "id="+string+"&signature="+ssss+"&allow_access=1";

	                  con3.setDoOutput(true);
	                  DataOutputStream wr = new DataOutputStream(con3.getOutputStream());
	                  wr.writeBytes(urlParameters);
	                  wr.flush();
	                  wr.close();

	                  int responseCode1 = con3.getResponseCode();
	                  System.out.println("\nSending 'POST' request to URL : " + url);
	                  System.out.println("Post parameters : " + urlParameters);
	                  System.out.println("Response Code : " + responseCode1);


	                  con3.disconnect();

	                  driver.findElement(By.cssSelector(".page a")).click();
	                
	                
 // PART 5
	                driver.findElement(By.partialLinkText("Download File")).click();
				    Thread.sleep(5000);
				   
				      File file = new File("/home/nupursharma/Downloads/file_handle_test.dat");
					  
					FileInputStream fileInput=null;
					try {
						fileInput = new FileInputStream(file);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					Properties prop = new Properties();
				
					try {
						prop.load(fileInput);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					driver.findElement(By.id("signature")).sendKeys(prop.getProperty("Signature"));
					driver.findElement(By.className("submit")).click();
					System.out.println("property: "+prop.getProperty("Signature"));
					driver.close(); 

	}
}
            




