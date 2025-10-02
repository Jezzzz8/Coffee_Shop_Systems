package ui;

import backend.*;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.AbstractCellEditor;
import javax.swing.table.TableCellEditor;

public class POSFrame extends javax.swing.JFrame {

    private List<CartItem> cartItems;
    private double totalAmount;
    private final String[] PAYMENT_METHODS = {"Cash", "Credit Card", "Debit Card", "GCash"};
    private Map<Integer, Order> orderMap; // Maps order IDs to Order objects
    private Map<Integer, List<OrderItem>> orderItemsMap; // Maps order IDs to their items
    private int currentProcessingOrderId = -1;
    private boolean isProcessingExistingOrder = false;
    private PrintService printService;

    public POSFrame() {
        initComponents();
        initializePOS();
        checkUserAccess();
        
    }
    
    private void checkUserAccess() {
        if (!backend.SessionManager.isLoggedIn()) {
            redirectToLogin();
            return;
        }

        String currentUser = backend.SessionManager.getCurrentUsername();

        if (!backend.UserAuthentication.isAdmin(currentUser) && 
            !backend.UserAuthentication.isCashier(currentUser)) {
            JOptionPane.showMessageDialog(this, 
                "Access Denied! Only Administrators and Cashiers can access the POS system.", 
                "Permission Error", 
                JOptionPane.ERROR_MESSAGE);
            redirectToLogin();
        }
    }
    
    private void redirectToLogin() {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
        this.dispose();
    }
    
