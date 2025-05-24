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

public class PurchaseRequisitionManagement {
    private final String FILE_PATH = "purchaserequisition.txt";

    public void savePR(String[] data) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(String.join(",", data));
            bw.newLine();
        }
    }

    public List<String[]> getAllPRs() throws IOException {
        List<String[]> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line.split(","));
            }
        }
        return list;
    }

    public boolean deletePR(String prId) throws IOException {
        File original = new File(FILE_PATH);
        File temp = new File("temp_pr.txt");

        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(original));
             BufferedWriter bw = new BufferedWriter(new FileWriter(temp))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(prId + ",")) {
                    found = true;
                    continue;
                }
                bw.write(line);
                bw.newLine();
            }
        }

        if (found) {
            original.delete();
            temp.renameTo(original);
        } else {
            temp.delete();
        }
        return found;
    }

    public boolean updatePR(String prId, String[] updatedData) throws IOException {
        if (deletePR(prId)) {
            savePR(updatedData);
            return true;
        }
        return false;
    }
}
