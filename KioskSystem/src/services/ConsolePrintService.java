package services;

import gui.PrintPreviewDialog;
import objects.*;

public class ConsolePrintService implements PrintService {
    
    public void printOrderSlip(OrderSlip orderSlip) {
        
        System.out.println("=== ORDER SLIP CONTENT ===");
        System.out.println(orderSlip.formatOrderSlip());
        System.out.println("=== END OF ORDER SLIP ===");
    }
    
    @Override
    public void displayOrderSlip(OrderSlip orderSlip) {
        String content = orderSlip.formatOrderSlip();
        PrintPreviewDialog.showDialog(null, "Order Slip - Order #" + orderSlip.getOrderId(), content);
    }
}