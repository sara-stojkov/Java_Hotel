package entities;
import java.time.LocalDate;

import enums.Pol;
import enums.Uloga;

public abstract class Korisnik {

	protected String ime;
	protected String prezime;
	protected Pol pol;  // musko ili zensko
	protected Uloga uloga;
	protected LocalDate datumRodjenja;
	protected String brojTelefona;
	protected String adresa;
	protected String korisnickoIme;
	protected String lozinka;
	

	public Korisnik(String ime, String prezime, Pol pol, LocalDate datumRodjenja, String brojTelefona, String adresa,
			String korisnickoIme, String lozinka) {
		this.ime = ime;
		this.prezime = prezime;
		this.pol = pol;
		this.uloga = null;
		this.datumRodjenja = datumRodjenja;
		this.brojTelefona = brojTelefona;
		this.adresa = adresa;
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
	}
	
	


	public String getIme() {
		return ime;
	}

	protected void setIme(String ime) {
		this.ime = ime;
	}

	protected void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	protected void setPol(Pol pol) {
		this.pol = pol;
	}

	protected void setDatumRodjenja(LocalDate datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}

	protected void setBrojTelefona(String brojTelefona) {
		this.brojTelefona = brojTelefona;
	}

	protected void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	protected void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	protected void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public String getPrezime() {
		return prezime;
	}

	public Pol getPol() {
		return pol;
	}

	public LocalDate getDatumRodjenja() {
		return datumRodjenja;
	}

	public String getBrojTelefona() {
		return brojTelefona;
	}

	public String getAdresa() {
		return adresa;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public String getLozinka() {
		return lozinka;
	}


	public Uloga getUloga() {
		return uloga;
	}


	public void setUloga(Uloga uloga) {
		this.uloga = uloga;
	}
	
	
	
	

}
