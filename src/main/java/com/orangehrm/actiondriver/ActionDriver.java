package com.orangehrm.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class ActionDriver {
	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = LoggerManager.getLogger(ActionDriver.class);

	public ActionDriver(WebDriver driver) {
		super();
		this.driver = driver;
		int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		logger.info("WebDriver instance is created.");
	}

	// wait for element to be clickable
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));

		} catch (Exception e) {
			logger.error("Element is not clickable: " + e.getMessage());

		}
	}

	// Wait for element to be visible
	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Element is not visible: " + e.getMessage());
		}
	}

	// method to click the element
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		try {
			applyBorder(by, "green");
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			ExtentManager.logStep("Click an element: " + elementDescription);
			logger.info("Click an element --> " + elementDescription);
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("Unable to click the element: " + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to click the element: ", elementDescription);
			logger.error("unable to click an element");
		}
	}

	// method to enter the text into the input field
	public void enterText(By by, String text) {
		try {
			applyBorder(by, "green");
			waitForElementToBeVisible(by);
			WebElement element = driver.findElement(by);

			element.clear();
			element.sendKeys(text);
			logger.info("Text entered: " + getElementDescription(by) + text);
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("unable to enter the text: " + e.getMessage());
			logger.error("Text entered: " + text);
		}
	}

	// Method to get text from an input field
	public String getText(By by) {
		String text = null;
		try {
			applyBorder(by, "green");
			waitForElementToBeVisible(by);
			text = driver.findElement(by).getText();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("unable to get the text: " + e.getMessage());
		}
		return text;
	}

	// method to compare two text -- changed the return type
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (actualText.equalsIgnoreCase(expectedText)) {
				applyBorder(by, "green");
				logger.info("Text are matching: " + actualText + " equals " + expectedText);
				return true;
			} else {
				logger.error("Text are matching: " + actualText + " not equals " + expectedText);
				return false;
			}
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("Unable to compare texts: " + e.getMessage());
		}
		return false;
	}

	// method to check if an element is displayed
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			logger.info("Element is display: " + getElementDescription(by));
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			logger.error("Element is not displayed: " + e.getMessage());
			return false;
		}
	}

	public void scrollToElement(By by) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true)", element);
		} catch (Exception e) {
			System.out.println("Unable to locate element: " + e.getMessage());
		}
	}

	// Wait for the page to load
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			System.out.println("Page loaded successfully.");
		} catch (Exception e) {
			System.out.println("Page did not load within " + timeOutInSec + "seconds. Exception: " + e.getMessage());
		}
	}

	// method to get the description of an element using by locator
	public String getElementDescription(By locator) {
		String error = "Unable to describe the element";
		// check for null driver or locator to avoid nullPointer Exception
		if (driver == null) {
			return "driver is null";
		}
		if (locator == null) {
			return "locator is null";
		}
		try
		{
			// find the element using the locator
			WebElement element = driver.findElement(locator);

			// get element attribute
			String name = element.getDomAttribute("name");
			String id = element.getDomAttribute("id");
			String text = element.getText();
			String className = element.getDomAttribute("class");
			String placeHolder = element.getDomAttribute("placeholder");

			// Return the description based on element attributes
			if (isNotEmpty(name)) 
			{
				return "Element with name: " + name;
			} 
			else if (isNotEmpty(id)) 
			{
				return "Element with id: " + id;
			} 
			else if (isNotEmpty(text)) 
			{
				return "Element with text: " + truncate(text, 50);
			}
			else if (isNotEmpty(className)) 
			{
				return "Element with text: " + className;
			} 
			else if (isNotEmpty(placeHolder)) 
			{
				return "Element with placeholder: " + placeHolder;
			}
		}
		catch(Exception e)
		{
			logger.error("Unable to describe the element " + e.getMessage());
		}
		return error;
	}

	// utility method to check a String is null
	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	// utility method to truncated long string
	private String truncate(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		}
		return value.substring(0, maxLength) + "...";
	}
	
	//Utility method to border an element 
	public void applyBorder(By by, String color)
	{
		try {
			//Locate an element 
			WebElement element = driver.findElement(by);
			//apply the border
			String script = "arguments[0].apply.border='3px' solid "+color+"'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeAsyncScript(script, element);
			logger.info("Apply the border with the color "+color+"to element "+getElementDescription(by));
		} catch (Exception e) {
			logger.warn("Failed to apply the border to an element: " + getElementDescription(by));
		}
	}

}
