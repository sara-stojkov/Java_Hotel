package reports;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import entities.Rezervacija;
import entities.Zaposleni;
import enums.StatusRezervacije;
import managers.KorisnikManager;
import managers.RezervacijaManager;

public class PrihodiRashodi {
	
	private static double dobaviPrihode(ArrayList <Rezervacija> sveRezervacije, LocalDate pocetak, LocalDate kraj) {
		double prihodi = 0;
		for (Rezervacija rez : sveRezervacije) {
			if (rez.getStatusRezervacije() == StatusRezervacije.ODBIJENA) {
				continue;
			}
			if ((rez.getDatumKreiranja().isAfter(pocetak) && rez.getDatumKreiranja().isBefore(kraj)) || rez.getDatumKreiranja().isEqual(pocetak) || rez.getDatumKreiranja().isEqual(kraj)) {
				prihodi += rez.getCena();
			}
		}
		return prihodi;
	}
	
	private static double dobaviRashode(ArrayList <Zaposleni> sviZaposleni, LocalDate pocetak, LocalDate kraj) {
		double rashodi = 0;
		long razlikaUDanima = ChronoUnit.DAYS.between(pocetak, kraj);
//		System.out.println("dana izmedju: " + razlikaUDanima);
		for (Zaposleni zap : sviZaposleni) {
			rashodi += (zap.getPlata()/30) * razlikaUDanima;
		}
		return rashodi;
	}
	
	public static Object[][] formatirajPrihodeRashode(RezervacijaManager rezMng, KorisnikManager korMng, LocalDate pocetak, LocalDate kraj){
		double prihodi = dobaviPrihode(rezMng.getRezervacije(), pocetak, kraj);
		double rashodi = dobaviRashode(korMng.getZaposleni(), pocetak, kraj);
		Object [][] lista = {{prihodi, rashodi, prihodi - rashodi}};
		return lista;
	}

}
