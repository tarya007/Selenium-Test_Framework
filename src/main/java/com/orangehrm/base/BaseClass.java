package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {
	protected static Properties prop;
//	protected static WebDriver driver;
//	private static ActionDriver actionDriver;
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	
	
	
	@BeforeSuite
	public void loadConfig() throws IOException
	{
		//load the configuration file
		prop = new Properties();
		FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
		prop.load(fis);
		logger.info("config properties file loaded");
	}
	
	@BeforeMethod
	public synchronized void setup()
	{
		System.out.println("Setting up Webdriver: " + this.getClass().getSimpleName());
		lauchBrowser();
		configureBrowser();
		staticWait(2);
		
		logger.info("Webdriver Initialized and Browser Maximized");
		logger.trace("Trace message");
		logger.error("Error message");
		logger.debug("debug message");
		logger.fatal("This is fatal response");
		logger.warn("This is warn message");
		
		//Initialize the actiondriver only once
//		if(actionDriver == null)
//		{
//			actionDriver = new ActionDriver(driver);
//			logger.info("ActionDriver instance is created.");
//			logger.info("ActionDriver instance is created. " + Thread.currentThread().getId());
//		}
		
		//Initialize the actiondriver for the current Thread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initiated for thread: " + Thread.currentThread().getId());
	}
	

	//Initialize the webdriver based on browser defined in config.properties file
	private synchronized void lauchBrowser()
	{
				String browser = prop.getProperty("browser");
				
				if(browser.equalsIgnoreCase("chrome"))
				{
//					driver = new ChromeDriver();
					driver.set(new ChromeDriver());
					ExtentManager.registerDriver(getDriver());
					logger.info("ChromeDriver Instance is created!");
				}
				else if (browser.equalsIgnoreCase("edge"))
				{
//					driver = new EdgeDriver();
					driver.set(new EdgeDriver());
					ExtentManager.registerDriver(getDriver());
					logger.info("EdgeDriver Instance is created!");
				}
				else if (browser.equalsIgnoreCase("firefox"))
				{
//					driver = new FirefoxDriver();
					driver.set(new FirefoxDriver());
					ExtentManager.registerDriver(getDriver());
					logger.info("FirefoxDriver Instance is created!");
				}
				else
				{
					throw new IllegalArgumentException("Browser not supported" + browser);
				}
	}
	//Configure browser setting as such as implicit wait, maximize browser the
	//browser and navigate to URl
	private void configureBrowser()
	{
		int implicitWait = 	Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		
		//maximize the driver
		getDriver().manage().window().maximize();
		
		//Navigate to URL
		try
		{
			getDriver().get(prop.getProperty("url"));
			logger.info("Successfully navigated to URL");
		}
		catch(Exception e)
		{
			logger.error("Failed to Navigate to the URL: " + e.getMessage());
			throw e;
		}
	}

	
	
	@AfterMethod
	public void tearDown()
	{
		if (getDriver()!=null)
		{
			try
			{
				getDriver().quit();
			}
			catch (Exception e)
			{
				System.out.println("unable to quit the driver: " + e.getMessage());
			}
		}
		logger.info("WebDriver instance is closed.");
		driver.remove();
		actionDriver.remove();
//		driver = null;
//		actionDriver = null;
//		ExtentManager.endTest();
	}
	
	//Driver getter method
	public static WebDriver getDriver()
	{
	    if (driver.get() == null)
	    {
	        throw new IllegalStateException("WebDriver is not initialized");
	    }

	    return driver.get();
	}
	
	public static ActionDriver getActionDriver()
	{
		if (actionDriver.get() == null)
		{
			System.out.println("Actiondriver is not initialized");
			throw new IllegalStateException("Actiondriver is not initialized");
		}
		return actionDriver.get();
	}
	
	
	//getter method for prop
	public static Properties getProp()
	{
		return prop;
	}
	
	
	//static wait for pause
	public void staticWait(int seconds)
	{
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}
	

}
