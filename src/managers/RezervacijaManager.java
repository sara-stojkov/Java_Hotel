package managers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import entities.*;
import enums.StatusRezervacije;
import enums.StatusSobe;

public class RezervacijaManager {
	
	private static RezervacijaManager instance;
    private String fajl;
    private String fajlDolasci;
    private SobaManager sobaMng;
    private static final DateTimeFormatter formaterDatuma = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private ArrayList <Rezervacija> rezervacije;
    private ArrayList <Rezervacija> danasnjePromeneRez;


    private RezervacijaManager(String fajl, SobaManager sobaMng, String fajl2) {
		super();
		this.fajl = fajl;
		this.fajlDolasci = fajl2;
		this.rezervacije = new ArrayList <Rezervacija>();
		this.sobaMng = sobaMng;
	}
    
    public static RezervacijaManager getInstance(String fajl, SobaManager sobaMng, String fajl2) {
    	if (instance == null) {
    		instance = new RezervacijaManager(fajl, sobaMng, fajl2);
    	}
    	return instance;
    }

	public boolean rezervacijeUListu(ArrayList<Gost> listaGostiju, ArrayList<TipSobe> listaTipovaSoba) throws IOException {
		
		try{
        	BufferedReader citac = new BufferedReader(new FileReader(fajl));
     
	        String red;
	        while ((red = citac.readLine()) != null) {
	           
	            String podaci[] = red.split(",");
	            if (podaci.length > 0) {
	            	rezervacije.add(obradaRezervacije(podaci, listaGostiju, listaTipovaSoba));
	            }
	        }
	        upisiFajl();
	        citac.close();
	        
	        BufferedReader citac2 = new BufferedReader(new FileReader(fajlDolasci));
	        
	        String red2;
	        boolean prviRed = true;
	        while ((red2 = citac2.readLine()) != null) {
	           if (prviRed) {
	        	   LocalDate datumPodataka = LocalDate.parse(red2, formaterDatuma);
	        	   if (!datumPodataka.equals(LocalDate.now())) {
	        		   System.out.println("resetujemo podatke...");
	        		   danasnjePromeneRez = new ArrayList<>();
	        		   break;
	        	   }
	           }
	            String podaci2[] = red2.split(",");
	            if (podaci2.length > 0) {
	            	danasnjePromeneRez.add(obradaRezervacije(podaci2, listaGostiju, listaTipovaSoba));
	            }
	        }
	        citac2.close();
        
        } catch (IOException e) {
			return false;
		}
		return true;
    }

    private Rezervacija obradaRezervacije(String[] nizPodataka, ArrayList<Gost> listaGostiju, ArrayList<TipSobe> listaTipovaSoba) {
        String id = nizPodataka[0];
        StatusRezervacije status = StatusRezervacije.fromInteger(Integer.parseInt(nizPodataka[1]));
        String korisnickoImeGosta = nizPodataka[3];
        Gost gostSaRezervacije = null; 
        for (Gost gost : listaGostiju) {
            if (gost.getKorisnickoIme().equals(korisnickoImeGosta)) {
                gostSaRezervacije = gost; 
                break;
            }
        }

        String oznakaTipaSobe = nizPodataka[4];
        TipSobe tipSobeSaRezervacije = null;
        for (TipSobe tipSobe : listaTipovaSoba) {
            if (tipSobe.getOznaka().equals(oznakaTipaSobe)) {
                tipSobeSaRezervacije = tipSobe; 
                break;
            }
        }

        LocalDate datumPocetka = LocalDate.parse(nizPodataka[6], formaterDatuma);
        LocalDate datumKraja = LocalDate.parse(nizPodataka[7], formaterDatuma);
        ArrayList<DodatnaUsluga> dodatneUsluge = new ArrayList<>();
        String[] niz = nizPodataka[2].split("-");

        for (String token : niz) {
            DodatnaUsluga usluga = new DodatnaUsluga(token);
            dodatneUsluge.add(usluga);
        }
        ArrayList<String> osobineSobe = new ArrayList<>();
        String[] nizOsobina = nizPodataka[5].split("-");
        if (!nizPodataka[5].equals("/")) {
	        for (String osobina : nizOsobina) {
	        	osobineSobe.add(osobina);
        }}
	    double cena = Double.parseDouble(nizPodataka[8]);
        LocalDate datumKreacije = LocalDate.parse(nizPodataka[9], formaterDatuma);

        if (status == StatusRezervacije.NA_ČEKANjU) {
            LocalDate today = LocalDate.now();
//            System.out.println("Trenutno: " + today);
//            System.out.println("Pocetak rezervacije: " + datumPocetka);
            if (datumPocetka.isBefore(today)) {  // odbijamo prosle rezervacije
//                System.out.println("odbijena je rez koja je trebala da pocne " + datumPocetka.toString());
                status = StatusRezervacije.ODBIJENA;
            }
        }
        
        Soba dodeljenaSoba = sobaMng.dobaviSobuPoBroju(Integer.parseInt(nizPodataka[10]));
        
        return new Rezervacija(id, status, gostSaRezervacije, dodatneUsluge, tipSobeSaRezervacije, osobineSobe, datumPocetka, datumKraja, cena, datumKreacije, dodeljenaSoba);
    }


