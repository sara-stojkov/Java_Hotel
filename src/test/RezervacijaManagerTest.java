package test;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import entities.Gost;
import entities.Rezervacija;
import entities.TipSobe;
import enums.Pol;
import enums.StatusRezervacije;
import managers.RezervacijaManager;
import managers.SobaManager;

public class RezervacijaManagerTest {

    private static RezervacijaManager rezervacijaManager;
    private static SobaManager sobaManager;
    private static String fajlRez = "./testData/testRezervacije.csv";
    private static String fajlDolasci = "./testData/testDolasci.csv";

    @BeforeClass
    public static void setUp() throws IOException {
        sobaManager = SobaManager.getInstance("./testData/testSobe.csv", "./testData/testCiscenje.csv");
        sobaManager.sobeUListu();
        rezervacijaManager = RezervacijaManager.getInstance(fajlRez, sobaManager, fajlDolasci);
    }

    @Test
    public void testGetInstance() {
        assertNotNull(rezervacijaManager);
    }

    @Test
    public void testRezervacijeUListu() throws IOException {
    	
    	FileWriter writer = new FileWriter(fajlRez);
        writer.write("1,5,rucak-dorucak,verica,2+1,wifi,06/11/2024,06/19/2024,27600.0,06/11/2024,102\n");
        writer.write("2,2,dorucak,mihajlo,1+1,terasa,07/02/2024,07/14/2024,78720.0,06/11/2024,0\n");
        writer.write("3,3,/,mihajlo,2,terasa,08/02/2024,08/10/2024,46400.0,06/11/2024,0\n");
        writer.close();
    	
    	rezervacijaManager.setRezervacije(new ArrayList<>());
        ArrayList<Gost> listaGostiju = new ArrayList<>();
        listaGostiju.add(new Gost("Mihajlo", "Mihajlo", Pol.M, LocalDate.of(1988, 11, 11), "0686895623", "Bulevar oslobodjenja 34 Novi Sad", "mihajlo", "mihajlo")); 
        listaGostiju.add(new Gost("Verica", "Verica", Pol.Z, LocalDate.of(1988, 11, 11), "0686895623", "Bulevar oslobodjenja 34 Novi Sad", "verica", "verica")); 
        ArrayList<TipSobe> listaTipovaSoba = new ArrayList<>();
        listaTipovaSoba.add(new TipSobe("trokrevetna", "2+1", 3)); 
        listaTipovaSoba.add(new TipSobe("dvokrevetna", "1+1", 2)); 
        listaTipovaSoba.add(new TipSobe("dvokrevetna", "2", 2)); 
        
        boolean result = rezervacijaManager.rezervacijeUListu(listaGostiju, listaTipovaSoba);

        assertTrue("Rezervacije bi trealo da su ucitane", result);
        assertEquals("Trebalo bi da ima 3 rezervacije", 3, rezervacijaManager.getRezervacije().size());
    }

    @Test
    public void testDodajRezervaciju() throws IOException {
        Gost noviGost = new Gost("Ljubica", "Ljubica", Pol.Z, LocalDate.of(1988, 11, 11), "0686895623", "Bulevar oslobodjenja 34 Novi Sad", "ljubica", "ljubica"); 
        TipSobe noviTip = new TipSobe("trokrevetna", "2+1", 3); 

        LocalDate datumPocetka = LocalDate.now().plusDays(1);
        LocalDate datumKraja = LocalDate.now().plusDays(6);
        Rezervacija rezervacija = new Rezervacija("10", StatusRezervacije.POTVRĐENA, noviGost, new ArrayList<>(), noviTip, new ArrayList<>(), datumPocetka, datumKraja, 10000.0, LocalDate.now(), null);

        rezervacijaManager.dodajRezervaciju(rezervacija);

        assertTrue(rezervacijaManager.getRezervacije().contains(rezervacija));
    }

    @Test
    public void testObrisiRezervaciju() throws IOException {
    	Gost noviGost = new Gost("Dunja", "Dunja", Pol.Z, LocalDate.of(1988, 11, 11), "0686895623", "Bulevar oslobodjenja 34 Novi Sad", "dunja", "dunja"); 
        TipSobe noviTip = new TipSobe("trokrevetna", "2+1", 3);
        LocalDate datumPocetka = LocalDate.now().plusDays(1);
        LocalDate datumKraja = LocalDate.now().plusDays(6);
        Rezervacija rezervacija = new Rezervacija("11", StatusRezervacije.NA_ČEKANjU, noviGost, new ArrayList<>(), noviTip, new ArrayList<>(), datumPocetka, datumKraja, 100.0, LocalDate.now(), null);

        rezervacijaManager.dodajRezervaciju(rezervacija);
        rezervacijaManager.obrisiRezervaciju(rezervacija);

        assertFalse(rezervacijaManager.getRezervacije().contains(rezervacija));
    }

    @Test
    public void testIzmeniRezervaciju() throws IOException {
    	Gost noviGost = new Gost("Vanja", "Vanja", Pol.Z, LocalDate.of(1988, 11, 11), "0686895623", "Bulevar oslobodjenja 34 Novi Sad", "vanja", "vanja"); 
        TipSobe noviTip = new TipSobe("cetvorokrevetna", "2+2", 4);
        LocalDate datumPocetka = LocalDate.now().plusDays(1);
        LocalDate datumKraja = LocalDate.now().plusDays(5);
        Rezervacija staraRezervacija = new Rezervacija("12", StatusRezervacije.POTVRĐENA, noviGost, new ArrayList<>(), noviTip, new ArrayList<>(), datumPocetka, datumKraja, 10000.0, LocalDate.now(), null);
        Rezervacija novaRezervacija = new Rezervacija("12", StatusRezervacije.POTVRĐENA, noviGost, new ArrayList<>(), noviTip, new ArrayList<>(), datumPocetka, datumKraja, 15000.0, LocalDate.now(), null);

        rezervacijaManager.dodajRezervaciju(staraRezervacija);
        rezervacijaManager.izmeniRezervaciju(novaRezervacija, staraRezervacija);

        assertTrue(rezervacijaManager.getRezervacije().contains(novaRezervacija));
        assertFalse(rezervacijaManager.getRezervacije().contains(staraRezervacija));
    }

