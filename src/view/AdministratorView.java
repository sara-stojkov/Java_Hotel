package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import entities.*;
import enums.StatusRezervacije;
import enums.StatusSobe;
import managers.*;
import view.addEdit.*;
import reports.*;


public class AdministratorView extends JFrame {

	private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tableR;
    private JTable tableS;
    private JTable tableG;
	private JTable tableZ;
	private JTable tableC;
	private JTable tableTS;
	private JTable tableDU;
    private static final String[] columnNamesRezerv = {"id", "korisnicko ime", "tip sobe", "datum pocetka", "datum kraja", "cena", "status", "soba"};
    private static final String[] columnNamesSobe = {"broj sobe", "tip sobe", "status sobe"};
    private static final String[] columnNamesGosti = {"korisnicko ime", "ime", "prezime", "pol", "datum rođenja", "broj telefona"};
    private static final String[] columnNamesZaposleni = {"korisnicko ime", "uloga", "ime", "prezime", "pol", "datum rođenja","broj telefona", "nivo spreme", "godina staža"};
    private static final String[] columnNamesDodatneUsluge = {"naziv", "trenutno aktivna"};
    private static final String[] columnNamesTipSobe = {"oznaka (id)", "naziv", "broj osoba (kapacitet)"};
    private static final String[] columnNamesCenovnik = {"id", "datum pocetka", "datum kraja"};
    private static final String[] columnNamesPrihodiRashodi = {"prihodi", "rashodi", "profit (prihodi - rashodi)"};
    private static final String[] columnNamesSobaricaIzv = {"korisnicko ime sobarice", "broj ociscenih soba"};
    private static final String[] columnNamesPotvRez = {"pocetni datum", "krajnji datum", "broj potvrđenih rezervacija"};
    private static final String[] columnNamesObrRez = {"status rezervacije", "broj rezervacija sa tim statusom"};
    private static final String[] columnNamesSobaIzv = {"broj sobe", "tip sobe", "ukupan broj nocenja", "ukupan prihod"};
    private RezervacijaManager rezMng;
    private DodatneUslugeManager dodatneUslugeMng;
    private SobaManager sobaMng;
    private TipSobeManager tipoviSobaMng;
    private CenovnikManager cenovnikMng;
    private KorisnikManager korisnikMng;
    private ManagerFactory managers;
	private JDatePickerImpl datePickerStart;
	private JDatePickerImpl datePickerEnd;
	private JTable izvestajiTabela1;

    public AdministratorView(ManagerFactory managers, String korisnickoImeAdmina) {
        this.managers = managers;
        this.rezMng = managers.getRezervacijaMng();
        this.dodatneUslugeMng = managers.getDodatneUslugeMng();
        this.tipoviSobaMng = managers.getTipSobeMng();
        this.cenovnikMng = managers.getCenovnikMng();
        this.sobaMng = managers.getSobaMng();
        this.korisnikMng = managers.getKorisnikMng();
        
        initAdminView(korisnickoImeAdmina);    
    }
	
    public void initAdminView(String korisnickoIme) {
        setTitle("Sarin hotel - ADMINISTRATOR");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1400, 600);
        setIconImage(new ImageIcon("./img/admin.png").getImage());

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        
        
        // PANEL ZA ZAPOSLENE
        JPanel zaposleniPanel = new JPanel();
        zaposleniPanel.setLayout(new BorderLayout());
        tabbedPane.addTab("Zaposleni", null, zaposleniPanel, null);

        Object[][] listaZaposlenih = korisnikMng.formatirajZaposleneMatrica();

        JLabel lblTitleZ= new JLabel("Zaposleni");
        lblTitleZ.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitleZ.setFont(new Font("Tahoma", Font.PLAIN, 24));
        zaposleniPanel.add(lblTitleZ, BorderLayout.NORTH);

