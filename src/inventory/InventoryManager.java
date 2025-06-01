/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package inventory;
import model.Employee;
import javax.swing.JOptionPane;

public class InventoryManager extends Employee {

    public InventoryManager(String id, String name, String username, String password) {
        super(id, name, username, password, "IM");
    }

    @Override
    public void openDashboard() {
        JOptionPane.showMessageDialog(null, "Welcome, " + name + "! This is the Inventory Manager Dashboard.");
        // new InventoryManagerDashboard(this).setVisible(true);
    }
}

