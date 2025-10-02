package services;

import objects.*;

public interface PrintService {
    void displayOrderSlip(OrderSlip orderSlip);
    void displayReceipt(Receipt receipt);
}