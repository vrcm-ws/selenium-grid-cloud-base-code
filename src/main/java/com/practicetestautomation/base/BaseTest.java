package com.practicetestautomation.base;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest
{
	protected WebDriver driver;
	protected Logger log;

	@Parameters({ "browser", "grid" })
	@BeforeMethod(alwaysRun = true)
	public void setUp(Method method, @Optional("chrome") String browser, @Optional("false") boolean grid, ITestContext ctx)
	{
		log = LogManager.getLogger(ctx.getCurrentXmlTest().getSuite().getName());
		
		if(grid)
		{
			driver = new BrowserDriverFactory(browser, log).createRemoteDriver();
		}
		else
		{
			driver = new BrowserDriverFactory(browser, log).createDriver();
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
