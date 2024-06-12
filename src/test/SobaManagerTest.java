package test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import managers.SobaManager;
import entities.Soba;
import enums.StatusSobe;
import entities.TipSobe;

public class SobaManagerTest {

    private static SobaManager sobaManager;
    private static final String sobeFajl = "./testdata/testSobe.csv";
    private static final String ciscenjeFajl = "./testdata/testCiscenje.csv";
    private static final DateTimeFormatter formaterDatuma = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @BeforeClass
    public static void setUp() throws Exception {
        sobaManager = SobaManager.getInstance(sobeFajl, ciscenjeFajl);
        sobaManager.sobeUListu();
    }
    
    @Test
    public void testSobeUListu() throws IOException {
    	sobaManager.setSobe(new ArrayList<>());
        boolean result = sobaManager.sobeUListu();
        assertTrue("Sobe su uspesno ucitane iz fajla", result);

        ArrayList<Soba> sobe = sobaManager.getSobe();
        assertEquals("Trebalo bi da u listi budu 3 sobe", 3, sobe.size());
        assertEquals("Broj prve sobe bi trebao da bude 101", 101, sobe.get(0).getBrojSobe());
    }

    @Test
    public void testDodajSobu() throws IOException {
        TipSobe tipSobe = new TipSobe("dvokrevetna", "1+1", 2);
        ArrayList<String> osobine = new ArrayList<>();
        osobine.add("klima");
        osobine.add("wifi");
        Soba novaSoba = new Soba(tipSobe, 104, osobine); 
        sobaManager.dodajSobu(novaSoba);

        ArrayList<Soba> sobe = sobaManager.getSobe();
        assertTrue("Nova soba bi trebala da bude dodata listi soba", sobe.contains(novaSoba));
    }

    @Test
    public void testObrisiSobu() throws IOException {
        Soba sobaZaBrisanje = sobaManager.dobaviSobuPoBroju(103);
        sobaManager.obrisiSobu(sobaZaBrisanje);

        ArrayList<Soba> sobe = sobaManager.getSobe();
        assertFalse("Soba ne bi trebala vise da bude u listi", sobe.contains(sobaZaBrisanje));
    }

    @Test
    public void testIzmeniSobu() throws IOException {
        Soba staraSoba = sobaManager.dobaviSobuPoBroju(102); 
        TipSobe tipSobe = new TipSobe("jednokrevetna", "1", 1); 
        ArrayList<String> osobine = new ArrayList<>();
        osobine.add("tv");
        osobine.add("klima");
        osobine.add("wifi");
        osobine.add("terasa");
        Soba novaSoba = new Soba(tipSobe, 102, osobine);
        sobaManager.izmeniSobu(novaSoba, staraSoba);

        Soba izmenjenaSoba = sobaManager.dobaviSobuPoBroju(102);
        assertEquals("Soba bi trebala da bude izmenjena, a id sobe isti", novaSoba.getBrojSobe(), izmenjenaSoba.getBrojSobe());
    }
    
    @Test
    public void testIzmeniStatusSobe() throws IOException {
        int brojSobe = 102; 
        StatusSobe noviStatus = StatusSobe.ZAUZETO; 
        boolean result = sobaManager.izmeniStatusSobe(brojSobe, noviStatus);
        
        Soba izmenjenaSoba = sobaManager.dobaviSobuPoBroju(brojSobe);
        
        assertTrue("Status izmene bi trebao da bude true", result);
        assertNotNull("Soba sa brojem " + brojSobe + " bi trebala da postoji", izmenjenaSoba);
        assertEquals("Status sobe je promenjen na " + noviStatus, noviStatus, izmenjenaSoba.getStatusSobe());
    }
    
    @Test
    public void testDodajOciscenuSobu() throws IOException {
        int brojSobe = 101;
        String korisnickoIme = "testUser";
		String datumDanas = LocalDate.now().format(formaterDatuma).toString();
        String expectedLogEntry = "testUser," + datumDanas + ",101";

        sobaManager.dodajOciscenuSobu(brojSobe, korisnickoIme);

        List<String> lines = Files.readAllLines(Path.of(ciscenjeFajl));
        assertTrue("Log file should contain the entry for the cleaned room", lines.contains(expectedLogEntry));
    }
    
    @Test
    public void testUpisiFajl() throws Exception {
        ArrayList<Soba> sobe = new ArrayList<>();
        TipSobe noviTip = new TipSobe("dvokrevetna", "1+1", 2);
        TipSobe noviTip2 = new TipSobe("jednokrevetna", "1", 1);
        ArrayList<String> osobine1 = new ArrayList<>(); osobine1.add("tv"); osobine1.add("klima"); osobine1.add("wifi");
        ArrayList<String> osobine2 = new ArrayList<>(); osobine2.add("tv"); osobine2.add("klima"); osobine2.add("wifi"); osobine2.add("terasa");
        ArrayList<String> osobine3 = new ArrayList<>(); osobine3.add("klima"); osobine3.add("wifi");

        sobe.add(new Soba(noviTip2, 101, osobine1, StatusSobe.SLOBODNA));
        sobe.add(new Soba(noviTip2, 102, osobine2, StatusSobe.ZAUZETO));
        sobe.add(new Soba(noviTip, 103, osobine3, StatusSobe.SLOBODNA));
        sobaManager.setSobe(sobe);

        Method method = SobaManager.class.getDeclaredMethod("upisiFajl");
        method.setAccessible(true);
        method.invoke(sobaManager);

        ArrayList<String> lines = new ArrayList<>(Files.readAllLines(Paths.get(sobeFajl)));
        assertEquals("Trebalo bi da budu 3 linije", 3, lines.size());
        assertEquals("Prva linija bi trebala da bude tacno formatirana", "101,jednokrevetna,1,1,tv-klima-wifi,1", lines.get(0));
        assertEquals("Druga linija bi trebala da bude tacno formatirana", "102,jednokrevetna,1,1,tv-klima-wifi-terasa,2", lines.get(1));
        assertEquals("Treca linija bi trebala da bude tacno formatirana", "103,dvokrevetna,1+1,2,klima-wifi,1", lines.get(2));
    }
    

    @AfterClass
    public static void tearDown() {
        try {
            FileWriter writer = new FileWriter(sobeFajl);
            writer.write("101,jednokrevetna,1,1,tv-klima-wifi,1\n");
            writer.write("102,jednokrevetna,1,1,tv-klima-wifi-terasa,2\n");
            writer.write("103,dvokrevetna,1+1,2,klima-wifi,1\n");
            writer.close();
            
            FileWriter writer2 = new FileWriter(ciscenjeFajl);
            writer2.write("janaj,06/08/2024,106\n");
            writer2.write("draga,06/09/2024,104\n");
            writer2.write("janaj,06/11/2024,204\n");
            writer2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
