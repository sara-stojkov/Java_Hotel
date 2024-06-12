package view.addEdit;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import entities.Recepcioner;
import entities.Sobarica;
import entities.Zaposleni;
import enums.NivoiStrucneSpreme;
import enums.Pol;
import enums.Uloga;
import managers.KorisnikManager;
import managers.ManagerFactory;

public class ZaposleniAddEditDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFrame roditelj;
	private Zaposleni editZaposleni;
	private KorisnikManager korisnikMng;
	private JTextField tfName;
	private JTextField tfLastName;
	private Pol selectedPol;
	private JTextField tfUserName;
	private JTextField tfPassword;
	private JDatePickerImpl datePickerBirthday;
	private JTextField tfPhone;
	private JTextField tfAdress;
	private JTextField tfExperience;
	private JComboBox comboBox;
	private Uloga selectedUloga = Uloga.RECEPCIONER;

	public ZaposleniAddEditDialog(JFrame roditelj, ManagerFactory manager, Zaposleni editZap) {
        super(roditelj, true);
        this.roditelj = roditelj;
        if (editZap != null) {
            setTitle("Izmena zaposlenog");
        } else {
            setTitle("Dodavanje zaposlenog");
        }
        this.korisnikMng = manager.getKorisnikMng();
        this.editZaposleni = editZap;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setBounds(100, 100, 600, 700);
        initZaposleniDialog();
        pack();
    } 
	
	private void initZaposleniDialog() {
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel roleLabel = new JLabel("Radno mesto");
        roleLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(roleLabel, gbc);

        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup roleButtonGroup = new ButtonGroup();
        
        JRadioButton receptionistButton = new JRadioButton("Recepcioner");
        receptionistButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        rolePanel.add(receptionistButton);
        
        JRadioButton roomkeeperButton = new JRadioButton("Sobarica");
        roomkeeperButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        rolePanel.add(roomkeeperButton);
        
        roleButtonGroup.add(receptionistButton);
        roleButtonGroup.add(roomkeeperButton);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        contentPane.add(rolePanel, gbc);
        
        receptionistButton.setSelected(true);

        
        receptionistButton.addActionListener(e -> {
            if (receptionistButton.isSelected()) {
                this.selectedUloga = Uloga.RECEPCIONER;
            }
            
        });

        roomkeeperButton.addActionListener(e -> {
            if (roomkeeperButton.isSelected()) {
                this.selectedUloga = Uloga.SOBARICA;
            }
        });

        JLabel nameLabel = new JLabel("Ime");
        nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPane.add(nameLabel, gbc);

        this.tfName = new JTextField();
        tfName.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPane.add(tfName, gbc);
        
        JLabel lastnameLabel = new JLabel("Prezime");
        lastnameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPane.add(lastnameLabel, gbc);

        this.tfLastName = new JTextField();
        tfLastName.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPane.add(tfLastName, gbc);
        
        JLabel polLabel = new JLabel("Pol");
        polLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPane.add(polLabel, gbc);

        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup polButtonGroup = new ButtonGroup();
        
        JRadioButton maleRadioButton = new JRadioButton("M");
        maleRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        genderPanel.add(maleRadioButton);
        
        JRadioButton femaleRadioButton = new JRadioButton("Z");
        femaleRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        genderPanel.add(femaleRadioButton);
        
        polButtonGroup.add(femaleRadioButton);
        polButtonGroup.add(maleRadioButton);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPane.add(genderPanel, gbc);
        
        maleRadioButton.addActionListener(e -> {
            if (maleRadioButton.isSelected()) {
                selectedPol = Pol.M;
            }
        });

        femaleRadioButton.addActionListener(e -> {
            if (femaleRadioButton.isSelected()) {
            	selectedPol = Pol.Z;
            }
        });
        
        JLabel usernameLabel = new JLabel("Korisnicko ime");
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPane.add(usernameLabel, gbc);

        this.tfUserName = new JTextField();
        tfUserName.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 4;
        contentPane.add(tfUserName, gbc);
        
        JLabel passwordLabel = new JLabel("Lozinka");
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 5;
        contentPane.add(passwordLabel, gbc);

        this.tfPassword = new JTextField();
        tfPassword.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 5;
        contentPane.add(tfPassword, gbc); 

        JLabel DoBLabel = new JLabel("Datum rođenja");
        DoBLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 6;
        contentPane.add(DoBLabel, gbc);

        SqlDateModel modelStart = new SqlDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanelStart = new JDatePanelImpl(modelStart, p);
        this.datePickerBirthday = new JDatePickerImpl(datePanelStart, new DateComponentFormatter());
        datePickerBirthday.getJFormattedTextField().setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.gridy = 6;
        contentPane.add(datePickerBirthday, gbc);

        JLabel phNumLabel = new JLabel("Broj telefona");
        phNumLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 7;
        contentPane.add(phNumLabel, gbc);

        this.tfPhone = new JTextField();
        tfPhone.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 7;
        contentPane.add(tfPhone, gbc);
        
        JLabel adressLbl = new JLabel("Adresa (bez zareza)");
        adressLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 8;
        contentPane.add(adressLbl, gbc);

        this.tfAdress = new JTextField();
        tfAdress.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 8;
        contentPane.add(tfAdress, gbc);
        
        JLabel educationLbl = new JLabel("Nivo spreme");
        educationLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
        tfPhone.setColumns(10);
        gbc.gridx = 0;
        gbc.gridy = 9;
        contentPane.add(educationLbl, gbc);

        this.comboBox = new JComboBox<String>();
        gbc.gridx = 1;
        gbc.gridy = 9;
        contentPane.add(comboBox, gbc);
        
        dodajNivoeSpreme(comboBox);
        
        JLabel experienceLbl = new JLabel("Godina staža");
        experienceLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 10;
        contentPane.add(experienceLbl, gbc);

        this.tfExperience = new JTextField();
        tfExperience.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 10;
        contentPane.add(tfExperience, gbc);

        if (editZaposleni != null) {
        	tfName.setText(editZaposleni.getIme());
        	tfLastName.setText(editZaposleni.getPrezime());
        	tfUserName.setText(editZaposleni.getKorisnickoIme());
        	tfUserName.setEditable(false);
        	tfPassword.setText(editZaposleni.getLozinka());
        	if (editZaposleni.getPol() == Pol.M) {
        		maleRadioButton.setSelected(true);
        		selectedPol = Pol.M;
        	}
        	else if (editZaposleni.getPol() == Pol.Z) {
        		femaleRadioButton.setSelected(true);
        		selectedPol = Pol.Z;
        	}
        	
        	maleRadioButton.setEnabled(false);
        	femaleRadioButton.setEnabled(false);

        	
        	if (editZaposleni.getUloga() == Uloga.RECEPCIONER) {
        		receptionistButton.setSelected(true);
        		selectedUloga = Uloga.RECEPCIONER;
        	}
        	else if (editZaposleni.getUloga() == Uloga.SOBARICA) {
        		roomkeeperButton.setSelected(true);
        		selectedUloga = Uloga.SOBARICA;
        	}
        	
        	receptionistButton.setEnabled(false);
        	roomkeeperButton.setEnabled(false);
        	
        	SqlDateModel modelDoB = (SqlDateModel) datePickerBirthday.getModel();
            modelDoB.setValue(java.sql.Date.valueOf(editZaposleni.getDatumRodjenja()));
        	
            tfPhone.setText(editZaposleni.getBrojTelefona());
            tfAdress.setText(editZaposleni.getAdresa());
            
            comboBox.setSelectedItem(editZaposleni.getNivoStrucneSpreme().toString());
            tfExperience.setText(String.valueOf(editZaposleni.getStaz()));
        }

        

        JButton saveBtn = new JButton("Sačuvaj");
        saveBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        contentPane.add(saveBtn, gbc);
        
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validirajZaposlenog(editZaposleni != null)) {
                    String ime = tfName.getText();
                    String prezime = tfLastName.getText();
                    String korisnickoIme = tfUserName.getText();
                    String lozinka = tfPassword.getText();
                    String adresa = tfAdress.getText();
                    String brojTelefona = tfPhone.getText();
                    java.sql.Date birthDate = (java.sql.Date) datePickerBirthday.getModel().getValue();
                    LocalDate datumRodjenja = birthDate.toLocalDate();
                    NivoiStrucneSpreme nivoSpreme = NivoiStrucneSpreme.fromInteger(comboBox.getSelectedIndex() + 1);
                    int staz = Integer.parseInt(tfExperience.getText());

                    if (editZaposleni != null) {
                        if (selectedUloga == Uloga.SOBARICA) {
                            Sobarica novaSobarica = new Sobarica(ime, prezime, selectedPol, datumRodjenja, brojTelefona, adresa, korisnickoIme, lozinka, nivoSpreme, staz);
                            korisnikMng.izmeniKorisnika(novaSobarica, editZaposleni);
                        } else if (selectedUloga == Uloga.RECEPCIONER) {
                            Recepcioner noviRecepcioner = new Recepcioner(ime, prezime, selectedPol, datumRodjenja, brojTelefona, adresa, korisnickoIme, lozinka, nivoSpreme, staz);
                            korisnikMng.izmeniKorisnika(noviRecepcioner, editZaposleni);
                        }
                        JOptionPane.showMessageDialog(null, "Uspešno izmenjen zaposleni!");
                    } else {
                        if (selectedUloga == Uloga.SOBARICA) {
                            Sobarica novaSobarica = new Sobarica(ime, prezime, selectedPol, datumRodjenja, brojTelefona, adresa, korisnickoIme, lozinka, nivoSpreme, staz);
                            korisnikMng.dodajKorisnika(novaSobarica);
                        } else if (selectedUloga == Uloga.RECEPCIONER) {
                            Recepcioner noviRecepcioner = new Recepcioner(ime, prezime, selectedPol, datumRodjenja, brojTelefona, adresa, korisnickoIme, lozinka, nivoSpreme, staz);
                            korisnikMng.dodajKorisnika(noviRecepcioner);
                        }
                        JOptionPane.showMessageDialog(null, "Uspešno dodat zaposleni!");
                    }
                    setVisible(false);
                    dispose();
                }
            }
        });

             

        JButton cancelBtn = new JButton("Otkaži");
        cancelBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        contentPane.add(cancelBtn, gbc);

        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setContentPane(contentPane);
    }



    private void dodajNivoeSpreme(JComboBox<String> comboBoxX) {
    	for (String nss : NivoiStrucneSpreme.opis) {
            comboBoxX.addItem(nss);
        }		
	}

	private boolean validirajZaposlenog(Boolean edit) {
    	
    	if (tfName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ime ne može biti prazno.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    	
    	if (tfLastName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Prezime ne može biti prazno.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    	
    	if (tfUserName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Korisničko ime ne može biti prazno.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    	

        if (tfPassword.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lozinka ne može biti prazna.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (tfAdress.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adresa ne može biti prazna.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (tfPhone.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Broj telefona ne može biti prazan.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (datePickerBirthday.getModel().getValue() == null) {
            JOptionPane.showMessageDialog(this, "Morate odabrati krajnji datum.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        java.sql.Date datumRodj = (java.sql.Date) datePickerBirthday.getModel().getValue();
        LocalDate datumRodjenja = datumRodj.toLocalDate();

        if (datumRodjenja.isAfter(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "Datum rođenja ne može biti nakon trenutnog datuma.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (! edit) {
	        if (!korisnikMng.validirajUsername(tfUserName.getText().trim())) {
	            JOptionPane.showMessageDialog(this, "Ovo korisničko ime je zauzeto.", "Greška", JOptionPane.ERROR_MESSAGE);
	            return false;
	        }
        }
        
        if (tfName.getText().trim().contains(",") || tfLastName.getText().trim().contains(",") || tfUserName.getText().trim().contains(",") || tfPassword.getText().trim().contains(",") || tfAdress.getText().trim().contains(",") || tfPhone.getText().trim().contains(",") || tfExperience.getText().trim().contains(",")) {
        	JOptionPane.showMessageDialog(this, "Ni jedno od polja ne sme da sadrži zarez (','). Izmenite ih u skladu sa ovim. ", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (tfExperience.getText().trim().isEmpty()) {
        	JOptionPane.showMessageDialog(this, "Broj godina staža ne može biti prazan.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
        	int i = Integer.parseInt(tfExperience.getText().trim());
        	if (i < 0) {
            	JOptionPane.showMessageDialog(this, "Broj godina staža mora biti POZITIVAN.", "Greška", JOptionPane.ERROR_MESSAGE);
        		return false;
        	}
        }
        catch (Exception e) {
        	JOptionPane.showMessageDialog(this, "Broj godina staža mora biti BROJ.", "Greška", JOptionPane.ERROR_MESSAGE);
        	return false;
        }
        
        return true;
    }
    
}
