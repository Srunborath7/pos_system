package com.example.pos_system.service;

import com.example.pos_system.model.Invoice;
import com.example.pos_system.model.Order;
import com.example.pos_system.model.OrderItem;
import com.example.pos_system.repository.InvoiceRepository;
import com.example.pos_system.repository.OrderRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private OrderRepository orderRepository;
    public void generateInvoice(Order order) throws Exception {
        Document doc = new Document();

        String folderPath = "invoice/";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();  // create folder if it doesn't exist
        }

        String fileName = "invoice_" + order.getId() + ".pdf";
        String filePath = folderPath + fileName;

        PdfWriter.getInstance(doc, new FileOutputStream(filePath));

        doc.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        // Title
        doc.add(new Paragraph("Invoice", titleFont));
        doc.add(new Paragraph(" "));

        // Invoice Info
        doc.add(new Paragraph("Invoice ID: " + order.getId(), bodyFont));
        doc.add(new Paragraph("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), bodyFont));
        doc.add(new Paragraph("Customer: " + order.getCustomerName(), bodyFont));
        doc.add(new Paragraph("Seller: " + order.getCreatedBy().getUsername(), bodyFont));
        doc.add(new Paragraph(" "));

        // Table Headers
        PdfPTable table = new PdfPTable(4); // Product, Qty, Price, Subtotal
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 1, 1, 1});

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        String[] headers = {"Product Name", "Quantity", "Price", "Subtotal"};
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(headerCell);
        }

        // Order Items
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

        // Subtotal and Total
        doc.add(new Paragraph("Subtotal: $" + String.format("%.2f", total), bodyFont));
        doc.add(new Paragraph("Total: $" + String.format("%.2f", total), titleFont));

        doc.close();

        // Save to DB
        Invoice invoice = Invoice.builder()
                .order(order)
                .filePath(filePath)
                .customerName(order.getCustomerName())
                .sellerName(order.getCreatedBy().getUsername())
                .totalAmount(total)
                .createdAt(LocalDateTime.now())
                .createdBy(order.getCreatedBy())
                .build();

        invoiceRepository.save(invoice);
    }

    public void generateInvoicePdf(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + orderId));
        try {
            generateInvoice(order);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate invoice for order " + orderId, e);
        }
    }

}
