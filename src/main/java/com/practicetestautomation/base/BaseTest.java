package com.practicetestautomation.base;

import java.lang.reflect.Method;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

@Listeners({SauceLabsTestListener.class})
public class BaseTest
{
	protected WebDriver driver;
	protected Logger log;
	protected String testName;

	@Parameters({ "browser", "driverType" })
	@BeforeMethod(alwaysRun = true)
	public void setUp(Method method, @Optional("chrome") String browser, @Optional String driverType, ITestContext testContext)
	{	
		testName = method.getDeclaringClass().getSimpleName() + " :: " + method.getName();
		log = LogManager.getLogger(testName);
		
		DriverFactory factory = new DriverFactory(browser, log, testName);
		
		testContext.setAttribute("sauceLabs", false);
		
		switch(driverType.toLowerCase())
		{
		case "local":
			driver = factory.createDriver();
			break;
		case "grid":
			driver = factory.createRemoteDriver();
			break;
		case "cloud":	
			driver = factory.createCloudDriver();
			
			testContext.setAttribute("sauceLabs", true);
			testContext.setAttribute("sessionID", factory.getSessionId());	
			
			break;
		default:
			driver = factory.createDriver();
			break;			
		}
					
		//driver.manage().window().maximize();
	}
 
	@AfterMethod(alwaysRun = true)
	public void tearDown()
	{
		log.info("Close driver");
		driver.quit();
	}	
}
