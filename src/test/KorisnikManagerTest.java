package test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import managers.KorisnikManager;
import managers.SobaManager;
import entities.Administrator;
import entities.Korisnik;
import entities.Recepcioner;
import entities.Soba;
import entities.Sobarica;
import enums.NivoiStrucneSpreme;
import enums.Pol;

public class KorisnikManagerTest {

    private static KorisnikManager korisnikManager;
    private static final String korisniciFajl = "./testdata/testKorisnici.csv";
    private static SobaManager sobaManager;
    private static final String sobeFajl = "./testdata/testSobe.csv";
    private static final String ciscenjeFajl = "./testdata/testCiscenje.csv";
    
    @BeforeClass
    public static void setUp() throws Exception {
    	sobaManager = SobaManager.getInstance(sobeFajl, ciscenjeFajl);
        sobaManager.sobeUListu();
    	korisnikManager = KorisnikManager.getInstance(korisniciFajl, sobaManager);
        korisnikManager.ucitajKorisnike();
    }
    
    @Test
    public void testUcitajKorisnike() throws IOException {
    	korisnikManager.setKorisnici(new ArrayList<>());
        boolean result = korisnikManager.ucitajKorisnike();
        assertTrue("Korisnici su uspesno ucitani iz fajla", result);

        ArrayList<Korisnik> korisnici = korisnikManager.getKorisnici();
        assertEquals("Trebalo bi da u listi bude 6 korisnika", 6, korisnici.size());
        assertEquals("Korisnicko ime prvog korisnika bi trebalo da bude 'perap'", "perap", korisnici.get(0).getKorisnickoIme());
    }

    @Test
    public void testDodajKorisnika() {
        int initialSize = korisnikManager.getKorisnici().size();
        Recepcioner noviKorisnik = new Recepcioner("Novi", "Korisnik", Pol.M, LocalDate.of(1998, 10, 10), "06111111", "Panciceva 32 Novi Sad", "novikor", "novikorisnik123", NivoiStrucneSpreme.sedmi, 4);
        korisnikManager.dodajKorisnika(noviKorisnik);
        int newSize = korisnikManager.getKorisnici().size();
        assertEquals("Broj korisnika treba da se poveća za 1", initialSize + 1, newSize);
        assertTrue("Novi korisnik treba da bude dodat", korisnikManager.getKorisnici().contains(noviKorisnik));
    }
    
    @Test
    public void testObrisiKorisnika() {
        int initialSize = korisnikManager.getKorisnici().size();
        Korisnik korisnikZaBrisanje = korisnikManager.getKorisnici().get(3);
        korisnikManager.obrisiKorisnika(korisnikZaBrisanje.getKorisnickoIme());
        int newSize = korisnikManager.getKorisnici().size();
        assertEquals("Broj korisnika treba da se smanji za 1", initialSize - 1, newSize);
        assertFalse("Obrisan korisnik ne sme da bude prisutan", korisnikManager.getKorisnici().contains(korisnikZaBrisanje));
    }
    
    @Test
    public void testIzmeniKorisnika() {
        Korisnik stariKorisnik = korisnikManager.getKorisnici().get(0);
        Korisnik noviKorisnik = new Administrator("NovoIme", "NovoPrezime", Pol.M, LocalDate.of(1988, 11, 11), "0686895623", "Bulevar oslobodjenja 34 Novi Sad", "perap", "pera", NivoiStrucneSpreme.osmi, 13);
        korisnikManager.izmeniKorisnika(noviKorisnik, stariKorisnik);
        assertEquals("Korisnicko ime treba da ostane isto", stariKorisnik.getKorisnickoIme(), noviKorisnik.getKorisnickoIme());
        assertEquals("Ime korisnika treba da bude promenjeno", "NovoIme", noviKorisnik.getIme());
        assertEquals("Prezime korisnika treba da bude promenjeno", "NovoPrezime", noviKorisnik.getPrezime());
    }
    
    @Test
    public void testDodeliSobuZaCiscenje() {
        Sobarica sobarica = new Sobarica("Jelena", "Jelenić", Pol.Z, LocalDate.of(1999, 5, 5), "123456", "Adresa", "jelena", "jelena", NivoiStrucneSpreme.peti, 3);
        korisnikManager.dodajKorisnika(sobarica);

        int initialSobaCount = sobarica.getListaDodeljenihSoba().size();
        korisnikManager.dodeliSobuZaCiscenje("101");
        int finalSobaCount = sobarica.getListaDodeljenihSoba().size();

        assertEquals("Broj dodeljenih soba bi trebao da se poveca za 1", initialSobaCount + 1, finalSobaCount);
        korisnikManager.obrisiKorisnika("jelena");
    }

    @Test
    public void testUkloniSobu() {
        Soba soba = sobaManager.dobaviSobuPoBroju(101);
        Sobarica sobarica = new Sobarica("Jelena", "Jelenić", Pol.Z, LocalDate.of(1999, 5, 5), "123456", "Adresa", "jelena", "jelena", NivoiStrucneSpreme.peti, 3);
        sobarica.dodajSobu(soba);
        korisnikManager.dodajKorisnika(sobarica);

        korisnikManager.ukloniSobu(sobarica, 101);
        assertFalse("Soba bi trebala da bude obrisana iz Jeleninih soba", sobarica.getListaDodeljenihSoba().contains(soba));
        korisnikManager.obrisiKorisnika("jelena");
    }

    @Test
    public void testValidirajUsername() {
        assertFalse("Korisnicko ime 'mika' bi trebalo da bude nevalidno jer vec postoji", korisnikManager.validirajUsername("mika"));
        assertTrue("Korisnicko ime 'milivoje' bi trebalo da bude validno jer ne postoji", korisnikManager.validirajUsername("milivoje"));
    }

        
    @AfterClass
    public static void tearDown() throws IOException {
        FileWriter writer = new FileWriter(korisniciFajl);
        List<String> lines = new ArrayList<>();
        lines.add("pera,peric,0,1,04/20/1985,0612345678,Bulevar Oslobodjenja Novi Sad,perap,pera,8,10");
        lines.add("mika,mikic,1,1,05/05/1985,0612345678,Bulevar Oslobodjenja Novi Sad,mika,mika,6,5");
        lines.add("nikola,nikolic,1,1,02/14/1985,0612345678,Bulevar Oslobodjenja Novi Sad,nikola,nikola,6,6");
        lines.add("jana,janic,2,2,09/07/1985,0612345678,Bulevar Oslobodjenja Novi Sad,janaj,jancica,4,10,102");
        lines.add("Milica,Milić,3,2,01/04/1985,0612345678,Bulevar Oslobodjenja Novi Sad,milica,milica");
        lines.add("Ana,Anić,3,2,08/04/1985,0612345678,Bulevar Oslobodjenja Novi Sad,ana,ana");

        for (String line : lines) {
            writer.write(line + "\n");
        }

        writer.close();
    }

}
