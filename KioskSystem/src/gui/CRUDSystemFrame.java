package gui;

import java.awt.Color;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import objects.*;
import services.*;

public class CRUDSystemFrame extends javax.swing.JFrame {
    
    private List<Product> currentProducts;
    private int currentProductIdForUpdate = 0;
    
    public CRUDSystemFrame() {
        initComponents();
        
        initApp();
        
        updateSidebarButtonSelection();
        
        ProductChooseImageFileButton.setForeground(Color.BLACK);
        AddProductConfirmButton.setForeground(Color.BLACK);
        ProductChooseImageFileUpdateButton.setForeground(Color.BLACK);
        ProductAddItemConfirmUpdateButton.setForeground(Color.BLACK);
        ProductAddItemGoBackButton.setForeground(Color.BLACK);
        
        FilterChoice.setFocusable(false);
    }
    
    private void initApp() {
        try {
            initializeData();
            initializeFilterOptions();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), 
                                        "Connection Error", JOptionPane.ERROR_MESSAGE);
            int option = JOptionPane.showConfirmDialog(this, 
                "Would you like to retry the connection?", 
                "Connection Failed", 
                JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                initApp();
            } else {
                System.exit(1);
            }
        }
    }
    
    private void initializeFilterOptions() {
        FilterChoice.removeAll();

        FilterChoice.add("All Products");
        FilterChoice.add("Available Only");
        FilterChoice.add("Unavailable Only");
        FilterChoice.add("Hidden Products");

        FilterChoice.setFocusable(false);

        FilterChoice.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                filterInventoryTable();
            }
        });
    }
    
    private void initializeData() {
        currentProducts = ProductManager.getAllProducts();
        refreshInventoryTable();
    }
    
    private void refreshInventoryTable() {
        refreshInventoryTableSafely();
    }
    
    private void filterInventoryTable() {
        String searchTerm = ProductListSearchBar.getText().trim().toLowerCase();
        String filterChoice = FilterChoice.getSelectedItem();
        DefaultTableModel model = (DefaultTableModel) ProductListTable.getModel();

        List<Product> filteredProducts = new ArrayList<>();

        for (Product product : currentProducts) {

            boolean matchesSearch = searchTerm.isEmpty() ||
                product.getName().toLowerCase().contains(searchTerm) ||
                product.getDescription().toLowerCase().contains(searchTerm) ||
                String.valueOf(product.getId()).contains(searchTerm) ||
                String.valueOf(product.getPrice()).contains(searchTerm) ||
                (product.isAvailable() ? "yes" : "no").contains(searchTerm) ||
                (product.isArchived() ? "archived" : "").contains(searchTerm);

            if (!matchesSearch) {
                continue;
            }

            switch (filterChoice) {
                case "Available Only":
                    if (product.isAvailable() && !product.isArchived()) {
                        filteredProducts.add(product);
                    }
                    break;
                case "Unavailable Only":
                    if (!product.isAvailable() && !product.isArchived()) {
                        filteredProducts.add(product);
                    }
                    break;
                case "Hidden Products":
                    if (product.isArchived()) {
                        filteredProducts.add(product);
                    }
                    break;
                case "All Products":
                default:
                    if (!product.isArchived()) {
                        filteredProducts.add(product);
                    }
                    break;
            }
        }

        model.setRowCount(0);

        for (int i = 0; i < filteredProducts.size(); i++) {
            Product product = filteredProducts.get(i);

            Object[] row = {
                i + 1,
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.isAvailable(),
                product.getId(),
                product.isArchived()
            };
            model.addRow(row);
        }

        if (ProductListTable.getColumnModel().getColumnCount() > 7) {
            ProductListTable.getColumnModel().getColumn(5).setCellRenderer(new BooleanRenderer());
            ProductListTable.getColumnModel().getColumn(5).setCellEditor(new BooleanEditor());
            ProductListTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
            ProductListTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor());
            ProductListTable.getColumnModel().getColumn(7).setCellRenderer(new BooleanRenderer());
            ProductListTable.getColumnModel().getColumn(7).setCellEditor(new BooleanEditor());
        }
    }
    
    private void resetFilter() {
        FilterChoice.select("All Products");
        filterInventoryTable();
    }
    
    private void updateSidebarButtonSelection() {
        int selectedIndex = MainTabbedPane.getSelectedIndex();

        resetSidebarButtons();

        switch (selectedIndex) {
            case 0:
                setButtonSelected(ProductListButton, true);
                break;
            case 1:
                setButtonSelected(AddProductButton, true);
                break;
            case 2:
                setButtonSelected(ProductListButton, true);
                break;
        }
    }
    
    private void resetSidebarButtons() {
        setButtonSelected(ProductListButton, false);
        setButtonSelected(AddProductButton, false);
    }

    private void setButtonSelected(JButton button, boolean selected) {
        if (selected) {
            button.setBackground(new Color(31, 40, 35));
            button.setForeground(new Color(255, 255, 255));
            button.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 2));

            if (button == ProductListButton) {
                button.setIcon(new ImageIcon(getClass().getResource("/gui/Images/icons/items_selected.png")));
            } else if (button == AddProductButton)
                button.setIcon(new ImageIcon(getClass().getResource("/gui/Images/icons/queue_selected.png")));
        } else {
            button.setBackground(new Color(249, 241, 240));
            button.setForeground(new Color(0, 0, 0));
            button.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 2));

            if (button == ProductListButton) {
                button.setIcon(new ImageIcon(getClass().getResource("/gui/Images/icons/items.png")));
            } else if (button == AddProductButton)
                button.setIcon(new ImageIcon(getClass().getResource("/gui/Images/icons/queue.png")));
            }
    }
    
    private void addNewProduct() {
        try {
            String name = ProductNameTextBar.getText().trim();
            double price = Double.parseDouble(ProductPriceText.getText());
            String description = ProductDescriptionTextField.getText().trim();
            String imageFilename = ProductChooseImagePathTextField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a product name!");
                return;
            }

            if (description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a description!");
                return;
            }

            if (price <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid price (positive number)!");
                return;
            }
            
            if (imageFilename.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a product image!");
                return;
            }
            
            Product existingProduct = ProductManager.getProductByName(name);
            if (existingProduct != null) {
                JOptionPane.showMessageDialog(this, 
                    "Product name '" + name + "' already exists!\nPlease choose a different product name.",
                    "Duplicate Product Name",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            Product product = new Product(0, name, price, description, imageFilename, true);

            if (ProductManager.addProduct(product)) {
                JOptionPane.showMessageDialog(this, "Product added successfully!");
                clearAddItemForm();
                initializeData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add product!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid price!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage());
        }
    }
    
    private String extractFileName(String fullPath) {
        if (fullPath == null || fullPath.trim().isEmpty()) {
            return "default.png";
        }

        String fileName = fullPath;
        int lastSeparator = Math.max(fullPath.lastIndexOf('\\'), fullPath.lastIndexOf('/'));

        if (lastSeparator != -1 && lastSeparator < fullPath.length() - 1) {
            fileName = fullPath.substring(lastSeparator + 1);
        }

        return fileName;
    }
    
    private int getCurrentProductId() {
        if (currentProductIdForUpdate == 0) {
            JOptionPane.showMessageDialog(this, "Please select a product to update first!");
            return 0;
        }
        return currentProductIdForUpdate;
    }
    
    private void loadProductForUpdate(int productId) {
        try {
            Product product = ProductManager.getProductById(productId);
            if (product != null) {
                
                currentProductIdForUpdate = productId;

                ProductNameUpdateTextBar.setText(product.getName());
                ProductPriceUpdateText.setText(String.valueOf(product.getPrice()));
                ProductDescriptionUpdateTextField.setText(product.getDescription());

                String imagePath = product.getImagePath();
                if (imagePath != null && !imagePath.trim().isEmpty()) {
                    ProductChooseImagePathUpdateTextField.setText(imagePath);
                } else {
                    ProductChooseImagePathUpdateTextField.setText("default.png");
                }

                for (int i = 0; i < MainTabbedPane.getTabCount(); i++) {
                    if (MainTabbedPane.getTitleAt(i) != null && 
                        MainTabbedPane.getTitleAt(i).toLowerCase().contains("update")) {
                        MainTabbedPane.setSelectedIndex(i);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Product not found!");
            }
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(this, 
                "Error loading product details. Please try again.\nError: " + e.getMessage(),
                "Load Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void updateProduct() {
        try {
            String name = ProductNameUpdateTextBar.getText();
            double price = Double.parseDouble(ProductPriceUpdateText.getText());
            String description = ProductDescriptionUpdateTextField.getText();
            String imageFilename = ProductChooseImagePathUpdateTextField.getText();

            int productId = getCurrentProductId();
            if (productId == 0) {
                JOptionPane.showMessageDialog(this, "Please select a product to update first!");
                return;
            }
            
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a product name!");
                return;
            }

            if (description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a description!");
                return;
            }

            if (price <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid price (positive number)!");
                return;
            }
            
            if (imageFilename.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a product image!");
                return;
            }
            
            Product existingProduct = ProductManager.getProductByName(name);
            if (existingProduct != null) {
                JOptionPane.showMessageDialog(this, 
                    "Product name '" + name + "' already exists!\nPlease choose a different product name.",
                    "Duplicate Product Name",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Product product = new Product(productId, name, price, description, imageFilename);

            if (ProductManager.updateProduct(product)) {
                JOptionPane.showMessageDialog(this, "Product updated successfully!");
                
                currentProductIdForUpdate = 0;
                initializeData();
                MainTabbedPane.setSelectedIndex(0);
                resetSidebarButtons();
                setButtonSelected(ProductListButton, true);
                
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update product!");
                MainTabbedPane.setSelectedIndex(0);
                resetSidebarButtons();
                setButtonSelected(ProductListButton, true);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid data!");
        }
    }
    
    private void clearAddItemForm() {
        ProductNameTextBar.setText("");
        ProductPriceText.setText("");
        ProductDescriptionTextField.setText("");
        ProductChooseImagePathTextField.setText("default.png");
    }
    
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton updateBtn, deleteBtn, retrieveBtn;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
            setOpaque(true);

            updateBtn = new JButton("Update");
            deleteBtn = new JButton("Delete");
            retrieveBtn = new JButton("Retrieve");

            Dimension buttonSize = new Dimension(90, 30);
            updateBtn.setPreferredSize(buttonSize);
            updateBtn.setMaximumSize(buttonSize);
            updateBtn.setMinimumSize(buttonSize);

            deleteBtn.setPreferredSize(buttonSize);
            deleteBtn.setMaximumSize(buttonSize);
            deleteBtn.setMinimumSize(buttonSize);

            retrieveBtn.setPreferredSize(new Dimension(100, 30));
            retrieveBtn.setMaximumSize(new Dimension(100, 30));
            retrieveBtn.setMinimumSize(new Dimension(100, 30));
            
            retrieveBtn.setBackground(new Color(52, 168, 83));
            retrieveBtn.setForeground(Color.WHITE);

            add(updateBtn);
            add(deleteBtn);
            add(retrieveBtn);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            boolean isArchived = false;
            try {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                if (row < model.getRowCount() && model.getColumnCount() > 7) {
                    isArchived = (Boolean) model.getValueAt(row, 7);
                }
            } catch (Exception e) {
                isArchived = false;
            }

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                updateBtn.setBackground(new Color(66,133,244));
                deleteBtn.setBackground(new Color(234,67,53));
                retrieveBtn.setBackground(new Color(52,168,83));

                updateBtn.setForeground(Color.WHITE);
                deleteBtn.setForeground(Color.WHITE);
                retrieveBtn.setForeground(Color.WHITE);
            } else {
                setBackground(table.getBackground());
                updateBtn.setBackground(new Color(66,133,244));
                deleteBtn.setBackground(new Color(234,67,53));
                retrieveBtn.setBackground(new Color(52,168,83));

                updateBtn.setForeground(Color.WHITE);
                deleteBtn.setForeground(Color.WHITE);
                retrieveBtn.setForeground(Color.WHITE);
            }
            
            updateBtn.setVisible(!isArchived);
            deleteBtn.setVisible(!isArchived);
            retrieveBtn.setVisible(isArchived);

            return this;
        }
    }
    
    private void refreshInventoryTableSafely() {
        if (ProductListTable.isEditing()) {
            ProductListTable.getCellEditor().cancelCellEditing();
        }

        SwingUtilities.invokeLater(() -> {
            try {
                
                currentProducts = ProductManager.getAllProductsIncludingArchived();

                filterInventoryTable();

            } catch (Exception e) {
                System.err.println("Error refreshing table: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton updateBtn, deleteBtn, retrieveBtn;
        private int currentProductId;
        private int currentRow;
        private boolean isArchived;

        public ButtonEditor() {
            panel = new JPanel();
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            panel.setOpaque(true);

            updateBtn = new JButton("Update");
            deleteBtn = new JButton("Delete");
            retrieveBtn = new JButton("Retrieve");

            Dimension buttonSize = new Dimension(90, 30);
            updateBtn.setPreferredSize(buttonSize);
            updateBtn.setMaximumSize(buttonSize);
            updateBtn.setMinimumSize(buttonSize);

            deleteBtn.setPreferredSize(buttonSize);
            deleteBtn.setMaximumSize(buttonSize);
            deleteBtn.setMinimumSize(buttonSize);

            retrieveBtn.setPreferredSize(new Dimension(100, 30));
            retrieveBtn.setMaximumSize(new Dimension(100, 30));
            retrieveBtn.setMinimumSize(new Dimension(100, 30));
            
            retrieveBtn.setBackground(new Color(52, 168, 83));
            retrieveBtn.setForeground(Color.WHITE);

            panel.add(updateBtn);
            panel.add(deleteBtn);
            panel.add(retrieveBtn);

            updateBtn.addActionListener(e -> {
                fireEditingStopped();
                SwingUtilities.invokeLater(() -> {
                    MainTabbedPane.setSelectedIndex(2);
                    resetSidebarButtons();
                    loadProductForUpdate(currentProductId);
                });
            });

            deleteBtn.addActionListener(e -> {
                fireEditingStopped();
                SwingUtilities.invokeLater(() -> {
                    showDeleteConfirmationDialog(currentProductId);
                });
            });

            retrieveBtn.addActionListener(e -> {
                fireEditingStopped();
                SwingUtilities.invokeLater(() -> {
                    showRetrieveConfirmationDialog(currentProductId);
                });
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentProductId = (Integer) value;
            currentRow = row;
            
            try {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                if (row < model.getRowCount() && model.getColumnCount() > 7) {
                    isArchived = (Boolean) model.getValueAt(row, 7);
                }
            } catch (Exception e) {
                isArchived = false;
            }

            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
                updateBtn.setBackground(new Color(66,133,244));
                deleteBtn.setBackground(new Color(234,67,53));
                retrieveBtn.setBackground(new Color(52,168,83));

                updateBtn.setForeground(Color.WHITE);
                deleteBtn.setForeground(Color.WHITE);
                retrieveBtn.setForeground(Color.WHITE);
            } else {
                panel.setBackground(table.getBackground());
                updateBtn.setBackground(new Color(66,133,244));
                deleteBtn.setBackground(new Color(234,67,53));
                retrieveBtn.setBackground(new Color(52,168,83));

                updateBtn.setForeground(Color.WHITE);
                deleteBtn.setForeground(Color.WHITE);
                retrieveBtn.setForeground(Color.WHITE);
            }
            
            updateBtn.setVisible(!isArchived);
            deleteBtn.setVisible(!isArchived);
            retrieveBtn.setVisible(isArchived);

            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return currentProductId;
        }

        private void showRetrieveConfirmationDialog(int productId) {
            Product product = ProductManager.getProductById(productId);
            if (product == null) {
                JOptionPane.showMessageDialog(
                    CRUDSystemFrame.this,
                    "Product not found!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String productName = product.getName();

            int confirm = JOptionPane.showConfirmDialog(
                CRUDSystemFrame.this,
                "<html><b>Retrieve Product: " + productName + "</b><br><br>" +
                "This will restore the product and make it available in the system.<br>" +
                "The product will be visible in the kiosk and can be ordered again.</html>",
                "Confirm Product Retrieval",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                performRetrieveProduct(productId, productName);
            }
        }

        private void performRetrieveProduct(int productId, String productName) {
            try {
                if (ProductManager.restoreProduct(productId)) {
                    JOptionPane.showMessageDialog(
                        CRUDSystemFrame.this, 
                        "Product '" + productName + "' has been retrieved successfully!\n" +
                        "It is now available in the system and visible in the kiosk.",
                        "Product Retrieved",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    refreshInventoryTableSafely();
                } else {
                    JOptionPane.showMessageDialog(
                        CRUDSystemFrame.this, 
                        "Failed to retrieve product '" + productName + "'!",
                        "Retrieve Failed",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    CRUDSystemFrame.this, 
                    "Error retrieving product: " + ex.getMessage(),
                    "Retrieve Error",
                    JOptionPane.ERROR_MESSAGE
                );
                ex.printStackTrace();
            }
        }
        
        private void showDeleteConfirmationDialog(int productId) {
            Product product = ProductManager.getProductById(productId);
            if (product == null) {
                JOptionPane.showMessageDialog(
                    CRUDSystemFrame.this,
                    "Product not found!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String productName = product.getName();

            boolean hasOrders = ProductManager.hasExistingOrders(productId);

            if (hasOrders) {
                JOptionPane.showMessageDialog(
                    CRUDSystemFrame.this,
                    "<html><b>Cannot Hard Delete: " + productName + "</b><br><br>" +
                    "This product has existing orders in the system.<br>" +
                    "For data integrity, only <b>Soft Delete (Archive)</b> is available.<br><br>" +
                    "Soft delete will make the product unavailable for new orders<br>" +
                    "while preserving order history.</html>",
                    "Product Has Existing Orders",
                    JOptionPane.WARNING_MESSAGE
                );
                performSoftDeleteProduct(productId, productName);
            } else {
                showDeleteOptionsDialog(productId, productName);
            }
        }

        private void showDeleteOptionsDialog(int productId, String productName) {
            Object[] options = {"Soft Delete", "Hard Delete", "Cancel"};

            int choice = JOptionPane.showOptionDialog(
                CRUDSystemFrame.this,
                "<html><b>Delete Product: " + productName + "</b><br><br>" +
                "Choose deletion method:<br>" +
                "• <b>Soft Delete</b>: Archive product (can be restored)<br>" +
                "• <b>Hard Delete</b>: Permanently remove from database<br><br>" +
                "<font color='red'><b>Warning:</b> Hard delete cannot be undone!</font></html>",
                "Confirm Product Deletion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[2]
            );

            switch (choice) {
                case 0:
                    performSoftDeleteProduct(productId, productName);
                    break;
                case 1:
                    performHardDeleteProduct(productId, productName);
                    break;
                case 2:
                default:
                    break;
            }
        }

        private void performSoftDeleteProduct(int productId, String productName) {
            try {
                if (ProductManager.archiveProduct(productId)) {
                    JOptionPane.showMessageDialog(
                        CRUDSystemFrame.this, 
                        "Product '" + productName + "' has been archived successfully!\n" +
                        "It is now hidden from the kiosk but can be retrieved later.",
                        "Product Archived",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    refreshInventoryTableSafely();
                } else {
                    JOptionPane.showMessageDialog(
                        CRUDSystemFrame.this, 
                        "Failed to archive product '" + productName + "'!",
                        "Archive Failed",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    CRUDSystemFrame.this, 
                    "Error archiving product: " + ex.getMessage(),
                    "Archive Error",
                    JOptionPane.ERROR_MESSAGE
                );
                ex.printStackTrace();
            }
        }

        private void performHardDeleteProduct(int productId, String productName) {
            if (ProductManager.hasExistingOrders(productId)) {
                JOptionPane.showMessageDialog(
                    CRUDSystemFrame.this,
                    "<html><b>Cannot Delete: " + productName + "</b><br><br>" +
                    "This product now has existing orders and cannot be hard deleted.<br>" +
                    "It will be archived instead.</html>",
                    "Deletion Blocked",
                    JOptionPane.WARNING_MESSAGE
                );
                performSoftDeleteProduct(productId, productName);
                return;
            }

            int finalConfirm = JOptionPane.showConfirmDialog(
                CRUDSystemFrame.this,
                "<html><b>FINAL WARNING: This action cannot be undone!</b><br><br>" +
                "You are about to permanently delete:<br>" +
                "<b>" + productName + "</b><br><br>" +
                "This will remove all traces of this product from the database.<br>" +
                "<font color='red'><b>Are you absolutely sure?</b></font></html>",
                "Confirm Permanent Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (finalConfirm == JOptionPane.YES_OPTION) {
                try {
                    if (ProductManager.hardDeleteProduct(productId)) {
                        JOptionPane.showMessageDialog(
                            CRUDSystemFrame.this, 
                            "Product '" + productName + "' has been permanently deleted!",
                            "Product Deleted",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        refreshInventoryTableSafely();
                    } else {
                        JOptionPane.showMessageDialog(
                            CRUDSystemFrame.this, 
                            "Failed to delete product '" + productName + "'! " +
                            "The product may be referenced elsewhere in the system.",
                            "Deletion Failed",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        CRUDSystemFrame.this, 
                        "Error deleting product: " + ex.getMessage(),
                        "Deletion Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    ex.printStackTrace();
                }
            }
        }
    }
    
    class BooleanRenderer extends JPanel implements TableCellRenderer {
        private JCheckBox checkBox;

        public BooleanRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER));
            setOpaque(true);
            checkBox = new JCheckBox();
            checkBox.setEnabled(false);
            add(checkBox);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            if (value instanceof Boolean) {
                checkBox.setSelected((Boolean) value);
            }
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());

            } else {
                setBackground(table.getBackground());
            }
            
            return this;
        }
    }
    
    class BooleanEditor extends AbstractCellEditor implements TableCellEditor {
        private JCheckBox checkBox;
        private int currentProductId;
        private boolean currentValue;

        public BooleanEditor() {
            checkBox = new JCheckBox();      
            checkBox.addActionListener(e -> {
                fireEditingStopped();
                updateProductAvailability(currentProductId, checkBox.isSelected());
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (value instanceof Boolean) {
                currentValue = (Boolean) value;
                checkBox.setSelected(currentValue);
            }

            try {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                if (row < model.getRowCount()) {
                    currentProductId = (Integer) model.getValueAt(row, 1);
                }
            } catch (Exception e) {
                System.err.println("Error getting product ID: " + e.getMessage());
            }

            if (isSelected) {
                checkBox.setBackground(table.getSelectionBackground());
            } else {
                checkBox.setBackground(table.getBackground());
            }
            
            return checkBox;
        }

        @Override
        public Object getCellEditorValue() {
            return checkBox.isSelected();
        }

        private void updateProductAvailability(int productId, boolean isAvailable) {
            SwingUtilities.invokeLater(() -> {
                try {
                    if (ProductManager.updateProductAvailability(productId, isAvailable)) {
                        
                        refreshInventoryTableSafely();
                    } else {
                        JOptionPane.showMessageDialog(CRUDSystemFrame.this, "Failed to update product availability!");
                        
                        refreshInventoryTableSafely();
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(CRUDSystemFrame.this, "Error updating availability: " + e.getMessage());
                    refreshInventoryTableSafely();
                }
            });
        }
    }
    
    private void logout() {
        LoginFrame login = new LoginFrame();
        login.setVisible(true);
        login.pack();
        login.setLocationRelativeTo(null);
        this.dispose();
    }
    
    private void selectImageFile(TextField textField) {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        
        File productImagesDir = new File("product_images");
        String lastDir = prefs.get("LAST_IMAGE_DIR", 
            productImagesDir.exists() ? productImagesDir.getAbsolutePath() : System.getProperty("user.home"));

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(lastDir));
        
        if (!lastDir.contains("product_images")) {
            JOptionPane.showMessageDialog(this,
                "For images to display in the kiosk, please:\n" +
                "1. Place images in the 'product_images' folder\n" +
                "2. Select images from that location\n" +
                "3. Ensure images are properly formatted (JPG, PNG, etc.)",
                "Kiosk Image Requirements",
                JOptionPane.INFORMATION_MESSAGE);
        }

        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
            "Image files (JPG, JPEG, PNG, GIF, BMP)", 
            "jpg", "jpeg", "png", "gif", "bmp"
        );

        fileChooser.setFileFilter(imageFilter);

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String fileName = file.getName();
            textField.setText(fileName);

            prefs.put("LAST_IMAGE_DIR", file.getParent());
            
            if (!file.getParent().contains("product_images")) {
                JOptionPane.showMessageDialog(this,
                    "Warning: This image is not in the 'product_images' folder.\n" +
                    "It may not display correctly in the kiosk.\n" +
                    "Please move the image to the 'product_images' folder.",
                    "Image Location Warning",
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        LogoutButton = new javax.swing.JButton();
        SideBarPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        ProductListButton = new javax.swing.JButton();
        AddProductButton = new javax.swing.JButton();
        MainTabbedPane = new javax.swing.JTabbedPane();
        ProductListTab = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        ProductListTabTitleLabel1 = new javax.swing.JLabel();
        ProductListContentPanel = new javax.swing.JPanel();
        ProductListTablePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ProductListTable = new javax.swing.JTable();
        ProductListSearchBar = new javax.swing.JTextField();
        ProductListSearchLabel = new javax.swing.JLabel();
        FilterChoice = new java.awt.Choice();
        footer1 = new javax.swing.JPanel();
        AddProductTab = new javax.swing.JPanel();
        header2 = new javax.swing.JPanel();
        AddProductTitleLabel = new javax.swing.JLabel();
        AddProductContentPanel = new javax.swing.JPanel();
        AddProductPanel = new javax.swing.JPanel();
        ProductNameLabel = new javax.swing.JLabel();
        ProductNameTextBar = new javax.swing.JTextField();
        ProductPriceLabel = new javax.swing.JLabel();
        ProductPriceText = new javax.swing.JTextField();
        ProductDescriptionLabel = new javax.swing.JLabel();
        ProductDescriptionTextField = new javax.swing.JTextField();
        AddProductConfirmButton = new javax.swing.JButton();
        ProductChooseImageLabel = new javax.swing.JLabel();
        ProductChooseImagePathTextField = new java.awt.TextField();
        ProductChooseImageFileButton = new javax.swing.JButton();
        AddProductPanelLabel = new javax.swing.JLabel();
        footer3 = new javax.swing.JPanel();
        UpdateProductEntryTab = new javax.swing.JPanel();
        header7 = new javax.swing.JPanel();
        UpdateEntryTabTitleLabel = new javax.swing.JLabel();
        UpdateProductPanelContent = new javax.swing.JPanel();
        ProductNameUpdateLabel = new javax.swing.JLabel();
        ProductNameUpdateTextBar = new javax.swing.JTextField();
        ProductPriceUpdateLabel = new javax.swing.JLabel();
        ProductPriceUpdateText = new javax.swing.JTextField();
        ProductDescriptionUpdateLabel = new javax.swing.JLabel();
        ProductDescriptionUpdateTextField = new javax.swing.JTextField();
        ProductAddItemConfirmUpdateButton = new javax.swing.JButton();
        ProductChooseImageUpdateLabel = new javax.swing.JLabel();
        ProductChooseImagePathUpdateTextField = new java.awt.TextField();
        ProductChooseImageFileUpdateButton = new javax.swing.JButton();
        ProductAddItemGoBackButton = new javax.swing.JButton();
        footer8 = new javax.swing.JPanel();
        UpdateIProductPanelLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Crud");
        setBackground(new java.awt.Color(201, 177, 158));
        setLocationByPlatform(true);
        setMaximizedBounds(new java.awt.Rectangle(0, 0, 1000, 600));
        setMinimumSize(new java.awt.Dimension(1000, 600));
        setPreferredSize(new java.awt.Dimension(1015, 635));
        setResizable(false);
        setSize(new java.awt.Dimension(1015, 635));

        jPanel1.setBackground(new java.awt.Color(201, 177, 158));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 600));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LogoutButton.setBackground(new java.awt.Color(249, 241, 240));
        LogoutButton.setForeground(new java.awt.Color(31, 40, 35));
        LogoutButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/logout.png"))); // NOI18N
        LogoutButton.setText("LOGOUT");
        LogoutButton.setBorder(null);
        LogoutButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LogoutButton.setFocusable(false);
        LogoutButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        LogoutButton.setIconTextGap(10);
        LogoutButton.setPreferredSize(new java.awt.Dimension(150, 40));
        LogoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutButtonActionPerformed(evt);
            }
        });
        jPanel1.add(LogoutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 555, -1, -1));

        SideBarPanel.setBackground(new java.awt.Color(0, 0, 0));
        SideBarPanel.setPreferredSize(new java.awt.Dimension(150, 600));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/logos/smalldonmacblack.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        jLabel1.setPreferredSize(new java.awt.Dimension(96, 96));
        SideBarPanel.add(jLabel1);

        ProductListButton.setBackground(new java.awt.Color(249, 241, 240));
        ProductListButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/items.png"))); // NOI18N
        ProductListButton.setText("PRODUCT LIST");
        ProductListButton.setBorder(null);
        ProductListButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ProductListButton.setFocusable(false);
        ProductListButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ProductListButton.setIconTextGap(10);
        ProductListButton.setPreferredSize(new java.awt.Dimension(150, 100));
        ProductListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductListButtonActionPerformed(evt);
            }
        });
        SideBarPanel.add(ProductListButton);

        AddProductButton.setBackground(new java.awt.Color(249, 241, 240));
        AddProductButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/Images/icons/queue.png"))); // NOI18N
        AddProductButton.setText("ADD PRODUCT");
        AddProductButton.setBorder(null);
        AddProductButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddProductButton.setFocusable(false);
        AddProductButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        AddProductButton.setIconTextGap(10);
        AddProductButton.setPreferredSize(new java.awt.Dimension(150, 100));
        AddProductButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddProductButtonActionPerformed(evt);
            }
        });
        SideBarPanel.add(AddProductButton);

        jPanel1.add(SideBarPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        MainTabbedPane.setBackground(new java.awt.Color(249, 241, 240));
        MainTabbedPane.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        MainTabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        MainTabbedPane.setPreferredSize(new java.awt.Dimension(850, 600));

        ProductListTab.setBackground(new java.awt.Color(31, 40, 35));
        ProductListTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ProductListTab.setForeground(new java.awt.Color(31, 40, 35));
        ProductListTab.setRequestFocusEnabled(false);
        ProductListTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header.setBackground(new java.awt.Color(31, 40, 35));
        header.setPreferredSize(new java.awt.Dimension(1000, 50));
        header.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ProductListTabTitleLabel1.setBackground(new java.awt.Color(249, 241, 240));
        ProductListTabTitleLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        ProductListTabTitleLabel1.setForeground(new java.awt.Color(255, 255, 255));
        ProductListTabTitleLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ProductListTabTitleLabel1.setText("PRODUCT LIST");
        ProductListTabTitleLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header.add(ProductListTabTitleLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 50));

        ProductListTab.add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 50));

        ProductListContentPanel.setBackground(new java.awt.Color(249, 241, 240));
        ProductListContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        ProductListContentPanel.setRequestFocusEnabled(false);
        ProductListContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ProductListTablePanel.setBackground(new java.awt.Color(249, 241, 240));
        ProductListTablePanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ProductListTablePanel.setForeground(new java.awt.Color(31, 40, 35));
        ProductListTablePanel.setPreferredSize(new java.awt.Dimension(500, 200));
        ProductListTablePanel.setRequestFocusEnabled(false);
        ProductListTablePanel.setLayout(new javax.swing.BoxLayout(ProductListTablePanel, javax.swing.BoxLayout.LINE_AXIS));

        ProductListTable.setBackground(new java.awt.Color(249, 241, 240));
        ProductListTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(31, 40, 35), 2, true));
        ProductListTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ProductListTable.setForeground(new java.awt.Color(31, 40, 35));
        ProductListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                { new Integer(1),  new Integer(1), "Coffee", "This is a Coffee Blend",  new Double(39.0),  new Boolean(true), null, null}
            },
            new String [] {
                "#", "ID", "Name", "Description", "Price", "Available", "Actions", "Archived"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ProductListTable.setFillsViewportHeight(true);
        ProductListTable.setGridColor(new java.awt.Color(31, 40, 35));
        ProductListTable.setRowHeight(40);
        ProductListTable.setSelectionBackground(new java.awt.Color(31, 40, 35));
        ProductListTable.setSelectionForeground(new java.awt.Color(249, 241, 240));
        ProductListTable.setShowGrid(true);
        ProductListTable.getTableHeader().setResizingAllowed(false);
        ProductListTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(ProductListTable);
        if (ProductListTable.getColumnModel().getColumnCount() > 0) {
            ProductListTable.getColumnModel().getColumn(0).setResizable(false);
            ProductListTable.getColumnModel().getColumn(0).setPreferredWidth(5);
            ProductListTable.getColumnModel().getColumn(1).setResizable(false);
            ProductListTable.getColumnModel().getColumn(1).setPreferredWidth(5);
            ProductListTable.getColumnModel().getColumn(2).setResizable(false);
            ProductListTable.getColumnModel().getColumn(2).setPreferredWidth(75);
            ProductListTable.getColumnModel().getColumn(3).setResizable(false);
            ProductListTable.getColumnModel().getColumn(3).setPreferredWidth(150);
            ProductListTable.getColumnModel().getColumn(4).setResizable(false);
            ProductListTable.getColumnModel().getColumn(4).setPreferredWidth(20);
            ProductListTable.getColumnModel().getColumn(5).setResizable(false);
            ProductListTable.getColumnModel().getColumn(5).setPreferredWidth(25);
            ProductListTable.getColumnModel().getColumn(6).setResizable(false);
            ProductListTable.getColumnModel().getColumn(6).setPreferredWidth(175);
            ProductListTable.getColumnModel().getColumn(7).setResizable(false);
            ProductListTable.getColumnModel().getColumn(7).setPreferredWidth(25);
        }

        ProductListTablePanel.add(jScrollPane1);

        ProductListContentPanel.add(ProductListTablePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 770, 450));

        ProductListSearchBar.setBackground(new java.awt.Color(249, 241, 240));
        ProductListSearchBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ProductListSearchBar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 2));
        ProductListSearchBar.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                filterInventoryTable();
            }

            public void removeUpdate(DocumentEvent e) {
                filterInventoryTable();
            }

            public void changedUpdate(DocumentEvent e) {
                filterInventoryTable();
            }
        });
        ProductListSearchBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductListSearchBarActionPerformed(evt);
            }
        });
        ProductListContentPanel.add(ProductListSearchBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 180, -1));

        ProductListSearchLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ProductListSearchLabel.setForeground(new java.awt.Color(31, 40, 35));
        ProductListSearchLabel.setText("Search:");
        ProductListContentPanel.add(ProductListSearchLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 70, 40));

        FilterChoice.setBackground(new java.awt.Color(249, 241, 240));
        FilterChoice.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        FilterChoice.setFocusable(false);
        FilterChoice.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        FilterChoice.setForeground(new java.awt.Color(31, 40, 35));
        FilterChoice.setName("language"); // NOI18N
        FilterChoice.setPreferredSize(new java.awt.Dimension(150, 20));
        ProductListContentPanel.add(FilterChoice, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 10, -1, -1));

        ProductListTab.add(ProductListContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 790, 500));

        footer1.setBackground(new java.awt.Color(31, 40, 35));
        footer1.setPreferredSize(new java.awt.Dimension(990, 50));
        footer1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        ProductListTab.add(footer1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        MainTabbedPane.addTab("tab2", ProductListTab);

        AddProductTab.setBackground(new java.awt.Color(249, 241, 240));
        AddProductTab.setForeground(new java.awt.Color(31, 40, 35));
        AddProductTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header2.setBackground(new java.awt.Color(31, 40, 35));
        header2.setPreferredSize(new java.awt.Dimension(1000, 50));
        header2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        AddProductTitleLabel.setBackground(new java.awt.Color(249, 241, 240));
        AddProductTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        AddProductTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        AddProductTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AddProductTitleLabel.setText("ADD PRODUCT");
        AddProductTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header2.add(AddProductTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 50));

        AddProductTab.add(header2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, -1));

        AddProductContentPanel.setBackground(new java.awt.Color(249, 241, 240));
        AddProductContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        AddProductContentPanel.setRequestFocusEnabled(false);
        AddProductContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        AddProductPanel.setBackground(new java.awt.Color(31, 40, 35));
        AddProductPanel.setForeground(new java.awt.Color(31, 40, 35));
        AddProductPanel.setPreferredSize(new java.awt.Dimension(500, 200));
        AddProductPanel.setRequestFocusEnabled(false);
        AddProductPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ProductNameLabel.setBackground(new java.awt.Color(255, 255, 255));
        ProductNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ProductNameLabel.setForeground(new java.awt.Color(255, 255, 255));
        ProductNameLabel.setText("Product Name");
        AddProductPanel.add(ProductNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 360, 20));

        ProductNameTextBar.setBackground(new java.awt.Color(249, 241, 240));
        ProductNameTextBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ProductNameTextBar.setBorder(null);
        ProductNameTextBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductNameTextBarActionPerformed(evt);
            }
        });
        AddProductPanel.add(ProductNameTextBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 680, 30));

        ProductPriceLabel.setBackground(new java.awt.Color(255, 255, 255));
        ProductPriceLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ProductPriceLabel.setForeground(new java.awt.Color(255, 255, 255));
        ProductPriceLabel.setText("Product Price");
        AddProductPanel.add(ProductPriceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 360, 20));

        ProductPriceText.setBackground(new java.awt.Color(249, 241, 240));
        ProductPriceText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ProductPriceText.setBorder(null);
        ProductPriceText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductPriceTextActionPerformed(evt);
            }
        });
        AddProductPanel.add(ProductPriceText, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 680, 30));

        ProductDescriptionLabel.setBackground(new java.awt.Color(255, 255, 255));
        ProductDescriptionLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ProductDescriptionLabel.setForeground(new java.awt.Color(255, 255, 255));
        ProductDescriptionLabel.setText("Product Description");
        AddProductPanel.add(ProductDescriptionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 360, 20));

        ProductDescriptionTextField.setBackground(new java.awt.Color(249, 241, 240));
        ProductDescriptionTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ProductDescriptionTextField.setBorder(null);
        AddProductPanel.add(ProductDescriptionTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 680, 60));

        AddProductConfirmButton.setBackground(new java.awt.Color(249, 241, 240));
        AddProductConfirmButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AddProductConfirmButton.setText("ADD");
        AddProductConfirmButton.setBorder(null);
        AddProductConfirmButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddProductConfirmButton.setFocusable(false);
        AddProductConfirmButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        AddProductConfirmButton.setIconTextGap(10);
        AddProductConfirmButton.setPreferredSize(new java.awt.Dimension(120, 40));
        AddProductConfirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddProductConfirmButtonActionPerformed(evt);
            }
        });
        AddProductPanel.add(AddProductConfirmButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 120, 40));

        ProductChooseImageLabel.setBackground(new java.awt.Color(255, 255, 255));
        ProductChooseImageLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ProductChooseImageLabel.setForeground(new java.awt.Color(255, 255, 255));
        ProductChooseImageLabel.setText("Product Image");
        AddProductPanel.add(ProductChooseImageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 360, 20));

        ProductChooseImagePathTextField.setBackground(new java.awt.Color(249, 241, 240));
        ProductChooseImagePathTextField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        ProductChooseImagePathTextField.setEditable(false);
        ProductChooseImagePathTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ProductChooseImagePathTextField.setForeground(new java.awt.Color(31, 40, 35));
        ProductChooseImagePathTextField.setText("default.png");
        ProductChooseImagePathTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductChooseImagePathTextFieldActionPerformed(evt);
            }
        });
        AddProductPanel.add(ProductChooseImagePathTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 360, -1));

        ProductChooseImageFileButton.setBackground(new java.awt.Color(249, 241, 240));
        ProductChooseImageFileButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ProductChooseImageFileButton.setText("Choose Image");
        ProductChooseImageFileButton.setBorder(null);
        ProductChooseImageFileButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ProductChooseImageFileButton.setFocusable(false);
        ProductChooseImageFileButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ProductChooseImageFileButton.setIconTextGap(10);
        ProductChooseImageFileButton.setPreferredSize(new java.awt.Dimension(120, 40));
        ProductChooseImageFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductChooseImageFileButtonActionPerformed(evt);
            }
        });
        AddProductPanel.add(ProductChooseImageFileButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 190, 120, 40));

        AddProductContentPanel.add(AddProductPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 720, 440));

        AddProductPanelLabel.setBackground(new java.awt.Color(249, 241, 240));
        AddProductPanelLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        AddProductPanelLabel.setText("ADD NEW PRODUCT");
        AddProductPanelLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        AddProductContentPanel.add(AddProductPanelLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 720, 40));

        AddProductTab.add(AddProductContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 790, 500));

        footer3.setBackground(new java.awt.Color(31, 40, 35));
        footer3.setPreferredSize(new java.awt.Dimension(990, 50));
        footer3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        AddProductTab.add(footer3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 40));

        MainTabbedPane.addTab("tab3", AddProductTab);

        UpdateProductEntryTab.setBackground(new java.awt.Color(249, 241, 240));
        UpdateProductEntryTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        UpdateProductEntryTab.setForeground(new java.awt.Color(31, 40, 35));
        UpdateProductEntryTab.setRequestFocusEnabled(false);
        UpdateProductEntryTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header7.setBackground(new java.awt.Color(31, 40, 35));
        header7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        header7.setPreferredSize(new java.awt.Dimension(1000, 50));
        header7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        UpdateEntryTabTitleLabel.setBackground(new java.awt.Color(249, 241, 240));
        UpdateEntryTabTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        UpdateEntryTabTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        UpdateEntryTabTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UpdateEntryTabTitleLabel.setText("UPDATE ENTRY");
        UpdateEntryTabTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header7.add(UpdateEntryTabTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 50));

        UpdateProductEntryTab.add(header7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, -1));

        UpdateProductPanelContent.setBackground(new java.awt.Color(31, 40, 35));
        UpdateProductPanelContent.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        UpdateProductPanelContent.setForeground(new java.awt.Color(31, 40, 35));
        UpdateProductPanelContent.setPreferredSize(new java.awt.Dimension(500, 200));
        UpdateProductPanelContent.setRequestFocusEnabled(false);
        UpdateProductPanelContent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ProductNameUpdateLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ProductNameUpdateLabel.setForeground(new java.awt.Color(255, 255, 255));
        ProductNameUpdateLabel.setText("Product Name");
        UpdateProductPanelContent.add(ProductNameUpdateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 360, 20));

        ProductNameUpdateTextBar.setBackground(new java.awt.Color(249, 241, 240));
        ProductNameUpdateTextBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ProductNameUpdateTextBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ProductNameUpdateTextBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductNameUpdateTextBarActionPerformed(evt);
            }
        });
        UpdateProductPanelContent.add(ProductNameUpdateTextBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 680, 30));

        ProductPriceUpdateLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ProductPriceUpdateLabel.setForeground(new java.awt.Color(255, 255, 255));
        ProductPriceUpdateLabel.setText("Product Price");
        UpdateProductPanelContent.add(ProductPriceUpdateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 360, 20));

        ProductPriceUpdateText.setBackground(new java.awt.Color(249, 241, 240));
        ProductPriceUpdateText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ProductPriceUpdateText.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ProductPriceUpdateText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductPriceUpdateTextActionPerformed(evt);
            }
        });
        UpdateProductPanelContent.add(ProductPriceUpdateText, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 680, 30));

        ProductDescriptionUpdateLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ProductDescriptionUpdateLabel.setForeground(new java.awt.Color(255, 255, 255));
        ProductDescriptionUpdateLabel.setText("Product Description");
        UpdateProductPanelContent.add(ProductDescriptionUpdateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 360, 20));

        ProductDescriptionUpdateTextField.setBackground(new java.awt.Color(249, 241, 240));
        ProductDescriptionUpdateTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ProductDescriptionUpdateTextField.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        UpdateProductPanelContent.add(ProductDescriptionUpdateTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 680, 60));

        ProductAddItemConfirmUpdateButton.setBackground(new java.awt.Color(249, 241, 240));
        ProductAddItemConfirmUpdateButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ProductAddItemConfirmUpdateButton.setText("UPDATE");
        ProductAddItemConfirmUpdateButton.setBorder(null);
        ProductAddItemConfirmUpdateButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ProductAddItemConfirmUpdateButton.setFocusable(false);
        ProductAddItemConfirmUpdateButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ProductAddItemConfirmUpdateButton.setIconTextGap(10);
        ProductAddItemConfirmUpdateButton.setPreferredSize(new java.awt.Dimension(120, 40));
        ProductAddItemConfirmUpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductAddItemConfirmUpdateButtonActionPerformed(evt);
            }
        });
        UpdateProductPanelContent.add(ProductAddItemConfirmUpdateButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 120, 40));

        ProductChooseImageUpdateLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ProductChooseImageUpdateLabel.setForeground(new java.awt.Color(255, 255, 255));
        ProductChooseImageUpdateLabel.setText("Product Image");
        UpdateProductPanelContent.add(ProductChooseImageUpdateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 360, 20));

        ProductChooseImagePathUpdateTextField.setBackground(new java.awt.Color(249, 241, 240));
        ProductChooseImagePathUpdateTextField.setEditable(false);
        ProductChooseImagePathUpdateTextField.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        ProductChooseImagePathUpdateTextField.setForeground(new java.awt.Color(31, 40, 35));
        ProductChooseImagePathUpdateTextField.setText("default.png");
        ProductChooseImagePathUpdateTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductChooseImagePathUpdateTextFieldActionPerformed(evt);
            }
        });
        UpdateProductPanelContent.add(ProductChooseImagePathUpdateTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 360, -1));

        ProductChooseImageFileUpdateButton.setBackground(new java.awt.Color(249, 241, 240));
        ProductChooseImageFileUpdateButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ProductChooseImageFileUpdateButton.setText("Choose Image");
        ProductChooseImageFileUpdateButton.setBorder(null);
        ProductChooseImageFileUpdateButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ProductChooseImageFileUpdateButton.setFocusable(false);
        ProductChooseImageFileUpdateButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ProductChooseImageFileUpdateButton.setIconTextGap(10);
        ProductChooseImageFileUpdateButton.setPreferredSize(new java.awt.Dimension(120, 40));
        ProductChooseImageFileUpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductChooseImageFileUpdateButtonActionPerformed(evt);
            }
        });
        UpdateProductPanelContent.add(ProductChooseImageFileUpdateButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 190, 120, 40));

        ProductAddItemGoBackButton.setBackground(new java.awt.Color(249, 241, 240));
        ProductAddItemGoBackButton.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        ProductAddItemGoBackButton.setText("GO BACK");
        ProductAddItemGoBackButton.setBorder(null);
        ProductAddItemGoBackButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ProductAddItemGoBackButton.setFocusable(false);
        ProductAddItemGoBackButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ProductAddItemGoBackButton.setIconTextGap(10);
        ProductAddItemGoBackButton.setPreferredSize(new java.awt.Dimension(75, 30));
        ProductAddItemGoBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductAddItemGoBackButtonActionPerformed(evt);
            }
        });
        UpdateProductPanelContent.add(ProductAddItemGoBackButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 400, -1, -1));

        UpdateProductEntryTab.add(UpdateProductPanelContent, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 740, 440));

        footer8.setBackground(new java.awt.Color(31, 40, 35));
        footer8.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        footer8.setPreferredSize(new java.awt.Dimension(990, 50));
        footer8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        UpdateProductEntryTab.add(footer8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        UpdateIProductPanelLabel.setBackground(new java.awt.Color(249, 241, 240));
        UpdateIProductPanelLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        UpdateIProductPanelLabel.setText("UPDATE PRODUCT");
        UpdateIProductPanelLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        UpdateProductEntryTab.add(UpdateIProductPanelLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 730, 40));

        MainTabbedPane.addTab("tab8", UpdateProductEntryTab);

        jPanel1.add(MainTabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 910, 600));
        MainTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                updateSidebarButtonSelection();
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        getAccessibleContext().setAccessibleName("INVENTORY");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ProductListSearchBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductListSearchBarActionPerformed
        filterInventoryTable();
    }//GEN-LAST:event_ProductListSearchBarActionPerformed

    private void ProductNameTextBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductNameTextBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ProductNameTextBarActionPerformed

    private void ProductPriceTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductPriceTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ProductPriceTextActionPerformed

    private void AddProductConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddProductConfirmButtonActionPerformed
        addNewProduct();
    }//GEN-LAST:event_AddProductConfirmButtonActionPerformed

    private void ProductChooseImageFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductChooseImageFileButtonActionPerformed
        selectImageFile(ProductChooseImagePathTextField);
    }//GEN-LAST:event_ProductChooseImageFileButtonActionPerformed

    private void ProductChooseImagePathTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductChooseImagePathTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ProductChooseImagePathTextFieldActionPerformed

    private void ProductNameUpdateTextBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductNameUpdateTextBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ProductNameUpdateTextBarActionPerformed

    private void ProductPriceUpdateTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductPriceUpdateTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ProductPriceUpdateTextActionPerformed

    private void ProductAddItemConfirmUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductAddItemConfirmUpdateButtonActionPerformed
        updateProduct();
    }//GEN-LAST:event_ProductAddItemConfirmUpdateButtonActionPerformed

    private void ProductChooseImagePathUpdateTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductChooseImagePathUpdateTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ProductChooseImagePathUpdateTextFieldActionPerformed

    private void ProductChooseImageFileUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductChooseImageFileUpdateButtonActionPerformed
        selectImageFile(ProductChooseImagePathUpdateTextField);
    }//GEN-LAST:event_ProductChooseImageFileUpdateButtonActionPerformed

    private void AddProductButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddProductButtonActionPerformed
        MainTabbedPane.setSelectedIndex(1);
    }//GEN-LAST:event_AddProductButtonActionPerformed

    private void ProductListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductListButtonActionPerformed
        MainTabbedPane.setSelectedIndex(0);
        resetFilter();
    }//GEN-LAST:event_ProductListButtonActionPerformed

    private void LogoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutButtonActionPerformed
        logout();
    }//GEN-LAST:event_LogoutButtonActionPerformed

    private void ProductAddItemGoBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductAddItemGoBackButtonActionPerformed
        MainTabbedPane.setSelectedIndex(0);
    }//GEN-LAST:event_ProductAddItemGoBackButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddProductButton;
    private javax.swing.JButton AddProductConfirmButton;
    private javax.swing.JPanel AddProductContentPanel;
    private javax.swing.JPanel AddProductPanel;
    private javax.swing.JLabel AddProductPanelLabel;
    private javax.swing.JPanel AddProductTab;
    private javax.swing.JLabel AddProductTitleLabel;
    private java.awt.Choice FilterChoice;
    private javax.swing.JButton LogoutButton;
    private javax.swing.JTabbedPane MainTabbedPane;
    private javax.swing.JButton ProductAddItemConfirmUpdateButton;
    private javax.swing.JButton ProductAddItemGoBackButton;
    private javax.swing.JButton ProductChooseImageFileButton;
    private javax.swing.JButton ProductChooseImageFileUpdateButton;
    private javax.swing.JLabel ProductChooseImageLabel;
    private java.awt.TextField ProductChooseImagePathTextField;
    private java.awt.TextField ProductChooseImagePathUpdateTextField;
    private javax.swing.JLabel ProductChooseImageUpdateLabel;
    private javax.swing.JLabel ProductDescriptionLabel;
    private javax.swing.JTextField ProductDescriptionTextField;
    private javax.swing.JLabel ProductDescriptionUpdateLabel;
    private javax.swing.JTextField ProductDescriptionUpdateTextField;
    private javax.swing.JButton ProductListButton;
    private javax.swing.JPanel ProductListContentPanel;
    private javax.swing.JTextField ProductListSearchBar;
    private javax.swing.JLabel ProductListSearchLabel;
    private javax.swing.JPanel ProductListTab;
    private javax.swing.JLabel ProductListTabTitleLabel1;
    private javax.swing.JTable ProductListTable;
    private javax.swing.JPanel ProductListTablePanel;
    private javax.swing.JLabel ProductNameLabel;
    private javax.swing.JTextField ProductNameTextBar;
    private javax.swing.JLabel ProductNameUpdateLabel;
    private javax.swing.JTextField ProductNameUpdateTextBar;
    private javax.swing.JLabel ProductPriceLabel;
    private javax.swing.JTextField ProductPriceText;
    private javax.swing.JLabel ProductPriceUpdateLabel;
    private javax.swing.JTextField ProductPriceUpdateText;
    private javax.swing.JPanel SideBarPanel;
    private javax.swing.JLabel UpdateEntryTabTitleLabel;
    private javax.swing.JLabel UpdateIProductPanelLabel;
    private javax.swing.JPanel UpdateProductEntryTab;
    private javax.swing.JPanel UpdateProductPanelContent;
    private javax.swing.JPanel footer1;
    private javax.swing.JPanel footer3;
    private javax.swing.JPanel footer8;
    private javax.swing.JPanel header;
    private javax.swing.JPanel header2;
    private javax.swing.JPanel header7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}