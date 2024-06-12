 package managers;

import enums.NivoiStrucneSpreme;
import enums.Pol;
import enums.Uloga;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import entities.*;

public class KorisnikManager {
	
	private static KorisnikManager instance;
    private String korisniciFajl;
    private ArrayList <Korisnik> korisnici;
    private SobaManager sobaMng;
    private static final DateTimeFormatter formaterDatuma = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	
    private KorisnikManager(String korisniciFilePath, SobaManager menadzerSoba) {
		this.korisniciFajl = korisniciFilePath;
		korisnici = new ArrayList<Korisnik>();
		this.sobaMng = menadzerSoba;
	}
    
    public static KorisnikManager getInstance(String filePath, SobaManager menadzerSoba) {
    	if (instance == null) {
    		instance = new KorisnikManager(filePath, menadzerSoba);	
    	}
    	return instance;
    }
    
    
	public ArrayList<Korisnik> getKorisnici() {
		return korisnici;
	}


	public static Korisnik login(String korisnickoIme, String lozinka, ArrayList<Korisnik> korisnici) {
		
		for (Korisnik korisnik : korisnici) {
			if (korisnik.getKorisnickoIme().equals(korisnickoIme) && korisnik.getLozinka().equals(lozinka)) {
				return korisnik;
			}
		}
		
		return null;
	}
	
	public boolean ucitajKorisnike() {
		try {
			BufferedReader citac = new BufferedReader(new FileReader(korisniciFajl));
	        String red;
	        while ((red = citac.readLine()) != null) {
	            String podaci[] = red.split(",");
	            if (podaci.length > 0) {
	            	korisnici.add(obradaKorisnika(podaci));
	            }
	        }
	        citac.close();
	} catch (IOException e) {
		return false;
	}
	return true;
}

	
	public Korisnik obradaKorisnika(String[] nizPodataka) {
		 String ime = nizPodataka[0];
        String prezime = nizPodataka[1];
        Uloga radnoMesto = Uloga.fromInteger(Integer.parseInt(nizPodataka[2]));
        Pol pol = Pol.fromString(nizPodataka[3]);
        LocalDate datumRodjenja = LocalDate.parse(nizPodataka[4], formaterDatuma);
        String brojTelefona = nizPodataka[5];
        String adresa = nizPodataka[6];
        String korisnickoIme = nizPodataka[7];
        String lozinka = nizPodataka[8];
        
        if (nizPodataka.length == 9) {
        	return new Gost(ime, prezime, pol, datumRodjenja, brojTelefona, adresa, korisnickoIme, lozinka);
        }

        NivoiStrucneSpreme nivoSpreme = NivoiStrucneSpreme.fromInteger(Integer.parseInt(nizPodataka[9]));
        int staz = Integer.parseInt(nizPodataka[10]);


        if (radnoMesto == Uloga.SOBARICA) {
        	ArrayList <Soba> listaDodeljenihSoba = new ArrayList<Soba>();
        	if (nizPodataka[11].equals("/")) {
        		
        	}
        	else{
        		String[] sobe = nizPodataka[11].split("-");
        	
	        	for (String brojSobe : sobe) {
	        		Soba soba = sobaMng.dobaviSobuPoBroju(Integer.parseInt(brojSobe));
	        		listaDodeljenihSoba.add(soba);
	        	}
        	}
            return new Sobarica(ime, prezime, pol, datumRodjenja, brojTelefona, adresa, korisnickoIme, lozinka, nivoSpreme, staz, listaDodeljenihSoba);

        }
        else if (radnoMesto == Uloga.ADMINISTRATOR) {
        	return new Administrator(ime, prezime, pol, datumRodjenja, brojTelefona, adresa, korisnickoIme, lozinka, nivoSpreme, staz);
        }
        else if (radnoMesto == Uloga.RECEPCIONER) {
        	return new Recepcioner(ime, prezime, pol, datumRodjenja, brojTelefona, adresa, korisnickoIme, lozinka, nivoSpreme, staz);
        }
        System.out.println("neka greska sa ulogom...");
        return null; 
	}

	
	public ArrayList<Gost> getGosti() {
	    ArrayList<Gost> listaGostiju = new ArrayList<>();
	    for (Korisnik korisnik : korisnici) {
	        if (korisnik.getUloga() == Uloga.GOST) {
	            if (korisnik instanceof Gost) {
	                listaGostiju.add((Gost) korisnik);
	            }
	        }
	    }
	    return listaGostiju;
	}
	
