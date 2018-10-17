package autoPromoClient;

import java.util.concurrent.TimeUnit;

public class ClientMain {
	
	static NetworkComm networkComm;

	public static void main(String[] args) {
		
		ClientGui.showLogin();
		Dashboard.showDashboard();
		pauseApp(1);
		networkComm = new NetworkComm();
		networkComm.start();
	}
	
	public static void login(String email, String password, String aktivasi) {
		if (networkComm.isConnected) {
			networkComm.sendMsg("01" +  email);
			networkComm.sendMsg("02" +  password);
			networkComm.sendMsg("03" +  aktivasi);
			pushNotif("Attempting to login");
		}else {
			pushNotif("No connection is established");
		}
	}
	
	public static void loginSuccess() {
		pushNotif("logged in");
		ClientGui.window.frmAutoPromoTokkopedia.setVisible(false);
		Dashboard.dashboard.frmDashboard.setVisible(true);
	}
	
	public static void sendMsg(String s) {
		networkComm.sendMsg(s);
	}
	
	public static void pushNotif(String s) {
		ClientGui.notif.setText(s);
		Dashboard.notif.setText(s);
		System.out.println(s);
	}
	
	public static void pauseApp(int seconds) {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeApp() {
		if(networkComm.isConnected) {
			networkComm.sendMsg("QUIT");
			networkComm.closeSocket();
		}
		pauseApp(1);
		System.exit(0);
	}

}
