package com.practicetestautomation.base;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.logging.Level;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class BrowserDriverFactory
{
	private ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private RemoteWebDriver remoteDriver;
	private String browser;
	private Logger log;

	public BrowserDriverFactory(String browser, Logger log)
	{
		this.browser = browser;
		this.log = log;
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
				
			case "edge":
				driver.set(new EdgeDriver());
				break;
	
			default:				
				driver.set(new ChromeDriver());
				break;
		}
		
		java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
		
		return driver.get();
	}
	
	public RemoteWebDriver createRemoteDriver()
	{
		DesiredCapabilities capabilities = new DesiredCapabilities();				
		String gridServer = "http://192.168.100.31:4444";

		capabilities.setBrowserName(browser);
		
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
}
