/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inventory;

/**
 *
 * @author user
 */
public class PurchaseOrder {
        private String poID, prID, itemCode, itemName, supplierID, date, salesManagerID, status;
    private int quantity;

    public PurchaseOrder(String poID, String prID, String itemCode, String itemName, int quantity,
                         String supplierID, String date, String salesManagerID, String status) {
        this.poID = poID;
        this.prID = prID;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.quantity = quantity;
        this.supplierID = supplierID;
        this.date = date;
        this.salesManagerID = salesManagerID;
        this.status = status;
    }

    // Getters only (or add setters if needed)
    public String getPoID() { return poID; }
    public String getPrID() { return prID; }
    public String getItemCode() { return itemCode; }
    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public String getSupplierID() { return supplierID; }
    public String getDate() { return date; }
    public String getSalesManagerID() { return salesManagerID; }
    public String getStatus() { return status; }
}