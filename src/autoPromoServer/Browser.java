package autoPromoServer;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

class Browser {
	WebDriver browser;
	/**Initializes the Browser in order for it to be ready to be used
	 * 
	 */
	public Browser() {
		String exePath = "./chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", exePath);
		browser = new ChromeDriver();
    	browser.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		browser.manage().window().maximize();
	}
	/**Returns true on successful login
	 * @param email
	 * @param password
	 * @return true (login successful), false (login failed)
	 */
	Boolean login(String email, String password) {
		browser.get("http://tokopedia.com/");
		browser.findElement(By.id("login-ddl-link")).click();
		browser.switchTo().frame(browser.findElement(By.id("iframe-accounts")));
		browser.findElement(By.id("inputEmail")).sendKeys(email);
		browser.findElement(By.id("inputPassword")).sendKeys(password);
		browser.findElement(By.id("global_login_btn")).click();
		
		if (browser.getCurrentUrl().contains("https://accounts.tokopedia.com/otp/c/page")) {
			return false;
		} else if (browser.getCurrentUrl().contains("https://accounts.tokopedia.com/authorize")) { //if login is wrong, tokopedia redirects to this link
			return false;
		}
		return true;
	}
	void logout() {
		browser.get("https://www.tokopedia.com/logout?=");
	}
	void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
}

