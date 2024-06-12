package test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import managers.CenovnikManager;
import entities.Cenovnik;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class CenovnikManagerTest {
    
    private static CenovnikManager cenovnikManager;
    private static final String cenovniciFajl = "./testdata/testCenovnici.csv";

    @BeforeClass
    public static void setUp() throws Exception {
        cenovnikManager = CenovnikManager.getInstance(cenovniciFajl);
        cenovnikManager.cenovniciUListu();
    }

    @Test
    public void testCenovniciUListu() throws IOException {
    	FileWriter writer = new FileWriter(cenovniciFajl);
        writer.write("1,2024-01-01,2024-12-31,1:1790,2:2100,1+1:2250,2+1:2560,rucak:400,dorucak:320\n");
        writer.write("2,2024-06-30,2024-08-30,1:1900,2:2150,1+1:2350,2+1:2480,rucak:430,dorucak:350\n");
        writer.close();
    	
        cenovnikManager.setCenovnici(new ArrayList<>());
        boolean result = cenovnikManager.cenovniciUListu();
        assertTrue("Cenovnici su uspešno učitani iz fajla", result);

        ArrayList<Cenovnik> cenovnici = cenovnikManager.getCenovnici();
        assertEquals("Treba da bude učitano 2 cenovnika", 2, cenovnici.size());
    }

    @Test
    public void testDodajCenovnik() throws IOException {
        HashMap<String, Integer> ceneTipovaSoba = new HashMap<>();
        ceneTipovaSoba.put("1", 2000);
        ceneTipovaSoba.put("1+1", 2400);
        ceneTipovaSoba.put("2", 2300);
        ceneTipovaSoba.put("2+1", 2900);
        
        HashMap<String, Integer> ceneUsluga = new HashMap<>();
        ceneUsluga.put("dorucak", 500);
        ceneUsluga.put("rucak", 600);

        Cenovnik noviCenovnik = new Cenovnik("3", LocalDate.now(), LocalDate.now().plusDays(30), ceneTipovaSoba, ceneUsluga);
        cenovnikManager.dodajCenovnik(noviCenovnik);

        ArrayList<Cenovnik> sviCenovnici = cenovnikManager.getCenovnici();
        assertTrue("Novi cenovnik bi trebao da bude dodat u listu cenovnika", sviCenovnici.contains(noviCenovnik));
    }

    @Test
    public void testObrisiCenovnik() throws IOException {
        String idZaBrisanje = "2";
        boolean result = cenovnikManager.obrisiCenovnik(idZaBrisanje);
        assertTrue("Uspesno obrisan cenovnik sa id-jem 2", result);
        assertNull("Cenovnik sa datim id-jem ne bi trebao više da bude u listi", cenovnikManager.dobaviCenovnikPoId(idZaBrisanje));
    }

    @Test
    public void testIzmeniCenovnik() throws IOException {
        Cenovnik stariCenovnik = cenovnikManager.dobaviCenovnikPoId("2");
        HashMap<String, Integer> ceneTipovaSoba = new HashMap<>();
        ceneTipovaSoba.put("dvokrevetna", 5000);
        HashMap<String, Integer> ceneUsluga = new HashMap<>();
        ceneUsluga.put("vecera", 800);

        Cenovnik noviCenovnik = new Cenovnik("2", LocalDate.now(), LocalDate.now().plusDays(30), ceneTipovaSoba, ceneUsluga);
        cenovnikManager.izmeniCenovnik(noviCenovnik, stariCenovnik);

        Cenovnik izmenjenCenovnik = cenovnikManager.dobaviCenovnikPoId("2");
        assertEquals("Izmenjeni cenovnik bi trebao da ima novu cenu tipa sobe", Integer.valueOf(5000), izmenjenCenovnik.getCeneTipovaSoba().get("dvokrevetna"));
    }

    @Test
    public void testUpisiUFajl() throws Exception {
        ArrayList<Cenovnik> cenovnici = new ArrayList<>();
        HashMap<String, Integer> ceneTipovaSoba1 = new HashMap<>();
        ceneTipovaSoba1.put("jednokrevetna", 3000);
        HashMap<String, Integer> ceneUsluga1 = new HashMap<>();
        ceneUsluga1.put("dorucak", 500);

        HashMap<String, Integer> ceneTipovaSoba2 = new HashMap<>();
        ceneTipovaSoba2.put("dvokrevetna", 5000);
        HashMap<String, Integer> ceneUsluga2 = new HashMap<>();
        ceneUsluga2.put("vecera", 800);

        Cenovnik cenovnik1 = new Cenovnik("1", LocalDate.now(), LocalDate.now().plusDays(60), ceneTipovaSoba1, ceneUsluga1);
        Cenovnik cenovnik2 = new Cenovnik("2", LocalDate.now(), LocalDate.now().plusDays(30), ceneTipovaSoba2, ceneUsluga2);

        cenovnici.add(cenovnik1);
        cenovnici.add(cenovnik2);

        cenovnikManager.setCenovnici(cenovnici);

        Method method = CenovnikManager.class.getDeclaredMethod("upisiUFajl");
        method.setAccessible(true);
        method.invoke(cenovnikManager);

        ArrayList<String> lines = new ArrayList<>(Files.readAllLines(Paths.get(cenovniciFajl)));
        assertEquals("Treba da bude 2 linije", 2, lines.size());
        assertTrue("Prva linija bi trebala da bude tačno formatirana", lines.get(0).contains("1"));
        assertTrue("Druga linija bi trebala da bude tačno formatirana", lines.get(1).contains("2"));
    }

    @AfterClass
    public static void tearDown() {
        try {
            FileWriter writer = new FileWriter(cenovniciFajl);
            writer.write("1,2024-01-01,2024-12-31,1:1790,2:2100,1+1:2250,2+1:2560,rucak:400,dorucak:320\n");
            writer.write("2,2024-06-30,2024-08-30,1:1900,2:2150,1+1:2350,2+1:2480,rucak:430,dorucak:350\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
