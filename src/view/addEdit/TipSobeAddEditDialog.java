package view.addEdit;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import entities.TipSobe;
import managers.ManagerFactory;
import managers.TipSobeManager;

public class TipSobeAddEditDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JFrame roditelj;
	private TipSobeManager tSobaMng;
	private TipSobe editTipSobe;
	private JPanel contentPane;
	private JTextField tfId;
	private JTextField tfName;
	private JTextField tfCapacity;
	public boolean additionSuccess = false;

	
	public TipSobeAddEditDialog(JFrame parent, ManagerFactory manager, TipSobe editTipSobe) {
		super(parent, true);
        this.roditelj = parent;
        if (editTipSobe != null) {
            setTitle("Izmena tipa sobe");
        } else {
            setTitle("Dodavanje tipa sobe");
        }
        this.tSobaMng = manager.getTipSobeMng();
        this.editTipSobe = editTipSobe;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setBounds(100, 100, 700, 800);
        initTipSobeDialog();
        pack();
	}
	
	public void initTipSobeDialog() {
		contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel numLabel = new JLabel("Oznaka sobe");
        numLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(numLabel, gbc);

        this.tfId = new JTextField();
        tfId.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        contentPane.add(tfId, gbc);
        
        JLabel roomTypeLabel = new JLabel("Naziv tipa sobe");
        roomTypeLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPane.add(roomTypeLabel, gbc);

        this.tfName = new JTextField();
        tfName.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPane.add(tfName, gbc);
        
        JLabel capacityLbl = new JLabel("Kapacitet (max broj osoba): ");
        capacityLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPane.add(capacityLbl, gbc);

        this.tfCapacity = new JTextField();
        tfCapacity.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPane.add(tfCapacity, gbc);
        
        if (editTipSobe != null) {
        	tfId.setText(editTipSobe.getOznaka());
        	tfId.setEnabled(false); // ovo je identifikacija, nema potrebe da se menja
        	tfName.setText(editTipSobe.getNaziv());
        	tfCapacity.setText(String.valueOf(editTipSobe.getBrojOsoba()));
        }
        
        
        JButton btnSacuvaj = new JButton("Sačuvaj");
        btnSacuvaj.setFont(new Font("Tahoma", Font.PLAIN, 16));

        btnSacuvaj.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                if (validirajTipSobe(editTipSobe != null)) {
                    String kap = tfCapacity.getText().trim();
                    int kapacitet = Integer.parseInt(kap);
                    
                    String oznaka = tfId.getText().trim();
                    String naziv = tfName.getText().trim();
                    
                    try {
	                    if (editTipSobe != null) {
	                    	TipSobe stariTipSobe = tSobaMng.dobaviTipPoOznaci(oznaka);
	                    	TipSobe noviTipSobe = new TipSobe(naziv, oznaka, kapacitet);
							tSobaMng.izmeniTipSobe(noviTipSobe, stariTipSobe);                           
	                        
	                        JOptionPane.showMessageDialog(null, "Uspešno izmenjen tip sobe!");
                    } else {
                    	TipSobe noviTipSobe = new TipSobe(naziv, oznaka, kapacitet);
                    	tSobaMng.dodajTipSobe(noviTipSobe);
                        additionSuccess = true;
                        JOptionPane.showMessageDialog(null, "Uspešno dodat tip sobe!");
                    }
                    setVisible(false);
                    dispose();
                    }
                    catch(IOException e2) {
                        JOptionPane.showMessageDialog(null, "Došlo je do greške pri dodavanju/izmeni soba...");
                    }
                }
            }


        });
        gbc.gridx = 1;
        gbc.gridy = 3;
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
        gbc.gridy = 3;
        contentPane.add(btnOdustani, gbc);
        
        setContentPane(contentPane);

	}
	
	
	

	private boolean validirajTipSobe(boolean b) {
		if (editTipSobe == null) {
			if (!tSobaMng.proveriZauzetostOznake(tfId.getText().trim())) {
				JOptionPane.showMessageDialog(this, "Ova oznaka tipa sobe je zauzeta.", "Greška", JOptionPane.ERROR_MESSAGE);
	            return false;
			}
			if (tfId.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Oznaka tipa sobe ne sme ostati prazna.", "Greška", JOptionPane.ERROR_MESSAGE);
	            return false;
			}
		}
		
		if (tfCapacity.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kapacitet ne može biti prazan.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (tfName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Naziv tipa sobe ne sme da bude prazan.", "Greška", JOptionPane.ERROR_MESSAGE);
            return false;
        }
		
		return true;
	}

}
