package com.practicetestautomation.base;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.testng.SauceOnDemandAuthenticationProvider;

public class DriverFactory implements SauceOnDemandAuthenticationProvider
{
	private ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private RemoteWebDriver remoteDriver;
	private String browser;
	private Logger log;
	private String test;
	private AbstractDriverOptions<?> browserOptions;
	private SauceOnDemandAuthentication auth = new SauceOnDemandAuthentication();
	
	public DriverFactory(String browser, Logger log, String test)
	{
		this.browser = browser;
		this.log = log;
		this.test = test;
		
		auth.setUsername(System.getenv("SAUCE_USERNAME"));
		auth.setAccessKey(System.getenv("SAUCE_ACCESS_KEY"));
	}

	public WebDriver createDriver()
	{
		log.info("Create driver: " + browser);

		switch (browser)
		{
			case "chrome":
				driver.set(new ChromeDriver());
				break;
	
			case "firefox":
				driver.set(new FirefoxDriver());
				break;
				
			case "Microsoft Edge":
				driver.set(new EdgeDriver());
				break;
	
			default:				
				driver.set(new ChromeDriver());
				break;
		}
		
		java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
		
		return driver.get();
	}


	public RemoteWebDriver createCloudDriver()
	{
		Map<String, Object> sauceOptions = new HashMap<>();
		String cloudServer = "https://ondemand.us-west-1.saucelabs.com:443/wd/hub";

		sauceOptions.put("username", auth.getUsername());
		sauceOptions.put("accessKey", auth.getAccessKey());				
		sauceOptions.put("build", getBuildName());		
		sauceOptions.put("name", test);
		
		switch (browser)
		{
			case "chrome":
				browserOptions = new ChromeOptions();
				browserOptions.setPlatformName("Windows 10");
				break;
	
			case "firefox":
				browserOptions = new FirefoxOptions();
				browserOptions.setPlatformName("Linux");
				break;
				
			case "MicrosoftEdge":
				browserOptions = new EdgeOptions();
				browserOptions.setPlatformName("Windows 11");
				break;
	
			default:
				browserOptions = new ChromeOptions();
				break;
		}

		browserOptions.setBrowserVersion("latest");
		browserOptions.setCapability("sauce:options", sauceOptions);

		try
		{
			remoteDriver = new RemoteWebDriver(URI.create(cloudServer).toURL(), browserOptions);
		}
		catch (MalformedURLException e)
		{
			log.info(e.getMessage());
		}

		return remoteDriver;
	}

	
	public RemoteWebDriver createRemoteDriver()
	{
		DesiredCapabilities capabilities = new DesiredCapabilities();
		
		String gridServer = "http://192.168.100.31:4444";

		capabilities.setBrowserName(browser);
		capabilities.setPlatform(Platform.WIN10);
		
		try
		{				
			remoteDriver = new RemoteWebDriver(URI.create(gridServer).toURL(), capabilities);
		}
		catch (MalformedURLException e)
		{
			log.info(e.getMessage());
		}
		
		return remoteDriver;
	}
	
	private String getBuildName()
	{
		String buildName = System.getenv("BUILD_NUMBER"); // From Jenkins
		
		if (buildName == null)
		{
		    buildName = "Local-Run-" + System.currentTimeMillis(); // Fallback for local dev
		}

		return buildName;
	}
	
	@Override
	public SauceOnDemandAuthentication getAuthentication()
	{		
		return auth;
	}
}
