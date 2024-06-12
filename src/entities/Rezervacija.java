package entities;

import java.time.LocalDate;
import java.util.ArrayList;

import enums.StatusRezervacije;

public class Rezervacija {
	
	private String id;
	private StatusRezervacije statusRezervacije; 
	private Gost imeRezervacije;
	private ArrayList<DodatnaUsluga> dodatneUsluge;
	private ArrayList<String> osobineSobe;
	private TipSobe tipSobe;
	private LocalDate datumPocetka;
	private LocalDate datumKraja;
	private double cena;
	private LocalDate datumKreiranja;
	private Soba soba;
	
	public Rezervacija(String id, Gost imeRezervacije, ArrayList<DodatnaUsluga> dodatneUsluge,
			TipSobe tipSobe, ArrayList<String> listaOsobina, LocalDate datumPocetka, LocalDate datumKraja, ArrayList<Cenovnik> cenovnici) {
		super();
		this.id = id;
		this.statusRezervacije = StatusRezervacije.NA_ČEKANjU;  // uvek pri kreiranju nove rezervacije ona ima ovaj status
		this.imeRezervacije = imeRezervacije;
		this.dodatneUsluge = dodatneUsluge;
		this.osobineSobe = listaOsobina;
		this.tipSobe = tipSobe;
		this.datumPocetka = datumPocetka;
		this.datumKraja = datumKraja;
		this.cena = nadjiCenu(cenovnici);
		this.datumKreiranja = LocalDate.now();
		this.soba = null;
		
	}
	
	public Rezervacija(String id, StatusRezervacije statusRezervacije, Gost imeRezervacije, ArrayList<DodatnaUsluga> dodatneUsluge,
			TipSobe tipSobe, ArrayList<String> listaOsobina, LocalDate datumPocetka, LocalDate datumKraja, double cena, LocalDate datumKreiranja, Soba dodeljenaSoba) {
		super();
		this.id = id;
		this.statusRezervacije = statusRezervacije;
		this.imeRezervacije = imeRezervacije;
		this.dodatneUsluge = dodatneUsluge;
		this.tipSobe = tipSobe;
		this.osobineSobe = listaOsobina;
		this.datumPocetka = datumPocetka;
		this.datumKraja = datumKraja;
		this.cena = cena;
		this.datumKreiranja = datumKreiranja;
		this.soba = dodeljenaSoba;

	}
	
	public Rezervacija(String id, StatusRezervacije statusRezervacije, Gost imeRezervacije, ArrayList<DodatnaUsluga> dodatneUsluge,
			TipSobe tipSobe, ArrayList<String> listaOsobina, LocalDate datumPocetka, LocalDate datumKraja, double cena, LocalDate datumKreiranja) {
		super();
		this.id = id;
		this.statusRezervacije = statusRezervacije;
		this.imeRezervacije = imeRezervacije;
		this.dodatneUsluge = dodatneUsluge;
		this.tipSobe = tipSobe;
		this.osobineSobe = listaOsobina;
		this.datumPocetka = datumPocetka;
		this.datumKraja = datumKraja;
		this.cena = cena;
		this.datumKreiranja = datumKreiranja;
		this.soba = null;

	}
	
	public ArrayList<String> getOsobineSobe() {
		return osobineSobe;
	}

	public void setOsobineSobe(ArrayList<String> osobineSobe) {
		this.osobineSobe = osobineSobe;
	}

	public StatusRezervacije getStatusRezervacije() {
		return statusRezervacije;
	}
	public void setStatusRezervacije(StatusRezervacije statusRezervacije) {
		this.statusRezervacije = statusRezervacije;
	}
	public Gost getImeRezervacije() {
		return imeRezervacije;
	}
	public void setImeRezervacije(Gost imeRezervacije) {
		this.imeRezervacije = imeRezervacije;
	}
	public ArrayList<DodatnaUsluga> getDodatneUsluge() {
		return dodatneUsluge;
	}
	public void setDodatneUsluge(ArrayList<DodatnaUsluga> dodatneUsluge) {
		this.dodatneUsluge = dodatneUsluge;
	}
	public TipSobe getTipSobe() {
		return tipSobe;
	}
	public void setTipSobe(TipSobe tipSobe) {
		this.tipSobe = tipSobe;
	}
	public LocalDate getDatumPocetka() {
		return datumPocetka;
	}
	public void setDatumPocetka(LocalDate datumPocetka) {
		this.datumPocetka = datumPocetka;
	}
	public LocalDate getDatumKraja() {
		return datumKraja;
	}
	public void setDatumKraja(LocalDate datumKraja) {
		this.datumKraja = datumKraja;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getCena() {
		return cena;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}

	private double nadjiCenu(ArrayList<Cenovnik> cenovnici) {
	    double cenaRez = 0;
	    for (LocalDate date = this.datumPocetka; date.isBefore(datumKraja); date = date.plusDays(1)) {
	        for (Cenovnik cenovnik : cenovnici) {
	            LocalDate pocetak = cenovnik.getDatumPocetka();
	            LocalDate kraj = cenovnik.getDatumKraja();
	            
	            if ((date.isEqual(pocetak) || date.isAfter(pocetak)) && (date.isEqual(kraj) || date.isBefore(kraj))) {
	                cenaRez += cenovnik.getCenaTipaSobe(tipSobe.getOznaka());
	                for (DodatnaUsluga dodUsl : dodatneUsluge) {
	                    cenaRez += cenovnik.getCenaUsluge(dodUsl.getNaziv());
	                }
	            }
	        }
	    }
	    return cenaRez;
	}

	public LocalDate getDatumKreiranja() {
		return datumKreiranja;
	}

	public void setDatumKreiranja(LocalDate datumKreiranja) {
		this.datumKreiranja = datumKreiranja;
	}

	public Soba getSoba() {
		return soba;
	}

	public void setSoba(Soba soba) {
		this.soba = soba;
	}


}