    public void dodajRezervaciju(Rezervacija rezervacija) throws IOException {
    	System.out.println("uslo u dodavanje");
        FileWriter upisivanje = new FileWriter(fajl, true);
        rezervacije.add(rezervacija);
        upisiFajl();
        System.out.println("\nDodata rezervacija na ime " + rezervacija.getImeRezervacije().getIme() + " " + rezervacija.getImeRezervacije().getPrezime() + ".\n");
        upisivanje.close();
    }
    

    static private String formatirajRezervaciju(Rezervacija rezervacija) {
    	String brojSobeRez = "";
		if (rezervacija.getSoba() == null) {
    		brojSobeRez = "0";
    	}
    	else {
    		brojSobeRez = String.valueOf(rezervacija.getSoba().getBrojSobe());
    	}
		String stringUsluga, stringOsobina = "";
    	if (rezervacija.getDodatneUsluge().size() != 0) {
	    	ArrayList<String> listaStringova = new ArrayList<>();
	    	for (DodatnaUsluga usluga : rezervacija.getDodatneUsluge()) {
	    	    listaStringova.add(usluga.getNaziv());
	    	}
	    	stringUsluga = String.join("-", listaStringova);
	    	
    	}
    	else{
    		stringUsluga = "/";
    	}
    	
    	if (rezervacija.getOsobineSobe().size() != 0) {
	    	ArrayList<String> listaStringova = new ArrayList<>();
	    	for (String osobina : rezervacija.getOsobineSobe()) {
	    	    listaStringova.add(osobina);
	    	}
	    	stringOsobina = String.join("-", listaStringova);
	    	
    	}
    	else{
        	stringOsobina = "/";
    	}
    	
    	String stringRezervacije = String.join(",", rezervacija.getId(), String.valueOf(rezervacija.getStatusRezervacije().ordinal()+1), stringUsluga, rezervacija.getImeRezervacije().getKorisnickoIme(), rezervacija.getTipSobe().getOznaka(), stringOsobina, rezervacija.getDatumPocetka().format(formaterDatuma), rezervacija.getDatumKraja().format(formaterDatuma), String.valueOf(rezervacija.getCena()), rezervacija.getDatumKreiranja().format(formaterDatuma), brojSobeRez);
        return stringRezervacije;
    		
    	}

    public void obrisiRezervaciju(Rezervacija odabranaRezervacija) throws IOException {
    	boolean removed = rezervacije.removeIf(rezervacija -> rezervacija.getId().equals(odabranaRezervacija.getId()));
        if (removed) {
            upisiFajl();
            System.out.println("\nObrisana rezervacija " + odabranaRezervacija.getId() + ".\n ");
        } else {
            System.out.println("Nema rezervacije sa id-jem " + odabranaRezervacija.getId() + " u sistemu.\n");
        }
    }
    
