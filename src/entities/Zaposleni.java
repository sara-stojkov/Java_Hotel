package entities;

import java.time.LocalDate;
import java.util.Objects;

import enums.NivoiStrucneSpreme;
import enums.Pol;

public class Zaposleni extends Korisnik{

	protected NivoiStrucneSpreme nivoStrucneSpreme;
	
	protected int staz;
	
	protected double plata;
	
	

	public Zaposleni(String ime, String prezime, Pol pol, LocalDate datumRodjenja, String brojTelefona, String adresa,
			String korisnickoIme, String lozinka, NivoiStrucneSpreme nivoStrucneSpreme, int staz) {
		super(ime, prezime, pol, datumRodjenja, brojTelefona, adresa, korisnickoIme, lozinka);
		this.nivoStrucneSpreme = nivoStrucneSpreme;
		this.staz = staz;
		this.plata = 50000 + this.nivoStrucneSpreme.ordinal() * 1000 + this.staz * 750;
	}

	public NivoiStrucneSpreme getNivoStrucneSpreme() {
		return nivoStrucneSpreme;
	}



	public void setNivoStrucneSpreme(NivoiStrucneSpreme nivoStrucneSpreme) {
		this.nivoStrucneSpreme = nivoStrucneSpreme;
	}



	public int getStaz() {
		return staz;
	}



	public void setStaz(int staz) {
		this.staz = staz;
	}



	public double getPlata() {
		return plata;
	}

// nema setPlata jer se plata racuna na osnovu ostalog, ne moze da se menja

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
	        return true;
	    }
	    if (obj == null || getClass() != obj.getClass()) {
	        return false;
	    }
	    Zaposleni other = (Zaposleni) obj;
	    return Objects.equals(ime, other.ime) &&
	           Objects.equals(prezime, other.prezime) &&
	           pol == other.pol &&
	           Objects.equals(datumRodjenja, other.datumRodjenja) &&
	           Objects.equals(brojTelefona, other.brojTelefona) &&
	           Objects.equals(adresa, other.adresa) &&
	           Objects.equals(korisnickoIme, other.korisnickoIme) &&
	           Objects.equals(lozinka, other.lozinka) &&
	           nivoStrucneSpreme == other.nivoStrucneSpreme &&
	           staz == other.staz;
	}

	
}
