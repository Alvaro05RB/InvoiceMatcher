package Logic;

import Model.InvoiceBatch;

import java.io.File;

public class InvoiceParserA implements InvoiceParser{
    @Override
    public InvoiceBatch read(File file) {
        InvoiceBatch result = new InvoiceBatch();
        return result;
    }
}
