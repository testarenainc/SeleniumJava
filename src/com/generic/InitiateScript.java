package com.generic;
import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class InitiateScript {
    public static WebDriver driver;
    static String sMessage = null;
    public static Properties prop;
    static String sResultFolderPath = null;
    public static Logger log = Logger.getLogger(InitiateScript.class.getName()); 
	 
    public static void setUp()
    {
    	DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    	Date date = new Date();
//    	System.out.println(dateFormat.format(date));
    	String sDirName = "Result_" + dateFormat.format(date);
    	String sDirPath = "./Results/" + sDirName;
    	new File(sDirPath).mkdir();
    	sResultFolderPath = sDirPath;
    }
    
    
	 /* ===================================================================================
	  * Description: Function used to find the element
	  * Function findElement(oLocator);
	  * Where oLocator is targeted element
	  * Usage: findElement("css=input[name='email']");
	  * Create By: Saurabh Gupta
	  * Note: for CSS locator always start as 'css=<locator>'  
	 =======================================================================================*/
	 public static WebElement findElement(String oLocator) throws InterruptedException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	 {
		 WebElement oElement = null;
		 String oLocatorUpperCase;
		 String oLocatorValue = "";
//		 int iTime;
//		 boolean bElementExistence; 
		 // Changing case of locator given with in function
//		 oLocator = (String) ObjectRepository.class.getDeclaredField(oLocator).get(oLocator);
		 oLocatorUpperCase = oLocator.toUpperCase();
		 WebDriverWait wait = new WebDriverWait(driver, 120);
		 // Verifying any prefix is given 
		 if (oLocatorUpperCase.startsWith("XPATH=") || oLocatorUpperCase.startsWith("CSS=") 
				 || oLocatorUpperCase.startsWith("ID=") || oLocatorUpperCase.startsWith("NAME=") 
				 ||oLocatorUpperCase.startsWith("LINK=") || oLocatorUpperCase.startsWith("CLASSNAME="))
		 {
			 String[] oLocatorArray = oLocator.split("=");
			 if (oLocatorArray.length != 1 )
			 {
				 oLocatorValue = oLocatorArray[1];
				 for (int i = 1; i < oLocatorArray.length - 1; i++)
				 {
					 oLocatorValue = oLocatorValue + "=" + oLocatorArray[i+1];
				 }
				 oLocator = oLocatorValue;
			 }
			 else
			 {
				 oLocator = oLocatorArray[1];
			 }
		 }
		 
		 // Find Element
		 if (oLocatorUpperCase.contains("XPATH") || oLocatorUpperCase.startsWith("//"))
		 {
			oElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(oLocator)));
		 }
		 else if (oLocatorUpperCase.contains("CSS"))
		 {
			 oElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(oLocator)));
		 }
		 else if (oLocatorUpperCase.contains("LINK"))
		 {
			 oElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(oLocator)));
		 }
		 else if (oLocatorUpperCase.contains("ID")){
			 oElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(oLocator)));
		 }
		 else if (oLocatorUpperCase.startsWith("NAME")){
			 oElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.name(oLocator)));
		 }
		 else if (oLocatorUpperCase.contains("CLASSNAME")){
			 oElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.name(oLocator)));
		 }

		 return oElement;
	 }
	 
	 public static void afterMethod() {
	        if (driver != null) {
	            LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
	            for (int i =0; i >= logEntries.getAll().size(); i++ ){
	                System.out.println(logEntries.getAll().get(i).toString());
	            }
	        }
	    }
