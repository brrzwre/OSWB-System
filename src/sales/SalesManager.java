/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sales;
import model.Employee;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

public class SalesManager extends Employee {
    
    private ItemManagement itemManagement;
    private DailySalesManagement dailySalesManager;
    private ViewPurchaseOrderManagement purchaseOrderManagement;

    public SalesManager(String id, String name, String username, String password) {
        super(id, name, username, password, "SM");
        this.itemManagement = new ItemManagement();
        this.dailySalesManager = new DailySalesManagement();
        this.purchaseOrderManagement = new ViewPurchaseOrderManagement();
    }

    @Override
    public void openDashboard() {
        JOptionPane.showMessageDialog(null, "Welcome, " + name + "! This is the Sales Manager Dashboard.");
        // new SalesManagerDashboard(this).setVisible(true); // Replace with actual frame later
    }
    
    public void addItem(String code, String name, String supplier, String quantity) throws IOException {
        itemManagement.saveItem(code, name, supplier, quantity);
    }

    public List<String[]> getAllItems() throws IOException {
        return itemManagement.getAllItems();
    }

    public boolean deleteItem(String code) throws IOException {
        return itemManagement.deleteItemByCode(code);
    }

    public boolean updateItem(String code, String newName, String newSupplier, String newQuantity) throws IOException {
        return itemManagement.updateItem(code, newName, newSupplier, newQuantity);
    }
    
    public void saveSale(String salesId, String itemCode, int qtySold, double pricePerUnit,
                         double totalPrice, String dateOfSale, String salesManagerId) throws IOException {
        dailySalesManager.saveSale(salesId, itemCode, qtySold, pricePerUnit, totalPrice, dateOfSale, salesManagerId);
    }

    public List<String[]> getAllSales() throws IOException {
        return dailySalesManager.getAllSales();
    }

    public boolean deleteSale(String salesId) throws IOException {
        return dailySalesManager.deleteSale(salesId);
    }

    public boolean updateSale(String salesId, String itemCode, int qtySold, double pricePerUnit,
                              double totalPrice, String dateOfSale, String salesManagerId) throws IOException {
        return dailySalesManager.updateSale(salesId, itemCode, qtySold, pricePerUnit, totalPrice, dateOfSale, salesManagerId);
    }
    
    public List<String[]> getAllPurchaseOrders() throws IOException {
        ViewPurchaseOrderManagement pom = new ViewPurchaseOrderManagement();
        return purchaseOrderManagement.getAllPurchaseOrders();
    }

    // Add method to search and filter purchase orders by keyword and status
    public List<String[]> searchPurchaseOrders(String keyword, String status) throws IOException {
        return purchaseOrderManagement.searchAndFilterPurchaseOrders(keyword, status);
    }
}