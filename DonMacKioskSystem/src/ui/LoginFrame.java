package ui;

public class LoginFrame extends javax.swing.JFrame {

    public LoginFrame() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollBar1 = new javax.swing.JScrollBar();
        LoginPanel = new javax.swing.JPanel();
        LEFT = new javax.swing.JPanel();
        LogoLabel = new javax.swing.JLabel();
        RIGHT = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        UsernameLabel = new javax.swing.JLabel();
        UsernameText = new javax.swing.JTextField();
        PasswordLabel = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        LoginButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setBackground(new java.awt.Color(204, 204, 204));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setSize(new java.awt.Dimension(800, 500));

        LoginPanel.setBackground(new java.awt.Color(255, 255, 255));
        LoginPanel.setPreferredSize(new java.awt.Dimension(800, 500));
        LoginPanel.setLayout(null);

        LEFT.setBackground(new java.awt.Color(0, 0, 0));

        LogoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/logos/logo1.png"))); // NOI18N
        LogoLabel.setText("jLabel2");
        LogoLabel.setMinimumSize(new java.awt.Dimension(2089, 2048));
        LogoLabel.setPreferredSize(new java.awt.Dimension(400, 400));

        javax.swing.GroupLayout LEFTLayout = new javax.swing.GroupLayout(LEFT);
        LEFT.setLayout(LEFTLayout);
        LEFTLayout.setHorizontalGroup(
            LEFTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LEFTLayout.createSequentialGroup()
                .addComponent(LogoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        LEFTLayout.setVerticalGroup(
            LEFTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LEFTLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(LogoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        LoginPanel.add(LEFT);
        LEFT.setBounds(0, 0, 400, 500);

        RIGHT.setBackground(new java.awt.Color(249, 241, 240));
        RIGHT.setPreferredSize(new java.awt.Dimension(400, 500));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("LOGIN");

        UsernameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        UsernameLabel.setText("Username");

        UsernameText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        UsernameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsernameTextActionPerformed(evt);
            }
        });

        PasswordLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        PasswordLabel.setText("Password");

        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        LoginButton.setBackground(new java.awt.Color(0, 0, 0));
        LoginButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        LoginButton.setForeground(new java.awt.Color(255, 255, 255));
        LoginButton.setText("Login");
        LoginButton.setPreferredSize(new java.awt.Dimension(91, 35));
        LoginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout RIGHTLayout = new javax.swing.GroupLayout(RIGHT);
        RIGHT.setLayout(RIGHTLayout);
        RIGHTLayout.setHorizontalGroup(
            RIGHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RIGHTLayout.createSequentialGroup()
                .addGroup(RIGHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(RIGHTLayout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(jLabel1))
                    .addGroup(RIGHTLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(RIGHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(UsernameLabel)
                            .addComponent(UsernameText, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                            .addComponent(PasswordLabel)
                            .addComponent(jPasswordField1)
                            .addComponent(LoginButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        RIGHTLayout.setVerticalGroup(
            RIGHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RIGHTLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addGap(50, 50, 50)
                .addComponent(UsernameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UsernameText, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PasswordLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(LoginButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(171, Short.MAX_VALUE))
        );

        LoginPanel.add(RIGHT);
        RIGHT.setBounds(400, 0, 400, 500);

        getContentPane().add(LoginPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void UsernameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UsernameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UsernameTextActionPerformed

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField1ActionPerformed

    private void LoginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoginButtonActionPerformed

        String username = UsernameText.getText();
        String password = new String(jPasswordField1.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // UPDATED: Use authenticate() method instead of login()
        if (backend.UserAuthentication.authenticate(username, password)) {
            // Session is now set automatically in the authenticate() method
            // No need to call SessionManager.login() separately

            String fullName = backend.UserAuthentication.getUserFullName(username);
            String role = backend.UserAuthentication.getUserRole(username);

            String welcomeMessage = "Welcome, " + fullName + "!\n";
            welcomeMessage += "Role: " + role + "\n\n";

            // Role-specific redirection
            if (backend.UserAuthentication.isAdmin(username)) {
                welcomeMessage += "You have full system access privileges.";
                javax.swing.JOptionPane.showMessageDialog(this, welcomeMessage, "Login Successful", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                InventoryFrame InventoryFrame = new InventoryFrame();
                InventoryFrame.setVisible(true);
                InventoryFrame.pack();
                InventoryFrame.setLocationRelativeTo(null);
                this.dispose();

            } else if (backend.UserAuthentication.isManager(username)) {
                welcomeMessage += "You can manage inventory and products.";
                javax.swing.JOptionPane.showMessageDialog(this, welcomeMessage, "Login Successful", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                InventoryFrame InventoryFrame = new InventoryFrame();
                InventoryFrame.setVisible(true);
                InventoryFrame.pack();
                InventoryFrame.setLocationRelativeTo(null);
                this.dispose();

            } else if (backend.UserAuthentication.isCashier(username)) {
                welcomeMessage += "You can process sales and transactions.";
                javax.swing.JOptionPane.showMessageDialog(this, welcomeMessage, "Login Successful", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                POSFrame POSFrame = new POSFrame();
                POSFrame.setVisible(true);
                POSFrame.pack();
                POSFrame.setLocationRelativeTo(null);
                this.dispose();

            } else if (backend.UserAuthentication.isClerk(username)) {
                // ADDED: Clerk role handling (if needed)
                welcomeMessage += "You have clerk access privileges.";
                javax.swing.JOptionPane.showMessageDialog(this, welcomeMessage, "Login Successful", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                InventoryFrame InventoryFrame = new InventoryFrame();
                InventoryFrame.setVisible(true);
                InventoryFrame.pack();
                InventoryFrame.setLocationRelativeTo(null);
                this.dispose();

            } else {
                // Default users (customers/regular users) go to Kiosk
                welcomeMessage += "You have basic user access.";
                javax.swing.JOptionPane.showMessageDialog(this, welcomeMessage, "Login Successful", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                KioskFrame KioskFrame = new KioskFrame();
                KioskFrame.setVisible(true);
                KioskFrame.pack();
                KioskFrame.setLocationRelativeTo(null);
                this.dispose();
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_LoginButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel LEFT;
    private javax.swing.JButton LoginButton;
    private javax.swing.JPanel LoginPanel;
    private javax.swing.JLabel LogoLabel;
    private javax.swing.JLabel PasswordLabel;
    private javax.swing.JPanel RIGHT;
    private javax.swing.JLabel UsernameLabel;
    private javax.swing.JTextField UsernameText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollBar jScrollBar1;
    // End of variables declaration//GEN-END:variables
}
