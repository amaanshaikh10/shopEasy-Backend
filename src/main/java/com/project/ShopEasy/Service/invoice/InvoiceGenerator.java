package com.project.ShopEasy.Service.invoice;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.project.ShopEasy.Model.InvoiceItem;
import com.project.ShopEasy.Model.OrderItem;
import com.project.ShopEasy.Service.email.EmailService;

@Component
public class InvoiceGenerator {

	@Autowired
	EmailService emailService;

	public static String generateInvoice(String custName, String email, List<InvoiceItem> items) {
		String filePath = "invoice_" + System.currentTimeMillis() + ".pdf";

		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(filePath));
			document.open();

			document.add(new Paragraph("Invoice for: " + custName));
			document.add(new Paragraph("Email: " + email));
			document.add(new Paragraph("Date: " + java.time.LocalDate.now() + "\n\n"));

			PdfPTable table = new PdfPTable(3);
			table.addCell(getCell("Item Name", PdfPCell.ALIGN_CENTER, Font.BOLD));
			table.addCell(getCell("Quantity", PdfPCell.ALIGN_CENTER, Font.BOLD));
			table.addCell(getCell("Price", PdfPCell.ALIGN_CENTER, Font.BOLD));

			BigDecimal total = BigDecimal.ZERO;

			for (InvoiceItem item : items) {
				table.addCell(getCell(item.getName(), PdfPCell.ALIGN_LEFT, Font.NORMAL));
				table.addCell(getCell(String.valueOf(item.getQuantity()), PdfPCell.ALIGN_CENTER, Font.NORMAL));
				table.addCell(getCell(String.valueOf(item.getPrice()), PdfPCell.ALIGN_RIGHT, Font.NORMAL));
				total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
			}

			document.add(table);

			document.add(new Paragraph("\nTotal: ₹" + total));
			document.close();
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}

		return filePath;
	}

	private static PdfPCell getCell(String text, int alignment, int fontStyle) {
		Font font = new Font(Font.FontFamily.HELVETICA, 12, fontStyle);
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setPadding(5);
		cell.setHorizontalAlignment(alignment);
		return cell;
	}

	public void processOrder(String name, String email, List<OrderItem> items) {
		List<InvoiceItem> invoiceItems = new ArrayList<>();

		for (OrderItem item : items) {
			invoiceItems.add(new InvoiceItem(item.getProduct().getName(), item.getQuantity(), item.getPrice()));
		}

		String pdfPath = InvoiceGenerator.generateInvoice(name, email, invoiceItems);
		emailService.sendInvoiceEmail(email, "Order Invoice", "Thank you for your order! ", pdfPath);
	}

}
