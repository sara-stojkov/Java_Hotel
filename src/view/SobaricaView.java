package view;

import javax.swing.JFrame;
import javax.swing.JTable;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import entities.Soba;
import entities.Sobarica;
import enums.StatusSobe;
import managers.KorisnikManager;
import managers.ManagerFactory;
import managers.SobaManager;

import javax.swing.JScrollPane;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class SobaricaView extends JFrame {
	
    private static final String[] columnNames = {"broj sobe", "tip sobe", "status sobe"};
	private static final long serialVersionUID = 1L;
	private JTable table;
	private JPanel contentPane;
	private ManagerFactory managers;
	private SobaManager sobaMng;
	private KorisnikManager korisnikMng;
	private int preostaloSoba;
	private JLabel brSobaLbl;
	
	public SobaricaView(ManagerFactory mng, String korisnickoImeSobarice) {
		
		this.managers = mng;
		this.sobaMng = mng.getSobaMng();
		this.korisnikMng = mng.getKorisnikMng();
	
		initSobaricaView(korisnickoImeSobarice);
	}


	public void initSobaricaView(String korisnickoIme) {

		Sobarica sobarica = (Sobarica)korisnikMng.dobaviKorisnikaPoUsername(korisnickoIme);
	    Object[][] sobeLista = sobaMng.prikazSoba(sobarica.getListaDodeljenihSoba());

	    setTitle("Sarin hotel - SOBARICA");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBounds(100, 100, 1168, 564);
	    setResizable(false);
	    setIconImage(new ImageIcon("./img/metla.png").getImage());
	    contentPane = new JPanel();
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    contentPane.setLayout(null); 
	    setContentPane(contentPane); 
	    
	    preostaloSoba = sobeLista.length;

	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setBounds(42, 84, 1098, 333);
	    scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    contentPane.add(scrollPane); 
	    
	    JLabel lblNewLabel = new JLabel("Sobe za čišćenje sobarice " + korisnickoIme);
	    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
	    lblNewLabel.setBounds(366, 10, 405, 58);
	    contentPane.add(lblNewLabel); 

	    TableModel modelS = new DefaultTableModel(sobeLista, columnNames) {
	    	static final long serialVersionUID = 1179924601967078430L;
			public Class<?> getColumnClass(int column) {
              Class<?> returnValue;
              if ((column >= 0) && (column < getColumnCount())) {
                returnValue = getValueAt(0, column).getClass();
              } else {
                returnValue = Object.class;
              }
              return returnValue;
            }
          };
	    table = new JTable(modelS);
	    table.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    table.getTableHeader().setOpaque(false);
	    table.getTableHeader().setBackground(Color.LIGHT_GRAY);
	    table.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 20));
	    table.getTableHeader().setReorderingAllowed(false);

	    scrollPane.setViewportView(table);
	    
	    TableRowSorter<TableModel> sorterS = new TableRowSorter<TableModel>(modelS);
        table.setRowSorter(sorterS);

	    JButton confirmCleanBtn = new JButton("Potvrdi očišćenu odabranu sobu");
	    confirmCleanBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
	    confirmCleanBtn.setBounds(100, 436, 400, 58); 
	    contentPane.add(confirmCleanBtn); 
	    
	    confirmCleanBtn.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            int selectedRow = table.getSelectedRow();
	            if (selectedRow != -1) {
	                int confirm = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da je ova soba očišćena i spremna?", "Potvrda",
	                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	                
	                if (confirm == JOptionPane.YES_OPTION) {
	                    int brojSobe = (int)table.getValueAt(selectedRow, 0);
	                    sobaMng.izmeniStatusSobe(brojSobe, StatusSobe.SLOBODNA);
	                    korisnikMng.ukloniSobu(sobarica, brojSobe);
	                    sobaMng.dodajOciscenuSobu(brojSobe, korisnickoIme);
	                    preostaloSoba -= 1;
	                    brSobaLbl.setText("Broj preostalih soba: " + preostaloSoba);

	                    updateTableData(korisnickoIme);
	                } else if (confirm == JOptionPane.NO_OPTION) {
	                    System.out.println("Ništa nije preduzeto.");
	                }
	            } else {
	                JOptionPane.showMessageDialog(null, "Niste izabrali rezervaciju!", "Greška", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    });

	    brSobaLbl = new JLabel("Broj preostalih soba: " + preostaloSoba);
	    brSobaLbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
	    brSobaLbl.setBounds(812, 439, 297, 55);
	    contentPane.add(brSobaLbl); 
	    
	    JButton logoutButton = new JButton("Logout");
	    logoutButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
	    logoutButton.setBounds(992, 21, 111, 42);
	    contentPane.add(logoutButton); 
	    
	    logoutButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            int confirmLogout = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da želite da se izlogujete i vratite na početni ekran?", "Potvrda",
	                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);            
	            if (confirmLogout == JOptionPane.YES_OPTION) {
	                System.out.println("bivate izlogovani");
	                setVisible(false);
	                dispose();
	                StartScreen startScreen = new StartScreen(managers);
	                startScreen.setVisible(true);
	            } else if (confirmLogout == JOptionPane.NO_OPTION) {
	                System.out.println("ipak cete ostati ovde");
	            }
	        }
	    });
	}

	private void updateTableData(String korisnickoIme) {
		Sobarica sobarica = (Sobarica)korisnikMng.dobaviKorisnikaPoUsername(korisnickoIme);
	    Object[][] sobeLista = sobaMng.prikazSoba(sobarica.getListaDodeljenihSoba());	    
	    
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Object[] row : sobeLista) {
            model.addRow(row);
        }
        table.revalidate();
        table.repaint();
	}

}


