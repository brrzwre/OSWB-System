/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package purchase;

import model.Employee;
import javax.swing.JOptionPane;

public class PurchaseManager extends Employee {

    public PurchaseManager(String id, String name, String username, String password) {
        super(id, name, username, password, "PM");
    }

    @Override
    public void openDashboard() {
        JOptionPane.showMessageDialog(null, "Welcome, " + name + "! This is the Purchase Manager Dashboard.");
        // new PurchaseManagerDashboard(this).setVisible(true);
    }
}

