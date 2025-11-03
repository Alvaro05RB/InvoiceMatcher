package Logic;

import Model.InvoiceBatch;

import java.io.File;

public interface InvoiceParser {
    InvoiceBatch read(File file);
}
