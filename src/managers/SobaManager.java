package managers;

import enums.StatusSobe;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import entities.*;

public class SobaManager {
	
	private static SobaManager instance;
	private ArrayList<Soba> sobe;
	private ArrayList<String> listaOciscenihSoba = new ArrayList<>();
	private String[] sveOsobineSoba = {"klima", "balkon", "terasa", "tv", "wifi", "pušačka"};
	private String fajl;
	private String fajl2;
    private static final DateTimeFormatter formaterDatuma = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	
	private SobaManager(String sobaFilePath, String filePathCiscenjeLog) {
		super();
		this.fajl = sobaFilePath;
		this.fajl2 = filePathCiscenjeLog;
		this.sobe = new ArrayList<Soba>();
	}
	
	public static SobaManager getInstance(String filePathSobe, String filePathCiscenjeLog) {
		if (instance == null) {
			instance = new SobaManager(filePathSobe, filePathCiscenjeLog);
		}
		return instance;
	}

    public boolean sobeUListu() throws IOException {
        try{
        	BufferedReader citac = new BufferedReader(new FileReader(fajl));
	        String red;
	        while ((red = citac.readLine()) != null) {
	         
	            String podaci[] = red.split(",");
	            if (podaci.length > 0) {
	            	sobe.add(obradaSoba(podaci));
	            }
		        }
		        citac.close();
		        
		    BufferedReader citac2 = new BufferedReader(new FileReader(fajl2));
		    String red2;
		    while ((red2 = citac2.readLine()) != null) {
		    	listaOciscenihSoba.add(red2);
		    }
		    
		    citac2.close();
	        }
        catch (IOException e) {
			return false;
		}
		return true;
	}

    private Soba obradaSoba(String[] nizPodataka) {
        TipSobe tipSobe = new TipSobe(nizPodataka[1], nizPodataka[2], Integer.parseInt(nizPodataka[3]));
        int brojSobe = Integer.parseInt(nizPodataka[0]);
        ArrayList <String> listaOsobina = new ArrayList<>();
        String[] osobine = nizPodataka[4].split("-");
        for (String osobina : osobine) {
        	listaOsobina.add(osobina);
        }
        StatusSobe status = StatusSobe.fromInteger(Integer.parseInt(nizPodataka[5]));
        
        return new Soba(tipSobe, brojSobe, listaOsobina, status);
    }

    public void dodajSobu(Soba soba) throws IOException {
        FileWriter upisivanje = new FileWriter(fajl, true);
        upisivanje.write(formatirajSobu(soba) + "\n");
        sobe.add(soba);
        upisivanje.close();
    }

    static private String formatirajSobu(Soba soba) {
    	String listaOsobinaStr = String.join("-", soba.getOsobineSobe());
        String stringSobe = String.join(",", String.valueOf(soba.getBrojSobe()), soba.getTipSobe().getNaziv(), soba.getTipSobe().getOznaka(), String.valueOf(soba.getTipSobe().getBrojOsoba()), listaOsobinaStr, String.valueOf(soba.getStatusSobe().ordinal() + 1));
        return stringSobe;
    }


    public void obrisiSobu(Soba odabranaSoba) throws IOException {
    	boolean removed = sobe.removeIf(soba -> soba.equals(odabranaSoba));
        if (removed) {
            upisiFajl();
            System.out.println("\nObrisana Soba " + odabranaSoba.getBrojSobe() + " " + odabranaSoba.getTipSobe().getNaziv());
        } else {
            System.out.println("Nema sobe broj " + odabranaSoba.getBrojSobe() + " u sistemu");
        }
    }
    
    public void izmeniSobu(Soba nova, Soba stara) throws IOException{
    	for (int i = 0; i < sobe.size(); i++) {
    		if (sobe.get(i).equals(stara)) {
    			sobe.set(i,nova);
    			upisiFajl();
    			return;
    		}
    	}
    	System.out.println("Soba "+ stara.getBrojSobe() + " nije izmenjena jer nije nadjena u sistemu.");
    	
    }


    public void upisiFajl() throws IOException {
        FileWriter upisivanje = new FileWriter(fajl, false);
        for (Soba soba : sobe) {
            upisivanje.write(formatirajSobu(soba) + "\n");
        }
        upisivanje.close();
    }
    
    public ArrayList<String> getListaOciscenihSoba() {
		return listaOciscenihSoba;
	}

	public void setListaOciscenihSoba(ArrayList<String> listaOciscenihSoba) {
		this.listaOciscenihSoba = listaOciscenihSoba;
	}

	public ArrayList<Soba> sobeZaCiscenje(){
    	ArrayList<Soba> filtriraneSobe = new ArrayList<Soba>();
    	
    	for (Soba soba : sobe) {
    		if (soba.getStatusSobe() == StatusSobe.SPREMANjE) {
    			filtriraneSobe.add(soba);
    		}
    	}
    	return filtriraneSobe;
    }
    
    public ArrayList<Soba> nadjiSobeSobarici(String korisnickoIme){
    	return sobeZaCiscenje();
    }

	public Object[][] prikazSoba(ArrayList <Soba> listaSoba) {
		Object [][] modifikovanaLista = new Object[listaSoba.size()][3];
		for (int i = 0; i < listaSoba.size(); i++) {
			modifikovanaLista [i][0] = listaSoba.get(i).getBrojSobe();
			modifikovanaLista [i][1] = listaSoba.get(i).getTipSobe().getOznaka();
			modifikovanaLista [i][2] = listaSoba.get(i).getStatusSobe();
		}
		return modifikovanaLista;
	}
	
	public Soba dobaviSobuPoBroju(int brojSobe) {
		if (brojSobe == 0) {
			return null;
		}
		for (Soba soba : sobe) {
			if (soba.getBrojSobe() == brojSobe) {
				return soba;
			}
		}
		return null;
	}
	
	public boolean izmeniStatusSobe(int brojSobe, StatusSobe status) {
		Soba odabranaSoba = dobaviSobuPoBroju(brojSobe);
		odabranaSoba.setStatusSobe(status);
		try {
			upisiFajl();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public ArrayList<Soba> getSobe() {
		return sobe;
	}

	public String[] getSveOsobineSoba() {
		return sveOsobineSoba;
	}

	public void setSveOsobineSoba(String[] sveOsobineSoba) {
		this.sveOsobineSoba = sveOsobineSoba;
	}

	public void dodajOciscenuSobu(int brojSobe, String korisnickoIme) {
		listaOciscenihSoba.add(formatirajCiscenje(brojSobe, korisnickoIme));
		FileWriter upisivanje;
		try {
			upisivanje = new FileWriter(fajl2, true);
			upisivanje.write(formatirajCiscenje(brojSobe, korisnickoIme) + "\n");
	        upisivanje.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        		
	}

	private String formatirajCiscenje(int brojSobe, String korisnickoImeSobarice) {
		
		String stringBrojSobe = String.valueOf(brojSobe);
		String datumDanas = LocalDate.now().format(formaterDatuma).toString();
		
		return String.join(",", korisnickoImeSobarice, datumDanas,stringBrojSobe);
	}

	
	public boolean proveriZauzetostBrojaSobe(int brojSobe) {
		for (Soba soba : sobe) {
			if (soba.getBrojSobe() == brojSobe) {
				return false;
			}
		}
		return true;
	}

}
