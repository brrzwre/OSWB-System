/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sales;

/**
 *
 * @author aaish
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewPurchaseOrderManagement {
    private static final String FILE_PATH = "purchaseorder.txt";

    // Read all purchase orders as list of string arrays
    public List<String[]> getAllPurchaseOrders() throws IOException {
        List<String[]> purchaseOrders = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                // Skip header if present
                if (isFirstLine) {
                    isFirstLine = false;
                    if (line.toLowerCase().contains("po id")) continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 9) {  // At least 9 columns expected
                    purchaseOrders.add(parts);
                }
            }
        }
        return purchaseOrders;
    }

    // Filter purchase orders by keyword in any column (case-insensitive)
    public List<String[]> searchPurchaseOrders(String keyword) throws IOException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllPurchaseOrders();
        }
        keyword = keyword.toLowerCase();
        List<String[]> filtered = new ArrayList<>();
        for (String[] po : getAllPurchaseOrders()) {
            for (String field : po) {
                if (field.toLowerCase().contains(keyword)) {
                    filtered.add(po);
                    break;
                }
            }
        }
        return filtered;
    }

    // Filter purchase orders by status column (assumed last column at index 8)
    public List<String[]> filterByStatus(String status) throws IOException {
        if (status == null || status.equals("-")) {
            return getAllPurchaseOrders();
        }
        List<String[]> filtered = new ArrayList<>();
        for (String[] po : getAllPurchaseOrders()) {
            if (po.length > 8 && po[8].equalsIgnoreCase(status)) {
                filtered.add(po);
            }
        }
        return filtered;
    }

    // Combined filter: first filter by status, then by keyword search
    public List<String[]> filterPurchaseOrders(String status, String keyword) throws IOException {
        List<String[]> list = filterByStatus(status);
        if (keyword == null || keyword.trim().isEmpty()) {
            return list;
        }
        keyword = keyword.toLowerCase();
        List<String[]> filtered = new ArrayList<>();
        for (String[] po : list) {
            for (String field : po) {
                if (field.toLowerCase().contains(keyword)) {
                    filtered.add(po);
                    break;
                }
            }
        }
        return filtered;
    }

    // Alternative combined method with concise logic
    public List<String[]> searchAndFilterPurchaseOrders(String keyword, String status) throws IOException {
        List<String[]> filteredOrders = new ArrayList<>();
        List<String[]> allOrders = getAllPurchaseOrders();

        for (String[] order : allOrders) {
            String rowData = String.join(" ", order).toLowerCase();
            boolean keywordMatches = keyword == null || keyword.isEmpty() || rowData.contains(keyword.toLowerCase());
            boolean statusMatches = status == null || status.equals("-") || (order.length > 8 && order[8].equalsIgnoreCase(status));

            if (keywordMatches && statusMatches) {
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }
}
