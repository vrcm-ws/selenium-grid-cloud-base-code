package com.practicetestautomation.base;

import java.io.IOException;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.JobsEndpoint;
import com.saucelabs.saucerest.model.jobs.UpdateJobParameter;

public class SauceLabsTestListener extends TestListenerAdapter
{
	private boolean sauceLabs = false;
	private String sessionID;
	private SauceREST api;
	private JobsEndpoint jobs;	
	
	@Override
	public void onTestStart(ITestResult result)
	{
		super.onTestStart(result);
		
		sauceLabs = (boolean) result.getTestContext().getAttribute("sauceLabs");
		
		if (sauceLabs)
		{
			sessionID = (String) result.getTestContext().getAttribute("sessionID");			
			api = new SauceREST(System.getenv("SAUCE_USERNAME"), System.getenv("SAUCE_ACCESS_KEY"), DataCenter.US_WEST);
			jobs = api.getJobsEndpoint();
		}
	}
	
	@Override
	public void onTestSuccess(ITestResult result)
	{
		super.onTestSuccess(result);
		
		if(sauceLabs)
		{
			try
			{
				jobs.updateJob(sessionID, new UpdateJobParameter.Builder().setPassed(true).build());
			}
			catch (IOException e)
			{			
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onTestFailure(ITestResult result)
	{
		super.onTestFailure(result);
		
		if(sauceLabs)
		{
			try
			{
				jobs.updateJob(sessionID, new UpdateJobParameter.Builder().setPassed(false).build());		
			}
			catch (IOException e)
			{			
				e.printStackTrace();
			}
		}		
	}	
}
