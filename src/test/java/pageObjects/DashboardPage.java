package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashboardPage extends BasePage {

	// constructor
	public DashboardPage(WebDriver driver) {
		super(driver);
	}

	// locators
	@FindBy(xpath = "//h6[text()='Dashboard']")
	WebElement imageDahsboard;

	// actions
	public boolean getDashboardVisibilityConfirmation() {
		return imageDahsboard.isDisplayed();
	}

	public String getDashboardText() {
		try {
			return imageDahsboard.getText();
		} catch (Exception e) {
			return (e.getMessage());
		}
	}

}
