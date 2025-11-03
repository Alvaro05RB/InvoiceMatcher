package Presentation;

import Logic.*;
import Model.Invoice;
import Model.InvoiceBatch;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;

public class App {
    private final JFrame frame;
    private File fileA, fileB;
    private JTextArea resultAinB, resultBinA;
    private final JLabel fileALabel;
    private final JLabel fileBLabel;

    // Formatter for clean currency
    private static final NumberFormat EUR = NumberFormat.getInstance(Locale.GERMANY);
    static { EUR.setMinimumFractionDigits(2); EUR.setMaximumFractionDigits(2); }

    public App() {
        frame = new JFrame("Invoice Matcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 800);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // === TOP PANEL ===
        JPanel topPanel = new JPanel();
        JButton loadA = new JButton("Load File A");
        JButton loadB = new JButton("Load File B");
        JButton compareButton = new JButton("Compare");
        fileALabel = new JLabel("No file A");
        fileBLabel = new JLabel("No file B");

        topPanel.add(loadA); topPanel.add(fileALabel);
        topPanel.add(loadB); topPanel.add(fileBLabel);
        topPanel.add(compareButton);

        // === RESULTS PANELS ===
        resultAinB = new JTextArea(20, 40);
        resultAinB.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultAinB.setEditable(false);
        resultAinB.setBorder(BorderFactory.createTitledBorder("Invoices in A not in B"));

        resultBinA = new JTextArea(20, 40);
        resultBinA.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultBinA.setEditable(false);
        resultBinA.setBorder(BorderFactory.createTitledBorder("Invoices in B not in A"));

        JPanel resultsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        resultsPanel.add(new JScrollPane(resultAinB));
        resultsPanel.add(new JScrollPane(resultBinA));

        // === LAYOUT ===
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(resultsPanel, BorderLayout.CENTER);
        frame.add(mainPanel);

        // === ACTIONS ===
        loadA.addActionListener(e -> fileA = loadFile("Select File A"));
        loadB.addActionListener(e -> fileB = loadFile("Select File B"));

        compareButton.addActionListener(e -> compareFiles());

        frame.setVisible(true);
    }

    private File loadFile(String title) {
        JFileChooser fc = new JFileChooser(System.getProperty("user.home") + "/Downloads");
        fc.setDialogTitle(title);
        if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            if (title.contains("A")) fileALabel.setText(f.getName());
            else fileBLabel.setText(f.getName());
            return f;
        }
        return null;
    }

    private void compareFiles() {
        if (fileA == null || fileB == null) {
            JOptionPane.showMessageDialog(frame, "Load both files first!");
            return;
        }

        try {
            InvoiceParser parserA = new InvoiceParserA();
            InvoiceParser parserB = new InvoiceParserB();

            InvoiceBatch batchA = parserA.read(fileA);
            InvoiceBatch batchB = parserB.read(fileB);

            InvoiceBatch AnotInB = BatchMatcher.match(batchA, batchB);
            InvoiceBatch BnotInA = BatchMatcher.match(batchB, batchA);

            // === DISPLAY A not in B ===
            resultAinB.setText("");
            resultAinB.append(String.format("%-20s %-12s %-12s\n", "ID", "PRICE", "COMMISSION"));
            resultAinB.append("-".repeat(50) + "\n");
            for (Invoice inv : AnotInB.getInvoices().values()) {
                resultAinB.append(String.format("%-20s €%-11s €%-11s\n",
                        inv.ID(),
                        EUR.format(inv.price()),
                        EUR.format(inv.commission())));
            }

            // === DISPLAY B not in A ===
            resultBinA.setText("");
            resultBinA.append(String.format("%-20s %-12s %-12s\n", "ID", "PRICE", "COMMISSION"));
            resultBinA.append("-".repeat(50) + "\n");
            for (Invoice inv : BnotInA.getInvoices().values()) {
                resultBinA.append(String.format("%-20s €%-11s €%-11s\n",
                        inv.ID(),
                        EUR.format(inv.price()),
                        EUR.format(inv.commission())));
            }

            // Optional: Show totals
            JOptionPane.showMessageDialog(frame,
                    "Comparison Complete!\n" +
                            "A → B: " + AnotInB.size() + " missing\n" +
                            "B → A: " + BnotInA.size() + " missing",
                    "Done", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame,
                    "Error: " + ex.getMessage(),
                    "Parse Failed", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }
}