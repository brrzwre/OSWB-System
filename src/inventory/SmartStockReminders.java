/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inventory;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
/**
 *
 * @author user
 */
public class SmartStockReminders extends JPanel{
    
    private JTextArea alertArea;
    
        public SmartStockReminders() {
        // Panel setup
        setBackground(new Color(255, 255, 153));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));


        // Alert area setup
        alertArea = new JTextArea();
        alertArea.setEditable(false);
        alertArea.setBackground(new Color(255, 255, 204));
        alertArea.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        alertArea.setLineWrap(true);
        alertArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(alertArea);
        add(scrollPane, BorderLayout.CENTER);

        // Load stock data
        loadLowStockItems();
    }

    private void loadLowStockItems() {
        ArrayList<String> lowStockItems = getLowStockItems("items.txt", 200);
        alertArea.setText("");

    if (lowStockItems.isEmpty()) {
       alertArea.setText("✅ All items are sufficiently stocked.");
   } else {
       for (String item : lowStockItems) {
           alertArea.append("⚠️ LOW STOCK: " + item.toUpperCase() + "\n");
       }
    }
    }

    private ArrayList<String> getLowStockItems(String filePath, int threshold) {
        ArrayList<String> lowStock = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String itemName = parts[1];
                    int quantity = Integer.parseInt(parts[3].trim());
                    if (quantity < threshold) {
                        lowStock.add(itemName + " (Qty: " + quantity + ")");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lowStock;
    }
}