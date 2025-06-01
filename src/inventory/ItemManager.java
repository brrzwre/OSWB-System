/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inventory;

import java.io.*;
import java.util.*;
/**
 *
 * @author user
 */
public class ItemManager {
    
    private final String FILE_PATH = "items.txt";

    public List<Items> loadItems() {
        List<Items> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String code = parts[0];
                    String name = parts[1];
                    String supplier = parts[2];
                    int qty = Integer.parseInt(parts[3]);
                    items.add(new Items(code, name, supplier, qty));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading items: " + e.getMessage());
        }
        return items;
    }
}