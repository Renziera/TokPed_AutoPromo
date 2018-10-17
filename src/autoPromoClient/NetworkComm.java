package autoPromoClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

/*
 * Transmission protocol
 * 
 * 01 Email
 * 02 Password
 * 03 Kode aktivasi
 * 04 Status
 * 05 Durasi
 * 06 Item Promo
 * 07 Saldo
 * 08 Nama Rekening
 * 09 Nomor Rekening
 * 10 Beli
 * 11 Log
 * 12 Additional Notes
 * 13 Nama toko
 * 14 Produk >>> 140 produk biasa | 141 produk terpilih
 * 15 Request produk
 * 16 Produk yg dipilih
 * 17 Berhasil login
 * 18 Gagal login
 * 19 flush database item
 * 20 Request OTP
 * 21 Method OTP (wa, sms, call)
 * 22 Kode OTP
 * 
 */

public class NetworkComm extends Thread{
	
	private static Socket socket;
	public static final int port = 15120;
	static String host = "autopromo.hopto.org";
	static InetAddress address = null;
	static DataInputStream in = null;
	static BufferedReader br = null;
	static DataOutputStream out = null;
	static BufferedWriter bw = null;
	public boolean isConnected = false;
	
	String receivedMsg;
	String receivedMsgCode;
	String sendMsg;
	
	public NetworkComm() {
		try {
			ClientMain.pushNotif("Attempting to connect to server");			
			address = InetAddress.getByName(host);
			socket = new Socket(address, port);
			
			in = new DataInputStream(socket.getInputStream());
			br = new BufferedReader(new InputStreamReader(in));
			out = new DataOutputStream(socket.getOutputStream());
			bw = new BufferedWriter(new OutputStreamWriter(out));
			
			isConnected = true;
			ClientMain.pushNotif("Connection to server established");			
			
		}catch (Exception e) {
			ClientMain.pushNotif("Couldn't connect to server, check your connection and restart application");
			e.printStackTrace();
		}

	}
	
	public void run() {
		try {
			while(true) {
				receivedMsg = br.readLine();
				debug("Received a message " + receivedMsg);
				
				receivedMsgCode = receivedMsg.substring(0, 2);
				receivedMsg = receivedMsg.substring(2);	
				
				handleMsg(receivedMsgCode, receivedMsg);
			}
			
		} catch (SocketException e) {
			ClientMain.pushNotif("Koneksi teputus");
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMsg(String msg) {
		try {
			bw.write( msg + "\n");
			bw.flush();
		} catch (Exception e) {
			this.isConnected = false;
			ClientMain.pushNotif("Disconnected from server, bring to login page");
		}
	}
	
	public void closeSocket() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void debug(String s) {
		System.out.println(s);
	}
	
	public void handleMsg(String receivedMsgCode, String receivedMsg) {
		
		switch (receivedMsgCode) {
		case "04":
			Dashboard.status.setText(receivedMsg);
			break;
		case "05":
			Dashboard.durasi.setText(receivedMsg);
			break;
		case "06":
			Dashboard.itemPromo.setText(receivedMsg);
			break;
		case "07":
			Dashboard.saldo.setText(receivedMsg);
			break;
		case "11":
			Dashboard.log.append(receivedMsg + "\n");
			break;
		case "12":
			Dashboard.addNotes.setText(receivedMsg);
			break;
		case "13":
			Dashboard.namaToko.setText(receivedMsg);
			break;
		case "14":
			if(receivedMsg.charAt(0) == '0') {
				Dashboard.addToList(receivedMsg.substring(1), false);
			}else {
				Dashboard.addToList(receivedMsg.substring(1), true);
			}
			break;
		case "17":
			ClientMain.loginSuccess();
			break;
		case "18":
			ClientMain.pushNotif("Login gagal, silahkan coba lagi");
			break;
		case "20":
			ClientGui.ShowOTPWindow();
			break;
		default:
			break;
		}
	}

}
