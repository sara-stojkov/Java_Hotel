package reports;

import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.util.ArrayList;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.PieStyler;

import entities.Rezervacija;
import enums.StatusRezervacije;
import managers.RezervacijaManager;

public class ChartRezervacije implements ExampleChart<PieChart>  {

    private final RezervacijaManager rezMng;

    public ChartRezervacije(RezervacijaManager rezMng) {
        this.rezMng = rezMng;
        
    }

    @Override
    public PieChart getChart() {
        PieChart chart = new PieChartBuilder().width(800).height(600).title("Statusi rezervacija u poslednjih 30 dana").build();

        Object[][] podaci = dobaviRezervacije30(rezMng);

        for (Object[] row : podaci) {
            String statusRez = (String) row[0];
            int brojRezervacija = (int) row[1];
            chart.addSeries(statusRez, brojRezervacija);
        }

        PieStyler styler = chart.getStyler();
        styler.setLegendVisible(true);
        Font legendFont = new Font("Arial", Font.PLAIN, 20);
        styler.setLegendFont(legendFont);

        Color[] sliceColors = new Color[] { new Color(56, 198, 52), new Color(102, 178, 255), new Color(255, 153, 51), new Color(243, 180, 159), new Color(255, 255, 51) };
        chart.getStyler().setSeriesColors(sliceColors);

        return chart;
    }

    public static Object[][] dobaviRezervacije30(RezervacijaManager rezMng) {
        LocalDate kraj = LocalDate.now();
        LocalDate pocetak = LocalDate.now().minusDays(30); // idemo 30 dana unazad
        ArrayList<Rezervacija> sveRez = rezMng.getRezervacije();
        Object[][] listaStatusa = new Object[4][2];
        for (int i = 0; i < 4; i++) {
            int brRez = 0;
            for (Rezervacija rez : sveRez) {
            	if (rez.getStatusRezervacije().equals(StatusRezervacije.fromInteger(i+1))) {
            	if (rez.getDatumKreiranja().isEqual(pocetak) || rez.getDatumKreiranja().isEqual(kraj) || (rez.getDatumKreiranja().isBefore(kraj) && rez.getDatumKreiranja().isAfter(pocetak))) {
    				brRez ++;
                }}
            }
            listaStatusa[i][0] = StatusRezervacije.fromInteger(i+1).toString();
            listaStatusa[i][1] = brRez;
        }
        return listaStatusa;
    }

	@Override
	public String getExampleChartName() {
		return "Rezervacije";
	}
}
	

