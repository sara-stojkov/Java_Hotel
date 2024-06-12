package view.addEdit;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import entities.Soba;
import entities.TipSobe;
import managers.ManagerFactory;
import managers.SobaManager;
import managers.TipSobeManager;

public class SobaAddEditDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private Object roditelj;
	private TipSobeManager tSobaMng;
	private SobaManager sobaMng;
	private JPanel contentPane;
	private JTextField tfNumber;
	private JComboBox comboBox;
	private Object osobineCheckBoxes;
	private Soba editSoba;


	public SobaAddEditDialog(JFrame parent, ManagerFactory manager, Soba editSoba) {
		super(parent, true);
        this.roditelj = parent;
        if (editSoba != null) {
            setTitle("Izmena sobe");
        } else {
            setTitle("Dodavanje sobe");
        }
        this.tSobaMng = manager.getTipSobeMng();
        this.sobaMng = manager.getSobaMng(); 
        this.editSoba = editSoba;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setBounds(100, 100, 700, 800);
        initSobaDialog();
        pack();
	}
	public void initSobaDialog() {
		contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel numLabel = new JLabel("Broj sobe");
        numLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(numLabel, gbc);

        this.tfNumber = new JTextField();
        tfNumber.setColumns(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        contentPane.add(tfNumber, gbc);
        
        JLabel roomTypeLabel = new JLabel("Tip sobe");
        roomTypeLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPane.add(roomTypeLabel, gbc);

        this.comboBox = new JComboBox<>();
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPane.add(comboBox, gbc);

        dodajTipoveSoba(comboBox, tSobaMng.getTipoviSoba());
        
        JLabel roomCharLbl = new JLabel("Osobine sobe: ");
        roomCharLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        contentPane.add(roomCharLbl, gbc);

        JPanel characteristicsPanel = new JPanel();
        characteristicsPanel.setLayout(new BoxLayout(characteristicsPanel, BoxLayout.Y_AXIS));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        contentPane.add(characteristicsPanel, gbc);

        osobineCheckBoxes = dodajOsobine(sobaMng.getSveOsobineSoba(), characteristicsPanel);
        
        if (editSoba != null) {
        	tfNumber.setText(String.valueOf(editSoba.getBrojSobe()));
        	tfNumber.setEnabled(false); // ovo je identifikacija, nema potrebe da se menja
            comboBox.setSelectedItem(editSoba.getTipSobe().getOznaka());

            Component[] components = characteristicsPanel.getComponents();
            for (Component component : components) {
                if (component instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) component;
                    for (String osobina : editSoba.getOsobineSobe()) {
                        if (checkBox.getText().equals(osobina)) {
                            checkBox.setSelected(true);
                            break;
                        }
                    }
                }
            }
        }
        
        
        JButton btnSacuvaj = new JButton("Sačuvaj");
        btnSacuvaj.setFont(new Font("Tahoma", Font.PLAIN, 16));

        btnSacuvaj.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                if (validirajSobu(editSoba != null)) {
                    String br = tfNumber.getText();
                    int brojSobe = Integer.parseInt(br);
                    
                    TipSobe tipSobe = tSobaMng.getTipoviSoba().get(comboBox.getSelectedIndex());

                    ArrayList<String> odabraneOsobine = dobaviOdabraneOsobine();

                    try {
                    if (editSoba != null) {
                    	Soba staraSoba = sobaMng.dobaviSobuPoBroju(brojSobe);
	                	Soba novaSoba = new Soba(tipSobe, brojSobe, odabraneOsobine);
						sobaMng.izmeniSobu(novaSoba, staraSoba);                           
                        
                        JOptionPane.showMessageDialog(null, "Uspešno izmenjena soba!");
                    } else {
                    	Soba novaSoba = new Soba(tipSobe, brojSobe, odabraneOsobine);
    					sobaMng.dodajSobu(novaSoba);
                        
                        JOptionPane.showMessageDialog(null, "Uspešno dodata soba!");
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
        gbc.gridy = 5;
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
        gbc.gridy = 5;
        contentPane.add(btnOdustani, gbc);
        
        setContentPane(contentPane);

	}
	
	
	private ArrayList<String> dobaviOdabraneOsobine() {
		ArrayList<String> odabraneOsobine = new ArrayList<>();
        Component[] components = contentPane.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                Component[] nestedComponents = ((JPanel) component).getComponents();
                for (Component nestedComponent : nestedComponents) {
                    if (nestedComponent instanceof JCheckBox) {
                        JCheckBox checkBox = (JCheckBox) nestedComponent;
                        if (checkBox.isSelected()) {
                            odabraneOsobine.add(checkBox.getText());
                        }
                    }
                }
            }
        }
        return odabraneOsobine;
	}

	private boolean validirajSobu(boolean b) {
		if (editSoba == null) {
			if (!sobaMng.proveriZauzetostBrojaSobe(Integer.parseInt(tfNumber.getText().trim()))) {
				JOptionPane.showMessageDialog(this, "Ovaj broj sobe je zauzet.", "Greška", JOptionPane.ERROR_MESSAGE);
	            return false;
			}
			if (tfNumber.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Broj sobe ne sme ostati prazan.", "Greška", JOptionPane.ERROR_MESSAGE);
	            return false;
			}
		}
		
		return true;
	}
	private void dodajTipoveSoba(JComboBox<String> comboBox, ArrayList<TipSobe> tipoviSoba) {
        for (TipSobe ts : tipoviSoba) {
            comboBox.addItem(ts.getOznaka());
        }
    }
	
	private ArrayList<JCheckBox> dodajOsobine(String[] listaOsobina, JPanel osobinePanel) {
        ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
        for (String karakteristika : listaOsobina) {
            JCheckBox cb = new JCheckBox(karakteristika);
            osobinePanel.add(cb);
            checkBoxes.add(cb);
        }
        return checkBoxes;
    }

}
