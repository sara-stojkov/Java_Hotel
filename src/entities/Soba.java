package entities;

import java.util.ArrayList;

import enums.StatusSobe;

public class Soba {
	
	protected TipSobe tipSobe;
	
	protected int brojSobe;
	
	protected StatusSobe statusSobe;
	
	protected ArrayList<String> osobineSobe;

	public Soba(TipSobe tipSobe, int brojSobe, ArrayList<String> listaOsobina) {
		this.tipSobe = tipSobe;
		this.brojSobe = brojSobe;
		this.statusSobe = StatusSobe.SLOBODNA;
		this.osobineSobe = listaOsobina;
	}
	
	public Soba(TipSobe tipSobe, int brojSobe, ArrayList<String> listaOsobina, StatusSobe statusSobe) {
		super();
		this.tipSobe = tipSobe;
		this.brojSobe = brojSobe;
		this.osobineSobe = listaOsobina;
		this.statusSobe = statusSobe;
	}

	public TipSobe getTipSobe() {
		return tipSobe;
	}

	public void setTipSobe(TipSobe tipSobe) {
		this.tipSobe = tipSobe;
	}

	public int getBrojSobe() {
		return brojSobe;
	}

	public void setBrojSobe(int brojSobe) {
		this.brojSobe = brojSobe;
	}

	public ArrayList<String> getOsobineSobe() {
		return osobineSobe;
	}

	public void setOsobineSobe(ArrayList<String> osobineSobe) {
		this.osobineSobe = osobineSobe;
	}

	public StatusSobe getStatusSobe() {
		return statusSobe;
	}

	public void setStatusSobe(StatusSobe statusSobe) {
		this.statusSobe = statusSobe;
	}

}
