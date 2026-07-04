package com.orangeHRM.test;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.utilities.ApiUtility;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

import io.restassured.response.Response;

public class ApiTest {
	
	@Test
	public void verifyGetUserAPI() {
		
		SoftAssert softAssert = new SoftAssert();
		
		//Step1: Define API Endpoint
		String endPoint = "https://jsonplaceholder.typicode.com/users/1";
		ExtentManager.logStep("API Endpoint" + endPoint);
		
		
		//Step2: Send Get Request
		ExtentManager.logStep("Sending Get Request to the API");
		Response response = ApiUtility.sendGetRequest(endPoint);
		
		//Step3: validate status code
		ExtentManager.logStep("Validating API response status code");
		boolean isStatusCodeValid = ApiUtility.validateStatusCode(response, 200);
		
		softAssert.assertTrue(isStatusCodeValid, "Status code is not as Expected");
		
		if(isStatusCodeValid) {
			ExtentManager.logStepValidationForAPI("Status Code Validation Passed!");
		}
		else {
			ExtentManager.logFailureAPI("Status Code Validation Failed!");
		}
		
		
		//Step4: validate username
		ExtentManager.logStep("Validating response body for username");
		String userName = ApiUtility.getJsonValue(response, "username");
		boolean isUserNameValid = "Bret".equals(userName);
		softAssert.assertTrue(isUserNameValid, "Username is not valid");
		if(isUserNameValid) {
			ExtentManager.logStepValidationForAPI("Username Validation Passed!");
		}
		else {
			ExtentManager.logStepValidationForAPI("Username Validation Failed!");
		}
		
		//Step4: validate email
		ExtentManager.logStep("Validating response body for email");
		String userEmail = ApiUtility.getJsonValue(response, "email");
		boolean isEmailValid = "Sincere@april.biz".equals(userEmail);
		softAssert.assertTrue(isEmailValid, "Email is not valid");
		if(isEmailValid) {
			ExtentManager.logStepValidationForAPI("Email Validation Passed!");
		}
		else {
			ExtentManager.logStepValidationForAPI("Email Validation Failed!");
		}
		
		softAssert.assertAll();
	}

}
