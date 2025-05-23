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

public class viewRequisitionManagement {
   private final String FILE = "purchaserequisition.txt";

    // Read all PR records from the file
    public List<String[]> getAllPRs() throws IOException {
        List<String[]> list = new ArrayList<>();
        File file = new File(FILE);

        // Create file if not exist to avoid FileNotFoundException
        if (!file.exists()) {
            file.createNewFile();
            return list;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line.split(","));
            }
        }

        return list;
    }

    public boolean updateStatus(String prID, String newStatus) throws IOException {
    File file = new File("purchaserequisition.txt");
    File tempFile = new File("temp_pr.txt");

    boolean updated = false;

    try (BufferedReader br = new BufferedReader(new FileReader(file));
         BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data[0].equalsIgnoreCase(prID)) {
                data[data.length - 1] = newStatus;
                updated = true;
            }
            bw.write(String.join(",", data));
            bw.newLine();
        }
    }

    if (updated) {
        file.delete();
        tempFile.renameTo(file);
    } else {
        tempFile.delete();
    }

    return updated;
}

}
