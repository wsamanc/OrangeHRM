package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
//import java.net.URL;
import java.net.URL;

//Extent report 5.x...//version

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBase.BaseClass;

public class ExtentReportManager implements ITestListener {
	public ExtentSparkReporter sparkReporter;
	public ExtentReports extent;
	public ExtentTest test;

	String repName;

	public void onStart(ITestContext testContext) {

		/*
		 * SimpleDateFormat df=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss"); Date dt=new
		 * Date(); String currentdatetimestamp=df.format(dt);
		 */
		// create date with time
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());// time stamp
		// make report name
		repName = "Test-Report-" + timeStamp + ".html";
		// store the report name with time stamp
		sparkReporter = new ExtentSparkReporter(".\\reports\\" + repName);// specify location of the report

		sparkReporter.config().setDocumentTitle("OrangeHRM Automation Report"); // Title of report
		sparkReporter.config().setReportName("OrangeHRM Functional Testing"); // name of the report
		sparkReporter.config().setTheme(Theme.DARK);

		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		// add project related things
		extent.setSystemInfo("Application", "OrangeHRM");
		extent.setSystemInfo("Module", "Admin");
		extent.setSystemInfo("Sub Module", "Customers");
		//
		extent.setSystemInfo("User Name", System.getProperty("user.name"));
		extent.setSystemInfo("Environemnt", "QA");

		// capture details from xml file dynamically
		String os = testContext.getCurrentXmlTest().getParameter("os");
		extent.setSystemInfo("Operating System", os);

		String browser = testContext.getCurrentXmlTest().getParameter("browser");
		extent.setSystemInfo("Browser", browser);
		// get groups related details from xml file
		// getCurrentXmlTest() will return xml file
		// getIncludedGroups will capture include section group names
		List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
		if (!includedGroups.isEmpty()) {
			// add group info to the report
			extent.setSystemInfo("Groups", includedGroups.toString());
		}
	}

	public void onTestSuccess(ITestResult result) {

		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups()); // to display groups in report
		test.log(Status.PASS, result.getName() + " got successfully executed");

	}

	public void onTestFailure(ITestResult result) {
		// get class name
		test = extent.createTest(result.getTestClass().getName());
		// get which group executed
		test.assignCategory(result.getMethod().getGroups());

		test.log(Status.FAIL, result.getName() + " got failed");
		test.log(Status.INFO, result.getThrowable().getMessage());

		try {

			String imgPath = new BaseClass().captureScreen(result.getName());
			test.addScreenCaptureFromPath(imgPath);

		} catch (IOException e1) {
			// can print the warning on the console by printStackTrace()
			e1.printStackTrace();
		}
	}

	public void onTestSkipped(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.SKIP, result.getName() + " got skipped");
		test.log(Status.INFO, result.getThrowable().getMessage());
	}

	public void onFinish(ITestContext testContext) {

		//
		extent.flush();

		String pathOfExtentReport = System.getProperty("user.dir") + "\\reports\\" + repName;
		File extentReport = new File(pathOfExtentReport);

		try {
			// will open the report on browser automatically
			Desktop.getDesktop().browse(extentReport.toURI());
		} catch (IOException e) {
			e.printStackTrace();
		}
/*
		// get url format
		try {
			URL url = new URL("file:///" + System.getProperty("user.dir") + "\\reports\\" + repName);

			// Create the email message
			ImageHtmlEmail email = new ImageHtmlEmail();
			email.setDataSourceResolver(new DataSourceUrlResolver(url));
			email.setHostName("smtp.googlemail.com");
			email.setSmtpPort(465);
			// set who is send the mail
			email.setAuthenticator(new DefaultAuthenticator("wsamanc@gmail.com", "Wijsach90"));
			email.setSSLOnConnect(true);
			
			email.setFrom("wsamanc@gmail.com");
			// Sender email.setSubject("Test Results");
			email.setMsg("Please find Attached Report....");
			email.addTo("wsamanc@gmail.com"); // Receiver
			email.attach(url, "extent report", "please check report...");
			email.send(); // send the email }
		}

		catch (Exception e) {
			e.printStackTrace();
		}
*/
	}

}
