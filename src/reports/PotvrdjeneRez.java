package reports;

import java.time.LocalDate;
import java.util.ArrayList;

import entities.Rezervacija;
import enums.StatusRezervacije;
import managers.RezervacijaManager;

public class PotvrdjeneRez {

	private static int dobaviBrRez(ArrayList <Rezervacija> sveRezervacije, LocalDate pocetak, LocalDate kraj) {
		int brojRez = 0;
		for (Rezervacija rez : sveRezervacije) {
			if (rez.getStatusRezervacije() == StatusRezervacije.POTVRĐENA && (pocetak.isEqual(rez.getDatumPocetka()) || kraj.isEqual(rez.getDatumPocetka()) || 
					kraj.isEqual(rez.getDatumPocetka()) || kraj.isEqual(rez.getDatumKraja()) || (rez.getDatumPocetka().isAfter(pocetak) && rez.getDatumPocetka().isBefore(kraj)) 
							|| (rez.getDatumKraja().isAfter(pocetak) && rez.getDatumKraja().isBefore(kraj)))) {
				brojRez ++;
			}
		}
		return brojRez;
	}
	
	public static Object[][] formatirajPrihodeRashode(RezervacijaManager rezMng, LocalDate pocetak, LocalDate kraj){
		int potvrRez = dobaviBrRez(rezMng.getRezervacije(), pocetak, kraj);
		Object [][] lista = {{pocetak, kraj, potvrRez}};
		return lista;
	}
	
}
