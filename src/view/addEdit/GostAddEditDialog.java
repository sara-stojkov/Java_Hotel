package view.addEdit;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Properties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import entities.Gost;
import entities.Recepcioner;
import entities.Sobarica;

import org.jdatepicker.impl.DateComponentFormatter;

import enums.NivoiStrucneSpreme;
import enums.Pol;
import enums.Uloga;
import managers.KorisnikManager;
import managers.ManagerFactory;
import view.RecepcionerView;

public class GostAddEditDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JFrame roditelj;
    private JDatePickerImpl datePickerBirthday;
	private KorisnikManager korisnikMng;
	private Gost editGost;
	private JTextField tfName;
    private JTextField tfUserName;
    private JTextField tfLastName;
	private JTextField tfPassword;
	private JTextField tfPhone;
	private JTextField tfAdress;
	private Pol selectedPol;

    public GostAddEditDialog(JFrame roditelj, ManagerFactory manager, Gost editGost) {
        super(roditelj, true);
        this.roditelj = roditelj;
        if (editGost != null) {
            setTitle("Izmena gosta");
        } else {
            setTitle("Dodavanje gosta");
        }
        this.korisnikMng = manager.getKorisnikMng();
        this.editGost = editGost;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setBounds(100, 100, 600, 700);
        initGostDialog();
        pack();
    }

    private void initGostDialog() {
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Ime");
        nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(nameLabel, gbc);

        this.tfName = new JTextField();
        tfName.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        contentPane.add(tfName, gbc);
        
        JLabel lastnameLabel = new JLabel("Prezime");
        lastnameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPane.add(lastnameLabel, gbc);

        this.tfLastName = new JTextField();
        tfLastName.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPane.add(tfLastName, gbc);
        
        JLabel polLabel = new JLabel("Pol");
        polLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 2;
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
        gbc.gridy = 2;
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
        gbc.gridy = 3;
        contentPane.add(usernameLabel, gbc);

        this.tfUserName = new JTextField();
        tfUserName.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPane.add(tfUserName, gbc);
        
        JLabel passwordLabel = new JLabel("Lozinka");
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPane.add(passwordLabel, gbc);

        this.tfPassword = new JTextField();
        tfPassword.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 4;
        contentPane.add(tfPassword, gbc); 

        JLabel DoBLabel = new JLabel("Datum rođenja");
        DoBLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 5;
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
        gbc.gridy = 5;
        contentPane.add(datePickerBirthday, gbc);

        JLabel phNumLabel = new JLabel("Broj telefona");
        phNumLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 6;
        contentPane.add(phNumLabel, gbc);

        this.tfPhone = new JTextField();
        tfPhone.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 6;
        contentPane.add(tfPhone, gbc);
        
        JLabel adressLbl = new JLabel("Adresa (bez zareza)");
        adressLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 7;
        contentPane.add(adressLbl, gbc);

        this.tfAdress = new JTextField();
        tfAdress.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 7;
        contentPane.add(tfAdress, gbc);

        
        if (editGost != null) {  // ako je edit u pitanju

        	tfName.setText(editGost.getIme());
        	tfLastName.setText(editGost.getPrezime());
        	tfUserName.setText(editGost.getKorisnickoIme());
        	tfUserName.setEditable(false);
        	tfPassword.setText(editGost.getLozinka());
        	if (editGost.getPol() == Pol.M) {
        		maleRadioButton.setSelected(true);
        		selectedPol = Pol.M;
        	}
        	else if (editGost.getPol() == Pol.Z) {
        		femaleRadioButton.setSelected(true);
        		selectedPol = Pol.Z;
        	}
        	
        	maleRadioButton.setEnabled(false);
        	femaleRadioButton.setEnabled(false);

        	SqlDateModel modelDoB = (SqlDateModel) datePickerBirthday.getModel();
            modelDoB.setValue(java.sql.Date.valueOf(editGost.getDatumRodjenja()));
        	
            tfPhone.setText(editGost.getBrojTelefona());
            tfAdress.setText(editGost.getAdresa());
        
        }


        JButton saveBtn = new JButton("Sačuvaj");
        saveBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        contentPane.add(saveBtn, gbc);
        
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validirajGosta(editGost != null)) {
                    String ime = tfName.getText();
                    String prezime = tfLastName.getText();
                    String korisnickoIme = tfUserName.getText();
                    String lozinka = tfPassword.getText();
                    String adresa = tfAdress.getText();
                    String brojTelefona = tfPhone.getText();
                    java.sql.Date birthDate = (java.sql.Date) datePickerBirthday.getModel().getValue();
                    LocalDate datumRodjenja = birthDate.toLocalDate();

                    if (editGost != null) {
                    	Gost stariGost = (Gost) korisnikMng.dobaviKorisnikaPoUsername(korisnickoIme);
	                	Gost noviGost = new Gost(ime, prezime, selectedPol, datumRodjenja, brojTelefona, adresa, korisnickoIme, lozinka);
						korisnikMng.izmeniKorisnika(noviGost, stariGost);                           
                        
                        JOptionPane.showMessageDialog(null, "Uspešno izmenjen gost!");
                    } else {
                    	Gost noviGost = new Gost(ime, prezime, selectedPol, datumRodjenja, brojTelefona, adresa, korisnickoIme, lozinka);
    					korisnikMng.dodajKorisnika(noviGost);
                        
                        JOptionPane.showMessageDialog(null, "Uspešno dodat gost!");
                    }
                    setVisible(false);
                    dispose();
                }
            }
        });
        

        JButton cancelBtn = new JButton("Otkaži");
        cancelBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        contentPane.add(cancelBtn, gbc);

        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setContentPane(contentPane);
    }



    private boolean validirajGosta(boolean edit) {
    	
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
        
        
        if (tfName.getText().trim().contains(",") || tfLastName.getText().trim().contains(",") || tfUserName.getText().trim().contains(",") || tfPassword.getText().trim().contains(",") || tfAdress.getText().trim().contains(",") || tfPhone.getText().trim().contains(",")) {
        	JOptionPane.showMessageDialog(this, "Ni jedno od polja ne sme da sadrži zarez (','). Izmenite ih u skladu sa ovim. ", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
}

