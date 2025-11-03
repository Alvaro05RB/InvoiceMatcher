package Logic;

import Model.InvoiceBatch;

import java.io.File;

public class InvoiceParserB implements InvoiceParser{
    @Override
    public InvoiceBatch read(File file) {
        InvoiceBatch result = new InvoiceBatch();
        return result;
    }
}
