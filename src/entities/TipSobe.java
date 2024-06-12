package entities;

public class TipSobe {
	
	private String naziv;
	
	private String oznaka;
	
	private int brojOsoba;
	
	public TipSobe(String naziv, String oznaka, int brojOsoba) {
		this.naziv = naziv;
		this.oznaka = oznaka;
		this.brojOsoba = brojOsoba;
	}
	
	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getOznaka() {
		return oznaka;
	}

	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}

	public int getBrojOsoba() {
		return brojOsoba;
	}

	public void setBrojOsoba(int brojOsoba) {
		this.brojOsoba = brojOsoba;
	}

	
}
