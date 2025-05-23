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

public class ItemManagement {
    private final String filename = "items.txt";

    public void saveItem(String code, String name, String supplier, String quantity) throws IOException {
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(code + "," + name + "," + supplier + "," + quantity + "\n");
        }
    }

    public List<String[]> getAllItems() throws IOException {
        List<String[]> items = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            return items; // return empty list if file doesn't exist
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    items.add(data);
                }
            }
        }
        return items;
    }

    public boolean deleteItemByCode(String code) throws IOException {
        File inputFile = new File(filename);
        File tempFile = new File("temp_" + filename);

        boolean found = false;

        try (
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String[] data = currentLine.split(",");
                if (data.length == 4 && data[0].equalsIgnoreCase(code)) {
                    found = true; // skip this line (delete)
                } else {
                    writer.write(currentLine);
                    writer.newLine();
                }
            }
        }

        if (!found) {
            tempFile.delete();
            return false; // item not found
        }

        if (!inputFile.delete()) {
            throw new IOException("Could not delete original file");
        }

        if (!tempFile.renameTo(inputFile)) {
            throw new IOException("Could not rename temp file");
        }

        return true; // deleted successfully
    }
    
    public boolean updateItem(String code, String newName, String newSupplier, String newQuantity) throws IOException {
    File inputFile = new File(filename);
    File tempFile = new File("temp_" + filename);
    boolean found = false;

    try (
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
    ) {
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            String[] data = currentLine.split(",");
            if (data.length == 4 && data[0].equalsIgnoreCase(code)) {
                // Write updated record
                writer.write(code + "," + newName + "," + newSupplier + "," + newQuantity);
                writer.newLine();
                found = true;
            } else {
                writer.write(currentLine);
                writer.newLine();
            }
        }
    }

    if (!found) {
        tempFile.delete();
        return false; // item not found
    }

    if (!inputFile.delete()) throw new IOException("Could not delete original file");
    if (!tempFile.renameTo(inputFile)) throw new IOException("Could not rename temp file");

    return true;
    }
}
