package ui;

public class InventoryFrame extends javax.swing.JFrame {

    public InventoryFrame() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuBar1 = new java.awt.MenuBar();
        menu1 = new java.awt.Menu();
        menu2 = new java.awt.Menu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        DashboardPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        InventoryButton = new javax.swing.JButton();
        AddItemButton = new javax.swing.JButton();
        SalesReportButton = new javax.swing.JButton();
        CategoriesButton = new javax.swing.JButton();
        UserManagementButton = new javax.swing.JButton();
        LogoutButton = new javax.swing.JButton();
        DashboardButton = new javax.swing.JButton();
        MainTabbedPane = new javax.swing.JTabbedPane();
        DashboardTab = new javax.swing.JPanel();
        header5 = new javax.swing.JPanel();
        DashboardTitleLabel = new javax.swing.JLabel();
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
        footer1 = new javax.swing.JPanel();
        AddItemTab = new javax.swing.JPanel();
        header2 = new javax.swing.JPanel();
        AddItemTitleLabel = new javax.swing.JLabel();
        footer3 = new javax.swing.JPanel();
        SalesReportTab = new javax.swing.JPanel();
        header3 = new javax.swing.JPanel();
        SalesAndReportTitleLabel = new javax.swing.JLabel();
        footer4 = new javax.swing.JPanel();
        CategoriesTab = new javax.swing.JPanel();
        header1 = new javax.swing.JPanel();
        CategoriesTabTitleLabel = new javax.swing.JLabel();
        footer2 = new javax.swing.JPanel();
        UserManagementTab = new javax.swing.JPanel();
        header4 = new javax.swing.JPanel();
        UserManagementTabTitleLabel = new javax.swing.JLabel();
        footer5 = new javax.swing.JPanel();

        menu1.setLabel("File");
        menuBar1.add(menu1);

        menu2.setLabel("Edit");
        menuBar1.add(menu2);

        jMenuItem1.setText("jMenuItem1");

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

