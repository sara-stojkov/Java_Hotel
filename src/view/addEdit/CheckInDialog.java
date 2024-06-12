package view.addEdit;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import entities.Soba;
import enums.StatusRezervacije;
import enums.StatusSobe;
import managers.ManagerFactory;
import managers.RezervacijaManager;
import managers.SobaManager;

public class CheckInDialog {

	public static void showCheckInDialog(JFrame parent, ManagerFactory managers, String idRezervacije) {
		SobaManager sobaMng = managers.getSobaMng();
		RezervacijaManager rezMng = managers.getRezervacijaMng();
		
		ArrayList<Soba> listaOdgSoba = rezMng.dobaviSlobodneSobe(idRezervacije);
		String[] brojeviSoba = rezMng.formatirajBrSoba(listaOdgSoba);
		if (brojeviSoba.length == 0) {
			JOptionPane.showMessageDialog(parent, "S obzirom da nema slobodih soba trenutno, ne može se uraditi check-in. (kontaktirajte sobarice)", "Obaveštenje", JOptionPane.PLAIN_MESSAGE);
			return;
		}
		final JComboBox<String> combo = new JComboBox<>(brojeviSoba);
		
		String[] options = { "OK", "Cancel" };

		
		String title = "Check-in gosta - Odaberi sobu";
		int selection = JOptionPane.showOptionDialog(parent, combo, title,
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

		if (selection == JOptionPane.OK_OPTION) {
			Object broj = combo.getSelectedItem();
			if (broj != null) {
				String brojSobe = (String) broj;
				System.out.println("izabrana soba: " + broj);
				rezMng.promeniStatusRezervacije(idRezervacije, StatusRezervacije.U_TOKU);
				rezMng.dobaviRezervacijuPoId(idRezervacije).setSoba(sobaMng.dobaviSobuPoBroju(Integer.parseInt(brojSobe)));
				sobaMng.izmeniStatusSobe(Integer.parseInt(brojSobe), StatusSobe.ZAUZETO);
			}
		} else {
			System.out.println("Ne radimo ništa, stisnut je Cancel");
		}
	}
}
