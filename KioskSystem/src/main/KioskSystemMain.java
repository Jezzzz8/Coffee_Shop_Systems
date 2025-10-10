package main;

import services.QueueManager;

import gui.KioskFrame;

public class KioskSystemMain {

    public static void main(String[] args) {
        System.out.println(QueueManager.peekNextOrder());
        
        KioskFrame kiosk = new KioskFrame();
        kiosk.setVisible(true);
        kiosk.pack();
        kiosk.setLocationRelativeTo(null);
    }
}
