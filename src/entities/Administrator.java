package entities;

import java.time.LocalDate;

import enums.NivoiStrucneSpreme;
import enums.Pol;
import enums.Uloga;

public class Administrator extends Zaposleni{
	
//	    private ZaposleniManager zaposleniManager;
//	    private GostManager gostManager;
//	    private TipSobeManager tipSobeManager;
//	    private SobaManager sobaManager;
//	    private DodatneUslugeManager dodatneUslugeManager;
//	    private CenovnikManager cenovnikManager;
//	    private RezervacijaManager rezervacijaManager;

	public Administrator(String ime, String prezime, Pol pol, LocalDate datumRodjenja,
			String brojTelefona, String adresa, String korisnickoIme, String lozinka,
			NivoiStrucneSpreme nivoStrucneSpreme, int staz) {
		super(ime, prezime, pol, datumRodjenja, brojTelefona, adresa, korisnickoIme, lozinka, nivoStrucneSpreme,
				staz);
		this.uloga = Uloga.ADMINISTRATOR;
		// TODO Auto-generated constructor stub
	}
	
//    public void dodajZaposlenog(Zaposleni zaposleni, ArrayList<Zaposleni> listaZaposlenih) throws IOException {
//        zaposleniManager.dodajZaposlenog(zaposleni, listaZaposlenih);
//    }
//
//    public ArrayList<Zaposleni> zaposleniUListu() throws IOException {
//        return zaposleniManager.zaposleniUListu();
//    }
//
////    public void izmeniZaposlenog(Zaposleni zaposleni, ArrayList<Zaposleni> listaZaposlenih) throws IOException {
////        zaposleniManager.izmeniZaposlenog(zaposleni, listaZaposlenih);
////    }
//
//    public void obrisiZaposlenog(Zaposleni zaposleni, ArrayList<Zaposleni> listaZaposlenih) throws IOException {
//        zaposleniManager.obrisiZaposlenog(zaposleni, listaZaposlenih);
//    }
//	    
//    public void pregledSvihZaposlenih(ArrayList<Zaposleni> listaZaposlenih) throws IOException {
//        zaposleniManager.pregledSvihZaposlenih(listaZaposlenih);
//    }
//    
//    
//	public void dodajGosta(Gost gost, ArrayList<Gost> listaGostiju) throws IOException {
//        gostManager.dodajGosta(gost, listaGostiju);
//    }
//
//    public ArrayList<Gost> gostiUListu() throws IOException {
//        return gostManager.gostiUListu();
//    }
//
//    public void izmeniGosta(Gost stari, Gost novi, ArrayList<Gost> listaGosti) throws IOException {
//        gostManager.izmeniGosta(stari, novi, listaGosti);
//    }
//
//    public void obrisiGosta(Gost gost, ArrayList<Gost> listaGostiju) throws IOException {
//    	gostManager.obrisiGosta(gost, listaGostiju);
//    }
//	    
//    public void pregledGostiju(ArrayList<Gost> listaGostiju) throws IOException {
//    	gostManager.pregledGostiju(listaGostiju);
//    }
//    
//    
//    public void dodajTipSobe(TipSobe tipSobe, ArrayList<TipSobe> tipoviSoba) throws IOException {
//    	tipSobeManager.dodajTipSobe(tipSobe, tipoviSoba);
//    }
//
//    public ArrayList<TipSobe> tipoviSobaUListu() throws IOException {
//        return tipSobeManager.tipoviSobaUListu();
//    }
//
//    public void izmeniTipSobe(TipSobe stari, TipSobe novi, ArrayList<TipSobe> listaTipoviSoba) throws IOException {
//        tipSobeManager.izmeniTipSobe(stari, novi, listaTipoviSoba);
//    }
//
//    public void obrisiTipSobe(TipSobe tipSobe, ArrayList<TipSobe> tipoviSoba) throws IOException {
//    	tipSobeManager.obrisiTipSobe(tipSobe, tipoviSoba);
//    }
//	    
//    public void pregledTipovaSoba(ArrayList<TipSobe> tipoviSoba) throws IOException {
//    	tipSobeManager.pregledTipovaSoba(tipoviSoba);
//    }
//    
//    
//    public void dodajSobu(Soba soba, ArrayList<Soba> sveSobe) throws IOException {
//    	sobaManager.dodajSobu(soba, sveSobe);
//    }
//
//    public ArrayList<Soba> sobeUListu() throws IOException {
//        return sobaManager.sobeUListu();
//    }
//
//    public void izmeniSobu(Soba stara, Soba nova, ArrayList<Soba> sveSobe) throws IOException {
//    	sobaManager.izmeniSobu(stara, nova, sveSobe);
//    }
//
//    public void obrisiSobu(Soba soba, ArrayList<Soba> sveSobe) throws IOException {
//    	sobaManager.obrisiSobu(soba, sveSobe);
//    }
//	    
//    public void pregledSoba(ArrayList<Soba> sveSobe) throws IOException {
//    	sobaManager.pregledSoba(sveSobe);
//    }
//    
//    
//    public void dodajUslugu(DodatnaUsluga usluga, ArrayList<DodatnaUsluga> listaUsluga) throws IOException {
//    	dodatneUslugeManager.dodajUslugu(usluga, listaUsluga);
//    }
//
//    public ArrayList<DodatnaUsluga> uslugeUListu() throws IOException {
//        return dodatneUslugeManager.uslugeUListu();
//    }
//
//    public void izmeniUsugu(DodatnaUsluga nova, DodatnaUsluga stara, ArrayList<DodatnaUsluga> listaUsluga) throws IOException {
//    	dodatneUslugeManager.izmeniUslugu(stara, nova, listaUsluga);
//    }
//
//    public void obrisiUslugu(DodatnaUsluga odabranaUsluga, ArrayList<DodatnaUsluga> listaUsluga) throws IOException {
//    	dodatneUslugeManager.obrisiUslugu(odabranaUsluga, listaUsluga);
//    }
//	    
//    public void pregledUsluga(ArrayList <DodatnaUsluga> listaUsluga) throws IOException {
//    	dodatneUslugeManager.pregledUsluga(listaUsluga);
//    	
//    }
//   
//    public void definisiCenovnik(String id, LocalDate datumPocetka, LocalDate datumKraja, ArrayList<TipSobe> listaTipovaSoba, ArrayList<DodatnaUsluga> listaUsluga, ArrayList<Integer> listaCenaTipovaSoba, ArrayList<Integer> listaCenaUsluga, ArrayList<Cenovnik> sviCenovnici) throws IOException {
//    	cenovnikManager.definisiCenovnik(id, datumPocetka, datumKraja, listaTipovaSoba, listaUsluga, listaCenaTipovaSoba, listaCenaUsluga, sviCenovnici);
//    }
//
//    public ArrayList<Cenovnik> cenovniciUListu() throws IOException {
//        return cenovnikManager.cenovniciUListu();
//    }
//
//    public void izmeniCenuUsluge(String nazivUsluge, int novaCena, Cenovnik cenovnik, ArrayList<Cenovnik> sviCenovnici) throws IOException {
//    	cenovnikManager.izmeniCenuUsluge(nazivUsluge, novaCena, cenovnik, sviCenovnici);
//    }
//    
//    public void izmeniCenuTipaSobe(String oznakaTipaSobe, int novaCena, Cenovnik cenovnik, ArrayList<Cenovnik> sviCenovnici) throws IOException {
//    	cenovnikManager.izmeniCenuTipaSobe(oznakaTipaSobe, novaCena, cenovnik, sviCenovnici);
//    }
//    
//    public void obrisiCenovnik(String id, ArrayList<Cenovnik> cenovnici) {
//    	cenovnikManager.obrisiCenovnik(id, cenovnici);
//    }
//	    
//    public void pregledCenovnika(Cenovnik cenovnik) {
//    	cenovnikManager.pregledCenovnika(cenovnik);
//    }
//
//    public void pregledSvihCenovnika(ArrayList<Cenovnik> sviCenovnici) {
//    	cenovnikManager.pregledSvihCenovnika(sviCenovnici);
//    }
//    
//    
//    public void dodajRezervaciju(Rezervacija rezervacija, ArrayList<Rezervacija> listaRezervacija) throws IOException {
//    	rezervacijaManager.dodajRezervaciju(rezervacija, listaRezervacija);
//    }
//
//    public ArrayList<Rezervacija> rezervacijeUListu(ArrayList<Gost> listaGostiju, ArrayList<TipSobe> listaTipovaSoba) throws IOException {
//        return rezervacijaManager.rezervacijeUListu(listaGostiju, listaTipovaSoba);
//    }
//    
//    public Rezervacija nadjiRezervacijuPoIdu(String id, ArrayList<Rezervacija> rezervacije) {
//        return rezervacijaManager.nadjiRezervacijuPoIdu(id, rezervacije);
//    }
//
//    public void obrisiRezervaciju(Rezervacija odabranaRezervacija, ArrayList<Rezervacija> sveRezervacije) throws IOException {
//    	rezervacijaManager.obrisiRezervaciju(odabranaRezervacija, sveRezervacije);
//    }
//
//    public void izmeniRezervaciju(Rezervacija nova, Rezervacija stara, ArrayList<Rezervacija> listaRezervacija) throws IOException {
//    	rezervacijaManager.izmeniRezervaciju(nova, stara, listaRezervacija);
//    }
//	    
//    public void pregledRezervacija(ArrayList <Rezervacija> sveRezervacije) throws IOException {
//    	rezervacijaManager.pregledRezervacija(sveRezervacije);
//    }
//    
//    public void pregledRezervacijaGosta(Gost gost, ArrayList <Rezervacija> sveRezervacije) {
//    	rezervacijaManager.pregledRezervacijaGosta(gost, sveRezervacije);
//    }
//    
//    public ArrayList<TipSobe> pregledDostupnihTipovaSoba(ArrayList<Rezervacija> listaRezervacija, LocalDate pocetniDatum, LocalDate krajnjiDatum, ArrayList<TipSobe> sviTipoviSoba) {
//    	return rezervacijaManager.pregledDostupnihTipovaSoba(listaRezervacija, pocetniDatum, krajnjiDatum, sviTipoviSoba);
//    }
}

