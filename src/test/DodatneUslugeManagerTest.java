package test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import managers.DodatneUslugeManager;
import entities.DodatnaUsluga;

public class DodatneUslugeManagerTest {

    private static DodatneUslugeManager dodatneUslugeManager;
    private static final String uslugeFajl = "./testdata/testUsluge.csv";
 
    @BeforeClass
    public static void setUp() throws Exception {
        dodatneUslugeManager = DodatneUslugeManager.getInstance(uslugeFajl);
        dodatneUslugeManager.uslugeUListu();
    }
    
    @Test
    public void testUslugeUListu() throws IOException {
    	FileWriter writer = new FileWriter(uslugeFajl);
        writer.write("bazen,true\n");
        writer.write("rucak,true\n");
        writer.write("dorucak,true\n");
        writer.write("vecera,true\n");
        writer.close();
    	
    	dodatneUslugeManager.setDodatneUsluge(new ArrayList<>());
        boolean result = dodatneUslugeManager.uslugeUListu();
        assertTrue("Dodatne usluge su uspesno ucitane iz fajla", result);

        ArrayList<DodatnaUsluga> usluge = dodatneUslugeManager.getDodatneUsluge();
        assertEquals("Trebalo bi da u listi budu 4 dodatne usluge", 4, usluge.size());
        assertEquals("Naziv prve uslugee bi trebao da bude 'bazen'", "bazen", usluge.get(0).getNaziv());
    }

    @Test
    public void testDodajUslugu() throws IOException {
        DodatnaUsluga novaUsluga = new DodatnaUsluga("sauna");
 
        dodatneUslugeManager.dodajUslugu(novaUsluga);

        ArrayList<DodatnaUsluga> sveUsluge = dodatneUslugeManager.getDodatneUsluge();
        assertTrue("Nova usluga bi trebala da bude dodata listi usluga", sveUsluge.contains(novaUsluga));
    }

    @Test
    public void testObrisiUslugu() throws IOException {
        DodatnaUsluga uslugaZaBrisanje = dodatneUslugeManager.nadjiUsluguPoNazivu("vecera");
        dodatneUslugeManager.obrisiUslugu("vecera");

        assertEquals("Usluga bi trebala da bude NEaktivna (logicko brisanje)", false, uslugaZaBrisanje.isAktivna());
    }

    @Test
    public void testIzmeniUslugu() throws IOException {
    	DodatnaUsluga staraUsluga = dodatneUslugeManager.nadjiUsluguPoNazivu("dorucak"); 
    	DodatnaUsluga novaUsluga = new DodatnaUsluga("doručak"); 
    
        dodatneUslugeManager.izmeniUslugu(novaUsluga, staraUsluga);

        assertNull("Stari naziv ne bi trebao da bude u listi", dodatneUslugeManager.nadjiUsluguPoNazivu("dorucak"));
        assertNotNull("Novi naziv se nalazi u listi", dodatneUslugeManager.nadjiUsluguPoNazivu("doručak"));
    }
    
    
    @Test
    public void testUpisiFajl() throws Exception {
        ArrayList<DodatnaUsluga> usluge = new ArrayList<>();
        DodatnaUsluga usluga1 = new DodatnaUsluga("bazen");
        DodatnaUsluga usluga2 = new DodatnaUsluga("dorucak");
        DodatnaUsluga usluga3 = new DodatnaUsluga("rucak");
        DodatnaUsluga usluga4 = new DodatnaUsluga("vecera");

        usluge.add(usluga1);
        usluge.add(usluga3);
        usluge.add(usluga2);
        usluge.add(usluga4);
        
        dodatneUslugeManager.setDodatneUsluge(usluge);

        Method method = DodatneUslugeManager.class.getDeclaredMethod("upisiFajl");
        method.setAccessible(true);
        method.invoke(dodatneUslugeManager);

        ArrayList<String> lines = new ArrayList<>(Files.readAllLines(Paths.get(uslugeFajl)));
        assertEquals("Trebalo bi da budu 4 linije", 4, lines.size());
        assertEquals("Prva linija bi trebala da bude tacno formatirana", "bazen,true", lines.get(0));
        assertEquals("Druga linija bi trebala da bude tacno formatirana", "rucak,true", lines.get(1));
        assertEquals("Treca linija bi trebala da bude tacno formatirana", "dorucak,true", lines.get(2));
        assertEquals("Cetvrta linija bi trebala da bude tacno formatirana", "vecera,true", lines.get(3));
    }
    

    @AfterClass
    public static void tearDown() {
        try {
            FileWriter writer = new FileWriter(uslugeFajl);
            writer.write("bazen,true\n");
            writer.write("rucak,true\n");
            writer.write("dorucak,true\n");
            writer.write("vecera,true\n");
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
