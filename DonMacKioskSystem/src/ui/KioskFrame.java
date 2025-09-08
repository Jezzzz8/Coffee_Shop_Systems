package ui;

import backend.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import backend.SalesReportManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class KioskFrame extends javax.swing.JFrame {

    private List<Product> menuProducts;
    private List<Product> specialsProducts;
    private Map<Product, Integer> cartItems;
    private double totalPrice;
    private Customer selectedCustomer;
    private Map<Integer, Integer> productStockMap; // Map to store product ID to stock quantity
    private PrintService printService;
    private int lastOrderId = -1;

    public KioskFrame() {
        initComponents();
        cartItems = new HashMap<>();
        totalPrice = 0.0;
        selectedCustomer = null;
        productStockMap = new HashMap<>();
        printService = new ConsolePrintService();

        menu_category_box.remove(MenuProductDetailBoxPanel);
        specials_category_box.remove(SpecialsProductDetailBoxPanel);
        CartItemsPanel.remove(CartProductDetailBoxPanel);

        // Set scroll bar policies
        menu_category_scroll_pane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        menu_category_scroll_pane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        specials_category_scroll_pane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        specials_category_scroll_pane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        CartScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        CartScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Add window listener to auto-save cart when closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveCartToFile();
            }

            @Override
            public void windowOpened(WindowEvent e) {
                loadCartFromFile();
                updateCartDisplay();
                updateCartAnnouncement();
            }
        });

        initializeProducts();
        checkUserAccess();

        // Load cart after products are initialized
        loadCartFromFile();
        updateCartDisplay();
        updateCartAnnouncement();
    }
   
    private void checkUserAccess() {
        if (backend.SessionManager.isLoggedIn()) {
            String currentUser = backend.SessionManager.getCurrentUsername();
            
            // Hide Inventory button for non-staff users
            if (!backend.UserAuthentication.isAdmin(currentUser) && 
                !backend.UserAuthentication.isCashier(currentUser) &&
                !backend.UserAuthentication.isClerk(currentUser)) {
                ToLoginButton.setVisible(false);
            }
        } else {
            ToLoginButton.setVisible(true);
        }
    }
    
    private void initializeProducts() {
        System.out.println("Loading products...");

        // Load products from database
        menuProducts = ProductManager.getAllProducts();
        specialsProducts = new ArrayList<>(); // Empty list

        System.out.println("Menu products loaded: " + menuProducts.size());

        // Load stock quantities for all products
        loadProductStocks();

        // Update the UI with the loaded products
        updateProductDisplays();

        System.out.println("Product display updated");
    }
    
    private void loadProductStocks() {
        // Load stock quantities for all products from InventoryManager
        for (Product product : menuProducts) {
            int stock = InventoryManager.getStockQuantity(product.getId());
            productStockMap.put(product.getId(), stock);
        }
        
        for (Product product : specialsProducts) {
            if (!productStockMap.containsKey(product.getId())) {
                int stock = InventoryManager.getStockQuantity(product.getId());
                productStockMap.put(product.getId(), stock);
            }
        }
    }
    
    private List<Product> getBestSellingProducts() {
        try {
            List<SalesRecord> bestSellers = SalesReportManager.getBestSellingProducts();
            List<Product> bestProducts = new ArrayList<>();

            // Get top 8 best-selling products
            int count = 0;
            for (SalesRecord record : bestSellers) {
                if (count >= 8) break;

                List<Product> products = ProductManager.searchProductsByName(record.getProductName());
                if (!products.isEmpty()) {
                    Product product = products.get(0);
                    if (getProductStock(product.getId()) > 0) {
                        bestProducts.add(product);
                        count++;
                    }
                }
            }
            return bestProducts;
        } catch (Exception e) {
            System.out.println("Error loading best sellers, using empty list: " + e.getMessage());
            return new ArrayList<>(); // Return empty list instead of crashing
        }
    }
    
    private JPanel createProductBox(Product product, boolean isMenuProduct) {
        // Create a panel that follows the MenuProductDetailBoxPanel structure
        JPanel panel = new JPanel();
        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panel.setBackground(new Color(249, 241, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new SoftBevelBorder(BevelBorder.RAISED),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setPreferredSize(new Dimension(700, 200));

        // Add this check at the beginning
        if (product == null) {
            return new JPanel(); // Return empty panel if product is null
        }

        // Image
        JLabel imageLabel = new JLabel();
        String imagePath = product.getImagePath();

        // Handle image path properly
        if (imagePath == null || imagePath.isEmpty()) {
            // Use default image if no path is provided
            imagePath = "/ui/Images/product_images/default.png";
        } else {
            // Ensure the path starts with "/" if it's a resource path
            if (!imagePath.startsWith("/")) {
                imagePath = "/" + imagePath;
            }

            // Check if the image exists in the resources
            java.net.URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl == null) {
                // If the image doesn't exist in resources, use default
                imagePath = "/ui/Images/product_images/default.png";
            }
        }

        try {
            java.net.URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                Image scaledImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                // Fallback to default image if URL is still null
                ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/ui/Images/product_images/default.png"));
                Image scaledImage = defaultIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            }
        } catch (Exception e) {
            System.out.println("Error loading image for product: " + product.getName() + ", path: " + imagePath);
            System.out.println("Error: " + e.getMessage());
            // Use default image on error
            ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/ui/Images/product_images/default.png"));
            Image scaledImage = defaultIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        }

        imageLabel.setBorder(BorderFactory.createLineBorder(new Color(31, 40, 35), 2));
        imageLabel.setPreferredSize(new Dimension(150, 150));
        panel.add(imageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        // Product name
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        nameLabel.setForeground(new Color(31, 40, 35));
        panel.add(nameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 500, 30));

        // Price
        JLabel priceLabel = new JLabel(String.format("₱%.2f", product.getPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        priceLabel.setForeground(new Color(100, 100, 100));
        panel.add(priceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, 260, 40));

        // Stock
        int currentStock = getProductStock(product.getId());
        int minValue = 0;
        int maxValue = currentStock; // Just use the current stock, no need to subtract cart items

        JLabel stockLabel = new JLabel("Stock: " + currentStock);
        stockLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        stockLabel.setForeground(currentStock > 0 ? new Color(42, 168, 83) : new Color(255, 0, 0));
        panel.add(stockLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 100, 260, 40));

        // Quantity spinner - use current stock directly
        JSpinner quantitySpinner = new JSpinner();
        quantitySpinner.setFont(new Font("Segoe UI", Font.BOLD, 24));
        quantitySpinner.setModel(new SpinnerNumberModel(0, minValue, Math.max(0, maxValue), 1));
        quantitySpinner.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        quantitySpinner.setPreferredSize(new Dimension(128, 64));
        panel.add(quantitySpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 120, -1, -1));

        boolean hasStock = currentStock > 0; // Check if any stock is available
        quantitySpinner.setEnabled(hasStock);

        // Add to cart button
        JButton addButton = new JButton();
        addButton.setIcon(new ImageIcon(getClass().getResource("/ui/Images/icons/add.png")));
        addButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addButton.setBackground(new Color(249, 241, 240));
        addButton.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.setEnabled(hasStock);
        addButton.setToolTipText("Add to Cart");
        addButton.addActionListener(e -> {
            int quantity = (Integer) quantitySpinner.getValue();
            if (quantity > 0) {
                addToCart(product, quantity);
                quantitySpinner.setValue(0); // Reset spinner after adding

                // Update the spinner's maximum value to reflect new available stock
                int updatedStock = getProductStock(product.getId());
                quantitySpinner.setModel(new SpinnerNumberModel(0, 0, Math.max(0, updatedStock), 1));
                quantitySpinner.setEnabled(updatedStock > 0);
            }
        });
        panel.add(addButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 120, 64, 64));

        return panel;
    }
    
    private void updateProductDisplays() {
        // Clear existing products
        menu_category_box.removeAll();
        specials_category_box.removeAll();

        // Add menu products with FlowLayout
        for (Product product : menuProducts) {
            JPanel productBox = createProductBox(product, true);
            menu_category_box.add(productBox);
        }

        // Add only best-selling products to specials
        List<Product> bestSellingProducts = getActualBestSellingProducts();
        for (Product product : bestSellingProducts) {
            JPanel productBox = createProductBox(product, false);
            specials_category_box.add(productBox);
        }

        // Adjust menu category box height based on number of products
        adjustMenuCategoryBoxHeight();

        // Refresh the containers
        menu_category_box.revalidate();
        menu_category_box.repaint();
        specials_category_box.revalidate();
        specials_category_box.repaint();

        // Update the cart display as well to reflect stock changes
        updateCartDisplay();

        // Update the cart announcement
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
        List<SalesRecord> bestSellers = SalesReportManager.getBestSellingProducts();
        List<Product> bestProducts = new ArrayList<>();

        // Get top 8 best-selling products
        int count = 0;
        for (SalesRecord record : bestSellers) {
            if (count >= 8) break;

            // Try to find product by name
            List<Product> products = ProductManager.searchProductsByName(record.getProductName());
            if (!products.isEmpty()) {
                Product product = products.get(0);
                // Check if product is in stock
                if (getProductStock(product.getId()) > 0) {
                    bestProducts.add(product);
                    count++;
                }
            }
        }

        return bestProducts;
    }
    
    private void addToCart(Product product, int quantity) {
        if (quantity <= 0) {
            return;
        }

        // Check stock availability using InventoryManager
        int availableStock = getProductStock(product.getId());

        if (availableStock < quantity) {
            JOptionPane.showMessageDialog(this, 
                "Not enough stock available for " + product.getName() + 
                ". Only " + availableStock + " items available.", 
                "Stock Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Add product to cart (update quantity if already exists)
        int alreadyInCart = cartItems.getOrDefault(product, 0);
        cartItems.put(product, alreadyInCart + quantity);

        // Update total price
        totalPrice += product.getPrice() * quantity;

        // Update stock immediately - decrease available stock
        boolean stockUpdated = InventoryManager.updateStock(product.getId(), -quantity);

        if (stockUpdated) {
            // Update local stock cache
            productStockMap.put(product.getId(), availableStock - quantity);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to update stock for: " + product.getName(), 
                "Stock Error", JOptionPane.ERROR_MESSAGE);
            // Rollback the cart addition if stock can't be updated
            cartItems.put(product, alreadyInCart); // Revert to previous quantity
            totalPrice -= product.getPrice() * quantity;
            return;
        }

        // Update cart display
        updateCartDisplay();

        // Update cart announcement
        updateCartAnnouncement();

        // Refresh product displays to update stock indicators and spinner limits
        updateProductDisplays();

        // Auto-save cart after modification
        saveCartToFile();
    }
    
    private int getProductStock(int productId) {
        // Always fetch the latest stock from the database to ensure accuracy
        int stock = InventoryManager.getStockQuantity(productId);
        productStockMap.put(productId, stock);
        return stock;
    }
    
    private void updateCartDisplay() {
        
        // Clear existing cart items
        CartItemsPanel.removeAll();

        if (cartItems.isEmpty()) {
            // Show empty cart message
            JLabel emptyLabel = new JLabel("Your cart is empty");
            emptyLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyLabel.setForeground(new Color(100, 100, 100));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            CartItemsPanel.add(emptyLabel);
        } else {
            // Add cart items vertically
            for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                JPanel cartItemPanel = createCartItemPanel(product, quantity);
                cartItemPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                CartItemsPanel.add(cartItemPanel);
                CartItemsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing between items
            }
        }

        // Update total price
        TotalPriceNumerLabel.setText(String.format("₱%.2f", totalPrice));

        // Adjust panel height based on number of items
        adjustCartPanelHeight();

        // Refresh the panel
        CartItemsPanel.revalidate();
        CartItemsPanel.repaint();
    }
    
    private void adjustMenuCategoryBoxHeight() {
        int productCount = menuProducts.size();
        int preferredHeight;

        if (productCount > 2) {
            // Calculate height based on number of products (200px per product + spacing)
            int rows = (int) Math.ceil(productCount / 2.0); // 2 products per row
            preferredHeight = rows * 440 + 40; // 420px per row (400px product + 420px spacing) + 40px padding
        } else {
            preferredHeight = 400; // Default height
        }

        menu_category_box.setPreferredSize(new Dimension(760, preferredHeight));
    }
    
    private void adjustCartPanelHeight() {
        if (!cartItems.isEmpty()) {
            int itemCount = cartItems.size();
            int preferredHeight = Math.max(400, itemCount * 440 + 40); // 400px per item + spacing
            CartItemsPanel.setPreferredSize(new Dimension(730, preferredHeight));
        } else {
            CartItemsPanel.setPreferredSize(new Dimension(730, 400));
        }
    }
    
    private JPanel createCartItemPanel(Product product, int quantity) {
        JPanel panel = new JPanel();
        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panel.setBackground(new Color(249, 241, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new SoftBevelBorder(BevelBorder.RAISED),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setPreferredSize(new Dimension(700, 200));
        panel.setMaximumSize(new Dimension(700, 200));

        // Add this check at the beginning
        if (product == null) {
            return new JPanel(); // Return empty panel if product is null
        }

        // Image - Use the same logic as createProductBox
        JLabel imageLabel = new JLabel();
        String imagePath = product.getImagePath();

        // Handle image path properly (same as createProductBox)
        if (imagePath == null || imagePath.isEmpty()) {
            // Use default image if no path is provided
            imagePath = "/ui/Images/product_images/default.png";
        } else {
            // Ensure the path starts with "/" if it's a resource path
            if (!imagePath.startsWith("/")) {
                imagePath = "/" + imagePath;
            }

            // Check if the image exists in the resources
            java.net.URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl == null) {
                // If the image doesn't exist in resources, use default
                imagePath = "/ui/Images/product_images/default.png";
            }
        }

        try {
            java.net.URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                Image scaledImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                // Fallback to default image if URL is still null
                ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/ui/Images/product_images/default.png"));
                Image scaledImage = defaultIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            }
        } catch (Exception e) {
            System.out.println("Error loading image for product: " + product.getName() + ", path: " + imagePath);
            System.out.println("Error: " + e.getMessage());
            // Use default image on error
            ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/ui/Images/product_images/default.png"));
            Image scaledImage = defaultIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        }

        imageLabel.setBorder(BorderFactory.createLineBorder(new Color(31, 40, 35), 2));
        imageLabel.setPreferredSize(new Dimension(150, 150));
        panel.add(imageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        // Product name
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        nameLabel.setForeground(new Color(31, 40, 35));
        panel.add(nameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 500, 30));

        // Price (same format as createProductBox)
        JLabel priceLabel = new JLabel(String.format("₱%.2f", product.getPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        priceLabel.setForeground(new Color(100, 100, 100));
        panel.add(priceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, 260, 40));

        // Stock (same format as createProductBox)
        int currentStock = getProductStock(product.getId());
        int maxAllowed = currentStock + quantity; // This should be the total available

        JLabel stockLabel = new JLabel("Stock: " + currentStock);
        stockLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        stockLabel.setForeground(currentStock > 0 ? new Color(42, 168, 83) : new Color(255, 0, 0));
        panel.add(stockLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 100, 260, 40));
        
        // Total price for this item (positioned below stock level)
        JLabel itemTotalLabel = new JLabel(String.format("Total: ₱%.2f", product.getPrice() * quantity));
        itemTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        itemTotalLabel.setForeground(new Color(31, 40, 35));
        panel.add(itemTotalLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, 260, 30));

        // Quantity label (same format as createProductBox)
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        quantityLabel.setForeground(new Color(66, 133, 244));
        panel.add(quantityLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 120, 80, 60));

        // Quantity spinner (same format as createProductBox)
        JSpinner quantitySpinner = new JSpinner();
        quantitySpinner.setFont(new Font("Segoe UI", Font.BOLD, 24));

        // Calculate maximum allowed quantity: current available stock + current cart quantity
        quantitySpinner.setModel(new SpinnerNumberModel(quantity, 0, maxAllowed, 1)); // Allow 0 for deletion

        quantitySpinner.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
        quantitySpinner.setPreferredSize(new Dimension(128, 64));
        panel.add(quantitySpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 120, -1, -1));
        
        quantitySpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int newQuantity = (Integer) quantitySpinner.getValue();
                int oldQuantity = cartItems.get(product);

                if (newQuantity != oldQuantity) {
                    int quantityDifference = newQuantity - oldQuantity;
                    int currentStock = getProductStock(product.getId());

                    // Check if we have enough stock for the increase
                    if (quantityDifference > 0 && quantityDifference > currentStock) {
                        JOptionPane.showMessageDialog(KioskFrame.this,
                                "Cannot increase quantity. Only " + currentStock + " available in stock.",
                                "Stock Limit", JOptionPane.WARNING_MESSAGE);
                        quantitySpinner.setValue(oldQuantity); // Reset to previous value
                        return;
                    }

                    // Update inventory
                    boolean stockUpdated = InventoryManager.updateStock(product.getId(), -quantityDifference);

                    if (!stockUpdated) {
                        JOptionPane.showMessageDialog(KioskFrame.this,
                                "Failed to update stock for: " + product.getName(),
                                "Stock Error", JOptionPane.ERROR_MESSAGE);
                        quantitySpinner.setValue(oldQuantity); // Reset to previous value
                        return;
                    }

                    // Update local stock cache
                    productStockMap.put(product.getId(), currentStock - quantityDifference);

                    // Update cart and totals
                    totalPrice += product.getPrice() * quantityDifference;
                    cartItems.put(product, newQuantity);
                    itemTotalLabel.setText(String.format("Total: ₱%.2f", product.getPrice() * newQuantity));
                    TotalPriceNumerLabel.setText(String.format("₱%.2f", totalPrice));
                    updateCartAnnouncement();

                    // Update the spinner's maximum value based on new available stock
                    int updatedAvailableStock = getProductStock(product.getId());
                    quantitySpinner.setModel(new SpinnerNumberModel(newQuantity, 0, newQuantity + updatedAvailableStock, 1));

                    // Refresh product displays to show updated stock
                    updateProductDisplays();
                    saveCartToFile();
                }
            }
        });

        // DELETE button (changed from add button)
        JButton deleteButton = new JButton();
        deleteButton.setIcon(new ImageIcon(getClass().getResource("/ui/Images/icons/delete.png")));
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

                // Return stock to inventory
                boolean stockUpdated = InventoryManager.updateStock(product.getId(), quantityToRemove);
                if (stockUpdated) {
                    // Update local stock cache
                    int currentStock = getProductStock(product.getId());
                    productStockMap.put(product.getId(), currentStock + quantityToRemove);
                }

                updateCartDisplay();
                updateCartAnnouncement();
                updateProductDisplays(); // Refresh product displays to show updated stock

                // Auto-save cart after modification
                saveCartToFile();
            }
        });
        panel.add(deleteButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 120, -1, -1));

        return panel;
    }
    
    private boolean isCartValidForCheckout() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!", "Checkout Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Check if any items in cart are out of stock, but allow checkout anyway
        // (just show a warning instead of preventing checkout)
        boolean hasOutOfStockItems = false;
        StringBuilder outOfStockMessage = new StringBuilder();

        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            Product product = entry.getKey();
            int quantityInCart = entry.getValue();
            int availableStock = getProductStock(product.getId());

            if (availableStock < quantityInCart) {
                hasOutOfStockItems = true;
                outOfStockMessage.append("- ")
                                .append(product.getName())
                                .append(": Ordered ")
                                .append(quantityInCart)
                                .append(", Available ")
                                .append(availableStock)
                                .append("\n");
            }
        }

        if (hasOutOfStockItems) {
            // Show warning but allow checkout to proceed
            int result = JOptionPane.showConfirmDialog(this,
                "Warning: Some items in your cart have insufficient stock:\n\n" +
                outOfStockMessage.toString() + "\n" +
                "Do you still want to proceed with checkout? The store will need to restock these items.",
                "Stock Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            return result == JOptionPane.YES_OPTION;
        }

        return true;
    }
    
    private void checkout() {
        System.out.println("Checkout started. Cart items: " + cartItems.size());

        // Check if cart is empty
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!", "Checkout Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check for out-of-stock items and show warning
        boolean hasOutOfStockItems = false;
        StringBuilder outOfStockMessage = new StringBuilder();

        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            Product product = entry.getKey();
            int quantityInCart = entry.getValue();
            int availableStock = getProductStock(product.getId());

            if (availableStock < quantityInCart) {
                hasOutOfStockItems = true;
                outOfStockMessage.append("- ")
                                .append(product.getName())
                                .append(": Ordered ")
                                .append(quantityInCart)
                                .append(", Available ")
                                .append(availableStock)
                                .append("\n");
            }
        }

        if (hasOutOfStockItems) {
            // Show warning but allow checkout to proceed
            int result = JOptionPane.showConfirmDialog(this,
                "Warning: Some items in your cart have insufficient stock:\n\n" +
                outOfStockMessage.toString() + "\n" +
                "Do you still want to proceed with checkout? The store will need to restock these items.",
                "Stock Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            // If user selects "No", cancel the checkout
            if (result != JOptionPane.YES_OPTION) {
                return;
            }
        }

        // Rest of the checkout process...
        // Automatically create a walk-in customer instead of requiring selection
        if (selectedCustomer == null) {
            System.out.println("No customer selected, creating walk-in customer");
            selectedCustomer = createWalkInCustomer();
            if (selectedCustomer == null) {
                JOptionPane.showMessageDialog(this, "Failed to create customer profile", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.out.println("Walk-in customer created: " + selectedCustomer.getCustomerId());
        }

        // Process order and get the order ID
        int orderId = processOrder();
        System.out.println("Process order returned: " + orderId);

        if (orderId != -1) {
            // Store the order ID for later use
            lastOrderId = orderId;

            // Generate and display order slip using the stored order ID
            OrderSlip orderSlip = OrderSlipManager.generateOrderSlip(orderId);
            if (orderSlip != null) {
                printService.displayOrderSlip(orderSlip);
            }

            JOptionPane.showMessageDialog(this, 
                "Order #" + orderId + " placed successfully! Please proceed to counter with your order slip.", 
                "Order Complete", 
                JOptionPane.INFORMATION_MESSAGE);

            clearCart();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to process order", "Order Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Customer createWalkInCustomer() {
        // Create a default walk-in customer with separate first and last names
        String walkInFirstName = "Walk-in";
        String walkInLastName = "Customer";
        String walkInPhone = "000-000-0000";
        String walkInEmail = "walkin@donmac.com";

        // Check if walk-in customer already exists
        Customer existingCustomer = CustomerManager.getCustomerByPhone(walkInPhone);
        if (existingCustomer != null) {
            return existingCustomer;
        }

        // Create new walk-in customer
        int customerId = CustomerManager.addCustomerReturnId(walkInFirstName, walkInLastName, walkInPhone, walkInEmail);
        if (customerId != -1) {
            return CustomerManager.getCustomer(customerId);
        }

        // Fallback: create a temporary customer object if database operation fails
        return new Customer(0, walkInFirstName, walkInLastName, walkInPhone, walkInEmail, null);
    }
    
    private void selectCustomer() {
        // Show a dialog to select or create a customer
        CustomerDialog customerDialog = new CustomerDialog(this, true);
        customerDialog.setVisible(true);
        
        if (customerDialog.isConfirmed()) {
            selectedCustomer = customerDialog.getSelectedCustomer();
            // You might want to display customer info in the UI
        }
    }
    
    private boolean isCartEmpty() {
        return cartItems.isEmpty();
    }
    
    private int processOrder() {
        int orderId = -1;
        try {
            // For Kiosk system, set cashier_id to 0 (unassigned)
            int cashierId = 0;

            System.out.println("Creating order for customer: " + selectedCustomer.getCustomerId());

            // Create new order with status "Pending"
            orderId = OrderManager.createNewOrder(selectedCustomer.getCustomerId(), cashierId, "Cash");

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

                boolean itemAdded = OrderManager.addOrderItem(orderId, product.getId(), quantity, true);

                if (!itemAdded) {
                    JOptionPane.showMessageDialog(this, "Failed to add item: " + product.getName(), "Order Error", JOptionPane.ERROR_MESSAGE);
                    // Rollback the order creation if items can't be added
                    OrderManager.deleteOrder(orderId);
                    return -1;
                }
            }

            // Final update of order total
            OrderManager.updateOrderTotal(orderId);

            System.out.println("Order processed successfully. Order ID: " + orderId);
            return orderId;

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error processing order: " + e.getMessage(), "Order Error", JOptionPane.ERROR_MESSAGE);

            // Return stock if order fails (optional)
            for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                InventoryManager.updateStock(product.getId(), quantity);

                // Update local stock cache
                int currentStock = getProductStock(product.getId());
                productStockMap.put(product.getId(), currentStock + quantity);
            }

            // If order fails, delete the order if it was created
            if (orderId != -1) {
                OrderManager.deleteOrder(orderId);
            }

            return -1;
        }
    }
    
    private void saveCartToFile() {
        try {
            File cartFile = new File("cart_data.dat");
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(cartFile))) {
                // Create a serializable representation
                Map<Integer, Integer> serializableCart = new HashMap<>();
                for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
                    serializableCart.put(entry.getKey().getId(), entry.getValue());
                }
                oos.writeObject(serializableCart);
                oos.writeDouble(totalPrice);
            }
        } catch (Exception e) {
            System.out.println("Error saving cart to file: " + e.getMessage());
        }
    }

    private void loadCartFromFile() {
        try {
            File cartFile = new File("cart_data.dat");
            if (cartFile.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(cartFile))) {
                    @SuppressWarnings("unchecked")
                    Map<Integer, Integer> serializableCart = (Map<Integer, Integer>) ois.readObject();
                    totalPrice = ois.readDouble();

                    // Clear current cart before loading
                    cartItems.clear();

                    // Reconstruct the cart
                    for (Map.Entry<Integer, Integer> entry : serializableCart.entrySet()) {
                        int productId = entry.getKey();
                        int quantity = entry.getValue();

                        // Find the product in our loaded products
                        for (Product product : menuProducts) {
                            if (product.getId() == productId) {
                                cartItems.put(product, quantity);
                                break;
                            }
                        }

                        // Also check specials products
                        for (Product product : specialsProducts) {
                            if (product.getId() == productId && !cartItems.containsKey(product)) {
                                cartItems.put(product, quantity);
                                break;
                            }
                        }
                    }

                    System.out.println("Cart loaded from file: " + cartItems.size() + " items");
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading cart from file: " + e.getMessage());
            // If there's an error, start with an empty cart
            cartItems.clear();
            totalPrice = 0.0;
        }
    }
    
    private void cleanupOldCartFile() {
        File cartFile = new File("cart_data.dat");
        if (cartFile.exists()) {
            // Delete file if it's older than 24 hours
            long lastModified = cartFile.lastModified();
            long twentyFourHoursAgo = System.currentTimeMillis() - (24 * 60 * 60 * 1000);

            if (lastModified < twentyFourHoursAgo) {
                if (cartFile.delete()) {
                    System.out.println("Deleted old cart file");
                }
            }
        }
    }
    
    private void clearCart() {
        cartItems.clear();
        totalPrice = 0.0;
        selectedCustomer = null;
        updateCartDisplay();
        updateCartAnnouncement();
        updateProductDisplays(); // Refresh to show updated stock

        // Auto-save cart after clearing
        saveCartToFile();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        SideBarPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        MenuButton = new javax.swing.JButton();
        SpecialsButton = new javax.swing.JButton();
        CartButton = new javax.swing.JButton();
        GetHelpButton1 = new javax.swing.JButton();
        ToLoginButton = new javax.swing.JButton();
        MainTabbedPane = new javax.swing.JTabbedPane();
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
        ProductStockDetailLabel1 = new javax.swing.JLabel();
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
        SpecialsProductStockDetailLabel = new javax.swing.JLabel();
        footer4 = new javax.swing.JPanel();
        CartUpdateAnnouncementOffscreenLabel2 = new javax.swing.JLabel();
        footer1 = new javax.swing.JPanel();
        CartPanelTab = new javax.swing.JPanel();
        header2 = new javax.swing.JPanel();
        CartTabTitleLabel1 = new javax.swing.JLabel();
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
        CartProductStockDetailLabel = new javax.swing.JLabel();
        CartItemTotalAmountLabel = new javax.swing.JLabel();
        CheckOutButton = new javax.swing.JButton();
        TotalPriceLabel = new javax.swing.JLabel();
        TotalPriceNumerLabel = new javax.swing.JLabel();
        footer2 = new javax.swing.JPanel();
        GetHelpPanelTab = new javax.swing.JPanel();
        header3 = new javax.swing.JPanel();
        GetHelpTitleLabel = new javax.swing.JLabel();
        CartContentPanel1 = new javax.swing.JPanel();
        footer3 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Kiosk");
        setBackground(new java.awt.Color(201, 177, 158));
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setPreferredSize(new java.awt.Dimension(1000, 600));
        setSize(new java.awt.Dimension(1000, 600));

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));
        jPanel2.setMinimumSize(new java.awt.Dimension(1000, 600));
        jPanel2.setPreferredSize(new java.awt.Dimension(1000, 600));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SideBarPanel.setBackground(new java.awt.Color(255, 255, 255));
        SideBarPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        SideBarPanel.setPreferredSize(new java.awt.Dimension(200, 600));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/logos/smalldonmacwhite.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        jLabel1.setPreferredSize(new java.awt.Dimension(96, 96));
        SideBarPanel.add(jLabel1);

        MenuButton.setBackground(new java.awt.Color(31, 40, 35));
        MenuButton.setForeground(new java.awt.Color(255, 255, 255));
        MenuButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/menu.png"))); // NOI18N
        MenuButton.setText("MENU");
        MenuButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        MenuButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        MenuButton.setIconTextGap(10);
        MenuButton.setPreferredSize(new java.awt.Dimension(200, 75));
        MenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuButtonActionPerformed(evt);
            }
        });
        SideBarPanel.add(MenuButton);

        SpecialsButton.setBackground(new java.awt.Color(31, 40, 35));
        SpecialsButton.setForeground(new java.awt.Color(255, 255, 255));
        SpecialsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/cup.png"))); // NOI18N
        SpecialsButton.setText("BEST SELLING");
        SpecialsButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        SpecialsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        SpecialsButton.setIconTextGap(10);
        SpecialsButton.setPreferredSize(new java.awt.Dimension(200, 75));
        SpecialsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SpecialsButtonActionPerformed(evt);
            }
        });
        SideBarPanel.add(SpecialsButton);

        CartButton.setBackground(new java.awt.Color(31, 40, 35));
        CartButton.setForeground(new java.awt.Color(255, 255, 255));
        CartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/cart.png"))); // NOI18N
        CartButton.setText("CART");
        CartButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CartButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CartButton.setIconTextGap(10);
        CartButton.setPreferredSize(new java.awt.Dimension(200, 75));
        CartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CartButtonActionPerformed(evt);
            }
        });
        SideBarPanel.add(CartButton);

        GetHelpButton1.setBackground(new java.awt.Color(31, 40, 35));
        GetHelpButton1.setForeground(new java.awt.Color(255, 255, 255));
        GetHelpButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/assist.png"))); // NOI18N
        GetHelpButton1.setText("GET HELP");
        GetHelpButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        GetHelpButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        GetHelpButton1.setIconTextGap(10);
        GetHelpButton1.setPreferredSize(new java.awt.Dimension(200, 75));
        GetHelpButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GetHelpButton1ActionPerformed(evt);
            }
        });
        SideBarPanel.add(GetHelpButton1);

        ToLoginButton.setBackground(new java.awt.Color(249, 241, 240));
        ToLoginButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/login.png"))); // NOI18N
        ToLoginButton.setText("LOGIN");
        ToLoginButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ToLoginButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ToLoginButton.setIconTextGap(10);
        ToLoginButton.setPreferredSize(new java.awt.Dimension(200, 75));
        ToLoginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToLoginButtonActionPerformed(evt);
            }
        });
        SideBarPanel.add(ToLoginButton);

        jPanel2.add(SideBarPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 600));

        MainTabbedPane.setBackground(new java.awt.Color(201, 177, 158));
        MainTabbedPane.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        MainTabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        MainTabbedPane.setToolTipText("");
        MainTabbedPane.setPreferredSize(new java.awt.Dimension(850, 600));

        MenuPanelTab.setBackground(new java.awt.Color(201, 177, 158));
        MenuPanelTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        MenuPanelTab.setForeground(new java.awt.Color(31, 40, 35));
        MenuPanelTab.setMinimumSize(new java.awt.Dimension(800, 600));
        MenuPanelTab.setPreferredSize(new java.awt.Dimension(790, 600));
        MenuPanelTab.setRequestFocusEnabled(false);
        MenuPanelTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header.setBackground(new java.awt.Color(173, 103, 48));
        header.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        header.setPreferredSize(new java.awt.Dimension(1000, 50));
        header.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        MainTabTitleLabel1.setBackground(new java.awt.Color(249, 241, 240));
        MainTabTitleLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        MainTabTitleLabel1.setForeground(new java.awt.Color(255, 255, 255));
        MainTabTitleLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        MainTabTitleLabel1.setText("MENU");
        MainTabTitleLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header.add(MainTabTitleLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 50));

        MenuPanelTab.add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, -1));

        MenuContentPanel.setBackground(new java.awt.Color(201, 177, 158));
        MenuContentPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
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
        menu_category_box.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        menu_category_box.setMinimumSize(new java.awt.Dimension(760, 400));
        menu_category_box.setPreferredSize(new java.awt.Dimension(760, 400));

        MenuProductDetailBoxPanel.setBackground(new java.awt.Color(249, 241, 240));
        MenuProductDetailBoxPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        MenuProductDetailBoxPanel.setForeground(new java.awt.Color(31, 40, 35));
        MenuProductDetailBoxPanel.setPreferredSize(new java.awt.Dimension(700, 200));
        MenuProductDetailBoxPanel.setRequestFocusEnabled(false);
        MenuProductDetailBoxPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ProductQuantityDetailLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        ProductQuantityDetailLabel.setForeground(new java.awt.Color(66, 133, 244));
        ProductQuantityDetailLabel.setText("Quantity:");
        MenuProductDetailBoxPanel.add(ProductQuantityDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 120, 80, 60));

        ProductNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        ProductNameLabel.setForeground(new java.awt.Color(31, 40, 35));
        ProductNameLabel.setText("Name: ");
        MenuProductDetailBoxPanel.add(ProductNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 500, 30));

        MainProductImageLabel.setBackground(new java.awt.Color(249, 241, 240));
        MainProductImageLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        MainProductImageLabel.setForeground(new java.awt.Color(255, 255, 255));
        MainProductImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        MainProductImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/default.png"))); // NOI18N
        MainProductImageLabel.setText("PRODUCT DETAILS");
        MainProductImageLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));
        MainProductImageLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        MainProductImageLabel.setMaximumSize(new java.awt.Dimension(150, 150));
        MainProductImageLabel.setMinimumSize(new java.awt.Dimension(150, 150));
        MainProductImageLabel.setName(""); // NOI18N
        MainProductImageLabel.setPreferredSize(new java.awt.Dimension(150, 150));
        MenuProductDetailBoxPanel.add(MainProductImageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        ProductAddToCartButton.setBackground(new java.awt.Color(249, 241, 240));
        ProductAddToCartButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ProductAddToCartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/add.png"))); // NOI18N
        ProductAddToCartButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ProductAddToCartButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ProductAddToCartButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ProductAddToCartButton.setIconTextGap(10);
        ProductAddToCartButton.setPreferredSize(new java.awt.Dimension(64, 64));
        ProductAddToCartButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_add.png"))); // NOI18N
        ProductAddToCartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductAddToCartButtonActionPerformed(evt);
            }
        });
        MenuProductDetailBoxPanel.add(ProductAddToCartButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 120, -1, -1));

        ProductQuantitySpinner.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        ProductQuantitySpinner.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ProductQuantitySpinner.setPreferredSize(new java.awt.Dimension(128, 64));
        MenuProductDetailBoxPanel.add(ProductQuantitySpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 120, -1, -1));

        ProductPriceDetailLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ProductPriceDetailLabel.setForeground(new java.awt.Color(102, 102, 102));
        ProductPriceDetailLabel.setText("Price:");
        MenuProductDetailBoxPanel.add(ProductPriceDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, 260, 40));

        ProductStockDetailLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ProductStockDetailLabel1.setForeground(new java.awt.Color(42, 168, 83));
        ProductStockDetailLabel1.setText("Stock:");
        MenuProductDetailBoxPanel.add(ProductStockDetailLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 100, 260, 40));

        menu_category_box.add(MenuProductDetailBoxPanel);

        menu_category_scroll_pane1.setViewportView(menu_category_box);

        MenuContentPanel.add(menu_category_scroll_pane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 760, -1));

        CartUpdateAnnouncementOffscreenLabel1.setBackground(new java.awt.Color(249, 241, 240));
        CartUpdateAnnouncementOffscreenLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CartUpdateAnnouncementOffscreenLabel1.setForeground(new java.awt.Color(255, 255, 255));
        CartUpdateAnnouncementOffscreenLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CartUpdateAnnouncementOffscreenLabel1.setText("CART UPDATE ANNOUNCEMENT");
        CartUpdateAnnouncementOffscreenLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        MenuContentPanel.add(CartUpdateAnnouncementOffscreenLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 800, 50));

        MenuPanelTab.add(MenuContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 790, 500));

        footer.setBackground(new java.awt.Color(121, 63, 26));
        footer.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        footer.setPreferredSize(new java.awt.Dimension(990, 50));
        footer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        MenuPanelTab.add(footer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        MainTabbedPane.addTab("tab1", MenuPanelTab);

        SpecialsPanelTab.setBackground(new java.awt.Color(201, 177, 158));
        SpecialsPanelTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        SpecialsPanelTab.setForeground(new java.awt.Color(31, 40, 35));
        SpecialsPanelTab.setPreferredSize(new java.awt.Dimension(800, 600));
        SpecialsPanelTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header1.setBackground(new java.awt.Color(173, 103, 48));
        header1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        header1.setPreferredSize(new java.awt.Dimension(1000, 50));
        header1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SpecialsTabTitleLabel.setBackground(new java.awt.Color(249, 241, 240));
        SpecialsTabTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        SpecialsTabTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        SpecialsTabTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SpecialsTabTitleLabel.setText("SPECIALS");
        SpecialsTabTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header1.add(SpecialsTabTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 50));

        SpecialsPanelTab.add(header1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, -1));

        SpecialsContentPanel.setBackground(new java.awt.Color(201, 177, 158));
        SpecialsContentPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
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
        specials_category_box.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        specials_category_box.setPreferredSize(new java.awt.Dimension(760, 400));

        SpecialsProductDetailBoxPanel.setBackground(new java.awt.Color(249, 241, 240));
        SpecialsProductDetailBoxPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        SpecialsProductDetailBoxPanel.setForeground(new java.awt.Color(31, 40, 35));
        SpecialsProductDetailBoxPanel.setPreferredSize(new java.awt.Dimension(700, 200));
        SpecialsProductDetailBoxPanel.setRequestFocusEnabled(false);
        SpecialsProductDetailBoxPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SpecialsProductQuantityDetailLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        SpecialsProductQuantityDetailLabel.setForeground(new java.awt.Color(66, 133, 244));
        SpecialsProductQuantityDetailLabel.setText("Quantity:");
        SpecialsProductDetailBoxPanel.add(SpecialsProductQuantityDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 120, 80, 60));

        SpecialsProductNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        SpecialsProductNameLabel.setForeground(new java.awt.Color(31, 40, 35));
        SpecialsProductNameLabel.setText("Name: ");
        SpecialsProductDetailBoxPanel.add(SpecialsProductNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 500, 30));

        SpecialsProductImageLabel.setBackground(new java.awt.Color(249, 241, 240));
        SpecialsProductImageLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        SpecialsProductImageLabel.setForeground(new java.awt.Color(255, 255, 255));
        SpecialsProductImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SpecialsProductImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/default.png"))); // NOI18N
        SpecialsProductImageLabel.setText("PRODUCT DETAILS");
        SpecialsProductImageLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));
        SpecialsProductImageLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        SpecialsProductImageLabel.setMaximumSize(new java.awt.Dimension(150, 150));
        SpecialsProductImageLabel.setMinimumSize(new java.awt.Dimension(150, 150));
        SpecialsProductImageLabel.setPreferredSize(new java.awt.Dimension(150, 150));
        SpecialsProductDetailBoxPanel.add(SpecialsProductImageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        SpecialsProductAddToCartButton.setBackground(new java.awt.Color(249, 241, 240));
        SpecialsProductAddToCartButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        SpecialsProductAddToCartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/add.png"))); // NOI18N
        SpecialsProductAddToCartButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        SpecialsProductAddToCartButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        SpecialsProductAddToCartButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        SpecialsProductAddToCartButton.setIconTextGap(10);
        SpecialsProductAddToCartButton.setPreferredSize(new java.awt.Dimension(64, 64));
        SpecialsProductAddToCartButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_add.png"))); // NOI18N
        SpecialsProductAddToCartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SpecialsProductAddToCartButtonActionPerformed(evt);
            }
        });
        SpecialsProductDetailBoxPanel.add(SpecialsProductAddToCartButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 120, -1, -1));

        SpecialsProductQuantitySpinner.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        SpecialsProductQuantitySpinner.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        SpecialsProductQuantitySpinner.setPreferredSize(new java.awt.Dimension(128, 64));
        SpecialsProductDetailBoxPanel.add(SpecialsProductQuantitySpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 120, -1, -1));

        SpecialsProductPriceDetailLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        SpecialsProductPriceDetailLabel.setForeground(new java.awt.Color(102, 102, 102));
        SpecialsProductPriceDetailLabel.setText("Price:");
        SpecialsProductDetailBoxPanel.add(SpecialsProductPriceDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, 260, 40));

        SpecialsProductStockDetailLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        SpecialsProductStockDetailLabel.setForeground(new java.awt.Color(42, 168, 83));
        SpecialsProductStockDetailLabel.setText("Stock:");
        SpecialsProductDetailBoxPanel.add(SpecialsProductStockDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 100, 260, 40));

        specials_category_box.add(SpecialsProductDetailBoxPanel);

        specials_category_scroll_pane1.setViewportView(specials_category_box);

        SpecialsContentPanel.add(specials_category_scroll_pane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        footer4.setBackground(new java.awt.Color(121, 63, 26));
        footer4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        footer4.setPreferredSize(new java.awt.Dimension(990, 50));
        footer4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        SpecialsContentPanel.add(footer4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        CartUpdateAnnouncementOffscreenLabel2.setBackground(new java.awt.Color(249, 241, 240));
        CartUpdateAnnouncementOffscreenLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CartUpdateAnnouncementOffscreenLabel2.setForeground(new java.awt.Color(255, 255, 255));
        CartUpdateAnnouncementOffscreenLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CartUpdateAnnouncementOffscreenLabel2.setText("CART UPDATE ANNOUNCEMENT");
        CartUpdateAnnouncementOffscreenLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        SpecialsContentPanel.add(CartUpdateAnnouncementOffscreenLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 800, 50));

        SpecialsPanelTab.add(SpecialsContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 790, 500));

        footer1.setBackground(new java.awt.Color(121, 63, 26));
        footer1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        footer1.setPreferredSize(new java.awt.Dimension(990, 50));
        footer1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        SpecialsPanelTab.add(footer1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        MainTabbedPane.addTab("tab2", SpecialsPanelTab);

        CartPanelTab.setBackground(new java.awt.Color(201, 177, 158));
        CartPanelTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        CartPanelTab.setForeground(new java.awt.Color(31, 40, 35));
        CartPanelTab.setPreferredSize(new java.awt.Dimension(780, 850));
        CartPanelTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header2.setBackground(new java.awt.Color(173, 103, 48));
        header2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        header2.setPreferredSize(new java.awt.Dimension(1000, 50));
        header2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CartTabTitleLabel1.setBackground(new java.awt.Color(249, 241, 240));
        CartTabTitleLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CartTabTitleLabel1.setForeground(new java.awt.Color(255, 255, 255));
        CartTabTitleLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CartTabTitleLabel1.setText("CART");
        CartTabTitleLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header2.add(CartTabTitleLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 50));

        CartPanelTab.add(header2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, -1));

        CartContentPanel.setBackground(new java.awt.Color(201, 177, 158));
        CartContentPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        CartContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        CartContentPanel.setRequestFocusEnabled(false);
        CartContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CartScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        CartScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        CartScrollPane.setPreferredSize(new java.awt.Dimension(730, 400));

        CartItemsPanel.setBackground(new java.awt.Color(31, 40, 35));
        CartItemsPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
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
        CartProductDetailBoxPanel.setPreferredSize(new java.awt.Dimension(700, 200));
        CartProductDetailBoxPanel.setRequestFocusEnabled(false);
        CartProductDetailBoxPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CartProductQuantityDetailLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        CartProductQuantityDetailLabel.setForeground(new java.awt.Color(66, 133, 244));
        CartProductQuantityDetailLabel.setText("Quantity:");
        CartProductDetailBoxPanel.add(CartProductQuantityDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 120, 80, 60));

        CartProductNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CartProductNameLabel.setForeground(new java.awt.Color(31, 40, 35));
        CartProductNameLabel.setText("Name: ");
        CartProductDetailBoxPanel.add(CartProductNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 500, 40));

        CartMainProductImageLabel.setBackground(new java.awt.Color(249, 241, 240));
        CartMainProductImageLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        CartMainProductImageLabel.setForeground(new java.awt.Color(255, 255, 255));
        CartMainProductImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CartMainProductImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/default.png"))); // NOI18N
        CartMainProductImageLabel.setText("PRODUCT DETAILS");
        CartMainProductImageLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));
        CartMainProductImageLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        CartMainProductImageLabel.setMaximumSize(new java.awt.Dimension(150, 150));
        CartMainProductImageLabel.setMinimumSize(new java.awt.Dimension(150, 150));
        CartMainProductImageLabel.setPreferredSize(new java.awt.Dimension(150, 150));
        CartProductDetailBoxPanel.add(CartMainProductImageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        CartProductDeleteToCartButton.setBackground(new java.awt.Color(249, 241, 240));
        CartProductDeleteToCartButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        CartProductDeleteToCartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/delete.png"))); // NOI18N
        CartProductDeleteToCartButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CartProductDeleteToCartButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CartProductDeleteToCartButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        CartProductDeleteToCartButton.setIconTextGap(10);
        CartProductDeleteToCartButton.setPreferredSize(new java.awt.Dimension(64, 64));
        CartProductDeleteToCartButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_delete.png"))); // NOI18N
        CartProductDeleteToCartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CartProductDeleteToCartButtonActionPerformed(evt);
            }
        });
        CartProductDetailBoxPanel.add(CartProductDeleteToCartButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 120, -1, -1));

        CartProductQuantitySpinner.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        CartProductQuantitySpinner.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CartProductQuantitySpinner.setPreferredSize(new java.awt.Dimension(128, 64));
        CartProductDetailBoxPanel.add(CartProductQuantitySpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 120, -1, -1));

        CartProductPriceDetailLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        CartProductPriceDetailLabel.setForeground(new java.awt.Color(102, 102, 102));
        CartProductPriceDetailLabel.setText("Price:");
        CartProductDetailBoxPanel.add(CartProductPriceDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, 260, 40));

        CartProductStockDetailLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        CartProductStockDetailLabel.setForeground(new java.awt.Color(42, 168, 83));
        CartProductStockDetailLabel.setText("Stock:");
        CartProductDetailBoxPanel.add(CartProductStockDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 100, 260, 40));

        CartItemTotalAmountLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        CartItemTotalAmountLabel.setForeground(new java.awt.Color(31, 40, 35));
        CartItemTotalAmountLabel.setText("Total: ");
        CartProductDetailBoxPanel.add(CartItemTotalAmountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, 130, 30));

        CartItemsPanel.add(CartProductDetailBoxPanel);

        CartScrollPane.setViewportView(CartItemsPanel);

        CartContentPanel.add(CartScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 760, 400));

        CheckOutButton.setBackground(new java.awt.Color(249, 241, 240));
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
        CartContentPanel.add(CheckOutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 450, -1, -1));

        TotalPriceLabel.setBackground(new java.awt.Color(249, 241, 240));
        TotalPriceLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TotalPriceLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalPriceLabel.setText("Total Price:");
        TotalPriceLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TotalPriceLabel.setPreferredSize(new java.awt.Dimension(150, 40));
        CartContentPanel.add(TotalPriceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, 130, -1));

        TotalPriceNumerLabel.setBackground(new java.awt.Color(249, 241, 240));
        TotalPriceNumerLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TotalPriceNumerLabel.setText("₱0.00");
        TotalPriceNumerLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TotalPriceNumerLabel.setPreferredSize(new java.awt.Dimension(150, 40));
        CartContentPanel.add(TotalPriceNumerLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 450, 140, -1));

        CartPanelTab.add(CartContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 790, 500));

        footer2.setBackground(new java.awt.Color(121, 63, 26));
        footer2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        footer2.setPreferredSize(new java.awt.Dimension(990, 50));
        footer2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        CartPanelTab.add(footer2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        MainTabbedPane.addTab("tab3", CartPanelTab);

        GetHelpPanelTab.setBackground(new java.awt.Color(201, 177, 158));
        GetHelpPanelTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        GetHelpPanelTab.setForeground(new java.awt.Color(31, 40, 35));
        GetHelpPanelTab.setPreferredSize(new java.awt.Dimension(780, 850));
        GetHelpPanelTab.setRequestFocusEnabled(false);
        GetHelpPanelTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header3.setBackground(new java.awt.Color(173, 103, 48));
        header3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        header3.setPreferredSize(new java.awt.Dimension(1000, 50));
        header3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        GetHelpTitleLabel.setBackground(new java.awt.Color(249, 241, 240));
        GetHelpTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        GetHelpTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        GetHelpTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        GetHelpTitleLabel.setText("HELP & FAQ");
        GetHelpTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header3.add(GetHelpTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 50));

        GetHelpPanelTab.add(header3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, -1));

        CartContentPanel1.setBackground(new java.awt.Color(201, 177, 158));
        CartContentPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        CartContentPanel1.setForeground(new java.awt.Color(31, 40, 35));
        CartContentPanel1.setRequestFocusEnabled(false);
        CartContentPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        GetHelpPanelTab.add(CartContentPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 790, 500));

        footer3.setBackground(new java.awt.Color(121, 63, 26));
        footer3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        footer3.setPreferredSize(new java.awt.Dimension(990, 50));
        footer3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        GetHelpPanelTab.add(footer3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        MainTabbedPane.addTab("tab4", GetHelpPanelTab);

        jPanel2.add(MainTabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 850, 600));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        getAccessibleContext().setAccessibleName("KIOSK");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void GetHelpButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GetHelpButton1ActionPerformed
        MainTabbedPane.setSelectedIndex(3);
        updateProductDisplays();
    }//GEN-LAST:event_GetHelpButton1ActionPerformed

    private void CartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CartButtonActionPerformed
        MainTabbedPane.setSelectedIndex(2);
        updateProductDisplays();
    }//GEN-LAST:event_CartButtonActionPerformed

    private void MenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuButtonActionPerformed
        MainTabbedPane.setSelectedIndex(0);
        updateProductDisplays();
    }//GEN-LAST:event_MenuButtonActionPerformed

    private void SpecialsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SpecialsButtonActionPerformed
        MainTabbedPane.setSelectedIndex(1);
        updateProductDisplays();
    }//GEN-LAST:event_SpecialsButtonActionPerformed
    // TODO add your handling code here:


    private void CheckOutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckOutButtonActionPerformed
        checkout();
        updateProductDisplays();
    }//GEN-LAST:event_CheckOutButtonActionPerformed

    private void ToLoginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToLoginButtonActionPerformed
        LoginFrame LoginFrame = new LoginFrame();
        LoginFrame.setVisible(true);
        LoginFrame.pack();
        LoginFrame.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_ToLoginButtonActionPerformed

    private void SpecialsProductAddToCartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SpecialsProductAddToCartButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SpecialsProductAddToCartButtonActionPerformed

    private void ProductAddToCartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductAddToCartButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ProductAddToCartButtonActionPerformed

    private void CartProductDeleteToCartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CartProductDeleteToCartButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CartProductDeleteToCartButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CartButton;
    private javax.swing.JPanel CartContentPanel;
    private javax.swing.JPanel CartContentPanel1;
    private javax.swing.JLabel CartItemTotalAmountLabel;
    private javax.swing.JPanel CartItemsPanel;
    private javax.swing.JLabel CartMainProductImageLabel;
    private javax.swing.JPanel CartPanelTab;
    private javax.swing.JButton CartProductDeleteToCartButton;
    private javax.swing.JPanel CartProductDetailBoxPanel;
    private javax.swing.JLabel CartProductNameLabel;
    private javax.swing.JLabel CartProductPriceDetailLabel;
    private javax.swing.JLabel CartProductQuantityDetailLabel;
    private javax.swing.JSpinner CartProductQuantitySpinner;
    private javax.swing.JLabel CartProductStockDetailLabel;
    private javax.swing.JScrollPane CartScrollPane;
    private javax.swing.JLabel CartTabTitleLabel1;
    private javax.swing.JLabel CartUpdateAnnouncementOffscreenLabel1;
    private javax.swing.JLabel CartUpdateAnnouncementOffscreenLabel2;
    private javax.swing.JLabel CartemptyLabel;
    private javax.swing.JButton CheckOutButton;
    private javax.swing.JButton GetHelpButton1;
    private javax.swing.JPanel GetHelpPanelTab;
    private javax.swing.JLabel GetHelpTitleLabel;
    private javax.swing.JLabel MainProductImageLabel;
    private javax.swing.JLabel MainTabTitleLabel1;
    private javax.swing.JTabbedPane MainTabbedPane;
    private javax.swing.JButton MenuButton;
    private javax.swing.JPanel MenuContentPanel;
    private javax.swing.JPanel MenuPanelTab;
    private javax.swing.JPanel MenuProductDetailBoxPanel;
    private javax.swing.JButton ProductAddToCartButton;
    private javax.swing.JLabel ProductNameLabel;
    private javax.swing.JLabel ProductPriceDetailLabel;
    private javax.swing.JLabel ProductQuantityDetailLabel;
    private javax.swing.JSpinner ProductQuantitySpinner;
    private javax.swing.JLabel ProductStockDetailLabel1;
    private javax.swing.JPanel SideBarPanel;
    private javax.swing.JButton SpecialsButton;
    private javax.swing.JPanel SpecialsContentPanel;
    private javax.swing.JPanel SpecialsPanelTab;
    private javax.swing.JButton SpecialsProductAddToCartButton;
    private javax.swing.JPanel SpecialsProductDetailBoxPanel;
    private javax.swing.JLabel SpecialsProductImageLabel;
    private javax.swing.JLabel SpecialsProductNameLabel;
    private javax.swing.JLabel SpecialsProductPriceDetailLabel;
    private javax.swing.JLabel SpecialsProductQuantityDetailLabel;
    private javax.swing.JSpinner SpecialsProductQuantitySpinner;
    private javax.swing.JLabel SpecialsProductStockDetailLabel;
    private javax.swing.JLabel SpecialsTabTitleLabel;
    private javax.swing.JButton ToLoginButton;
    private javax.swing.JLabel TotalPriceLabel;
    private javax.swing.JLabel TotalPriceNumerLabel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel footer;
    private javax.swing.JPanel footer1;
    private javax.swing.JPanel footer2;
    private javax.swing.JPanel footer3;
    private javax.swing.JPanel footer4;
    private javax.swing.JPanel header;
    private javax.swing.JPanel header1;
    private javax.swing.JPanel header2;
    private javax.swing.JPanel header3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel menu_category_box;
    private javax.swing.JScrollPane menu_category_scroll_pane1;
    private javax.swing.JPanel specials_category_box;
    private javax.swing.JScrollPane specials_category_scroll_pane1;
    // End of variables declaration//GEN-END:variables
}

