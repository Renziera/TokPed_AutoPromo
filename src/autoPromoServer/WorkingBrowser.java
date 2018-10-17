package autoPromoServer;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

class WorkingBrowser extends Browser {
	
	String username;
	String password;
	Handler handler;
	public String pickedOption = "", kodeOTP = "";
	
	public WorkingBrowser(Handler handler) {
		this.handler = handler;
	}

	Boolean checkLogin(String username, String password) {
		this.username = username;
		this.password = password;
		Boolean login = login(username, password);
		if(!login) {
			if (browser.getCurrentUrl().contains("https://accounts.tokopedia.com/otp/c/page")) {
				handler.requestOTP();
				int i = 0;
				while(pickedOption == "") {//wait for user to pick verification method
					sleep(2000);
					if (i > 10) {
						return false;
					}
					i++;
				}
				browser.findElement(By.id("cotp__method--" + pickedOption)).click(); //wa, sms, call
				sleep(1000);
				while(kodeOTP == "") {//wait for User to set kodeOTP
					sleep(2000);
					if (i > 40) {
						return false;
					}
					i++;
				}
				browser.findElement(By.id("otp-number-input-1")).sendKeys(kodeOTP);

				if (browser.findElement(By.id("code-error")).getAttribute("class") == "fs-12 cotp__code-error invalid") {// if OTP Number correct
					return true;
				}
				return false;
			}
		}
		return login;
	}
	List<String> getProducts() {
        goToToko();
        sleep(1000);
        List<String> names = new ArrayList<>();
        List<String> links = new ArrayList<>();
        while (true) {
            List<WebElement> productsHTML = browser.findElements(By.cssSelector(".product.shop-product"));
            for (WebElement product : productsHTML){
                names.add(product.getAttribute("data-wname"));
                links.add(product.findElement(By.cssSelector("a")).getAttribute("href"));
            }

            try {// is there nextPageButton?
                browser.findElement(By.linkText("»")).click();;
            } catch (Exception e) {
                break;
            }
        }
        handler.incProductsLinks = links;
        return names;
    }
	
	String getTokoName() {
		return browser.findElement(By.cssSelector(".fs-12.break-word")).getText();
	}
	
	void goToToko() {
		browser.findElement(By.id("shop-tab")).click();
		sleep(1000); //wait for dropdown to appear
		browser.findElement(By.cssSelector(".dropdown-shop > li:first-child")).click();
	}
	
	void closeBrowser() {
		browser.close();
	}
	
}
