package reports;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.style.AxesChartStyler.TextAlignment;
import org.knowm.xchart.style.Styler.LegendPosition;

import entities.Rezervacija;
import entities.TipSobe;
import enums.StatusRezervacije;
import managers.RezervacijaManager;
import managers.TipSobeManager;

public class ChartPrihoda implements ExampleChart<XYChart> {

    private final RezervacijaManager rezMng;
    private final TipSobeManager tSMng;

    public ChartPrihoda(RezervacijaManager rezMng, TipSobeManager tSobeMng) {
        this.rezMng = rezMng;
        this.tSMng = tSobeMng;
    }

    @Override
    public XYChart getChart() {

    	Object[][] podaci = dobaviPrihode12(rezMng, tSMng);

        XYChart chart = new XYChartBuilder().width(900).height(700).title("Prihodi po tipu sobe u poslednjih 12 meseci")
                .xAxisTitle("Meseci").yAxisTitle("Prihod").build();

        String[] meseci = new String[12];
        LocalDate now = LocalDate.now();
        for (int i = 0; i < 12; i++) {
            meseci[i] = now.minusMonths(11 - i).getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        }

        double[] xData = new double[12];
        for (int i = 0; i < 12; i++) {
            xData[i] = i + 1;
        }

        for (Object[] row : podaci) {
            String tipSobe = (String) row[0];
            double[] prihodi = (double[]) row[1];
            chart.addSeries(tipSobe, xData, prihodi);
        }
        
        chart.getStyler().setYAxisDecimalPattern("#,###");
        chart.getStyler().setChartTitleVisible(true);
        chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
        chart.getStyler().setXAxisLabelAlignment(TextAlignment.Right);
        chart.getStyler().setXAxisLabelRotation(0);


        Map<Double, String> xAxisLabelMap = new HashMap<>();
        for (int i = 0; i < meseci.length; i++) {
            xAxisLabelMap.put((double) (i + 1), meseci[i]);
        }

        Function<Double, String> xAxisLabelFormatter = xAxisLabelMap::get;
        chart.setCustomXAxisTickLabelsFormatter(xAxisLabelFormatter);

        return chart;
    }
    
    
    public static Object[][] dobaviPrihode12(RezervacijaManager rezMng, TipSobeManager tSMng) {
        LocalDate skrozPocetak = LocalDate.now().minusMonths(11); // idemo 12 meseci unazad
        ArrayList<Rezervacija> sveRez = rezMng.getRezervacije();
        ArrayList<TipSobe> tipoviSoba = tSMng.getTipoviSoba();
        Object[][] listaPrihoda = new Object[tipoviSoba.size() + 1][2];  // plus 1 jer nam treba i ukupno

        for (int i = 0; i < tipoviSoba.size(); i++) {
            double[] listaTipaSobe = new double[12];
//            System.out.println("trenutni tip sobe: " + tipoviSoba.get(i).getOznaka());

            for (int j = 0; j < 12; j++) { 
                double prihodMeseca = 0;
                LocalDate pocetak = skrozPocetak.plusMonths(j).withDayOfMonth(1);
                LocalDate krajMeseca = pocetak.withDayOfMonth(pocetak.lengthOfMonth());

//                System.out.println("  Mesec: " + pocetak.getMonth() + " (" + pocetak + " to " + krajMeseca + ")");

                for (Rezervacija rez : sveRez) {
                    if (rez.getTipSobe().getOznaka().equals(tipoviSoba.get(i).getOznaka())
                            && rez.getStatusRezervacije() != (StatusRezervacije.ODBIJENA)) {
                        if ((rez.getDatumKreiranja().isEqual(pocetak) || rez.getDatumKreiranja().isEqual(krajMeseca)
                                || (rez.getDatumKreiranja().isAfter(pocetak)
                                        && rez.getDatumKreiranja().isBefore(krajMeseca)))) {
                            prihodMeseca += rez.getCena();
//                            System.out.println("    Rezervacija: " + rez.getDatumKreiranja() + ", Price: " + rez.getCena());
                        }
                    }
                }
                listaTipaSobe[j] = prihodMeseca; 
//                System.out.println(" Mesecni prihodi: " + prihodMeseca);
            }

            listaPrihoda[i][0] = tipoviSoba.get(i).getOznaka();
            listaPrihoda[i][1] = listaTipaSobe;
        }

        double[] ukupnoPrihodi = new double[12];
        for (int i = 0; i < 12; i++) {
            double ukupno = 0;
            for (int j = 0; j < tipoviSoba.size(); j++) {
                double[] prihodMeseci = (double[]) listaPrihoda[j][1];
                ukupno += prihodMeseci[i];
            }
            ukupnoPrihodi[i] = ukupno;
        }

        listaPrihoda[tipoviSoba.size()][0] = "Ukupno";
        listaPrihoda[tipoviSoba.size()][1] = ukupnoPrihodi;

//        System.out.println("Ukupno po mesecima: " + Arrays.toString(ukupnoPrihodi));

        return listaPrihoda;
    }



    @Override
    public String getExampleChartName() {
        return "Prihodi";
    }
}
