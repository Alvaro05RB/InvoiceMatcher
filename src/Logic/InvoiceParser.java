package Logic;

import Model.InvoiceBatch;

import java.io.File;

public interface InvoiceParser {
    public InvoiceBatch read(File file);
}
