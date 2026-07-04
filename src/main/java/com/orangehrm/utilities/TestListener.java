package com.orangehrm.utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.orangehrm.base.BaseClass;

public class TestListener implements ITestListener, IAnnotationTransformer {

	
	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}

	@Override
	public void onTestStart(ITestResult result) {
		System.out.println("Listener Executed");
		String testName = result.getMethod().getMethodName();
		//start logging in extent reports
		ExtentManager.startTest(testName);
		ExtentManager.logStep("Test started: " + testName);
		
	}

	@Override
	public void onTestSuccess(ITestResult result) {

	    String testName = result.getMethod().getMethodName();

	    if(result.getTestClass().getName().toLowerCase().contains("api")) {

	        // API Test
	        ExtentManager.logStepValidationForAPI(
	                "Test End: " + testName + " - Test Passed");

	    } else {

	        // UI Test
	        ExtentManager.logStepWithScreenshot(
	                BaseClass.getDriver(),
	                "Test Passed Successfully!",
	                "Test End: " + testName + " - Test Passed");
	    }
	}

	@Override
	public void onTestFailure(ITestResult result) {

	    String testName = result.getMethod().getMethodName();

	    String failureMessage = result.getThrowable().getMessage();
	    ExtentManager.logStep(failureMessage);

	    if(result.getTestClass().getName().toLowerCase().contains("api")) {

	        // API Test
	        ExtentManager.logFailureAPI(
	                "Test End: " + testName + " - Test Failed");

	    } else {

	        // UI Test
	        ExtentManager.logFailure(
	                BaseClass.getDriver(),
	                "Test Failed!",
	                "Test End: " + testName + " - Test Failed");
	    }
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logStep("Test Skipped" + testName);
		
	}

	//Triggered when a suite starts
	@Override
	public void onStart(ITestContext context) {
		//Initilize the Extent reports
		ExtentManager.getReporter();
		
	}

	//Triggered when suite ends
	@Override
	public void onFinish(ITestContext context) {
		ExtentManager.endTest();
	}
	

}