    @Test
    public void testIzracunajCenuKorisniku() throws IOException {
    	Gost noviGost = new Gost("Vanja", "Vanja", Pol.Z, LocalDate.of(1988, 11, 11), "0686895623", "Bulevar oslobodjenja 34 Novi Sad", "vanja", "vanja"); 
        TipSobe noviTip = new TipSobe("cetvorokrevetna", "2+2", 4);
        LocalDate datumPocetka = LocalDate.now().plusDays(1);
        LocalDate datumKraja = LocalDate.now().plusDays(5);
        Rezervacija rezervacija1 = new Rezervacija("15", StatusRezervacije.POTVRĐENA, noviGost, new ArrayList<>(), noviTip, new ArrayList<>(), datumPocetka, datumKraja, 12000.0, LocalDate.now(), null);
        Rezervacija rezervacija2 = new Rezervacija("16", StatusRezervacije.ODBIJENA, noviGost, new ArrayList<>(), noviTip, new ArrayList<>(), datumPocetka, datumKraja, 12000.0, LocalDate.now(), null);

        rezervacijaManager.dodajRezervaciju(rezervacija1);
        rezervacijaManager.dodajRezervaciju(rezervacija2);

        assertEquals(12000.0, rezervacijaManager.izracunajCenuKorisniku("vanja"), 0.001);
        
        
    	Gost noviGost2 = new Gost("Dunja", "Dunja", Pol.Z, LocalDate.of(1988, 11, 11), "0686895623", "Bulevar oslobodjenja 34 Novi Sad", "dunja", "dunja"); 
        TipSobe noviTip2 = new TipSobe("cetvorokrevetna", "2+2", 4);
        LocalDate datumPocetka2 = LocalDate.now().plusDays(10);
        LocalDate datumKraja2 = LocalDate.now().plusDays(15);
        Rezervacija rezervacija12 = new Rezervacija("21", StatusRezervacije.POTVRĐENA, noviGost2, new ArrayList<>(), noviTip2, new ArrayList<>(), datumPocetka2, datumKraja2, 14000.0, LocalDate.now(), null);
        Rezervacija rezervacija22 = new Rezervacija("22", StatusRezervacije.OTKAZANA, noviGost2, new ArrayList<>(), noviTip2, new ArrayList<>(), datumPocetka2, datumKraja2, 14000.0, LocalDate.now(), null);

        rezervacijaManager.dodajRezervaciju(rezervacija12);
        rezervacijaManager.dodajRezervaciju(rezervacija22);

        assertEquals(28000.0, rezervacijaManager.izracunajCenuKorisniku("dunja"), 0.001);
        
    }

    @Test
    public void testDobaviRezervacijuPoId() throws IOException {
    	Gost gost = new Gost("Dunja", "Dunja", Pol.Z, LocalDate.of(1988, 11, 11), "0686895623", "Bulevar oslobodjenja 34 Novi Sad", "dunja", "dunja"); 
        TipSobe tipSobe = new TipSobe("cetvorokrevetna", "2+2", 4);
        LocalDate datumPocetka = LocalDate.now().plusDays(1);
        LocalDate datumKraja = LocalDate.now().plusDays(5);
        Rezervacija rezervacija = new Rezervacija("30", StatusRezervacije.POTVRĐENA, gost, new ArrayList<>(), tipSobe, new ArrayList<>(), datumPocetka, datumKraja, 100.0, LocalDate.now(), null);

        rezervacijaManager.dodajRezervaciju(rezervacija);

        assertEquals(rezervacija, rezervacijaManager.dobaviRezervacijuPoId("30"));
    }

    @Test
    public void testPromeniStatusRezervacije() throws IOException {
    	Gost gost = new Gost("Dunja", "Dunja", Pol.Z, LocalDate.of(1988, 11, 11), "0686895623", "Bulevar oslobodjenja 34 Novi Sad", "dunja", "dunja"); 
        TipSobe tipSobe = new TipSobe("cetvorokrevetna", "2+2", 4);
        LocalDate datumPocetka = LocalDate.now().plusDays(1);
        LocalDate datumKraja = LocalDate.now().plusDays(5);
        Rezervacija rezervacija = new Rezervacija("24", StatusRezervacije.POTVRĐENA, gost, new ArrayList<>(), tipSobe, new ArrayList<>(), datumPocetka, datumKraja, 100.0, LocalDate.now(), null);

        rezervacijaManager.dodajRezervaciju(rezervacija);
        rezervacijaManager.promeniStatusRezervacije("24", StatusRezervacije.ODBIJENA);

        assertEquals(StatusRezervacije.ODBIJENA, rezervacijaManager.dobaviRezervacijuPoId("24").getStatusRezervacije());
    }
    
    
    @AfterClass
    public static void tearDown() {
        try {
            FileWriter writer = new FileWriter(fajlRez);
            writer.write("1,5,rucak-dorucak,verica,2+1,wifi,06/11/2024,06/19/2024,27600.0,06/11/2024,102\n");
            writer.write("2,2,dorucak,mihajlo,1+1,terasa,07/02/2024,07/14/2024,78720.0,06/11/2024,0\n");
            writer.write("3,3,/,mihajlo,2,terasa,08/02/2024,08/10/2024,46400.0,06/11/2024,0\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}