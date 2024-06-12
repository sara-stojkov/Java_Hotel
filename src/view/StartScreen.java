package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.*;

import managers.KorisnikManager;
import managers.ManagerFactory;

import javax.swing.border.EmptyBorder;

import entities.*;

public class StartScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfkorisnickoIme;
    private JPasswordField pflozinka;
    private ArrayList<Korisnik> listaKorisnika;

    private ManagerFactory managers;
	protected JButton loginButton;

	public StartScreen(ManagerFactory managers) {
		
		this.managers = managers;

		InitStartScreen();
	}

    public void InitStartScreen() {

    	managers.getKorisnikMng();
		listaKorisnika = managers.getKorisnikMng().getKorisnici();
		
    	setResizable(false);
        setIconImage(new ImageIcon("./img/main.jpg").getImage());

        setTitle("Sarin hotel - PRIJAVA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 594, 377);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        tfkorisnickoIme = new JTextField();
        tfkorisnickoIme.setFont(new Font("Tahoma", Font.PLAIN, 18));
        tfkorisnickoIme.setBounds(230, 128, 223, 36);
        contentPane.add(tfkorisnickoIme);
        tfkorisnickoIme.setColumns(10);
        
        
        tfkorisnickoIme.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    pflozinka.requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        JLabel lblNewLabel_1 = new JLabel("Korisničko ime");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblNewLabel_1.setBounds(59, 126, 141, 39);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel = new JLabel("Lozinka");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblNewLabel.setBounds(59, 189, 132, 38);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_2 = new JLabel("Dobrodošli u Sarin hotel!");
        lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblNewLabel_2.setBounds(10, 10, 560, 67);
        contentPane.add(lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel("Unesite svoje podatke: ");
        lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblNewLabel_3.setBounds(178, 66, 229, 36);
        contentPane.add(lblNewLabel_3);

        pflozinka = new JPasswordField();
        pflozinka.setFont(new Font("Tahoma", Font.PLAIN, 18));
        pflozinka.setBounds(230, 191, 223, 36);
        contentPane.add(pflozinka);
        
        pflozinka.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });

        JButton cancelButton = new JButton("Izađi");
        cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 24));
        cancelButton.setBounds(91, 267, 141, 47);
        contentPane.add(cancelButton);

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                contentPane.setVisible(false);
                dispose();
            }
        });

        this.loginButton = new JButton("Prijavi se");
        loginButton.setFont(new Font("Tahoma", Font.PLAIN, 24));
        loginButton.setBounds(330, 267, 141, 47);
        contentPane.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String korisnickoIme = tfkorisnickoIme.getText().trim();
                String lozinka = new String(pflozinka.getPassword()).trim();
//                System.out.printf("Korisnicko ime: %s ; lozinka: %s\n", korisnickoIme, lozinka);
                if (korisnickoIme.isEmpty() && lozinka.isEmpty()) {
                    JOptionPane.showMessageDialog(StartScreen.this, "Niste uneli nikakve podatke.");
                    return;
                }
                
                else if (korisnickoIme.isEmpty() || lozinka.isEmpty()) {
                	JOptionPane.showMessageDialog(StartScreen.this, "Sva polja moraju biti popunjena.");
                    return;
                }

                Korisnik korisnik = KorisnikManager.login(korisnickoIme, lozinka, listaKorisnika);

                if (korisnik == null) {
                    JOptionPane.showMessageDialog(StartScreen.this, "Neuspela prijava na sistem.");
                    tfkorisnickoIme.setText("");
                    pflozinka.setText("");
                    tfkorisnickoIme.grabFocus();
                    tfkorisnickoIme.requestFocus();
  
                } else {
                    JOptionPane.showMessageDialog(StartScreen.this, "Uspešno ste se ulogovali.");
//                    System.out.println("login uspešan \n\n");
                    switch (korisnik.getUloga()) {
                        case SOBARICA:
                            System.out.println("Ulazimo u prozor sobarice...");
                            contentPane.setVisible(false);
                            dispose();
                            SobaricaView sobarica = new SobaricaView(managers, korisnik.getKorisnickoIme());
                            sobarica.setVisible(true);
                            break;
                        case ADMINISTRATOR:
                            System.out.println("Ulazimo u prozor administratora...");
                            contentPane.setVisible(false);
                            dispose();
                            AdministratorView admin = new AdministratorView(managers, korisnik.getKorisnickoIme());
                            admin.setVisible(true);                            
                            break;
                        case RECEPCIONER:
                            System.out.println("Ulazimo u prozor recepcionera...");
                            contentPane.setVisible(false);
                            dispose();
                            RecepcionerView recepcioner = new RecepcionerView(managers, korisnik.getKorisnickoIme());
                            recepcioner.setVisible(true);
                            break;
                        case GOST:
                            System.out.println("Ulazimo u prozor gosta...");
                            contentPane.setVisible(false);
                            dispose();
                            GostView gost = new GostView(managers, korisnik.getKorisnickoIme());
                            gost.setVisible(true);
                            contentPane.setVisible(true);
                            break;
                        default:
                            JOptionPane.showMessageDialog(StartScreen.this, "Vaša uloga u sistemu nije validna, program se gasi...");
                            contentPane.setVisible(false);
                            dispose();
                            break;
                    }
                }
            }
        });
    }
}


