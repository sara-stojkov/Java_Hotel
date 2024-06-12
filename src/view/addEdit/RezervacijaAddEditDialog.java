package view.addEdit; 

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import entities.Cenovnik;
import entities.DodatnaUsluga;
import entities.Gost;
import entities.Rezervacija;
import entities.TipSobe;

import org.jdatepicker.impl.DateComponentFormatter;

import managers.CenovnikManager;
import managers.DodatneUslugeManager;
import managers.KorisnikManager;
import managers.ManagerFactory;
import managers.RezervacijaManager;
import managers.SobaManager;
import managers.TipSobeManager;

public class RezervacijaAddEditDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private RezervacijaManager rezMng;
    private Rezervacija editRez;
    private JFrame roditelj;
    private String[] listaUsluga;
    private int moc;
    private String username;
    private ArrayList<TipSobe> tipoviSoba;
    private TipSobeManager tSobaMng;
    private CenovnikManager cenMng;
    private KorisnikManager korisnikMng;
    private DodatneUslugeManager dodUslMng;
    private JTextField tfId;
    private JTextField tfUserName;
    private JComboBox<String> comboBox;
    private JDatePickerImpl datePickerStart;
    private JDatePickerImpl datePickerEnd;
    private JLabel priceLbl;
    private ArrayList<JCheckBox> uslugeCheckBoxes;
    private boolean uspesnoDodavanjeIliIzmena = false;
	private SobaManager sobeMng;
	private ArrayList<JCheckBox> osobineCheckBoxes;
	private JPanel uslugePanel;
	private JPanel osobinePanel;


    public RezervacijaAddEditDialog(JFrame roditelj, ManagerFactory managers, Rezervacija editRezervacija, String korisnickoIme, String[] listaUsluga, int autoritet) {
        super(roditelj, true);
        this.roditelj = roditelj;
        if (editRez != null) {
            setTitle("Izmena rezervacije");
        } else {
            setTitle("Dodavanje rezervacije");
        }
        this.rezMng = managers.getRezervacijaMng();
        this.editRez = editRezervacija;
        this.listaUsluga = listaUsluga;
        this.moc = autoritet;
        this.username = korisnickoIme;
        this.tSobaMng = managers.getTipSobeMng();
        this.tipoviSoba = tSobaMng.getTipoviSoba();
        this.sobeMng = managers.getSobaMng();
        this.cenMng = managers.getCenovnikMng();
        this.korisnikMng = managers.getKorisnikMng();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setBounds(100, 100, 600, 700);
        initRezDialog();
        pack();
    }

    public void initRezDialog() {
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel idLabel = new JLabel("ID rezervacije");
        idLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(idLabel, gbc);

        this.tfId = new JTextField();
        tfId.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        contentPane.add(tfId, gbc);
        
        JLabel usernameLabel = new JLabel("Korisnicko ime gosta");
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPane.add(usernameLabel, gbc);

        this.tfUserName = new JTextField();
        tfUserName.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPane.add(tfUserName, gbc);

        if (moc < 2 && username != null) {
            tfUserName.setText(username);
            tfUserName.setEditable(false);
        	tfId.setText(generisiId());
            tfId.setEditable(false);
        }
        
        if (moc < 2 && editRez != null) {
        	tfId.setText(editRez.getId());
            tfId.setEditable(false);
        }
        
        if (moc < 2 && username == null) {
        	tfId.setText(generisiId());
            tfId.setEditable(false);
        }


        JLabel lblNewLabel_3 = new JLabel("Tip sobe");
        lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPane.add(lblNewLabel_3, gbc);

        this.comboBox = new JComboBox<>();
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPane.add(comboBox, gbc);

        dodajTipoveSoba(comboBox, tipoviSoba);

        JLabel lblNewLabel_4 = new JLabel("Početni datum");
        lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPane.add(lblNewLabel_4, gbc);

        SqlDateModel modelStart = new SqlDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanelStart = new JDatePanelImpl(modelStart, p);
        this.datePickerStart = new JDatePickerImpl(datePanelStart, new DateComponentFormatter());
        datePickerStart.getJFormattedTextField().setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPane.add(datePickerStart, gbc);

        JLabel lblNewLabel_4_1 = new JLabel("Krajnji datum");
        lblNewLabel_4_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPane.add(lblNewLabel_4_1, gbc);

        SqlDateModel modelEnd = new SqlDateModel();
        JDatePanelImpl datePanelEnd = new JDatePanelImpl(modelEnd, p);
        this.datePickerEnd = new JDatePickerImpl(datePanelEnd, new DateComponentFormatter());
        datePickerEnd.getJFormattedTextField().setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.gridy = 4;
        contentPane.add(datePickerEnd, gbc);

        JLabel lblNewLabel_2 = new JLabel("Dodatne usluge");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        contentPane.add(lblNewLabel_2, gbc);

        uslugePanel = new JPanel();
        uslugePanel.setLayout(new BoxLayout(uslugePanel, BoxLayout.Y_AXIS));
        gbc.gridx = 0;
        gbc.gridy = 6;
        contentPane.add(uslugePanel, gbc);

        uslugeCheckBoxes = dodajUsluge(listaUsluga, uslugePanel);
        
        JLabel osobineLbl = new JLabel("Ostale osobine sobe:");
        osobineLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        contentPane.add(osobineLbl, gbc);

        osobinePanel = new JPanel();
        osobinePanel.setLayout(new BoxLayout(osobinePanel, BoxLayout.Y_AXIS));
        gbc.gridx = 1;
        gbc.gridy = 6;
        contentPane.add(osobinePanel, gbc);

        osobineCheckBoxes = dodajOsobineSoba(sobeMng.getSveOsobineSoba(), osobinePanel);
        
       if (username == null) {
           tfUserName.setEditable(true);
       }
        
        
        if (editRez != null) {
            Component[] components = uslugePanel.getComponents();
            for (Component component : components) {
                if (component instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) component;
                    for (DodatnaUsluga usluga : editRez.getDodatneUsluge()) {
                        if (checkBox.getText().equals(usluga.getNaziv())) {
                            checkBox.setSelected(true);
                            if (moc < 2) {
                            checkBox.setEnabled(false);
                            }
                            break;
                        }
                    }
                }
            }
            
            Component[] components2 = osobinePanel.getComponents();
            for (Component component : components2) {
                if (component instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) component;
                    for (String osobina : editRez.getOsobineSobe()) {
                        if (checkBox.getText().equals(osobina)) {
                            checkBox.setSelected(true);
                            break;
                        }
                    }
                }
            }

            SqlDateModel model1Start = (SqlDateModel) datePickerStart.getModel();
            SqlDateModel model1End = (SqlDateModel) datePickerEnd.getModel();
            model1Start.setValue(java.sql.Date.valueOf(editRez.getDatumPocetka()));
            model1End.setValue(java.sql.Date.valueOf(editRez.getDatumKraja()));

            comboBox.setSelectedItem(editRez.getTipSobe().getOznaka());
            
            if (moc < 2) {
            comboBox.setEnabled(false);
            datePickerStart.getComponent(1).setEnabled(false); 
            datePickerEnd.getComponent(1).setEnabled(false); 
            }
            
            if (moc == 2) {
            	tfUserName.setText(editRez.getImeRezervacije().getKorisnickoIme());
            }
            
        }

        priceLbl = new JLabel("Cena: ");
        priceLbl.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        contentPane.add(priceLbl, gbc);

        JButton btnSacuvaj = new JButton("Sačuvaj");
        btnSacuvaj.setFont(new Font("Tahoma", Font.PLAIN, 16));

        btnSacuvaj.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (dodavanjeIzmena()) {
                        JOptionPane.showMessageDialog(RezervacijaAddEditDialog.this, "Rezervacija je uspešno sačuvana.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
                        RezervacijaAddEditDialog.this.dispose();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(RezervacijaAddEditDialog.this, "Došlo je do greške prilikom čuvanja rezervacije.", "Greška", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        contentPane.add(btnSacuvaj, gbc);

        JButton btnOdustani = new JButton("Odustani");
        btnOdustani.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnOdustani.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 8;
        contentPane.add(btnOdustani, gbc);

        comboBox.addActionListener(e -> updatePriceLabel());
        datePickerStart.addActionListener(e -> updatePriceLabel());
        datePickerEnd.addActionListener(e -> updatePriceLabel());

        for (JCheckBox checkBox : uslugeCheckBoxes) {
            checkBox.addActionListener(e -> updatePriceLabel());
        }

        setContentPane(contentPane);
    }


	private void updatePriceLabel() {
        try {
            double price = nadjiCenu();
            priceLbl.setText("Cena: " + price);
        } catch (Exception e) {
            priceLbl.setText("Cena: N/A");
        }
    }

    private ArrayList<JCheckBox> dodajUsluge(String[] listaUsluga, JPanel uslugePanel) {
        ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
        for (String usluga : listaUsluga) {
            JCheckBox cb = new JCheckBox(usluga);
            uslugePanel.add(cb);
            checkBoxes.add(cb);
        }
        return checkBoxes;
    }
    
    private ArrayList<JCheckBox> dodajOsobineSoba(String[] listaOsobina, JPanel osobinePanel) {
        ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
        for (String osobina : listaOsobina) {
            JCheckBox cb = new JCheckBox(osobina);
            osobinePanel.add(cb);
            checkBoxes.add(cb);
        }
        return checkBoxes;
    }


    private String generisiId() {
        int maxId = 0;
        for (Rezervacija rez : rezMng.getRezervacije()) {
            int currentId = Integer.parseInt(rez.getId());
            if (currentId > maxId) {
                maxId = currentId;
            }
        }
        return Integer.toString(maxId + 1);
    }

    private void dodajTipoveSoba(JComboBox<String> comboBox, ArrayList<TipSobe> tipoviSoba) {
        for (TipSobe ts : tipoviSoba) {
            comboBox.addItem(ts.getOznaka());
        }
    }

    private boolean dodavanjeIzmena() throws IOException {
        if (!validacija()) {
            return false;
        }

        String id = tfId.getText();
        String korisnickoIme = tfUserName.getText();
        LocalDate datumPocetka = ((java.sql.Date) datePickerStart.getModel().getValue()).toLocalDate();
        LocalDate datumKraja = ((java.sql.Date) datePickerEnd.getModel().getValue()).toLocalDate();

        TipSobe tipSobe = tipoviSoba.get(comboBox.getSelectedIndex());
        ArrayList<DodatnaUsluga> odabraneUsluge = dobaviDodatneUsluge();
        ArrayList<String> odabraneOsobine = dobaviOsobineSobe();

        if (editRez == null) {
            editRez = new Rezervacija(id, (Gost) korisnikMng.dobaviKorisnikaPoUsername(korisnickoIme), odabraneUsluge, tipSobe, odabraneOsobine, datumPocetka, datumKraja, cenMng.getCenovnici());
            rezMng.dodajRezervaciju(editRez);
        } else {
        	ArrayList <DodatnaUsluga> razlikaUsluga = new ArrayList<>();
        	for (DodatnaUsluga dodUsl : odabraneUsluge) {
        		if (!editRez.getDodatneUsluge().contains(dodUsl)) {
        			razlikaUsluga.add(dodUsl);
        		}
        	}
        	double cena = editRez.getCena();
        	for (DodatnaUsluga dodUsluga : razlikaUsluga) {
                cena += cenMng.getCenovnici().get(cenMng.getCenovnici().size() - 1).getCenaUsluge(dodUsluga.getNaziv()) * ChronoUnit.DAYS.between(datumPocetka, datumKraja);
        	}
        	Rezervacija nova = new Rezervacija(id, editRez.getStatusRezervacije(), (Gost) korisnikMng.dobaviKorisnikaPoUsername(korisnickoIme), odabraneUsluge, tipSobe, odabraneOsobine, datumPocetka, datumKraja, cena, editRez.getDatumKreiranja());
            rezMng.izmeniRezervaciju(nova, editRez);
        }

        uspesnoDodavanjeIliIzmena = true;

        return true;
    }

	private boolean validacija() {
    	if (tfId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID ne može biti prazan.", "Greška", JOptionPane.ERROR_MESSAGE);
    	}
    	
    	if (editRez == null) { // ako u pitanju nije edit
	    	if (!rezMng.proveriId(tfId.getText().trim())) {
	            JOptionPane.showMessageDialog(this, "Ovaj ID već postoji. Promenite ga.", "Greška", JOptionPane.ERROR_MESSAGE);
	            return false;
	    	}
    	}
    	
    	if (tfUserName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Korisničko ime ne može biti prazno.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (comboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Morate odabrati tip sobe.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (datePickerStart.getModel().getValue() == null) {
            JOptionPane.showMessageDialog(this, "Morate odabrati početni datum.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (datePickerEnd.getModel().getValue() == null) {
            JOptionPane.showMessageDialog(this, "Morate odabrati krajnji datum.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        java.sql.Date startDate = (java.sql.Date) datePickerStart.getModel().getValue();
        java.sql.Date endDate = (java.sql.Date) datePickerEnd.getModel().getValue();
        LocalDate datumPocetka = startDate.toLocalDate();
        LocalDate datumKraja = endDate.toLocalDate();

        if (datumPocetka.isAfter(datumKraja)) {
            JOptionPane.showMessageDialog(this, "Početni datum ne može biti posle krajnjeg datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
    
    
    private double nadjiCenu() {
        LocalDate datumPocetka = ((java.sql.Date) datePickerStart.getModel().getValue()).toLocalDate();
        LocalDate datumKraja = ((java.sql.Date) datePickerEnd.getModel().getValue()).toLocalDate();
        TipSobe tipSobe = tipoviSoba.get(comboBox.getSelectedIndex());

        ArrayList<DodatnaUsluga> odabraneUsluge = dobaviDodatneUsluge();

        
        double cena = 0;
	    for (LocalDate date = datumPocetka; date.isBefore(datumKraja); date = date.plusDays(1)) {
	        for (Cenovnik cenovnik : cenMng.getCenovnici()) {
	            LocalDate pocetak = cenovnik.getDatumPocetka();
	            LocalDate kraj = cenovnik.getDatumKraja();
	            
	            if ((date.isEqual(pocetak) || date.isAfter(pocetak)) && (date.isEqual(kraj) || date.isBefore(kraj))) {
	            	cena += cenovnik.getCenaTipaSobe(tipSobe.getOznaka());
	                for (DodatnaUsluga dodUsl : odabraneUsluge) {
	                	cena += cenovnik.getCenaUsluge(dodUsl.getNaziv());
	                }
	            }
	        }
    
    	}
            return cena;
    }
    
    private ArrayList<DodatnaUsluga> dobaviDodatneUsluge() {
        ArrayList<DodatnaUsluga> odabraneUsluge = new ArrayList<>();
        Component[] components = uslugePanel.getComponents(); // Iterate over uslugePanel components
        for (Component component : components) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                if (checkBox.isSelected()) {
                    odabraneUsluge.add(new DodatnaUsluga(checkBox.getText()));
                }
            }
        }
        return odabraneUsluge;
    }

    private ArrayList<String> dobaviOsobineSobe() {
        ArrayList<String> odabraneOsobine = new ArrayList<>();
        Component[] components = osobinePanel.getComponents(); // Iterate over osobinePanel components
        for (Component component : components) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                if (checkBox.isSelected()) {
                    odabraneOsobine.add(checkBox.getText());
                }
            }
        }
        return odabraneOsobine;
    }


    public boolean proveriUspesnost() {
        return uspesnoDodavanjeIliIzmena;
    }
}
