package services;

import gui.PrintPreviewDialog;
import objects.*;

public class ConsolePrintService implements PrintService {
    
    public void printOrderSlip(OrderSlip orderSlip) {
        
        System.out.println("=== ORDER SLIP CONTENT ===");
        System.out.println(orderSlip.formatOrderSlip());
        System.out.println("=== END OF ORDER SLIP ===");
    }
    
    public void printReceipt(Receipt receipt) {
        
        System.out.println("=== RECEIPT CONTENT ===");
        System.out.println(receipt.formatReceipt());
        System.out.println("=== END OF RECEIPT ===");
    }
    
    @Override
    public void displayOrderSlip(OrderSlip orderSlip) {
        String content = orderSlip.formatOrderSlip();
        PrintPreviewDialog.showDialog(null, "Order Slip - Order #" + orderSlip.getOrderId(), content);
    }
    
    @Override
    public void displayReceipt(Receipt receipt) {
        String content = receipt.formatReceipt();
        PrintPreviewDialog.showDialog(null, "Receipt - Order #" + receipt.getOrderId(), content);
    }
}