//	 
	 /* ===================================================================================
	  * Description: Function used to open given URL
	  * Function openBrowser (url)
	  * Where oLocator is targeted element and sText is value which need to be entered  
	  * Usage: openBrowser ("www.google.com")
	  * Create By: Saurabh Gupta
	  * Note:   
	 =======================================================================================*/
	 public static String openBrowser (String url) throws InterruptedException, IOException, HeadlessException, AWTException
	 {
		 loadConfigProperties();
		 String browserName = prop.getProperty("TestArena.Browser");
		 browserName = browserName.toString().toUpperCase();
		 
		 if (browserName.toString().equals("FIREFOX"))
		 {
			 DesiredCapabilities dc = DesiredCapabilities.firefox();
		     LoggingPreferences prefs = new LoggingPreferences();
		     prefs.enable(LogType.BROWSER, Level.ALL);
		     dc.setCapability(CapabilityType.LOGGING_PREFS, prefs);
		     System.setProperty("webdriver.gecko.driver", "geckodriver");
		     // /usr/local/Cellar/geckodriver/0.19.0/bin
			 driver = new FirefoxDriver();
		 }
		 if (browserName.equals("IE") || browserName.equals("INTERNET EXPLORER"))
		 {
			 DesiredCapabilities dc = DesiredCapabilities.internetExplorer();
		     LoggingPreferences prefs = new LoggingPreferences();
		     prefs.enable(LogType.BROWSER, Level.ALL);
		     dc.setCapability(CapabilityType.LOGGING_PREFS, prefs);
			 File file = new File("IEDriverServer.exe");
			 System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			  driver = new InternetExplorerDriver();
		 }
		 if (browserName.equals("GOOGLE CHROME") || browserName.equals("CHROME"))
		 {
			 DesiredCapabilities dc = DesiredCapabilities.chrome();
		     LoggingPreferences prefs = new LoggingPreferences();
		     prefs.enable(LogType.BROWSER, Level.ALL);
		     dc.setCapability(CapabilityType.LOGGING_PREFS, prefs);
			 System.setProperty("webdriver.chrome.driver", "chromedriver");
			 driver = new ChromeDriver();
		 }
		 if (browserName.toString().equals("SAFARI"))
		 {
//			 DesiredCapabilities dc = DesiredCapabilities.firefox();
//		     LoggingPreferences prefs = new LoggingPreferences();
//		     prefs.enable(LogType.BROWSER, Level.ALL);
//		     dc.setCapability(CapabilityType.LOGGING_PREFS, prefs);
			 driver = new SafariDriver();
//			 DesiredCapabilities capabilities = new DesiredCapabilities();
//			 capabilities.setBrowserName("safari");
//			 CommandExecutor executor = new SeleneseCommandExecutor(new URL("http://localhost:4444/"), new URL("http://www.google.com/"), capabilities);
//			 WebDriver driver = new RemoteWebDriver(executor, capabilities);
		 }
		 try
		 {
			 if (url.startsWith("http"))
			 {
				 driver.manage().window().maximize();
				 driver.navigate().to(url);
			 }
			 else
			 {
				 url = "http://" + url;
				 driver.get(url);
			 }
			 sMessage = "Open Browser with URL: - " + url ;
		 }
		 catch(Exception e)
		 {
			 sMessage = "Object not found and exception is: " + e.getMessage();
		 }
		 log.info(sMessage);
		 return sMessage;
	 }
	 
	 /* ===================================================================================
	  * Description: Function used to set text in input field
	  * Function inputText (String oLocator, String sText)
	  * Where oLocator is targeted element and sText is value which need to be entered  
	  * Usage: inputText ("//input[@name='Email']", "SampleValue")
	  * Create By: Saurabh Gupta
	  * Note: for CSS locator always start as 'css=<locator>'  
	 =======================================================================================*/
	 public static String inputText (String oLocator, String sText) throws InterruptedException, IOException, HeadlessException, AWTException
	 {
		 WebElement oElement;
		 try
		 {
			 oElement = findElement(oLocator);
			 oElement.clear();
			 oElement.sendKeys(sText);
			 sMessage = "Object '" + oLocator  + "' found and set value: - '" + sText + "'";
		 }
		 catch(Exception e)
		 {
			 sMessage = "Object '" + oLocator  + "' not found and exception is: " + e.getMessage();
		 }
		 takeScreenShot();
		 //handleAlert();
		 log.info(sMessage);
//		 afterMethod();
		 return sMessage;
	 }
	 
	 /* ===================================================================================
	  * Description: Function used to click button
	  * Function clickButton (oLocator)
	  * Where oLocator is targeted element   
	  * Usage: clickButton ("//input[@id='SignIn']"))
	  * Create By: Saurabh Gupta
	  * Note: for CSS locator always start as 'css=<locator>'  
	 =======================================================================================*/
	 public static String clickObject (String oLocator) throws InterruptedException, IOException, HeadlessException, AWTException
	 {
		 WebElement oElement;
		 try
		 {
			 oElement = findElement(oLocator);
			 oElement.click();
			 sMessage = "Object '" + oLocator  + "' found and clicked";
		 }
		 catch(Exception e)
		 {
			 sMessage = "Object '" + oLocator  + "' not found and exception is: " + e.getMessage();
		 }
		 takeScreenShot();
		 log.info(sMessage);
		 //handleAlert();
//		 afterMethod();
		 return sMessage;
	 }
	 
	 /* ===================================================================================
	  * Description: Function used to click button
	  * Function clickButton (oLocator)
	  * Where oLocator is targeted element   
	  * Usage: clickButton ("//input[@id='SignIn']"))
	  * Create By: Saurabh Gupta
	  * Note: for CSS locator always start as 'css=<locator>'  
	 =======================================================================================*/
	 public static String clickObjectWithOutAlertHandle (String oLocator) throws InterruptedException, IOException, HeadlessException, AWTException
	 {
		 WebElement oElement;
		 try
		 {
			 oElement = findElement(oLocator);
			 oElement.click();
			 sMessage = "Object '" + oLocator  + "' found and clicked";
		 }
		 catch(Exception e)
		 {
			 sMessage = "Object '" + oLocator  + "' not found and exception is: " + e.getMessage();
		 }
		 takeScreenShot();
//		 //handleAlert();
		 log.info(sMessage);
//		 afterMethod();
		 return sMessage;
	 }
	 
	 /* ===================================================================================
	  * Description: Function used to select checkbox
	  * Function selectCheckbox (oLocator)
	  * Where oLocator is targeted element   
	  * Usage: selectCheckbox ("//checkbox[@id='SignIn']"))
	  * Create By: Saurabh Gupta
	  * Note: for CSS locator always start as 'css=<locator>'  
	 =======================================================================================*/
	 public static String selectCheckbox (String oLocator) throws InterruptedException, IOException, HeadlessException, AWTException
	 {
		 WebElement oElement;
		 boolean bChecked;
		 try
		 {
			 oElement = findElement(oLocator);
			 bChecked = oElement.isSelected();
			 if (bChecked == false)
			 {
				 oElement.click();
			 }
			 sMessage = "Object '" + oLocator  + "'  found and selected";
		 }
		 catch(Exception e)
		 {
			 sMessage = "Object '" + oLocator  + "' not found and exception is: " + e.getMessage();
		 }
		 takeScreenShot();
		 //handleAlert();
		 log.info(sMessage);
		 return sMessage;
	 }

	 /* ===================================================================================
	  * Description: Function used to deselect checkbox
	  * Function deselectCheckbox (oLocator)
	  * Where oLocator is targeted element   
	  * Usage: deselectCheckbox ("//checkbox[@id='SignIn']"))
	  * Create By: Saurabh Gupta
	  * Note: for CSS locator always start as 'css=<locator>'  
	 =======================================================================================*/
	 public static String deselectCheckbox (String oLocator) throws InterruptedException, IOException, HeadlessException, AWTException
	 {
		 WebElement oElement;
		 boolean bChecked;
		 try
		 {
			 oElement = findElement(oLocator);
			 bChecked = oElement.isSelected();
			 if (bChecked == true)
			 {
				 oElement.click();
			 }
			 sMessage = "Object '" + oLocator  + "' found and deselected";
		 }
		 catch(Exception e)
		 {
			 sMessage = "Object '" + oLocator  + "' not found and exception is: " + e.getMessage();
		 }
		 //takeScreenShot();
		 //handleAlert();
		 log.info(sMessage);
		 return sMessage;
	 }
	 
	 /* ===================================================================================
	  * Description: Function used to verify that object is displayed or not
	  * Function verifyObjectIsDisplayed (oLocator)
	  * Where oLocator is targeted element   
	  * Usage: verifyObjectIsDisplayed ("//checkbox[@id='SignIn']"))
	  * Create By: Saurabh Gupta
	  * Note: for CSS locator always start as 'css=<locator>'  
	 =======================================================================================*/
	 public static String verifyObjectIsDisplayed (String oLocator) throws IOException, HeadlessException, AWTException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	 {
		 WebElement oElement;
		 boolean bElementExistence;
		 int iTime;
		 try
		 {
			 oElement = findElement(oLocator);
			 for(iTime = 1; iTime <= 30; iTime++)
			 {
				 bElementExistence = oElement.isDisplayed();
				 if (bElementExistence == false)
				 {
					Thread.sleep(1);
					log.error("Fail, Object '" + oLocator + "' is displayed false"); 
					sMessage = "Fail";
					takeScreenShot();
				 }
				 else
				 {
					 log.info("Pass, Object '" + oLocator + "' is displayed true");
					 sMessage = "Pass";
					 break;
				 }
			 }
		 }
		 catch(InterruptedException e)
		 {
			 log.error("Fail, Object not found and exception is: " + e.getMessage());
			 sMessage = "Fail";
		 }
//		 System.out.println(sMessage);
		 //takeScreenShot();
//		 log.info(sMessage);
		 return sMessage;
	 }
	 
	 /* ===================================================================================
	  * Description: Function used to verify that object is displayed or not
	  * Function verifyObjectIsDisplayedNegative (oLocator)
	  * Where oLocator is targeted element   
	  * Usage: verifyObjectIsDisplayedNegative ("//checkbox[@id='SignIn']"))
	  * Create By: Saurabh Gupta
	  * Note: for CSS locator always start as 'css=<locator>'  
	 =======================================================================================*/
	 public static void verifyObjectIsDisplayedNegative (String oLocator) throws IOException, HeadlessException, AWTException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	 {
		 WebElement oElement;
		 boolean bElementExistence;
		 int iTime;
		 try
		 {
			 oElement = findElement(oLocator);
			 for(iTime = 1; iTime <= 30; iTime++)
			 {
				 bElementExistence = oElement.isDisplayed();
				 if (bElementExistence == false)
				 {
					Thread.sleep(1);
					log.info("Pass, Object '" + oLocator + "' is displayed false"); 
				 }
				 else
				 {
					 log.error("Fail, Object '" + oLocator + "' is displayed true");
					 takeScreenShot();
					 break;
				 }
			 }
		 }
		 catch(InterruptedException e)
		 {
			 log.error("Fail, Object not found and exception is: " + e.getMessage());
		 }
//		 System.out.println(sMessage);
		 //takeScreenShot();
//		 log.info(sMessage);
//		 return sMessage;
	 }
	 
	 /* ===================================================================================
	  * Description: Function used to load config properties
	  * Function loadConfigProperties ()  
	  * Usage: loadConfigProperties 
	  * Create By: Saurabh Gupta  
	 =======================================================================================*/
	 public static void loadConfigProperties() throws IOException
	 {
		 prop = new Properties();

			try {
				// load a properties file
				prop.load(new FileInputStream("./Configuration/config.properties"));
				System.out.println("...Loaded Configuration property file...");
			} catch (FileNotFoundException e) {
				System.out
						.println("...Failed to load Configuration property file...");
				e.printStackTrace();
			}
	 }
	 
	 /* ===================================================================================
	  * Description: Function used to handle alert.
	  * Function handleAlert ()  
	  * Usage: handleAlert() 
	  * Create By: Saurabh Gupta  
	  * Note: If any alert appear on page then it will accept the alert
	 =======================================================================================*/
	 public static void handleAlert() throws InterruptedException, IOException, HeadlessException, AWTException
	 { 
		 int i=0;
		   while(i++<1)
		   {
		        try
		        {
		            Alert alert = driver.switchTo().alert();
		            Thread.sleep(1000);
		            takeScreenShot();
		            log.warn(alert.getText());
				    alert.accept();
		            break;
		        }
		        catch(NoAlertPresentException e)
		        {
//		        	System.out.println("No Alert Present");
		          Thread.sleep(1000);
		          continue;
		        }
		   }
	 }
	 
	 /* ===================================================================================
	  * Description: Function used to screenshot of the page.
	  * Function takeScreenShot ()  
	  * Usage: takeScreenShot() 
	  * Create By: Saurabh Gupta  
	 =======================================================================================*/
	 public static void takeScreenShot() throws IOException, HeadlessException, AWTException
	 {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	    Date date = new Date();
//	    System.out.println(dateFormat.format(date));
	    String sImageName = "SnapShot_" + dateFormat.format(date);
	    String sImagePath = sResultFolderPath + "/" + sImageName + ".jpg";
//		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
//		// Now you can do whatever you need to do with it, for example copy somewhere
//		FileUtils.copyFile(scrFile, new File(sImagePath));
	    BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	    ImageIO.write(image, "jpg", new File(sImagePath));
	 }
	 
	 /* ===================================================================================
	  * Description: Function used to select a value from dropdown on the basic of value of options
	  * Function selectDropdownByValue(oLocator, sValue)
	  * Where oLocator is targeted element 
	  * sValue is the value of option need to select  
	  * Usage: selectDropdownByValue ("//Select[@id='Designation']", "CEO"))
	  * Create By: Saurabh Gupta
	  * Note: for CSS locator always start as 'css=<locator>'  
	 =======================================================================================*/
	 public static String selectDropdownByValue(String oLocator, String sValue) throws HeadlessException, InterruptedException, IOException, AWTException
	 {
		 WebElement oElement;
		 try
		 {
			 oElement = findElement(oLocator);
			 Select dropdown = new Select(oElement);
			 dropdown.selectByValue(sValue);
			 sMessage = "Object '" + oLocator  + "' found and selected the value :- " + sValue;
		 }
		 catch(Exception e)
		 {
			 sMessage = "Object '" + oLocator  + "' not found and exception is: " + e.getMessage();
		 }
		 //takeScreenShot();
		 log.info(sMessage);
		 //handleAlert();
		 //afterMethod();
		 return sMessage;
	 }
	 
	 
	 
	 /* ===================================================================================
	  * Description: Function used to select a value from dropdown on the basic of index
	  * Function selectDropdownByIndex(oLocator, index)
	  * Where oLocator is targeted element 
	  * sValue is the value of option need to select  
	  * Usage: selectDropdownByIndex ("//Select[@id='Designation']", 1))
	  * Create By: Saurabh Gupta
	  * Note: for CSS locator always start as 'css=<locator>'  
	 =======================================================================================*/
	 public static String selectDropdownByIndex(String oLocator, int index) throws HeadlessException, InterruptedException, IOException, AWTException
	 {
		 WebElement oElement;
		 try
		 {
			 oElement = findElement(oLocator);
			 Select dropdown = new Select(oElement);
			 dropdown.selectByIndex(index);
			 sMessage = "Object '" + oLocator  + "' found and selected with index :- " + index;
		 }
		 catch(Exception e)
		 {
			 sMessage = "Object '" + oLocator  + "' not found and exception is: " + e.getMessage();
		 }
		 //takeScreenShot();
		 log.info(sMessage);
		 //handleAlert();
		 //afterMethod();
		 return sMessage;
	 }
	 
	 /* ===================================================================================
	  * Description: Function used to select a value from dropdown on the basic of visible text options
	  * Function selectDropdownByVisibleText(oLocator, sValue)
	  * Where oLocator is targeted element 
	  * sValue is the value of option need to select  
	  * Usage: selectDropdownByVisibleText ("//Select[@id='Designation']", "CEO"))
	  * Create By: Saurabh Gupta
	  * Note: for CSS locator always start as 'css=<locator>'  
	 =======================================================================================*/
	 public static String selectDropdownByVisibleText(String oLocator, String sValue) throws HeadlessException, InterruptedException, IOException, AWTException
	 {
		 WebElement oElement;
		 try
		 {
			 oElement = InitiateScript.findElement(oLocator);
			 Select dropdown = new Select(oElement);
			 dropdown.selectByVisibleText(sValue);
			 InitiateScript.log.info("Object found and selected the visible text :- " + sValue);
		 }
		 catch(Exception e)
		 {
			 InitiateScript.log.error("Object not found and exception is: " + e.getMessage());
		 }
		 //takeScreenShot();
		 //afterMethod();
		 //handleAlert();
		 return InitiateScript.sMessage;
	 }
	 
	 /* ===================================================================================
	  * Description: Function used to get the attribute value
	  * Function getObjectAttributeValue(oLocator, sAttributeName)
	  * Where oLocator is targeted element 
	  * sValue is the value of option need to select  
	  * Usage: getObjectAttributeValue ("//Select[@id='Designation']", "value"))
	  * Create By: Saurabh Gupta
	  * Note: for CSS locator always start as 'css=<locator>'  
	 =======================================================================================*/
	 public static String getObjectAttributeValue(String oLocator, String sAttribute) throws HeadlessException, InterruptedException, IOException, AWTException
	 {
		 WebElement oElement;
		 String sAttributeValue = null;
		 try
		 {
			 oElement = findElement(oLocator);
			 sAttributeValue = oElement.getAttribute(sAttribute);
			 sMessage = "Object '" + oLocator  + "' found andand its attribute value is :- " + sAttributeValue;
		 }
		 catch(Exception e)
		 {
			 sMessage = "Object '" + oLocator  + "' not found and exception is: " + e.getMessage();
		 }
		 //takeScreenShot();
		// handleAlert();	
		 log.info(sMessage);
		 return sAttributeValue;
	 }
	 
	 /*=================================================================================
		* DESCRIPTION: To compare the given two values along with a message, this is an overloaded function
		* AUTHOR: Saurabh Gupta
		* USAGE: compareValue(sText1,  sValue1, sText2, sValue2, bIsNegative)
		  Where sValue1 is the first value, sValue2 is the second value which compare with the first
		* EXAMPLE:compareValue("Expected Value of object", "0034748494546", "Actual value of Object", "0034748494546")
		* NOTES: 
		==================================================================================*/
		public static void compareValue(String sText1,  String sValue1, String sText2, String sValue2, boolean bIsNegative)
		{
			String sStatus;							// * Variable use to store the function status
			String sStatusMessage;					// * Variable use to store the function status Message
		
			sValue1 = sValue1.toString().trim();
			sValue2 = sValue2.toString().trim();
			try
			{
				if (bIsNegative == true)
				{
					if (sValue1.equals(sValue2))
					{
						sStatus = "Pass";
						sStatusMessage = sText1 +  sValue1 + " matched with the " + sText2  + sValue2;
						log.info(sStatus + " ; " + sStatusMessage); 
					}
					else
					{
						sStatus = "Fail";
						sStatusMessage = sText1 +  sValue1 + " not matched with the " + sText2 + sValue2;
						log.error(sStatus + " ; " + sStatusMessage); 
					}
				}
				else
				{
					if (sValue1.equals(sValue2))
					{
						sStatus = "Fail";
						sStatusMessage = sText1 +  sValue1 + " matched with the " + sText2 + sValue2;
						log.error(sStatus + " ; " + sStatusMessage); 
					}
					else
					{
						sStatus = "Pass";
						sStatusMessage = sText1 +  sValue1 + " not matched with the " + sText2 + sValue2;
						log.info(sStatus + " ; " + sStatusMessage); 
					}
				}
			}
			catch (Exception e)
			{
				sStatus = "Fail";
				sStatusMessage = e.getMessage();
				log.error(sStatus + " ; " + sStatusMessage); 
			}
		}
		
		/*=================================================================================
		* DESCRIPTION: To get the value from defined excel sheet and column
		* AUTHOR: Saurabh Gupta
		* DATE: 
		* USAGE: getValuefromExcelFile(sSheetPath,sSheetName, sColumnName)
		* where sSheetPath is a workbook location, sSheetName is a sheet name under the workbook,
		  sColumnName is a column name in first row.
		* EXAMPLE: getValuefromExcelFile("C:\\Factory\\PSS_Automation\SuiteFile.xls","Sheet1", Address1)
		* NOTES: Look for the excel sheet given name in a column
		==================================================================================*/
		public static String getValuefromExcelFile(String sSheetPath, String sSheetName, String sColumnName) throws BiffException, IOException
		{
			int iActualColNumber;		// * Variable use to storefinal Column number as per the condition provide in function
			String sColumnValue = null	;		// * Variable use to store the column value
			
			Workbook oResultwb = Workbook.getWorkbook(new File(sSheetPath.toString()));
			Sheet oSheet = oResultwb.getSheet(sSheetName);
			
			try
			{
				// ** Creating Excel object
//				int iRowCount = oSheet.getRows();	//Store the Row count in excel sheet
				int iColCount = oSheet.getColumns();
				//Integer iColumnCount = oSheet.getColumns()
			
				int col;
				for (col = 0; col < iColCount; col++)
				{
					Cell testCaseCell = oSheet.getCell(col, 0);
					String sTestCase = testCaseCell.getContents();
					if (sTestCase.toUpperCase().equals(sColumnName.toUpperCase()) )
					{
						iActualColNumber = col;
						Cell sColumnValueCell = oSheet.getCell(iActualColNumber, 1);
						sColumnValue = sColumnValueCell.getContents();
						break;
					}
					else
					{
						sColumnValue = "No Column found in the sheet:- '" + sSheetPath +"' with the name :- '" + sColumnName + "'.";
					}
				}	
			}
			catch (Exception e)
			{
				sColumnValue = e.getMessage();
			}
			oResultwb.close();
			return sColumnValue;
		}
		
		
		 /* ===================================================================================
		  * Description: Function used to verify the object text
		  * Function verifyObjectText(oLocator, sExpectedText)
		  * Where oLocator is targeted element 
		  * sExpectedText is the expected value which need to be verify
		  * Usage: verifyObjectText ("//Select[@id='Designation']", "value"))
		  * Create By: Saurabh Gupta
		  * Note: for CSS locator always start as 'css=<locator>'  
		 =======================================================================================*/
		public static String verifyObjectText(String oLocator, String sExpectedText)
		{
			WebElement oElement;
			String sActualValue = null;
			try
			{
				oElement = findElement(oLocator);
				sActualValue = oElement.getText();
				if (sActualValue.contains(sExpectedText))
				{
					sMessage = "Pass, Object '" + oLocator  + "' found and its Actual value :- '" + sActualValue + "' contain the expected text :- '" + sExpectedText + "'.";
				}
				else
				{
					sMessage = "Fail, Object '" + oLocator  + "' found and its Actual value :- '" + sActualValue + "' does not contain the expected text :- '" + sExpectedText + "'.";
				}
			}
			catch(Exception e)
			{
				sMessage = "Object '" + oLocator  + "' not found and exception is: " + e.getMessage();
			}
			
			log.info(sMessage);
			return sMessage;
			
		}
		
		
		/* ===================================================================================
		  * Description: Function used to call JS command
		  * Function callJavaScript(jsCommand)
		  * Where oLocator is targeted element 
		  * Usage: callJavaScript (""))
		  * Create By: Saurabh Gupta 
		 =======================================================================================*/
		public static void callJavaScript(String jsCommand)
		{
			JavascriptExecutor js = null;
			if (driver instanceof JavascriptExecutor) {
			    js = (JavascriptExecutor)driver;
			}
			js.executeScript(jsCommand);
			
		}
}
