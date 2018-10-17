package autoPromoServer;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultCaret;

import javax.swing.JButton;

public class ServerMain {

	private JFrame frmServerAutopromo;
	private static JTextField Sqlcommand;
	private static JTextArea txtrServerlog;
	private JButton btnDisconnectDatabase;

	/**
	 * Launch the application.
	 * This server program made with pure Java 8 JDK 1.8
	 * in Eclipse Luna with Standard JRE libraries and
	 * java.Socket for connection
	 * H2 embedded SQL database
	 * more info stanleyheryanto@Gmail.com
	 * 
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			ServerMain.logToServer(e1.toString());
			e1.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerMain window = new ServerMain();
					window.frmServerAutopromo.setVisible(true);
				} catch (Exception e) {
					ServerMain.logToServer(e.toString());
					e.printStackTrace();
				}
			}
		});
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		ServerMain.logToServer("Tokopedia AutoPromo server initializing...");
		
		DB.setUpDB();
		DB.stopAutoPromo();
		NetworkComm.setUpNetwork();
		
		//load users from DB and select unexpired ones to run
		
		List<String> userList = new ArrayList<String>(DB.getAllUser());
		List<String> legitUser = new ArrayList<String>();
		
		for(String user : userList) {
			if(!DB.getExpired(user)) {
				legitUser.add(user);
			}
		}
		
		for(String user : legitUser) {
			BrowserManager.add(new User(user, DB.getUserValue(user, "PASSWORD"), DB.getProductsLink(user)));
		}
		
		logToServer("Server has finished initializing");
	}

	/**
	 * Create the application.
	 */
	public ServerMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */	
	private void initialize() {
		frmServerAutopromo = new JFrame();
		frmServerAutopromo.setResizable(false);
		frmServerAutopromo.setTitle("Server AutoPromo");
		frmServerAutopromo.setBounds(100, 100, 632, 528);
		frmServerAutopromo.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmServerAutopromo.getContentPane().setLayout(null);
		frmServerAutopromo.setLocationRelativeTo(null);
		frmServerAutopromo.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				DB.releaseDB();
				NetworkComm.t.closeServerSocket();
				System.exit(0);
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 596, 391);
		frmServerAutopromo.getContentPane().add(scrollPane);
		
		txtrServerlog = new JTextArea();
		txtrServerlog.setText("ServerLog\r\n");
		txtrServerlog.setFont(new Font("Monospaced", Font.PLAIN, 16));
		txtrServerlog.setEditable(false);
		txtrServerlog.setLineWrap(true);
		scrollPane.setViewportView(txtrServerlog);
		DefaultCaret caret = (DefaultCaret) txtrServerlog.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		Sqlcommand = new JTextField();
		Sqlcommand.setBounds(10, 413, 596, 27);
		frmServerAutopromo.getContentPane().add(Sqlcommand);
		Sqlcommand.setColumns(10);
		
		JButton btnRunSql = new JButton("RUN SQL");
		btnRunSql.setBounds(438, 451, 90, 30);
		btnRunSql.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = Sqlcommand.getText().trim();
				Sqlcommand.setText("");
				if(btnDisconnectDatabase.getText().equals("Disconnect Database")) {
					if(!command.equals("")) {
						logToServer("SQL Query : " + command);
						DB.executeCommand(command);
					}	
				}else {
					logToServer("Failed to run SQL Query " + command +", database not connected");
				}
				
			}
		});
		frmServerAutopromo.getContentPane().add(btnRunSql);
		
		btnDisconnectDatabase = new JButton("Disconnect Database");
		btnDisconnectDatabase.setBounds(10, 451, 140, 30);
		btnDisconnectDatabase.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(btnDisconnectDatabase.getText().equals("Disconnect Database")) {
					DB.releaseDB();
					btnDisconnectDatabase.setText("Connect Database");
					logToServer("Database disconnected, try to reconnect ASAP");
				}else if(btnDisconnectDatabase.getText().equals("Connect Database")) {
					DB.setUpDB();
					btnDisconnectDatabase.setText("Disconnect Database");
					logToServer("Database reconnected successfuly");
				}
			}
		});
		frmServerAutopromo.getContentPane().add(btnDisconnectDatabase);
	}
	
	public static void logToServer(String log) {
		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
		txtrServerlog.append(timeStamp + " " + log + System.getProperty("line.separator")); 
		
		if(txtrServerlog.getText().length() > 9999) {
			try {
				PrintWriter out = new PrintWriter(new FileOutputStream(new File("ServerLog.txt"), true));
				out.println(txtrServerlog.getText());
				out.close();
				txtrServerlog.setText("");
			} catch (FileNotFoundException e) {
				logToServer("Failed to dump server log");
				e.printStackTrace();
			}
		}
		
	}
}
