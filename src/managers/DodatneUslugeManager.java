package managers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import entities.DodatnaUsluga;

public class DodatneUslugeManager {
	
	private static DodatneUslugeManager instance;
	private String fajl;
	private ArrayList<DodatnaUsluga> dodatneUsluge;
	

    private DodatneUslugeManager(String fajl) {
		super();
		this.fajl = fajl;
		this.dodatneUsluge = new ArrayList<DodatnaUsluga>();
	}
    
    public static DodatneUslugeManager getInstance(String fajl) {
    	if (instance == null) {
    		instance = new DodatneUslugeManager(fajl);
    	}
    	return instance;
    }

	public boolean uslugeUListu() throws IOException {
        try{
        	BufferedReader citac = new BufferedReader(new FileReader(fajl));
        
        String red;
        while ((red = citac.readLine()) != null) {
         
            String podaci[] = red.split(",");
            if (podaci.length > 0) {
            	dodatneUsluge.add(obradaUsluge(podaci));
            }
        }
        citac.close();
        }catch (IOException e) {
			return false;
		}
		return true;
    }

    private DodatnaUsluga obradaUsluge(String[] nizPodataka) {
        String naziv = nizPodataka[0];
        boolean status = Boolean.parseBoolean(nizPodataka[1]);
        return new DodatnaUsluga(naziv, status);
    }

    public void dodajUslugu(DodatnaUsluga usluga) throws IOException {
        FileWriter upisivanje = new FileWriter(fajl, true);
        upisivanje.write(formatirajUslugu(usluga) + "\n");
        dodatneUsluge.add(usluga);
        System.out.println("\nDodata usluga " + usluga.getNaziv() + ".\n");
        upisivanje.close();
    }

    static private String formatirajUslugu(DodatnaUsluga usluga) {
        String stringSobe = String.join(",", usluga.getNaziv(), String.valueOf(usluga.isAktivna()));
        return stringSobe;
    }

    public DodatnaUsluga nadjiUsluguPoNazivu(String naziv) {
        for (DodatnaUsluga usluga : dodatneUsluge) {
            if (usluga.getNaziv().equals(naziv)) {
                return usluga;
            }
        }
        System.out.println("\nNije pronadjena usluga pod nazivom " + naziv);
        return null;
    }


    public void obrisiUslugu(String naziv) throws IOException {
    	DodatnaUsluga dodUsl = nadjiUsluguPoNazivu(naziv);
    	if (dodUsl != null) {
    		dodUsl.setAktivna(false);
    		upisiFajl();
    	}
    	else {
    		System.out.println("nista nije obrisano jer nema usluge sa tim nazivom");
    	}
    }
    
    public void izmeniUslugu(DodatnaUsluga nova, DodatnaUsluga stara) throws IOException{
    	for (int i = 0; i < dodatneUsluge.size(); i++) {
    		if (dodatneUsluge.get(i).equals(stara)) {
    			dodatneUsluge.set(i,nova);
    			System.out.println("\nUspesno izmenjena usluga "+ nova.getNaziv() + ".\n");
    			upisiFajl();
    			return;
    		}
    	}
    	System.out.println("Usluga "+ stara.getNaziv() + " nije izmenjena jer nije nadjena u sistemu.");
    	
    }


    public void upisiFajl() throws IOException {
        FileWriter upisivanje = new FileWriter(fajl, false);
        for (DodatnaUsluga usluga : dodatneUsluge) {
            upisivanje.write(formatirajUslugu(usluga) + "\n");
        }
        upisivanje.close();
    }

    public String[] uslugeStringLista() {
        String[] listaUsluga = new String[dodatneUsluge.size()];
        int index = 0;
        for (int i = 0; i < dodatneUsluge.size(); i++) {
            if (dodatneUsluge.get(i).isAktivna()) {
                listaUsluga[index++] = dodatneUsluge.get(i).getNaziv();
            }
        }
        return Arrays.copyOf(listaUsluga, index);
    }

	public String getFajl() {
		return fajl;
	}

	public ArrayList<DodatnaUsluga> getDodatneUsluge() {
		return dodatneUsluge;
	}

	public Object[][] prikazDodatnihUsluga(ArrayList<DodatnaUsluga> dodatneUsluge) {
		Object [][] modifikovanaLista = new Object[dodatneUsluge.size()][2];
		for (int i = 0; i < dodatneUsluge.size(); i++) {
			modifikovanaLista [i][0] = dodatneUsluge.get(i).getNaziv();
			modifikovanaLista [i][1] = dodatneUsluge.get(i).isAktivna();
		}
		return modifikovanaLista;
	}

	public boolean proveriZauzetostNaziva(String naziv) {
		
		return true;
	}

}
