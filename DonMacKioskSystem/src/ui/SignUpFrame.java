package ui;

public class SignUpFrame extends javax.swing.JFrame {

    public SignUpFrame() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        SignUpPanel = new javax.swing.JPanel();
        LEFT = new javax.swing.JPanel();
        LogoLabel = new javax.swing.JLabel();
        RIGHT = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        FirstNameLabel = new javax.swing.JLabel();
        FirstNameText = new javax.swing.JTextField();
        LastNameLabel = new javax.swing.JLabel();
        LastNameText = new javax.swing.JTextField();
        UsernameLabel2 = new javax.swing.JLabel();
        UsernameText2 = new javax.swing.JTextField();
        PasswordLabel = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        SignUpButton = new javax.swing.JButton();
        LoginLabel = new javax.swing.JLabel();
        LoginButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sign Up");
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setPreferredSize(new java.awt.Dimension(800, 500));

        SignUpPanel.setBackground(new java.awt.Color(255, 255, 255));
        SignUpPanel.setPreferredSize(new java.awt.Dimension(800, 500));
        SignUpPanel.setLayout(null);

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

        SignUpPanel.add(LEFT);
        LEFT.setBounds(0, 0, 400, 500);

        RIGHT.setBackground(new java.awt.Color(249, 241, 240));
        RIGHT.setPreferredSize(new java.awt.Dimension(400, 500));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("SIGN UP");
        jLabel1.setPreferredSize(new java.awt.Dimension(144, 48));

        FirstNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        FirstNameLabel.setText("First Name");

        FirstNameText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        FirstNameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FirstNameTextActionPerformed(evt);
            }
        });

        LastNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        LastNameLabel.setText("Last Name");

        LastNameText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        LastNameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LastNameTextActionPerformed(evt);
            }
        });

        UsernameLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        UsernameLabel2.setText("Username");

        UsernameText2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        UsernameText2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsernameText2ActionPerformed(evt);
            }
        });

        PasswordLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        PasswordLabel.setText("Password");

        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        SignUpButton.setBackground(new java.awt.Color(0, 0, 0));
        SignUpButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        SignUpButton.setForeground(new java.awt.Color(255, 255, 255));
        SignUpButton.setText("Sign Up");
        SignUpButton.setPreferredSize(new java.awt.Dimension(91, 35));
        SignUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SignUpButtonActionPerformed(evt);
            }
        });

        LoginLabel.setText("I already have an account");

        LoginButton.setBackground(new java.awt.Color(0, 0, 0));
        LoginButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        LoginButton.setForeground(new java.awt.Color(0, 204, 204));
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
                .addGap(45, 45, 45)
                .addGroup(RIGHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(RIGHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(FirstNameText)
                        .addComponent(jPasswordField1)
                        .addComponent(LastNameText)
                        .addComponent(UsernameText2)
                        .addGroup(RIGHTLayout.createSequentialGroup()
                            .addGroup(RIGHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(FirstNameLabel)
                                .addComponent(PasswordLabel)
                                .addComponent(SignUpButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(LastNameLabel)
                                .addComponent(UsernameLabel2)
                                .addGroup(RIGHTLayout.createSequentialGroup()
                                    .addComponent(LoginLabel)
                                    .addGap(12, 12, 12)
                                    .addComponent(LoginButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(73, 73, 73)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RIGHTLayout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(82, 82, 82)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        RIGHTLayout.setVerticalGroup(
            RIGHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RIGHTLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FirstNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FirstNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LastNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LastNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UsernameLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UsernameText2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PasswordLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SignUpButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(RIGHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(LoginButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LoginLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        SignUpPanel.add(RIGHT);
        RIGHT.setBounds(400, 0, 400, 500);

        getContentPane().add(SignUpPanel, java.awt.BorderLayout.CENTER);

        getAccessibleContext().setAccessibleName("SIGNUP");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void FirstNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FirstNameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FirstNameTextActionPerformed

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField1ActionPerformed

    private void LoginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoginButtonActionPerformed
        LoginFrame LoginFrame = new LoginFrame();
        LoginFrame.setVisible(true);
        LoginFrame.pack();
        LoginFrame.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_LoginButtonActionPerformed

    private void LastNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LastNameTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LastNameTextActionPerformed

    private void UsernameText2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UsernameText2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UsernameText2ActionPerformed

    private void SignUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SignUpButtonActionPerformed
        LoginFrame LoginFrame = new LoginFrame();
        LoginFrame.setVisible(true);
        LoginFrame.pack();
        LoginFrame.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_SignUpButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel FirstNameLabel;
    private javax.swing.JTextField FirstNameText;
    private javax.swing.JPanel LEFT;
    private javax.swing.JLabel LastNameLabel;
    private javax.swing.JTextField LastNameText;
    private javax.swing.JButton LoginButton;
    private javax.swing.JLabel LoginLabel;
    private javax.swing.JLabel LogoLabel;
    private javax.swing.JLabel PasswordLabel;
    private javax.swing.JPanel RIGHT;
    private javax.swing.JButton SignUpButton;
    private javax.swing.JPanel SignUpPanel;
    private javax.swing.JLabel UsernameLabel2;
    private javax.swing.JTextField UsernameText2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPasswordField jPasswordField1;
    // End of variables declaration//GEN-END:variables
}
