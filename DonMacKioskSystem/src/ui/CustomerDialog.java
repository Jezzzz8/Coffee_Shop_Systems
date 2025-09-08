package ui;

import backend.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CustomerDialog extends JDialog {
    private Customer selectedCustomer;
    private boolean confirmed = false;
    
    private JList<Customer> customerList;
    private DefaultListModel<Customer> listModel;
    private JTextField searchField;
    
    public CustomerDialog(JFrame parent, boolean modal) {
        super(parent, modal);
        setTitle("Select Customer");
        setSize(500, 400);
        setLocationRelativeTo(parent);
        initComponents();
        loadCustomers();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchCustomers());
        
        searchPanel.add(new JLabel("Search:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);
        
        // Customer list
        listModel = new DefaultListModel<>();
        customerList = new JList<>(listModel);
        customerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(customerList);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton selectBtn = new JButton("Select");
        JButton newBtn = new JButton("New Customer");
        JButton cancelBtn = new JButton("Cancel");
        
        selectBtn.addActionListener(e -> {
            selectedCustomer = customerList.getSelectedValue();
            if (selectedCustomer != null) {
                confirmed = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a customer", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        newBtn.addActionListener(e -> createNewCustomer());
        cancelBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(selectBtn);
        buttonPanel.add(newBtn);
        buttonPanel.add(cancelBtn);
        
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadCustomers() {
        listModel.clear();
        List<Customer> customers = CustomerManager.getAllCustomers();
        for (Customer customer : customers) {
            listModel.addElement(customer);
        }
    }
    
    private void searchCustomers() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadCustomers();
            return;
        }
        
        listModel.clear();
        List<Customer> results = CustomerManager.searchCustomers(searchTerm);
        for (Customer customer : results) {
            listModel.addElement(customer);
        }
    }
    
    private void createNewCustomer() {
        // Show a dialog to create a new customer
        // This would typically have fields for name, contact, email, etc.
        // For simplicity, I'll just show a message
        JOptionPane.showMessageDialog(this, "New customer functionality would be implemented here", "New Customer", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
}