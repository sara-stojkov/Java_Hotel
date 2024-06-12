package entities;

import java.time.LocalDate;

import enums.Pol;
import enums.Uloga;

public class Gost extends Korisnik {

	public Gost(String ime, String prezime, Pol pol, LocalDate datumRodjenja, String brojTelefona, String adresa,
			String korisnickoIme, String lozinka) {
		super(ime, prezime, pol, datumRodjenja, brojTelefona, adresa, korisnickoIme, lozinka);
		this.uloga = Uloga.GOST;
	}


}
