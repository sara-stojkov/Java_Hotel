package entities;

public class DodatnaUsluga {

	private String naziv;
	private boolean aktivna;

	public DodatnaUsluga(String naziv) {
		this.naziv = naziv;
		this.aktivna = true;
	}
	
	public DodatnaUsluga(String naziv, boolean status) {
		this.naziv = naziv;
		this.aktivna = status;
	}

	public boolean isAktivna() {
		return aktivna;
	}

	public void setAktivna(boolean aktivna) {
		this.aktivna = aktivna;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
	
	
}
