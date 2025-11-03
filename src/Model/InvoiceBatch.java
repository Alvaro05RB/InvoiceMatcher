package Model;

import java.util.HashMap;

public class InvoiceBatch {
    private final HashMap<String,Invoice> invoices;
    public InvoiceBatch(){
        invoices = new HashMap<>();
    }

    public void addInvoice(Invoice invoice){
        invoices.putIfAbsent(invoice.ID(), invoice);
    }
    public Invoice getInvoiceById(String Id){
        return invoices.get(Id);
    }
    public HashMap<String, Invoice> getInvoices() {
        return invoices;
    }
}
