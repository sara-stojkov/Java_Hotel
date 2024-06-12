package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import entities.Rezervacija;
import enums.StatusRezervacije;
import enums.StatusSobe;
import managers.CenovnikManager;
import managers.DodatneUslugeManager;
import managers.KorisnikManager;
import managers.ManagerFactory;
import managers.RezervacijaManager;
import managers.SobaManager;
import managers.TipSobeManager;
import view.addEdit.CheckInDialog;
import view.addEdit.GostAddEditDialog;
import view.addEdit.RezervacijaAddEditDialog;

public class RecepcionerView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tableR;
    private JTable tableS;
    private JTable tableG;
    private static final String[] columnNamesRez = {"id", "korisnicko ime", "tip sobe", "datum pocetka", "datum kraja", "cena", "status", "soba"};
    private static final String[] columnNamesRooms = {"broj sobe", "tip sobe", "status sobe"};
    private static final String[] columnNamesGuest = {"korisnicko ime", "ime", "prezime", "pol", "datum rođenja", "broj telefona"};
    private RezervacijaManager rezMng;
    private DodatneUslugeManager dodatneUslugeMng;
    private TipSobeManager tipoviSobaMng;
    private CenovnikManager cenovnikMng;
    private ManagerFactory managers;
	private SobaManager sobeMng;
	private KorisnikManager korisnikMng;
	private JTable dolasciTable;
	private JTable odlasciTable;
	private JTable dolasciOdlasciTable;

    public RecepcionerView(ManagerFactory managers, String korisnickoImeGosta) {
        this.managers = managers;
        this.rezMng = managers.getRezervacijaMng();
        this.dodatneUslugeMng = managers.getDodatneUslugeMng();
        this.tipoviSobaMng = managers.getTipSobeMng();
        this.cenovnikMng = managers.getCenovnikMng();
        this.sobeMng = managers.getSobaMng();
        this.korisnikMng = managers.getKorisnikMng();
        initRecepcionerView(korisnickoImeGosta);
    }

    public void initRecepcionerView(String korisnickoIme) {
        setTitle("Sarin hotel - RECEPCIONER");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1400, 600);
        setResizable(true);
        setIconImage(new ImageIcon("./img/recepcija.png").getImage());

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        
     
        // Panel za REZERVACIJE
        JPanel rezervacijePanel = new JPanel();
        rezervacijePanel.setLayout(new BorderLayout());
        tabbedPane.addTab("Rezervacije", null, rezervacijePanel, null);

        Object[][] listaRezervacija = rezMng.prikazRez(rezMng.getRezervacije());

        JLabel lblTitleR= new JLabel("Rezervacije");
        lblTitleR.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitleR.setFont(new Font("Tahoma", Font.PLAIN, 24));
        rezervacijePanel.add(lblTitleR, BorderLayout.NORTH);

        JScrollPane scrollPaneR = new JScrollPane();
        TableModel modelR = new DefaultTableModel(listaRezervacija, columnNamesRez) {
			private static final long serialVersionUID = 2800776531457097506L;

			@Override
            public Class<?> getColumnClass(int column) {
                if (getRowCount() == 0) {
                    return Object.class;
                }
                return getValueAt(0, column).getClass();
            }
          };        
        tableR = new JTable(modelR);
        tableR.setFont(new Font("Tahoma", Font.PLAIN, 18));
        tableR.getTableHeader().setOpaque(false);
        tableR.getTableHeader().setBackground(Color.LIGHT_GRAY);
        tableR.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 20));
        tableR.getTableHeader().setReorderingAllowed(false);
        scrollPaneR.setViewportView(tableR);
        rezervacijePanel.add(scrollPaneR, BorderLayout.CENTER);
        
        TableRowSorter<TableModel> sorterR = new TableRowSorter<>(modelR);
        tableR.setRowSorter(sorterR);

        JPanel rezervacijeButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 25)); 
        rezervacijeButtonPanel.add(createButton("Dodaj rezervaciju", e -> addReservationAction()));
        rezervacijeButtonPanel.add(createButton("Izmeni odabranu rezervaciju", e -> editReservationAction()));
        rezervacijeButtonPanel.add(createButton("Potvrdi rezervaciju", e -> confirmReservationAction()));
        rezervacijeButtonPanel.add(createButton("Odbij rezervaciju", e -> denyReservationAction()));
        rezervacijeButtonPanel.add(createButton("Check in", e -> checkInAction()));
        rezervacijeButtonPanel.add(createButton("Check out", e -> checkOutAction()));
        rezervacijePanel.add(rezervacijeButtonPanel, BorderLayout.SOUTH);


        // Panel za SOBE
        JPanel sobePanel = new JPanel();
        sobePanel.setLayout(new BorderLayout());
        tabbedPane.addTab("Sobe", null, sobePanel, null);

        Object[][] listaSoba = sobeMng.prikazSoba(sobeMng.getSobe());

        JLabel lblTitleS = new JLabel("Sobe");
        lblTitleS.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitleS.setFont(new Font("Tahoma", Font.PLAIN, 24));
        sobePanel.add(lblTitleS, BorderLayout.NORTH);

        JScrollPane scrollPaneS = new JScrollPane();
        TableModel modelS = new DefaultTableModel(listaSoba, columnNamesRooms) {
			private static final long serialVersionUID = -6502737823538104070L;
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
        tableS = new JTable(modelS);
        tableS.setFont(new Font("Tahoma", Font.PLAIN, 18));
        tableS.getTableHeader().setOpaque(false);
        tableS.getTableHeader().setBackground(Color.LIGHT_GRAY);
        tableS.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 20));
        tableS.getTableHeader().setReorderingAllowed(false);
        scrollPaneS.setViewportView(tableS);
        sobePanel.add(scrollPaneS, BorderLayout.CENTER);
        
        TableRowSorter<TableModel> sorterS = new TableRowSorter<>(modelS);
        tableS.setRowSorter(sorterS);

        JPanel sobeButtonPanel = new JPanel();
        sobePanel.add(sobeButtonPanel, BorderLayout.SOUTH);

        // Panel za GOSTE
        JPanel gostiPanel = new JPanel();
        gostiPanel.setLayout(new BorderLayout());
        tabbedPane.addTab("Gosti", null, gostiPanel, null);

        Object[][] listaGostiju = korisnikMng.formatirajGoste();

        JLabel lblTitleG = new JLabel("Gosti");
        lblTitleG.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitleG.setFont(new Font("Tahoma", Font.PLAIN, 24));
        gostiPanel.add(lblTitleG, BorderLayout.NORTH);

        JScrollPane scrollPaneG = new JScrollPane();
        TableModel modelG = new DefaultTableModel(listaGostiju, columnNamesGuest) {
			private static final long serialVersionUID = 3421467595468045319L;
			@Override
			public Class<?> getColumnClass(int columnIndex) {
			    if (getRowCount() == 0) {
			        return Object.class;
			    } else {
			        return getValueAt(0, columnIndex).getClass();
			    }
            }
          };
        tableG = new JTable(modelG);
        tableG.setFont(new Font("Tahoma", Font.PLAIN, 18));
        tableG.getTableHeader().setOpaque(false);
        tableG.getTableHeader().setBackground(Color.LIGHT_GRAY);
        tableG.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 20));
        tableG.getTableHeader().setReorderingAllowed(false);
        scrollPaneG.setViewportView(tableG);
        gostiPanel.add(scrollPaneG, BorderLayout.CENTER);
        
        TableRowSorter<TableModel> sorterG = new TableRowSorter<>(modelG);
        tableG.setRowSorter(sorterG);

        JPanel gostiButtonPanel = new JPanel();
        gostiButtonPanel.add(createButton("Dodaj gosta", e -> addGuestAction()));
        gostiPanel.add(gostiButtonPanel, BorderLayout.SOUTH);
        
        
        // PREGLED DOLAZAKA I ODLAZAKA
        JPanel dolasciOdlasciPanel = new JPanel();
        dolasciOdlasciPanel.setLayout(new BorderLayout());
        tabbedPane.addTab("Dolasci & odlasci", null, dolasciOdlasciPanel, null);
        
        JTabbedPane tabbedPaneDiO = new JTabbedPane(JTabbedPane.LEFT);
        tabbedPaneDiO.setBounds(25, 500, 1100, 300);
        dolasciOdlasciPanel.add(tabbedPaneDiO, BorderLayout.CENTER);

        JPanel dolasciPanel = new JPanel(new BorderLayout());
        tabbedPaneDiO.addTab("Očekivani dolasci", null, dolasciPanel, null);

        Object[][] listaOcekivanihDolazaka = rezMng.prikazRez(rezMng.dobaviOcekivano(StatusRezervacije.POTVRĐENA));
        dolasciTable = new JTable(new DefaultTableModel(listaOcekivanihDolazaka, columnNamesRez));
        dolasciPanel.add(new JScrollPane(dolasciTable), BorderLayout.CENTER);

        JPanel odlasciPanel = new JPanel(new BorderLayout());
        tabbedPaneDiO.addTab("Očekivani odlasci", null, odlasciPanel, null);

        Object[][] listaOcekivanihOdlazaka = rezMng.prikazRez(rezMng.dobaviOcekivano(StatusRezervacije.U_TOKU));
        odlasciTable = new JTable(new DefaultTableModel(listaOcekivanihOdlazaka, columnNamesRez));
        odlasciPanel.add(new JScrollPane(odlasciTable), BorderLayout.CENTER);

        JPanel danasnjePromenePanel = new JPanel(new BorderLayout());
        tabbedPaneDiO.addTab("Dolasci i odlasci", null, danasnjePromenePanel, null);

        Object[][] listaDolazakaIOdlazaka = rezMng.prikazRez(rezMng.getDanasnjePromeneRez());
        dolasciOdlasciTable = new JTable(new DefaultTableModel(listaDolazakaIOdlazaka, columnNamesRez));
        danasnjePromenePanel.add(new JScrollPane(dolasciOdlasciTable), BorderLayout.CENTER);
   

        // Logout dugme
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        logoutButton.addActionListener(e -> logout());
        logoutPanel.add(logoutButton);
        contentPane.add(logoutPanel, BorderLayout.NORTH);
    }

    private void checkInAction() {
    	int selectedRow = tableR.getSelectedRow();
        if (selectedRow != -1) {
            String idString = String.valueOf(tableR.getValueAt(selectedRow, 0));
            Rezervacija odabranaRez = rezMng.dobaviRezervacijuPoId(idString);
            if (!odabranaRez.getDatumPocetka().equals(LocalDate.now())) {
                JOptionPane.showMessageDialog(null, "Check-in se može izvršiti tek na dan početka rezervacije!", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (odabranaRez.getStatusRezervacije() != StatusRezervacije.POTVRĐENA) {
                JOptionPane.showMessageDialog(null, "Check-in se može izvršiti samo za POTVRĐENE rezervacije!", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }
            CheckInDialog.showCheckInDialog(RecepcionerView.this, managers, idString);
            updateTableDataRez();
            updateTableDataSoba();
            updateTableDataDolasci();
            updateTableDataDanasnje();
        } else {
            JOptionPane.showMessageDialog(null, "Morate izabrati registraciju gosta koji vrši check-in!", "Greška", JOptionPane.ERROR_MESSAGE);
        }	
    }

	private void checkOutAction() {
		int selectedRow = tableR.getSelectedRow();
        if (selectedRow != -1) {
            String idString = String.valueOf(tableR.getValueAt(selectedRow, 0));
            if (rezMng.dobaviRezervacijuPoId(idString).getStatusRezervacije() != StatusRezervacije.U_TOKU) {
                JOptionPane.showMessageDialog(null, "Samo gosti sa rezervacijama koje su trenutno U TOKU mogu da izvrše check-out!", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int confirmLogout = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da želite da izvršite check-out?", "Potvrda",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (confirmLogout == JOptionPane.YES_OPTION) {
            	rezMng.promeniStatusRezervacije(idString, StatusRezervacije.PROŠLA);
            	rezMng.dobaviRezervacijuPoId(idString).getSoba().setStatusSobe(StatusSobe.SPREMANjE);
            	korisnikMng.dodeliSobuZaCiscenje(String.valueOf(rezMng.dobaviRezervacijuPoId(idString).getSoba().getBrojSobe()));
                JOptionPane.showMessageDialog(null, "Uspešno je izvršen check-out!", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
                updateTableDataRez();
                updateTableDataSoba();
                updateTableDataOdlasci();
                updateTableDataDanasnje();
            }
            updateTableDataRez();
        } else {
            JOptionPane.showMessageDialog(null, "Morate izabrati registraciju gosta koji vrši check-out!", "Greška", JOptionPane.ERROR_MESSAGE);
        }
		
	}


	private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Tahoma", Font.PLAIN, 20));
        button.addActionListener(actionListener);
        return button;
    }

    private void editReservationAction() {
        int selectedRow = tableR.getSelectedRow();
        if (selectedRow != -1) {
            String idString = String.valueOf(tableR.getValueAt(selectedRow, 0));
            Rezervacija odabranaRezervacija = rezMng.dobaviRezervacijuPoId(idString);
            if (odabranaRezervacija.getStatusRezervacije() == StatusRezervacije.PROŠLA || odabranaRezervacija.getStatusRezervacije() == StatusRezervacije.OTKAZANA || odabranaRezervacija.getStatusRezervacije() == StatusRezervacije.ODBIJENA) {
                JOptionPane.showMessageDialog(null, "Ova rezervacija nije podložna izmeni!", "Greška", JOptionPane.ERROR_MESSAGE);
                return;

            }
            RezervacijaAddEditDialog rez = new RezervacijaAddEditDialog(RecepcionerView.this, managers, odabranaRezervacija, odabranaRezervacija.getImeRezervacije().getKorisnickoIme(), dodatneUslugeMng.uslugeStringLista(), 1);
            rez.setVisible(true);
            updateTableDataRez();
        } else {
            JOptionPane.showMessageDialog(null, "Niste izabrali rezervaciju!", "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmReservationAction() {
        int selectedRow = tableR.getSelectedRow();
        if (selectedRow != -1) {
            String idString = String.valueOf(tableR.getValueAt(selectedRow, 0));
            if (rezMng.dobaviRezervacijuPoId(idString).getStatusRezervacije() != StatusRezervacije.NA_ČEKANjU) {
                JOptionPane.showMessageDialog(null, "Samo registracije sa statusom NA ČEKANjU mogu biti potvrđene.", "Greška", JOptionPane.ERROR_MESSAGE);
                return;

            }
            if (rezMng.validirajPotvrduRezervacije(idString)) {
            	rezMng.promeniStatusRezervacije(idString, StatusRezervacije.POTVRĐENA);
                JOptionPane.showMessageDialog(null, "Uspešno potvrđena rezervacija", "Uspeh!", JOptionPane.INFORMATION_MESSAGE);
                updateTableDataRez();
                updateTableDataDolasci();

            } else {
                JOptionPane.showMessageDialog(null, "Nažalost, ova rezervacija ne može biti potvrđena jer nemamo slobodnih soba za taj period.", "Greška", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Niste izabrali rezervaciju!", "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void denyReservationAction() {
        int selectedRow = tableR.getSelectedRow();
        if (selectedRow != -1) {
            String idString = String.valueOf(tableR.getValueAt(selectedRow, 0));
            if (rezMng.dobaviRezervacijuPoId(idString).getStatusRezervacije() != StatusRezervacije.NA_ČEKANjU) {
                JOptionPane.showMessageDialog(null, "Samo registracije sa statusom NA ČEKANjU mogu biti odbijene.", "Greška", JOptionPane.ERROR_MESSAGE);
                return;

            }
            if (!rezMng.validirajPotvrduRezervacije(idString)) {
            	rezMng.promeniStatusRezervacije(idString, StatusRezervacije.ODBIJENA);
                JOptionPane.showMessageDialog(null, "Rezervacija odbijena.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
                updateTableDataRez();

            } else {
                JOptionPane.showMessageDialog(null, "Imamo kapacitete za ovu rezervaciju. Ne mora još da se odbije...", "Greška", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Niste izabrali rezervaciju!", "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addReservationAction() {
        RezervacijaAddEditDialog rez = new RezervacijaAddEditDialog(RecepcionerView.this, managers, null, null, dodatneUslugeMng.uslugeStringLista(), 1);
        rez.setVisible(true);
        updateTableDataRez();
    }
    
    private void addGuestAction() {
        GostAddEditDialog gostiD = new GostAddEditDialog(RecepcionerView.this, managers, null);
        gostiD.setVisible(true);
        updateTableDataGosti();
    }

    private void logout() {
        int confirmLogout = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da želite da se izlogujete i vratite na početni ekran?", "Potvrda",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (confirmLogout == JOptionPane.YES_OPTION) {
            setVisible(false);
            dispose();
            StartScreen startScreen = new StartScreen(managers);
            startScreen.setVisible(true);
        }
    }

    private void updateTableDataRez() {
        Object[][] updatedReservations = rezMng.prikazRez(rezMng.getRezervacije());
        DefaultTableModel modelR = (DefaultTableModel) tableR.getModel();
        modelR.setRowCount(0);
        for (Object[] row : updatedReservations) {
            modelR.addRow(row);
        }
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) tableR.getRowSorter();
        tableR.setRowSorter(null); 
        tableR.setRowSorter(sorter); 
        tableR.revalidate();
        tableR.repaint();
    }

    private void updateTableDataSoba() {
        Object[][] updatedRooms = sobeMng.prikazSoba(sobeMng.getSobe());
        DefaultTableModel model = new DefaultTableModel(updatedRooms, columnNamesRooms);
        tableS.setModel(model);
        tableS.revalidate();
        tableS.repaint();
    }

    private void updateTableDataGosti() {
        Object[][] updatedGosti = korisnikMng.formatirajGoste();
        DefaultTableModel model = (DefaultTableModel) tableG.getModel();
        model.setRowCount(0); 
        for (Object[] row : updatedGosti) {
            model.addRow(row); 
        }
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) tableG.getRowSorter();
        tableG.setRowSorter(null); 
        tableG.setRowSorter(sorter); 
        tableG.revalidate();
        tableG.repaint();
    }   

	private void updateTableDataDolasci() {
		Object[][] updatedDolasci = rezMng.prikazRez(rezMng.dobaviOcekivano(StatusRezervacije.POTVRĐENA));
        DefaultTableModel model = (DefaultTableModel) dolasciTable.getModel();
        model.setRowCount(0); 
        for (Object[] row : updatedDolasci) {
            model.addRow(row); 
        }
        dolasciTable.revalidate();
        dolasciTable.repaint();
	}
	
	private void updateTableDataOdlasci() {
		Object[][] updatedDolasci = rezMng.prikazRez(rezMng.dobaviOcekivano(StatusRezervacije.U_TOKU));
        DefaultTableModel model = (DefaultTableModel) odlasciTable.getModel();
        model.setRowCount(0); 
        for (Object[] row : updatedDolasci) {
            model.addRow(row); 
        }
        odlasciTable.revalidate();
        odlasciTable.repaint();
	}
	
	private void updateTableDataDanasnje() {
		 Object[][] listaDolazakaIOdlazaka = rezMng.prikazRez(rezMng.getDanasnjePromeneRez());
        DefaultTableModel model = (DefaultTableModel) dolasciOdlasciTable.getModel();
        model.setRowCount(0); 
        for (Object[] row : listaDolazakaIOdlazaka) {
            model.addRow(row); 
        }
        dolasciOdlasciTable.revalidate();
        dolasciOdlasciTable.repaint();
	}
}

