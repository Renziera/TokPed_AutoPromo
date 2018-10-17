package autoPromoServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DB {
	
	static Statement database, database1, database2, databaseExt;
	static Connection conn;
	
	public static void setUpDB() {
		
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection("jdbc:h2:./AutoPromoDB", "admin", "admin");
			database = conn.createStatement();
			database1 = conn.createStatement();
			database2 = conn.createStatement();
			databaseExt = conn.createStatement();
			ServerMain.logToServer("Database connection established");
		} catch (ClassNotFoundException e1) {
			ServerMain.logToServer(e1.toString());
			e1.printStackTrace();
		} catch (SQLException e) {
			ServerMain.logToServer(e.toString());
			e.printStackTrace();
		}
	}
	
	public static void releaseDB() {
		try {
			conn.close();
			ServerMain.logToServer("Database connection closed");
		} catch (SQLException e) {
			ServerMain.logToServer(e.toString());
			e.printStackTrace();
		}
	}
	
	public static void addUser(String username, String password) {
			try {
				database.executeUpdate("INSERT INTO USER (EMAIL, PASSWORD) VALUES ('" + username + "', '" + password + "')");
			} catch (SQLException e) {
				ServerMain.logToServer(e.toString());
				e.printStackTrace();
			}
	}
	
	public static boolean checkUserExist(String username) {
		try {
			ResultSet rs = database.executeQuery("SELECT EMAIL FROM USER WHERE EMAIL = '" + username + "'");
			return rs.next();
		} catch (SQLException e) {
			ServerMain.logToServer(e.toString());
			e.printStackTrace();
		}
		return false;
	}
	
	public static void updateUser(String username, String whatToUpdate, String value) {
		
		try {
			database.executeUpdate("UPDATE USER SET " + whatToUpdate + " = '" + value + "' WHERE EMAIL = '" + username + "'");
		} catch (SQLException e) {
			ServerMain.logToServer(e.toString());
			e.printStackTrace();
		}	
	}
	
	public static void updateUserInt(String username, String whatToUpdate, int value) {
		
		try {
			database.executeUpdate("UPDATE USER SET " + whatToUpdate + " = " + value + " WHERE EMAIL = '" + username + "'");
		} catch (SQLException e) {
			ServerMain.logToServer(e.toString());
			e.printStackTrace();
		}	
	}
	
	public static String getUserValue(String username, String whatToGet) {
		
		try {
			ResultSet rs = database.executeQuery("SELECT " + whatToGet + " FROM USER WHERE EMAIL = '" + username + "'");
			rs.next();
			return rs.getString(whatToGet);
		} catch (SQLException e) {
			ServerMain.logToServer(e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	public static int getUserValueInt(String username, String whatToGet) {
		
		try {
			ResultSet rs = database.executeQuery("SELECT " + whatToGet + " FROM USER WHERE EMAIL = '" + username + "'");
			rs.next();
			return rs.getInt(whatToGet);
		} catch (SQLException e) {
			ServerMain.logToServer(e.toString());
			e.printStackTrace();
		}
		return 0;
	}
	
	public static List<String> getAllUser() {
		List<String> userList = new ArrayList<>();
		ResultSet rs;
		try {
			rs = database.executeQuery("SELECT EMAIL FROM USER");
			while(rs.next()) {
				userList.add(rs.getString("EMAIL"));
			}
		} catch (SQLException e) {
			ServerMain.logToServer(e.toString());
			e.printStackTrace();
		}
		return userList;
	}
	
	public static void addProduct(String username, String product, String link) {
		
			try {
				database.executeUpdate("INSERT INTO PRODUK (EMAIL, NAMAPRODUK, LINKPRODUK) VALUES ('" + username +
						"', '" + product + "', '" + link + "')");
			} catch (SQLException e) {
				ServerMain.logToServer(e.toString());
				e.printStackTrace();
			}
	}
	
	public static List<String> getProductsLink(String username) {
		ResultSet rs;
		List<String> links = new ArrayList<String>();
		try {
			rs = database.executeQuery("SELECT LINKPRODUK FROM PRODUK WHERE EMAIL = '" + username + "'");
			while(rs.next()) {
				links.add(rs.getString("LINKPRODUK"));
			}
		} catch (SQLException e) {
			ServerMain.logToServer(e.toString());
			e.printStackTrace();
		}
		return links;
	}
	
	public static List<String> getProductsName(String username) {
		ResultSet rs;
		List<String> links = new ArrayList<String>();
		try {
			rs = database.executeQuery("SELECT NAMAPRODUK FROM PRODUK WHERE EMAIL = '" + username + "'");
			while(rs.next()) {
				links.add(rs.getString("NAMAPRODUK"));
			}
		} catch (SQLException e) {
			ServerMain.logToServer(e.toString());
			e.printStackTrace();
		}
		return links;
	}
	
	public static void flushProducts(String username) {
		try {
			database.executeUpdate("DELETE FROM PRODUK WHERE EMAIL = '" + username + "'");
			database.executeUpdate("UPDATE USER SET ITEMPROMO = 0 WHERE EMAIL = '" + username + "'");
		} catch (SQLException e) {
			ServerMain.logToServer(e.toString());
			e.printStackTrace();
		}
	}
	
	public static int numberOfProducts(String username) {
		ResultSet rs;
		int amount = 0;
		try {
			rs = database.executeQuery("SELECT * FROM PRODUK WHERE EMAIL = '" + username + "'");
			while(rs.next()) {
				amount++;
			}
		} catch (SQLException e) {
			ServerMain.logToServer(e.toString());
			e.printStackTrace();
		}
		return amount;
	}
	
	public static void executeCommand(String command) {
		try {
			databaseExt.executeUpdate(command);
			ServerMain.logToServer("SQL Query executed : " + command );
		} catch (SQLException e) {
			ServerMain.logToServer("Failed to run SQL Query : " + command + e.toString());
			e.printStackTrace();
		}
	}
	
	public static void stopAutoPromo() {
		Runnable run = () -> {
			ResultSet rs;
			while(true) {
				try {
					Thread.sleep(7200000);
					rs = database1.executeQuery("SELECT EMAIL FROM USER");
					while(rs.next()) {
						String username = rs.getString("EMAIL");
						System.out.println("checking username: " + username);
						if(getExpired(username)) {
							BrowserManager.removeUser(username);							
						}
					}
				} catch (SQLException | InterruptedException e) {
					ServerMain.logToServer(e.toString());
					e.printStackTrace();
				}
			}
		};
		Thread thread = new Thread(run);
		thread.start();
	}
	
	
	public static Boolean getExpired(String username) {
		ResultSet rs;
		try {
			rs =  database2.executeQuery("SELECT DURASI FROM USER WHERE EMAIL = '" + username + "'");
			rs.next();
			int deadline = rs.getInt("DURASI");
			int currentDate = Integer.parseInt(new SimpleDateFormat("yyMMdd").format(new Date()));
			if(deadline < currentDate) {
				return true;
			}
		} catch (SQLException e) {
			ServerMain.logToServer(e.toString());
			e.printStackTrace();
		}
		return false;
	}
}
