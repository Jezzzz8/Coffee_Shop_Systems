package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import objects.OrderSlip;
import objects.Product;
import services.ConsolePrintService;
import services.OrderManager;
import services.OrderSlipManager;
import services.PaymentManager;
import services.PrintService;
import services.ProductManager;
import services.QueueManager;


public class KioskFrame extends javax.swing.JFrame {

    private List<Product> menuProducts;
    private List<Product> specialsProducts;
    private Map<Product, Integer> cartItems;
    private double totalPrice;
    private PrintService printService;
    private int lastOrderId = -1;
    private String selectedPaymentMethod = "Cash";
    
    public KioskFrame() {
        initComponents();
        cartItems = new HashMap<>();
        totalPrice = 0.0;
        printService = new ConsolePrintService();
        
        QueueManager.initializeQueue();
        
        customizeOptionPane();
        
        initializeLanguageChoice();
        
        menu_category_box.remove(MenuProductDetailBoxPanel);
        specials_category_box.remove(SpecialsProductDetailBoxPanel);
        CartItemsPanel.remove(CartProductDetailBoxPanel);

        
        menu_category_scroll_pane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        menu_category_scroll_pane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        specials_category_scroll_pane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        specials_category_scroll_pane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        CartScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        CartScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        
        MainTabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateProgressBar();
            }
        });

        updateCartDisplay();
        updateCartAnnouncement();
        updateProgressBar();

        initializeProducts();
        updatePaymentButtonAvailability();
    }
    
    private void updateProgressBar() {
        int currentTabIndex = MainTabbedPane.getSelectedIndex();
        int progress = 0;
        
        switch (currentTabIndex) {
            case 0:
                progress = 25;
                break;
            case 1:
                progress = 50;
                break;
            case 2:
                progress = 75;
                break;
            case 3:
                progress = 75;
                break;
            case 4:
                progress = 75;
                break;
            default:
                progress = 25;
        }
        
        OrderingProgressBar.setValue(progress);
    }
    
    private void initializeProducts() {
        System.out.println("Loading products...");

        
        menuProducts = ProductManager.getAllProducts(); 
        specialsProducts = new ArrayList<>(); 

        System.out.println("Menu products loaded: " + menuProducts.size());

        
        updatePaymentButtonAvailability();

        
        updateProductDisplays();

        System.out.println("Product display updated");
    }
    
    private String loadProductImage(Product product) {
    String imageDirectory = "/gui/Images/product_images/";
    String imageFilename = product.getImageFilename();

    
    if (imageFilename == null || imageFilename.trim().isEmpty()) {
        imageFilename = "default.png";
    }

    return imageDirectory + imageFilename;
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

private void initializeLanguageChoice() {
        // Clear existing items and add available languages
        LanguageChoice.removeAll();
        String[] languages = LanguageContent.getAvailableLanguages();
        for (String language : languages) {
            LanguageChoice.add(language);
        }
        
        // Set default to English
        LanguageChoice.select(0);
        
        // Add item listener to handle language changes
        LanguageChoice.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                languageChoiceItemStateChanged(evt);
            }
        });
}

private void languageChoiceItemStateChanged(java.awt.event.ItemEvent evt) {
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            String selectedLanguage = LanguageChoice.getSelectedItem();
            updateHelpContent(selectedLanguage);
        }
}

private void updateHelpContent(String language) {
        String helpContent = LanguageContent.getHelpContent(language);
        ContentsJTextArea.setText(helpContent);
        
        // Scroll to top when language changes
        ContentsJTextArea.setCaretPosition(0);
    }

