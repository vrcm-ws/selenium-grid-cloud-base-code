package com.practicetestautomation.base;

import java.util.logging.Level;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BrowserDriverFactory
{
	private ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private String browser;
	private Logger log;

	public BrowserDriverFactory(String browser, Logger log)
	{
		this.browser = browser.toLowerCase();
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
	
			default:
				log.debug("Do not know how to start: " + browser + ", starting chrome.");
				driver.set(new ChromeDriver());
				break;
		}
		
		java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
		
		return driver.get();
	}
}