        DashboardPanel.setBackground(new java.awt.Color(0, 0, 0));
        DashboardPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        DashboardPanel.setPreferredSize(new java.awt.Dimension(200, 600));
        DashboardPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/logos/smalldonmacblack.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        jLabel1.setPreferredSize(new java.awt.Dimension(96, 96));
        DashboardPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, -1));

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
        DashboardPanel.add(InventoryButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, -1, -1));

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
        DashboardPanel.add(AddItemButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, -1, -1));

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
        DashboardPanel.add(SalesReportButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, -1, -1));

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
        DashboardPanel.add(CategoriesButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, -1, -1));

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
        DashboardPanel.add(UserManagementButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 430, 200, 60));

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
        DashboardPanel.add(LogoutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 490, -1, -1));

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
        DashboardPanel.add(DashboardButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, -1, -1));

        jPanel1.add(DashboardPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 600));

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

        TotalItemBoxPanel.setBackground(new java.awt.Color(249, 241, 240));
        TotalItemBoxPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        TotalItemBoxPanel.setPreferredSize(new java.awt.Dimension(150, 150));
        TotalItemBoxPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TotalItemIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalItemIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/items.png"))); // NOI18N
        TotalItemBoxPanel.add(TotalItemIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 50, 40));

        TotalItemTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        TotalItemTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalItemTitleLabel.setText("TOTAL ITEMS");
        TotalItemBoxPanel.add(TotalItemTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 150, 30));

        TotalItemAmountLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TotalItemAmountLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalItemAmountLabel.setText("0");
        TotalItemBoxPanel.add(TotalItemAmountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 150, 40));

        DashboardTab.add(TotalItemBoxPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 170, -1));

        TotalSalesBoxPanel.setBackground(new java.awt.Color(249, 241, 240));
        TotalSalesBoxPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        TotalSalesBoxPanel.setPreferredSize(new java.awt.Dimension(150, 150));
        TotalSalesBoxPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TotalSalesIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalSalesIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/peso.png"))); // NOI18N
        TotalSalesBoxPanel.add(TotalSalesIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 50, 40));

        TotalSalesTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        TotalSalesTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalSalesTitleLabel.setText("TOTAL SALES");
        TotalSalesBoxPanel.add(TotalSalesTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 150, 30));

        TotalSalesAmountLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TotalSalesAmountLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalSalesAmountLabel.setText("0");
        TotalSalesBoxPanel.add(TotalSalesAmountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 150, 40));

        DashboardTab.add(TotalSalesBoxPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 70, 170, -1));

        AvailableCategoriesBoxPanel.setBackground(new java.awt.Color(249, 241, 240));
        AvailableCategoriesBoxPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        AvailableCategoriesBoxPanel.setPreferredSize(new java.awt.Dimension(150, 150));
        AvailableCategoriesBoxPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        AvailableCategoriesIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AvailableCategoriesIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/category-alt.png"))); // NOI18N
        AvailableCategoriesBoxPanel.add(AvailableCategoriesIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 50, 40));

        AvailableCategoriesTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        AvailableCategoriesTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AvailableCategoriesTitleLabel.setText("AVAILABLE CATEGORIES");
        AvailableCategoriesBoxPanel.add(AvailableCategoriesTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 150, 30));

        AvailableCategoriesAmountLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        AvailableCategoriesAmountLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AvailableCategoriesAmountLabel.setText("0");
        AvailableCategoriesBoxPanel.add(AvailableCategoriesAmountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 150, 40));

        DashboardTab.add(AvailableCategoriesBoxPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 70, 170, -1));

        UsersBoxPanel.setBackground(new java.awt.Color(249, 241, 240));
        UsersBoxPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        UsersBoxPanel.setPreferredSize(new java.awt.Dimension(150, 150));
        UsersBoxPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        UsersIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UsersIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/users.png"))); // NOI18N
        UsersBoxPanel.add(UsersIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 50, 40));

        UsersTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        UsersTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UsersTitleLabel.setText("USERS");
        UsersBoxPanel.add(UsersTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 150, 30));

        UsersAmountLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        UsersAmountLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UsersAmountLabel.setText("0");
        UsersBoxPanel.add(UsersAmountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 150, 40));

        DashboardTab.add(UsersBoxPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 70, 170, -1));

        footer6.setBackground(new java.awt.Color(121, 63, 26));
        footer6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        footer6.setPreferredSize(new java.awt.Dimension(990, 50));
        footer6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        DashboardTab.add(footer6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        MainTabbedPane.addTab("tab5", DashboardTab);

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

        footer1.setBackground(new java.awt.Color(121, 63, 26));
        footer1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        footer1.setPreferredSize(new java.awt.Dimension(990, 50));
        footer1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        InventoryTab.add(footer1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        MainTabbedPane.addTab("tab6", InventoryTab);

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
        AddItemTitleLabel.setText("ADD ITEM");
        AddItemTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header2.add(AddItemTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 50));

        AddItemTab.add(header2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, -1));

        footer3.setBackground(new java.awt.Color(121, 63, 26));
        footer3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        footer3.setPreferredSize(new java.awt.Dimension(990, 50));
        footer3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        AddItemTab.add(footer3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        MainTabbedPane.addTab("tab5", AddItemTab);

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

        footer4.setBackground(new java.awt.Color(121, 63, 26));
        footer4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        footer4.setPreferredSize(new java.awt.Dimension(990, 50));
        footer4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        SalesReportTab.add(footer4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        MainTabbedPane.addTab("tab1", SalesReportTab);

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

        footer5.setBackground(new java.awt.Color(121, 63, 26));
        footer5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        footer5.setPreferredSize(new java.awt.Dimension(990, 50));
        footer5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        UserManagementTab.add(footer5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 800, 50));

        MainTabbedPane.addTab("tab5", UserManagementTab);

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
        this.dispose();
    }//GEN-LAST:event_LogoutButtonActionPerformed

    private void UserManagementButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserManagementButtonActionPerformed
        MainTabbedPane.setSelectedIndex(5);
    }//GEN-LAST:event_UserManagementButtonActionPerformed

    private void DashboardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardButtonActionPerformed
        MainTabbedPane.setSelectedIndex(0);
    }//GEN-LAST:event_DashboardButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddItemButton;
    private javax.swing.JPanel AddItemTab;
    private javax.swing.JLabel AddItemTitleLabel;
    private javax.swing.JLabel AvailableCategoriesAmountLabel;
    private javax.swing.JPanel AvailableCategoriesBoxPanel;
    private javax.swing.JLabel AvailableCategoriesIcon;
    private javax.swing.JLabel AvailableCategoriesTitleLabel;
    private javax.swing.JButton CategoriesButton;
    private javax.swing.JPanel CategoriesTab;
    private javax.swing.JLabel CategoriesTabTitleLabel;
    private javax.swing.JButton DashboardButton;
    private javax.swing.JPanel DashboardPanel;
    private javax.swing.JPanel DashboardTab;
    private javax.swing.JLabel DashboardTitleLabel;
    private javax.swing.JButton InventoryButton;
    private javax.swing.JPanel InventoryTab;
    private javax.swing.JLabel InventoryTabTitleLabel1;
    private javax.swing.JButton LogoutButton;
    private javax.swing.JTabbedPane MainTabbedPane;
    private javax.swing.JLabel SalesAndReportTitleLabel;
    private javax.swing.JButton SalesReportButton;
    private javax.swing.JPanel SalesReportTab;
    private javax.swing.JLabel TotalItemAmountLabel;
    private javax.swing.JPanel TotalItemBoxPanel;
    private javax.swing.JLabel TotalItemIcon;
    private javax.swing.JLabel TotalItemTitleLabel;
    private javax.swing.JLabel TotalSalesAmountLabel;
    private javax.swing.JPanel TotalSalesBoxPanel;
    private javax.swing.JLabel TotalSalesIcon;
    private javax.swing.JLabel TotalSalesTitleLabel;
    private javax.swing.JButton UserManagementButton;
    private javax.swing.JPanel UserManagementTab;
    private javax.swing.JLabel UserManagementTabTitleLabel;
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
    private javax.swing.JPanel header;
    private javax.swing.JPanel header1;
    private javax.swing.JPanel header2;
    private javax.swing.JPanel header3;
    private javax.swing.JPanel header4;
    private javax.swing.JPanel header5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private java.awt.Menu menu1;
    private java.awt.Menu menu2;
    private java.awt.MenuBar menuBar1;
    // End of variables declaration//GEN-END:variables
}
