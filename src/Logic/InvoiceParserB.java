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

public class InvoiceParserB implements InvoiceParser {

    // German locale: handles 1.607,26 → 1607.26
    private static final NumberFormat ES_FORMAT = NumberFormat.getInstance(Locale.GERMANY);

    @Override
    public InvoiceBatch read(File csvFile) {
        InvoiceBatch batch = new InvoiceBatch();

        try (InputStream is = new FileInputStream(csvFile);
             InputStreamReader isr = new InputStreamReader(is, "UTF-8");
             BufferedReader br = new BufferedReader(isr)) {

            // Skip BOM
            br.mark(1);
            if (br.read() != 0xFEFF) {
                br.reset();
            }

            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(',')  // COMMA SEPARATOR
                    .withQuoteChar('"')
                    .withIgnoreQuotations(false)
                    .build();

            CSVReader reader = new CSVReaderBuilder(br)
                    .withCSVParser(parser)
                    .withSkipLines(1)
                    .build();

            String[] columns;
            int rowNum = 1;
            int totalRows = 0;

            while ((columns = reader.readNext()) != null) {
                rowNum++;
                totalRows++;

                if (columns.length < 14) continue;

                try {
                    String id = columns[0].trim();
                    String priceRaw = columns[12].trim();
                    String commRaw = columns[13].trim();

                    double price = parseEsNumber(priceRaw);
                    double commission = parseEsNumber(commRaw);

                    batch.addInvoice(new Invoice(id, price, commission));
                } catch (Exception ignored) {}
            }

            System.out.println("InvoiceParserB: Total rows loaded: " + totalRows);

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