	public ArrayList<Sobarica> getSobarice() {
	    ArrayList<Sobarica> listaSobarica = new ArrayList<>();
	    for (Korisnik korisnik : korisnici) {
	        if (korisnik.getUloga() == Uloga.SOBARICA) {
	            if (korisnik instanceof Sobarica) {
	            	listaSobarica.add((Sobarica) korisnik);
	            }
	        }
	    }
	    return listaSobarica;
	}
	
	public ArrayList<Zaposleni> getZaposleni() {
	    ArrayList<Zaposleni> listaZaposlenih = new ArrayList<>();
	    for (Korisnik korisnik : korisnici) {
	        if (korisnik.getUloga() == Uloga.SOBARICA) {
	            if (korisnik instanceof Sobarica) {
	            	listaZaposlenih.add((Sobarica) korisnik);
	            }
	        }
	        
	        else if (korisnik.getUloga() == Uloga.RECEPCIONER) {
	            if (korisnik instanceof Recepcioner) {
	            	listaZaposlenih.add((Recepcioner) korisnik);
	            }
	        }
	        continue;
	    }
	    return listaZaposlenih;
	}
	
	
	public Korisnik dobaviKorisnikaPoUsername(String korisnickoIme) {
		for (Korisnik korisnik : korisnici) {
			if (korisnik.getKorisnickoIme().equals(korisnickoIme)) {
				if (korisnik.getUloga() == Uloga.SOBARICA) {
					return (Sobarica)korisnik;
				}
				else if (korisnik.getUloga() == Uloga.GOST) {
					return (Gost)korisnik;
				}
				else if (korisnik.getUloga() == Uloga.RECEPCIONER) {
					return (Recepcioner)korisnik;
				}
				else if (korisnik.getUloga() == Uloga.ADMINISTRATOR) {
					return (Administrator)korisnik;
				}
			}
		}
		return null;
	}


	public Object[][] formatirajGoste() {
		ArrayList <Gost> listaGostiju = getGosti();
		Object [][] modifikovanaLista = new Object[listaGostiju.size()][6];
		for (int i = 0; i < listaGostiju.size(); i++) {
			modifikovanaLista [i][0] = listaGostiju.get(i).getKorisnickoIme();
			modifikovanaLista [i][1] = listaGostiju.get(i).getIme();
			modifikovanaLista [i][2] = listaGostiju.get(i).getPrezime();
			modifikovanaLista [i][3] = listaGostiju.get(i).getPol().toString();
			modifikovanaLista [i][4] = listaGostiju.get(i).getDatumRodjenja();
			modifikovanaLista [i][5] = listaGostiju.get(i).getBrojTelefona();
		}
		return modifikovanaLista;
	}


	public void dodajKorisnika(Korisnik noviKorisnik) {
	    synchronized (korisnici) {
	        for (Korisnik korisnik : korisnici) {
	            if (korisnik.getKorisnickoIme().equals(noviKorisnik.getKorisnickoIme())) {
	                System.out.println("greska - vec postoji ovaj korisnik");
	                return;
	            }
	        }
	        korisnici.add(noviKorisnik);
	    }
	    upisiUFajl();
	}


