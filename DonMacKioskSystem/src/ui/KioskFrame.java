package ui;

public class KioskFrame extends javax.swing.JFrame {

    public KioskFrame() {
        initComponents();
        checkUserAccess();
    }
   
    private void checkUserAccess() {
        // This would normally get the current logged-in user from a session

        // In a real application, you would get the current user from a session manager
        String currentUser = "user"; // This should come from session management

        // Hide Inventory button for non-staff users
        if (!backend.UserAuthentication.isAdmin(currentUser) && 
            !backend.UserAuthentication.isCashier(currentUser) && 
            !backend.UserAuthentication.isManager(currentUser)) {

            ToInventoryButton.setVisible(true);
        }
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
        ToInventoryButton = new javax.swing.JButton();
        MainTabbedPane = new javax.swing.JTabbedPane();
        MenuPanelTab = new javax.swing.JPanel();
        header = new javax.swing.JPanel();
        MainTabTitleLabel1 = new javax.swing.JLabel();
        MenuContentScrollPane = new javax.swing.JScrollPane();
        MenuContentPanel = new javax.swing.JPanel();
        menu_category_name1 = new javax.swing.JLabel();
        menu_category_scroll_pane1 = new javax.swing.JScrollPane();
        menu_category_box = new javax.swing.JPanel();
        product_box1 = new javax.swing.JPanel();
        product_image1 = new javax.swing.JLabel();
        product_name1 = new javax.swing.JLabel();
        product_price1 = new javax.swing.JLabel();
        product_item_amount1 = new javax.swing.JSpinner();
        product_add1 = new javax.swing.JButton();
        product_box2 = new javax.swing.JPanel();
        product_image2 = new javax.swing.JLabel();
        product_name2 = new javax.swing.JLabel();
        product_price2 = new javax.swing.JLabel();
        product_item_amount2 = new javax.swing.JSpinner();
        special_product_add2 = new javax.swing.JButton();
        product_box4 = new javax.swing.JPanel();
        product_image4 = new javax.swing.JLabel();
        product_name4 = new javax.swing.JLabel();
        product_price4 = new javax.swing.JLabel();
        product_item_amount4 = new javax.swing.JSpinner();
        special_product_add4 = new javax.swing.JButton();
        product_box3 = new javax.swing.JPanel();
        product_image3 = new javax.swing.JLabel();
        product_name3 = new javax.swing.JLabel();
        product_price3 = new javax.swing.JLabel();
        product_item_amount3 = new javax.swing.JSpinner();
        special_product_add3 = new javax.swing.JButton();
        product_box8 = new javax.swing.JPanel();
        product_image8 = new javax.swing.JLabel();
        product_name8 = new javax.swing.JLabel();
        product_price8 = new javax.swing.JLabel();
        product_item_amount8 = new javax.swing.JSpinner();
        special_product_add10 = new javax.swing.JButton();
        product_box6 = new javax.swing.JPanel();
        product_image6 = new javax.swing.JLabel();
        product_name6 = new javax.swing.JLabel();
        product_price6 = new javax.swing.JLabel();
        product_item_amount6 = new javax.swing.JSpinner();
        product_add6 = new javax.swing.JButton();
        product_box7 = new javax.swing.JPanel();
        product_image7 = new javax.swing.JLabel();
        product_name7 = new javax.swing.JLabel();
        product_price7 = new javax.swing.JLabel();
        product_item_amount7 = new javax.swing.JSpinner();
        product_add7 = new javax.swing.JButton();
        product_box5 = new javax.swing.JPanel();
        product_image5 = new javax.swing.JLabel();
        product_name5 = new javax.swing.JLabel();
        product_price5 = new javax.swing.JLabel();
        product_item_amount5 = new javax.swing.JSpinner();
        product_add5 = new javax.swing.JButton();
        footer = new javax.swing.JPanel();
        SpecialsPanelTab = new javax.swing.JPanel();
        header1 = new javax.swing.JPanel();
        SpecialsTabTitleLabel = new javax.swing.JLabel();
        SpecialsContentScrollPane = new javax.swing.JScrollPane();
        SpecialsContentPanel = new javax.swing.JPanel();
        specials_category_scroll_pane1 = new javax.swing.JScrollPane();
        specials_category_box = new javax.swing.JPanel();
        special_product_box1 = new javax.swing.JPanel();
        special_product_image1 = new javax.swing.JLabel();
        special_product_name1 = new javax.swing.JLabel();
        special_product_price1 = new javax.swing.JLabel();
        special_item_amount1 = new javax.swing.JSpinner();
        special_product_add1 = new javax.swing.JButton();
        special_product_box2 = new javax.swing.JPanel();
        special_product_image2 = new javax.swing.JLabel();
        special_product_name2 = new javax.swing.JLabel();
        special_product_price2 = new javax.swing.JLabel();
        special_item_amount2 = new javax.swing.JSpinner();
        special_product_add7 = new javax.swing.JButton();
        special_product_box3 = new javax.swing.JPanel();
        special_product_image3 = new javax.swing.JLabel();
        special_product_name3 = new javax.swing.JLabel();
        special_product_price3 = new javax.swing.JLabel();
        special_item_amount3 = new javax.swing.JSpinner();
        special_product_add8 = new javax.swing.JButton();
        special_product_box4 = new javax.swing.JPanel();
        special_product_image4 = new javax.swing.JLabel();
        special_product_name4 = new javax.swing.JLabel();
        special_product_price4 = new javax.swing.JLabel();
        special_item_amount4 = new javax.swing.JSpinner();
        special_product_add9 = new javax.swing.JButton();
        special_product_box5 = new javax.swing.JPanel();
        special_product_image5 = new javax.swing.JLabel();
        special_product_name5 = new javax.swing.JLabel();
        special_product_price5 = new javax.swing.JLabel();
        special_product_add5 = new javax.swing.JButton();
        special_item_amount5 = new javax.swing.JSpinner();
        specials_category_name1 = new javax.swing.JLabel();
        footer1 = new javax.swing.JPanel();
        CartPanelTab = new javax.swing.JPanel();
        header2 = new javax.swing.JPanel();
        CartTabTitleLabel = new javax.swing.JLabel();
        CartContentPanel = new javax.swing.JPanel();
        CartScrollPane = new javax.swing.JScrollPane();
        CartItemsPanel = new javax.swing.JPanel();
        cart_item_box1 = new javax.swing.JPanel();
        product_name9 = new javax.swing.JLabel();
        cart_item_amount1 = new javax.swing.JSpinner();
        cart_item_delete1 = new javax.swing.JButton();
        cart_item_image1 = new javax.swing.JLabel();
        cart_item_price1 = new javax.swing.JLabel();
        cart_item_box2 = new javax.swing.JPanel();
        product_name10 = new javax.swing.JLabel();
        cart_item_amount2 = new javax.swing.JSpinner();
        cart_item_image2 = new javax.swing.JLabel();
        cart_item_delete5 = new javax.swing.JButton();
        cart_item_price2 = new javax.swing.JLabel();
        cart_item_box4 = new javax.swing.JPanel();
        product_name12 = new javax.swing.JLabel();
        cart_item_amount4 = new javax.swing.JSpinner();
        cart_item_image4 = new javax.swing.JLabel();
        cart_item_delete7 = new javax.swing.JButton();
        cart_item_price4 = new javax.swing.JLabel();
        cart_item_box3 = new javax.swing.JPanel();
        product_name11 = new javax.swing.JLabel();
        cart_item_amount3 = new javax.swing.JSpinner();
        cart_item_image3 = new javax.swing.JLabel();
        cart_item_delete6 = new javax.swing.JButton();
        cart_item_price3 = new javax.swing.JLabel();
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
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setPreferredSize(new java.awt.Dimension(1000, 600));
        setSize(new java.awt.Dimension(1000, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        SpecialsButton.setText("SPECIALS");
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

        ToInventoryButton.setBackground(new java.awt.Color(249, 241, 240));
        ToInventoryButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/shipping.png"))); // NOI18N
        ToInventoryButton.setText("INVENTORY");
        ToInventoryButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ToInventoryButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ToInventoryButton.setIconTextGap(10);
        ToInventoryButton.setPreferredSize(new java.awt.Dimension(200, 75));
        ToInventoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToInventoryButtonActionPerformed(evt);
            }
        });
        SideBarPanel.add(ToInventoryButton);

