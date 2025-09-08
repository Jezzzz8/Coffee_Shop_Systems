package kiosksystem;

import backend.*;
import ui.*;

public class DonMacKioskSystem {
    
    public static void main(String[] args) {
        QueueManager.initializeQueue();
        
        KioskFrame KioskFrame = new KioskFrame();
        KioskFrame.setVisible(true);
        KioskFrame.pack();
        KioskFrame.setLocationRelativeTo(null);
    }
}
