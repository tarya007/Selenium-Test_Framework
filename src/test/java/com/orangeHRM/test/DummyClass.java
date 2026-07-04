package com.orangeHRM.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;

public class DummyClass extends BaseClass{
	
	@Test
	public void dummyTest()
	{
		String title = getDriver().getTitle();
		Assert.assertEquals("OrangeHRM", title);
		
		System.out.println("Test Passed - Title is matching");
	}

}
