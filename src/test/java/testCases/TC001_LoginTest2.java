package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.DashboardPage;
import pageObjects.LoginPage;
import testBase.BaseClass;

public class TC001_LoginTest2 extends BaseClass {

	@Test(groups = {"smoke"})
	public void verify_login() {

		try {
			logger.info("********** Starting TC001_LoginTest **********");
			LoginPage lp = new LoginPage(driver);
			lp.enterUserName(p.getProperty("username"));
			logger.info("Entered username");
			lp.enterPassword(p.getProperty("password"));
			logger.info("Entered password");
			lp.clickLoginBtn();
			logger.info("Clicked the Login button");

			DashboardPage dp = new DashboardPage(driver);
			boolean dashboardVisibilityStatus = dp.getDashboardVisibilityConfirmation();
			Assert.assertTrue(dashboardVisibilityStatus, "Dashboard Text is not visible...");

			logger.info("Validated Dashboard Text");

			String TxtDashboard = dp.getDashboardText();
			if (TxtDashboard.equals("Dashboard")) {
				Assert.assertTrue(true);
			} else {
				logger.error("Dashboard Text assertion failed");
				logger.debug("Debug logs");
				Assert.assertTrue(false);
			}
			randomString();
			//logger.info("********** End TC001_LoginTest **********");
		} catch (Exception e) {

			Assert.fail();
		}
		logger.info("********** Finished TC001_LoginTest **********");
	}

}
