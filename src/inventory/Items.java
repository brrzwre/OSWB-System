/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inventory;

/**
 *
 * @author user
 */
public class Items {
    
    private String itemCode;
    private String itemName;
    private String supplierID;
    private int quantity;

    public Items(String itemCode, String itemName, String supplierID, int quantity) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.supplierID = supplierID;
        this.quantity = quantity;
    }

    // Getters
    public String getItemCode() { return itemCode; }
    public String getItemName() { return itemName; }
    public String getSupplierID() { return supplierID; }
    public int getQuantity() { return quantity; }
}