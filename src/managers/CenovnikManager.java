package managers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import entities.Cenovnik;
import entities.DodatnaUsluga;
import entities.TipSobe;

public class CenovnikManager {
    
	private static CenovnikManager instance;
    private String fajl;
    private ArrayList <Cenovnik> cenovnici;
    
    private CenovnikManager(String fajl) {
    	this.fajl = fajl;
    	this.cenovnici = new ArrayList <Cenovnik>();
    }
    
    public static CenovnikManager getInstance(String fajl) {
    	if (instance == null) {
    		instance = new CenovnikManager(fajl);
    	}
    	return instance;
    }
    
    
    public void definisiCenovnik(String id, LocalDate datumPocetka, LocalDate datumKraja, ArrayList<TipSobe> listaTipovaSoba, ArrayList<DodatnaUsluga> listaUsluga, ArrayList<Integer> listaCenaTipovaSoba, ArrayList<Integer> listaCenaUsluga) {
    	HashMap<String, Integer> ceneTipovaSoba = new HashMap<String, Integer>();
    	HashMap<String, Integer> ceneUsluga = new HashMap<String, Integer>();

    	for (int i = 0; i < listaCenaTipovaSoba.size(); i++) {
            ceneTipovaSoba.put(listaTipovaSoba.get(i).getOznaka(), listaCenaTipovaSoba.get(i));
        }
        for (int i = 0; i < listaCenaUsluga.size(); i++) {
            ceneUsluga.put(listaUsluga.get(i).getNaziv(), listaCenaUsluga.get(i));
        }
        
        Cenovnik cenovnik = new Cenovnik(id, datumPocetka, datumKraja, ceneTipovaSoba, ceneUsluga);
        System.out.println("\nUspesno je dodat sledeci cenovnik: \n");
        cenovnici.add(cenovnik);
        upisiUFajl();
    }
    
    private String formatirajCenovnik(Cenovnik cenovnik) {
        StringBuilder result = new StringBuilder(cenovnik.getId() + "," + cenovnik.getDatumPocetka() + "," + cenovnik.getDatumKraja());
        
        HashMap<String, Integer> ceneTipovaSoba = cenovnik.getCeneTipovaSoba();
        for (Entry<String, Integer> entry : ceneTipovaSoba.entrySet()) {
            String oznaka = entry.getKey();
            Integer cena = entry.getValue();
            result.append(",").append(oznaka).append(":").append(cena);
        }
        
        HashMap<String, Integer> ceneUsluga = cenovnik.getCeneUsluga();
        for (Entry<String, Integer> entry : ceneUsluga.entrySet()) {
            String naziv = entry.getKey();
            Integer cena = entry.getValue();
            result.append(",").append(naziv).append(":").append(cena);
        }
        
        return result.toString();
    }


    public void dodajCenovnik (Cenovnik cen) {
    	cenovnici.add(cen);
    	upisiUFajl();
    }
    
