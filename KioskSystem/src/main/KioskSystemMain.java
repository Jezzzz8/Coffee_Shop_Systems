package main;

import gui.*;

public class KioskSystemMain {

    public static void main(String[] args) {
//        KioskFrame kiosk = new KioskFrame();
//        kiosk.setVisible(true);
//        kiosk.pack();
//        kiosk.setLocationRelativeTo(null);
        
//        CRUDSystemFrame crud = new CRUDSystemFrame();
//        crud.setVisible(true);
//        crud.pack();
//        crud.setLocationRelativeTo(null);
        
        LoginFrame login = new LoginFrame();
        login.setVisible(true);
        login.pack();
        login.setLocationRelativeTo(null);
    }
}