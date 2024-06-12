package managers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import entities.Rezervacija;
import entities.Soba;
import entities.TipSobe;

public class TipSobeManager {

	private static TipSobeManager instance;
    private String fajl;
    private ArrayList<TipSobe> tipoviSoba;
    


    private TipSobeManager(String fajl) {
		super();
		this.fajl = fajl;
		this.tipoviSoba = new ArrayList<TipSobe>();
	}
    
    public static TipSobeManager getInstance(String fajl) {
    	if (instance == null) {
    		instance = new TipSobeManager(fajl);
    	}
    	return instance;
    }

	public boolean tipoviSobaUListu() throws IOException {
        try {
		BufferedReader citac = new BufferedReader(new FileReader(fajl));
        String red;
        while ((red = citac.readLine()) != null) {
         
            String podaci[] = red.split(",");
            if (podaci.length > 0) {
            	tipoviSoba.add(obradaTipaSobe(podaci));
            }
        }
        citac.close();
        } catch (IOException e) {
			return false;
		}
		return true;
    }

    private TipSobe obradaTipaSobe(String[] nizPodataka) {
        String naziv = nizPodataka[0];
        String oznaka = nizPodataka[1];
        int brojOsoba = Integer.parseInt(nizPodataka[2]);

        return new TipSobe(naziv, oznaka, brojOsoba);
    }

    public void dodajTipSobe(TipSobe tipSobe) throws IOException {
        FileWriter upisivanje = new FileWriter(fajl, true);
        upisivanje.write(formatirajTipSobe(tipSobe) + "\n");
        tipoviSoba.add(tipSobe);
        upisivanje.close();
    }

    static private String formatirajTipSobe(TipSobe tipSobe) {
        String stringTipaSobe = String.join(",", tipSobe.getNaziv(), tipSobe.getOznaka(), String.valueOf(tipSobe.getBrojOsoba()));
        return stringTipaSobe;
    }
    
    public TipSobe nadjiTipPoOznaci(String oznaka) {
        for (TipSobe tipSobe : tipoviSoba) {
            if (tipSobe.getOznaka().equals(oznaka)) {
                return tipSobe;
            }
        }
        System.out.println("Nije pronadjen tip sobe sa oznakom " + oznaka);
        return null;
    }


    public boolean obrisiTipSobe(TipSobe odabraniTipSobe, ManagerFactory menadzeri) throws IOException {
    	if (validirajBrisanjeTipaSobe(odabraniTipSobe, menadzeri)) {
	    	boolean removed = tipoviSoba.removeIf(tipSobe -> tipSobe.equals(odabraniTipSobe));
	        if (removed) {
	            upisiFajl();
	        } else {
	            System.out.println("Nema tipa sa oznakom " + odabraniTipSobe.getOznaka() + " u sistemu");
	        }
        return true;
        }
    	else {
    		System.out.println("nema brisanja tipa soba jer su za njega vezane sobe i rezervacije");
    		return false;
    	}
    }
    
    private boolean validirajBrisanjeTipaSobe(TipSobe odabraniTipSobe, ManagerFactory menadzeri) {
		for (Soba soba : menadzeri.getSobaMng().getSobe()) {
			if (soba.getTipSobe().getOznaka().equals(odabraniTipSobe.getOznaka())) {
				return false;
			}
		}
		
		for (Rezervacija rez : menadzeri.getRezervacijaMng().getRezervacije()) {
			if (rez.getTipSobe().getOznaka().equals(odabraniTipSobe.getOznaka())) {
				return false;
			}
		}
    	
    	
    	return true;
	}

	public void izmeniTipSobe(TipSobe novi, TipSobe stari) throws IOException{
    	for (int i = 0; i < tipoviSoba.size(); i++) {
    		if (tipoviSoba.get(i).equals(stari)) {
    			tipoviSoba.set(i,novi);
    			upisiFajl();
    			return;
    		}
    	}
    }


    public void upisiFajl() throws IOException {
        FileWriter upisivanje = new FileWriter(fajl, false);
        for (TipSobe tipSobe : tipoviSoba) {
            upisivanje.write(formatirajTipSobe(tipSobe) + "\n");
        }
        upisivanje.close();
    }

    public void pregledTipovaSoba() throws IOException {
    	System.out.println("\n Pregled tipova soba\n");
        System.out.printf("%-20s| %-10s| %-10s \n", "naziv tipa sobe", "oznaka", "broj osoba");       
        System.out.println("-----------------------------------------------------------");
    	
        for (TipSobe tipSobe : tipoviSoba) {
            System.out.printf("%-20s| %-10s | %-10s\n", tipSobe.getNaziv(), tipSobe.getOznaka(), String.valueOf(tipSobe.getBrojOsoba()));

        }
       
    }

	public ArrayList<TipSobe> getTipoviSoba() {
		return tipoviSoba;
	}

	public String getFajl() {
		return fajl;
	}

	public TipSobe dobaviTipPoOznaci(String id) {
		for (TipSobe tipSobe : tipoviSoba) {
			if (tipSobe.getOznaka().equals(id)) {
				return tipSobe;
			}
		}
		System.out.println("nije pronadjena soba sa oznakom " + id);
		return null;
	}

	public Object[][] prikazTipovaSoba(ArrayList<TipSobe> tipoviSoba) {
		Object [][] modifikovanaLista = new Object[tipoviSoba.size()][3];
		for (int i = 0; i < tipoviSoba.size(); i++) {
			modifikovanaLista [i][0] = tipoviSoba.get(i).getOznaka();
			modifikovanaLista [i][1] = tipoviSoba.get(i).getNaziv();			
			modifikovanaLista [i][2] = tipoviSoba.get(i).getBrojOsoba();
		}
		return modifikovanaLista;
	}

	public boolean proveriZauzetostOznake(String oznaka) {
		for (TipSobe tSobe : tipoviSoba) {
			if (tSobe.getOznaka().equals(oznaka)) {
				return false;
			}
		}
		return true;
	}


}