    private void initializePOS() {
        printService = new ConsolePrintService();
        cartItems = new ArrayList<>();
        totalAmount = 0.0;
        orderMap = new HashMap<>();
        orderItemsMap = new HashMap<>();
        currentProcessingOrderId = -1;
        isProcessingExistingOrder = false;

        // Initialize table model
        initializeCartTable();
        updateTotalAmount();
        loadCustomerOrders(); // Load orders on startup
        
        TotalAmountTextBar.setText("TEST");
        System.out.println("Test update: " + TotalAmountTextBar.getText());
        
        // Set up payment tab
        TotalAmountTextBar.setText("₱0.00");
        ChangeTextBar.setText("₱0.00");
        TotalAmountDueTitleLabel.setText("Total Amount Due: ₱0.00");

        // Add real-time search functionality
        setupRealTimeSearch();
        
        PaymentReceivedTextBar.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            calculateChange();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            calculateChange();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            calculateChange();
        }
    });
    }

    private void setupRealTimeSearch() {
        Timer timer = new Timer(300, null); // 300ms delay
        timer.setRepeats(false);

        AddItemNameTextBar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                timer.restart();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                timer.restart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                timer.restart();
            }
        });

        timer.addActionListener(e -> {
            searchProduct();
        });

        // Add Enter key support to move to quantity field
        AddItemNameTextBar.addActionListener(e -> {
            AddItemQuantityTextBar.requestFocus();
        });

        // Add Enter key support in quantity field to add to cart
        AddItemQuantityTextBar.addActionListener(e -> {
            addToCart();
        });
    }
    
    private void loadCustomerOrders() {
        // Clear existing data
        orderMap.clear();
        orderItemsMap.clear();

        // Load only pending orders (not completed ones)
        List<Order> pendingOrders = OrderManager.getOrdersByStatus("Pending");

        // Create a list model for the JList
        DefaultListModel<String> listModel = new DefaultListModel<>();

        if (pendingOrders.isEmpty()) {
            listModel.addElement("No Orders");
        } else {
            for (Order order : pendingOrders) {
                orderMap.put(order.getOrderId(), order);

                // Get order items
                List<OrderItem> items = OrderManager.getOrderItems(order.getOrderId());
                orderItemsMap.put(order.getOrderId(), items);

                // Add to list model
                String orderInfo = String.format("Order #%d - ₱%.2f", 
                    order.getOrderId(), order.getTotalAmount());
                listModel.addElement(orderInfo);
            }
        }

        CostumersOrderIDList.setModel(listModel);

        // Disable selection when "No Orders" is displayed
        CostumersOrderIDList.setEnabled(!pendingOrders.isEmpty());

        // Add selection listener (only if there are orders)
        if (!pendingOrders.isEmpty()) {
            CostumersOrderIDList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int selectedIndex = CostumersOrderIDList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        String selectedValue = CostumersOrderIDList.getSelectedValue();
                        // Extract order ID from the displayed string
                        int orderId = extractOrderId(selectedValue);
                        showOrderDetails(orderId);
                    }
                }
            });
        }
    }
    
    private int extractOrderId(String orderInfo) {
        try {
            // Check if it's the "No Orders" message
            if ("No Orders".equals(orderInfo)) {
                return -1;
            }

            // Extract the order ID from the formatted string
            // Handle both formats: "[PENDING] Order #123" and "Order #123"
            String processedInfo = orderInfo.replace("[PENDING] ", "");
            String[] parts = processedInfo.split(" - ");
            if (parts.length > 0) {
                String orderPart = parts[0].replace("Order #", "");
                return Integer.parseInt(orderPart);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    private void showOrderDetails(int orderId) {
        if (orderId == -1) {
            return;
        }

        refreshOrderFromDatabase(orderId);
        Order order = orderMap.get(orderId);
        List<OrderItem> items = orderItemsMap.get(orderId);

        if (order == null || items == null) {
            JOptionPane.showMessageDialog(this, "Order details not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Clear current cart and load the selected order
        cartItems.clear();

        for (OrderItem orderItem : items) {
            Product product = ProductManager.getProductById(orderItem.getProductId());
            if (product != null) {
                CartItem cartItem = new CartItem(product, orderItem.getQuantity());
                cartItems.add(cartItem);
            }
        }

        // Update UI
        updateCartTable();
        updateTotalAmount(); // This will now update both tabs

        // Show ORDER information (not product information)
        ItemNameLabel.setText("Order #" + orderId);
        ItemPriceLabel.setText(String.format("₱%.2f", order.getTotalAmount()));

        // Update description
        ItemDescriptionLabel.setText("Pending Order - Ready for Processing");

        // Clear product search labels to avoid confusion
        ProductNameLabel.setText("");
        ProductPriceLabel.setText("");
        ProductDescriptionLabel.setText("");
        AddItemNameTextBar.setText("");
        AddItemQuantityTextBar.setText("");

        // Set the current processing order
        currentProcessingOrderId = orderId;
        isProcessingExistingOrder = true;

        // Update the payment tab's total amount to match the current cart
        TotalAmountTextBar.setText(String.format("₱%.2f", totalAmount));
    }
    
    private void processNextOrder() {
        try {
            // Get the next order from the queue
            Integer nextOrderId = QueueManager.getNextOrder();

            if (nextOrderId == null) {
                JOptionPane.showMessageDialog(this, "No orders in queue", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Get current cashier
            String currentUser = backend.SessionManager.getCurrentUsername();
            User user = UserAuthentication.getUserByUsername(currentUser);

            if (user == null) {
                JOptionPane.showMessageDialog(this, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int cashierId = user.getId();

            // Assign this order to the current cashier and update status to "Processing"
            boolean success = OrderManager.assignOrderToCashier(nextOrderId, cashierId);

            if (success) {
                // Load the order details into the POS interface
                loadOrderIntoPOS(nextOrderId);
                JOptionPane.showMessageDialog(this, "Processing order #" + nextOrderId, "Order Assigned", JOptionPane.INFORMATION_MESSAGE);

                // Switch to processing tab
                jTabbedPane1.setSelectedIndex(1);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to assign order", "Error", JOptionPane.ERROR_MESSAGE);
                // Put the order back in queue if assignment fails
                QueueManager.addToQueue(nextOrderId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error processing next order: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initializeCartTable() {
        DefaultTableModel model = (DefaultTableModel) CartOrderTable.getModel();
        model.setRowCount(0); // Clear existing data

        // Set up the renderer and editor for the Actions column
        if (CartOrderTable.getColumnModel().getColumnCount() > 6) {
            CartOrderTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
            CartOrderTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor());
        }
        
        // Add a table model listener to handle quantity changes
        model.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                if (column == 2) { // Quantity column
                    try {
                        int newQuantity = Integer.parseInt(model.getValueAt(row, column).toString());
                        if (newQuantity <= 0) {
                            JOptionPane.showMessageDialog(this, "Quantity must be greater than 0", "Input Error", JOptionPane.WARNING_MESSAGE);
                            updateCartTable(); // Refresh to revert invalid input
                            return;
                        }

                        // Get the CartItem from your cartItems list using the row index
                        if (row < cartItems.size()) {
                            CartItem item = cartItems.get(row);

                            // Check stock availability
                            int availableStock = InventoryManager.getStockQuantity(item.getProduct().getId());
                            if (availableStock < newQuantity - item.getQuantity()) {
                                JOptionPane.showMessageDialog(this, 
                                    "Insufficient stock! Available: " + availableStock, 
                                    "Stock Error", 
                                    JOptionPane.WARNING_MESSAGE);
                                updateCartTable(); // Refresh to revert invalid input
                                return;
                            }

                            item.setQuantity(newQuantity);
                            updateTotalAmount();

                            // Also update change calculation if on payment tab
                            if (jTabbedPane1.getSelectedIndex() == 1) {
                                calculateChange();
                            }
                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Please enter a valid quantity number", "Input Error", JOptionPane.WARNING_MESSAGE);
                        updateCartTable(); // Refresh to revert invalid input
                    } catch (IndexOutOfBoundsException ex) {
                        JOptionPane.showMessageDialog(this, "Error accessing cart item", "Error", JOptionPane.ERROR_MESSAGE);
                        updateCartTable(); // Refresh table
                    }
                }
            }
        });
    }
    
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton deleteBtn;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
            setOpaque(true);
            deleteBtn = new JButton("Delete");

            // Style button to be more compact - same as your existing buttons
            Dimension buttonSize = new Dimension(70, 25);
            deleteBtn.setPreferredSize(buttonSize);
            deleteBtn.setMaximumSize(buttonSize);
            deleteBtn.setMinimumSize(buttonSize);

            // Set button colors to match your inventory system (red delete button)
            deleteBtn.setBackground(new java.awt.Color(234, 67, 53));
            deleteBtn.setForeground(new java.awt.Color(255, 255, 255));
            deleteBtn.setFocusPainted(false);
            deleteBtn.setBorderPainted(false);

            add(deleteBtn);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            // The value parameter should be the row index
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                deleteBtn.setBackground(new java.awt.Color(234, 67, 53));
                deleteBtn.setForeground(new java.awt.Color(255, 255, 255));
            } else {
                setBackground(table.getBackground());
                deleteBtn.setBackground(new java.awt.Color(234, 67, 53));
                deleteBtn.setForeground(new java.awt.Color(255, 255, 255));
            }
            return this;
        }
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private JButton deleteBtn;
    private int currentRow; // Track the current row

    public ButtonEditor() {
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        panel.setOpaque(true);

        deleteBtn = new JButton("Delete");

        // Style button to be more compact - same as your existing buttons
        Dimension buttonSize = new Dimension(70, 25);
        deleteBtn.setPreferredSize(buttonSize);
        deleteBtn.setMaximumSize(buttonSize);
        deleteBtn.setMinimumSize(buttonSize);

        // Set button colors to match your inventory system
        deleteBtn.setBackground(new java.awt.Color(234, 67, 53));
        deleteBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);

        panel.add(deleteBtn);

        // Add action listener for cart item deletion
        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to remove this item from cart?", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                removeFromCart(currentRow);
            }
            fireEditingStopped();
        });
    }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            // Add bounds checking
            if (row >= 0 && row < cartItems.size()) {
                currentRow = row; // Store the actual row index, not the value from the cell
            } else {
                currentRow = -1; // Invalid row
            }

            if (isSelected) {
                deleteBtn.setBackground(new java.awt.Color(234, 67, 53));
                deleteBtn.setForeground(new java.awt.Color(255, 255, 255));
            } else {
                deleteBtn.setBackground(new java.awt.Color(234, 67, 53));
                deleteBtn.setForeground(new java.awt.Color(255, 255, 255));
            }

            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return currentRow;
        }
    }

    // Update the refreshTable method to use the new renderer/editor
    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) CartOrderTable.getModel();
        model.setRowCount(0);

        List<User> users = UserAuthentication.getAllUsers();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            Object[] row = {
                i + 1,
                user.getUsername(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUsername()  // Store username for actions instead of button panel
            };
            model.addRow(row);
        }

        // Set up the renderer and editor for the Actions column
        CartOrderTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        CartOrderTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor());
    }
    
    private void addToCart() {
        String itemName = AddItemNameTextBar.getText().trim();
        String quantityText = AddItemQuantityTextBar.getText().trim();

        // Check if we have a valid product displayed
        if (ProductNameLabel.getText().isEmpty() || ProductNameLabel.getText().equals("Product not found")) {
            JOptionPane.showMessageDialog(this, "Please search for a valid product first", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter quantity", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Find the product that's currently displayed (from the search results)
            List<Product> foundProducts = ProductManager.searchProducts(itemName);
            if (foundProducts.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Product not found: " + itemName, "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Product product = foundProducts.get(0); // Get first match

            // Check stock availability
            int availableStock = InventoryManager.getStockQuantity(product.getId());
            if (availableStock < quantity) {
                JOptionPane.showMessageDialog(this, 
                    "Insufficient stock! Available: " + availableStock, 
                    "Stock Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Check if this product is already in the cart
            boolean productExists = false;
            for (CartItem item : cartItems) {
                if (item.getProduct().getId() == product.getId()) {
                    // Update existing item quantity
                    item.quantity += quantity;
                    productExists = true;

                    // If processing existing order, update database
                    if (isProcessingExistingOrder && currentProcessingOrderId != -1) {
                        OrderManager.updateOrderItemQuantity(
                            currentProcessingOrderId, 
                            product.getId(), 
                            item.getQuantity()
                        );
                    }
                    break;
                }
            }

            // If product doesn't exist in cart, add it
            if (!productExists) {
                CartItem cartItem = new CartItem(product, quantity);
                cartItems.add(cartItem);

                // If processing existing order, add to database
                if (isProcessingExistingOrder && currentProcessingOrderId != -1) {
                    OrderManager.addOrderItem(
                        currentProcessingOrderId, 
                        product.getId(), 
                        quantity,
                        true
                    );
                }
            }
            
            if (isProcessingExistingOrder && currentProcessingOrderId != -1) {
                OrderManager.updateOrderTotal(currentProcessingOrderId);

                // Refresh the order from database to get updated total
                Order updatedOrder = OrderManager.getOrderById(currentProcessingOrderId);
                if (updatedOrder != null) {
                    totalAmount = updatedOrder.getTotalAmount();
                }
            }

            // Update UI
            updateCartTable();
            updateTotalAmount(); // This should now update both tabs properly

            // Clear quantity field but keep the product name for adding more
            AddItemQuantityTextBar.setText("");
            
            // Force update of payment tab if we're on it
            if (jTabbedPane1.getSelectedIndex() == 1) {
                calculateChange();
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity number", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void updateOrderDetailsDisplay(int orderId) {
        Order order = orderMap.get(orderId);
        if (order != null) {
            ItemNameLabel.setText("Order #" + orderId);
            ItemPriceLabel.setText(String.format("₱%.2f", totalAmount)); // Update with current total
            ItemDescriptionLabel.setText("Order being modified - Current Total: ₱" + String.format("%.2f", totalAmount));
        }
    }
    
    private void removeFromCart(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < cartItems.size()) {
            CartItem removedItem = cartItems.get(rowIndex);

            // If processing an existing order, delete from database too
            if (isProcessingExistingOrder && currentProcessingOrderId != -1) {
                try {
                    // 1. Delete from database
                    boolean dbSuccess = OrderManager.deleteOrderItem(
                        currentProcessingOrderId, 
                        removedItem.getProduct().getId()
                    );

                    if (!dbSuccess) {
                        JOptionPane.showMessageDialog(this, 
                            "Failed to remove item from database", 
                            "Database Error", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // 2. Update order total in database
                    OrderManager.updateOrderTotal(currentProcessingOrderId);

                    // 3. Restore inventory
                    boolean inventoryRestored = InventoryManager.updateStock(
                        removedItem.getProduct().getId(), 
                        removedItem.getQuantity() // Positive value to add back to stock
                    );

                    if (!inventoryRestored) {
                        System.err.println("Warning: Failed to restore inventory for product: " + 
                                          removedItem.getProduct().getName());
                    }

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, 
                        "Error during database operations: " + e.getMessage(), 
                        "Database Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Remove from local cart
            cartItems.remove(rowIndex);
            updateCartTable();
            updateTotalAmount();

            if (jTabbedPane1.getSelectedIndex() == 1) {
                calculateChange();
            }
        }
    }
    
    private void searchProduct() {
        String itemName = AddItemNameTextBar.getText().trim();

        // Clear product labels if search field is empty
        if (itemName.isEmpty()) {
            ProductNameLabel.setText("");
            ProductPriceLabel.setText("");
            ProductDescriptionLabel.setText("");
            return;
        }

        // Find product in database
        List<Product> foundProducts = ProductManager.searchProducts(itemName);
        if (foundProducts.isEmpty()) {
            // Don't show error message for real-time search, just clear the labels
            ProductNameLabel.setText("Product not found");
            ProductPriceLabel.setText("");
            ProductDescriptionLabel.setText("");
            return;
        }

        Product product = foundProducts.get(0); // Get first match

        // Update the product details labels (NOT the order labels)
        ProductNameLabel.setText(product.getName());
        ProductPriceLabel.setText(String.format("₱%.2f", product.getPrice()));

        // Use description if available, otherwise use category
        if (product.getDescription() != null && !product.getDescription().isEmpty()) {
            ProductDescriptionLabel.setText(product.getDescription());
        } else {
            ProductDescriptionLabel.setText(product.getCategory());
        }
    }
    
    private void updateCartTable() {
        DefaultTableModel model = (DefaultTableModel) CartOrderTable.getModel();
        model.setRowCount(0); // Clear table

        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            Product product = item.getProduct();
            Object[] row = {
                product.getId(),
                product.getName(),
                item.getQuantity(), // This will be editable
                product.getPrice(),
                product.getCategory(),
                item.getSubtotal(),
                i  // Store the current row index for actions
            };
            model.addRow(row);
        }

        // Set up the renderer and editor for the Actions column (Column 6)
        if (CartOrderTable.getColumnModel().getColumnCount() > 6) {
            CartOrderTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
            CartOrderTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor());
        }

        // Fire table data changed to ensure UI updates
        model.fireTableDataChanged();
    }
    
    private void updateTotalAmount() {
        totalAmount = 0.0;
        for (CartItem item : cartItems) {
            totalAmount += item.getSubtotal();
        }

        System.out.println("Total amount calculated: " + totalAmount);

        // Update main tab label - use Unicode for Philippine Peso
        TotalAmountDueTitleLabel.setText("Total Amount Due: \u20B1" + String.format("%.2f", totalAmount));

        // Update payment tab text field - use Unicode for Philippine Peso
        TotalAmountTextBar.setText("\u20B1" + String.format("%.2f", totalAmount));

        System.out.println("Text field set to: " + TotalAmountTextBar.getText());

        // Force UI update
        TotalAmountTextBar.revalidate();
        TotalAmountTextBar.repaint();

        // ALSO update the AmountDetailLabel if we want it to show current cart total
        ItemPriceLabel.setText("\u20B1" + String.format("%.2f", totalAmount));

        // Update order description if processing an existing order
        if (isProcessingExistingOrder && currentProcessingOrderId != -1) {
            ItemNameLabel.setText("Order #" + currentProcessingOrderId);
            ItemDescriptionLabel.setText("Modified Order - Current Total: \u20B1" + String.format("%.2f", totalAmount));
        }

        // Also trigger change calculation if we're on the payment tab
        if (jTabbedPane1.getSelectedIndex() == 1) {
            calculateChange();
        }
    }
    
    private void loadCustomers() {
        List<Customer> customers = CustomerManager.getAllCustomers();
        // Populate a dropdown or list for customer selection
    }
    
    private void checkout() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!", "Checkout Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate payment
        try {
            double paymentReceived = Double.parseDouble(PaymentReceivedTextBar.getText().replace("₱", "").trim());
            if (paymentReceived < totalAmount) {
                JOptionPane.showMessageDialog(this, "Payment received is less than total amount!", "Payment Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid payment amount", "Payment Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Process payment and create/update order
        boolean success;

        if (isProcessingExistingOrder && currentProcessingOrderId != -1) {
            // Update existing order (from queue or customer list)
            success = updateExistingOrder(currentProcessingOrderId);
        } else {
            // Create new order
            success = processOrder();
        }

        if (success) {
            try {
                double paymentReceived = Double.parseDouble(PaymentReceivedTextBar.getText().replace("₱", "").trim());
                double change = paymentReceived - totalAmount;

                // Generate and display receipt
                Receipt receipt = OrderSlipManager.generateReceipt(currentProcessingOrderId, paymentReceived, change);
                if (receipt != null) {
                    printService.displayReceipt(receipt);
                }
            } catch (NumberFormatException e) {
                System.err.println("Error generating receipt: " + e.getMessage());
            }

            JOptionPane.showMessageDialog(this, "Order processed successfully!", "Order Complete", JOptionPane.INFORMATION_MESSAGE);
            resetPOS();
            jTabbedPane1.setSelectedIndex(0);
        }
    }
    
    private void showPrintPreview(String title, String content) {
        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 500));

        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private boolean updateExistingOrder(int orderId) {
        try {
            Order order = orderMap.get(orderId);

            // Update order total amount with current cart items FIRST
            boolean totalUpdated = OrderManager.updateOrderTotalAmount(orderId, totalAmount);

            if (!totalUpdated) {
                JOptionPane.showMessageDialog(this, 
                    "Failed to update order total", 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Update order status to "Completed"
            boolean success = OrderManager.updateOrderStatus(orderId, "Completed");

            if (success) {
                // Update inventory for all items (subtract quantities)
                for (CartItem item : cartItems) {
                    boolean inventorySuccess = InventoryManager.updateStock(
                        item.getProduct().getId(), 
                        -item.getQuantity() // Negative value to subtract from stock
                    );

                    if (!inventorySuccess) {
                        JOptionPane.showMessageDialog(this, 
                            "Failed to update inventory for: " + item.getProduct().getName(), 
                            "Stock Error", 
                            JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }

                // Remove from queue if it was a queued order
                QueueManager.initializeQueue(); // Refresh the queue

                return true;
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating order: " + e.getMessage(), "Order Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private void resetPOS() {
        cartItems.clear();
        currentProcessingOrderId = -1;
        isProcessingExistingOrder = false;
        initializeCartTable();
        updateTotalAmount();
        TotalAmountTextBar.setText("₱0.00");
        PaymentReceivedTextBar.setText("");
        ChangeTextBar.setText("₱0.00");

        // Clear ALL fields including order details
        AddItemNameTextBar.setText("");
        AddItemQuantityTextBar.setText("");
        ItemNameLabel.setText("");
        ItemPriceLabel.setText("");
        ItemDescriptionLabel.setText("");
        ProductNameLabel.setText("");
        ProductPriceLabel.setText("");
        ProductDescriptionLabel.setText("");

        // Refresh orders list (this will remove completed orders from the list)
        loadCustomerOrders();
        QueueManager.initializeQueue();
    }
    
    private boolean processOrder() {
        try {
            // Get current user (cashier)
            String currentUser = backend.SessionManager.getCurrentUsername();
            User user = UserAuthentication.getUserByUsername(currentUser);

            if (user == null) {
                JOptionPane.showMessageDialog(this, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            int cashierId = user.getId();
            int defaultCustomerId = 1; // Should be replaced with customer selection UI
            String paymentMethod = "Cash"; // Default payment method

            // Create new order
            int orderId = OrderManager.createNewOrder(defaultCustomerId, cashierId, paymentMethod);

            if (orderId == -1) {
                JOptionPane.showMessageDialog(this, "Failed to create order", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Add all cart items to the order and update inventory
            for (CartItem item : cartItems) {
                // Add item to order
                boolean orderSuccess = OrderManager.addOrderItem(
                    orderId, 
                    item.getProduct().getId(), 
                    item.getQuantity(),
                    true
                );

                if (!orderSuccess) {
                    JOptionPane.showMessageDialog(this, "Failed to add item to order: " + item.getProduct().getName(), "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                // Update inventory stock (subtract quantity sold)
                boolean inventorySuccess = InventoryManager.updateStock(
                    item.getProduct().getId(), 
                    -item.getQuantity() // Negative value to subtract from stock
                );

                if (!inventorySuccess) {
                    JOptionPane.showMessageDialog(this, "Failed to update inventory for: " + item.getProduct().getName(), "Stock Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error processing order: " + e.getMessage(), "Order Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private void calculateChange() {
        try {
            String paymentText = PaymentReceivedTextBar.getText().replace("\u20B1", "").trim();
            if (paymentText.isEmpty()) {
                ChangeTextBar.setText("\u20B10.00");
                ChangeTextBar.setForeground(new java.awt.Color(31,40,35));
                return;
            }

            double paymentReceived = Double.parseDouble(paymentText);
            double change = paymentReceived - totalAmount;

            if (change < 0) {
                ChangeTextBar.setText("Insufficient payment");
                ChangeTextBar.setForeground(new java.awt.Color(234, 67, 53));
            } else {
                ChangeTextBar.setText("\u20B1" + String.format("%.2f", change));
                ChangeTextBar.setForeground(new java.awt.Color(31,40,35));
            }
        } catch (NumberFormatException e) {
            ChangeTextBar.setText("Invalid amount");
            ChangeTextBar.setForeground(new java.awt.Color(234, 67, 53));
        }
    }
    
    private void refreshOrdersList() {
        loadCustomerOrders(); // Reload the orders list

        // If we just processed an order from the queue, remove it from the list
        if (currentProcessingOrderId != -1) {
            // You might want to implement logic to remove the processed order from the JList
        }
    }
    
    private void loadOrderIntoPOS(int orderId) {
        Order order = OrderManager.getOrderById(orderId);
        List<OrderItem> items = OrderManager.getOrderItems(orderId);

        if (order == null || items == null) {
            JOptionPane.showMessageDialog(this, "Order details not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Clear current cart and load the selected order
        cartItems.clear();

        for (OrderItem orderItem : items) {
            Product product = ProductManager.getProductById(orderItem.getProductId());
            if (product != null) {
                CartItem cartItem = new CartItem(product, orderItem.getQuantity());
                cartItems.add(cartItem);
            }
        }

        // Update UI
        updateCartTable();
        updateTotalAmount();

        // Set the current processing order
        currentProcessingOrderId = orderId;
        isProcessingExistingOrder = true;

        // Show order information
        ItemNameLabel.setText("Order #" + orderId);
        ItemPriceLabel.setText(String.format("₱%.2f", order.getTotalAmount()));

        // Update description based on order status
        if (order.getStatus().equals("Pending")) {
            ItemDescriptionLabel.setText("Pending Order - Ready for Processing");
        } else {
            ItemDescriptionLabel.setText("Completed Order - View Only");
        }

        // Update the payment tab's total amount
        TotalAmountTextBar.setText(String.format("₱%.2f", totalAmount));
    }
    
    // Inner class for cart items
    private class CartItem {
        private Product product;
        private int quantity;

        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() { return product; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public double getSubtotal() { return product.getPrice() * quantity; }
    }
    
    private boolean processSelectedOrder(int orderId) {
        if (orderId == -1) {
            JOptionPane.showMessageDialog(this, "No orders available to process", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        
        try {
            Order order = orderMap.get(orderId);

            // If it's a pending order, switch to processing tab
            if (order != null && order.getStatus().equals("Pending")) {
                // Load the order details - refresh from database to get latest data
                loadOrderIntoPOS(orderId); // Use this instead of showOrderDetails

                // Update the total amount from the database
                Order currentOrder = OrderManager.getOrderById(orderId);
                if (currentOrder != null) {
                    totalAmount = currentOrder.getTotalAmount();
                }

                // Update UI
                updateTotalAmount();

                // Switch to the processing tab
                jTabbedPane1.setSelectedIndex(1);

                return true;
            } else {
                // For completed orders, just show a message
                JOptionPane.showMessageDialog(this, 
                    "This order is already completed. You can view it but cannot modify it.", 
                    "Order Completed", 
                    JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void refreshOrderFromDatabase(int orderId) {
        Order order = OrderManager.getOrderById(orderId);
        List<OrderItem> items = OrderManager.getOrderItems(orderId);

        if (order != null && items != null) {
            orderMap.put(orderId, order);
            orderItemsMap.put(orderId, items);

            // Update total amount from database
            totalAmount = order.getTotalAmount();
        }
    }
    
    private void startNewOrder() {
        // Clear everything and reset for a new order
        resetPOS();
        isProcessingExistingOrder = false;
        currentProcessingOrderId = -1;

        // Clear the order details labels
        ItemNameLabel.setText("");
        ItemPriceLabel.setText("");
        ItemDescriptionLabel.setText("");

        JOptionPane.showMessageDialog(this, "Ready for new order", "New Order", JOptionPane.INFORMATION_MESSAGE);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        MainPOSPanelTab = new javax.swing.JPanel();
        CartTablePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        CartOrderTable = new javax.swing.JTable();
        CustomerOrdersLabel1 = new javax.swing.JLabel();
        ItemDetailsPanel = new javax.swing.JPanel();
        DescriptionDetailLabel = new javax.swing.JLabel();
        ItemDescriptionLabel = new javax.swing.JLabel();
        AmountDetailLabel = new javax.swing.JLabel();
        NameDetailLabel = new javax.swing.JLabel();
        ItemNameLabel = new javax.swing.JLabel();
        ItemPriceLabel = new javax.swing.JLabel();
        ProcessOrderButton = new javax.swing.JButton();
        ItemDetailLabel = new javax.swing.JLabel();
        TotalAmountDueTitleLabel = new javax.swing.JLabel();
        ActionsPanel = new javax.swing.JPanel();
        AddItemNameTextBar = new javax.swing.JTextField();
        AddItemQuantityLabel = new javax.swing.JLabel();
        AddItemQuantityTextBar = new javax.swing.JTextField();
        AddItemNameLabel1 = new javax.swing.JLabel();
        ProductSearchLabel = new javax.swing.JLabel();
        CustomersListPanel = new javax.swing.JPanel();
        CustomerOrdersLabel = new javax.swing.JLabel();
        CustomerOrdersList = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        CostumersOrderIDList = new javax.swing.JList<>();
        LogoutButton = new javax.swing.JButton();
        ItemDetailsPanel1 = new javax.swing.JPanel();
        ProductDescriptionDetailLabel = new javax.swing.JLabel();
        ProductDescriptionLabel = new javax.swing.JLabel();
        ProductPriceDetailLabel = new javax.swing.JLabel();
        ProductNameDetailLabel = new javax.swing.JLabel();
        ProductNameLabel = new javax.swing.JLabel();
        ProductPriceLabel = new javax.swing.JLabel();
        ProcessOrderButton1 = new javax.swing.JButton();
        ProductDetailLabel = new javax.swing.JLabel();
        AddToCartButton = new javax.swing.JButton();
        POSTitleLabel2 = new javax.swing.JLabel();
        ProcessingOrderPanelTab = new javax.swing.JPanel();
        ActionsPanel1 = new javax.swing.JPanel();
        TotalAmountTextBar = new javax.swing.JTextField();
        ChangeLabel = new javax.swing.JLabel();
        ChangeTextBar = new javax.swing.JTextField();
        TotalAmountLabel = new javax.swing.JLabel();
        PaymentTitleLabel = new javax.swing.JLabel();
        PaymentReceivedTextBar = new javax.swing.JTextField();
        PaymentReceivedLabel = new javax.swing.JLabel();
        SubmitButton = new javax.swing.JButton();
        BackToPOSButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Point of Sale");
        setBackground(new java.awt.Color(201, 177, 158));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1000, 600));
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        MainPOSPanelTab.setBackground(new java.awt.Color(121, 63, 26));
        MainPOSPanelTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        MainPOSPanelTab.setForeground(new java.awt.Color(31, 40, 35));
        MainPOSPanelTab.setPreferredSize(new java.awt.Dimension(1000, 600));
        MainPOSPanelTab.setRequestFocusEnabled(false);
        MainPOSPanelTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CartTablePanel.setBackground(new java.awt.Color(201, 177, 158));
        CartTablePanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        CartTablePanel.setForeground(new java.awt.Color(31, 40, 35));
        CartTablePanel.setPreferredSize(new java.awt.Dimension(500, 200));
        CartTablePanel.setRequestFocusEnabled(false);
        CartTablePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CartOrderTable.setBackground(new java.awt.Color(249, 241, 240));
        CartOrderTable.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        CartOrderTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        CartOrderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item ID", "Item Name", "Quantity", "Price", "Category", "Sub Total", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.String.class, java.lang.Float.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        CartOrderTable.setFillsViewportHeight(true);
        CartOrderTable.setGridColor(new java.awt.Color(121, 63, 26));
        CartOrderTable.setRowHeight(30);
        CartOrderTable.setShowGrid(true);
        CartOrderTable.getTableHeader().setResizingAllowed(false);
        CartOrderTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(CartOrderTable);
        if (CartOrderTable.getColumnModel().getColumnCount() > 0) {
            CartOrderTable.getColumnModel().getColumn(0).setResizable(false);
            CartOrderTable.getColumnModel().getColumn(0).setPreferredWidth(5);
            CartOrderTable.getColumnModel().getColumn(1).setResizable(false);
            CartOrderTable.getColumnModel().getColumn(1).setPreferredWidth(100);
            CartOrderTable.getColumnModel().getColumn(2).setResizable(false);
            CartOrderTable.getColumnModel().getColumn(2).setPreferredWidth(20);
            CartOrderTable.getColumnModel().getColumn(3).setResizable(false);
            CartOrderTable.getColumnModel().getColumn(3).setPreferredWidth(20);
            CartOrderTable.getColumnModel().getColumn(4).setResizable(false);
            CartOrderTable.getColumnModel().getColumn(4).setPreferredWidth(100);
            CartOrderTable.getColumnModel().getColumn(5).setResizable(false);
            CartOrderTable.getColumnModel().getColumn(5).setPreferredWidth(50);
            CartOrderTable.getColumnModel().getColumn(6).setResizable(false);
            CartOrderTable.getColumnModel().getColumn(6).setPreferredWidth(70);
        }

        CartTablePanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 710, 280));

        CustomerOrdersLabel1.setBackground(new java.awt.Color(249, 241, 240));
        CustomerOrdersLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CustomerOrdersLabel1.setForeground(new java.awt.Color(255, 255, 255));
        CustomerOrdersLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CustomerOrdersLabel1.setText("ITEMS LIST");
        CustomerOrdersLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        CustomerOrdersLabel1.setPreferredSize(new java.awt.Dimension(480, 50));
        CartTablePanel.add(CustomerOrdersLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 730, -1));

        MainPOSPanelTab.add(CartTablePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 50, 730, 340));

        ItemDetailsPanel.setBackground(new java.awt.Color(201, 177, 158));
        ItemDetailsPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ItemDetailsPanel.setForeground(new java.awt.Color(31, 40, 35));
        ItemDetailsPanel.setPreferredSize(new java.awt.Dimension(500, 200));
        ItemDetailsPanel.setRequestFocusEnabled(false);
        ItemDetailsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        DescriptionDetailLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        DescriptionDetailLabel.setForeground(new java.awt.Color(31, 40, 35));
        DescriptionDetailLabel.setText("Description:");
        ItemDetailsPanel.add(DescriptionDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 90, 30));

        ItemDescriptionLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ItemDescriptionLabel.setForeground(new java.awt.Color(31, 40, 35));
        ItemDetailsPanel.add(ItemDescriptionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 100, 260, 30));

        AmountDetailLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        AmountDetailLabel.setForeground(new java.awt.Color(31, 40, 35));
        AmountDetailLabel.setText("Amount:");
        ItemDetailsPanel.add(AmountDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 90, 30));

        NameDetailLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        NameDetailLabel.setForeground(new java.awt.Color(31, 40, 35));
        NameDetailLabel.setText("Name: ");
        ItemDetailsPanel.add(NameDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 90, 30));

        ItemNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ItemNameLabel.setForeground(new java.awt.Color(31, 40, 35));
        ItemDetailsPanel.add(ItemNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 260, 30));

        ItemPriceLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ItemPriceLabel.setForeground(new java.awt.Color(31, 40, 35));
        ItemDetailsPanel.add(ItemPriceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 260, 30));

        ProcessOrderButton.setBackground(new java.awt.Color(255, 188, 5));
        ProcessOrderButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ProcessOrderButton.setText("PROCESS");
        ProcessOrderButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ProcessOrderButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ProcessOrderButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ProcessOrderButton.setIconTextGap(10);
        ProcessOrderButton.setPreferredSize(new java.awt.Dimension(120, 30));
        ProcessOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProcessOrderButtonActionPerformed(evt);
            }
        });
        ItemDetailsPanel.add(ProcessOrderButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 130, -1, -1));

        ItemDetailLabel.setBackground(new java.awt.Color(249, 241, 240));
        ItemDetailLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ItemDetailLabel.setForeground(new java.awt.Color(255, 255, 255));
        ItemDetailLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemDetailLabel.setText("ORDER DETAILS");
        ItemDetailLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ItemDetailLabel.setPreferredSize(new java.awt.Dimension(480, 50));
        ItemDetailsPanel.add(ItemDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 380, 30));

        MainPOSPanelTab.add(ItemDetailsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 400, 380, 170));

        TotalAmountDueTitleLabel.setBackground(new java.awt.Color(249, 241, 240));
        TotalAmountDueTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TotalAmountDueTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        TotalAmountDueTitleLabel.setText("Total Amount Due: ₱\" + String.format(\"%.2f");
        TotalAmountDueTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TotalAmountDueTitleLabel.setPreferredSize(new java.awt.Dimension(480, 50));
        MainPOSPanelTab.add(TotalAmountDueTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 370, 30));

        ActionsPanel.setBackground(new java.awt.Color(201, 177, 158));
        ActionsPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ActionsPanel.setForeground(new java.awt.Color(31, 40, 35));
        ActionsPanel.setPreferredSize(new java.awt.Dimension(500, 200));
        ActionsPanel.setRequestFocusEnabled(false);
        ActionsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        AddItemNameTextBar.setBackground(new java.awt.Color(249, 241, 240));
        AddItemNameTextBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AddItemNameTextBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        AddItemNameTextBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddItemNameTextBarActionPerformed(evt);
            }
        });
        ActionsPanel.add(AddItemNameTextBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 210, 30));

        AddItemQuantityLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        AddItemQuantityLabel.setForeground(new java.awt.Color(31, 40, 35));
        AddItemQuantityLabel.setText("Quantity");
        ActionsPanel.add(AddItemQuantityLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 90, 20));

        AddItemQuantityTextBar.setBackground(new java.awt.Color(249, 241, 240));
        AddItemQuantityTextBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AddItemQuantityTextBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        AddItemQuantityTextBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddItemQuantityTextBarActionPerformed(evt);
            }
        });
        ActionsPanel.add(AddItemQuantityTextBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 210, 30));

        AddItemNameLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        AddItemNameLabel1.setForeground(new java.awt.Color(31, 40, 35));
        AddItemNameLabel1.setText("Item Name");
        ActionsPanel.add(AddItemNameLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 90, 20));

        ProductSearchLabel.setBackground(new java.awt.Color(249, 241, 240));
        ProductSearchLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ProductSearchLabel.setForeground(new java.awt.Color(255, 255, 255));
        ProductSearchLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ProductSearchLabel.setText("SEARCH PRODUCT");
        ProductSearchLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ProductSearchLabel.setPreferredSize(new java.awt.Dimension(480, 50));
        ActionsPanel.add(ProductSearchLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 230, 30));

        MainPOSPanelTab.add(ActionsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 400, 230, 170));

        CustomersListPanel.setBackground(new java.awt.Color(201, 177, 158));
        CustomersListPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        CustomersListPanel.setForeground(new java.awt.Color(31, 40, 35));
        CustomersListPanel.setPreferredSize(new java.awt.Dimension(220, 380));
        CustomersListPanel.setRequestFocusEnabled(false);
        CustomersListPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CustomerOrdersLabel.setBackground(new java.awt.Color(249, 241, 240));
        CustomerOrdersLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        CustomerOrdersLabel.setForeground(new java.awt.Color(255, 255, 255));
        CustomerOrdersLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CustomerOrdersLabel.setText("ORDERS LIST");
        CustomerOrdersLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        CustomerOrdersLabel.setPreferredSize(new java.awt.Dimension(480, 50));
        CustomersListPanel.add(CustomerOrdersLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 190, 20));

        CustomerOrdersList.setBackground(new java.awt.Color(201, 177, 158));
        CustomerOrdersList.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        CustomerOrdersList.setForeground(new java.awt.Color(31, 40, 35));
        CustomerOrdersList.setPreferredSize(new java.awt.Dimension(180, 600));
        CustomerOrdersList.setRequestFocusEnabled(false);
        CustomerOrdersList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CostumersOrderIDList.setBackground(new java.awt.Color(249, 241, 240));
        CostumersOrderIDList.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CostumersOrderIDList.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        CostumersOrderIDList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        CostumersOrderIDList.setToolTipText("");
        CostumersOrderIDList.setPreferredSize(new java.awt.Dimension(170, 90));
        CostumersOrderIDList.setSelectionBackground(new java.awt.Color(173, 103, 48));
        jScrollPane3.setViewportView(CostumersOrderIDList);

        CustomerOrdersList.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 190, 270));

        CustomersListPanel.add(CustomerOrdersList, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 210, 290));

        MainPOSPanelTab.add(CustomersListPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 230, 340));

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
        MainPOSPanelTab.add(LogoutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 8, 110, 38));

        ItemDetailsPanel1.setBackground(new java.awt.Color(201, 177, 158));
        ItemDetailsPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ItemDetailsPanel1.setForeground(new java.awt.Color(31, 40, 35));
        ItemDetailsPanel1.setPreferredSize(new java.awt.Dimension(500, 200));
        ItemDetailsPanel1.setRequestFocusEnabled(false);
        ItemDetailsPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ProductDescriptionDetailLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ProductDescriptionDetailLabel.setForeground(new java.awt.Color(31, 40, 35));
        ProductDescriptionDetailLabel.setText("Description:");
        ItemDetailsPanel1.add(ProductDescriptionDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 90, 30));

        ProductDescriptionLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ProductDescriptionLabel.setForeground(new java.awt.Color(31, 40, 35));
        ItemDetailsPanel1.add(ProductDescriptionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 100, 220, 30));

        ProductPriceDetailLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ProductPriceDetailLabel.setForeground(new java.awt.Color(31, 40, 35));
        ProductPriceDetailLabel.setText("Price:");
        ItemDetailsPanel1.add(ProductPriceDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 90, 30));

        ProductNameDetailLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ProductNameDetailLabel.setForeground(new java.awt.Color(31, 40, 35));
        ProductNameDetailLabel.setText("Name: ");
        ItemDetailsPanel1.add(ProductNameDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 90, 30));

        ProductNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ProductNameLabel.setForeground(new java.awt.Color(31, 40, 35));
        ItemDetailsPanel1.add(ProductNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 220, 30));

        ProductPriceLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ProductPriceLabel.setForeground(new java.awt.Color(31, 40, 35));
        ItemDetailsPanel1.add(ProductPriceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 220, 30));

        ProcessOrderButton1.setBackground(new java.awt.Color(255, 188, 5));
        ProcessOrderButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ProcessOrderButton1.setText("PROCESS");
        ProcessOrderButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ProcessOrderButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ProcessOrderButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ProcessOrderButton1.setIconTextGap(10);
        ProcessOrderButton1.setPreferredSize(new java.awt.Dimension(120, 40));
        ProcessOrderButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProcessOrderButton1ActionPerformed(evt);
            }
        });
        ItemDetailsPanel1.add(ProcessOrderButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 120, -1, 30));

        ProductDetailLabel.setBackground(new java.awt.Color(249, 241, 240));
        ProductDetailLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ProductDetailLabel.setForeground(new java.awt.Color(255, 255, 255));
        ProductDetailLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ProductDetailLabel.setText("PRODUCT DETAILS");
        ProductDetailLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ProductDetailLabel.setPreferredSize(new java.awt.Dimension(480, 50));
        ItemDetailsPanel1.add(ProductDetailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 340, 30));

        AddToCartButton.setBackground(new java.awt.Color(66, 133, 244));
        AddToCartButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AddToCartButton.setText("ADD TO CART");
        AddToCartButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        AddToCartButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddToCartButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        AddToCartButton.setIconTextGap(10);
        AddToCartButton.setPreferredSize(new java.awt.Dimension(120, 30));
        AddToCartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddToCartButtonActionPerformed(evt);
            }
        });
        ItemDetailsPanel1.add(AddToCartButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, -1, -1));

        MainPOSPanelTab.add(ItemDetailsPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 340, 170));

        POSTitleLabel2.setBackground(new java.awt.Color(249, 241, 240));
        POSTitleLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        POSTitleLabel2.setForeground(new java.awt.Color(255, 255, 255));
        POSTitleLabel2.setText("POS SYSTEM");
        POSTitleLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        POSTitleLabel2.setPreferredSize(new java.awt.Dimension(480, 50));
        MainPOSPanelTab.add(POSTitleLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 210, 30));

        jTabbedPane1.addTab("tab1", MainPOSPanelTab);

        ProcessingOrderPanelTab.setBackground(new java.awt.Color(121, 63, 26));
        ProcessingOrderPanelTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ProcessingOrderPanelTab.setForeground(new java.awt.Color(31, 40, 35));
        ProcessingOrderPanelTab.setPreferredSize(new java.awt.Dimension(1000, 600));
        ProcessingOrderPanelTab.setRequestFocusEnabled(false);
        ProcessingOrderPanelTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ActionsPanel1.setBackground(new java.awt.Color(201, 177, 158));
        ActionsPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ActionsPanel1.setForeground(new java.awt.Color(31, 40, 35));
        ActionsPanel1.setPreferredSize(new java.awt.Dimension(500, 200));
        ActionsPanel1.setRequestFocusEnabled(false);
        ActionsPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TotalAmountTextBar.setEditable(false);
        TotalAmountTextBar.setBackground(new java.awt.Color(249, 241, 240));
        TotalAmountTextBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TotalAmountTextBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        TotalAmountTextBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TotalAmountTextBarActionPerformed(evt);
            }
        });
        ActionsPanel1.add(TotalAmountTextBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 160, 310, 30));

        ChangeLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ChangeLabel.setForeground(new java.awt.Color(31, 40, 35));
        ChangeLabel.setText("Change:");
        ActionsPanel1.add(ChangeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 270, 310, 20));

        ChangeTextBar.setEditable(false);
        ChangeTextBar.setBackground(new java.awt.Color(249, 241, 240));
        ChangeTextBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ChangeTextBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ChangeTextBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChangeTextBarActionPerformed(evt);
            }
        });
        ActionsPanel1.add(ChangeTextBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 300, 310, 30));

        TotalAmountLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        TotalAmountLabel.setForeground(new java.awt.Color(31, 40, 35));
        TotalAmountLabel.setText("Total Amount:");
        ActionsPanel1.add(TotalAmountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 130, 310, 20));

        PaymentTitleLabel.setBackground(new java.awt.Color(249, 241, 240));
        PaymentTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        PaymentTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        PaymentTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PaymentTitleLabel.setText("PAYMENT");
        PaymentTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        PaymentTitleLabel.setPreferredSize(new java.awt.Dimension(480, 50));
        ActionsPanel1.add(PaymentTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 920, 90));

        PaymentReceivedTextBar.setBackground(new java.awt.Color(249, 241, 240));
        PaymentReceivedTextBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        PaymentReceivedTextBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        PaymentReceivedTextBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PaymentReceivedTextBarActionPerformed(evt);
            }
        });
        ActionsPanel1.add(PaymentReceivedTextBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 230, 310, 30));
        PaymentReceivedTextBar.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                calculateChange();
            }

            public void removeUpdate(DocumentEvent e) {
                calculateChange();
            }

            public void changedUpdate(DocumentEvent e) {
                calculateChange();
            }
        });

        PaymentReceivedLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        PaymentReceivedLabel.setForeground(new java.awt.Color(31, 40, 35));
        PaymentReceivedLabel.setText("Payment Received:");
        ActionsPanel1.add(PaymentReceivedLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 200, 310, 20));

        SubmitButton.setBackground(new java.awt.Color(52, 168, 83));
        SubmitButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        SubmitButton.setText("SUBMIT");
        SubmitButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        SubmitButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        SubmitButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        SubmitButton.setIconTextGap(10);
        SubmitButton.setPreferredSize(new java.awt.Dimension(120, 40));
        SubmitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubmitButtonActionPerformed(evt);
            }
        });
        ActionsPanel1.add(SubmitButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 360, 310, 30));

        BackToPOSButton.setBackground(new java.awt.Color(66, 133, 244));
        BackToPOSButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        BackToPOSButton.setText("GO BACK");
        BackToPOSButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        BackToPOSButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BackToPOSButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        BackToPOSButton.setIconTextGap(10);
        BackToPOSButton.setPreferredSize(new java.awt.Dimension(120, 30));
        BackToPOSButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackToPOSButtonActionPerformed(evt);
            }
        });
        ActionsPanel1.add(BackToPOSButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 430, -1, -1));

        ProcessingOrderPanelTab.add(ActionsPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 920, 510));

        jTabbedPane1.addTab("tab1", ProcessingOrderPanelTab);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -30, -1, 630));
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
                if (jTabbedPane1.getSelectedIndex() == 1) { // Payment tab
                    // Refresh order data from database if processing existing order
                    if (isProcessingExistingOrder && currentProcessingOrderId != -1) {
                        refreshOrderFromDatabase(currentProcessingOrderId);
                    }
                    updateTotalAmount(); // Refresh the total when switching to payment tab
                    calculateChange();   // Recalculate change
                }
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void LogoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutButtonActionPerformed
        KioskFrame KioskFrame = new KioskFrame();
        KioskFrame.setVisible(true);
        KioskFrame.pack();
        KioskFrame.setLocationRelativeTo(null);
        backend.SessionManager.logout();
        this.dispose();
    }//GEN-LAST:event_LogoutButtonActionPerformed

    private void SubmitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubmitButtonActionPerformed
        checkout();
    }//GEN-LAST:event_SubmitButtonActionPerformed

    private void AddToCartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddToCartButtonActionPerformed
        addToCart();
    }//GEN-LAST:event_AddToCartButtonActionPerformed

    private void AddItemNameTextBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddItemNameTextBarActionPerformed
        AddItemQuantityTextBar.requestFocus();
    }//GEN-LAST:event_AddItemNameTextBarActionPerformed

    private void AddItemQuantityTextBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddItemQuantityTextBarActionPerformed
        addToCart();
    }//GEN-LAST:event_AddItemQuantityTextBarActionPerformed

    private void ProcessOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProcessOrderButtonActionPerformed
        if (CostumersOrderIDList.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order first", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedValue = CostumersOrderIDList.getSelectedValue();

        // Check if it's the "No Orders" message
        if ("No Orders".equals(selectedValue)) {
            JOptionPane.showMessageDialog(this, "No orders available to process", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int orderId = extractOrderId(selectedValue);

        // Process the order (switch to processing tab)
        boolean success = processSelectedOrder(orderId);

        if (!success) {
            JOptionPane.showMessageDialog(this, "Failed to process order", "Error", JOptionPane.ERROR_MESSAGE);
        }

        updateTotalAmount();
    }//GEN-LAST:event_ProcessOrderButtonActionPerformed

    private void TotalAmountTextBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TotalAmountTextBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TotalAmountTextBarActionPerformed

    private void ChangeTextBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChangeTextBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChangeTextBarActionPerformed

    private void PaymentReceivedTextBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PaymentReceivedTextBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PaymentReceivedTextBarActionPerformed

    private void ProcessOrderButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProcessOrderButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ProcessOrderButton1ActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        if (jTabbedPane1.getSelectedIndex() == 1) { // Payment tab
            updateTotalAmount(); // Refresh the total when switching to payment tab
            calculateChange();   // Recalculate change
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void BackToPOSButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackToPOSButtonActionPerformed
        jTabbedPane1.setSelectedIndex(0);
        resetPOS();
    }//GEN-LAST:event_BackToPOSButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ActionsPanel;
    private javax.swing.JPanel ActionsPanel1;
    private javax.swing.JLabel AddItemNameLabel1;
    private javax.swing.JTextField AddItemNameTextBar;
    private javax.swing.JLabel AddItemQuantityLabel;
    private javax.swing.JTextField AddItemQuantityTextBar;
    private javax.swing.JButton AddToCartButton;
    private javax.swing.JLabel AmountDetailLabel;
    private javax.swing.JButton BackToPOSButton;
    private javax.swing.JTable CartOrderTable;
    private javax.swing.JPanel CartTablePanel;
    private javax.swing.JLabel ChangeLabel;
    private javax.swing.JTextField ChangeTextBar;
    private javax.swing.JList<String> CostumersOrderIDList;
    private javax.swing.JLabel CustomerOrdersLabel;
    private javax.swing.JLabel CustomerOrdersLabel1;
    private javax.swing.JPanel CustomerOrdersList;
    private javax.swing.JPanel CustomersListPanel;
    private javax.swing.JLabel DescriptionDetailLabel;
    private javax.swing.JLabel ItemDescriptionLabel;
    private javax.swing.JLabel ItemDetailLabel;
    private javax.swing.JPanel ItemDetailsPanel;
    private javax.swing.JPanel ItemDetailsPanel1;
    private javax.swing.JLabel ItemNameLabel;
    private javax.swing.JLabel ItemPriceLabel;
    private javax.swing.JButton LogoutButton;
    private javax.swing.JPanel MainPOSPanelTab;
    private javax.swing.JLabel NameDetailLabel;
    private javax.swing.JLabel POSTitleLabel2;
    private javax.swing.JLabel PaymentReceivedLabel;
    private javax.swing.JTextField PaymentReceivedTextBar;
    private javax.swing.JLabel PaymentTitleLabel;
    private javax.swing.JButton ProcessOrderButton;
    private javax.swing.JButton ProcessOrderButton1;
    private javax.swing.JPanel ProcessingOrderPanelTab;
    private javax.swing.JLabel ProductDescriptionDetailLabel;
    private javax.swing.JLabel ProductDescriptionLabel;
    private javax.swing.JLabel ProductDetailLabel;
    private javax.swing.JLabel ProductNameDetailLabel;
    private javax.swing.JLabel ProductNameLabel;
    private javax.swing.JLabel ProductPriceDetailLabel;
    private javax.swing.JLabel ProductPriceLabel;
    private javax.swing.JLabel ProductSearchLabel;
    private javax.swing.JButton SubmitButton;
    private javax.swing.JLabel TotalAmountDueTitleLabel;
    private javax.swing.JLabel TotalAmountLabel;
    private javax.swing.JTextField TotalAmountTextBar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
