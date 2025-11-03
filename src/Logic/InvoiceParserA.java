package Logic;

import Model.Invoice;
import Model.InvoiceBatch;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class InvoiceParserA implements InvoiceParser{
    private static final NumberFormat ES_FORMAT = NumberFormat.getInstance(Locale.GERMANY);
    @Override
    public InvoiceBatch read(File csvFile) {
        InvoiceBatch batch = new InvoiceBatch();

        try (InputStream is = new FileInputStream(csvFile);
             InputStreamReader isr = new InputStreamReader(is, "UTF-8");
             BufferedReader br = new BufferedReader(isr)) {

            // Skip BOM (fixes hidden char issues)
            br.mark(1);
            if (br.read() != 0xFEFF) {
                br.reset();
            }

            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .withQuoteChar('"')
                    .withIgnoreQuotations(false)     // Handles multiline cells
                    .withEscapeChar('\\')            // Escapes quotes inside fields
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            CSVReader reader = new CSVReaderBuilder(br)
                    .withCSVParser(parser)
                    .withSkipLines(2)                // Skip 2 header rows
                    .withMultilineLimit(10000)       // Allow huge multiline cells
                    .withKeepCarriageReturn(true)    // Preserve \r\n inside quotes
                    .build();

            String[] columns;
            int rowNum = 2;
            int totalRows = 0;

            while ((columns = reader.readNext()) != null) {
                rowNum++;
                totalRows++;

                if (columns.length < 37) {
                    continue; // Skip malformed
                }

                try {
                    String id = columns[3].trim();           // Column D
                    String priceRaw = columns[34].trim();    // AI = Price
                    String commRaw = columns[36].trim();     // AK = Commission

                    double price = parseEsNumber(priceRaw);
                    double commission = parseEsNumber(commRaw);

                    batch.addInvoice(new Invoice(id, price, commission));
                } catch (Exception ignored) {}
            }

            System.out.println("InvoiceParserA: Total rows read: " + totalRows);

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return batch;
    }

    private double parseEsNumber(String s) throws ParseException {
        if (s == null || s.isEmpty() || s.equals("-")) return 0.0;
        s = s.replace("\"", "").trim();
        return ES_FORMAT.parse(s).doubleValue();
    }
}