package entities;

import java.time.LocalDate;
import java.util.HashMap;

public class Cenovnik {

	private String id;
    private LocalDate datumPocetka;
    private LocalDate datumKraja;
    private HashMap<String, Integer> ceneTipovaSoba;
    private HashMap<String, Integer> ceneUsluga;

	public Cenovnik(String id, LocalDate datumPocetka, LocalDate datumKraja, HashMap<String, Integer> ceneTipovaSoba,
			HashMap<String, Integer> ceneUsluga) {
		this.id = id;
		this.datumPocetka = datumPocetka;
		this.datumKraja = datumKraja;
		this.ceneTipovaSoba = ceneTipovaSoba;
		this.ceneUsluga = ceneUsluga;
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

	public HashMap<String, Integer> getCeneTipovaSoba() {
		return ceneTipovaSoba;
	}
	
	public int getCenaTipaSobe(String oznakaTSobe) {
		return getCeneTipovaSoba().get(oznakaTSobe);
	}

	public void setCeneTipovaSoba(HashMap<String, Integer> ceneTipovaSoba) {
		this.ceneTipovaSoba = ceneTipovaSoba;
	}

	public HashMap<String, Integer> getCeneUsluga() {
		return ceneUsluga;
	}

	public void setCeneUsluga(HashMap<String, Integer> ceneUsluga) {
		this.ceneUsluga = ceneUsluga;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCenaUsluge(String naziv) {
		return getCeneUsluga().get(naziv);
	}
    
}