        JScrollPane scrollPaneZ = new JScrollPane();
        TableModel modelZ = new DefaultTableModel(listaZaposlenih, columnNamesZaposleni) {
			private static final long serialVersionUID = 1L;

			@Override
            public Class<?> getColumnClass(int column) {
                if (getRowCount() == 0) {
                    return Object.class;
                }
                return getValueAt(0, column).getClass();
            }
          };        
        tableZ = new JTable(modelZ);
        tableZ.setFont(new Font("Tahoma", Font.PLAIN, 18));
        tableZ.getTableHeader().setOpaque(false);
        tableZ.getTableHeader().setBackground(Color.LIGHT_GRAY);
        tableZ.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 20));
        tableZ.getTableHeader().setReorderingAllowed(false);
        scrollPaneZ.setViewportView(tableZ);
        zaposleniPanel.add(scrollPaneZ, BorderLayout.CENTER);
        
        TableRowSorter<TableModel> sorterZ = new TableRowSorter<>(modelZ);
        tableZ.setRowSorter(sorterZ);

        JPanel zaposleniButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 25));
        zaposleniButtonPanel.add(createButton("Dodaj zaposlenog", e -> addZaposleni()));
        zaposleniButtonPanel.add(createButton("Izmeni odabranog zaposlenog", e -> editZaposleni()));
        zaposleniButtonPanel.add(createButton("Obriši odabranog zaposlenog", e -> deleteZaposleni()));
        zaposleniPanel.add(zaposleniButtonPanel, BorderLayout.SOUTH);
        
        
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
        TableModel modelG = new DefaultTableModel(listaGostiju, columnNamesGosti) {
			private static final long serialVersionUID = 1L;

			@Override
            public Class<?> getColumnClass(int column) {
                if (getRowCount() == 0) {
                    return Object.class;
                }
                return getValueAt(0, column).getClass();
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
        

        JPanel gostiButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 25));
        gostiButtonPanel.add(createButton("Dodaj gosta", e -> addGuestAction()));
        gostiButtonPanel.add(createButton("Izmeni odabranog gosta", e -> editGuestAction()));
        gostiButtonPanel.add(createButton("Obriši odabranog gosta", e -> deleteGuestAction()));        
        gostiPanel.add(gostiButtonPanel, BorderLayout.SOUTH);

        
        // Panel za REZERVACIJE
        JPanel rezervacijePanel = new JPanel();
        rezervacijePanel.setLayout(new BorderLayout());
        tabbedPane.addTab("Rezervacije", null, rezervacijePanel, null);

        Object[][] listaRezervacija = rezMng.prikazRez(rezMng.getRezervacije());for (int i = 0; i < listaRezervacija.length; i++);
            

        JLabel lblTitleR= new JLabel("Rezervacije");
        lblTitleR.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitleR.setFont(new Font("Tahoma", Font.PLAIN, 24));
        rezervacijePanel.add(lblTitleR, BorderLayout.NORTH);

        JScrollPane scrollPaneR = new JScrollPane();
        TableModel modelR = new DefaultTableModel(listaRezervacija, columnNamesRezerv) {
			private static final long serialVersionUID = 1L;

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

        JPanel rezervacijeButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 25)); 
        rezervacijeButtonPanel.add(createButton("Dodaj rezervaciju", e -> addReservationAction()));
        rezervacijeButtonPanel.add(createButton("Izmeni odabranu rezervaciju", e -> editReservationAction()));
        rezervacijeButtonPanel.add(createButton("Obriši odabranu rezervaciju", e -> deleteReservationAction()));
        rezervacijePanel.add(rezervacijeButtonPanel, BorderLayout.SOUTH);


        // Panel za SOBE
        JPanel sobePanel = new JPanel();
        sobePanel.setLayout(new BorderLayout());
        tabbedPane.addTab("Sobe", null, sobePanel, null);

        SobaManager sobeMng = managers.getSobaMng();
        Object[][] listaSoba = sobeMng.prikazSoba(sobeMng.getSobe());

        JLabel lblTitleS = new JLabel("Sobe");
        lblTitleS.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitleS.setFont(new Font("Tahoma", Font.PLAIN, 24));
        sobePanel.add(lblTitleS, BorderLayout.NORTH);

        JScrollPane scrollPaneS = new JScrollPane();
        DefaultTableModel modelS = new DefaultTableModel(listaSoba, columnNamesSobe);
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

        JPanel sobeButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 25));
        sobeButtonPanel.add(createButton("Dodaj sobu", e -> addRoomAction()));
        sobeButtonPanel.add(createButton("Izmeni odabranu sobu", e -> editRoomAction()));
        sobeButtonPanel.add(createButton("Obriši odabranu sobu", e -> deleteRoomAction()));
        sobePanel.add(sobeButtonPanel, BorderLayout.SOUTH);

        
     // Panel za TIPOVE SOBA
        JPanel tipSobePanel = new JPanel();
        tipSobePanel.setLayout(new BorderLayout());
        tabbedPane.addTab("Tipovi soba", null, tipSobePanel, null);

        TipSobeManager tipSobeMng = managers.getTipSobeMng();
        Object[][] listaTipovaSoba = tipSobeMng.prikazTipovaSoba(tipSobeMng.getTipoviSoba());

        JLabel lblTitleTS = new JLabel("Tipovi soba");
        lblTitleTS.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitleTS.setFont(new Font("Tahoma", Font.PLAIN, 24));
        tipSobePanel.add(lblTitleTS, BorderLayout.NORTH);

        JScrollPane scrollPaneTS = new JScrollPane();
        DefaultTableModel modelTS = new DefaultTableModel(listaTipovaSoba, columnNamesTipSobe);
        tableTS = new JTable(modelTS);
        tableTS.setFont(new Font("Tahoma", Font.PLAIN, 18));
        tableTS.getTableHeader().setOpaque(false);
        tableTS.getTableHeader().setBackground(Color.LIGHT_GRAY);
        tableTS.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 20));
        tableTS.getTableHeader().setReorderingAllowed(false);
        scrollPaneTS.setViewportView(tableTS);
        tipSobePanel.add(scrollPaneTS, BorderLayout.CENTER);
        
        TableRowSorter<TableModel> sorterTS = new TableRowSorter<>(modelTS);
        tableTS.setRowSorter(sorterTS);

        JPanel tipoviSobaButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 25));
        tipoviSobaButtonPanel.add(createButton("Dodaj tip sobe", e -> addTipSobe()));
        tipoviSobaButtonPanel.add(createButton("Izmeni odabrani tip sobe", e -> editTipSobe()));
        tipoviSobaButtonPanel.add(createButton("Izbriši odabrani tip sobe", e -> deleteTipSobe()));
        tipSobePanel.add(tipoviSobaButtonPanel, BorderLayout.SOUTH);

        
     // Panel za DODATNE USLUGE
        JPanel uslugePanel = new JPanel();
        uslugePanel.setLayout(new BorderLayout());
        tabbedPane.addTab("Dodatne usluge", null, uslugePanel, null);

        DodatneUslugeManager uslugeMng = managers.getDodatneUslugeMng();
        Object[][] listaUsluga = uslugeMng.prikazDodatnihUsluga(uslugeMng.getDodatneUsluge());

        JLabel lblTitleDU = new JLabel("Dodatne usluge hotela");
        lblTitleDU.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitleDU.setFont(new Font("Tahoma", Font.PLAIN, 24));
        uslugePanel.add(lblTitleDU, BorderLayout.NORTH);

        JScrollPane scrollPaneDU = new JScrollPane();
        DefaultTableModel modelDU = new DefaultTableModel(listaUsluga, columnNamesDodatneUsluge);
        tableDU = new JTable(modelDU);
        tableDU.setFont(new Font("Tahoma", Font.PLAIN, 18));
        tableDU.getTableHeader().setOpaque(false);
        tableDU.getTableHeader().setBackground(Color.LIGHT_GRAY);
        tableDU.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 20));
        tableDU.getTableHeader().setReorderingAllowed(false);
        scrollPaneDU.setViewportView(tableDU);
        uslugePanel.add(scrollPaneDU, BorderLayout.CENTER);
        
        TableRowSorter<TableModel> sorterDU = new TableRowSorter<>(modelDU);
        tableDU.setRowSorter(sorterDU);

        JPanel uslugeButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 25));
        uslugeButtonPanel.add(createButton("Dodaj uslugu", e -> addUsluga()));
        uslugeButtonPanel.add(createButton("Izmeni odabranu uslugu", e -> editUsluga()));
        uslugeButtonPanel.add(createButton("Izbriši odabranu uslugu", e -> deleteUsluga()));
        uslugePanel.add(uslugeButtonPanel, BorderLayout.SOUTH);

        
     // Panel za CENOVNIKE
        JPanel cenovnikPanel = new JPanel();
        cenovnikPanel.setLayout(new BorderLayout());
        tabbedPane.addTab("Cenovnici", null, cenovnikPanel, null);

        CenovnikManager cenovniciMng = managers.getCenovnikMng();
        Object[][] listaCenovnika = cenovniciMng.prikazCenovnika(cenovniciMng.getCenovnici());

        JLabel lblTitleC = new JLabel("Cenovnici");
        lblTitleC.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitleC.setFont(new Font("Tahoma", Font.PLAIN, 24));
        cenovnikPanel.add(lblTitleC, BorderLayout.NORTH);

        JScrollPane scrollPaneC = new JScrollPane();
        tableC = new JTable(listaCenovnika, columnNamesCenovnik);
        tableC.setFont(new Font("Tahoma", Font.PLAIN, 18));
        tableC.getTableHeader().setOpaque(false);
        tableC.getTableHeader().setBackground(Color.LIGHT_GRAY);
        tableC.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 20));
        tableC.getTableHeader().setReorderingAllowed(false);
        scrollPaneC.setViewportView(tableC);
        cenovnikPanel.add(scrollPaneC, BorderLayout.CENTER);

        JPanel cenovniciButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 25));
        cenovniciButtonPanel.add(createButton("Dodaj cenovnik", e -> addCenovnik()));
        cenovniciButtonPanel.add(createButton("Izmeni odabrani cenovnik", e -> editCenovnik()));
        cenovniciButtonPanel.add(createButton("Izbriši odabrani cenovnik", e -> deleteCenovnik()));
        cenovnikPanel.add(cenovniciButtonPanel, BorderLayout.SOUTH);
        
        
       // PANEL ZA IZVESTAJE
        JPanel izvestajiPanel = new JPanel();
        izvestajiPanel.setLayout(new BorderLayout());
        tabbedPane.addTab("Izveštaji", null, izvestajiPanel, null);
        
        JTabbedPane izvestajiTabPane = new JTabbedPane(JTabbedPane.LEFT);
        izvestajiTabPane.setBounds(25, 500, 1100, 300);
        izvestajiPanel.add(izvestajiTabPane, BorderLayout.CENTER);
        
        JPanel izvestaji1Panel = new JPanel(new BorderLayout());
        izvestajiTabPane.addTab("Opseg datuma", null, izvestaji1Panel, null);
        
        JPanel izvestaji1Datumi = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 25));
        
        JLabel pocetniDatumLbl = new JLabel("Početni datum");
        pocetniDatumLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
        izvestaji1Datumi.add(pocetniDatumLbl);

        SqlDateModel modelStart = new SqlDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanelStart = new JDatePanelImpl(modelStart, p);
        this.datePickerStart = new JDatePickerImpl(datePanelStart, new DateComponentFormatter());
        datePickerStart.getJFormattedTextField().setFont(new Font("Tahoma", Font.PLAIN, 16));
        
        izvestaji1Datumi.add(datePickerStart);

        JLabel krajDatumLbl = new JLabel("Krajnji datum");
        krajDatumLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
        izvestaji1Datumi.add(krajDatumLbl);

        SqlDateModel modelEnd = new SqlDateModel();
        JDatePanelImpl datePanelEnd = new JDatePanelImpl(modelEnd, p);
        this.datePickerEnd = new JDatePickerImpl(datePanelEnd, new DateComponentFormatter());
        datePickerEnd.getJFormattedTextField().setFont(new Font("Tahoma", Font.PLAIN, 16));
        izvestaji1Datumi.add(datePickerEnd);
        
        izvestaji1Panel.add(izvestaji1Datumi, BorderLayout.NORTH);
        
        JPanel izvestaji1BtnPanel = new JPanel();
        izvestaji1BtnPanel.setLayout(new BoxLayout(izvestaji1BtnPanel, BoxLayout.Y_AXIS));
        izvestaji1BtnPanel.setBorder(new EmptyBorder(20, 10, 20, 10)); 
        izvestaji1BtnPanel.add(Box.createVerticalGlue());

        izvestaji1BtnPanel.add(createButton("Prihodi/ rashodi", e -> {
            if (datePickerStart.getModel().getValue() == null || datePickerEnd.getModel().getValue() == null) {
                JOptionPane.showMessageDialog(null, "Morate izabrati datume.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                updateTableIzvestaji(columnNamesPrihodiRashodi, PrihodiRashodi.formatirajPrihodeRashode(rezMng, korisnikMng, ((java.sql.Date)  datePickerStart.getModel().getValue()).toLocalDate(), ((java.sql.Date)  datePickerEnd.getModel().getValue()).toLocalDate()));
            }
        }));
        izvestaji1BtnPanel.add(Box.createVerticalStrut(10));

        izvestaji1BtnPanel.add(createButton("Sobarica -> sobe", e -> {
            if (datePickerStart.getModel().getValue() == null || datePickerEnd.getModel().getValue() == null) {
                JOptionPane.showMessageDialog(null, "Morate izabrati datume.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                updateTableIzvestaji(columnNamesSobaricaIzv, SobaricaBrSoba.dobaviBrSobaPoSobarici(sobaMng, korisnikMng, ((java.sql.Date)  datePickerStart.getModel().getValue()).toLocalDate(), ((java.sql.Date)  datePickerEnd.getModel().getValue()).toLocalDate()));
            }
        }));
        izvestaji1BtnPanel.add(Box.createVerticalStrut(10));

        izvestaji1BtnPanel.add(createButton("Broj potvrđenih rezervacija", e -> {
            if (datePickerStart.getModel().getValue() == null || datePickerEnd.getModel().getValue() == null) {
                JOptionPane.showMessageDialog(null, "Morate izabrati datume.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                updateTableIzvestaji(columnNamesPotvRez, PotvrdjeneRez.formatirajPrihodeRashode(rezMng, ((java.sql.Date) datePickerStart.getModel().getValue()).toLocalDate(), ((java.sql.Date)  datePickerEnd.getModel().getValue()).toLocalDate()));
            }
        }));
        izvestaji1BtnPanel.add(Box.createVerticalStrut(10));

        izvestaji1BtnPanel.add(createButton("Broj i statusi obrađenih rezervacija", e -> {
            if (datePickerStart.getModel().getValue() == null || datePickerEnd.getModel().getValue() == null) {
                JOptionPane.showMessageDialog(null, "Morate izabrati datume.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                updateTableIzvestaji(columnNamesObrRez, ObradjeneRez.dobaviBrRez(rezMng.getRezervacije(), ((java.sql.Date) datePickerStart.getModel().getValue()).toLocalDate(), ((java.sql.Date) datePickerEnd.getModel().getValue()).toLocalDate()));
            }
        }));
        izvestaji1BtnPanel.add(Box.createVerticalStrut(10));

        izvestaji1BtnPanel.add(createButton("Svaka soba pojedinačno", e -> {
            if (datePickerStart.getModel().getValue() == null || datePickerEnd.getModel().getValue() == null) {
                JOptionPane.showMessageDialog(null, "Morate izabrati datume.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                updateTableIzvestaji(columnNamesSobaIzv, SobaIzvestaj.nadjiPodatkeOSobama(sobaMng, rezMng, ((java.sql.Date) datePickerStart.getModel().getValue()).toLocalDate(), ((java.sql.Date) datePickerEnd.getModel().getValue()).toLocalDate()));
            }
        }));
        izvestaji1BtnPanel.add(Box.createVerticalGlue());

        izvestaji1Panel.add(izvestaji1BtnPanel, BorderLayout.WEST);
        
        izvestajiTabela1 = new JTable(new DefaultTableModel());
        izvestaji1Panel.add(new JScrollPane(izvestajiTabela1), BorderLayout.CENTER);
          
        JPanel izvestaji2Panel = new JPanel(new BorderLayout());
        izvestajiTabPane.addTab("Chartovi", null, izvestaji2Panel, null);
        
        JLabel izvestaji2Title = new JLabel("Da biste videli grafički prikaz, pritisnite dugme izveštaja koji Vas zanima: ");
        izvestaji2Title.setHorizontalAlignment(SwingConstants.CENTER);
        izvestaji2Title.setFont(new Font("Tahoma", Font.PLAIN, 22));
        izvestaji2Title.setBorder(new EmptyBorder(10, 0, 10, 0)); 
        izvestaji2Panel.add(izvestaji2Title, BorderLayout.NORTH);
        
        JPanel izvestaji2BtnPanel = new JPanel();
	    izvestaji2BtnPanel.setLayout(new BoxLayout(izvestaji2BtnPanel, BoxLayout.Y_AXIS));
        izvestaji2BtnPanel.setBorder(new EmptyBorder(40, 60, 40, 60)); 
	   
	    izvestaji2BtnPanel.add(createButton("Opterećenost sobarica u poslednjih 30 dana", e -> {
	    	ChartSobarice chartSobarice = new ChartSobarice(sobaMng, korisnikMng);
	        PieChart pieChart = chartSobarice.getChart();
	        JFrame frame = new JFrame("Grafički prikaz opterećenja sobarica u poslednjih 30 dana");
	        XChartPanel<PieChart> chartPanel = new XChartPanel<>(pieChart);
	        frame.getContentPane().add(chartPanel);
	        frame.pack();
	        frame.setVisible(true);
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	    }));
	    izvestaji2BtnPanel.add(Box.createVerticalStrut(10));
	    
	    izvestaji2BtnPanel.add(createButton("Statusi rezervacija u poslednjih 30 dana", e -> {
	    	ChartRezervacije chartRezervacije = new ChartRezervacije(rezMng);
	        PieChart pieChart = chartRezervacije.getChart();
	        JFrame frame = new JFrame("Grafički prikaz statusa rezervacija u poslednjih 30 dana");
	        XChartPanel<PieChart> chartPanel = new XChartPanel<>(pieChart);
	        frame.getContentPane().add(chartPanel);
	        frame.pack();
	        frame.setVisible(true);
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	    }));
	    
	    izvestaji2BtnPanel.add(Box.createVerticalStrut(10));
	    
	    izvestaji2BtnPanel.add(createButton("Prikaz prihoda u poslednjih 12 meseci", e -> {
	    	ChartPrihoda chartPrihoda = new ChartPrihoda(rezMng, tipSobeMng);
	    	
	        XYChart xyChart = chartPrihoda.getChart();
	        JFrame frame = new JFrame("Grafički prikaz prihoda");
	        XChartPanel<XYChart> chartPanel = new XChartPanel<>(xyChart);
	        frame.getContentPane().add(chartPanel);
	        frame.pack();
	        frame.setVisible(true);
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	    }));
	    izvestaji2BtnPanel.add(Box.createVerticalGlue());
        izvestaji2Panel.add(izvestaji2BtnPanel, BorderLayout.CENTER);


        // Logout dugme
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        logoutButton.addActionListener(e -> logout());
        logoutPanel.add(logoutButton);
        contentPane.add(logoutPanel, BorderLayout.NORTH);
    }
	
    private void updateTableIzvestaji(String[] naziviKolona, Object[][] listaObjekata) {
        DefaultTableModel model = new DefaultTableModel(listaObjekata, naziviKolona);
        izvestajiTabela1.setModel(model);
        izvestajiTabela1.getTableHeader().setOpaque(false);
        izvestajiTabela1.getTableHeader().setBackground(Color.LIGHT_GRAY);
        izvestajiTabela1.getTableHeader().setReorderingAllowed(false); 
        izvestajiTabela1.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 16));
        izvestajiTabela1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        izvestajiTabela1.revalidate();
        izvestajiTabela1.repaint();
    }


	private void addZaposleni() {
	    	ZaposleniAddEditDialog zaps = new ZaposleniAddEditDialog(AdministratorView.this, managers, null);
	        zaps.setVisible(true);
	        updateTableDataZaposleni();
	}

    private void deleteZaposleni() {
    	int selectedRow = tableZ.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da želite da obrišete ovog zaposlenog?", "Potvrda",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                String korisnickoIme = (String) tableZ.getValueAt(selectedRow, 0);
				korisnikMng.obrisiKorisnika(korisnickoIme);

				updateTableDataZaposleni();
				
            } else if (confirm == JOptionPane.NO_OPTION) {
                System.out.println("Ništa nije preduzeto.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Niste izabrali zaposlenog!", "Greška", JOptionPane.ERROR_MESSAGE);
        }		
	}

	private void editZaposleni() {
		int selectedRow = tableZ.getSelectedRow();
        if (selectedRow != -1) {
            String korisnickoIme = (String) tableZ.getValueAt(selectedRow, 0);
            Zaposleni odabraniZap = (Zaposleni) korisnikMng.dobaviKorisnikaPoUsername(korisnickoIme);
        	ZaposleniAddEditDialog zaps = new ZaposleniAddEditDialog(AdministratorView.this, managers, odabraniZap);
            zaps.setVisible(true);
            updateTableDataZaposleni();
        } else {
            JOptionPane.showMessageDialog(null, "Niste izabrali zaposlenog!", "Greška", JOptionPane.ERROR_MESSAGE);
        }	
	}

	private void addGuestAction() {
		GostAddEditDialog gostiD = new GostAddEditDialog(AdministratorView.this, managers, null);
        gostiD.setVisible(true);
        updateTableDataGosti();
	}
	
	private void editGuestAction() {
		int selectedRow = tableG.getSelectedRow();
        if (selectedRow != -1) {
            String korisnickoIme = (String) tableG.getValueAt(selectedRow, 0);
    		GostAddEditDialog gostiD = new GostAddEditDialog(AdministratorView.this, managers, (Gost) korisnikMng.dobaviKorisnikaPoUsername(korisnickoIme));
            gostiD.setVisible(true);
            updateTableDataGosti();
        } else {
            JOptionPane.showMessageDialog(null, "Niste izabrali gosta!", "Greška", JOptionPane.ERROR_MESSAGE);
        }	
	}
	
	private void deleteGuestAction() {
		int selectedRow = tableG.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da želite da obrišete ovog gosta?", "Potvrda",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                String idString = String.valueOf(tableG.getValueAt(selectedRow, 0));
                korisnikMng.obrisiKorisnika(idString);
		        updateTableDataGosti();
		        
            } else if (confirm == JOptionPane.NO_OPTION) {
                System.out.println("Ništa nije preduzeto.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Niste izabrali gosta!", "Greška", JOptionPane.ERROR_MESSAGE);
        }
	}

	
	private void editRoomAction() {
		int selectedRow = tableS.getSelectedRow();
        if (selectedRow != -1) {
            String idString = String.valueOf(tableS.getValueAt(selectedRow, 0));
            int brojSobe = Integer.parseInt(idString);
            if (sobaMng.dobaviSobuPoBroju(brojSobe).getStatusSobe() == StatusSobe.ZAUZETO) {
                JOptionPane.showMessageDialog(null, "Samo sobe sa statusom SLOBODNA ili SPREMANjE mogu biti izmenjene.", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Soba odabranaSoba = sobaMng.dobaviSobuPoBroju(brojSobe);
			SobaAddEditDialog sobaD = new SobaAddEditDialog(AdministratorView.this, managers, odabranaSoba);
	        sobaD.setVisible(true);
	        updateTableDataSobe();
        }
	 else {
        JOptionPane.showMessageDialog(null, "Niste izabrali sobu!", "Greška", JOptionPane.ERROR_MESSAGE);
	 	}
	}
	
	private void addRoomAction() {
		SobaAddEditDialog sobaD = new SobaAddEditDialog(AdministratorView.this, managers, null);
        sobaD.setVisible(true);
        updateTableDataSobe();
		}
	
	private void deleteRoomAction() {
		int selectedRow = tableS.getSelectedRow();
        if (selectedRow != -1) {
            String idString = String.valueOf(tableS.getValueAt(selectedRow, 0));
            int brojSobe = Integer.parseInt(idString);
            if (!(sobaMng.dobaviSobuPoBroju(brojSobe).getStatusSobe() == StatusSobe.SLOBODNA || sobaMng.dobaviSobuPoBroju(brojSobe).getStatusSobe() == StatusSobe.SPREMANjE)) {
                JOptionPane.showMessageDialog(null, "Samo sobe sa statusom SLOBODNA ili SPREMANjE mogu biti obrisane.", "Greška", JOptionPane.ERROR_MESSAGE);
                return;

            }
      
            int cofirmDeletion = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da želite da obrišete ovu sobu? Ova akcija će imati posledice.", "Potvrda", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);            
        	if (cofirmDeletion == JOptionPane.YES_OPTION) {
	        	sobaMng.izmeniStatusSobe(brojSobe, StatusSobe.OBRISANA);
	            JOptionPane.showMessageDialog(null, "Soba obrisana. Ovo bi moglo da utiče na rezervacije, posavetujte se sa šefom smene.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
	            updateTableDataSobe();
        	}
            
        } else {
            JOptionPane.showMessageDialog(null, "Niste izabrali sobu!", "Greška", JOptionPane.ERROR_MESSAGE);
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
            if (rezMng.dobaviRezervacijuPoId(idString).getStatusRezervacije() != StatusRezervacije.NA_ČEKANjU) {
                JOptionPane.showMessageDialog(null, "Samo rezervacije sa statusom NA_ČEKANjU mogu da se menjaju!", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Rezervacija odabranaRezervacija = rezMng.dobaviRezervacijuPoId(idString);
            RezervacijaAddEditDialog rez = new RezervacijaAddEditDialog(AdministratorView.this, managers, odabranaRezervacija, null, dodatneUslugeMng.uslugeStringLista(), 2);
            rez.setVisible(true);
            updateTableDataRez();
        } else {
            JOptionPane.showMessageDialog(null, "Niste izabrali rezervaciju!", "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addReservationAction() {
        RezervacijaAddEditDialog rez = new RezervacijaAddEditDialog(AdministratorView.this, managers, null, null, dodatneUslugeMng.uslugeStringLista(), 2);
        rez.setVisible(true);
        updateTableDataRez();
    }
    
    private void deleteReservationAction() {
    	int selectedRow = tableR.getSelectedRow();
        if (selectedRow != -1) {
            String idString = String.valueOf(tableR.getValueAt(selectedRow, 0));
            if (!(rezMng.dobaviRezervacijuPoId(idString).getStatusRezervacije() == StatusRezervacije.NA_ČEKANjU || rezMng.dobaviRezervacijuPoId(idString).getStatusRezervacije() == StatusRezervacije.POTVRĐENA)) {
                JOptionPane.showMessageDialog(null, "Samo registracije sa statusom NA ČEKANjU ili POTVRĐENA mogu biti obrisane.", "Greška", JOptionPane.ERROR_MESSAGE);
                return;

            }
        	rezMng.promeniStatusRezervacije(idString, StatusRezervacije.OBRISANA);
            JOptionPane.showMessageDialog(null, "Rezervacija obrisana.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
            updateTableDataRez();

            
        } else {
            JOptionPane.showMessageDialog(null, "Niste izabrali rezervaciju!", "Greška", JOptionPane.ERROR_MESSAGE);
        }
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
    
    private void deleteTipSobe() {
    	int selectedRow = tableTS.getSelectedRow();
        if (selectedRow != -1) {
            String idString = String.valueOf(tableTS.getValueAt(selectedRow, 0));
      
            int cofirmDeletion = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da želite da obrišete ovaj tip sobe? Ova akcija će imati posledice.", "Potvrda", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);            
        	if (cofirmDeletion == JOptionPane.YES_OPTION) {
        		TipSobe odabraniTip = tipoviSobaMng.dobaviTipPoOznaci(idString);
        		try {
					if (tipoviSobaMng.obrisiTipSobe(odabraniTip, managers)) {
					    JOptionPane.showMessageDialog(null, "Tip sobe obrisan. Ovo bi moglo da utiče na rezervacije i sobe, posavetujte se sa šefom smene.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
					    updateTableDataTipoviSoba();
					}
					else {
					    JOptionPane.showMessageDialog(null, "Tip sobe NIJE obrisan jer je povezan sa sobama i rezervacijama u sistemu.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);

					}
				} catch (HeadlessException | IOException e) {
				    JOptionPane.showMessageDialog(null, "Došlo je do greške.", "Greška", JOptionPane.ERROR_MESSAGE);

				}
        	}
            
        } else {
            JOptionPane.showMessageDialog(null, "Niste izabrali tip sobe!", "Greška", JOptionPane.ERROR_MESSAGE);
        }
	}

	private void editTipSobe() {
		int selectedRow = tableTS.getSelectedRow();
        if (selectedRow != -1) {
            String idString = String.valueOf(tableTS.getValueAt(selectedRow, 0));
            
            TipSobe odabraniTipSobe = tipoviSobaMng.dobaviTipPoOznaci(idString);
            TipSobeAddEditDialog tipSobeD = new TipSobeAddEditDialog(AdministratorView.this, managers, odabraniTipSobe);
			tipSobeD.setVisible(true);
	        updateTableDataTipoviSoba();
        } else {
            JOptionPane.showMessageDialog(null, "Niste izabrali tip sobe!", "Greška", JOptionPane.ERROR_MESSAGE);
        }
	}

	private void addTipSobe() {
		TipSobeAddEditDialog tipSobeD = new TipSobeAddEditDialog(AdministratorView.this, managers, null);
		tipSobeD.setVisible(true);
        
		if (tipSobeD.additionSuccess) {
			try {
				int cenaNocenja = Integer.parseInt(JOptionPane.showInputDialog(null,"Unesite cenu ovog tipa sobe po noćenju: ").trim()); 
				TipSobe tipSobe = tipoviSobaMng.getTipoviSoba().get(tipoviSobaMng.getTipoviSoba().size() - 1); // uzimamo poslednji tip sa liste
				cenovnikMng.dodajTipSobeCenovniku(tipSobe, cenaNocenja);
		        JOptionPane.showMessageDialog(null, "Uspešno je dodata cena tipu sobe " + tipSobe.getOznaka() + "!", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
				}
				catch (Exception e3) {
					JOptionPane.showMessageDialog(null, "Cena mora biti broj!", "Greška", JOptionPane.ERROR_MESSAGE);
				}   
		}
	     updateTableDataTipoviSoba();
	}

	private void deleteUsluga() {
		int selectedRow = tableDU.getSelectedRow();
        if (selectedRow != -1) {
            String nazivUsl = String.valueOf(tableDU.getValueAt(selectedRow, 0));
            int cofirmDeletion = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da želite da obrišete ovu dodatnu uslugu?", "Potvrda", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);            
        	if (cofirmDeletion == JOptionPane.YES_OPTION) {
        		try {
					dodatneUslugeMng.obrisiUslugu(nazivUsl);
				    JOptionPane.showMessageDialog(null, "Uspešno obrisana usluga.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
					updateTableDataUsluge();
				} catch (HeadlessException | IOException e) {
				    JOptionPane.showMessageDialog(null, "Došlo je do greške.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
        		}}else {
            JOptionPane.showMessageDialog(null, "Niste izabrali ni jednu uslugu!", "Greška", JOptionPane.ERROR_MESSAGE);
        		}
	}

	private void editUsluga() {
		
		int selectedRow = tableDU.getSelectedRow();
        if (selectedRow != -1) {
            String nazivUsl = String.valueOf(tableDU.getValueAt(selectedRow, 0));
            
            DodatnaUsluga odabranaUsl = dodatneUslugeMng.nadjiUsluguPoNazivu(nazivUsl);
			String imeUsluge = JOptionPane.showInputDialog(null,"Pažljivo, zbog rezervacija i cenovnika vezanih za usluge, unesite novi naziv za odabranu uslugu: "); 
			if (imeUsluge == null) {
	            return;
	        }
	        if (imeUsluge.trim().isEmpty()) {
	            JOptionPane.showMessageDialog(null, "Ime usluge ne sme biti prazno!", "Greška", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
			DodatnaUsluga usl = new DodatnaUsluga(imeUsluge);
			try {
				dodatneUslugeMng.izmeniUslugu(usl, odabranaUsl);
			    JOptionPane.showMessageDialog(null, "Uspešno izmenjena usluga.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
				updateTableDataUsluge();
			} catch (IOException e) {
	            JOptionPane.showMessageDialog(null, "Došlo je do greške pri izmeni usluge!", "Greška", JOptionPane.ERROR_MESSAGE);
			}
        } else {
            JOptionPane.showMessageDialog(null, "Niste izabrali tip sobe!", "Greška", JOptionPane.ERROR_MESSAGE);
        }
	}

	private void addUsluga() {
		String imeUsluge = JOptionPane.showInputDialog(null,"Unesite naziv dodatne usluge koju želite da dodate: ").trim(); 
		DodatnaUsluga usl = new DodatnaUsluga(imeUsluge);
		try {
			dodatneUslugeMng.dodajUslugu(usl);
			try {
				int cenaUsluge = Integer.parseInt(JOptionPane.showInputDialog(null,"Unesite cenu ove dodatne usluge: ").trim()); 
				cenovnikMng.dodajUsluguCenovniku(usl, cenaUsluge);
	            JOptionPane.showMessageDialog(null, "Uspešno je dodata usluga " + imeUsluge + "!", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
	            updateTableDataUsluge();
			}
			catch (Exception e3) {
				JOptionPane.showMessageDialog(null, "Cena mora biti broj!", "Greška", JOptionPane.ERROR_MESSAGE);
			}
		} catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Došlo je do greške pri dodavanju usluge!", "Greška", JOptionPane.ERROR_MESSAGE);

		}
	}

	private void addCenovnik() {
		CenovnikAddEditDialog cen = new CenovnikAddEditDialog(AdministratorView.this, managers, null);
        cen.setVisible(true);
        updateTableDataCenovnici();
        }

	private void editCenovnik() {
		int selectedRow = tableC.getSelectedRow();
        if (selectedRow != -1) {
            String idCen = String.valueOf(tableC.getValueAt(selectedRow, 0));
            
        		try {
        			Cenovnik stariCen = cenovnikMng.dobaviCenovnikPoId(idCen);
        			CenovnikAddEditDialog cen = new CenovnikAddEditDialog(AdministratorView.this, managers, stariCen);
        	        cen.setVisible(true);
        	        updateTableDataCenovnici();
				} catch (Exception e) {
				    JOptionPane.showMessageDialog(null, "Došlo je do greškeee.", "Greška", JOptionPane.ERROR_MESSAGE);
				    e.printStackTrace();
					}
		}else {
        JOptionPane.showMessageDialog(null, "Niste izabrali ni jedan cenovnik!", "Greška", JOptionPane.ERROR_MESSAGE);
    		}
	}

	private void deleteCenovnik() {
		int selectedRow = tableC.getSelectedRow();
        if (selectedRow != -1) {
            String idCen = String.valueOf(tableC.getValueAt(selectedRow, 0));
            int cofirmDeletion = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da želite da obrišete ovaj cenovnik?", "Potvrda", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);            
        	if (cofirmDeletion == JOptionPane.YES_OPTION) {
        		try {
					if (cenovnikMng.obrisiCenovnik(idCen)) {
				    JOptionPane.showMessageDialog(null, "Uspešno obrisan cenovnik.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
					updateTableDataCenovnici();
					}
					else {
					    JOptionPane.showMessageDialog(null, "Cenovnik nije obrisan, mora postojati barem 1.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (Exception e) {
				    JOptionPane.showMessageDialog(null, "Došlo je do greške.", "Greška", JOptionPane.ERROR_MESSAGE);
					}
        		}}else {
            JOptionPane.showMessageDialog(null, "Niste izabrali ni jedan cenovnik!", "Greška", JOptionPane.ERROR_MESSAGE);
        		}
	}


    private void updateTableDataZaposleni() {
    	Object[][] updatedZaposleni = korisnikMng.formatirajZaposleneMatrica();
    	DefaultTableModel model = (DefaultTableModel) tableZ.getModel();
	        
        model.setRowCount(0);
        for (Object[] row : updatedZaposleni) {
            model.addRow(row);
        }
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) tableZ.getRowSorter();
        tableZ.setRowSorter(null); 
        tableZ.setRowSorter(sorter); 
        tableZ.revalidate();
        tableZ.repaint();	
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
    
    
    private void updateTableDataRez() {
        Object[][] updatedReservations = rezMng.prikazRez(rezMng.getRezervacije());
        DefaultTableModel model = (DefaultTableModel) tableR.getModel();
        
        model.setRowCount(0);
        for (Object[] row : updatedReservations) {
            model.addRow(row);
        }
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) tableR.getRowSorter();
        tableR.setRowSorter(null); 
        tableR.setRowSorter(sorter); 
        tableR.revalidate();
        tableR.repaint();
    }

    private void updateTableDataSobe() {
        Object[][] updatedRooms = sobaMng.prikazSoba(sobaMng.getSobe());
        DefaultTableModel model = (DefaultTableModel) tableS.getModel();
        
        model.setRowCount(0);
        for (Object[] row : updatedRooms) {
            model.addRow(row);
        }
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) tableS.getRowSorter();
        tableS.setRowSorter(null); 
        tableS.setRowSorter(sorter); 
        tableS.revalidate();
        tableS.repaint();
    }
    
    private void updateTableDataTipoviSoba() {
        Object[][] updatedRoomTypes = tipoviSobaMng.prikazTipovaSoba(tipoviSobaMng.getTipoviSoba());
        
        if (!(tableTS.getModel() instanceof DefaultTableModel)) {
            tableTS.setModel(new DefaultTableModel(updatedRoomTypes, columnNamesTipSobe));
        } else {
            DefaultTableModel model = (DefaultTableModel) tableTS.getModel();
            model.setRowCount(0); 
            
            for (Object[] row : updatedRoomTypes) {
                model.addRow(row);
            }
        }
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) tableTS.getRowSorter();
        tableTS.setRowSorter(null);
        tableTS.setRowSorter(sorter);
        tableTS.revalidate();
        tableTS.repaint();
    }

    private void updateTableDataUsluge() {
        Object[][] updatedServices = dodatneUslugeMng.prikazDodatnihUsluga(dodatneUslugeMng.getDodatneUsluge());
        DefaultTableModel model = (DefaultTableModel) tableDU.getModel();
        
        model.setRowCount(0);
        for (Object[] row : updatedServices) {
            model.addRow(row);
        }
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) tableDU.getRowSorter();
        tableDU.setRowSorter(null); 
        tableDU.setRowSorter(sorter); 
        tableDU.revalidate();
        tableDU.repaint();
    }
    
    private void updateTableDataCenovnici() {
        Object[][] updatedCenovnici = cenovnikMng.prikazCenovnika(cenovnikMng.getCenovnici());
        
        if (!(tableC.getModel() instanceof DefaultTableModel)) {
            tableC.setModel(new DefaultTableModel(updatedCenovnici, columnNamesCenovnik));
        } else {
            DefaultTableModel model = (DefaultTableModel) tableC.getModel();
            model.setRowCount(0); 
            
            for (Object[] row : updatedCenovnici) {
                model.addRow(row);
            }
        }
        
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) tableC.getRowSorter();
        tableC.setRowSorter(null); 
        tableC.setRowSorter(sorter); 
        tableC.revalidate();
        tableC.repaint();
    }
}
