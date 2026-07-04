package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {
	private ActionDriver ad;
	private WebDriver driver;

	public LoginPage(WebDriver driver) {
		this.ad = BaseClass.getActionDriver();
		this.driver=driver;
	}
	

	//define locators using by class
	private By userNameField = By.name("username");
	private By passwordField = By.cssSelector("input[type='password']");
	private By loginButton = By.cssSelector("button[type='submit']");
	private By errorMessage = By.xpath("//p[text()='Invalid credentials']");
	
	
	//methods to perform login
	public void login(String username, String password)
	{
		ad.enterText(userNameField, username);
		ad.enterText(passwordField, password);
		ad.click(loginButton);
	}
	
	//method to check if error message is displayed
	public boolean isErrorMessageDisplayed()
	{
		return ad.isDisplayed(errorMessage);
	}
	
	//method to get text from error message
	public String getErrorMessageText()
	{
		return ad.getText(errorMessage);
	}
	
	//verfiy if error message is correct or not
	public boolean verifyErrorMessage(String expectedError)
	{
		return ad.compareText(errorMessage, expectedError);
	}

}
