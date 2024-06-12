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
import managers.TipSobeManager;
import entities.TipSobe;

public class TipSobeManagerTest {

    private static TipSobeManager tipSobeManager;
    private static final String tipoviSobaFajl = "./testdata/testTipoviSoba.csv";

    @BeforeClass
    public static void setUp() throws Exception {
    	tipSobeManager = TipSobeManager.getInstance(tipoviSobaFajl);
    	tipSobeManager.tipoviSobaUListu();
    }
    
    @Test
    public void testtipoviSobaUListu() throws IOException {
    	tipSobeManager.setTipoviSoba(new ArrayList<>());
        boolean result = tipSobeManager.tipoviSobaUListu();
        assertTrue("Tipovi soba su uspesno ucitani iz fajla", result);

        ArrayList<TipSobe> tipoviSoba = tipSobeManager.getTipoviSoba();
        assertEquals("Trebalo bi da u listi budu 3 tipa soba", 3, tipoviSoba.size());
        assertEquals("Oznaka prvog tipa bi trebala da bude '1'", "1", tipoviSoba.get(0).getOznaka());
    }

    @Test
    public void testDodajTipSobe() throws IOException {
        TipSobe tipSobe = new TipSobe("trokrevetna", "2+1", 3); 
        tipSobeManager.dodajTipSobe(tipSobe);
        
        ArrayList<TipSobe> tipoviSoba = tipSobeManager.getTipoviSoba();
        assertTrue("Novi tip sobe 1 bi trebao da bude dodat listi tipova soba", tipoviSoba.contains(tipSobe));
        tipSobeManager.obrisiTipSobe(tipSobe);
        TipSobe tipSobe2 = new TipSobe("cetvorokrevetna", "2+2", 4); 
        tipSobeManager.dodajTipSobe(tipSobe2);
        assertTrue("Novi tip sobe 2 bi trebao da bude dodat listi tipova soba", tipoviSoba.contains(tipSobe2));
    }

    @Test
    public void testObrisiTipSobe() throws IOException {
        TipSobe odabraniTip = tipSobeManager.nadjiTipPoOznaci("1+1");
        tipSobeManager.obrisiTipSobe(odabraniTip);
        
        ArrayList<TipSobe> sobe = tipSobeManager.getTipoviSoba();
        assertFalse("Tip sobe sa oznakom '1+1' ne bi trebao da bude u listi tipova soba", sobe.contains(odabraniTip));
    }

    @Test
    public void testIzmeniTipSobe() throws IOException {
    	TipSobe stariTipSobe = tipSobeManager.dobaviTipPoOznaci("1");
    	TipSobe noviTipSobe = new TipSobe("dvokrevetna", "1", 2); // zamisao: dodajemo onaj mobilni krevet u svaku ovu sobu :D

    	tipSobeManager.izmeniTipSobe(noviTipSobe, stariTipSobe);

        TipSobe izmenjeniTip = tipSobeManager.dobaviTipPoOznaci("1");
        assertEquals("Tip sobe bi trebao da bude izmenjen, a oznaka tipa sobe ista", noviTipSobe.getOznaka(), izmenjeniTip.getOznaka());
    }
    
    @Test
    public void testProveriZauzetostOznake() {
        assertFalse("Oznaka '1+1' bi trebala da bude nevalidna jer ne postiji", tipSobeManager.proveriZauzetostOznake("1+1"));
        assertTrue("Oznaka '2+2+1' bi trebala da bude validna jer ne postoji", tipSobeManager.proveriZauzetostOznake("2+2+1"));
    }

    
    @Test
    public void testUpisiFajl() throws Exception {
        ArrayList<TipSobe> tipoviSoba = new ArrayList<>();
        
        tipoviSoba.add(new TipSobe("jednokrevetna", "1", 1));
        tipoviSoba.add(new TipSobe("dvokrevetna", "2", 2));
        tipoviSoba.add(new TipSobe("dvokrevetna", "1+1", 2));
        tipSobeManager.setTipoviSoba(tipoviSoba);

        Method method = TipSobeManager.class.getDeclaredMethod("upisiFajl");
        method.setAccessible(true);
        method.invoke(tipSobeManager);

        ArrayList<String> lines = new ArrayList<>(Files.readAllLines(Paths.get(tipoviSobaFajl)));
        assertEquals("Broj linija bi trebao da bude 3", 3, lines.size());
        assertEquals("Prva linija bi trebala da bude tacno formatirana", "jednokrevetna,1,1", lines.get(0));
        assertEquals("Druga linija bi trebala da bude tacno formatirana", "dvokrevetna,2,2", lines.get(1));
        assertEquals("Treca linija bi trebala da bude tacno formatirana", "dvokrevetna,1+1,2", lines.get(2));
    }
    

    @AfterClass
    public static void tearDown() {
        try {
            FileWriter writer = new FileWriter(tipoviSobaFajl);
            writer.write("jednokrevetna,1,1\n");
            writer.write("dvokrevetna,2,2\n");
            writer.write("dvokrevetna,1+1,2\n");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
