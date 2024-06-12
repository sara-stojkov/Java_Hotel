package reports;

import java.time.LocalDate;
import java.util.ArrayList;

import entities.Rezervacija;
import enums.StatusRezervacije;

public class ObradjeneRez {

    public static Object[][] dobaviBrRez(ArrayList<Rezervacija> sveRezervacije, LocalDate pocetak, LocalDate kraj) {
        int brojOtkazanihRez = 0;
        int brojOdbijenihRez = 0;
        int brojPotvrdjenihRez = 0;
        int brojProslihRez = 0;
        int brojTekucihRez = 0;

        for (Rezervacija rez : sveRezervacije) {
            LocalDate rezPocetak = rez.getDatumPocetka();
            LocalDate rezKraj = rez.getDatumKraja();

            if (rezPocetak.isEqual(pocetak) || (rezPocetak.isBefore(kraj)&& rezPocetak.isAfter(pocetak)) ||
                rezKraj.isEqual(kraj) || (rezKraj.isBefore(kraj) && rezKraj.isAfter(pocetak))) {
                if (rez.getStatusRezervacije().equals(StatusRezervacije.POTVRĐENA)) {
                    brojPotvrdjenihRez++;
                } else if (rez.getStatusRezervacije().equals(StatusRezervacije.ODBIJENA)) {
                    brojOdbijenihRez++;
                } else if (rez.getStatusRezervacije().equals(StatusRezervacije.OTKAZANA)) {
                    brojOtkazanihRez++;
	            } else if (rez.getStatusRezervacije().equals(StatusRezervacije.PROŠLA)) {
	            	brojProslihRez++;
	            } else if (rez.getStatusRezervacije().equals(StatusRezervacije.U_TOKU)) {
	            	brojTekucihRez++;
                }
            }
        }

        Object[][] lista = {
            {"OTKAZANA", brojOtkazanihRez},
            {"ODBIJENA", brojOdbijenihRez},
            {"POTVRĐENA", brojPotvrdjenihRez},
            {"PROŠLA", brojProslihRez},
            {"U_TOKU", brojTekucihRez}
        };
        return lista;
    }
}
