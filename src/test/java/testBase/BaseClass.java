package testBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager; // log4j
import org.apache.logging.log4j.Logger; // log4j
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public class BaseClass {

	public static WebDriver driver; // for onTestFailure()
	public Logger logger;
	public Properties p;

	@BeforeClass(groups = "smoke")
	@Parameters({ "os", "browser" })
	public void setup(String os, String browser) throws IOException {

		// loading config.properties file
		FileReader file = new FileReader(".//src//test//resources//config.properties");
		p = new Properties();
		p.load(file);

		logger = LogManager.getLogger(this.getClass());
		
		// this is for if remote
		if(p.getProperty("execution_env").equalsIgnoreCase("remote"))
		{
			DesiredCapabilities capabilities = new DesiredCapabilities();
			// os
			if(os.equalsIgnoreCase("Windows")) {
				capabilities.setPlatform(Platform.WIN11);
			}else if(os.equalsIgnoreCase("mac")){
				capabilities.setPlatform(Platform.MAC);
			}else if(os.equalsIgnoreCase("linux")){
				capabilities.setPlatform(Platform.LINUX);
			}else {
				System.out.println("no matching os");
				return; // automatically exit from the if
			}
			
			// broswer
			switch (browser.toLowerCase())
			{
			case "chrome":capabilities.setBrowserName("Chrome");break;
			case "edge":capabilities.setBrowserName("MicrosoftEdge");break;
			case "firefox":capabilities.setBrowserName("firefox");break;
			default:System.out.println("no matching browser");return;
			}
			
			driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),capabilities);
		}
		
	
		if (p.getProperty("execution_env").equalsIgnoreCase("local"))
			
			switch (browser.toLowerCase())
			{
			case "chrome":driver = new ChromeDriver();break;
			case "edge":driver = new EdgeDriver();break;
			case "firefox":driver = new FirefoxDriver();break;
			default:System.out.println("Invalid broswer name given...");return;
			}

		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get(p.getProperty("url"));
		driver.manage().window().maximize();
	}

	@AfterClass(groups = "smoke")
	public void tearDown() {

		driver.quit();
	}

	public void randomString() {

		String generatedRandomString = RandomStringUtils.random(5, 0, 0, true, true, null, new SecureRandom());
		System.out.println("Random String ==> " + generatedRandomString);
	}

	public String captureScreen(String tname) throws IOException {

		String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

		TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
		File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);

		String targetFilePath = System.getProperty("user.dir") + "\\screenshots\\" + tname + "_" + timeStamp + ".png";
		File targetFile = new File(targetFilePath);

		sourceFile.renameTo(targetFile);

		return targetFilePath;

	}
}
