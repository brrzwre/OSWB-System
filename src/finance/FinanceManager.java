/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finance;

import model.Employee;
import javax.swing.JOptionPane;

public class FinanceManager extends Employee {

    public FinanceManager(String id, String name, String username, String password) {
        super(id, name, username, password, "FM");
    }

    @Override
    public void openDashboard() {
        JOptionPane.showMessageDialog(null, "Welcome, " + name + "! This is the Finance Manager Dashboard.");
    }
}

