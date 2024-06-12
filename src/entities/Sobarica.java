package entities;

import java.time.LocalDate;
import java.util.ArrayList;

import enums.NivoiStrucneSpreme;
import enums.Pol;
import enums.Uloga;

public class Sobarica extends Zaposleni{
	
	ArrayList <Soba> listaDodeljenihSoba;

	public Sobarica(String ime, String prezime, Pol pol, LocalDate datumRodjenja, String brojTelefona, String adresa,
			String korisnickoIme, String lozinka, NivoiStrucneSpreme nivoStrucneSpreme, int staz) {
		super(ime, prezime, pol, datumRodjenja, brojTelefona, adresa, korisnickoIme, lozinka, nivoStrucneSpreme, staz);
		this.uloga = Uloga.SOBARICA;
		this.listaDodeljenihSoba = new ArrayList<Soba>();
	}
	
	public Sobarica(String ime, String prezime, Pol pol, LocalDate datumRodjenja, String brojTelefona, String adresa,
			String korisnickoIme, String lozinka, NivoiStrucneSpreme nivoStrucneSpreme, int staz, ArrayList<Soba>listaSoba) {
		super(ime, prezime, pol, datumRodjenja, brojTelefona, adresa, korisnickoIme, lozinka, nivoStrucneSpreme, staz);
		this.uloga = Uloga.SOBARICA;
		this.listaDodeljenihSoba = listaSoba;
	}

	public ArrayList<Soba> getListaDodeljenihSoba() {
		return listaDodeljenihSoba;
	}
	
	public String[] listaSobaString() {
		String[] listaStringova = new String[listaDodeljenihSoba.size()];
		for (int i = 0; i < listaDodeljenihSoba.size(); i++) {
			listaStringova[i] = (String.valueOf(listaDodeljenihSoba.get(i).getBrojSobe()));
		}
		return listaStringova;
	}

	public void setListaDodeljenihSoba(ArrayList<Soba> listaDodeljenihSoba) {
		this.listaDodeljenihSoba = listaDodeljenihSoba;
	}

	public void dodajSobu(Soba soba) {
		this.listaDodeljenihSoba.add(soba);
	}

	
	
}