        jPanel2.add(SideBarPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 600));

        MainTabbedPane.setBackground(new java.awt.Color(201, 177, 158));
        MainTabbedPane.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        MainTabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
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

        MenuContentScrollPane.setPreferredSize(new java.awt.Dimension(800, 600));

        MenuContentPanel.setBackground(new java.awt.Color(201, 177, 158));
        MenuContentPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        MenuContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        MenuContentPanel.setPreferredSize(new java.awt.Dimension(780, 600));
        MenuContentPanel.setRequestFocusEnabled(false);
        MenuContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menu_category_name1.setBackground(new java.awt.Color(255, 255, 255));
        menu_category_name1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        menu_category_name1.setText("DRINKS");
        menu_category_name1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        menu_category_name1.setPreferredSize(new java.awt.Dimension(740, 30));
        MenuContentPanel.add(menu_category_name1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        menu_category_scroll_pane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(249, 241, 240), 2, true));
        menu_category_scroll_pane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        menu_category_scroll_pane1.setPreferredSize(new java.awt.Dimension(760, 430));
        menu_category_scroll_pane1.setRequestFocusEnabled(false);

        menu_category_box.setBackground(new java.awt.Color(31, 40, 35));
        menu_category_box.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        menu_category_box.setMinimumSize(new java.awt.Dimension(730, 450));
        menu_category_box.setPreferredSize(new java.awt.Dimension(1760, 400));
        menu_category_box.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        product_box1.setBackground(new java.awt.Color(249, 241, 240));
        product_box1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_box1.setPreferredSize(new java.awt.Dimension(200, 380));
        product_box1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        product_image1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/iced_caramel.png"))); // NOI18N
        product_image1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 3));
        product_image1.setPreferredSize(new java.awt.Dimension(150, 150));
        product_box1.add(product_image1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        product_name1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        product_name1.setForeground(new java.awt.Color(31, 40, 35));
        product_name1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_name1.setText("Iced Caramel Macchiato");
        product_name1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        product_box1.add(product_name1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 200, 20));

        product_price1.setBackground(new java.awt.Color(201, 177, 158));
        product_price1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        product_price1.setForeground(new java.awt.Color(31, 40, 35));
        product_price1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_price1.setText("₱39.00");
        product_price1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 2));
        product_price1.setOpaque(true);
        product_price1.setPreferredSize(new java.awt.Dimension(80, 50));
        product_box1.add(product_price1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        product_item_amount1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        product_item_amount1.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        product_item_amount1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_item_amount1.setPreferredSize(new java.awt.Dimension(80, 80));
        product_box1.add(product_item_amount1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 110, 60));

        product_add1.setBackground(new java.awt.Color(249, 241, 240));
        product_add1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/add.png"))); // NOI18N
        product_add1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_add1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        product_add1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_add.png"))); // NOI18N
        product_add1.setRolloverEnabled(false);
        product_box1.add(product_add1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 310, 60, 60));

        menu_category_box.add(product_box1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        product_box2.setBackground(new java.awt.Color(249, 241, 240));
        product_box2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_box2.setPreferredSize(new java.awt.Dimension(200, 380));
        product_box2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        product_image2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/spanish_latte.png"))); // NOI18N
        product_image2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 3));
        product_image2.setPreferredSize(new java.awt.Dimension(150, 150));
        product_box2.add(product_image2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        product_name2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        product_name2.setForeground(new java.awt.Color(31, 40, 35));
        product_name2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_name2.setText("Spanishe Latte");
        product_box2.add(product_name2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 200, 20));

        product_price2.setBackground(new java.awt.Color(201, 177, 158));
        product_price2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        product_price2.setForeground(new java.awt.Color(31, 40, 35));
        product_price2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_price2.setText("₱39.00");
        product_price2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 2));
        product_price2.setOpaque(true);
        product_price2.setPreferredSize(new java.awt.Dimension(80, 50));
        product_box2.add(product_price2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        product_item_amount2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        product_item_amount2.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        product_item_amount2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_item_amount2.setPreferredSize(new java.awt.Dimension(80, 80));
        product_box2.add(product_item_amount2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 110, 60));

        special_product_add2.setBackground(new java.awt.Color(249, 241, 240));
        special_product_add2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/add.png"))); // NOI18N
        special_product_add2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        special_product_add2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        special_product_add2.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_add.png"))); // NOI18N
        special_product_add2.setRolloverEnabled(false);
        product_box2.add(special_product_add2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 310, 60, 60));

        menu_category_box.add(product_box2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, -1, -1));

        product_box4.setBackground(new java.awt.Color(249, 241, 240));
        product_box4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_box4.setPreferredSize(new java.awt.Dimension(200, 380));
        product_box4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        product_image4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/donya_berry.png"))); // NOI18N
        product_image4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 3));
        product_image4.setPreferredSize(new java.awt.Dimension(150, 150));
        product_box4.add(product_image4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        product_name4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        product_name4.setForeground(new java.awt.Color(31, 40, 35));
        product_name4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_name4.setText("Donya Berry");
        product_box4.add(product_name4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 200, 20));

        product_price4.setBackground(new java.awt.Color(201, 177, 158));
        product_price4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        product_price4.setForeground(new java.awt.Color(31, 40, 35));
        product_price4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_price4.setText("₱39.00");
        product_price4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 2));
        product_price4.setOpaque(true);
        product_price4.setPreferredSize(new java.awt.Dimension(80, 50));
        product_box4.add(product_price4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        product_item_amount4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        product_item_amount4.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        product_item_amount4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_item_amount4.setPreferredSize(new java.awt.Dimension(80, 80));
        product_box4.add(product_item_amount4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 110, 60));

        special_product_add4.setBackground(new java.awt.Color(249, 241, 240));
        special_product_add4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/add.png"))); // NOI18N
        special_product_add4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        special_product_add4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        special_product_add4.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_add.png"))); // NOI18N
        special_product_add4.setRolloverEnabled(false);
        product_box4.add(special_product_add4, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 310, 60, 60));

        menu_category_box.add(product_box4, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 10, -1, -1));

        product_box3.setBackground(new java.awt.Color(249, 241, 240));
        product_box3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_box3.setPreferredSize(new java.awt.Dimension(200, 380));
        product_box3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        product_image3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/black_forest.png"))); // NOI18N
        product_image3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 3));
        product_image3.setPreferredSize(new java.awt.Dimension(150, 150));
        product_box3.add(product_image3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        product_name3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        product_name3.setForeground(new java.awt.Color(31, 40, 35));
        product_name3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_name3.setText("Black Forest");
        product_box3.add(product_name3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 200, 20));

        product_price3.setBackground(new java.awt.Color(201, 177, 158));
        product_price3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        product_price3.setForeground(new java.awt.Color(31, 40, 35));
        product_price3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_price3.setText("₱39.00");
        product_price3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 2));
        product_price3.setOpaque(true);
        product_price3.setPreferredSize(new java.awt.Dimension(80, 50));
        product_box3.add(product_price3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        product_item_amount3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        product_item_amount3.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        product_item_amount3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_item_amount3.setPreferredSize(new java.awt.Dimension(80, 80));
        product_box3.add(product_item_amount3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 110, 60));

        special_product_add3.setBackground(new java.awt.Color(249, 241, 240));
        special_product_add3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/add.png"))); // NOI18N
        special_product_add3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        special_product_add3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        special_product_add3.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_add.png"))); // NOI18N
        special_product_add3.setRolloverEnabled(false);
        product_box3.add(special_product_add3, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 310, 60, 60));

        menu_category_box.add(product_box3, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, -1, -1));

        product_box8.setBackground(new java.awt.Color(249, 241, 240));
        product_box8.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_box8.setPreferredSize(new java.awt.Dimension(200, 380));
        product_box8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        product_image8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/don_darko.png"))); // NOI18N
        product_image8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 3));
        product_image8.setPreferredSize(new java.awt.Dimension(150, 150));
        product_box8.add(product_image8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        product_name8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        product_name8.setForeground(new java.awt.Color(31, 40, 35));
        product_name8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_name8.setText("Don Darko");
        product_box8.add(product_name8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 200, 20));

        product_price8.setBackground(new java.awt.Color(201, 177, 158));
        product_price8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        product_price8.setForeground(new java.awt.Color(31, 40, 35));
        product_price8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_price8.setText("₱39.00");
        product_price8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 2));
        product_price8.setOpaque(true);
        product_price8.setPreferredSize(new java.awt.Dimension(80, 50));
        product_box8.add(product_price8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        product_item_amount8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        product_item_amount8.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        product_item_amount8.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_item_amount8.setPreferredSize(new java.awt.Dimension(80, 80));
        product_box8.add(product_item_amount8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 110, 60));

        special_product_add10.setBackground(new java.awt.Color(249, 241, 240));
        special_product_add10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/add.png"))); // NOI18N
        special_product_add10.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        special_product_add10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        special_product_add10.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_add.png"))); // NOI18N
        special_product_add10.setRolloverEnabled(false);
        product_box8.add(special_product_add10, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 310, 60, 60));

        menu_category_box.add(product_box8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1550, 10, -1, -1));

        product_box6.setBackground(new java.awt.Color(249, 241, 240));
        product_box6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_box6.setPreferredSize(new java.awt.Dimension(200, 380));
        product_box6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        product_image6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/matcha_berry.png"))); // NOI18N
        product_image6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 3));
        product_image6.setPreferredSize(new java.awt.Dimension(150, 150));
        product_box6.add(product_image6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        product_name6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        product_name6.setForeground(new java.awt.Color(31, 40, 35));
        product_name6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_name6.setText("Matcha Berry");
        product_box6.add(product_name6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 200, 20));

        product_price6.setBackground(new java.awt.Color(201, 177, 158));
        product_price6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        product_price6.setForeground(new java.awt.Color(31, 40, 35));
        product_price6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_price6.setText("₱39.00");
        product_price6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 2));
        product_price6.setOpaque(true);
        product_price6.setPreferredSize(new java.awt.Dimension(80, 50));
        product_box6.add(product_price6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        product_item_amount6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        product_item_amount6.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        product_item_amount6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_item_amount6.setPreferredSize(new java.awt.Dimension(80, 80));
        product_box6.add(product_item_amount6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 110, 60));

        product_add6.setBackground(new java.awt.Color(249, 241, 240));
        product_add6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/add.png"))); // NOI18N
        product_add6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_add6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        product_add6.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_add.png"))); // NOI18N
        product_add6.setRolloverEnabled(false);
        product_box6.add(product_add6, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 310, 60, 60));

        menu_category_box.add(product_box6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 10, -1, -1));

        product_box7.setBackground(new java.awt.Color(249, 241, 240));
        product_box7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_box7.setPreferredSize(new java.awt.Dimension(200, 380));
        product_box7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        product_image7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/ore_coffee.png"))); // NOI18N
        product_image7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 3));
        product_image7.setPreferredSize(new java.awt.Dimension(150, 150));
        product_box7.add(product_image7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        product_name7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        product_name7.setForeground(new java.awt.Color(31, 40, 35));
        product_name7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_name7.setText("Oreo Coffee");
        product_box7.add(product_name7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 200, 20));

        product_price7.setBackground(new java.awt.Color(201, 177, 158));
        product_price7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        product_price7.setForeground(new java.awt.Color(31, 40, 35));
        product_price7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_price7.setText("₱39.00");
        product_price7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 2));
        product_price7.setOpaque(true);
        product_price7.setPreferredSize(new java.awt.Dimension(80, 50));
        product_box7.add(product_price7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        product_item_amount7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        product_item_amount7.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        product_item_amount7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_item_amount7.setPreferredSize(new java.awt.Dimension(80, 80));
        product_box7.add(product_item_amount7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 110, 60));

        product_add7.setBackground(new java.awt.Color(249, 241, 240));
        product_add7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/add.png"))); // NOI18N
        product_add7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_add7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        product_add7.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_add.png"))); // NOI18N
        product_add7.setRolloverEnabled(false);
        product_box7.add(product_add7, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 310, 60, 60));

        menu_category_box.add(product_box7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1330, 10, -1, -1));

        product_box5.setBackground(new java.awt.Color(249, 241, 240));
        product_box5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_box5.setPreferredSize(new java.awt.Dimension(200, 380));
        product_box5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        product_image5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/don_matchatos.png"))); // NOI18N
        product_image5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 3));
        product_image5.setPreferredSize(new java.awt.Dimension(150, 150));
        product_box5.add(product_image5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        product_name5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        product_name5.setForeground(new java.awt.Color(31, 40, 35));
        product_name5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_name5.setText("Don Matchatos");
        product_box5.add(product_name5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 200, 20));

        product_price5.setBackground(new java.awt.Color(201, 177, 158));
        product_price5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        product_price5.setForeground(new java.awt.Color(31, 40, 35));
        product_price5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_price5.setText("₱39.00");
        product_price5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 2));
        product_price5.setOpaque(true);
        product_price5.setPreferredSize(new java.awt.Dimension(80, 50));
        product_box5.add(product_price5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        product_item_amount5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        product_item_amount5.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        product_item_amount5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_item_amount5.setPreferredSize(new java.awt.Dimension(80, 80));
        product_box5.add(product_item_amount5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 110, 60));

        product_add5.setBackground(new java.awt.Color(249, 241, 240));
        product_add5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/add.png"))); // NOI18N
        product_add5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        product_add5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        product_add5.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_add.png"))); // NOI18N
        product_add5.setRolloverEnabled(false);
        product_box5.add(product_add5, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 310, 60, 60));

        menu_category_box.add(product_box5, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 10, -1, -1));

        menu_category_scroll_pane1.setViewportView(menu_category_box);

        MenuContentPanel.add(menu_category_scroll_pane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 760, -1));

        MenuContentScrollPane.setViewportView(MenuContentPanel);

        MenuPanelTab.add(MenuContentScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 800, 500));

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

        SpecialsContentScrollPane.setPreferredSize(new java.awt.Dimension(800, 600));

        SpecialsContentPanel.setBackground(new java.awt.Color(201, 177, 158));
        SpecialsContentPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        SpecialsContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        SpecialsContentPanel.setPreferredSize(new java.awt.Dimension(780, 600));
        SpecialsContentPanel.setRequestFocusEnabled(false);
        SpecialsContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        specials_category_scroll_pane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(249, 241, 240), 2, true));
        specials_category_scroll_pane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        specials_category_scroll_pane1.setPreferredSize(new java.awt.Dimension(760, 430));
        specials_category_scroll_pane1.setRequestFocusEnabled(false);

        specials_category_box.setBackground(new java.awt.Color(31, 40, 35));
        specials_category_box.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        specials_category_box.setPreferredSize(new java.awt.Dimension(1760, 400));
        specials_category_box.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        special_product_box1.setBackground(new java.awt.Color(249, 241, 240));
        special_product_box1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        special_product_box1.setPreferredSize(new java.awt.Dimension(200, 380));
        special_product_box1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        special_product_image1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/pure_ube.png"))); // NOI18N
        special_product_image1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 3));
        special_product_image1.setPreferredSize(new java.awt.Dimension(150, 150));
        special_product_box1.add(special_product_image1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        special_product_name1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        special_product_name1.setForeground(new java.awt.Color(31, 40, 35));
        special_product_name1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        special_product_name1.setText("Pure Ube");
        special_product_name1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        special_product_box1.add(special_product_name1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 150, 20));

        special_product_price1.setBackground(new java.awt.Color(201, 177, 158));
        special_product_price1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        special_product_price1.setForeground(new java.awt.Color(31, 40, 35));
        special_product_price1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        special_product_price1.setText("₱39.00");
        special_product_price1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 2));
        special_product_price1.setOpaque(true);
        special_product_price1.setPreferredSize(new java.awt.Dimension(80, 50));
        special_product_box1.add(special_product_price1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        special_item_amount1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        special_item_amount1.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        special_item_amount1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        special_item_amount1.setPreferredSize(new java.awt.Dimension(80, 80));
        special_product_box1.add(special_item_amount1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 110, 60));

        special_product_add1.setBackground(new java.awt.Color(249, 241, 240));
        special_product_add1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/add.png"))); // NOI18N
        special_product_add1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        special_product_add1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        special_product_add1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_add.png"))); // NOI18N
        special_product_add1.setRolloverEnabled(false);
        special_product_box1.add(special_product_add1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 310, 60, 60));

        specials_category_box.add(special_product_box1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        special_product_box2.setBackground(new java.awt.Color(249, 241, 240));
        special_product_box2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        special_product_box2.setPreferredSize(new java.awt.Dimension(200, 380));
        special_product_box2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        special_product_image2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/hot_caramel.png"))); // NOI18N
        special_product_image2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 3));
        special_product_image2.setPreferredSize(new java.awt.Dimension(150, 150));
        special_product_box2.add(special_product_image2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 150, -1));

        special_product_name2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        special_product_name2.setForeground(new java.awt.Color(31, 40, 35));
        special_product_name2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        special_product_name2.setText("Hot Caramel");
        special_product_box2.add(special_product_name2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 150, 20));

        special_product_price2.setBackground(new java.awt.Color(201, 177, 158));
        special_product_price2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        special_product_price2.setForeground(new java.awt.Color(31, 40, 35));
        special_product_price2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        special_product_price2.setText("₱39.00");
        special_product_price2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 2));
        special_product_price2.setOpaque(true);
        special_product_price2.setPreferredSize(new java.awt.Dimension(80, 50));
        special_product_box2.add(special_product_price2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        special_item_amount2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        special_item_amount2.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        special_item_amount2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        special_item_amount2.setPreferredSize(new java.awt.Dimension(80, 80));
        special_product_box2.add(special_item_amount2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 110, 60));

        special_product_add7.setBackground(new java.awt.Color(249, 241, 240));
        special_product_add7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/add.png"))); // NOI18N
        special_product_add7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        special_product_add7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        special_product_add7.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_add.png"))); // NOI18N
        special_product_add7.setRolloverEnabled(false);
        special_product_box2.add(special_product_add7, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 310, 60, 60));

        specials_category_box.add(special_product_box2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, -1, -1));

        special_product_box3.setBackground(new java.awt.Color(249, 241, 240));
        special_product_box3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        special_product_box3.setPreferredSize(new java.awt.Dimension(200, 380));
        special_product_box3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        special_product_image3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/hot_darko.png"))); // NOI18N
        special_product_image3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 3));
        special_product_image3.setPreferredSize(new java.awt.Dimension(150, 150));
        special_product_box3.add(special_product_image3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        special_product_name3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        special_product_name3.setForeground(new java.awt.Color(31, 40, 35));
        special_product_name3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        special_product_name3.setText("Hot Darko");
        special_product_box3.add(special_product_name3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 150, 20));

        special_product_price3.setBackground(new java.awt.Color(201, 177, 158));
        special_product_price3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        special_product_price3.setForeground(new java.awt.Color(31, 40, 35));
        special_product_price3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        special_product_price3.setText("₱39.00");
        special_product_price3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 2));
        special_product_price3.setOpaque(true);
        special_product_price3.setPreferredSize(new java.awt.Dimension(80, 50));
        special_product_box3.add(special_product_price3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        special_item_amount3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        special_item_amount3.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        special_item_amount3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        special_item_amount3.setPreferredSize(new java.awt.Dimension(80, 80));
        special_product_box3.add(special_item_amount3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 110, 60));

        special_product_add8.setBackground(new java.awt.Color(249, 241, 240));
        special_product_add8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/add.png"))); // NOI18N
        special_product_add8.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        special_product_add8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        special_product_add8.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_add.png"))); // NOI18N
        special_product_add8.setRolloverEnabled(false);
        special_product_box3.add(special_product_add8, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 310, 60, 60));

        specials_category_box.add(special_product_box3, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, -1, -1));

        special_product_box4.setBackground(new java.awt.Color(249, 241, 240));
        special_product_box4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        special_product_box4.setPreferredSize(new java.awt.Dimension(200, 380));
        special_product_box4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        special_product_image4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/don_barako.png"))); // NOI18N
        special_product_image4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 3));
        special_product_image4.setPreferredSize(new java.awt.Dimension(150, 150));
        special_product_box4.add(special_product_image4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        special_product_name4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        special_product_name4.setForeground(new java.awt.Color(31, 40, 35));
        special_product_name4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        special_product_name4.setText("Don Barako");
        special_product_box4.add(special_product_name4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 150, 20));

        special_product_price4.setBackground(new java.awt.Color(201, 177, 158));
        special_product_price4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        special_product_price4.setForeground(new java.awt.Color(31, 40, 35));
        special_product_price4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        special_product_price4.setText("₱39.00");
        special_product_price4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 2));
        special_product_price4.setOpaque(true);
        special_product_price4.setPreferredSize(new java.awt.Dimension(80, 50));
        special_product_box4.add(special_product_price4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        special_item_amount4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        special_item_amount4.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        special_item_amount4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        special_item_amount4.setPreferredSize(new java.awt.Dimension(80, 80));
        special_product_box4.add(special_item_amount4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 110, 60));

        special_product_add9.setBackground(new java.awt.Color(249, 241, 240));
        special_product_add9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/add.png"))); // NOI18N
        special_product_add9.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        special_product_add9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        special_product_add9.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_add.png"))); // NOI18N
        special_product_add9.setRolloverEnabled(false);
        special_product_box4.add(special_product_add9, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 310, 60, 60));

        specials_category_box.add(special_product_box4, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 10, -1, -1));

        special_product_box5.setBackground(new java.awt.Color(249, 241, 240));
        special_product_box5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        special_product_box5.setPreferredSize(new java.awt.Dimension(200, 380));
        special_product_box5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        special_product_image5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/pure_pistachio.png"))); // NOI18N
        special_product_image5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 3));
        special_product_image5.setPreferredSize(new java.awt.Dimension(150, 150));
        special_product_box5.add(special_product_image5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        special_product_name5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        special_product_name5.setForeground(new java.awt.Color(31, 40, 35));
        special_product_name5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        special_product_name5.setText("Don Pistachio");
        special_product_box5.add(special_product_name5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 150, 20));

        special_product_price5.setBackground(new java.awt.Color(201, 177, 158));
        special_product_price5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        special_product_price5.setForeground(new java.awt.Color(31, 40, 35));
        special_product_price5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        special_product_price5.setText("₱39.00");
        special_product_price5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 2));
        special_product_price5.setOpaque(true);
        special_product_price5.setPreferredSize(new java.awt.Dimension(80, 50));
        special_product_box5.add(special_product_price5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        special_product_add5.setBackground(new java.awt.Color(249, 241, 240));
        special_product_add5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/add.png"))); // NOI18N
        special_product_add5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        special_product_add5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        special_product_add5.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_add.png"))); // NOI18N
        special_product_add5.setRolloverEnabled(false);
        special_product_box5.add(special_product_add5, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 310, 60, 60));

        special_item_amount5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        special_item_amount5.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        special_item_amount5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        special_item_amount5.setPreferredSize(new java.awt.Dimension(80, 80));
        special_product_box5.add(special_item_amount5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 110, 60));

        specials_category_box.add(special_product_box5, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 10, -1, -1));

        specials_category_scroll_pane1.setViewportView(specials_category_box);

        SpecialsContentPanel.add(specials_category_scroll_pane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, 430));

        specials_category_name1.setBackground(new java.awt.Color(255, 255, 255));
        specials_category_name1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        specials_category_name1.setText("DRINKS");
        specials_category_name1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        specials_category_name1.setPreferredSize(new java.awt.Dimension(740, 30));
        SpecialsContentPanel.add(specials_category_name1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        SpecialsContentScrollPane.setViewportView(SpecialsContentPanel);

        SpecialsPanelTab.add(SpecialsContentScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 800, 500));

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

        CartTabTitleLabel.setBackground(new java.awt.Color(249, 241, 240));
        CartTabTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CartTabTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        CartTabTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CartTabTitleLabel.setText("CART");
        CartTabTitleLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        header2.add(CartTabTitleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 50));

        CartPanelTab.add(header2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, -1));

        CartContentPanel.setBackground(new java.awt.Color(201, 177, 158));
        CartContentPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        CartContentPanel.setForeground(new java.awt.Color(31, 40, 35));
        CartContentPanel.setRequestFocusEnabled(false);
        CartContentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CartScrollPane.setPreferredSize(new java.awt.Dimension(730, 410));

        CartItemsPanel.setBackground(new java.awt.Color(31, 40, 35));
        CartItemsPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        CartItemsPanel.setForeground(new java.awt.Color(31, 40, 35));
        CartItemsPanel.setPreferredSize(new java.awt.Dimension(700, 730));
        CartItemsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cart_item_box1.setBackground(new java.awt.Color(249, 241, 240));
        cart_item_box1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cart_item_box1.setPreferredSize(new java.awt.Dimension(650, 170));
        cart_item_box1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        product_name9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        product_name9.setForeground(new java.awt.Color(31, 40, 35));
        product_name9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_name9.setText("Iced Caramel Macchiato");
        product_name9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        product_name9.setPreferredSize(new java.awt.Dimension(220, 40));
        cart_item_box1.add(product_name9, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, -1, -1));

        cart_item_amount1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cart_item_amount1.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        cart_item_amount1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cart_item_amount1.setPreferredSize(new java.awt.Dimension(80, 80));
        cart_item_box1.add(cart_item_amount1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 50, -1, -1));

        cart_item_delete1.setBackground(new java.awt.Color(249, 241, 240));
        cart_item_delete1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/delete.png"))); // NOI18N
        cart_item_delete1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cart_item_delete1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cart_item_delete1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_delete.png"))); // NOI18N
        cart_item_delete1.setRolloverEnabled(false);
        cart_item_delete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cart_item_delete1ActionPerformed(evt);
            }
        });
        cart_item_box1.add(cart_item_delete1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 70, -1, -1));

        cart_item_image1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/iced_caramel.png"))); // NOI18N
        cart_item_image1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 3));
        cart_item_image1.setPreferredSize(new java.awt.Dimension(150, 150));
        cart_item_box1.add(cart_item_image1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        cart_item_price1.setBackground(new java.awt.Color(255, 255, 255));
        cart_item_price1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cart_item_price1.setForeground(new java.awt.Color(31, 40, 35));
        cart_item_price1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cart_item_price1.setText("₱39.00");
        cart_item_price1.setPreferredSize(new java.awt.Dimension(80, 50));
        cart_item_box1.add(cart_item_price1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 60, 80, 60));

        CartItemsPanel.add(cart_item_box1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 680, -1));

        cart_item_box2.setBackground(new java.awt.Color(249, 241, 240));
        cart_item_box2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cart_item_box2.setPreferredSize(new java.awt.Dimension(650, 170));
        cart_item_box2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        product_name10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        product_name10.setForeground(new java.awt.Color(31, 40, 35));
        product_name10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_name10.setText("Spanishe Latte");
        product_name10.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cart_item_box2.add(product_name10, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, 220, 40));

        cart_item_amount2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cart_item_amount2.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        cart_item_amount2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cart_item_amount2.setPreferredSize(new java.awt.Dimension(80, 80));
        cart_item_box2.add(cart_item_amount2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, -1, -1));

        cart_item_image2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/spanish_latte.png"))); // NOI18N
        cart_item_image2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 3));
        cart_item_image2.setPreferredSize(new java.awt.Dimension(150, 150));
        cart_item_box2.add(cart_item_image2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        cart_item_delete5.setBackground(new java.awt.Color(249, 241, 240));
        cart_item_delete5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/delete.png"))); // NOI18N
        cart_item_delete5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cart_item_delete5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cart_item_delete5.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_delete.png"))); // NOI18N
        cart_item_delete5.setRolloverEnabled(false);
        cart_item_delete5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cart_item_delete5ActionPerformed(evt);
            }
        });
        cart_item_box2.add(cart_item_delete5, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 70, -1, -1));

        cart_item_price2.setBackground(new java.awt.Color(255, 255, 255));
        cart_item_price2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cart_item_price2.setForeground(new java.awt.Color(31, 40, 35));
        cart_item_price2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cart_item_price2.setText("₱39.00");
        cart_item_price2.setPreferredSize(new java.awt.Dimension(80, 50));
        cart_item_box2.add(cart_item_price2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 60, 80, 60));

        CartItemsPanel.add(cart_item_box2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 680, -1));

        cart_item_box4.setBackground(new java.awt.Color(249, 241, 240));
        cart_item_box4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cart_item_box4.setPreferredSize(new java.awt.Dimension(650, 170));
        cart_item_box4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        product_name12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        product_name12.setForeground(new java.awt.Color(31, 40, 35));
        product_name12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_name12.setText("Pure Ube");
        product_name12.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cart_item_box4.add(product_name12, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, 220, 40));

        cart_item_amount4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cart_item_amount4.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        cart_item_amount4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cart_item_amount4.setPreferredSize(new java.awt.Dimension(80, 80));
        cart_item_box4.add(cart_item_amount4, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, -1, -1));

        cart_item_image4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/pure_ube.png"))); // NOI18N
        cart_item_image4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 3));
        cart_item_image4.setPreferredSize(new java.awt.Dimension(150, 150));
        cart_item_box4.add(cart_item_image4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        cart_item_delete7.setBackground(new java.awt.Color(249, 241, 240));
        cart_item_delete7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/delete.png"))); // NOI18N
        cart_item_delete7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cart_item_delete7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cart_item_delete7.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_delete.png"))); // NOI18N
        cart_item_delete7.setRolloverEnabled(false);
        cart_item_delete7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cart_item_delete7ActionPerformed(evt);
            }
        });
        cart_item_box4.add(cart_item_delete7, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 70, -1, -1));

        cart_item_price4.setBackground(new java.awt.Color(255, 255, 255));
        cart_item_price4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cart_item_price4.setForeground(new java.awt.Color(31, 40, 35));
        cart_item_price4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cart_item_price4.setText("₱39.00");
        cart_item_price4.setPreferredSize(new java.awt.Dimension(80, 50));
        cart_item_box4.add(cart_item_price4, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 60, 80, 60));

        CartItemsPanel.add(cart_item_box4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 550, 680, -1));

        cart_item_box3.setBackground(new java.awt.Color(249, 241, 240));
        cart_item_box3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cart_item_box3.setPreferredSize(new java.awt.Dimension(650, 170));
        cart_item_box3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        product_name11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        product_name11.setForeground(new java.awt.Color(31, 40, 35));
        product_name11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        product_name11.setText("Don Matchatos");
        product_name11.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cart_item_box3.add(product_name11, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, 220, 40));

        cart_item_amount3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cart_item_amount3.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        cart_item_amount3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cart_item_amount3.setPreferredSize(new java.awt.Dimension(80, 80));
        cart_item_box3.add(cart_item_amount3, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, -1, -1));

        cart_item_image3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/product_images/don_matchatos.png"))); // NOI18N
        cart_item_image3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 40, 35), 3));
        cart_item_image3.setPreferredSize(new java.awt.Dimension(150, 150));
        cart_item_box3.add(cart_item_image3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        cart_item_delete6.setBackground(new java.awt.Color(249, 241, 240));
        cart_item_delete6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/delete.png"))); // NOI18N
        cart_item_delete6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cart_item_delete6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cart_item_delete6.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/Images/icons/pressed_delete.png"))); // NOI18N
        cart_item_delete6.setRolloverEnabled(false);
        cart_item_delete6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cart_item_delete6ActionPerformed(evt);
            }
        });
        cart_item_box3.add(cart_item_delete6, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 70, -1, -1));

        cart_item_price3.setBackground(new java.awt.Color(255, 255, 255));
        cart_item_price3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cart_item_price3.setForeground(new java.awt.Color(31, 40, 35));
        cart_item_price3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cart_item_price3.setText("₱39.00");
        cart_item_price3.setPreferredSize(new java.awt.Dimension(80, 50));
        cart_item_box3.add(cart_item_price3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 60, 80, 60));

        CartItemsPanel.add(cart_item_box3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 680, -1));

        CartScrollPane.setViewportView(CartItemsPanel);

        CartContentPanel.add(CartScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 730, 410));

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
        TotalPriceNumerLabel.setText("₱136.00");
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

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 600));

        getAccessibleContext().setAccessibleName("KIOSK");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void GetHelpButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GetHelpButton1ActionPerformed
        MainTabbedPane.setSelectedIndex(3);
    }//GEN-LAST:event_GetHelpButton1ActionPerformed

    private void CartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CartButtonActionPerformed
        MainTabbedPane.setSelectedIndex(2);
    }//GEN-LAST:event_CartButtonActionPerformed

    private void MenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuButtonActionPerformed
        MainTabbedPane.setSelectedIndex(0);
    }//GEN-LAST:event_MenuButtonActionPerformed

    private void SpecialsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SpecialsButtonActionPerformed
        MainTabbedPane.setSelectedIndex(1);
    }//GEN-LAST:event_SpecialsButtonActionPerformed
    // TODO add your handling code here:


    private void cart_item_delete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cart_item_delete1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cart_item_delete1ActionPerformed

    private void cart_item_delete5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cart_item_delete5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cart_item_delete5ActionPerformed

    private void cart_item_delete6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cart_item_delete6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cart_item_delete6ActionPerformed

    private void cart_item_delete7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cart_item_delete7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cart_item_delete7ActionPerformed

    private void CheckOutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckOutButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CheckOutButtonActionPerformed

    private void ToInventoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToInventoryButtonActionPerformed
        LoginFrame LoginFrame = new LoginFrame();
        LoginFrame.setVisible(true);
        LoginFrame.pack();
        LoginFrame.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_ToInventoryButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CartButton;
    private javax.swing.JPanel CartContentPanel;
    private javax.swing.JPanel CartContentPanel1;
    private javax.swing.JPanel CartItemsPanel;
    private javax.swing.JPanel CartPanelTab;
    private javax.swing.JScrollPane CartScrollPane;
    private javax.swing.JLabel CartTabTitleLabel;
    private javax.swing.JButton CheckOutButton;
    private javax.swing.JButton GetHelpButton1;
    private javax.swing.JPanel GetHelpPanelTab;
    private javax.swing.JLabel GetHelpTitleLabel;
    private javax.swing.JLabel MainTabTitleLabel1;
    private javax.swing.JTabbedPane MainTabbedPane;
    private javax.swing.JButton MenuButton;
    private javax.swing.JPanel MenuContentPanel;
    private javax.swing.JScrollPane MenuContentScrollPane;
    private javax.swing.JPanel MenuPanelTab;
    private javax.swing.JPanel SideBarPanel;
    private javax.swing.JButton SpecialsButton;
    private javax.swing.JPanel SpecialsContentPanel;
    private javax.swing.JScrollPane SpecialsContentScrollPane;
    private javax.swing.JPanel SpecialsPanelTab;
    private javax.swing.JLabel SpecialsTabTitleLabel;
    private javax.swing.JButton ToInventoryButton;
    private javax.swing.JLabel TotalPriceLabel;
    private javax.swing.JLabel TotalPriceNumerLabel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JSpinner cart_item_amount1;
    private javax.swing.JSpinner cart_item_amount2;
    private javax.swing.JSpinner cart_item_amount3;
    private javax.swing.JSpinner cart_item_amount4;
    private javax.swing.JPanel cart_item_box1;
    private javax.swing.JPanel cart_item_box2;
    private javax.swing.JPanel cart_item_box3;
    private javax.swing.JPanel cart_item_box4;
    private javax.swing.JButton cart_item_delete1;
    private javax.swing.JButton cart_item_delete5;
    private javax.swing.JButton cart_item_delete6;
    private javax.swing.JButton cart_item_delete7;
    private javax.swing.JLabel cart_item_image1;
    private javax.swing.JLabel cart_item_image2;
    private javax.swing.JLabel cart_item_image3;
    private javax.swing.JLabel cart_item_image4;
    private javax.swing.JLabel cart_item_price1;
    private javax.swing.JLabel cart_item_price2;
    private javax.swing.JLabel cart_item_price3;
    private javax.swing.JLabel cart_item_price4;
    private javax.swing.JPanel footer;
    private javax.swing.JPanel footer1;
    private javax.swing.JPanel footer2;
    private javax.swing.JPanel footer3;
    private javax.swing.JPanel header;
    private javax.swing.JPanel header1;
    private javax.swing.JPanel header2;
    private javax.swing.JPanel header3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel menu_category_box;
    private javax.swing.JLabel menu_category_name1;
    private javax.swing.JScrollPane menu_category_scroll_pane1;
    private javax.swing.JButton product_add1;
    private javax.swing.JButton product_add5;
    private javax.swing.JButton product_add6;
    private javax.swing.JButton product_add7;
    private javax.swing.JPanel product_box1;
    private javax.swing.JPanel product_box2;
    private javax.swing.JPanel product_box3;
    private javax.swing.JPanel product_box4;
    private javax.swing.JPanel product_box5;
    private javax.swing.JPanel product_box6;
    private javax.swing.JPanel product_box7;
    private javax.swing.JPanel product_box8;
    private javax.swing.JLabel product_image1;
    private javax.swing.JLabel product_image2;
    private javax.swing.JLabel product_image3;
    private javax.swing.JLabel product_image4;
    private javax.swing.JLabel product_image5;
    private javax.swing.JLabel product_image6;
    private javax.swing.JLabel product_image7;
    private javax.swing.JLabel product_image8;
    private javax.swing.JSpinner product_item_amount1;
    private javax.swing.JSpinner product_item_amount2;
    private javax.swing.JSpinner product_item_amount3;
    private javax.swing.JSpinner product_item_amount4;
    private javax.swing.JSpinner product_item_amount5;
    private javax.swing.JSpinner product_item_amount6;
    private javax.swing.JSpinner product_item_amount7;
    private javax.swing.JSpinner product_item_amount8;
    private javax.swing.JLabel product_name1;
    private javax.swing.JLabel product_name10;
    private javax.swing.JLabel product_name11;
    private javax.swing.JLabel product_name12;
    private javax.swing.JLabel product_name2;
    private javax.swing.JLabel product_name3;
    private javax.swing.JLabel product_name4;
    private javax.swing.JLabel product_name5;
    private javax.swing.JLabel product_name6;
    private javax.swing.JLabel product_name7;
    private javax.swing.JLabel product_name8;
    private javax.swing.JLabel product_name9;
    private javax.swing.JLabel product_price1;
    private javax.swing.JLabel product_price2;
    private javax.swing.JLabel product_price3;
    private javax.swing.JLabel product_price4;
    private javax.swing.JLabel product_price5;
    private javax.swing.JLabel product_price6;
    private javax.swing.JLabel product_price7;
    private javax.swing.JLabel product_price8;
    private javax.swing.JSpinner special_item_amount1;
    private javax.swing.JSpinner special_item_amount2;
    private javax.swing.JSpinner special_item_amount3;
    private javax.swing.JSpinner special_item_amount4;
    private javax.swing.JSpinner special_item_amount5;
    private javax.swing.JButton special_product_add1;
    private javax.swing.JButton special_product_add10;
    private javax.swing.JButton special_product_add2;
    private javax.swing.JButton special_product_add3;
    private javax.swing.JButton special_product_add4;
    private javax.swing.JButton special_product_add5;
    private javax.swing.JButton special_product_add7;
    private javax.swing.JButton special_product_add8;
    private javax.swing.JButton special_product_add9;
    private javax.swing.JPanel special_product_box1;
    private javax.swing.JPanel special_product_box2;
    private javax.swing.JPanel special_product_box3;
    private javax.swing.JPanel special_product_box4;
    private javax.swing.JPanel special_product_box5;
    private javax.swing.JLabel special_product_image1;
    private javax.swing.JLabel special_product_image2;
    private javax.swing.JLabel special_product_image3;
    private javax.swing.JLabel special_product_image4;
    private javax.swing.JLabel special_product_image5;
    private javax.swing.JLabel special_product_name1;
    private javax.swing.JLabel special_product_name2;
    private javax.swing.JLabel special_product_name3;
    private javax.swing.JLabel special_product_name4;
    private javax.swing.JLabel special_product_name5;
    private javax.swing.JLabel special_product_price1;
    private javax.swing.JLabel special_product_price2;
    private javax.swing.JLabel special_product_price3;
    private javax.swing.JLabel special_product_price4;
    private javax.swing.JLabel special_product_price5;
    private javax.swing.JPanel specials_category_box;
    private javax.swing.JLabel specials_category_name1;
    private javax.swing.JScrollPane specials_category_scroll_pane1;
    // End of variables declaration//GEN-END:variables
}
