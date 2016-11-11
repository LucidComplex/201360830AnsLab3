import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Chart extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JScrollPane chartScrollPane;
    private JButton saveAsJPEGButton;
    private JButton exportButton;
    private JPanel panel;
    private List<TrendList> trends;

    public Chart() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        saveAsJPEGButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ImageSaver.saveComponentToImage(chartScrollPane.getViewport().getComponent(0));
            }
        });
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fc = new JFileChooser() {
                    @Override
                    public void approveSelection() {
                        File f = getSelectedFile();
                        if (f.exists() && getDialogType() == SAVE_DIALOG) {
                            int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file", JOptionPane.YES_NO_CANCEL_OPTION);
                            switch (result) {
                                case JOptionPane.YES_OPTION:
                                    super.approveSelection();
                                    return;
                                case JOptionPane.CANCEL_OPTION:
                                    cancelSelection();
                                case JOptionPane.NO_OPTION:
                                case JOptionPane.CLOSED_OPTION:
                                    return;
                            }
                        }
                        super.approveSelection();
                    }
                };
                fc.showSaveDialog(null);
                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fc.getSelectedFile()));
                    for (TrendList list : trends) {
                        bufferedWriter.write(list.name + "\t");
                        bufferedWriter.write(String.valueOf(list.list.size()));
                        for (Trend trend : list.list) {
                            bufferedWriter.write("\t" + trend.start + "-" + trend.end);
                        }
                        bufferedWriter.write("\n");
                    }
                    bufferedWriter.close();
                } catch (IOException e) {
                    Error.showError(e.getMessage());
                }
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void showChart(List<XYChart> charts, List<TrendList> trendsList) {
        Chart dialog = new Chart();
        dialog.trends = trendsList;
        Container container = new Container();
        container.setLayout(new GridLayout(charts.size(), 1));
        for (XYChart chart : charts) {
            container.add(new XChartPanel<XYChart>(chart));
        }
        dialog.chartScrollPane.getViewport().add(container);
        dialog.pack();
        dialog.setVisible(true);
    }
}
