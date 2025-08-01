package com.example.pos_system.service;

import com.example.pos_system.model.Invoice;
import com.example.pos_system.model.Order;
import com.example.pos_system.model.OrderItem;
import com.example.pos_system.repository.InvoiceRepository;
import com.example.pos_system.repository.OrderRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;

    public void generateInvoice(Order order, double cashReceived, double changeReturned) throws Exception {
        Document doc = new Document();

        String folderPath = "invoice/";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fileName = "invoice_" + order.getId() + ".pdf";
        String filePath = folderPath + fileName;

        PdfWriter.getInstance(doc, new FileOutputStream(filePath));
        doc.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Paragraph title = new Paragraph("Mini Mart", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);
        doc.add(new Paragraph("Invoice", titleFont));
        doc.add(new Paragraph(" "));

        doc.add(new Paragraph("Invoice ID: " + order.getId(), bodyFont));
        doc.add(new Paragraph("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), bodyFont));
        doc.add(new Paragraph("Customer: " + order.getCustomerName(), bodyFont));
        doc.add(new Paragraph("Seller: " + order.getCreatedBy().getUsername(), bodyFont));
        doc.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 1, 1, 1});

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        String[] headers = {"Product Name", "Quantity", "Price", "Subtotal"};
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(headerCell);
        }

        double total = 0;
        for (OrderItem item : order.getOrderItems()) {
            String productName = item.getProduct().getName();
            int quantity = item.getQuantity();
            double price = item.getPrice();
            double subtotal = price * quantity;
            total += subtotal;

            table.addCell(productName);
            table.addCell(String.valueOf(quantity));
            table.addCell(String.format("$%.2f", price));
            table.addCell(String.format("$%.2f", subtotal));
        }

        doc.add(table);
        doc.add(new Paragraph(" "));

        doc.add(new Paragraph("Subtotal: $" + String.format("%.2f", total), bodyFont));
        doc.add(new Paragraph("Total: $" + String.format("%.2f", total), titleFont));
        doc.add(new Paragraph(" "));

        doc.add(new Paragraph("Cash received: $" + String.format("%.2f", cashReceived), bodyFont));
        doc.add(new Paragraph("Change returned: $" + String.format("%.2f", changeReturned), bodyFont));

        doc.close();

        Invoice invoice = Invoice.builder()
                .order(order)
                .filePath(fileName) // just file name, keep it relative to "invoice/" folder
                .customerName(order.getCustomerName())
                .sellerName(order.getCreatedBy().getUsername())
                .totalAmount(total)
                .cashReceived(cashReceived)
                .changeReturned(changeReturned)
                .createdAt(LocalDateTime.now())
                .createdBy(order.getCreatedBy())
                .build();

        invoiceRepository.save(invoice);
    }

    public void generateInvoicePdf(Long orderId, double cashReceived, double changeReturned) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + orderId));
        try {
            generateInvoice(order, cashReceived, changeReturned);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate invoice for order " + orderId, e);
        }
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    public Invoice getInvoiceByOrderId(Long orderId) {
        return invoiceRepository.findByOrderId(orderId).orElse(null);
    }
    public double getTotalAmount() {
        Double total = invoiceRepository.getTotalRevenue();
        return total != null ? total : 0.0;
    }
}
