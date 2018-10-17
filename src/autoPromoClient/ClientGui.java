package autoPromoClient;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import java.awt.Toolkit;

public class ClientGui {

	public JFrame frmAutoPromoTokkopedia;
	private JTextField emailField;
	private JPasswordField passwordField;
	private final Action action = new SwingAction();
	
	public static JLabel notif;
	public static ClientGui window;

	/**
	 * Launch the application.
	 */
	public static void showLogin() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new ClientGui();
					window.frmAutoPromoTokkopedia.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAutoPromoTokkopedia = new JFrame();
		frmAutoPromoTokkopedia.setIconImage(Toolkit.getDefaultToolkit().getImage(Dashboard.class.getResource("/autoPromoClient/default_toped-16.png")));
		frmAutoPromoTokkopedia.setResizable(false);
		frmAutoPromoTokkopedia.setTitle("Auto Promo Tokopedia");
		frmAutoPromoTokkopedia.setBounds(100, 100, 665, 205);
		frmAutoPromoTokkopedia.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAutoPromoTokkopedia.getContentPane().setLayout(null);
		frmAutoPromoTokkopedia.setLocationRelativeTo(null);
		frmAutoPromoTokkopedia.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				ClientMain.closeApp();
			}
		});
		
		emailField = new JTextField();
		emailField.setToolTipText("Masukkan alamat email tokopedia anda");
		emailField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		emailField.setBounds(144, 41, 492, 25);
		frmAutoPromoTokkopedia.getContentPane().add(emailField);
		emailField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Email");
		lblNewLabel.setFont(new Font("Sitka Text", Font.BOLD, 18));
		lblNewLabel.setBounds(10, 44, 124, 20);
		frmAutoPromoTokkopedia.getContentPane().add(lblNewLabel);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Sitka Text", Font.BOLD, 18));
		lblPassword.setBounds(10, 75, 124, 20);
		frmAutoPromoTokkopedia.getContentPane().add(lblPassword);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.setAction(action);
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 24));
		btnNewButton.setBounds(489, 90, 130, 40);
		frmAutoPromoTokkopedia.getContentPane().add(btnNewButton);
		
		passwordField = new JPasswordField();
		passwordField.setToolTipText("Masukkan password tokopedia anda");
		passwordField.setBounds(144, 74, 255, 25);
		frmAutoPromoTokkopedia.getContentPane().add(passwordField);
		
		notif = new JLabel("Program started");
		notif.setFont(new Font("Sitka Text", Font.PLAIN, 16));
		notif.setBounds(10, 137, 626, 18);
		frmAutoPromoTokkopedia.getContentPane().add(notif);
		
		JLabel lblSilahkanLoginId = new JLabel("Silahkan login ID Tokopedia anda terlebih dahulu");
		lblSilahkanLoginId.setFont(new Font("Sitka Text", Font.PLAIN, 18));
		lblSilahkanLoginId.setBounds(10, 11, 629, 20);
		frmAutoPromoTokkopedia.getContentPane().add(lblSilahkanLoginId);
	}
	@SuppressWarnings("serial")
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "LOGIN");
			putValue(SHORT_DESCRIPTION, "Login tokopedia");
		}
		public void actionPerformed(ActionEvent e) {
				ClientMain.login(emailField.getText().trim(), String.valueOf(passwordField.getPassword()), "");
		}
	}
	public static void ShowOTPWindow() {
		JFrame pilihanOTP = new JFrame();
		pilihanOTP.setIconImage(Toolkit.getDefaultToolkit().getImage(ClientGui.class.getResource("/autoPromoClient/favicon.ico")));
		pilihanOTP.setResizable(false);
		pilihanOTP.setTitle("Pilihan OTP");
		pilihanOTP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pilihanOTP.getContentPane().setLayout(null);
		pilihanOTP.setLocationRelativeTo(null);
		
		
		JLabel pilihOTP = new JLabel("Silahkan pilih metode verifikasi Tokopedia anda");
		pilihOTP.setFont(new Font("Sitka Text", Font.PLAIN, 18));
		pilihanOTP.getContentPane().add(pilihOTP);

		JButton buttonWa = new JButton("WhatsApp");
		buttonWa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ClientMain.sendMsg("21wa");
				pilihanOTP.setVisible(false);
				String kode = "";
				while(kode.length() < 1) {
					kode = JOptionPane.showInputDialog(null, new String("Silahkan masukkan kode verifikasi Anda"), "AutoPromo", JOptionPane.PLAIN_MESSAGE);
				}
				ClientMain.sendMsg("22" + kode);
				pilihanOTP.dispose();
			}
		});
		
		JButton buttonSMS = new JButton("SMS");
		buttonSMS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ClientMain.sendMsg("21sms");
				pilihanOTP.setVisible(false);
				String kode = "";
				while(kode.length() < 1) {
					kode = JOptionPane.showInputDialog(null, new String("Silahkan masukkan kode verifikasi Anda"), "AutoPromo", JOptionPane.PLAIN_MESSAGE);
				}
				ClientMain.sendMsg("22" + kode);
				pilihanOTP.dispose();
			}
		});
		
		JButton buttonCall = new JButton("Telepon");
		buttonCall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ClientMain.sendMsg("21call");
				pilihanOTP.setVisible(false);
				String kode = "";
				while(kode.length() < 1) {
					kode = JOptionPane.showInputDialog(null, new String("Silahkan masukkan kode verifikasi Anda"), "AutoPromo", JOptionPane.PLAIN_MESSAGE);
				}
				ClientMain.sendMsg("22" + kode);
				pilihanOTP.dispose();
			}
		});
		pilihanOTP.add(buttonWa);
		pilihanOTP.add(buttonSMS);
		pilihanOTP.add(buttonCall);
		pilihanOTP.pack();
		pilihanOTP.setVisible(true);
	}
}
