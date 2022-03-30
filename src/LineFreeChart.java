import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;


public class LineFreeChart extends ApplicationFrame {
    int t[]=new int[50];
    int p[]=new int[50];
    int c=0;
    int i=0;

    public LineFreeChart(final String title, int a[],int b[],int counter) {
        super(title);
        final CategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 500));
        setContentPane(chartPanel);

        t=b;
        p=a;
        c=counter;
    }


    private CategoryDataset createDataset() {

        // row keys...

        final String series1 = "Span";


        // column keys...

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String[] st = new String[c];
        for(int k = 0; k<=c; k++)        //copy values of integer t[ ] into a string array. and then pass it to dataset.addValue()
        {

            st[k]=Integer.toString(t[k]);

        }
        for(int k=0;k<=c;k++)
        {
            dataset.addValue(p[k],series1, st[k]);

        }

        return dataset;

    }

    private JFreeChart createChart(final CategoryDataset dataset) {

        // create the chart...
        final JFreeChart chart = ChartFactory.createLineChart(
                "Graph debicting the delay and etc etc",       // chart title
                "no of packets dropped",                    // domain axis label
                "packetLoss",                   // range axis label
                dataset,                   // data
                PlotOrientation.VERTICAL,  // orientation
                true,                      // include legend
                true,                      // tooltips
                false                      // urls
        );


        chart.setBackgroundPaint(Color.white);

        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);


        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);

        return chart;
    }
}