    public void izmeniRezervaciju(Rezervacija nova, Rezervacija stara) throws IOException{
    	for (int i = 0; i < rezervacije.size(); i++) {
    		if (rezervacije.get(i).equals(stara)) {
    			rezervacije.set(i,nova);
    			System.out.println("uspesno izmenjena rezervacija");
    			upisiFajl();
    			return;
    		}
    	}
    }


    public void upisiFajl() throws IOException {
        FileWriter upisivanje = new FileWriter(fajl, false);
        for (Rezervacija rezervacija : rezervacije) {
            upisivanje.write(formatirajRezervaciju(rezervacija) + "\n");
        }
        upisivanje.close();
    }
    
    public void upisiPromene() throws IOException {
        FileWriter upisivanje = new FileWriter(fajlDolasci, false);
        for (Rezervacija rezervacija : danasnjePromeneRez) {
            upisivanje.write(formatirajRezervaciju(rezervacija) + "\n");
        }
        upisivanje.close();
    }
    
    
    public ArrayList<TipSobe> pregledDostupnihTipovaSoba(LocalDate pocetniDatum, LocalDate krajnjiDatum, ArrayList<TipSobe> sviTipoviSoba) {
    	ArrayList<TipSobe> filtriranaLista = sviTipoviSoba;
    	
    	for (Rezervacija rezervacija : rezervacije) {
    		for (TipSobe tipSobe : sviTipoviSoba) {
    			if (rezervacija.getTipSobe().getOznaka().equals(tipSobe.getOznaka())){
    				if((pocetniDatum.isAfter(rezervacija.getDatumPocetka()) && pocetniDatum.isBefore(rezervacija.getDatumPocetka())) || (krajnjiDatum.isAfter(rezervacija.getDatumPocetka()) && krajnjiDatum.isBefore(rezervacija.getDatumPocetka())) || ((pocetniDatum.isBefore(rezervacija.getDatumPocetka()) && (pocetniDatum.isAfter(rezervacija.getDatumKraja()))))){
						filtriranaLista.remove(tipSobe);
    					
    				}
    			}
    		}
    	}
    	
    	return filtriranaLista;
    }
    
    public ArrayList<Rezervacija> nadjiRezervacijeGosta(String korisnickoIme){
    	ArrayList<Rezervacija> konacno = new ArrayList<Rezervacija>();
    	
    	for (Rezervacija rezervacija : rezervacije) {
	        	
	        	if (rezervacija.getImeRezervacije().getKorisnickoIme().equals(korisnickoIme)) {
	        		konacno.add(rezervacija);
	        	
	        	}
    	}
    	return konacno;
    }

	public ArrayList<Rezervacija> getRezervacije() {
		return rezervacije;
	}

	public float izracunajCenuKorisniku(String korisnickoIme) {
		float ukupno = 0;
		for (Rezervacija rezervacija : rezervacije) {
			if (rezervacija.getImeRezervacije().getKorisnickoIme().equals(korisnickoIme) && rezervacija.getStatusRezervacije() != StatusRezervacije.ODBIJENA) {
				ukupno += rezervacija.getCena();
			}
		}
		return ukupno;
	}
	
	public Rezervacija dobaviRezervacijuPoId(String idRez) {
		for (Rezervacija rezervacija : rezervacije) {
			if (rezervacija.getId().equals(idRez)) {
				return rezervacija;
			}
		}
		System.out.println("nije pronadjena rezervacija sa id-jem" + idRez);
		return null;
	}
	
