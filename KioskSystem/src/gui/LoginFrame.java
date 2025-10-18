package gui;

import java.awt.Color;
import objects.*;
import services.UserAuthentication;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.UIManager;

public class LoginFrame extends javax.swing.JFrame {
    
    private boolean isLoggingIn = false;

     public LoginFrame() {
        initComponents();
        setupEnterKeySupport();
        customizeOptionPane();
    }
     
     private void customizeOptionPane() {
         UIManager.put("OptionPane.background", new Color(249, 241, 240));
         UIManager.put("Panel.background", new Color(249, 241, 240));
         UIManager.put("OptionPane.messageForeground", new Color(31, 40, 35));
         UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
         UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 12));
         
         UIManager.put("Button.margin", new Insets(10, 20, 10, 20));
         UIManager.put("Button.padding", new Insets(8, 15, 8, 15));
         
         UIManager.put("Button.background", new Color(31, 40, 35));
         UIManager.put("Button.foreground", Color.WHITE);
         UIManager.put("Button.focus", new Color(51, 60, 55));
    }
     
    private void setupEnterKeySupport() {
        UsernameText.addActionListener(e -> PasswordText.requestFocus());
        PasswordText.addActionListener(e -> attemptLogin());
    }
    
    private void showWelcomeMessage() {
        String welcomeMessage = "Welcome, Admin!\n\n";
        welcomeMessage += "You have full system access privileges.";
        
        JOptionPane.showMessageDialog(this, welcomeMessage, "Login Successful", JOptionPane.INFORMATION_MESSAGE);
        redirectToCrud();
    }
    
    private void attemptLogin() {
        if (isLoggingIn) {
            System.out.println("Login already in progress...");
            return;
        }
        
        String username = UsernameText.getText().trim();
        String password = new String(PasswordText.getPassword());
        
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Username: " + username);
        System.out.println("Password: " + (password.isEmpty() ? "[empty]" : "[provided]"));
        
        if (username.isEmpty()) {
            showError("Please enter your username", "Input Error", UsernameText);
            return;
        }
        
        if (password.isEmpty()) {
            showError("Please enter your password", "Input Error", PasswordText);
            return;
        }
        
        isLoggingIn = true;
        setUiState(false);
        
        new SwingWorker<UserAuthentication.AuthResult, Void>() {
            @Override
            protected UserAuthentication.AuthResult doInBackground() throws Exception {
                System.out.println("Starting authentication process...");
                return UserAuthentication.authenticateWithDetails(username, password);
            }
            
            @Override
            protected void done() {
                try {
                    UserAuthentication.AuthResult result = get();
                    System.out.println("Authentication result: " + result.isSuccess());
                    
                    if (result.isSuccess()) {
                        handleSuccessfulLogin(username);
                    } else {
                        handleFailedLogin(result.getErrorMessage(), result.getErrorType());
                    }
                } catch (Exception ex) {
                    System.out.println("Exception during login: " + ex.getMessage());
                    ex.printStackTrace();
                    showError("An unexpected error occurred during login.", "System Error", null);
                    PasswordText.setText("");
                } finally {
                    // Reset logging state
                    isLoggingIn = false;
                    setUiState(true);
                }
            }
        }.execute();
    }
    
    private void handleSuccessfulLogin(String username) {
        System.out.println("Authentication successful!");
        System.out.println("  Username: " + username);
        
        // Set session data
        Login.login(username);
        
        System.out.println("Session data set:");
        System.out.println("  Current username: " + Login.getCurrentUsername());
        System.out.println("  Is logged in: " + Login.isLoggedIn());
        
        showWelcomeMessage();
    }
    
    private void handleFailedLogin(String errorMessage, String errorType) {
        System.out.println("Authentication failed: " + errorMessage);
        
        switch (errorType) {
            case "USERNAME_ERROR":
                showError("Invalid username. Please try again.", "Login Failed", UsernameText);
                UsernameText.setText("");
                PasswordText.setText("");
                UsernameText.requestFocus();
                break;
                
            case "PASSWORD_ERROR":
                showError("Invalid password. Please try again.", "Login Failed", PasswordText);
                PasswordText.setText("");
                PasswordText.requestFocus();
                break;
                
            default:
                showError(errorMessage, "Login Failed", null);
                break;
        }
    }
    
    private void showError(String message, String title, javax.swing.JComponent focusComponent) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
        if (focusComponent != null) {
            focusComponent.requestFocus();
        }
    }
    
    private void setUiState(boolean enabled) {
        UsernameText.setEnabled(enabled);
        PasswordText.setEnabled(enabled);
        LoginButton.setEnabled(enabled);
        
        if (enabled) {
            LoginButton.setText("Login");
            setCursor(Cursor.getDefaultCursor());
        } else {
            LoginButton.setText("Logging in...");
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }
    }
    
    private void redirectToCrud() {
        System.out.println("Redirecting to Inventory...");
        CRUDSystemFrame crud = new CRUDSystemFrame();
        crud.setVisible(true);
        this.dispose();
    }
    
    private void redirectToKiosk() {
        System.out.println("Redirecting to Kiosk...");
        KioskFrame kioskFrame = new KioskFrame();
        kioskFrame.setVisible(true);
        this.dispose();
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
        PasswordText = new javax.swing.JPasswordField();
        LoginButton = new javax.swing.JButton();
        SeePasswordToggleButton = new javax.swing.JToggleButton();
        KioskButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Login");
        setBackground(new java.awt.Color(201, 177, 158));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        setSize(new java.awt.Dimension(800, 500));

        LoginPanel.setBackground(new java.awt.Color(255, 255, 255));
        LoginPanel.setPreferredSize(new java.awt.Dimension(800, 500));

        LEFT.setBackground(new java.awt.Color(0, 0, 0));

        LogoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/logos/logo1.png"))); // NOI18N
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
                .addContainerGap(44, Short.MAX_VALUE))
        );

        RIGHT.setBackground(new java.awt.Color(249, 241, 240));
        RIGHT.setPreferredSize(new java.awt.Dimension(400, 500));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("LOGIN");

        UsernameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        UsernameLabel.setText("Username");

        UsernameText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        UsernameText.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        UsernameText.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        UsernameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsernameTextActionPerformed(evt);
            }
        });

        PasswordLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        PasswordLabel.setText("Password");

        PasswordText.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        PasswordText.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        PasswordText.setPreferredSize(new java.awt.Dimension(300, 40));
        PasswordText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PasswordTextActionPerformed(evt);
            }
        });

        LoginButton.setBackground(new java.awt.Color(0, 0, 0));
        LoginButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        LoginButton.setForeground(new java.awt.Color(255, 255, 255));
        LoginButton.setText("Login");
        LoginButton.setBorder(null);
        LoginButton.setFocusable(false);
        LoginButton.setPreferredSize(new java.awt.Dimension(91, 35));
        LoginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginButtonActionPerformed(evt);
            }
        });

        SeePasswordToggleButton.setBackground(new java.awt.Color(249, 241, 240));
        SeePasswordToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/unsee.png"))); // NOI18N
        SeePasswordToggleButton.setBorder(null);
        SeePasswordToggleButton.setBorderPainted(false);
        SeePasswordToggleButton.setContentAreaFilled(false);
        SeePasswordToggleButton.setFocusable(false);
        SeePasswordToggleButton.setPreferredSize(new java.awt.Dimension(40, 40));
        SeePasswordToggleButton.setRolloverEnabled(false);
        SeePasswordToggleButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/see.png"))); // NOI18N
        SeePasswordToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SeePasswordToggleButtonActionPerformed(evt);
            }
        });

        KioskButton.setBackground(new java.awt.Color(0, 0, 0));
        KioskButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        KioskButton.setForeground(new java.awt.Color(255, 255, 255));
        KioskButton.setText("CUSTOMER");
        KioskButton.setBorder(null);
        KioskButton.setFocusable(false);
        KioskButton.setPreferredSize(new java.awt.Dimension(100, 35));
        KioskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KioskButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout RIGHTLayout = new javax.swing.GroupLayout(RIGHT);
        RIGHT.setLayout(RIGHTLayout);
        RIGHTLayout.setHorizontalGroup(
            RIGHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RIGHTLayout.createSequentialGroup()
                .addGroup(RIGHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(KioskButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(RIGHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(RIGHTLayout.createSequentialGroup()
                            .addGap(40, 40, 40)
                            .addGroup(RIGHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(UsernameLabel)
                                .addComponent(PasswordLabel)
                                .addComponent(LoginButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(RIGHTLayout.createSequentialGroup()
                                    .addGroup(RIGHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(UsernameText, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(PasswordText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(SeePasswordToggleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(RIGHTLayout.createSequentialGroup()
                            .addGap(145, 145, 145)
                            .addComponent(jLabel1))))
                .addContainerGap(16, Short.MAX_VALUE))
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
                .addGroup(RIGHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(SeePasswordToggleButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PasswordText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(LoginButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                .addComponent(KioskButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout LoginPanelLayout = new javax.swing.GroupLayout(LoginPanel);
        LoginPanel.setLayout(LoginPanelLayout);
        LoginPanelLayout.setHorizontalGroup(
            LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addComponent(LEFT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(RIGHT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        LoginPanelLayout.setVerticalGroup(
            LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LEFT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(RIGHT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LoginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LoginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void UsernameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UsernameTextActionPerformed
        PasswordText.requestFocus();
    }//GEN-LAST:event_UsernameTextActionPerformed

    private void PasswordTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PasswordTextActionPerformed
        attemptLogin();
    }//GEN-LAST:event_PasswordTextActionPerformed

    private void LoginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoginButtonActionPerformed
        attemptLogin();
    }//GEN-LAST:event_LoginButtonActionPerformed

    private void SeePasswordToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SeePasswordToggleButtonActionPerformed
        if (SeePasswordToggleButton.isSelected()) {
            PasswordText.setEchoChar((char) 0);
        } else {
            PasswordText.setEchoChar('•');
        }
    }//GEN-LAST:event_SeePasswordToggleButtonActionPerformed

    private void KioskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KioskButtonActionPerformed
        redirectToKiosk();
    }//GEN-LAST:event_KioskButtonActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton KioskButton;
    private javax.swing.JPanel LEFT;
    private javax.swing.JButton LoginButton;
    private javax.swing.JPanel LoginPanel;
    private javax.swing.JLabel LogoLabel;
    private javax.swing.JLabel PasswordLabel;
    private javax.swing.JPasswordField PasswordText;
    private javax.swing.JPanel RIGHT;
    private javax.swing.JToggleButton SeePasswordToggleButton;
    private javax.swing.JLabel UsernameLabel;
    private javax.swing.JTextField UsernameText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollBar jScrollBar1;
    // End of variables declaration//GEN-END:variables
}
