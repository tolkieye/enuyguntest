package com.saf.framework;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.util.Base64;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;

public class MyTestNGBaseClass {

	public static WebDriver oDriver;
	public static ExtentReports oExtentReport;
	public static ExtentTest oExtentTest;
	public static int ssNumber;
	public static String reportPath;
	public static int testCaseId = 0;
	public static String sDriverName = "";

	@Parameters({ "browserName"})
	@BeforeSuite
	public void BeforeSuite(@Optional("")String browserName) throws Throwable{
		reportPath = "Report_" + new Date().getDate() + "-" + (new Date().getMonth() + 1) + "-" + new Date().getHours() + "-" + new Date().getMinutes() + "-" + new Date().getSeconds();
		File f = new File("Reports/" + reportPath);
		File ss = new File("Reports/" + reportPath + "/Screenshots");
		try{
			f.mkdir();
			ss.mkdir();
		}catch(Exception e) {
			e.printStackTrace();
		}
		oExtentReport = new ExtentReports("Reports/" + reportPath + "/TestSuiteReport.html", true);
		oExtentReport.loadConfig(new File("config.xml"));
		ssNumber=0;
		if (browserName.equalsIgnoreCase("ie")) {
			sDriverName = "ie";
		}
		else if (browserName.equalsIgnoreCase("firefox")) {
			sDriverName = "firefox";
		}
		else if (browserName.equalsIgnoreCase("chrome")) {
			sDriverName = "chrome";
		}
		else if (browserName.equalsIgnoreCase("htmlunit")) {
			sDriverName = "htmlunit";
		}
		else{
			throw new Exception("Unknown driver name = " + sDriverName +
					"Valid names are: ie,firefox,chrome,htmlunit");
		}
		oDriver = CommonLib.getDriver(sDriverName);
	}
	@Parameters({"VIDEO_RECORDING"})
	@AfterMethod
	public void stopRecording(@Optional("")String VIDEO_RECORDING, ITestResult result) throws Exception
	{
		if (VIDEO_RECORDING.equalsIgnoreCase("true")) {
			String media = ((CanRecordScreen) oDriver).stopRecordingScreen();
			File video = new File("Reports/" + reportPath + "/Videos");
			if(!video.exists()) {
				video.mkdirs();
			}
			FileOutputStream stream = new FileOutputStream(video + "/" + result.getName() + ".mp4");
			stream.write(Base64.decodeBase64(media));
		}
	}
	@AfterClass
	public void automationTeardown() throws Exception
	{
		testCaseId = 0;
	}
	@AfterSuite
	public void afterSuite()  throws Throwable{
		oExtentReport.endTest(oExtentTest);
		oExtentReport.flush();
		oDriver.quit();
	}
	public static boolean reportResult(String status, String message, boolean ssFlag){
		try {
			String dest = "";
			if(ssFlag) {
				ssNumber++;
				TakesScreenshot ts = (TakesScreenshot)oDriver;
				File source = ts.getScreenshotAs(OutputType.FILE);
				dest = System.getProperty("user.dir") + "/Reports/" + reportPath + "/Screenshots/" + ssNumber + ".png";
				File destination = new File(dest);
				FileUtils.copyFile(source, destination);
			}
			if(status.equalsIgnoreCase("PASS")) {
				if(ssFlag) {
					oExtentTest.log(LogStatus.PASS, message + "\n" + oExtentTest.addScreenCapture(dest));
				}else {
					oExtentTest.log(LogStatus.PASS, message);
				}
			}else if(status.equalsIgnoreCase("FAIL")) {
				if(ssFlag) {
					oExtentTest.log(LogStatus.FAIL, message + "\n" + oExtentTest.addScreenCapture(dest));
				}else {
					oExtentTest.log(LogStatus.FAIL, message);
				}
				//DBReporting.insertExecutionDetailsIntoDB(testCaseId, "FAIL", message, className, System.getProperty("user.name"));
			}else {
				if(ssFlag) {
					oExtentTest.log(LogStatus.INFO, message + "\n" + oExtentTest.addScreenCapture(dest));
				}else {
					oExtentTest.log(LogStatus.INFO, message);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean  startTest(String scenarioName) {
		oExtentTest = oExtentReport.startTest(scenarioName);
		return true;
	}
}
