package autoPromoServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openqa.selenium.By;

public class BrowserManager {
	static boolean firstRun = true; //has this ran yet?
	static List<AutoBrowser> browsers = new ArrayList<>();
	static byte maxAccAmountInOneBrowser = 10;
	static byte accAmount = 0; //amount of accounts currently active in one browser
	static List<Integer> browserIndexes = new ArrayList<>();
	static Integer latestBrowserIndex = 0; //what browser is working
	static ExecutorService workingThread = Executors.newSingleThreadExecutor();

	static void add(User user) {
		if (firstRun == true) {
			browserIndexes.add(latestBrowserIndex);
			newBrowser(user);
			firstRun = false;
		} else if (accAmount == maxAccAmountInOneBrowser) { //account browser have reached browser limit
			latestBrowserIndex++; //add current browser index
			browserIndexes.remove(0);
			browserIndexes.add(latestBrowserIndex);
			newBrowser(user);
		} else {
			addToCurrentBrowser(user);
		}
	}
	
	private static void newBrowser(User user) {
		accAmount=0;
		browsers.add(new AutoBrowser());
		AutoBrowser currentBrowser = browsers.get(browserIndexes.get(0));
		currentBrowser.addUser(user);
		accAmount++;
		
		Runnable task = () -> {
			currentBrowser.run();
		};
		Thread thread = new Thread(task);
		thread.start();
	}
	private static void addToCurrentBrowser(User user) {
		int lastIndex = browserIndexes.size() - 1;
		browsers.get(browserIndexes.get(lastIndex)).addUser(user); //add user in the currentBrowser
		
		browserIndexes.remove(lastIndex);
		if (browserIndexes.size() == 0) {
			browserIndexes.add(latestBrowserIndex);
			accAmount++; //add amount of user in browser
		}
	}
	/**Single User Removal
	 * @param username
	 */
	static void removeUser(String username) {
		for(int i = 0; i < browsers.size(); i++) {
			if (browsers.get(i).removeUser(username)) {
				browserIndexes.add(i);
				ServerMain.logToServer("User removed from AutoBrowser " + username);
				break;
			}
		}
	}
	public static class AutoBrowser extends Browser{
		List<User> users = new ArrayList<User>();
		Boolean busy = false;
		/**Runs AutoClick for the browser. Infinite Loop
		 * 
		 */
		void run() {
			while(true) {
				busy = true;
				for(int i = 0; i < users.size(); i++) {
					try {
						login(users.get(i).username, users.get(i).password);
						clickPromoPerJam(users.get(i).getLink());
						logout();
					} catch (Exception e) {
						continue;
					}
				}
				busy = false;
				sleep(3660000);
			}
		}
		Boolean removeUser(String username) {
			while (busy) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) { }
			}
			for(User user : users) {
				if (user.username == username) {
					users.remove(user);
					return true;
				}
			}
			return false;
		}
		/**Adds user to List of Users to Auto Click
		 * @param user
		 */
		void addUser(User user) {
			users.add(user);
			ServerMain.logToServer("New user added to AutoBrowser " + user.username);
		}
		private void clickPromoPerJam(String link) {
			String returnVal = "";
			String responseText = "";
			browser.get(link);
			try {
				browser.findElement(By.id("promo-text-default")).click();
				responseText = browser.findElement(By.cssSelector(".pt-0 > .mb-0")).getText();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (responseText.matches("Promo pada produk .+ telah berhasil")) {
				returnVal = "Produk sukses di klik";
			} else if (responseText.matches("Anda belum dapat menggunakan fitur promo pada saat ini.")) {
				returnVal = "Produk sudah di klik";
			} else {
				returnVal = "Produk Click failed: something went wrong";
			}
			ServerMain.logToServer("ClickPromoPerJam :" + returnVal + " : " + link);
		}
	}
}