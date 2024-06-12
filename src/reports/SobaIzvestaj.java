package reports;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import entities.Rezervacija;
import entities.Soba;
import managers.RezervacijaManager;
import managers.SobaManager;

public class SobaIzvestaj {
	
//	za prikaz soba, što podrazumeva prikaz podataka o sobi i njenom tipu, ukupan broj
//	noćenja u toj sobi za izabrani opseg datuma, kao i ukupne prihode iz svake sobe na
//	osnovu izabranog opsega datuma
	
	public static Object[][] nadjiPodatkeOSobama(SobaManager sobaMng, RezervacijaManager rezMng, LocalDate pocetak, LocalDate kraj){
		ArrayList <Soba> listaSoba = sobaMng.getSobe();
		Object[][] konacnaLista = new Object[listaSoba.size()][4];
		
		for (int i = 0; i < listaSoba.size(); i++) {
			konacnaLista[i][0] = listaSoba.get(i).getBrojSobe();
			konacnaLista[i][1] = listaSoba.get(i).getTipSobe().getOznaka();
			int brojNocenja = 0;
			double ukPrihod = 0;
			
			for (Rezervacija rez : rezMng.getRezervacije()) {
				if (rez.getSoba() == null) {
					continue;
				}
                if (rez.getSoba().getBrojSobe() == listaSoba.get(i).getBrojSobe()) {
                    LocalDate rezPocetak = rez.getDatumPocetka();
                    LocalDate rezKraj = rez.getDatumKraja();

                    if ((rezPocetak.isBefore(kraj) || rezPocetak.isEqual(kraj)) && (rezKraj.isAfter(pocetak) || rezKraj.isEqual(pocetak))) {
                        LocalDate effectiveStart = (rezPocetak.isBefore(pocetak)) ? pocetak : rezPocetak;
                        LocalDate effectiveEnd = (rezKraj.isAfter(kraj)) ? kraj : rezKraj;
                        int numberOfNightsReservation = (int) ChronoUnit.DAYS.between(rezPocetak, rezKraj);
                        int numberOfNightsInSpan = (int) ChronoUnit.DAYS.between(effectiveStart, effectiveEnd);

                        brojNocenja += numberOfNightsInSpan;
                        ukPrihod += rez.getCena() / numberOfNightsReservation * numberOfNightsInSpan;  // cena po nocenju * onoliko dana koliko se preklapaju
                    }
                }
            }

            konacnaLista[i][2] = brojNocenja;
            konacnaLista[i][3] = ukPrihod;
        }

        return konacnaLista;
    }
}
