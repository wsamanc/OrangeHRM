package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

	// constructor
	public LoginPage(WebDriver driver) {
		super(driver);
	}

	// locators
	@FindBy(xpath = "//input[@name='username']")
	WebElement txtUserName;

	@FindBy(xpath = "//input[@name='password']")
	WebElement txtPassword;

	@FindBy(xpath = "//button[@type='submit']")
	WebElement btnlogin;

	// actions

	public void enterUserName(String username) {
		txtUserName.sendKeys(username);
	}

	public void enterPassword(String pwd) {
		txtPassword.sendKeys(pwd);
	}

	public void clickLoginBtn() {
		btnlogin.click();
	}
}
