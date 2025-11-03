package Presentation;

import Logic.BatchMatcher;
import Logic.InvoiceParser;
import Logic.InvoiceParserA;
import Logic.InvoiceParserB;
import Model.Invoice;
import Model.InvoiceBatch;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class App {
    private JFrame frame;
    private JPanel mainPanel;
    private JButton loadA,loadB,compareButton;
    private File fileA,fileB;
    private JTextArea resultAinB, resultBinA;
    private JLabel fileALabel, fileBLabel;

    public App() {
        frame = new JFrame("Invoice Matcher");
        mainPanel = new JPanel(new BorderLayout());

        // Top Buttons
        JPanel topPanel = new JPanel();
        loadA = new JButton("Load File A");
        loadB = new JButton("Load File B");
        compareButton = new JButton("Compare");
        fileALabel = new JLabel("No file selected");
        fileBLabel = new JLabel("No file selected");
        topPanel.add(loadA);
        topPanel.add(fileALabel);
        topPanel.add(loadB);
        topPanel.add(fileBLabel);
        topPanel.add(compareButton);

        loadA.addActionListener(e -> {
            fileA = loadFile(frame);
            if (fileA != null) {
                fileALabel.setText(fileA.getName());
            }
        });

        loadB.addActionListener(e -> {
            fileB = loadFile(frame);
            if (fileB!= null) {
                fileBLabel.setText(fileB.getName());
            }
        });

        compareButton.addActionListener(e -> {
            if (fileA != null && fileB != null) {
                try {
                    InvoiceParser parserA = new InvoiceParserA();
                    InvoiceParser parserB = new InvoiceParserB();

                    InvoiceBatch batchA = parserA.read(fileA);
                    InvoiceBatch batchB = parserB.read(fileB);

                    // Bidirectional match using BatchMatcher
                    InvoiceBatch AnotInB = BatchMatcher.match(batchA, batchB);
                    InvoiceBatch BnotInA = BatchMatcher.match(batchB, batchA);

                    // Display results in JTextAreas
                    resultAinB.setText("");
                    for (Invoice inv : AnotInB.getInvoices().values()) {
                        resultAinB.append(inv.ID() + " | " + inv.price() + "\n");
                    }

                    resultBinA.setText("");
                    for (Invoice inv : BnotInA.getInvoices().values()) {
                        resultBinA.append(inv.ID() + " | " + inv.price() + "\n");
                    }

                } catch (Exception ex) {
                    // Display error
                    JOptionPane.showMessageDialog(frame,
                            "Error parsing files:\n" + ex.getMessage(),
                            "Parsing Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(frame, "Please load both files first.");
            }
        });

        // Results UI
        resultAinB = new JTextArea(10, 30);
        resultAinB.setBorder(BorderFactory.createTitledBorder("Invoices in A not in B"));

        resultBinA = new JTextArea(10, 30);
        resultBinA.setBorder(BorderFactory.createTitledBorder("Invoices in B not in A"));

        JPanel resultsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        resultsPanel.add(new JScrollPane(resultAinB),BorderLayout.EAST);
        resultsPanel.add(new JScrollPane(resultBinA),BorderLayout.WEST);

        // Merging both panels
        mainPanel.add(topPanel,BorderLayout.NORTH);
        mainPanel.add(resultsPanel,BorderLayout.CENTER);
        frame.add(mainPanel);

        //Window settings
        frame.setSize(1400, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    private File loadFile(JFrame frame) {
        String userHome = System.getProperty("user.home");
        File downloads = new File(userHome, "Downloads");
        JFileChooser fileChooser = new JFileChooser(downloads);
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    public static void main(String[] args) {
        new App();
    }

}
