package entities;

import java.time.LocalDate;
import enums.NivoiStrucneSpreme;
import enums.Pol;
import enums.Uloga;

public class Recepcioner extends Zaposleni{
		
	public Recepcioner(String ime, String prezime, Pol pol, LocalDate datumRodjenja, String brojTelefona,
			String adresa, String korisnickoIme, String lozinka, NivoiStrucneSpreme nivoStrucneSpreme, int staz) {
		super(ime, prezime, pol, datumRodjenja, brojTelefona, adresa, korisnickoIme, lozinka, nivoStrucneSpreme, staz);
		this.uloga = Uloga.RECEPCIONER;
		}

}
