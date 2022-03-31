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

import static java.lang.Math.log;

public class GrafiekenBesturingssystemen extends JFrame {

    public GrafiekenBesturingssystemen() {
        initUI();
    }

    private void initUI() {

        XYDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);

        pack();
        setTitle("Scheduling Algoritms");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private XYDataset createDataset() {
        Main.initialiseerArrays();
        ReadXMLFile.readingXMLFile(Main.aantalProcessen, Main.pid, Main.aankomsttijd, Main.bedieningstijd);
        var dataset = new XYSeriesCollection();
        Main.berekenFCFS();
        berekenAlgoritme(dataset, "FCFS");
        Main.berekenSJF();
        berekenAlgoritme(dataset, "SJF");
        Main.berekenSRT();
        berekenAlgoritme(dataset, "SRT");
        Main.berekenRR(1);
        berekenAlgoritme(dataset, "RR q=1");
        Main.berekenRR(4);
        berekenAlgoritme(dataset, "RR q=4");
        Main.berekenRR(8);
        berekenAlgoritme(dataset, "RR q=8");
        Main.berekenHRRN();
        berekenAlgoritme(dataset, "HRRN");
        return dataset;
    }

    private void berekenAlgoritme(XYSeriesCollection dataset, String algoritme) {
        var serie = new XYSeries(algoritme);
        int percentielteller = 0;
        int percentielgrootte = (Main.aantalProcessen/100);
        int genormOmlooptijd = 0;
        for (int i=0; i<Main.aantalProcessen; i++){
            genormOmlooptijd += Main.genormaliseerdeOmlooptijd[i];
            if (percentielteller != 0 && percentielteller%percentielgrootte == 0) {
                serie.add(percentielteller/percentielgrootte, genormOmlooptijd/percentielgrootte);
                genormOmlooptijd=0;
                }
            percentielteller++;
        }
        dataset.addSeries(serie);
    }

    private JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Scheduling Algoritmes",
                "Bedieningstijd voor 10000 (percentiel)",
                "Genormaliseerde omlooptijd",
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

        NumberAxis yAxis = new LogarithmicAxis("Genormaliseerde omlooptijd");
        plot.setRangeAxis(yAxis);

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);



        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle("Genormaliseerde omlooptijd ifv bedieningstijd",
                        new Font("Serif", java.awt.Font.BOLD, 18))
        );
        return chart;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var graph = new GrafiekenBesturingssystemen();
            graph.setVisible(true);
        });
    }
}