	public void promeniStatusRezervacije(String idRezervacije, StatusRezervacije noviStatus) {
		Rezervacija rez = dobaviRezervacijuPoId(idRezervacije);
		rez.setStatusRezervacije(noviStatus);
        try {
			upisiFajl();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getFajl() {
		return fajl;
	}
	
	public String getFajlDolasci() {
		return fajlDolasci;
	}

	public void setFajlDolasci(String fajlDolasci) {
		this.fajlDolasci = fajlDolasci;
	}

	public ArrayList<Rezervacija> getDanasnjePromeneRez() {
		return danasnjePromeneRez;
	}

	public void setDanasnjePromeneRez(ArrayList<Rezervacija> danasnjePromeneRez) {
		this.danasnjePromeneRez = danasnjePromeneRez;
	}

	public void refreshRezervacije(ArrayList<Gost> listaGostiju, ArrayList<TipSobe> listaTipovaSoba) throws IOException {
	    rezervacije.clear(); 
	    rezervacijeUListu(listaGostiju, listaTipovaSoba);  
	}
	
	// bice koriscena da se dobave ocekivani dolasci i odlasci
	public ArrayList<Rezervacija> dobaviOcekivano(StatusRezervacije status){  
		LocalDate todaysDate = LocalDate.now();
		ArrayList <Rezervacija> filtriranaLista = new ArrayList<>();
		
		for (Rezervacija rez : rezervacije) {
			if (status == StatusRezervacije.POTVRĐENA) {
				if (rez.getDatumPocetka().isEqual(todaysDate) && rez.getStatusRezervacije() == status) {
					filtriranaLista.add(rez);
					continue;
				}
			}
			else if (status == StatusRezervacije.U_TOKU) {
				if (rez.getDatumKraja().isEqual(todaysDate) && rez.getStatusRezervacije() == status) {
					filtriranaLista.add(rez);
					continue;
				}
			}
		}
		return filtriranaLista;
	}
	
	
	public Object[][] prikazRez(ArrayList<Rezervacija> rezervacije) {
		if (rezervacije == null) {
			return new Object[0][0];
		}
	    int redovi = rezervacije.size();
	    int kolone = 8; 

	    Object[][] data = new Object[redovi][kolone];

	    for (int i = 0; i < redovi; i++) {
	        Rezervacija rez = rezervacije.get(i);
	        data[i][0] = Integer.parseInt(rez.getId());
	        data[i][1] = rez.getImeRezervacije().getKorisnickoIme();
	        data[i][2] = rez.getTipSobe().getOznaka();
	        data[i][3] = rez.getDatumPocetka();
	        data[i][4] = rez.getDatumKraja();
	        data[i][5] = rez.getCena();
	        data[i][6] = rez.getStatusRezervacije();
	        if (rez.getSoba() == null) {
		        data[i][7] = 0;
	        }
	        else {
	        data[i][7] = rez.getSoba().getBrojSobe();
	        }
	    }

	    return data;
	}

	public Object[][] prikazRezGostu(ArrayList<Rezervacija> rezervacije) {
	    int redovi = rezervacije.size();
	    int kolone = 6; 

	    Object[][] data = new Object[redovi][kolone];

	    for (int i = 0; i < redovi; i++) {
	        Rezervacija rez = rezervacije.get(i);
	        data[i][0] = Integer.parseInt(rez.getId());
	        data[i][1] = rez.getTipSobe().getOznaka();
	        data[i][2] = rez.getDatumPocetka();
	        data[i][3] = rez.getDatumKraja();
	        data[i][4] = rez.getCena();
	        data[i][5] = rez.getStatusRezervacije();
	    }

	    return data;
	}
	
	public boolean validirajPotvrduRezervacije(String idRezervacije) {
	    Rezervacija novaRezervacija = dobaviRezervacijuPoId(idRezervacije);
		int brojDostupnihSoba = 0;
		if (novaRezervacija == null) {
	        System.out.println("Greška - prosleđena loša rezervacija");
	        return false;  // provera za grešku, ovo je kao 'da li postoji rezervacija sa ovim id'
	    }	    
//		System.out.println("tip sobe " + novaRezervacija.getTipSobe().getOznaka() + " a zahtevi " + novaRezervacija.getOsobineSobe());

		for (Soba soba : sobaMng.getSobe()) {
			boolean imaSve = true;
			if (soba.getTipSobe().getOznaka().equals(novaRezervacija.getTipSobe().getOznaka()) && !soba.getStatusSobe().equals(StatusSobe.OBRISANA)) {
				if (novaRezervacija.getOsobineSobe().size() == 0) {
//					System.out.println("daj sta das");
					brojDostupnihSoba++;
					continue;
				}
				for (String osobina : novaRezervacija.getOsobineSobe()) {
					if (!soba.getOsobineSobe().contains(osobina)) {
//						System.out.println("fali dodatak sobi");
						imaSve = false;
						break;
					}
				}
				
				if (imaSve) {
				brojDostupnihSoba += 1; // gledamo koliko ima soba tog tipa sa datim osobinama (visak osobina nije problem)
				}}
		}
		for (Rezervacija rez : rezervacije) {
			if (rez.getTipSobe().getOznaka().equals(novaRezervacija.getTipSobe().getOznaka()) && (rez.getStatusRezervacije() == StatusRezervacije.POTVRĐENA || rez.getStatusRezervacije() == StatusRezervacije.U_TOKU)) {
				 if ((novaRezervacija.getDatumPocetka().isBefore(rez.getDatumKraja()) && novaRezervacija.getDatumKraja().isAfter(rez.getDatumPocetka())) ||
		            	    (novaRezervacija.getDatumPocetka().isEqual(rez.getDatumKraja()) || novaRezervacija.getDatumKraja().isEqual(rez.getDatumPocetka())) ||
		            	    (novaRezervacija.getDatumPocetka().isBefore(rez.getDatumPocetka()) && novaRezervacija.getDatumKraja().isAfter(rez.getDatumPocetka())) ||
		            	    (novaRezervacija.getDatumPocetka().isBefore(rez.getDatumKraja()) && novaRezervacija.getDatumKraja().isEqual(rez.getDatumKraja()))) {
//		            	System.out.println("op preklapanje");
		            	brojDostupnihSoba -= 1;
		            	}

		        }
		}
		return brojDostupnihSoba > 0;
	}

	
	public ArrayList<Soba> dobaviSlobodneSobe(String idRezervacije) {
		ArrayList<Soba> filtriraneSobe = new ArrayList<>();
		Rezervacija odabranaRez = dobaviRezervacijuPoId(idRezervacije);
		for (Soba soba : sobaMng.getSobe()) {
			if (soba.getStatusSobe() == StatusSobe.SLOBODNA && odabranaRez.getTipSobe().getOznaka().equals(soba.getTipSobe().getOznaka())) {
				boolean imaOsobine = true;
				if (odabranaRez.getOsobineSobe().size() == 0) {
					filtriraneSobe.add(soba); 
				}
				for (String osobina : odabranaRez.getOsobineSobe()) {
					if (!soba.getOsobineSobe().contains(osobina)) {
//						System.out.println("fali dodatak sobi");
						imaOsobine = false;
					}
				}
				if (imaOsobine) {
				filtriraneSobe.add(soba);
				}
			}
		}
		
		return filtriraneSobe;
	}

	public String[] formatirajBrSoba(ArrayList<Soba> listaOdgSoba) {
		String [] lista = new String[listaOdgSoba.size()];
		for (int i = 0; i < listaOdgSoba.size(); i++) {
			lista[i] = String.valueOf(listaOdgSoba.get(i).getBrojSobe());
		}
		return lista;
	}

	public ArrayList<Rezervacija> ucitajDanasnjePodatke(ArrayList<Gost> listaGostiju, ArrayList<TipSobe> listaTipovaSoba) {
		ArrayList <Rezervacija> odabrane = new ArrayList<>();
		try{
        	BufferedReader citac = new BufferedReader(new FileReader(fajl));
     
        String red;
        while ((red = citac.readLine()) != null) {
           
            String podaci[] = red.split(",");
            if (podaci.length > 0) {
            	odabrane.add(obradaRezervacije(podaci, listaGostiju, listaTipovaSoba));
            }
        }
        citac.close();
        } catch (IOException e) {
			System.out.println("greska pri ucitavanju dnevnog loga dolazaka i odlazaka");
		}
		return odabrane;
	}

	public boolean proveriId(String idRez) {
		for (Rezervacija rez : rezervacije) {
			if (rez.getId().equals(idRez)) {
				return false;
			}
		}
		
		return true;
	}


}


