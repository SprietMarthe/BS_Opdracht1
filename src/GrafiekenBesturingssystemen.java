import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.Objects;

public class GrafiekenBesturingssystemen extends JFrame {

    public GrafiekenBesturingssystemen(int i) {
        initUI(i);
    }

    private void initUI(int i) {

        XYDataset datasetnormTAT = createDataset(i);
        JFreeChart chart = createChart("Genormaliseerde omlooptijd ifv percentiel van nodige tijd", "Genormaliseerde omlooptijd", datasetnormTAT);
        XYDataset datasetwachttijd = createDataset(i);
        JFreeChart chart2 = createChart("Wachttijd ifv percentiel van nodige tijd", "Wachttijd", datasetwachttijd);

        ChartPanel chartPanel;
        if (i == 1) chartPanel= new ChartPanel(chart);
        else chartPanel = new ChartPanel(chart2);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);

        pack();
        setTitle("Scheduling Algoritms");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Tweede chart

    }

    private XYDataset createDataset(int i) {
        Main.initialiseerArrays();
        ReadXMLFile.readingXMLFile(Main.aantalProcessen, Main.pid, Main.aankomsttijd, Main.bedieningstijd);
        var dataset = new XYSeriesCollection();
        var dataset2 = new XYSeriesCollection();
        Main.berekenFCFS();
        berekenAlgoritme(dataset, dataset2, "FCFS");
        Main.berekenSJF();
        berekenAlgoritme(dataset, dataset2, "SJF");
        Main.berekenSRT();
        berekenAlgoritme(dataset, dataset2, "SRT");
        Main.berekenRR(1);
        berekenAlgoritme(dataset, dataset2, "RR q=1");
        Main.berekenRR(4);
        berekenAlgoritme(dataset, dataset2, "RR q=4");
        Main.berekenRR(8);
        berekenAlgoritme(dataset, dataset2, "RR q=8");
        Main.berekenHRRN();
        berekenAlgoritme(dataset, dataset2, "HRRN");
        Main.berekenMLFB(1);
        berekenAlgoritme(dataset, dataset2, "MLFB q=1");
        Main.berekenMLFB(2);
        berekenAlgoritme(dataset, dataset2, "MLFB q=2^i");
        if (i==1) return dataset;
        else return dataset2;
    }

    private void berekenAlgoritme(XYSeriesCollection dataset, XYSeriesCollection dataset2, String algoritme) {
        var serie = new XYSeries(algoritme);
        var serie2 = new XYSeries(algoritme);
        int percentielteller = 0;
        int percentielgrootte = (Main.aantalProcessen/100);
        int genormOmlooptijd = 0;
        int wachttijd = 0;
        for (int i=0; i<Main.aantalProcessen; i++){
            genormOmlooptijd += Main.genormaliseerdeOmlooptijd[i];
            wachttijd += Main.wachttijd[i];
            if (percentielteller != 0 && percentielteller%percentielgrootte == 0) {
                serie.add(percentielteller/percentielgrootte, genormOmlooptijd/percentielgrootte);
                serie2.add(percentielteller/percentielgrootte, wachttijd/percentielgrootte);
                genormOmlooptijd=0;
                wachttijd = 0;
                }
            percentielteller++;
        }
        dataset.addSeries(serie);
        dataset2.addSeries(serie2);
    }

    private JFreeChart createChart(String titel, String y, XYDataset dataset) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Scheduling Algoritmes",
                "Percentiel van nodige tijd",
                y,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        XYPlot plot = chart.getXYPlot();

        var renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        renderer.setDrawSeriesLineAsPath(true);

        NumberAxis yAxis = new LogarithmicAxis(y);
        plot.setRangeAxis(yAxis);

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);



        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle(titel,
                        new Font("Serif", java.awt.Font.BOLD, 18))
        );
        return chart;
    }

    public static void main(String[] args, int i) {
        EventQueue.invokeLater(() -> {
            var graph = new GrafiekenBesturingssystemen(1);
            graph.setVisible(true);
        });
    }
}