package autoPromoServer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class Handler {
	
	WorkingBrowser wb = new WorkingBrowser(this);
	NetworkComm.ClientHandler clientHandler;
	
	public Handler(NetworkComm.ClientHandler clientHandler) {
		this.clientHandler = clientHandler;
	}
	
	public void sendMsgToClient(String s) {
		clientHandler.sendMsgToClient(s);
	}
	
	void getProducts() {
		List<String> checkedProducts = DB.getProductsName(wb.username);
		List<String> incProducts = wb.getProducts();
		incProductsNames = new ArrayList<String>(incProducts);
		for (String product : checkedProducts) {
			if(incProducts.contains(product)) {
				incProducts.remove(product);
				clientHandler.sendMsgToClient("141" + product);
			}
		}
		
		for(String product : incProducts) {
			clientHandler.sendMsgToClient("140" + product);
		}				
	}
	
	void flushProducts() {
		DB.flushProducts(wb.username);
		BrowserManager.removeUser(wb.username);
		Runnable run = () -> {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				ServerMain.logToServer(e.toString());
				e.printStackTrace();
			}
			BrowserManager.add(new User(wb.username, wb.password, DB.getProductsLink(wb.username)));
			
		};
		Thread thread = new Thread(run);
		thread.run();
	}
	
	List<String> incProductsLinks = null;
	List<String> incProductsNames = null;
	
	void addProducts(String product) {
		
		int itemPromo = DB.numberOfProducts(wb.username);
		
		if(itemPromo < DB.getUserValueInt(wb.username, "MAXPRODUK")) {
			DB.addProduct(wb.username, product , incProductsLinks.get(incProductsNames.indexOf(product)));
			clientHandler.logToClient("Berhasil menambah produk " + product);
			ServerMain.logToServer("Client " + wb.username + " on " +  clientHandler.clientSocket.getRemoteSocketAddress() + " has added product " + product);
			itemPromo++;
			DB.updateUserInt(wb.username, "ITEMPROMO", itemPromo );
			clientHandler.sendMsgToClient("06" + itemPromo + " Item");
		}else {
			clientHandler.logToClient("Gagal menambah produk " + product);
			clientHandler.logToClient("Jumlah produk melebihi batas, silahkan mengupgrade paket");
			ServerMain.logToServer("Client " + wb.username + " on " +  clientHandler.clientSocket.getRemoteSocketAddress() + " failed to add " + product + " limit reached");
		}
	}
	
	/**Check whether or not login is valid.
	 * @param username
	 * @param password
	 * @return
	 */
	Boolean checkLogin(String username, String password) {
		ServerMain.logToServer("Attemptng to login " + username);
		boolean bool = wb.checkLogin(username, password);
		
		if(bool) {
			if(!DB.checkUserExist(username)) { //user baru
				DB.addUser(username, password);
				DB.updateUser(username, "STATUS", "TRIAL");
				DB.updateUserInt(username, "DURASI", tambahDurasi(Integer.parseInt(new SimpleDateFormat("yyMMdd").format(new Date())), 3));
				DB.updateUserInt(username, "ITEMPROMO", 0);
				DB.updateUserInt(username, "SALDO", 0);
				DB.updateUserInt(username, "MAXPRODUK", 3);
				ServerMain.logToServer("CLIENT BARU " + username + " KIRIMIN DIA EMAIL NOTIFIKASI WOI");
			}else {
				if(!password.equals(DB.getUserValue(username, "PASSWORD"))) {
					DB.updateUser(username, "PASSWORD", password); //dia ganti password
				}
			}
			clientHandler.sendMsgToClient("13" + wb.getTokoName());
			DB.updateUser(username, "NAMATOKO", wb.getTokoName());
			ServerMain.logToServer("Login success " + username);
		}
		return bool;
	}
	
	void sendStatusFromDB() {
		clientHandler.sendMsgToClient("04" + DB.getUserValue(wb.username, "STATUS"));
		int durasi = DB.getUserValueInt(wb.username, "DURASI");
		int interval = intervalInDays(durasi);
		if(interval < 0) {
			clientHandler.sendMsgToClient("05EXPIRED");
		}else {
			clientHandler.sendMsgToClient("05" + interval + " hari");
		}
		clientHandler.sendMsgToClient("06" + DB.getUserValueInt(wb.username, "ITEMPROMO") + " Item");
		clientHandler.sendMsgToClient("07Rp" + DB.getUserValueInt(wb.username, "SALDO"));
		String addNotes = DB.getUserValue(wb.username, "ADDNOTES");
		if(addNotes != null && addNotes != "") clientHandler.sendMsgToClient("12" + addNotes);
		
	}

	void requestOTP() {
		clientHandler.sendMsgToClient("20");
	}
	
	public void beli(String paket) {
		
		DB.updateUser(wb.username, "STATUS", paket);
		int saldo = DB.getUserValueInt(wb.username, "SALDO");
		
		switch (paket) {
		case "3 ITEM":
			saldo -= 30000;
			DB.updateUserInt(wb.username, "SALDO", saldo);
			DB.updateUserInt(wb.username, "MAXPRODUK", 3);
			break;
		case "10 ITEM":
			saldo -= 50000;
			DB.updateUserInt(wb.username, "SALDO", saldo);
			DB.updateUserInt(wb.username, "MAXPRODUK", 10);
			break;
		case "SEPUASNYA":
			saldo -= 60000;
			DB.updateUserInt(wb.username, "SALDO", saldo);
			DB.updateUserInt(wb.username, "MAXPRODUK", 9999);
			break;

		default:
			break;
		}
		
		int durasi = DB.getUserValueInt(wb.username, "DURASI");
		ServerMain.logToServer(""+durasi);
		if(DB.getExpired(wb.username)) {
			durasi = tambahDurasi(Integer.parseInt(new SimpleDateFormat("yyMMdd").format(new Date())), 30);
		}else {
			durasi = tambahDurasi(durasi, 30);
		}
		DB.updateUserInt(wb.username, "DURASI", durasi);
		
		clientHandler.sendMsgToClient("04" + paket);
		clientHandler.sendMsgToClient("07Rp" + saldo);
		
		int interval = intervalInDays(durasi);
		if(interval < 0) {
			clientHandler.sendMsgToClient("05EXPIRED");
		}else {
			clientHandler.sendMsgToClient("05" + interval + " hari");
		}
		
		clientHandler.logToClient("Berhasil membeli paket " + paket);
		
	}
	
	static int intervalInDays (int durasiInt) {
		String durasiString = "" + durasiInt;
		DateFormat format = new SimpleDateFormat("yyMMdd", Locale.ENGLISH);
		Date dateExpire = null;
		try {
			dateExpire = format.parse(durasiString);
		} catch (ParseException e) {
			ServerMain.logToServer(e.toString());
			e.printStackTrace();
		}
		Date dateNow = new Date();
		long diffInMillies = dateExpire.getTime() - dateNow.getTime();
		int diff = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1;

		return diff;
	}
	
	static int tambahDurasi (int durasi, int tambah) {
        SimpleDateFormat format = new SimpleDateFormat("yyMMdd", Locale.ENGLISH);
        String durasiString = "" + durasi;
        Date durasiDate = null;
        try {
            durasiDate = format.parse(durasiString);
        } catch (ParseException e) {
        	ServerMain.logToServer(e.toString());
            e.printStackTrace();
        }

        long tambahMillis = TimeUnit.MILLISECONDS.convert(tambah, TimeUnit.DAYS);
        long tertambahMillis = durasiDate.getTime() + tambahMillis;
        int tertambahDate = Integer.parseInt(new SimpleDateFormat("yyMMdd").format(new Date(tertambahMillis)));
        return tertambahDate;
    }

}
