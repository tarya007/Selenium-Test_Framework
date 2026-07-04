package com.orangeHRM.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;

public class HomePageTest extends BaseClass {
	private LoginPage loginpage;
	private HomePage homepage;
	
	@BeforeMethod
	public void setUpPages()
	{
		loginpage = new LoginPage(getDriver());
		homepage = new HomePage(getDriver());
	}
	
	@Test
	public void verifyOrangeHRMlogo()
	{
		loginpage.login("Admin", "admin123");
		Assert.assertTrue(homepage.verifyOrangeHRMlogo(), "Logo is not visible");
	}
	
	

}
