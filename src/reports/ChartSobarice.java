package reports;

import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.PieStyler;

import entities.Sobarica;
import managers.KorisnikManager;
import managers.SobaManager;

public class ChartSobarice implements ExampleChart<PieChart> {

    private static final DateTimeFormatter formaterDatuma = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private final SobaManager sobaMng;
    private final KorisnikManager korMng;

    public ChartSobarice(SobaManager sobaMng, KorisnikManager korMng) {
        this.sobaMng = sobaMng;
        this.korMng = korMng;
        
    }

    @Override
    public PieChart getChart() {
        PieChart chart = new PieChartBuilder().width(800).height(600).title("Opterećenost sobarica u poslednjih 30 dana").build();

        Object[][] podaci = dobaviBrSobaPoSobarici30(sobaMng, korMng);

        for (Object[] row : podaci) {
            String imeSobarice = (String) row[0];
            int brojSobaSobarice = (int) row[1];
            chart.addSeries(imeSobarice, brojSobaSobarice);
        }

        PieStyler styler = chart.getStyler();
        styler.setLegendVisible(true);
        Font legendFont = new Font("Arial", Font.PLAIN, 20);
        styler.setLegendFont(legendFont);

        Color[] sliceColors = new Color[] { new Color(224, 68, 14), new Color(102, 107, 255), new Color(255, 255, 51), new Color(243, 180, 159), new Color(246, 199, 182) };
        chart.getStyler().setSeriesColors(sliceColors);

        return chart;
    }

    public static Object[][] dobaviBrSobaPoSobarici30(SobaManager sobaMng, KorisnikManager korMng) {
        LocalDate kraj = LocalDate.now();
        LocalDate pocetak = LocalDate.now().minusDays(30); // idemo 30 dana unazad
        ArrayList<Sobarica> sveSobarice = korMng.getSobarice();
        ArrayList<String> strCiscenjeLogovi = sobaMng.getListaOciscenihSoba();
        Object[][] listaSobaricaSoba = new Object[sveSobarice.size()][2];
        for (int i = 0; i < sveSobarice.size(); i++) {
            int brSoba = 0;
            for (String jednoCiscenje : strCiscenjeLogovi) {
                String[] nizDelova = jednoCiscenje.split(",");
                if (nizDelova[0].equals(sveSobarice.get(i).getKorisnickoIme())) {
                    LocalDate datumCiscenja = LocalDate.parse(nizDelova[1], formaterDatuma);
                    if (datumCiscenja.isEqual(pocetak) || datumCiscenja.isEqual(kraj) || (datumCiscenja.isBefore(kraj) && datumCiscenja.isAfter(pocetak))) {
                        brSoba += 1; 
                    }
                }
            }
            listaSobaricaSoba[i][0] = sveSobarice.get(i).getKorisnickoIme();
            listaSobaricaSoba[i][1] = brSoba;
        }
        return listaSobaricaSoba;
    }

	@Override
	public String getExampleChartName() {
		return "Sobarica";
	}
}