    public boolean cenovniciUListu() {
        try {
            BufferedReader citac = new BufferedReader(new FileReader(fajl));
            String red;
            while ((red = citac.readLine()) != null) {
                Cenovnik cenovnik = obradaCenovnika(red.split(","));
                cenovnici.add(cenovnik);
            }
            citac.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Cenovnik obradaCenovnika(String[] nizPodataka) {
        String id = nizPodataka[0];
        LocalDate datumPocetka = LocalDate.parse(nizPodataka[1]);
        LocalDate datumKraja = LocalDate.parse(nizPodataka[2]);
        HashMap<String, Integer> ceneTipovaSoba = new HashMap<>();
        HashMap<String, Integer> ceneUsluga = new HashMap<>();

        for (int i = 3; i < nizPodataka.length; i++) {
            String[] cenaSplit = nizPodataka[i].split(":");
            String naziv = cenaSplit[0];
            int cena = Integer.parseInt(cenaSplit[1]);

            if (naziv.matches("[\\d+()]+")) { 
                ceneTipovaSoba.put(naziv, cena); 
            } else { 
                ceneUsluga.put(naziv, cena);
            }
        }
        return new Cenovnik(id, datumPocetka, datumKraja, ceneTipovaSoba, ceneUsluga);
    }

    public Cenovnik dobaviCenovnikPoId(String idCenovnika) {
    	for (Cenovnik cenovnik : cenovnici) {
    		if (cenovnik.getId().equals(idCenovnika)) {
    			return cenovnik;
    		}
    	}
    	return null;
    }


    
    public boolean obrisiCenovnik(String id) {
    	if (cenovnici.size() > 1) {
        cenovnici.removeIf(cenovnik -> cenovnik.getId().equals(id));
        upisiUFajl(); 
        System.out.println("Cenovnik sa id-jem " + id + " je uspešno obrisan.");
        return true;
    	}
    		System.out.println("cenovnik nije obrisan jer je jedan jedini");
    		return false;
    	
    }
    
    public void upisiUFajl() {
        try {
            FileWriter upisivanje = new FileWriter(fajl, false);
            for (Cenovnik cenovnik : cenovnici) {
                upisivanje.write(formatirajCenovnik(cenovnik) + "\n");
                System.out.println(formatirajCenovnik(cenovnik) + "\n");
            }
            upisivanje.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    
	public void izmeniCenovnik(Cenovnik novi, Cenovnik stari) throws IOException{
	    	for (int i = 0; i < cenovnici.size(); i++) {
	    		if (cenovnici.get(i).equals(stari)) {
	    			cenovnici.set(i,novi);
	    			upisiUFajl();
	    			return;
	    		}
	    	}
	    }
	
	
	public String getFajl() {
		return fajl;
	}

	public void setFajl(String fajl) {
		this.fajl = fajl;
	}

	public ArrayList<Cenovnik> getCenovnici() {
		return cenovnici;
	}

	public void setCenovnici(ArrayList<Cenovnik> cenovnici) {
		this.cenovnici = cenovnici;
	}
	
	

	public Object[][] prikazCenovnika(ArrayList<Cenovnik> listaCenovnika) {
		Object [][] modifikovanaLista = new Object[listaCenovnika.size()][3];
		for (int i = 0; i < listaCenovnika.size(); i++) {
			modifikovanaLista [i][0] = listaCenovnika.get(i).getId();
			modifikovanaLista [i][1] = listaCenovnika.get(i).getDatumPocetka();			
			modifikovanaLista [i][2] = listaCenovnika.get(i).getDatumKraja();			
		}
		return modifikovanaLista;
	}

	public void dodajUsluguCenovniku(DodatnaUsluga usl, int cena) {

		for (Cenovnik cen : cenovnici) {
			cen.getCeneUsluga().put(usl.getNaziv(), cena);
		}
		
		upisiUFajl();
	}
	
	public void dodajTipSobeCenovniku(TipSobe tSobe, int cena) {
		
		for (Cenovnik cen : cenovnici) {
			cen.getCeneTipovaSoba().put(tSobe.getOznaka(), cena);
		}
		
		upisiUFajl();
		
	}    
	
	
	public void izmeniCenuTipaSobe(String oznakaTipaSobe, int novaCena, Cenovnik cenovnik) {
        if (cenovnik.getCeneTipovaSoba().containsKey(oznakaTipaSobe)) {
            cenovnik.getCeneTipovaSoba().put(oznakaTipaSobe, novaCena);
            System.out.println("Cena tipa sobe '" + oznakaTipaSobe + "' uspesno promenjena na " + novaCena + ".");
            for (int i = 0; i < cenovnici.size(); i++) {
                if (cenovnici.get(i).getId().equals(cenovnik.getId())) {
                    cenovnici.set(i, cenovnik);
                    break;
                }
            }
            upisiUFajl(); 
        } else {
            System.out.println("Tip sobe '" + oznakaTipaSobe + "' ne postoji u cenovniku.");
        }
    }

    public void izmeniCenuUsluge(String nazivUsluge, int novaCena, Cenovnik cenovnik) {
        if (cenovnik.getCeneUsluga().containsKey(nazivUsluge)) {
            cenovnik.getCeneUsluga().put(nazivUsluge, novaCena);
            System.out.println("Cena usluge '" + nazivUsluge + "' uspesno promenjena na " + novaCena + ".");
            
            for (int i = 0; i < cenovnici.size(); i++) {
                if (cenovnici.get(i).getId().equals(cenovnik.getId())) {
                	cenovnici.set(i, cenovnik);
                    break;
                }
            }
            upisiUFajl();
        } else {
            System.out.println("Usluga '" + nazivUsluge + "' ne postoji u cenovniku.");
        }
    }    
    
    public void pregledCenovnika(Cenovnik pojedinacniCenovnik) {
        System.out.println("\n Prikaz cenovnika " + pojedinacniCenovnik.getId() + "\n");
        System.out.println("   Datum početka │   Datum kraja   ");
        System.out.println("-------------------------------------");
        System.out.printf(" %-15s │ %-15s \n", pojedinacniCenovnik.getDatumPocetka(), pojedinacniCenovnik.getDatumKraja());
        System.out.println("---------------------------------------");
        System.out.println("   Cene tipova soba                  \n ");
        for (String oznaka : pojedinacniCenovnik.getCeneTipovaSoba().keySet()) {
            System.out.printf("   %-15s | %-10s      \n", oznaka, pojedinacniCenovnik.getCeneTipovaSoba().get(oznaka));
        }
        System.out.println("---------------------------------------");
        System.out.println("   Cene dodatnih usluga              \n ");
        for (String naziv : pojedinacniCenovnik.getCeneUsluga().keySet()) {
            System.out.printf("   %-15s | %-10s      \n", naziv, pojedinacniCenovnik.getCeneUsluga().get(naziv));
        }
    }
    
    public void pregledSvihCenovnika(ArrayList<Cenovnik> sviCenovnici) {
        for (Cenovnik cenovnik : sviCenovnici) {
            pregledCenovnika(cenovnik);
            System.out.println("----------------------------------------");
        }
    }
}
