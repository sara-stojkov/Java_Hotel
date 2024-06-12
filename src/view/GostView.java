package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import entities.Gost;
import entities.Rezervacija;
import entities.TipSobe;

import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import enums.StatusRezervacije;
import managers.CenovnikManager;
import managers.DodatneUslugeManager;
import managers.ManagerFactory;
import managers.RezervacijaManager;
import managers.TipSobeManager;
import view.addEdit.RezervacijaAddEditDialog;

public class GostView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private static final String[] columnNames = {"id", "tip sobe", "datum pocetka", "datum kraja", "cena", "status"};
    private RezervacijaManager rezMng;
    private DodatneUslugeManager dodatneUslugeMng;
    private TipSobeManager tipoviSobaMng;
    private CenovnikManager cenovnikMng;
    private ManagerFactory managers;
    private double ukupanTrosak;
    private JLabel totalCostLabel;
    
    public GostView(ManagerFactory managers,String korisnickoImeGosta) {
        this.managers = managers;
    	this.rezMng = managers.getRezervacijaMng();
        this.dodatneUslugeMng = managers.getDodatneUslugeMng();
        tipoviSobaMng = managers.getTipSobeMng();
        this.cenovnikMng = managers.getCenovnikMng();
        ArrayList <Gost> gosti = managers.getKorisnikMng().getGosti();
        ArrayList <TipSobe> sobe = tipoviSobaMng.getTipoviSoba();
        initGostView(korisnickoImeGosta, rezMng, gosti);
    }

    public void initGostView(String korisnickoIme, RezervacijaManager rezMng, ArrayList<Gost> gosti) {
        ArrayList<Rezervacija> rezervacijeKorisnikaAL = rezMng.nadjiRezervacijeGosta(korisnickoIme);
        Object[][] rezervacijeKorisnika = rezMng.prikazRezGostu(rezervacijeKorisnikaAL);
        String[] dostupneUsluge = dodatneUslugeMng.uslugeStringLista();
        ukupanTrosak = rezMng.izracunajCenuKorisniku(korisnickoIme);

        setTitle("Sarin hotel - GOST");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1168, 564);
        setResizable(false);
        setIconImage(new ImageIcon("./img/gost.png").getImage());
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("Rezervacije korisnika " + korisnickoIme);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 24));
        lblTitle.setBounds(347, 20, 409, 42);
        contentPane.add(lblTitle);

        JButton addReservationButton = new JButton("Dodaj rezervaciju");
        addReservationButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
        addReservationButton.setBounds(57, 458, 267, 42);
        contentPane.add(addReservationButton);
        
        addReservationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RezervacijaAddEditDialog rez = new RezervacijaAddEditDialog(GostView.this, managers, null, korisnickoIme, dostupneUsluge, 0);
                rez.setVisible(true);
                if (rez.proveriUspesnost()) {
                    ukupanTrosak += rezMng.getRezervacije().get(rezMng.getRezervacije().size() - 1).getCena();
                    totalCostLabel.setText("Ukupan trošak: " + ukupanTrosak);
                    updateTableData(korisnickoIme);
                }
            }
        });

        
        
        JButton cancelReservationButton = new JButton("Otkaži odabranu rezervaciju");
        cancelReservationButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
        cancelReservationButton.setBounds(419, 458, 307, 42);
        contentPane.add(cancelReservationButton);

        cancelReservationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String idString = String.valueOf(table.getValueAt(selectedRow, 0));
                    if (rezMng.dobaviRezervacijuPoId(idString).getStatusRezervacije() != StatusRezervacije.NA_ČEKANjU) {
                        JOptionPane.showMessageDialog(null, "Samo rezervacije NA ČEKANjU mogu da se otkažu!", "Greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int confirm = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da želite da otkažete ovu rezervaciju?", "Potvrda",
                            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
						rezMng.promeniStatusRezervacije(idString, StatusRezervacije.OTKAZANA);

						updateTableData(korisnickoIme);
                    } else if (confirm == JOptionPane.NO_OPTION) {
                        System.out.println("Ništa nije preduzeto.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Niste izabrali rezervaciju!", "Greška", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

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
            	}
            	else if (confirmLogout == JOptionPane.NO_OPTION) {
            		System.out.println("ipak cete ostati ovde");
            	}
            }
         });

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(25, 99, 1101, 336);
        contentPane.add(scrollPane);
        
        TableModel model = new DefaultTableModel(rezervacijeKorisnika, columnNames) {
            private static final long serialVersionUID = 5668735439825556971L;

            @Override
            public Class<?> getColumnClass(int column) {
                if (getRowCount() == 0) {
                    return Object.class;
                }
                return getValueAt(0, column).getClass();
            }

        };
          
        table = new JTable(model);
        table.setFont(new Font("Tahoma", Font.PLAIN, 18));
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(Color.LIGHT_GRAY);
        table.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 20));
        table.getTableHeader().setReorderingAllowed(false);
        scrollPane.setViewportView(table);
        
        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        table.setRowSorter(sorter);
        
        totalCostLabel = new JLabel("Ukupan trošak: " + ukupanTrosak);
        totalCostLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        totalCostLabel.setBounds(818, 458, 285, 42);
        contentPane.add(totalCostLabel);
    }

    private void updateTableData(String korisnickoIme) {
        ArrayList<Rezervacija> rezervacijeKorisnikaAL = rezMng.nadjiRezervacijeGosta(korisnickoIme);
        Object[][] updatedReservations = rezMng.prikazRezGostu(rezervacijeKorisnikaAL);
        DefaultTableModel modelR = (DefaultTableModel) table.getModel();
        
        modelR.setRowCount(0);
        for (Object[] row : updatedReservations) {
            modelR.addRow(row);
        }
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) table.getRowSorter();
        table.setRowSorter(null); 
        table.setRowSorter(sorter); 
        table.revalidate();
        table.repaint();
    }

}
