import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by tan on 11/1/16.
 */
public class Plotter {
    public static void plot(List<Sequence> sequences, int windowLength, Scale scale, double threshold) throws InvalidProteinSequences {
        XYChartBuilder builder = new XYChartBuilder();
        builder.yAxisTitle("Average Hydrophobicity");
        builder.xAxisTitle("Index Position");
        int min = windowLength / 2;
        List<XYChart> charts = new ArrayList<>();
        List<TrendList> trendsList = new ArrayList<>();
        InvalidProteinSequences throwable = new InvalidProteinSequences();
        for (Sequence sequence : sequences) {
            String proteinSequence = sequence.getSequence().toUpperCase();
            builder.title(sequence.getName());
            XYChart chart = builder.build();
            double[] xData = new double[proteinSequence.length() - (2 * min)];
            for (int i = 0, ii = min; i < xData.length; i++, ii++) { // range(min, sequence - min + 1)
                xData[i] = ii;
            }
            boolean error = false;
            double[] yData = new double[xData.length];
            for (int i = 0; i < yData.length; i++) {
                float sum = 0;
                error = false;
                for (int j = (int) (xData[i] - min); j <= xData[i] + min; j++) {
                    try {
                        sum += scale.getWeight(proteinSequence.charAt(j));
                    } catch (Throwable t) {
                        error = true;
                        throwable.add(sequence.getName());
                        break;
                    }
                }
                if (error) {
                    break;
                }
                yData[i] = sum / windowLength;
            }
            if (error) {
                continue;
            }
            chart.addSeries(sequence.getName(), xData, yData);
            trendsList.add(markTrends(chart, xData, yData, windowLength, threshold));

            // plot threshold line
            xData = new double[2];
            xData[0] = 0;
            xData[1] = proteinSequence.length();
            yData = new double[2];
            yData[0] = threshold;
            yData[1] = threshold;
            chart.addSeries("Threshold", xData, yData);
            charts.add(chart);
        }
        if (throwable.size() != 0) {
            throw throwable;
        }
        Chart.showChart(charts, trendsList);
    }

    private static TrendList markTrends(XYChart chart, double[] xData, double[] yData, int windowLength, double threshold) {
        TrendList trends = new TrendList();
        trends.name = chart.getTitle();
        int count = 0;
        int trendCount = 0;
        int a = 0;
        double max = 0;
        for (int i = 0; i < yData.length; i++) {
            if (yData[i] > max) { // find the max y value
                max = yData[i];
            }
        }
        max += 0.5d;
        double[] y = new double[] {max, max};
        for (int i = 0; i < yData.length; i++) {
            if (yData[i] > threshold) {
                if (count == 0) {
                    a = i;
                }
                count++;
            } else {
                if (count >= windowLength) {
                    double[] x = new double[] {xData[a], xData[i - 1]};
                    chart.addSeries("Trend #" + ++trendCount + " (" + ((int) xData[a] + 1) + " - " + ((int) xData[i - 1] + 1) + ")", x, y);
                    trends.list.add(new Trend(((int) xData[a] + 1), ((int) xData[i - 1] + 1)));
                }
                count = 0;
            }
        }
        if (count > 0) {
            if (count >= windowLength) {
                double[] x = new double[]{xData[a], xData[yData.length - 1]};
                chart.addSeries("Trend #" + ++trendCount + " (" + ((int) xData[a] + 1) + " - " + ((int) xData[yData.length - 1] + 1) + ")", x, y);
                trends.list.add(new Trend(((int) xData[a] + 1), ((int) xData[yData.length - 1] + 1)));
            }
        }
        return trends;
    }
}

class InvalidProteinSequences extends Throwable {
    private List<String> proteinSequences;
    public InvalidProteinSequences() {
        proteinSequences = new ArrayList<>();
    }

    public void add(String seq) {
        proteinSequences.add(seq);
    }

    public List<String> getList() {
        return proteinSequences;
    }

    public int size() {
        return proteinSequences.size();
    }
}