package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private static Map<Long, WebDriver> driverMap = new HashMap<>();

	// Initialize the Extent Report
	public synchronized static ExtentReports getReporter() {

	    if (extent == null) {

	        String reportPath = System.getProperty("user.dir")
	                + "/test-output/ExtentReport.html";

	        System.out.println("Creating report at:");
	        System.out.println(reportPath);

	        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);

	        spark.config().setReportName("Automation Test Report");
	        spark.config().setDocumentTitle("OrangeHRM Report");
	        spark.config().setTheme(Theme.DARK);

	        extent = new ExtentReports();
	        extent.attachReporter(spark);

	        extent.setSystemInfo("OS", System.getProperty("os.name"));
	    }

	    return extent;
	}

	// Start the Test
	public synchronized static ExtentTest startTest(String testName) {
		System.out.println("Starting test: " + testName);
		ExtentTest extentTest = getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;
	}
	
	//End a Test
	public synchronized static void endTest() {
		getReporter().flush();
	}
	
	//Get current Thread's test
	public synchronized static ExtentTest getTest()
	{
		return test.get();
	}
	
	//Method to get the name of the current Test
	public static String getTestName()
	{
		ExtentTest currentTest = getTest();
		if(currentTest != null)
		{
			return currentTest.getModel().getName();
		}
		else
		{
			return "No test is currently active for this thread";
		}
	}
	
	//Log a step
	public static void logStep(String logMessage)
	{
		getTest().info(logMessage);
	}
	
	//Log a step validation with screenshot
	public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenShotMessage)
	{
		getTest().pass(logMessage);
		//Screenshot method
		attachScreenshot(driver, screenShotMessage);
	}
	
	//Log a step validation for API
	public static void logStepValidationForAPI(String logMessage)
	{
		getTest().pass(logMessage);
	}
	
	//Log a failure
	public static void logFailure(WebDriver driver, String logMessage, String screenShotMessage)
	{
		String colorMessage = String.format(
	            "<span style='color:red;'>%s</span>",
	            logMessage);
		getTest().fail(logMessage);
		attachScreenshot(driver, screenShotMessage);
	}
	
	// Log a Failure for API
	public static void logFailureAPI(String logMessage) {

	    String colorMessage = String.format(
	            "<span style='color:red;'>%s</span>",
	            logMessage);

	    getTest().fail(colorMessage);
	}
	
	//Log a skip
	public static void logSkip(String logMessage)
	{
		getTest().skip(logMessage);
	}
	
	//Take the screenshot with date and time
	public synchronized static String takeScreenshot(WebDriver driver, String screenShotName) {
		TakesScreenshot ts = (TakesScreenshot)driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		//Format date and time for file name
		String timeStamp= new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		
		//Saving the screenshot to a file
		String destPath = System.getProperty("user.dir")
		        + "/src/test/resources/screenshots/"
		        + screenShotName + "_"
		        + timeStamp
		        + ".png";
		File finalPath = new File(destPath);
		try {
			FileUtils.copyFile(src, finalPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Convert screenshot to Base64 fir embedding in th report
		String base64Format = convertToBase64(src);
		return base64Format;
		
	}
	
	//convert screenshot to Base64
	public static String convertToBase64(File screenshot)
	{
		String base64Format = "";
		//Read the file content into a byte array
		try {
			byte[] fileContent = FileUtils.readFileToByteArray(screenshot);
			base64Format = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return base64Format;
		
	}
	
	//Attach screenshot to report using base64
	public synchronized static void attachScreenshot(WebDriver driver, String message)
	{
		try {
			String screenShotBase64 = takeScreenshot(driver, getTestName());
			getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotBase64).build());
		} catch (Exception e) {
			getTest().fail("Failed to attach screenshot: " + message);
			e.printStackTrace();
		}
	}

	// Register WebDriver for current Thread
	public static void registerDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().getId(), driver);
	}
}
