package Logic;

import Model.Invoice;
import Model.InvoiceBatch;

import java.util.Map;

/**
 * Utility class for comparing two InvoiceBatch instances.
 * Provides methods to identify missing invoices between batches.
 */
public class BatchMatcher {
    /**
     * Returns all invoices present in batch A but missing in batch B.
     */
    public static InvoiceBatch match (InvoiceBatch a, InvoiceBatch b){
        InvoiceBatch result = new InvoiceBatch();
        for (Map.Entry<String, Invoice> entry : a.getInvoices().entrySet()) {
            String id = entry.getKey();
            Invoice invoiceA = entry.getValue();
            if (b.getInvoiceById(id) == null) {
                result.addInvoice(invoiceA);
            }
        }
        return result;
    }
}
