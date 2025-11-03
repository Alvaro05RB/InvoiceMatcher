package Model;

import java.util.HashMap;

public class InvoiceBatch {
    private final HashMap<String,Invoice> invoices;
    public InvoiceBatch(){
        invoices = new HashMap<>();
    }

    public boolean addInvoice(Invoice invoice){
        return invoices.putIfAbsent(invoice.ID(), invoice) == null;
    }
    public Invoice getInvoiceById(String Id){
        return invoices.get(Id);
    }
}
