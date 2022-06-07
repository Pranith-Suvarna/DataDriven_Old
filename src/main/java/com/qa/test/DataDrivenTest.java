package com.qa.test;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.utilities.TestUtility;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DataDrivenTest {
	
	protected static ThreadLocal <WebDriver> driver = new ThreadLocal<WebDriver>();
    
	public static WebDriver getDriver() {
		  return driver.get();
	  }
	  
	  public static void setDriver(WebDriver driver2) {
		  driver.set(driver2);
	  }
	/*
	SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
	Date date = new Date();
	String DOB = sdf1.format(date);
	*/
		
	public static String getCurrentDateTime()
	{
		DateFormat customFormat = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");
		Date currentDate = new Date();
		
		return customFormat.format(currentDate);
	}
	
	public static String captureScreenshot(WebDriver driver)
	{
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String screenshotPath = System.getProperty("user.dir")+"/Screenshots/"+ getCurrentDateTime() + ".png";
		try {
			FileHandler.copy(src, new File(screenshotPath ));
		} catch (IOException e) {
			System.out.println("Unable to capture screenshot"+e.getMessage());
		}
		
		return screenshotPath;
	}
	
	public static void selectDropdownOption(WebElement element,String valueToBeSelected)
	{
		Select selectmonth = new Select(element);
        selectmonth.selectByVisibleText(valueToBeSelected);
	}
	

	public static void selectdate(String dayvalue)
	{
	 By dateattribute = By.xpath("//*[contains(@class,'react-datepicker__day')]");
	 List<WebElement> dates= getDriver().findElements(dateattribute);
 
	//Grab common attribute//Put into list and iterate
	int count = dates.size();

	for(int i=0;i<count;i++)
	{
	 String text=dates.get(i).getText();
	 if(text.equalsIgnoreCase(dayvalue))
	 {
		 dates.get(i).click();
	break;
	 }
	}
	}
	
	public static void selectRadioButton(List<WebElement> element,String radiobuttonvalue) 
	{
		for (WebElement button : element) {
		if(button.getText().equalsIgnoreCase(radiobuttonvalue))	
		{
			button.click();
			break;
		}			
	  }
	}
	
	
	@BeforeMethod
	public void setUp() {
		
	  //System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
		
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		setDriver(driver);
     // driverThreadSafe.set(new ChromeDriver());
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(30,TimeUnit.SECONDS);
    //  driver = driverThreadSafe.get();
		driver.get("https://demoqa.com/automation-practice-form");
		
	}
	
	@DataProvider(parallel=true)
	public Iterator<Object[]> getTestData() {
		
		ArrayList<Object[]> testData = TestUtility.getDataFromExcel();	
		return testData.iterator();
	}
	
	@Test(dataProvider="getTestData")
	public void formTest(String firstname , String lastname , String email , String number , String address, String dob , String hobbies) throws InterruptedException {
		
		String dobvalue = dob;
		String dateArr[] = dobvalue.split(" ");
		String day = dateArr[0];
		String month = dateArr[1];
		String year = dateArr[2];
		
		getDriver().findElement(By.id("firstName")).sendKeys(firstname);
		
		getDriver().findElement(By.id("lastName")).sendKeys(lastname);
		
		getDriver().findElement(By.id("userEmail")).sendKeys(email);
		
		getDriver().findElement(By.xpath("//*[text()='Male']")).click();
		
		((JavascriptExecutor)driver).executeScript("scroll(0,400)");
		
		getDriver().findElement(By.id("userNumber")).sendKeys(number);
		
		getDriver().findElement(By.id("dateOfBirthInput")).click();
        
        selectDropdownOption(getDriver().findElement(By.className("react-datepicker__month-select")),month);       
        selectDropdownOption(getDriver().findElement(By.className("react-datepicker__year-select")),year);		
        selectdate(dobvalue);
				        
	/*	
	    WebElement ele = driver.findElement(By.xpath("(//*[contains(@id,'hobbies-checkbox')])[1]"));
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("arguments[0].click()", ele);
	*/	
        selectRadioButton(getDriver().findElements(By.xpath("//*[contains(@id,'hobbies-checkbox')]")),hobbies);

        getDriver().findElement(By.id("currentAddress")).sendKeys(address);
		
	/*	driver.findElement(By.id("state")).click();
		driver.findElement(By.xpath("//*[text()='NCR']")).click();

		
		driver.findElement(By.id("city")).click();
		driver.findElement(By.xpath("//*[text()='Delhi']")).click();
		
		
		driver.findElement(By.id("submit")).click();
    */
		Thread.sleep(10000);
		
		DataDrivenTest.captureScreenshot(getDriver());

	}
	
	@AfterMethod
	public void tearDown(ITestResult result) {

		if(result.getStatus()==ITestResult.FAILURE) 
		{
			DataDrivenTest.captureScreenshot(getDriver());
		}
		
	  //driverThreadSafe.get().quit();
	    getDriver().quit();
		
	}
	
}
