package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

public class PrintPreviewDialog extends JDialog {
    private JTextArea textArea;
    private JButton copyButton;
    private JButton closeButton;
    
    public PrintPreviewDialog(JFrame parent, String title, String content) {
        super(parent, title, true);
        setSize(500, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        
        textArea = new JTextArea(content);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        copyButton = new JButton("Copy to Clipboard");
        copyButton.addActionListener(this::copyAction);
        buttonPanel.add(copyButton);
        
        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        
        textArea.setCaretPosition(0);
    }
    
    private void copyAction(ActionEvent e) {
        StringSelection stringSelection = new StringSelection(textArea.getText());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        JOptionPane.showMessageDialog(this, "Content copied to clipboard!", "Copy", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void showDialog(JFrame parent, String title, String content) {
        PrintPreviewDialog dialog = new PrintPreviewDialog(parent, title, content);
        dialog.setVisible(true);
    }
}