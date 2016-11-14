import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * Created by tan on 10/24/16.
 */
public class MainWindow {
    private JPanel mainPanel;
    private JButton uploadFASTAButton;
    private JTextArea inputTextArea;
    private JComboBox hydrophobicityComboBox;
    private JButton plotButton;
    private JButton resetToDefaultButton;
    private JButton clearInputButton;
    private JComboBox slidingWindowComboBox;
    private JSpinner averageThresholdSpinner;

    public MainWindow() {
        uploadFASTAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();
                int returnCode = fileChooser.showOpenDialog(mainPanel);
                if (returnCode == JFileChooser.APPROVE_OPTION) {
                    File fastaFile = fileChooser.getSelectedFile();
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(fastaFile));
                        String line;
                        inputTextArea.setText("");
                        while ((line = reader.readLine()) != null) {
                            inputTextArea.append(line);
                            inputTextArea.append("\n");
                        }
                    } catch (java.io.IOException e) {
                        Error.showError(e.getMessage());
                    }
                }
            }
        });
        clearInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                inputTextArea.setText("");
            }
        });
        resetToDefaultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                slidingWindowComboBox.setSelectedItem(19);
                hydrophobicityComboBox.setSelectedIndex(0);
                averageThresholdSpinner.setValue(1d);
            }
        });
        plotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                List<Sequence> sequences = parseProtein();
                int windowLength = (int) slidingWindowComboBox.getSelectedItem();
                Scale scale = (Scale) hydrophobicityComboBox.getSelectedItem();
                double threshold = (double) averageThresholdSpinner.getValue();
                try {
                    Plotter.plot(sequences, windowLength, scale, threshold);
                } catch (InvalidProteinSequences throwable) {
                    String message = "";
                    List<String> invalid = throwable.getList();
                    for (String m : invalid) {
                        message += m + " is not a valid protein sequence.\n";
                        for (int i = 0; i < sequences.size(); i++) {
                            Sequence s = sequences.get(i);
                            if (m == s.getName()) {
                                sequences.remove(i);
                                break;
                            }
                        }
                    }
                    Error.showError(message);
                    try {
                        if (sequences.size() > 0) {
                            Plotter.plot(sequences, windowLength, scale, threshold);
                        }
                    } catch (InvalidProteinSequences e) {

                    }
                }
            }
        });
    }

    private List<Sequence> parseProtein() {
        String in = inputTextArea.getText();
        String[] split = in.split("\n");
        List<Sequence> sequences = new ArrayList<>();
        int i = -1;
        for (String line : split) {
            if (line.length() == 0) {
                continue;
            }
            if (line.startsWith(">")) {
                sequences.add(new Sequence(line.substring(1)));
                i++;
                continue;
            }
            Sequence sequence = sequences.get(i);
            sequence.appendSequence(line);
        }
        return sequences;
    }

    public void show() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        Vector<Integer> windowChoices = new Vector<>();
        for (int i = 3; i <= 51; i += 2) {
            windowChoices.add(i);
        }
        slidingWindowComboBox = new JComboBox(windowChoices);
        slidingWindowComboBox.setSelectedItem(19);

        Vector<Scale> hydrophobicityChoices = new Vector<>();
        String[] scales = new String[] {
                "Kyte-Doolittle", "Hopp-Woods", "Cornette", "Eisenberg",
                "Rose", "Janin", "Engelman GES"
        };
        for (String scale : scales) {
            hydrophobicityChoices.add(new Scale(scale));
        }
        hydrophobicityComboBox = new JComboBox(hydrophobicityChoices);

        averageThresholdSpinner = new JSpinner();
        averageThresholdSpinner.setModel(new SpinnerNumberModel(1d, 0.5d, 2.5d, 0.1d));
    }
}
