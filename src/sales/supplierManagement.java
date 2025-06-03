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

public class supplierManagement {
    private final String filename = "supplier.txt";

    public void saveSupplier(String id, String name, String email, String contact) throws IOException {
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(id + "," + name + "," + email + "," + contact + "\n");
        }
    }

    public List<String[]> getAllSuppliers() throws IOException {
        List<String[]> suppliers = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            return suppliers; 
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    suppliers.add(data);
                }
            }
        }
        return suppliers;
    }

    public boolean updateSupplier(String id, String newName, String newEmail, String newContact) throws IOException {
        File inputFile = new File(filename);
        File tempFile = new File("temp_" + filename);
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] data = currentLine.split(",");
                if (data.length == 4 && data[0].equalsIgnoreCase(id)) {
                    writer.write(id + "," + newName + "," + newEmail + "," + newContact);
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
            return false; 
        }

        if (!inputFile.delete()) throw new IOException("Could not delete original file");
        if (!tempFile.renameTo(inputFile)) throw new IOException("Could not rename temp file");
        return true;
    }

    public boolean deleteSupplier(String id) throws IOException {
        File inputFile = new File(filename);
        File tempFile = new File("temp_" + filename);
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] data = currentLine.split(",");
                if (data.length == 4 && data[0].equalsIgnoreCase(id)) {
                    found = true; 
                } else {
                    writer.write(currentLine);
                    writer.newLine();
                }
            }
        }

        if (!found) {
            tempFile.delete();
            return false; 
        }

        if (!inputFile.delete()) throw new IOException("Could not delete original file");
        if (!tempFile.renameTo(inputFile)) throw new IOException("Could not rename temp file");
        return true;
    }
}