private JLabel createImageLabel(Product product) {
    JLabel imageLabel = new JLabel();
    String fullImagePath = loadProductImage(product);

    try {
        java.net.URL imageUrl = getClass().getResource(fullImagePath);
        if (imageUrl != null) {
            ImageIcon originalIcon = new ImageIcon(imageUrl);
            Image scaledImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            
            System.out.println("Image not found: " + fullImagePath);
            ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/gui/Images/product_images/default.png"));
            if (defaultIcon.getImage() != null) {
                Image scaledImage = defaultIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            }
        }
    } catch (Exception e) {
        System.out.println("Error loading image for product: " + product.getName());
        System.out.println("Attempted path: " + fullImagePath);
        e.printStackTrace();
    }

    imageLabel.setBorder(BorderFactory.createLineBorder(new Color(31, 40, 35), 2));
    imageLabel.setPreferredSize(new Dimension(150, 150));
    return imageLabel;
}
    
    private JPanel createProductBox(Product product, boolean isMenuProduct) {
        JPanel panel = new JPanel();
        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panel.setBackground(new Color(249, 241, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new SoftBevelBorder(BevelBorder.RAISED),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setPreferredSize(new Dimension(700, 250));

        if (product == null) {
            return new JPanel(); 
        }

        JLabel imageLabel = createImageLabel(product);
        panel.add(imageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        imageLabel.setBorder(BorderFactory.createLineBorder(new Color(31, 40, 35), 2));
        imageLabel.setPreferredSize(new Dimension(150, 150));
        panel.add(imageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        // Product Name
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        nameLabel.setForeground(new Color(31, 40, 35));
        panel.add(nameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 500, 30));

        // Product Price
        JLabel priceLabel = new JLabel(String.format("₱%.2f", product.getPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        priceLabel.setForeground(new Color(100, 100, 100));
        panel.add(priceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 260, 25));

        // Product Description
        JLabel descriptionLabel = new JLabel(product.getDescription());
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descriptionLabel.setForeground(new Color(80, 80, 80));
        descriptionLabel.setVerticalAlignment(SwingConstants.TOP);
        panel.add(descriptionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 500, 45));

        // Availability
        boolean isAvailable = product.isAvailable();
        JLabel availabilityLabel = new JLabel(isAvailable ? "Available" : "Not Available");
        availabilityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        availabilityLabel.setForeground(isAvailable ? new Color(42, 168, 83) : new Color(255, 0, 0));
        panel.add(availabilityLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 170, 260, 20));
        
        // Quantity Label
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        quantityLabel.setForeground(new Color(66, 133, 244));
        panel.add(quantityLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 155, 80, 40));
        
        // Quantity Spinner
        JSpinner quantitySpinner = new JSpinner();
        quantitySpinner.setFont(new Font("Segoe UI", Font.BOLD, 24));

        int maxQuantity = isAvailable ? 100 : 0;
        quantitySpinner.setModel(new SpinnerNumberModel(0, 0, maxQuantity, 1));

        quantitySpinner.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        quantitySpinner.setPreferredSize(new Dimension(128, 64));
        quantitySpinner.setEnabled(isAvailable); 

        if (!isAvailable) {
            quantitySpinner.setBackground(new Color(240, 240, 240));
        }

        panel.add(quantitySpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 150, -1, -1));

        // Add Button
        JButton addButton = new JButton();
        addButton.setIcon(new ImageIcon(getClass().getResource("/gui/Images/icons/add.png")));
        addButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addButton.setBackground(isAvailable ? new Color(249, 241, 240) : new Color(200, 200, 200));
        addButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        addButton.setCursor(isAvailable ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));
        addButton.setEnabled(isAvailable);
        addButton.setToolTipText(isAvailable ? "Add to Cart" : "Product not available");

        addButton.addActionListener(e -> {
            int quantity = (Integer) quantitySpinner.getValue();
            if (quantity > 0) {
                addToCart(product, quantity);
                quantitySpinner.setValue(0); 
            }
        });
        panel.add(addButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 150, 64, 64));

        return panel;
    }
    
    private void updateProductDisplays() {
        
        menu_category_box.removeAll();
        specials_category_box.removeAll();

        
        for (Product product : menuProducts) {
            JPanel productBox = createProductBox(product, true);
            menu_category_box.add(productBox);
        }

        
        List<Product> bestSellingProducts = getActualBestSellingProducts();
        for (Product product : bestSellingProducts) {
            JPanel productBox = createProductBox(product, false);
            specials_category_box.add(productBox);
        }

        
        adjustMenuCategoryBoxHeight();
        adjustSpecialsCategoryBoxHeight();

        
        menu_category_box.revalidate();
        menu_category_box.repaint();
        specials_category_box.revalidate();
        specials_category_box.repaint();

        
        updateCartDisplay();

        
        updateCartAnnouncement();
    }
    
    private void updateCartAnnouncement() {
        int totalItems = cartItems.values().stream().mapToInt(Integer::intValue).sum();
        String announcement = totalItems > 0 ? 
            "You have " + totalItems + " item(s) in your cart." : 
            "Your cart is empty.";

        CartUpdateAnnouncementOffscreenLabel1.setText(announcement);
        CartUpdateAnnouncementOffscreenLabel2.setText(announcement);
    }
    
    private List<Product> getActualBestSellingProducts() {
        List<Product> bestProducts = new ArrayList<>();

        try {
            String sql = "SELECT p.product_id, p.product_name, p.price, p.description, " +
                        "p.image_filename, p.is_available, " +
                        "COALESCE(SUM(oi.quantity), 0) as total_sold " +
                        "FROM product_tb p " +
                        "LEFT JOIN order_item_tb oi ON p.product_id = oi.product_id " +
                        "GROUP BY p.product_id, p.product_name, p.price, p.description, " +
                        "p.image_filename, p.is_available " +
                        "ORDER BY total_sold DESC, p.product_name " +
                        "LIMIT 8";

            java.sql.Connection conn = database.DatabaseConnection.getConnection();
            java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
            java.sql.ResultSet rs = pstmt.executeQuery();

            while (rs.next() && bestProducts.size() < 8) {
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                double price = rs.getDouble("price");
                String description = rs.getString("description");
                String imageFilename = rs.getString("image_filename");
                boolean isAvailable = rs.getBoolean("is_available");

                Product product = new Product(productId, productName, price, 
                                            description, imageFilename, isAvailable);


                bestProducts.add(product);
            }

            rs.close();
            pstmt.close();

            // Update the specialsProducts field
            specialsProducts = bestProducts;

        } catch (Exception e) {
            System.out.println("Error loading best selling products: " + e.getMessage());
            e.printStackTrace();


            for (Product product : menuProducts) {
                if (bestProducts.size() >= 8) break;
                bestProducts.add(product);
            }

            // Update the specialsProducts field even in case of error
            specialsProducts = bestProducts;
        }

        return bestProducts;
    }
    
    private void addToCart(Product product, int quantity) {
        if (quantity <= 0) {
            return;
        }
        
        if (!product.isAvailable()) {
            JOptionPane.showMessageDialog(this, 
                product.getName() + " is not available.", 
                "Product Not Available", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Product existingProduct = findProductInCart(product.getId());

        if (existingProduct != null) {
            int currentQuantity = cartItems.get(existingProduct);
            cartItems.put(existingProduct, currentQuantity + quantity);
        } else {
            cartItems.put(product, quantity);
        }
        
        totalPrice += product.getPrice() * quantity;
        
        updateCartDisplay();
        updateCartAnnouncement();
    }

    private Product findProductInCart(int productId) {
        for (Product cartProduct : cartItems.keySet()) {
            if (cartProduct.getId() == productId) {
                return cartProduct;
            }
        }
        return null;
    }
    
    private void updateCartDisplay() {
        CartItemsPanel.removeAll();

        if (cartItems.isEmpty()) {
            JLabel emptyLabel = new JLabel("Your cart is empty");
            emptyLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyLabel.setForeground(new Color(100, 100, 100));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            CartItemsPanel.add(emptyLabel);
            
            CheckOutButton.setEnabled(false);
        } else {
            boolean hasItems = cartItems.values().stream().anyMatch(qty -> qty > 0);
            CheckOutButton.setEnabled(hasItems && totalPrice > 0);
            
            for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                
                if (quantity > 0) {
                    JPanel cartItemPanel = createCartItemPanel(product, quantity);
                    cartItemPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    CartItemsPanel.add(cartItemPanel);
                    CartItemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        }
        
        TotalPriceNumberLabel.setText(String.format("₱%.2f", totalPrice));
        
        adjustCartPanelHeight();
        
        CartItemsPanel.revalidate();
        CartItemsPanel.repaint();
    }
    
    private void adjustMenuCategoryBoxHeight() {
        int productCount = menuProducts.size();
        int preferredHeight;

        if (productCount >= 2) {
            int rows = (int) Math.ceil(productCount / 2.0); 
            preferredHeight = rows * 470 + 40;
        } else {
            preferredHeight = 400;
        }

        menu_category_box.setPreferredSize(new Dimension(760, preferredHeight));
    }
    
    private void adjustSpecialsCategoryBoxHeight() {
        int productCount = specialsProducts.size();
        int preferredHeight;

        if (productCount >= 2) {
            int rows = (int) Math.ceil(productCount / 2.0); 
            preferredHeight = rows * 500 + 60;
        } else {
            preferredHeight = 400;
        }

        specials_category_box.setPreferredSize(new Dimension(760, preferredHeight));
    }
    
    private void adjustCartPanelHeight() {
        int cartCount = cartItems.size();
        int preferredHeight;
        
        if (cartCount >= 2) {
            int rows = (int) Math.ceil(cartCount / 2.0);
            preferredHeight = rows * 500 + 40;
        } else {
            preferredHeight = 400;
        }
        
        CartItemsPanel.setPreferredSize(new Dimension(760, preferredHeight));
    }
    
    private JPanel createCartItemPanel(Product product, int quantity) {
        JPanel panel = new JPanel();
        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panel.setBackground(new Color(249, 241, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new SoftBevelBorder(BevelBorder.RAISED),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setPreferredSize(new Dimension(700, 250));
        panel.setMaximumSize(new Dimension(700, 250));

        if (product == null) {
            return new JPanel(); 
        }

        JLabel imageLabel = createImageLabel(product);
        panel.add(imageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        imageLabel.setBorder(BorderFactory.createLineBorder(new Color(31, 40, 35), 2));
        imageLabel.setPreferredSize(new Dimension(150, 150));
        panel.add(imageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        // Product Name
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        nameLabel.setForeground(new Color(31, 40, 35));
        panel.add(nameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 500, 30));

        // Product Price
        JLabel priceLabel = new JLabel(String.format("₱%.2f", product.getPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        priceLabel.setForeground(new Color(100, 100, 100));
        panel.add(priceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 260, 25));

        // Product Description
        JLabel descriptionLabel = new JLabel(product.getDescription());
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descriptionLabel.setForeground(new Color(80, 80, 80));
        descriptionLabel.setVerticalAlignment(SwingConstants.TOP);
        panel.add(descriptionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 500, 45));

        // Availability
        boolean isAvailable = product.isAvailable();
        JLabel availabilityLabel = new JLabel(isAvailable ? "Available" : "Not Available");
        availabilityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        availabilityLabel.setForeground(isAvailable ? new Color(42, 168, 83) : new Color(255, 0, 0));
        panel.add(availabilityLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 170, 260, 20));

        // Item Total
        JLabel itemTotalLabel = new JLabel(String.format("Total: ₱%.2f", product.getPrice() * quantity));
        itemTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        itemTotalLabel.setForeground(new Color(31, 40, 35));
        panel.add(itemTotalLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 120, 260, 25));

        // Quantity Label
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        quantityLabel.setForeground(new Color(66, 133, 244));
        panel.add(quantityLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 155, 80, 40));

        // Quantity Spinner
        JSpinner quantitySpinner = new JSpinner();
        quantitySpinner.setFont(new Font("Segoe UI", Font.BOLD, 24));

        quantitySpinner.setModel(new SpinnerNumberModel(quantity, 0, 100, 1));

        quantitySpinner.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        quantitySpinner.setPreferredSize(new Dimension(128, 64));
        panel.add(quantitySpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 150, -1, -1));

        quantitySpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int newQuantity = (Integer) quantitySpinner.getValue();
                int oldQuantity = cartItems.get(product);

                if (newQuantity != oldQuantity) {
                    totalPrice += product.getPrice() * (newQuantity - oldQuantity);

                    if (newQuantity == 0) {
                        cartItems.remove(product);
                    } else {
                        cartItems.put(product, newQuantity);
                    }

                    itemTotalLabel.setText(String.format("Total: ₱%.2f", product.getPrice() * newQuantity));
                    TotalPriceNumberLabel.setText(String.format("₱%.2f", totalPrice));
                    updateCartAnnouncement();
                    updateCartDisplay();
                }
            }
        });

        // Delete Button
        JButton deleteButton = new JButton();
        deleteButton.setIcon(new ImageIcon(getClass().getResource("/gui/Images/icons/delete.png")));
        deleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        deleteButton.setBackground(new Color(249, 241, 240));
        deleteButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.setToolTipText("Remove from Cart");
        deleteButton.setPreferredSize(new Dimension(64, 64));

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quantityToRemove = cartItems.get(product);
                totalPrice -= product.getPrice() * quantityToRemove;
                cartItems.remove(product);

                updateCartDisplay();
                updateCartAnnouncement();
            }
        });
        panel.add(deleteButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 150, -1, -1));

        return panel;
    }
    
    private void updatePaymentButtonAvailability() {
        boolean isGCashAvailable = PaymentManager.isPaymentMethodAvailable("GCash");
        boolean isCashAvailable = PaymentManager.isPaymentMethodAvailable("Cash");

        GCashButton.setEnabled(isGCashAvailable);
        CashButton.setEnabled(isCashAvailable);

        // Update button appearance based on availability
        if (!isGCashAvailable) {
            GCashButton.setBackground(new Color(200, 200, 200)); 
            GCashButton.setToolTipText("GCash payment is currently unavailable");
        } else {
            GCashButton.setBackground(new Color(249, 241, 240)); 
            GCashButton.setToolTipText("Pay with GCash");
        }
        
        if (!isCashAvailable) {
            CashButton.setBackground(new Color(200, 200, 200)); 
            CashButton.setToolTipText("Cash payment is currently unavailable");
        } else {
            CashButton.setBackground(new Color(249, 241, 240)); 
            CashButton.setToolTipText("Pay with Cash");
        }
    }
    
    private void checkout() {
        System.out.println("Checkout started. Cart items: " + cartItems.size());
        
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!", "Checkout Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmation = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to proceed with checkout?\n\nTotal Items: " + getTotalItemCount() + "\nTotal Price: " + String.format("₱%.2f", totalPrice),
            "Confirm Checkout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirmation == JOptionPane.YES_OPTION) {
            // Check for unavailable items
            boolean hasUnavailableItems = false;
            StringBuilder unavailableMessage = new StringBuilder();

            for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
                Product product = entry.getKey();
                if (!product.isAvailable()) {
                    hasUnavailableItems = true;
                    unavailableMessage.append("- ")
                                    .append(product.getName())
                                    .append(" is no longer available\n");
                }
            }

            if (hasUnavailableItems) {
                // Show warning about unavailable items
                int result = JOptionPane.showConfirmDialog(this,
                    "Warning: Some items in your cart are no longer available:\n\n" +
                    unavailableMessage.toString() + "\n" +
                    "Do you still want to proceed with checkout? These items will be removed.",
                    "Availability Warning",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

                // If user doesn't want to proceed, return
                if (result != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // Proceed to payment selection
            MainTabbedPane.setSelectedIndex(2);
        }
    }

    private int getTotalItemCount() {
        return cartItems.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    private void checkoutWithPayment(String paymentMethod) {
        System.out.println("Checkout started with payment: " + paymentMethod);
        this.selectedPaymentMethod = paymentMethod;

        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!", "Checkout Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if payment method is available using PaymentManager
        if (!PaymentManager.isPaymentMethodAvailable(paymentMethod)) {
            JOptionPane.showMessageDialog(this, 
                paymentMethod + " payment is currently unavailable. Please choose another payment method.", 
                "Payment Unavailable", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ... rest of the checkoutWithPayment method remains the same ...
        boolean hasUnavailableItems = false;
        StringBuilder unavailableMessage = new StringBuilder();

        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            Product product = entry.getKey();
            if (!product.isAvailable()) {
                hasUnavailableItems = true;
                unavailableMessage.append("- ")
                                .append(product.getName())
                                .append(" is no longer available\n");
            }
        }

        if (hasUnavailableItems) {
            int result = JOptionPane.showConfirmDialog(this,
                "Warning: Some items in your cart are no longer available:\n\n" +
                unavailableMessage.toString() + "\n" +
                "Do you still want to proceed with checkout? These items will be removed.",
                "Availability Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (result != JOptionPane.YES_OPTION) {
                return;
            }
        }

        int orderId = processOrderWithPayment(paymentMethod);
        System.out.println("Process order returned: " + orderId);

        if (orderId != -1) {
            lastOrderId = orderId;

            OrderSlip orderSlip = OrderSlipManager.generateOrderSlip(orderId);
            if (orderSlip != null) {
                printService.displayOrderSlip(orderSlip);
            }

            JOptionPane.showMessageDialog(this, 
                "Order #" + orderId + " placed successfully!\n" +
                "Payment Method: " + paymentMethod + "\n" +
                "Please proceed to counter with your order slip.", 
                "Order Complete", 
                JOptionPane.INFORMATION_MESSAGE);

            clearCart();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to process order", "Order Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int processOrderWithPayment(String paymentMethod) {
        int orderId = -1;
        try {
            System.out.println("Creating order with payment method: " + paymentMethod);

            // Create order
            orderId = OrderManager.createNewOrder(totalPrice, paymentMethod);

            System.out.println("Order creation result: " + orderId);

            if (orderId == -1) {
                JOptionPane.showMessageDialog(this, "Failed to create order in database", "Order Error", JOptionPane.ERROR_MESSAGE);
                return -1;
            }

            // Add order items
            for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                System.out.println("Adding item to order: " + product.getName() + " x " + quantity);
                boolean itemAdded = OrderManager.addOrderItem(orderId, product.getId(), quantity);

                if (!itemAdded) {
                    JOptionPane.showMessageDialog(this, "Failed to add item: " + product.getName(), "Order Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            QueueManager.addToQueue(orderId);
            System.out.println("Order " + orderId + " added to queue. Queue size: " + QueueManager.getQueueSize());

            System.out.println("Order processed successfully: " + orderId);
            return orderId;

        } catch (Exception e) {
            System.out.println("Error processing order: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error processing order: " + e.getMessage(), "Order Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }
    
    private void clearCart() {
        cartItems.clear();
        totalPrice = 0.0;
        updateCartDisplay();
        updateCartAnnouncement();
        CheckOutButton.setEnabled(false); 
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        ProgressBarHeaderPanel = new javax.swing.JPanel();
        OrderingProgressBar = new javax.swing.JProgressBar();
        new_order_progress_image = new javax.swing.JLabel();
        select_order_progress_image = new javax.swing.JLabel();
        confirm_payment_progress_image = new javax.swing.JLabel();
        MainTabbedPane = new javax.swing.JTabbedPane();
        NewOrderPanelTab = new javax.swing.JPanel();
        header4 = new javax.swing.JPanel();
        NewOrderTitleLabel = new javax.swing.JLabel();
        NewOrderContentPanel = new javax.swing.JPanel();
        NewOrder_box = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        NewOrderButton = new javax.swing.JButton();
        footer5 = new javax.swing.JPanel();
        PlaceOrderPanelTab = new javax.swing.JPanel();
        PlaceOrderContentPanel = new javax.swing.JPanel();
        MenuSideBarPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        MenuButton = new javax.swing.JButton();
        SpecialsButton = new javax.swing.JButton();
        CartButton = new javax.swing.JButton();
        GetHelpButton1 = new javax.swing.JButton();
        PlaceOrderTabbedPane = new javax.swing.JTabbedPane();
        MenuPanelTab = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        MainTabTitleLabel1 = new javax.swing.JLabel();
        MenuContentPanel = new javax.swing.JPanel();
        menu_category_scroll_pane1 = new javax.swing.JScrollPane();
        menu_category_box = new javax.swing.JPanel();
        MenuProductDetailBoxPanel = new javax.swing.JPanel();
        ProductQuantityDetailLabel = new javax.swing.JLabel();
        ProductNameLabel = new javax.swing.JLabel();
        MainProductImageLabel = new javax.swing.JLabel();
        ProductAddToCartButton = new javax.swing.JButton();
        ProductQuantitySpinner = new javax.swing.JSpinner();
        ProductPriceDetailLabel = new javax.swing.JLabel();
        ProductAvailabilityDetailLabel = new javax.swing.JLabel();
        ProductDescriptionDetailLabel = new javax.swing.JLabel();
        CartUpdateAnnouncementOffscreenLabel1 = new javax.swing.JLabel();
        footer = new javax.swing.JPanel();
        SpecialsPanelTab = new javax.swing.JPanel();
        header1 = new javax.swing.JPanel();
        SpecialsTabTitleLabel = new javax.swing.JLabel();
        SpecialsContentPanel = new javax.swing.JPanel();
        specials_category_scroll_pane1 = new javax.swing.JScrollPane();
        specials_category_box = new javax.swing.JPanel();
        SpecialsProductDetailBoxPanel = new javax.swing.JPanel();
        SpecialsProductQuantityDetailLabel = new javax.swing.JLabel();
        SpecialsProductNameLabel = new javax.swing.JLabel();
        SpecialsProductImageLabel = new javax.swing.JLabel();
        SpecialsProductAddToCartButton = new javax.swing.JButton();
        SpecialsProductQuantitySpinner = new javax.swing.JSpinner();
        SpecialsProductPriceDetailLabel = new javax.swing.JLabel();
        SpecialsAvailabilityDetailLabel1 = new javax.swing.JLabel();
        SpecialsDescriptionDetailLabel1 = new javax.swing.JLabel();
        footer4 = new javax.swing.JPanel();
        CartUpdateAnnouncementOffscreenLabel2 = new javax.swing.JLabel();
        footer1 = new javax.swing.JPanel();
        CartPanelTab = new javax.swing.JPanel();
        header7 = new javax.swing.JPanel();
        CartTabTitleLabel2 = new javax.swing.JLabel();
        CartContentPanel = new javax.swing.JPanel();
        CartScrollPane = new javax.swing.JScrollPane();
        CartItemsPanel = new javax.swing.JPanel();
        CartemptyLabel = new javax.swing.JLabel();
        CartProductDetailBoxPanel = new javax.swing.JPanel();
        CartProductQuantityDetailLabel = new javax.swing.JLabel();
        CartProductNameLabel = new javax.swing.JLabel();
        CartMainProductImageLabel = new javax.swing.JLabel();
        CartProductDeleteToCartButton = new javax.swing.JButton();
        CartProductQuantitySpinner = new javax.swing.JSpinner();
        CartProductPriceDetailLabel = new javax.swing.JLabel();
        CartItemTotalAmountLabel = new javax.swing.JLabel();
        CartItemAvailabilityDetailLabel = new javax.swing.JLabel();
        CartProductDescriptionDetailLabel = new javax.swing.JLabel();
        CheckOutButton = new javax.swing.JButton();
        TotalPriceLabel = new javax.swing.JLabel();
        TotalPriceNumberLabel = new javax.swing.JLabel();
        footer8 = new javax.swing.JPanel();
        GetHelpPanelTab = new javax.swing.JPanel();
        header3 = new javax.swing.JPanel();
        GetHelpTitleLabel = new javax.swing.JLabel();
        GetHelpContentPanel = new javax.swing.JPanel();
        LanguageChoice = new java.awt.Choice();
        ContentScrollPane = new javax.swing.JScrollPane();
        ContentsJTextArea = new javax.swing.JTextArea();
        footer3 = new javax.swing.JPanel();
        GetHelpSideBarPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        MenuButton3 = new javax.swing.JButton();
        SpecialsButton3 = new javax.swing.JButton();
        CartButton3 = new javax.swing.JButton();
        GetHelpButton4 = new javax.swing.JButton();
        SelectPaymentPanelTab1 = new javax.swing.JPanel();
        header6 = new javax.swing.JPanel();
        NewOrderTitleLabel1 = new javax.swing.JLabel();
        SelectPaymentContentPanel = new javax.swing.JPanel();
        CashPayment_box = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        CashButton = new javax.swing.JButton();
        GCashPayment_box = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        GCashButton = new javax.swing.JButton();
        footer7 = new javax.swing.JPanel();
        ConfirmGCashPaymentPanelTab = new javax.swing.JPanel();
        header8 = new javax.swing.JPanel();
        ConfirmPaymentTitleLabel = new javax.swing.JLabel();
        ConfirmGCashOrderContentPanel = new javax.swing.JPanel();
        ConfirmGCashPayment_box = new javax.swing.JPanel();
        GCashPaymentQRCodeImage = new javax.swing.JLabel();
        ConfirmGCashPaymentButton = new javax.swing.JButton();
        footer9 = new javax.swing.JPanel();
        ConfirmCashPaymentPanelTab = new javax.swing.JPanel();
        header9 = new javax.swing.JPanel();
        ConfirmCashPaymentTitleLabel = new javax.swing.JLabel();
        ConfirmCashOrderContentPanel = new javax.swing.JPanel();
        ConfirmCashPayment_box = new javax.swing.JPanel();
        GCashPaymentQRCodeImage1 = new javax.swing.JLabel();
        ConfirmCashPaymentButton = new javax.swing.JButton();
        footer10 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Kiosk");
        setBackground(new java.awt.Color(201, 177, 158));
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setPreferredSize(new java.awt.Dimension(1000, 650));
        setSize(new java.awt.Dimension(1000, 650));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));
        jPanel2.setMinimumSize(new java.awt.Dimension(1000, 600));
        jPanel2.setPreferredSize(new java.awt.Dimension(1000, 650));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ProgressBarHeaderPanel.setBackground(new java.awt.Color(255, 255, 255));
        ProgressBarHeaderPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ProgressBarHeaderPanel.setPreferredSize(new java.awt.Dimension(1000, 50));
        ProgressBarHeaderPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        OrderingProgressBar.setBackground(new java.awt.Color(255, 255, 255));
        OrderingProgressBar.setForeground(new java.awt.Color(31, 40, 35));
        OrderingProgressBar.setValue(25);
        OrderingProgressBar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        OrderingProgressBar.setPreferredSize(new java.awt.Dimension(800, 15));
        ProgressBarHeaderPanel.add(OrderingProgressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, -1, -1));

        new_order_progress_image.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        new_order_progress_image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/new_cup.png"))); // NOI18N
        new_order_progress_image.setPreferredSize(new java.awt.Dimension(96, 96));
        ProgressBarHeaderPanel.add(new_order_progress_image, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 0, 30, 30));

        select_order_progress_image.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        select_order_progress_image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/select_coffee.png"))); // NOI18N
        select_order_progress_image.setPreferredSize(new java.awt.Dimension(96, 96));
        ProgressBarHeaderPanel.add(select_order_progress_image, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 0, 30, 30));

        confirm_payment_progress_image.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        confirm_payment_progress_image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/confirm_payment.png"))); // NOI18N
        confirm_payment_progress_image.setPreferredSize(new java.awt.Dimension(96, 96));
        ProgressBarHeaderPanel.add(confirm_payment_progress_image, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 0, 30, 30));

        jPanel2.add(ProgressBarHeaderPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        MainTabbedPane.setBackground(new java.awt.Color(201, 177, 158));
        MainTabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        MainTabbedPane.setToolTipText("");
        MainTabbedPane.setPreferredSize(new java.awt.Dimension(850, 600));

        NewOrderPanelTab.setBackground(new java.awt.Color(201, 177, 158));
        NewOrderPanelTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        NewOrderPanelTab.setForeground(new java.awt.Color(31, 40, 35));
        NewOrderPanelTab.setMinimumSize(new java.awt.Dimension(800, 600));
        NewOrderPanelTab.setPreferredSize(new java.awt.Dimension(1000, 600));
        NewOrderPanelTab.setRequestFocusEnabled(false);
        NewOrderPanelTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header4.setBackground(new java.awt.Color(31, 40, 35));
        header4.setPreferredSize(new java.awt.Dimension(1000, 50));
        header4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        NewOrderTitleLabel.setBackground(new java.awt.Color(255, 255, 255));
        NewOrderTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        NewOrderTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        NewOrderTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NewOrderTitleLabel.setText("ORDER YOUR DRINKS HERE!");
        NewOrderTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header4.add(NewOrderTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 50));

        NewOrderPanelTab.add(header4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, -1));

        NewOrderContentPanel.setBackground(new java.awt.Color(255, 255, 255));
        NewOrderContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        NewOrderContentPanel.setPreferredSize(new java.awt.Dimension(780, 500));
        NewOrderContentPanel.setRequestFocusEnabled(false);
        NewOrderContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        NewOrder_box.setBackground(new java.awt.Color(0, 0, 0));
        NewOrder_box.setPreferredSize(new java.awt.Dimension(760, 400));
        NewOrder_box.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/logos/logo1.png"))); // NOI18N
        jLabel3.setText("jLabel1");
        jLabel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 5, true));
        jLabel3.setPreferredSize(new java.awt.Dimension(100, 100));
        NewOrder_box.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, 400, 380));

        NewOrderButton.setBackground(new java.awt.Color(249, 241, 240));
        NewOrderButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NewOrderButton.setText("NEW ORDER");
        NewOrderButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        NewOrderButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        NewOrderButton.setIconTextGap(10);
        NewOrderButton.setPreferredSize(new java.awt.Dimension(200, 100));
        NewOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewOrderButtonActionPerformed(evt);
            }
        });
        NewOrder_box.add(NewOrderButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 400, 260, 70));

        NewOrderContentPanel.add(NewOrder_box, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 760, 480));

        NewOrderPanelTab.add(NewOrderContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 1000, 500));

        footer5.setBackground(new java.awt.Color(31, 40, 35));
        footer5.setPreferredSize(new java.awt.Dimension(990, 50));
        footer5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        NewOrderPanelTab.add(footer5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 1000, 50));

        MainTabbedPane.addTab("tab1", NewOrderPanelTab);

        PlaceOrderPanelTab.setBackground(new java.awt.Color(201, 177, 158));
        PlaceOrderPanelTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        PlaceOrderPanelTab.setForeground(new java.awt.Color(31, 40, 35));
        PlaceOrderPanelTab.setMinimumSize(new java.awt.Dimension(800, 600));
        PlaceOrderPanelTab.setPreferredSize(new java.awt.Dimension(790, 600));
        PlaceOrderPanelTab.setRequestFocusEnabled(false);
        PlaceOrderPanelTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PlaceOrderContentPanel.setBackground(new java.awt.Color(249, 241, 240));
        PlaceOrderContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        PlaceOrderContentPanel.setPreferredSize(new java.awt.Dimension(780, 500));
        PlaceOrderContentPanel.setRequestFocusEnabled(false);
        PlaceOrderContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        MenuSideBarPanel.setBackground(new java.awt.Color(255, 255, 255));
        MenuSideBarPanel.setPreferredSize(new java.awt.Dimension(150, 600));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/logos/smalldonmacwhite.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        jLabel1.setPreferredSize(new java.awt.Dimension(96, 96));
        MenuSideBarPanel.add(jLabel1);

        MenuButton.setBackground(new java.awt.Color(31, 40, 35));
        MenuButton.setForeground(new java.awt.Color(255, 255, 255));
        MenuButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/menu.png"))); // NOI18N
        MenuButton.setText("MENU");
        MenuButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        MenuButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        MenuButton.setIconTextGap(10);
        MenuButton.setPreferredSize(new java.awt.Dimension(200, 100));
        MenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuButtonActionPerformed(evt);
            }
        });
        MenuSideBarPanel.add(MenuButton);

        SpecialsButton.setBackground(new java.awt.Color(31, 40, 35));
        SpecialsButton.setForeground(new java.awt.Color(255, 255, 255));
        SpecialsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/cup.png"))); // NOI18N
        SpecialsButton.setText("BEST SELLING");
        SpecialsButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        SpecialsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        SpecialsButton.setIconTextGap(10);
        SpecialsButton.setPreferredSize(new java.awt.Dimension(200, 100));
        SpecialsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SpecialsButtonActionPerformed(evt);
            }
        });
        MenuSideBarPanel.add(SpecialsButton);

        CartButton.setBackground(new java.awt.Color(31, 40, 35));
        CartButton.setForeground(new java.awt.Color(255, 255, 255));
        CartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/cart.png"))); // NOI18N
        CartButton.setText("CART");
        CartButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CartButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CartButton.setIconTextGap(10);
        CartButton.setPreferredSize(new java.awt.Dimension(200, 100));
        CartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CartButtonActionPerformed(evt);
            }
        });
        MenuSideBarPanel.add(CartButton);

        GetHelpButton1.setBackground(new java.awt.Color(31, 40, 35));
        GetHelpButton1.setForeground(new java.awt.Color(255, 255, 255));
        GetHelpButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/assist.png"))); // NOI18N
        GetHelpButton1.setText("GET HELP");
        GetHelpButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        GetHelpButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        GetHelpButton1.setIconTextGap(10);
        GetHelpButton1.setPreferredSize(new java.awt.Dimension(200, 100));
        GetHelpButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GetHelpButton1ActionPerformed(evt);
            }
        });
        MenuSideBarPanel.add(GetHelpButton1);

        PlaceOrderContentPanel.add(MenuSideBarPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        PlaceOrderTabbedPane.setBackground(new java.awt.Color(201, 177, 158));
        PlaceOrderTabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        PlaceOrderTabbedPane.setToolTipText("");
        PlaceOrderTabbedPane.setPreferredSize(new java.awt.Dimension(850, 600));

        MenuPanelTab.setBackground(new java.awt.Color(201, 177, 158));
        MenuPanelTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        MenuPanelTab.setForeground(new java.awt.Color(31, 40, 35));
        MenuPanelTab.setMinimumSize(new java.awt.Dimension(800, 600));
        MenuPanelTab.setPreferredSize(new java.awt.Dimension(790, 600));
        MenuPanelTab.setRequestFocusEnabled(false);
        MenuPanelTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header.setBackground(new java.awt.Color(31, 40, 35));
        header.setPreferredSize(new java.awt.Dimension(1000, 50));
        header.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        MainTabTitleLabel1.setBackground(new java.awt.Color(249, 241, 240));
        MainTabTitleLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        MainTabTitleLabel1.setForeground(new java.awt.Color(255, 255, 255));
        MainTabTitleLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        MainTabTitleLabel1.setText("MENU");
        MainTabTitleLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header.add(MainTabTitleLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 0, 840, 50));

        MenuPanelTab.add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 850, -1));

        MenuContentPanel.setBackground(new java.awt.Color(249, 241, 240));
        MenuContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        MenuContentPanel.setPreferredSize(new java.awt.Dimension(780, 500));
        MenuContentPanel.setRequestFocusEnabled(false);
        MenuContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menu_category_scroll_pane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(249, 241, 240), 2, true));
        menu_category_scroll_pane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        menu_category_scroll_pane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        menu_category_scroll_pane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        menu_category_scroll_pane1.setPreferredSize(new java.awt.Dimension(760, 400));
        menu_category_scroll_pane1.setRequestFocusEnabled(false);

        menu_category_box.setBackground(new java.awt.Color(31, 40, 35));
        menu_category_box.setMinimumSize(new java.awt.Dimension(760, 400));
        menu_category_box.setPreferredSize(new java.awt.Dimension(760, 400));

        MenuProductDetailBoxPanel.setBackground(new java.awt.Color(249, 241, 240));
        MenuProductDetailBoxPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        MenuProductDetailBoxPanel.setForeground(new java.awt.Color(31, 40, 35));
        MenuProductDetailBoxPanel.setPreferredSize(new java.awt.Dimension(700, 250));
        MenuProductDetailBoxPanel.setRequestFocusEnabled(false);
        MenuProductDetailBoxPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ProductQuantityDetailLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        ProductQuantityDetailLabel.setForeground(new java.awt.Color(66, 133, 244));
        ProductQuantityDetailLabel.setText("Quantity:");
        MenuProductDetailBoxPanel.add(ProductQuantityDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 155, 80, 60));

        ProductNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        ProductNameLabel.setForeground(new java.awt.Color(31, 40, 35));
        ProductNameLabel.setText("Name: ");
        MenuProductDetailBoxPanel.add(ProductNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 500, 30));

        MainProductImageLabel.setBackground(new java.awt.Color(249, 241, 240));
        MainProductImageLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        MainProductImageLabel.setForeground(new java.awt.Color(255, 255, 255));
        MainProductImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        MainProductImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/product_images/default.png"))); // NOI18N
        MainProductImageLabel.setText("PRODUCT DETAILS");
        MainProductImageLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));
        MainProductImageLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        MainProductImageLabel.setMaximumSize(new java.awt.Dimension(150, 150));
        MainProductImageLabel.setMinimumSize(new java.awt.Dimension(150, 150));
        MainProductImageLabel.setName(""); // NOI18N
        MainProductImageLabel.setPreferredSize(new java.awt.Dimension(150, 150));
        MenuProductDetailBoxPanel.add(MainProductImageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        ProductAddToCartButton.setBackground(new java.awt.Color(249, 241, 240));
        ProductAddToCartButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ProductAddToCartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/add.png"))); // NOI18N
        ProductAddToCartButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ProductAddToCartButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ProductAddToCartButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ProductAddToCartButton.setIconTextGap(10);
        ProductAddToCartButton.setPreferredSize(new java.awt.Dimension(64, 64));
        ProductAddToCartButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/pressed_add.png"))); // NOI18N
        ProductAddToCartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductAddToCartButtonActionPerformed(evt);
            }
        });
        MenuProductDetailBoxPanel.add(ProductAddToCartButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 155, -1, -1));

        ProductQuantitySpinner.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        ProductQuantitySpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        ProductQuantitySpinner.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ProductQuantitySpinner.setPreferredSize(new java.awt.Dimension(128, 64));
        MenuProductDetailBoxPanel.add(ProductQuantitySpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 155, -1, -1));

        ProductPriceDetailLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ProductPriceDetailLabel.setForeground(new java.awt.Color(102, 102, 102));
        ProductPriceDetailLabel.setText("Price:");
        MenuProductDetailBoxPanel.add(ProductPriceDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 260, 40));

        ProductAvailabilityDetailLabel.setBackground(new java.awt.Color(42, 168, 83));
        ProductAvailabilityDetailLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ProductAvailabilityDetailLabel.setForeground(new java.awt.Color(42, 168, 83));
        ProductAvailabilityDetailLabel.setText("Available");
        MenuProductDetailBoxPanel.add(ProductAvailabilityDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 170, 80, 30));

        ProductDescriptionDetailLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ProductDescriptionDetailLabel.setForeground(new java.awt.Color(102, 102, 102));
        ProductDescriptionDetailLabel.setText("Description");
        MenuProductDetailBoxPanel.add(ProductDescriptionDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 260, 30));

        menu_category_box.add(MenuProductDetailBoxPanel);

        menu_category_scroll_pane1.setViewportView(menu_category_box);

        MenuContentPanel.add(menu_category_scroll_pane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 800, -1));

        CartUpdateAnnouncementOffscreenLabel1.setBackground(new java.awt.Color(249, 241, 240));
        CartUpdateAnnouncementOffscreenLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CartUpdateAnnouncementOffscreenLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CartUpdateAnnouncementOffscreenLabel1.setText("CART UPDATE ANNOUNCEMENT");
        CartUpdateAnnouncementOffscreenLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        MenuContentPanel.add(CartUpdateAnnouncementOffscreenLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 800, 50));

        MenuPanelTab.add(MenuContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 850, 500));

        footer.setBackground(new java.awt.Color(31, 40, 35));
        footer.setPreferredSize(new java.awt.Dimension(990, 50));
        footer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        MenuPanelTab.add(footer, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 550, 850, 50));

        PlaceOrderTabbedPane.addTab("tab1", MenuPanelTab);

        SpecialsPanelTab.setBackground(new java.awt.Color(201, 177, 158));
        SpecialsPanelTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        SpecialsPanelTab.setForeground(new java.awt.Color(31, 40, 35));
        SpecialsPanelTab.setPreferredSize(new java.awt.Dimension(800, 600));
        SpecialsPanelTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header1.setBackground(new java.awt.Color(31, 40, 35));
        header1.setPreferredSize(new java.awt.Dimension(1000, 50));
        header1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SpecialsTabTitleLabel.setBackground(new java.awt.Color(249, 241, 240));
        SpecialsTabTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        SpecialsTabTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        SpecialsTabTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SpecialsTabTitleLabel.setText("BEST SELLING");
        SpecialsTabTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header1.add(SpecialsTabTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 840, 50));

        SpecialsPanelTab.add(header1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 850, -1));

        SpecialsContentPanel.setBackground(new java.awt.Color(249, 241, 240));
        SpecialsContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        SpecialsContentPanel.setPreferredSize(new java.awt.Dimension(788, 500));
        SpecialsContentPanel.setRequestFocusEnabled(false);
        SpecialsContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        specials_category_scroll_pane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(249, 241, 240), 2, true));
        specials_category_scroll_pane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        specials_category_scroll_pane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        specials_category_scroll_pane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        specials_category_scroll_pane1.setPreferredSize(new java.awt.Dimension(760, 400));
        specials_category_scroll_pane1.setRequestFocusEnabled(false);

        specials_category_box.setBackground(new java.awt.Color(31, 40, 35));
        specials_category_box.setPreferredSize(new java.awt.Dimension(760, 400));

        SpecialsProductDetailBoxPanel.setBackground(new java.awt.Color(249, 241, 240));
        SpecialsProductDetailBoxPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        SpecialsProductDetailBoxPanel.setForeground(new java.awt.Color(31, 40, 35));
        SpecialsProductDetailBoxPanel.setPreferredSize(new java.awt.Dimension(700, 250));
        SpecialsProductDetailBoxPanel.setRequestFocusEnabled(false);
        SpecialsProductDetailBoxPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SpecialsProductQuantityDetailLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        SpecialsProductQuantityDetailLabel.setForeground(new java.awt.Color(66, 133, 244));
        SpecialsProductQuantityDetailLabel.setText("Quantity:");
        SpecialsProductDetailBoxPanel.add(SpecialsProductQuantityDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 155, 80, 60));

        SpecialsProductNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        SpecialsProductNameLabel.setForeground(new java.awt.Color(31, 40, 35));
        SpecialsProductNameLabel.setText("Name: ");
        SpecialsProductDetailBoxPanel.add(SpecialsProductNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 500, 30));

        SpecialsProductImageLabel.setBackground(new java.awt.Color(249, 241, 240));
        SpecialsProductImageLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        SpecialsProductImageLabel.setForeground(new java.awt.Color(255, 255, 255));
        SpecialsProductImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SpecialsProductImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/product_images/default.png"))); // NOI18N
        SpecialsProductImageLabel.setText("PRODUCT DETAILS");
        SpecialsProductImageLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));
        SpecialsProductImageLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        SpecialsProductImageLabel.setMaximumSize(new java.awt.Dimension(150, 150));
        SpecialsProductImageLabel.setMinimumSize(new java.awt.Dimension(150, 150));
        SpecialsProductImageLabel.setPreferredSize(new java.awt.Dimension(150, 150));
        SpecialsProductDetailBoxPanel.add(SpecialsProductImageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        SpecialsProductAddToCartButton.setBackground(new java.awt.Color(249, 241, 240));
        SpecialsProductAddToCartButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        SpecialsProductAddToCartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/add.png"))); // NOI18N
        SpecialsProductAddToCartButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        SpecialsProductAddToCartButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        SpecialsProductAddToCartButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        SpecialsProductAddToCartButton.setIconTextGap(10);
        SpecialsProductAddToCartButton.setPreferredSize(new java.awt.Dimension(64, 64));
        SpecialsProductAddToCartButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/pressed_add.png"))); // NOI18N
        SpecialsProductAddToCartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SpecialsProductAddToCartButtonActionPerformed(evt);
            }
        });
        SpecialsProductDetailBoxPanel.add(SpecialsProductAddToCartButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 155, -1, -1));

        SpecialsProductQuantitySpinner.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        SpecialsProductQuantitySpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        SpecialsProductQuantitySpinner.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        SpecialsProductQuantitySpinner.setPreferredSize(new java.awt.Dimension(128, 64));
        SpecialsProductDetailBoxPanel.add(SpecialsProductQuantitySpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 155, -1, -1));

        SpecialsProductPriceDetailLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        SpecialsProductPriceDetailLabel.setForeground(new java.awt.Color(102, 102, 102));
        SpecialsProductPriceDetailLabel.setText("Price:");
        SpecialsProductDetailBoxPanel.add(SpecialsProductPriceDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 260, 30));

        SpecialsAvailabilityDetailLabel1.setBackground(new java.awt.Color(42, 168, 83));
        SpecialsAvailabilityDetailLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        SpecialsAvailabilityDetailLabel1.setForeground(new java.awt.Color(42, 168, 83));
        SpecialsAvailabilityDetailLabel1.setText("Available");
        SpecialsProductDetailBoxPanel.add(SpecialsAvailabilityDetailLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 165, 80, 30));

        SpecialsDescriptionDetailLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        SpecialsDescriptionDetailLabel1.setForeground(new java.awt.Color(102, 102, 102));
        SpecialsDescriptionDetailLabel1.setText("Description");
        SpecialsProductDetailBoxPanel.add(SpecialsDescriptionDetailLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 260, 30));

        specials_category_box.add(SpecialsProductDetailBoxPanel);

        specials_category_scroll_pane1.setViewportView(specials_category_box);

        SpecialsContentPanel.add(specials_category_scroll_pane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 800, -1));

        footer4.setBackground(new java.awt.Color(121, 63, 26));
        footer4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        footer4.setPreferredSize(new java.awt.Dimension(990, 50));
        footer4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        SpecialsContentPanel.add(footer4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        CartUpdateAnnouncementOffscreenLabel2.setBackground(new java.awt.Color(249, 241, 240));
        CartUpdateAnnouncementOffscreenLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CartUpdateAnnouncementOffscreenLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CartUpdateAnnouncementOffscreenLabel2.setText("CART UPDATE ANNOUNCEMENT");
        CartUpdateAnnouncementOffscreenLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        SpecialsContentPanel.add(CartUpdateAnnouncementOffscreenLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 800, 50));

        SpecialsPanelTab.add(SpecialsContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 850, 500));

        footer1.setBackground(new java.awt.Color(31, 40, 35));
        footer1.setPreferredSize(new java.awt.Dimension(990, 50));
        footer1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        SpecialsPanelTab.add(footer1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 550, 850, 50));

        PlaceOrderTabbedPane.addTab("tab2", SpecialsPanelTab);

        CartPanelTab.setBackground(new java.awt.Color(201, 177, 158));
        CartPanelTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        CartPanelTab.setForeground(new java.awt.Color(31, 40, 35));
        CartPanelTab.setPreferredSize(new java.awt.Dimension(790, 600));
        CartPanelTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header7.setBackground(new java.awt.Color(31, 40, 35));
        header7.setPreferredSize(new java.awt.Dimension(1000, 50));
        header7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CartTabTitleLabel2.setBackground(new java.awt.Color(249, 241, 240));
        CartTabTitleLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CartTabTitleLabel2.setForeground(new java.awt.Color(255, 255, 255));
        CartTabTitleLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CartTabTitleLabel2.setText("CART");
        CartTabTitleLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header7.add(CartTabTitleLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 850, 50));

        CartPanelTab.add(header7, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 850, -1));

        CartContentPanel.setBackground(new java.awt.Color(249, 241, 240));
        CartContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        CartContentPanel.setRequestFocusEnabled(false);
        CartContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CartScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        CartScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        CartScrollPane.setPreferredSize(new java.awt.Dimension(730, 400));

        CartItemsPanel.setBackground(new java.awt.Color(31, 40, 35));
        CartItemsPanel.setForeground(new java.awt.Color(31, 40, 35));
        CartItemsPanel.setPreferredSize(new java.awt.Dimension(730, 400));

        CartemptyLabel.setBackground(new java.awt.Color(100, 100, 100));
        CartemptyLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        CartemptyLabel.setForeground(new java.awt.Color(100, 100, 100));
        CartemptyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CartemptyLabel.setText("Your cart is empty");
        CartemptyLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        CartItemsPanel.add(CartemptyLabel);

        CartProductDetailBoxPanel.setBackground(new java.awt.Color(249, 241, 240));
        CartProductDetailBoxPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        CartProductDetailBoxPanel.setForeground(new java.awt.Color(31, 40, 35));
        CartProductDetailBoxPanel.setPreferredSize(new java.awt.Dimension(700, 250));
        CartProductDetailBoxPanel.setRequestFocusEnabled(false);
        CartProductDetailBoxPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CartProductQuantityDetailLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        CartProductQuantityDetailLabel.setForeground(new java.awt.Color(66, 133, 244));
        CartProductQuantityDetailLabel.setText("Quantity:");
        CartProductDetailBoxPanel.add(CartProductQuantityDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 155, 80, 60));

        CartProductNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CartProductNameLabel.setForeground(new java.awt.Color(31, 40, 35));
        CartProductNameLabel.setText("Name: ");
        CartProductDetailBoxPanel.add(CartProductNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 500, 30));

        CartMainProductImageLabel.setBackground(new java.awt.Color(249, 241, 240));
        CartMainProductImageLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        CartMainProductImageLabel.setForeground(new java.awt.Color(255, 255, 255));
        CartMainProductImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CartMainProductImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/product_images/default.png"))); // NOI18N
        CartMainProductImageLabel.setText("PRODUCT DETAILS");
        CartMainProductImageLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));
        CartMainProductImageLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        CartMainProductImageLabel.setMaximumSize(new java.awt.Dimension(150, 150));
        CartMainProductImageLabel.setMinimumSize(new java.awt.Dimension(150, 150));
        CartMainProductImageLabel.setPreferredSize(new java.awt.Dimension(150, 150));
        CartProductDetailBoxPanel.add(CartMainProductImageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        CartProductDeleteToCartButton.setBackground(new java.awt.Color(249, 241, 240));
        CartProductDeleteToCartButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        CartProductDeleteToCartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/delete.png"))); // NOI18N
        CartProductDeleteToCartButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CartProductDeleteToCartButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CartProductDeleteToCartButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        CartProductDeleteToCartButton.setIconTextGap(10);
        CartProductDeleteToCartButton.setPreferredSize(new java.awt.Dimension(64, 64));
        CartProductDeleteToCartButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/pressed_delete.png"))); // NOI18N
        CartProductDeleteToCartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CartProductDeleteToCartButtonActionPerformed(evt);
            }
        });
        CartProductDetailBoxPanel.add(CartProductDeleteToCartButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 155, -1, -1));

        CartProductQuantitySpinner.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        CartProductQuantitySpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        CartProductQuantitySpinner.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CartProductQuantitySpinner.setPreferredSize(new java.awt.Dimension(128, 64));
        CartProductDetailBoxPanel.add(CartProductQuantitySpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 155, -1, -1));

        CartProductPriceDetailLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        CartProductPriceDetailLabel.setForeground(new java.awt.Color(102, 102, 102));
        CartProductPriceDetailLabel.setText("Price:");
        CartProductDetailBoxPanel.add(CartProductPriceDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 260, 30));

        CartItemTotalAmountLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        CartItemTotalAmountLabel.setForeground(new java.awt.Color(31, 40, 35));
        CartItemTotalAmountLabel.setText("Total: ");
        CartProductDetailBoxPanel.add(CartItemTotalAmountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 120, 130, 30));

        CartItemAvailabilityDetailLabel.setBackground(new java.awt.Color(42, 168, 83));
        CartItemAvailabilityDetailLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        CartItemAvailabilityDetailLabel.setForeground(new java.awt.Color(42, 168, 83));
        CartItemAvailabilityDetailLabel.setText("Available");
        CartProductDetailBoxPanel.add(CartItemAvailabilityDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 170, 80, 30));

        CartProductDescriptionDetailLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        CartProductDescriptionDetailLabel.setForeground(new java.awt.Color(102, 102, 102));
        CartProductDescriptionDetailLabel.setText("Description");
        CartProductDetailBoxPanel.add(CartProductDescriptionDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 260, 30));

        CartItemsPanel.add(CartProductDetailBoxPanel);

        CartScrollPane.setViewportView(CartItemsPanel);

        CartContentPanel.add(CartScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 800, 400));

        CheckOutButton.setBackground(new java.awt.Color(31, 40, 35));
        CheckOutButton.setForeground(new java.awt.Color(255, 255, 255));
        CheckOutButton.setText("CHECKOUT");
        CheckOutButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CheckOutButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CheckOutButton.setIconTextGap(1);
        CheckOutButton.setPreferredSize(new java.awt.Dimension(200, 40));
        CheckOutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CheckOutButtonActionPerformed(evt);
            }
        });
        CartContentPanel.add(CheckOutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 450, -1, -1));

        TotalPriceLabel.setBackground(new java.awt.Color(249, 241, 240));
        TotalPriceLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TotalPriceLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalPriceLabel.setText("Total Price:");
        TotalPriceLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TotalPriceLabel.setPreferredSize(new java.awt.Dimension(150, 40));
        CartContentPanel.add(TotalPriceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, 130, -1));

        TotalPriceNumberLabel.setBackground(new java.awt.Color(249, 241, 240));
        TotalPriceNumberLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TotalPriceNumberLabel.setText("₱0.00");
        TotalPriceNumberLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TotalPriceNumberLabel.setPreferredSize(new java.awt.Dimension(150, 40));
        CartContentPanel.add(TotalPriceNumberLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 450, 140, -1));

        CartPanelTab.add(CartContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 850, 500));

        footer8.setBackground(new java.awt.Color(31, 40, 35));
        footer8.setPreferredSize(new java.awt.Dimension(990, 50));
        footer8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        CartPanelTab.add(footer8, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 550, 850, 50));

        PlaceOrderTabbedPane.addTab("tab5", CartPanelTab);

        GetHelpPanelTab.setBackground(new java.awt.Color(201, 177, 158));
        GetHelpPanelTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        GetHelpPanelTab.setForeground(new java.awt.Color(31, 40, 35));
        GetHelpPanelTab.setPreferredSize(new java.awt.Dimension(780, 850));
        GetHelpPanelTab.setRequestFocusEnabled(false);
        GetHelpPanelTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header3.setBackground(new java.awt.Color(31, 40, 35));
        header3.setPreferredSize(new java.awt.Dimension(1000, 50));
        header3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        GetHelpTitleLabel.setBackground(new java.awt.Color(249, 241, 240));
        GetHelpTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        GetHelpTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        GetHelpTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        GetHelpTitleLabel.setText("HELP & FAQ");
        GetHelpTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header3.add(GetHelpTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 840, 50));

        GetHelpPanelTab.add(header3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 850, -1));

        GetHelpContentPanel.setBackground(new java.awt.Color(249, 241, 240));
        GetHelpContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        GetHelpContentPanel.setRequestFocusEnabled(false);
        GetHelpContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LanguageChoice.setBackground(new java.awt.Color(249, 241, 240));
        LanguageChoice.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        LanguageChoice.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        LanguageChoice.setForeground(new java.awt.Color(31, 40, 35));
        LanguageChoice.setName("language"); // NOI18N
        LanguageChoice.setPreferredSize(new java.awt.Dimension(100, 20));
        GetHelpContentPanel.add(LanguageChoice, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 10, -1, -1));

        ContentsJTextArea.setEditable(false);
        ContentsJTextArea.setBackground(new java.awt.Color(249, 241, 240));
        ContentsJTextArea.setColumns(20);
        ContentsJTextArea.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ContentsJTextArea.setForeground(new java.awt.Color(31, 40, 35));
        ContentsJTextArea.setLineWrap(true);
        ContentsJTextArea.setRows(5);
        ContentsJTextArea.setText("HELP & FREQUENTLY ASKED QUESTIONS\n\nHOW TO PLACE AN ORDER:\n1. Click 'NEW ORDER' on the main screen\n2. Browse our MENU or BEST SELLING items\n3. Select quantity and click 'ADD TO CART'\n4. Review your cart and click 'CHECKOUT'\n5. Choose your payment method\n6. Receive your order slip\n\nPAYMENT METHODS:\n• CASH - Pay with cash at the counter\n• GCASH - Digital payment via QR code\n\nPRODUCT AVAILABILITY:\n• Green 'Available' = Product is in stock\n• Red 'Not Available' = Temporarily out of stock\n\nFREQUENTLY ASKED QUESTIONS:\nQ: Can I customize my drink (extra sugar, less ice, etc.)?\nA: We're sorry, but our kiosk does not support drink customizations.\nAll our beverages are prepared as signature drinks with carefully crafted recipes to ensure consistent quality and taste.\n\nQ: Can I modify my order after payment?\nA: Please speak with our staff for modifications.\n\nQ: What if a product is out of stock?\nA: Unavailable items cannot be added to cart.\n\nNEED MORE HELP?\nPlease approach our staff for assistance.\nWe're here to make your experience great!");
        ContentsJTextArea.setWrapStyleWord(true);
        ContentsJTextArea.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ContentsJTextArea.setMargin(new java.awt.Insets(10, 10, 10, 10));
        ContentScrollPane.setViewportView(ContentsJTextArea);

        GetHelpContentPanel.add(ContentScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 850, 500));

        GetHelpPanelTab.add(GetHelpContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 850, 500));

        footer3.setBackground(new java.awt.Color(31, 40, 35));
        footer3.setPreferredSize(new java.awt.Dimension(990, 50));
        footer3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        GetHelpPanelTab.add(footer3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 550, 850, 50));

        GetHelpSideBarPanel.setBackground(new java.awt.Color(255, 255, 255));
        GetHelpSideBarPanel.setPreferredSize(new java.awt.Dimension(150, 600));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/logos/smalldonmacwhite.png"))); // NOI18N
        jLabel4.setText("jLabel1");
        jLabel4.setPreferredSize(new java.awt.Dimension(96, 96));
        GetHelpSideBarPanel.add(jLabel4);

        MenuButton3.setBackground(new java.awt.Color(31, 40, 35));
        MenuButton3.setForeground(new java.awt.Color(255, 255, 255));
        MenuButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/menu.png"))); // NOI18N
        MenuButton3.setText("MENU");
        MenuButton3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        MenuButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        MenuButton3.setIconTextGap(10);
        MenuButton3.setPreferredSize(new java.awt.Dimension(200, 75));
        MenuButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuButton3ActionPerformed(evt);
            }
        });
        GetHelpSideBarPanel.add(MenuButton3);

        SpecialsButton3.setBackground(new java.awt.Color(31, 40, 35));
        SpecialsButton3.setForeground(new java.awt.Color(255, 255, 255));
        SpecialsButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/cup.png"))); // NOI18N
        SpecialsButton3.setText("BEST SELLING");
        SpecialsButton3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        SpecialsButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        SpecialsButton3.setIconTextGap(10);
        SpecialsButton3.setPreferredSize(new java.awt.Dimension(200, 75));
        SpecialsButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SpecialsButton3ActionPerformed(evt);
            }
        });
        GetHelpSideBarPanel.add(SpecialsButton3);

        CartButton3.setBackground(new java.awt.Color(31, 40, 35));
        CartButton3.setForeground(new java.awt.Color(255, 255, 255));
        CartButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/cart.png"))); // NOI18N
        CartButton3.setText("CART");
        CartButton3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CartButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CartButton3.setIconTextGap(10);
        CartButton3.setPreferredSize(new java.awt.Dimension(200, 75));
        CartButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CartButton3ActionPerformed(evt);
            }
        });
        GetHelpSideBarPanel.add(CartButton3);

        GetHelpButton4.setBackground(new java.awt.Color(31, 40, 35));
        GetHelpButton4.setForeground(new java.awt.Color(255, 255, 255));
        GetHelpButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/assist.png"))); // NOI18N
        GetHelpButton4.setText("GET HELP");
        GetHelpButton4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        GetHelpButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        GetHelpButton4.setIconTextGap(10);
        GetHelpButton4.setPreferredSize(new java.awt.Dimension(200, 75));
        GetHelpButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GetHelpButton4ActionPerformed(evt);
            }
        });
        GetHelpSideBarPanel.add(GetHelpButton4);

        GetHelpPanelTab.add(GetHelpSideBarPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        PlaceOrderTabbedPane.addTab("tab4", GetHelpPanelTab);

        PlaceOrderContentPanel.add(PlaceOrderTabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(-50, 0, 1050, 600));

        PlaceOrderPanelTab.add(PlaceOrderContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 600));

        MainTabbedPane.addTab("tab1", PlaceOrderPanelTab);

        SelectPaymentPanelTab1.setBackground(new java.awt.Color(201, 177, 158));
        SelectPaymentPanelTab1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        SelectPaymentPanelTab1.setForeground(new java.awt.Color(31, 40, 35));
        SelectPaymentPanelTab1.setMinimumSize(new java.awt.Dimension(800, 600));
        SelectPaymentPanelTab1.setPreferredSize(new java.awt.Dimension(790, 600));
        SelectPaymentPanelTab1.setRequestFocusEnabled(false);
        SelectPaymentPanelTab1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header6.setBackground(new java.awt.Color(31, 40, 35));
        header6.setPreferredSize(new java.awt.Dimension(1000, 50));
        header6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        NewOrderTitleLabel1.setBackground(new java.awt.Color(249, 241, 240));
        NewOrderTitleLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        NewOrderTitleLabel1.setForeground(new java.awt.Color(255, 255, 255));
        NewOrderTitleLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NewOrderTitleLabel1.setText("PAYMENT OPTION");
        NewOrderTitleLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header6.add(NewOrderTitleLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 990, 50));

        SelectPaymentPanelTab1.add(header6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, -1));

        SelectPaymentContentPanel.setBackground(new java.awt.Color(255, 255, 255));
        SelectPaymentContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        SelectPaymentContentPanel.setPreferredSize(new java.awt.Dimension(780, 500));
        SelectPaymentContentPanel.setRequestFocusEnabled(false);
        SelectPaymentContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CashPayment_box.setBackground(new java.awt.Color(0, 0, 0));
        CashPayment_box.setPreferredSize(new java.awt.Dimension(760, 400));
        CashPayment_box.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/cash-payment.png"))); // NOI18N
        jLabel5.setToolTipText("");
        jLabel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 5, true));
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel5.setPreferredSize(new java.awt.Dimension(100, 100));
        CashPayment_box.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 400, 380));

        CashButton.setBackground(new java.awt.Color(249, 241, 240));
        CashButton.setText(" CASH PAYMENT");
        CashButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CashButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CashButton.setIconTextGap(10);
        CashButton.setPreferredSize(new java.awt.Dimension(200, 100));
        CashButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CashButtonActionPerformed(evt);
            }
        });
        CashPayment_box.add(CashButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 400, 260, 70));

        SelectPaymentContentPanel.add(CashPayment_box, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 440, 480));

        GCashPayment_box.setBackground(new java.awt.Color(0, 0, 0));
        GCashPayment_box.setPreferredSize(new java.awt.Dimension(760, 400));
        GCashPayment_box.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/cashless-payment.png"))); // NOI18N
        jLabel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 5, true));
        jLabel6.setPreferredSize(new java.awt.Dimension(100, 100));
        GCashPayment_box.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 400, 380));

        GCashButton.setBackground(new java.awt.Color(249, 241, 240));
        GCashButton.setText("GCASH PAYMENT");
        GCashButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        GCashButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        GCashButton.setIconTextGap(10);
        GCashButton.setPreferredSize(new java.awt.Dimension(200, 100));
        GCashButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GCashButtonActionPerformed(evt);
            }
        });
        GCashPayment_box.add(GCashButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 400, 260, 70));

        SelectPaymentContentPanel.add(GCashPayment_box, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, 440, 480));

        SelectPaymentPanelTab1.add(SelectPaymentContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 1000, 500));

        footer7.setBackground(new java.awt.Color(31, 40, 35));
        footer7.setPreferredSize(new java.awt.Dimension(990, 50));
        footer7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        SelectPaymentPanelTab1.add(footer7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 1000, 50));

        MainTabbedPane.addTab("tab1", SelectPaymentPanelTab1);

        ConfirmGCashPaymentPanelTab.setBackground(new java.awt.Color(201, 177, 158));
        ConfirmGCashPaymentPanelTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ConfirmGCashPaymentPanelTab.setForeground(new java.awt.Color(31, 40, 35));
        ConfirmGCashPaymentPanelTab.setMinimumSize(new java.awt.Dimension(800, 600));
        ConfirmGCashPaymentPanelTab.setPreferredSize(new java.awt.Dimension(790, 600));
        ConfirmGCashPaymentPanelTab.setRequestFocusEnabled(false);
        ConfirmGCashPaymentPanelTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header8.setBackground(new java.awt.Color(31, 40, 35));
        header8.setPreferredSize(new java.awt.Dimension(1000, 50));
        header8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ConfirmPaymentTitleLabel.setBackground(new java.awt.Color(249, 241, 240));
        ConfirmPaymentTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        ConfirmPaymentTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        ConfirmPaymentTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ConfirmPaymentTitleLabel.setText("GCASH PAYMENT");
        ConfirmPaymentTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header8.add(ConfirmPaymentTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 980, 50));

        ConfirmGCashPaymentPanelTab.add(header8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, -1));

        ConfirmGCashOrderContentPanel.setBackground(new java.awt.Color(249, 241, 240));
        ConfirmGCashOrderContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        ConfirmGCashOrderContentPanel.setPreferredSize(new java.awt.Dimension(780, 500));
        ConfirmGCashOrderContentPanel.setRequestFocusEnabled(false);
        ConfirmGCashOrderContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ConfirmGCashPayment_box.setBackground(new java.awt.Color(31, 40, 35));
        ConfirmGCashPayment_box.setPreferredSize(new java.awt.Dimension(760, 400));
        ConfirmGCashPayment_box.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        GCashPaymentQRCodeImage.setBackground(new java.awt.Color(31, 40, 35));
        GCashPaymentQRCodeImage.setForeground(new java.awt.Color(255, 255, 255));
        GCashPaymentQRCodeImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        GCashPaymentQRCodeImage.setText("QR CODE IMAGE");
        GCashPaymentQRCodeImage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        GCashPaymentQRCodeImage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        GCashPaymentQRCodeImage.setPreferredSize(new java.awt.Dimension(100, 100));
        ConfirmGCashPayment_box.add(GCashPaymentQRCodeImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 400, 380));

        ConfirmGCashPaymentButton.setBackground(new java.awt.Color(249, 241, 240));
        ConfirmGCashPaymentButton.setText("CONFIRM");
        ConfirmGCashPaymentButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ConfirmGCashPaymentButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ConfirmGCashPaymentButton.setIconTextGap(10);
        ConfirmGCashPaymentButton.setPreferredSize(new java.awt.Dimension(200, 100));
        ConfirmGCashPaymentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConfirmGCashPaymentButtonActionPerformed(evt);
            }
        });
        ConfirmGCashPayment_box.add(ConfirmGCashPaymentButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 400, 260, 70));

        ConfirmGCashOrderContentPanel.add(ConfirmGCashPayment_box, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, 440, 480));

        ConfirmGCashPaymentPanelTab.add(ConfirmGCashOrderContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 1000, 500));

        footer9.setBackground(new java.awt.Color(31, 40, 35));
        footer9.setPreferredSize(new java.awt.Dimension(990, 50));
        footer9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        ConfirmGCashPaymentPanelTab.add(footer9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 1000, 50));

        MainTabbedPane.addTab("tab1", ConfirmGCashPaymentPanelTab);

        ConfirmCashPaymentPanelTab.setBackground(new java.awt.Color(201, 177, 158));
        ConfirmCashPaymentPanelTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ConfirmCashPaymentPanelTab.setForeground(new java.awt.Color(31, 40, 35));
        ConfirmCashPaymentPanelTab.setMinimumSize(new java.awt.Dimension(800, 600));
        ConfirmCashPaymentPanelTab.setPreferredSize(new java.awt.Dimension(790, 600));
        ConfirmCashPaymentPanelTab.setRequestFocusEnabled(false);
        ConfirmCashPaymentPanelTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header9.setBackground(new java.awt.Color(31, 40, 35));
        header9.setPreferredSize(new java.awt.Dimension(1000, 50));
        header9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ConfirmCashPaymentTitleLabel.setBackground(new java.awt.Color(249, 241, 240));
        ConfirmCashPaymentTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        ConfirmCashPaymentTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        ConfirmCashPaymentTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ConfirmCashPaymentTitleLabel.setText("CASH PAYMENT");
        ConfirmCashPaymentTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header9.add(ConfirmCashPaymentTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 980, 50));

        ConfirmCashPaymentPanelTab.add(header9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, -1));

        ConfirmCashOrderContentPanel.setBackground(new java.awt.Color(249, 241, 240));
        ConfirmCashOrderContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        ConfirmCashOrderContentPanel.setPreferredSize(new java.awt.Dimension(780, 500));
        ConfirmCashOrderContentPanel.setRequestFocusEnabled(false);
        ConfirmCashOrderContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ConfirmCashPayment_box.setBackground(new java.awt.Color(31, 40, 35));
        ConfirmCashPayment_box.setPreferredSize(new java.awt.Dimension(760, 400));
        ConfirmCashPayment_box.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        GCashPaymentQRCodeImage1.setBackground(new java.awt.Color(31, 40, 35));
        GCashPaymentQRCodeImage1.setForeground(new java.awt.Color(255, 255, 255));
        GCashPaymentQRCodeImage1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        GCashPaymentQRCodeImage1.setText("PROCEED TO COUNTER");
        GCashPaymentQRCodeImage1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        GCashPaymentQRCodeImage1.setPreferredSize(new java.awt.Dimension(100, 100));
        ConfirmCashPayment_box.add(GCashPaymentQRCodeImage1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 400, 380));

        ConfirmCashPaymentButton.setBackground(new java.awt.Color(249, 241, 240));
        ConfirmCashPaymentButton.setText("CONFIRM");
        ConfirmCashPaymentButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ConfirmCashPaymentButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ConfirmCashPaymentButton.setIconTextGap(10);
        ConfirmCashPaymentButton.setPreferredSize(new java.awt.Dimension(200, 100));
        ConfirmCashPaymentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConfirmCashPaymentButtonActionPerformed(evt);
            }
        });
        ConfirmCashPayment_box.add(ConfirmCashPaymentButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 400, 260, 70));

        ConfirmCashOrderContentPanel.add(ConfirmCashPayment_box, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, 440, 480));

        ConfirmCashPaymentPanelTab.add(ConfirmCashOrderContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 1000, 500));

        footer10.setBackground(new java.awt.Color(31, 40, 35));
        footer10.setPreferredSize(new java.awt.Dimension(990, 50));
        footer10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        ConfirmCashPaymentPanelTab.add(footer10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 1000, 50));

        MainTabbedPane.addTab("tab1", ConfirmCashPaymentPanelTab);

        jPanel2.add(MainTabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(-50, 50, 1050, 650));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getAccessibleContext().setAccessibleName("KIOSK");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void GetHelpButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GetHelpButton1ActionPerformed
        PlaceOrderTabbedPane.setSelectedIndex(3);
        updateProductDisplays();
        
        String selectedLanguage = LanguageChoice.getSelectedItem();
        updateHelpContent(selectedLanguage);
    }//GEN-LAST:event_GetHelpButton1ActionPerformed

    private void CartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CartButtonActionPerformed
        PlaceOrderTabbedPane.setSelectedIndex(2);
        updateProductDisplays();
    }//GEN-LAST:event_CartButtonActionPerformed

    private void MenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuButtonActionPerformed
        PlaceOrderTabbedPane.setSelectedIndex(0);
        updateProductDisplays();
    }//GEN-LAST:event_MenuButtonActionPerformed

    private void SpecialsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SpecialsButtonActionPerformed
        PlaceOrderTabbedPane.setSelectedIndex(1);
        updateProductDisplays();
    }//GEN-LAST:event_SpecialsButtonActionPerformed

    private void SpecialsProductAddToCartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SpecialsProductAddToCartButtonActionPerformed
        
    }//GEN-LAST:event_SpecialsProductAddToCartButtonActionPerformed

    private void ProductAddToCartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductAddToCartButtonActionPerformed
        
    }//GEN-LAST:event_ProductAddToCartButtonActionPerformed

    private void MenuButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuButton3ActionPerformed
        
    }//GEN-LAST:event_MenuButton3ActionPerformed

    private void SpecialsButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SpecialsButton3ActionPerformed
        
    }//GEN-LAST:event_SpecialsButton3ActionPerformed

    private void CartButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CartButton3ActionPerformed
        
    }//GEN-LAST:event_CartButton3ActionPerformed

    private void GetHelpButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GetHelpButton4ActionPerformed
        
    }//GEN-LAST:event_GetHelpButton4ActionPerformed

    private void CartProductDeleteToCartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CartProductDeleteToCartButtonActionPerformed
        
    }//GEN-LAST:event_CartProductDeleteToCartButtonActionPerformed

    private void CheckOutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckOutButtonActionPerformed
        checkout();
    }//GEN-LAST:event_CheckOutButtonActionPerformed

    private void NewOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewOrderButtonActionPerformed
        MainTabbedPane.setSelectedIndex(1);
        updateProductDisplays();
    }//GEN-LAST:event_NewOrderButtonActionPerformed

    private void CashButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CashButtonActionPerformed
        MainTabbedPane.setSelectedIndex(4);
        updateProductDisplays();
    }//GEN-LAST:event_CashButtonActionPerformed

    private void GCashButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GCashButtonActionPerformed
        MainTabbedPane.setSelectedIndex(3);
        updateProductDisplays();
    }//GEN-LAST:event_GCashButtonActionPerformed

    private void ConfirmGCashPaymentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConfirmGCashPaymentButtonActionPerformed
        checkoutWithPayment("GCash");
        MainTabbedPane.setSelectedIndex(0);
        PlaceOrderTabbedPane.setSelectedIndex(0);
        updateProductDisplays();
    }//GEN-LAST:event_ConfirmGCashPaymentButtonActionPerformed

    private void ConfirmCashPaymentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConfirmCashPaymentButtonActionPerformed
        checkoutWithPayment("Cash");
        MainTabbedPane.setSelectedIndex(0);
        PlaceOrderTabbedPane.setSelectedIndex(0);
        updateProductDisplays();
    }//GEN-LAST:event_ConfirmCashPaymentButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CartButton;
    private javax.swing.JButton CartButton3;
    private javax.swing.JPanel CartContentPanel;
    private javax.swing.JLabel CartItemAvailabilityDetailLabel;
    private javax.swing.JLabel CartItemTotalAmountLabel;
    private javax.swing.JPanel CartItemsPanel;
    private javax.swing.JLabel CartMainProductImageLabel;
    private javax.swing.JPanel CartPanelTab;
    private javax.swing.JButton CartProductDeleteToCartButton;
    private javax.swing.JLabel CartProductDescriptionDetailLabel;
    private javax.swing.JPanel CartProductDetailBoxPanel;
    private javax.swing.JLabel CartProductNameLabel;
    private javax.swing.JLabel CartProductPriceDetailLabel;
    private javax.swing.JLabel CartProductQuantityDetailLabel;
    private javax.swing.JSpinner CartProductQuantitySpinner;
    private javax.swing.JScrollPane CartScrollPane;
    private javax.swing.JLabel CartTabTitleLabel2;
    private javax.swing.JLabel CartUpdateAnnouncementOffscreenLabel1;
    private javax.swing.JLabel CartUpdateAnnouncementOffscreenLabel2;
    private javax.swing.JLabel CartemptyLabel;
    private javax.swing.JButton CashButton;
    private javax.swing.JPanel CashPayment_box;
    private javax.swing.JButton CheckOutButton;
    private javax.swing.JPanel ConfirmCashOrderContentPanel;
    private javax.swing.JButton ConfirmCashPaymentButton;
    private javax.swing.JPanel ConfirmCashPaymentPanelTab;
    private javax.swing.JLabel ConfirmCashPaymentTitleLabel;
    private javax.swing.JPanel ConfirmCashPayment_box;
    private javax.swing.JPanel ConfirmGCashOrderContentPanel;
    private javax.swing.JButton ConfirmGCashPaymentButton;
    private javax.swing.JPanel ConfirmGCashPaymentPanelTab;
    private javax.swing.JPanel ConfirmGCashPayment_box;
    private javax.swing.JLabel ConfirmPaymentTitleLabel;
    private javax.swing.JScrollPane ContentScrollPane;
    private javax.swing.JTextArea ContentsJTextArea;
    private javax.swing.JButton GCashButton;
    private javax.swing.JLabel GCashPaymentQRCodeImage;
    private javax.swing.JLabel GCashPaymentQRCodeImage1;
    private javax.swing.JPanel GCashPayment_box;
    private javax.swing.JButton GetHelpButton1;
    private javax.swing.JButton GetHelpButton4;
    private javax.swing.JPanel GetHelpContentPanel;
    private javax.swing.JPanel GetHelpPanelTab;
    private javax.swing.JPanel GetHelpSideBarPanel;
    private javax.swing.JLabel GetHelpTitleLabel;
    private java.awt.Choice LanguageChoice;
    private javax.swing.JLabel MainProductImageLabel;
    private javax.swing.JLabel MainTabTitleLabel1;
    private javax.swing.JTabbedPane MainTabbedPane;
    private javax.swing.JButton MenuButton;
    private javax.swing.JButton MenuButton3;
    private javax.swing.JPanel MenuContentPanel;
    private javax.swing.JPanel MenuPanelTab;
    private javax.swing.JPanel MenuProductDetailBoxPanel;
    private javax.swing.JPanel MenuSideBarPanel;
    private javax.swing.JButton NewOrderButton;
    private javax.swing.JPanel NewOrderContentPanel;
    private javax.swing.JPanel NewOrderPanelTab;
    private javax.swing.JLabel NewOrderTitleLabel;
    private javax.swing.JLabel NewOrderTitleLabel1;
    private javax.swing.JPanel NewOrder_box;
    private javax.swing.JProgressBar OrderingProgressBar;
    private javax.swing.JPanel PlaceOrderContentPanel;
    private javax.swing.JPanel PlaceOrderPanelTab;
    private javax.swing.JTabbedPane PlaceOrderTabbedPane;
    private javax.swing.JButton ProductAddToCartButton;
    private javax.swing.JLabel ProductAvailabilityDetailLabel;
    private javax.swing.JLabel ProductDescriptionDetailLabel;
    private javax.swing.JLabel ProductNameLabel;
    private javax.swing.JLabel ProductPriceDetailLabel;
    private javax.swing.JLabel ProductQuantityDetailLabel;
    private javax.swing.JSpinner ProductQuantitySpinner;
    private javax.swing.JPanel ProgressBarHeaderPanel;
    private javax.swing.JPanel SelectPaymentContentPanel;
    private javax.swing.JPanel SelectPaymentPanelTab1;
    private javax.swing.JLabel SpecialsAvailabilityDetailLabel1;
    private javax.swing.JButton SpecialsButton;
    private javax.swing.JButton SpecialsButton3;
    private javax.swing.JPanel SpecialsContentPanel;
    private javax.swing.JLabel SpecialsDescriptionDetailLabel1;
    private javax.swing.JPanel SpecialsPanelTab;
    private javax.swing.JButton SpecialsProductAddToCartButton;
    private javax.swing.JPanel SpecialsProductDetailBoxPanel;
    private javax.swing.JLabel SpecialsProductImageLabel;
    private javax.swing.JLabel SpecialsProductNameLabel;
    private javax.swing.JLabel SpecialsProductPriceDetailLabel;
    private javax.swing.JLabel SpecialsProductQuantityDetailLabel;
    private javax.swing.JSpinner SpecialsProductQuantitySpinner;
    private javax.swing.JLabel SpecialsTabTitleLabel;
    private javax.swing.JLabel TotalPriceLabel;
    private javax.swing.JLabel TotalPriceNumberLabel;
    private javax.swing.JLabel confirm_payment_progress_image;
    private javax.swing.JPanel footer;
    private javax.swing.JPanel footer1;
    private javax.swing.JPanel footer10;
    private javax.swing.JPanel footer3;
    private javax.swing.JPanel footer4;
    private javax.swing.JPanel footer5;
    private javax.swing.JPanel footer7;
    private javax.swing.JPanel footer8;
    private javax.swing.JPanel footer9;
    private javax.swing.JPanel header;
    private javax.swing.JPanel header1;
    private javax.swing.JPanel header3;
    private javax.swing.JPanel header4;
    private javax.swing.JPanel header6;
    private javax.swing.JPanel header7;
    private javax.swing.JPanel header8;
    private javax.swing.JPanel header9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel menu_category_box;
    private javax.swing.JScrollPane menu_category_scroll_pane1;
    private javax.swing.JLabel new_order_progress_image;
    private javax.swing.JLabel select_order_progress_image;
    private javax.swing.JPanel specials_category_box;
    private javax.swing.JScrollPane specials_category_scroll_pane1;
    // End of variables declaration//GEN-END:variables
}

