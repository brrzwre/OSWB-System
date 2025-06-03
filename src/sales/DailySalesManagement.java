/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sales;

/**
 *
 * @author aaish
 */

import java.io.*;
import java.util.*;

public class DailySalesManagement {
    private final String FILE_PATH = "salesData.txt";
   
    public void saveSale(String salesId, String itemCode, int qtySold, double pricePerUnit,
                         double totalPrice, String dateOfSale, String salesManagerId) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String line = String.join(",",
                salesId, itemCode, String.valueOf(qtySold), 
                String.format("%.2f", pricePerUnit),
                String.format("%.2f", totalPrice),
                dateOfSale, salesManagerId);
            bw.write(line);
            bw.newLine();
        }
    }
    
    public List<String[]> getAllSales() throws IOException {
        List<String[]> sales = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return sales;  

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                sales.add(line.split(","));
            }
        }
        return sales;
    }
    
    public boolean deleteSale(String salesId) throws IOException {
        File inputFile = new File(FILE_PATH);
        File tempFile = new File("tempSalesData.txt");

        boolean deleted = false;

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equalsIgnoreCase(salesId)) {
                    deleted = true;  
                    continue;
                }
                bw.write(line);
                bw.newLine();
            }
        }

        if (deleted) {
            if (!inputFile.delete()) throw new IOException("Could not delete original file");
            if (!tempFile.renameTo(inputFile)) throw new IOException("Could not rename temp file");
        } else {
            tempFile.delete();
        }
        return deleted;
    }
    
    public boolean updateSale(String salesId, String itemCode, int qtySold, double pricePerUnit,
                              double totalPrice, String dateOfSale, String salesManagerId) throws IOException {
        boolean deleted = deleteSale(salesId);
        if (deleted) {
            saveSale(salesId, itemCode, qtySold, pricePerUnit, totalPrice, dateOfSale, salesManagerId);
            return true;
        }
        return false;
    }
}
