package autoPromoClient;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.JPanel;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JTextArea;
import java.awt.SystemColor;
import java.awt.Toolkit;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;


public class Dashboard {

	public JFrame frmDashboard;
	public static Dashboard dashboard;
	public static JLabel notif, saldo, itemPromo, durasi, addNotes, status, namaToko, lblRp_1;
	public static JTextArea log;
	public static JList <CheckListItem> listItem;
	public static DefaultListModel <CheckListItem> listItemModel;

	/**
	 * Launch the application.
	 */
	public static void showDashboard() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					dashboard = new Dashboard();
					dashboard.frmDashboard.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Dashboard() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	@SuppressWarnings("unchecked")
	private void initialize() {
		frmDashboard = new JFrame();
		frmDashboard.setIconImage(Toolkit.getDefaultToolkit().getImage(Dashboard.class.getResource("/autoPromoClient/default_toped-16.png")));
		frmDashboard.getContentPane().setBackground(Color.DARK_GRAY);
		frmDashboard.setBackground(Color.DARK_GRAY);
		frmDashboard.setResizable(false);
		frmDashboard.getContentPane().setForeground(Color.BLACK);
		frmDashboard.setForeground(Color.DARK_GRAY);
		frmDashboard.setTitle("Dashboard");
		frmDashboard.setFont(new Font("Sitka Text", Font.PLAIN, 20));
		frmDashboard.setBounds(100, 100, 800, 560);
		frmDashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDashboard.getContentPane().setLayout(null);
		frmDashboard.setLocationRelativeTo(null);
		frmDashboard.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				ClientMain.closeApp();
			}
		});
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 774, 497);
		frmDashboard.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		tabbedPane.addTab("Overview", null, panel, null);
		panel.setLayout(null);
		
		namaToko = new JLabel("Nama Toko");
		namaToko.setForeground(new Color(0, 128, 128));
		namaToko.setToolTipText("Nama toko anda");
		namaToko.setHorizontalAlignment(SwingConstants.CENTER);
		namaToko.setFont(new Font("Sitka Text", Font.BOLD, 24));
		namaToko.setBounds(153, 11, 386, 39);
		panel.add(namaToko);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 233, 749, 160);
		panel.add(scrollPane_1);
		
		log = new JTextArea();
		scrollPane_1.setViewportView(log);
		log.setBackground(Color.LIGHT_GRAY);
		log.setEditable(false);
		log.setText("Log\r\n");
		DefaultCaret caret = (DefaultCaret) log.getCaret();
		
		JLabel lblStatus = new JLabel("Status");
		lblStatus.setFont(new Font("Sitka Text", Font.BOLD, 20));
		lblStatus.setBounds(10, 66, 145, 39);
		panel.add(lblStatus);
		
		JLabel lblJumlahItem = new JLabel("Item Promo");
		lblJumlahItem.setFont(new Font("Sitka Text", Font.BOLD, 20));
		lblJumlahItem.setBounds(10, 166, 145, 39);
		panel.add(lblJumlahItem);
		
		addNotes = new JLabel("Additional Notes");
		addNotes.setForeground(new Color(169, 169, 169));
		addNotes.setVerticalAlignment(SwingConstants.TOP);
		addNotes.setFont(new Font("Sitka Text", Font.PLAIN, 20));
		addNotes.setBounds(10, 404, 749, 54);
		panel.add(addNotes);
		
		JLabel lblDurasi = new JLabel("Durasi");
		lblDurasi.setFont(new Font("Sitka Text", Font.BOLD, 20));
		lblDurasi.setBounds(10, 116, 145, 39);
		panel.add(lblDurasi);
		
		status = new JLabel("TRIAL");
		status.setHorizontalAlignment(SwingConstants.TRAILING);
		status.setForeground(new Color(47, 79, 79));
		status.setFont(new Font("Sitka Text", Font.BOLD, 20));
		status.setBounds(67, 66, 145, 39);
		panel.add(status);
		
		durasi = new JLabel("3 Hari");
		durasi.setForeground(new Color(47, 79, 79));
		durasi.setFont(new Font("Sitka Text", Font.BOLD, 20));
		durasi.setBounds(153, 116, 145, 39);
		panel.add(durasi);
		
		itemPromo = new JLabel("8 / 10");
		itemPromo.setForeground(new Color(47, 79, 79));
		itemPromo.setFont(new Font("Sitka Text", Font.BOLD, 20));
		itemPromo.setBounds(153, 166, 145, 39);
		panel.add(itemPromo);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(249, 66, 7, 139);
		panel.add(separator);
		
		JLabel lblSaldo = new JLabel("Saldo");
		lblSaldo.setFont(new Font("Sitka Text", Font.BOLD, 20));
		lblSaldo.setBounds(266, 66, 145, 39);
		panel.add(lblSaldo);
		
		saldo = new JLabel("Rp150.000,00");
		saldo.setForeground(new Color(199, 21, 133));
		saldo.setFont(new Font("Sitka Text", Font.BOLD, 20));
		saldo.setBounds(383, 61, 145, 39);
		panel.add(saldo);
		
		JButton btnTambahSaldo = new JButton("Tambah Saldo");
		btnTambahSaldo.setFont(new Font("Sitka Text", Font.BOLD, 18));
		btnTambahSaldo.setBounds(602, 61, 157, 31);
		btnTambahSaldo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String namaRekening = null, nomorRekening = null;
				int i = -1;
					while(i < 0) {
						namaRekening = JOptionPane.showInputDialog(null, new String("Silahkan masukkan nama rekening anda"), 
								"AutoPromo", JOptionPane.QUESTION_MESSAGE);
						if(namaRekening.length() > 0) {
							i++;
						}
					}
					
					while(i <= 0) {
						nomorRekening = JOptionPane.showInputDialog(null, new String("Silahkan masukkan nomor rekening anda"), 
								"AutoPromo", JOptionPane.QUESTION_MESSAGE);
						if(nomorRekening.length() > 0) {
							i++;
						}
					}
					
					while(i <= 1) {
						@SuppressWarnings("unused")
						String jumlahSaldo = JOptionPane.showInputDialog(null, new String("Silahkan jumlah saldo yang ingin ditambahkan"), 
								"AutoPromo", JOptionPane.QUESTION_MESSAGE);
						if(nomorRekening.length() > 0) {
							i++;
						}
					}
				JOptionPane.showMessageDialog(null, new String("Nama rekening: " + namaRekening +
						"\nNomor Rekening: " + nomorRekening + "\nData anda telah dikirim.Silahkan tunggu "
								+ "konfirmasi admin."), "AutoPromo", JOptionPane.INFORMATION_MESSAGE);
				
				JOptionPane.showMessageDialog(null, new String("Pengisian saldo dilakukan dengan cara pengiriman"
						+ " ke Rek. BCA 5930544916 an Julius."), "AutoPromo", JOptionPane.INFORMATION_MESSAGE);
				
				ClientMain.sendMsg("08" + namaRekening);
				ClientMain.sendMsg("09" + nomorRekening);
			}
		});
		panel.add(btnTambahSaldo);
		
		JLabel lblBeliPaketBulanan = new JLabel("Beli paket bulanan");
		lblBeliPaketBulanan.setToolTipText("Untuk paket custom dapat langsung menghubungi admin kami");
		lblBeliPaketBulanan.setFont(new Font("Sitka Text", Font.BOLD, 20));
		lblBeliPaketBulanan.setBounds(266, 116, 201, 39);
		panel.add(lblBeliPaketBulanan);
		
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"", "3 ITEM", "10 ITEM", "SEPUASNYA"}));
		comboBox.setFont(new Font("Sitka Text", Font.BOLD, 18));
		comboBox.setBounds(492, 120, 167, 31);
		comboBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					String pilihan = e.getItem().toString();
					
					switch (pilihan) {
					case "":
						lblRp_1.setText("");
						break;
					case "3 ITEM":
						lblRp_1.setText("Rp30.000,00");
						break;
					case "10 ITEM":
						lblRp_1.setText("Rp50.000,00");
						break;
					case "SEPUASNYA":
						lblRp_1.setText("Rp60.000,00");
						break;

					default:
						break;
					}
					
				}
				
			}
		});
		panel.add(comboBox);
		
		JLabel lblTarif = new JLabel("Tarif");
		lblTarif.setFont(new Font("Sitka Text", Font.BOLD, 20));
		lblTarif.setBounds(266, 166, 145, 39);
		panel.add(lblTarif);
		
		lblRp_1 = new JLabel("");
		lblRp_1.setForeground(new Color(47, 79, 79));
		lblRp_1.setFont(new Font("Sitka Text", Font.BOLD, 20));
		lblRp_1.setBounds(383, 166, 145, 39);
		panel.add(lblRp_1);
		
		JButton btnBeli = new JButton("Beli");
		btnBeli.setFont(new Font("Sitka Text", Font.BOLD, 18));
		btnBeli.setBounds(538, 170, 121, 31);
		btnBeli.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Trial, 3 Produk, 10 Produk, Unlimited
				
				String paketSkrg = status.getText();
				String paketDipilih = comboBox.getSelectedItem().toString();
				int paketSkrgInt = 0;
				int paketDipilihInt = 0;
				int harga = 0;
				
				switch (paketSkrg) {
				case "TRIAL":
					paketSkrgInt = 0;
					break;
				case "3 ITEM":
					paketSkrgInt = 1;
					break;
				case "10 ITEM":
					paketSkrgInt = 2;
					break;
				case "SEPUASNYA":
					paketSkrgInt = 3;
					break;
				default:
					break;
				}
				
				switch (paketDipilih) {
				case "":
					paketDipilihInt = -1;
					break;
				case "3 ITEM":
					paketDipilihInt = 1;
					harga = 30000;
					break;
				case "10 ITEM":
					paketDipilihInt = 2;
					harga = 50000;
					break;
				case "SEPUASNYA":
					paketDipilihInt = 3;
					harga = 60000;
					break;

				default:
					break;
				}
				
				if(paketDipilihInt >= paketSkrgInt) {
					if(Integer.parseInt(saldo.getText().substring(2)) >= harga) {
						ClientMain.sendMsg("10" + paketDipilih);
						ClientMain.pushNotif("Pembelian anda sedang diproses server");
						JOptionPane.showMessageDialog(null, new String("Pembelian anda sedang diproses"), "AutoPromo", 1);
					}else {
						ClientMain.pushNotif("Saldo anda tidak mencukupi");
					}
				}else {
					ClientMain.pushNotif("Tidak dapat memilih paket yang lebih rendah");
				}		
			}
		});
		panel.add(btnBeli);
	
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(267, 103, 492, 2);
		panel.add(separator_1);
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.GRAY);
		tabbedPane.addTab("Produk", null, panel_1, null);
		panel_1.setLayout(null);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(138, 435, 89, 23);
		btnRefresh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientMain.pushNotif("Waiting for server");
				listItemModel.clear();
				ClientMain.sendMsg("15");
				
			}
		});
		panel_1.add(btnRefresh);
		
		JButton btnApply = new JButton("Apply");
		btnApply.setBounds(540, 435, 89, 23);
		btnApply.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientMain.sendMsg("19");
				ClientMain.pushNotif("Sedang diproses...");
				ClientMain.pauseApp(2000);
				for (int i = 0; i < listItem.getModel().getSize(); i++) {
					
					if(listItem.getModel().getElementAt(i).isSelected) {
						ClientMain.sendMsg("16" + listItem.getModel().getElementAt(i).label);
					}
				}
				ClientMain.pushNotif("List updated");
			}
		});
		panel_1.add(btnApply);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.LIGHT_GRAY);
		tabbedPane.addTab("About", null, panel_2, null);
		panel_2.setLayout(null);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setViewportBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_2.setBounds(0, 0, 769, 436);
		panel_2.add(scrollPane_2);
		
		JTextArea txtrTerimaKasihTelah = new JTextArea();
		txtrTerimaKasihTelah.setWrapStyleWord(true);
		txtrTerimaKasihTelah.setText("Terima kasih telah mencoba program AutoPromo Tokopedia.\r\n\r\nProgram AutoPromo akan mempromosikan item anda 1 kali per jam selama 24 jam. Aplikasi ini tidak perlu dibuka terus-menerus untuk berjalan. Cukup memilih produk yang ingin dipromo dan menekan tombol “Apply”. Setelah itu, Anda boleh menutup aplikasi ini dan produk akan tetap di promosikan.\r\n\r\n\r\nOTP\r\nOTP adalah metode verifikasi yang digunakan Tokopedia. Saat keluar 3 pilihan (WA, SMS, Call) untuk melakukan verifikasi, silahkan memilih salah satu metode yang tersedia. Setelah memilih metode verifikasi, masukkan kode verifikasi tersebut. Jika kode belum juga masuk, mohon ditunggu selama 5 menit sampai kode verifikasi tersambungkan.\r\n\r\n\r\nTrial\r\nAnda berikan masa trial selama 3 hari. Dalam masa 3 hari ini, Anda bisa memakai program AutoPromo secara gratis dengan maksimal 3 produk yang di promo. Sebelum masa trial habis Anda bisa melakukan penambahan saldo dan memilih paket yang ingin di pakai.\r\n\r\n\r\nPenambahan Saldo\r\nSetelah anda mengklik tombol tombol “Tambah Saldo”. Silahkan transfer jumlah saldo yang ingin anda tambah ke rekening berikut:\r\nNomor Rekening (BCA): 5930544916\r\nAtas Nama: Julius\r\nPengecekan transfer akan dilakukan sekali per 2x24 jam. Mohon ditunggu untuk perubahan jumlah saldo. Untuk informasi lebih lanjut, silahkan hubungi admin kami dengan WhatsApp 089604058768\r\n\r\n\r\nPaket \r\nTerdapat 3 paket AutoPromo:\r\n3 Produk – Rp. 30.000,-\r\n10 Produk – Rp. 50.000,-\r\nUnlimited – Rp. 60.000,-\r\nKami membuat sistem paket sebagai alat untuk membantu Anda. Jika Anda tidak memiliki banyak produk yang ingin di promo, Anda bisa memilih paket yang sesuai dengan keperluan Anda.\r\n\r\n\r\nPilih Produk\r\nDi tab “Produk” akan muncul daftar produk yang anda jual di toko anda. Silahkan memilih produk yang anda ingin promosikan dan klik tombol “Apply”. Jumlah produk maksimum yang bisa dipilih sesuai dengan paket Anda. Trial membolehkan maksimal 3 produk.\r\n\r\n\r\nJika ada pertanyaan lebih lanjut silahkan menghubungi admin kami: \r\nWhatsApp 089604058768\r\nWhatsApp 081311109955");
		txtrTerimaKasihTelah.setLineWrap(true);
		txtrTerimaKasihTelah.setFont(new Font("Sitka Text", Font.PLAIN, 16));
		txtrTerimaKasihTelah.setEditable(false);
		txtrTerimaKasihTelah.setBackground(SystemColor.activeCaption);
		scrollPane_2.setViewportView(txtrTerimaKasihTelah);
		
		JButton btnTutorialYoutube = new JButton("Tutorial Youtube");
		btnTutorialYoutube.setFont(new Font("Sitka Text", Font.BOLD, 20));
		btnTutorialYoutube.setBounds(505, 435, 264, 34);
		btnTutorialYoutube.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					URL url = new URL("https://www.youtube.com/channel/UC-PDABmGa0VmJrwfAmJV3Jw");
					java.awt.Desktop.getDesktop().browse(url.toURI());
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel_2.add(btnTutorialYoutube);
		
		JLabel lblMasihKurangPaham = new JLabel("Masih kurang paham atau ragu-ragu ? Cek saja ---->>");
		lblMasihKurangPaham.setHorizontalAlignment(SwingConstants.CENTER);
		lblMasihKurangPaham.setFont(new Font("Sitka Text", Font.BOLD, 18));
		lblMasihKurangPaham.setBounds(0, 435, 505, 34);
		panel_2.add(lblMasihKurangPaham);
		
		notif = new JLabel("Notifikasi");
		notif.setForeground(new Color(128, 128, 128));
		notif.setFont(new Font("Sitka Text", Font.PLAIN, 16));
		notif.setBounds(10, 506, 774, 25);
		frmDashboard.getContentPane().add(notif);
		
		listItemModel = new DefaultListModel<CheckListItem>();
		listItem = new JList<CheckListItem>(listItemModel);
		listItem.setCellRenderer(new CheckListRenderer());
		listItem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listItem.setBackground(Color.LIGHT_GRAY);
		listItem.setFont(new Font("Sitka Text", Font.PLAIN, 16));
		listItem.addMouseListener(new MouseAdapter() {
			
			@SuppressWarnings("rawtypes")
			@Override
			public void mouseClicked(MouseEvent event) {
				JList list = (JList) event.getSource();
		        int index = list.locationToIndex(event.getPoint());// Get index of item clicked
		                                                           
		        CheckListItem item = (CheckListItem) list.getModel().getElementAt(index);
		        item.setSelected(!item.isSelected()); // Toggle selected state
		        list.repaint(list.getCellBounds(index, index));// Repaint cell
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(listItem);
		scrollPane.setBounds(10, 57, 749, 367);
		panel_1.add(scrollPane);
		
		JLabel lblDaftarProduk = new JLabel("Daftar Produk");
		lblDaftarProduk.setHorizontalAlignment(SwingConstants.CENTER);
		lblDaftarProduk.setFont(new Font("Sitka Text", Font.BOLD, 20));
		lblDaftarProduk.setBounds(10, 11, 749, 35);
		panel_1.add(lblDaftarProduk);
		
	}
	
	static class CheckListItem {

		  private String label;
		  private boolean isSelected = false;

		  public CheckListItem(String label, boolean isSelected) {
		    this.label = label;
		    this.isSelected = isSelected;
		  }

		  public boolean isSelected() {
		    return isSelected;
		  }

		  public void setSelected(boolean isSelected) {
		    this.isSelected = isSelected;
		  }

		  @Override
		  public String toString() {
		    return label;
		  }
		}
	
	@SuppressWarnings({ "serial", "rawtypes" })
	class CheckListRenderer extends JCheckBox implements ListCellRenderer {
		  public Component getListCellRendererComponent(JList list, Object value,
		      int index, boolean isSelected, boolean hasFocus) {
		    setEnabled(list.isEnabled());
		    setSelected(((CheckListItem) value).isSelected());
		    setFont(list.getFont());
		    setBackground(list.getBackground());
		    setForeground(list.getForeground());
		    setText(value.toString());
		    return this;
		  }
		}
	
	public static void addToList(String name, boolean isSelected) {
		listItemModel.addElement(new CheckListItem(name, isSelected));
		listItem.ensureIndexIsVisible(listItemModel.getSize());
	}
}
