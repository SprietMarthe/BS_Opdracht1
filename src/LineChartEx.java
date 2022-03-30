import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
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

public class LineChartEx extends JFrame {

    public LineChartEx() {
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
        setTitle("Scheduling Algoritmes");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private XYDataset createDataset() {
        Main.aantalProcessen=5;//TODO
        Main.initialiseerArrays();
        ReadXMLFile.readingXMLFile(Main.aantalProcessen, Main.pid, Main.aankomsttijd, Main.bedieningstijd);

        Main.berekenFCFS();

        var fcfs = new XYSeries("FCFS");//TODO
        for (int i=0; i<Main.aantalProcessen; i++){
            fcfs.add(Main.genormaliseerdeOmlooptijd[i], Main.bedieningstijd[i]);//TODO
        }
        var dataset = new XYSeriesCollection();
        dataset.addSeries(fcfs);

        return dataset;
    }

    private JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Scheduling Algoritmes",
                "Bedieningstijd 10000 processen",
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
            var fcfs = new LineChartEx();
            fcfs.setVisible(true);
        });
    }
}