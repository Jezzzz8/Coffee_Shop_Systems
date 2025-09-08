package ui;

import backend.*;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.awt.Cursor;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoginFrame extends javax.swing.JFrame {
    
    private AtomicBoolean isLoggingIn = new AtomicBoolean(false);

    public LoginFrame() {
        initComponents();
        checkDatabaseConnection();
        setupEnterKeySupport();
    }
    
    private void setupEnterKeySupport() {
        // Enter key support for both fields
        UsernameText.addActionListener(e -> PasswordText.requestFocus());
        PasswordText.addActionListener(e -> attemptLogin());
    }
    
    private void checkDatabaseConnection() {
        try {
            System.out.println("Checking database connection...");
            if (DatabaseConnection.getConnection() == null) {
                JOptionPane.showMessageDialog(this, 
                    "Database connection failed. Please check your database settings.", 
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            } else {
                System.out.println("Database connection successful!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Database connection error: " + e.getMessage(), 
                "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    private void debugSessionData() {
        System.out.println("=== SESSION DEBUG ===");
        System.out.println("Current username: " + backend.SessionManager.getCurrentUsername());
        System.out.println("Current role: " + backend.SessionManager.getCurrentRole());
        System.out.println("Current user ID: " + backend.SessionManager.getCurrentUserId());
        System.out.println("Is logged in: " + backend.SessionManager.isLoggedIn());

        // Verify with database
        User user = backend.UserAuthentication.getUserByUsername(backend.SessionManager.getCurrentUsername());
        if (user != null) {
            System.out.println("Database role: " + user.getRole());
            System.out.println("Session vs Database role match: " + 
                              user.getRole().equals(backend.SessionManager.getCurrentRole()));
        }
        System.out.println("=== END SESSION DEBUG ===");
    }
    
    private void showWelcomeMessage(User user) {
        String fullName = user.getFullName();
        String role = user.getRole();

        String welcomeMessage = "Welcome, " + fullName + "!\n";
        welcomeMessage += "Role: " + role + "\n\n";

        System.out.println("Redirecting based on role: " + role);
        
        // Role-specific redirection
        if ("admin".equalsIgnoreCase(role)) {
            welcomeMessage += "You have full system access privileges.";
            JOptionPane.showMessageDialog(this, welcomeMessage, "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            redirectToInventory();
        } else if ("cashier".equalsIgnoreCase(role)) {
            welcomeMessage += "You can process sales and transactions.";
            JOptionPane.showMessageDialog(this, welcomeMessage, "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            redirectToPOS();
        } else if ("clerk".equalsIgnoreCase(role)) {
            welcomeMessage += "You have clerk access privileges.";
            JOptionPane.showMessageDialog(this, welcomeMessage, "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            redirectToInventory();
        } else {
            welcomeMessage += "You have basic user access.";
            JOptionPane.showMessageDialog(this, welcomeMessage, "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            redirectToKiosk();
        }
    }
    
    private void attemptLogin() {
        if (isLoggingIn.get()) {
            System.out.println("Login already in progress...");
            return;
        }
        
        String username = UsernameText.getText().trim();
        String password = new String(PasswordText.getPassword());
        
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Username: " + username);
        System.out.println("Password: " + (password.isEmpty() ? "[empty]" : "[provided]"));
        
        // Input validation
        if (username.isEmpty()) {
            showError("Please enter your username", "Input Error", UsernameText);
            return;
        }
        
        if (password.isEmpty()) {
            showError("Please enter your password", "Input Error", PasswordText);
            return;
        }
        
        // Check if username exists first
        if (!backend.UserAuthentication.usernameExists(username)) {
            showError("Username not found. Please check your username and try again.", 
                     "Login Failed", UsernameText);
            return;
        }
        
        // Set logging state
        isLoggingIn.set(true);
        setUiState(false);
        
        // Use SwingWorker for background authentication
        new SwingWorker<UserAuthentication.AuthResult, Void>() {
            @Override
            protected UserAuthentication.AuthResult doInBackground() throws Exception {
                System.out.println("Starting authentication process...");
                return backend.UserAuthentication.authenticateWithDetails(username, password);
            }
            
            @Override
            protected void done() {
                try {
                    UserAuthentication.AuthResult result = get();
                    System.out.println("Authentication result: " + result.isSuccess());
                    
                    if (result.isSuccess()) {
                        handleSuccessfulLogin(result.getUser());
                    } else {
                        handleFailedLogin(result.getErrorMessage(), result.getErrorType());
                    }
                } catch (Exception ex) {
                    System.out.println("Exception during login: " + ex.getMessage());
                    ex.printStackTrace();
                    handleException(ex);
                } finally {
                    // Reset UI state
                    isLoggingIn.set(false);
                    setUiState(true);
                }
            }
        }.execute();
    }
    
    private void handleSuccessfulLogin(User user) {
        System.out.println("Authentication successful!");
        System.out.println("User details retrieved:");
        System.out.println("  ID: " + user.getId());
        System.out.println("  Name: " + user.getFullName());
        System.out.println("  Role: " + user.getRole());
        System.out.println("  Username: " + user.getUsername());
        
        // Set session data
        backend.SessionManager.login(user.getUsername());
        backend.SessionManager.setCurrentRole(user.getRole());
        backend.SessionManager.setCurrentUserId(user.getId());
        
        System.out.println("Session data set:");
        System.out.println("  Current username: " + backend.SessionManager.getCurrentUsername());
        System.out.println("  Current role: " + backend.SessionManager.getCurrentRole());
        System.out.println("  Current user ID: " + backend.SessionManager.getCurrentUserId());
        System.out.println("  Is logged in: " + backend.SessionManager.isLoggedIn());
        
        // Show welcome message and redirect
        showWelcomeMessage(user);
    }
    
    private void handleFailedLogin(String errorMessage, String errorType) {
        System.out.println("Authentication failed: " + errorMessage);
        
        switch (errorType) {
            case "INVALID_PASSWORD":
                showError("Invalid password. Please try again.", "Login Failed", PasswordText);
                PasswordText.setText("");
                PasswordText.requestFocus();
                break;
                
            case "ACCOUNT_LOCKED":
                showError("Your account is temporarily locked. Please contact administrator.", 
                         "Account Locked", null);
                break;
                
            case "INACTIVE_ACCOUNT":
                showError("Your account is inactive. Please contact administrator.", 
                         "Account Inactive", null);
                break;
                
            default:
                showError(errorMessage, "Login Failed", null);
                break;
        }
    }
    
    private void handleException(Exception ex) {
        String errorMessage = "An unexpected error occurred during login.";
        if (ex.getMessage() != null && ex.getMessage().contains("Connection")) {
            errorMessage = "Database connection error. Please check your connection and try again.";
        }
        
        showError(errorMessage, "System Error", null);
        PasswordText.setText("");
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setBackground(new java.awt.Color(201, 177, 158));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setSize(new java.awt.Dimension(800, 500));

        LoginPanel.setBackground(new java.awt.Color(255, 255, 255));
        LoginPanel.setPreferredSize(new java.awt.Dimension(800, 500));

        LEFT.setBackground(new java.awt.Color(0, 0, 0));
        LEFT.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

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
                .addContainerGap(44, Short.MAX_VALUE))
        );

        RIGHT.setBackground(new java.awt.Color(249, 241, 240));
        RIGHT.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        RIGHT.setPreferredSize(new java.awt.Dimension(400, 500));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("LOGIN");

        UsernameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        UsernameLabel.setText("Username");

        UsernameText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        UsernameText.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        UsernameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsernameTextActionPerformed(evt);
            }
        });

        PasswordLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        PasswordLabel.setText("Password");

        PasswordText.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
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
        LoginButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        LoginButton.setPreferredSize(new java.awt.Dimension(91, 35));
        LoginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginButtonActionPerformed(evt);
            }
        });

        SeePasswordToggleButton.setBackground(new java.awt.Color(249, 241, 240));
        SeePasswordToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/unsee.png"))); // NOI18N
        SeePasswordToggleButton.setBorder(null);
        SeePasswordToggleButton.setBorderPainted(false);
        SeePasswordToggleButton.setContentAreaFilled(false);
        SeePasswordToggleButton.setFocusable(false);
        SeePasswordToggleButton.setPreferredSize(new java.awt.Dimension(40, 40));
        SeePasswordToggleButton.setRolloverEnabled(false);
        SeePasswordToggleButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/see.png"))); // NOI18N
        SeePasswordToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SeePasswordToggleButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout RIGHTLayout = new javax.swing.GroupLayout(RIGHT);
        RIGHT.setLayout(RIGHTLayout);
        RIGHTLayout.setHorizontalGroup(
            RIGHTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RIGHTLayout.createSequentialGroup()
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
                        .addComponent(jLabel1)))
                .addContainerGap(10, Short.MAX_VALUE))
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
                .addContainerGap(165, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout LoginPanelLayout = new javax.swing.GroupLayout(LoginPanel);
        LoginPanel.setLayout(LoginPanelLayout);
        LoginPanelLayout.setHorizontalGroup(
            LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addComponent(LEFT, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            // Show password
            PasswordText.setEchoChar((char) 0); // Show plain text
        } else {
            // Hide password
            PasswordText.setEchoChar('•'); // Show bullet characters (or use the default echo char)
            // Alternatively, you can use the default echo character:
            // jPasswordField1.setEchoChar(jPasswordField1.getEchoChar());
        }
    }//GEN-LAST:event_SeePasswordToggleButtonActionPerformed

    private void redirectToInventory() {
        System.out.println("Redirecting to Inventory...");
        InventoryFrame inventoryFrame = new InventoryFrame();
        inventoryFrame.setVisible(true);
        this.dispose();
    }
    
    private void redirectToPOS() {
        System.out.println("Redirecting to POS...");
        POSFrame posFrame = new POSFrame();
        posFrame.setVisible(true);
        posFrame.pack();
        posFrame.setLocationRelativeTo(null);
        this.dispose();
    }
    
    private void redirectToKiosk() {
        System.out.println("Redirecting to Kiosk...");
        KioskFrame kioskFrame = new KioskFrame();
        kioskFrame.setVisible(true);
        kioskFrame.pack();
        kioskFrame.setLocationRelativeTo(null);
        this.dispose();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
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