	private void upisiUFajl() {
		FileWriter upisivanje;
		try {
			upisivanje = new FileWriter(korisniciFajl, false);
			for (Korisnik korisnik : korisnici) {
				upisivanje.write(formatirajKorisnika(korisnik) + "\n");
        }
        upisivanje.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}
	
	static private String formatirajKorisnika(Korisnik korisnik) {
	    if (korisnik instanceof Gost) {
	        return formatirajGosta((Gost) korisnik);
	    } else if (korisnik instanceof Zaposleni) {
	        return formatirajZaposlenog((Zaposleni) korisnik);
	    }
	    return null; 
	}

	
	
	static private String formatirajZaposlenog(Zaposleni zaposleni) {
		String stringZaposlenog = "";
		if (zaposleni.getUloga() == Uloga.SOBARICA) {
			String sobe = String.join("-", ((Sobarica)zaposleni).listaSobaString());
			if (((Sobarica)zaposleni).listaSobaString().length == 0) {
				sobe = "/";
			}
	        stringZaposlenog = String.join(",", zaposleni.getIme(), zaposleni.getPrezime(), String.valueOf(zaposleni.getUloga().ordinal()), String.valueOf((zaposleni.getPol().ordinal()+1)), zaposleni.getDatumRodjenja().format(formaterDatuma), zaposleni.getBrojTelefona(), zaposleni.getAdresa(), zaposleni.getKorisnickoIme(), zaposleni.getLozinka(), String.valueOf(zaposleni.getNivoStrucneSpreme().ordinal() + 1), String.valueOf(zaposleni.getStaz()), sobe);
		}
		else {
			stringZaposlenog = String.join(",", zaposleni.getIme(), zaposleni.getPrezime(), String.valueOf(zaposleni.getUloga().ordinal()), String.valueOf((zaposleni.getPol().ordinal()+1)), zaposleni.getDatumRodjenja().format(formaterDatuma), zaposleni.getBrojTelefona(), zaposleni.getAdresa(), zaposleni.getKorisnickoIme(), zaposleni.getLozinka(), String.valueOf(zaposleni.getNivoStrucneSpreme().ordinal() + 1), String.valueOf(zaposleni.getStaz()));
		}
		return stringZaposlenog;
    }


	static private String formatirajGosta(Gost korisnik) {
		if (korisnik.getUloga() == Uloga.GOST) {
			String stringGost = String.join(",", korisnik.getIme(), korisnik.getPrezime(),String.valueOf(korisnik.getUloga().ordinal()), String.valueOf((korisnik.getPol().ordinal() + 1)), korisnik.getDatumRodjenja().format(formaterDatuma), korisnik.getBrojTelefona(), korisnik.getAdresa(), korisnik.getKorisnickoIme(), korisnik.getLozinka());
	        return stringGost;
		}
		
		return null;
	}

	public Object[][] formatirajZaposleneMatrica() {
		ArrayList <Zaposleni> listaZap = getZaposleni();
		Object [][] modifikovanaLista = new Object[listaZap.size()][9];
		for (int i = 0; i < listaZap.size(); i++) {
			
			modifikovanaLista [i][0] = listaZap.get(i).getKorisnickoIme();
			modifikovanaLista [i][1] = listaZap.get(i).getUloga();
			modifikovanaLista [i][2] = listaZap.get(i).getIme();
			modifikovanaLista [i][3] = listaZap.get(i).getPrezime();
			modifikovanaLista [i][4] = listaZap.get(i).getPol();
			modifikovanaLista [i][5] = listaZap.get(i).getDatumRodjenja();
			modifikovanaLista [i][6] = listaZap.get(i).getBrojTelefona();
			modifikovanaLista [i][7] = listaZap.get(i).getNivoStrucneSpreme();
			modifikovanaLista [i][8] = listaZap.get(i).getStaz();
			
		}
		return modifikovanaLista;
	}

	public void obrisiKorisnika(String korisnickoIme) {
		Korisnik korisnik = dobaviKorisnikaPoUsername(korisnickoIme);
		korisnici.remove(korisnik);
		upisiUFajl();
	}

	public void dodeliSobuZaCiscenje(String brSobe) {
		ArrayList<Sobarica> sveSobarice = getSobarice();
		String korisnickoImeSobarice = "";
		int najmanjiBrojSoba = 100000;
		for (Sobarica sobarica : sveSobarice) {
			if (sobarica.getListaDodeljenihSoba().size() < najmanjiBrojSoba) {
				najmanjiBrojSoba = sobarica.getListaDodeljenihSoba().size();
				korisnickoImeSobarice = sobarica.getKorisnickoIme();
				System.out.println("soba " + brSobe + " dodeljena sobarici " + korisnickoImeSobarice);
			}
		}
		
		Sobarica odabranaSobarica = (Sobarica) dobaviKorisnikaPoUsername(korisnickoImeSobarice);
		odabranaSobarica.dodajSobu(sobaMng.dobaviSobuPoBroju(Integer.parseInt(brSobe)));
		
	}

	public void ukloniSobu(Sobarica sobarica, int brojSobe) {
		sobarica.getListaDodeljenihSoba().remove(sobaMng.dobaviSobuPoBroju(brojSobe));
		upisiUFajl();
	}

	public boolean validirajUsername(String noviUsername) {
		for (Korisnik korisnik : korisnici) {
			if (korisnik.getKorisnickoIme().equals(noviUsername)) {
				return false;
			}
		}
		
		return true;
	}
	
	public void izmeniKorisnika(Korisnik novi, Korisnik stari) {
	    synchronized (korisnici) {
	        boolean found = false;
	        for (int i = 0; i < korisnici.size(); i++) {
	            Korisnik korisnik = korisnici.get(i);
	            if (korisnik.getKorisnickoIme().equals(stari.getKorisnickoIme())) {
	                korisnici.set(i, novi);
	                found = true;
	                break;
	            }
	        }
	        if (!found) {
	            System.out.println("Stari korisnik nije pronađen - izmena neuspela.");
	        }
	    }
	    upisiUFajl();
	}


}
