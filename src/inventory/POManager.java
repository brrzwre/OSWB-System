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
public class POManager {

    private final String FILE_PATH = "purchaseorder.txt";

    public List<PurchaseOrder> loadAllPOs() {
        List<PurchaseOrder> poList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 9) {
                    PurchaseOrder po = new PurchaseOrder(
                        parts[0], parts[1], parts[2], parts[3],
                        Integer.parseInt(parts[4]), parts[5], parts[6], parts[7], parts[8]
                    );
                    poList.add(po);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading POs: " + e.getMessage());
        }
        return poList;
    }

    public List<PurchaseOrder> getApprovedPOs() {
        List<PurchaseOrder> all = loadAllPOs();
        List<PurchaseOrder> approved = new ArrayList<>();
        for (PurchaseOrder po : all) {
            if ("Approved".equalsIgnoreCase(po.getStatus())) {
                approved.add(po);
            }
        }
        return approved;
    }
}
