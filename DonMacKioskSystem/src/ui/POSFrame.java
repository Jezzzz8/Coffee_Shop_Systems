package ui;

public class POSFrame extends javax.swing.JFrame {

    public POSFrame() {
        initComponents();
        checkUserAccess();
    }
    
    private void checkUserAccess() {
        // This would normally get the current logged-in user from a session
        // For now, we'll simulate it by checking if user has cashier or admin role

        // In a real application, you would get the current user from a session manager
        String currentUser = "cashier"; // This should come from session management

        if (!backend.UserAuthentication.isAdmin(currentUser) && 
            !backend.UserAuthentication.isCashier(currentUser)) {

            javax.swing.JOptionPane.showMessageDialog(this, 
                "Access Denied! Only Administrators and Cashiers can access the POS system.", 
                "Permission Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);

            // Redirect to login or appropriate frame
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            loginFrame.pack();
            loginFrame.setLocationRelativeTo(null);
            this.dispose();
            return;
        }

        // Initialize POS system for cashier
        initializePOS();
    }
    
    private void initializePOS() {
        // Initialize POS components here
        TotalAmountTitleLabel.setText("Total Amount Due: ₱39 .00");

        // You can add more POS initialization code here
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        TotalAmountTitleLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        POSTitleLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        CostumersList = new javax.swing.JList<>();
        POSTitleLabel1 = new javax.swing.JLabel();
        LogoutButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(121, 63, 26));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel1.setForeground(new java.awt.Color(31, 40, 35));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 600));
        jPanel1.setRequestFocusEnabled(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(249, 241, 240));
        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel2.setForeground(new java.awt.Color(31, 40, 35));
        jPanel2.setPreferredSize(new java.awt.Dimension(500, 200));
        jPanel2.setRequestFocusEnabled(false);
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setBackground(new java.awt.Color(201, 177, 158));
        jTable1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                { new Integer(5), "Don Machattos",  new Integer(1),  new Float(39.0), "Drinks",  new Float(39.0)}
            },
            new String [] {
                "Item ID", "Item Name", "Quantity", "Price", "Category", "Sub Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.String.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setFillsViewportHeight(true);
        jTable1.setGridColor(new java.awt.Color(121, 63, 26));
        jTable1.setRowHeight(30);
        jTable1.setShowGrid(true);
        jTable1.getTableHeader().setResizingAllowed(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
        }

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 720, 320));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 50, 740, 340));

        jPanel4.setBackground(new java.awt.Color(201, 177, 158));
        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel4.setForeground(new java.awt.Color(31, 40, 35));
        jPanel4.setPreferredSize(new java.awt.Dimension(500, 200));
        jPanel4.setRequestFocusEnabled(false);
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 400, 490, 170));

        TotalAmountTitleLabel.setBackground(new java.awt.Color(249, 241, 240));
        TotalAmountTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TotalAmountTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        TotalAmountTitleLabel.setText("Total Amount Due: ₱0.00");
        TotalAmountTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TotalAmountTitleLabel.setPreferredSize(new java.awt.Dimension(480, 50));
        jPanel1.add(TotalAmountTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 370, 30));

        jPanel5.setBackground(new java.awt.Color(201, 177, 158));
        jPanel5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel5.setForeground(new java.awt.Color(31, 40, 35));
        jPanel5.setPreferredSize(new java.awt.Dimension(500, 200));
        jPanel5.setRequestFocusEnabled(false);
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 490, 170));

        jPanel3.setBackground(new java.awt.Color(201, 177, 158));
        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel3.setForeground(new java.awt.Color(31, 40, 35));
        jPanel3.setPreferredSize(new java.awt.Dimension(220, 380));
        jPanel3.setRequestFocusEnabled(false);
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        POSTitleLabel2.setBackground(new java.awt.Color(249, 241, 240));
        POSTitleLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        POSTitleLabel2.setForeground(new java.awt.Color(255, 255, 255));
        POSTitleLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        POSTitleLabel2.setText("ORDERS");
        POSTitleLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        POSTitleLabel2.setPreferredSize(new java.awt.Dimension(480, 50));
        jPanel3.add(POSTitleLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 190, 20));

        jPanel6.setBackground(new java.awt.Color(201, 177, 158));
        jPanel6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel6.setForeground(new java.awt.Color(31, 40, 35));
        jPanel6.setPreferredSize(new java.awt.Dimension(180, 600));
        jPanel6.setRequestFocusEnabled(false);
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CostumersList.setBackground(new java.awt.Color(249, 241, 240));
        CostumersList.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CostumersList.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        CostumersList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "CustomerOrderID1", "CustomerOrderID2" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        CostumersList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        CostumersList.setToolTipText("");
        CostumersList.setPreferredSize(new java.awt.Dimension(170, 90));
        CostumersList.setSelectionBackground(new java.awt.Color(173, 103, 48));
        jScrollPane3.setViewportView(CostumersList);

        jPanel6.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 190, 270));

        jPanel3.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 210, 290));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 230, 340));

        POSTitleLabel1.setBackground(new java.awt.Color(249, 241, 240));
        POSTitleLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        POSTitleLabel1.setForeground(new java.awt.Color(255, 255, 255));
        POSTitleLabel1.setText("POS SYSTEM");
        POSTitleLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        POSTitleLabel1.setPreferredSize(new java.awt.Dimension(480, 50));
        jPanel1.add(POSTitleLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 340, 30));

        LogoutButton.setBackground(new java.awt.Color(249, 241, 240));
        LogoutButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        LogoutButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/logout.png"))); // NOI18N
        LogoutButton.setText("LOGOUT");
        LogoutButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        LogoutButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LogoutButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        LogoutButton.setIconTextGap(10);
        LogoutButton.setPreferredSize(new java.awt.Dimension(200, 60));
        LogoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutButtonActionPerformed(evt);
            }
        });
        jPanel1.add(LogoutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 8, 110, 38));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void LogoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutButtonActionPerformed
        LoginFrame LoginFrame = new LoginFrame();
        LoginFrame.setVisible(true);
        LoginFrame.pack();
        LoginFrame.setLocationRelativeTo(null);
        backend.SessionManager.logout();
        this.dispose();
    }//GEN-LAST:event_LogoutButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> CostumersList;
    private javax.swing.JButton LogoutButton;
    private javax.swing.JLabel POSTitleLabel1;
    private javax.swing.JLabel POSTitleLabel2;
    private javax.swing.JLabel TotalAmountTitleLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
