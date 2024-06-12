package view.addEdit;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import entities.*;
import managers.CenovnikManager;
import managers.DodatneUslugeManager;
import managers.ManagerFactory;
import managers.TipSobeManager;

public class CenovnikAddEditDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFrame roditelj;
	private Cenovnik editCenovnik;
	private CenovnikManager cenMng;
	private DodatneUslugeManager dodUslMng;
	private TipSobeManager tSobaMng;
	private JTextField tfId;
	private JTextField tfDatumPocetka;
	private JTextField tfDatumKraja;
	private JDatePickerImpl datePickerStart;
	private JDatePickerImpl datePickerEnd;
    private Map<JTextField, TipSobe> ceneTipovaSobaTextFields;
    private Map<JTextField, DodatnaUsluga> ceneUslugaTextFields;

	public CenovnikAddEditDialog(JFrame parent, ManagerFactory manager, Cenovnik editCenovnik) {
        super(parent, true);
        this.roditelj = parent;
        if (editCenovnik != null) {
            setTitle("Izmena cenovnika");
        } else {
            setTitle("Dodavanje cenovnika");
        }
        this.cenMng = manager.getCenovnikMng();
        this.editCenovnik = editCenovnik;
        this.tSobaMng = manager.getTipSobeMng();
        this.dodUslMng = manager.getDodatneUslugeMng();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setBounds(100, 100, 800, 900);
        initCenovnikDialog();
        pack();
    }

    public void initCenovnikDialog() {
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel idLabel = new JLabel("ID cenovnika");
        idLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(idLabel, gbc);

        this.tfId = new JTextField();
        tfId.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        contentPane.add(tfId, gbc);
        
        JLabel startDateLbl = new JLabel("Početni datum");
        startDateLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPane.add(startDateLbl, gbc);

        SqlDateModel modelStart = new SqlDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanelStart = new JDatePanelImpl(modelStart, p);
        this.datePickerStart = new JDatePickerImpl(datePanelStart, new DateComponentFormatter());
        datePickerStart.getJFormattedTextField().setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPane.add(datePickerStart, gbc);

        JLabel endDateLbl = new JLabel("Krajnji datum");
        endDateLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPane.add(endDateLbl, gbc);

        SqlDateModel modelEnd = new SqlDateModel();
        JDatePanelImpl datePanelEnd = new JDatePanelImpl(modelEnd, p);
        this.datePickerEnd = new JDatePickerImpl(datePanelEnd, new DateComponentFormatter());
        datePickerEnd.getJFormattedTextField().setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPane.add(datePickerEnd, gbc);

        ceneTipovaSobaTextFields = new HashMap<>();
        ceneUslugaTextFields = new HashMap<>();

        int row = 3;
        for (TipSobe ts : tSobaMng.getTipoviSoba()) {
            JLabel label = new JLabel("Cena za " + ts.getOznaka());
            label.setFont(new Font("Tahoma", Font.PLAIN, 18));
            gbc.gridx = 0;
            gbc.gridy = row;
            contentPane.add(label, gbc);

            JTextField tfCena = new JTextField();
            tfCena.setColumns(10);
            gbc.gridx = 1;
            gbc.gridy = row;
            contentPane.add(tfCena, gbc);

            ceneTipovaSobaTextFields.put(tfCena, ts);
            row++;
        }

        for (DodatnaUsluga du : dodUslMng.getDodatneUsluge()) {
            JLabel label = new JLabel("Cena za " + du.getNaziv());
            label.setFont(new Font("Tahoma", Font.PLAIN, 18));
            gbc.gridx = 0;
            gbc.gridy = row;
            contentPane.add(label, gbc);

            JTextField tfCena = new JTextField();
            tfCena.setColumns(10);
            gbc.gridx = 1;
            gbc.gridy = row;
            contentPane.add(tfCena, gbc);

            ceneUslugaTextFields.put(tfCena, du);
            row++;
        }
        

        if (editCenovnik != null) {
            tfId.setText(editCenovnik.getId());
            tfId.setEnabled(false);
            
            SqlDateModel model1Start = (SqlDateModel) datePickerStart.getModel();
            SqlDateModel model1End = (SqlDateModel) datePickerEnd.getModel();
            model1Start.setValue(java.sql.Date.valueOf(editCenovnik.getDatumPocetka()));
            model1End.setValue(java.sql.Date.valueOf(editCenovnik.getDatumKraja()));
            
            for (Map.Entry<JTextField, TipSobe> entry : ceneTipovaSobaTextFields.entrySet()) {
                JTextField textField = entry.getKey();
                TipSobe tipSobe = entry.getValue();
                Integer cena = editCenovnik.getCeneTipovaSoba().get(tipSobe.getOznaka());
                if (cena != null) {
                    textField.setText(cena.toString());
                } else {
                    System.out.println("Fali kljuc za: " + tipSobe.getOznaka());
                    textField.setText("");
                }
            }

            for (Map.Entry<JTextField, DodatnaUsluga> entry : ceneUslugaTextFields.entrySet()) {
                JTextField textField = entry.getKey();
                DodatnaUsluga usluga = entry.getValue();
                Integer cena = editCenovnik.getCeneUsluga().get(usluga.getNaziv());
                if (cena != null) {
                    textField.setText(cena.toString());
                } else {
                    System.out.println("Fali kljuc za: " + usluga.getNaziv());
                    textField.setText("");
                }
            }

        }

        JButton btnSacuvaj = new JButton("Sačuvaj");
        btnSacuvaj.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnSacuvaj.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validirajCenovnik(editCenovnik != null)) {
                    String id = tfId.getText().trim();
                    LocalDate datumPocetka = ((java.sql.Date) datePickerStart.getModel().getValue()).toLocalDate();
                    LocalDate datumKraja = ((java.sql.Date) datePickerEnd.getModel().getValue()).toLocalDate();
                    
                    HashMap<String, Integer> ceneTipovaSoba = new HashMap<>();
                    for (Map.Entry<JTextField, TipSobe> entry : ceneTipovaSobaTextFields.entrySet()) {
                        JTextField textField = entry.getKey();
                        TipSobe tipSobe = entry.getValue();
                        ceneTipovaSoba.put(tipSobe.getOznaka(), Integer.parseInt(textField.getText().trim()));
                    }

                    HashMap<String, Integer> ceneUsluga = new HashMap<>();
                    for (Map.Entry<JTextField, DodatnaUsluga> entry : ceneUslugaTextFields.entrySet()) {
                        JTextField textField = entry.getKey();
                        DodatnaUsluga usluga = entry.getValue();
                        ceneUsluga.put(usluga.getNaziv(), Integer.parseInt(textField.getText().trim()));
                    }   
                    
                    if (editCenovnik != null) {
					    Cenovnik noviCenovnik = new Cenovnik(id, datumPocetka, datumKraja, ceneTipovaSoba, ceneUsluga);
					    try {
							cenMng.izmeniCenovnik(noviCenovnik, editCenovnik);
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(null, "Došlo je do greške pri čuvaju izmena..", "Greška", JOptionPane.ERROR_MESSAGE);
						}
					    JOptionPane.showMessageDialog(null, "Uspešno izmenjen cenovnik!");
					} else {
					    Cenovnik noviCenovnik = new Cenovnik(id, datumPocetka, datumKraja, ceneTipovaSoba, ceneUsluga);
					    cenMng.dodajCenovnik(noviCenovnik);
					    JOptionPane.showMessageDialog(null, "Uspešno dodat cenovnik!");
					}
					setVisible(false);
					dispose();
                }
            }
        });
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        contentPane.add(btnSacuvaj, gbc);

        JButton btnOdustani = new JButton("Odustani");
        btnOdustani.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnOdustani.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                return;
            }
        });
        gbc.gridx = 0;
        gbc.gridy = row;
        contentPane.add(btnOdustani, gbc);

        setContentPane(contentPane);
    }

    private boolean validirajCenovnik(boolean isEdit) {
        if (!isEdit) {
            if (tfId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID cenovnika ne sme ostati prazno.", "Greška", JOptionPane.ERROR_MESSAGE);
                return false;
            }
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

        for (JTextField textField : ceneTipovaSobaTextFields.keySet()) {
            if (textField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Sve cene tipova soba moraju biti popunjene.", "Greška", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            try {
                Double.parseDouble(textField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Cena mora biti broj.", "Greška", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        for (JTextField textField : ceneUslugaTextFields.keySet()) {
            if (textField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Sve cene dodatnih usluga moraju biti popunjene.", "Greška", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            try {
                Double.parseDouble(textField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Cena mora biti broj.", "Greška", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }
}