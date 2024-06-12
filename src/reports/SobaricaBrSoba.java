package reports;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import entities.Sobarica;
import managers.KorisnikManager;
import managers.SobaManager;

public class SobaricaBrSoba {
	
    private static final DateTimeFormatter formaterDatuma = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	public static Object[][] dobaviBrSobaPoSobarici(SobaManager sobaMng, KorisnikManager korMng, LocalDate pocetak, LocalDate kraj){
		ArrayList <Sobarica> sveSobarice = korMng.getSobarice();
		ArrayList <String> strCiscenjeLogovi = sobaMng.getListaOciscenihSoba();
		Object[][] listaSobaricaSoba = new Object[sveSobarice.size()][2];
		for (int i = 0; i < sveSobarice.size(); i ++) {
			int brSoba = 0;
			for (String jednoCiscenje : strCiscenjeLogovi) {
				String[] nizDelova = jednoCiscenje.split(",");
				if (nizDelova[0].equals(sveSobarice.get(i).getKorisnickoIme())) {
					LocalDate datumCiscenja = LocalDate.parse(nizDelova[1], formaterDatuma);
					if (datumCiscenja.isEqual(pocetak) || datumCiscenja.isEqual(kraj) || (datumCiscenja.isBefore(kraj) && datumCiscenja.isAfter(pocetak))) {
						brSoba += 1;  // odnosno, ovo je koliko je ciscenja obavljeno
					}
						
				}
				continue;
			}
			listaSobaricaSoba[i][0] = sveSobarice.get(i).getKorisnickoIme();
			listaSobaricaSoba[i][1] = brSoba;
		}
		return listaSobaricaSoba;
	}

}
