package com.orangeHRM.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;

@Listeners(com.orangehrm.utilities.TestListener.class)
public class LoginPageTest extends BaseClass {

	private LoginPage loginpage;
	private HomePage homepage;
	
	


	@BeforeMethod
	public void setUpPages()
	{
		loginpage = new LoginPage(getDriver());
		homepage = new HomePage(getDriver());
	}
	
	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
	public void verfiyValidLoginTest(String userName, String password)
	{
		loginpage.login(userName, password);
		Assert.assertTrue(homepage.isAdminTabVisible(), "Admin tab should be visible after successfull");
		homepage.logout();
		staticWait(2);
	}
	
	@Test(dataProvider = "inValidLoginData", dataProviderClass = DataProviders.class)
	public void invalidLoginTest(String userName, String password)
	{
		loginpage.login(userName, password);
		String expectedErrorMessage = "Invalid credentials";
		Assert.assertTrue(loginpage.verifyErrorMessage(expectedErrorMessage), "Test Falied: Invalid error message");
	}
	
	
	
}
