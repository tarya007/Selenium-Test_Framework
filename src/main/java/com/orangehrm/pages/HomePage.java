package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class HomePage {
	private ActionDriver ad;
	private WebDriver driver;
	
	public HomePage(WebDriver driver) {
		this.ad = BaseClass.getActionDriver();
	}
	
	//define locators using by class
	private By adminTab = By.xpath("//span[normalize-space()='Admin']");
	private By userIDButton = By.className("oxd-userdropdown-name");
	private By logoutButton = By.xpath("//a[normalize-space()='Logout']");
	private By orangeHRMlogo = By.xpath("//div[@class='oxd-brand-banner']");
	
	
	//method to verify if Admin tab is visible
	public boolean isAdminTabVisible()
	{
		return ad.isDisplayed(adminTab);
	}
	
	public boolean verifyOrangeHRMlogo()
	{
		return ad.isDisplayed(orangeHRMlogo);
	}
	
	//method to perform logout operation
	public void logout()
	{
		ad.click(userIDButton);
		ad.click(logoutButton);
	}
	

}
