package com.highradius;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class LocatorAssignment {

		@SuppressWarnings({ "unused", "unlikely-arg-type" })
		public static void main(String[] args) throws InterruptedException, SQLException {

				Connection dbConnection = null;
				PreparedStatement preparedStatement = null;
				String url = "jdbc:mysql://localhost:3306/";
				String schema = "selenium_assignment_1";
				String user = "root";
				String pass = "root";
				ResultSet resultSet = null;
				String query = "INSERT INTO Selenium_Vegetable (vegetable_name, price, weight) VALUES (?,?,?)";
				try {
						Class.forName("com.mysql.cj.jdbc.Driver");
						dbConnection = DriverManager.getConnection(url + schema, user, pass);
						ArrayList<PojoLocator> pojoArrayList = new ArrayList<>();
						preparedStatement = dbConnection.prepareStatement(query);
						if (dbConnection != null)
								System.out.println("Connection Successfully Done!");
						else
								System.out.println("Connection Unsuccessful!!!");
						System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
						WebDriver webDriver = new FirefoxDriver();

						webDriver.manage().window().maximize();
						webDriver.manage().deleteAllCookies();

						// dynamic wait
						webDriver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
						webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
						
						webDriver.get("https://rahulshettyacademy.com/#/practice-project");
						
						webDriver.findElement(By.xpath("//input[contains(@id, 'name')]")).sendKeys("Padmanabha Das");
						webDriver.findElement(By.xpath("//input[contains(@type, 'email')]"))
						                .sendKeys("padmanabha.das@highradius.com");

						Thread.sleep(3000);

						webDriver.findElement(By.xpath("//input[contains(@id, 'agreeTerms')]")).click();

						Thread.sleep(3000);

						webDriver.findElement(By.xpath("//button[contains(text(), 'Submit')]")).click();

						Thread.sleep(3000);

						webDriver.findElement(By.xpath("//a[contains(text(), 'Automation Practise - 1')]")).click();

						Thread.sleep(3000);

						JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webDriver;
						javascriptExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight)");

						List<WebElement> linklist = webDriver.findElements(By.xpath("//h4[@class='product-name']"));
						List<WebElement> linklist_1 = webDriver.findElements(By.xpath("//p[@class='product-price']"));
						String arr[] = null;
						for (int i = 0; i < linklist.size(); i++) {
								PojoLocator pojoLocator = new PojoLocator();
								String linktext = linklist.get(i).getText();
								if (linktext.contains("-"))
										arr = linktext.split("-", 2);
								else {
										if (!linktext.contains("Kg")) {
												arr[0] = linktext;
												arr[1] = null;
										}
								}
								String s = linklist_1.get(i).getText();
								pojoLocator.setVegetableName(arr[0]);
								pojoLocator.setWeight(arr[1]);
								pojoLocator.setPrice(s);
								pojoArrayList.add(pojoLocator);
						}
						for (PojoLocator i : pojoArrayList) {
								preparedStatement.setString(1, i.getVegetableName());
								preparedStatement.setString(2, i.getPrice());
								preparedStatement.setString(3, i.getWeight());
								preparedStatement.execute();
						}

						webDriver.findElement(By.xpath("//input[contains(@type, 'search')]")).sendKeys("Brocolli");
						webDriver.findElement(By.xpath("//a[contains(@class,'increment')]")).click();
						Thread.sleep(1000);
						webDriver.findElement(By.xpath("//button[contains(text(), 'ADD TO CART')]")).click();

						Thread.sleep(3000);

						webDriver.findElement(By.xpath(
						                "//img[contains(@src, 'https://res.cloudinary.com/sivadass/image/upload/v1493548928/icons/bag.png')]"))
						                .click();

						String Name = webDriver.findElement(By.xpath("//h4[@class='product-name']")).getText();
						int price = Integer.parseInt(webDriver
						                .findElement(By.xpath("//div[@class='product']//p[@class='product-price']"))
						                .getText());
						String quantity = webDriver.findElement(By.xpath("//input[@class='quantity']"))
						                .getAttribute("value");
						int quant = Integer.parseInt(quantity);
						String cartName = webDriver
						                .findElement(By.xpath("//div[@class='product-info']//p[@class='product-name']"))
						                .getText();
						String cartQuantity = webDriver.findElement(By.xpath("//p[@class='quantity']")).getText()
						                .replaceAll("[^\\d]", "");
						int cartAmount = Integer
						                .parseInt(webDriver.findElement(By.xpath("//p[@class='amount']")).getText());
						int total = price * quant;

						if (Name.equals(cartName) && total == cartAmount && quantity.equals(cartQuantity))
								System.out.println("Name, Weight & Price Validated!");
						else
								System.out.println("Not Getting Validated!! Try again!");

						webDriver.findElement(By.xpath("//button[contains(text(), 'PROCEED TO CHECKOUT')]")).click();

						Thread.sleep(2000);

						webDriver.findElement(By.xpath("//button[contains(text(), 'Apply')]")).click();

						Thread.sleep(1000);

						webDriver.findElement(By.xpath("//button[contains(text(), 'Place Order')]")).click();

						Thread.sleep(1500);

						Select selectCountry = new Select(
						                webDriver.findElement(By.xpath("//select[starts-with(@style, 'width:')]")));
						selectCountry.selectByValue("India");

						webDriver.findElement(By.xpath("//input[contains(@class, 'chkAgree')]")).click();

						Thread.sleep(1000);

						webDriver.findElement(By.xpath("//button[contains(text(), 'Proceed')]")).click();

						WebElement webElementConfirmation = webDriver
						                .findElement(By.xpath("//span[starts-with(text(),'Thank you')]"));
						if (webElementConfirmation != null)
								System.out.println("Order completed");
						else
								System.out.println("Order not successful, Please try again");

						dbConnection.close();
						preparedStatement.close();
				} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
		}
}
