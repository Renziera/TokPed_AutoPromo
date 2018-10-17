package autoPromoServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 * 19 Flush database item
 * 20 Request OTP
 * 21 Method OTP (wa, sms, call)
 * 22 Kode OTP
 * 
 */

public class NetworkComm extends Thread{
	
	private ServerSocket serverSocket;
	private Socket socket;
	public static final int port = 15120;
	public static NetworkComm t;
	
	//Thread utama program server
	public static void setUpNetwork() {
		try {
			t = new NetworkComm();
			t.start();
		}catch(Exception  e){
			ServerMain.logToServer(e.toString());
			e.printStackTrace();
		}
	}
	
	//Constructor
	public NetworkComm() throws IOException{
		serverSocket = new ServerSocket(port);
		//serverSocket.setSoTimeout(9999);
	}
	
	public void closeServerSocket() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			ServerMain.logToServer(e.toString());
			e.printStackTrace();
		}
	}
	
	//Child thread method that delegates any client(s) to individual threads
	//Should only be run once TODO : jadikan singleton
	public void run() {
		while(true) {
			try {
				ServerMain.logToServer("Waiting for connection on " + serverSocket.getLocalPort() + "...");
				socket = serverSocket.accept(); //waits here until a client connects
				ServerMain.logToServer("A client has connected from " + socket.getRemoteSocketAddress());
			}catch(IOException e) {
				ServerMain.logToServer(e.toString());
				return;
			}
			new ClientHandler(socket).start();
		}
	}
	
	
	//Class yg di instantiate tiap kali ada koneksi client baru
	class ClientHandler extends Thread {
		
		protected Socket clientSocket;
		DataInputStream in = null;
		BufferedReader br = null;
		DataOutputStream out = null;
		BufferedWriter bw = null;
		
		String email, password, kodeAktivasi, namaRek, nomorRek;
		
		Handler handler = new Handler(this);
		
		public ClientHandler(Socket tempSocket) {
			this.clientSocket = tempSocket;
			
		}
		
		public void run() {
			
			try {
				clientSocket.setSoTimeout(1800000);
				in = new DataInputStream(clientSocket.getInputStream());
				br = new BufferedReader(new InputStreamReader(in));
				out = new DataOutputStream(clientSocket.getOutputStream());
				bw = new BufferedWriter(new OutputStreamWriter(out));
			} catch (IOException e) {
				ServerMain.logToServer(e.toString());
				e.printStackTrace();
			}
			
			ServerMain.logToServer("Client Handler for " + clientSocket.getRemoteSocketAddress() + " has successfuly set up");
			
			String receivedMsg;
			String receivedMsgCode;
			
			while(true) {
				try {
					receivedMsg = br.readLine();
					ServerMain.logToServer("Received message from " + clientSocket.getRemoteSocketAddress() + " : " +receivedMsg);
					if ((receivedMsg == null) || receivedMsg.equalsIgnoreCase("QUIT")) {
	                    socket.close();
	                    ServerMain.logToServer("Socket for " + clientSocket.getRemoteSocketAddress() + " has been closed");
	                    handler.wb.closeBrowser();
	                    return;
	                }
					
					receivedMsgCode = receivedMsg.substring(0, 2);
					receivedMsg = receivedMsg.substring(2);	
					
					
						handleMsg(receivedMsgCode, receivedMsg);
					
				}  catch (SocketTimeoutException e) {
					ServerMain.logToServer(e.toString());
					ServerMain.logToServer("Socket " + clientSocket.getRemoteSocketAddress() + " has timed out");
					handler.wb.closeBrowser();
					try {
						clientSocket.close();
						return;
					} catch (IOException e1) {
						ServerMain.logToServer(e1.toString());
						e1.printStackTrace();
					}
				}catch (IOException e) {
					ServerMain.logToServer(e.toString());
				}
			}
		}
		
		public void logToClient(String s) {
			String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
			sendMsgToClient("11" + timeStamp + "   " + s);
		}
		
		public void sendMsgToClient(String s) {
			try {
				bw.write(s + "\n");
				bw.flush();
			} catch (IOException e) {
				ServerMain.logToServer(e.toString());
				ServerMain.logToServer("Failed to send message to client " + s);
				try {
					clientSocket.close();
					handler.wb.closeBrowser();
					ServerMain.logToServer("Client " + clientSocket.getRemoteSocketAddress() + " has disconnected");
				} catch (IOException e1) {
					ServerMain.logToServer(e.toString());
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			
		}
		
		public void handleMsg(String receivedMsgCode, String receivedMsg) {
			
			switch (receivedMsgCode) {
			case "01":
				email = receivedMsg;
				break;
			case "02":
				password = receivedMsg;
				break;
			case "03":
				kodeAktivasi = receivedMsg;
				if(handler.checkLogin(email, password)) {
					sendMsgToClient("17");
					logToClient("Berhasil masuk ke akun tokopedia anda");
					ServerMain.logToServer("Client " + email + " on " + clientSocket.getRemoteSocketAddress() + " logged in");
					handler.sendStatusFromDB();
					handler.getProducts();
				}else {
					sendMsgToClient("18");
				}
				break;
			case "08":
				namaRek = receivedMsg;
				DB.updateUser(email, "NAMAREKENING", receivedMsg);
				break;
			case "09":
				nomorRek = receivedMsg;
				DB.updateUser(email, "NOMORREKENING", receivedMsg);
				ServerMain.logToServer("CLIENT " + namaRek + " Nomor Rek: " + nomorRek + " MENUNGGU KONFIRMASI PENGISIAN SALDO");
				break;
			case "10":
				handler.beli(receivedMsg);
				ServerMain.logToServer("Client " + email + " on " +  clientSocket.getRemoteSocketAddress() + " membeli paket " + receivedMsg);
				break;
			case "15":
				logToClient("Server sedang memproses produk...");
				handler.getProducts();
				break;
			case "16":
				if(!DB.getExpired(email)) {
					handler.addProducts(receivedMsg);
				}else {
					logToClient("Gagal mengupdate produk, paket anda sudah expired");
				}
				break;
			case "19":
				handler.flushProducts();
				ServerMain.logToServer("Client " + email + "on" + clientSocket.getRemoteSocketAddress() + " has flushed products");
				break;
			case "21":
				handler.wb.pickedOption = receivedMsg;
				break;
			case "22":
				handler.wb.kodeOTP = receivedMsg;
				break;
				
			default:
				break;
			}
		}
		
	}

}