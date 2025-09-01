package ui;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import backend.*;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class InventoryFrame extends javax.swing.JFrame {
    
    private List<Product> currentProducts;
    private List<Category> currentCategories;
    private List<SalesRecord> currentSales;
    private String currentSalesPeriod = "Today";
    
    public InventoryFrame() {
        initComponents();
        initApp();
    }
    
    private void checkUserAccess() {
        if (!backend.SessionManager.isLoggedIn()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please login first", "Access Denied", javax.swing.JOptionPane.ERROR_MESSAGE);
            redirectToLogin();
            return;
        }

        String currentUser = backend.SessionManager.getCurrentUsername();

        if (!backend.UserAuthentication.isAdmin(currentUser) && 
            !backend.UserAuthentication.isManager(currentUser)) {

            javax.swing.JOptionPane.showMessageDialog(this, 
                "Access Denied! Only Administrators and Clerks can access this system.", 
                "Permission Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);

            redirectToLogin();
            return;
        }
    }
    
    private void setupRoleBasedAccess() {
        String currentUser = backend.SessionManager.getCurrentUsername();

        // Hide User Management tab and button for clerks
        if (!backend.UserAuthentication.isAdmin(currentUser)) {
            // Remove User Management tab
            MainTabbedPane.removeTabAt(5); // User Management is at index 5

            // Hide User Management button from sidebar
            UserManagementButton.setVisible(false);

            // Adjust the layout since we removed a button
            reorganizeSidebarLayout();
        }

        // Update dashboard with user-specific data
        updateDashboard();
    }
    
    private void reorganizeSidebarLayout() {
        // Remove all components from sidebar
        SideBarPanel.removeAll();

        // Re-add components in order, excluding hidden ones
        SideBarPanel.add(jLabel1);
        SideBarPanel.add(DashboardButton);
        SideBarPanel.add(InventoryButton);
        SideBarPanel.add(AddItemButton);
        SideBarPanel.add(SalesReportButton);
        SideBarPanel.add(CategoriesButton);

        // Only add Logout button (UserManagementButton is hidden)
        SideBarPanel.add(LogoutButton);

        // Revalidate and repaint to update the layout
        SideBarPanel.revalidate();
        SideBarPanel.repaint();
    }
    
    private void initializeData() {
        // Load products
        currentProducts = ProductManager.getAllProducts();
        refreshInventoryTable();

        // Load categories
        currentCategories = CategoryManager.getAllCategories();
        refreshCategoryTable();

        // Load category names for dropdown
        List<String> categoryNames = CategoryManager.getCategoryNames();
        CategoryChoice.removeAll();
        for (String name : categoryNames) {
            CategoryChoice.add(name);
        }

        // Load today's sales by default
        currentSales = SalesReportManager.getTodaySales();
        refreshSalesReportTable();

        // Update dashboard
        updateDashboard();
    }
    
    private void refreshInventoryTable() {
        DefaultTableModel model = (DefaultTableModel) InventoryTable.getModel();
        model.setRowCount(0);

        for (int i = 0; i < currentProducts.size(); i++) {
            Product product = currentProducts.get(i);
            Object[] row = {
                i + 1,
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getDescription(),
                product.getStock(),
                product.getPrice(),
                product.getId()  // Store just the product ID instead of the panel
            };
            model.addRow(row);
        }

        // Set up the renderer and editor for the Actions column
        InventoryTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        InventoryTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor());
    }
    
    private Object createActionButtons(int productId) {
        JPanel panel = new javax.swing.JPanel();
        JButton updateBtn = new JButton("Update");
        JButton stockBtn = new JButton("Stock");
        JButton deleteBtn = new JButton("Delete");

        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch to update tab and load product data
                MainTabbedPane.setSelectedIndex(7); // UpdateEntryTab index
                loadProductForUpdate(productId);
            }
        });

        stockBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch to stock entry tab and load product data
                MainTabbedPane.setSelectedIndex(6); // StockEntryTab index
                loadProductForStockEntry(productId);
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, 
                    "Are you sure you want to delete this product?", "Confirm Delete", 
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    if (ProductManager.deleteProduct(productId)) {
                        JOptionPane.showMessageDialog(null, "Product deleted successfully!");
                        initializeData(); // Refresh data
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to delete product!");
                    }
                }
            }
        });

        panel.add(updateBtn);
        panel.add(stockBtn);
        panel.add(deleteBtn);

        return panel;
    }
    
    private void loadProductForUpdate(int productId) {
        Product product = ProductManager.getProductById(productId);
        if (product != null) {
            ItemIDUpdateTextBar.setText(String.valueOf(product.getId()));
            ItemNameUpdateTextBar.setText(product.getName());
            CategoryUpdateText.setText(product.getCategory());
            PriceUpdateText.setText(String.valueOf(product.getPrice()));
            DescriptionUpdateTextField.setText(product.getDescription());
            ChooseImagePathUpdateTextField.setText(product.getImagePath());
        }
    }
    
    private void loadProductForStockEntry(int productId) {
        Product product = ProductManager.getProductById(productId);
        if (product != null) {
            IDInformationValueLabel.setText(String.valueOf(product.getId()));
            NameInformationValueLabel.setText(product.getName());
            CategoryInformationValueLabel.setText(product.getCategory());
            DescriptionInformationValueLabel.setText(product.getDescription());
            QuantityInformationValueLabel.setText(String.valueOf(product.getStock()));
            PriceInformationValueLabel.setText(String.valueOf(product.getPrice()));
        }
    }
    
    private void refreshCategoryTable() {
        DefaultTableModel model = (DefaultTableModel) CategoryTable.getModel();
        model.setRowCount(0);

        for (int i = 0; i < currentCategories.size(); i++) {
            Category category = currentCategories.get(i);
            Object[] row = {
                i + 1,
                category.getCreatedAt(),
                category.getName(),
                category.getCreatedBy(),
                createCategoryDeleteButton(category.getName())
            };
            model.addRow(row);
        }
    }
    
    private Object createCategoryDeleteButton(String categoryName) {
        JButton deleteBtn = new JButton("Delete");

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, 
                    "Are you sure you want to delete this category?", "Confirm Delete", 
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    if (CategoryManager.deleteCategory(categoryName)) {
                        JOptionPane.showMessageDialog(null, "Category deleted successfully!");
                        initializeData(); // Refresh data
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to delete category!");
                    }
                }
            }
        });

        return deleteBtn;
    }
    
    private void refreshSalesReportTable() {
        DefaultTableModel model = (DefaultTableModel) SalesReportTable.getModel();
        model.setRowCount(0);

        double totalSales = 0;

        for (SalesRecord record : currentSales) {
            Object[] row = {
                record.getDate(),
                record.getProductName(),
                record.getPrice(),
                record.getQuantity(),
                record.getSubtotal()
            };
            model.addRow(row);
            totalSales += record.getSubtotal();
        }

        TotalSalesTableLabel.setText("Total Sales: ₱" + String.format("%.2f", totalSales));
    }
    
    private void updateDashboard() {
        // Update user count (only show for admins)
        String currentUser = backend.SessionManager.getCurrentUsername();
        if (backend.UserAuthentication.isAdmin(currentUser)) {
            UsersAmountLabel.setText(String.valueOf(backend.UserAuthentication.getUserCount()));
        } else {
            // Hide users box for non-admins
            UsersBoxPanel.setVisible(false);
        }

        // Update other dashboard values
        TotalItemAmountLabel.setText(String.valueOf(ProductManager.getTotalProducts()));
        AvailableCategoriesAmountLabel.setText(String.valueOf(CategoryManager.getCategoryCount()));

        // Calculate total sales (you might need to implement this)
        double totalSales = calculateTotalSales();
        TotalSalesAmountLabel.setText(String.format("%.2f", totalSales));
    }
    
    private double calculateTotalSales() {
        // This is a placeholder - you'll need to implement actual sales calculation
        return 0.0;
    }
    
    
    private void redirectToLogin() {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
        backend.SessionManager.logout();
        this.dispose();
    }
    
    // Custom renderer for action buttons
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton updateBtn, stockBtn, deleteBtn;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            updateBtn = new JButton("Update");
            stockBtn = new JButton("Stock");
            deleteBtn = new JButton("Delete");

            add(updateBtn);
            add(stockBtn);
            add(deleteBtn);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
    
    // Custom editor for action buttons
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton updateBtn, stockBtn, deleteBtn;
        private int currentProductId;

        public ButtonEditor() {
            panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS)); // Use BoxLayout horizontally
            panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); // Add some padding

            updateBtn = new JButton("Update");
            stockBtn = new JButton("Stock");
            deleteBtn = new JButton("Delete");

            // Style buttons to be more compact
            Dimension buttonSize = new Dimension(70, 25);
            updateBtn.setPreferredSize(buttonSize);
            updateBtn.setMaximumSize(buttonSize);
            updateBtn.setMinimumSize(buttonSize);

            stockBtn.setPreferredSize(buttonSize);
            stockBtn.setMaximumSize(buttonSize);
            stockBtn.setMinimumSize(buttonSize);

            deleteBtn.setPreferredSize(buttonSize);
            deleteBtn.setMaximumSize(buttonSize);
            deleteBtn.setMinimumSize(buttonSize);

            // Add small gaps between buttons
            panel.add(updateBtn);
            panel.add(Box.createHorizontalStrut(5)); // 5px gap
            panel.add(stockBtn);
            panel.add(Box.createHorizontalStrut(5)); // 5px gap
            panel.add(deleteBtn);

            // Add action listeners
            updateBtn.addActionListener(e -> {
                // Switch to update tab and load product data
                MainTabbedPane.setSelectedIndex(7);
                loadProductForUpdate(currentProductId);
                fireEditingStopped();
            });

            stockBtn.addActionListener(e -> {
                // Switch to stock entry tab and load product data
                MainTabbedPane.setSelectedIndex(6);
                loadProductForStockEntry(currentProductId);
                fireEditingStopped();
            });

            deleteBtn.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(null, 
                    "Are you sure you want to delete this product?", "Confirm Delete", 
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    if (ProductManager.deleteProduct(currentProductId)) {
                        JOptionPane.showMessageDialog(null, "Product deleted successfully!");
                        initializeData(); // Refresh data
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to delete product!");
                    }
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentProductId = (Integer) value;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return currentProductId;
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        SideBarPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        DashboardButton = new javax.swing.JButton();
        InventoryButton = new javax.swing.JButton();
        AddItemButton = new javax.swing.JButton();
        SalesReportButton = new javax.swing.JButton();
        CategoriesButton = new javax.swing.JButton();
        UserManagementButton = new javax.swing.JButton();
        LogoutButton = new javax.swing.JButton();
        MainTabbedPane = new javax.swing.JTabbedPane();
        DashboardTab = new javax.swing.JPanel();
        header5 = new javax.swing.JPanel();
        DashboardTitleLabel = new javax.swing.JLabel();
        DashboardContentPanel = new javax.swing.JPanel();
        DashboardUpdatesPanel = new javax.swing.JPanel();
        TotalItemBoxPanel = new javax.swing.JPanel();
        TotalItemIcon = new javax.swing.JLabel();
        TotalItemTitleLabel = new javax.swing.JLabel();
        TotalItemAmountLabel = new javax.swing.JLabel();
        TotalSalesBoxPanel = new javax.swing.JPanel();
        TotalSalesIcon = new javax.swing.JLabel();
        TotalSalesTitleLabel = new javax.swing.JLabel();
        TotalSalesAmountLabel = new javax.swing.JLabel();
        AvailableCategoriesBoxPanel = new javax.swing.JPanel();
        AvailableCategoriesIcon = new javax.swing.JLabel();
        AvailableCategoriesTitleLabel = new javax.swing.JLabel();
        AvailableCategoriesAmountLabel = new javax.swing.JLabel();
        UsersBoxPanel = new javax.swing.JPanel();
        UsersIcon = new javax.swing.JLabel();
        UsersTitleLabel = new javax.swing.JLabel();
        UsersAmountLabel = new javax.swing.JLabel();
        footer6 = new javax.swing.JPanel();
        InventoryTab = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        InventoryTabTitleLabel1 = new javax.swing.JLabel();
        InventoryContentPanel = new javax.swing.JPanel();
        InventoryTablePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        InventoryTable = new javax.swing.JTable();
        InventorySearchBar = new javax.swing.JTextField();
        InventorySearchLabel = new javax.swing.JLabel();
        InventoryListTableLabel = new javax.swing.JLabel();
        footer1 = new javax.swing.JPanel();
        AddItemTab = new javax.swing.JPanel();
        header2 = new javax.swing.JPanel();
        AddItemTitleLabel = new javax.swing.JLabel();
        AddItemContentPanel = new javax.swing.JPanel();
        AddItemPanel = new javax.swing.JPanel();
        ItemNameLabel = new javax.swing.JLabel();
        ItemNameTextBar = new javax.swing.JTextField();
        CategoryLabel = new javax.swing.JLabel();
        PriceLabel = new javax.swing.JLabel();
        PriceText = new javax.swing.JTextField();
        DescriptionLabel = new javax.swing.JLabel();
        DescriptionTextField = new javax.swing.JTextField();
        AddItemConfirmButton = new javax.swing.JButton();
        ItemIDLabel = new javax.swing.JLabel();
        ItemIDTextBar = new javax.swing.JTextField();
        ChooseImageLabel = new javax.swing.JLabel();
        ChooseImagePathTextField = new java.awt.TextField();
        ChooseImageFileButton = new javax.swing.JButton();
        CategoryChoice = new java.awt.Choice();
        footer3 = new javax.swing.JPanel();
        SalesReportTab = new javax.swing.JPanel();
        header3 = new javax.swing.JPanel();
        SalesAndReportTitleLabel = new javax.swing.JLabel();
        SalesReportContentPanel = new javax.swing.JPanel();
        SalesReportTablePanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        SalesReportTable = new javax.swing.JTable();
        AnnualSalesButton = new javax.swing.JButton();
        TotalSalesTableLabel = new javax.swing.JLabel();
        TodaySalesButton = new javax.swing.JButton();
        ThisWeekSalesButton = new javax.swing.JButton();
        MonthlySalesButton = new javax.swing.JButton();
        ReportSalesTableLabel = new javax.swing.JLabel();
        footer4 = new javax.swing.JPanel();
        CategoriesTab = new javax.swing.JPanel();
        header1 = new javax.swing.JPanel();
        CategoriesTabTitleLabel = new javax.swing.JLabel();
        CategoriesContentPanel = new javax.swing.JPanel();
        CategorySearchBar = new javax.swing.JTextField();
        CategorySearchLabel = new javax.swing.JLabel();
        CategoryTablePanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        CategoryTable = new javax.swing.JTable();
        AddCategoryLabel = new javax.swing.JLabel();
        AddCategoryText = new javax.swing.JTextField();
        AddCategoryConfirmButton = new javax.swing.JButton();
        CategoryListPanelLabel = new javax.swing.JLabel();
        footer2 = new javax.swing.JPanel();
        UserManagementTab = new javax.swing.JPanel();
        header4 = new javax.swing.JPanel();
        UserManagementTabTitleLabel = new javax.swing.JLabel();
        UserManagementContentPanel = new javax.swing.JPanel();
        AddUsersPanel = new javax.swing.JPanel();
        UsernameTextBar = new javax.swing.JTextField();
        UsernameLabel = new javax.swing.JLabel();
        PasswordLabel = new javax.swing.JLabel();
        RepeatPasswordLabel = new javax.swing.JLabel();
        AccountTypeLabel = new javax.swing.JLabel();
        RoleChoice = new java.awt.Choice();
        LastNameTextBar = new javax.swing.JTextField();
        LastnameLabel = new javax.swing.JLabel();
        FirstnameLabel = new javax.swing.JLabel();
        FirstNameTextBar = new javax.swing.JTextField();
        PasswordTextBar = new javax.swing.JPasswordField();
        RepeatPasswordTextBar1 = new javax.swing.JPasswordField();
        AddNewUserConfirmButton2 = new javax.swing.JButton();
        AllUsersPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        AllUsersTable = new javax.swing.JTable();
        DeleteUserTextBar = new javax.swing.JButton();
        ItemInformationPanelLabel1 = new javax.swing.JLabel();
        AddStockPanelLabel1 = new javax.swing.JLabel();
        footer5 = new javax.swing.JPanel();
        StockEntryTab = new javax.swing.JPanel();
        header6 = new javax.swing.JPanel();
        StockEntryTabTitleLabel = new javax.swing.JLabel();
        StockEntryContentPanel = new javax.swing.JPanel();
        TablePanel1 = new javax.swing.JPanel();
        AddItemConfirmButton1 = new javax.swing.JButton();
        StockEntryTextBar = new javax.swing.JTextField();
        StockInLabel1 = new javax.swing.JLabel();
        TablePanel2 = new javax.swing.JPanel();
        PriceInformationLabel = new javax.swing.JLabel();
        IDInformationLabel = new javax.swing.JLabel();
        NameInformationLabel = new javax.swing.JLabel();
        CategoryInformationLabel = new javax.swing.JLabel();
        DescriptionInformationLabel = new javax.swing.JLabel();
        QuantityInformationLabel = new javax.swing.JLabel();
        PriceInformationValueLabel = new javax.swing.JLabel();
        QuantityInformationValueLabel = new javax.swing.JLabel();
        DescriptionInformationValueLabel = new javax.swing.JLabel();
        CategoryInformationValueLabel = new javax.swing.JLabel();
        NameInformationValueLabel = new javax.swing.JLabel();
        IDInformationValueLabel = new javax.swing.JLabel();
        ItemInformationPanelLabel = new javax.swing.JLabel();
        AddStockPanelLabel2 = new javax.swing.JLabel();
        footer7 = new javax.swing.JPanel();
        UpdateEntryTab = new javax.swing.JPanel();
        header7 = new javax.swing.JPanel();
        UpdateEntryTabTitleLabel = new javax.swing.JLabel();
        UpdateItemPanelContent = new javax.swing.JPanel();
        ItemNameUpdateLabel = new javax.swing.JLabel();
        ItemNameUpdateTextBar = new javax.swing.JTextField();
        CategoryUpdateLabel = new javax.swing.JLabel();
        CategoryUpdateText = new javax.swing.JTextField();
        PriceUpdateLabel = new javax.swing.JLabel();
        PriceUpdateText = new javax.swing.JTextField();
        DescriptionUpdateLabel = new javax.swing.JLabel();
        DescriptionUpdateTextField = new javax.swing.JTextField();
        AddItemConfirmUpdateButton = new javax.swing.JButton();
        ItemIDUpdateLabel = new javax.swing.JLabel();
        ItemIDUpdateTextBar = new javax.swing.JTextField();
        ChooseImageUpdateLabel = new javax.swing.JLabel();
        ChooseImagePathUpdateTextField = new java.awt.TextField();
        ChooseImageFileUpdateButton = new javax.swing.JButton();
        footer8 = new javax.swing.JPanel();
        UpdateItemPanelLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Inventory");
        setLocationByPlatform(true);
        setMaximizedBounds(new java.awt.Rectangle(0, 0, 1000, 600));
        setMinimumSize(new java.awt.Dimension(1000, 600));
        setPreferredSize(new java.awt.Dimension(1000, 600));
        setSize(new java.awt.Dimension(1000, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 600));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SideBarPanel.setBackground(new java.awt.Color(0, 0, 0));
        SideBarPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        SideBarPanel.setPreferredSize(new java.awt.Dimension(200, 600));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/logos/smalldonmacblack.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        jLabel1.setPreferredSize(new java.awt.Dimension(96, 96));
        SideBarPanel.add(jLabel1);

        DashboardButton.setBackground(new java.awt.Color(249, 241, 240));
        DashboardButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        DashboardButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/dashboard.png"))); // NOI18N
        DashboardButton.setText("DASHBOARD");
        DashboardButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        DashboardButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        DashboardButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        DashboardButton.setIconTextGap(10);
        DashboardButton.setPreferredSize(new java.awt.Dimension(200, 60));
        DashboardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DashboardButtonActionPerformed(evt);
            }
        });
        SideBarPanel.add(DashboardButton);

        InventoryButton.setBackground(new java.awt.Color(249, 241, 240));
        InventoryButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        InventoryButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/shipping.png"))); // NOI18N
        InventoryButton.setText("INVENTORY");
        InventoryButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        InventoryButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        InventoryButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        InventoryButton.setIconTextGap(10);
        InventoryButton.setPreferredSize(new java.awt.Dimension(200, 60));
        InventoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InventoryButtonActionPerformed(evt);
            }
        });
        SideBarPanel.add(InventoryButton);

        AddItemButton.setBackground(new java.awt.Color(249, 241, 240));
        AddItemButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AddItemButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/queue.png"))); // NOI18N
        AddItemButton.setText("ADD ITEM");
        AddItemButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        AddItemButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddItemButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        AddItemButton.setIconTextGap(10);
        AddItemButton.setPreferredSize(new java.awt.Dimension(200, 60));
        AddItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddItemButtonActionPerformed(evt);
            }
        });
        SideBarPanel.add(AddItemButton);

        SalesReportButton.setBackground(new java.awt.Color(249, 241, 240));
        SalesReportButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        SalesReportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/sales.png"))); // NOI18N
        SalesReportButton.setText("SALES REPORT");
        SalesReportButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        SalesReportButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        SalesReportButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        SalesReportButton.setIconTextGap(10);
        SalesReportButton.setPreferredSize(new java.awt.Dimension(200, 60));
        SalesReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalesReportButtonActionPerformed(evt);
            }
        });
        SideBarPanel.add(SalesReportButton);

        CategoriesButton.setBackground(new java.awt.Color(249, 241, 240));
        CategoriesButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        CategoriesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/tag.png"))); // NOI18N
        CategoriesButton.setText("CATEGORIES");
        CategoriesButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CategoriesButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CategoriesButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        CategoriesButton.setIconTextGap(10);
        CategoriesButton.setPreferredSize(new java.awt.Dimension(200, 60));
        CategoriesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CategoriesButtonActionPerformed(evt);
            }
        });
        SideBarPanel.add(CategoriesButton);

        UserManagementButton.setBackground(new java.awt.Color(249, 241, 240));
        UserManagementButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        UserManagementButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/users.png"))); // NOI18N
        UserManagementButton.setText("USER MANAGEMENT");
        UserManagementButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        UserManagementButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        UserManagementButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        UserManagementButton.setIconTextGap(10);
        UserManagementButton.setPreferredSize(new java.awt.Dimension(200, 60));
        UserManagementButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserManagementButtonActionPerformed(evt);
            }
        });
        SideBarPanel.add(UserManagementButton);

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
        SideBarPanel.add(LogoutButton);

        jPanel1.add(SideBarPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 600));

        MainTabbedPane.setBackground(new java.awt.Color(201, 177, 158));
        MainTabbedPane.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        MainTabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        MainTabbedPane.setPreferredSize(new java.awt.Dimension(850, 600));

        DashboardTab.setBackground(new java.awt.Color(201, 177, 158));
        DashboardTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        DashboardTab.setForeground(new java.awt.Color(31, 40, 35));
        DashboardTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header5.setBackground(new java.awt.Color(173, 103, 48));
        header5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        header5.setPreferredSize(new java.awt.Dimension(1000, 50));
        header5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        DashboardTitleLabel.setBackground(new java.awt.Color(249, 241, 240));
        DashboardTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        DashboardTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        DashboardTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        DashboardTitleLabel.setText("DASHBOARD");
        DashboardTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header5.add(DashboardTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 50));

        DashboardTab.add(header5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, -1));

        DashboardContentPanel.setBackground(new java.awt.Color(201, 177, 158));
        DashboardContentPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        DashboardContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        DashboardContentPanel.setRequestFocusEnabled(false);
        DashboardContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        DashboardUpdatesPanel.setBackground(new java.awt.Color(173, 103, 48));
        DashboardUpdatesPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        DashboardUpdatesPanel.setPreferredSize(new java.awt.Dimension(1000, 50));
        DashboardUpdatesPanel.setLayout(new javax.swing.BoxLayout(DashboardUpdatesPanel, javax.swing.BoxLayout.LINE_AXIS));

        TotalItemBoxPanel.setBackground(new java.awt.Color(249, 241, 240));
        TotalItemBoxPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        TotalItemBoxPanel.setPreferredSize(new java.awt.Dimension(150, 150));
        TotalItemBoxPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TotalItemIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalItemIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/items.png"))); // NOI18N
        TotalItemBoxPanel.add(TotalItemIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 50, 40));

        TotalItemTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        TotalItemTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalItemTitleLabel.setText("TOTAL ITEMS");
        TotalItemBoxPanel.add(TotalItemTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 150, 30));

        TotalItemAmountLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TotalItemAmountLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalItemAmountLabel.setText("0");
        TotalItemBoxPanel.add(TotalItemAmountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 150, 40));

        DashboardUpdatesPanel.add(TotalItemBoxPanel);

        TotalSalesBoxPanel.setBackground(new java.awt.Color(249, 241, 240));
        TotalSalesBoxPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        TotalSalesBoxPanel.setPreferredSize(new java.awt.Dimension(150, 150));
        TotalSalesBoxPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TotalSalesIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalSalesIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/peso.png"))); // NOI18N
        TotalSalesBoxPanel.add(TotalSalesIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 50, 40));

        TotalSalesTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        TotalSalesTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalSalesTitleLabel.setText("TOTAL SALES");
        TotalSalesBoxPanel.add(TotalSalesTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 150, 30));

        TotalSalesAmountLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TotalSalesAmountLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalSalesAmountLabel.setText("0");
        TotalSalesBoxPanel.add(TotalSalesAmountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 150, 40));

        DashboardUpdatesPanel.add(TotalSalesBoxPanel);

        AvailableCategoriesBoxPanel.setBackground(new java.awt.Color(249, 241, 240));
        AvailableCategoriesBoxPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        AvailableCategoriesBoxPanel.setPreferredSize(new java.awt.Dimension(150, 150));
        AvailableCategoriesBoxPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        AvailableCategoriesIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AvailableCategoriesIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/category-alt.png"))); // NOI18N
        AvailableCategoriesBoxPanel.add(AvailableCategoriesIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 50, 40));

        AvailableCategoriesTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        AvailableCategoriesTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AvailableCategoriesTitleLabel.setText("AVAILABLE CATEGORIES");
        AvailableCategoriesBoxPanel.add(AvailableCategoriesTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 150, 30));

        AvailableCategoriesAmountLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        AvailableCategoriesAmountLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AvailableCategoriesAmountLabel.setText("0");
        AvailableCategoriesBoxPanel.add(AvailableCategoriesAmountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 150, 40));

        DashboardUpdatesPanel.add(AvailableCategoriesBoxPanel);

        UsersBoxPanel.setBackground(new java.awt.Color(249, 241, 240));
        UsersBoxPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        UsersBoxPanel.setPreferredSize(new java.awt.Dimension(150, 150));
        UsersBoxPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        UsersIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UsersIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/users.png"))); // NOI18N
        UsersBoxPanel.add(UsersIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 50, 40));

        UsersTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        UsersTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UsersTitleLabel.setText("USERS");
        UsersBoxPanel.add(UsersTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 150, 30));

        UsersAmountLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        UsersAmountLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UsersAmountLabel.setText("0");
        UsersBoxPanel.add(UsersAmountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 150, 40));

        DashboardUpdatesPanel.add(UsersBoxPanel);

        DashboardContentPanel.add(DashboardUpdatesPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 770, 170));

        DashboardTab.add(DashboardContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 790, 500));

        footer6.setBackground(new java.awt.Color(121, 63, 26));
        footer6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        footer6.setPreferredSize(new java.awt.Dimension(990, 50));
        footer6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        DashboardTab.add(footer6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        MainTabbedPane.addTab("tab1", DashboardTab);

        InventoryTab.setBackground(new java.awt.Color(201, 177, 158));
        InventoryTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        InventoryTab.setForeground(new java.awt.Color(31, 40, 35));
        InventoryTab.setRequestFocusEnabled(false);
        InventoryTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header.setBackground(new java.awt.Color(173, 103, 48));
        header.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        header.setPreferredSize(new java.awt.Dimension(1000, 50));
        header.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        InventoryTabTitleLabel1.setBackground(new java.awt.Color(249, 241, 240));
        InventoryTabTitleLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        InventoryTabTitleLabel1.setForeground(new java.awt.Color(255, 255, 255));
        InventoryTabTitleLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        InventoryTabTitleLabel1.setText("INVENTORY");
        InventoryTabTitleLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header.add(InventoryTabTitleLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 50));

        InventoryTab.add(header, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, -1));

        InventoryContentPanel.setBackground(new java.awt.Color(201, 177, 158));
        InventoryContentPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        InventoryContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        InventoryContentPanel.setRequestFocusEnabled(false);
        InventoryContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        InventoryTablePanel.setBackground(new java.awt.Color(249, 241, 240));
        InventoryTablePanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        InventoryTablePanel.setForeground(new java.awt.Color(31, 40, 35));
        InventoryTablePanel.setPreferredSize(new java.awt.Dimension(500, 200));
        InventoryTablePanel.setRequestFocusEnabled(false);
        InventoryTablePanel.setLayout(new javax.swing.BoxLayout(InventoryTablePanel, javax.swing.BoxLayout.LINE_AXIS));

        InventoryTable.setBackground(new java.awt.Color(249, 241, 240));
        InventoryTable.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        InventoryTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        InventoryTable.setForeground(new java.awt.Color(31, 40, 35));
        InventoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "ID", "Name", "Category", "Description", "Quantity", "Price", "Actions"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        InventoryTable.setFillsViewportHeight(true);
        InventoryTable.setGridColor(new java.awt.Color(121, 63, 26));
        InventoryTable.setRowHeight(35);
        InventoryTable.setSelectionBackground(new java.awt.Color(173, 103, 48));
        InventoryTable.setSelectionForeground(new java.awt.Color(249, 241, 240));
        InventoryTable.setShowGrid(true);
        InventoryTable.getTableHeader().setResizingAllowed(false);
        InventoryTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(InventoryTable);
        if (InventoryTable.getColumnModel().getColumnCount() > 0) {
            InventoryTable.getColumnModel().getColumn(0).setResizable(false);
            InventoryTable.getColumnModel().getColumn(0).setPreferredWidth(10);
            InventoryTable.getColumnModel().getColumn(1).setResizable(false);
            InventoryTable.getColumnModel().getColumn(1).setPreferredWidth(10);
            InventoryTable.getColumnModel().getColumn(2).setResizable(false);
            InventoryTable.getColumnModel().getColumn(2).setPreferredWidth(50);
            InventoryTable.getColumnModel().getColumn(3).setResizable(false);
            InventoryTable.getColumnModel().getColumn(3).setPreferredWidth(50);
            InventoryTable.getColumnModel().getColumn(4).setResizable(false);
            InventoryTable.getColumnModel().getColumn(4).setPreferredWidth(50);
            InventoryTable.getColumnModel().getColumn(5).setResizable(false);
            InventoryTable.getColumnModel().getColumn(5).setPreferredWidth(10);
            InventoryTable.getColumnModel().getColumn(6).setResizable(false);
            InventoryTable.getColumnModel().getColumn(6).setPreferredWidth(10);
            InventoryTable.getColumnModel().getColumn(7).setResizable(false);
            InventoryTable.getColumnModel().getColumn(7).setPreferredWidth(100);
        }

        InventoryTablePanel.add(jScrollPane1);

        InventoryContentPanel.add(InventoryTablePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 770, 450));

        InventorySearchBar.setBackground(new java.awt.Color(249, 241, 240));
        InventorySearchBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        InventorySearchBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        InventorySearchBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InventorySearchBarActionPerformed(evt);
            }
        });
        InventoryContentPanel.add(InventorySearchBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(594, 10, 180, -1));

        InventorySearchLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        InventorySearchLabel.setForeground(new java.awt.Color(31, 40, 35));
        InventorySearchLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        InventorySearchLabel.setText("Search:");
        InventoryContentPanel.add(InventorySearchLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 6, 90, 30));

        InventoryListTableLabel.setBackground(new java.awt.Color(249, 241, 240));
        InventoryListTableLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        InventoryListTableLabel.setForeground(new java.awt.Color(255, 255, 255));
        InventoryListTableLabel.setText("INVENTORY LIST");
        InventoryListTableLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        InventoryContentPanel.add(InventoryListTableLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 360, 40));

        InventoryTab.add(InventoryContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 790, 500));

        footer1.setBackground(new java.awt.Color(121, 63, 26));
        footer1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        footer1.setPreferredSize(new java.awt.Dimension(990, 50));
        footer1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        InventoryTab.add(footer1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        MainTabbedPane.addTab("tab2", InventoryTab);

        AddItemTab.setBackground(new java.awt.Color(201, 177, 158));
        AddItemTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        AddItemTab.setForeground(new java.awt.Color(31, 40, 35));
        AddItemTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header2.setBackground(new java.awt.Color(173, 103, 48));
        header2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        header2.setPreferredSize(new java.awt.Dimension(1000, 50));
        header2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        AddItemTitleLabel.setBackground(new java.awt.Color(249, 241, 240));
        AddItemTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        AddItemTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        AddItemTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AddItemTitleLabel.setText("ADD NEW ITEM");
        AddItemTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header2.add(AddItemTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 50));

        AddItemTab.add(header2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, -1));

        AddItemContentPanel.setBackground(new java.awt.Color(201, 177, 158));
        AddItemContentPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        AddItemContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        AddItemContentPanel.setRequestFocusEnabled(false);
        AddItemContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        AddItemPanel.setBackground(new java.awt.Color(201, 177, 158));
        AddItemPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        AddItemPanel.setForeground(new java.awt.Color(31, 40, 35));
        AddItemPanel.setPreferredSize(new java.awt.Dimension(500, 200));
        AddItemPanel.setRequestFocusEnabled(false);
        AddItemPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ItemNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ItemNameLabel.setForeground(new java.awt.Color(31, 40, 35));
        ItemNameLabel.setText("Item Name");
        AddItemPanel.add(ItemNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 140, 20));

        ItemNameTextBar.setBackground(new java.awt.Color(249, 241, 240));
        ItemNameTextBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ItemNameTextBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ItemNameTextBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ItemNameTextBarActionPerformed(evt);
            }
        });
        AddItemPanel.add(ItemNameTextBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 730, 30));

        CategoryLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        CategoryLabel.setForeground(new java.awt.Color(31, 40, 35));
        CategoryLabel.setText("Category");
        AddItemPanel.add(CategoryLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 140, 20));

        PriceLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        PriceLabel.setForeground(new java.awt.Color(31, 40, 35));
        PriceLabel.setText("Price");
        AddItemPanel.add(PriceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 140, 20));

        PriceText.setBackground(new java.awt.Color(249, 241, 240));
        PriceText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        PriceText.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        PriceText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PriceTextActionPerformed(evt);
            }
        });
        AddItemPanel.add(PriceText, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 730, 30));

        DescriptionLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        DescriptionLabel.setForeground(new java.awt.Color(31, 40, 35));
        DescriptionLabel.setText("Item Description");
        AddItemPanel.add(DescriptionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 140, 20));

        DescriptionTextField.setBackground(new java.awt.Color(249, 241, 240));
        DescriptionTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        DescriptionTextField.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        AddItemPanel.add(DescriptionTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 730, 80));

        AddItemConfirmButton.setBackground(new java.awt.Color(173, 103, 48));
        AddItemConfirmButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AddItemConfirmButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/queue.png"))); // NOI18N
        AddItemConfirmButton.setText("ADD ITEM");
        AddItemConfirmButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        AddItemConfirmButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddItemConfirmButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        AddItemConfirmButton.setIconTextGap(10);
        AddItemConfirmButton.setPreferredSize(new java.awt.Dimension(120, 40));
        AddItemConfirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddItemConfirmButtonActionPerformed(evt);
            }
        });
        AddItemPanel.add(AddItemConfirmButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 120, 40));

        ItemIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ItemIDLabel.setForeground(new java.awt.Color(31, 40, 35));
        ItemIDLabel.setText("Item ID");
        AddItemPanel.add(ItemIDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 140, 20));

        ItemIDTextBar.setBackground(new java.awt.Color(249, 241, 240));
        ItemIDTextBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ItemIDTextBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ItemIDTextBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ItemIDTextBarActionPerformed(evt);
            }
        });
        AddItemPanel.add(ItemIDTextBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 730, 30));

        ChooseImageLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ChooseImageLabel.setForeground(new java.awt.Color(31, 40, 35));
        ChooseImageLabel.setText("Item Image");
        AddItemPanel.add(ChooseImageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 140, 20));

        ChooseImagePathTextField.setBackground(new java.awt.Color(249, 241, 240));
        ChooseImagePathTextField.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        ChooseImagePathTextField.setForeground(new java.awt.Color(31, 40, 35));
        ChooseImagePathTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChooseImagePathTextFieldActionPerformed(evt);
            }
        });
        AddItemPanel.add(ChooseImagePathTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 360, 20));

        ChooseImageFileButton.setBackground(new java.awt.Color(249, 241, 240));
        ChooseImageFileButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ChooseImageFileButton.setText("Choose File");
        ChooseImageFileButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ChooseImageFileButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ChooseImageFileButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChooseImageFileButton.setIconTextGap(10);
        ChooseImageFileButton.setPreferredSize(new java.awt.Dimension(120, 40));
        ChooseImageFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChooseImageFileButtonActionPerformed(evt);
            }
        });
        AddItemPanel.add(ChooseImageFileButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 270, 120, 40));

        CategoryChoice.setBackground(new java.awt.Color(249, 241, 240));
        AddItemPanel.add(CategoryChoice, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 210, 730, 40));

        AddItemContentPanel.add(AddItemPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 770, 480));

        AddItemTab.add(AddItemContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 790, 500));

        footer3.setBackground(new java.awt.Color(121, 63, 26));
        footer3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        footer3.setPreferredSize(new java.awt.Dimension(990, 50));
        footer3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        AddItemTab.add(footer3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        MainTabbedPane.addTab("tab3", AddItemTab);

        SalesReportTab.setBackground(new java.awt.Color(201, 177, 158));
        SalesReportTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        SalesReportTab.setForeground(new java.awt.Color(31, 40, 35));
        SalesReportTab.setPreferredSize(new java.awt.Dimension(780, 850));
        SalesReportTab.setRequestFocusEnabled(false);
        SalesReportTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header3.setBackground(new java.awt.Color(173, 103, 48));
        header3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        header3.setPreferredSize(new java.awt.Dimension(1000, 50));
        header3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SalesAndReportTitleLabel.setBackground(new java.awt.Color(249, 241, 240));
        SalesAndReportTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        SalesAndReportTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        SalesAndReportTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SalesAndReportTitleLabel.setText("SALES & REPORT");
        SalesAndReportTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header3.add(SalesAndReportTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 50));

        SalesReportTab.add(header3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, -1));

        SalesReportContentPanel.setBackground(new java.awt.Color(201, 177, 158));
        SalesReportContentPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        SalesReportContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        SalesReportContentPanel.setRequestFocusEnabled(false);
        SalesReportContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SalesReportTablePanel.setBackground(new java.awt.Color(249, 241, 240));
        SalesReportTablePanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        SalesReportTablePanel.setForeground(new java.awt.Color(31, 40, 35));
        SalesReportTablePanel.setPreferredSize(new java.awt.Dimension(500, 200));
        SalesReportTablePanel.setRequestFocusEnabled(false);
        SalesReportTablePanel.setLayout(new javax.swing.BoxLayout(SalesReportTablePanel, javax.swing.BoxLayout.LINE_AXIS));

        SalesReportTable.setBackground(new java.awt.Color(249, 241, 240));
        SalesReportTable.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        SalesReportTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        SalesReportTable.setForeground(new java.awt.Color(31, 40, 35));
        SalesReportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Item", "Price", "Quantity", "Subtotal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Integer.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        SalesReportTable.setFillsViewportHeight(true);
        SalesReportTable.setGridColor(new java.awt.Color(121, 63, 26));
        SalesReportTable.setRowHeight(35);
        SalesReportTable.setSelectionBackground(new java.awt.Color(173, 103, 48));
        SalesReportTable.setSelectionForeground(new java.awt.Color(249, 241, 240));
        SalesReportTable.setShowGrid(true);
        SalesReportTable.getTableHeader().setResizingAllowed(false);
        SalesReportTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(SalesReportTable);
        if (SalesReportTable.getColumnModel().getColumnCount() > 0) {
            SalesReportTable.getColumnModel().getColumn(0).setResizable(false);
            SalesReportTable.getColumnModel().getColumn(0).setPreferredWidth(100);
            SalesReportTable.getColumnModel().getColumn(1).setResizable(false);
            SalesReportTable.getColumnModel().getColumn(1).setPreferredWidth(100);
            SalesReportTable.getColumnModel().getColumn(2).setResizable(false);
            SalesReportTable.getColumnModel().getColumn(2).setPreferredWidth(50);
            SalesReportTable.getColumnModel().getColumn(3).setResizable(false);
            SalesReportTable.getColumnModel().getColumn(3).setPreferredWidth(50);
            SalesReportTable.getColumnModel().getColumn(4).setResizable(false);
            SalesReportTable.getColumnModel().getColumn(4).setPreferredWidth(50);
        }

        SalesReportTablePanel.add(jScrollPane3);

        SalesReportContentPanel.add(SalesReportTablePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 770, 310));

        AnnualSalesButton.setBackground(new java.awt.Color(173, 103, 48));
        AnnualSalesButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AnnualSalesButton.setText("Annual Sales");
        AnnualSalesButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        AnnualSalesButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AnnualSalesButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        AnnualSalesButton.setIconTextGap(10);
        AnnualSalesButton.setPreferredSize(new java.awt.Dimension(120, 40));
        AnnualSalesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnnualSalesButtonActionPerformed(evt);
            }
        });
        SalesReportContentPanel.add(AnnualSalesButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, 120, 50));

        TotalSalesTableLabel.setBackground(new java.awt.Color(249, 241, 240));
        TotalSalesTableLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TotalSalesTableLabel.setForeground(new java.awt.Color(255, 255, 255));
        TotalSalesTableLabel.setText("Total Sales: ₱0.00");
        TotalSalesTableLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        SalesReportContentPanel.add(TotalSalesTableLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 360, 30));

        TodaySalesButton.setBackground(new java.awt.Color(173, 103, 48));
        TodaySalesButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TodaySalesButton.setText("Today's Sales");
        TodaySalesButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        TodaySalesButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        TodaySalesButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        TodaySalesButton.setIconTextGap(10);
        TodaySalesButton.setPreferredSize(new java.awt.Dimension(120, 40));
        TodaySalesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TodaySalesButtonActionPerformed(evt);
            }
        });
        SalesReportContentPanel.add(TodaySalesButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 120, 50));

        ThisWeekSalesButton.setBackground(new java.awt.Color(173, 103, 48));
        ThisWeekSalesButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ThisWeekSalesButton.setText("This Week");
        ThisWeekSalesButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ThisWeekSalesButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ThisWeekSalesButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ThisWeekSalesButton.setIconTextGap(10);
        ThisWeekSalesButton.setPreferredSize(new java.awt.Dimension(120, 40));
        ThisWeekSalesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ThisWeekSalesButtonActionPerformed(evt);
            }
        });
        SalesReportContentPanel.add(ThisWeekSalesButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 120, 50));

        MonthlySalesButton.setBackground(new java.awt.Color(173, 103, 48));
        MonthlySalesButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        MonthlySalesButton.setText("Monthly Sales");
        MonthlySalesButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        MonthlySalesButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        MonthlySalesButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MonthlySalesButton.setIconTextGap(10);
        MonthlySalesButton.setPreferredSize(new java.awt.Dimension(120, 40));
        MonthlySalesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MonthlySalesButtonActionPerformed(evt);
            }
        });
        SalesReportContentPanel.add(MonthlySalesButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 20, 120, 50));

        ReportSalesTableLabel.setBackground(new java.awt.Color(249, 241, 240));
        ReportSalesTableLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        ReportSalesTableLabel.setForeground(new java.awt.Color(255, 255, 255));
        ReportSalesTableLabel.setText("REPORT");
        ReportSalesTableLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        SalesReportContentPanel.add(ReportSalesTableLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 360, 40));

        SalesReportTab.add(SalesReportContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 790, 500));

        footer4.setBackground(new java.awt.Color(121, 63, 26));
        footer4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        footer4.setPreferredSize(new java.awt.Dimension(990, 50));
        footer4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        SalesReportTab.add(footer4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        MainTabbedPane.addTab("tab4", SalesReportTab);

        CategoriesTab.setBackground(new java.awt.Color(201, 177, 158));
        CategoriesTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        CategoriesTab.setForeground(new java.awt.Color(31, 40, 35));
        CategoriesTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header1.setBackground(new java.awt.Color(173, 103, 48));
        header1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        header1.setPreferredSize(new java.awt.Dimension(1000, 50));
        header1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CategoriesTabTitleLabel.setBackground(new java.awt.Color(249, 241, 240));
        CategoriesTabTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CategoriesTabTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        CategoriesTabTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CategoriesTabTitleLabel.setText("CATEGORIES");
        CategoriesTabTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header1.add(CategoriesTabTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 50));

        CategoriesTab.add(header1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, -1));

        CategoriesContentPanel.setBackground(new java.awt.Color(201, 177, 158));
        CategoriesContentPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        CategoriesContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        CategoriesContentPanel.setRequestFocusEnabled(false);
        CategoriesContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CategorySearchBar.setBackground(new java.awt.Color(249, 241, 240));
        CategorySearchBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        CategorySearchBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        CategorySearchBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CategorySearchBarActionPerformed(evt);
            }
        });
        CategoriesContentPanel.add(CategorySearchBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 150, 180, -1));

        CategorySearchLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        CategorySearchLabel.setForeground(new java.awt.Color(31, 40, 35));
        CategorySearchLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        CategorySearchLabel.setText("Search:");
        CategoriesContentPanel.add(CategorySearchLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 150, 90, 30));

        CategoryTablePanel.setBackground(new java.awt.Color(249, 241, 240));
        CategoryTablePanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        CategoryTablePanel.setForeground(new java.awt.Color(31, 40, 35));
        CategoryTablePanel.setPreferredSize(new java.awt.Dimension(500, 200));
        CategoryTablePanel.setRequestFocusEnabled(false);
        CategoryTablePanel.setLayout(new javax.swing.BoxLayout(CategoryTablePanel, javax.swing.BoxLayout.LINE_AXIS));

        CategoryTable.setBackground(new java.awt.Color(249, 241, 240));
        CategoryTable.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        CategoryTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        CategoryTable.setForeground(new java.awt.Color(31, 40, 35));
        CategoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Date/Time", "Name", "Created By", "Actions"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        CategoryTable.setFillsViewportHeight(true);
        CategoryTable.setGridColor(new java.awt.Color(121, 63, 26));
        CategoryTable.setRowHeight(35);
        CategoryTable.setSelectionBackground(new java.awt.Color(173, 103, 48));
        CategoryTable.setSelectionForeground(new java.awt.Color(249, 241, 240));
        CategoryTable.setShowGrid(true);
        CategoryTable.getTableHeader().setResizingAllowed(false);
        CategoryTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(CategoryTable);
        if (CategoryTable.getColumnModel().getColumnCount() > 0) {
            CategoryTable.getColumnModel().getColumn(0).setResizable(false);
            CategoryTable.getColumnModel().getColumn(0).setPreferredWidth(5);
            CategoryTable.getColumnModel().getColumn(1).setResizable(false);
            CategoryTable.getColumnModel().getColumn(1).setPreferredWidth(100);
            CategoryTable.getColumnModel().getColumn(2).setResizable(false);
            CategoryTable.getColumnModel().getColumn(2).setPreferredWidth(50);
            CategoryTable.getColumnModel().getColumn(3).setResizable(false);
            CategoryTable.getColumnModel().getColumn(3).setPreferredWidth(50);
            CategoryTable.getColumnModel().getColumn(4).setResizable(false);
            CategoryTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        CategoryTablePanel.add(jScrollPane2);

        CategoriesContentPanel.add(CategoryTablePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 770, 310));

        AddCategoryLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        AddCategoryLabel.setForeground(new java.awt.Color(31, 40, 35));
        AddCategoryLabel.setText("Category Name");
        CategoriesContentPanel.add(AddCategoryLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 180, 30));

        AddCategoryText.setBackground(new java.awt.Color(249, 241, 240));
        AddCategoryText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AddCategoryText.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        AddCategoryText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddCategoryTextActionPerformed(evt);
            }
        });
        CategoriesContentPanel.add(AddCategoryText, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 750, -1));

        AddCategoryConfirmButton.setBackground(new java.awt.Color(173, 103, 48));
        AddCategoryConfirmButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AddCategoryConfirmButton.setText("ADD CATEGORY");
        AddCategoryConfirmButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        AddCategoryConfirmButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddCategoryConfirmButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        AddCategoryConfirmButton.setIconTextGap(10);
        AddCategoryConfirmButton.setPreferredSize(new java.awt.Dimension(120, 40));
        AddCategoryConfirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddCategoryConfirmButtonActionPerformed(evt);
            }
        });
        CategoriesContentPanel.add(AddCategoryConfirmButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 120, 30));

        CategoryListPanelLabel.setBackground(new java.awt.Color(249, 241, 240));
        CategoryListPanelLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CategoryListPanelLabel.setForeground(new java.awt.Color(255, 255, 255));
        CategoryListPanelLabel.setText("CATEGORY LIST");
        CategoryListPanelLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        CategoriesContentPanel.add(CategoryListPanelLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 360, 40));

        CategoriesTab.add(CategoriesContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 790, 500));

        footer2.setBackground(new java.awt.Color(121, 63, 26));
        footer2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        footer2.setPreferredSize(new java.awt.Dimension(990, 50));
        footer2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        CategoriesTab.add(footer2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        MainTabbedPane.addTab("tab5", CategoriesTab);

        UserManagementTab.setBackground(new java.awt.Color(201, 177, 158));
        UserManagementTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        UserManagementTab.setForeground(new java.awt.Color(31, 40, 35));
        UserManagementTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header4.setBackground(new java.awt.Color(173, 103, 48));
        header4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        header4.setPreferredSize(new java.awt.Dimension(1000, 50));
        header4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        UserManagementTabTitleLabel.setBackground(new java.awt.Color(249, 241, 240));
        UserManagementTabTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        UserManagementTabTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        UserManagementTabTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UserManagementTabTitleLabel.setText("USER MANAGEMENT");
        UserManagementTabTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header4.add(UserManagementTabTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 50));

        UserManagementTab.add(header4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, -1));

        UserManagementContentPanel.setBackground(new java.awt.Color(201, 177, 158));
        UserManagementContentPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        UserManagementContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        UserManagementContentPanel.setRequestFocusEnabled(false);
        UserManagementContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        AddUsersPanel.setBackground(new java.awt.Color(201, 177, 158));
        AddUsersPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        AddUsersPanel.setForeground(new java.awt.Color(31, 40, 35));
        AddUsersPanel.setPreferredSize(new java.awt.Dimension(500, 200));
        AddUsersPanel.setRequestFocusEnabled(false);
        AddUsersPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        UsernameTextBar.setBackground(new java.awt.Color(249, 241, 240));
        UsernameTextBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        UsernameTextBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        UsernameTextBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsernameTextBarActionPerformed(evt);
            }
        });
        AddUsersPanel.add(UsernameTextBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 180, 30));

        UsernameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        UsernameLabel.setForeground(new java.awt.Color(31, 40, 35));
        UsernameLabel.setText("Username");
        AddUsersPanel.add(UsernameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 180, 50));

        PasswordLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        PasswordLabel.setForeground(new java.awt.Color(31, 40, 35));
        PasswordLabel.setText("Password");
        AddUsersPanel.add(PasswordLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 180, 50));

        RepeatPasswordLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        RepeatPasswordLabel.setForeground(new java.awt.Color(31, 40, 35));
        RepeatPasswordLabel.setText("Repeat Password");
        AddUsersPanel.add(RepeatPasswordLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 180, 50));

        AccountTypeLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        AccountTypeLabel.setForeground(new java.awt.Color(31, 40, 35));
        AccountTypeLabel.setText("Account Type");
        AddUsersPanel.add(AccountTypeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 180, 30));
        AddUsersPanel.add(RoleChoice, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 180, 20));

        LastNameTextBar.setBackground(new java.awt.Color(249, 241, 240));
        LastNameTextBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        LastNameTextBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        LastNameTextBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LastNameTextBarActionPerformed(evt);
            }
        });
        AddUsersPanel.add(LastNameTextBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 180, 30));

        LastnameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        LastnameLabel.setForeground(new java.awt.Color(31, 40, 35));
        LastnameLabel.setText("LastName");
        AddUsersPanel.add(LastnameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 180, 50));

        FirstnameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        FirstnameLabel.setForeground(new java.awt.Color(31, 40, 35));
        FirstnameLabel.setText("FirstName");
        AddUsersPanel.add(FirstnameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 180, 50));

        FirstNameTextBar.setBackground(new java.awt.Color(249, 241, 240));
        FirstNameTextBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        FirstNameTextBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        FirstNameTextBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FirstNameTextBarActionPerformed(evt);
            }
        });
        AddUsersPanel.add(FirstNameTextBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 180, 30));

        PasswordTextBar.setBackground(new java.awt.Color(249, 241, 240));
        PasswordTextBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        PasswordTextBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PasswordTextBarActionPerformed(evt);
            }
        });
        AddUsersPanel.add(PasswordTextBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 180, 30));

        RepeatPasswordTextBar1.setBackground(new java.awt.Color(249, 241, 240));
        RepeatPasswordTextBar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        RepeatPasswordTextBar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RepeatPasswordTextBar1ActionPerformed(evt);
            }
        });
        AddUsersPanel.add(RepeatPasswordTextBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 180, 30));

        AddNewUserConfirmButton2.setBackground(new java.awt.Color(173, 103, 48));
        AddNewUserConfirmButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AddNewUserConfirmButton2.setText("ADD NEW USER");
        AddNewUserConfirmButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        AddNewUserConfirmButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddNewUserConfirmButton2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        AddNewUserConfirmButton2.setIconTextGap(10);
        AddNewUserConfirmButton2.setPreferredSize(new java.awt.Dimension(120, 40));
        AddNewUserConfirmButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddNewUserConfirmButton2ActionPerformed(evt);
            }
        });
        AddUsersPanel.add(AddNewUserConfirmButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, -1, -1));

        UserManagementContentPanel.add(AddUsersPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 260, 450));

        AllUsersPanel.setBackground(new java.awt.Color(201, 177, 158));
        AllUsersPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        AllUsersPanel.setForeground(new java.awt.Color(31, 40, 35));
        AllUsersPanel.setPreferredSize(new java.awt.Dimension(500, 200));
        AllUsersPanel.setRequestFocusEnabled(false);
        AllUsersPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        AllUsersTable.setBackground(new java.awt.Color(249, 241, 240));
        AllUsersTable.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        AllUsersTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AllUsersTable.setForeground(new java.awt.Color(31, 40, 35));
        AllUsersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Username", "Account Type", "Date/Time Created", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        AllUsersTable.setFillsViewportHeight(true);
        AllUsersTable.setGridColor(new java.awt.Color(121, 63, 26));
        AllUsersTable.setRowHeight(35);
        AllUsersTable.setSelectionBackground(new java.awt.Color(173, 103, 48));
        AllUsersTable.setSelectionForeground(new java.awt.Color(249, 241, 240));
        AllUsersTable.setShowGrid(true);
        AllUsersTable.getTableHeader().setResizingAllowed(false);
        AllUsersTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(AllUsersTable);
        if (AllUsersTable.getColumnModel().getColumnCount() > 0) {
            AllUsersTable.getColumnModel().getColumn(0).setResizable(false);
            AllUsersTable.getColumnModel().getColumn(0).setPreferredWidth(50);
            AllUsersTable.getColumnModel().getColumn(1).setResizable(false);
            AllUsersTable.getColumnModel().getColumn(1).setPreferredWidth(70);
            AllUsersTable.getColumnModel().getColumn(2).setResizable(false);
            AllUsersTable.getColumnModel().getColumn(2).setPreferredWidth(70);
            AllUsersTable.getColumnModel().getColumn(3).setResizable(false);
            AllUsersTable.getColumnModel().getColumn(3).setPreferredWidth(100);
            AllUsersTable.getColumnModel().getColumn(4).setResizable(false);
            AllUsersTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        AllUsersPanel.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 480, 380));

        DeleteUserTextBar.setBackground(new java.awt.Color(173, 103, 48));
        DeleteUserTextBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        DeleteUserTextBar.setText("DELETE USER");
        DeleteUserTextBar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        DeleteUserTextBar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        DeleteUserTextBar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        DeleteUserTextBar.setIconTextGap(10);
        DeleteUserTextBar.setPreferredSize(new java.awt.Dimension(120, 40));
        DeleteUserTextBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteUserTextBarActionPerformed(evt);
            }
        });
        AllUsersPanel.add(DeleteUserTextBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 400, -1, -1));

        UserManagementContentPanel.add(AllUsersPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, 500, 450));

        ItemInformationPanelLabel1.setBackground(new java.awt.Color(249, 241, 240));
        ItemInformationPanelLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        ItemInformationPanelLabel1.setForeground(new java.awt.Color(255, 255, 255));
        ItemInformationPanelLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemInformationPanelLabel1.setText("ALL USERS");
        ItemInformationPanelLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        UserManagementContentPanel.add(ItemInformationPanelLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 0, 500, 40));

        AddStockPanelLabel1.setBackground(new java.awt.Color(249, 241, 240));
        AddStockPanelLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        AddStockPanelLabel1.setForeground(new java.awt.Color(255, 255, 255));
        AddStockPanelLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AddStockPanelLabel1.setText("ADD USERS");
        AddStockPanelLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        UserManagementContentPanel.add(AddStockPanelLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 260, 40));

        UserManagementTab.add(UserManagementContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 790, 500));

        footer5.setBackground(new java.awt.Color(121, 63, 26));
        footer5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        footer5.setPreferredSize(new java.awt.Dimension(990, 50));
        footer5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        UserManagementTab.add(footer5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        MainTabbedPane.addTab("tab6", UserManagementTab);

        StockEntryTab.setBackground(new java.awt.Color(201, 177, 158));
        StockEntryTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        StockEntryTab.setForeground(new java.awt.Color(31, 40, 35));
        StockEntryTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header6.setBackground(new java.awt.Color(173, 103, 48));
        header6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        header6.setPreferredSize(new java.awt.Dimension(1000, 50));
        header6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        StockEntryTabTitleLabel.setBackground(new java.awt.Color(249, 241, 240));
        StockEntryTabTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        StockEntryTabTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        StockEntryTabTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        StockEntryTabTitleLabel.setText("STOCK ENTRY");
        StockEntryTabTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header6.add(StockEntryTabTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 50));

        StockEntryTab.add(header6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, -1));

        StockEntryContentPanel.setBackground(new java.awt.Color(201, 177, 158));
        StockEntryContentPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        StockEntryContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        StockEntryContentPanel.setRequestFocusEnabled(false);
        StockEntryContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TablePanel1.setBackground(new java.awt.Color(201, 177, 158));
        TablePanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        TablePanel1.setForeground(new java.awt.Color(31, 40, 35));
        TablePanel1.setPreferredSize(new java.awt.Dimension(500, 200));
        TablePanel1.setRequestFocusEnabled(false);
        TablePanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        AddItemConfirmButton1.setBackground(new java.awt.Color(173, 103, 48));
        AddItemConfirmButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AddItemConfirmButton1.setText("ADD");
        AddItemConfirmButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        AddItemConfirmButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddItemConfirmButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        AddItemConfirmButton1.setIconTextGap(10);
        AddItemConfirmButton1.setPreferredSize(new java.awt.Dimension(120, 40));
        AddItemConfirmButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddItemConfirmButton1ActionPerformed(evt);
            }
        });
        TablePanel1.add(AddItemConfirmButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 50, 30));

        StockEntryTextBar.setBackground(new java.awt.Color(249, 241, 240));
        StockEntryTextBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        StockEntryTextBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        StockEntryTextBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StockEntryTextBarActionPerformed(evt);
            }
        });
        TablePanel1.add(StockEntryTextBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 180, 30));

        StockInLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        StockInLabel1.setForeground(new java.awt.Color(31, 40, 35));
        StockInLabel1.setText("Stock In");
        TablePanel1.add(StockInLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 90, 30));

        StockEntryContentPanel.add(TablePanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 360, 450));

        TablePanel2.setBackground(new java.awt.Color(201, 177, 158));
        TablePanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        TablePanel2.setForeground(new java.awt.Color(31, 40, 35));
        TablePanel2.setPreferredSize(new java.awt.Dimension(500, 200));
        TablePanel2.setRequestFocusEnabled(false);
        TablePanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PriceInformationLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        PriceInformationLabel.setForeground(new java.awt.Color(31, 40, 35));
        PriceInformationLabel.setText("Price");
        PriceInformationLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        PriceInformationLabel.setPreferredSize(new java.awt.Dimension(100, 35));
        TablePanel2.add(PriceInformationLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, -1, -1));

        IDInformationLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        IDInformationLabel.setForeground(new java.awt.Color(31, 40, 35));
        IDInformationLabel.setText("ID");
        IDInformationLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        IDInformationLabel.setPreferredSize(new java.awt.Dimension(100, 35));
        TablePanel2.add(IDInformationLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        NameInformationLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        NameInformationLabel.setForeground(new java.awt.Color(31, 40, 35));
        NameInformationLabel.setText("Name");
        NameInformationLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        NameInformationLabel.setPreferredSize(new java.awt.Dimension(100, 35));
        TablePanel2.add(NameInformationLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        CategoryInformationLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        CategoryInformationLabel.setForeground(new java.awt.Color(31, 40, 35));
        CategoryInformationLabel.setText("Category");
        CategoryInformationLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CategoryInformationLabel.setPreferredSize(new java.awt.Dimension(100, 35));
        TablePanel2.add(CategoryInformationLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        DescriptionInformationLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        DescriptionInformationLabel.setForeground(new java.awt.Color(31, 40, 35));
        DescriptionInformationLabel.setText("Description");
        DescriptionInformationLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        DescriptionInformationLabel.setPreferredSize(new java.awt.Dimension(100, 35));
        TablePanel2.add(DescriptionInformationLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, -1, -1));

        QuantityInformationLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        QuantityInformationLabel.setForeground(new java.awt.Color(31, 40, 35));
        QuantityInformationLabel.setText("Quantity");
        QuantityInformationLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        QuantityInformationLabel.setPreferredSize(new java.awt.Dimension(100, 35));
        TablePanel2.add(QuantityInformationLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, -1, -1));

        PriceInformationValueLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        PriceInformationValueLabel.setForeground(new java.awt.Color(31, 40, 35));
        PriceInformationValueLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        PriceInformationValueLabel.setPreferredSize(new java.awt.Dimension(100, 35));
        TablePanel2.add(PriceInformationValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 320, 240, -1));

        QuantityInformationValueLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        QuantityInformationValueLabel.setForeground(new java.awt.Color(31, 40, 35));
        QuantityInformationValueLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        QuantityInformationValueLabel.setPreferredSize(new java.awt.Dimension(100, 35));
        TablePanel2.add(QuantityInformationValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 260, 240, -1));

        DescriptionInformationValueLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        DescriptionInformationValueLabel.setForeground(new java.awt.Color(31, 40, 35));
        DescriptionInformationValueLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        DescriptionInformationValueLabel.setPreferredSize(new java.awt.Dimension(100, 35));
        TablePanel2.add(DescriptionInformationValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 240, -1));

        CategoryInformationValueLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        CategoryInformationValueLabel.setForeground(new java.awt.Color(31, 40, 35));
        CategoryInformationValueLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        CategoryInformationValueLabel.setPreferredSize(new java.awt.Dimension(100, 35));
        TablePanel2.add(CategoryInformationValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, 240, -1));

        NameInformationValueLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        NameInformationValueLabel.setForeground(new java.awt.Color(31, 40, 35));
        NameInformationValueLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        NameInformationValueLabel.setPreferredSize(new java.awt.Dimension(100, 35));
        TablePanel2.add(NameInformationValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 240, -1));

        IDInformationValueLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        IDInformationValueLabel.setForeground(new java.awt.Color(31, 40, 35));
        IDInformationValueLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        IDInformationValueLabel.setPreferredSize(new java.awt.Dimension(100, 35));
        TablePanel2.add(IDInformationValueLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 240, -1));

        StockEntryContentPanel.add(TablePanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 40, 400, 450));

        ItemInformationPanelLabel.setBackground(new java.awt.Color(249, 241, 240));
        ItemInformationPanelLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        ItemInformationPanelLabel.setForeground(new java.awt.Color(255, 255, 255));
        ItemInformationPanelLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ItemInformationPanelLabel.setText("ITEM INFORMATION");
        ItemInformationPanelLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        StockEntryContentPanel.add(ItemInformationPanelLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 400, 40));

        AddStockPanelLabel2.setBackground(new java.awt.Color(249, 241, 240));
        AddStockPanelLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        AddStockPanelLabel2.setForeground(new java.awt.Color(255, 255, 255));
        AddStockPanelLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AddStockPanelLabel2.setText("ADD STOCK/S");
        AddStockPanelLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        StockEntryContentPanel.add(AddStockPanelLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 360, 40));

        StockEntryTab.add(StockEntryContentPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 790, 500));

        footer7.setBackground(new java.awt.Color(121, 63, 26));
        footer7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        footer7.setPreferredSize(new java.awt.Dimension(990, 50));
        footer7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        StockEntryTab.add(footer7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        MainTabbedPane.addTab("tab6", StockEntryTab);

        UpdateEntryTab.setBackground(new java.awt.Color(201, 177, 158));
        UpdateEntryTab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        UpdateEntryTab.setForeground(new java.awt.Color(31, 40, 35));
        UpdateEntryTab.setRequestFocusEnabled(false);
        UpdateEntryTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        header7.setBackground(new java.awt.Color(173, 103, 48));
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

        UpdateEntryTab.add(header7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, -1));

        UpdateItemPanelContent.setBackground(new java.awt.Color(201, 177, 158));
        UpdateItemPanelContent.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        UpdateItemPanelContent.setForeground(new java.awt.Color(31, 40, 35));
        UpdateItemPanelContent.setPreferredSize(new java.awt.Dimension(500, 200));
        UpdateItemPanelContent.setRequestFocusEnabled(false);
        UpdateItemPanelContent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ItemNameUpdateLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ItemNameUpdateLabel.setForeground(new java.awt.Color(31, 40, 35));
        ItemNameUpdateLabel.setText("Item Name");
        UpdateItemPanelContent.add(ItemNameUpdateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 140, 20));

        ItemNameUpdateTextBar.setBackground(new java.awt.Color(249, 241, 240));
        ItemNameUpdateTextBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ItemNameUpdateTextBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ItemNameUpdateTextBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ItemNameUpdateTextBarActionPerformed(evt);
            }
        });
        UpdateItemPanelContent.add(ItemNameUpdateTextBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 670, 30));

        CategoryUpdateLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        CategoryUpdateLabel.setForeground(new java.awt.Color(31, 40, 35));
        CategoryUpdateLabel.setText("Category");
        UpdateItemPanelContent.add(CategoryUpdateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 140, 20));

        CategoryUpdateText.setBackground(new java.awt.Color(249, 241, 240));
        CategoryUpdateText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        CategoryUpdateText.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        CategoryUpdateText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CategoryUpdateTextActionPerformed(evt);
            }
        });
        UpdateItemPanelContent.add(CategoryUpdateText, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 670, 30));

        PriceUpdateLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        PriceUpdateLabel.setForeground(new java.awt.Color(31, 40, 35));
        PriceUpdateLabel.setText("Price");
        UpdateItemPanelContent.add(PriceUpdateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 140, 20));

        PriceUpdateText.setBackground(new java.awt.Color(249, 241, 240));
        PriceUpdateText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        PriceUpdateText.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        PriceUpdateText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PriceUpdateTextActionPerformed(evt);
            }
        });
        UpdateItemPanelContent.add(PriceUpdateText, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 670, 30));

        DescriptionUpdateLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        DescriptionUpdateLabel.setForeground(new java.awt.Color(31, 40, 35));
        DescriptionUpdateLabel.setText("Item Description");
        UpdateItemPanelContent.add(DescriptionUpdateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 140, 20));

        DescriptionUpdateTextField.setBackground(new java.awt.Color(249, 241, 240));
        DescriptionUpdateTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        DescriptionUpdateTextField.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        UpdateItemPanelContent.add(DescriptionUpdateTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 670, 60));

        AddItemConfirmUpdateButton.setBackground(new java.awt.Color(173, 103, 48));
        AddItemConfirmUpdateButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        AddItemConfirmUpdateButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/queue.png"))); // NOI18N
        AddItemConfirmUpdateButton.setText("ADD ITEM");
        AddItemConfirmUpdateButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        AddItemConfirmUpdateButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddItemConfirmUpdateButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        AddItemConfirmUpdateButton.setIconTextGap(10);
        AddItemConfirmUpdateButton.setPreferredSize(new java.awt.Dimension(120, 40));
        AddItemConfirmUpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddItemConfirmUpdateButtonActionPerformed(evt);
            }
        });
        UpdateItemPanelContent.add(AddItemConfirmUpdateButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 120, 40));

        ItemIDUpdateLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ItemIDUpdateLabel.setForeground(new java.awt.Color(31, 40, 35));
        ItemIDUpdateLabel.setText("Item ID");
        UpdateItemPanelContent.add(ItemIDUpdateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 140, 20));

        ItemIDUpdateTextBar.setBackground(new java.awt.Color(249, 241, 240));
        ItemIDUpdateTextBar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ItemIDUpdateTextBar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ItemIDUpdateTextBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ItemIDUpdateTextBarActionPerformed(evt);
            }
        });
        UpdateItemPanelContent.add(ItemIDUpdateTextBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 670, 30));

        ChooseImageUpdateLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ChooseImageUpdateLabel.setForeground(new java.awt.Color(31, 40, 35));
        ChooseImageUpdateLabel.setText("Item Image");
        UpdateItemPanelContent.add(ChooseImageUpdateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 140, 20));

        ChooseImagePathUpdateTextField.setBackground(new java.awt.Color(249, 241, 240));
        ChooseImagePathUpdateTextField.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        ChooseImagePathUpdateTextField.setForeground(new java.awt.Color(31, 40, 35));
        ChooseImagePathUpdateTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChooseImagePathUpdateTextFieldActionPerformed(evt);
            }
        });
        UpdateItemPanelContent.add(ChooseImagePathUpdateTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 360, 20));

        ChooseImageFileUpdateButton.setBackground(new java.awt.Color(249, 241, 240));
        ChooseImageFileUpdateButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ChooseImageFileUpdateButton.setText("Choose File");
        ChooseImageFileUpdateButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ChooseImageFileUpdateButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ChooseImageFileUpdateButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChooseImageFileUpdateButton.setIconTextGap(10);
        ChooseImageFileUpdateButton.setPreferredSize(new java.awt.Dimension(120, 40));
        ChooseImageFileUpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChooseImageFileUpdateButtonActionPerformed(evt);
            }
        });
        UpdateItemPanelContent.add(ChooseImageFileUpdateButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 270, 120, 40));

        UpdateEntryTab.add(UpdateItemPanelContent, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 720, 460));

        footer8.setBackground(new java.awt.Color(121, 63, 26));
        footer8.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        footer8.setPreferredSize(new java.awt.Dimension(990, 50));
        footer8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        UpdateEntryTab.add(footer8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        UpdateItemPanelLabel.setBackground(new java.awt.Color(249, 241, 240));
        UpdateItemPanelLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        UpdateItemPanelLabel.setForeground(new java.awt.Color(255, 255, 255));
        UpdateItemPanelLabel.setText("UPDATE ITEM");
        UpdateItemPanelLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        UpdateEntryTab.add(UpdateItemPanelLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 720, 30));

        MainTabbedPane.addTab("tab8", UpdateEntryTab);

        jPanel1.add(MainTabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        getAccessibleContext().setAccessibleName("INVENTORY");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SalesReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalesReportButtonActionPerformed
        MainTabbedPane.setSelectedIndex(3);
    }//GEN-LAST:event_SalesReportButtonActionPerformed

    private void InventoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InventoryButtonActionPerformed
        MainTabbedPane.setSelectedIndex(1);
    }//GEN-LAST:event_InventoryButtonActionPerformed

    private void CategoriesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CategoriesButtonActionPerformed
        MainTabbedPane.setSelectedIndex(4);
    }//GEN-LAST:event_CategoriesButtonActionPerformed

    private void AddItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddItemButtonActionPerformed
        MainTabbedPane.setSelectedIndex(2);
    }//GEN-LAST:event_AddItemButtonActionPerformed

    private void LogoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutButtonActionPerformed
        KioskFrame KioskFrame = new KioskFrame();
        KioskFrame.setVisible(true);
        KioskFrame.pack();
        KioskFrame.setLocationRelativeTo(null);
        backend.SessionManager.logout();
        this.dispose();
    }//GEN-LAST:event_LogoutButtonActionPerformed

    private void UserManagementButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserManagementButtonActionPerformed
        String currentUser = backend.SessionManager.getCurrentUsername();
    
        if (!backend.UserAuthentication.isAdmin(currentUser)) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Access Denied! Only Administrators can manage users.", 
                "Permission Error", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        MainTabbedPane.setSelectedIndex(5);
    }//GEN-LAST:event_UserManagementButtonActionPerformed

    private void DashboardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardButtonActionPerformed
        MainTabbedPane.setSelectedIndex(0);
    }//GEN-LAST:event_DashboardButtonActionPerformed

    private void InventorySearchBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InventorySearchBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InventorySearchBarActionPerformed

    private void ItemNameTextBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ItemNameTextBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ItemNameTextBarActionPerformed

    private void PriceTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PriceTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PriceTextActionPerformed

    private void AddItemConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddItemConfirmButtonActionPerformed
        try {
            String name = ItemNameTextBar.getText();
            String category = CategoryChoice.getSelectedItem();
            double price = Double.parseDouble(PriceText.getText());
            String description = DescriptionTextField.getText();
            String imagePath = ChooseImagePathTextField.getText();

            Product product = new Product(0, name, description, price, 0, category, imagePath);

            if (ProductManager.addProduct(product)) {
                JOptionPane.showMessageDialog(this, "Product added successfully!");
                clearAddItemForm();
                initializeData(); // Refresh data
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add product!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid price!");
        }
    }//GEN-LAST:event_AddItemConfirmButtonActionPerformed

    private void clearAddItemForm() {
        ItemNameTextBar.setText("");
        PriceText.setText("");
        DescriptionTextField.setText("");
        ChooseImagePathTextField.setText("");
    }
    
    private void ItemIDTextBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ItemIDTextBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ItemIDTextBarActionPerformed

    private void ChooseImageFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChooseImageFileButtonActionPerformed
        JFileChooser filechooser = new JFileChooser();
        
        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
        "Image files (JPG, JPEG, PNG, GIF, BMP)", 
        "jpg", "jpeg", "png", "gif", "bmp"
        );
        
        filechooser.setFileFilter(imageFilter);
        
        int result = filechooser.showOpenDialog(null);
        
         if (filechooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = filechooser.getSelectedFile();
            String filename = f.getAbsolutePath();
            ChooseImagePathTextField.setText(filename);
        }
    }//GEN-LAST:event_ChooseImageFileButtonActionPerformed

    private void ChooseImagePathTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChooseImagePathTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChooseImagePathTextFieldActionPerformed

    private void StockEntryTextBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StockEntryTextBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_StockEntryTextBarActionPerformed

    private void AddItemConfirmButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddItemConfirmButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddItemConfirmButton1ActionPerformed

    private void ItemNameUpdateTextBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ItemNameUpdateTextBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ItemNameUpdateTextBarActionPerformed

    private void CategoryUpdateTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CategoryUpdateTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CategoryUpdateTextActionPerformed

    private void PriceUpdateTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PriceUpdateTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PriceUpdateTextActionPerformed

    private void AddItemConfirmUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddItemConfirmUpdateButtonActionPerformed
            try {
            int id = Integer.parseInt(ItemIDUpdateTextBar.getText());
            String name = ItemNameUpdateTextBar.getText();
            String category = CategoryUpdateText.getText();
            double price = Double.parseDouble(PriceUpdateText.getText());
            String description = DescriptionUpdateTextField.getText();
            String imagePath = ChooseImagePathUpdateTextField.getText();

            Product product = new Product(id, name, description, price, 0, category, imagePath);

            if (ProductManager.updateProduct(product)) {
                JOptionPane.showMessageDialog(this, "Product updated successfully!");
                initializeData(); // Refresh data
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update product!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid data!");
        }
    }//GEN-LAST:event_AddItemConfirmUpdateButtonActionPerformed
    
    private void StockEntryConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int id = Integer.parseInt(IDInformationValueLabel.getText());
            int quantity = Integer.parseInt(StockEntryTextBar.getText());

            if (ProductManager.updateStock(id, quantity)) {
                JOptionPane.showMessageDialog(this, "Stock updated successfully!");
                StockEntryTextBar.setText("");
                initializeData(); // Refresh data
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update stock!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid quantity!");
        }
    }
    
    private void AddUserConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String firstName = FirstNameTextBar.getText();
        String lastName = LastNameTextBar.getText();
        String username = UsernameTextBar.getText();
        String password = new String(PasswordTextBar.getPassword());
        String role = RoleChoice.getSelectedItem().toString();

        if (UserAuthentication.addUser(firstName, lastName, username, password, role)) {
            JOptionPane.showMessageDialog(this, "User added successfully!");
            clearAddUserForm();
            initializeData(); // Refresh data
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add user!");
        }
    }
    
    private void clearAddUserForm() {
        FirstNameTextBar.setText("");
        LastNameTextBar.setText("");
        UsernameTextBar.setText("");
        PasswordTextBar.setText("");
    }
    
    private void DeleteUserButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String username = DeleteUserTextBar.getText();

        if (username.equals(SessionManager.getCurrentUsername())) {
            JOptionPane.showMessageDialog(this, "You cannot delete your own account!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete user: " + username + "?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (UserAuthentication.deleteUser(username)) {
                JOptionPane.showMessageDialog(this, "User deleted successfully!");
                DeleteUserTextBar.setText("");
                initializeData(); // Refresh data
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete user!");
            }
        }
    }
    
    
    
    private void ItemIDUpdateTextBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ItemIDUpdateTextBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ItemIDUpdateTextBarActionPerformed

    private void ChooseImagePathUpdateTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChooseImagePathUpdateTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChooseImagePathUpdateTextFieldActionPerformed

    private void ChooseImageFileUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChooseImageFileUpdateButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChooseImageFileUpdateButtonActionPerformed

    private void CategorySearchBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CategorySearchBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CategorySearchBarActionPerformed

    private void AddCategoryTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddCategoryTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddCategoryTextActionPerformed

    private void AddCategoryConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddCategoryConfirmButtonActionPerformed
        String categoryName = AddCategoryText.getText();
        String currentUser = SessionManager.getCurrentUsername();

        if (CategoryManager.addCategory(categoryName, currentUser)) {
            JOptionPane.showMessageDialog(this, "Category added successfully!");
            AddCategoryText.setText("");
            initializeData(); // Refresh data
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add category! It may already exist.");
        }
    }//GEN-LAST:event_AddCategoryConfirmButtonActionPerformed

    private void AnnualSalesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnnualSalesButtonActionPerformed
        currentSales = SalesReportManager.getAnnualSales();
        currentSalesPeriod = "Annual";
        refreshSalesReportTable();
    }//GEN-LAST:event_AnnualSalesButtonActionPerformed

    private void TodaySalesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TodaySalesButtonActionPerformed
        currentSales = SalesReportManager.getTodaySales();
        currentSalesPeriod = "Today";
        refreshSalesReportTable();
    }//GEN-LAST:event_TodaySalesButtonActionPerformed

    private void ThisWeekSalesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ThisWeekSalesButtonActionPerformed
        currentSales = SalesReportManager.getThisWeekSales();
        currentSalesPeriod = "This Week";
        refreshSalesReportTable();
    }//GEN-LAST:event_ThisWeekSalesButtonActionPerformed

    private void MonthlySalesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MonthlySalesButtonActionPerformed
        currentSales = SalesReportManager.getMonthlySales();
        currentSalesPeriod = "Monthly";
        refreshSalesReportTable();
    }//GEN-LAST:event_MonthlySalesButtonActionPerformed

    private void AddNewUserConfirmButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddNewUserConfirmButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddNewUserConfirmButton2ActionPerformed

    private void UsernameTextBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UsernameTextBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UsernameTextBarActionPerformed

    private void LastNameTextBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LastNameTextBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LastNameTextBarActionPerformed

    private void FirstNameTextBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FirstNameTextBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FirstNameTextBarActionPerformed

    private void PasswordTextBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PasswordTextBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PasswordTextBarActionPerformed

    private void RepeatPasswordTextBar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RepeatPasswordTextBar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RepeatPasswordTextBar1ActionPerformed

    private void DeleteUserTextBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteUserTextBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DeleteUserTextBarActionPerformed
    
    private void initApp() {
        // Initialize database connection
        DatabaseConnection.getConnection();

        // Initialize data
        initializeData();

        // Set up event listeners for sales report buttons
        TodaySalesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TodaySalesButtonActionPerformed(e);
            }
        });

        ThisWeekSalesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ThisWeekSalesButtonActionPerformed(e);
            }
        });

        MonthlySalesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MonthlySalesButtonActionPerformed(e);
            }
        });

        AnnualSalesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnnualSalesButtonActionPerformed(e);
            }
        });

        // Add other button listeners...
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AccountTypeLabel;
    private javax.swing.JButton AddCategoryConfirmButton;
    private javax.swing.JLabel AddCategoryLabel;
    private javax.swing.JTextField AddCategoryText;
    private javax.swing.JButton AddItemButton;
    private javax.swing.JButton AddItemConfirmButton;
    private javax.swing.JButton AddItemConfirmButton1;
    private javax.swing.JButton AddItemConfirmUpdateButton;
    private javax.swing.JPanel AddItemContentPanel;
    private javax.swing.JPanel AddItemPanel;
    private javax.swing.JPanel AddItemTab;
    private javax.swing.JLabel AddItemTitleLabel;
    private javax.swing.JButton AddNewUserConfirmButton2;
    private javax.swing.JLabel AddStockPanelLabel1;
    private javax.swing.JLabel AddStockPanelLabel2;
    private javax.swing.JPanel AddUsersPanel;
    private javax.swing.JPanel AllUsersPanel;
    private javax.swing.JTable AllUsersTable;
    private javax.swing.JButton AnnualSalesButton;
    private javax.swing.JLabel AvailableCategoriesAmountLabel;
    private javax.swing.JPanel AvailableCategoriesBoxPanel;
    private javax.swing.JLabel AvailableCategoriesIcon;
    private javax.swing.JLabel AvailableCategoriesTitleLabel;
    private javax.swing.JButton CategoriesButton;
    private javax.swing.JPanel CategoriesContentPanel;
    private javax.swing.JPanel CategoriesTab;
    private javax.swing.JLabel CategoriesTabTitleLabel;
    private java.awt.Choice CategoryChoice;
    private javax.swing.JLabel CategoryInformationLabel;
    private javax.swing.JLabel CategoryInformationValueLabel;
    private javax.swing.JLabel CategoryLabel;
    private javax.swing.JLabel CategoryListPanelLabel;
    private javax.swing.JTextField CategorySearchBar;
    private javax.swing.JLabel CategorySearchLabel;
    private javax.swing.JTable CategoryTable;
    private javax.swing.JPanel CategoryTablePanel;
    private javax.swing.JLabel CategoryUpdateLabel;
    private javax.swing.JTextField CategoryUpdateText;
    private javax.swing.JButton ChooseImageFileButton;
    private javax.swing.JButton ChooseImageFileUpdateButton;
    private javax.swing.JLabel ChooseImageLabel;
    private java.awt.TextField ChooseImagePathTextField;
    private java.awt.TextField ChooseImagePathUpdateTextField;
    private javax.swing.JLabel ChooseImageUpdateLabel;
    private javax.swing.JButton DashboardButton;
    private javax.swing.JPanel DashboardContentPanel;
    private javax.swing.JPanel DashboardTab;
    private javax.swing.JLabel DashboardTitleLabel;
    private javax.swing.JPanel DashboardUpdatesPanel;
    private javax.swing.JButton DeleteUserTextBar;
    private javax.swing.JLabel DescriptionInformationLabel;
    private javax.swing.JLabel DescriptionInformationValueLabel;
    private javax.swing.JLabel DescriptionLabel;
    private javax.swing.JTextField DescriptionTextField;
    private javax.swing.JLabel DescriptionUpdateLabel;
    private javax.swing.JTextField DescriptionUpdateTextField;
    private javax.swing.JTextField FirstNameTextBar;
    private javax.swing.JLabel FirstnameLabel;
    private javax.swing.JLabel IDInformationLabel;
    private javax.swing.JLabel IDInformationValueLabel;
    private javax.swing.JButton InventoryButton;
    private javax.swing.JPanel InventoryContentPanel;
    private javax.swing.JLabel InventoryListTableLabel;
    private javax.swing.JTextField InventorySearchBar;
    private javax.swing.JLabel InventorySearchLabel;
    private javax.swing.JPanel InventoryTab;
    private javax.swing.JLabel InventoryTabTitleLabel1;
    private javax.swing.JTable InventoryTable;
    private javax.swing.JPanel InventoryTablePanel;
    private javax.swing.JLabel ItemIDLabel;
    private javax.swing.JTextField ItemIDTextBar;
    private javax.swing.JLabel ItemIDUpdateLabel;
    private javax.swing.JTextField ItemIDUpdateTextBar;
    private javax.swing.JLabel ItemInformationPanelLabel;
    private javax.swing.JLabel ItemInformationPanelLabel1;
    private javax.swing.JLabel ItemNameLabel;
    private javax.swing.JTextField ItemNameTextBar;
    private javax.swing.JLabel ItemNameUpdateLabel;
    private javax.swing.JTextField ItemNameUpdateTextBar;
    private javax.swing.JTextField LastNameTextBar;
    private javax.swing.JLabel LastnameLabel;
    private javax.swing.JButton LogoutButton;
    private javax.swing.JTabbedPane MainTabbedPane;
    private javax.swing.JButton MonthlySalesButton;
    private javax.swing.JLabel NameInformationLabel;
    private javax.swing.JLabel NameInformationValueLabel;
    private javax.swing.JLabel PasswordLabel;
    private javax.swing.JPasswordField PasswordTextBar;
    private javax.swing.JLabel PriceInformationLabel;
    private javax.swing.JLabel PriceInformationValueLabel;
    private javax.swing.JLabel PriceLabel;
    private javax.swing.JTextField PriceText;
    private javax.swing.JLabel PriceUpdateLabel;
    private javax.swing.JTextField PriceUpdateText;
    private javax.swing.JLabel QuantityInformationLabel;
    private javax.swing.JLabel QuantityInformationValueLabel;
    private javax.swing.JLabel RepeatPasswordLabel;
    private javax.swing.JPasswordField RepeatPasswordTextBar1;
    private javax.swing.JLabel ReportSalesTableLabel;
    private java.awt.Choice RoleChoice;
    private javax.swing.JLabel SalesAndReportTitleLabel;
    private javax.swing.JButton SalesReportButton;
    private javax.swing.JPanel SalesReportContentPanel;
    private javax.swing.JPanel SalesReportTab;
    private javax.swing.JTable SalesReportTable;
    private javax.swing.JPanel SalesReportTablePanel;
    private javax.swing.JPanel SideBarPanel;
    private javax.swing.JPanel StockEntryContentPanel;
    private javax.swing.JPanel StockEntryTab;
    private javax.swing.JLabel StockEntryTabTitleLabel;
    private javax.swing.JTextField StockEntryTextBar;
    private javax.swing.JLabel StockInLabel1;
    private javax.swing.JPanel TablePanel1;
    private javax.swing.JPanel TablePanel2;
    private javax.swing.JButton ThisWeekSalesButton;
    private javax.swing.JButton TodaySalesButton;
    private javax.swing.JLabel TotalItemAmountLabel;
    private javax.swing.JPanel TotalItemBoxPanel;
    private javax.swing.JLabel TotalItemIcon;
    private javax.swing.JLabel TotalItemTitleLabel;
    private javax.swing.JLabel TotalSalesAmountLabel;
    private javax.swing.JPanel TotalSalesBoxPanel;
    private javax.swing.JLabel TotalSalesIcon;
    private javax.swing.JLabel TotalSalesTableLabel;
    private javax.swing.JLabel TotalSalesTitleLabel;
    private javax.swing.JPanel UpdateEntryTab;
    private javax.swing.JLabel UpdateEntryTabTitleLabel;
    private javax.swing.JPanel UpdateItemPanelContent;
    private javax.swing.JLabel UpdateItemPanelLabel;
    private javax.swing.JButton UserManagementButton;
    private javax.swing.JPanel UserManagementContentPanel;
    private javax.swing.JPanel UserManagementTab;
    private javax.swing.JLabel UserManagementTabTitleLabel;
    private javax.swing.JLabel UsernameLabel;
    private javax.swing.JTextField UsernameTextBar;
    private javax.swing.JLabel UsersAmountLabel;
    private javax.swing.JPanel UsersBoxPanel;
    private javax.swing.JLabel UsersIcon;
    private javax.swing.JLabel UsersTitleLabel;
    private javax.swing.JPanel footer1;
    private javax.swing.JPanel footer2;
    private javax.swing.JPanel footer3;
    private javax.swing.JPanel footer4;
    private javax.swing.JPanel footer5;
    private javax.swing.JPanel footer6;
    private javax.swing.JPanel footer7;
    private javax.swing.JPanel footer8;
    private javax.swing.JPanel header;
    private javax.swing.JPanel header1;
    private javax.swing.JPanel header2;
    private javax.swing.JPanel header3;
    private javax.swing.JPanel header4;
    private javax.swing.JPanel header5;
    private javax.swing.JPanel header6;
    private javax.swing.JPanel header7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    // End of variables declaration//GEN-END:variables
}